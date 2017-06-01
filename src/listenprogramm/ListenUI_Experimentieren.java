/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listenprogramm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Philipp
 */
public class ListenUI_Experimentieren extends javax.swing.JFrame {

    
    //Der Vector der als Listmodel fungiert
    Vector jListListenVector;
    //Der Vector in dem Die Listen-Objekte gespeichert sind
    Vector listenVector;
    
    
    //Der Vector für die Einträge
    Vector eintraegeVector;
    /**
     * Creates new form ListenUI
     */
    public ListenUI_Experimentieren() {
        
        //die Vectoren initialisieren und den Listen übergeben
        listenVector = new Vector();   
        jListListenVector = new Vector();
         eintraegeVector = new Vector();
         
        initComponents();
        listenListe.setListData(jListListenVector);
        eintraegeListe.setListData(eintraegeVector);
        
        
         //Actionlistener für den Plus Button
        /* Zuerst wird ein neues ListenObjekt erstellt, welches anschließend
        dem Vector, in dem alle ListenObjekte gespeichtert werden,
        hinzugefügt wird. Dann wird der Titel der Liste dem ListModel der 
        JList hinzugefügt und dieses aktualisiert um das hinzugefügte
        Element sichtbar zu machen. */
        addListe.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Liste neueListe = new Liste(listenEingabe.getText(), new Vector());
                listenVector.add(neueListe);
                jListListenVector.add(neueListe.getTitle());
                listenEingabe.setText("");
                listenListe.setListData(jListListenVector);
            }
        });
        
        
        //ActionListener für den Löschen Button
        /*Zunächst wird das in der JList ausgewählt Listen-Objekt aus 
        dem dazugehörigen Vector gelöscht. Anschließend wird auch der Eintrag
        in der JList selbst gelöscht und das ListModel aktualisiert.*/
        deleteListe.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                listenVector.remove(listenListe.getSelectedIndex());
                jListListenVector.remove(listenListe.getSelectedIndex());
                listenListe.setListData(jListListenVector);
            }
        });
        
        
        
          //Übergeben der Listen an das Rechte Panel
        /*Zunächst wird der derzeit angewählte Listeneintrag ermittelt.
        anschließend wird die, durch den index ermittelte, Liste
        aus dem dazugehörigen Vector ermittelt. 
        Von dieser wird nun ihr Vector mit Listeneinträgen durch die
        getVector Methode zurückgegeben. Dieser wird zuletzt in einer Variable gespeichert,
        welche dann der JList im rechten Panel als ListModel übergeben wird.*/
        listenListe.addListSelectionListener(new ListSelectionListener(){
 
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = listenListe.getSelectedIndex();
                Liste ausgewaehlteListe = (Liste) listenVector.elementAt(index);
                Vector uebertragenerVector = ausgewaehlteListe.getVector();
                eintraegeVector = uebertragenerVector;
                eintraegeListe.setListData(eintraegeVector);
            }
        });
        
        
                
        //ActionListener für den Plus Button
        addEintrag.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String eintrag = eintragEingabe.getText();
                eintraegeVector.add(eintrag);
                eintragEingabe.setText("");
                eintraegeListe.setListData(eintraegeVector);
            }
        });
        
        
        
        
        //Listeneintrag löschen
        deleteEintrag.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                eintraegeVector.remove(eintraegeListe.getSelectedIndex());
                eintraegeListe.setListData(eintraegeVector);
            }
        });
        
        
        
        
        
        
        //Funktionierender Code zum Laden des JListListenVectors, ohne das Laden der
        //anderen Vectoren jedoch nicht einsetzbar
        try(BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Philipp\\Documents\\Listen\\listentitel.txt"))){
            String line;
            try {
                while((line = br.readLine()) != null){
                    jListListenVector.add(line);
                    listenVector.add(new Liste(line, new Vector()));
                }
            } catch (IOException ex) {
                Logger.getLogger(ListenProgramm.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ListenProgramm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ListenProgramm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        //Funktioniernder Code zum Laden der einzelnen Listenelemente
        for(int i=0; i < jListListenVector.size(); i++){
            try(BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Philipp\\Documents\\Listen\\" + jListListenVector.elementAt(i) + ".txt"))){
            String line;
            Liste aktuelleListe = (Liste) listenVector.elementAt(i);
            try {
                while((line = br.readLine()) != null){
                    String neuerEintrag = line;
                    Vector aktuellerVector = aktuelleListe.getVector();
                    aktuellerVector.add(line);
                }
            } catch (IOException ex) {
                Logger.getLogger(ListenProgramm.class.getName()).log(Level.SEVERE, null, ex);
            }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ListenProgramm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ListenProgramm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        //Beim Schließen des Fensters werden alle Listen gespeichert
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                
                //Speichern der Listentitel in einer eigenen Datei
                try {
                    BufferedWriter outputWriter = null;
                    try {
                        outputWriter = new BufferedWriter(new FileWriter("C:\\Users\\Philipp\\Documents\\Listen\\listentitel.txt"));
                    } catch (IOException ex) {
                        Logger.getLogger(ListenProgramm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    for(int i=0; i < jListListenVector.size(); i++){
                        try {
                            outputWriter.write((String) jListListenVector.elementAt(i));
                            outputWriter.newLine();
                        } catch (IOException ex) {
                            Logger.getLogger(ListenProgramm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    outputWriter.flush();
                    outputWriter.close();
                } catch (IOException ex) {
                    Logger.getLogger(ListenProgramm.class.getName()).log(Level.SEVERE, null, ex);
                }
               
                
                //Speichern der vollständigen Listen in jeweils einer eigenen Datei für jede Liste
                for(int i=0; i < jListListenVector.size(); i++){
                    Liste aktuelleListe = (Liste) listenVector.elementAt(i);
                    Vector aktuellerVector = aktuelleListe.getVector();
                    try {
                    BufferedWriter outputWriter = null;
                    try {
                        outputWriter = new BufferedWriter(new FileWriter("C:\\Users\\Philipp\\Documents\\Listen\\" + aktuelleListe.getTitle() + ".txt"));
                    } catch (IOException ex) {
                        Logger.getLogger(ListenProgramm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    for(int j=0; j < aktuellerVector.size(); j++){
                        try {
                            outputWriter.write((String) aktuellerVector.elementAt(j));
                            outputWriter.newLine();
                        } catch (IOException ex) {
                            Logger.getLogger(ListenProgramm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    outputWriter.flush();
                    outputWriter.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ListenProgramm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                
                System.exit(0);
            }
        } );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        addListe = new javax.swing.JButton();
        deleteListe = new javax.swing.JButton();
        listenEingabe = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        listenListe = new javax.swing.JList<>();
        jPanel2 = new javax.swing.JPanel();
        addEintrag = new javax.swing.JButton();
        deleteEintrag = new javax.swing.JButton();
        eintragEingabe = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        eintraegeListe = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setPreferredSize(new java.awt.Dimension(270, 203));

        addListe.setText("Hinzufügen");

        deleteListe.setText("Löschen");
        deleteListe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteListeActionPerformed(evt);
            }
        });

        listenListe.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(listenListe);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(listenEingabe, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addListe))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(deleteListe)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addListe, deleteListe});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addListe)
                    .addComponent(listenEingabe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteListe)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setPreferredSize(new java.awt.Dimension(270, 493));

        addEintrag.setText("Hinzufügen");

        deleteEintrag.setText("Löschen");

        eintragEingabe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eintragEingabeActionPerformed(evt);
            }
        });

        eintraegeListe.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        eintraegeListe.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(eintraegeListe);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(deleteEintrag))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(eintragEingabe, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addEintrag)))
                        .addContainerGap())))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addEintrag, deleteEintrag});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addEintrag)
                    .addComponent(eintragEingabe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteEintrag)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jScrollPane4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextPane1.setEditable(false);
        jTextPane1.setBorder(null);
        jTextPane1.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jTextPane1.setText("I feel tired of passing out before dark \nand losing a vital spark of my childhood charm \nIt was fun but now I’ve turned into a bore \nNo more hospital wards \nand I owe you one. \n\nI could’ve let you slip through my fingers \nSleepless Sundays put through the wringer \nIt’s something I wanna do \nbut they doubt that I’ll see it through \nI’m half convinced that’s how things are gonna be. \n\nAt times we're all invaded by doubt \nA desire to blow out, a flickering flame \nNo shame, a drunken reverie \nNo pain, till you're a memory \nThough I've never been the king of moderation \n\nI did upset you when I was on a winner \nAnd will I still shine in your eyes as my hair gets thinner \nIt’s something I wanna do \nbut they doubt that I’ll see it through \nI’m half convinced that’s how things are gonna be.");
        jScrollPane4.setViewportView(jTextPane1);

        jButton1.setText("Zusatzinfo bearbeiten");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Zusatzinfo speichern");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton2});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4))
        );

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void deleteListeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteListeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteListeActionPerformed

    private void eintragEingabeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eintragEingabeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_eintragEingabeActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(ListenUI_Experimentieren.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListenUI_Experimentieren.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListenUI_Experimentieren.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListenUI_Experimentieren.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ListenUI_Experimentieren().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addEintrag;
    private javax.swing.JButton addListe;
    private javax.swing.JButton deleteEintrag;
    private javax.swing.JButton deleteListe;
    private javax.swing.JList<String> eintraegeListe;
    private javax.swing.JTextField eintragEingabe;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextField listenEingabe;
    private javax.swing.JList<String> listenListe;
    // End of variables declaration//GEN-END:variables
}
