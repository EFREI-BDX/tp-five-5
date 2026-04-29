use super::{
    CardEntry, CardType, DomainEvent, GoalEntry, MatchStarted, MatchStatus, MatchSummary,
    PlayerId, Score, SubstitutionEntry, TeamId,
};
use anyhow::{Result, anyhow};
use std::collections::{HashMap, HashSet};

#[derive(Clone, Default)]
pub struct MatchAggregate {
    started: bool,
    finished: bool,
    cancelled: bool,
    forfeited: bool,
    home_team_id: Option<TeamId>,
    away_team_id: Option<TeamId>,
    computed_home_score: u32,
    computed_away_score: u32,
    expelled_players: HashSet<PlayerId>,
    paused: bool,
    goal_teams: HashMap<String, TeamId>,
    goals: Vec<GoalEntry>,
    cards: Vec<CardEntry>,
    substitutions: Vec<SubstitutionEntry>,
    match_end_second: u32,
}

impl MatchAggregate {
    /// Returns true if at least one event has been applied to this aggregate.
    pub fn is_known(&self) -> bool {
        self.started || self.cancelled
    }

    pub fn to_summary(&self, match_id: &str) -> MatchSummary {
        let status = if self.cancelled {
            MatchStatus::Cancelled
        } else if self.forfeited {
            MatchStatus::Forfeited
        } else if self.finished {
            MatchStatus::Finished
        } else if self.paused {
            MatchStatus::Paused
        } else if self.started {
            MatchStatus::InProgress
        } else {
            MatchStatus::NotStarted
        };

        MatchSummary {
            match_id: match_id.to_string(),
            status,
            home_team_id: self.home_team_id.clone(),
            away_team_id: self.away_team_id.clone(),
            score: Score {
                home: self.computed_home_score,
                away: self.computed_away_score,
            },
            goals: self.goals.clone(),
            cards: self.cards.clone(),
            substitutions: self.substitutions.clone(),
        }
    }

