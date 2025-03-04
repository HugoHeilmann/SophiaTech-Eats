# language: fr

Fonctionnalité: gérer les groupes

  Contexte:
    Etant donné que je suis un utilisateur de SophiaEats

  Scénario: Créer un groupe
    Quand je créer une commande de groupe pour 12h30 aux "Templiers"
    Alors je recois un id pour un groupe de commande pour 12h30 aux "Templiers"

  Scénario: Rejoindre un groupe
    Etant donné un groupe de commande pour 12h30 aux "Templiers"
    Quand je rejoins un le groupe grace à l'id
    Alors la localisation de ma commande est "Templiers" et l'heure de livraison est 12h30

  Scénario: Cloturer une commande de groupe
    Etant donné un groupe de commande pour 12h30 aux "Templiers"
    Quand je cloture le groupe grace à l'id
    Alors on ne peut plus recupérer les informations du groupe

  Scénario: Un etudiant essaie de rejoindre un groupe déjà fermé
    Etant donné un groupe de commande pour 12h30 aux "Templiers"
    Quand je cloture le groupe grace à l'id
    Alors quand j'essaie de rejoindre le groupe, le site me dit qu'il ne trouve pas le groupe

  Scénario: Cloturer une commande de groupe de je ne suis pas membre
    Etant donné un groupe de commande pour 12h30 aux "Templiers"
    Quand je cloture le groupe grace à l'id alors que je ne suis pas membre
    Alors le groupe existe toujours


  Scénario: End to End test d'une commande de groupe
    Etant donné 2 utilisateurs de SophiaEats
    Quand le premier utilisateur crée une commande de groupe aux "Templiers"
    Et que le deuxième utilisateur rejoint le groupe
    Et que les deux utilisateurs réalisent leur commande
    Et que le deuxième utilisateur cloture le groupe et met l'horaire de livraison à 12h30
    Alors l'horaire de livraison de la commande des deux utilisateur est 12h30 aux "Templiers" et le groupe n'est plus accessible



