use crate::application::{ApplicationError, ApplicationResult, MatchRepository};
use crate::domain::{DomainEvent, MatchAggregate};
use async_trait::async_trait;
use std::collections::HashMap;
use std::sync::{Arc, Mutex};

#[derive(Clone)]
pub struct InMemoryMatchRepository {
    events: Arc<Mutex<HashMap<String, Vec<DomainEvent>>>>,
}

impl Default for InMemoryMatchRepository {
    fn default() -> Self {
        Self {
            events: Arc::new(Mutex::new(HashMap::new())),
        }
    }
}

impl InMemoryMatchRepository {
    pub fn new() -> Self {
        Self::default()
    }
}

#[async_trait]
impl MatchRepository for InMemoryMatchRepository {
    async fn load(&self, match_id: &str) -> ApplicationResult<MatchAggregate> {
        let events = {
            let guard = self
                .events
                .lock()
                .map_err(|_| ApplicationError::repository("match repository lock poisoned"))?;
            guard.get(match_id).cloned().unwrap_or_default()
        };

        replay_events(events)
    }

    async fn append(&self, event: DomainEvent) -> ApplicationResult<()> {
        let match_id = event.match_id().to_string();
        let mut guard = self
            .events
            .lock()
            .map_err(|_| ApplicationError::repository("match repository lock poisoned"))?;

        guard.entry(match_id).or_default().push(event);
        Ok(())
    }
}

pub(crate) fn replay_events(
    events: impl IntoIterator<Item = DomainEvent>,
) -> ApplicationResult<MatchAggregate> {
    let mut aggregate = MatchAggregate::default();
    for event in events {
        aggregate
            .handle_event(event)
            .map_err(|error| ApplicationError::domain(error.to_string()))?;
    }
    Ok(aggregate)
}

#[cfg(test)]
mod tests {
    use super::InMemoryMatchRepository;
    use crate::application::MatchRepository;
    use crate::domain::{DomainEvent, MatchStarted, MatchTime, Player, PlayerId, Team, TeamId};

    fn match_started() -> DomainEvent {
        let team = |team_id: &str, player_offset: u8| Team {
            team_id: TeamId(team_id.to_string()),
            starting_players: (0..5)
                .map(|i| Player {
                    player_id: PlayerId(format!(
                        "00000000-0000-0000-0000-{:012}",
                        player_offset + i
                    )),
                    is_goalkeeper: i == 0,
                })
                .collect(),
        };

        DomainEvent::MatchStarted(MatchStarted {
            event_id: "550e8400-e29b-41d4-a716-446655440000".to_string(),
            match_id: "11111111-2222-3333-4444-555555555555".to_string(),
            occurred_at: "2026-04-28T12:00:00Z".to_string(),
            match_time: MatchTime {
                minute: 0,
                second: 0,
                period: "FIRST_HALF".to_string(),
            },
            home_team: team("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee", 1),
            away_team: team("ffffffff-eeee-dddd-cccc-bbbbbbbbbbbb", 6),
            scheduled_duration_minutes: 40,
        })
    }

    #[tokio::test]
    async fn load_replays_persisted_events() {
        let repository = InMemoryMatchRepository::new();
        let event = match_started();
        let match_id = event.match_id().to_string();

        repository.append(event).await.expect("append should pass");
        let mut aggregate = repository.load(&match_id).await.expect("load should pass");

        let duplicate = match_started();
        let result = aggregate.handle_event(duplicate);
        assert!(
            result.is_err(),
            "replayed aggregate should know match started"
        );
    }
}
