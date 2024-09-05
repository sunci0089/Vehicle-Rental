package pack.izvjestaji;

import java.time.LocalDate;

/**
 * {@code DnevniIzvjestaj} nasljedjuje klasu izvjestaj i prosiruje je
 * datumom {@code datum} dana za koji se izvjestaj generise
 */
public class DnevniIzvjestaj extends Izvjestaj{
    //datum formata d.M.yyyy
    private LocalDate datum=null;

    /**
     * vraca tip izvjestaja
     * @return dnevni tip izvjestaja
     */
    @Override
    public String tip(){
        return "dnevni";
    }
    //get i set metode
    public void setDatum(LocalDate datum){
        this.datum = datum;
    }
    public LocalDate getDatum(){
        return datum;
    }
}
