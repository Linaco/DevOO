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
		//Generation du document
		livDoc.getDocumentElement().normalize();

		Node entrepotNode = livDoc.getElementsByTagName("Entrepot").item(0);
		if(entrepotNode.getNodeType()==Node.ELEMENT_NODE){
			Element entreElement = (Element)entrepotNode;
			try{
				int idInter = Integer.parseInt(entreElement.getAttribute("adresse"));
				if(grapheRoutier.interExiste(idInter)){
					feuilledeRoute.renseignerEntrepot(grapheRoutier.rechercherInterParId(idInter));
				}else{
					System.err.println("Entrepot invalide");
					return false;
				}
			}catch(Exception e){
				System.err.println("Entrepot invalide");
				return false;
			}
		}
		
		NodeList phNodeList = livDoc.getElementsByTagName("Plage");
		if(phNodeList.getLength()==0){
			System.err.println("Aucune plage horaire ");
			return false;
		}
		
		//premier passage, creation des plages horaires
		for(int i =0;i<phNodeList.getLength();i++){
			Node phNode = phNodeList.item(i);
			if(phNode.getNodeType() == Node.ELEMENT_NODE){
				Element phElement = (Element)phNode;
				if(phElement.hasAttributes()){
					NamedNodeMap nnm = phElement.getAttributes();
					/*
					 * Vérification de l'intégrité des données pour les PlageHoraire
					 */
					try{
						String heureDeb = nnm.getNamedItem("heureDebut").getTextContent();
						String heureF = nnm.getNamedItem("heureFin").getTextContent();
						if(heureDeb == null || heureF==null){
							System.err.println("Heure invalide");
							return false;
						}else{
							try{
								Date heureDebut = HOUR_FORMAT.parse(heureDeb);
								Date heureFin = HOUR_FORMAT.parse(heureF);
								// Vérification de la cohérence des heures fournies
								if(heureDebut.getHours()>=0 && heureDebut.getHours()<=24
								&& heureFin.getHours()>=0 && heureFin.getHours()<=24
									&& heureDebut.getHours() < heureFin.getHours()
									&& feuilledeRoute.checkHB(heureDebut)){
									PlageHoraire ph = new PlageHoraire(heureDebut,heureFin);
									feuilledeRoute.ajouterPlageHoraire(ph);
								}else{
									System.err.println("Format d'heure invalide.");
									return false;
								}
							}catch (Exception e){
								e.printStackTrace();
								System.err.println("Format d'heure invalide.");
								return false;
							}
						}
					}catch(Exception e){
						System.err.println("Format Plage horaire invalide.");
						return false;
					}
				}else{
					System.err.println("HasAttributes.");
					return false;
				}
			}
		}
		
		/* Deuxième passage avec création des Livraison et mise en relation avec la plage horaire 
		 * correspondante.
		 */
		
		NodeList livNodeList = livDoc.getElementsByTagName("Livraison");
		if(livNodeList.getLength() ==0){
			System.err.println("Aucune livraison");
			return false;
		}
		for(int j=0; j<livNodeList.getLength(); j++){
			Node livNode = livNodeList.item(j);
			if(livNode.getNodeType() == Node.ELEMENT_NODE){
				// Récupération de la PlageHoraire parente
				Element livElement = (Element)livNode;
				Element phParent = (Element)livElement.getParentNode().getParentNode();
				String hDeb = phParent.getAttribute("heureDebut");
				PlageHoraire phParentObj;
				try{
					Date hDebDate = HOUR_FORMAT.parse(hDeb);
					phParentObj = feuilledeRoute.rechercherPHParHD(hDebDate);

					if(phParentObj == null ){
						System.err.println("Plage Horaire Parente non trouvée");
						return false;
					}
					
				}catch(Exception e){
					e.printStackTrace();
					System.err.println("Page horaire parente invalide");
					return false;
				}
				
				if(livElement.hasAttributes()){
					NamedNodeMap nnm = livElement.getAttributes();
					// Vérification de l'intégrité des données pour les Livraisons
					try{
						int id = Integer.parseInt(nnm.getNamedItem("id").getTextContent());
						int idClient = Integer.parseInt(nnm.getNamedItem("client").getTextContent());
						int idInter = Integer.parseInt(nnm.getNamedItem("adresse").getTextContent());
						
						
						if(grapheRoutier.interExiste(idInter)){
							Intersection inter = grapheRoutier.rechercherInterParId(idInter);
							Livraison liv = new Livraison(inter,id, idClient);
							phParentObj.addLivraison(liv);
						}
					}catch(Exception e){
						System.err.println("Format de livraion invalide");
						return false;
					}
					
				}else{
					System.err.println("Has arrtibute");
					return false;
				}
			}
		}
		
		
		return true;
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
		
		//remise à zéro du graphe routier
		grapheRoutier.clean();
		plan.getDocumentElement().normalize();
		
		//premier passage avec création des intersections
		NodeList intersections = plan.getElementsByTagName("Noeud");
		
		if(intersections.getLength() == 0){
			System.err.println("Aucune intersections");
			return false;
		}
			
		
		for (int i = 0; i<intersections.getLength();i++){
			Node noeud = intersections.item(i);
			if(noeud.getNodeType() == Node.ELEMENT_NODE){
				
				Element elementNoeud = (Element) noeud;
				if(elementNoeud.hasAttributes()){
					NamedNodeMap attr= elementNoeud.getAttributes();
					/* Vérification de l'intégrité des données fournies par le document pour les 
					 * intersections
					 */
					try{
						int id = Integer.parseInt(attr.getNamedItem("id").getTextContent());
						if(grapheRoutier.interExiste(id)){
							System.err.println("Intersection déjà existante");
							return false;
						}
						int x = Integer.parseInt(attr.getNamedItem("x").getTextContent());
						if(x<0){
							System.err.println("x < 0");
							return false;
						}
						int y = Integer.parseInt(attr.getNamedItem("y").getTextContent());
						if(y<0){
							System.err.println("y < 0");
							return false;
						}
						Intersection inter = new Intersection(id,x,y);
						grapheRoutier.ajouterIntersection(inter);
						}
					catch(Exception e){
						e.printStackTrace();
						return false;
					}
				}else{
					System.err.println("hasAttribute...");
					return false;
				}
			}
		}

		//deuxième passage avec création des Routes
		NodeList routes = plan.getElementsByTagName("LeTronconSortant");
		if(routes.getLength()==0)
			return false;
		for(int j = 0 ; j<routes.getLength() ; j++){
			Node routeNode = routes.item(j);
			if(routeNode.getNodeType() == Node.ELEMENT_NODE){
				Element elementRoute = (Element) routeNode;
				if(elementRoute.hasAttributes()){
					/* Vérification de l'intégrité des données fournies par le xml
					 */
					try{
						NamedNodeMap attr = elementRoute.getAttributes();
						String nom = attr.getNamedItem("nomRue").getTextContent();
						double vitesse = Double.parseDouble(attr.getNamedItem("vitesse").getTextContent().replace(",", "."));
						if(vitesse<0){
							System.err.println("Vitesse <0");
							return false;
						}
						double longueur = Double.parseDouble(attr.getNamedItem("longueur").getTextContent().replace(",", "."));
						if(longueur<0){
							System.err.println("longueur <0");
							return false;
						}
						/* Vérification de l'existence de la destination
						 * Récupération de l'intersection de provenance pour lui ajouter la nouvelle route
						 */
						int idInter = Integer.parseInt(attr.getNamedItem("idNoeudDestination").getTextContent());
						if(grapheRoutier.interExiste(idInter)){
							Intersection inter = grapheRoutier.rechercherInterParId(idInter);
							Route routeObj = new Route(nom,vitesse,longueur,inter);
							int  idParent = Integer.parseInt(elementRoute.getParentNode().getAttributes().getNamedItem("id").getTextContent());
							Intersection parent = grapheRoutier.rechercherInterParId(idParent);
							parent.addTroncSortant(routeObj);
						}else{
							System.err.println("Erreur sur destination");
							return false;
						}
					}catch(Exception e){
						e.printStackTrace();
						return false;
					}
					
				}else{
					return false;
				}
			}
		}
		
		return true;
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