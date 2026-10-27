# Etape 1 : diagnostic

Les numéros de ligne sont ceux des classes copiées dans transport-refactored.

## Rôle des classes

| Classe | Ce qu'elle fait | Dépend de |
|---|---|---|
| Main | démo dans la console | Flotte, ServiceClient, ServiceExpedition, Colis |
| ServiceClient | crée un client et lui envoie un email de bienvenue | Client, Database, EmailService |
| ServiceExpedition | gère toute l'expédition : vérifications, prix, délai, véhicule, statut, notifications | Database, CalculateurPrix, AffectationVehicule, Utils, EmailService, SmsService, Expedition |
| CalculateurPrix | calcule le prix HT | Colis |
| AffectationVehicule | cherche un véhicule libre et compatible | Flotte, les classes de véhicules |
| Expedition | données d'une expédition et facture | Client, Colis, Vehicule, Utils |
| Client | données d'un client | rien |
| Colis | données d'un colis (le type est un int) | rien |
| IVehicule | interface de tous les véhicules | Colis |
| Vehicule | classe abstraite des véhicules | IVehicule, Colis |
| Camion | véhicule pour la route | Vehicule |
| CamionFrigorifique | camion pour les colis réfrigérés | Camion, Colis |
| Train, Bateau, Avion | un véhicule par mode | Vehicule |
| Flotte | liste statique des véhicules | les véhicules |
| Database | stockage en mémoire, c'est un singleton | Client, Expedition |
| Utils | distances, dates, arrondi, email | rien |
| EmailService | envoi d'email simulé | rien |
| SmsService | envoi de SMS simulé | rien |

## Violations SOLID

- S : ServiceExpedition fait presque tout (vérifications, prix TTC, délai, déplacement du véhicule, notifications).
- O : CalculateurPrix fait des if/else sur le mode (l. 25-41), il faut modifier la classe pour ajouter un mode. Pareil pour le switch de calculerDelai dans ServiceExpedition (l. 103).
- L : Train.faireLePlein() lance une exception (l. 31) alors que Vehicule.faireLePlein() marche. ServiceExpedition est obligé d'attraper l'exception (l. 138-141).
- I : IVehicule oblige tous les véhicules à avoir rouler, naviguer, voler et activerRefrigeration, alors qu'un camion ne vole pas.
- D : ServiceExpedition crée lui-même ses dépendances (Database.getInstance(), new CalculateurPrix(), new AffectationVehicule(), l. 12-14).

## Tableau défaut / emplacement / principe

| Défaut | Emplacement | Principe |
|---|---|---|
| une classe qui fait tout | ServiceExpedition | S |
| la facture est générée dans l'objet Expedition | Expedition.genererFacture l. 52 | S |
| classe fourre-tout | Utils | S |
| if/else sur le mode pour le prix | CalculateurPrix l. 25-41 | O |
| if sur le type de client pour la remise | CalculateurPrix l. 50-59 | O |
| switch sur le mode pour le délai | ServiceExpedition l. 103-125 | O |
| instanceof sur chaque véhicule | AffectationVehicule l. 21-49 | O |
| faireLePlein lance une exception | Train l. 31 | L |
| charger refuse les colis non réfrigérés | CamionFrigorifique l. 13 | L |
| méthodes qui lancent UnsupportedOperationException | Camion, Train, Bateau, Avion | L et I |
| interface trop grosse | IVehicule | I |
| dépendances créées dans le service | ServiceExpedition l. 12-14 | D |
| singleton Database | Database, ServiceClient l. 23 | D |
| liste statique de la flotte lue directement | AffectationVehicule l. 17 | D |

## Ajouter le mode FLUVIAL

- CalculateurPrix : ajouter le tarif fluvial (l. 25-41)
- ServiceExpedition : refuser l'urgence (l. 43), ajouter le délai dans calculerDelai (l. 103-125), faire naviguer la péniche (l. 142-149). Le réfrigéré est déjà refusé (l. 39) car seuls ROUTE et MER sont autorisés.
- AffectationVehicule : ajouter le cas de la péniche (l. 20-52)
- Flotte : ajouter la péniche PEN-0001
- Utils : ajouter la distance Paris - Le Havre (365 km)
- nouvelle classe Peniche, qui doit quand même avoir rouler, voler et activerRefrigeration
- ModeTransport dans le contrat n'a pas FLUVIAL, mais on n'a pas le droit de le modifier

Donc 5 fichiers existants à modifier et une nouvelle classe.

## Ajouter le supplément hors gabarit

- Colis : ajouter une méthode qui dit si un côté dépasse 200 cm
- CalculateurPrix : appliquer x1,10 avec les autres suppléments (l. 44-47)

Donc 1 ou 2 fichiers.
