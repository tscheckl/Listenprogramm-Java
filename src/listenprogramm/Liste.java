/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listenprogramm;

import java.util.Vector;

/**
 *
 * @author Philipp
 */
public class Liste {
    
    String title;
    Vector eintraege;
    
    public Liste(String title, Vector eintraege){
        this.title = title;
        this.eintraege = eintraege;
    }
    
    public void hinzufuegen(Listeneintrag eintrag){
        eintraege.add(eintrag);
    }
    
    public String getTitle(){
        return title;
    }
    
    public String setTitle(String newTitle){
        title = newTitle;
        return title;
    }
    
    public Vector getVector(){
        return eintraege;
    }
}
