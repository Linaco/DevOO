              package controleur;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
import java.util.*;
import modele.*;

public class Controleur {
	
	private static final DateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm:ss");
	
	private GrapheRoutier grapheRoutier;
	private GrapheLivraison grapheLivraison;
	private FeuilleDeRoute feuilleDeRoute;
	private List<Commande> listeCommande;

	public Controleur(){
		feuilleDeRoute = new FeuilleDeRoute();
		listeCommande = new ArrayList<Commande>();
		grapheRoutier = new GrapheRoutier();
		grapheLivraison = new GrapheLivraison();
	}
	
	public boolean chargerLivraisons(String path){
	
		//Generation du document
		Document livDoc;		
		try{
			File fXmlFile = new File(path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			livDoc = dBuilder.parse(fXmlFile);
		}catch(Exception e){
			System.err.println("Erreur à la création de doc");
			e.printStackTrace();
			return false;
		}
		livDoc.getDocumentElement().normalize();

		NodeList phNodeList = livDoc.getElementsByTagName("Plage");
		if(phNodeList.getLength()==0){
			System.err.println("Aucune plage horaire ");
			return false;
		}
		
		//premier passage, creation des plages horaires
		//TODO géré les heures casse couille
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
								PlageHoraire ph = new PlageHoraire(heureDebut,heureFin);
								feuilleDeRoute.ajouterPlageHoraire(ph);
							}catch (Exception e){
								System.err.println("Format d'heure invalide.");
								return false;
							}
						}
					}catch(Exception e){
						e.printStackTrace();
						return false;
					}
				}else{
					System.err.println("HasAttributes.");
					return false;
				}
			}
		}
		
		return true;
	}
	
	public boolean chargerPlan(String path){
		Document plan;
		try{
			File fXmlFile = new File(path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			plan = dBuilder.parse(fXmlFile);
		}catch(Exception e){
			System.err.println("Erreur à la création de doc");
			e.printStackTrace();
			return false;
		}
		plan.getDocumentElement().normalize();
		//premier passage et création des intersections
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
							System.err.println("x inférieur à 0");
							return false;
						}
						int y = Integer.parseInt(attr.getNamedItem("y").getTextContent());
						if(y<0){
							System.err.println("y inférieur à 0");
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
		
		//deuxième passage 
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
							inter.addTroncSortant(routeObj);
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

	
	public void doCommande(Commande commande){
		
	}
	
	public void undoCommande(){
		
	}
	
	public void redoCommande(){
		
	}


	public GrapheRoutier getGrapheRoutier(){return this.grapheRoutier;}

	public GrapheLivraison getGrapheLivraison(){return this.grapheLivraison;}
	public FeuilleDeRoute getFeuilleDeRoute(){return this.feuilleDeRoute;}
}