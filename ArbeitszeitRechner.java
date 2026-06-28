public class ArbeitszeitRechner {
    public static void main(String[] args) {
        double atoss=1.5 * 60;
        //Eingabe Montag
        double startStundeMo=8;
        double startMinuteMo=0;

        //Eingabe Dienstag
        double endStundeMo=16;
        double endMinuteMo=30;
        double startStundeDi=8;
        double startMinuteDi=0;

        //Eingabe Mittwoch
        double endStundeDi=16;
        double endMinuteDi=30;
        double startStundeMi=8;
        double startMinuteMi=0;

        //Eingabe Donnerstag
        double endStundeMi=16;
        double endMinuteMi=30;
        double startStundeDo=8;
        double startMinuteDo=0;

        //Eingabe Freitag
        double endStundeDo=16;
        double endMinuteDo=30;
        double startStundeFr=8;
        double startMinuteFr=0;

        //Berechnung Zeitspanne Montag
        double startInMinutenMo=startStundeMo * 60 + startMinuteMo;
        double endInMinutenMo=endStundeMo * 60 + endMinuteMo;
        double zeitspanneMo=endInMinutenMo - startInMinutenMo;

        //Berechnung Zeitspanne Dienstag
        double startInMinutenDi=startStundeDi * 60 + startMinuteDi;
        double endInMinutenDi=endStundeDi * 60 + endMinuteDi;
        double zeitspanneDi=endInMinutenDi - startInMinutenDi;

        //Berechnung Zeitspanne Mittwoch
        double startInMinutenMi=startStundeMi * 60 + startMinuteMi;
        double endInMinutenMi=endStundeMi * 60 + endMinuteMi;
        double zeitspanneMi=endInMinutenMi - startInMinutenMi;

        //Berechnung Zeitspanne Donnerstag
        double startInMinutenDo=startStundeDo * 60 + startMinuteDo;
        double endInMinutenDo=endStundeDo * 60 + endMinuteDo;
        double zeitspanneDo=endInMinutenDo - startInMinutenDo;

        //Berechnung Startzeit Freitag
        double startInMinutenFr=startStundeFr * 60 + startMinuteFr;

        //BERECHNUNG RESTMINUTEN
        double bereitsGeleistet=zeitspanneMo+zeitspanneDi+zeitspanneMi+zeitspanneDo-120;
        double nochOffen=2310-atoss-bereitsGeleistet;
        double endZeitpunktFr=startInMinutenFr+nochOffen;
        //Umrechnung in Std./Min.
        double endStundeFr=endZeitpunktFr/60; 
        int eSF= (int)endStundeFr;
        double endMinuteFr=endZeitpunktFr%60;
        int eMF= (int)endMinuteFr;
        
        System.out.printf("Du musst bis %02d:%02d Uhr arbeiten.%n", eSF, eMF);

    }
}
