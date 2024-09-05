package pack.entity;

/**
 * Interfejs {@code Punjivo} predstavlja entitet koji se moze puniti i prazniti.
 * Implementirajuće klase treba da obezbede logiku za postepeno punjenje i pražnjenje.
 * Prosiruje {@code Runnable} interfejs, što znaci da implementirajuće klase treba
 * da obezbede implementaciju u višenitnom okruženju.
 */
public interface Punjivo extends Runnable{
    /**
     * Metod za praznjenje baterije tokom vremena.
     * <p>
     * Implementacije treba da simuliraju postepeno smanjenje nivoa baterije
     * na osnovu specificnih obrazaca koristenja.
     * </p>
     *
     * @throws InterruptedException ako je nit koja izvrsava metodu prekinuta
     *                              tokom praznjenja baterije.
     */
    void prazniBateriju() throws InterruptedException;
    /**
     * Metod za punjenje baterije tokom vremena.
     * <p>
     * Implementacije treba da simuliraju postepeno povecanje nivoa baterije
     * na osnovu specificnih obrazaca koristenja.
     * </p>
     *
     * @throws InterruptedException ako je nit koja izvrsava metodu prekinuta
     *                              tokom punjenja baterije.
     */
    void puniBateriju() throws InterruptedException;
}
