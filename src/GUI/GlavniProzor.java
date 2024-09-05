package GUI;

import dao.DaoIznajmljivanjeImpl;
import pack.entity.Iznajmljivanje;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.List;
/**
 * {@code GlavniProzor} klasa predstavlja glavnu graficku komponentu aplikacije.
 * Ona nasledjuje JFrame i koristi se za kreiranje glavnog prozora
 * sa svim neophodnim menijima i mapom kretanja vozila.
 */
@SuppressWarnings("serial")
public class GlavniProzor extends JFrame {
    /**
     * Brojanje iznajmljivanja koja su se desila, te se po zavrsetku odlucuje da li
     * korisnik ostvaruje popust ili ne
     */
    public static int brojIznajmljivanja=0;
    /**
     * Objekat za rad sa kolekcijom iznajmljivanja
     */
    private DaoIznajmljivanjeImpl iznajmljivanjeDao;

    // Komponente korisnickog interfejsa
    private JFrame ovaj;
    private JMenuBar menuBar;
    private JMenu mnAplikacija;
    private JMenuItem mntmIzlaz;
    private JMenu mnVozila;
    private JMenuItem mntmVozila;
    private JMenu mnIzvjestaji;
    private JMenuItem mntmIzvjestaji, mntmKvarovi, mntmVozilaPrihodi;
    private JLabel vrijemeLabel;
    private JPanel contentPanel, gridPanel;

