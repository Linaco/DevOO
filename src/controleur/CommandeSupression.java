package controleur;

import java.util.*;

import modele.*;

/**
 * 
 */
public class CommandeSupression implements Commande {

	private FeuilleDeRoute feuilleDeRoute;
	private Livraison livraison;
	private GrapheRoutier grapheRoutier;
    
    public CommandeSupression(Livraison l, GrapheRoutier gR, FeuilleDeRoute fdr) {
    	this.feuilleDeRoute = fdr;
    	this.livraison = l;
    	this.grapheRoutier = gR;
    }

    
    @Override
    public void annuler() {
        // TODO implement here
    }

	@Override
	public void executer() {
		this.feuilleDeRoute.supprimerLivraison(this.livraison, this.grapheRoutier);
	}

    

}