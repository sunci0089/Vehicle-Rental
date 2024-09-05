package pack.entity;
/**
 * Klasa {@code Korisnik} predstavlja entitet sa korisnickim informacijama.
 * <p>
 * Klasa obuhvata atrbute vezane za korisnika, kao sto je ime,
 * identifikacioni dokument i broj vozacke dozvole.
 * Takodje pruza implementaciju metoda za vracanje
 * i postavljanje parametara
 * </p>
 */
public class Korisnik {
    private String ime;
    private String dokumentID;
    private String brVozacke;
    /**
     * Konstruktor klase {@code Korisnik}. Identifikacioni dokument
     * i broj vozacke dozvole se generisu na osnovu imena korisnika.
     * @param id Ime korisnika (username).
     */
    public Korisnik(String id){
        this.ime=id;
        this.dokumentID=id+".dokument";
        this.brVozacke=id+".vozacka";
    }
    //get i set metode
    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getDokumentID() {
        return dokumentID;
    }

    public void setDokumentID(String dokumentID) {
        this.dokumentID = dokumentID;
    }

    public String getBrVozacke() {
        return brVozacke;
    }

    public void setBrVozacke(String brVozacke) {
        this.brVozacke = brVozacke;
    }
}
