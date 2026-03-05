# Contexts Map -

**Bounded contexts identifiés**
- **Player Management** - Gestion des joueurs inscrits dans le système.

**Responsabilités et frontières (une ligne chacune)**
- Player Management - Création, mise à jour et suppression des profils joueurs.
- Player Management - Gestion des informations personnelles (nom, email, téléphone, niveau).
- Player Management - Association des joueurs aux équipes ou réservations.
- Player Management - Consultation des informations et du statut des joueurs.


**APIs produits**
- `POST /players` - créer un nouveau joueur.
- `GET /players` - récupérer la liste des joueurs.
- `GET /players/{id}` - récupérer les informations d’un joueur.
- `PUT /players/{id}` - modifier les informations d’un joueur.
- `DELETE /players/{id}` - supprimer un joueur.
