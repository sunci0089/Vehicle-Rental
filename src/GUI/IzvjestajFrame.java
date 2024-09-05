package GUI;

import pack.izvjestaji.DnevniIzvjestaj;
import pack.izvjestaji.Izvjestaj;
import pack.izvjestaji.SumarniIzvjestaj;

import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

/**
 * Klasa koja predstavlja prozor sa izveštajima u GUI aplikaciji.
 * Nasleđuje {@link JFrame} i sadrži tabele za prikaz sumarnog i dnevnog izveštaja.
 */
@SuppressWarnings("serial")
public class IzvjestajFrame extends JFrame {
        private Vector<Object> koloneSumarni, koloneDnevni;
        private Vector<Vector<Object>> podaciSumarni, podaciDnevni;
        private SumarniIzvjestaj sumarni;
        private List<DnevniIzvjestaj> dnevni;
        private JPanel contentPane;
        private JScrollPane scrollPaneSumarni, scrollPaneDnevni;
        private JTable tableSumarniIzvjestaj, tableDnevniIzvjestaj;

    /**
     * Konstruktor klase IzvjestajFrame.
     * @param sumarni sumarni izveštaj koji će biti prikazan u tabeli
     * @param dnevni lista dnevnih izveštaja koji će biti prikazani u tabeli
     */
    public IzvjestajFrame(SumarniIzvjestaj sumarni, List<DnevniIzvjestaj> dnevni) {
            this.sumarni=sumarni;
            this.dnevni=dnevni;
            convertListToVector();
            setTitle("Izvještaji");
            initialize();
    }
    /**
     * Konvertuje podatke iz listi u vektore koji se koriste za prikaz u tabelama.
     * @return {@code true} ako je konverzija uspešna, {@code false} inače
     */
    public void convertListToVector() {
        podaciSumarni = new Vector<>();
        podaciDnevni = new Vector<>();
        //popunjavanje podataka za sumarni izvjestaj
        Vector<Object> sRow = new Vector<>();
        sRow.add(String.format("%.4f",sumarni.getUkupanPrihod()));
        sRow.add(String.format("%.4f",sumarni.getUkupanPopust()));
        sRow.add(String.format("%.4f",sumarni.getUkupnoPromocije()));
        sRow.add(String.format("%.4f",sumarni.getIznosVoznji()));
        sRow.add(String.format("%.4f",sumarni.getIznosZaOdrzavanje()));
        sRow.add(String.format("%.4f",sumarni.getIznosZaPopravke()));
        sRow.add(String.format("%.4f",sumarni.getUkupniTroskovi()));
        sRow.add(String.format("%.4f",sumarni.getUkupanPorez()));
        podaciSumarni.add(sRow);
        //popunjavanje podataka za dnevni izvjestaj
        for (DnevniIzvjestaj d : dnevni) {
            Vector<Object> row = new Vector<>();
            row.add(d.getDatum());
            row.add(String.format("%.4f",d.getUkupanPrihod()));
            row.add(String.format("%.4f",d.getUkupanPopust()));
            row.add(String.format("%.4f",d.getUkupnoPromocije()));
            row.add(String.format("%.4f",d.getIznosVoznji()));
            row.add(String.format("%.4f",d.getIznosZaOdrzavanje()));
            row.add(String.format("%.4f",d.getIznosZaPopravke()));
            podaciDnevni.add(row);
        }
    }
    /**
     * Inicijalizuje prozor sa tabelama i dodaje ih u sadržaj prozora.
     */
    private void initialize() {
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setBounds(100, 100, 815, 600);
            setLocationRelativeTo(null);
            this.contentPane = new JPanel();
            this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
            this.contentPane.setLayout(new BorderLayout(0, 0));
            setContentPane(this.contentPane);

            JPanel tablesPanel = new JPanel();
            tablesPanel.setLayout(new GridLayout(4, 1, 2, 2));

            // Add labels and report tables
            tablesPanel.add(new JLabel("Sumarni Izvještaj"));
            tablesPanel.add(getScrollPaneSumarni());

            tablesPanel.add(new JLabel("Dnevni Izvještaj"));
            tablesPanel.add(getScrollPaneDnevni());

            this.contentPane.add(tablesPanel, BorderLayout.CENTER);
            setContentPane(this.contentPane);
    }
    /**
     * Vraca {@link JScrollPane} za sumarni izveštaj.
     * @return {@link JScrollPane} koji sadrži tabelu sa sumarnim izveštajem
     */
    private JScrollPane getScrollPaneSumarni() {
            if (scrollPaneSumarni == null) {
                scrollPaneSumarni = new JScrollPane();
                scrollPaneSumarni.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scrollPaneSumarni.setViewportView(getTableSumarniIzvjestaj());
            }
            return scrollPaneSumarni;
    }
    /**
     * Vraca {@link JScrollPane} za dnevni izveštaj.
     * @return {@link JScrollPane} koji sadrži tabelu sa dnevnim izveštajem
     */
    private JScrollPane getScrollPaneDnevni() {
            if (scrollPaneDnevni == null) {
                scrollPaneDnevni = new JScrollPane();
                scrollPaneDnevni.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scrollPaneDnevni.setViewportView(getTableDnevniIzvjestaj());
            }
            return scrollPaneDnevni;
    }
    /**
     * Vraca {@link JTable} za sumarni izveštaj.
     * @return {@link JTable} koja prikazuje sumarni izvestaj
     */
    private JTable getTableSumarniIzvjestaj() {
            if (tableSumarniIzvjestaj == null) {
                tableSumarniIzvjestaj = createTableSumarniIzvjestaj();
                tableSumarniIzvjestaj.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tableSumarniIzvjestaj.setFillsViewportHeight(true);
            }
            return tableSumarniIzvjestaj;
    }
    /**
     * Vraća {@link JTable} za dnevni izveštaj.
     *
     * @return {@link JTable} koja prikazuje dnevni izveštaj
     */
    private JTable getTableDnevniIzvjestaj() {
            if (tableDnevniIzvjestaj == null) {
                tableDnevniIzvjestaj = createTableDnevniIzvjestaj();
                tableDnevniIzvjestaj.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tableDnevniIzvjestaj.setFillsViewportHeight(true);
            }
            return tableDnevniIzvjestaj;
    }
    /**
     * Kreira {@link JTable} za sumarni izveštaj sa odgovarajućim kolonama i podacima.
     * @return {@link JTable} za sumarni izveštaj
     */
    private JTable createTableSumarniIzvjestaj() {
            koloneSumarni = new Vector<>();
            koloneSumarni.add("Ukupni Prihod");
            koloneSumarni.add("Ukupni Popust");
            koloneSumarni.add("Ukupne Promocije");
            koloneSumarni.add("Ukupan iznos svih Voznji");
            koloneSumarni.add("Ukupni iznos Održavanja");
            koloneSumarni.add("Ukupni iznos Popravki");
            koloneSumarni.add("Ukupni Troškovi");
            koloneSumarni.add("Ukupni Porez");

            return new JTable(new DefaultTableModel(podaciSumarni, koloneSumarni) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
    }
    /**
     * Kreira {@link JTable} za dnevni izveštaj sa odgovarajućim kolonama i podacima.
     * @return {@link JTable} za dnevni izveštaj
     */
    private JTable createTableDnevniIzvjestaj() {
            koloneDnevni = new Vector<>();
            koloneDnevni.add("Datum");
            koloneDnevni.add("Ukupni Prihod");
            koloneDnevni.add("Ukupni Popust");
            koloneDnevni.add("Ukupne Promocije");
            koloneDnevni.add("Ukupan iznos svih Voznji");
            koloneDnevni.add("Ukupni iznos Održavanja");
            koloneDnevni.add("Ukupni iznos Popravki");

            return new JTable(new DefaultTableModel(podaciDnevni, koloneDnevni) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
    }
}