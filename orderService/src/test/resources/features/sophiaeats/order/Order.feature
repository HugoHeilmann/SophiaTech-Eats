# language: fr
Fonctionnalité: gérer les commandes

  Contexte:

  Scénario: Création de commande dans le restaurant 0
    Etant donné Que je suis sur la page du restaurant d'id 0
    Quand Je créé une commande dans ce restaurant
    Alors j'ai dans mon panier une commande sans articles dans le restaurant d'id 0

  Scénario: Création de commande dans le restaurant 1
    Etant donné Que je suis sur la page du restaurant d'id 1
    Quand Je créé une commande dans ce restaurant
    Alors j'ai dans mon panier une commande sans articles dans le restaurant d'id 1

  Scénario: Choix du lieux Antibes et de la date de livraison 01-01-2025 à 10h10
    Etant donné Que j'ai dans mon panier une commande sans articles dans le restaurant d'id 0
    Quand Je choisis le lieux "Antibes" et la date 01-01-2025 à 10h10 de livraison
    Alors j'ai dans mon panier une commande sans articles dans le restaurant d'id 0 avec le lieux "Antibes" et et la date 01-01-2025 à 10h10 de livraison

  Scénario: Choix du lieux Nice et de la date de livraison 24-12-2024 à 12h00
    Etant donné Que j'ai dans mon panier une commande sans articles dans le restaurant d'id 0
    Quand Je choisis le lieux "140 rue Albert Einstein, 06560 Valbonne" et la date 24-12-2024 à 12h00 de livraison
    Alors j'ai dans mon panier une commande sans articles dans le restaurant d'id 0 avec le lieux "140 rue Albert Einstein, 06560 Valbonne" et et la date 24-12-2024 à 12h00 de livraison