package modele;

import java.util.ArrayList;
import java.util.List;

public class Intersection {
	
	
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
	 * Ajout de la route passé en parametre a troncsSortants
	 * @param route
	 */
	public void addTroncSortant(Route route){
		this.troncSortants.add(route);
	}
}
