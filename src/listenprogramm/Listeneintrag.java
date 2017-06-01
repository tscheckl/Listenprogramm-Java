/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listenprogramm;

/**
 *
 * @author Philipp
 */
public class Listeneintrag {
    
    String title;
    String information;
    
    public Listeneintrag(String title, String information){
        this.title = title;
        this.information = information;
    }
    
    public String getTitle(){
        return title;
    }
    
    public String getInformation(){
        return information;
    }
    
    public String setTitle(String newTitle){
        title = newTitle;
        return title;
    }
    
    public String setInformation(String newInformation){
        information = newInformation;
        return information;
    }
}
