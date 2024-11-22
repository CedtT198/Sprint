# Sprint

- Changer "destinationDir" dans "build.bat" pour spécfier la destination du JAR File. Le JAR sera présent dans le répertoire actuel si chemin erroné.

# Configuration dans web.xml : 

- Changer la valeur de param-value en le nom du package contenant vos controllers

# Comment utiliser le framework ?

- Il faut que l'utilisateur annote ses controllers de l'annotation "Annotation.Controller" et les méthodes de ces controllers de l'annotation 'Get' ou 'Post' avec une valeur unique pour chaque méthode
- Toutes les fonctions annotées doivent être de type "java.lang.String" ou "modelandview.ModelAndView" seulement
- Pour les passages de données via un formulaire, vous avez 2 choix :
    - Annoter les paramètres des méthodes de l'annotation "Annotation.RequestParam"
    - Mettre un objet en paramètre des méthodes et annoter la classe de cet objet  et ses attributs de "Annotation.Model" et de "Annotation.Field"
- Pour l'utilisation de Session, ajouter util.MySession aux paramètres de la méthode des controllers
- Pour les validations de formulaire
    - Annoter les attributs des Models avec les annotations dans package "validation.annotation"
    - Utiliser la fonction Validation.validate(Object object)


N.B :
- Ligne 138 tokony soloina anaranle page index
- Choisir ligne 11 ou 14 "build.bat" pour compilation en fonction de la version de votre JDK