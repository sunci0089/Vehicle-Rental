package GUI;

import dao.DaoVoziloImpl;
import pack.entity.Automobil;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import pack.entity.*;
import java.util.List;

/**
 * Klasa VozilaFrame predstavlja GUI prozor u kojem se prikazuju vozila
 * podeljena po kategorijama (automobili, bicikli i trotineti).
 * <p>
 * U prozoru se koriste tabele za svaku od kategorija, sa informacijama o svakom vozilu.
 * Podaci se generišu na osnovu kolekcije vozila koja je učitana preko DaoVoziloImpl objekta.
 * </p>
 */
@SuppressWarnings("serial")
public class VozilaFrame extends JFrame {

    VozilaFrame ovaj;
    private Vector<Object> koloneAutomobil, koloneBicikl, koloneTrotinet;
    private Vector<Vector<Object>> podaciAutomobil, podaciBicikl, podaciTrotinet;
    // kolekcija podataka vozila
    private DaoVoziloImpl vozilaDao;
    // Komponente okvira
    private JPanel contentPane;
    private JScrollPane scrollPane1;
    private JScrollPane scrollPane2;
    private JScrollPane scrollPane3;
    private JTable tableAutomobili;
    private JTable tableBicikli;
    private JTable tableTrotinet;
    /**
     * Kreira instancu VozilaFrame sa tabelama koje prikazuju vozila podeljena
     * po kategorijama.
     * @param voziloDao DaoVoziloImpl objekat koji sadrži listu vozila.
     */
    public VozilaFrame(DaoVoziloImpl voziloDao) {
        ovaj=this;
        this.vozilaDao=voziloDao;
        setTitle("Vozila");
        convertListToVector(vozilaDao.getList());
        initialize();
    }
    /**
     * Inicijalizuje GUI prozor i postavlja tabele u raspored.
     */
    private void initialize() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 815, 800);
        setLocationRelativeTo(null);
        this.contentPane = new JPanel();
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(this.contentPane);
        JPanel tablesPanel = new JPanel();
        tablesPanel.setLayout(new GridLayout(6, 1, 2, 2));

        // Add labels and tables to the panel
        tablesPanel.add(new JLabel("Automobili"));
        tablesPanel.add(getScrollPane1());

        tablesPanel.add(new JLabel("Bicikli"));
        tablesPanel.add(getScrollPane2());

        tablesPanel.add(new JLabel("Trotineti"));
        tablesPanel.add(getScrollPane3());

        // Add the tables panel to the CENTER of the contentPane
        this.contentPane.add(tablesPanel, BorderLayout.CENTER);

        setContentPane(this.contentPane);
    }
    /**
     * Kreira i vraća JScrollPane sa tabelom automobila.
     * @return JScrollPane sa tabelom automobila.
     */
    private JScrollPane getScrollPane1() {
        if (scrollPane1 == null) {
            scrollPane1 = new JScrollPane();
            scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane1.setViewportView(getTableAutomobili());
        }
        return scrollPane1;
    }
    /**
     * Kreira i vraca {@link JScrollPane} sa tabelom bicikala.
     * @return JScrollPane sa tabelom bicikala.
     */
    private JScrollPane getScrollPane2() {
        if (scrollPane2 == null) {
            scrollPane2 = new JScrollPane();
            scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane2.setViewportView(getTableBicikli());
        }
        return scrollPane2;
    }
    /**
     * Kreira i vraca {@link JScrollPane} sa tabelom trotineta.
     * @return JScrollPane sa tabelom trotineta.
     */
    private JScrollPane getScrollPane3() {
        if (scrollPane3 == null) {
            scrollPane3 = new JScrollPane();
            scrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane3.setViewportView(getTableTrotineti());
        }
        return scrollPane3;
    }
    /**
     * Kreira tabelu automobila sa odgovarajućim kolonama i podacima.
     * @return JTable sa podacima o automobilima.
     */
    private JTable createTableAutomobil() {
        koloneAutomobil = new Vector<>();
        koloneAutomobil.add("ID");
        koloneAutomobil.add("Proizvodjac");
        koloneAutomobil.add("Model");
        koloneAutomobil.add("Cijena nabavke");
        koloneAutomobil.add("Datum nabavke");
        koloneAutomobil.add("Opis");
        koloneAutomobil.add("Trenutni nivo baterije");

        return new JTable(new DefaultTableModel(podaciAutomobil, koloneAutomobil) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }
    /**
     * Konvertuje listu vozila u strukturu podataka koja se moze prikazati u tabelama.
     * @param vozila Lista vozila koja se prikazuje u tabelama.
     */
    public void convertListToVector(List<Vozilo> vozila) {
        podaciAutomobil = new Vector<>();
        podaciBicikl = new Vector<>();
        podaciTrotinet = new Vector<>();

        for (Vozilo vozilo : vozila) {
            Vector<Object> row = new Vector<>();
            row.add(vozilo.getId());
            row.add(vozilo.getProizvodjac());
            row.add(vozilo.getModel());
            row.add(vozilo.getCijenaNabavke());
            switch (vozilo.vrstaVozila()){
                case "automobil":
                    row.add(((Automobil) vozilo).getDatumNabavke().toString());
                    row.add(((Automobil) vozilo).getOpis());
                    row.add(vozilo.getTrenutniNivoBaterije());
                    podaciAutomobil.add(row);

                    break;
                case "bicikl":
                    row.add(((Bicikl) vozilo).getDometSaJednimPunjenjem());
                    row.add(vozilo.getTrenutniNivoBaterije());
                    podaciBicikl.add(row);
                    break;
                case "trotinet":
                    row.add(((Trotinet) vozilo).getMaxBrzina());
                    row.add(vozilo.getTrenutniNivoBaterije());
                    podaciTrotinet.add(row);
                    break;
                default: break;
            }
        }
    }
    /**
     * Kreira tabelu bicikala sa odgovarajućim kolonama i podacima.
     * @return JTable sa podacima o biciklima.
     */
    private JTable createTableBicikli() {
        koloneBicikl = new Vector<>();
        koloneBicikl.add("ID");
        koloneBicikl.add("Proizvodjac");
        koloneBicikl.add("Model");
        koloneBicikl.add("Cijena nabavke");
        koloneBicikl.add("Domet sa jednim punjenjem");
        koloneBicikl.add("Trenutni nivo baterije");

        return new JTable(new DefaultTableModel(podaciBicikl, koloneBicikl) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }
    /**
     * Kreira i vraća tabelu automobila.
     * @return JTable sa podacima o automobilima.
     */
    private JTable getTableAutomobili() {
        if (tableAutomobili == null) {
            tableAutomobili = createTableAutomobil();

            tableAutomobili.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tableAutomobili.setFillsViewportHeight(true);
            tableAutomobili.getColumnModel().getColumn(0).setPreferredWidth(50);
            tableAutomobili.getColumnModel().getColumn(1).setPreferredWidth(100);
            tableAutomobili.getColumnModel().getColumn(2).setPreferredWidth(100);
            tableAutomobili.getColumnModel().getColumn(3).setPreferredWidth(100);
            tableAutomobili.getColumnModel().getColumn(4).setPreferredWidth(100);
            tableAutomobili.getColumnModel().getColumn(5).setPreferredWidth(200);
            tableAutomobili.getColumnModel().getColumn(6).setPreferredWidth(50);
        }

        return tableAutomobili;
    }
    /**
     * Kreira tabelu trotineta sa odgovarajućim kolonama i podacima.
     * @return JTable sa podacima o trotinetima.
     */
    private JTable createTableTrotinet() {
        koloneTrotinet = new Vector<>();
        koloneTrotinet.add("ID");
        koloneTrotinet.add("Proizvodjac");
        koloneTrotinet.add("Model");
        koloneTrotinet.add("Cijena nabavke");
        koloneTrotinet.add("Max brzina");
        koloneTrotinet.add("Trenutni nivo baterije");

        return new JTable(new DefaultTableModel(podaciTrotinet, koloneTrotinet) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }
    /**
     * Kreira i vraća tabelu trotineta.
     * @return JTable sa podacima o trotinetima.
     */
    private JTable getTableTrotineti() {
        if (tableTrotinet == null) {
            tableTrotinet = createTableTrotinet();

            tableTrotinet.getColumnModel().getColumn(0).setPreferredWidth(50);
            tableTrotinet.getColumnModel().getColumn(1).setPreferredWidth(100);
            tableTrotinet.getColumnModel().getColumn(2).setPreferredWidth(100);
            tableTrotinet.getColumnModel().getColumn(3).setPreferredWidth(100);
            tableTrotinet.getColumnModel().getColumn(4).setPreferredWidth(100);
            tableTrotinet.getColumnModel().getColumn(5).setPreferredWidth(50);
        }
        return tableTrotinet;
    }
    /**
     * Kreira i vraća tabelu bicikli.
     * @return JTable sa podacima o biciklima.
     */
    private JTable getTableBicikli() {
        if (tableBicikli == null) {
            tableBicikli = createTableBicikli();
            tableBicikli.getColumnModel().getColumn(0).setPreferredWidth(50);
            tableBicikli.getColumnModel().getColumn(1).setPreferredWidth(100);
            tableBicikli.getColumnModel().getColumn(2).setPreferredWidth(100);
            tableBicikli.getColumnModel().getColumn(3).setPreferredWidth(100);
            tableBicikli.getColumnModel().getColumn(4).setPreferredWidth(100);
            tableBicikli.getColumnModel().getColumn(5).setPreferredWidth(50);
        }
        return tableBicikli;
    }

}