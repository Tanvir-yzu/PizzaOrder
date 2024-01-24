import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PizzaOrderGUI());
    }
}

class PizzaOrderGUI extends JFrame {

    private final PizzaOrderController controller;

    public PizzaOrderGUI() {
        controller = new PizzaOrderController();

        setLayout(new BorderLayout());
        add(controller, BorderLayout.CENTER);

        setTitle("Pizza Order");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}

class PizzaOrderController extends JPanel {

    private final JComboBox<String> sizeCombo;
    private final JTextField toppingsField;
    private final JSpinner numPizzasSpinner;
    private final JButton orderButton;
    private final JTextArea receiptArea;

    PizzaOrderController() {
        setLayout(new BorderLayout());

        // Create input panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        String[] sizes = {"Small", "Medium", "Large"};
        sizeCombo = new JComboBox<>(sizes);
        inputPanel.add(new JLabel("Size:"));
        inputPanel.add(sizeCombo);

        toppingsField = new JTextField(20);
        inputPanel.add(new JLabel("Toppings:"));
        inputPanel.add(toppingsField);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 10, 1);
        numPizzasSpinner = new JSpinner(spinnerModel);
        inputPanel.add(new JLabel("Number of Pizzas:"));
        inputPanel.add(numPizzasSpinner);

        orderButton = new JButton("Order");
        inputPanel.add(new JLabel()); // Placeholder for spacing
        inputPanel.add(orderButton);

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
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
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
