public class Tannenbaum {
  public static void main(String[] args) {

  int höhe=5;
    for (int i = 0; i<höhe; i++) {
        int leerzeichen=höhe-i-1;
        int sterne=i*2+1;
        
        for (int j=0; j<leerzeichen;j++){
            System.out.print(" ");
        }
        for (int j=0;j<sterne;j++){
            System.out.print("*");
        }
        System.out.println();
        
    } 
    
    
  }
}
