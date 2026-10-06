import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.*;


public class ExpenseAnalyzer extends JFrame {


   JTextField amountField;
   JComboBox<String> categoryBox;
   JTable table;
   DefaultTableModel model;
   JLabel budgetLabel;


   java.util.List<Expense> expenses = new ArrayList<>();
   double budget = 0;


   ExpenseAnalyzer() {


       setTitle("Personal Expense Analyzer");
       setSize(800, 600);
       setLocationRelativeTo(null);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setLayout(new BorderLayout());


       // TOP PANEL
       JPanel topContainer = new JPanel(new BorderLayout());
       topContainer.setBackground(new Color(255, 243, 205));


       JLabel title = new JLabel("Personal Expense Analyzer", JLabel.CENTER);
       title.setFont(new Font("Arial", Font.BOLD, 22));
       title.setOpaque(true);
       title.setBackground(new Color(52, 152, 219));
       title.setForeground(Color.WHITE);
       title.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
       topContainer.add(title, BorderLayout.NORTH);


       JPanel budgetPanel = new JPanel();
       budgetPanel.setBackground(new Color(255, 243, 205));


       JButton budgetBtn = new JButton("Set Budget");
       budgetBtn.setBackground(new Color(231, 76, 60));
       budgetBtn.setForeground(Color.WHITE);
       budgetBtn.setFocusPainted(false);


       budgetLabel = new JLabel("Budget: Not Set");


       budgetPanel.add(budgetBtn);
       budgetPanel.add(budgetLabel);


       topContainer.add(budgetPanel, BorderLayout.CENTER);


       // INPUT PANEL
       JPanel inputPanel = new JPanel();
       inputPanel.setBorder(BorderFactory.createTitledBorder("Add Expense"));
       inputPanel.setBackground(new Color(236, 240, 241));


       amountField = new JTextField(10);


       String[] categories = {"Food", "Travel", "Shopping", "Bills", "Other"};
       categoryBox = new JComboBox<>(categories);


       JButton addBtn = new JButton("Add");
       JButton analyzeBtn = new JButton("Analyze");


       addBtn.setBackground(new Color(46, 204, 113));
       analyzeBtn.setBackground(new Color(155, 89, 182));


       addBtn.setForeground(Color.WHITE);
       analyzeBtn.setForeground(Color.WHITE);


       inputPanel.add(new JLabel("Amount:"));
       inputPanel.add(amountField);
       inputPanel.add(new JLabel("Category:"));
       inputPanel.add(categoryBox);
       inputPanel.add(addBtn);
       inputPanel.add(analyzeBtn);


       topContainer.add(inputPanel, BorderLayout.SOUTH);


       add(topContainer, BorderLayout.NORTH);


       // TABLE
       model = new DefaultTableModel(new String[]{"Amount (₹)", "Category"}, 0);
       table = new JTable(model);


       JTableHeader header = table.getTableHeader();
       header.setBackground(new Color(25, 42, 86));
       header.setForeground(Color.WHITE);
       header.setFont(new Font("Arial", Font.BOLD, 14));


       table.setRowHeight(28);


       add(new JScrollPane(table), BorderLayout.CENTER);


       // FOOTER
       JLabel footer = new JLabel("Track Smart • Save More", JLabel.CENTER);
       footer.setOpaque(true);
       footer.setBackground(new Color(52, 73, 94));
       footer.setForeground(Color.WHITE);
       add(footer, BorderLayout.SOUTH);


       // ADD EXPENSE
       addBtn.addActionListener(e -> {
           try {
               double amount = Double.parseDouble(amountField.getText());
               String category = String.valueOf(categoryBox.getSelectedItem());


               expenses.add(new Expense(amount, category));
               model.addRow(new Object[]{amount, category});


               amountField.setText("");


           } catch (Exception ex) {
               JOptionPane.showMessageDialog(null, "Enter valid amount!");
           }
       });


       // SET BUDGET
       budgetBtn.addActionListener(e -> {
           String input = JOptionPane.showInputDialog("Enter Budget:");
           if (input == null || input.trim().isEmpty()) return;


           try {
               budget = Double.parseDouble(input);
               budgetLabel.setText("Budget: ₹" + budget);
           } catch (Exception ex) {
               JOptionPane.showMessageDialog(null, "Invalid input!");
           }
       });


       // ANALYZE
       analyzeBtn.addActionListener(e -> {
           if (budget <= 0) {
               JOptionPane.showMessageDialog(null, "Set budget first!");
               return;
           }
           analyzeData();
       });


       setVisible(true);
   }


   // ANALYSIS
   void analyzeData() {


       double total = 0;
       HashMap<String, Double> map = new HashMap<>();


       for (Expense e : expenses) {
           total += e.amount;
           map.put(e.category, map.getOrDefault(e.category, 0.0) + e.amount);
       }


       String result = "Total Expense: ₹" + total + "\n\n";


       for (String key : map.keySet()) {
           result += key + ": ₹" + map.get(key) + "\n";
       }


       if (total > budget) {
           double exceed = total - budget;


           JOptionPane.showMessageDialog(
                   null,
                   "⚠ Budget Exceeded!\nExceeded by ₹" + exceed,
                   "Warning",
                   JOptionPane.ERROR_MESSAGE
           );


           JOptionPane.showMessageDialog(null, result);
           return;
       }


       JOptionPane.showMessageDialog(null, result);
       showPieChart(map, total);
   }


   // PIE CHART CALL
   void showPieChart(HashMap<String, Double> data, double total) {
       new PieChartFrame(data, total, budget);
   }


   public static void main(String[] args) {
       new ExpenseAnalyzer();
   }
}
