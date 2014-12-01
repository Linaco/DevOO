package controleur;

import java.util.*;

import modele.Livraison;

/**
 * 
 */
public class CommandeSupression implements Commande {

    
    public CommandeSupression(Livraison livraison) {
    	this.livraison = livraison;
    }

    
    private Livraison livraison;

    
    @Override
    public void annuler() {
        // TODO implement here
    }

	@Override
	public void executer(Livraison l) {
		// TODO Auto-generated method stub
		
	}

    

}