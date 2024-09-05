package pack.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Prilagodjene staticke metode za parsiranje podataka tipa int, LocalDate,
 * i LocalDateTime
 */
public class Parser {
    /**
     * Metoda za parsiranje stringa u cijeli broj (int).
     * Ako je string nevalidan, vraća se -1, umjesto
     * {@code NumberFormatExceptiona} metode {@code parseInt}
     * iz klase {@link Integer}
     * @param s String koji treba parsirati.
     * @return int vrednost ili -1 ako parsiranje ne uspije.
     */
    public static int parseInt(String s){
        int i=-1;
        try {
            i=Integer.parseInt(s);
        }
        catch (NumberFormatException e)
        {
            return i;
        }
        return i;
    }
    /**
     * Metoda za parsiranje datuma u formatu "d.M.yyyy".
     * Vraća LocalDate ili null ako parsiranje ne uspije.
     * @param datum String koji predstavlja datum u formatu "d.M.yyyy".
     * @return LocalDate ili null ako parsiranje ne uspe.
     */
    public static LocalDate parseDate(String datum){
        try {
            //System.out.println("Datum: "+ datum);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");
            String popravljen = datum + " 00:00";
            // Parse the string to a LocalDateTime object

            return (LocalDateTime.parse(popravljen, formatter)).toLocalDate();
            //System.out.println("Datum: "+ this.datum.toString());
        }catch (DateTimeParseException e){
            return null;
        }
    }
    /**
     * Metoda za parsiranje datuma i vremena u formatu "d.M.yyyy HH:mm".
     * Vraća LocalDateTime ili null ako parsiranje ne uspe.
     * @param datum String koji predstavlja datum i vreme u formatu "d.M.yyyy HH:mm".
     * @return LocalDateTime ili null ako parsiranje ne uspe.
     */
    public static LocalDateTime parseDateTime(String datum){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");
            return LocalDateTime.parse(datum, formatter);
        }catch (DateTimeParseException e){
            return null;
        }
    }
}
