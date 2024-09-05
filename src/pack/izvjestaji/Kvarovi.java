package pack.izvjestaji;

import java.time.LocalDateTime;

/**
 * Entitet koji reprezentuje kvar cuvajuci datum i vrijeme
 * {@code vrijeme} kada se desio te razlog kvara {@code opisKvara}.
 */
public class Kvarovi {
    LocalDateTime vrijeme;
    String opisKvara;

    /**
     * Konstruktor klase kvarovi koji postavlja vrijeme i opis kvara
     * @param vrijeme Datum i vrijeme kvara
     * @param opisKvara Razlog kvara
     */
    public Kvarovi(LocalDateTime vrijeme, String opisKvara){
        this.vrijeme=vrijeme;
        this.opisKvara="Razlog kvara";
    }
    //get i set metode
    public LocalDateTime getVrijeme() {
        return vrijeme;
    }
    public void setVrijeme(LocalDateTime vrijeme) {
        this.vrijeme = vrijeme;
    }
    public String getOpisKvara() {
        return opisKvara;
    }
    public void setOpisKvara(String opisKvara) {
        this.opisKvara = opisKvara;
    }
}
