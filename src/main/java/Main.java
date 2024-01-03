import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        GutscheinGUI gui = new GutscheinGUI();
        SwingUtilities.invokeLater(() -> gui.setVisible(true));
    }

}