use super::{
    CardEntry, CardType, MatchResult, MatchStatus, MatchSummary, MatchTime, PlayerId,
    PlayerMatchStats, Score, SubstitutionEntry, TeamId,
};
use std::collections::HashMap;

#[derive(Debug, Clone)]
pub struct RecordMatchFeed {
    pub match_id: String,
    pub home_team_id: Option<TeamId>,
    pub away_team_id: Option<TeamId>,
    pub players: Vec<RecordPlayer>,
    pub events: Vec<RecordMatchEvent>,
}

#[derive(Debug, Clone)]
pub struct RecordPlayer {
    pub player_id: PlayerId,
    pub team_id: TeamId,
}

#[derive(Debug, Clone)]
pub struct RecordMatchEvent {
    pub event_id: String,
    pub action: RecordAction,
    pub actor_player_id: Option<PlayerId>,
    pub actor_team_id: Option<TeamId>,
    pub secondary_player_id: Option<PlayerId>,
    pub secondary_team_id: Option<TeamId>,
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum RecordAction {
    MatchStarted,
    MatchFinished,
    Goal,
    Assist,
    Shot,
    YellowCard,
    RedCard,
    Save,
    Foul,
    Substitution,
    SubIn,
    SubOut,
    ShotBlocked,
    Unknown,
}

impl RecordAction {
    pub fn from_record_name(value: &str) -> Self {
        match normalize(value).as_str() {
            "kickoff" | "start" | "debut" | "dbut" | "matchstarted" | "match_started" => {
                Self::MatchStarted
            }
            "endmatch" | "end" | "fin" | "matchended" | "match_finished" | "matchfinished" => {
                Self::MatchFinished
            }
            "goal" | "but" => Self::Goal,
            "asst" | "assist" | "passedecisive" | "passedcisive" => Self::Assist,
            "shot" | "tir" | "tircadre" | "tircadr" => Self::Shot,
            "yel" | "yellowcard" | "yellow_card" | "cartonjaune" => Self::YellowCard,
            "red" | "redcard" | "red_card" | "cartonrouge" => Self::RedCard,
            "save" | "arret" | "arrt" => Self::Save,
            "foul" | "faute" => Self::Foul,
            "subev" | "substitution" | "remplacement" => Self::Substitution,
            "subin" | "entree" | "entre" => Self::SubIn,
            "subof" | "subout" | "sortie" => Self::SubOut,
            "shblk" | "shotblocked" | "shot_blocked" | "tirbloque" | "tirbloqu" => {
                Self::ShotBlocked
            }
            _ => Self::Unknown,
        }
    }
}

#[derive(Debug, Clone)]
pub struct ProjectedMatch {
    pub summary: MatchSummary,
    pub player_stats: Vec<PlayerMatchStats>,
}

#[derive(Default)]
struct PlayerStatsBuilder {
    team_id: Option<TeamId>,
    goals: u32,
    assists: u32,
    saves: u32,
}

pub fn project_record_match(feed: RecordMatchFeed) -> ProjectedMatch {
    let player_teams = player_teams(&feed);
    let mut stats = seed_player_stats(&feed.players);
    let mut home_score = 0;
    let mut away_score = 0;
    let mut cards = Vec::new();
    let mut substitutions = Vec::new();
    let mut started = false;
    let mut finished = false;

    for event in &feed.events {
        match event.action {
            RecordAction::MatchStarted => started = true,
            RecordAction::MatchFinished => finished = true,
            RecordAction::Goal => {
                if let Some(player_id) = &event.actor_player_id {
                    stats.entry(player_id.clone()).or_default().goals += 1;
                }

                if let Some(team_id) = event_team(event, &player_teams) {
                    if feed.home_team_id.as_ref() == Some(&team_id) {
                        home_score += 1;
                    } else if feed.away_team_id.as_ref() == Some(&team_id) {
                        away_score += 1;
                    }
                }
            }
            RecordAction::Assist => {
                if let Some(player_id) = &event.actor_player_id {
                    stats.entry(player_id.clone()).or_default().assists += 1;
                }
            }
            RecordAction::Save => {
                if let Some(player_id) = &event.actor_player_id {
                    stats.entry(player_id.clone()).or_default().saves += 1;
                }
            }
            RecordAction::YellowCard | RecordAction::RedCard => {
                if let (Some(player_id), Some(team_id)) =
                    (&event.actor_player_id, event_team(event, &player_teams))
                {
                    let card_type = match event.action {
                        RecordAction::YellowCard => CardType::Yellow { card_number: 1 },
                        RecordAction::RedCard => CardType::Red {
                            is_double_yellow: false,
                        },
                        _ => unreachable!(),
                    };

                    cards.push(CardEntry {
                        event_id: event.event_id.clone(),
                        player_id: player_id.clone(),
                        team_id,
                        match_time: default_match_time(),
                        card_type,
                    });
                }
            }
            RecordAction::Substitution => {
                if let (Some(player_in), Some(player_out), Some(team_id)) = (
                    &event.actor_player_id,
                    &event.secondary_player_id,
                    event_team(event, &player_teams),
                ) {
                    substitutions.push(SubstitutionEntry {
                        event_id: event.event_id.clone(),
                        team_id,
                        player_out: player_out.clone(),
                        player_in: player_in.clone(),
                        match_time: default_match_time(),
                    });
                }
            }
            _ => {}
        }
    }

    for (player_id, team_id) in player_teams {
        stats.entry(player_id).or_default().team_id = Some(team_id);
    }

    let status = if finished {
        MatchStatus::Finished
    } else if started || !feed.events.is_empty() {
        MatchStatus::InProgress
    } else {
        MatchStatus::NotStarted
    };

    let score = Score {
        home: home_score,
        away: away_score,
    };

    let player_stats = build_player_match_stats(
        stats,
        feed.home_team_id.as_ref(),
        feed.away_team_id.as_ref(),
        &score,
        status == MatchStatus::Finished,
    );

    ProjectedMatch {
        summary: MatchSummary {
            match_id: feed.match_id,
            status,
            home_team_id: feed.home_team_id,
            away_team_id: feed.away_team_id,
            score,
            goals: Vec::new(),
            cards,
            substitutions,
        },
        player_stats,
    }
}

fn player_teams(feed: &RecordMatchFeed) -> HashMap<PlayerId, TeamId> {
    let mut teams = HashMap::new();
    for player in &feed.players {
        teams.insert(player.player_id.clone(), player.team_id.clone());
    }
    for event in &feed.events {
        if let (Some(player_id), Some(team_id)) = (&event.actor_player_id, &event.actor_team_id) {
            teams.insert(player_id.clone(), team_id.clone());
        }
        if let (Some(player_id), Some(team_id)) =
            (&event.secondary_player_id, &event.secondary_team_id)
        {
            teams.insert(player_id.clone(), team_id.clone());
        }
    }
    teams
}

fn seed_player_stats(players: &[RecordPlayer]) -> HashMap<PlayerId, PlayerStatsBuilder> {
    players
        .iter()
        .map(|player| {
            (
                player.player_id.clone(),
                PlayerStatsBuilder {
                    team_id: Some(player.team_id.clone()),
                    ..Default::default()
                },
            )
        })
        .collect()
}

fn event_team(
    event: &RecordMatchEvent,
    player_teams: &HashMap<PlayerId, TeamId>,
) -> Option<TeamId> {
    event.actor_team_id.clone().or_else(|| {
        event
            .actor_player_id
            .as_ref()
            .and_then(|player_id| player_teams.get(player_id).cloned())
    })
}

fn build_player_match_stats(
    stats: HashMap<PlayerId, PlayerStatsBuilder>,
    home_team_id: Option<&TeamId>,
    away_team_id: Option<&TeamId>,
    score: &Score,
    finished: bool,
) -> Vec<PlayerMatchStats> {
    let home_result = match score.home.cmp(&score.away) {
        std::cmp::Ordering::Greater => MatchResult::Win,
        std::cmp::Ordering::Less => MatchResult::Loss,
        std::cmp::Ordering::Equal => MatchResult::Draw,
    };
    let away_result = match home_result {
        MatchResult::Win => MatchResult::Loss,
        MatchResult::Loss => MatchResult::Win,
        MatchResult::Draw => MatchResult::Draw,
    };

    let max_goals = stats.values().map(|s| s.goals).max().unwrap_or(0);
    let max_assists = stats.values().map(|s| s.assists).max().unwrap_or(0);
    let max_mvp_score = stats
        .values()
        .map(|s| s.goals + s.assists + s.saves)
        .max()
        .unwrap_or(0);

    let mut rows = stats
        .into_iter()
        .map(|(player_id, stats)| {
            let result = if !finished {
                MatchResult::Draw
            } else if stats.team_id.as_ref() == home_team_id {
                home_result.clone()
            } else if stats.team_id.as_ref() == away_team_id {
                away_result.clone()
            } else {
                MatchResult::Draw
            };

            PlayerMatchStats {
                player_id,
                goals: stats.goals,
                assists: stats.assists,
                saves: stats.saves,
                result,
                best_scorer: max_goals > 0 && stats.goals == max_goals,
                best_assists_provider: max_assists > 0 && stats.assists == max_assists,
                mvp: max_mvp_score > 0
                    && stats.goals + stats.assists + stats.saves == max_mvp_score,
                play_time: 0,
            }
        })
        .collect::<Vec<_>>();

    rows.sort_by(|left, right| left.player_id.0.cmp(&right.player_id.0));
    rows
}

fn default_match_time() -> MatchTime {
    MatchTime {
        minute: 0,
        second: 0,
        period: "FIRST_HALF".to_string(),
    }
}

fn normalize(value: &str) -> String {
    value
        .chars()
        .filter(|c| c.is_ascii_alphanumeric() || *c == '_')
        .flat_map(|c| c.to_lowercase())
        .collect()
}
