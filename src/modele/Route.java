package modele;


public class Route {
	
	private static int idRoutes =0;
	
	private String name;
	private double vitesse,longueur;
	private int id;
	private Intersection inter;
	
	public Route(String nom, double vitesse, double longueur, Intersection inter1){
		this.name = nom;
		this.vitesse = vitesse;
		this.longueur = longueur;
		this.id = idRoutes++;
		this.inter = inter1;
	}
	
	
	//Getter
	public int getId(){return id;}
	
	public String getName(){return this.name;}
	
	public double getVitesse(){return this.vitesse;}
	
	public double getLongueur(){return this.longueur;}

        
    public double getTempsParcours(){
            return longueur/vitesse;
    }
	
	public Intersection getInter(){return this.inter;}
	
}
