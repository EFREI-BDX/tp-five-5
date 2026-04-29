use super::{
    ApplicationError, ApplicationResult, ApplicationService, DomainEventPublisher, MatchRepository,
    NoOpPlayerDataPort, NoOpPublisher, PlayerDataPort,
};
use crate::domain::DomainEvent;
use async_trait::async_trait;

pub struct MatchSummaryService<R: MatchRepository> {
    repository: R,
    publisher: Box<dyn DomainEventPublisher>,
    player_data_port: Box<dyn PlayerDataPort>,
}

impl<R: MatchRepository> MatchSummaryService<R> {
    /// Creates the service with no-op publisher and no-op player data port.
    pub fn new(repository: R) -> Self {
        Self::with_publisher(repository, NoOpPublisher)
    }

    /// Creates the service with a real domain event publisher and no-op player data port.
    pub fn with_publisher(repository: R, publisher: impl DomainEventPublisher + 'static) -> Self {
        Self {
            repository,
            publisher: Box::new(publisher),
            player_data_port: Box::new(NoOpPlayerDataPort),
        }
    }

    /// Creates the service with all ports wired explicitly.
    pub fn with_all(
        repository: R,
        publisher: impl DomainEventPublisher + 'static,
        player_data_port: impl PlayerDataPort + 'static,
    ) -> Self {
        Self {
            repository,
            publisher: Box::new(publisher),
            player_data_port: Box::new(player_data_port),
        }
    }
}

#[async_trait]
impl<R: MatchRepository> ApplicationService for MatchSummaryService<R> {
    async fn handle_event(&self, event: DomainEvent) -> ApplicationResult<()> {
        let match_id = event.match_id().to_string();
        let mut aggregate = self.repository.load(&match_id).await?;
        aggregate
            .handle_event(event.clone())
            .map_err(|error| ApplicationError::domain(error.to_string()))?;
        self.repository.append(event.clone()).await?;

        if matches!(event, DomainEvent::MatchFinished(_)) {
            let player_data = aggregate.to_player_data_events();
            self.player_data_port
                .publish(&player_data)
                .await
                .map_err(|e| ApplicationError::repository(format!("player data port error: {}", e)))?;
        }

        self.publisher
            .publish(&event)
            .await
            .map_err(|e| ApplicationError::repository(format!("publisher error: {}", e)))
    }
}

#[cfg(test)]
mod tests {
    use super::{ApplicationService, MatchSummaryService};
    use crate::application::{
        ApplicationError, ApplicationResult, DomainEventPublisher, MatchRepository,
    };
    use crate::domain::{
        DomainEvent, GoalScored, MatchAggregate, MatchFinished, MatchStarted, MatchTime, Player,
        PlayerId, Score, Team, TeamId,
    };
    use async_trait::async_trait;
    use std::collections::HashMap;
    use std::sync::Mutex;

    #[derive(Default)]
    struct FakeMatchRepository {
        events: Mutex<HashMap<String, Vec<DomainEvent>>>,
    }

    #[async_trait]
    impl MatchRepository for FakeMatchRepository {
        async fn load(&self, match_id: &str) -> ApplicationResult<MatchAggregate> {
            let events = {
                let guard = self.events.lock().expect("repository lock should not fail");
                guard.get(match_id).cloned().unwrap_or_default()
            };

            let mut aggregate = MatchAggregate::default();
            for event in events {
                aggregate
                    .handle_event(event)
                    .map_err(|error| ApplicationError::domain(error.to_string()))?;
            }

            Ok(aggregate)
        }

        async fn append(&self, event: DomainEvent) -> ApplicationResult<()> {
            let match_id = event.match_id().to_string();
            let mut guard = self.events.lock().expect("repository lock should not fail");
            guard.entry(match_id).or_default().push(event);
            Ok(())
        }
    }

    struct FakeEventPublisher;

    #[async_trait]
    impl DomainEventPublisher for FakeEventPublisher {
        async fn publish(&self, _event: &DomainEvent) -> ApplicationResult<()> {
            Ok(())
        }
    }

