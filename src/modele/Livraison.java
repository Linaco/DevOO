package modele;

import java.util.*;

/**
 * 
 */
public class Livraison {
   
	private double heureDePassageEffective;
    //private pair<double, double> plageHoraireDemandee;
    private Intersection PointDeLivraison;
	private String heureDebut,heureFin;
	private int idXML,idClient,adresse;
	private static int id = 0;
	
    public Livraison(Intersection inter, String heureDeb, String heureF, int idXML, int idClient, int adresse) {
    	PointDeLivraison = inter;
    	heureDebut = heureDeb;
    	heureFin = heureF;
    	this.idXML = idXML;
    	this.idClient = idClient;
    	this.adresse = adresse;
    	this.id = id++;
    }
    
    /*public pair<double, double> getPlageHoraire() {
        // TODO implement here
        return null;
    }*/
   
    public boolean isRealisable() {
        // TODO implement here
        return false;
    }
    
    public void setHeurePassage(double heure) {
        // TODO implement here
    }
    
    public int getId() {
    	return id;
    }
    
    public void setHeureDePassageEffective(int heure) {
    	heureDePassageEffective = heure;
    }

}