    pub fn handle_event(&mut self, event: DomainEvent) -> Result<()> {
        match event {
            DomainEvent::MatchStarted(match_started) => self.start_match(match_started),
            DomainEvent::GoalScored(goal) => {
                self.validate_active("GOAL_SCORED")?;
                self.reject_if_expelled(&goal.scorer_id)?;
                if let Some(assist) = &goal.assist_id {
                    self.reject_if_expelled(assist)?;
                }

                let home = self
                    .home_team_id
                    .as_ref()
                    .ok_or_else(|| anyhow!("missing home team id in match state"))?;
                let away = self
                    .away_team_id
                    .as_ref()
                    .ok_or_else(|| anyhow!("missing away team id in match state"))?;

                if &goal.scoring_team_id == home {
                    self.computed_home_score += 1;
                } else if &goal.scoring_team_id == away {
                    self.computed_away_score += 1;
                } else {
                    return Err(anyhow!(
                        "GOAL_SCORED scoringTeamId does not belong to the match teams"
                    ));
                }

                self.goal_teams
                    .insert(goal.event_id.clone(), goal.scoring_team_id.clone());

                self.goals.push(GoalEntry {
                    event_id: goal.event_id,
                    scoring_team_id: goal.scoring_team_id,
                    scorer_id: goal.scorer_id,
                    assist_id: goal.assist_id,
                    is_own_goal: goal.is_own_goal,
                    match_time: goal.match_time,
                    cancelled: false,
                });
                Ok(())
            }
            DomainEvent::GoalCancelled(cancelled) => {
                self.validate_active("GOAL_CANCELLED")?;
                let scoring_team_id = self
                    .goal_teams
                    .remove(&cancelled.cancelled_goal_event_id.0)
                    .ok_or_else(|| anyhow!("GOAL_CANCELLED references an unknown GOAL_SCORED"))?;

                let home = self
                    .home_team_id
                    .as_ref()
                    .ok_or_else(|| anyhow!("missing home team id in match state"))?;

                if &scoring_team_id == home {
                    self.computed_home_score = self.computed_home_score.saturating_sub(1);
                } else {
                    self.computed_away_score = self.computed_away_score.saturating_sub(1);
                }

                if let Some(entry) = self
                    .goals
                    .iter_mut()
                    .find(|g| g.event_id == cancelled.cancelled_goal_event_id.0)
                {
                    entry.cancelled = true;
                }

                Ok(())
            }
            DomainEvent::PassAttempted(pass) => {
                self.validate_active("PASS_ATTEMPTED")?;
                self.reject_if_expelled(&pass.player_id)
            }
            DomainEvent::ShotAttempted(shot) => {
                self.validate_active("SHOT_ATTEMPTED")?;
                self.reject_if_expelled(&shot.player_id)
            }
            DomainEvent::FoulCommitted(foul) => {
                self.validate_active("FOUL_COMMITTED")?;
                self.reject_if_expelled(&foul.player_id)
            }
            DomainEvent::YellowCard(card) => {
                self.validate_active("YELLOW_CARD")?;
                self.reject_if_expelled(&card.player_id)?;
                self.cards.push(CardEntry {
                    event_id: card.event_id,
                    player_id: card.player_id,
                    team_id: card.team_id,
                    match_time: card.match_time,
                    card_type: CardType::Yellow {
                        card_number: card.card_number,
                    },
                });
                Ok(())
            }
            DomainEvent::SaveMade(save) => {
                self.validate_active("SAVE_MADE")?;
                self.reject_if_expelled(&save.keeper_id)
            }
            DomainEvent::Substitution(sub) => {
                self.validate_active("SUBSTITUTION")?;
                self.reject_if_expelled(&sub.player_out)?;
                self.reject_if_expelled(&sub.player_in)?;
                self.substitutions.push(SubstitutionEntry {
                    event_id: sub.event_id,
                    team_id: sub.team_id,
                    player_out: sub.player_out,
                    player_in: sub.player_in,
                    match_time: sub.match_time,
                });
                Ok(())
            }
            DomainEvent::RedCard(red) => {
                self.validate_active("RED_CARD")?;
                self.cards.push(CardEntry {
                    event_id: red.event_id.clone(),
                    player_id: red.player_id.clone(),
                    team_id: red.team_id,
                    match_time: red.match_time,
                    card_type: CardType::Red {
                        is_double_yellow: red.is_double_yellow,
                    },
                });
                self.expelled_players.insert(red.player_id);
                Ok(())
            }
            DomainEvent::MatchPaused(_) => {
                self.validate_active("MATCH_PAUSED")?;
                if self.paused {
                    return Err(anyhow!("MATCH_PAUSED received while already paused"));
                }
                self.paused = true;
                Ok(())
            }
            DomainEvent::MatchResumed(_) => {
                self.validate_active("MATCH_RESUMED")?;
                if !self.paused {
                    return Err(anyhow!("MATCH_RESUMED received while match is not paused"));
                }
                self.paused = false;
                Ok(())
            }
            DomainEvent::MatchCancelled(_) => {
                if self.finished {
                    return Err(anyhow!(
                        "MATCH_CANCELLED received after terminal match event"
                    ));
                }
                self.cancelled = true;
                self.finished = true;
                Ok(())
            }
            DomainEvent::MatchForfeited(forfeited) => {
                self.validate_active("MATCH_FORFEITED")?;
                self.forfeited = true;
                self.finished = true;
                self.computed_home_score = forfeited.administrative_score.home;
                self.computed_away_score = forfeited.administrative_score.away;
                self.match_end_second =
                    forfeited.match_time.minute * 60 + forfeited.match_time.second;
                Ok(())
            }
            DomainEvent::MatchFinished(finished) => {
                if !self.started {
                    return Err(anyhow!("MATCH_FINISHED received before MATCH_STARTED"));
                }

                if self.finished {
                    return Err(anyhow!("MATCH_FINISHED can only occur once"));
                }

                if self.computed_home_score != finished.final_score.home
                    || self.computed_away_score != finished.final_score.away
                {
                    return Err(anyhow!(
                        "MATCH_FINISHED score mismatch: expected {}-{}, got {}-{}",
                        self.computed_home_score,
                        self.computed_away_score,
                        finished.final_score.home,
                        finished.final_score.away
                    ));
                }

                self.match_end_second =
                    finished.match_time.minute * 60 + finished.match_time.second;
                self.finished = true;
                Ok(())
            }
        }
    }

    fn start_match(&mut self, event: MatchStarted) -> Result<()> {
        Self::validate_match_started(&event)?;

        if self.started {
            return Err(anyhow!(
                "MATCH_STARTED must be the first event and can only occur once per match"
            ));
        }

        self.started = true;
        self.home_team_id = Some(event.home_team.team_id.clone());
        self.away_team_id = Some(event.away_team.team_id.clone());

        Ok(())
    }

    fn validate_match_started(event: &MatchStarted) -> Result<()> {
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

    fn reject_if_expelled(&self, player_id: &PlayerId) -> Result<()> {
        if self.expelled_players.contains(player_id) {
            return Err(anyhow!("action by expelled player is not allowed"));
        }
        Ok(())
    }

    fn validate_active(&self, event_type: &str) -> Result<()> {
        if self.finished {
            return Err(anyhow!(
                "{} received after terminal match event",
                event_type
            ));
        }

        if !self.started {
            return Err(anyhow!("{} received before MATCH_STARTED", event_type));
        }

        Ok(())
    }
}

#[cfg(test)]
mod tests {
    use super::MatchAggregate;
    use crate::domain::{
        DomainEvent, EventId, GoalCancelled, GoalScored, MatchCancelled, MatchFinished,
        MatchForfeited, MatchPaused, MatchResumed, MatchStarted, MatchTime, PassAttempted, Player,
        PlayerId, Score, Team, TeamId,
    };

