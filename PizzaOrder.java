import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PizzaOrderGUI());
    }
}

class PizzaOrderGUI extends JFrame {

    public PizzaOrderGUI() {
        PizzaOrderController controller = new PizzaOrderController();
        add(controller);

        setTitle("Pizza Order");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }
}

class PizzaOrderController extends JPanel {

    private final JComboBox<String> sizeCombo;
    private final JTextField toppingsField;
    private final JSpinner numPizzasSpinner;
    private final JButton orderButton;
    private final JButton cancelButton;
    private final JTextArea receiptArea;

    private final File orderSoundFile = new File("src\\pip.wav"); // Replace with the actual path
    private final File cancelSoundFile = new File("src\\can.wav"); // Replace with the actual path

    public PizzaOrderController() {
        setLayout(new BorderLayout());

        // Create input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        String[] sizes = {"Small", "Medium", "Large"};
        sizeCombo = new JComboBox<>(sizes);
        addRow(inputPanel, "Size:", sizeCombo, gbc);

        toppingsField = new JTextField(20);
        addRow(inputPanel, "Toppings:", toppingsField, gbc);

        numPizzasSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        addRow(inputPanel, "Number of Pizzas:", numPizzasSpinner, gbc);

        orderButton = new JButton("Order");
        addRow(inputPanel, "", orderButton, gbc);

        cancelButton = new JButton("Cancel");
        addRow(inputPanel, "", cancelButton, gbc);

        // Create receipt panel
        receiptArea = new JTextArea(10, 20);
        receiptArea.setEditable(false);

        JPanel receiptPanel = new JPanel(new BorderLayout());
        receiptPanel.add(new JLabel("Receipt:"), BorderLayout.NORTH);
        receiptPanel.add(new JScrollPane(receiptArea), BorderLayout.CENTER);

        // Add input and receipt panels to the controller
        add(inputPanel, BorderLayout.NORTH);
        add(receiptPanel, BorderLayout.CENTER);

        // Listeners
        orderButton.addActionListener(new OrderListener());
        cancelButton.addActionListener(new CancelListener());
    }

    private void addRow(JPanel panel, String label, JComponent component, GridBagConstraints gbc) {
        panel.add(new JLabel(label), gbc);
        gbc.gridx++;
        panel.add(component, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
    }

    private class OrderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                // Get input
                String size = (String) sizeCombo.getSelectedItem();
                String toppings = toppingsField.getText();
                int numPizzas = (Integer) numPizzasSpinner.getValue();

                // Calculate and display receipt
                PizzaOrderService orderService = new PizzaOrderService();
                double price = orderService.calculatePrice(size, toppings, numPizzas);
                String receipt = orderService.generateReceipt(size, toppings, numPizzas, price);

                receiptArea.setText(receipt);

                // Play order sound
                playSound(orderSoundFile);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    }

    private class CancelListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Clear input fields and receipt area
            sizeCombo.setSelectedIndex(0);
            toppingsField.setText("");
            numPizzasSpinner.setValue(1);
            receiptArea.setText("");

            // Play cancel sound
            playSound(cancelSoundFile);
        }
    }

    private void playSound(File soundFile) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }
}

class PizzaOrderService {

    public double calculatePrice(String size, String toppings, int numPizzas) {
        double basePrice = 0;

        if (size.equalsIgnoreCase("Small")) {
            basePrice = 10;
        } else if (size.equalsIgnoreCase("Medium")) {
            basePrice = 12;
        } else if (size.equalsIgnoreCase("Large")) {
            basePrice = 14;
        }

        double toppingPricePerPizza = toppings.split(",").length * 0.5;

        return (basePrice + toppingPricePerPizza) * numPizzas;
    }

    public String generateReceipt(String size, String toppings, int numPizzas, double price) {
        String receipt = "===== RECEIPT =====\n";
        receipt += "Size: " + size + "\n";
        receipt += "Toppings: " + toppings + "\n";
        receipt += "Number of Pizzas: " + numPizzas + "\n";
        receipt += "Total Price: $" + price + "\n";

        return receipt;
    }
}
