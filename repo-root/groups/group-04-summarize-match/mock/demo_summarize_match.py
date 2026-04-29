#!/usr/bin/env python3
"""
Demo generator for the summarize-match service.

It simulates the upstream record-match context by sending a valid, varied match
event sequence to POST /events, then reads the computed summary.
"""

import argparse
import json
import random
import sys
import time
import urllib.error
import urllib.request
import uuid


HOME_TEAM_ID = "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"
AWAY_TEAM_ID = "ffffffff-eeee-dddd-cccc-bbbbbbbbbbbb"

HOME_PLAYERS = [
    "00000000-0000-0000-0000-000000000001",
    "00000000-0000-0000-0000-000000000002",
    "00000000-0000-0000-0000-000000000003",
    "00000000-0000-0000-0000-000000000004",
    "00000000-0000-0000-0000-000000000005",
]
AWAY_PLAYERS = [
    "00000000-0000-0000-0000-000000000006",
    "00000000-0000-0000-0000-000000000007",
    "00000000-0000-0000-0000-000000000008",
    "00000000-0000-0000-0000-000000000009",
    "00000000-0000-0000-0000-000000000010",
]

HOME_BENCH = [
    "00000000-0000-0000-0000-000000000011",
    "00000000-0000-0000-0000-000000000012",
]
AWAY_BENCH = [
    "00000000-0000-0000-0000-000000000013",
    "00000000-0000-0000-0000-000000000014",
]


def main():
    args = parse_args()
    rng = random.Random(args.seed)
    base_url = args.base_url.rstrip("/")

    if not args.skip_health:
        wait_for_health(base_url, args.timeout_seconds)

    match_id = args.match_id or str(uuid.uuid4())
    events, expected_score = build_demo_events(match_id, rng)

    print(f"Demo match id: {match_id}")
    print(f"Expected final score: {expected_score['home']}-{expected_score['away']}")
    print()
    print_sent_events(events)
    print_expected_summary_events(events)
    print()

    for index, event in enumerate(events, start=1):
        status, body = post_json(f"{base_url}/events", event, args.timeout_seconds)
        print(f"{index:02d}. POST {event['type']:<15} -> HTTP {status}")
        if status != 202:
            print_error_body(body)
            return 1

    status, body = get_json(
        f"{base_url}/matches/{match_id}/summary",
        args.timeout_seconds,
    )
    print()
    print(f"GET /matches/{match_id}/summary -> HTTP {status}")
    if status != 200:
        print_error_body(body)
        return 1

    print()
    print("Computed summary:")
    print(json.dumps(body, indent=2, ensure_ascii=False))

    validate_summary(body, expected_score)
    print()
    print("Demo OK: summary is FINISHED and matches the generated score.")
    return 0


def parse_args():
    parser = argparse.ArgumentParser(
        description="Send a random but valid match event sequence to summarize-match."
    )
    parser.add_argument(
        "--base-url",
        default="http://localhost:3000",
        help="summarize-match base URL, default: http://localhost:3000",
    )
    parser.add_argument(
        "--match-id",
        help="UUID to reuse for the demo. By default a fresh UUID is generated.",
    )
    parser.add_argument(
        "--seed",
        type=int,
        help="Random seed for reproducible demos.",
    )
    parser.add_argument(
        "--timeout-seconds",
        type=float,
        default=5.0,
        help="HTTP timeout in seconds, default: 5",
    )
    parser.add_argument(
        "--skip-health",
        action="store_true",
        help="Do not wait for GET /health before sending events.",
    )
    return parser.parse_args()


