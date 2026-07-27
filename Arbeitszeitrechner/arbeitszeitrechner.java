import java.util.Locale; // Landessprache, hier Komma statt Punkt
import java.util.Scanner; // Benutzereingaben einlesen über Tastatur (System.in)
import java.time.LocalTime; // speichert und verarbeitet reine Uhrzeiten
import java.time.format.DateTimeFormatter; //formattiert Daten in Zeitobjekte mit definiertem 
import java.time.format.DateTimeParseException; //fängt fehlerhafte Zeiteingaben ab
import java.time.temporal.ChronoUnit; //berechnet Zeitspanne in Minuten

class arbeitszeitrechner {
    public static void main(String[] args) {

        // 1. Abfrage der regulären Arbeitszeitparameter: Wochenarbeitszeit und
        // Arbeitstage
        Locale.setDefault(Locale.GERMANY); // setzt alles auf deutsches Datum und Zahlenformat um
        Scanner scanner = new Scanner(System.in); // Hauptscanner liest Tastatureingaben
        System.out.println();
        System.out.println(
                "Willkommen im Arbeitszeitrechner! Beantworten Sie die folgenden Fragen und ich berechne Ihre korrekte Abschlusszeit für den letzten Tag in dieser Woche!");
        System.out.println();
        System.out.println("Wie viele Wochenstunden beträgt Ihre Pflicht-Arbeitszeit?");

        double wochenPflichtArbeitszeit; // Variable zur Speicherung der gültigen Arbeitszeit
        double sollArbeitstage;
        double tagesSollZeit;

        while (true) {
            String eingabe = scanner.next().replace('.', ','); // liest die Eingabe als Text ein und ersetzt Punkte in
                                                               // Kommas
            Scanner tempScanner = new Scanner(eingabe); // erstellt einen vorübergehenden Scanner der nur den
                                                        // eingegebenen Text analysiert
            tempScanner.useLocale(Locale.GERMANY); // sagt dem temporären Scannner, dass er das deutsche Format nutzen
                                                   // soll

            if (tempScanner.hasNextDouble()) {
                wochenPflichtArbeitszeit = tempScanner.nextDouble(); // der Scanner parst gleich mit

                if (wochenPflichtArbeitszeit >= 1 && wochenPflichtArbeitszeit <= 40) {
                    tempScanner.close();
                    break;
                }
            }
            System.out.print("Ungültig-bitte numerische Zahlen zwischen 1-40 mit Punkt oder Komma eingeben!");
        }
        System.out.println("Wie viele Arbeitstage pro Woche haben Sie geregelt? Geben Sie eine ganze Zahl zwischen 1-7 ein: ");

        while (true) {
            String eingabe = scanner.next();
            Scanner tempScanner = new Scanner(eingabe);
            if (tempScanner.hasNextInt()) {
                sollArbeitstage = tempScanner.nextInt();
                if (sollArbeitstage >= 1 && sollArbeitstage <= 7) {
                    //jetzt prüfen der 10h Grenze
                    if ((wochenPflichtArbeitszeit/sollArbeitstage)>10.0){
                        System.out.println("Über 10 Stunden sind nicht erlaubt, wir sind nicht in Asien. Bitte verteilen Sie Ihre Arbeitszeit auf mehr Tage!");
                    }else{
                    tempScanner.close();
                    break;
                }
            }
            }
            tempScanner.close();
            System.out.println("Neuer Versuch: ");
        }
        tagesSollZeit = wochenPflichtArbeitszeit / sollArbeitstage;
        int stunden = (int) tagesSollZeit;
        int minuten = (int) Math.round((tagesSollZeit - stunden) * 60);

        // Ausgabeübersicht für den Nutzer über seine Eingaben und tägliche
        // Arbeitszeitdauer
        System.out.println(
                "Bei einer " + wochenPflichtArbeitszeit + "-Stundenwoche und " + sollArbeitstage
                        + " Arbeitstagen, beträgt Ihr durchschnittliches tägliches Stundenpensum: " + stunden
                        + " Stunden und "
                        + minuten + " Minuten");
            // Pausenhinweis
            if (tagesSollZeit>6.0){
                System.out.println("Hinweis: Denken Sie an die gesetzliche Pause. Am besten mit netten Kollegen und in der großzügigen Kantine.");
            }else{
                System.out.println("Hinweis: Bei bis zu 6h täglicher Arbeit müssen Sie keine Pause machen. Das ist von Vorteil, wenn man schneller heim will.");
            }
    System.out.println();
        // Anpassung der Wochenarbeitszeit bei Ausfalltagen
        System.out.println("Gab es diese Woche Ausfalltage? ja/nein");
        String antwort;
        while (true) {
            antwort = scanner.next();
            if (antwort.equalsIgnoreCase("ja") || antwort.equalsIgnoreCase("nein")) {
                break;
            }
            System.out.print("Bitte antworten Sie mit 'ja' oder 'nein'!");
        }
        double ausfalltage = 0;
        if (antwort.equalsIgnoreCase("ja")) {
            System.out.println("Wie viele Ausfalltage, z.B. Feiertag, Krankheitstag oder Urlaub, gab es?");

            while (true) {
                String eingabe = scanner.next().replace('.', ',');
                Scanner tempScanner = new Scanner(eingabe);
                tempScanner.useLocale(Locale.GERMANY);
                if (tempScanner.hasNextDouble()) {
                    ausfalltage = tempScanner.nextDouble();
                    if (ausfalltage > 0 && ausfalltage <= sollArbeitstage) {
                        tempScanner.close();
                        break;
                    }
                }
                tempScanner.close();
                System.out.println("Eingabefehler - bitte eine Zahl zwischen 0.5 und " +
                        +sollArbeitstage + " eingeben: ");
            }
            sollArbeitstage = sollArbeitstage - ausfalltage;
            wochenPflichtArbeitszeit = sollArbeitstage * tagesSollZeit;
            tagesSollZeit = wochenPflichtArbeitszeit / sollArbeitstage;
            int stundenWochenPflichtArbeitszeit = (int) wochenPflichtArbeitszeit;
            int minutenWochenPflichtArbeitszeit = (int) Math
                    .round((wochenPflichtArbeitszeit - stundenWochenPflichtArbeitszeit) * 60);
            System.out
                    .println("Damit hat sich Ihre Wochenarbeitszeit auf " + stundenWochenPflichtArbeitszeit
                            + " Stunden und "
                            + minutenWochenPflichtArbeitszeit + " Minuten verringert.");
        }
    // 2. Zeitkontoeinstellungen
        // Schablone für das Zeitformat definieren
        DateTimeFormatter formatVorgabe = DateTimeFormatter.ofPattern("HH:mm");
        // Variable zur Erfassung des alten Zeitkontostandes
        int eingabeVorwocheMinuten=0;

        while(true){ //Abfangen von Eingabefehlern
        // Abfrage Zeitkonto Vorwoche
        System.out.println();
        System.out.println(
                "Bitte Stand des Zeitkontos aus der Vorwoche eingeben (Format HH:mm oder -HH:mm, z.B. 02:30 oder -10:15)");
        String eingabeVorwoche = scanner.next();
            try{     
                // Rechenweg bei Minuszeichen
                if (eingabeVorwoche.startsWith("-")){
                //Minus abschneiden, parsen und direkt mit -1 multiplizieren
                LocalTime zeitVorwoche=LocalTime.parse(eingabeVorwoche.substring(1), formatVorgabe);
                eingabeVorwocheMinuten=(zeitVorwoche.getHour()*60+zeitVorwoche.getMinute())*-1;
                }else{
                //normal rechnen bei positivem Zeitkonto
                LocalTime zeitVorwoche=LocalTime.parse(eingabeVorwoche, formatVorgabe);
                eingabeVorwocheMinuten=(zeitVorwoche.getHour()*60+zeitVorwoche.getMinute());
                }
                break; //wenn parsing klappt, springen wir aus der Schleife raus
            }catch (DateTimeParseException e){
                System.out.println("Ungültige Eingabe, versuchen Sie es nochmal mit dem richtigen Format:");
            }
        }
        // Erfassung des ZielZeitkontostandes
        int zielZeitkontominuten=0;
        while(true){
            System.out.println("Mit welchem Zeitkontostand wollen Sie aus der Woche raus? (Format HH:mm, z.B. 00:00 oder 02:30)");
            String eingabeZiel=scanner.next();
            try{ //der parse ins Zeitformat filtert falsche Eingaben (Minus, Länge, Buchstaben)
                LocalTime zeitZiel=LocalTime.parse(eingabeZiel, formatVorgabe);
                zielZeitkontominuten=zeitZiel.getHour()*60+zeitZiel.getMinute();
                break; //erfolgreich raus aus der Schleife
            }catch (DateTimeParseException e) {
                System.out.println("Ungültige Eingabe, versuchen Sie es nochmal mit dem richtigen Format:");
            }
        }

// Wie gestalte ich die Abfrage über Arrays für die Zeitspannen der Arbeitstage
    //Option 1: verschiedene Arrays (für die verschiedenen Datentypen)
    //Option 2: mehrdimensionales Array (Problem der verschiedenen Datentypen)
    //Option 3: Objektorientiert: Objekt Arbeitstag

    }
    
}

