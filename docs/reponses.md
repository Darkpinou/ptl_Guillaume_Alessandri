# Réponses aux questions de la feuille de route

## Etape 3 : ServiceClient, une ou deux responsabilités ?

Deux. Les règles de validité d'un client (nom, email, SIRET de 14 caractères pour une entreprise) changent quand
le métier change. L'enchaînement de la création (enregistrer, puis envoyer l'email de bienvenue) change quand on
touche à la persistance ou aux notifications. Ce sont deux raisons de modifier la classe.

On a donc extrait `ValidateurClient`, sur le modèle de `ValidateurExpedition`. `ServiceClient` ne fait plus que
créer le client : il appelle le validateur, enregistre le client, puis envoie l'email de bienvenue. Ces deux appels restent dans
`ServiceClient` à cette étape : `Database` et `EmailService` sont traités à l'étape 4.