def build_demo_events(match_id, rng):
    events = []
    score = {"home": 0, "away": 0}
    active_players = {
        HOME_TEAM_ID: HOME_PLAYERS[:],
        AWAY_TEAM_ID: AWAY_PLAYERS[:],
    }
    bench_players = {
        HOME_TEAM_ID: HOME_BENCH[:],
        AWAY_TEAM_ID: AWAY_BENCH[:],
    }

    def add(event_type, minute, second, payload):
        event = envelope(match_id, event_type, minute, second, payload)
        events.append(event)
        return event["eventId"]

    def random_team():
        return rng.choice(
            [
                (HOME_TEAM_ID, "home"),
                (AWAY_TEAM_ID, "away"),
            ]
        )

    def random_outfield(team_id):
        return rng.choice(active_players[team_id][1:])

    def random_opponent(team_id):
        opponent_id = AWAY_TEAM_ID if team_id == HOME_TEAM_ID else HOME_TEAM_ID
        return random_outfield(opponent_id)

    add(
        "MATCH_STARTED",
        0,
        0,
        {
            "homeTeam": team_payload(HOME_TEAM_ID, HOME_PLAYERS),
            "awayTeam": team_payload(AWAY_TEAM_ID, AWAY_PLAYERS),
            "scheduledDurationMinutes": 40,
        },
    )

    goal_count = rng.randint(1, 3)
    playable_goal_minutes = list(range(12, 20)) + list(range(21, 33))
    goal_minutes = rng.sample(playable_goal_minutes, goal_count)
    for minute in goal_minutes:
        team_id, label = random_team()
        scorer = random_outfield(team_id)
        possible_assists = [
            player for player in active_players[team_id][1:] if player != scorer
        ]
        add(
            "GOAL_SCORED",
            minute,
            rng.randint(0, 59),
            {
                "scoringTeamId": team_id,
                "scorerId": scorer,
                "assistId": rng.choice(possible_assists),
                "isOwnGoal": False,
            },
        )
        score[label] += 1

    action_minutes = [minute for minute in range(2, 38) if minute not in goal_minutes]
    random_actions = []
    random_actions.extend(["PASS_ATTEMPTED"] * rng.randint(3, 6))
    random_actions.extend(["SHOT_ATTEMPTED"] * rng.randint(2, 5))
    random_actions.extend(["FOUL_COMMITTED"] * rng.randint(2, 4))
    random_actions.extend(["YELLOW_CARD"] * rng.randint(1, 3))
    random_actions.extend(["SUBSTITUTION"] * rng.randint(1, 3))
    rng.shuffle(random_actions)

    used_foul_ids = []
    used_shot_ids = []
    selected_minutes = rng.sample(action_minutes, len(random_actions))

    for action_type, minute in zip(random_actions, selected_minutes):
        second = rng.randint(0, 59)
        team_id, _ = random_team()

        if action_type == "PASS_ATTEMPTED":
            passer = random_outfield(team_id)
            receivers = [
                player for player in active_players[team_id][1:] if player != passer
            ]
            add(
                "PASS_ATTEMPTED",
                minute,
                second,
                {
                    "passerId": passer,
                    "teamId": team_id,
                    "receiverId": rng.choice(receivers) if receivers else None,
                    "succeeded": rng.choice([True, True, True, False]),
                },
            )
        elif action_type == "SHOT_ATTEMPTED":
            shot_id = add(
                "SHOT_ATTEMPTED",
                minute,
                second,
                {
                    "shooterId": random_outfield(team_id),
                    "teamId": team_id,
                    "onTarget": rng.choice([True, False]),
                    "outcome": rng.choice(["SAVED", "BLOCKED", "WIDE", "POST"]),
                },
            )
            used_shot_ids.append((shot_id, team_id, minute, second))
            if rng.choice([True, False]):
                keeper_team_id = AWAY_TEAM_ID if team_id == HOME_TEAM_ID else HOME_TEAM_ID
                add(
                    "SAVE_MADE",
                    minute,
                    min(second + 1, 59),
                    {
                        "keeperId": active_players[keeper_team_id][0],
                        "keeperTeamId": keeper_team_id,
                        "relatedShotEventId": shot_id,
                    },
                )
        elif action_type == "FOUL_COMMITTED":
            foul_id = add(
                "FOUL_COMMITTED",
                minute,
                second,
                {
                    "playerId": random_outfield(team_id),
                    "teamId": team_id,
                    "againstPlayerId": random_opponent(team_id),
                },
            )
            used_foul_ids.append((foul_id, team_id))
        elif action_type == "YELLOW_CARD":
            related_foul = rng.choice(used_foul_ids) if used_foul_ids else (None, team_id)
            card_team_id = related_foul[1]
            add(
                "YELLOW_CARD",
                minute,
                second,
                {
                    "playerId": random_outfield(card_team_id),
                    "teamId": card_team_id,
                    "relatedFoulEventId": related_foul[0],
                    "cardNumber": rng.choice([1, 1, 2]),
                },
            )
        elif action_type == "SUBSTITUTION":
            if bench_players[team_id]:
                player_out = random_outfield(team_id)
                player_in = bench_players[team_id].pop(0)
                active_players[team_id].remove(player_out)
                active_players[team_id].append(player_in)
                add(
                    "SUBSTITUTION",
                    minute,
                    second,
                    {
                        "teamId": team_id,
                        "playerOutId": player_out,
                        "playerInId": player_in,
                    },
                )

    add(
        "MATCH_PAUSED",
        20,
        0,
        {
            "reason": "HALF_TIME",
        },
    )
    add(
        "MATCH_RESUMED",
        20,
        30,
        {
            "reason": "HALF_TIME_END",
        },
    )

    add(
        "MATCH_FINISHED",
        40,
        0,
        {
            "finalScore": score,
        },
    )

    events.sort(key=lambda event: (event["matchTime"]["minute"], event["matchTime"]["second"]))
    return events, score


def print_sent_events(events):
    print("Events sent to the API:")
    for index, event in enumerate(events, start=1):
        print(f"  {index:02d}. {format_event_for_print(event)}")
    print()


def print_expected_summary_events(events):
    visible_types = {"GOAL_SCORED", "YELLOW_CARD", "RED_CARD", "SUBSTITUTION"}
    visible_events = [event for event in events if event["type"] in visible_types]

    print("Expected events in the final summary:")
    if not visible_events:
        print("  None")
        return

    for index, event in enumerate(visible_events, start=1):
        print(f"  {index:02d}. {format_event_for_print(event)}")


