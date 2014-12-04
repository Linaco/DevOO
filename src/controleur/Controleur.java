package controleur;
import java.util.List;
import java.util.Stack;

import org.w3c.dom.*;

import modele.*;

public class Controleur {
	
	/**
	 * Attribut de classe DateFormat permettant le formatage de l'heure à partir d'une String
	 */
	
	
	private GrapheRoutier grapheRoutier;
	private FeuilleDeRoute feuilledeRoute;
	private Stack<Commande> listeFaits;
	private Stack<Commande> listeAnnules;
	/**
	 * Constructeur de Controleur
	 * Initialisation du graphe routier et de la feuille de route
	 */
	 public Controleur(){
		grapheRoutier = new GrapheRoutier();
		feuilledeRoute = new FeuilleDeRoute();
	}
	
	
	/**
	 * Remise à zéro de la feuille de route
	 * Appelle à la méthode pour charger le document passé en paramètre
	 * @param livDoc
	 * 		: document à parser
	 * @return boolean
	 * 		 true si la feuille de route est correctement générée //
	 * 		 false si la feuille est incorrecte
	 */
	public boolean chargerLivraisons(Document livDoc){
		
		feuilledeRoute.clean();
		feuilledeRoute = ChargerLivraisons.chargerLivraisons(livDoc, grapheRoutier);
		if(feuilledeRoute.getPlagesHoraires().size() == 0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Remise à zéro du graphe routier
	 * Appelle à la méthode pour charger le document passé en paramètre
	 * @param plan
	 * 		: document à parser
	 * @return boolean
	 * 		 true si le graphe routier est correctement généré //
	 * 		 false si le graphe routier incorrecte
	 */
	public boolean chargerPlan(Document plan){
		grapheRoutier.clean();
		grapheRoutier = ChargerPlan.chargerPlan(plan);
		if(grapheRoutier.getListeIntersections().size() == 0){
			return false;
		}else{
			return true;
		}
	}

	
	
	/**
	 * getter sur grapheRoutier
	 * @return {@link GrapheRoutier} : graphe routier du controleur 
	 * 		
	 */
	public GrapheRoutier getGrapheRoutier() {
		return this.grapheRoutier;
		}
	
	
	
	/** getter de la feuille de route contenant les PlageHoraire de livraisons
	 * @return {@link FeuilleDeRoute} : feuille de route du controleur
	 */
	public FeuilleDeRoute getFeuilleDeRoute() {
		return this.feuilledeRoute;
		}
	
	
	
	/**
	 * Ajoute une livraison et l'enregistre dans la pile pour undo/redo
	 * @param idIntersection : id de l'intersection de l'adresse de livraison
	 * @param idClient : id du client de la livraison
	 * @param idLivraisonPrecedente : id de la livraison effectué avant celle ajoutée
	 */
	public void ajouterLivraison(int idIntersection, int idClient, int idLivraisonPrecedente) {
		Livraison precedente = this.getFeuilleDeRoute().getGrapheLivraison().getLivraison(idLivraisonPrecedente);
		Intersection inter = this.grapheRoutier.getIntersection(idIntersection);
		List<Livraison> lI = precedente.getPlageHoraire().getListeLivraison();
		
		
		
		//creation de la commandeAjout
		Livraison nouvelle = new Livraison(inter, lI.size()+1 , idClient);
		nouvelle.setPlageHoraire(precedente.getPlageHoraire());
		CommandeAjout c = new CommandeAjout(nouvelle, precedente, this.getFeuilleDeRoute(), this.grapheRoutier);
		
		c.executer();
		this.listeFaits.push(c);
		this.listeAnnules.clear(); // supprime les actions annulees
	}
	
	/**
	 * Supprime la livraison concernee
	 * @param idLivraison : id de la livraison a supprimer
	 */
	public void supprimerLivraison(int idLivraison) {
		Livraison livraison = this.getFeuilleDeRoute().getGrapheLivraison().getLivraison(idLivraison);
		CommandeSupression c = new CommandeSupression(livraison, this.grapheRoutier, this.feuilledeRoute);
		
		c.executer();
		this.listeFaits.push(c);
		this.listeAnnules.clear(); // supprime les actions annulees
	}
	
	/**
	 * annule l'ajout/suppression et met a jour les piles
	 */
	public void annuler() {
		Commande c = this.listeFaits.pop();
		c.annuler();
		this.listeAnnules.push(c); // met sur la pile des actions annullees
	}
	
	/**
	 * Retabli l'annulation et met a jour les piles
	 */
	public void retablir() {
		Commande c = this.listeAnnules.pop();
		c.executer();
		this.listeFaits.push(c); // remet sur la pile des actions faites
	}
}