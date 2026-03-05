# Domain Summary — Character Management

**Objectif métier**  
Gérer la création, le profil et l'inventaire des personnages joueurs afin de permettre la persistance, le partage
d'événements métier et l'interaction avec les autres bounded contexts (Inventory, Equipment, Shop).

**Ubiquitous language**

- **Character** — entité représentant un joueur (id, name, ownerId, metadata).
- **Inventory** — collection d'items possédés par un Character.
- **Item** — ressource identifiée (itemId) avec des attributs `ItemStats`.
- **Value Object (VO)** — objets immuables et sans identité : `Quantity`, `ItemStats`, `EquipmentSlot`.
- **Event** — message métier publié sur le bus (ex. `CharacterCreated`, `ItemAdded`, `ItemEquipped`).
- **Command** — requête synchrone initiée par un client (ex. `CreateCharacter`, `AddItem`).
- **Aggregate** — racine transactionnelle (ici `Character`).

**Principales invariants métier**

- **Character.id** doit être un UUID unique.
- **Character.name** ne peut pas être vide et doit respecter la longueur minimale définie (ex. ≥ 3 caractères).
- **Quantity.quantity** doit être un entier ≥ 1.
- **ItemStats.attack**, **defense** et **weight** doivent être ≥ 0.
- **EquipmentSlot.slot** doit appartenir à l'énumération autorisée (`head`, `body`, `mainHand`, `offHand`, `legs`,
  `feet`).
- Les events publiés doivent contenir un **timestamp** ISO‑8601 et un **traceId** pour corrélation.

**Principaux events produits**

- **CharacterCreated** — payload minimal : `id`, `ownerId`, `name`, `timestamp`.
- **ItemAdded** — payload minimal : `characterId`, `itemId`, `quantity`, `stats`, `timestamp`.
- **ItemEquipped** — payload minimal : `characterId`, `itemId`, `slot`, `timestamp`.
