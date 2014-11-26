package appcom;

<<<<<<< HEAD
import controleur.Controleur;

public class ServiceModele extends ServiceHandler {
	private Controleur controleur;
	
	public ServiceModele(Controleur controleur, String nomService, ServeurLivraison serveur) {
		super(nomService, serveur);
		this.controleur = controleur;
	}
	
	protected Controleur getControleur(){
		return this.controleur;
	}
=======
public class ServiceModele extends ServiceHandler {

	// Attributs
	
	
	
	// Constructeur
	public ServiceModele(String nomService, ServeurLivraison serveur) {
		super(nomService, serveur);
		// TODO Auto-generated constructor stub
	}
	
	
	// Methodes

>>>>>>> origin/master
}
