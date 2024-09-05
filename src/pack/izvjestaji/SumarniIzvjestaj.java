package pack.izvjestaji;

/**
 * Naslijedjuje klasu {@link Izvjestaj} i sadrzi funkcije
 * za racunanje dodatnih vrijednosti specificnih za sumarni izvjestaj
 */
public class SumarniIzvjestaj extends Izvjestaj{
    private double ukupniTroskovi; //20% ukupnog prihoda
    private double ukupanPorez;

    /**
     * Računa ukupne troškove kao 20% od ukupnog prihoda.
     */
    public void racunajUkupneTroskove()
    {
        ukupniTroskovi=getUkupanPrihod()*0.2;
    }
    /**
     * Računa porez kao 10% od iznosa nakon sto odbijemo troškove održavanja,
     * popravki i ukupnih troškova.
     */
    public void racunajUkupanPorez(){
        ukupanPorez=0.1*(getUkupanPrihod()-getIznosZaOdrzavanje()-getIznosZaPopravke()-ukupniTroskovi);
    }

    /**
     * Vraca tip izvjestaja
     * @return sumarni tip izvjestaja
     */
    @Override
    public String tip(){
        return "sumarni";
    }
    //get i set metode
    public double getUkupniTroskovi() {
        return ukupniTroskovi;
    }

    public void setUkupniTroskovi(double ukupniTroskovi) {
        this.ukupniTroskovi = ukupniTroskovi;
    }

    public double getUkupanPorez() {
        return ukupanPorez;
    }

    public void setUkupanPorez(double ukupanPorez) {
        this.ukupanPorez = ukupanPorez;
    }

}
