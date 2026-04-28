# Match Event Catalog

Ce dossier separe le contrat d'entree des events consommes et le contrat de sortie des events produits par `resume-match`.

## Inbound contract

Contrat d'entree du bounded context `resume-match` (physiquement dans `inbound/`).

### Reference commune

- [BaseEvent](inbound/BaseEvent.md) - enveloppe commune et regles globales.

### Events consommes

- [MATCH_STARTED](inbound/MATCH_STARTED.md) - coup d'envoi et composition initiale.
- [MATCH_PAUSED](inbound/MATCH_PAUSED.md) - interruption temporaire.
- [MATCH_RESUMED](inbound/MATCH_RESUMED.md) - reprise du match.
- [MATCH_FINISHED](inbound/MATCH_FINISHED.md) - cloture du match.
- [MATCH_FORFEITED](inbound/MATCH_FORFEITED.md) - forfait.
- [MATCH_CANCELLED](inbound/MATCH_CANCELLED.md) - annulation.
- [GOAL_SCORED](inbound/GOAL_SCORED.md) - but valide.
- [GOAL_CANCELLED](inbound/GOAL_CANCELLED.md) - but annule.
- [SHOT_ATTEMPTED](inbound/SHOT_ATTEMPTED.md) - tentative de tir.
- [SAVE_MADE](inbound/SAVE_MADE.md) - arret du gardien.
- [PASS_ATTEMPTED](inbound/PASS_ATTEMPTED.md) - tentative de passe.
- [FOUL_COMMITTED](inbound/FOUL_COMMITTED.md) - faute.
- [YELLOW_CARD](inbound/YELLOW_CARD.md) - carton jaune.
- [RED_CARD](inbound/RED_CARD.md) - carton rouge.
- [SUBSTITUTION](inbound/SUBSTITUTION.md) - remplacement.

## Outbound contract

Contrat de sortie du bounded context `resume-match` (physiquement dans `outbound/`).

### Events produits

- [PlayerData](outbound/PlayerData.md) — résumé des statistiques par joueur produit après calcul du match.

## Convention de conception

- Un fichier par event de transport.
- Un seul fichier commun pour l'enveloppe `BaseEvent`.
- Les regles de cohérence transverses sont centralisees dans `BaseEvent.md`.
- `PASS_SUCCEEDED` n'est pas documente comme event distinct: le transport garde `PASS_ATTEMPTED` avec `succeeded=true`.

## Frontiere DDD

L'ACL inbound traduit les events du contexte amont vers le modele metier. Si un jour le service publie un resume ou des anomalies, ces sorties devront etre documentees ici dans la section outbound et referencees dans `service-declaration.json`.