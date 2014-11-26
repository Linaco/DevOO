package controleur;

import java.util.*;

/**
 * 
 */
public interface Commande {

    /**
     * 
     */
    public void executer();

    /**
     * 
     */
    public void annuler();
    
    ArrayList<String> arguments = new ArrayList<String>();
    

}