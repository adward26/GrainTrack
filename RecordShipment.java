import java.sql.*;
import javax.swing.*;

public class RecordShipment extends javax.swing.JFrame {

 
    public RecordShipment() {
        initComponents();
        
        
        loadRiceVarieties(cbRiceVariety);
    
        
        
    // Add this to connect the button click to your shipment recording
    btnRecordShip.addActionListener(e -> recordShipment());
      
    }
    
    
 

    public Connection connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/userdata", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed.");
            return null;
        }
    }

    public void loadRiceVarieties(JComboBox<String> comboBox) {
        try {
            Connection conn = connectDB(); // Connect to DB
            String query = "SELECT DISTINCT variety_name FROM inventory";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            comboBox.removeAllItems();
            while (rs.next()) {
                String variety = rs.getString("variety_name");
                comboBox.addItem(variety);
            }

            rs.close();
            pst.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load rice varieties: " + e.getMessage());
        }
        
    }
    private boolean quantityAlreadyChecked = false;

    private void recordShipment() {
    String variety = (String) cbRiceVariety.getSelectedItem();
    String quantityStr = txtQuantity.getText().trim();

    if (variety == null || variety.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please select a rice variety.");
        return;
    }

   if (quantityStr.isEmpty()) {
    if (!quantityAlreadyChecked) {
        JOptionPane.showMessageDialog(this, "Please enter the quantity.");
        quantityAlreadyChecked = true;
    }
    return;
} else {
    quantityAlreadyChecked = false;  // reset if quantity has value
}


    double quantity;
    try {
        quantity = Double.parseDouble(quantityStr);
        if (quantity <= 0) {
            JOptionPane.showMessageDialog(this, "Quantity must be greater than zero.");
            return;
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a number.");
        return;
    }

    Connection conn = null;
    PreparedStatement pstShipment = null;
    PreparedStatement pstUpdateInventory = null;

    try {
        conn = connectDB();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.");
            return;
        }

        conn.setAutoCommit(false);

        // Insert shipment record
        String insertShipmentSQL = "INSERT INTO shipment_history (arrival_date, variety_name, quantity_received) VALUES (?, ?, ?)";
        pstShipment = conn.prepareStatement(insertShipmentSQL);
        pstShipment.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
        pstShipment.setString(2, variety);
        pstShipment.setDouble(3, quantity);

        System.out.println("Executing shipment insert...");
        int rowsInserted = pstShipment.executeUpdate();
        System.out.println("Rows inserted in shipment_history: " + rowsInserted);

        if (rowsInserted <= 0) {
            conn.rollback();
            JOptionPane.showMessageDialog(this, "Failed to record shipment.");
            return;
        }

        // Update inventory stock
        String updateInventorySQL = "UPDATE inventory SET stock = stock + ? WHERE variety_name = ?";
        pstUpdateInventory = conn.prepareStatement(updateInventorySQL);
        pstUpdateInventory.setDouble(1, quantity);
        pstUpdateInventory.setString(2, variety);

        System.out.println("Executing inventory update...");
        int rowsUpdated = pstUpdateInventory.executeUpdate();
        System.out.println("Rows updated in inventory: " + rowsUpdated);

        if (rowsUpdated <= 0) {
            conn.rollback();
            JOptionPane.showMessageDialog(this, "Failed to update inventory. Variety not found.");
            return;
        }

        conn.commit();

        JOptionPane.showMessageDialog(this, "Shipment recorded successfully.");
        txtQuantity.setText("");

    } catch (Exception ex) {
        try {
            if (conn != null) conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    } finally {
        try {
            if (pstShipment != null) pstShipment.close();
            if (pstUpdateInventory != null) pstUpdateInventory.close();
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        txtQuantity = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cbRiceVariety = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnRecordShip = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 51)));

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 51), 3, true));

        buttonGroup1.add(invbutton);
        invbutton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        invbutton.setForeground(new java.awt.Color(0, 153, 51));
        invbutton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/inventory-alt.png"))); // NOI18N
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
        recShipbutton.setSelected(true);
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
                        .addComponent(recShipbutton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                        .addComponent(recSalebutton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(hisbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(400, 400, 400)
                        .addComponent(databutton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(22, 22, 22))
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

        txtQuantity.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtQuantity.setForeground(new java.awt.Color(102, 102, 102));
        txtQuantity.setText("0");
        txtQuantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuantityActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(0, 102, 102));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 102));
        jLabel4.setText("Record Incoming Shipment");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Rice Variety");

        cbRiceVariety.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbRiceVarietyActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(0, 153, 102));
        jLabel2.setText("Choose the variety that arrived.");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Quantity Recieved (kg)");

        jLabel5.setForeground(new java.awt.Color(0, 153, 102));
        jLabel5.setText("Enter the ammount received in kilograms.");

        btnRecordShip.setBackground(new java.awt.Color(0, 153, 51));
        btnRecordShip.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnRecordShip.setForeground(new java.awt.Color(255, 255, 255));
        btnRecordShip.setText("Record Shipment");
        btnRecordShip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecordShipActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel4))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5)
                            .addComponent(cbRiceVariety, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnRecordShip, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbRiceVariety, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(24, 24, 24)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRecordShip, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jLabel9.setFont(new java.awt.Font("Segoe UI Historic", 1, 28)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 102, 102));
        jLabel9.setText("BIGASAN INVENTORY MANAGEMENT SYSTEM");

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/eye (1).png"))); // NOI18N

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/mill.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap(357, Short.MAX_VALUE)
                    .addComponent(jLabel10)
                    .addGap(337, 337, 337)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(12, 12, 12)))
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap(261, Short.MAX_VALUE)
                    .addComponent(jLabel10)
                    .addGap(220, 220, 220)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbRiceVarietyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRiceVarietyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbRiceVarietyActionPerformed

    private void txtQuantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuantityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuantityActionPerformed

    private void btnRecordShipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecordShipActionPerformed
   
        
        // Get selected rice variety from ComboBox
    String variety = (String) cbRiceVariety.getSelectedItem();
    
    // Get the quantity received from the TextField
    String quantityText = txtQuantity.getText();

    // Validate input
    if (variety == null || variety.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please select a rice variety.");
        return;
    }

    if (quantityText.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter the quantity received.");
        return;
    }

    // Try to parse the quantity to an integer
    int quantity;
    try {
        quantity = Integer.parseInt(quantityText); // Parse quantity to integer
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid quantity format. Please enter a valid number.");
        return;
    }

    // Prepare the query to check the current stock for the selected rice variety
    String checkStockQuery = "SELECT stock FROM inventory WHERE variety_name = ?";

    try (Connection conn = connectDB(); 
         PreparedStatement checkStockPst = conn.prepareStatement(checkStockQuery)) {
        
        // Set parameter for the rice variety
        checkStockPst.setString(1, variety);

        // Execute the query to get the current stock
        ResultSet rs = checkStockPst.executeQuery();

        if (rs.next()) {
            int currentStock = rs.getInt("stock"); // Get the current stock

            // Prepare the query to update the stock
            String updateStockQuery = "UPDATE inventory SET stock = ? WHERE variety_name = ?";

            try (PreparedStatement updateStockPst = conn.prepareStatement(updateStockQuery)) {
                // Set the new stock (current stock + received quantity)
                updateStockPst.setInt(1, currentStock + quantity); // Add quantity to current stock
                updateStockPst.setString(2, variety); // Set the rice variety

                // Execute the update
                int rowsAffected = updateStockPst.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Shipment recorded successfully. New stock updated.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update stock.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Rice variety not found in the inventory.");
        }

        rs.close();
        checkStockPst.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error recording shipment: " + e.getMessage());
    }
    }//GEN-LAST:event_btnRecordShipActionPerformed

    private void addbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addbuttonActionPerformed
         AddItem addFrame = new AddItem();
        addFrame.setVisible(true);
        addFrame.pack();
        addFrame.setLocationRelativeTo(null);
        this.dispose(); // open new frame
        
    }//GEN-LAST:event_addbuttonActionPerformed

    private void invbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invbuttonActionPerformed
 Inventori invFrame = new Inventori();
        invFrame.setVisible(true);
        invFrame.pack();
        invFrame.setLocationRelativeTo(null);
        this.dispose(); // open new frame

    }//GEN-LAST:event_invbuttonActionPerformed

    private void recShipbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recShipbuttonActionPerformed
    
    }//GEN-LAST:event_recShipbuttonActionPerformed

    private void recSalebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recSalebuttonActionPerformed
  RecordSale recsaleFrame = new RecordSale();
        recsaleFrame.setVisible(true);
        recsaleFrame.pack();
        recsaleFrame.setLocationRelativeTo(null);
        this.dispose(); // open new frame


    }//GEN-LAST:event_recSalebuttonActionPerformed

    private void hisbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hisbuttonActionPerformed
       History hisFrame = new History();
        hisFrame.setVisible(true);
        hisFrame.pack();
       hisFrame.setLocationRelativeTo(null);
        this.dispose(); // open new frame
    }//GEN-LAST:event_hisbuttonActionPerformed

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
    private javax.swing.JButton btnRecordShip;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbRiceVariety;
    private javax.swing.JToggleButton databutton;
    private javax.swing.JToggleButton hisbutton;
    private javax.swing.JToggleButton invbutton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JToggleButton recSalebutton;
    private javax.swing.JToggleButton recShipbutton;
    private javax.swing.JTextField txtQuantity;
    // End of variables declaration//GEN-END:variables
}
