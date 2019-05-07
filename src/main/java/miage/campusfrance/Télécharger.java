package miage.campusfrance;


import miage.campusfrance.cf.Messages;
import miage.campusfrance.cf.QuitListener;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Télécharger implements QuitListener {

    String login = "";
    String mdp = "";
    String nom = "";
    String OS = "WIN";
    private ChromeDriver driver;


    String getUrlWebDriver() {
        if (OS.equals("WIN")) {
            return "https://chromedriver.storage.googleapis.com/74.0.3729.6/chromedriver_win32.zip";
        }
        else if (OS.equals("MACOS")) {
            return "https://chromedriver.storage.googleapis.com/74.0.3729.6/chromedriver_mac64.zip";
        }
        else{
            return "https://chromedriver.storage.googleapis.com/74.0.3729.6/chromedriver_linux64.zip";
        }
    }

    String getWebDriverName() {
        if (OS.equals("WIN")) {
            return "chromedriver.exe";
        }
        else {
            return "chromedriver";
        }
    }

    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }



    void verifDir(String nom) {
        File dir = new File(nom);
        if (dir.isDirectory()) Messages.message("dossier '"+nom+"' ok");
        else if (dir.isFile()) {
            Messages.messageErreur("fichier '"+nom+"'  existant, ce n'est pas un dossier");
            System.exit(-1);
        }
        else dir.mkdir();
    }


    public Télécharger() {

        File propFile = new File("cf.properties");
        if (!propFile.isFile()) {
            Messages.messageErreur("Il n'y a pas le fichier cf.properties");
            Messages.messageErreur("cf.login=...");
            Messages.messageErreur("cf.mdp=...");
            Messages.messageErreur("cf.nom=NOM_Prenom");
            Messages.messageErreur("cf.OS=WIN ou LINUX ou MACOS");
            System.exit(-1);

        }


        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(propFile));


            this.login = prop.getProperty("cf.login");
            this.mdp = prop.getProperty("cf.mdp");
            this.nom = prop.getProperty("cf.nom");
            this.OS = prop.getProperty("cf.OS");


        } catch (IOException e) {
            e.printStackTrace();
            Messages.messageErreur("erreur de chargement de cf.properties");
            System.exit(-1);
        }

        Messages.addQuitListener(this);

        verifDir("dossiers");
        verifDir("driver");

        File driver = new File("driver"+File.separator+"chromedriver.exe");  // .exe...

        // https://chromedriver.storage.googleapis.com/74.0.3729.6/chromedriver_win32.zip
        // ou https://chromedriver.storage.googleapis.com/74.0.3729.6/chromedriver_linux64.zip

        // https://stackoverflow.com/questions/921262/how-to-download-and-save-a-file-from-internet-using-java

        if (! driver.isFile()) {
            Messages.message("téléchargement du webdriver");
            try {
                URL website = new URL(getUrlWebDriver());
                ReadableByteChannel rbc = null;
                rbc = Channels.newChannel(website.openStream());

                String name = "driver"+File.separator+getWebDriverName();
                String nameZip = name+".zip";

                FileOutputStream fos = new FileOutputStream(nameZip);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                ZipInputStream zipIn = new ZipInputStream(new FileInputStream(nameZip));
                ZipEntry entry = zipIn.getNextEntry(); // il n'y en a qu'une
                extractFile(zipIn, name);
                zipIn.closeEntry();


            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

    }


    public static void main(String [] args) {

        Télécharger cf = new Télécharger();


        try {
            cf.selenium();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void selenium() throws IOException {

        ChromeOptions opts = new ChromeOptions();



        System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("download.default_directory",   System.getProperty("user.dir")+File.separator+"dossiers" + File.separator );
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
         driver = new ChromeDriver(options);


        // puisqu'avec les options cela ne fonctionne pas... on change les settings
        driver.get("chrome://settings/content/pdfDocuments");
        String [] chemin = {"settings-ui", "settings-main", "settings-basic-page",
                "settings-privacy-page",


                "settings-pdf-documents",}; //  "settings-toggle-button"

        WebElement  root = driver.findElement(By.tagName(chemin[0]));
        root = expandRootElement(driver, root);

        for(int shadow = 1; shadow < chemin.length; shadow++) {
            System.out.println(chemin[shadow]);
            root = root.findElement(By.cssSelector(chemin[shadow]));
            root = expandRootElement(driver, root);
        }

        root.findElement(By.cssSelector("settings-toggle-button")).click();

        driver.get("https://pastel.diplomatie.gouv.fr/etudesenfrance/dyn/public/authentification/login.html");

        WebElement loginElt = driver.findElement(By.cssSelector("input[placeholder=Identifiant]"));
        loginElt.sendKeys(this.login);

        WebElement passElt = driver.findElement(By.cssSelector("input[placeholder='Mot de passe']"));
        passElt.sendKeys(this.mdp);

        WebElement bouton = driver.findElement(By.cssSelector("input[value=Connexion]"));
        bouton.click();

        // plutot que de cliquer sur les elements
        WebElement lien = driver.findElement(By.cssSelector("a[href^='/etudesenfrance/dyn/protected/etablissement/EF_RCA/rechercheDefaut.html']"));
        driver.get(lien.getAttribute("href"));


        List<WebElement> listeTr = driver.findElements(By.cssSelector("table#tableauDossiers > tbody > tr"));
        Messages.message("Il y a "+listeTr.size()+" dossiers");

        String [] noms = new String[listeTr.size()];
        String [] liens = new String[listeTr.size()];
        for(int i = 0; i < listeTr.size(); i++) {
            WebElement nom = listeTr.get(i).findElement(By.cssSelector("td:nth-of-type(5)"));
            noms[i] = nom.getText();
            WebElement lienDossier = listeTr.get(i).findElement(By.cssSelector("td:nth-of-type(10) > a"));
            liens[i]= lienDossier.getAttribute("href");

        }


        // clear
        LocalDateTime ldt = LocalDateTime.now();
        String date = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault()).format(ldt);
        File oldDownload = new File("dossiers"+File.separator+"dossier_"+this.nom+"_"+date+".pdf");
        if (oldDownload.isFile()) oldDownload.delete();


        for(int i = 0; i < listeTr.size(); i++) {
            téléchargerDossier(driver, liens[i], noms[i] );
            Messages.progresser(i+1, listeTr.size());
        }

        Messages.message("fini !");
        driver.quit();
        // téléchargerDossier(driver, liens[0], noms[0], "" );

    }

    private void téléchargerDossier(WebDriver driver, String lien, String nomFichier) {
        File destination = new File("dossiers"+File.separator + nomFichier + ".pdf");

        if (destination.isFile()) {
            // do nothing
            Messages.messageWarning("fichier déjà présent : "+destination.getName());

            return;
        }
        else {
            driver.get(lien);

            String href = driver.findElement(By.id("exportDossier")).getAttribute("href");

            LocalDateTime ldt = LocalDateTime.now();
            String date = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault()).format(ldt);

            driver.findElement(By.id("exportDossier")).click();

            File download = new File("dossiers"+File.separator+"dossier_"+this.nom+"_"+date+".pdf");
            String [] blink = {"*", " "};
            int i = 0;
            while (! download.isFile()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                    System.out.print(blink[i]+"download.isFile() ="+download.isFile()+" \r");
                    i = (i+1)%2;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("");

            boolean result = download.renameTo(destination);
            if (result) Messages.messageOk("download de "+destination.getName());
            else Messages.messageErreur("problème avec "+destination.getName());
        }

    }



    //Returns webelement
    public WebElement expandRootElement(WebDriver driver, WebElement element) {
        WebElement ele = (WebElement) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].shadowRoot",element);
        return ele;
    }

    @Override
    public void quitter() {
        if (driver != null) driver.quit();
    }
}
