
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
//D:\E\CashFlowMinimizer\cashFlowWithBackgroundImg.html
public class One {

    private JFrame frame;
    private JTextField personsField;
    private JTable transactionsTable;
    private JTextArea resultArea;
    private JButton createTableButton, calculateButton;
    private int numberOfPersons;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                One window = new One();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public One() {
        initialize();
    }

    private void initialize() {
        // Create main frame
        frame = new JFrame("Cash Flow Minimizer");
        frame.setBounds(100, 100, 700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Set light background color
        frame.getContentPane().setBackground(new Color(240, 248, 255));  // AliceBlue

        // Main panel for center content
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(220, 240, 255));  // LightBlue
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel titleLabel = new JLabel("Cash Flow Minimizer");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 48));  // Increased font size
        titleLabel.setForeground(new Color(0, 0, 0)); //black
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Number of persons label
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel personsLabel = new JLabel("Enter number of persons:");
        personsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(personsLabel, gbc);

        // Input field for number of persons
        gbc.gridx = 1;
        personsField = new JTextField(5);
        personsField.setFont(new Font("Arial", Font.PLAIN, 20));
        mainPanel.add(personsField, gbc);

        // Create Table button
        gbc.gridx = 0;
        gbc.gridy = 2;
        createTableButton = new JButton("Create Table");
        createTableButton.setFont(new Font("Arial", Font.BOLD, 20));
        createTableButton.setBackground(new Color(255, 138, 138)); 
        createTableButton.setForeground(Color.BLACK);
        mainPanel.add(createTableButton, gbc);

        // Minimize Transactions button
        gbc.gridx = 1;
        calculateButton = new JButton("Minimize Transactions");
        calculateButton.setFont(new Font("Arial", Font.BOLD, 20));
        calculateButton.setBackground(new Color(138, 191, 163)); // Minimize transaction Button
        calculateButton.setForeground(Color.BLACK);
        calculateButton.setEnabled(false);
        mainPanel.add(calculateButton, gbc);

        // Adding main panel to frame
        frame.add(mainPanel, BorderLayout.NORTH);

        // Scroll pane for transaction table
        transactionsTable = new JTable();
        transactionsTable.setRowHeight(40); 
        transactionsTable.setBackground(new Color(255, 233, 208));  // I/P table colour
        transactionsTable.setForeground(new Color(0, 0, 0));  
        transactionsTable.setGridColor(new Color(0, 0, 0));  // Steel blue grid lines

        JScrollPane tableScrollPane = new JScrollPane(transactionsTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 250)); // Adjusted height
        frame.add(tableScrollPane, BorderLayout.CENTER);

        // Result area for minimized transactions
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setPreferredSize(new Dimension(600, 150));
        frame.add(resultScrollPane, BorderLayout.SOUTH);

        // Button Actions
        createTableButton.addActionListener(e -> createTable());
        calculateButton.addActionListener(e -> minimizeTransactions());
    }

    // Create transaction table dynamically based on input
    private void createTable() {
        try {
            numberOfPersons = Integer.parseInt(personsField.getText());
            if (numberOfPersons <= 1) {
                JOptionPane.showMessageDialog(frame, "Enter at least 2 persons.");
                return;
            }
            String[] columns = new String[numberOfPersons];
            for (int i = 0; i < numberOfPersons; i++) {
                columns[i] = "Person " + (i + 1);
            }

            transactionsTable.setModel(new javax.swing.table.DefaultTableModel(new Object[numberOfPersons][numberOfPersons], columns));
            calculateButton.setEnabled(true); // Enable minimize button after table creation
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid number.");
        }
    }

    // Calculate minimized transactions//////////////////////////////////////////
    private void minimizeTransactions() {
        int[][] transactions = new int[numberOfPersons][numberOfPersons];
        try {
            for (int i = 0; i < numberOfPersons; i++) {
                for (int j = 0; j < numberOfPersons; j++) {
                    if (i != j) {
                        transactions[i][j] = Integer.parseInt(transactionsTable.getValueAt(i, j).toString());
                    }
                }
            }

            int[] netBalance = new int[numberOfPersons];
            for (int i = 0; i < numberOfPersons; i++) {
                for (int j = 0; j < numberOfPersons; j++) {
                    netBalance[i] += (transactions[j][i] - transactions[i][j]);
                }
            }

            resultArea.setText(""); // Clear previous results
            settleTransactions(netBalance);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Please fill the table with valid numbers.");
        }
    }

    // Helper method to settle debts
    private void settleTransactions(int[] netBalance) {
        while (true) {
            int maxCreditor = findMax(netBalance);
            int maxDebtor = findMin(netBalance);

            if (netBalance[maxCreditor] == 0 && netBalance[maxDebtor] == 0) {
                break;
            }

            int minValue = Math.min(-netBalance[maxDebtor], netBalance[maxCreditor]);
            netBalance[maxCreditor] -= minValue;
            netBalance[maxDebtor] += minValue;

            resultArea.append("Person " + (maxDebtor + 1) + " pays " + minValue + " to Person " + (maxCreditor + 1) + "\n");
        }
    }

    private int findMax(int[] balance) {
        int maxIndex = 0;
        for (int i = 1; i < balance.length; i++) {
            if (balance[i] > balance[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private int findMin(int[] balance) {
        int minIndex = 0;
        for (int i = 1; i < balance.length; i++) {
            if (balance[i] < balance[minIndex]) {
                minIndex = i;
            }
        }
        return minIndex;
    }
}

