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
<<<<<<< HEAD
		this.fdr.ajouterLivraison(l,lP,gR);
=======
		//this.fdr.ajouterLivraison(l);
>>>>>>> 0f6cc08e026357a268c43e66c4524753f45470fa
	}

}