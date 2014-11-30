package modele;

import java.util.*;

/**
 * 
 */
public class FeuilleDeRoute {
	
	private List<PlageHoraire>	plageHoraires;

    /**
     * 
     */
    public FeuilleDeRoute() {
    	this.plageHoraires= new ArrayList<PlageHoraire>();
    	
    }
    
    //getters
    public List<PlageHoraire> getPlagesHoraires(){return this.plageHoraires;}
    
    
    public void ajouterPlageHoraire(PlageHoraire ph){
    	this.plageHoraires.add(ph);
    }
    
    public void calculerParcours() {
        // TODO implement here
    }

    public void ajouterLivraison(Livraison l) {
        // TODO implement here
    }

    public void supprimerLivraison(int idLivraison) {
        // TODO implement here
    }
    
    //private ArrayList<Etape> itineraire = new ArrayList<Etape>();

}