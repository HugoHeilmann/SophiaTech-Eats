# language: fr
Fonctionnalité: Stratégie étudiante

  Contexte:

  Scénario: Application de la stratégie étudiante pour un étudiant
    Etant donné Que je suis un étudiant
    Et Que j'ai une commande valide auprès d'un restaurant avec un stratégie de réduction pour les étudiants
    Quand Je passe ma commande auprès de ce restaurant avec la stratégie de réduction pour les étudiants
    Alors Je devrais bénéficier de la réduction de 10%

  Scénario: Application de la stratégie étudiante pour un non étudiant
    Etant donné Que je ne suis pas un étudiant
    Et Que j'ai une commande valide auprès d'un restaurant avec un stratégie de réduction pour les étudiants
    Quand Je passe ma commande auprès de ce restaurant avec la stratégie de réduction pour les étudiants
    Alors Je ne devrais pas bénéficier de la réduction de 10%