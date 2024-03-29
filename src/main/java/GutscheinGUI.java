import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;



public class GutscheinGUI extends JFrame {
    private final Map<String, Gutschein> gutscheinInfos = new HashMap<>();
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JLabel gesamtPreisLabel = new JLabel("Gesamtpreis der Bestellung: 0.00 €");
    private final JList<String> gutscheinList = new JList<>(listModel);

    public GutscheinGUI() {
        setTitle("Gutschein Manager");
        setSize(800, 900);
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

        JPanel ausgewaehltePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel ausgewaehlteLabel = new JLabel("Ausgewählte Gutscheine:");
        ausgewaehlteLabel.setFont(labelFont);
        ausgewaehltePanel.add(ausgewaehlteLabel);
        add(ausgewaehltePanel);

        gutscheinList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JButton deleteButton = new JButton("Ausgewählte Gutscheine löschen");
        deleteButton.addActionListener(e -> {
            int[] selectedIndices = gutscheinList.getSelectedIndices();
            if (selectedIndices.length > 0) {
                List<String> removedEntries = new ArrayList<>();
                for(int i = selectedIndices.length - 1; i >= 0; i--) {
                    removedEntries.add(listModel.get(selectedIndices[i]));
                    listModel.remove(selectedIndices[i]);
                }
                resetCheckboxForRemovedGutscheine(removedEntries);
                updateGesamtPreis();
            }
        });

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(new JScrollPane(gutscheinList), BorderLayout.CENTER);
        listPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JButton bestellenButton = new JButton("Bestellen");
        bestellenButton.addActionListener(e -> bestellen());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(bestellenButton);
        buttonPanel.add(deleteButton);

        JButton newGutscheinButton = new JButton("Neuen Gutschein erstellen");
        newGutscheinButton.addActionListener(e -> createNewGutscheinModal());
        buttonPanel.add(newGutscheinButton);

        listPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(listPanel);

        JPanel gesamtPreisPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        gesamtPreisLabel.setFont(labelFont);
        gesamtPreisLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        gesamtPreisPanel.add(gesamtPreisLabel);
        add(gesamtPreisPanel);

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

        JLabel preisLabel = new JLabel("VPE: " + String.format("%.2f €", preisProStueck));
        preisLabel.setPreferredSize(new Dimension(100, preisLabel.getPreferredSize().height));
        panel.add(preisLabel);

        JLabel spacer2 = new JLabel(" ");
        spacer2.setPreferredSize(new Dimension(20, 0));
        panel.add(spacer2);

        JComboBox<Integer> mengeComboBox = new JComboBox<>(new Integer[]{0, 50, 100, 1000});
        mengeComboBox.setPreferredSize(new Dimension(100, mengeComboBox.getPreferredSize().height));
        panel.add(mengeComboBox);

        JLabel spacer3 = new JLabel(" ");
        spacer3.setPreferredSize(new Dimension(40, 0));
        panel.add(spacer3);

        JLabel gesamtpreisLabel = new JLabel("Gesamtpreis: 0.00 €");
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
                if (hinzugefuegteMenge > 0) {
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

                            String neuerEintrag = gutscheinName + ", " + neueMenge + " Stk, " + String.format("%.2f €", neuerGesamtpreis);
                            listModel.set(i, neuerEintrag);

                            gutscheinGefunden = true;
                            break;
                        }
                    }

                    if (!gutscheinGefunden) {
                        listModel.addElement(gutscheinName + ", " + hinzugefuegteMenge + " Stk, " + String.format("%.2f €", gesamtpreisHinzugefuegt));
                    }

