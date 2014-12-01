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
   
   public void ajouterLivraison(Livraison l){
	   PlageHoraire pH = l.getPlageHoraire();
	   Boolean b = false;
	   int index = 0;
	   for(int i=0; i<this.plageHoraires.size();i++){
		   if(plageHoraires.get(i).getHeureDebut()==pH.getHeureDebut() && plageHoraires.get(i).getHeureFin()==pH.getHeureFin()){
			   b = true;
			   index = i;
			   i=this.plageHoraires.size();
		   }
	   }
	   if(!b){
		   pH.addLivraison(l);
		   plageHoraires.add(pH);
	   }else{
		   plageHoraires.get(index).addLivraison(l);
	   }
   }
   
   public void supprimerLivraison(Livraison l){
	   PlageHoraire pH = l.getPlageHoraire();
	   pH.deleteLivraison(l);
   }
   
    public void calculerParcours() {
        // TODO implement here
    }

    

}