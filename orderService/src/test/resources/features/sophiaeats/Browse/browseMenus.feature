# language: fr
Fonctionnalité: Tout utilisateur d'internet peux chercher les menus de différents restaurants.

  Contexte:

  Plan du scénario: Chercher les menus d'un restaurant
    Etant donné que je suis un utilisateur d'internet
    Et qu'il existe un restaurant d'id <id> et de nom de menus <menus>
    Quand je recherche les menus du restaurant d'id <id>
    Alors je reçois le tableau des menus du restaurant

    Exemples:
      | id | menus                                                                                      |
      | 0  | "Cheeseburger originel, Cheeseburger double, Hamburger vert, Salade de pates, Menu enfant" |
      | 1  | "Reine, 4 fromages, Pates bolognaise, Pates carbonara"                                     |
      | 2  | "Classique jambon, Signature Chicken Mexicali, Classique Veggie Delite"                    |
      | 3  | "Menu enfant, Pates bolognaise, Salade de pommes de terre, Raviolis maison"                |
      | 4  | "Pates huilées, Ratatouille"                                                               |