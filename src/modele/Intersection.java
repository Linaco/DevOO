package modele;


import java.util.*;

public class Intersection {
	
	
	private int id,x,y;
	private List<Route> troncSortants;
	
	public Intersection(int id, int x, int y ){
		this.id = id;
		this.x = x;
		this.y = y;
		this.troncSortants = new ArrayList<Route>();

	}
	

	public int getId(){
		return id;
	}
	
	public int getX(){
		return x;
	}


	public void ajouterRoute(Route route) {
		troncSortants.add(route);
	}

	public int getY(){
		return y;
	}
	
}
