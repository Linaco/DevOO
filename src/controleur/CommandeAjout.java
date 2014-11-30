package controleur;

import java.util.*;
import modele.*;

/**
 * 
 */
public class CommandeAjout implements Commande {

	private FeuilleDeRoute fdr;
	
    public CommandeAjout(FeuilleDeRoute feuilleDeRoute) {
    	fdr = feuilleDeRoute;
    }
    

    @Override
    public void annuler() {
        // TODO implement here
    }

	@Override
	public void executer(Livraison l) {
		fdr.ajouterLivraison(l);
		
	}

}