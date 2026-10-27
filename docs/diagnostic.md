# Étape 1 : diagnostic du code historique

Les numéros de ligne renvoient aux classes copiées dans `transport-refactored/src/main/java/fr/efrei/transfret`, dans l'état du commit `86b63e1`.

## Rôle et dépendances de chaque classe

| Classe | Rôle | Dépend de |
|---|---|---|
| `Main` | Démo en console : crée des clients, des expéditions, imprime des factures | `Flotte`, `ServiceClient`, `ServiceExpedition`, `Colis` |
| `ServiceClient` | Valide et crée un client, l'enregistre, envoie l'email de bienvenue | `Client`, `Database` (singleton), `EmailService` (statique) |
| `ServiceExpedition` | Tout le cycle de vie d'une expédition : règles d'acceptation, identifiant, prix TTC, délai, date prévue, déplacement du véhicule, statuts, frais d'annulation, notifications | `Database`, `CalculateurPrix`, `AffectationVehicule`, `Utils`, `EmailService`, `SmsService`, `Expedition`, `Vehicule`, `Colis`, `Client` |
| `CalculateurPrix` | Prix HT : tarif par mode, suppléments, remise client, assurance | `Colis` |
| `AffectationVehicule` | Choisit le premier véhicule libre et compatible avec le mode et les colis | `Flotte` (liste statique), `Camion`, `CamionFrigorifique`, `Train`, `Bateau`, `Avion` |
| `Expedition` | Données d'une expédition, totaux (poids, volume, valeur) et génération du texte de facture | `Client`, `Colis`, `Vehicule`, `Utils` |
| `Client` | Données client, type en `String` | aucune |
| `Colis` | Données d'un colis, type en `int` (0 à 3), volume, libellé du type | aucune |
| `IVehicule` | Interface de tous les véhicules (13 méthodes, dont rouler, naviguer, voler, réfrigérer) | `Colis` |
| `Vehicule` | Classe abstraite : capacité, chargement, disponibilité, plein | `IVehicule`, `Colis` |
| `Camion` | Véhicule routier, certification ADR optionnelle | `Vehicule` |
| `CamionFrigorifique` | Camion qui n'accepte que des colis réfrigérés | `Camion`, `Colis` |
| `Train`, `Bateau`, `Avion` | Un véhicule par mode, les méthodes hors sujet lèvent `UnsupportedOperationException` | `Vehicule` |
| `Flotte` | Liste statique des 6 véhicules | les classes de véhicules |
| `Database` | Singleton, stockage en mémoire dans des `Map` statiques, faux SQL affiché en console | `Client`, `Expedition` |
| `Utils` | Fourre-tout : distances en dur, format de date, arrondi, validation d'email | aucune |
| `EmailService` | Envoi simulé, méthodes et journal statiques | aucune |
| `SmsService` | Envoi simulé, journal statique, clé d'API en dur | aucune |

## Violations SOLID

