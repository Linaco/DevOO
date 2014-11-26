package controleur;

import java.util.*;

import modele.Livraison;

/**
 * 
 */
public class CommandeAjout implements Commande {

    
    public CommandeAjout(Livraison livraison) {
    	this.livraison = livraison;
    }
    

    @Override
    public void annuler() {
        // TODO implement here
    }

	@Override
	public void executer() {
		// TODO Auto-generated method stub
		
	}

    private Livraison livraison;

}