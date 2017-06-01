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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
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
public class ListenUI extends javax.swing.JFrame {

    
    //Der Vector der als Listmodel fungiert
    Vector jListListenVector;
    //Der Vector in dem Die Listen-Objekte gespeichert sind
    Vector listenVector;
    
    
    
    //Der Vector mit den Listeneintrag-Objekten
    Vector eintraegeVector;
    //Der Vector für die Einträge der in der 2. JList als Listmodel fungiert
    Vector JEintraegeVector;
    
    /**
     * Creates new form ListenUI
     */
    public ListenUI() throws IOException {
        
        //die Vectoren initialisieren und den Listen übergeben
        listenVector = new Vector();   
        jListListenVector = new Vector();
        eintraegeVector = new Vector();
        JEintraegeVector = new Vector();
         
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
        listenListe.addListSelectionListener(new ListSelectionListener(){
 
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //Das Panel mit der Zusatzinfo wird zurückgesetzt falls direkt die
                //ganze Liste und nicht nur der Eintrag gewechselt wird
                zusatzinfoAnzeige.setText("");
                zusatzinfoAnzeige.setEditable(false);
                
                /*Zunächst wird der derzeit angewählte Listeneintrag ermittelt.
                anschließend wird die, durch den index ermittelte, Liste
                aus dem dazugehörigen Vector ermittelt. 
                Von dieser wird nun ihr Vector mit Listeneinträgen durch die
                getVector Methode zurückgegeben. Dieser wird zuletzt in einer Variable gespeichert,
                welche dann der JList im rechten Panel als ListModel übergeben wird.*/
                int index = listenListe.getSelectedIndex();
               
                /*Durch das Checken ob der Index >= 0 ist wird eine NullPointerException beim direkten
                Wechsel der Listen verhindert.*/
                if(index >= 0){
                    Liste ausgewaehlteListe = (Liste) listenVector.elementAt(index);
                    Vector uebertragenerVector = ausgewaehlteListe.getVector();
                    JEintraegeVector.clear();
                       
                    /*Da der übertragene Vector nur Listeneintrag-Objekte enthält
                    wird hier dynamisch ein Vector für die JList erstellt, der nur
                    die Titel der jeweiligen Listeneinträge enthalt*/
                    for(int i=0; i < uebertragenerVector.size(); i++){
                        Listeneintrag aktuellerEintrag = (Listeneintrag) uebertragenerVector.elementAt(i);
                        String aktuellerTitel = aktuellerEintrag.getTitle();
                        JEintraegeVector.add(aktuellerTitel);
                    }
                    eintraegeVector = uebertragenerVector;
                    eintraegeListe.setListData(JEintraegeVector);
                }
            }
        });
        
        
                
