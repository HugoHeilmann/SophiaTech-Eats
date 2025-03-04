# SopiaTech Eats-Team-24-25 #template


## TEAM Q

Guigon Dorian : PO 

Heilmann Hugo : DevOps

Heschung Erwan : DevOps

Magnin Mathis : SA 

Roques Maxence : QA

## Un exemple de User Story 

Recherche des restaurants selon les filtres : [issue #29](https://github.com/PNS-Conception/STE-24-25--teamq/issues/29)

**Description** : 

En tant que Utilisateur d'internet, Je veux rechercher les restaurants selon leur nom, type de nourriture ou disponibilité afin de ne voir que les restaurants qui peuvent m'intéresser.

Lien vers la feature : [browseRestaurants.feature](https://github.com/PNS-Conception/STE-24-25--teamq/blob/master/src/test/resources/features/sophiaeats/Browse/browseRestaurants.feature)


## Comment lancer le projet et l'installer

Il vous suffit d'avoir maven et d'executer mvn clean package à la racine du projet 

## Structure du projet 

A la racine du projet, vous retrouverez : 

### [.github](https://github.com/PNS-Conception/STE-24-25--teamq/tree/master/.github): 
   - Contient sous workflows/maven.yml, une version d'un fichier d'actions qui est déclenché dès que vous poussez du code. 
   - Contient sous ISSUE_TEMPLATE, les modèles pour les issues user_story et bug. Vous pouvez le compléter à votre guise.

### [src](https://github.com/PNS-Conception/STE-24-25--teamq/tree/master/src) : 
   - Vous retrouverez le répertoire de [tests](https://github.com/PNS-Conception/STE-24-25--teamq/tree/master/src/test)
        - Celui-ci est découpé en un dossier resources avec les différents features correspondant au tests cucumber
        - Vous trouverez également [le dossier](https://github.com/PNS-Conception/STE-24-25--teamq/tree/master/src/test/java/sophiaeats) où sont repertoriées les tests unitaires [unitTests](https://github.com/PNS-Conception/STE-24-25--teamq/tree/master/src/master/java/sophiaeats/unitTests) et la définition des pas des tests cucumber [stepDefs](https://github.com/PNS-Conception/STE-24-25--teamq/tree/develop/src/test/java/sophiaeats/stepDefs)
   - Vous retrouverez le répertoire avec le [code source](https://github.com/PNS-Conception/STE-24-25--teamq/tree/master/src/main/java/sophiaeats)
     Vous y trouverez :
        - le [package customer](https://github.com/PNS-Conception/STE-24-25--teamq/tree/master/src/main/java/sophiaeats/customer) la classe Customer et son enum CustomerStatus
        - le [package exceptions](https://github.com/PNS-Conception/STE-24-25--teamq/tree/master/src/main/java/sophiaeats/exceptions) les différentes excpetions personnalisées levées dans la classe DataBase
        - le [package facade](https://github.com/PNS-Conception/STE-24-25--teamq/tree/master/src/main/java/sophiaeats/facade) les différents DP facades permettant le liens avec les autres classes
        - le [package location](https://github.com/PNS-Conception/STE-24-25--teamq/tree/master/src/main/java/sophiaeats/location) avec la gestion de la localisation 
        - le [package strategy](https://github.com/PNS-Conception/STE-24-25--teamq/tree/master/src/main/java/sophiaeats/strategy) avec les différentes stratégies de réductions
        - différentes classes permettant le bon fonctionnement du backend comme :
             - DataBase : une classe qui enregistres toutes les informations nécessaires
             - Restaurant
             - Menu
             - Order
             - Group
             - Capacity
             - FoodType

### [doc](https://github.com/PNS-Conception/STE-24-25--teamq/tree/master/doc) :
   - Vous trouverez les rendus et certains diagrammes UML (à utiliser avec plantUML)
   
### pom.xml : 
   - Cucumber 7 et JUnit 5
   - JDK 17 - Etc.
