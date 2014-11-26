package modele;

import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
    
    public Intersection rechercherInterParId(int id){
    	return listeIntersection.get(id);
    }
    
   


    /**
     * 
     */
    public void CalculerPlusCourtChemin() {
        // TODO implement here
    }

}