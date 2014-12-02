package controleur;

import java.util.*;
import modele.*;

/**
 * 
 */
public class CommandeAjout implements Commande {

	private FeuilleDeRoute fdr;
	private GrapheRoutier gR;
	private Livraison l;
	private Livraison lP;
	
    public CommandeAjout(Livraison nouvelleLivraison, Livraison livraisonPrecedente, FeuilleDeRoute feuilleDeRoute, GrapheRoutier grapheRoutier) {
    	fdr = feuilleDeRoute;
    	gR = grapheRoutier;
    	l = nouvelleLivraison;
    	lP = livraisonPrecedente;
    }
    

    @Override
    public void annuler() {
        // TODO implement here
    }

	@Override
	public void executer() {
		this.fdr.ajouterLivraison(l,lP,gR);
	}

}