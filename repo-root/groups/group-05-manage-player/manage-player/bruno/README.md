# Bruno - Manage Player API

Ouvre ce dossier `bruno/` avec Bruno.

Selectionne l'environnement `Local`.

Variables utiles :

- `apiKey` : `dev-api-key`.
- `playerId` : a remplacer par l'id retourne par `Players / Create Player`.
- `teamId` : a remplacer par un id d'equipe existant pour tester les evenements d'equipe.

Les URLs sont ecrites en dur sur `http://localhost:8081` pour eviter les erreurs `Invalid URL` si l'environnement Bruno n'est pas selectionne.
Si ton API tourne sur `8080`, remplace `8081` par `8080` dans les requetes.

Les routes admin repondent `404` tant que l'application n'est pas lancee avec `app.admin.enabled=true`.
