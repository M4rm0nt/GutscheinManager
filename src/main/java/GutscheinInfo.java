import javax.swing.*;

public class GutscheinInfo {
    double preisProStueck;
    JComboBox<Integer> mengeComboBox;
    JLabel gesamtpreisLabel;

    public GutscheinInfo(double preisProStueck, JComboBox<Integer> mengeComboBox, JLabel gesamtpreisLabel) {
        this.preisProStueck = preisProStueck;
        this.mengeComboBox = mengeComboBox;
        this.gesamtpreisLabel = gesamtpreisLabel;
    }
}