    const MATCH_ID: &str = "11111111-2222-3333-4444-555555555555";
    const HOME_TEAM_ID: &str = "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee";
    const AWAY_TEAM_ID: &str = "ffffffff-eeee-dddd-cccc-bbbbbbbbbbbb";
    const PLAYER_2_ID: &str = "00000000-0000-0000-0000-000000000002";

    fn match_time() -> MatchTime {
        MatchTime {
            minute: 0,
            second: 0,
            period: "FIRST_HALF".to_string(),
        }
    }

    fn started_event() -> DomainEvent {
        let team = |team_id: &str, first_player: u32| Team {
            team_id: TeamId(team_id.to_string()),
            starting_players: (0..5)
                .map(|i| Player {
                    player_id: PlayerId(format!(
                        "00000000-0000-0000-0000-{:012}",
                        first_player + i
                    )),
                    is_goalkeeper: i == 0,
                })
                .collect(),
        };

        DomainEvent::MatchStarted(MatchStarted {
            event_id: "550e8400-e29b-41d4-a716-446655440000".to_string(),
            match_id: MATCH_ID.to_string(),
            occurred_at: "2026-04-28T12:00:00Z".to_string(),
            match_time: match_time(),
            home_team: team(HOME_TEAM_ID, 1),
            away_team: team(AWAY_TEAM_ID, 6),
            scheduled_duration_minutes: 40,
        })
    }

    fn goal_event(event_id: &str) -> DomainEvent {
        DomainEvent::GoalScored(GoalScored {
            event_id: event_id.to_string(),
            match_id: MATCH_ID.to_string(),
            occurred_at: "2026-04-28T12:07:00Z".to_string(),
            match_time: match_time(),
            scoring_team_id: TeamId(HOME_TEAM_ID.to_string()),
            scorer_id: PlayerId(PLAYER_2_ID.to_string()),
            assist_id: None,
            is_own_goal: false,
        })
    }

    fn finish_event(home: u32, away: u32) -> DomainEvent {
        DomainEvent::MatchFinished(MatchFinished {
            event_id: "850e8400-e29b-41d4-a716-446655440001".to_string(),
            match_id: MATCH_ID.to_string(),
            occurred_at: "2026-04-28T12:40:00Z".to_string(),
            match_time: MatchTime {
                minute: 40,
                second: 0,
                period: "SECOND_HALF".to_string(),
            },
            final_score: Score { home, away },
        })
    }

    #[test]
    fn pass_before_match_started_is_rejected() {
        let mut aggregate = MatchAggregate::default();

        let result = aggregate.handle_event(DomainEvent::PassAttempted(PassAttempted {
            event_id: "650e8400-e29b-41d4-a716-446655440002".to_string(),
            match_id: MATCH_ID.to_string(),
            occurred_at: "2026-04-28T12:03:05Z".to_string(),
            match_time: match_time(),
            player_id: PlayerId(PLAYER_2_ID.to_string()),
            team_id: TeamId(HOME_TEAM_ID.to_string()),
            target_player_id: None,
            succeeded: true,
        }));

        assert!(result.is_err(), "game actions before start must fail");
    }

    #[test]
    fn goal_cancelled_for_unknown_goal_is_rejected() {
        let mut aggregate = MatchAggregate::default();
        aggregate
            .handle_event(started_event())
            .expect("start should pass");

        let result = aggregate.handle_event(DomainEvent::GoalCancelled(GoalCancelled {
            event_id: "650e8400-e29b-41d4-a716-446655440008".to_string(),
            match_id: MATCH_ID.to_string(),
            occurred_at: "2026-04-28T12:07:55Z".to_string(),
            match_time: match_time(),
            cancelled_goal_event_id: EventId("650e8400-e29b-41d4-a716-446655449999".to_string()),
            reason: "OFFSIDE".to_string(),
        }));

        assert!(result.is_err(), "unknown cancelled goal should fail");
    }

    #[test]
    fn cancelled_goal_recalculates_final_score() {
        let mut aggregate = MatchAggregate::default();
        aggregate
            .handle_event(started_event())
            .expect("start should pass");
        aggregate
            .handle_event(goal_event("650e8400-e29b-41d4-a716-446655440001"))
            .expect("goal should pass");
        aggregate
            .handle_event(DomainEvent::GoalCancelled(GoalCancelled {
                event_id: "650e8400-e29b-41d4-a716-446655440008".to_string(),
                match_id: MATCH_ID.to_string(),
                occurred_at: "2026-04-28T12:07:55Z".to_string(),
                match_time: match_time(),
                cancelled_goal_event_id: EventId(
                    "650e8400-e29b-41d4-a716-446655440001".to_string(),
                ),
                reason: "OFFSIDE".to_string(),
            }))
            .expect("goal cancellation should pass");

        aggregate
            .handle_event(finish_event(0, 0))
            .expect("final score should account for cancelled goal");
    }

