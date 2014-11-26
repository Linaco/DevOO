package modele;

import java.util.*;
import modele.GrapheRoutier.*;

/**
 * 
 */
public class GrapheLivraison {

	private List<Livraison> listeLivraison;
	private Intersection entrepot;
	private String heureDebut,heureFin;
	private int id,idClient,adresse;
    
    public GrapheLivraison(Intersection entrepot) {
    	listeLivraison = = new ArrayList<Livraison>();
    	this.entrepot = entrepot;
    }

    public void calculerOrdreLivraisons() {
        // TODO implement here
    }
    
    public void AjouterLivraison(Livraison livraison) {
    	this.listeLivraison.add(livraison.getId(),livraison);
    }
    
    public void SupprimerLivraison(int idLivraison) {
    	this.listeLivraison.add(idLivraison, null);
    }

    
}