    fn service() -> MatchSummaryService<FakeMatchRepository> {
        MatchSummaryService::with_publisher(FakeMatchRepository::default(), FakeEventPublisher)
    }

    fn build_match_started(home_gk: usize, away_gk: usize) -> MatchStarted {
        let mk_team = |team_id: &str, gk_count: usize| Team {
            team_id: TeamId(team_id.to_string()),
            starting_players: (0..5)
                .map(|i| Player {
                    player_id: PlayerId(format!("00000000-0000-0000-0000-{:012}", i + 1)),
                    is_goalkeeper: i < gk_count,
                })
                .collect(),
        };

        MatchStarted {
            event_id: "550e8400-e29b-41d4-a716-446655440000".to_string(),
            match_id: "11111111-2222-3333-4444-555555555555".to_string(),
            occurred_at: "2026-04-28T12:00:00Z".to_string(),
            match_time: MatchTime {
                minute: 0,
                second: 0,
                period: "FIRST_HALF".to_string(),
            },
            home_team: mk_team("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee", home_gk),
            away_team: mk_team("ffffffff-eeee-dddd-cccc-bbbbbbbbbbbb", away_gk),
            scheduled_duration_minutes: 40,
        }
    }

    #[tokio::test]
    async fn match_started_is_accepted_once() {
        let svc = service();
        let event = DomainEvent::MatchStarted(build_match_started(1, 1));
        svc.handle_event(event)
            .await
            .expect("first start should pass");
    }

    #[tokio::test]
    async fn duplicate_match_started_is_rejected() {
        let svc = service();
        let event = DomainEvent::MatchStarted(build_match_started(1, 1));
        svc.handle_event(event.clone())
            .await
            .expect("first start should pass");
        let second = svc.handle_event(event).await;
        assert!(second.is_err(), "duplicate MATCH_STARTED should fail");
    }

    #[tokio::test]
    async fn invalid_goalkeeper_count_is_rejected() {
        let svc = service();
        let bad = DomainEvent::MatchStarted(build_match_started(0, 1));
        let result = svc.handle_event(bad).await;
        assert!(result.is_err(), "home team without goalkeeper should fail");
    }

    #[tokio::test]
    async fn match_finished_score_must_match_computed_goals() {
        let svc = service();
        let started = DomainEvent::MatchStarted(build_match_started(1, 1));
        svc.handle_event(started).await.expect("start should pass");

        let goal = DomainEvent::GoalScored(GoalScored {
            event_id: "g1".to_string(),
            match_id: "11111111-2222-3333-4444-555555555555".to_string(),
            occurred_at: "2026-04-28T12:07:00Z".to_string(),
            match_time: MatchTime {
                minute: 7,
                second: 0,
                period: "FIRST_HALF".to_string(),
            },
            scoring_team_id: TeamId("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee".to_string()),
            scorer_id: PlayerId("00000000-0000-0000-0000-000000000002".to_string()),
            assist_id: None,
            is_own_goal: false,
        });
        svc.handle_event(goal).await.expect("goal should pass");

        let finished_bad = DomainEvent::MatchFinished(MatchFinished {
            event_id: "f1".to_string(),
            match_id: "11111111-2222-3333-4444-555555555555".to_string(),
            occurred_at: "2026-04-28T12:40:00Z".to_string(),
            match_time: MatchTime {
                minute: 40,
                second: 0,
                period: "SECOND_HALF".to_string(),
            },
            final_score: Score { home: 0, away: 0 },
        });

        let result = svc.handle_event(finished_bad).await;
        assert!(result.is_err(), "mismatched final score should fail");
    }

