package modele;

import java.util.ArrayList;
import java.util.List;

public class Intersection {
	
	
	private int id,x,y;
	private List<Route> troncSortants;
	
	public Intersection(int id, int x, int y ){
		this.id = id;
		this.x = x;
		this.y = y;
		this.troncSortants = new ArrayList<Route>();
	}
	
	public void ajouterRoute(Route route){
		this.troncSortants.add(route);
	}
	
	public int getId(){
		return id;
	}
	
	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}
	
}
