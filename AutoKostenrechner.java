import javax.swing.JOptionPane;

public class AutoKostenrechner {
    public static void main(String[] args) {
        String eingabeLaengeArbeitsweg = JOptionPane
                .showInputDialog("Geben Sie die Länge Ihres Arbeitsweges in km ein:");
        double laengeArbeitsweg = Double.parseDouble(eingabeLaengeArbeitsweg);

        String eingabeKraftstoffverbrauch = JOptionPane
                .showInputDialog("Geben Sie den Kraftstoffverbrauch Ihres Autos in l/100km ein:");
        double kraftstoffverbrauch = Double.parseDouble(eingabeKraftstoffverbrauch);

        String eingabeKraftstoffpreis = JOptionPane
                .showInputDialog("Geben Sie den aktuellen Kraftstoffpreis in €/l ein:");
        double kraftstoffpreis = Double.parseDouble(eingabeKraftstoffpreis);

        double kostenProFahrt = (laengeArbeitsweg / 100) * kraftstoffverbrauch * kraftstoffpreis;

        String eingabeArbeitstage = JOptionPane.showInputDialog("Geben Sie die Anzahl der Arbeitstage pro Monat ein:");
        int arbeitstage = Integer.parseInt(eingabeArbeitstage);

        double monatlicheKosten = kostenProFahrt * arbeitstage;
        JOptionPane.showMessageDialog(null,
                "Ihre monatlichen Spritkosten für die Arbeit betragen: " + String.format("%.2f", monatlicheKosten)
                        + " €");
    }
}
