import java.util.Locale; // Landessprache, hier Komma statt Punkt
import java.util.Scanner; // Benutzereingaben einlesen über Tastatur (System.in)
import java.time.LocalTime; // speichert und verarbeitet reine Uhrzeiten
import java.time.format.DateTimeFormatter; //formattiert Daten in Zeitobjekte mit definiertem 
import java.time.format.DateTimeParseException; //fängt fehlerhafte Zeiteingaben ab

public class AzubiQuickCheck{
    public static void main(String[]args){
        Locale.setDefault(Locale.GERMANY);
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter timeFormatter=DateTimeFormatter.ofPattern("HH:mm");

        //feste Azubi-Parameter (38,5h / 5Tage = 7 h 42 min = 462 Minuten pro Tag
        final int tagesSoll=462;

        System.out.println("\n === AZUBI QUICK-CHECK ===");
        System.out.println("38,5h - 5 Tage die Woche - 7,42h am Tag\n");

         // 1. Einstempelzeit einlesen mit try-catch
        LocalTime kommen = null;
        while (true) {
            System.out.print("Einstempelzeit heute eingeben (Format HH:mm, z.B. 07:30): \n");
            String eingabeZeit = scanner.next();

            try {
                kommen = LocalTime.parse(eingabeZeit, timeFormatter);
                break; // Hat geklappt! Wir springen aus der Schleife.
            } catch (DateTimeParseException e) {
                // Falls die Eingabe ungültig war, fangen wir den Fehler ab:
                System.out.println("Ungültiges Format! Bitte um Korrektur:\n");
            }
        }
        int eingabeVorwocheMinuten=0;
         while(true){ //Abfangen von Eingabefehlern
        // 2. Abfrage Zeitkonto Vorwoche
        
        System.out.println(
                "Bitte Stand des Zeitkontos aus der Vorwoche eingeben (Format HH:mm oder -HH:mm, z.B. 02:30 oder -10:15)");
        String eingabeVorwoche = scanner.next();
            try{     
                // Rechenweg bei Minuszeichen
                if (eingabeVorwoche.startsWith("-")){
                //Minus abschneiden, parsen und direkt mit -1 multiplizieren
                LocalTime zeitVorwoche=LocalTime.parse(eingabeVorwoche.substring(1), timeFormatter);
                eingabeVorwocheMinuten=(zeitVorwoche.getHour()*60+zeitVorwoche.getMinute())*-1;
                }else{
                //normal rechnen bei positivem Zeitkonto
                LocalTime zeitVorwoche=LocalTime.parse(eingabeVorwoche, timeFormatter);
                eingabeVorwocheMinuten=(zeitVorwoche.getHour()*60+zeitVorwoche.getMinute());
                }
                break; //wenn parsing klappt, springen wir aus der Schleife raus
            }catch (DateTimeParseException e){
                System.out.println("Ungültige Eingabe, versuchen Sie es nochmal mit dem richtigen Format:");
            }
        }
        // 3. Erfassung des ZielZeitkontostandes
        int zielZeitkontominuten=0;
        while(true){
            System.out.println("Mit welchem Zeitkontostand wollen Sie aus der Woche raus? (Format HH:mm, z.B. 00:00 oder 02:30)");
            String eingabeZiel=scanner.next();
            try{ //der parse ins Zeitformat filtert falsche Eingaben (Minus, Länge, Buchstaben)
                LocalTime zeitZiel=LocalTime.parse(eingabeZiel, timeFormatter);
                zielZeitkontominuten=zeitZiel.getHour()*60+zeitZiel.getMinute();
                break; //erfolgreich raus aus der Schleife
            }catch (DateTimeParseException e) {
                System.out.println("Ungültige Eingabe, versuchen Sie es nochmal mit dem richtigen Format:");
            }
        }
        //4. Netto-Arbeitszeit für den letzten Tag berechnen
        int nettoArbeitsMinuten=tagesSoll+zielZeitkontominuten-eingabeVorwocheMinuten;

        //5. Überprüfung der Grenzwerte
        if (nettoArbeitsMinuten>600){
            final int MAX_NETTO_MINUTEN=600;
            final int MAX_PAUSE_MINUTEN=45; //gesetzlich bei > 9h
            //späteste Ausstempelzeit
            LocalTime maxFeierAbend=kommen
            .plusMinutes(MAX_NETTO_MINUTEN)
            .plusMinutes(MAX_PAUSE_MINUTEN);

        int zeitkontoNachMaxRechnungMinuten=nettoArbeitsMinuten-MAX_NETTO_MINUTEN;
        System.out.println("Über 10 Stunden sind nicht erlaubt, wir sind nicht in Asien. Spätestens um " +maxFeierAbend.format(timeFormatter)+ " Uhr musst du ausstempeln (45 min. Pause inkl.)!");
        System.out.println("Es verbleiben dann noch " + (zeitkontoNachMaxRechnungMinuten/60)+ " Std. "+(zeitkontoNachMaxRechnungMinuten%60)+" Min. auf deinem Zeitkonto.");
        scanner.close();
        return;
        }
        //6. gesetzliche Pausenregelung
        int pauseMinuten=0;
        if (nettoArbeitsMinuten>540){
            pauseMinuten=45;
        }else if (nettoArbeitsMinuten>360){
            pauseMinuten=30;
        }else{
            pauseMinuten=0;
        }
        //7. FeierabendUhrzeit berechnen
        LocalTime feierabend=kommen
                .plusMinutes(nettoArbeitsMinuten)
                .plusMinutes(pauseMinuten);

        //8. Ergebnis
        System.out.println("Du musst um "+feierabend.format(timeFormatter)+" Uhr gehen. Dabei sind " +pauseMinuten+" Pause eingerechnet.");
        }
        }
    