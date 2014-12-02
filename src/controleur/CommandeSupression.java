package controleur;

import java.util.*;
import modele.*;

/**
 * 
 */
public class CommandeSupression implements Commande {

	private FeuilleDeRoute fdr;
    
    public CommandeSupression(FeuilleDeRoute feuilleDeRoute) {
    	this.fdr = feuilleDeRoute;
    }

    
    @Override
    public void annuler() {
        // TODO implement here
    }

	@Override
	public void executer() {
		//this.fdr.supprimerLivraison(l);
	}

    

}