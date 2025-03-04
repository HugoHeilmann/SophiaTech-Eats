# language: fr
Fonctionnalité: Quand j'ai un horaire de livraison prédéfini, alors je peux voir les menus disponibles

  Contexte:

  Plan du scénario: Chercher les menus d'un restaurant par disponibilité
    Etant donné Que j'ai une commande avec un horaire de livraison prévu dans <time> minutes dans le restaurant <RestaurantId>
    Quand Je recherche les menus dans ce meme restaurant
    Alors Je reçois le tableau des menus du restaurant avec un temps de préparation inférieur

    Exemples:
      | time | RestaurantId |
      | 7    | 0            |
      | 10   | 0            |
      | 15   | 0            |
      | 25   | 0            |
      | 30   | 0            |
      | 35   | 0            |

  Scénario: Chercher les menus d'un restaurant par disponibilité avec un menus dans la commande
    Etant donné Que j'ai une commande avec un horaire de livraison prévu dans 30 minutes dans le restaurant 0
    Et Que j'ai un item dans mon panier avec un temps de préparation de 3 minutes
    Et Que j'ai un item dans mon panier avec un temps de préparation de 4 minutes
    Quand Je recherche les menus dans ce meme restaurant
    Alors Je reçois le tableau des menus du restaurant avec un temps de préparation inférieur

  Scénario: Chercher les menus d'un restaurant par disponibilité avec déjà plusieurs menus dans la commande
    Etant donné Que j'ai une commande avec un horaire de livraison prévu dans 30 minutes dans le restaurant 0
    Et Que j'ai un item dans mon panier avec un temps de préparation de 15 minutes
    Et Que j'ai un item dans mon panier avec un temps de préparation de 15 minutes
    Quand Je recherche les menus dans ce meme restaurant
    Alors Je reçois le tableau des menus du restaurant avec un temps de préparation inférieur