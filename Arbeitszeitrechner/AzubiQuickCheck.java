import java.util.Scanner; // Benutzereingaben einlesen über Tastatur (System.in)
import java.time.LocalTime; // speichert und verarbeitet reine Uhrzeiten
import java.time.format.DateTimeFormatter; //formattiert Daten in Zeitobjekte mit definiertem Format
import java.time.format.DateTimeParseException; //fängt fehlerhafte Zeiteingaben ab

public class AzubiQuickCheck{
    public static void main(String[]args){
         Scanner scanner = new Scanner(System.in);
        DateTimeFormatter timeFormatter=DateTimeFormatter.ofPattern("HH:mm");

        //feste Azubi-Parameter (38,5h / 5Tage = 7 h 42 min = 462 Minuten pro Tag
        final int tagesSoll=462; // Fixvariable

        System.out.println("\n ======= AZUBI TIME-CHECK =======");
        System.out.println("\nDas ist ein Arbeitszeitrechner. Anhand der Eingabe des aktuellen und gewünschten Zeitkontostandes \nermittelt es die exakte Ausstempelzeit für den jeweiligen Tag. Ideal zum Wochenabschluss.\nNicht ins Minus, aber so früh wie möglich nach Hause :)");
        System.out.println("\nGrunddaten: 38,5h - 5 Tage die Woche - 7,42h am Tag");
        System.out.println("\nVorgaben:\n- Kernarbeitszeit von 9-15 Uhr. Freitags kannst du früher Schluss machen.\n- Stempelzeiten: nur im Zeitraum zwischen 6 Uhr und 18 Uhr erlaubt\n- Pausen: ab 6h-30min, ab 9h-45min und werden automatisch abgezogen und mitberechnet\n- Arbeitszeit: maximal 10h/Tag");
        System.out.println("\nMusterwoche: \nDu baust von Mo-Do täglich 30min. Plus auf dein Zeitkonto. Am Freitag hast du dann zwei \nPlusstunden, setzt deine Zielzeit für kommende Woche wieder auf 00:00 und kannst nach \n5 Std. 42 Min. gehen.\n");
        System.out.println("LET`S GO!\n");

         // 1. EINGABEVALIDIERUNGSSCHLEIFE (Eingabefehler und Grenzwerte einfangen)
         // Einstempelzeit einlesen: Die Eingabe erfolgt in eine Schleife auf true mit try/catch und startet mit einer Eingabeaufforderung
         // Die Eingabe erfolgt als String, der in ein Zeitformat geparst wird. 
         // Bei Erfolg von try / richtigem Eingabeformat wird das Ergebnis im Datentyp LocalTime gespeichert
         // Dann erfolgt die Prüfung auf die Bedingungen der Kernzeitsperre, trifft eine zu - wird mit einem Hinweis und continue die Schleife neu gestartet
         // trifft keine Bedingung zu wird die Schleife mit break verlassen 
         // hat das parsen nicht geklappt wird mit catch (DateTimeParseException) ein Hinweis ausgegeben und die Schleife wiederholt sich

        LocalTime einstempeln = null; //1. Eingabevariable
        LocalTime minKernzeit=LocalTime.of(6,0);
        LocalTime maxKernzeit=LocalTime.of(9,0);
        while (true) {
            System.out.print("Einstempelzeit heute eingeben (Format HH:mm, z.B. 07:30): \n");
            String eingabeEinstempeln = scanner.next();

            try {
                einstempeln = LocalTime.parse(eingabeEinstempeln, timeFormatter);
                if (einstempeln.isBefore(minKernzeit)|| einstempeln.isAfter(maxKernzeit)){
                    System.out.println("\nAchtung-Der Arbeitsbeginn muss in der Kernzeit zwischen 6-9 Uhr liegen. Bitte korrigieren:\n");
                    continue; //zurück an den Anfang der Schleife
                }
                break; // Hat geklappt! Wir springen aus der Schleife.
            } catch (DateTimeParseException e) {
                // Falls die Eingabe ungültig war, fangen wir den Fehler ab:
                System.out.println("\nUngültiges Zeitformat! Bitte um Korrektur:\n");
            }
        }
        // 2. EINGABEVALIDIERUNGSSCHLEIFE (Eingabefehler und Minuswerte abfangen)
        // auf gleichem Weg das alte Zeitkonto einlesen
        // Die Eingabe erfolgt als String, der zunächst auf ein Minuszeichen geprüft wird bevor er in die richtige Verzweigung geht um in ein in ein Zeitformat geparst zu werden. 
        // klappt das parsen wird die Schleife mit break verlassen und das Ergebnis im Datentyp int gespeichert
        // wenn nicht wird mit catch (DateTimeParseException) ein Hinweis ausgegeben und die Schleife wiederholt sich

        int altesZeitkontoMinuten=0; //2. Eingabevariable
        while(true){                 
            System.out.println(
                "\nBitte aktuellen Stand des Zeitkontos eingeben (Format HH:mm oder -HH:mm, z.B. 02:30 oder -10:15)");
            String eingabeAltesZeitkonto= scanner.next();

            try{     
                // Rechenweg bei Minuszeichen: Minus abschneiden, parsen und direkt mit -1 multiplizieren
                if (eingabeAltesZeitkonto.startsWith("-")){
                LocalTime zeitAlt=LocalTime.parse(eingabeAltesZeitkonto.substring(1), timeFormatter);
                altesZeitkontoMinuten=(zeitAlt.getHour()*60+zeitAlt.getMinute())*-1;
                }else{
                //normal rechnen bei positivem Zeitkonto
                LocalTime zeitAlt=LocalTime.parse(eingabeAltesZeitkonto, timeFormatter);
                altesZeitkontoMinuten=(zeitAlt.getHour()*60+zeitAlt.getMinute());
                }
                break; 
            }catch (DateTimeParseException e){
                System.out.println("\nUngültige Eingabe, versuche es nochmal mit dem richtigen Zeitformat:");
            }
        }
        // 3. EINGABEVALIDIERUNGSSCHLEIFE (Eingabefehler und Grenzwert abfangen)
        // auf gleichem Weg neues ZielZeitkonto einlesen, nach dem parsen auf die 2h-max. Bedingung prüfen

        int neuesZeitkontoMinuten=0; //3. Eingabevariable
        while(true){
            System.out.println("\nAuf welchen Wert wollen wir dein Zeitkonto bringen? Wähle etwas zwischen (Format HH:mm, z.B. 00:00 oder 02:00):");
            String eingabeZiel=scanner.next();
            try{ //der parse ins Zeitformat filtert falsche Eingaben (Minus, Länge, Buchstaben)
                LocalTime zeitZiel=LocalTime.parse(eingabeZiel, timeFormatter);
                neuesZeitkontoMinuten=zeitZiel.getHour()*60+zeitZiel.getMinute();
                if (neuesZeitkontoMinuten>120){
                    System.out.println("\nLeider darf man als Azubi keine Überstunden anhäufen, deshalb kannst du nur max. 2 angeben.");
                }else{
                break; //erfolgreich raus aus der Schleife
                }
            }catch (DateTimeParseException e) {
                System.out.println("Ungültige Eingabe, versuche es nochmal mit dem richtigen Format und positiver Zielzeit:");
            }
        }
        //4. Netto-Arbeitszeit für den letzten Tag berechnen
        // Hauptrechnung und Programmvariable

        int nettoArbeitsMinuten=tagesSoll+neuesZeitkontoMinuten-altesZeitkontoMinuten;

        //5. Berechnung gesetzliche Pausenregelung 
        int pauseMinuten=0; //Ausgabevariable
        if (nettoArbeitsMinuten>540){
            pauseMinuten=45;
            
        }else if (nettoArbeitsMinuten>360){
            pauseMinuten=30;
           
        }else{
            pauseMinuten=0;
            
        }
        //6. Berechnung Ausstempelzeit 
        LocalTime ausstempeln=einstempeln //Ziel- und Ausgabevariable
                .plusMinutes(nettoArbeitsMinuten)
                .plusMinutes(pauseMinuten);    

        // 7. Prüfen, ob die Ergebnisvariable im gültigen Bereich liegt mit Flags (Booleans) und Sonderausgaben   
        // A) Limit durch die 18-Uhr-Grenze ermitteln

        LocalTime maxFeierabend=LocalTime.of(18,0);
        //Abruf einer Methode zur Berechnung der Zeitspanne in den LocalTime Variablen
        int maxAnwesenheitMinuten = (int) java.time.Duration.between(einstempeln, maxFeierabend).toMinutes(); 

        //Anpassung der Pause mit dem Ternär Operator
        int angepasstePause = (maxAnwesenheitMinuten > 585) ? 45 : ((maxAnwesenheitMinuten > 390) ? 30 : 0);

        // Berechnung der durch die angepasste Pause sich neu ergebenden Nettoarbeitsminuten für den Trichtervergleich in C2
        int maxNettoBis18 = maxAnwesenheitMinuten - angepasstePause;

        // B) Flags für die Grenzverletzungen setzen
        boolean verletzt10Stunden = nettoArbeitsMinuten > 600;
        boolean verletzt18Uhr = ausstempeln.isAfter(maxFeierabend);

        // C) Auswertung über die Flags
        if (verletzt10Stunden || verletzt18Uhr) {
            
            // Textausgabe je nach Zustand der Flags
            if (verletzt10Stunden && verletzt18Uhr) {
                System.out.println("\nDu überschreitest die maximale Arbeitszeit von 10h UND die 18 Uhr Grenze.");
            } else if (verletzt10Stunden) {
                System.out.println("\nÜber 10 Stunden sind nicht erlaubt. Wir sind nicht in Asien!");
            } else if (verletzt18Uhr) {
                System.out.println("\nDu erreichst die 18 Uhr Grenze. Länger darfst du nicht arbeiten.");
            }
            // Erlaubte Netto-Minuten durch den Trichter bestimmen
            int erlaubtesNetto = nettoArbeitsMinuten;
            if (verletzt10Stunden) {
                erlaubtesNetto = Math.min(erlaubtesNetto, 600);
            }
            if (verletzt18Uhr) {
                erlaubtesNetto = Math.min(erlaubtesNetto, maxNettoBis18);
            }
            // Ausstempelzeit neu berechnen
            int finalePause = (erlaubtesNetto > 540) ? 45 : ((erlaubtesNetto > 360) ? 30 : 0); //Sonderausgabenvariable
            LocalTime angepasstesAusstempeln = einstempeln.plusMinutes(erlaubtesNetto).plusMinutes(finalePause); //Sonderausgabenvariable

            // ZielZeitkonto neu berechnen, Umstellung der Hauptformel aus 4.
            int angepasstesZeitkonto = altesZeitkontoMinuten + erlaubtesNetto - tagesSoll; 

            // Formatierung in HH:mm inklusive Vorzeichen
            // Abfangen eines negativen Wertes durch Verkettung in einer Stringausgabe
            int absMinuten = Math.abs(angepasstesZeitkonto);
            String stundenText = String.format("%02d", absMinuten / 60);
            String minutenText = String.format("%02d", absMinuten % 60);
            String vorzeichen = (angepasstesZeitkonto < 0) ? "-" : "";
            String ausgabeAngepasstesZeitkonto = vorzeichen + stundenText + ":" + minutenText; //Sonderausgabenvariable

            // Ergebnisausgabe für den Sonderfall
            System.out.println("Spätestens um " + angepasstesAusstempeln+ " Uhr musst du ausstempeln, inkl. "+finalePause+" Minuten Pause.");
            System.out.println("Das angepasste Zeitkonto steht dann auf " + ausgabeAngepasstesZeitkonto+".");

            scanner.close();
            return;
        }
               
       //8. Anschluss zu 6. wenn Zielvariable im gültigen Bereich lag 
       //gesetzliche Pausenregelung Ausgabe, errechnent in 5.
        
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
        //9. Ergebnisausgabe
        System.out.println("\nAbflug um "+ausstempeln+" Uhr.");
        scanner.close();
        }
        }
    