package appcom;

public class ServiceControleur extends ServiceHandler {

	protected Object controleur;
	
	public ServiceControleur(Object controleur, String nomService, ServeurLivraison serveur) {
		super(nomService, serveur);
		this.controleur = controleur;
	}

}
