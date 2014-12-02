package modele;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 */
public class Livraison {
	
	private static final DateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm:ss");
   
	private static int idLivraison = 0;
	private Date heureDePassageEffective;
    private Etape etapePassagePrevue;
    private Intersection PointDeLivraison;
	private int id;
	private int idInPH,idClient;
	private PlageHoraire plageHoraire;
	
    public Livraison(Intersection inter, int id, int idClient) {
    	this.id = idLivraison++;
    	this.PointDeLivraison = inter;
    	this.idInPH = id;
    	this.idClient = idClient;
    }
    
    //getters
    public int getIdLiv(){return this.id;}
    public int getIdInPH(){return this.idInPH;}
    public Intersection getPointLivraison(){return this.PointDeLivraison;}
    public int getIdClient(){return this.idClient;}
    public PlageHoraire getPlageHoraire(){return this.plageHoraire;}
    public Date getHeureDePassage(){return this.heureDePassageEffective;}
    public Etape getEtape(){return this.etapePassagePrevue;}
    
   
    public boolean isRealisable() {
        return etapePassagePrevue.getHeurePassagePrevue().before(plageHoraire.getHeureFin());
    }
  
    
    public void setHeureDePassageEffective(String heure) {
    	try{
    		this.heureDePassageEffective = HOUR_FORMAT.parse(heure);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public void setPlageHoraire(PlageHoraire ph){
    	this.plageHoraire = ph;
    }
    
    public void setEtapePassagePrevue(Etape etape){
        etapePassagePrevue = etape;
        etape.setaLivraison();
    }
    
    public boolean display(PrintStream stream){
    	PointDeLivraison.display(stream);
    	plageHoraire.display(stream);
    	stream.print(" Client: "+idClient);
    	return true;
    }
    
    @Override
    public boolean equals(Object o){
        return((Livraison)o).id==this.id;
    }
    
    public String toStringXML() {
    	String res = "";
    	res += "<livraison id=\"" + this.id + "\" idClient=\"" + this.idClient + 
    			"\" idIntersection=\"" + this.PointDeLivraison.getId() + "\" />";
    	return res;
    }

}