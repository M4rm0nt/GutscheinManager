import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class GutscheinGUI extends JFrame {
    private final Map<String, Gutschein> gutscheinInfos = new HashMap<>();
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JLabel gesamtPreisLabel = new JLabel("Gesamtpreis der Bestellung: 0 €");
    private final JList<String> gutscheinList = new JList<>(listModel);

    public GutscheinGUI() {
        setTitle("Gutschein Manager");
        setSize(800, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        Color backgroundColor = new Color(230, 230, 250);
        Font labelFont = new Font("Arial", Font.BOLD, 12);

        getContentPane().setBackground(backgroundColor);

        JPanel gartenPanel = createGutscheinPanel("Gutschein Garten", 5.0);
        JPanel weihnachtenPanel = createGutscheinPanel("Gutschein Weihnachten", 6.0);
        JPanel werkzeugPanel = createGutscheinPanel("Gutschein Werkzeug", 7.0);
        JPanel geschenkPanel = createGutscheinPanel("Gutschein Geschenk", 8.0);
        JPanel farbePanel = createGutscheinPanel("Gutschein Farbe", 9.0);


        gartenPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        weihnachtenPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        werkzeugPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        geschenkPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        farbePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        add(gartenPanel);
        add(weihnachtenPanel);
        add(werkzeugPanel);
        add(geschenkPanel);
        add(farbePanel);

        JLabel ausgewaehlteLabel = new JLabel("Ausgewählte Gutscheine:");
        ausgewaehlteLabel.setFont(labelFont);
        add(ausgewaehlteLabel);

        gutscheinList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JButton deleteButton = new JButton("Ausgewählte Gutscheine löschen");
        deleteButton.addActionListener(e -> {
            int[] selectedIndices = gutscheinList.getSelectedIndices();
            if (selectedIndices.length > 0) {
                for (int i = selectedIndices.length - 1; i >= 0; i--) {
                    listModel.remove(selectedIndices[i]);
                }
                updateGesamtPreis();
            }
        });

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(new JScrollPane(gutscheinList), BorderLayout.CENTER);
        listPanel.add(deleteButton, BorderLayout.SOUTH);
        listPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        add(listPanel);

        gesamtPreisLabel.setFont(labelFont);
        gesamtPreisLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        add(gesamtPreisLabel);

        setVisible(true);
    }

    private JPanel createGutscheinPanel(String gutscheinName, double preisProStueck) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel nameLabel = new JLabel(gutscheinName);
        nameLabel.setPreferredSize(new Dimension(150, nameLabel.getPreferredSize().height));
        panel.add(nameLabel);

        JLabel spacer1 = new JLabel(" ");
        spacer1.setPreferredSize(new Dimension(20, 0));
        panel.add(spacer1);

        JLabel preisLabel = new JLabel("VPE: " + String.format("%.2f€", preisProStueck));
        preisLabel.setPreferredSize(new Dimension(100, preisLabel.getPreferredSize().height));
        panel.add(preisLabel);

        JLabel spacer2 = new JLabel(" ");
        spacer2.setPreferredSize(new Dimension(20, 0));
        panel.add(spacer2);

        JComboBox<Integer> mengeComboBox = new JComboBox<>(new Integer[]{0, 50, 100, 1000});
        mengeComboBox.setPreferredSize(new Dimension(100, mengeComboBox.getPreferredSize().height));
        panel.add(mengeComboBox);

        JLabel spacer3 = new JLabel(" ");
        spacer3.setPreferredSize(new Dimension(20, 0));
        panel.add(spacer3);

        JLabel gesamtpreisLabel = new JLabel("Gesamtpreis: 0€");
        gesamtpreisLabel.setPreferredSize(new Dimension(150, gesamtpreisLabel.getPreferredSize().height));
        panel.add(gesamtpreisLabel);

        JLabel spacer4 = new JLabel(" ");
        spacer4.setPreferredSize(new Dimension(20, 0));
        panel.add(spacer4);

        JButton addButton = new JButton("Hinzufügen");
        addButton.setPreferredSize(new Dimension(120, addButton.getPreferredSize().height));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int hinzugefuegteMenge = (int) mengeComboBox.getSelectedItem();
                double gesamtpreisHinzugefuegt = preisProStueck * hinzugefuegteMenge;

                boolean gutscheinGefunden = false;
                for (int i = 0; i < listModel.getSize(); i++) {
                    String eintrag = listModel.get(i);
                    if (eintrag.startsWith(gutscheinName)) {
                        String[] teile = eintrag.split(", ");
                        int vorhandeneMenge = Integer.parseInt(teile[1].split(" ")[0]);
                        double vorhandenerGesamtpreis = Double.parseDouble(teile[2].replace("€", "").trim().replace(",", "."));

                        int neueMenge = vorhandeneMenge + hinzugefuegteMenge;
                        double neuerGesamtpreis = vorhandenerGesamtpreis + gesamtpreisHinzugefuegt;

                        String neuerEintrag = gutscheinName + ", " + neueMenge + " Stk, " + String.format("%.2f€", neuerGesamtpreis);
                        listModel.set(i, neuerEintrag);

                        gutscheinGefunden = true;
                        break;
                    }
                }

                if (!gutscheinGefunden) {
                    listModel.addElement(gutscheinName + ", " + hinzugefuegteMenge + " Stk, " + String.format("%.2f€", gesamtpreisHinzugefuegt));
                }

                updateGesamtPreis();
            }
        });
        panel.add(addButton);

        gutscheinInfos.put(gutscheinName, new Gutschein(preisProStueck, mengeComboBox, gesamtpreisLabel));

        return panel;
    }


    private void updateGesamtPreis() {
        double gesamtPreis = 0;
        for (int i = 0; i < listModel.getSize(); i++) {
            String[] teile = listModel.get(i).split(", ");
            String preisString = teile[2].replace("€", "").trim();
            preisString = preisString.replace(",", ".");
            double preis = Double.parseDouble(preisString);
            gesamtPreis += preis;
        }
        gesamtPreisLabel.setText("Gesamtpreis der Bestellung: " + String.format("%.2f €", gesamtPreis));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GutscheinGUI::new);
    }
}