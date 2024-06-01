# Sprint

- Changer "destinationDir" dans "build.bat" pour spécfier la destination du JAR File. Le JAR sera présent dans le répertoire actuel si chemin erroné.


# Configuration dans web.xml : 

- Changer la valeur de param-value en le nom du package contenant vos controllers 
- Attention! Tous les controleurs doivent être dans un package du nom 'Controller'
- Il faut que l'utilisateur annote ses controllers de l'annotation 'AnnotationController'
- Il faut que l'utilisateur annote tous les méthodes de ces controllers de l'annotation 'Get' et ajouter une valeur pour chaque méthode
(RECOMANDE : Nom de méthode = Nom de la valeur de l'annotaion)
- Ajouter un url mapping comme valeur de l'annotation

# N.B :  Actuellement, toutes les fonctions annotés sont seulement de type String ou ModelAndView