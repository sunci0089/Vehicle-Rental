package dao;

import java.util.List;
/**
 * Interfejs {@code Dao} definise osnovne operacije za rad sa entitetima u skladistu podataka
 * koji je realizovan listom. Ovaj interfejs je genericki i može raditi sa bilo kojim tipom entiteta {@code T}.
 * <p>
 * Implementacije ovog interfejsa treba da obezbede konkretne nacine za citanje, dodavanje,
 * pretragu i dobijanje elemenata iz skladista podataka.
 * </p>
 * @param <T> Tip entiteta sa kojim se radi.
 */
public interface Dao<T> {
    /**
     * Vraća listu svih elemenata u skladištu podataka.
     * @return Lista svih elemenata tipa {@code T}.
     */
    List<T> getList();
    /**
     * Cita podatke iz datoteke i dodaje ih u listu podataka.
     * @param filePath Putanja do datoteke iz koje se citaju podaci.
     */
    void citajIz(String filePath);
    /**
     * Dodaje novi element u listu podataka.
     * @param element Element koji se dodaje u listu podataka.
     */
    void dodajElement(T element);
}
