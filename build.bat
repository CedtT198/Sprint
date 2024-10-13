@REM SCRIPT DE GENERATION FICHIER JAR
@echo off

set "projectPath=."
set "tempPath=temp"
set "jarFile=spring-ced.jar"
set "destinationDir=C:\ITU\S5\Framework\Test sprint"

mkdir temp
@REM 8
@REM javac -g -cp ".;lib/*" -d "%tempPath%" src/*.java

@REM 17
javac -g -cp ".;lib/*" -d "%tempPath%" "%projectPath%\src\*.java"
jar cvf "%tempPath%\%jarFile%" -C "%tempPath%" .

rem Vérifier si le répertoire de destination existe
if exist "%destinationDir%\" (
    rem Déplacer le fichier JAR vers le répertoire de destination
    move "%tempPath%\%jarFile%" "%destinationDir%\lib\%jarFile%"
) else (
    rem Si le répertoire de destination n'existe pas, le placer dans le répertoire actuel
    move "%tempPath%\%jarFile%" "%projectPath%\%jarFile%"
)

copy "%projectPath%\error.jsp" "%destinationDir%\"
copy "%projectPath%\lib\paranamer-2.8.jar" "%destinationDir%\lib\"

rem supprimer le répertoire temporaire
rmdir /s /q "%tempPath%"

@REM Appel du script du déploiement de l'application test
cd %destinationDir%
call deploy.bat

@REM pause