def format_event_for_print(event):
    event_type = event["type"]
    payload = event["payload"]
    time_label = format_match_time(event["matchTime"])

    if event_type == "GOAL_SCORED":
        return (
            f"{time_label} GOAL_SCORED team={payload['scoringTeamId']} "
            f"scorer={payload['scorerId']} assist={payload.get('assistId')}"
        )
    if event_type == "YELLOW_CARD":
        return (
            f"{time_label} YELLOW_CARD team={payload['teamId']} "
            f"player={payload['playerId']} cardNumber={payload['cardNumber']}"
        )
    if event_type == "RED_CARD":
        return f"{time_label} RED_CARD team={payload['teamId']} player={payload['playerId']}"
    if event_type == "SUBSTITUTION":
        return (
            f"{time_label} SUBSTITUTION team={payload['teamId']} "
            f"out={payload['playerOutId']} in={payload['playerInId']}"
        )
    if event_type == "PASS_ATTEMPTED":
        return (
            f"{time_label} PASS_ATTEMPTED team={payload['teamId']} "
            f"passer={payload['passerId']} receiver={payload.get('receiverId')} "
            f"succeeded={payload['succeeded']}"
        )
    if event_type == "SHOT_ATTEMPTED":
        return (
            f"{time_label} SHOT_ATTEMPTED team={payload['teamId']} "
            f"shooter={payload['shooterId']} outcome={payload['outcome']}"
        )
    if event_type == "SAVE_MADE":
        return (
            f"{time_label} SAVE_MADE team={payload['keeperTeamId']} "
            f"keeper={payload['keeperId']}"
        )
    if event_type == "FOUL_COMMITTED":
        return (
            f"{time_label} FOUL_COMMITTED team={payload['teamId']} "
            f"player={payload['playerId']}"
        )
    if event_type == "MATCH_FINISHED":
        score = payload["finalScore"]
        return f"{time_label} MATCH_FINISHED finalScore={score['home']}-{score['away']}"

    return f"{time_label} {event_type}"


def format_match_time(match_time):
    minute = str(match_time["minute"]).rjust(2, "0")
    second = str(match_time["second"]).rjust(2, "0")
    return f"{minute}:{second}"


def envelope(match_id, event_type, minute, second, payload):
    return {
        "eventId": str(uuid.uuid4()),
        "matchId": match_id,
        "type": event_type,
        "occurredAt": f"2026-04-28T12:{minute:02d}:{second:02d}Z",
        "matchTime": {
            "minute": minute,
            "second": second,
            "period": "FIRST_HALF" if minute < 20 else "SECOND_HALF",
        },
        "payload": payload,
    }


def team_payload(team_id, players):
    return {
        "teamId": team_id,
        "startingPlayers": [
            {"playerId": player_id, "isGoalkeeper": index == 0}
            for index, player_id in enumerate(players)
        ],
    }


def wait_for_health(base_url, timeout_seconds):
    deadline = time.monotonic() + timeout_seconds
    last_error = None

    while time.monotonic() < deadline:
        try:
            status, _ = get_json(f"{base_url}/health", timeout_seconds=1.0)
            if status == 200:
                print("GET /health -> HTTP 200")
                return
            last_error = f"unexpected HTTP {status}"
        except Exception as exc:  # noqa: BLE001 - CLI demo should print any connection issue.
            last_error = str(exc)
        time.sleep(0.3)

    raise SystemExit(f"Service is not ready at {base_url}: {last_error}")


def post_json(url, payload, timeout_seconds):
    data = json.dumps(payload).encode("utf-8")
    request = urllib.request.Request(
        url,
        data=data,
        headers={"Content-Type": "application/json"},
        method="POST",
    )
    return request_json(request, timeout_seconds)


def get_json(url, timeout_seconds):
    request = urllib.request.Request(url, method="GET")
    return request_json(request, timeout_seconds)


def request_json(request, timeout_seconds):
    try:
        with urllib.request.urlopen(request, timeout=timeout_seconds) as response:
            body = response.read().decode("utf-8")
            return response.status, parse_body(body)
    except urllib.error.HTTPError as error:
        body = error.read().decode("utf-8")
        return error.code, parse_body(body)


def parse_body(body):
    if not body:
        return None
    try:
        return json.loads(body)
    except json.JSONDecodeError:
        return body


def print_error_body(body):
    print("Request failed. Response body:")
    if isinstance(body, (dict, list)):
        print(json.dumps(body, indent=2, ensure_ascii=False))
    else:
        print(body)


def validate_summary(summary, expected_score):
    score = summary.get("score", {})
    if summary.get("status") != "FINISHED":
        raise SystemExit("Summary validation failed: status is not FINISHED.")
    if score.get("home") != expected_score["home"]:
        raise SystemExit("Summary validation failed: home score mismatch.")
    if score.get("away") != expected_score["away"]:
        raise SystemExit("Summary validation failed: away score mismatch.")
    if not summary.get("goals"):
        raise SystemExit("Summary validation failed: no goals in summary.")
    if not summary.get("cards") and not summary.get("substitutions"):
        raise SystemExit("Summary validation failed: no cards or substitutions.")


if __name__ == "__main__":
    sys.exit(main())
