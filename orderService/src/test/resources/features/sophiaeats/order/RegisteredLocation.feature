# language: fr
Fonctionnalité: Utilisateur enregistré peut ajouter et supprimer des localisations et l'utiliser pour passer une commande

  Contexte:

  Scénario: Ajouter une localisation
    Etant donné Que j'ai cliqué sur ajouter une localisation
    Quand Je rentre une localisation
    Alors La localisation est ajoutée à ma liste de localisations

  Scénario: Supprimer une localisation
    Quand Je supprime une localisation
    Alors La localisation est supprimée de ma liste de localisations


  Scénario: Utiliser une localisation pour passer une commande
    Etant donnée Que j'ai une localisation enregistrée
    Quand Je passe une commande pour utiliser cette localisation
    Alors Je peux choisir cette localisation pour ma commande