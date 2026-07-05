import javax.swing.JOptionPane;

public class kino {
    public static void main(String[] args) {
        String pin = "1234";
        boolean zugriffErlaubt = false;

        // 1. Teil: PIN Abfrage mit maximal 3 Versuchen
        for (int i = 1; i <= 3; i++) {
            String eingabePin = JOptionPane.showInputDialog("Bitte PIN eingeben:");

            // Abbruch-Check (wichtig für die Note!)
            if (eingabePin == null || eingabePin.equals("0")) {
                JOptionPane.showMessageDialog(null, "System beendet");
                return; 
            }

            if (eingabePin.equals(pin)) {
                zugriffErlaubt = true;
                break; // PIN richtig -> Schleife verlassen
            } else {
                int rest = 3 - i;
                if (rest > 0) {
                    JOptionPane.showMessageDialog(null, "Falsch! Noch " + rest + " Versuche.");
                } else {
                    JOptionPane.showMessageDialog(null, "System gesperrt");
                    return;
                }
            }
        }

        // 2. Teil: Altersprüfung (nur wenn PIN korrekt war)
        if (zugriffErlaubt) {
            String eingabeAlter = JOptionPane.showInputDialog("Wie alt bist du?");
            
            if (eingabeAlter == null) {
                JOptionPane.showMessageDialog(null, "Vorgang abgebrochen");
                return;
            }

            // Text in Zahl umwandeln
            int alter = Integer.parseInt(eingabeAlter);

            // Entscheidungs-Logik
            if (alter < 12) {
                JOptionPane.showMessageDialog(null, "Kein Zutritt für diesen Film!");
            } else if (alter >= 12 && alter <= 15) {
                JOptionPane.showMessageDialog(null, "Zutritt nur mit Begleitung eines Erwachsenen.");
            } else {
                // Mit Backslash für die Anführungszeichen im Text
                JOptionPane.showMessageDialog(null, "Viel Spaß beim Film \"Avatar\"!");
            }
        }
    }
}