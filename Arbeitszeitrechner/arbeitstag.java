import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class arbeitstag {
    //Variablen des Objekts
    private LocalTime kommen;
    private LocalTime gehen;
    private int pauseMinuten;

    //Konstruktor
    public arbeitstag(LocalTime kommenEingabe, LocalTime gehenEingabe, int pauseEingabe){
        kommen=kommenEingabe;
        gehen=gehenEingabe;
        pauseMinuten=pauseEingabe;
    }
    //Methode Zeitberechnung
    public int getNettoArbeitsMinuten(){
        int brutto=(int)ChronoUnit.MINUTES.between(kommen, gehen);
        return brutto-pauseMinuten;
    }

}