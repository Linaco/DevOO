package modele;

import java.util.*;

/**
 * 
 */
public class FeuilleDeRoute {
	
	private List<PlageHoraire>	plageHoraires;

   
	 private ArrayList<Etape> itineraire;
	 private GrapheLivraison grapheLivraison; //a initialiser
	 private List<Livraison> listeLivraison;
	
    public FeuilleDeRoute() {
    	this.plageHoraires= new ArrayList<PlageHoraire>();
    	itineraire = new ArrayList<Etape>();
    	listeLivraison = grapheLivraison.livraisons;
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
        listeLivraison.add(l.getId(),l);
    }

    public void supprimerLivraison(int idLivraison) {

        //Joindre les deux morceaux precedents
        listeLivraison.add(idLivraison,null);
    }
    

}