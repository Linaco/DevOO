package controleur;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;
import org.w3c.dom.*;
import modele.*;

public class Controleur {
	
	/**
	 * Attribut de classe DateFormat permettant le formatage de l'heure à partir d'une String
	 */
	private static final DateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm:ss");
	
	private GrapheRoutier grapheRoutier;
	private FeuilleDeRoute feuilledeRoute;
	private Stack<Commande> listeFaits;
	private Stack<Commande> listeAnnules;
	/**
	 * Constructeur de Controleur
	 */
	 public Controleur(){
		grapheRoutier = new GrapheRoutier();
		feuilledeRoute = new FeuilleDeRoute();
	}
	
	
	/**
	 * Génération de la FeuilleDeRoute à partir d'un document xml passé en paramètre
	 * Le document est parsé 2 fois, une fois pour générer les PlagesHoraires,
	 * une fois pour générer les Livraisons et les lier à la PlageHoraire parente
	 * Si tout se passe bien, renvoie true
	 * Sinon renvoie false avec affichage en console du type de problème
	 * @param livDoc
	 * @return boolean
	 */
	@SuppressWarnings("deprecation")
	public boolean chargerLivraisons(Document livDoc){
		
		feuilledeRoute.clean();
		feuilledeRoute = CommandeChargerLivraisons.chargerLivraisons(livDoc, grapheRoutier);
		if(feuilledeRoute.getPlagesHoraires().size() == 0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Génération du GrapheRoutier à partir d'un document xml passé en paramètre
	 * Le document est parsé 2 fois, une fois pour générer les Intersections,
	 * une fois pour générer les Routes et les lier aux Intersections correspondantes
	 * Si tout se passe bien, renvoie true
	 * Sinon renvoie false avec affichage en console du type de problème
	 * @param plan
	 * @return boolean
	 */
	public boolean chargerPlan(Document plan){
		grapheRoutier.clean();
		grapheRoutier = CommandeChargerPlan.chargerPlan(plan);
		if(grapheRoutier.getListeIntersections().size() == 0){
			return false;
		}else{
			return true;
		}
	}

	
	
	/**
	 * getter du graphe Routier contenant les Intersections
	 * @return GrapheRoutier
	 */
	public GrapheRoutier getGrapheRoutier() {
		return this.grapheRoutier;
		}
	
	
	
	/** getter de la feuille de route contenant les PlageHoraire de livraisons
	 * @return FeuilleDeRoute
	 */
	public FeuilleDeRoute getFeuilleDeRoute() {
		return this.feuilledeRoute;
		}
	
	
	
	/**
	 * Ajoute une livraison et l'enregistre dans la pile pour undo/redo
	 * @param idIntersection
	 * @param idClient
	 * @param idLivraisonPrecedente
	 */
	public void ajouterLivraison(int idIntersection, int idClient, int idLivraisonPrecedente) {
		Livraison precedente = this.getFeuilleDeRoute().getGrapheLivraison().getLivraison(idLivraisonPrecedente);
		Intersection inter = this.grapheRoutier.getIntersection(idIntersection);
		int idPlageHoraire = precedente.getPlageHoraire().getIdPlageHoraire();
		
		//creation de la commandeAjout
		Livraison nouvelle = new Livraison(inter, idPlageHoraire, idClient);
		CommandeAjout c = new CommandeAjout(nouvelle, precedente, this.getFeuilleDeRoute(), this.grapheRoutier);
		
		c.executer();
		this.listeFaits.push(c);
		this.listeAnnules.clear(); // supprime les actions annulees
	}
	
	/**
	 * Supprime la livraison concernee
	 * @param idLivraison id de la livraison a supprimer
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