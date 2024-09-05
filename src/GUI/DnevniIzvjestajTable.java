package GUI;

import pack.izvjestaji.DnevniIzvjestaj;
import pack.izvjestaji.Izvjestaj;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Model tabele za prikaz dnevnih izveštaja.
 * Nasleđuje {@link AbstractTableModel} i omogućava rad sa tabelarnim prikazom podataka o dnevnim izveštajima.
 */
@SuppressWarnings("serial")
public class DnevniIzvjestajTable extends AbstractTableModel { //samo dnevni
    private List<DnevniIzvjestaj> podaci;
    String[] kolone = new String[] { "Datum", "Ukupan prihod",
            "Ukupan popust", "Ukupno promocije", "Iznos voznji",
            "Iznos za odrzavanje", "Iznos za popravke" };

    /**
     * Konstruktor klase DnevniIzvjestajTable.
     * @param podaci lista dnevnih izveštaja koji će biti prikazani u tabeli
     */
    public DnevniIzvjestajTable(List<DnevniIzvjestaj> podaci) {
        setPodaci(podaci);
    }
    public void setPodaci(List<DnevniIzvjestaj> podaci) {
        this.podaci = podaci;
    }
    /**
     * Vraca dnevni izveštaj na određenom indeksu reda.
     * @param rowIndex indeks reda
     * @return dnevni izveštaj na datom indeksu reda
     */
    public Izvjestaj getIzvjestajAtRow(int rowIndex) {
        return podaci.get(rowIndex);
    }
    @Override
    public int getColumnCount() {
        return kolone.length;
    }
    @Override
    public String getColumnName(int column) {
        return kolone[column];
    }
    @Override
    public int getRowCount() {
        return podaci.size();
    }
    /**
     * Vraća vrednost ćelije u tabeli na datom indeksu reda i kolone.
     * @param rowIndex indeks reda
     * @param columnIndex indeks kolone
     * @return vrednost ćelije na datom indeksu reda i kolone
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DnevniIzvjestaj red = podaci.get(rowIndex);
        if (columnIndex == 0)
            return red.getDatum().toString();
        else if (columnIndex == 1)
            return red.getUkupanPrihod();
        else if (columnIndex == 2)
            return red.getUkupanPopust();
        else if (columnIndex == 3)
            return red.getUkupnoPromocije();
        else if (columnIndex == 4)
            return red.getIznosVoznji();
        else if (columnIndex == 5)
            return red.getIznosZaOdrzavanje();
        else if (columnIndex == 6)
            return red.getIznosZaPopravke();
        else
            return null;
    }

}
