import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class Inventori extends javax.swing.JFrame {

 
    public Inventori() {
        initComponents();
         SwingUtilities.invokeLater(() -> checkLowStocks());
        
 loadData(); 
jTable1.getColumn("          Action").setCellRenderer(new ButtonRenderer());
jTable1.getColumn("          Action").setCellEditor(new ButtonEditor(jTable1));

      // Set fixed column widths
    jTable1.getColumnModel().getColumn(0).setPreferredWidth(150);
    jTable1.getColumnModel().getColumn(0).setMinWidth(150);
    jTable1.getColumnModel().getColumn(0).setMaxWidth(150);
    jTable1.getColumnModel().getColumn(0).setResizable(false);

    jTable1.getColumnModel().getColumn(1).setPreferredWidth(80);
    jTable1.getColumnModel().getColumn(1).setMinWidth(80);
    jTable1.getColumnModel().getColumn(1).setMaxWidth(80);
    jTable1.getColumnModel().getColumn(1).setResizable(false);

    jTable1.getColumnModel().getColumn(2).setPreferredWidth(180);
    jTable1.getColumnModel().getColumn(2).setMinWidth(180);
    jTable1.getColumnModel().getColumn(2).setMaxWidth(180);
    jTable1.getColumnModel().getColumn(2).setResizable(false);

    jTable1.getColumnModel().getColumn(3).setPreferredWidth(100);
    jTable1.getColumnModel().getColumn(3).setMinWidth(100);
    jTable1.getColumnModel().getColumn(3).setMaxWidth(100);
    jTable1.getColumnModel().getColumn(3).setResizable(false);

    jTable1.getColumnModel().getColumn(4).setPreferredWidth(100);
    jTable1.getColumnModel().getColumn(4).setMinWidth(100);
    jTable1.getColumnModel().getColumn(4).setMaxWidth(100);
    jTable1.getColumnModel().getColumn(4).setResizable(false);
    
    
    }
    
    
    
    
    public void checkLowStocks() {
    try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdata", "root", "");
        String query = "SELECT variety_name, stock, threshold FROM inventory WHERE stock < threshold";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        StringBuilder alert = new StringBuilder();
        while (rs.next()) {
            String variety = rs.getString("variety_name");
            double stock = rs.getDouble("stock");
            double threshold = rs.getDouble("threshold");

            alert.append("• ").append(variety)
                 .append(" — Stock: ").append(stock)
                 .append("kg (Threshold: ").append(threshold).append("kg)\n");
        }

        if (alert.length() > 0) {
            JOptionPane.showMessageDialog(null,
                "⚠ Low Stock Detected:\n\n" + alert.toString(),
                "Low Stock Alert",
                JOptionPane.WARNING_MESSAGE);
        }

        rs.close();
        stmt.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error checking low stocks: " + e.getMessage());
    }
}

    
    
    
    
    // Renders buttons in JTable
class ButtonRenderer extends JPanel implements TableCellRenderer {
    public ButtonRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        JButton editBtn = new JButton("Edit");
  editBtn.setBackground(new java.awt.Color(0, 153, 76));
    editBtn.setForeground(java.awt.Color.WHITE);
    editBtn.setFocusPainted(false);
    editBtn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
    editBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    
        JButton deleteBtn = new JButton("Delete");
    deleteBtn.setBackground(new java.awt.Color(204, 0, 0));
    deleteBtn.setForeground(java.awt.Color.WHITE);
    deleteBtn.setFocusPainted(false);
    deleteBtn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
    deleteBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        add(editBtn);
        add(deleteBtn);
    }

    
    
    
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}

