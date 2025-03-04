# language: fr
Fonctionnalité: Tout utilisateur d'internet peux chercher un restaurant par son nom, son type de nourriture ou sa disponibilité.

  Contexte:

  Scénario: Chercher un restaurant par son nom
    Quand Un utilisateur d'internet recherche un restaurant en écrivant "Elysée" dans la barre de recherche
    Alors Il obtient une liste de 1 élément contenant le restaurant de nom "Elysée Restauration"

  Scénario: Chercher un restaurant par son type de nourriture
    Quand Un utilisateur d'internet recherche un restaurant qui fait de la nourriture végétarienne
    Alors Il obtient une liste de 4 éléments

  Scénario: Chercher un restaurant par disponibilité
    Quand Un utilisateur d'internet ne souhaite voir que les restaurants qui peuvent se permettre de prendre une nouvelle commande
    Alors Il obtient une liste de tous les restaurants disponible