                    updateGesamtPreis();
                }
            }
        });

        panel.add(addButton);

        mengeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int menge = (int) mengeComboBox.getSelectedItem();
                double gesamtpreis = preisProStueck * menge;
                gesamtpreisLabel.setText("Gesamtpreis: " + String.format("%.2f €", gesamtpreis));
            }
        });

        gutscheinInfos.put(gutscheinName, new Gutschein(preisProStueck, mengeComboBox, gesamtpreisLabel));

        return panel;
    }

    private void createNewGutscheinModal() {
        JDialog dialog = new JDialog(this, "Neuen Gutschein erstellen", true);
        dialog.setLayout(new FlowLayout());
        dialog.setSize(300, 200);

        JTextField nameField = new JTextField(20);
        JTextField preisField = new JTextField(20);
        JButton addButton = new JButton("Hinzufügen");

        addButton.addActionListener(e -> {
            try {
                String gutscheinName = nameField.getText().trim();
                double preisProStueck = Double.parseDouble(preisField.getText().trim());

                if (gutscheinInfos.containsKey(gutscheinName)) {
                    JOptionPane.showMessageDialog(dialog, "Ein Gutschein mit diesem Namen existiert bereits.", "Fehler", JOptionPane.ERROR_MESSAGE);
                } else if (!gutscheinName.isEmpty() && preisProStueck > 0) {
                    JPanel neuesGutscheinPanel = createGutscheinPanel(gutscheinName, preisProStueck);
                    add(neuesGutscheinPanel);
                    neuesGutscheinPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
                    revalidate();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Bitte gültigen Namen und Preis eingeben.", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Bitte einen gültigen Preis eingeben.", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(new JLabel("Gutschein Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Preis pro Stück:"));
        dialog.add(preisField);
        dialog.add(addButton);
        dialog.setVisible(true);
    }

    private void bestellen() {
        Markt markt = new Markt(41, "test.firma@firma.com", new Benutzer(Anrede.Herr, "Bob", 22, 41));
        Benutzer benutzer = new Benutzer(Anrede.Herr, "Alice", 54, 33);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String dateString = formatter.format(new Date());

        int index = 0;
        String basisDateiname = "bestellungen";
        String dateiEndung = ".txt";
        File datei = new File(basisDateiname + dateiEndung);

        while (datei.exists()) {
            index++;
            datei = new File(basisDateiname + "_" + index + dateiEndung);
        }

        try (FileWriter writer = new FileWriter(datei, true);
             BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write("Gutscheinbestellung" + "\t\tDatum: " + dateString + "\n");
            bw.write("\n");
            bw.write("Besteller: " + Anrede.Frau + " " + benutzer.getName() + "(" + benutzer.getMitarbeiternummer() + ")" + " aus Markt " + benutzer.getMarktnummer() + "\n");
            bw.write("\n");
            bw.write("Positionen:\n");

            for (int i = 0; i < listModel.getSize(); i++) {
                bw.write("\t" + "-\t" + listModel.get(i) + "\n");
            }

            bw.write("\n");
            bw.write(gesamtPreisLabel.getText() + " €" + "\n");
            bw.write("\n");
            bw.write("Bestellinformation wurde an " + markt.getEmail() + " weitergeleitet.");
            bw.write("\n");
            bw.write("Ansprechpartner: " + markt.getAnspartner().getAnrede() + " " + markt.getAnspartner().getName() + "(" + markt.getAnspartner().getMitarbeiternummer() + ")" + " aus Markt " + markt.getAnspartner().getMarktnummer() + "\n");

            bw.flush();

            String pdfDateiname = basisDateiname + "_" + index + ".pdf";
            String bestelltext = "Gutscheinbestellung" + "\t\tDatum: " + dateString + "\n" + "\n" + "Besteller: " + Anrede.Frau + " " + benutzer.getName() + "(" + benutzer.getMitarbeiternummer() + ")" + " aus Markt " + benutzer.getMarktnummer() + "\n" + "\n" + "Positionen:\n";

            for (int i = 0; i < listModel.getSize(); i++) {
                bestelltext += "\t" + "-\t" + listModel.get(i) + "\n";
            }

            bestelltext += "\n" + gesamtPreisLabel.getText() + " €" + "\n" + "\n" + "Bestellinformation wurde an " + markt.getEmail() + " weitergeleitet." + "\n" + "Ansprechpartner: " + markt.getAnspartner().getAnrede() + " " + markt.getAnspartner().getName() + "(" + markt.getAnspartner().getMitarbeiternummer() + ")" + " aus Markt " + markt.getAnspartner().getMarktnummer() + "\n";

            erstellePDF(pdfDateiname, bestelltext);

            JOptionPane.showMessageDialog(this, "Bestellung wurde in " + datei.getName() + " gespeichert.", "Bestellung", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Bestellung.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void erstellePDF(String pdfDateiname, String bestelltext) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(pdfDateiname));
            document.open();

            Paragraph paragraph = new Paragraph(bestelltext);
            document.add(paragraph);

            document.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    private void resetCheckboxForRemovedGutscheine(List<String> removedEntries) {
        for (String removedEntry : removedEntries) {
            for (Map.Entry<String, Gutschein> entry : gutscheinInfos.entrySet()) {
                Gutschein gutschein = entry.getValue();
                if (removedEntry.startsWith(entry.getKey())) {
                    gutschein.mengeComboBox.setSelectedItem(0);
                    break;
                }
            }
        }
    }
}