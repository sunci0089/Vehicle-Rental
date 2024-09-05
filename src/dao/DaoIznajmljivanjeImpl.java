package dao;

import GUI.Main;
import pack.entity.Iznajmljivanje;
import pack.entity.Korisnik;
import pack.entity.Vozilo;
import pack.finder.IzvjestajGenerator;
import pack.izvjestaji.DnevniIzvjestaj;
import pack.izvjestaji.Izvjestaj;
import pack.izvjestaji.SumarniIzvjestaj;
import pack.parser.Parser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
/**
 * Implementacija interfejsa {@code Dao} za rad sa entitetima tipa Iznajmljivanje.
 * Omogućava čitanje, dodavanje, pretragu i sortiranje podataka o iznajmljivanjima,
 * kao i generisanje dnevnog i sumarnog izveštaja.
 */
public class DaoIznajmljivanjeImpl implements Dao<Iznajmljivanje> {
    private List<Iznajmljivanje> iznajmljivanja;
    /**
     * Omogucava pristup kolekciji dostupnih vozila
     */
    private DaoVoziloImpl daoVozila;
    private List<DnevniIzvjestaj> dnevniIzvjestaji;
    private SumarniIzvjestaj sumarniIzvjestaj;
    /**
     * Konstruktor koji inicijalizuje listu iznajmljivanja i instancu {@code DaoVoziloImpl}.
     */
    public DaoIznajmljivanjeImpl(){
        iznajmljivanja= new ArrayList<>();
        daoVozila= new DaoVoziloImpl();
    }
    /**
     * Vraća listu svih iznajmljivanja.
     * @return lista iznajmljivanja
     */
    @Override
    public List<Iznajmljivanje> getList() {
        return iznajmljivanja;
    }
    /**
     * Čita podatke o iznajmljivanjima iz CSV fajla i vrši validaciju podataka.
     * Prvo učitava podatke o vozilima iz fajla definisanog u properties fajlu,
     * kreiranjem {@code DaoVoziloImpl} da bi pravilno izvrsili validaciju
     * iznajmljivanja pretragom dostupnih vozila
     * @param filePath putanja do CSV fajla sa podacima o iznajmljivanjima
     */
    @Override
    public void citajIz(String filePath) {
        //ucitati vozila
        daoVozila.citajIz(Main.PROP.getProperty("filePathVozila")
                .replace("${file.separator}", System.getProperty("file.separator")));
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Citaj zaglavlje
            String headerLine = br.readLine();
            if (headerLine == null) {
                throw new IOException("Prazan CSV file");
            }
            // Citaj linije podataka
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 10) {
                    LocalDateTime vrijemeIznajmljivanja = Parser.parseDateTime(values[0]);
                    if (vrijemeIznajmljivanja==null)
                        System.err.println("citajIznajmljivanja: nepravilno vrijeme!");
                    else {
                        String idKorisnik = values[1];
                        String idVozilo = values[2];
                        if (idKorisnik.isEmpty() && idVozilo.isEmpty())
                            System.err.println("citajIznajmljivanja: prazna polja!");
                        else {
                            int xPreuzimanja = Integer.parseInt(values[3].replaceAll("\"", ""));
                            int yPreuzimanja = Integer.parseInt(values[4].replaceAll("\"", ""));
                            int xOdredista = Integer.parseInt(values[5].replaceAll("\"", ""));
                            int yOdredista = Integer.parseInt(values[6].replaceAll("\"", ""));

                            int trajanje = Integer.parseInt(values[7]);
                            if (trajanje<=0) System.err.println("citajIznajmljivanja: trajanje nije Int ili <= 0");
                            else {
                                boolean kvar = values[8].equals("da");
                                boolean promocija = values[9].equals("da");
                                if(!kvar && !values[8].equals("ne"))
                                    System.err.println("citajIznajmljivanja: polje kvar nepravilno!");
                                else if(!promocija && !values[9].equals("ne"))
                                    System.err.println("citajIznajmljivanja: polje promocija nepravilno!");
                                else{
                                //provjera da li vozilo postoji
                                Vozilo v = daoVozila.pretrazi(idVozilo);
                                if (v == null) System.err.println("citajIznajmljivanja: vozilo ne postoji! " + line);
                                else {
                                    //provjera da li su koordinate u opsegu
                                    if (xPreuzimanja < 0 || xPreuzimanja > 19 || yPreuzimanja < 0 || yPreuzimanja > 19
                                         || xOdredista < 0 || xOdredista > 19 || yOdredista < 0 || yOdredista > 19)
                                        System.err.println("citajIznajmljivanja: koordinate van granica! " + line);
                                    else {
                                        Iznajmljivanje iznajmljivanje = new Iznajmljivanje(
                                            vrijemeIznajmljivanja, new Korisnik(idKorisnik), v, xPreuzimanja,
                                                yPreuzimanja, xOdredista, yOdredista, trajanje, kvar, promocija);
                                            if (this.pretrazi(iznajmljivanje) == null) {
                                                this.dodajElement(iznajmljivanje);
                                                System.out.println("citajIznajmljivanje: uspjesno: " + iznajmljivanje.toString());
                                                System.out.flush();
                                            } else
                                                System.err.println("citajIznajmljivanja: duplo iznajmljivanje! " + iznajmljivanje.toString());
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    System.err.println("citajIznajmljivanje: nepravilna linija! " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
    /**
     * Dodaje novo iznajmljivanje u listu.
     * @param element instanca klase Iznajmljivanje koja se dodaje u listu
     */
    @Override
    public void dodajElement(Iznajmljivanje element) {
        iznajmljivanja.add(element);
    }
    /**
     * Pretražuje listu iznajmljivanja i vraća prvo iznajmljivanje koje se poklapa sa datim.
     * @param element iznajmljivanje koje se pretražuje
     * @return pronađeno iznajmljivanje ili null ako ne postoji u listi
     */
    public Iznajmljivanje pretrazi(Iznajmljivanje element) {
        for (Iznajmljivanje iterator: iznajmljivanja)
            if(iterator.equals(element))
                return iterator;
        return null;
    }
    /**
     * Generiše sumarni izveštaj na osnovu fajlova u datom direktorijumu.
     * @param path putanja do direktorijuma sa .txt fajlovima racuni
     */
    public void generisiSumarniIzvjestaj(String path){
        sumarniIzvjestaj= new SumarniIzvjestaj();
        Path startingDir = Paths.get(path);
        String pattern = "*.txt";
        try {
            IzvjestajGenerator finder = new IzvjestajGenerator(pattern, daoVozila, sumarniIzvjestaj);
            Files.walkFileTree(startingDir, finder);
            finder.done();
        } catch (IOException e){
            System.err.println("generisiSumarniIzvjestaj: greska pri generisanju");
        }
    }
    /**
     * Generiše dnevne izveštaje za svaki datum na osnovu iznajmljivanja.
     * Poziva funkciju {@code generisiDnevniIzvjestaj} za odredjene datume
     * @param path putanja do direktorijuma sa fajlovima izveštaja
     */
    public void generisiSveDnevneIzvjestaje(String path){
        dnevniIzvjestaji= new ArrayList<>();
        Map<LocalDate, List<Iznajmljivanje>> groupedByDate = iznajmljivanja.stream()
                .collect(Collectors.groupingBy(Iznajmljivanje::getDate));

        for (LocalDate date : groupedByDate.keySet()) {
            String formattedDate= String.valueOf(date.getDayOfMonth())+"."+
                    String.valueOf(date.getMonthValue())+"."+
                    String.valueOf(date.getYear());
            System.out.println("Generisi dnevni izvjestaj: " + formattedDate);
            generisiDnevniIzvjestaj(formattedDate, path);
        }
    }
     /**
     * Generiše dnevni izveštaj za dati datum na osnovu fajlova u datom direktorijumu.
     * @param datum datum u formatu "dd.MM.yyyy"
     * @param path putanja do direktorijuma sa fajlovima izveštaja
     */
    public void generisiDnevniIzvjestaj(String datum, String path){
        Path startingDir = Paths.get(path);
        String pattern = datum+"*.txt";
        DnevniIzvjestaj dnevni = new DnevniIzvjestaj();
        try {
            IzvjestajGenerator finder = new IzvjestajGenerator(pattern, daoVozila, dnevni);
            Files.walkFileTree(startingDir, finder);
            finder.done();
            if(finder.getNumMatches()>0)
                dnevniIzvjestaji.add(dnevni);
        }catch (IOException e){
            e.printStackTrace();
            System.err.println("generisiDnevniIzvjestaj: greska pri generisanju");
        }
    }
    //get i set funkcije
    public DaoVoziloImpl getDaoVozila() {
        return daoVozila;
    }

    public void setDaoVozila(DaoVoziloImpl daoVozila) {
        this.daoVozila = daoVozila;
    }

    public void sortirajIznajmljivanja(){
        iznajmljivanja.sort(Comparator.comparing(Iznajmljivanje::getDateTime));
    }

    public List<DnevniIzvjestaj> getDnevniIzvjestaji() {
        return dnevniIzvjestaji;
    }

    public SumarniIzvjestaj getSumarniIzvjestaj() {
        return sumarniIzvjestaj;
    }
}
