package GUI;

import dao.DaoVoziloImpl;
import pack.entity.Automobil;
import pack.entity.Bicikl;
import pack.entity.Trotinet;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;

/**
 * Klasa {@code VoziloSaNajvecimPrihodimaFrame} prikazuje vozila koja imaju najveće prihode po
 * kategorijama (automobil, bicikl, trotinet). Podaci o vozilima se učitavaju
 * iz serijalizovanih fajlova.
 */
public class VoziloSaNajvecimPrihodimaFrame extends JFrame {
    JPanel panel;
    JLabel labelAutomobil;
    JLabel labelBike;
    JLabel labelTrotinet;
    /**
     * Kreira instancu {@code VoziloSaNajvecimPrihodimaFrame},
     * serijalizuje vozila sa najvecim prihodima, koja se nakon deserijalizacije
     * prikazuju.
     * @param voziloDao DaoVoziloImpl objekat koji upravlja podacima o vozilima.
     * @param path Putanja gde se nalaze serijalizovani fajlovi vozila.
     */
    public VoziloSaNajvecimPrihodimaFrame(DaoVoziloImpl voziloDao, String path) {
        voziloDao.serializeAll(path);
        setTitle("Vozila sa najvecim prihodima: ");
        setSize(700, 180);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Create a panel with GridLayout to hold the labels
        panel = new JPanel(new GridLayout(3, 1, 10, 10));

        // Create the labels
        Automobil a= DaoVoziloImpl.<Automobil>deserialize(path+"automobil.ser");
        if (a!=null)
            labelAutomobil = new JLabel("Automobil: " + a.toString()+" Prihodi: "+ String.format("%.2f",a.getPrihodi()));
        else labelAutomobil = new JLabel("Automobil: prihodi 0");
        Bicikl b= DaoVoziloImpl.<Bicikl>deserialize(path+"bicikl.ser");
        if (b!=null)
            labelBike = new JLabel("Bicikl: "+ b.toString()+" Prihodi: "+ String.format("%.2f",b.getPrihodi()));
        else labelBike = new JLabel("Bicikl: prihodi 0");
        Trotinet t= DaoVoziloImpl.<Trotinet>deserialize(path+"trotinet.ser");
        if (t!=null)
            labelTrotinet = new JLabel("Trotinet: "+ t.toString()+" Prihodi: "+ String.format("%.2f",t.getPrihodi()));
        else labelTrotinet = new JLabel("Trotinet: prihodi 0");
        // Add the labels to the panel
        panel.add(labelAutomobil);
        panel.add(labelBike);
        panel.add(labelTrotinet);

        // Add the panel to the frame
        add(panel);

        // Make the frame visible
        setVisible(true);
    }
}

