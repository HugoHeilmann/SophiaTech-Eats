# language: fr
Fonctionnalité: Ajout d'articles dans le panier

  Contexte:

  Plan du scénario: Ajout d'un article dans le panier
    Etant donné Que j'ai dans mon panier une commande dans le restaurant d'id <id> sans articles
    Quand J'y ajoute le <menu> <quantité> fois
    Alors j'ai dans mon panier une commande avec le <menu> <quantité> fois dans le restaurant d'id <id>

    Exemples:
      | id | menu                        | quantité |
      | 0  | "Cheeseburger originel"     | 4        |
      | 1  | "Pates bolognaise"          | 3        |
      | 2  | "Classique Veggie Delite"   | 2        |
      | 3  | "Salade de pommes de terre" | 1        |


  Scénario: Application de la réduction
    Etant donné Que j'ai une commande de plus de 10 articles auprès du restaurant d'id 0
    Quand Je passe ma commande auprès de ce restaurant
    Alors Je devrais bénéficier d'une réduction de 5%