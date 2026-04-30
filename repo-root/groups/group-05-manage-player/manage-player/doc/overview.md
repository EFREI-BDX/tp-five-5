# Vue d'Ensemble

## Objectif

`manage-player` gère les profils joueurs du système Efrei Five. Son rôle est de centraliser les informations d'identité, de contact, de statut et de statistiques liées à un joueur.

Le service couvre principalement :

- la création d'un joueur ;
- la consultation d'un joueur par identifiant ;
- la mise à jour partielle du profil ;
- la suppression logique d'un joueur ;
- la mise à jour des statistiques ;
- la synchronisation de la relation joueur-équipe depuis des événements entrants.

## Périmètre Fonctionnel

Le service manipule les données suivantes :

- identité : prénom, nom ;
- contact : email, téléphone ;
- profil : date de naissance, genre, taille ;
- statut : `actif`, `supprimé` ;
- statistiques : matchs joués, buts, passes décisives, victoires, défaites, matchs nuls, MVP ;
- appartenance équipe : liste des identifiants d'équipes associées au joueur.

## Hors Périmètre

Le service ne gère pas directement :

- la création des équipes ;
- les matchs ;
- les réservations ;
- l'authentification utilisateur complète ;
- la publication réelle d'événements sur un bus de messages.

Ces responsabilités appartiennent à d'autres bounded contexts ou sont prévues pour des évolutions futures.

## Position dans le Projet

Dans une architecture par domaines, `manage-player` représente le bounded context des joueurs. Il interagit avec d'autres services via des contrats simples :

- API HTTP pour les opérations directes sur les joueurs ;
- endpoints inbound pour recevoir des événements liés aux équipes ;
- DTO pour stabiliser les échanges entre les couches et avec l'extérieur.

La persistance est assurée par MariaDB. Le code applicatif ne manipule pas directement les tables : il passe par `PlayerRepository`, qui appelle les procédures stockées du schéma `fiveplayer`.
