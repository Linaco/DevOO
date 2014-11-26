package modele;

import java.util.function.IntUnaryOperator;

public class Route {
	
	private static int idRoutes =0;
	
	private String name;
	private double vitesse,longueur;
	private int id;
	private Intersection inter1;
	
	public Route(String nom, double vitesse, double longueur, Intersection inter1){
		this.name = nom;
		this.vitesse = vitesse;
		this.longueur = longueur;
		this.id = idRoutes++;
		this.inter1 = inter1;
	}
	
	public int getId(){
		return id;
	}
}
