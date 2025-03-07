---
name: User Story
about: Describe the User Story
title: "[US]"
labels: bug, US
assignees: ''

---

:star: **Titre :**<br>
En quelques mots, résumez l'objectif de l'US par exemple _Ajout d'un produit dans le panier_.

:star: **Description :**<br>
**En tant que** [type of user], **Je veux** [an action] **afin de** [a benefit/a value]

:star2: **Priorité :**<br>
C'est la priorité dans les attentes du client. Suivez l'échelle donnée ci-dessous.

[méthode MoSCoW](https://paper-leaf.com/insights/prioritize-user-stories/)

1. _**Doit avoir :** la première version de ce produit nécessite absolument cette fonctionnalité - elle est essentielle
   au succès du produit._
2. _**Devrait avoir** : l'idéal serait que la première version de ce produit dispose de cette fonctionnalité, mais elle
   n'est pas absolument nécessaire. Elles peuvent être aussi importantes, mais pas aussi critiques en termes de temps,
   que les "Must Have"._
3. _**Aurait pu**: l'histoire de l'utilisateur a de la valeur et est souhaitable, mais en fin de compte, elle n'est pas
   nécessaire._
4. _**N'aura pas** : l'histoire de l'utilisateur est considérée comme étant parmi les moins critiques ou les moins
   utiles._

:star2: **Estimation :**<br>
Préciser l'effort requis pour mettre en œuvre la US.
Ce point est estimé avec la méthode vue en cours.

:sparkles: **Règle métier :**<br>
Précisez ici les règles métiers essentielles pour le développement de cette user-story.
Ces règles sont écrites en français avec comme objectif de très facilement comprendre le travail à effectuer.

_Voici un exemple ci-dessous sur un panier d’un site e-commerce :_
```
lorsque je rajoute un élément supplémentaire d'un produit dans mon 
panier.
    - si quantité > stock alors erreur "pas assez de stock 
      disponible"
    - si quantité < stock alors on ajoute +1 à la quantité_
```

:star2: **Critère d'acceptation**<br>
Précisez l'ensemble des conditions que la story doit satisfaire pour être considérée comme complète et terminée.

Plus spécifiquement décrivez un ensemble de scénario qui deviendront des tests d'acceptation. 
Précisez bien les données associées comme vous le voyez dans les scénarios d'exemples données ci-dessous. 
Vous pouvez faire référence ici aux tests Gherkins correspondant

**Scenario:** objectif<br>
**Etant donné que** contexte<br>
**Quand** une action est effectuée<br>
**Alors** des résultats sont observables<br>

_Voici quelques exemples_<br>

_**Scénario:**_<br>
```
    Etant donné que je suis sur mon panier 
    Et que j'ai un produit d'id "1235" en quantité "1" <br>
    Et que le stock restant sur ce produit est de "10"
    Quand j'ajoute "1" quantité de mon produit
    Alors mon produit aura "2" quantités
```
## Legende 
- :star:  Requis 
- :star2: Requis avant le passage en développement.
- :sparkles: Un plus

> Ce texte et ces exemples s'inspirent de  : https://blog.myagilepartner.fr/index.php/2017/03/18/story-a4/