// Handles button actions
class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private JButton editBtn, deleteBtn;
    private JTable table;

    public ButtonEditor(JTable table) {
        this.table = table;
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

editBtn = new JButton("Edit");
editBtn.setBackground(new java.awt.Color(0, 153, 76)); // green
editBtn.setForeground(java.awt.Color.WHITE);
editBtn.setFocusPainted(false);
editBtn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
editBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

deleteBtn = new JButton("Delete");
deleteBtn.setBackground(new java.awt.Color(204, 0, 0)); // red
deleteBtn.setForeground(java.awt.Color.WHITE);
deleteBtn.setFocusPainted(false);
deleteBtn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
deleteBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));



        panel.add(editBtn);
        panel.add(deleteBtn);

        
        
        
        
        
        
        
        
        
        //EDIT BUTTON
       editBtn.addActionListener(e -> {
    int row = table.getSelectedRow();
    String variety = table.getValueAt(row, 0).toString();
    String stock = table.getValueAt(row, 1).toString();
    String threshold = table.getValueAt(row, 2).toString();

    // Create dialog
    JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(table), "Edit Inventory", true);
    dialog.setSize(350, 250);
    dialog.setLayout(null);
    dialog.setLocationRelativeTo(table);

    // Labels
    JLabel nameLabel = new JLabel("Variety Name:");
    JLabel stockLabel = new JLabel("Stock (kg):");
    JLabel thresholdLabel = new JLabel("Threshold (kg):");

    nameLabel.setBounds(20, 20, 100, 25);
    stockLabel.setBounds(20, 60, 100, 25);
    thresholdLabel.setBounds(20, 100, 120, 25);

    // Fields
    JTextField nameField = new JTextField(variety);
    nameField.setEnabled(false);
    JTextField stockField = new JTextField(stock);
    JTextField thresholdField = new JTextField(threshold);

    nameField.setBounds(140, 20, 170, 25);
    stockField.setBounds(140, 60, 170, 25);
    thresholdField.setBounds(140, 100, 170, 25);

    // Buttons
    JButton saveBtn = new JButton("Update");
    JButton cancelBtn = new JButton("Cancel");

    saveBtn.setBounds(60, 150, 100, 30);
    cancelBtn.setBounds(180, 150, 100, 30);

    saveBtn.setBackground(new java.awt.Color(0, 153, 76));
    saveBtn.setForeground(java.awt.Color.WHITE);
    cancelBtn.setBackground(new java.awt.Color(204, 0, 0));
    cancelBtn.setForeground(java.awt.Color.WHITE);

    // Add components
    dialog.add(nameLabel);
    dialog.add(stockLabel);
    dialog.add(thresholdLabel);
    dialog.add(nameField);
    dialog.add(stockField);
    dialog.add(thresholdField);
    dialog.add(saveBtn);
    dialog.add(cancelBtn);

    // Button Actions
    saveBtn.addActionListener(ev -> {
        try {
            double newStock = Double.parseDouble(stockField.getText());
            double newThreshold = Double.parseDouble(thresholdField.getText());

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdata", "root", "");
            String query = "UPDATE inventory SET stock = ?, threshold = ? WHERE variety_name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setDouble(1, newStock);
            stmt.setDouble(2, newThreshold);
            stmt.setString(3, variety);
            int updated = stmt.executeUpdate();
            conn.close();

            if (updated > 0) {
                table.setValueAt(newStock, row, 1);
                table.setValueAt(newThreshold, row, 2);
                JOptionPane.showMessageDialog(table, "Updated: " + variety);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Update failed.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, "Invalid input or DB error.");
        }
    });

    cancelBtn.addActionListener(ev -> dialog.dispose());

    // Show dialog
    dialog.setVisible(true);
});
       
       
       
       
       
       
       //DELETE BUTTON
       deleteBtn.addActionListener(e -> {
    int row = table.getSelectedRow();
    String variety = table.getValueAt(row, 0).toString();

    // Create custom dialog
    JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(table), "Confirm Delete", true);
    dialog.setSize(350, 180);
    dialog.setLayout(null);
    dialog.setLocationRelativeTo(table);

    // Label
    JLabel messageLabel = new JLabel("Are you sure you want to delete:");
    JLabel nameLabel = new JLabel("\"" + variety + "\"?");
    messageLabel.setBounds(40, 20, 280, 25);
    nameLabel.setBounds(100, 50, 200, 25);
    nameLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
    nameLabel.setForeground(new java.awt.Color(204, 0, 0));

    // Buttons
    JButton yesButton = new JButton("Yes");
    JButton noButton = new JButton("Cancel");

    yesButton.setBounds(60, 100, 100, 30);
    noButton.setBounds(180, 100, 100, 30);

    yesButton.setBackground(new java.awt.Color(204, 0, 0));
    yesButton.setForeground(java.awt.Color.WHITE);
    noButton.setBackground(new java.awt.Color(0, 153, 76));
    noButton.setForeground(java.awt.Color.WHITE);

    dialog.add(messageLabel);
    dialog.add(nameLabel);
    dialog.add(yesButton);
    dialog.add(noButton);

    // Actions
    yesButton.addActionListener(ev -> {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdata", "root", "");
            String query = "DELETE FROM inventory WHERE variety_name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, variety);
            int deleted = stmt.executeUpdate();
            conn.close();

            if (deleted > 0) {
                ((DefaultTableModel) table.getModel()).removeRow(row);
                JOptionPane.showMessageDialog(table, "Deleted: " + variety);
            } else {
                JOptionPane.showMessageDialog(table, "Delete failed.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, "Error deleting from database.");
        }

        dialog.dispose();
        fireEditingStopped();
    });

    noButton.addActionListener(ev -> {
        dialog.dispose();
        fireEditingStopped();
    });

    dialog.setVisible(true);
});
       
       
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int column) {
        return panel;
    }

    public Object getCellEditorValue() {
        return null;
    }
}

    private void loadData() {
            
  
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0); // Clear existing rows

    
    
   try {
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdata", "root", "");
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT variety_name, stock, threshold FROM inventory");

    while (rs.next()) {
        String variety = rs.getString("variety_name");
        double stock = rs.getDouble("stock");
        double threshold = rs.getDouble("threshold");

        // check s status
        String status = (stock < threshold) ? "Insufficient" : "Sufficient";

        // Update the status 
        PreparedStatement updateStmt = conn.prepareStatement(
            "UPDATE inventory SET status = ? WHERE variety_name = ?"
        );
        updateStmt.setString(1, status);
        updateStmt.setString(2, variety);
        updateStmt.executeUpdate();

        // Add row to the table model
        model.addRow(new Object[]{variety, stock, threshold, status, "Actions"});
    }

    conn.close();
} catch (SQLException e) {
    e.printStackTrace();
}

   }
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        invbutton = new javax.swing.JToggleButton();
        addbutton = new javax.swing.JToggleButton();
        recShipbutton = new javax.swing.JToggleButton();
        recSalebutton = new javax.swing.JToggleButton();
        hisbutton = new javax.swing.JToggleButton();
        databutton = new javax.swing.JToggleButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 51)));

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 51), 3, true));

        buttonGroup1.add(invbutton);
        invbutton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        invbutton.setForeground(new java.awt.Color(0, 153, 51));
        invbutton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/inventory-alt.png"))); // NOI18N
        invbutton.setSelected(true);
        invbutton.setText("Inventory");
        invbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invbuttonActionPerformed(evt);
            }
        });

        buttonGroup1.add(addbutton);
        addbutton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addbutton.setForeground(new java.awt.Color(0, 153, 51));
        addbutton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/square-plus.png"))); // NOI18N
        addbutton.setText("Add Item");
        addbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addbuttonActionPerformed(evt);
            }
        });

        buttonGroup1.add(recShipbutton);
        recShipbutton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        recShipbutton.setForeground(new java.awt.Color(0, 153, 51));
        recShipbutton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/truck-arrow-right.png"))); // NOI18N
        recShipbutton.setText("Record Shipment");
        recShipbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recShipbuttonActionPerformed(evt);
            }
        });

        buttonGroup1.add(recSalebutton);
        recSalebutton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        recSalebutton.setForeground(new java.awt.Color(0, 153, 51));
        recSalebutton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/shopping-cart.png"))); // NOI18N
        recSalebutton.setText("Record Sale");
        recSalebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recSalebuttonActionPerformed(evt);
            }
        });

        buttonGroup1.add(hisbutton);
        hisbutton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        hisbutton.setForeground(new java.awt.Color(0, 153, 51));
        hisbutton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/time-past.png"))); // NOI18N
        hisbutton.setText("History");
        hisbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hisbuttonActionPerformed(evt);
            }
        });

        databutton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        databutton.setForeground(new java.awt.Color(0, 153, 51));
        databutton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logout.png"))); // NOI18N
        databutton.setText("Log Out");
        databutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                databuttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(invbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(addbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(recShipbutton))
                    .addComponent(hisbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(recSalebutton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(databutton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(31, 31, 31))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(invbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recShipbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recSalebutton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hisbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(databutton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 102), 3, true));

        jLabel4.setBackground(new java.awt.Color(0, 102, 102));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 102));
        jLabel4.setText("Current Inventory");

        jScrollPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 3, true));
        jScrollPane1.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 51), 3, true));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "            Variety Name", "    Stock (kg)", "      Low Stock Threshold (kg)", "         Status", "          Action"
            }
        ));
        jTable1.setAlignmentX(1.0F);
        jTable1.setAlignmentY(1.0F);
        jTable1.setRowHeight(50);
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel4))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 616, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                .addGap(39, 39, 39))
        );

        jLabel9.setFont(new java.awt.Font("Segoe UI Historic", 1, 28)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 102, 102));
        jLabel9.setText("BIGASAN INVENTORY MANAGEMENT SYSTEM");

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/eye (1).png"))); // NOI18N
        jLabel8.setText("jLabel8");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents




    private void addbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addbuttonActionPerformed
         AddItem addFrame = new AddItem();
        addFrame.setVisible(true);
        addFrame.pack();
        addFrame.setLocationRelativeTo(null);
        this.dispose(); // open new frame
        
    }//GEN-LAST:event_addbuttonActionPerformed

    private void invbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invbuttonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_invbuttonActionPerformed

    private void hisbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hisbuttonActionPerformed
     History hisFrame = new History();
        hisFrame.setVisible(true);
        hisFrame.pack();
       hisFrame.setLocationRelativeTo(null);
        this.dispose(); // open new frame
    }//GEN-LAST:event_hisbuttonActionPerformed

    private void recShipbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recShipbuttonActionPerformed
        RecordShipment recshipFrame = new RecordShipment();
        recshipFrame.setVisible(true);
         recshipFrame.pack();
         recshipFrame.setLocationRelativeTo(null);
        this.dispose(); // open new frame


    }//GEN-LAST:event_recShipbuttonActionPerformed

    private void recSalebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recSalebuttonActionPerformed
  RecordSale recsaleFrame = new RecordSale();
        recsaleFrame.setVisible(true);
        recsaleFrame.pack();
        recsaleFrame.setLocationRelativeTo(null);
        this.dispose(); // open new frame

    }//GEN-LAST:event_recSalebuttonActionPerformed

    private void databuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_databuttonActionPerformed
        // Optionally confirm logout
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose(); // Close current window
            new Login().setVisible(true); // Open login form
        }
    }//GEN-LAST:event_databuttonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RecordShipment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RecordShipment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RecordShipment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RecordShipment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RecordShipment().setVisible(true);
            }
        });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton addbutton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JToggleButton databutton;
    private javax.swing.JToggleButton hisbutton;
    private javax.swing.JToggleButton invbutton;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToggleButton recSalebutton;
    private javax.swing.JToggleButton recShipbutton;
    // End of variables declaration//GEN-END:variables
}
