package modele;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 */
public class Etape implements DisplayTest{
	
	private Date heureDePassage;
        private int secondesAttenteAvantPassage;
	private Intersection adresse;

    /**
     * 
     */
    public Etape() {
    }
    
    public Etape(Date heurePassage, Intersection adresse){
    	this.adresse = adresse;
    	this.heureDePassage = heurePassage;
	DateFormat formatHeure = new SimpleDateFormat("HH:mm:ss");        
        secondesAttenteAvantPassage=0;
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

    public int getAttenteAvantPassage() {
        return secondesAttenteAvantPassage;
    }

    public void setAttenteAvantPassage(int attenteAvantPassage) {
        this.secondesAttenteAvantPassage = attenteAvantPassage;
    }
    
    public boolean display(PrintStream stream){
    	stream.println(heureDePassage.toString());
    	return true;
    }

}