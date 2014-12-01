package modele;

import java.util.*;

/**
 * 
 */
public class FeuilleDeRoute {
	
	private Intersection entrepot;
	private List<PlageHoraire> plagesHoraires;

   
	private ArrayList<Etape> itineraire;
	
    public FeuilleDeRoute() {
    	this.plagesHoraires= new ArrayList<PlageHoraire>();
    	itineraire = new ArrayList<Etape>();
    }
    
    //getters
    public List<PlageHoraire> getPlagesHoraires(){return this.plagesHoraires;}
    public Intersection getEntrepot(){return this.entrepot;}
    
    
    public void ajouterPlageHoraire(PlageHoraire ph){
    	this.plagesHoraires.add(ph);
    }
    
    public void renseignerEntrepot(Intersection entrepot){
    	this.entrepot = entrepot;
    } 
   
   public PlageHoraire rechercherPHParHD(Date hDeb){
	   for(int i=0;i<plagesHoraires.size();i++){
		   if(hDeb.equals(plagesHoraires.get(i).getHeureDebut())){
			   return plagesHoraires.get(i);
		   }
	   }
	   return null;
   }
<<<<<<< HEAD
   
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
=======
    public void calculerParcours(GrapheRoutier carte) {
        List<Livraison> livraisonsPlageCourante;
        List<Livraison> livraisonsPlageSuivante;
        List<Livraison> toutesLivraisons = new ArrayList<>();
        Livraison departCourant;
        Livraison arriveeCourante;
        Object[] cheminCourant;
        
        for(PlageHoraire p: plagesHoraires){
            toutesLivraisons.addAll(p.getListeLivraison());
        }
        
        int[][] matriceAdjacence = new int[toutesLivraisons.size()+1][toutesLivraisons.size()+1];
        for(int i=0; i<toutesLivraisons.size()+1;i++){
            for(int j=0; j<toutesLivraisons.size()+1;j++){
                matriceAdjacence[i][j]=-1;
            }
        }
        
        for(int i=0; i< plagesHoraires.size();i++){
            livraisonsPlageCourante = plagesHoraires.get(i).getListeLivraison();
            for(int j=0; j<livraisonsPlageCourante.size();j++){
                for(int k=j+1;k<livraisonsPlageCourante.size();k++){
                    departCourant = livraisonsPlageCourante.get(j);
                    arriveeCourante = livraisonsPlageCourante.get(k);
                    cheminCourant=carte.calculerPlusCourtChemin(departCourant.getPointLivraison(), arriveeCourante.getPointLivraison());
                    if(cheminCourant!=null) {
                        matriceAdjacence[toutesLivraisons.indexOf(departCourant)+1][toutesLivraisons.indexOf(arriveeCourante)+1]=(int)Math.round((Double)cheminCourant[1]);
                    }
                    cheminCourant=carte.calculerPlusCourtChemin(arriveeCourante.getPointLivraison(), departCourant.getPointLivraison());
                    if(cheminCourant!=null) {
                        matriceAdjacence[toutesLivraisons.indexOf(arriveeCourante)+1][toutesLivraisons.indexOf(departCourant)+1]=(int)Math.round((Double)cheminCourant[1]);
                    }
                }
            }
            
            if(i==0){
                for(Livraison l : livraisonsPlageCourante){
                    cheminCourant=carte.calculerPlusCourtChemin(entrepot,l.getPointLivraison());
                    if(cheminCourant!=null) {
                        matriceAdjacence[0][toutesLivraisons.indexOf(l)+1]=(int)Math.round((Double)cheminCourant[1]);
                    }
                }    
            }
            
            if(i==plagesHoraires.size()-1){
                for(Livraison l : livraisonsPlageCourante){
                    cheminCourant=carte.calculerPlusCourtChemin(l.getPointLivraison(),entrepot);
                    if(cheminCourant!=null) {
                        matriceAdjacence[toutesLivraisons.indexOf(l)+1][0]=(int)Math.round((Double)cheminCourant[1]);
                    }                   
                }
            }
            else{
                livraisonsPlageSuivante = plagesHoraires.get(i+1).getListeLivraison();
                for(Livraison lCourante:livraisonsPlageCourante){
                    for(Livraison lSuivante:livraisonsPlageSuivante){
                        cheminCourant=carte.calculerPlusCourtChemin(lCourante.getPointLivraison(),lSuivante.getPointLivraison());
                        if(cheminCourant!=null) {
                            matriceAdjacence[toutesLivraisons.indexOf(lCourante)+1][toutesLivraisons.indexOf(lSuivante)+1]=(int)Math.round((Double)cheminCourant[1]);
                        }     
                    }
                }
            }
        }
        GrapheLivraison solveur = new GrapheLivraison(matriceAdjacence, toutesLivraisons);
        List<Livraison> test = solveur.calculerOrdreLivraisons();
        for(Livraison l : test){
            System.out.println(l.getIdInPH());
        }
>>>>>>> 548ba3307d77b535e316f6382164eec13b93439c
    }

    

}