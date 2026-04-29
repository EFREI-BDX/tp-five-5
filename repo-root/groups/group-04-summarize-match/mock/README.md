# Demo summarize-match

Ce dossier contient une demo qui simule le contexte amont `record-match`.
Le script Python genere des events valides, les envoie a l'API, puis lit le
resume calcule du match.

## Lancer le service

Depuis la racine du groupe :

```powershell
docker compose up --build
```

## Lancer la demo

Dans un autre terminal :

```powershell
py mock\demo_summarize_match.py --base-url http://localhost:3000
```

Pour rendre le hasard reproductible :

```powershell
py mock\demo_summarize_match.py --base-url http://localhost:3000 --seed 42
```

Le script affiche chaque event poste sur `/events`, puis le resultat de
`GET /matches/{matchId}/summary`.

## Visualiser un summary

Ouvrir le fichier suivant dans un navigateur :

```text
mock/summary_visualiser.html
```

Renseigner le `matchId` affiche par le script Python, puis cliquer sur `Fetch`.
Si le navigateur bloque l'appel a l'API locale, copier le JSON affiche par le
script Python dans la zone `JSON`, puis cliquer sur `Render`.
