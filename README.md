# CFD
téléchargement de campus france, pour windows ou linux ou macos, avec Java, Selenium et Chrome (74)
Essayé pour l'instant uniquement sur windows 10.

## Utilisation : 
   * il faut régler la recherche sur le site campus france / pastel
   * se faire un fichier properties cf.properties du style
   ```
  cf.login=...
  cf.mdp=***
  cf.nom=NOM_Prenom
  cf.OS=WIN (WIN ou LINUX ou MACOS)
  ```
  * exécuter le programme, en ligne de commande : 
     * mvn clean package
     * mvn exec:java@cf ou cd target puis java -jar campusfrance-1-jar-with-dependencies.jar
     * le programme fait alors quelques vérifications (sur le fichier properties) (sur des dossiers, qu'il crée) (sur le téléchargement d'un webdriver), puis il télécharge (il ne retélécharge pas ce qui est déjà présent dans le dossier 'dossiers'
     * c'est téléchargé là où s'est exécuter.
