package modele;

import java.io.PrintStream;
import java.util.*;

/**
 * 
 */
public class Etape implements DisplayTest{
	
	private Date heureDePassage;
	private Intersection adresse;

    /**
     * 
     */
    public Etape() {
    }
    
    public Etape(Date heurePassage, Intersection adresse){
    	this.adresse = adresse;
    	this.heureDePassage = heurePassage;
    }

    /**
     * 
     */
    

    /**
     * @return
     */
    public Date getHeurePassagePrevue() {
        // TODO implement here
        return this.heureDePassage;
    }

    public Intersection getAdresse(){return this.adresse;}
    /**
     * @param double
     */
    public void setHeurePassagePrevue(Date nouvelleHeure) {
    	this.heureDePassage = nouvelleHeure;
        // TODO implement here
    }
    
    public boolean display(PrintStream stream){
    	stream.println(heureDePassage.toString());
    	return true;
    }

}