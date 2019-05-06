# CFD
téléchargement de campus france, pour windows ou linux, avec Java, Selenium et Chrome (74)

## Utilisation : 
   * il faut régler la recherche sur le site campus france / pastel
   * se faire un fichier properties cf.properties du style
   ```
  cf.login=...
  cf.mdp=***
  cf.nom=NOM_Prenom
  cf.OS=WIN (WIN ou LINUX)
  ```
  * exécuter le programme, en ligne de commande : 
     * mvn clean package
     * cd target 
     * java -jar campusfrance-1-jar-with-dependencies.jar
     * le programme fait alors quelques vérifications (sur le fichier properties) (sur des dossiers, qu'il crée) (sur le téléchargement d'un webdriver), puis il télécharge (il ne retélécharge pas ce qui est déjà présent dans le dossier 'dossiers'
     * c'est téléchargé là où s'est exécuter.
