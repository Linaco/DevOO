package modele;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlageHoraire {
	
	private Date heureDebut;
	private Date heureFin;
	private List<Livraison> listeLivraisons;
	
	public PlageHoraire(Date heureDeb, Date heureFin){
		listeLivraisons = new ArrayList<Livraison>();
		this.heureDebut = heureDeb;
		this.heureFin = heureFin;
				
	}
	
	//Getters
	public Date getHeureDebut(){return this.heureDebut;}
	public Date getHeureFin(){return this.heureFin;}
	public List<Livraison> getListeLivraison(){return this.listeLivraisons;}
	
	public void addLivraison(Livraison liv){
		this.listeLivraisons.add(liv);
	}
        
        @Override
        public boolean equals(Object o){
            PlageHoraire p = (PlageHoraire)o;
            return(this.heureDebut.equals(p.heureDebut)&&this.heureFin.equals(p.heureFin));
        }


}
