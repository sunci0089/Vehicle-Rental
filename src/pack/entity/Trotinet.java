package pack.entity;
/**
 * Klasa {@code Trotinet} predstavlja konkretan tip vozila, tj. trotinet.
 * Nasleđuje klasu {@code Vozilo}.
 * <p>
 * Klasa obuhvata dodatne atribute specifične za trotinete, kao što je maksimalna brzina.
 * Takođe pruža implementaciju metoda za vraćanje vrste vozila i njegovo predstavljanje
 * u formatu stringa.
 * </p>
 */
public class Trotinet extends Vozilo {
    private int maxBrzina;
    /**
     * Konstruktor klase {@code Trotinet}.
     * @param id ID trotineta.
     * @param proizvodjac Proizvođač trotineta.
     * @param model Model trotineta.
     * @param cijena Cena nabavke trotineta.
     * @param maxBrzina Maksimalna brzina trotineta.
     */
    public Trotinet(String id,String proizvodjac,String model, int cijena,int maxBrzina){
        super(id,proizvodjac,model,cijena);
        this.maxBrzina=maxBrzina;
    }
    /**
     * Vraća vrstu vozila kao {@code "trotinet"}.
     * @return Vrsta vozila.
     */
    @Override
    public String vrstaVozila(){
        return "trotinet";
    }
    /**
     * Vraća string predstavu trotineta.
     * @return String predstava trotineta u formatu:
     * ID, proizvođač, model, cijena nabavke, maksimalna brzina.
     */
    @Override
    public String toString() {
        return "ID: "+ getId()+ " Proizvodjac: "+ getProizvodjac() +
                " Model: " + getModel() + " Cijena nabavke: " + getCijenaNabavke() +
                " Max brzina: "+ getMaxBrzina();
    }
    public int getMaxBrzina() {
        return maxBrzina;
    }

    public void setMaxBrzina(int maxBrzina) {
        this.maxBrzina = maxBrzina;
    }
}
