# Value Object — MatchStatus

**Résumé métier**

`MatchStatus` représente l'état courant d'un match dans le read model. Il est dérivé de l'état de l'aggregate à l'instant de la requête `GET /matches/{matchId}/summary`.

**Variantes**

| Valeur JSON | Signification |
|---|---|
| `"NOT_STARTED"` | Aucun événement reçu (ne devrait pas apparaître en réponse HTTP car → 404) |
| `"IN_PROGRESS"` | `MATCH_STARTED` reçu, match en cours |
| `"PAUSED"` | Dernier événement reçu : `MATCH_PAUSED` |
| `"FINISHED"` | `MATCH_FINISHED` reçu — état terminal normal |
| `"CANCELLED"` | `MATCH_CANCELLED` reçu — état terminal |
| `"FORFEITED"` | `MATCH_FORFEITED` reçu — état terminal |

**Invariants**

- Les états `FINISHED`, `CANCELLED` et `FORFEITED` sont **terminaux** : aucun événement de jeu ne peut être traité après.
- `PAUSED` est transitoire : doit être suivi d'un `MATCH_RESUMED` avant tout autre événement de jeu.
- La priorité de calcul dans `to_summary()` : `Cancelled > Forfeited > Finished > Paused > InProgress > NotStarted`.

**Format JSON attendu**

```json
"IN_PROGRESS"
```

Sérialisation Rust : `#[serde(rename_all = "SCREAMING_SNAKE_CASE")]`.

**Schéma**

Présent dans le JSON retourné par `GET /matches/{matchId}/summary` (champ `status`). Pas de schéma JSON Schema dédié — validé implicitement par les tests d'intégration HTTP.

**Constructeur domaine**

Calculé dans `MatchAggregate::to_summary()` par inspection des champs booléens de l'aggregate (`cancelled`, `forfeited`, `finished`, `paused`, `started`).

**Références de code**

- Définition: `summarize-match/src/domain/summary.rs`
- Calcul: `MatchAggregate::to_summary()` — voir `summarize-match/src/domain/aggregate.rs`
- Exposé via: `MatchSummary.status` → `GET /matches/{matchId}/summary`
