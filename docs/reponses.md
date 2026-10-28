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
