# Contexts Map — Player Management

**Bounded contexts identifiés**
- **Player Management** - Gestion des joueurs inscrits dans le système.

**Responsabilités et frontières (une ligne chacune)**
- **Player Management** - Création, mise à jour, consultation et suppression logique des profils joueurs.
- **Player Management** - Gestion des informations personnelles des joueurs (nom, prénom, email, téléphone, niveau, genre, date de naissance, taille).
- **Player Management** - Gestion du statut métier des joueurs (`actif`, `inactif`, `supprimé`).
- **Player Management** - Gestion des statistiques des joueurs (matchs joués, buts marqués, passes décisives, victoires).
- **Player Management** - Référencement des liens entre un joueur et les équipes auxquelles il a participé via `teamIds`.
- **Player Management** - Exposition des données joueurs pour consommation par d’autres domaines (réservations, équipes, matchs).

**APIs produits**
- `POST /players` - créer un nouveau joueur.
- `GET /players` - récupérer la liste des joueurs.
- `GET /players/{id}` - récupérer les informations d’un joueur.
- `PUT /players/{id}` - modifier les informations d’un joueur.
- `DELETE /players/{id}` - supprimer logiquement un joueur.
- `POST /players/{id}/statistics` - mettre à jour les statistiques d’un joueur.