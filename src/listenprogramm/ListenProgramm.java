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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Philipp
 */
public class ListenProgramm extends JFrame {
    
    //Gesamtes Programm wird in diesem JSplitPanel laufen
    JSplitPane panel;
    
    //Das linke Panel für die Anzeige der Listen
    JPanel panelLeft;
    
    //Das rechte Panel für die Anzeige des Listeninhalts
    JPanel panelRight;
    
    //INHALTE DES LINKEN PANELS
    //Die eigentliche Liste
    JList listenListe;
    //Der Vector der als Listmodel fungiert
    Vector jListListenVector;
    //der Plus Button
    JButton addListe;
    //der Löschen Button
    JButton deleteListe;
    //Das Eingabe Feld für den gewünschten Namen der Liste
    JTextField listenEingabe;
    
    //Der Vector in dem Die Listen-Objekte gespeichert sind
    Vector listenVector;
    
    
    //INHALTE DES RECHTEN PANELS
    //Die Liste mit den Listeneinträgen
    JList eintraegeListe;
    //der Plus Button
    JButton addEintrag;
    //der Löschen Button
    JButton deleteEintrag;
    //Eingabefld für die Einträge der Liste
    JTextField eintragEingabe;
    
    //Der Vector für die Einträge
    Vector eintraegeVector;
    
    
    public ListenProgramm(){
        this.setSize(450, 300);
        
        
        //den Objekt Vector initialisieren
        listenVector = new Vector();
        
        //Die Panels initialisieren
        panelRight = new JPanel();
        panelLeft = new JPanel();
        
        //Das SplitPane vorbereiten
        panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        panel.setRightComponent(panelRight);
        panel.setLeftComponent(panelLeft);
        
        
        //DER LINKE TEIL
        
        //Die Bedienelemente initialisieren
        addListe = new JButton("+");
        deleteListe = new JButton("Löschen");
        listenEingabe = new JTextField(10);
        jListListenVector = new Vector();
        listenListe = new JList(jListListenVector);
        
        //Die Bedienelemente dem Panel hinzufügen
        panelLeft.add(addListe);
        panelLeft.add(deleteListe);
        panelLeft.add(listenEingabe);
        panelLeft.add(listenListe);
        
        
        
        //Das SplitPane dem Fenster hinzufügen
        this.add(panel);
        
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
        
        
        
        //DER RECHTE TEIL
        
        //Die Bedienelemente initialisieren
        addEintrag = new JButton("+");
        deleteEintrag = new JButton("Löschen");
        eintraegeListe = new JList();
        eintragEingabe = new JTextField(10);
        eintraegeVector = new Vector();
        eintraegeListe.setListData(eintraegeVector);
        
        //Die Bedienelemente hinzufügen
        panelRight.add(addEintrag);
        panelRight.add(deleteEintrag);
        panelRight.add(eintragEingabe);
        panelRight.add(eintraegeListe);
        
        
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
        try(BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Philipp\\Desktop\\a.txt"))){
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
            try(BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Philipp\\Desktop\\" + jListListenVector.elementAt(i) + ".txt"))){
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
        
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                
                try {
                    BufferedWriter outputWriter = null;
                    try {
                        outputWriter = new BufferedWriter(new FileWriter("C:\\Users\\Philipp\\Desktop\\a.txt"));
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
               
                
                
                for(int i=0; i < jListListenVector.size(); i++){
                    Liste aktuelleListe = (Liste) listenVector.elementAt(i);
                    Vector aktuellerVector = aktuelleListe.getVector();
                    try {
                    BufferedWriter outputWriter = null;
                    try {
                        outputWriter = new BufferedWriter(new FileWriter("C:\\Users\\Philipp\\Desktop\\" + aktuelleListe.getTitle() + ".txt"));
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
    
    public static void main(String[] args) {
        ListenProgramm programm = new ListenProgramm();
        
        programm.setVisible(true);
    }
    
}
