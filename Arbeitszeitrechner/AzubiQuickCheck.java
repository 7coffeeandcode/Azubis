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

        System.out.println("\n ======= AZUBI QUICK-CHECK =======");
        System.out.println("\nDas ist ein Arbeitszeitrechner. Anhand der Eingabe des aktuellen und gewünschten Zeitkontostandes \nermittelt es die exakte Ausstempelzeit für den jeweiligen Tag. Ideal zum Wochenabschluss.");
        System.out.println("\nGrunddaten: 38,5h - 5 Tage die Woche - 7,42h am Tag\n");
        System.out.println("\nBeachte die Kernarbeitszeit von 9-15 Uhr. Freitags kannst du früher Schluss machen.\n");
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
        int eingabeVorwocheMinuten=0;
         while(true){ //Abfangen von Eingabefehlern
        // 2. Abfrage Zeitkonto Vorwoche
        
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

        
        //5. gesetzliche Pausenregelung
        int pauseMinuten=0;
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
        //6. FeierabendUhrzeit berechnen
        LocalTime feierabend=kommen
                .plusMinutes(nettoArbeitsMinuten)
                .plusMinutes(pauseMinuten);

        LocalTime maxTarifFeierabend=LocalTime.of(18,0);

        //7. Feierabendsperre prüfen

        if (feierabend.isAfter(maxTarifFeierabend)){
            System.out.println("Achtung - arbeiten nach 18.00 Uhr ist nicht erlaubt!");

            // Berechnung wie viele Minuten bis 18 Uhr gearbeitet werden können
            int maxAnwesenheitMinuten=(int)java.time.Duration.between(kommen, maxTarifFeierabend).toMinutes();
            // Pause für die gekappte Zeit neu bestimmen (ArbZG §4)
            int tatsaechlichePause = 0;
            if (maxAnwesenheitMinuten > 585) {      // > 9 Std. Anwesenheit (540 Min Netto + 45 Min Pause)
            tatsaechlichePause = 45;
            } else if (maxAnwesenheitMinuten > 390) { // > 6 Std. Anwesenheit (360 Min Netto + 30 Min Pause)
            tatsaechlichePause = 30;
            }
            int verbleibendeNettoArbeitsMinuten=maxAnwesenheitMinuten-tatsaechlichePause;
            int nichtLeistbareMinuten=nettoArbeitsMinuten-verbleibendeNettoArbeitsMinuten;

            //neues angepasstes Zielzeitkonto
            int neuesZielMinuten=zielZeitkontominuten-nichtLeistbareMinuten;

            //Feierabend auf 18 Uhr setzen
            feierabend=maxTarifFeierabend;

            // Formatierung in HH:mm inklusive Vorzeichen
            int absMinuten = Math.abs(neuesZielMinuten);
            int stunden = absMinuten / 60;
            int minuten = absMinuten % 60;
            String stundenText = (stunden < 10) ? "0" + stunden : "" + stunden;
            String minutenText = (minuten < 10) ? "0" + minuten : "" + minuten;
            String vorzeichen = (neuesZielMinuten < 0) ? "-" : "";

            String neuesZielHHMM = vorzeichen + stundenText + ":" + minutenText;

            System.out.println("Dein geplantes Zeitkontoziel ist nicht bis 18 Uhr erreichbar.");
            System.out.println("Wir müssen dein Kontoziel auf " + neuesZielHHMM + " anpassen.");
            
        }
        //8. Überprüfung der 10h-Grenze
        if (nettoArbeitsMinuten>600){
            final int MAX_NETTO_MINUTEN=600;
            final int MAX_PAUSE_MINUTEN=45; //gesetzlich bei > 9h
            //späteste Ausstempelzeit
            LocalTime maxFeierAbend=kommen
            .plusMinutes(MAX_NETTO_MINUTEN)
            .plusMinutes(MAX_PAUSE_MINUTEN);

        int zeitkontoNachMaxRechnungMinuten=MAX_NETTO_MINUTEN-nettoArbeitsMinuten;
        System.out.println("\nÜber 10 Stunden sind nicht erlaubt, wir sind nicht in Asien. Spätestens um " +maxFeierAbend.format(timeFormatter)+ " Uhr musst du ausstempeln (45 min. Pause inkl.)!");
        System.out.println("Es verbleiben dann noch " + (zeitkontoNachMaxRechnungMinuten/60)+ " Std. "+(zeitkontoNachMaxRechnungMinuten%60)+" Min. auf deinem Zeitkonto.");
        scanner.close();
        return;
        }
       

        //9. Ergebnis
        System.out.println("\nAbflug um "+feierabend.format(timeFormatter)+" Uhr.");
        scanner.close();
        }
        }
    