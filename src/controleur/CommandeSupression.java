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
<<<<<<< HEAD
		
=======
>>>>>>> 0f6cc08e026357a268c43e66c4524753f45470fa
	}

    

}