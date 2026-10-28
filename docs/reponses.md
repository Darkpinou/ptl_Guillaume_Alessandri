# Réponses aux questions de la feuille de route

## Etape 3 : ServiceClient, une ou deux responsabilités ?

Deux. Les règles de validité d'un client (nom, email, SIRET de 14 caractères pour une entreprise) changent quand
le métier change. L'enchaînement de la création (enregistrer, puis envoyer l'email de bienvenue) change quand on
touche à la persistance ou aux notifications. Ce sont deux raisons de modifier la classe.

On a donc extrait `ValidateurClient`, sur le modèle de `ValidateurExpedition`. `ServiceClient` ne fait plus que
créer le client : il appelle le validateur, enregistre le client, puis envoie l'email de bienvenue. Ces deux appels restent dans
`ServiceClient` à cette étape : `Database` et `EmailService` sont traités à l'étape 4.

## Etape 5 : qui choisit la stratégie ?

Si l'enum porte la stratégie (`ModeTransport.ROUTE` connaît son `TarifTransport`), ajouter un mode tient en une
ligne et le compilateur refuse un mode sans tarif. Mais l'enum dépend alors des implémentations : impossible de
changer le tarif d'un mode sans recompiler, ou de le remplacer par un faux dans un test. Une
`Map<ModeTransport, TarifTransport>` injectée se configure au démarrage, en revanche un mode oublié dans la Map ne
se voit qu'à l'exécution.

On a choisi l'enum : nos stratégies n'ont pas d'état et on n'a pas besoin de les changer à l'exécution. L'enum
porte aussi les règles d'acceptation du mode (plus bas), et `TypeClient` porte sa `PolitiqueRemise` de la même
façon. Le délai par mode, donné en bonus, suit le même schéma avec `DelaiTransport` : on l'a fait parce que la
grille interdit tout `switch` sur un mode et qu'`EstimateurDelai` en avait un.

Aucune étape ne couvrait les `if` du validateur, de la facture et du `Notificateur` sur un mode ou un type de
client. On les remplace ici par des attributs d'enum. `ModeTransport` liste ses interdictions et, s'il refuse
l'urgence, le message de refus (pour MER, celui du legacy). Chaque `Interdiction` associe un type de marchandise à
son message d'erreur, repris mot pour mot du legacy. Le validateur parcourt les interdictions dans leur ordre de
déclaration, qui suit l'ordre des contrôles du legacy : dangereux, réfrigéré, puis urgence. Ajouter un mode ne
demande donc aucune modification du validateur. `TypeClient` dit si le SIRET est obligatoire (facture et
`ValidateurClient`) et si le client reçoit des SMS (`Notificateur`).

## Etape 6 : et la remise ?

La remise devient un décorateur, `Remise`, qui délègue à la `PolitiqueRemise` du type de client (étape 5). Sa
place dans la chaîne compte : la remise entreprise compare au seuil de 5000 euros le prix majoré par les
suppléments et l'urgence, et l'assurance s'ajoute ensuite sans remise. `CalculateurPrix` assemble donc
Assurance(Remise(Urgence(Réfrigéré(Dangereux(Fragile(prix de base)))))). Le test d'ordre prend une base de
4000 euros, urgente, pour une entreprise assurée : il attend 5300 euros, alors qu'on obtiendrait 5600 avec la
remise avant l'urgence et 5270 avec l'assurance avant la remise.

## Etape 7 : découper IVehicule ou la réduire ?

La piste 1 propose de découper `IVehicule` en petites interfaces (rouler, naviguer, voler, réfrigérer). Mais
avec la piste 3, `demarrerExpedition` n'appelle plus que `partir(distance)`, et chaque véhicule roule, navigue,
vole ou active sa réfrigération à l'intérieur de sa propre méthode. Plus aucun code extérieur n'appellerait
`voler` ou `activerRefrigeration` : des interfaces `Volant` ou `Refrigerable` n'auraient aucun client. On a donc
réduit `IVehicule` à ce que tous les véhicules savent faire, dont `partir()` et `peutPrendreEnCharge()`. Plus
aucune méthode ne lève `UnsupportedOperationException`.

La correction de Liskov change un comportement en ROUTE. Une expédition qui mélange un colis réfrigéré et un
colis standard part en camion frigorifique. Dans le legacy, `CamionFrigorifique.charger` refusait le colis
standard avec une `IllegalArgumentException`, et la création de l'expédition plantait : un camion frigorifique
ne pouvait pas remplacer un `Camion`, c'est la violation signalée par la feuille de route. Maintenant `charger`
accepte tous les colis, comme dans `Camion`, et l'expédition mixte est acceptée.

Un second écart vient de la piste 3. Quand un navire porte une expédition réfrigérée A et une expédition standard
B, le départ de B affiche maintenant le réglage des conteneurs frigorifiques, ce que le legacy ne faisait pas.
`partir()` regarde tout le chargement du véhicule, alors que le legacy ne regardait que les colis de
l'expédition démarrée. C'est plus juste physiquement : le navire transporte des colis réfrigérés, il doit les
garder au froid.

