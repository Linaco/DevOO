package modele;

public class Route {

	private static int idRoutes =0;
	
	private String name;
	private double vitesse,longueur;
	private int id;
	private Intersection inter;
	
	public Route(String nom, double vitesse, double longueur, Intersection inter){
		this.name = nom;
		this.vitesse = vitesse;
		this.longueur = longueur;
		this.id = idRoutes++;
		this.inter = inter;
	}
	
	public int getId(){
		return id;
	}
}
