package modele;

import java.util.*;

/**
 * 
 */
public class FeuilleDeRoute {
	
	private Intersection entrepot;
	private List<PlageHoraire>	plageHoraires;

   
	 private ArrayList<Etape> itineraire;
	
    public FeuilleDeRoute() {
    	this.plageHoraires= new ArrayList<PlageHoraire>();
    	itineraire = new ArrayList<Etape>();
    }
    
    //getters
    public List<PlageHoraire> getPlagesHoraires(){return this.plageHoraires;}
    public Intersection getEntrepot(){return this.entrepot;}
    
    
    public void ajouterPlageHoraire(PlageHoraire ph){
    	this.plageHoraires.add(ph);
    }
    
    public void renseignerEntrepot(Intersection entrepot){
    	this.entrepot = entrepot;
    }
    
   public PlageHoraire rechercherPHParHD(Date hDeb){
	   for(int i=0;i<plageHoraires.size();i++){
		   if(hDeb.equals(plageHoraires.get(i).getHeureDebut())){
			   return plageHoraires.get(i);
		   }
	   }
	   return null;
   }
    public void calculerParcours() {
        // TODO implement here
    }

    

}