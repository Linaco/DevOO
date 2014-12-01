package modele;

import java.io.PrintStream;
import java.util.*;

/**
 * 
 */
public class Etape {

    /**
     * 
     */
    public Etape() {
    }

    /**
     * 
     */
    private double heurePassagePrevue;

    /**
     * @return
     */
    public double getHeurePassagePrevue() {
        // TODO implement here
        return 0.0d;
    }

    /**
     * @param double
     */
    public void setHeurePassagePrevue(double heure) {
        // TODO implement here
    }
    
    public boolean display(PrintStream stream){
    	stream.println(heurePassagePrevue);
    	return true;
    }

}