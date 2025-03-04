# language: fr
Fonctionnalité: Stratégie de HappyHour

  Contexte:

  Plan du Scénario: Application de la stratégie HappyHour
    Etant donné Que j'ai une commande valide auprès d'un restaurant avec un stratégie de réduction happy hour
    Et Qu'il est <hour>h<min>
    Quand Je passe ma commande auprès de ce restaurant avec la stratégie de réduction happy hour
    Alors Je devrais bénéficier de la réduction de 25%

    Exemples:
      | hour | min |
      | 17   | 00  |
      | 17   | 30  |
      | 18   | 00  |
      | 18   | 59  |


  Plan du Scénario: Application de la stratégie HappyHour non applicable
    Etant donné Que j'ai une commande valide auprès d'un restaurant avec un stratégie de réduction happy hour
    Et Qu'il est <hour>h<min>
    Quand Je passe ma commande auprès de ce restaurant avec la stratégie de réduction happy hour
    Alors Je ne devrais pas bénéficier de la réduction de 25%

    Exemples:
      | hour | min |
      | 15   | 30  |
      | 16   | 59  |
      | 19   | 00  |
      | 20   | 00  |

