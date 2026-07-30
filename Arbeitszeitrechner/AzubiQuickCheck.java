import java.util.Locale; // Landessprache, hier Komma statt Punkt
import java.util.Scanner; // Benutzereingaben einlesen über Tastatur (System.in)
import java.time.LocalTime; // speichert und verarbeitet reine Uhrzeiten
import java.time.format.DateTimeFormatter; //formattiert Daten in Zeitobjekte mit definiertem Format
import java.time.format.DateTimeParseException; //fängt fehlerhafte Zeiteingaben ab

public class AzubiQuickCheck{
    public static void main(String[]args){
        Locale.setDefault(Locale.GERMANY);
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter timeFormatter=DateTimeFormatter.ofPattern("HH:mm");

        //feste Azubi-Parameter (38,5h / 5Tage = 7 h 42 min = 462 Minuten pro Tag
        final int tagesSoll=462;

        System.out.println("\n ======= AZUBI QUICK-CHECK =======");
        System.out.println("\nDas ist ein Arbeitszeitrechner. Anhand der Eingabe des aktuellen und gewünschten Zeitkontostandes \nermittelt es die exakte Ausstempelzeit für den jeweiligen Tag. Ideal zum Wochenabschluss.\nNicht ins Minus, aber so früh wie möglich nach Hause :)");
        System.out.println("\nGrunddaten: 38,5h - 5 Tage die Woche - 7,42h am Tag");
        System.out.println("\nBeachte die Kernarbeitszeit von 9-15 Uhr. Freitags kannst du früher Schluss machen.\nEinstempeln vor 6 Uhr und Ausstempeln nach 18 Uhr darfst du nicht.");
        System.out.println("\nMusterwoche: \nDu baust von Mo-Do täglich 30min. Plus auf dein Zeitkonto. Am Freitag hast du dann zwei \nPlusstunden, setzt deine Zielzeit für kommende Woche wieder auf 00:00 und kannst nach \n5 Std. 42 Min. gehen, Pause wird erst ab 6h fällig.\n");

         // 1. Einstempelzeit einlesen mit try-catch
        LocalTime kommen = null;
        LocalTime minKernzeit=LocalTime.of(6,0);
        LocalTime maxKernzeit=LocalTime.of(9,0);
        while (true) {
            System.out.print("Einstempelzeit heute eingeben (Format HH:mm, z.B. 07:30): \n");
            String eingabeZeit = scanner.next();

            try {
                kommen = LocalTime.parse(eingabeZeit, timeFormatter);
                if (kommen.isBefore(minKernzeit)|| kommen.isAfter(maxKernzeit)){
                    System.out.println("\nAchtung-Der Arbeitsbeginn muss in der Kernzeit zwischen 6-9 Uhr liegen. Bitte korrigieren:\n");
                    continue;
                }
                break; // Hat geklappt! Wir springen aus der Schleife.
            } catch (DateTimeParseException e) {
                // Falls die Eingabe ungültig war, fangen wir den Fehler ab:
                System.out.println("\nUngültiges Zeitformat! Bitte um Korrektur:\n");
            }
        }
        // 2. Abfrage Zeitkonto Vorwoche
        int eingabeVorwocheMinuten=0;
        while(true){ //Abfangen von Eingabefehlern
                
        System.out.println(
                "\nBitte aktuellen Stand des Zeitkontos eingeben (Format HH:mm oder -HH:mm, z.B. 02:30 oder -10:15)");
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
                System.out.println("\nUngültige Eingabe, versuche es nochmal mit dem richtigen Zeitformat:");
            }
        }
        // 3. Erfassung des ZielZeitkontostandes
        int zielZeitkontominuten=0;
        while(true){
            System.out.println("\nAuf welchen Wert wollen wir dein Zeitkonto bringen? Wähle etwas zwischen (Format HH:mm, z.B. 00:00 oder 02:00):");
            String eingabeZiel=scanner.next();
            try{ //der parse ins Zeitformat filtert falsche Eingaben (Minus, Länge, Buchstaben)
                LocalTime zeitZiel=LocalTime.parse(eingabeZiel, timeFormatter);
                zielZeitkontominuten=zeitZiel.getHour()*60+zeitZiel.getMinute();
                if (zielZeitkontominuten>120){
                    System.out.println("\nLeider darf man als Azubi keine Überstunden anhäufen, deshalb kannst du nur max. 2 angeben.");
                }else{
                break; //erfolgreich raus aus der Schleife
                }
            }catch (DateTimeParseException e) {
                System.out.println("Ungültige Eingabe, versuche es nochmal mit dem richtigen Format und positiver Zielzeit:");
            }
        }
        //4. Netto-Arbeitszeit für den letzten Tag berechnen
        int nettoArbeitsMinuten=tagesSoll+zielZeitkontominuten-eingabeVorwocheMinuten;

