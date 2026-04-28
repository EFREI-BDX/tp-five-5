# Outbound events

Ce dossier documente les events produits par `resume-match`.

### Reference commune

- [BaseEvent](BaseEvent.md) - enveloppe commune pour les events produits.

### Events produits

- [PlayerData](PlayerData.md) — résumé des statistiques par joueur produit après calcul du match.

Remarques :

- Les events produits suivent l'enveloppe `BaseEvent` et placent la charge métier dans `payload`.
- Si vous ajoutez de nouveaux events produits, créez un fichier par event et mettez à jour `service-declaration.json` (section `eventsProduced`) et `tests/schemas` si nécessaire.
