# Value Object — MatchResult

**Résumé métier**

`MatchResult` exprime le résultat d'un match du point de vue d'un joueur : victoire, défaite ou match nul. Calculé à partir du score final et de l'équipe du joueur.

**Variantes**

| Valeur JSON | Signification |
|---|---|
| `"Win"` | L'équipe du joueur a marqué plus de buts que l'adversaire |
| `"Loss"` | L'équipe du joueur a marqué moins de buts que l'adversaire |
| `"Draw"` | Score à égalité |

**Invariants**

- Les trois variantes sont exhaustives — tout score produit exactement un `MatchResult`.
- Calculé exclusivement lors du traitement de `MATCH_FINISHED` ou `MATCH_FORFEITED`.
- Un joueur de l'équipe domicile et un joueur de l'équipe visiteuse ont des résultats symétriques (Win ↔ Loss, Draw ↔ Draw).

**Format JSON attendu**

```json
"Win"
```

Sérialisation Rust : `#[derive(Serialize)]` sans attribut → les variantes s'écrivent telles quelles (`Win`, `Loss`, `Draw`).

**Schéma**

Expose par les read models de statistiques joueur :
```json
"result": { "type": "string", "enum": ["Win", "Loss", "Draw"] }
```

La route `GET /matches/{matchId}/players/stats` expose le meme contrat de valeurs dans le read model `PlayerMatchStats`.

**Constructeur domaine**

Calcule via comparaison du score final quand un read model de statistiques joueur est produit.

**Références de code**

- Définition: `summarize-match/src/domain/summary.rs`
- Utilisation cible: `PlayerMatchStats.result`
