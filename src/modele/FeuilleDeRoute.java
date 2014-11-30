package modele;

import java.util.*;

/**
 * 
 */
public class FeuilleDeRoute {

   
	 private ArrayList<Etape> itineraire;
	 private GrapheLivraison grapheLivraison; //a initialiser
	 private List<Livraison> listeLivraison;
	
    public FeuilleDeRoute() {
    	itineraire = new ArrayList<Etape>();
    	listeLivraison = grapheLivraison.livraisons;
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