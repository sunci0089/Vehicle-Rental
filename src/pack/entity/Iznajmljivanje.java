package pack.entity;

import GUI.Main;

import java.io.FileWriter;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
/**
 * Klasa {@code Iznajmljivanje} koja predstavlja iznajmljivanje vozila.
 * <p>
 * Sadrži informacije o iznajmljivanju: datum i vrijeme pocetka, korisniku vozila, o vozilu,
 * lokacijama preuzimanja i ostavljanja vozila: (x,y) koordinata
 * koje se cuvaju u dvodimenzionalnom nizu i informaciej o trajanju iznajmljivanja
 * u sekundama. Takodje se cuvaju i indikator kvara, popusta na svakih 10 iznajmljivanja
 * i promocija.
 * </p>
 */
public class Iznajmljivanje {
    private boolean imaPopust=false;
    //datum format 1.6.2024 09:00
    private LocalDateTime datumVijemeIznajmljivanja;
    private int [] lokacijaOstavljanja, lokacijaPreuzimanja;
    private int trajanje; //u sekundama koriscenja
    private Korisnik korisnik;
    private Vozilo vozilo;
    private boolean kvar;
    private boolean promocija;
    /**
     * Konstruktor klase Iznajmljivanje.
     * @param datum           Datum i vreme iznajmljivanja.
     * @param k               Korisnik koji iznajmljuje vozilo.
     * @param v               Vozilo koje se iznajmljuje.
     * @param xPocetno        X koordinata lokacije preuzimanja.
     * @param yPocetno        Y koordinata lokacije preuzimanja.
     * @param xOdrediste      X koordinata lokacije ostavljanja.
     * @param yOdrediste      Y koordinata lokacije ostavljanja.
     * @param trajanje        Trajanje iznajmljivanja u sekundama.
     * @param kvar            Indikator da li je vozilo imalo kvar.
     * @param promocija       Indikator da li je primenjena promocija.
     */
    public Iznajmljivanje(LocalDateTime datum, Korisnik k, Vozilo v, int xPocetno, int yPocetno,
                          int xOdrediste,int yOdrediste, int trajanje, boolean kvar, boolean promocija) {
        this.datumVijemeIznajmljivanja = datum;
        this.korisnik = k;
        this.vozilo = v;

        this.lokacijaPreuzimanja= new int [2];
        this.lokacijaOstavljanja= new int [2];
        this.lokacijaPreuzimanja[0]= xPocetno;
        this.lokacijaPreuzimanja[1]= yPocetno;
        this.lokacijaOstavljanja[0]= xOdrediste;
        this.lokacijaOstavljanja[1]= yOdrediste;

        this.trajanje = trajanje;
        this.kvar = kvar;
        this.promocija = promocija;
    }
    /**
     * Vraća tekstualni prikaz iznajmljivanja u formatu "datum i
     * vrijeme ID vozila" : d.M.yyyy HH-mm.
     * @return String prikaz iznajmljivanja.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy HH-mm");
        return datumVijemeIznajmljivanja.format(formatter)+" "+this.vozilo.getId();
        }
    /**
     * Proverava da li su dva iznajmljivanja ista na osnovu vremena i datuma i vozila
     * koje je iznajmljeno.
     * @param o Objekat koji se poredi sa trenutnim.
     * @return true ako su jednaki, false u suprotnom.
     */
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Iznajmljivanje other = (Iznajmljivanje) o;
        return this.datumVijemeIznajmljivanja.isEqual(other.getDatumVijemeIznajmljivanja())
                && this.vozilo.equals(other.getVozilo());
    }
    /**
     * Računa cijenu iznajmljivanja na osnovu trajanja, tipa vozila,
     * udaljenosti, popusta i promocija.
     * <p>
     * Iz properties {@code prop} cita
     * cijene i generise String {@code cjenovnik} u koga se redom
     * smjestaju cijene za ispis na racun u zavisnosti od vrste vozila
     * koristenog u iznajmljivanju, popusta i promocija.
     * Nakon generisanja cjenovnika, poziva se funkcija {@code pisiRacun}
     * </p>
     * @return Ukupna cijena za plaćanje. Ako dođe do greške, vraća -1.
     */
    public double racunajCijenu()
    {
         double car_unit_price=0, bike_unit_price=0, scooter_unit_price=0;
         double distance_narrow=0, distance_wide=0;
         double discount=0, discount_prom=0;

            car_unit_price=Double.parseDouble(Main.PROP.getProperty("car_unit_price"));
            bike_unit_price=Double.parseDouble(Main.PROP.getProperty("bike_unit_price"));
            scooter_unit_price=Double.parseDouble(Main.PROP.getProperty("scooter_unit_price"));
            distance_narrow=Double.parseDouble(Main.PROP.getProperty("distance_narrow"));
            distance_wide=Double.parseDouble(Main.PROP.getProperty("distance_wide"));
            discount=Double.parseDouble(Main.PROP.getProperty("discount"));
            discount_prom=Double.parseDouble(Main.PROP.getProperty("discount_prom"));

        double osnovnaCijena=1;
        double iznos=0;
        double cijenaPopusta=0;
        double cijenaPromocije=0;

        //string za generisanje racuna
        String cjenovnik="Osnovna cijena (";

            //racunamo osnovnu cijenu
        try{
        if (kvar) osnovnaCijena=0;
        else if (vozilo instanceof Automobil)
            osnovnaCijena=car_unit_price*trajanje;
        else if (vozilo instanceof Bicikl)
            osnovnaCijena= bike_unit_price*trajanje;
        else if (vozilo instanceof Trotinet)
            osnovnaCijena=scooter_unit_price*trajanje;

        cjenovnik+=vozilo.vrstaVozila()+"): "
                + String.format("%.2f",osnovnaCijena)+"\n" +
                "Iznos: osnovnaCijena*udaljenost (";

        } catch (NullPointerException e) {
                e.printStackTrace();
                return -1;
        }
            //racunamo iznos
        if (isDistanceWide()) iznos=osnovnaCijena*distance_wide;
        else iznos = osnovnaCijena*distance_narrow;

        cjenovnik+=(isDistanceWide())?"Wide":"Narrow";
        cjenovnik+="): " + String.format("%.2f", iznos)+"\n";

            //racunamo ukupno za placanje
        if (imaPopust) cijenaPopusta=osnovnaCijena*discount; // if true then
        if (promocija) cijenaPromocije=osnovnaCijena*discount_prom;

        double iznosZaPlacanje= iznos-cijenaPopusta-cijenaPromocije;
        vozilo.setPrihodi(vozilo.getPrihodi()+iznosZaPlacanje);
        cjenovnik+="Popust: " + String.format("%.2f", cijenaPopusta)+"\n" +
                "Promocija: "+ String.format("%.2f", cijenaPromocije)+"\n" +
                "Kvarovi: "+ ((kvar)?"ima":"nema")+"\n" +
                "-----------------\n" +
                "Iznos za placanje: "+String.format("%.2f", iznosZaPlacanje)+"\n";
        //pozivamo funkciju za pisanje racuna po cijenovniku
        pisiRacun(cjenovnik, Main.PROP.getProperty("filePathRacuni")
                .replace("${file.separator}", System.getProperty("file.separator")));

        return iznosZaPlacanje;

    }
    /**
     * Pravi novi .txt file i pise račun sa svim potrebnim informacijama koji nosi jedinstveno ime
     * u formatu: datum i vrijeme iznajmljivanja i ID iznajmljenog vozila. Generisan je dodatni
     * String {@code data} koji sadrzi sve potrebne informacije o iznajmljivanju
     * @param cjenovnik   Generisani string sa cijenama koji se dodaje na sve informacije
     *                    o iznajmljivanju.
     * @param path        Putanja do fajla gde se račun snima.
     */
    private void pisiRacun(String cjenovnik, String path){
        path+=this.toString()+".txt";
        File file = new File(path);
        // Pravimo novi file
        try {
            // provjeri da li postoji file
            if (!file.exists()) {
                // Create a new file
                if (file.createNewFile()) {
                    System.out.println("pisiRacun: napravljen fajl: " + file.getPath());
                } else {
                    //System.out.println("pisiRacun: fajl vec postoji.");
                }
            } else {
                //System.out.println("pisiRacun: fajl vec postoji.");
            }
        } catch (IOException e) {
            // Handle potential IOExceptions
            System.err.println("pisiRacun: greska pri pravljenu novog fajla!");
            e.printStackTrace();
        }
        //podaci za racun uz cijene
        String data="--- Racun ---\n" +
                "Vrijeme: "+ this.datumVijemeIznajmljivanja + "\n" +
                "Korisnik: "+ this.korisnik.getIme()+"\n" +
                "IDVozila: "+ this.vozilo.getId()+"\n" +
                "Pocetna lokacija: "+ lokacijaPreuzimanja[0]+","+lokacijaPreuzimanja[1] +"\n" +
                "Odredisna lokacija: "+ lokacijaOstavljanja[0]+","+lokacijaOstavljanja[1] +"\n" +
                "Trajanje koristenja: "+ this.trajanje +"\n" +
                "-----------------\n" +
                "ID dokumenta: "+ this.korisnik.getDokumentID() +"\n" +
                "Vozacka dozvola: "+ this.korisnik.getBrVozacke() +"\n"+
                "-----------------\n";

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(path, false))) {
            writer.write(data);
            writer.write(cjenovnik);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Proverava da li je udaljenost između lokacija preuzimanja
     * pripada siroj distanci ( <5, >14).
     * @return true ako je udaljenost velika, false u suprotnom.
     */
    private boolean isDistanceWide() {
        if (lokacijaPreuzimanja[0]<5 || lokacijaPreuzimanja[0]>14 ||
                lokacijaPreuzimanja[1]<5 || lokacijaPreuzimanja[1]>14 || lokacijaOstavljanja[0]<5
                || lokacijaOstavljanja[0]>14 || lokacijaOstavljanja[1]<5 || lokacijaOstavljanja[1]>14)
            return true;
        return false;
    }

    //get i set metode
    public int[] getLokacijaOstavljanja() {
        return lokacijaOstavljanja;
    }

    public void setLokacijaOstavljanja(int [] lokacijaOstavljanja) {
        this.lokacijaOstavljanja = lokacijaOstavljanja;
    }

    public int[] getLokacijaPreuzimanja() {
        return lokacijaPreuzimanja;
    }

    public void setLokacijaPreuzimanja(int[] lokacijaPreuzimanja) {
        this.lokacijaPreuzimanja = lokacijaPreuzimanja;
    }
    public LocalDateTime getDateTime(){
        return datumVijemeIznajmljivanja;
    }
    public LocalDate getDate(){
        return getDateTime().toLocalDate();
    }

    public int getTrajanje() {
        return trajanje;
    }
    public void setTrajanje(int set){
        trajanje=set;
    }
    public Korisnik getKorisnik() {
        return korisnik;
    }
    public void setKorisnik(Korisnik set){
        korisnik=set;
    }
    public Vozilo getVozilo() {
        return vozilo;
    }
    public void setVozilo(Vozilo set){
        vozilo=set;
    }

    public boolean getImaKvar() {
        return kvar;
    }
    public void setImaKvar(boolean set){
        kvar=set;
    }
    public boolean getPromocija() {
        return promocija;
    }
    public void setPromocija(boolean set){
        promocija=set;
    }
    public LocalDateTime getDatumVijemeIznajmljivanja() {
        return datumVijemeIznajmljivanja;
    }

    public void setDatumVijemeIznajmljivanja(LocalDateTime datumVijemeIznajmljivanja) {
        this.datumVijemeIznajmljivanja = datumVijemeIznajmljivanja;
    }
    public void setImaPopust(boolean imaPopust) {
        this.imaPopust = imaPopust;
    }
}