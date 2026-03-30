# Player

**Résumé métier**

Permettre la gestion des joueurs dans le système ERP du centre de foot à 5.

**Attributs**

- id - *VO Id*
- firstName - *VO FirstName*
- lastName - *VO LastName*
- email - *VO Email*
- phone - *VO Phone*
- level - *VO Level*
- gender - *VO Gender*
- birthDate - *VO BirthDate*
- height - *VO Height*
- teamIds - *List of TeamId*
- statistics - *VO PlayerStatistics*
- status - *VO Status*
- createdAt - *VO DateTime*
- updatedAt - *VO DateTime*

**Invariants**

- id doit être non vide
- firstName doit être non vide
- lastName doit être non vide
- email doit être non vide
- email doit respecter un format valide
- phone doit être non vide
- phone doit respecter un format valide
- gender doit être non vide
- gender doit être égal à `homme`, `femme`, `non binaire`, `non spécifié`
- birthDate doit être non vide
- birthDate doit respecter le format `JJ/MM/AAAA`
- height doit être strictement positive
- teamIds peut être vide
- teamIds ne doit pas contenir de doublons
- statistics doit être non vide
- createdAt doit être non vide
- updatedAt doit être non vide
- updatedAt doit être supérieur ou égal à createdAt
- level doit être non vide
- level doit appartenir à une liste de niveaux autorisés

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/Player.schema.json`
- **Fixture valide** : `tests/fixtures/Player.valid.json`
- **Fixture invalide** : `tests/fixtures/Player.invalid.json`

**Tests minimaux attendus**

- Vérifier qu’un player valide est accepté
- Vérifier qu’un id vide est rejeté
- Vérifier qu’un email invalide est rejeté
- Vérifier qu’un phone invalide est rejeté
- Vérifier qu’une birthDate future est rejetée
- Vérifier qu’une height <= 0 est rejetée
- Vérifier que teamIds sans doublons est accepté
- Vérifier que teamIds avec doublons est rejeté
- Vérifier que wins > matchesPlayed est rejeté
- Vérifier qu’un status hors enum est rejeté
- Vérifier que updatedAt < createdAt est rejeté