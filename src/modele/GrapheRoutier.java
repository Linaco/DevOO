package modele;

import java.util.*;

/**
 * 
 */
public class GrapheRoutier {

	private List<Intersection> listeIntersection;
    private List<Route> listeRoute;
    
    public GrapheRoutier() {
    	this.listeIntersection = new ArrayList<Intersection>();
    }
    
    public Intersection rechercherInterParId(int id){
    	return listeIntersection.get(id);
    }
    
    public void CalculerPlusCourtChemin() {
        // TODO implement here
    }
    
    public void AjouterIntersection(Intersection intersection) {
    	listeIntersection.add(intersection.getId(), intersection);
    }
    
    public void AjouterRoute(Route route) {
    	listeRoute.add(route.getId(),route);
    }
    
    
}