package GUI;

import pack.entity.Vozilo;
import pack.izvjestaji.Kvarovi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;
/**
 * Klasa KvaroviFrame predstavlja prozor (frame) koji prikazuje tabelu sa
 * informacijama o kvarovima vozila.
 * Nasleđuje {@link JFrame} i koristi {@link JTable} za prikaz podataka o kvarovima.
 */
public class KvaroviFrame extends JFrame {

        KvaroviFrame ovaj;
        private Vector<Object> kolone;
        private Vector<Vector<Object>> podaci;
        //kolekcija vozila iz kojih citamo liste kvarova za svako vozilo pojedinacno
        private List<Vozilo> vozila; //iz kojih citamo liste kvarova za svako vozilo pojedinacno

        private JPanel contentPane;
        private JScrollPane scrollPane;
        private JTable tableKvarovi;

    /**
     * Konstruktor klase KvaroviFrame.
     * @param vozila lista vozila iz koje se prikupljaju podaci o kvarovima.
     */
    public KvaroviFrame(List<Vozilo> vozila){
            ovaj=this;
            this.vozila=vozila;
            setTitle("Kvarovi");
            convertListToVector();
            initialize();
    }
    /**
     * Inicijalizuje prozor, postavlja osnovne karakteristike i komponente.
     */
    private void initialize() {
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setBounds(100, 100, 815, 800);
            setLocationRelativeTo(null);
            this.contentPane = new JPanel();
            this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
            this.contentPane.setLayout(new BorderLayout(0, 0));
            setContentPane(this.contentPane);

            // Add the tables panel to the CENTER of the contentPane
            this.contentPane.add(getScrollPane(), BorderLayout.CENTER);

            setContentPane(this.contentPane);
    }
    /**
     * Vraća JScrollPane koji sadrži tabelu sa kvarovima.
     * @return JScrollPane sa tabelom.
     */
    private JScrollPane getScrollPane() {
            if (scrollPane == null) {
                scrollPane = new JScrollPane();
                scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scrollPane.setViewportView(getTableKvarovi());
            }
            return scrollPane;
    }
    /**
     * Kreira JTable sa podacima o kvarovima.
     * @return {@link JTable} sa podacima o kvarovima.
     */
    private JTable createTableKvarovi() {
            kolone = new Vector<>();
            kolone.add("Vrsta vozila");
            kolone.add("ID vozila");
            kolone.add("Vrijeme kvara");
            kolone.add("Opis");

            return new JTable(new DefaultTableModel(podaci, kolone) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
    }
    /**
     * Konvertuje listu kvarova vozila u vektor podataka koji se koriste
     * za popunjavanje tabele.
     */
    public void convertListToVector() {
            podaci = new Vector<>();
            for (Vozilo v: vozila) {
                for (Kvarovi kvar: v.getKvarovi()){
                    Vector<Object> row = new Vector<>();
                    row.add(v.vrstaVozila());
                    row.add(v.getId());
                    row.add(kvar.getVrijeme().toString().replace("T"," "));
                    row.add(kvar.getOpisKvara());
                    podaci.add(row);
                }
            }
    }
    /**
     * Vraca JTable sa podacima o kvarovima.
     * @return JTable sa podacima o kvarovima.
     */
    private JTable getTableKvarovi() {
            if (tableKvarovi == null) {
                tableKvarovi = createTableKvarovi();
            }
            return tableKvarovi;
        }
    }
