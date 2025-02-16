# Sprint

<h2> Configuration :</h2>
<ul>
    <h3>Dans web.xml<h3>
    <li>Changer la valeur de param-value en le nom du package contenant vos controllers</li>
</ul>
<ul>
    <h3>Dans build.bat<h3>
    <li>Changer "destinationDir" dans "build.bat" pour spécfier la destination du JAR File. Le JAR sera présent dans le répertoire actuel si chemin erroné</li>
    <li> Changer valeur de 'uploadPath' dans fonction 'processFileUpload' dans 'FrontController' pour l'emplacement des fichiers a importer</li>
    <!-- <li>Ligne 138 tokony soloina anaranle page index </li> -->
    <li>Choisir ligne 11 ou 14 "build.bat" pour compilation en fonction de la version de votre JDK</li>
</ul> 

<h2>Comment utiliser le framework ?</h2>

<p style="color:red">UTILISER TOUJOURS DES OBJETS DANS LES VOS MODELS, SURTOUT PAS DE VARIABLES PRIMITIVES</p>

- Il faut que l'utilisateur annote ses controllers de l'annotation "Annotation.Controller" et les méthodes de ces controllers de l'annotation 'Get' ou 'Post' avec une valeur unique pour chaque méthode
- Toutes les fonctions annotées de @Controller doivent doivent avoir le type de retour "modelandview.ModelAndView" 
- Pour les passages de données via un formulaire, vous avez 2 choix :
    - Annoter les paramètres des méthodes de l'annotation "Annotation.RequestParam"
    - Mettre un objet en paramètre des méthodes et annoter cette derniere de "Annotation.RequestObject"
- Pour l'utilisation de Session, ajouter util.MySession aux paramètres de la méthode des controllers
- Pour les validations et gestion d'erreur de formulaire :
    - Annoter les attributs des Models avec les annotations dans package "validation.annotation"
    - Vous devez imperativement mettre en parametre des controllers le Model a valider
    - Le framework retourne un 'Map<String, String>' comme type d'erreur, a vous de faire un 'Map<String, List<String>> errors = (Map<String, List<String>>) session.getAttribute("errors")' dans votre formulaire pour prendre les erreurs
        - Cle : Nom du champ
        - Valeur :  L'erreur retourne