## Etape 8 : preuve par l'extension

Pour FLUVIAL, trois fichiers existants changent : `ModeTransport` (une ligne qui donne le tarif, le délai, les
interdictions et le refus d'urgence), `Flotte` (la péniche PEN-0001 de 2 500 t) et `Utils` (Paris - Le Havre,
365 km). Le reste tient dans trois nouvelles classes : `TarifFluvial`, `DelaiFluvial` et `Peniche`. Le diagnostic
prévoyait 5 fichiers existants (CalculateurPrix, ServiceExpedition, AffectationVehicule, Flotte, Utils) plus la
péniche. `CalculateurPrix`, `ServiceExpedition`, `AffectationVehicule` et `ValidateurExpedition` ne changent pas : à part
la ligne de l'enum, seules les données bougent (la flotte et la table des distances).

Pour le hors gabarit, le diagnostic prévoyait 1 ou 2 fichiers (Colis, CalculateurPrix). `Colis` ne change pas, ses
dimensions avaient déjà des getters. On ajoute `SupplementHorsGabarit` et une ligne dans l'assemblage de `Main`.
`CalculateurPrix` a changé une fois : il reçoit maintenant la liste des suppléments marchandise au constructeur,
pour la raison expliquée ci-dessous. Un prochain supplément marchandise ne le touchera plus.

Au total, 5 fichiers existants de `src/main` sont modifiés (ModeTransport, Flotte, Utils, CalculateurPrix, Main),
plus l'adaptateur de test. Le diagnostic en prévoyait 6 distincts (les 5 de FLUVIAL et Colis).

Les 68 tests passent-ils toujours ? Avec FLUVIAL, oui. Avec le hors gabarit actif partout, 6 échouent : quatre
tests de tarification, l'annulation en cours et une facture. Ils utilisent des colis de plus de 200 cm : les fûts
de solvant (240 cm), les produits laitiers (240 cm), les bobines d'acier (1200 cm) et le conteneur (1200 cm). Le
contrat décrit l'ancienne grille tarifaire, donc c'est un changement de spécification, pas une régression. Le
contrat est interdit en écriture : l'adaptateur assemble la configuration qu'il décrit, sans hors gabarit, et
`Main` assemble la nouvelle. Les 68 tests restent verts, la règle est active dans `Main` (les fûts passent de
3011,58 à 3312,74 euros TTC) et ses tests unitaires la vérifient.

## Grille d'auto-évaluation

| Critère | ✔ | Preuve |
|---|---|---|
| 68 tests de contrat verts | ✔ | `mvn install` : 68/68 dans le legacy, 110/110 dans le refactored (68 de contrat et 42 unitaires) |
| Aucun nombre magique | ✔ | Le grep des littéraux hors `static final` ne trouve que des chaînes (port JDBC, "TVA 20%", "14 chiffres"). Restent les données : table des distances (`Utils`), capacités de la flotte (`Flotte`), démo de `Main` |
| Aucun `if` / `switch` sur un mode, un type de client ou un type de marchandise | ✔ | Aucun `switch`, aucune comparaison à une constante `ModeTransport.` ou `TypeClient.`. Les seuls tests sur un type de marchandise sont `TypeMarchandise.X.estPresentDans` dans la classe dédiée à X (suppléments, camions, navire), règle de la classe validée avant l'étape 4 |
| Tarifs et remises en Strategy, suppléments en Decorator, chacun avec son test unitaire | ✔ | `TarifTransportTest` (9 tests, 5 tarifs), `PolitiqueRemiseTest` (5 tests, 3 remises), `DelaiTransportTest` (9 tests, 5 délais), `DecorateurPrixTest` (14 tests, 7 décorateurs, prix de base et ordre) |
| Aucun `instanceof`, aucune `UnsupportedOperationException` | ✔ | `grep -rnE 'instanceof\|UnsupportedOperationException' transport-refactored/src` : aucun |
| Aucun `static` modifiable, aucun singleton | ✔ | Grep des `static` non `final` et de `getInstance` dans `src/main` : aucun |
| L'adaptateur ne contient que de la traduction | ✔ | Il convertit les DTO et les enums du contrat et construit le graphe d'objets (rôle fixé à l'étape 4), dont la liste de suppléments décrite par le contrat |
| Ajouter `FLUVIAL` et « hors gabarit » n'a demandé que de nouvelles classes et l'assemblage | ✔ avec réserve | 4 nouvelles classes. Les fichiers existants changés sont l'assemblage (`ModeTransport`, `Main`) et des données (`Flotte`, `Utils`). Réserve : `CalculateurPrix` a changé une fois pour recevoir sa liste de suppléments, parce que le contrat impose l'ancienne grille |
