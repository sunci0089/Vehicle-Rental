package pack.entity;

import pack.izvjestaji.Kvarovi;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Apstraktna klasa {@code Vozilo} predstavlja osnovne karakteristike i funkcionalnosti
 * vozila. Ova klasa implementira {@code Serializable} i {@code Punjivo} interfejs,
 * i služi kao osnovna klasa za razlicite tipove vozila.
 * <p>
 * Klasa obuhvata atribute kao što su ID, proizvodjac, model, cijena nabavke, trenutni nivo
 * baterije, listu kvarova, da li se krece, cijena po sekundi voznje i prihodi.
 * Obezbeđuje metode za mijenjanje trenutnog nivoa baterije punjenjem i praznjenjem
 * sa nitima.
 * </p>
 */
public abstract class Vozilo implements Serializable, Punjivo {
    private String id;
    private String proizvodjac, model;
    private int cijenaNabavke;

    private transient volatile short [] trenutniNivoBaterije;
    public transient volatile boolean kreceSe=false;
    private transient List<Kvarovi> kvarovi;
    private transient int cijenaPoSekundi;
    private double prihodi=0;

    /**
     * Pokreće nit za upravljanje baterijom. Implementira {@code Runnable} interfejs,
     * i koristi {@code prazniBateriju} metodu za postepeno pražnjenje baterije.
     */
    @Override
    public void run(){
        try {
            //pokrecemo thread za praznjenje baterije
            prazniBateriju();
            //puniBateriju();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    /**
     * Pražnjenje baterije dok je vozilo u pokretu. Metoda smanjuje nivo baterije
     * u intervalu od 1 sekunde, dok je {@code kreceSe} {@code true} i nivo baterije
     * je veci od 0.
     * @throws InterruptedException ako je nit prekinuta tokom pražnjenja baterije.
     */
    @Override
    public void prazniBateriju() throws InterruptedException{
                while (!kreceSe) wait();
                while (kreceSe && trenutniNivoBaterije[0]>0){
                    Thread.sleep(1000);
                    synchronized (trenutniNivoBaterije){
                    trenutniNivoBaterije[0]--;
                }
        }
    }
    /**
     * Punjenje baterije dok vozilo nije u pokretu. Metoda povećava nivo baterije
     * u intervalu od 1 sekunde, dok nije u pokretu i nivo baterije je manji od 100.
     * @throws InterruptedException ako je nit prekinuta tokom punjenja baterije.
     */
    @Override
    public void puniBateriju() throws InterruptedException{
        while (kreceSe) wait();
        while(!kreceSe && trenutniNivoBaterije[0]<100){
            //System.err.println("Puni se");
            Thread.sleep(1000);
            synchronized (trenutniNivoBaterije){
                trenutniNivoBaterije[0]++;
            }
        }
    }
    /**
     * Apstraktna metoda koja treba da se implementira u podklasama. Vraća string
     * koji opisuje vozila po vrsti.
     * @return String koji opisuje vozila po vrsti.
     */
    @Override
    public abstract String toString();

    /**
     * Konstruktor klase {@code Vozilo}.
     * Na pocetku trenutni nivo baterije se inicijalizuje na 100%
     * @param id ID vozila.
     * @param proizvodjac Proizvođač vozila.
     * @param model Model vozila.
     * @param cijena Cena nabavke vozila.
     */
    protected Vozilo(String id, String proizvodjac, String model, int cijena)
    {
        this.id=id;
        this.proizvodjac=proizvodjac;
        this.model=model;
        this.cijenaNabavke=cijena;
        this.trenutniNivoBaterije= new short[1];
        trenutniNivoBaterije[0]=100;
        this.kvarovi=new ArrayList<>();
    }
    /**
     * Apstraktna metoda koja treba da se implementira u podklasama. Vraća string
     * koji opisuje vrstu vozila.
     * @return String koji opisuje vrstu vozila.
     */
    public abstract String vrstaVozila();
    /**
     * Proverava da li su dva vozila ista na osnovu ID-a.
     * @param o Objekt koji se upoređuje sa trenutnim vozilom.
     * @return {@code true} ako su vozila ista, {@code false} inače.
     */
    @Override
    public boolean equals(Object o)
    {
        if (this==o) return true;
        else if (o==null || this.getClass()!=o.getClass()) return false;
        Vozilo other = (Vozilo) o;
        return this.id.equals(other.getId());
    }
    //get i set metode
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getProizvodjac() {
        return proizvodjac;
    }

    /**
     * Postavlja proizvodjaca vozila.
     * @param proizvodjac  Proizvodjac vozila.
     */
    public void setProizvodjac(String proizvodjac) {
        this.proizvodjac = proizvodjac;
    }
    /**
     * Vraća model vozila.
     * @return model vozila.
     */
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCijenaNabavke() {
        return cijenaNabavke;
    }

    public void setCijenaNabavke(int cijenaNabavke) {
        this.cijenaNabavke = cijenaNabavke;
    }

    public double getCijenaPoSekundi() {
        return cijenaPoSekundi;
    }

    public void setCijenaPoSekundi(int cijenaPoSekundi) {
        this.cijenaPoSekundi = cijenaPoSekundi;
    }
    public double getPrihodi() {
        return prihodi;
    }

    public void setPrihodi(double prihodi) {
        this.prihodi = prihodi;
    }

    public short getTrenutniNivoBaterije() {
        return trenutniNivoBaterije[0];
    }

    public void setTrenutniNivoBaterije(short [] trenutniNivoBaterije) {
        this.trenutniNivoBaterije = trenutniNivoBaterije;
    }
    /**
     * Dodaje kvar u listu kvarova.
     * @param k Kvar koji se dodaje.
     */
    public void dodajKvar(Kvarovi k){
        kvarovi.add(k);
    }
    public List<Kvarovi> getKvarovi(){
        return kvarovi;
    }
}
