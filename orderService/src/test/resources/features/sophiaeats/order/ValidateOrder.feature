# language: fr
Fonctionnalité: Valider une commande

  Contexte:

  Scénario: Valider une commande
    Etant donné Que j'ai une commande de 1 article dans mon panier
    Quand Je valide ma commande
    Alors le système m'informe si l'opération a réussi ou échoué
    Et la commande est enregistrée dans mon historique de commande