    #[test]
    fn cancelled_goal_is_marked_in_summary() {
        let mut aggregate = MatchAggregate::default();
        aggregate.handle_event(started_event()).expect("start");
        aggregate
            .handle_event(goal_event("650e8400-e29b-41d4-a716-446655440001"))
            .expect("goal");
        aggregate
            .handle_event(DomainEvent::GoalCancelled(GoalCancelled {
                event_id: "650e8400-e29b-41d4-a716-446655440008".to_string(),
                match_id: MATCH_ID.to_string(),
                occurred_at: "2026-04-28T12:07:55Z".to_string(),
                match_time: match_time(),
                cancelled_goal_event_id: EventId(
                    "650e8400-e29b-41d4-a716-446655440001".to_string(),
                ),
                reason: "OFFSIDE".to_string(),
            }))
            .expect("cancellation");

        let summary = aggregate.to_summary(MATCH_ID);
        assert_eq!(summary.goals.len(), 1);
        assert!(
            summary.goals[0].cancelled,
            "goal should be marked as cancelled in summary"
        );
        assert_eq!(summary.score.home, 0, "score should be recalculated");
    }

    #[test]
    fn match_resumed_without_pause_is_rejected() {
        let mut aggregate = MatchAggregate::default();
        aggregate
            .handle_event(started_event())
            .expect("start should pass");

        let result = aggregate.handle_event(DomainEvent::MatchResumed(MatchResumed {
            event_id: "650e8400-e29b-41d4-a716-446655440010".to_string(),
            match_id: MATCH_ID.to_string(),
            occurred_at: "2026-04-28T12:25:00Z".to_string(),
            match_time: match_time(),
            reason: "HALF_TIME_END".to_string(),
        }));

        assert!(result.is_err(), "resume without pause should fail");
    }

    #[test]
    fn match_paused_twice_is_rejected() {
        let mut aggregate = MatchAggregate::default();
        aggregate
            .handle_event(started_event())
            .expect("start should pass");

        let pause = || {
            DomainEvent::MatchPaused(MatchPaused {
                event_id: "650e8400-e29b-41d4-a716-446655440009".to_string(),
                match_id: MATCH_ID.to_string(),
                occurred_at: "2026-04-28T12:20:00Z".to_string(),
                match_time: match_time(),
                reason: "HALF_TIME".to_string(),
            })
        };

        aggregate
            .handle_event(pause())
            .expect("first pause should pass");
        let result = aggregate.handle_event(pause());

        assert!(result.is_err(), "second pause should fail");
    }

    #[test]
    fn match_cancelled_is_terminal() {
        let mut aggregate = MatchAggregate::default();
        aggregate
            .handle_event(started_event())
            .expect("start should pass");
        aggregate
            .handle_event(DomainEvent::MatchCancelled(MatchCancelled {
                event_id: "650e8400-e29b-41d4-a716-446655440011".to_string(),
                match_id: MATCH_ID.to_string(),
                occurred_at: "2026-04-28T12:20:00Z".to_string(),
                match_time: match_time(),
                reason: "WEATHER".to_string(),
            }))
            .expect("cancel should pass");

        let result = aggregate.handle_event(goal_event("650e8400-e29b-41d4-a716-446655440001"));

        assert!(result.is_err(), "events after cancellation should fail");
    }

    #[test]
    fn match_forfeited_before_start_is_rejected() {
        let mut aggregate = MatchAggregate::default();

        let result = aggregate.handle_event(DomainEvent::MatchForfeited(MatchForfeited {
            event_id: "650e8400-e29b-41d4-a716-446655440012".to_string(),
            match_id: MATCH_ID.to_string(),
            occurred_at: "2026-04-28T12:12:30Z".to_string(),
            match_time: match_time(),
            forfeiting_team_id: TeamId(AWAY_TEAM_ID.to_string()),
            reason: "TEAM_ABSENT".to_string(),
            administrative_score: Score { home: 3, away: 0 },
            stats_policy: "DISCARDED".to_string(),
        }));

        assert!(result.is_err(), "forfeit before start should fail");
    }

    #[test]
    fn unknown_match_is_not_known() {
        let aggregate = MatchAggregate::default();
        assert!(!aggregate.is_known(), "fresh aggregate should not be known");
    }

    #[test]
    fn started_match_is_known() {
        let mut aggregate = MatchAggregate::default();
        aggregate.handle_event(started_event()).expect("start");
        assert!(aggregate.is_known(), "started match should be known");
    }
}
