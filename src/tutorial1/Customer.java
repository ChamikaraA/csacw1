/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tutorial1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import javax.swing.JOptionPane;
//import org.json.simple.JSONObject;
import org.json.JSONObject;
import org.json.JSONArray;
import java.lang.Object;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;

/**
 *
 * @author User
 */
public class Customer extends javax.swing.JFrame {

  javax.swing.table.DefaultTableModel tableModelJTable2 = new javax.swing.table.DefaultTableModel(); 
  //Vector tableModelJTable2 = new Vector();
    /**
     * Creates new form Customer
     */
    public Customer()  {
       
       // tableModelJTable2.setValueAt("1", 1, 1);
        /*tableModelJTable2.addRow(new Object[]{"1", "hh", "hh"},new String [] {
                "t1", "t2", "t3"
            });*/
        
       /*  commented to test web service call
         tableModelJTable2.addColumn(new String("t1")); //(new String [] {"t1"});
         tableModelJTable2.addColumn(new String("t2"));
         tableModelJTable2.addColumn(new String("t3"));
        Vector row = new Vector();
        row.add("1");
        row.add("def");
        row.add("ghi");
        tableModelJTable2.addRow(row);
        Vector row2 = new Vector();
        row2.add("2");
        row2.add("hjh");
        row2.add("lkh");
        tableModelJTable2.addRow(row2);*/
        
         tableModelJTable2.addColumn(new String("id"));
         tableModelJTable2.addColumn(new String("name"));
         tableModelJTable2.addColumn(new String("birthdate"));
         tableModelJTable2.addColumn(new String("accountNumber"));
        
          try {

		
              URL url = new URL("http://localhost:8080/csa-cw-0.0.1-SNAPSHOT/customers/");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code --: "
					+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
			(conn.getInputStream())));

		String output;
		System.out.println("Output from Server********* .... \n");
                
                StringBuilder sb = new StringBuilder();
                
		while ((output = br.readLine()) != null) {
			System.out.println(output);
                        System.out.println("********* "+output.hashCode());//getBytes("name"));
                      sb.append(output);  
                        
		}
                    try {
                        JSONArray jsonArray = new JSONArray(sb.toString());
                       // JSONObject json = new JSONObject(sb.toString());
                        
                        for(int i =0; i<jsonArray.length() ; i++){
                            JSONObject json = jsonArray.getJSONObject(i);
                            JSONArray jsonArrayaccountnos = json.getJSONArray("accountNumber");
                            System.out.println("*********json read --  "+  json.getString("birthDate"));

                            Vector row = new Vector();
                            row.add(json.getInt("id"));
                            row.add(json.getString("name"));
                            row.add(json.getString("birthDate")); 
                            row.add( jsonArrayaccountnos.get(0).toString() ); // get 0th because no way to display one by one
                            
                            tableModelJTable2.addRow(row);
                        }
                    } catch (JSONException ex) {
                        Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, "Error.something went wrong.");
                    }

		conn.disconnect();

	  } catch (MalformedURLException e) {

		e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error.something went wrong.");
	  } catch (IOException e) {

		e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error.something went wrong.");
	  }
          
        
       
  
        initComponents();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Page - Create new or Change existing customer details"));

        jButton1.setText("Add new Customer");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Or select one from below list");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(132, 132, 132)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        jButton4.setBackground(new java.awt.Color(204, 255, 204));
        jButton4.setText("<-");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jTable2.setModel(tableModelJTable2);
        jTable2.setColumnSelectionAllowed(true);
        jTable2.getTableHeader().setReorderingAllowed(false);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);
        jTable2.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 481, Short.MAX_VALUE)
                        .addComponent(jButton4))
                    .addComponent(jScrollPane2))
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
        );

        jPanel1.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // if customer name and account number does not exist in database, a warning should be given to create new customer. 
        // if name and account number does not match display error message. 
        // NewCustAccount a = new NewCustAccount(); //delete this once all variables are named and assigned appropriately.
         NewCustAccount a =  new NewCustAccount(0,"");
         a.setVisible(true);
         
        /*
        if ((username==CusName) && (AccNo==CusAcc)){
            NewCustAccount a = new NewCustAccount();
            a.setVisible(true);
        }
        else if ((username==CusName) || (AccNo==CusAcc)){
            JOptionPane.showMessageDialog( "Incorrect username or password"); 
        }
        else{
              JOptionPane.showMessageDialog( "Customer Name does not exist /n Create new customer?"); 
        }
        */
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // move back
       // Menu q = new Menu();
      //  q.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
        //System.out.println("-------------------table click1  -"+ jTable2.rowAtPoint(evt.getPoint()) );
       // System.out.println("-------------------table click2  -"+ jTable2.columnAtPoint(evt.getPoint()) );
        System.out.println("-------------------table click on ID  -"+ jTable2.getValueAt(jTable2.rowAtPoint(evt.getPoint()), 0) );
        
           NewCustAccount a = new NewCustAccount(1,jTable2.getValueAt(jTable2.rowAtPoint(evt.getPoint()), 0).toString()); //delete this once all variables are named and assigned appropriately.
            a.setVisible(true);
        
        
    }//GEN-LAST:event_jTable2MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* declare variables */
        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Customer().setVisible(true);
            }
        });
    } 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
