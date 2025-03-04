# language: fr
Fonctionnalité: Stratégie de fidélité

  Contexte:


  Plan du Scénario: Application de la stratégie de fidélité
    Etant donné Que j'ai une commande valide auprès d'un restaurant avec un stratégie de réduction fidelité
    Et Que j'ai déjà passé <nombreCommandes> commandes auprès de ce restaurant
    Quand Je passe ma commande auprès de ce restaurant avec la stratégie de réduction fidelité
    Alors Je devrais bénéficier de la réduction de 20%

    Exemples:
      | nombreCommandes |
      | 3               |
      | 7               |
      | 11              |


  Plan du Scénario: Application de la stratégie de fidélité non applicable
    Etant donné Que j'ai une commande valide auprès d'un restaurant avec un stratégie de réduction fidelité
    Et Que j'ai déjà passé <nombreCommandes> commandes auprès de ce restaurant
    Quand Je passe ma commande auprès de ce restaurant avec la stratégie de réduction fidelité
    Alors Je ne devrais pas bénéficier de la réduction de 20%

    Exemples:
      | nombreCommandes |
      | 1               |
      | 2               |
      | 5               |
      | 8               |
