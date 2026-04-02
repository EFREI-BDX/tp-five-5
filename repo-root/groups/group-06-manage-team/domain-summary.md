# Domain Summary — Manage Team

**Objectif métier**

Création, modification et suppression d'une équipe de Five.

**Ubiquitous language**

- **Team** : une équipe de five, constituée de 5 joueurs.
- **Player** : un joueur de five, qui appartient à une équipe.
- **Label** : une chaine de caractères qui sert de dénomination pour nommer une équipe
- **Tag** : une chaine de 3 caractères qui correspond à un acronyme ou un diminutif.
- **Name** : un prénom et un nom de famille qui servent à identifier un joueur
- **Id** : un identifiant unique pour une équipe ou un joueur, généré automatiquement lors de la création d'une nouvelle
  instance de Team ou Player.
- **TeamLeader** : un joueur désigné comme leader de l'équipe, responsable de la coordination et de la communication au
  sein de l'équipe.
- **State** : un état qui indique si l'équipe est active, incomplète ou dissoute.

**Invariants métier**

- Une équipe doit être constituée de 5 joueurs au minimum.
- Une équipe doit avoir un tag unique.
- Une équipe doit avoir un label unique.
- Un joueur doit appartenir à une seule équipe.
- Une équipe doit avoir un de ses joueurs désigné comme teamLeader.


