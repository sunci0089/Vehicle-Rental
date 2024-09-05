package pack.entity;

import java.time.LocalDate;
/**
 * Klasa {@code Automobil} predstavlja konkretan tip vozila, tj. automobil.
 * Nasleđuje klasu {@code Vozilo}.
 * <p>
 * Klasa obuhvata dodatne atribute specificne za automobile, kao što su datum nabavke
 * i opis. Takodje pruza implementaciju metoda za vracanje vrste vozila i njegovo
 * predstavljanje u formatu stringa.
 * </p>
 */
public class Automobil extends Vozilo {
    private LocalDate datumNabavke;
    private String opis;
    //prevoz vise ljudi
    /**
     * Konstruktor klase {@code Automobil}.
     * @param id ID automobila.
     * @param proizvodjac Proizvođač automobila.
     * @param model Model automobila.
     * @param datum Datum nabavke automobila.
     * @param cijena Cena nabavke automobila.
     * @param opis Opis automobila.
     */
    public Automobil(String id,String proizvodjac, String model, LocalDate datum,int cijena, String opis){
        super(id,proizvodjac,model,cijena);
        this.datumNabavke=datum;
        this.opis=opis;
    }
    /**
     * Vraća vrstu vozila kao {@code "automobil"}.
     * @return Vrsta vozila.
     */
    @Override
    public String vrstaVozila(){
        return "automobil";
    }
    /**
     * Vraća string predstavu automobila.
     * @return String predstava automobila u formatu:
     * ID, proizvodjac, model, datum nabavke, cijena nabavke, opis.
     */
    @Override
    public String toString() {
        return "ID: "+ getId()+ " Proizvodjac: "+ getProizvodjac() +
                " Model: " + getModel() + " Datum nabavke: " + getDatumNabavke()+
                " Cijena nabavke: " + getCijenaNabavke()+ " Opis: "+ getOpis();
    }

    public LocalDate getDatumNabavke() {
        return datumNabavke;
    }

    public void setDatumNabavke(LocalDate datumNabavke) {
        this.datumNabavke = datumNabavke;
    }
    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }
}
