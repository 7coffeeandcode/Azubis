import java.util.Locale; // Landessprache, hier Komma statt Punkt
import java.util.Scanner; // Benutzereingaben einlesen über Tastatur (System.in)
import java.time.LocalTime; // speichert und verarbeitet reine Uhrzeiten
import java.time.format.DateTimeFormatter; //formattiert Daten in Zeitobjekte mit definiertem 
import java.time.format.DateTimeParseException; //fängt fehlerhafte Zeiteingaben ab

public class AzubiQuickCheck{
    public static void main(String[]args){
        Locale.setDefault(Locale.GERMANY);
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter timeFormatter=DateTimeFormatter.ofPattern("HH:mm)");

        //feste Azubi  Parameter (38,5h / 5Tage = 7 h 42 min = 462 Minuten pro Tag
        int tagesSoll=462;

        System.out.println("=== AZUBI QUICK-CHECK (38,5h / 5 Tage) ===");
        System.out.println("Tagessoll: 7 Std. 42 Min.\n");

         // 1. Einstempelzeit einlesen mit try-catch
        LocalTime kommen = null;
        while (true) {
            System.out.print("Einstempelzeit heute eingeben (Format HH:mm, z.B. 07:30): ");
            String eingabeTime = scanner.next();

            try {
                kommen = LocalTime.parse(eingabeTime, timeFormatter);
                break; // Hat geklappt! Wir springen aus der Schleife.
            } catch (DateTimeParseException e) {
                // Falls die Eingabe ungültig war, fangen wir den Fehler ab:
                System.out.println("Ungültiges Format! Bitte genau im Format HH:mm eingeben (z. B. 08:00).\n");
            }
        }