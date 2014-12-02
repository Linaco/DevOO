package modele;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import modele.*;

/**
 * 
 */
public class FeuilleDeRoute {
	
	private Intersection entrepot;
<<<<<<< HEAD
	private List<PlageHoraire> plagesHoraires;   
=======
	private List<PlageHoraire> plagesHoraires;
        private Date debutJournee;
        private int tempsMoyenLivraisonSecondes;
   
>>>>>>> 3df05dbdc84da2f0f1ecbc5318902d8a93a61b22
	 private ArrayList<Etape> itineraire;
	
    public FeuilleDeRoute() {
    	this.plagesHoraires= new ArrayList<>();
    	itineraire = new ArrayList<>();
	DateFormat formatHeure = new SimpleDateFormat("HH:mm:ss");
        try{
            debutJournee = formatHeure.parse("08:00:00");
            tempsMoyenLivraisonSecondes = 10*60;
        }
        catch(Exception e){}
    }
    
    public void clean(){
    	this.itineraire.clear();
    	this.plagesHoraires.clear();
    }
    
    //getters
    public List<PlageHoraire> getPlagesHoraires(){return this.plagesHoraires;}
    public Intersection getEntrepot(){return this.entrepot;}

    public ArrayList<Etape> getItineraire() {
        return itineraire;
    }
    
    
    public boolean display(PrintStream stream){
    	entrepot.display(stream);
    	for(int i = 0 ; i < plagesHoraires.size() ; i++ ){
    		plagesHoraires.get(i).display(stream);
    	}
    	return true;
    }
    
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
   
   public void ajouterLivraison(Livraison l){
	   PlageHoraire pH = l.getPlageHoraire();
	   Boolean b = false;
	   int index = 0;
	   for(int i=0; i<this.plagesHoraires.size();i++){
		   if(plagesHoraires.get(i).getHeureDebut()==pH.getHeureDebut() && plagesHoraires.get(i).getHeureFin()==pH.getHeureFin()){
			   b = true;
			   index = i;
			   i=this.plagesHoraires.size();
		   }
	   }
	   if(!b){
		   pH.addLivraison(l);
		   plagesHoraires.add(pH);
	   }else{
		   plagesHoraires.get(index).addLivraison(l);
	   }
   }
   
   public void supprimerLivraison(Livraison l){
	   PlageHoraire pH = l.getPlageHoraire();
	   pH.deleteLivraison(l);
   }
   
   /**
    * Calcul le parcours pour les livraisons demandées et crée l'itinéraire
    * @param carte Le Graphe Routier à utiliser pour calculer le parcours
    */
   public void calculerParcours(GrapheRoutier carte) {
        List<Livraison> livraisonsPlageCourante;
        List<Livraison> livraisonsPlageSuivante;
        List<Livraison> toutesLivraisons = new ArrayList<>();
        Livraison departCourant;
        Livraison arriveeCourante;
        Object[] cheminCourant;
        List<Intersection> morceauItineraireCourant;
        

        for(PlageHoraire p: plagesHoraires){
            toutesLivraisons.addAll(p.getListeLivraison());
        }
        //Calcul de tous les plus courts chemins adéquats
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
        List<Livraison> livraisonsOrdonnees = solveur.calculerOrdreLivraisons();
        
        //Mise à jour de l'itinéraire
        Date heureCourante = debutJournee;
        Etape etapeCourante = new Etape(heureCourante, entrepot);
        Livraison livraisonCourante;
        int attenteCourante = 0;
        DateFormat formatHeure = new SimpleDateFormat("HH:mm:ss");
        itineraire.clear();
        itineraire.add(etapeCourante);
        cheminCourant=carte.calculerPlusCourtChemin(entrepot,livraisonsOrdonnees.get(0).getPointLivraison());
        morceauItineraireCourant = (List) cheminCourant[0];
        for(int i=0; i<morceauItineraireCourant.size()-1;i++){
            heureCourante=new Date(heureCourante.getTime()+(int)Math.round(carte.getRoute(morceauItineraireCourant.get(i),morceauItineraireCourant.get(i+1)).getTempsParcours()*1000));
            etapeCourante = new Etape(heureCourante,morceauItineraireCourant.get(i+1));
            itineraire.add(etapeCourante);
        }
        itineraire.remove(itineraire.size()-1);
        
        for(int i=0; i<livraisonsOrdonnees.size();i++){
            livraisonCourante = livraisonsOrdonnees.get(i);
            if(livraisonCourante.getPlageHoraire().getHeureDebut().after(heureCourante)){
                attenteCourante=(int) (livraisonCourante.getPlageHoraire().getHeureDebut().getTime()-heureCourante.getTime());
                heureCourante = livraisonCourante.getPlageHoraire().getHeureDebut();
            }
            else{      
                attenteCourante=0;
            }
            etapeCourante = new Etape(heureCourante,livraisonCourante.getPointLivraison());
            etapeCourante.setAttenteAvantPassage(attenteCourante);
            livraisonCourante.setEtapePassagePrevue(etapeCourante);
            itineraire.add(etapeCourante);
            heureCourante=new Date(heureCourante.getTime()+tempsMoyenLivraisonSecondes*1000);
                    
            if(i<livraisonsOrdonnees.size()-1) {
                cheminCourant=carte.calculerPlusCourtChemin(livraisonsOrdonnees.get(i).getPointLivraison(),livraisonsOrdonnees.get(i+1).getPointLivraison());
            }
            else{
                cheminCourant=carte.calculerPlusCourtChemin(livraisonsOrdonnees.get(i).getPointLivraison(), entrepot);
            }
            morceauItineraireCourant = (List) cheminCourant[0];
            for(int j=0; j<morceauItineraireCourant.size()-1;j++){
                heureCourante=new Date(heureCourante.getTime()+(int)Math.round(carte.getRoute(morceauItineraireCourant.get(j),morceauItineraireCourant.get(j+1)).getTempsParcours()*1000));
                etapeCourante = new Etape(heureCourante,morceauItineraireCourant.get(j+1));
                itineraire.add(etapeCourante);
            }
            if(i<livraisonsOrdonnees.size()-1) {
                itineraire.remove(itineraire.size()-1);
            }
        }
   }
}