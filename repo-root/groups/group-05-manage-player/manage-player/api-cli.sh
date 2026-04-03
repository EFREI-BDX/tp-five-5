#!/usr/bin/env bash

set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080}"
API_KEY="${API_KEY:-dev-api-key}"

print_usage() {
    cat <<'EOF'
Usage:
  ./api-cli.sh help
  ./api-cli.sh health
  ./api-cli.sh players get <player-id>
  ./api-cli.sh players create <firstName> <lastName> <email> <phone> <gender> <birthDate> <height>
  ./api-cli.sh players update <player-id> <firstName|-> <lastName|-> <email|-> <phone|-> <gender|-> <birthDate|-> <height|->
  ./api-cli.sh players delete <player-id>
  ./api-cli.sh players stats <player-id> <matchesPlayed> <goalsScored> <assists> <wins>
  ./api-cli.sh demo

Environment:
  BASE_URL=http://localhost:8080
  API_KEY=dev-api-key

Examples:
  ./api-cli.sh players create Jean Dupont jean@example.com +33612345678 homme 15/06/1995 178.5
  ./api-cli.sh players stats 11111111-1111-1111-1111-111111111111 10 4 2 6
EOF
}

pretty_print() {
    if command -v jq >/dev/null 2>&1; then
        jq .
    else
        cat
    fi
}

request() {
    local method="$1"
    local path="$2"
    local body="${3:-}"

    printf '\n[%s] %s%s\n' "$method" "$BASE_URL" "$path"

    if [[ -n "$body" ]]; then
        curl -sS -X "$method" \
            -H "Content-Type: application/json" \
            -H "X-API-KEY: $API_KEY" \
            "$BASE_URL$path" \
            -d "$body" | pretty_print
    else
        if [[ "$path" == "/health" ]]; then
            curl -sS -X "$method" "$BASE_URL$path" | pretty_print
        else
            curl -sS -X "$method" -H "X-API-KEY: $API_KEY" "$BASE_URL$path" | pretty_print
        fi
    fi
    printf '\n'
}

extract_json_field() {
    local field="$1"
    python3 -c 'import json,sys; print(json.load(sys.stdin).get(sys.argv[1], ""))' "$field"
}

health() {
    request GET "/health"
}

player_get() {
    request GET "/players/$1"
}

player_create() {
    request POST "/players" "$(printf '{"firstName":"%s","lastName":"%s","email":"%s","phone":"%s","gender":"%s","birthDate":"%s","height":%s}' "$1" "$2" "$3" "$4" "$5" "$6" "$7")"
}

player_update() {
    local body="{}"

    body="$(python3 - "$2" "$3" "$4" "$5" "$6" "$7" "$8" <<'PY'
import json, sys
keys = ["firstName", "lastName", "email", "phone", "gender", "birthDate", "height"]
values = sys.argv[1:]
payload = {}
for key, value in zip(keys, values):
    if value != "-":
        payload[key] = float(value) if key == "height" else value
print(json.dumps(payload))
PY
)"

    request PUT "/players/$1" "$body"
}

player_delete() {
    request DELETE "/players/$1"
}

player_stats() {
    request POST "/players/$1/statistics" "$(printf '{"matchesPlayed":%s,"goalsScored":%s,"assists":%s,"wins":%s}' "$2" "$3" "$4" "$5")"
}

demo() {
    local player_response
    local player_id

    printf 'Running demo against %s\n' "$BASE_URL"

    health

    player_response="$(curl -sS -X POST -H "Content-Type: application/json" -H "X-API-KEY: $API_KEY" "$BASE_URL/players" -d '{"firstName":"Jean","lastName":"Dupont","email":"jean.dupont@example.com","phone":"+33612345678","gender":"homme","birthDate":"15/06/1995","height":178.5}')"
    printf '\n[POST] %s/players\n' "$BASE_URL"
    printf '%s\n' "$player_response" | pretty_print
    player_id="$(printf '%s' "$player_response" | extract_json_field id)"

    player_stats "$player_id" 10 4 2 6
    player_get "$player_id"

    printf '\nDemo ids:\n'
    printf '  player_id=%s\n' "$player_id"
}

main() {
    local resource="${1:-help}"

    case "$resource" in
        help|-h|--help)
            print_usage
            ;;
        health)
            health
            ;;
        players)
            case "${2:-}" in
                get) player_get "$3" ;;
                create) player_create "$3" "$4" "$5" "$6" "$7" "$8" "$9" ;;
                update) player_update "$3" "$4" "$5" "$6" "$7" "$8" "$9" "${10:-}" ;;
                delete) player_delete "$3" ;;
                stats) player_stats "$3" "$4" "$5" "$6" "$7" ;;
                *) print_usage; exit 1 ;;
            esac
            ;;
        demo)
            demo
            ;;
        *)
            print_usage
            exit 1
            ;;
    esac
}

main "$@"
