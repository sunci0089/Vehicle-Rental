package pack.izvjestaji;

import pack.entity.Automobil;
import pack.entity.Bicikl;
import pack.entity.Trotinet;
import pack.entity.Vozilo;

import java.time.LocalDate;
/**
 * Klasa koja predstavlja izvještaj o poslovanju,
 * sa brojevnim podacima o prihodima, popustima, troškovima,
 * promocijama, održavanju, popravkama i porezu, te {@code datum}
 * koji oznacava dan za koji pravimo izvjestaj
 */
public abstract class Izvjestaj {
    private double ukupanPrihod=0;
    private double ukupanPopust=0;
    private double ukupnoPromocije=0;
    private double iznosVoznji=0;
    private double iznosZaOdrzavanje;
    private double iznosZaPopravke=0;
    /**
     * Vraća tekstualni prikaz izvještaja sa svim ključnim podacima.
     * @return String prikaz izvještaja.
     */
    @Override
    public String toString(){
        return "Ukupan prihod: "+ ukupanPrihod +" Ukupan popust: "+ ukupanPopust + " Ukupno promocije: " +
                ukupnoPromocije + "\nUkupan iznos svih voznji: " + iznosVoznji + " Iznos za odrzavanje: " +
                iznosZaOdrzavanje + "\nUkupan iznos za popravke: "+ iznosZaPopravke;//+ " Ukupni troskovi: " +
               // ukupniTroskovi + " Ukupan porez: " + ukupanPorez;
                //za dnevni ne idu dva zadnja
    }
    public abstract String tip();
    /**
     * Računa dodatne troškove poput održavanja, ukupnih troškova i poreza.
     */
    /**
     * Dodaje prihod izvještaju, tako sto se sumira sa prethodnim
     * @param prihod String koji predstavlja prihod.
     */
    public void racunajPrihod(String prihod)
    {
        try {
            ukupanPrihod += Double.parseDouble(prihod);
        } catch (Exception e){
            System.err.println("racunajPrihod: greska pri parsiranju");
        }
    }
    /**
     * Dodaje popust izvještaju, tako sto se sumira sa prethodnim
     * @param popust String koji predstavlja iznos popusta.
     */
    public void racunajPopust(String popust)
    {
        try {
            ukupanPopust += Double.parseDouble(popust);
        } catch (Exception e){
            System.err.println("racunajPopust: greska pri parsiranju");
        }
    }
    /**
     * Dodaje promocije izvještaju, tako sto se na postojecu sumira nova
     * @param promocija String koji predstavlja dodatni iznos promocije.
     */
    public void racunajPromocije(String promocija)
    {
        try {
            ukupnoPromocije += Double.parseDouble(promocija);
        } catch (Exception e){
            System.err.println("racunajPromocije: greska pri parsiranju");
        }
    }
    /**
     * Dodaje iznos vožnji izvještaju.
     * @param voznja String koji predstavlja iznos vožnje.
     */
    public void racunajIznosVoznji(String voznja)
    {
        try {
            iznosVoznji += Double.parseDouble(voznja);
        } catch (Exception e){
            try{
            iznosVoznji += Double.parseDouble(voznja.substring(2));}
            catch (Exception ex){
            System.err.println("racunajIznosVoznji: greska pri parsiranju");
            }
        }
    }
    /**
     * Računa troškove održavanja kao 20% od ukupnog prihoda.
     */
    public void racunajIznosZaOdrzavanje()
    {
        iznosZaOdrzavanje=ukupanPrihod*0.2;
    }
    /**
     * Računa troškove popravki. Na osnovu tipa vozila uzima se koeficijent
     * i njegova nabavna cijena.
     * @param vozilo Vozilo koje se popravlja.
     */
    public void racunajIznosZaPopravke(Vozilo vozilo)
    {
        if(vozilo==null) return;
        double koef=0;
            if (vozilo instanceof Automobil)
                koef=0.07;
            else if (vozilo instanceof Bicikl)
                koef=0.04;
            else if (vozilo instanceof Trotinet)
                koef=0.02;
            iznosZaPopravke+=vozilo.getCijenaNabavke()*koef;
    }
    //get i set metode
    public double getUkupanPrihod() {
        return ukupanPrihod;
    }

    public void setUkupanPrihod(double ukupanPrihod) {
        this.ukupanPrihod = ukupanPrihod;
    }

    public double getUkupanPopust() {
        return ukupanPopust;
    }

    public void setUkupanPopust(double ukupanPopust) {
        this.ukupanPopust = ukupanPopust;
    }

    public double getUkupnoPromocije() {
        return ukupnoPromocije;
    }

    public void setUkupnoPromocije(double ukupnoPromocije) {
        this.ukupnoPromocije = ukupnoPromocije;
    }

    public double getIznosVoznji() {
        return iznosVoznji;
    }

    public void setIznosVoznji(double iznosVoznji) {
        this.iznosVoznji = iznosVoznji;
    }

    public double getIznosZaOdrzavanje() {
        return iznosZaOdrzavanje;
    }

    public void setIznosZaOdrzavanje(double iznosZaOdrzavanje) {
        this.iznosZaOdrzavanje = iznosZaOdrzavanje;
    }

    public double getIznosZaPopravke() {
        return iznosZaPopravke;
    }

    public void setIznosZaPopravke(double iznosZaPopravke) {
        this.iznosZaPopravke = iznosZaPopravke;
    }

}
