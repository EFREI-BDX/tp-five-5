use crate::domain::{DomainEvent, PlayerId, TeamId};
use async_trait::async_trait;
use anyhow::{Result, anyhow};
use std::collections::HashMap;
use std::sync::Mutex;

#[async_trait]
pub trait ApplicationService: Send + Sync + 'static {
    async fn handle_event(&self, event: DomainEvent) -> Result<()>;
}

#[derive(Default)]
pub struct MatchSummaryService {
    state: Mutex<HashMap<String, MatchState>>,
}

#[derive(Default)]
struct MatchState {
    started: bool,
    finished: bool,
    home_team_id: Option<TeamId>,
    away_team_id: Option<TeamId>,
    computed_home_score: u32,
    computed_away_score: u32,
}

impl MatchSummaryService {
    pub fn new() -> Self {
        Self::default()
    }

    fn validate_match_started(event: &crate::domain::MatchStarted) -> Result<()> {
        let home_goalkeepers = event
            .home_team
            .starting_players
            .iter()
            .filter(|p| p.is_goalkeeper)
            .count();
        let away_goalkeepers = event
            .away_team
            .starting_players
            .iter()
            .filter(|p| p.is_goalkeeper)
            .count();

        if home_goalkeepers != 1 {
            return Err(anyhow!(
                "MATCH_STARTED invalid: home team must have exactly one goalkeeper"
            ));
        }

        if away_goalkeepers != 1 {
            return Err(anyhow!(
                "MATCH_STARTED invalid: away team must have exactly one goalkeeper"
            ));
        }

        Ok(())
    }
}

#[async_trait]
impl ApplicationService for MatchSummaryService {
    async fn handle_event(&self, event: DomainEvent) -> Result<()> {
        match event {
            DomainEvent::MatchStarted(match_started) => {
                Self::validate_match_started(&match_started)?;

                let mut guard = self.state.lock().map_err(|_| anyhow!("state lock poisoned"))?;
                let state = guard
                    .entry(match_started.match_id.clone())
                    .or_default();

                if state.started {
                    return Err(anyhow!(
                        "MATCH_STARTED must be the first event and can only occur once per match"
                    ));
                }

                state.started = true;
                state.home_team_id = Some(match_started.home_team.team_id.clone());
                state.away_team_id = Some(match_started.away_team.team_id.clone());
                Ok(())
            }
            DomainEvent::GoalScored(goal) => {
                let mut guard = self.state.lock().map_err(|_| anyhow!("state lock poisoned"))?;
                let state = guard.entry(goal.match_id.clone()).or_default();

                if !state.started {
                    return Err(anyhow!("GOAL_SCORED received before MATCH_STARTED"));
                }

                if state.finished {
                    return Err(anyhow!("GOAL_SCORED received after MATCH_FINISHED"));
                }

                let home = state
                    .home_team_id
                    .as_ref()
                    .ok_or_else(|| anyhow!("missing home team id in match state"))?;
                let away = state
                    .away_team_id
                    .as_ref()
                    .ok_or_else(|| anyhow!("missing away team id in match state"))?;

                if &goal.scoring_team_id == home {
                    state.computed_home_score += 1;
                } else if &goal.scoring_team_id == away {
                    state.computed_away_score += 1;
                } else {
                    return Err(anyhow!(
                        "GOAL_SCORED scoringTeamId does not belong to the match teams"
                    ));
                }

                Ok(())
            }
            DomainEvent::MatchFinished(finished) => {
                let mut guard = self.state.lock().map_err(|_| anyhow!("state lock poisoned"))?;
                let state = guard
                    .entry(finished.match_id.clone())
                    .or_default();

                if !state.started {
                    return Err(anyhow!("MATCH_FINISHED received before MATCH_STARTED"));
                }

                if state.finished {
                    return Err(anyhow!("MATCH_FINISHED can only occur once"));
                }

                if state.computed_home_score != finished.final_score.home
                    || state.computed_away_score != finished.final_score.away
                {
                    return Err(anyhow!(
                        "MATCH_FINISHED score mismatch: expected {}-{}, got {}-{}",
                        state.computed_home_score,
                        state.computed_away_score,
                        finished.final_score.home,
                        finished.final_score.away
                    ));
                }

                state.finished = true;
                Ok(())
            }
        }
    }
}

#[cfg(test)]
mod tests {
    use super::{ApplicationService, MatchSummaryService};
    use crate::domain::{
        DomainEvent, GoalScored, MatchFinished, MatchStarted, MatchTime, Player, Team,
        PlayerId, TeamId, Score,
    };

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
        let svc = MatchSummaryService::new();
        let event = DomainEvent::MatchStarted(build_match_started(1, 1));
        svc.handle_event(event).await.expect("first start should pass");
    }

    #[tokio::test]
    async fn duplicate_match_started_is_rejected() {
        let svc = MatchSummaryService::new();
        let event = DomainEvent::MatchStarted(build_match_started(1, 1));
        svc.handle_event(event.clone())
            .await
            .expect("first start should pass");
        let second = svc.handle_event(event).await;
        assert!(second.is_err(), "duplicate MATCH_STARTED should fail");
    }

    #[tokio::test]
    async fn invalid_goalkeeper_count_is_rejected() {
        let svc = MatchSummaryService::new();
        let bad = DomainEvent::MatchStarted(build_match_started(0, 1));
        let result = svc.handle_event(bad).await;
        assert!(result.is_err(), "home team without goalkeeper should fail");
    }

    #[tokio::test]
    async fn match_finished_score_must_match_computed_goals() {
        let svc = MatchSummaryService::new();
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
}
