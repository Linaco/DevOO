package modele;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Intersection implements DisplayTest{
	
	
	private int id,x,y;
	private List<Route> troncSortants;
	
	/**
	 * Constructeur d'intersection, initialisation de l'ArrayList
	 * @param id
	 * @param x
	 * @param y
	 */
	public Intersection(int id, int x, int y ){
		this.id = id;
		this.x = x;
		this.y = y;
		this.troncSortants = new ArrayList<Route>();
	}
	
	//Getters
	/**
	 * Getter de la liste de route sortant de l'intersection
	 * @return List<Route> troncSortants
	 */
	public List<Route> getTroncsSortants(){return this.troncSortants;}
	
	/**
	 * Getter de l'id
	 * @return id
	 */
	public int getId(){return id;}
	
	/**
	 * getter de la position x
	 * @return x
	 */
	public int getX(){return x;}

	/**
	 * getter de la position y
	 * @return y
	 */
	public int getY(){return y;}
	
	
	/**
	 * Ajout de la route pass� en parametre a troncsSortants
	 * @param route
	 */
	public void addTroncSortant(Route route){
		this.troncSortants.add(route);
	}

	public boolean display(PrintStream stream) {
		stream.println("Intersection : "+id+" "+"x:"+x+" y:"+y);
		stream.println("Troncs sortants :");
		for(int i = 0; i<troncSortants.size(); i++){
			stream.print("\t");
    		troncSortants.get(i).display(stream);
    	}
		return true;
	}
	
	public String toString(){
		return toStringXML();
	}
	
	public String toStringXML(){
		String res = "";
		res += "<intersection id=\""+this.id+"\" x=\""+this.x+"\" y=\""+this.y+"\" >";
		Iterator<Route> it = this.troncSortants.iterator();
		while( it.hasNext() ){
			res += it.next().toStringXML();
		}
		res += "</intersection>";
		return res;
	}
}
