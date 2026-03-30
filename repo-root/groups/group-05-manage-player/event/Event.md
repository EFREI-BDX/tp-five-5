# Events

## PlayerNameUpdated

**Résumé métier**

Événement émis lorsqu’un joueur modifie son prénom ou son nom.

**Payload**

- playerId - *String*
- fullName - *String* (concaténation de firstName + lastName)

**Déclenchement**

- Modification de `firstName` ou `lastName`

---

## PlayerDeleted

**Résumé métier**

Événement émis lorsqu’un joueur est supprimé.

**Payload**

- playerId - *String*

**Déclenchement**

- Suppression d’un player

---

## TeamNameUpdated

**Résumé métier**

Événement reçu lorsqu’une équipe change de nom.

**Payload**

- teamId - *String*
- name - *String*

**Réception**

- Mise à jour du nom d’une team

---

## PlayerJoinedTeam

**Résumé métier**

Événement reçu lorsqu’un joueur rejoint une équipe.

**Payload**

- teamId - *String*

**Réception**

- Ajout d’un player dans une team

---

## PlayerLeftTeam

**Résumé métier**

Événement reçu lorsqu’un joueur quitte une équipe.

**Payload**

- teamId - *String*

**Réception**

- Retrait d’un player d’une team

---

## MatchPlayerEvent

**Résumé métier**

Événement reçu lorsqu’un match envoie une information concernant un joueur.

**Payload**

- teamId - *String*
- data - *Object* (information liée au joueur)

**Réception**

- Réception d’une donnée provenant d’un match