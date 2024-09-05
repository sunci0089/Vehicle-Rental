package pack.entity;
/**
 * Klasa {@code Bicikl} predstavlja konkretan tip vozila, tj. bicikl.
 * Nasleđuje klasu {@code Vozilo}.
 * <p>
 * Klasa obuhvata dodatne atribute specificne za bicikle, kao što je domet sa jednim punjenjem.
 * Takođe pruza implementaciju metoda za vraćanje vrste vozila i njegovo predstavljanje
 * u formatu stringa.
 * </p>
 */
public class Bicikl extends Vozilo {
    int dometSaJednimPunjenjem;
    /**
     * Konstruktor klase {@code Bicikl}.
     * @param id ID bicikla.
     * @param proizvodjac Proizvođač bicikla.
     * @param model Model bicikla.
     * @param cijena Cijena nabavke bicikla.
     * @param domet Domet bicikla sa jednim punjenjem.
     */
    public Bicikl(String id,String proizvodjac, String model, int cijena,int domet){
        super(id,proizvodjac,model,cijena);
        this.dometSaJednimPunjenjem=domet;
    }
    /**
     * Vraća vrstu vozila kao {@code "bicikl"}.
     * @return Vrsta vozila.
     */
    @Override
    public String vrstaVozila(){
        return "bicikl";
    }
    /**
     * Vraća string predstavu bicikla.
     * @return String predstava bicikla u formatu:
     * ID, proizvođač, model, cijena nabavke, domet.
     */
    @Override
    public String toString() {
        return "ID: "+ getId()+ " Proizvodjac: "+ getProizvodjac() +
                " Model: " + getModel() + " Cijena nabavke: " +
                getCijenaNabavke()+ " Domet: "+ getDometSaJednimPunjenjem();

    }

    public int getDometSaJednimPunjenjem() {
        return dometSaJednimPunjenjem;
    }

    public void setDometSaJednimPunjenjem(int dometSaJednimPunjenjem) {
        this.dometSaJednimPunjenjem = dometSaJednimPunjenjem;
    }
}