S, responsabilité unique :
- `ServiceExpedition` mélange au moins sept responsabilités (règles d'acceptation l. 16-56, identifiant l. 63-64, TVA l. 76, délai l. 101-127, pilotage du véhicule l. 129-155, notifications l. 204-209, persistance).
- `Expedition.genererFacture()` (l. 52) met la mise en forme d'une facture dans l'objet métier.
- `Utils` regroupe des distances, des dates, un arrondi et une validation d'email.

O, ouvert/fermé :
- `CalculateurPrix` l. 25-41 : `if/else` sur le mode. Un nouveau mode oblige à modifier la classe.
- `CalculateurPrix` l. 44-47 et 50-59 : suppléments et remises codés en dur, impossibles à étendre sans modification.
- `ServiceExpedition.calculerDelai` l. 103 : `switch` sur le mode.
- `AffectationVehicule.trouverVehicule` l. 20-52 : `if` sur le mode et `instanceof` sur chaque classe de véhicule.
- `Colis.getLibelleType` l. 34 : `switch` sur le type.

L, substitution de Liskov :
- `Train.faireLePlein()` l. 31 lève une exception alors que `Vehicule.faireLePlein()` réussit. `ServiceExpedition` l. 138-141 avale l'exception pour contourner le problème.
- `CamionFrigorifique.charger()` l. 13 lève `IllegalArgumentException` là où `Vehicule.charger()` accepte tout colis qui tient. La précondition est renforcée, et `AffectationVehicule` l. 26 doit éviter ce camion à la main.
- `Camion`, `Train`, `Bateau` et `Avion` héritent de `rouler`, `naviguer`, `voler` et `activerRefrigeration` mais en lèvent la moitié en `UnsupportedOperationException`.

I, ségrégation des interfaces :
- `IVehicule` l. 6-32 impose `naviguer`, `voler` et `activerRefrigeration` à tous les véhicules, y compris ceux qui ne savent pas le faire.

D, inversion des dépendances :
- `ServiceExpedition` l. 12-14 crée ses dépendances concrètes (`Database.getInstance()`, `new CalculateurPrix()`, `new AffectationVehicule()`), appelle `EmailService` en statique (l. 205) et fait `new SmsService()` (l. 207).
- `ServiceClient` l. 23-24 appelle `Database.getInstance()` et `EmailService.envoyer` en dur.
- `AffectationVehicule` l. 17 lit directement la liste statique `Flotte.vehicules`.

## Tableau défaut, emplacement, principe violé

| Défaut | Emplacement | Principe violé |
|---|---|---|
| Classe qui fait tout le cycle de vie | `ServiceExpedition` (210 lignes) | S |
| Facture générée par l'entité | `Expedition.genererFacture` l. 52-81 | S |
| Classe fourre-tout | `Utils` | S |
| `if/else` sur le mode pour le tarif | `CalculateurPrix` l. 25-41 | O |
| Suppléments en multiplications en dur | `CalculateurPrix` l. 44-47 | O |
| Remise par `if` sur le type de client | `CalculateurPrix` l. 50-59 | O |
| `switch` sur le mode pour le délai | `ServiceExpedition.calculerDelai` l. 103-125 | O |
| `if` sur le mode pour déplacer le véhicule | `ServiceExpedition.demarrerExpedition` l. 142-149 | O |
| Règles d'acceptation par `if` sur le mode | `ServiceExpedition.creerExpedition` l. 35-46 | O |
| `instanceof` sur chaque véhicule | `AffectationVehicule` l. 21-49 | O, L |
| SMS seulement si `type.equals("PREMIUM")` | `ServiceExpedition.notifier` l. 206 | O |
| SIRET seulement si `ENTREPRISE` | `Expedition.genererFacture` l. 60 | O |
| `switch` sur le type de colis | `Colis.getLibelleType` l. 34 | O |
| `faireLePlein` lève une exception pour le train | `Train` l. 31 | L |
| Exception avalée pour contourner le train | `ServiceExpedition` l. 138-141 | L (conséquence) |
| Précondition renforcée sur `charger` | `CamionFrigorifique` l. 13 | L |
| Méthodes héritées qui lèvent `UnsupportedOperationException` | `Camion`, `Train`, `Bateau`, `Avion` | L, I |
| Interface trop large | `IVehicule` | I |
| Dépendances créées en dur dans le service | `ServiceExpedition` l. 12-14, 205, 207 | D |
| Singleton `Database` | `Database` l. 22, `ServiceClient` l. 23 | D |
| Flotte lue en statique | `AffectationVehicule` l. 17 | D |
| Mode, statut et type client en `String` | `Expedition`, `Client`, tous les services | primitive obsession |
| Type de colis en `int` comparé à 2 et 3 alors que `Colis.DANGEREUX` existe | `CalculateurPrix` l. 18-20, `AffectationVehicule` l. 14-15, `ServiceExpedition` l. 32-33 et 151, `CamionFrigorifique` l. 12 | nombres magiques |
| TVA 1.2, frais 0.5, vitesses, tarifs, seuils en dur | `ServiceExpedition` l. 76, 97, 106-123, 186, `CalculateurPrix` l. 25-63 | nombres magiques |
| Détection dangereux/réfrigéré copiée 3 fois | `CalculateurPrix`, `AffectationVehicule`, `ServiceExpedition` | DRY |
| Arrondi recopié alors que `Utils.arrondir` existe | `CalculateurPrix` l. 66, `ServiceExpedition` l. 76, 97, 186 | DRY |
| Validation d'email recopiée alors que `Utils.emailValide` existe | `ServiceClient` l. 10 | DRY |
| Volume calculé deux fois | `Colis.getVolume` l. 29, `Expedition.getVolumeTotal` l. 35 | DRY |
| État `static` modifiable | `ServiceExpedition.compteur`, `Database.expeditions/clients`, `Flotte.vehicules`, `EmailService.journal`, `SmsService.journal`, `Utils.FORMAT_DATE` | état global |
| Erreurs signalées par `System.out` puis `return null` | `ServiceExpedition`, `ServiceClient`, `Vehicule.charger` l. 41 | gestion d'erreurs |
| Exception ignorée | `EmailService` l. 23 | gestion d'erreurs |
| `-1` comme valeur d'erreur | `Utils.getDistance` l. 50, `ServiceExpedition.calculerDevis` l. 94 | gestion d'erreurs |
| Identifiant tiré au hasard | `ServiceExpedition` l. 64 | non déterministe |
| Clé d'API de production dans le code | `SmsService` l. 11 | sécurité |
| SQL construit par concaténation | `Database` l. 34, 39 | sécurité (injection) |
| Code mort : `historique`, `nbExpeditions`, `apiKey`, `arrondir`, `emailValide`, `formaterPrix`, `toutesLesExpeditions`, `getPoidsTotal`, `getVolumeTotal`, `getValeurTotale` | `Expedition`, `Client`, `SmsService`, `Utils`, `Database` | code mort |

## Ajouter le mode FLUVIAL

Spécification (étape 8) : 0,40 €/km + 0,02 €/kg, minimum 300 €, délai = distance / 360 arrondi au supérieur + 1 jour, réfrigéré interdit, pas d'urgence, péniche `PEN-0001` de 2 500 t, trajet Paris - Le Havre de 365 km.

| Fichier | Emplacement | Modification |
|---|---|---|
| `CalculateurPrix` | l. 25-41 | nouvelle branche `FLUVIAL` avec tarif et minimum de 300 € |
| `ServiceExpedition` | l. 39 | règle du réfrigéré : `FLUVIAL` est exclu seulement parce que la liste autorisée ne contient que ROUTE et MER, rien ne le dit |
| `ServiceExpedition` | l. 43 | ajouter `FLUVIAL` au refus de l'urgence |
| `ServiceExpedition` | l. 103-125 | nouveau `case` dans `calculerDelai` |
| `ServiceExpedition` | l. 142-149 | choisir la méthode de déplacement de la péniche (`naviguer`) |
| `AffectationVehicule` | l. 20-52 | nouvelle branche avec `instanceof Peniche` |
| `Peniche` (nouvelle classe) | | étend `Vehicule` et doit encore implémenter `rouler`, `voler` et `activerRefrigeration` en levant une exception |
| `Flotte` | l. 17 | ajouter `new Peniche("PEN-0001", 2500000)` |
| `Utils.getDistance` | l. 30-50 | ajouter Paris - Le Havre, 365 km |
| `ModeTransport` (contrat) | | il manque `FLUVIAL`, mais le module contrat est interdit en écriture : le nouveau mode se teste par des tests unitaires |
| `Main` | | facultatif, pour la démo |

Total : 5 fichiers existants à modifier dans le code (6 avec `Main`), 1 nouvelle classe.

## Ajouter le supplément hors gabarit

Spécification : prix x 1,10 si un colis dépasse 200 cm sur une dimension.

| Fichier | Emplacement | Modification |
|---|---|---|
| `Colis` | près de `getVolume` l. 29 | ajouter une méthode qui dit si une dimension dépasse 200 cm (sinon le test se fait dans le calculateur) |
| `CalculateurPrix` | boucle l. 14-21 | détecter un colis hors gabarit |
| `CalculateurPrix` | l. 44-47 | appliquer x 1,10 avec les autres suppléments, avant la remise |
| `Expedition.genererFacture` | l. 52 | facultatif, si la facture doit afficher le supplément |

Total : 1 à 2 fichiers existants. Le devis et la création passent tous deux par `CalculateurPrix.calculerPrixHT`, donc un seul endroit applique le supplément.

Ces deux listes servent de référence pour l'étape 8.
