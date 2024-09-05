package pack.finder;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.FileVisitor;
import java.nio.file.PathMatcher;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.FileVisitResult;
import java.util.List;
import java.nio.file.Files;
import java.io.IOException;

import dao.DaoVoziloImpl;
import pack.izvjestaji.DnevniIzvjestaj;
import pack.izvjestaji.Izvjestaj;
import pack.entity.Vozilo;
import pack.izvjestaji.SumarniIzvjestaj;
import pack.parser.Parser;

import static java.nio.file.FileVisitResult.CONTINUE;
/**
 * Klasa koja obilazi direktorijum i generiše izvještaj na osnovu pronađenih fajlova.
 * Implementira interfejs {@link FileVisitor<Path>} za pretragu i obradu fajlova.
 */
public class IzvjestajGenerator implements FileVisitor<Path>{
    private final PathMatcher matcher;
    private Izvjestaj izvjestaj;
    private DaoVoziloImpl vozila;

    public DaoVoziloImpl getVozila() {
        return vozila;
    }

    public void setVozila(DaoVoziloImpl vozila) {
        this.vozila = vozila;
    }

    /**
     * Broji pronadjene fajlove
     */
    private int numMatches=0;
    /**
     * Konstruktor koji inicijalizuje generator izvještaja.
     * @param pattern sablon po kojem se pretražuju fajlovi (npr. "*.txt").
     * @param izvjestaj Instanca izvjestaja koji će biti popunjavan prilikom obrade fajlova.
     */
    public IzvjestajGenerator(String pattern, DaoVoziloImpl vozila, Izvjestaj izvjestaj){
        this.izvjestaj= izvjestaj;
        if (!pattern.equals("*.txt") && izvjestaj instanceof DnevniIzvjestaj)
            ((DnevniIzvjestaj) izvjestaj).setDatum(Parser.parseDate(pattern.replace("*.txt", "")));
        matcher= FileSystems.getDefault().getPathMatcher("glob:"+pattern);
        this.vozila=vozila;
    }
    /**
     * Metoda koja analizira fajl i izvlači relevantne informacije za izvjestaj
     * po vrsti izvjestaja (dnevni ili sumarni).
     * @param file Putanja do foldera sa racunimma
     * @throws IOException Ako dođe do greške prilikom čitanja fajla.
     */
    void findRacun(Path file) throws IOException {
        Path name= file.getFileName();
        if (name != null && matcher.matches(name)){
            numMatches++;
            if (Files.isRegularFile(file)) {
                List<String> lines = Files.readAllLines(file);
                Vozilo v=null;
                for (String line : lines) {
                    switch(line.substring(0,6)){
                        case "IDVozi":
                            v = vozila.pretrazi(line.substring(10));
                            break;
                        case "Iznos:":
                            izvjestaj.racunajIznosVoznji(line.substring(40));
                            break;
                        case "Popust":
                            izvjestaj.racunajPopust(line.substring(8));
                            break;
                        case "Promoc":
                            izvjestaj.racunajPromocije(line.substring(11));
                            break;
                        case "Kvarov":
                            if (line.substring(9).equals("ima")) {
                                izvjestaj.racunajIznosZaPopravke(v);
                            }
                            break;
                        case "Iznos ":
                            izvjestaj.racunajPrihod(line.substring(19));
                            break;
                        default: break;
                    }
                }
                izvjestaj.racunajIznosZaOdrzavanje();
                if(izvjestaj instanceof SumarniIzvjestaj) {
                    ((SumarniIzvjestaj)izvjestaj).racunajUkupneTroskove();
                    ((SumarniIzvjestaj)izvjestaj).racunajUkupanPorez();
                }
                System.out.println("Read file: " + file + " with " + lines.size() + " lines.");
            }
        }
    }
    /**
     * Metoda koja broji posjecene fajlove
     * @param file Putanja do foldera u kome su fajlovi
     */
    void find(Path file) {
        Path name=file.getFileName();
        if (name!=null && matcher.matches(name)){
            numMatches++;
        }
    }

    /**
     * Vraca broj pronadjenih fajlova za dati pattern
     * @return numMatches int
     */
    public int getNumMatches(){
        return  numMatches;
    }
    public void done(){
        System.out.println("RacunFinder: done found files: "+ numMatches);
    }
    //ostale metode FileVisitor interfejsa za obilazak stabla
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs){
        find(dir);
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException arg1) throws IOException {
        find(dir);
        return CONTINUE;
    }
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
        try {
            findRacun(file);
        } catch (Exception e){
            e.printStackTrace();
            System.err.println("FileVisitor: can't read file");
        }
        finally{
            return FileVisitResult.CONTINUE;
        }
    }
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.err.println(exc);
        return CONTINUE;
    }

    public Izvjestaj getIzvjestaj() {
        return izvjestaj;
    }
}
