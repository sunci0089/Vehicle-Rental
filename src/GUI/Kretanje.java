package GUI;

import pack.entity.Iznajmljivanje;
import pack.izvjestaji.Kvarovi;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
/**
 * Klasa {@link Kretanje} predstavlja niti koje simuliraju kretanje vozila na mapi
 * realizovanoj mreza (grid).
 * <p>
 * Ona nasleđuje klasu {@link Thread} i omogućava simultano kretanje vozila
 * sa vizuelnim ažuriranjem na mreži, kao i praćenje stanja baterije i vremena.
 * Po zavrsetku kretanja, azuriraju se podaci iznajmljivanja i izdaje racun.
 * </p>
 */
class Kretanje extends Thread {
    private String name;
    private LocalDateTime startDate;
    Iznajmljivanje iznajmljivanje;
    private volatile JLabel[][] gridLabels;
    /**
     * Konstruktor klase Kretanje.
     * @param iznajmljivanje objekat koji sadrži informacije o iznajmljivanju vozila.
     * @param gridLabels dvodimenzionalni niz JLabel-ova koji predstavlja mrezu kretanja.
     */
    public Kretanje(Iznajmljivanje iznajmljivanje, JLabel[][] gridLabels) {
        this.name = iznajmljivanje.getVozilo().getId()+"("+iznajmljivanje.getVozilo().getTrenutniNivoBaterije()+") ";
        this.startDate = iznajmljivanje.getDateTime();
        this.iznajmljivanje=iznajmljivanje;
        this.gridLabels = gridLabels;
    }
    /**
     * Vraća datum i vreme početka kretanja.
     * @return datum i vreme početka kretanja.
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * Pokretanje niti kretanja.
     * <p>
     * Pokrece se nova nit za punjenje baterije
     * pristupanjem run metode vozila koje je koristeno u datom iznajmljivanju.
     * Poziva se funkcija {@code moveVehicle} za pomjeranje vozila
     * Po zavrsetku se inkrementuje {@code brojIznajmljivanja} i za svako
     * 10 iznajmljivanje daje popust. Ako ima kvarova dodaju se u listu kvarova
     * vozila. Zatim se na osnovu azuriranih podataka izdaje racun.
     * </p>
     */
    @Override
    public void run() {
        try {
            //krece praznjenje baterije
            Thread baterija=new Thread(iznajmljivanje.getVozilo());
            baterija.start();
            synchronized (iznajmljivanje.getVozilo()){
                    iznajmljivanje.getVozilo().kreceSe=true;
                    iznajmljivanje.getVozilo().notify();
            }
            //pokrecemo simulaciju
            moveVehicle();

            //kada zavrsi racunamo da li dobija popust
            GlavniProzor.brojIznajmljivanja++;

            if (GlavniProzor.brojIznajmljivanja % 10 == 0) {
                iznajmljivanje.setImaPopust(true);
            }
            //zatim pisemo racun
            iznajmljivanje.racunajCijenu();
            //dodajemo kvar u listu kvarova vozila
            if (iznajmljivanje.getImaKvar()){
                iznajmljivanje.getVozilo().dodajKvar(
                        new Kvarovi(iznajmljivanje.getDatumVijemeIznajmljivanja(),
                                "Razlog kvara"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Simulira kretanje vozila kroz mrežu.
     * <p>
     * Vozilo se krece tako sto se u dvije odvojene niti pomjeraju x i y
     * koordinate svakih {@code timePerXStep} x i y {@code timePerYStep}
     * vremenskih intervala za koje se vozilo zadrzava na jednom polju.
     * Treca nit koja se pokrece u isto vrijeme sluzi da azurira poziciju
     * na mrezi {@code girdLabels} svakih {@code timePerStep} vremenskih
     * intervala, sve dok vozilo ne dodje na finalnu poziciju.
     * </p>
     * @throws InterruptedException ako je nit prekinuta tokom pauze.
     */
    private void moveVehicle() throws InterruptedException {
        int totalXSteps = Math.abs(iznajmljivanje.getLokacijaOstavljanja()[0] -
                iznajmljivanje.getLokacijaPreuzimanja()[0])+1;
        int totalYSteps = Math.abs(iznajmljivanje.getLokacijaOstavljanja()[1] -
                iznajmljivanje.getLokacijaPreuzimanja()[1])+1;
        // Time per step for X and Y axes
        int timePerXStep = 1000 * iznajmljivanje.getTrajanje() / totalXSteps;
        int timePerYStep = 1000 * iznajmljivanje.getTrajanje() / totalYSteps;

        int steps = Math.max(totalXSteps,totalYSteps);
        int timePerStep = 1000 * iznajmljivanje.getTrajanje() / steps;

        // Direction of movement: +1 (forward) or -1 (backward)
        int xDirection = (iznajmljivanje.getLokacijaOstavljanja()[0] >
                iznajmljivanje.getLokacijaPreuzimanja()[0]) ? 1 : -1;
        int yDirection = (iznajmljivanje.getLokacijaOstavljanja()[1] >
                iznajmljivanje.getLokacijaPreuzimanja()[1]) ? 1 : -1;

        // Shared x and y positions between threads, 0 is previous 1 is next
        int[] x = {iznajmljivanje.getLokacijaPreuzimanja()[0] , iznajmljivanje.getLokacijaPreuzimanja()[0]};
        int[] y = {iznajmljivanje.getLokacijaPreuzimanja()[1] , iznajmljivanje.getLokacijaPreuzimanja()[1]};

        gridLabels[x[0]][y[0]].setText(name); // Update new position
        gridLabels[x[0]][y[0]].setForeground(Color.RED);
        gridLabels[x[0]][y[0]].setBackground(Color.YELLOW);

        gridLabels[iznajmljivanje.getLokacijaOstavljanja()[0]][iznajmljivanje.getLokacijaOstavljanja()[1]].
            setBackground(Color.GRAY);

        // Thread for X movement
        Thread moveXThread = new Thread(() -> {
            while(x[1]!=iznajmljivanje.getLokacijaOstavljanja()[0]){
                try {
                    Thread.sleep(timePerXStep); // Wait for X interval
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                synchronized (x) {
                    x[1] = x[0] + xDirection;
                }
            }});

        // Thread for Y movement
        Thread moveYThread = new Thread(() -> {
            while(y[1]!=iznajmljivanje.getLokacijaOstavljanja()[1]){
                try {
                    Thread.sleep(timePerYStep); // Wait for Y interval
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                synchronized (y) {
                    y[1] = y[0]+ yDirection;
                }
            }
        });

        boolean [] timeout = {false};
        Thread refreshRate = new Thread(() -> {
            boolean josJedan=false;
            do{
                synchronized (this) {
                    String stack=gridLabels[x[0]][y[0]].getText().replace(name,"");
                    gridLabels[x[0]][y[0]].setText(stack); // Clear previous position

                    name=iznajmljivanje.getVozilo().getId()+"("+iznajmljivanje.getVozilo().getTrenutniNivoBaterije()+") ";

                    if (!gridLabels[x[1]][y[1]].getText().equals("")) {
                        // Multiple vehicles in the same cell - you can customize how to handle this
                        gridLabels[x[1]][y[1]].setText(name + gridLabels[x[1]][y[1]].getText());
                    } else {
                        gridLabels[x[1]][y[1]].setText(name); // Add vehicle label normally
                    }
                    // Update new position
                    gridLabels[x[1]][y[1]].setForeground(Color.RED);
                    gridLabels[x[1]][y[1]].setBackground(Color.YELLOW);
                    x[0]=x[1];
                    y[0]=y[1];
                }
                if(!timeout[0]){
                try {
                    sleep(timePerStep);
                } catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }}
                if(timeout[0] && josJedan) josJedan=false;
                else if(timeout[0]) josJedan=true;
            }while (!timeout[0] || josJedan);
        });
        // pokreni threadove
        moveXThread.start();
        moveYThread.start();
        // svako timePerStep se iscrtava putanja
        refreshRate.start();
        //pricekati da se izvrsi tacno vrijeme trajanja
        sleep(iznajmljivanje.getTrajanje()*1000);
        // Wait for both threads to complete
        moveXThread.join();
        moveYThread.join();
        //staje praznjenje baterije baterije
        synchronized (iznajmljivanje.getVozilo()){
            iznajmljivanje.getVozilo().kreceSe=false;
            iznajmljivanje.getVozilo().notify();
        }
        timeout[0]=true;
        refreshRate.join();
    }
}