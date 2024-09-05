package GUI;

import dao.DaoIznajmljivanjeImpl;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Properties;
/**
 * Klasa Main predstavlja ulaznu tačku za aplikaciju.
 * <p>
 * Ova klasa se koristi za pokretanje aplikacije, postavljanje izgleda i osećaja za Swing komponente,
 * učitavanje konfiguracionih podataka iz .properties fajla, brisanje starih datoteka prethodnih izvrsavanja
 * aplikacije za pripremu novog izvrsavanja i prikaz glavnog prozora aplikacije.
 * </p>
 * @author Una Vuckovic 1131/21
 */
public class Main {
    /**
     * Glavni properties file dijeljen je sa svim klasama
     */
    public static Properties PROP;

    /**
     * {@code Prop} properties file ucitava se staticki na pocetku
     */
    static {
            PROP = new Properties();
            try(InputStream input = new FileInputStream("ostalo"+File.separator+"cijene.properties")){
                PROP.load(input);
            }
            catch (Exception e) {
                System.err.println("main: Properties not found!");
            }
    }
    /**
     * Briše sve datoteke i poddirektorijume unutar zadatog foldera.
     * Ako folder ne postoji, kreira ga.
     * @param folderPath Putanja do foldera.
     */
    public static void obrisiRacune(String folderPath){

    // Create a File object representing the folder
    File folder = new File(folderPath);

    // Check if the folder exists
        if (folder.exists()) {
        // If it exists, delete all files inside it
        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                deleteDirectory(file);
            }
        }
    } else {
        // If the folder does not exist, create it
        folder.mkdirs();
    }

        System.out.println("Folder " + folderPath + " is now empty and ready.");
    }

    /**
     * Rekurzivno briše direktorijum i njegov sadržaj.
     * @param directoryToBeDeleted direktorijum.
     */
    public static void deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
    }
    /**
     * Glavna metoda koja pokreće aplikaciju.
     * <p>
     * Postavlja izgled i osećaj za Swing komponente, učitava konfiguracione podatke iz .properties fajla,
     * briše stare datoteke, učitava podatke o iznajmljivanjima iz datoteka,
     * sortira ih i prikazuje glavni prozor aplikacije.
     * </p>
     * @param args Argumenti komandne linije (ne koriste se u ovoj aplikaciji).
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                        //obrisi racune sa prosle simulacije, i serijalizovana vozila sa prosle simulacije
                        obrisiRacune(PROP.getProperty("filePathRacuni")
                                .replace("${file.separator}", System.getProperty("file.separator")));
                        obrisiRacune(PROP.getProperty("filePathVozilaSaNajvisePrihoda")
                                .replace("${file.separator}", System.getProperty("file.separator")));;


                    DaoIznajmljivanjeImpl daoIznajm= new DaoIznajmljivanjeImpl();
                    daoIznajm.citajIz(PROP.getProperty("filePathIznajmljivanja")
                            .replace("${file.separator}", System.getProperty("file.separator")));
                    daoIznajm.sortirajIznajmljivanja();
                    GlavniProzor frame = new GlavniProzor(daoIznajm);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
