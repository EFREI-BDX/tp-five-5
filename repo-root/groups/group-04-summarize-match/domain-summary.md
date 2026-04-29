# Domain Summary - Match Summary

## Objectif metier

Le contexte `resume-match` consomme les events de `record-match` et produit une vision metier coherente du match: score, chronologie, sanctions, remplacements et statistiques derivees.

## Ubiquitous language

- **Match** : rencontre sportive identifiee par un `matchId`.
- **BaseEvent** : enveloppe commune a tous les events du contrat.
- **MatchTimeline** : suite ordonnee des events du match.
- **MatchSummary** : resultat metier calcule a partir de la timeline.
- **Goal** : but valide ou annule.
- **Shot** : tentative de tir, cadre ou non.
- **Pass** : tentative de passe, reussie ou non.
- **Discipline** : fautes, cartons jaunes, cartons rouges.
- **Substitution** : remplacement d'un joueur pendant le match.
- **Anomaly** : incoherence detectee entre la timeline et les invariants metier.

## Regles de domaine

- Les events sont traites dans l'ordre chronologique du match.
- Si l'ordre d'arrivee est incoherent, le domaine trie d'abord par `matchTime`, puis par `occurredAt`.
- Un joueur expulsé par `RED_CARD` ne doit plus porter d'actions de jeu valides ensuite.
- `MATCH_FINISHED` confirme un score qui doit pouvoir etre recalcule a partir de la timeline.
- `PASS_ATTEMPTED` couvre aussi le cas semantique d'une passe reussie via `succeeded=true`.

## Frontiere hexagonale

- **Inbound port** : consommation des events de match venant de `record-match`.
- **Application service** : orchestration du cas d'usage de resume du match.
- **Domain model** : validation des invariants et calcul des derives.
- **Outbound port (publication)** : `DomainEventPublisher` — interface de notification des contextes descendants apres chaque event accepte. Adapter actuel : `NoOpEventPublisher` (infra). A remplacer par un adapter Kafka/AMQP.
- **Outbound port (persistance)** : `MatchRepository` — event store append-only (PostgreSQL via SeaORM, ou InMemory pour tests).
- **Query port** : `MatchQueryService` — lecture du `MatchSummary` reconstruit par replay (CQRS).

### Interpretation operationnelle

- **Inbound port (entree du systeme)**
	- Recoit les messages externes et expose une interface stable au coeur applicatif.
	- Valide le contrat technique (schema, format, event type) et convertit le transport vers des objets internes.
	- Ne porte pas la logique metier profonde.

- **Application service (orchestrateur de use case)**
	- Coordonne le flux: validation d'entree, mapping, appel du domaine, gestion des erreurs de cas d'usage.
	- Maintient le contexte d'execution du cas d'usage (ordre des traitements, enchainement des etapes).
	- N'implemente pas les regles metier de fond qui appartiennent au domaine.

- **Domain model (coeur metier)**
	- Contient les invariants et les regles de coherence fonctionnelle de `resume-match`.
	- Produit les derives metier (score recalcule, timeline coerente, anomalies).
	- Reste decouple des details d'infrastructure (JSON, broker, framework web, etc.).

- **Outbound ports (sorties metier)**
	- Definissent les interfaces de sortie du coeur (publication d'evenements, persistance, integration externe).
	- `MatchRepository` : port de persistance (event store append-only).
	- `DomainEventPublisher` : port de publication vers les contextes descendants (reporting, ranking, statistics). Adapter NoOp par defaut, a remplacer par Kafka/AMQP.

### Regle de dependance (important)

- Les dependances vont de l'exterieur vers l'interieur:
	- `infrastructure -> application -> domain`
- Le domaine ne depend ni de l'infrastructure ni des details de transport.

