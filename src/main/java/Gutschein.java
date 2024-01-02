import javax.swing.*;

public class Gutschein {
    double preisProStueck;
    JComboBox<Integer> mengeComboBox;
    JLabel gesamtpreisLabel;

    public Gutschein(double preisProStueck, JComboBox<Integer> mengeComboBox, JLabel gesamtpreisLabel) {
        this.preisProStueck = preisProStueck;
        this.mengeComboBox = mengeComboBox;
        this.gesamtpreisLabel = gesamtpreisLabel;
    }
}