public class christmas{
    public static void main (String [] args){

        //Hauptvariablen am Anfang-gestalte deinen Wunschbaum!
        int wish=20; //Baumhöhe-Make a wish!
        String grusstext="Frohe Weihnachten"; //kann politisch korrekt angepasst werden
        String name="Elias"; //Place your name!

        //Stammgrösse und Berechnung der Ausrichtungen
        int stammgroesse=wish/3; //willkürlich-mein Stamm soll 1/3 der Krone sein
        int baumbreite=wish*2-1; //ungerade Sternchen pro Zeile, +/-1 abhängig vom Initialisierungswert i

        //===Baumkrone===
        for (int i=1; i<=wish; i++){ //hier befinden sich die Rechenbefehle, Achtung-der Initialisierungswert 0 oder 1 wirkt sich auf die Rechenformeln aus!
            int leerzeichen=wish-i; //z.B. 5-1=4, 2.Zeile: 5-2=3, 3. Zeile: 5-3=2
            int sterne=2*i-1; //ungerade Sternchen die auf die andere Seite rauswachsen, z.B. 2*1-1=1, 2.Zeile: 2*2-1=3, 3.Zeile: 2*3-1=5
            for (int j=1; j<=leerzeichen;j++){ //die Schleife läuft-siehe z.B.
                System.out.print(" "); //es werden die berechneten Leerzeichen gemacht
            }
            for (int j=1;j<=sterne; j++){ //die Scheife läuft-siehe z.B.
                System.out.print("*"); //es werden die berechneten Sternchen hinter die Leerzeichen gemacht
            }
            System.out.println(); //springe in eine neue Zeile und rechne mit i+ weiter
        }

        //===Stamm===
        int leerzeichenstamm=wish-stammgroesse/2; //Berechnung der notwendigen LZ zur Zentrierung
        for (int stammzeile=1;stammzeile<=stammgroesse;stammzeile++){ //fasst die nächsten zwei Schleifen aus der Kombi LZ und # zusammen und gibt sie Zeilenweise aus
            for (int j=1;j<=leerzeichenstamm;j++){
                System.out.print(" ");
            }
            for (int k=1;k<=stammgroesse;k++){
                System.out.print("#");
            }
            System.out.println();
        }

        //===Grusstext===
        System.out.println(); //eine Leerzeile
        int ausgabelaenge=grusstext.length()+1+name.length(); //1 ist PLatzhalter zwischen GT+N - es wird die Zeichenlänge berechnet
        int leerzeichengruss=(baumbreite-ausgabelaenge)/2; //sollte logisch sein :)
        for (int i=0;i<=leerzeichengruss;i++){
            System.out.print(" ");
        }
        System.out.print(grusstext+" "+name);
    }
}