        //ActionListener für den Plus Button
        /*Zunächst wird der Text aus dem Textfeld ausgelesen.
        Anschließend wird dieser zunächst als neues Listeneintrag-Objekt
        im dazugehörigen Vektor und dann nur der eingegebene Titel im Vector
        für die JList gespeichert. Anschließend wird diese aktualisiert.*/
        addEintrag.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String eintrag = eintragEingabe.getText();
                eintraegeVector.add(new Listeneintrag(eintrag, ""));
                JEintraegeVector.add(eintrag);
                eintragEingabe.setText("");
                eintraegeListe.setListData(JEintraegeVector);
            }
        });
        
        
        
        
        //Listeneintrag löschen
        /*Der ausgewählte Listeneintrag wird aus beiden Vectoren gelöscht und
        die JList aktualisiert.*/
        deleteEintrag.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                eintraegeVector.remove(eintraegeListe.getSelectedIndex());
                JEintraegeVector.remove(eintraegeListe.getSelectedIndex());
                eintraegeListe.setListData(JEintraegeVector);
            }
        });
        
        
        //ZUSATZINFO TEIL
        
        //Übergeben der Zusatzinformation an das 3. Panel
        /*Zunächst wird der aktuell ausgewählte Listeneintrag mithilfe des indexes ermittelt.
        Anschließend wird dessen Zusatzinformation abgerufen und dem Zusatzinfo-Panel als
        anzuzeigender Text übermittelt.*/
        eintraegeListe.addListSelectionListener(new ListSelectionListener(){
 
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = eintraegeListe.getSelectedIndex();
                
                //Verhinderung einer NullPointerException beim direkten Wechsel von Listen mithilfe dieses Checks
                if(index >= 0){
                    Listeneintrag ausgewaehlterEintrag = (Listeneintrag) eintraegeVector.elementAt(index);
                    String zusatzinfo = ausgewaehlterEintrag.getInformation();
                    zusatzinfoAnzeige.setText(zusatzinfo);
                }
            }
        });
       
       
        //Der Zusatzinfo Text wird bearbeitbar gemacht
        editInfo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                zusatzinfoAnzeige.setEditable(true);
            }
        });
        
        /*Zunächst wird der Zusatzinfo Text wieder uneditierbar gemacht.
        Dann wird der aktuell ausgewählte Eintrag mithilfe des Indexes ermittelt und
        anschließend seine Zusatzinfo aktualisiert indem der Text aus dem Panel 
        übergeben wird.*/
        saveInfo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                zusatzinfoAnzeige.setEditable(false);
                int index = eintraegeListe.getSelectedIndex();
                Listeneintrag aktuellerEintrag = (Listeneintrag) eintraegeVector.elementAt(index);
                aktuellerEintrag.setInformation(zusatzinfoAnzeige.getText());
            }
        });
        
        
        
        
        //Laden des JListListenVectors
        /*Zunächst wird (Für den 1. Gebrauch) gecheckt, ob die Datei mit den Listentiteln existiert.
        Falls nicht, wird diese erstellt. Falls sie existiert, wird diese mithilfe eines BufferedReaders 
        Zeile für Zeile ausgelesen. Dabei wird jede Zeile als einzelnes Element zunächst als String 
        im JListListenVector, welcher als ListModel für das 1. Panel dient, und anschließend als
        neues Listen-Objekt im Listen Vector gespeichert. Letzterer wird nun benötigt um die enthaltenen 
        leeren Listen-Objekte mit den gespeicherten Listeneinträgen zu füllen.*/
        File listentitel = new File("Listen\\Listentitel\\listentitel.txt");
        if(listentitel.exists()){
            try(BufferedReader br = new BufferedReader(new FileReader(listentitel))){
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
        } 
        else{
            listentitel.getParentFile().mkdirs();
            listentitel.createNewFile();
        }

        
        
        //Laden der einzelnen Listeneinträge
        /*Zunächst wird der JListListenVector mithilfe einer for-Schleife durchlaufen.
        Dabei wird mit einem BufferedReader die jeweilige Datei, die den Titel der Liste
        als Dateinamen hat und deren Inhalt die Listeneinträge sind, Zeile für Zeile ausgelesen.
        Hierfür wird zunächst das Aktuelle durchlaufene Listen-Objekt und anschließend dessen 
        Vector ermittelt. Dann wird der Name des Eintrags mithilfe des BufferedReader aus dem
        Inhalt der aktuellen Zeile entnommen und in einer Zwischenvariablen gespeichert.
        Danach wird zunächst die Zusatzinfo dieses Eintrags ermittelt. Diese befindet sich
        in einer seperaten Datei, die den Titel des Eintrags als Dateinamen und den Zusatzinfo
        Text als Datei-Inhalt hat.Zunächst wird geprüft ob diese Datei existiert.
        Sollte dies nicht der Fall sein wird dem Sammelvector für die Einträge dieser Liste
        einfach ein neues Listeneintrag-Objekt mit dem ermittelten Titel und einer leeren
        Zusatzinfo übergeben. Falls sie existiert, wird die Datei ausgelesen.
        Um diese Datei auszulesen wird zunächst ein Scanner initialisiert.
        Anschließend wird der Datei-Inhalt mithilfe des Scanners Zeile für Zeile ausgelesen und durch einen
        StringBuilder am Ende wieder zu einem Gesamttext zusammengesetzt. 
        Zu guter letzt wird ein neues Listeneintrag-Objekt mit den soeben ausgelesenen
        Daten erstellt und dem Sammelvector für die Einträge dieser Liste übergeben.*/
        for(int i=0; i < jListListenVector.size(); i++){
            try(BufferedReader br = new BufferedReader(new FileReader("Listen\\Listen\\" + jListListenVector.elementAt(i) + ".txt"))){
            
            String line;
            
            Liste aktuelleListe = (Liste) listenVector.elementAt(i);
            Vector aktuellerVector = aktuelleListe.getVector();
            
            try {
                while((line = br.readLine()) != null){
                    
                    String neuerTitel = line;
                    File infoDatei = new File("Listen\\Zusatzinfos\\" + aktuelleListe.getTitle() + "\\" + neuerTitel + ".txt");
                    
                    if(infoDatei.exists()){
                        Scanner sc = new Scanner(infoDatei);
                        StringBuilder sb = new StringBuilder();
                        
                        while(sc.hasNextLine()){
                            sb.append(sc.nextLine());
                            sb.append("\n");
                        
                        }
                        String zusatzinfo = sb.toString();
                        
                        aktuellerVector.add(new Listeneintrag(neuerTitel, zusatzinfo)); 
                    }
                    else{
                        
                        aktuellerVector.add(new Listeneintrag(neuerTitel, ""));
                    
                    }
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
        /*Die Listentitel werden alle zusammen in einer festgelegten Datei gespeichert.
        Dazu wird mithilfe einer for-schleife der Vector der die Listentitel enthält durchlaufen und
        die Einträge des Vectors einzeln und nacheinander in der Datei eingetragen*/
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                
                //Speichern der Listentitel in einer eigenen Datei
                try {
                    
                    BufferedWriter outputWriter = null;
                    
                    try {
                        outputWriter = new BufferedWriter(new FileWriter("Listen\\Listentitel\\listentitel.txt"));
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
                /*Die for-schleife durchläuft den Vector mit den Listentiteln, der alle eingegebenen Listen
                enthält. Zunächst wird die aktuell durchlaufene Liste, anschließend ihr Inhalt
                mithilfe des dazugehörigen Vectors mit den Listeneinträgen ermittelt.
                Dann wird eine neue Datei mit dem Titel der Liste als Dateinamen erstellt, falls diese noch nicht existiert,
                und mit den Listeneinträgen, welche mithilfe einer weiteren for-schleife, 
                die den zuvor ermittelten Einträge-Vector durchläuft, gefüllt. 
                Anschließend werden die Zusatzinformationen der eintelnen Einträge in jeweils eigenen Dateien,
                die jeweils den Titel des Eintrags als Dateinamen und die Zusatzinfo als Inhalt haben, gespeichert.
                Hierfür wird zunächst gecheckt ob die Datei bereits existiert, ist dies der Fall wird nun die 
                Zusatzinfo des aktuellen Eintrags ermittelt und anschließend in dieser Datei gespeichert. Ist
                dies nicht der Fall wird eine neue Datei erstellt und der eben beschriebene Prozess dann 
                durchgeführt.*/
                for(int i=0; i < jListListenVector.size(); i++){
                    
                    Liste aktuelleListe = (Liste) listenVector.elementAt(i);
                    Vector aktuellerVector = aktuelleListe.getVector();
                    
                    try {
                        
                        BufferedWriter outputWriter = null;
                        
                        try {
                            
                            File liste = new File("Listen\\Listen\\" + aktuelleListe.getTitle() + ".txt");
                            
                            if(liste.exists()){
                                outputWriter = new BufferedWriter(new FileWriter(liste));
                            }
                            else{
                                
                                liste.getParentFile().mkdirs();
                                liste.createNewFile();
                                outputWriter = new BufferedWriter(new FileWriter(liste));
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(ListenProgramm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        for(int j=0; j < aktuellerVector.size(); j++){
                            try {
                                
                                Listeneintrag aktuellerEintrag = (Listeneintrag) aktuellerVector.elementAt(j);
                                outputWriter.write((String) aktuellerEintrag.getTitle());
                                outputWriter.newLine();

                                String aktuelleInfo = aktuellerEintrag.getInformation();
                                try{
                                    
                                    File aktuelleDatei = new File("Listen\\Zusatzinfos\\" + aktuelleListe.getTitle() + "\\" + (String) aktuellerEintrag.getTitle() + ".txt");
                                    
                                    if(aktuelleDatei.exists()){
                                        
                                        BufferedWriter infoWriter = new BufferedWriter(new FileWriter(aktuelleDatei));
                                        infoWriter.write(aktuelleInfo);
                                        
                                        infoWriter.flush();
                                        infoWriter.close();
                                    }
                                    else{
                                        aktuelleDatei.getParentFile().mkdirs();
                                        aktuelleDatei.createNewFile();
                                        
                                        BufferedWriter infoWriter = new BufferedWriter(new FileWriter(aktuelleDatei));
                                        infoWriter.write(aktuelleInfo);
                                        
                                        infoWriter.flush();
                                        infoWriter.close();
                                    }
                                } catch (IOException ex) {
                                    Logger.getLogger(ListenProgramm.class.getName()).log(Level.SEVERE, null, ex);
                                }



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
        jPanel5 = new javax.swing.JPanel();
        saveInfo = new javax.swing.JButton();
        editInfo = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        zusatzinfoAnzeige = new javax.swing.JTextPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(940, 475));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Listen"));
        jPanel1.setPreferredSize(new java.awt.Dimension(270, 454));

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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(listenEingabe, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Listeneinträge"));
        jPanel2.setPreferredSize(new java.awt.Dimension(270, 203));

        addEintrag.setText("Hinzufügen");

        deleteEintrag.setText("Löschen");
        deleteEintrag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteEintragActionPerformed(evt);
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
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(eintragEingabe, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addEintrag))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(deleteEintrag)))
                .addContainerGap())
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
                .addComponent(jScrollPane2))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Zusatzinfo über den Listeneintrag"));

        saveInfo.setText("Zusatzinfo speichern");

        editInfo.setText("Zusatzinfo bearbeiten");
        editInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editInfoActionPerformed(evt);
            }
        });

        jScrollPane6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        zusatzinfoAnzeige.setEditable(false);
        zusatzinfoAnzeige.setBorder(null);
        zusatzinfoAnzeige.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jScrollPane6.setViewportView(zusatzinfoAnzeige);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(editInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(saveInfo)
                .addContainerGap())
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {editInfo, saveInfo});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editInfo)
                    .addComponent(saveInfo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6))
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
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void deleteListeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteListeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteListeActionPerformed

    private void deleteEintragActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteEintragActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteEintragActionPerformed

    private void editInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editInfoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editInfoActionPerformed

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
            java.util.logging.Logger.getLogger(ListenUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListenUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListenUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListenUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ListenUI().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(ListenUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addEintrag;
    private javax.swing.JButton addListe;
    private javax.swing.JButton deleteEintrag;
    private javax.swing.JButton deleteListe;
    private javax.swing.JButton editInfo;
    private javax.swing.JList<String> eintraegeListe;
    private javax.swing.JTextField eintragEingabe;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JTextField listenEingabe;
    private javax.swing.JList<String> listenListe;
    private javax.swing.JButton saveInfo;
    private javax.swing.JTextPane zusatzinfoAnzeige;
    // End of variables declaration//GEN-END:variables
}