    #[tokio::test]
    async fn red_card_expel_rejects_subsequent_player_goal() {
        let svc = service();
        let started = DomainEvent::MatchStarted(build_match_started(1, 1));
        svc.handle_event(started).await.expect("start should pass");

        let red = DomainEvent::RedCard(crate::domain::RedCard {
            event_id: "r1".to_string(),
            match_id: "11111111-2222-3333-4444-555555555555".to_string(),
            occurred_at: "2026-04-28T12:10:00Z".to_string(),
            match_time: MatchTime {
                minute: 10,
                second: 0,
                period: "FIRST_HALF".to_string(),
            },
            player_id: PlayerId("00000000-0000-0000-0000-000000000002".to_string()),
            team_id: TeamId("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee".to_string()),
            is_double_yellow: false,
            related_foul_event_id: None,
        });
        svc.handle_event(red)
            .await
            .expect("red card should be accepted");

        let goal_by_expelled = DomainEvent::GoalScored(crate::domain::GoalScored {
            event_id: "g_expelled".to_string(),
            match_id: "11111111-2222-3333-4444-555555555555".to_string(),
            occurred_at: "2026-04-28T12:11:00Z".to_string(),
            match_time: MatchTime {
                minute: 11,
                second: 0,
                period: "FIRST_HALF".to_string(),
            },
            scoring_team_id: TeamId("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee".to_string()),
            scorer_id: PlayerId("00000000-0000-0000-0000-000000000002".to_string()),
            assist_id: None,
            is_own_goal: false,
        });

        let res = svc.handle_event(goal_by_expelled).await;
        assert!(res.is_err(), "goal by expelled player should be rejected");
    }

    #[tokio::test]
    async fn expelled_player_pass_shot_foul_rejected() {
        let svc = service();
        let started = DomainEvent::MatchStarted(build_match_started(1, 1));
        svc.handle_event(started).await.expect("start should pass");

        let red = DomainEvent::RedCard(crate::domain::RedCard {
            event_id: "r2".to_string(),
            match_id: "11111111-2222-3333-4444-555555555555".to_string(),
            occurred_at: "2026-04-28T12:15:00Z".to_string(),
            match_time: MatchTime {
                minute: 15,
                second: 0,
                period: "FIRST_HALF".to_string(),
            },
            player_id: PlayerId("00000000-0000-0000-0000-000000000003".to_string()),
            team_id: TeamId("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee".to_string()),
            is_double_yellow: false,
            related_foul_event_id: None,
        });
        svc.handle_event(red)
            .await
            .expect("red card should be accepted");

        let pass = DomainEvent::PassAttempted(crate::domain::PassAttempted {
            event_id: "p1".to_string(),
            match_id: "11111111-2222-3333-4444-555555555555".to_string(),
            occurred_at: "2026-04-28T12:16:00Z".to_string(),
            match_time: MatchTime {
                minute: 16,
                second: 0,
                period: "FIRST_HALF".to_string(),
            },
            player_id: PlayerId("00000000-0000-0000-0000-000000000003".to_string()),
            team_id: TeamId("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee".to_string()),
            target_player_id: None,
            succeeded: true,
        });
        assert!(svc.handle_event(pass).await.is_err());

        let shot = DomainEvent::ShotAttempted(crate::domain::ShotAttempted {
            event_id: "s1".to_string(),
            match_id: "11111111-2222-3333-4444-555555555555".to_string(),
            occurred_at: "2026-04-28T12:17:00Z".to_string(),
            match_time: MatchTime {
                minute: 17,
                second: 0,
                period: "FIRST_HALF".to_string(),
            },
            player_id: PlayerId("00000000-0000-0000-0000-000000000003".to_string()),
            team_id: TeamId("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee".to_string()),
            on_target: true,
            outcome: "SAVED".to_string(),
        });
        assert!(svc.handle_event(shot).await.is_err());

        let foul = DomainEvent::FoulCommitted(crate::domain::FoulCommitted {
            event_id: "f2".to_string(),
            match_id: "11111111-2222-3333-4444-555555555555".to_string(),
            occurred_at: "2026-04-28T12:18:00Z".to_string(),
            match_time: MatchTime {
                minute: 18,
                second: 0,
                period: "FIRST_HALF".to_string(),
            },
            player_id: PlayerId("00000000-0000-0000-0000-000000000003".to_string()),
            team_id: TeamId("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee".to_string()),
            against_player_id: None,
        });
        assert!(svc.handle_event(foul).await.is_err());
    }
}
