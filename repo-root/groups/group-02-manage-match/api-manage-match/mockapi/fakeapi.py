from flask import Flask, jsonify, request
import uuid

app = Flask(__name__)

TEAMS = {
    "11111111-1111-1111-1111-111111111111": {
        "id": "11111111-1111-1111-1111-111111111111",
        "label": "Paris Saint-Germain",
        "tag": "PSG"
    },
    "22222222-2222-2222-2222-222222222222": {
        "id": "22222222-2222-2222-2222-222222222222",
        "label": "Olympique de Marseille",
        "tag": "OM"
    }
}

FIELDS = {
    "99999999-9999-9999-9999-999999999999": {
        "id": "99999999-9999-9999-9999-999999999999",
        "label": "Stade de France"
    }
}

@app.route('/teams/<team_id>', methods=['GET'])
def get_team(team_id):
    team = TEAMS.get(str(team_id))

    if team:
        return jsonify(team), 200
    else:
        return jsonify({"error": "Team not found"}), 404

@app.route('/v1//fields/<team_id>', methods=['GET'])
def get_field(team_id):
    team = FIELDS.get(str(team_id))

    if team:
        return jsonify(team), 200
    else:
        return jsonify({"error": "Team not found"}), 404

@app.route('/v1/fields/<field_id>/reservations', methods=['POST'])
def create_reservation(field_id):
    if str(field_id) not in FIELDS:
        return jsonify({"error": "Field not found"}), 404

    data = request.json

    response = {
        "id": field_id,
        "status": "CONFIRMED"
    }

    return jsonify(response), 201

if __name__ == '__main__':
    app.run(port=5000)