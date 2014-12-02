package controleur;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;






import java.util.Stack;

import org.w3c.dom.*;

import modele.*;

public class Controleur {
	
	private static final DateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm:ss");
	
	private GrapheRoutier grapheRoutier;
	private FeuilleDeRoute feuilledeRoute;
	private Stack<Commande> listeFaits;
	private Stack<Commande> listeAnnules;
	
	public Controleur(){
		grapheRoutier = new GrapheRoutier();
		feuilledeRoute = new FeuilleDeRoute();
	}
	
	//deprec sur getHours
	/**
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
		//TODO gï¿½rï¿½ les heures casse couille
		for(int i =0;i<phNodeList.getLength();i++){
			Node phNode = phNodeList.item(i);
			if(phNode.getNodeType() == Node.ELEMENT_NODE){
				Element phElement = (Element)phNode;
				if(phElement.hasAttributes()){
					NamedNodeMap nnm = phElement.getAttributes();
					try{
						String heureDeb = nnm.getNamedItem("heureDebut").getTextContent();
						String heureF = nnm.getNamedItem("heureFin").getTextContent();
						if(heureDeb.equals(null) && heureF.equals(null)){
							System.err.println("Heure invalide");
							return false;
						}else{
							try{
								Date heureDebut = HOUR_FORMAT.parse(heureDeb);
								Date heureFin = HOUR_FORMAT.parse(heureF);
								if(heureDebut.getHours()>=0 && heureDebut.getHours()<=24
								&& heureFin.getHours()>=0 && heureFin.getHours()<=24
									&& heureDebut.getHours() < heureFin.getHours()){
									PlageHoraire ph = new PlageHoraire(heureDebut,heureFin);
									feuilledeRoute.ajouterPlageHoraire(ph);
								}else{
									System.err.println("Format d'heure invalide.");
									return false;
								}
							}catch (Exception e){
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
			
		NodeList livNodeList = livDoc.getElementsByTagName("Livraison");
		if(livNodeList.getLength() ==0){
			System.err.println("Aucune livraison");
			return false;
		}
		for(int j=0; j<livNodeList.getLength(); j++){
			Node livNode = livNodeList.item(j);
			if(livNode.getNodeType() == Node.ELEMENT_NODE){
				Element livElement = (Element)livNode;
				Element phParent = (Element)livElement.getParentNode().getParentNode();
				String hDeb = phParent.getAttribute("heureDebut");
				PlageHoraire phParentObj;
				try{
					Date hDebDate = HOUR_FORMAT.parse(hDeb);
					phParentObj = feuilledeRoute.rechercherPHParHD(hDebDate);
<<<<<<< HEAD
					if(phParentObj.equals(null)){
						System.err.println("Plage Horaire Parente non trouvï¿½e");
=======
					if(phParentObj == null ){
						System.err.println("Plage Horaire Parente non trouvée");
>>>>>>> 0f6cc08e026357a268c43e66c4524753f45470fa
						return false;
					}
					
				}catch(Exception e){
					e.printStackTrace();
					System.err.println("Page horaire parente invalide");
					return false;
				}
				
				if(livElement.hasAttributes()){
					NamedNodeMap nnm = livElement.getAttributes();
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
	
	public boolean chargerPlan(Document plan){
		
		grapheRoutier.clean();
		
		plan.getDocumentElement().normalize();
		//premier passage et crï¿½ation des intersections
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
					try{
						int id = Integer.parseInt(attr.getNamedItem("id").getTextContent());
						int x = Integer.parseInt(attr.getNamedItem("x").getTextContent());
						if(x<0){
							System.err.println("x infï¿½rieur ï¿½ 0");
							return false;
						}
						int y = Integer.parseInt(attr.getNamedItem("y").getTextContent());
						if(y<0){
							System.err.println("y infï¿½rieur ï¿½ 0");
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
		
		//deuxiï¿½me passage 
		NodeList routes = plan.getElementsByTagName("LeTronconSortant");
		if(routes.getLength()==0)
			return false;
		for(int j = 0 ; j<routes.getLength() ; j++){
			Node routeNode = routes.item(j);
			if(routeNode.getNodeType() == Node.ELEMENT_NODE){
				Element elementRoute = (Element) routeNode;
				if(elementRoute.hasAttributes()){
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
						int idInter = Integer.parseInt(attr.getNamedItem("idNoeudDestination").getTextContent());
						if(grapheRoutier.interExiste(idInter)){
							Intersection inter = grapheRoutier.rechercherInterParId(idInter);
							Route routeObj = new Route(nom,vitesse,longueur,inter);
							int  idParent = Integer.parseInt(elementRoute.getParentNode().getAttributes().getNamedItem("id").getTextContent());
							Intersection parent = grapheRoutier.rechercherInterParId(idParent);
							parent.addTroncSortant(routeObj);
						}else{
							System.err.println("Erreur sur route");
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

	public GrapheRoutier getGrapheRoutier(){return this.grapheRoutier;}
	public FeuilleDeRoute getFeuilleDeRoute(){return this.feuilledeRoute;}
	
	public void annuler() {
		this.listeFaits.pop().annuler();
	}
	
	public void retablir() {
		this.listeAnnules.pop().executer();
	}
}