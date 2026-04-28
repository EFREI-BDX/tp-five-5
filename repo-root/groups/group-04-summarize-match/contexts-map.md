# Contexts Map - Match Summary

## Bounded contexts identifiés

- **record-match** - contexte amont qui publie les events de match.
- **resume-match** - contexte courant qui consomme le flux, reconstruit la timeline et produit le resume metier.
- **reporting / ranking / statistics** - contextes descendants qui exploitent la synthese de match.

## Responsabilites et frontieres

- `record-match` est la source de verite des events de jeu.
- `resume-match` ne reinterprete pas le jeu; il valide, ordonne et agrege la timeline.
- Les incoherences entre timeline et score final sont des anomalies de domaine, pas des erreurs techniques.
- La traduction du contrat d'events externes vers le modele interne appartient a un ACL inbound.

## Architecture hexagonale cible

- **Adapter entrant** : consumer d'events du contexte `record-match`.
- **Port entrant** : cas d'usage de resume du match.
- **Noyau metier** : regles de cohérence, score, sanctions, remplacements, statistiques derivees.
- **Adapters sortants** : aucun event de sortie documente pour l'instant.

## APIs produits

- Aucun endpoint metier HTTP pour le moment.
- `GET /health` - controle technique.

## Events produits

- Aucun event produit actuellement.
