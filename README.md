# Sprint

- Changer "destinationDir" dans "build.bat" pour spécfier la destination du JAR File. Le JAR sera présent dans le répertoire actuel si chemin erroné.


# Configuration dans web.xml : 

- Changer la valeur de param-value en le nom du package contenant vos controllers 
- Attention! Tous les controleurs doivent être dans un package du nom 'Controller'
- Il faut que l'utilisateur annote ses controllers de l'annotation 'AnnotationController'
- Il faut que l'utilisateur annote toutes les méthodes de ces controllers de l'annotation 'Get' avec une valeur unique pour chaque méthode
- Toutes les fonctions annotés doivent être de type String ou ModelAndView seulement
- Annoté les paramètres des méthodes de l'annotation RequestParam