package modele;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class PlageHoraire {
	
	protected static int idPhs = 0;
	private int id;
	private Date heureDebut;
	private Date heureFin;
	private List<Livraison> listeLivraisons;
	
	public PlageHoraire(Date heureDeb, Date heureFin){
		this.id = idPhs++;
		this.listeLivraisons = new ArrayList<Livraison>();
		this.heureDebut = heureDeb;
		this.heureFin = heureFin;
				
	}
	
	//Getters
	public Date getHeureDebut(){return this.heureDebut;}
	public Date getHeureFin(){return this.heureFin;}
	public List<Livraison> getListeLivraison(){return this.listeLivraisons;}
	public int getIdPlageHoraire(){return this.id;}
	
	public void addLivraison(Livraison liv){
		this.listeLivraisons.add(liv);
                liv.setPlageHoraire(this);
	}
	
	public void deleteLivraison(Livraison liv){
		this.listeLivraisons.remove(liv);
	}

	public boolean display(PrintStream stream) {
		stream.print(heureDebut+ " "+ heureFin);
		for(int i = 0; i<listeLivraisons.size(); i++){
    		listeLivraisons.get(i).display(stream);
                }
		return true;
	}

    @Override
    public boolean equals(Object o){
    	PlageHoraire p = (PlageHoraire)o;    
    	return(this.heureDebut.equals(p.heureDebut)&&this.heureFin.equals(p.heureFin));    
    }
    
    @SuppressWarnings("deprecation")
    public String toStringXML() {
    	String res = "";
    	
    	String minutes = String.format("%02d", this.heureDebut.getMinutes());
		String heureDebut = this.heureDebut.getHours() + "h" + minutes ;
		
		minutes = String.format("%02d", this.heureFin.getMinutes());
		String heureFin = this.heureFin.getHours() + "h" + minutes ;
    	
    	res += "<plage debut=\"" + heureDebut + "\" fin=\"" + heureFin + "\" >";
    	Iterator<Livraison> it = this.listeLivraisons.iterator();
    	while( it.hasNext() ){
			res += it.next().toStringXML();
		}
    	res += "</plage>";
    	return res;
    }
    
    public String getPlageXML() {
    	String res = "";
    	
    	String minutes = String.format("%02d", this.heureDebut.getMinutes());
		String heureDebut = this.heureDebut.getHours() + "h" + minutes ;
		
		minutes = String.format("%02d", this.heureFin.getMinutes());
		String heureFin = this.heureFin.getHours() + "h" + minutes ;
    	
    	res += "<plage id=\"" + this.getIdPlageHoraire() + "\" debut=\"" + heureDebut + 
    			"\" fin=\"" + heureFin + "\" />";
    	return res;
    }
        
}
