# Mockups Possibles

Ces mockups sont des propositions d'écrans pour présenter le service `manage-player`. Ils ne correspondent pas à une interface déjà implémentée ; ils servent à rendre les cas d'usage lisibles pendant la présentation.

## 1. Liste des Joueurs

Objectif : montrer rapidement les joueurs enregistrés, leur statut et leurs statistiques principales.

```text
┌────────────────────────────────────────────────────────────────────────────┐
│ Players                                                       [+ Nouveau] │
├────────────────────────────────────────────────────────────────────────────┤
│ Recherche...                     Statut [Tous v]       Trier [Nom v]       │
├──────────────┬──────────────┬──────────────┬────────────┬───────┬─────────┤
│ Joueur       │ Email        │ Téléphone    │ Statut     │ Match │ Actions │
├──────────────┼──────────────┼──────────────┼────────────┼───────┼─────────┤
│ Lionel Messi │ lionel...    │ +33610000001 │ actif      │ 60    │ Voir    │
│ Kylian Mb... │ kylian...    │ +33610000003 │ actif      │ 54    │ Voir    │
│ Neymar Jr    │ neymar...    │ +33610000008 │ supprimé   │ 42    │ Voir    │
└──────────────┴──────────────┴──────────────┴────────────┴───────┴─────────┘
```

Routes illustrées :

- `GET /players` pour charger le tableau ;
- `DELETE /players/{id}` depuis une action de suppression ;
- `POST /players` depuis le bouton de création.

## 2. Fiche Joueur

Objectif : présenter le détail complet d'un joueur et séparer clairement profil, statistiques et équipes.

```text
┌────────────────────────────────────────────────────────────────────────────┐
│ Lionel Messi                                      actif     [Modifier]     │
│ lionel.messi@example.com  +33610000001  24/06/1987  170 cm                │
├───────────────────────────────┬────────────────────────────────────────────┤
│ Statistiques                  │ Équipes                                    │
│ ┌────────┬──────┬────────┐    │ ┌────────────────────────────────────────┐ │
│ │ Matchs │ 60   │        │    │ │ 550e8400-e29b-41d4-a716-446655440000  │ │
│ │ Buts   │ 42   │        │    │ │ 9d8a5fbb-8c71-4c0e-a6f0-5f9b6a4b5d12  │ │
│ │ Passes │ 28   │        │    │ └────────────────────────────────────────┘ │
│ │ V/N/D  │39/10/11       │    │                                            │
│ │ MVP    │ 18   │        │    │                                            │
│ └────────┴──────┴────────┘    │                                            │
├───────────────────────────────┴────────────────────────────────────────────┤
│ [Mettre à jour les stats]                         [Supprimer logiquement] │
└────────────────────────────────────────────────────────────────────────────┘
```

Routes illustrées :

- `GET /players/{id}` ;
- `PUT /players/{id}` ;
- `POST /players/{id}/statistics` ;
- `DELETE /players/{id}`.

## 3. Formulaire de Création ou Modification

Objectif : montrer les règles de validation côté utilisateur.

```text
┌────────────────────────────────────────────────────────────────────────────┐
│ Nouveau joueur                                                             │
├───────────────────────────────┬────────────────────────────────────────────┤
│ Prénom                         │ Jean                                       │
│ Nom                            │ Dupont                                     │
│ Email                          │ jean.dupont@example.com                    │
│ Téléphone                      │ +33612345678                               │
│ Date de naissance              │ 15/06/1995                                 │
│ Genre                          │ homme                                      │
│ Taille                         │ 178.5                                      │
├───────────────────────────────┴────────────────────────────────────────────┤
│ [Annuler]                                                    [Enregistrer] │
└────────────────────────────────────────────────────────────────────────────┘
```

Validation à afficher :

- champs obligatoires à la création ;
- email au bon format ;
- téléphone français valide ;
- date au format `dd/MM/yyyy` ;
- taille strictement positive.

## 4. Mise à Jour des Statistiques

Objectif : rendre visibles les invariants métier.

```text
┌────────────────────────────────────────────────────────────────────────────┐
│ Statistiques du joueur                                                     │
├────────────────────────────────────────────────────────────────────────────┤
│ Matchs joués     [ 60 ]       Buts            [ 42 ]                       │
│ Passes décisives [ 28 ]       MVP             [ 18 ]                       │
│ Victoires        [ 39 ]       Défaites        [ 10 ]       Nuls [ 11 ]     │
├────────────────────────────────────────────────────────────────────────────┤
│ Règle: victoires + défaites + nuls ne doit pas dépasser matchs joués.       │
│ Règle: MVP ne doit pas dépasser matchs joués.                              │
├────────────────────────────────────────────────────────────────────────────┤
│ [Annuler]                                      [Enregistrer les stats]      │
└────────────────────────────────────────────────────────────────────────────┘
```

Route illustrée :

- `POST /players/{id}/statistics`.

## 5. Timeline Technique pour la Présentation

Objectif : expliquer les interactions sans montrer trop de code.

```text
Client
  │
  │ POST /players
  ▼
PlayerController
  │
  │ délègue
  ▼
PlayerService
  │
  │ valide les règles métier
  ▼
PlayerRepository
  │
  │ CALL fiveplayer.playerCreate(...)
  ▼
MariaDB
```

Ce mockup est utile pour expliquer que le service n'est pas seulement une API : il applique des règles, structure les erreurs et isole la base derrière un repository.
