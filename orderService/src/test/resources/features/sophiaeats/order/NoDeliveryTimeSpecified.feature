# language: fr
Fonctionnalité: Ajout d'un article dans une commande où le temps de livraison n'est pas spécifié

  Contexte:

  Scénario: Ajouter un article avec un temps de préparation de 50sc dans une commande où le temps de livraison n'est pas spécifié
    Etant donné Que je suis en train de passer une commande
    Et Que je n'ai pas spécifié l'heure de livraison
    Quand J'ajoute un menu à ma commande avec une durée de préparation de 50 secondes
    Alors La date de livraison est 50 secondes après la date de finalisation de la commande

  Scénario: Ajouter un article avec un temps de préparation de 200sc dans une commande où le temps de livraison n'est pas spécifié
    Etant donné Que je suis en train de passer une commande
    Et Que je n'ai pas spécifié l'heure de livraison
    Quand J'ajoute un menu à ma commande avec une durée de préparation de 200 secondes
    Alors La date de livraison est 200 secondes après la date de finalisation de la commande

  Scénario: Ajouter de plusieurs articles dans une commande où le temps de livraison n'est pas spécifié
    Etant donné Que je suis en train de passer une commande
    Et Que je n'ai pas spécifié l'heure de livraison
    Quand J'ajoute un menu à ma commande avec une durée de préparation de 150 secondes
    Et J'ajoute un menu à ma commande avec une durée de préparation de 70 secondes
    Et J'ajoute un menu à ma commande avec une durée de préparation de 32 secondes
    Alors La date de livraison est 252 secondes après la date de finalisation de la commande