        //5. gesetzliche Pausenregelung Berechnung
        int pauseMinuten=0;
        if (nettoArbeitsMinuten>540){
            pauseMinuten=45;
            
        }else if (nettoArbeitsMinuten>360){
            pauseMinuten=30;
           
        }else{
            pauseMinuten=0;
            
        }
        //6. FeierabendUhrzeit berechnen
        LocalTime feierabend=kommen
                .plusMinutes(nettoArbeitsMinuten)
                .plusMinutes(pauseMinuten);

        LocalTime maxTarifFeierabend=LocalTime.of(18,0);


        // 7. Prüfen, ob die Ergebnisvariable im gültigen Bereich liegt
        
        // 7. Prüfen der Grenzfälle mit Flags (Booleans)
        
        // A) Limit durch die 18-Uhr-Grenze ermitteln
        int maxAnwesenheitMinuten = (int) java.time.Duration.between(kommen, maxTarifFeierabend).toMinutes();
        int tatsaechlichePauseBis18 = (maxAnwesenheitMinuten > 585) ? 45 : ((maxAnwesenheitMinuten > 390) ? 30 : 0);
        int maxNettoBis18 = maxAnwesenheitMinuten - tatsaechlichePauseBis18;

        // B) Flags für die Grenzverletzungen setzen
        boolean verletzt10Stunden = nettoArbeitsMinuten > 600;
        boolean verletzt18Uhr = feierabend.isAfter(maxTarifFeierabend);

        // C) Auswertung über die Flags
        if (verletzt10Stunden || verletzt18Uhr) {
            
            // 1. Textausgabe je nach Zustand der Flags
            if (verletzt10Stunden && verletzt18Uhr) {
                System.out.println("\nDu überschreitest die maximale Arbeitszeit von 10h UND die 18 Uhr Grenze.");
            } else if (verletzt10Stunden) {
                System.out.println("\nÜber 10 Stunden sind nicht erlaubt. Wir sind nicht in Asien!");
            } else if (verletzt18Uhr) {
                System.out.println("\nDu erreichst die 18 Uhr Grenze. Länger darfst du nicht arbeiten.");
            }

            // 2. Erlaubte Netto-Minuten bestimmen
            int erlaubtesNetto = nettoArbeitsMinuten;
            if (verletzt10Stunden) {
                erlaubtesNetto = Math.min(erlaubtesNetto, 600);
            }
            if (verletzt18Uhr) {
                erlaubtesNetto = Math.min(erlaubtesNetto, maxNettoBis18);
            }

            // 3. Ausstempeln & neues Zielkonto neu berechnen
            int finalePause = (erlaubtesNetto > 540) ? 45 : ((erlaubtesNetto > 360) ? 30 : 0);
            LocalTime tatsaechlicherFeierabend = kommen.plusMinutes(erlaubtesNetto).plusMinutes(finalePause);

            int neuesZielMinuten = eingabeVorwocheMinuten + erlaubtesNetto - tagesSoll;

            // Formatierung in HH:mm inklusive Vorzeichen
            int absMinuten = Math.abs(neuesZielMinuten);
            String stundenText = String.format("%02d", absMinuten / 60);
            String minutenText = String.format("%02d", absMinuten % 60);
            String vorzeichen = (neuesZielMinuten < 0) ? "-" : "";
            String neuesZielHHMM = vorzeichen + stundenText + ":" + minutenText;

            System.out.println("Spätestens um " + tatsaechlicherFeierabend.format(timeFormatter) + " Uhr musst du ausstempeln.");
            System.out.println("Das angepasste Zeitkonto steht dann auf " + neuesZielHHMM);

            scanner.close();
            return;
        }
               
       //8. -aus 5. gesetzliche Pausenregelung Ausgabe
        
        if (nettoArbeitsMinuten>540){
            pauseMinuten=45;
            System.out.println("\nDein Arbeitstag hat entsprechend deiner Auswahl über neun Stunden. Nach §4 ArbZG rechne ich dir 45 min. Pause ein.");
        }else if (nettoArbeitsMinuten>360){
            pauseMinuten=30;
           System.out.println("\nDein Arbeitstag hat entsprechend deiner Auswahl über sechs Stunden. Nach §4 ArbZG rechne ich dir 30 min. Pause ein.");
        }else{
            pauseMinuten=0;
            System.out.println("\nDein Arbeitstag hat entsprechend deiner Auswahl unter sechs Stunden. Nach §4 ArbZG rechne ich keine Pause ein.");
        }

        //9. Ergebnis
        System.out.println("\nAbflug um "+feierabend.format(timeFormatter)+" Uhr.");
        scanner.close();
        }
        }
    