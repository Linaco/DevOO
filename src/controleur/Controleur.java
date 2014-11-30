package controleur;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
<<<<<<< HEAD
import java.util.*;
import modele.*;
=======
>>>>>>> dd526e843a1853acf69e58c7874317cbd05c3a5b

import modele.*;

public class Controleur {
<<<<<<< HEAD
	
<<<<<<< HEAD
	private FeuilleDeRoute feuilleDeRoute;
	private List<Commande> listeCommande;
=======
=======
>>>>>>> dd526e843a1853acf69e58c7874317cbd05c3a5b
	private GrapheRoutier grapheRoutier;
>>>>>>> 53de675fed369e4fab7050e250ca831b16951a75
	
	public Controleur(){
<<<<<<< HEAD
		feuilleDeRoute = new FeuilleDeRoute();
		listeCommande = new ArrayList<Commande>();
=======
		grapheRoutier = new GrapheRoutier();
>>>>>>> dd526e843a1853acf69e58c7874317cbd05c3a5b
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
						float vitesse = Float.parseFloat(attr.getNamedItem("vitesse").getTextContent().replace(",", "."));
						if(vitesse<0){
							System.err.println("Vitesse <0");
							return false;
						}
						float longueur = Float.parseFloat(attr.getNamedItem("longueur").getTextContent().replace(",", "."));
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
<<<<<<< HEAD
	
	public void doCommande(Commande commande){
		
	}
	
	public void undoCommande(){
		
	}
	
	public void redoCommande(){
		
	}
	
=======

	public GrapheRoutier getGrapheRoutier(){return this.grapheRoutier;}
>>>>>>> dd526e843a1853acf69e58c7874317cbd05c3a5b
}