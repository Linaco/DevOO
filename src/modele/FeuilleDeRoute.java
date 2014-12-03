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
	
	private GrapheLivraison grapheLivraison;
	private Intersection entrepot;
	private List<PlageHoraire> plagesHoraires;
    private Date debutJournee;
    private int tempsMoyenLivraisonSecondes;
   

    private ArrayList<Etape> itineraire;
	
    public FeuilleDeRoute() {
		grapheLivraison = new GrapheLivraison();
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
    	PlageHoraire.idPhs = 0;
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
    	this.plagesHoraires.add(ph.getIdPlageHoraire(),ph);
    }
    
    public void renseignerEntrepot(Intersection entrepot){
    	this.entrepot = entrepot;
    }
    
    public boolean checkHB(Date heureD){
    	int index = plagesHoraires.size();
    	if(index == 0){
    		return true;
    	}else if(this.plagesHoraires.get(index-1).getHeureFin().after(heureD)){
    		System.err.println("Plages supperpos�es");
    		return false;
    	}else{
    		return true;
    	}
    }
    
    public PlageHoraire rechercherPHParId(int id){
    	return this.plagesHoraires.get(id);
    }
    
   public PlageHoraire rechercherPHParHD(Date hDeb){
	   for(int i=0;i<plagesHoraires.size();i++){
		   if(hDeb.equals(plagesHoraires.get(i).getHeureDebut())){
			   return plagesHoraires.get(i);
		   }
	   }
	   return null;
   }
   /**
    * Fonction permettant de retrouver la position dans itinéraire de la livraison suivante dans la liste des étapes
    * @param livraison
    * @return liste<Integer>
    */
   public List<Integer> trouverSuivant(Livraison livraison){
	   List<Integer> listePosition = new ArrayList<Integer>();
	   Etape etape = livraison.getEtape();
	   int pos = itineraire.indexOf(etape);
	   int nextLivraison = 0;
	   nextLivraison+=pos+1;
	   while(!itineraire.get(nextLivraison).getaLivraison()){
		  nextLivraison++;
	   }
	   listePosition.add(pos);
	   listePosition.add(nextLivraison);
	   return listePosition;
   }
   /**
    * Fonction permettant de retrouver la position dans itinéraire de la livraison précédente dans la liste des étapes
    * @param livraison
    * @return List<Integer>
    */
   public List<Integer> trouverPrecedent(Livraison livraison){
	   List<Integer> listePosition = new ArrayList<Integer>();
	   Etape etape = livraison.getEtape();
	   int pos = itineraire.indexOf(etape);
	   int previousLivraison = 0;
	   previousLivraison += pos-1;
	   while(!itineraire.get(previousLivraison).getaLivraison()){
		   previousLivraison--;
	   }
	   listePosition.add(previousLivraison);
	   listePosition.add(pos);
	   return listePosition;
   }
   
   /**
    * Cette fonction permet de mettre à jour les horaires de passage de chaque étape suivant l'étape selectionnée en paramètres. 
    * @param etape
    * @param grapheRoutier
    */
   public void majHeureDePassage(Etape etape, GrapheRoutier carte){
	   int posEtape = this.getItineraire().indexOf(etape);
	   for(int i=posEtape+1; i<this.getItineraire().size();i++){
		   Object[]resultatCalcul = carte.calculerPlusCourtChemin(this.getItineraire().get(i-1).getAdresse(), this.getItineraire().get(i).getAdresse());
		   List<Intersection> listeIntersection = (List<Intersection>)resultatCalcul[0];
		   Date heureCourante=new Date(etape.getHeurePassagePrevue().getTime()+(int)Math.round(carte.getRoute(listeIntersection.get(i-1),listeIntersection.get(i)).getTempsParcours()*1000));
		   this.getItineraire().get(i).setHeurePassagePrevue(heureCourante);
	   }
   }
   
   /**
    * Fonction permettant d'ajouter une nouvelle livraison à la feuile de route tout en mettant à jour l'itinéraire.
    * @param nouvelleLivraison
    * @param livraisonPrecedente
    */
   public void ajouterLivraison(Livraison nouvelleLivraison, Livraison livraisonPrecedente, GrapheRoutier carte){
	   PlageHoraire pH = nouvelleLivraison.getPlageHoraire();
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
		   pH.addLivraison(nouvelleLivraison);
		   plagesHoraires.add(pH);
	   }else{
		   plagesHoraires.get(index).addLivraison(nouvelleLivraison);
	   }
	   List<Integer> posEtapes = trouverSuivant(livraisonPrecedente);
	   //Retrait des etapes obsolètes
	   for(int i=0; i<posEtapes.get(1)-posEtapes.get(0);i++){
		   itineraire.remove(posEtapes.get(0)+1);
	   }
	   //ajout des nouvelles étapes de livraison précédente-->nouvelle livraison
	   Object[]resultatCalcul = carte.calculerPlusCourtChemin(livraisonPrecedente.getPointLivraison(), nouvelleLivraison.getPointLivraison());
	   List<Intersection> listeIntersection = (List<Intersection>)resultatCalcul[0];
	   List<Etape> nouvellesEtapes = new ArrayList<Etape>();
	   Date heureCourante = itineraire.get(posEtapes.get(0)).getHeurePassagePrevue();
	   for(int i=1; i<listeIntersection.size();i++){
		   heureCourante=new Date(heureCourante.getTime()+(int)Math.round(carte.getRoute(listeIntersection.get(i-1),listeIntersection.get(i)).getTempsParcours()*1000));
		   nouvellesEtapes.add(new Etape(heureCourante,listeIntersection.get(i)));
	   }
	   nouvellesEtapes.get(nouvellesEtapes.size()-1).setaLivraison();
	   nouvelleLivraison.setEtapePassagePrevue(nouvellesEtapes.get(nouvellesEtapes.size()-1));
	   itineraire.addAll(posEtapes.get(0)+1, nouvellesEtapes);
	   int positionNouvelleLivraison = posEtapes.get(0)+nouvellesEtapes.size();
	   //ajout des nouvelles étapes de nouvelle livraison-->livraison suivante
	   resultatCalcul = carte.calculerPlusCourtChemin(nouvelleLivraison.getPointLivraison(), itineraire.get(positionNouvelleLivraison+1).getAdresse());
	   listeIntersection = (List<Intersection>)resultatCalcul[0];
	   nouvellesEtapes.clear();
	   for(int i=1;i<listeIntersection.size()-1;i++){
		   heureCourante=new Date(heureCourante.getTime()+(int)Math.round(carte.getRoute(listeIntersection.get(i-1),listeIntersection.get(i)).getTempsParcours()*1000));
		   nouvellesEtapes.add(new Etape(heureCourante,listeIntersection.get(i)));
	   }
	   itineraire.addAll(positionNouvelleLivraison+1, nouvellesEtapes);
	   this.majHeureDePassage(itineraire.get(positionNouvelleLivraison+nouvellesEtapes.size()), carte);
   }
   
   /**
    * Fonction permettant de supprimer une livraison de la feuille de route tout en mettant à jour l'itinéraire
    * @param livraison
    * @param grapheroutier
    */
   public void supprimerLivraison(Livraison l, GrapheRoutier carte){
	   PlageHoraire pH = l.getPlageHoraire();
	   pH.deleteLivraison(l);
	   int positionPrecedente = trouverPrecedent(l).get(0);
	   int positionSuivante = trouverSuivant(l).get(1);
	   //supression des etapes
	   for(int i=0;i<positionSuivante-positionPrecedente;i++){
		   itineraire.remove(positionPrecedente+1);
	   }
	   //liaison entre précédent et suivant
	   Object[]resultatCalcul = carte.calculerPlusCourtChemin(itineraire.get(positionPrecedente).getAdresse(), itineraire.get(positionSuivante).getAdresse());
	   List<Intersection> listeIntersection = (List<Intersection>)resultatCalcul[0];
	   List<Etape> nouvellesEtapes = new ArrayList<Etape>();
	   Date heureCourante = itineraire.get(positionPrecedente).getHeurePassagePrevue();
	   for (int i=1; i<listeIntersection.size()-1;i++){
		   heureCourante=new Date(heureCourante.getTime()+(int)Math.round(carte.getRoute(listeIntersection.get(i-1),listeIntersection.get(i)).getTempsParcours()*1000));
		   nouvellesEtapes.add(new Etape(heureCourante, listeIntersection.get(i)));
	   }
	   itineraire.addAll(positionSuivante, nouvellesEtapes);
	   this.majHeureDePassage(itineraire.get(positionPrecedente+nouvellesEtapes.size()), carte);
   }
   
   public GrapheLivraison getGrapheLivraison(){
	   return this.grapheLivraison;
   }

   /**
    * Calcul le parcours pour les livraisons demandees et cree l'itineraire
    * @param carte Le Graphe Routier a utiliser pour calculer le parcours
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
        
        //Calcul de tous les plus courts chemins adequats

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
        
        //Mise a jour de l'itineraire
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
   
	public String getItineraireXML(){
		String res = "";
		res += "<feuilleDeRoute>";
		res += "<itineraire>";
		Iterator<Etape> it = this.itineraire.iterator();
		while( it.hasNext() ){
			res += it.next().toStringXML(this.plagesHoraires);
		}
		res +="</itineraire>";
		res += "<livraisonsImpossibles>";
		for(PlageHoraire p : plagesHoraires){
			for(Livraison l : p.getListeLivraison()){
				if(!l.isRealisable()){
					res+=l.toStringXML();
				}
			}
		}
		res += "</livraisonsImpossibles>";
		res += "</feuilleDeRoute>";
		return res;
	}
	
	public String getLivraisonsXML(){
		String res = "";
		res += "<livraisons idEntrepot=\"" + this.entrepot.getId() + "\">";
		Iterator<PlageHoraire> it = this.plagesHoraires.iterator();
		while( it.hasNext() ){
			res += it.next().toStringXML();
		}
		res += "</livraisons>";
		return res;
	}
	
	public String getPlagesHorairesToXML() {
		String res = "<plagesHoraires>";
		Iterator<PlageHoraire> it = this.plagesHoraires.iterator();
		while(it.hasNext()) {
			PlageHoraire pH = it.next();
			res += pH.getPlageXML();
		}
		res += "</plagesHoraires>";
		return res;
	}
}