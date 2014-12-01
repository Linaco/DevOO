package modele;

import java.util.*;

/**
 * 
 */
public class GrapheRoutier {
	
	private List<Intersection> listeIntersection;

    /**
     * 
     */
    public GrapheRoutier() {
    	this.listeIntersection = new ArrayList<Intersection>();
    }
    
    public void ajouterIntersection(Intersection inter){
    	this.listeIntersection.add(inter.getId(),inter);
    }
    
    public boolean interExiste(int idInter){
    	for(int i = 0; i<listeIntersection.size(); i++){
    		if(idInter == listeIntersection.get(i).getId()){
    			return true;
    		}
    	}
    	return false;
    }
    
    public Intersection rechercherInterParId(int id){
    	return listeIntersection.get(id);
    }
    
   



    /**
     * 
     */
    public void CalculerPlusCourtChemin() {
        // TODO implement here

        Noeud noeudCourant = new Noeud(depart,null,0);
        Noeud successeurCourant;
        PriorityQueue<Noeud> frontiere = new PriorityQueue<>();
        frontiere.add(noeudCourant);
        List<Noeud> dejaExplores = new ArrayList<>();
        List<Intersection> solution = new ArrayList<>();
        
        for(;;){
            if(frontiere.isEmpty()) {
                return null;
            }
            noeudCourant= frontiere.poll();
            if(noeudCourant.intersection.equals(arrivee)){
                while(!noeudCourant.intersection.equals(depart)){
                    solution.add(noeudCourant.intersection);
                    noeudCourant=noeudCourant.origine;
                }
                solution.add(noeudCourant.intersection);
                Collections.reverse(solution);
                return solution;
            }
            dejaExplores.add(noeudCourant);
            for(Route r : noeudCourant.intersection.getTroncsSortants()){
                successeurCourant = new Noeud(r.getInter(),noeudCourant,noeudCourant.coutAcces+r.getTempsParcours());
                if(!dejaExplores.contains(successeurCourant)){
                    if(!frontiere.contains(successeurCourant)){
                        frontiere.add(successeurCourant);
                    }
                    else{
                        for(Noeud n:frontiere){
                            if (n.equals(successeurCourant) && n.coutAcces>successeurCourant.coutAcces){
                                frontiere.remove(n);
                                frontiere.add(successeurCourant);
                            }
                        }
                    }
                }
            }
        }
        
    }

}