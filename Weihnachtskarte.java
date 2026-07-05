public class Weihnachtskarte {
  public static void main(String[] args) {

  int wish=20; //Make a wish!
  String grusstext="Frohe Weihnachten"; //kann politisch korrekt angepasst werden 
  String name="Elias"; //Place your name!
  int stammgroesse=wish/3; // bei der Ganzzahldivision wird das Ergebnis nach dem Komma abgeschnitten
  int leerzeichenstamm=wish-stammgroesse/2;
  int baumbreite=wish*2-1;
  int ausgabelänge=grusstext.length()+1+name.length();
  int leerzeichengruss=(baumbreite-ausgabelänge)/2;
 
    for (int i = 1; i<=wish; i++) {
        int leerzeichen=wish-i;
        int sterne=i*2-1;
        for (int j=1; j<=leerzeichen;j++){
            System.out.print(" ");
        }
        for (int j=1;j<=sterne;j++){
            System.out.print("*");
        }
        System.out.println();
    } 
    for (int stammzeile=1; stammzeile<=stammgroesse; stammzeile++){
        for (int j=1; j<=leerzeichenstamm; j++){
            System.out.print(" ");
        }
        for (int k=1;k<=stammgroesse;k++){
            System.out.print("#");
        }
        System.out.println();
    }
    System.out.println();
    for (int i=0; i<leerzeichengruss; i++){
    System.out.print(" ");
    }
    System.out.println(grusstext+" "+name);        
  }
}
