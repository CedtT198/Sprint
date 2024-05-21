# Sprint

- Changer "destinationDir" dans "build.bat" pour spécfier la destination du JAR File. Le JAR sera présent dans le répertoire actuel si chemin erroné.


# Configuration dans web.xml : 

- Changer la valeur de param-value en le nom du package contenant vos controllers 
- Il faut que l'utilisateur annote ses controllers de l'annotation 'AnnotationController'
- Il faut que l'utilisateur annote tous les méthodes de ces controllers de l'annotation 'Get' et ajouter une valeur pour chaque méthode
- Ajouter un url mapping comme valeur de l'annotation
