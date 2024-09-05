package dao;

import factory.FactoryVozilo;
import pack.entity.Automobil;
import pack.entity.Bicikl;
import pack.entity.Trotinet;
import pack.entity.Vozilo;
import pack.parser.Parser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Implementacija DAO (Data Access Object) interfejsa za upravljanje i smjestanje
 * podataka u kolekciju vozila {@code vozila}.
 */
public class DaoVoziloImpl implements Dao<Vozilo> {
    private List<Vozilo> vozila;
    /**
     * Konstruktor klase DaoVoziloImpl. Inicijalizuje listu vozila.
     */
    public DaoVoziloImpl(){
        vozila= new ArrayList<>();
    }
    /**
     * Vraca listu svih vozila.
     * @return lista vozila
     */
    @Override
    public List<Vozilo> getList() {
        return vozila;
    }
    /**
     * Ucitaj vozila iz CSV fajla na datoj putanji.
     * Ako fajl nije prazan parsiraju se podaci, te se
     * ispravni dodaju u listu svih vozila {@code vozila}
     * @param filePath putanja do CSV fajla
     */
    @Override
    public void citajIz(String filePath) {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Citaj liniju zaglavlja
            String headerLine = br.readLine();
            if (headerLine == null) {
                throw new IOException("Prazan CSV file");
            }
            // Citaj linije podataka
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 9) {
                    String id=values[0];
                    String proizvodjac=values[1];
                    String model=values[2];
                    if(id.isEmpty() || proizvodjac.isEmpty() || model.isEmpty())
                        System.err.println("citajVozilo: prazno polje!" + line);
                    else{
                        String datum=values[3];

                        int cijena = Parser.parseInt(values[4]);
                        int domet= Parser.parseInt(values[5]);
                        int brzina= Parser.parseInt(values[6]);
                    if(cijena<0) System.err.println("citajVozilo: pogresna cijena!" + line);
                    else {
                        String opis = values[7];
                        String vrsta = values[8];

                        //provjera da li postoji vozilo sa istim id
                        if (pretrazi(id) == null) {
                            Vozilo vozilo = FactoryVozilo.novoVozilo(
                                    id, proizvodjac, model, datum, cijena, domet, brzina, opis, vrsta);
                            if(vozilo!=null)
                                this.dodajElement(vozilo);
                            else System.err.println("citajVozilo: nepravilan unos" + line);
                        } else System.err.println("citajVozilo: duplo vozilo! " + line);
                        }
                    }
                } else {
                    System.err.println("citajVozilo: nepravilna linija! " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
    /**
     * Dodaj vozilo u listu vozila.
     * @param vozilo vozilo koje treba dodati
     */
    @Override
    public void dodajElement(Vozilo vozilo) {
        vozila.add(vozilo);
    }
    /**
     * Pronađi vozilo po ID-u.
     * @param id ID vozila
     * @return vozilo sa datim ID-om, ili null ako ne postoji
     */
    public Vozilo pretrazi(String id){
        for (Vozilo vozilo: vozila){
            if (vozilo.getId().equals(id))
                return vozilo;
        }
        return null;
    }
    /**
     * Vraća vozilo sa najviše prihoda određene klase.
     * @param type klasa vozila za koju se traži vozilo sa najviše prihoda
     * @param <T> tip vozila
     * @return vozilo sa najviše prihoda, ili null ako ne postoji
     */
    public <T extends Vozilo> T voziloSaNajvisePrihoda(Class<T> type){
        T najboljeVozilo = null;
        double maxValue = 0;
            for (Vozilo vozilo : vozila) {
                if (type.isInstance(vozilo))
                    if (vozilo.getPrihodi()>maxValue){
                        maxValue=vozilo.getPrihodi();
                        najboljeVozilo=(T) vozilo;
                    }
            }
            return najboljeVozilo;
    }
    /**
     * Serijalizuje vozilo u fajl.
     * @param vozilo vozilo koje treba serijalizovati
     * @param path putanja do foldera gde će se sacuvati serijalizovani fajl .ser
     */
    private void serialize(Vozilo vozilo,String path){
        path+=File.separator+vozilo.vrstaVozila()+".ser";
        File file = new File(path);
        // Pravimo novi file
        try {
            // provjeri da li postoji file
            if (!file.exists()) {
                // napravi novi file
                if (file.createNewFile()) {
                    System.out.println("serializeVozilo: napravljen fajl: " + file.getPath());
                }
            }
        } catch (IOException e) {
            System.err.println("serializeVozilo: greska pri pravljenu novog fajla!");
            e.printStackTrace();
        }
        try (FileOutputStream fileOut = new FileOutputStream(path);
             ObjectOutputStream out = new ObjectOutputStream(fileOut))
        {
            out.writeObject(vozilo);
            System.out.println("Vozilo sa najvise prihoda: serializovan "+ path);
        } catch (IOException i) {
            i.printStackTrace();
            System.err.println("serializeVozilo: greska pri serijalizaciji");
        }
    }
    /**
     * Serijalizuje vozila sa najvise prihoda za tri vrste pojedinacno.
     * @param path putanja do foldera gde će se sačuvati serijalizovani fajlovi
     */
    public void serializeAll(String path){
        Automobil automobil = this.<Automobil>voziloSaNajvisePrihoda(Automobil.class);
        Bicikl bicikl = this.<Bicikl>voziloSaNajvisePrihoda(Bicikl.class);
        Trotinet trotinet = this.<Trotinet>voziloSaNajvisePrihoda(Trotinet.class);

        if(automobil!=null) {
            serialize(automobil,path);
            System.out.println("Automobil sa najvise prihoda: " + automobil.getId()
                    + " Prihodi: " + automobil.getPrihodi());
        }else System.out.println("Automobil sa najvise prihoda ne postoji");
        if(bicikl!=null) {
            serialize(bicikl,path);
            System.out.println("Bicikl sa najvise prihoda: " + bicikl.getId()
                    + " Prihodi: " + bicikl.getPrihodi());
        } else System.out.println("Bicikl sa najvise prihoda ne postoji");
        if(trotinet!=null) {
            serialize(trotinet,path);
            System.out.println("Trotinet sa najvise prihoda: " + trotinet.getId()
                    + " Prihodi: " + trotinet.getPrihodi());
        }else System.out.println("Trotinet sa najvise prihoda ne postoji");
    }
    /**
     * Deserijalizuje vozilo iz fajla.
     * @param filePath putanja do fajla sa serijalizovanim vozilom
     * @param <T> tip vozila
     * @return deserijalizovano vozilo, ili null ako dođe do greške
     */
    public static <T extends Vozilo> T deserialize(String filePath){
        T vozilo = null;
        try (FileInputStream fileIn = new FileInputStream(filePath);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            vozilo = (T) in.readObject();
            System.out.println(filePath + " deserialized");
            return vozilo;
        }catch (FileNotFoundException e) {
            System.err.println("Prihodi su 0 - nepostoji vozilo sa najvise prihoda " + filePath);
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }
        return null;
    }
}
