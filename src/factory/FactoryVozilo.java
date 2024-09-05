package factory;
import pack.entity.*;
import pack.parser.Parser;

import java.time.LocalDate;
/**
 * Klasa {@code FactoryVozilo} pruza staticke metode za kreiranje instanci vozila.
 * Ova klasa koristi podatke koje prosledi korisnik da bi kreirala konkretne vrste
 * vozila na osnovu datih parametara.
 * <p>
 * Metoda {@code novoVozilo} koristi parametre kao sto su ID, proizvodjac, model,
 * datum proizvodnje, cijena, domet, brzina, opis i vrsta da bi kreirala odgovarajuci
 * objekat tipa {@code Vozilo}. Ako parametri nisu validni za odredjenu vrstu vozila,
 * metoda vraca {@code null}.
 * </p>
 */
public class FactoryVozilo {
    /**
     * Kreira novu instancu vozila na osnovu prosledjenih parametara.
     * <p>
     * Ako je vrsta vozila {@code automobil} i opis nije prazan,
     * ako se datum moze parsirati metoda ce pokusati
     * da kreira {@code Automobil} objekat koristeci navedene parametre. Ako je vrsta
     * vozila {@code trotinet} i brzina je veca od 0, metoda ce kreirati {@code Trotinet}.
     * Ako je vrsta vozila {@code bicikl} i domet je veci od 0, metoda ce kreirati
     * {@code Bicikl}. U suprotnom, metoda vraca {@code null}.
     * </p>
     * @param id ID vozila.
     * @param proizvodjac Proizvodjac vozila.
     * @param model Model vozila.
     * @param datum Datum proizvodnje vozila u formatu d.M.yyyy koji je prihvatljiv za {@code LocalDate}.
     * @param cijena Cena vozila.
     * @param domet Domet vozila (za bicikl).
     * @param brzina Brzina vozila (za trotinet).
     * @param opis Opis vozila (za automobil).
     * @param vrsta Vrsta vozila (automobil, trotinet, bicikl).
     * @return Nova instanca vozila odgovarajuce vrste ili {@code null} ako parametri nisu validni.
     */
    public static Vozilo novoVozilo(String id,String proizvodjac, String model,String datum
            ,int cijena,int domet,int brzina,String opis,String vrsta)
    {
        if (vrsta.equalsIgnoreCase("automobil") && !opis.isEmpty()){
            LocalDate d = Parser.parseDate(datum.substring(0,datum.length()-1));
            if (d!=null)
                return new Automobil(id,proizvodjac,model,d,cijena,opis);
        }
        else if (vrsta.equalsIgnoreCase("trotinet") && brzina>0)
            return new Trotinet(id,proizvodjac,model,cijena,brzina);
        else if (vrsta.equalsIgnoreCase("Bicikl") && domet>0)
            return new Bicikl(id,proizvodjac,model,cijena,domet);
        return null;
    }
}