    /**
     * Konstruktor klase GlavniProzor.
     * @param iznajmljivanjeDao instanca klase DaoIznajmljivanjeImpl koja sluzi za pristup podacima o iznajmljivanjima
     */
    public GlavniProzor(DaoIznajmljivanjeImpl iznajmljivanjeDao) {
        this.iznajmljivanjeDao = iznajmljivanjeDao;
        initialize();
    }
    /**
     * Inicijalizacija komponenata glavnog prozora:
     * <p>Menu bar,
     * kreiranje mape sa naznacenim uzim i sirim dijelom grada.
     * Zatim zapocinje simulacija mape kretanja vozila pozivom funkcije
     * {@code startSimulation} i kreiranje nove niti
     * </p>
     */
    private void initialize() {
        ovaj = this;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                int rezultat = JOptionPane.showOptionDialog(ovaj,
                        "Da li ste sigurni da želite zatvoriti aplikaciju?",
                        "Potvrda zatvaranja", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null,
                        null,
                        null);
                if (rezultat == JOptionPane.YES_OPTION)
                    System.exit(0);
            }
        });
        setTitle("PJ2 - iznajmljivanje vozila");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 815, 420);
        setLocationRelativeTo(null);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setJMenuBar(getMenuBar_1());

        //MAPA kretanja vozila

        contentPanel = new JPanel(new BorderLayout());
        setContentPane(contentPanel);

        setVrijemeLabel("");
        vrijemeLabel.setSize(500,20);
        contentPanel.add(vrijemeLabel, BorderLayout.NORTH);

        // Create the grid panel
        gridLabels = new JLabel[20][20];
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(GRID_SIZE + 1, GRID_SIZE + 1)); // +1 for numbering

        // Prazan gornji lijevi ugao
        gridPanel.add(new JLabel(""));

        // X vrijednosti na vrhu
        for (int col = 0; col < GRID_SIZE; col++) {
            JLabel colLabel = new JLabel(String.valueOf(col), SwingConstants.CENTER);
            gridPanel.add(colLabel);
        }
        // Y vrijednosti i labele mape
        for (int y = 0; y < GRID_SIZE; y++) {
            // Y sa lijeva
            JLabel rowLabel = new JLabel(String.valueOf(y), SwingConstants.CENTER);
            gridPanel.add(rowLabel);
                for (int x = 0; x < GRID_SIZE; x++) {
                    gridLabels[x][y] = new JLabel("", SwingConstants.CENTER);
                    gridLabels[x][y].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    gridLabels[x][y].setOpaque(true);

                    // Boja mape u odnosu na podrucje grada
                    if (x >= 5 && x < 15 && y >= 5 && y < 15) {
                        gridLabels[x][y].setBackground(Color.CYAN); // Inner city
                    } else {
                        gridLabels[x][y].setBackground(Color.WHITE); // Outer city
                    }
                    gridPanel.add(gridLabels[x][y]);
                }
            }
        contentPanel.add(gridPanel, BorderLayout.CENTER);
        setContentPane(contentPanel);

        new Thread(this::startSimulation).start();
    }
    /**
     * Metoda koja boji celije grida prema lokaciji (uzi i siri dio).
     */
    private void colorGirdLabel(){
        for (int y = 0; y < GRID_SIZE; y++)
            for (int x = 0; x < 20; x++)
            {
                if (y >= 5 && y < 15 && x >= 5 && x < 15) {
                gridLabels[x][y].setBackground(Color.CYAN); // Narrow area
                } else {
                gridLabels[x][y].setBackground(Color.WHITE); // Wide area
                }
                gridLabels[x][y].setText("");
            }
    }
    /**
     * Metoda koja pokrece simulaciju kretanja vozila na mapi.
     * <p>
     * Inicijalizuje se mapa objekata prema vremenu i listi vise iznajmljivanja
     * koja su se tada dogodila. Simulacija zapocinje za 3 sekunde.
     * Pravi se novi objekat {@code kretanje} za svako kretanje koji rade u odvojenim nitima.
     * Nakon zavrsetka kolekcije kretanja za dato vrijeme, prikaz mape stoji jos 3 sekunde, a zatim se
     * osvjezava pozivom {@code colorGirdLabel}
     * </p>
     */
    private void startSimulation() {
        // Groupisemo iznajmljivanja po vremenu pocetka
        Map<LocalDateTime, List<Iznajmljivanje>> groupedIznajmljivanja;
        groupedIznajmljivanja = new TreeMap<>();
        for (Iznajmljivanje i : iznajmljivanjeDao.getList()) {
            groupedIznajmljivanja.computeIfAbsent(i.getDateTime(), k -> new ArrayList<>()).add(i);
        }
        //pricekamo 3 sekunde i zapocinjemo simulaciju
        try{
            Thread.sleep(3000);}
        catch (InterruptedException e){
            System.out.println("StartSimulation: interrupted exceotion");
        }
        // Iteriramo vremena i datume
        for (LocalDateTime date : groupedIznajmljivanja.keySet()) {

            List<Iznajmljivanje> vehiclesForDate = groupedIznajmljivanja.get(date);

            //kreiramo listu objekata za simulaciju (kretanja)
            List<Kretanje> kretanja = new ArrayList<>();

            //kreiramo nova kretanja i smjestamo u listu
            for (Iznajmljivanje v : vehiclesForDate) {
                kretanja.add(new Kretanje(v, gridLabels));
            }

            //pokrecemo sva kretanja za dato vrijeme
            SwingUtilities.invokeLater(() -> vrijemeLabel.setText("Datum i vrijeme: "
                    + date.toString().replace("T"," ")));
            for (Kretanje kretanje: kretanja)
                kretanje.start();

            //treba pricekati da se sva kretanja zavrse
            for (Kretanje kretanje: kretanja) {
                try {
                    kretanje.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // Zatim cekamo 5 sekundi pa krecemo novi datum
            try {
                    Thread.sleep(3000);
                    colorGirdLabel();
                    Thread.sleep(2000);
            } catch (InterruptedException e) {
                    e.printStackTrace();
            }



        }
    }

    /*
     * Graficki elementi mape
     */

    private static final int GRID_SIZE = 20; // 20x20 grid
    private JLabel[][] gridLabels;
    /**
     * Postavlja labelu za prikaz datuma i vremena.
     * @param vrijeme String koji predstavlja datum i vreme
     */
    public void setVrijemeLabel(String vrijeme){
        vrijemeLabel = new JLabel("Datum i vrijeme: "+
                vrijeme.replace("T",""));
    }


    /*
     * Menu bars
     */

    private JMenuBar getMenuBar_1() {
        if (menuBar == null) {
            menuBar = new JMenuBar();
            menuBar.add(getMnAplikacija());
            menuBar.add(getMnVozila());
            menuBar.add(getMnIzvjestaji());
            //menuBar.add(getMnUpravljajOtkupom());
            //menuBar.add(getMnIzvestaji());
        }
        return menuBar;
    }

    private JMenu getMnAplikacija() {
        if (mnAplikacija == null) {
            mnAplikacija = new JMenu("Aplikacija");
            mnAplikacija.setMnemonic('A');
            mnAplikacija.add(getMntmIzlaz());
        }
        return mnAplikacija;
    }

    private JMenuItem getMntmIzlaz() {
        if (mntmIzlaz == null) {
            mntmIzlaz = new JMenuItem("Izlaz");
            mntmIzlaz.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    ovaj.getToolkit()
                            .getSystemEventQueue()
                            .postEvent(
                                    new WindowEvent(ovaj,
                                            WindowEvent.WINDOW_CLOSING));
                }
            });
            mntmIzlaz.setMnemonic('I');
            mntmIzlaz.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                    InputEvent.ALT_MASK));
        }
        return mntmIzlaz;
    }

    private JMenu getMnIzvjestaji() {
        if (mnIzvjestaji == null) {
            mnIzvjestaji = new JMenu("Izvjestaj");
            mnIzvjestaji.setMnemonic('Z');
            mnIzvjestaji.add(getMntmIzvjestaji());
            mnIzvjestaji.add(getMntmKvarovi());
            mnIzvjestaji.add(getMntmVozilaPrihodi());
        }
        return mnIzvjestaji;
    }
    private JMenuItem getMntmVozilaPrihodi() {
        if (mntmVozilaPrihodi == null) {
            mntmVozilaPrihodi = new JMenuItem("Vozila sa najvise prihoda...");
            mntmVozilaPrihodi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new VoziloSaNajvecimPrihodimaFrame(iznajmljivanjeDao.getDaoVozila(),
                            Main.PROP.getProperty("filePathVozilaSaNajvisePrihoda")
                                    .replace("${file.separator}", System.getProperty("file.separator")));
                }
            });
            mntmVozilaPrihodi.setMnemonic('Z');
        }
        return mntmVozilaPrihodi;
    }

    private JMenuItem getMntmIzvjestaji() {
        if (mntmIzvjestaji == null) {
            mntmIzvjestaji = new JMenuItem("Izvjestaji...");
            mntmIzvjestaji.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String path= Main.PROP.getProperty("filePathRacuni").replace("${file.separator}", System.getProperty("file.separator"));
                    iznajmljivanjeDao.generisiSumarniIzvjestaj(path);
                    iznajmljivanjeDao.generisiSveDnevneIzvjestaje(path);
                    new IzvjestajFrame(iznajmljivanjeDao.getSumarniIzvjestaj(),
                            iznajmljivanjeDao.getDnevniIzvjestaji()).setVisible(true);
                }
            });
            mntmIzvjestaji.setMnemonic('Z');
        }
        return mntmIzvjestaji;
    }
    private JMenuItem getMntmKvarovi() {
        if (mntmKvarovi == null) {
            mntmKvarovi = new JMenuItem("Kvarovi...");
            mntmKvarovi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new KvaroviFrame(iznajmljivanjeDao.getDaoVozila().getList()).setVisible(true);
                }
            });
            mntmKvarovi.setMnemonic('K');
        }
        return mntmKvarovi;
    }

    private JMenuItem getMntmVozila() {
        if (mntmVozila == null) {
            mntmVozila = new JMenuItem("Vozila...");
            mntmVozila.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new VozilaFrame(iznajmljivanjeDao.getDaoVozila()).setVisible(true);
                }
            });
            mntmVozila.setMnemonic('R');
        }
        return mntmVozila;
    }

    private JMenu getMnVozila() {
        if (mnVozila == null) {
            mnVozila = new JMenu("Vozila");
            mnVozila.setMnemonic('V');
            mnVozila.add(getMntmVozila());
        }
        return mnVozila;
    }

}