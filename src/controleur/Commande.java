package controleur;

import java.util.*;
import modele.*;

/**
 * 
 */
public interface Commande {

    /**
     * 
     */
    public void executer(Livraison l);

    /**
     * 
     */
    public void annuler();
    
    ArrayList<String> arguments = new ArrayList<String>();
    

}