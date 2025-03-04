# language: fr
Fonctionnalité: Gérer un restaurant

  Contexte:

  Scénario: Changer les horaires d'ouverture d'un restaurant
    Etant donné que je suis le manager du restaurant 0
    Quand je change les horaires d'ouverture du restaurant 0 en 8h 22h
    Alors le restaurant 0 ouvre à 8h et ferme à 22h

  Scénario: Changer les horaires d'ouverture d'un jour spécifique d'un restaurant
    Etant donné que je suis le manager du restaurant 0
    Quand je change les horaires d'ouverture du restaurant 0 en 8h 23h le "LUNDI"
    Alors le restaurant 0 ouvre à 8h et ferme à 23h le "LUNDI" et ouvre de 9h à 22h les autres jours
    Et la capacité du restaurant 0 à 8h30 le "LUNDI" est différente de 0

  Scénario: Changer le personnel disponible d'un jour et d'une demi-heure spécifique d'un restaurant
    Etant donné que je suis le manager du restaurant 0
    Quand le manager change le nombre de membre du personnel disponible du restaurant 0 en 10 le "LUNDI" à 11h30
    Alors le restaurant 0 possède 3000 personnes disponible le "LUNDI" à 11h30

  Scénario: Ajouter un menu à un restaurant
    Quand le manager ajoute le nouveau menu au restaurant 0
    Alors le restaurant 0 possède 6 menu dans sa liste

  Scénario: Supprimer un menu d'un restaurant
    Quand le manager supprime le menu "Pates carbonara" du restaurant 1
    Alors le restaurant 1 possède 3 menus dans sa liste

  Scénario: Changer la description d'un menu
    Quand le manager modifie la description du menu "Pates carbonara" du restaurant 1 pour "Pates, crême fraiche, lardons, jaune d'oeuf"
    Alors le menu "Pates carbonara" du restaurant 1 possède comme description "Pates, crême fraiche, lardons, jaune d'oeuf"

  Scénario: Un client initialise une nouvelle commande
    Quand le client 0 débute une commande dans le restaurant 1
    Alors le restaurant 1 voit son nombre de commande possible de prendre en charge baisser à 0

  Scénario: Un client valide et paye pour sa commande
    Quand le client 0 valide sa commande faite dans le restaurant 1
    Alors le restaurant 1 voit son nombre de commandes qu'il peut prendre en charge augmenter à 1