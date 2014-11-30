package controleur;

import java.util.Iterator;

import org.w3c.dom.*;
import modele.*;

import modele.*;

public class Controleur {
	
	private GrapheRoutier grapheRoutier;
	
	public Controleur(){
		
	}
	
	public boolean checkInter(int id, int x, int y){
		if(id>=0 || id<=grapheRoutier.consulterListeIntersection().size()-1){
			return false;
		}else if(x<0){
			return false;
		}else if(y<0){
			return false;
		}else{
			return true;
		}		
	}
	
	public int chargerPlan(Document plan){
		//creation du graphe

		grapheRoutier = new GrapheRoutier();


		//premier passage et creation des intersections
		NodeList intersections = plan.getElementsByTagName("Noeud");
		
		for (int i = 0; i<intersections.getLength()-1;i++){
			Node noeud = intersections.item(i);
			if(noeud.hasAttributes()){
				NamedNodeMap attr= noeud.getAttributes();
				int id = Integer.parseInt(attr.getNamedItem("id").getTextContent());
				int x = Integer.parseInt(attr.getNamedItem("x").getTextContent());
				int y = Integer.parseInt(attr.getNamedItem("y").getTextContent());
				if(checkInter(id,x,y)){
					Intersection inter = new Intersection(id,x,y);
					grapheRoutier.ajouterIntersection(inter);
				}
			}
		}
		
		//deuxieme passage 
		NodeList routes = plan.getElementsByTagName("LeTronconSortant");
		for(int j = 0 ; j<routes.getLength()-1 ; j++){
			Node route = routes.item(j);
			if(route.hasAttributes()){
				NamedNodeMap attr = route.getAttributes();
				
				//creation de route
				String nom = attr.getNamedItem("nomRue").getTextContent();
				double vitesse = Double.parseDouble(attr.getNamedItem("vitesse").getTextContent());
				double longueur = Double.parseDouble(attr.getNamedItem("longueur").getTextContent());
				int idInter = Integer.parseInt(attr.getNamedItem("idNoeudDestination").getTextContent());
				Intersection inter = grapheRoutier.rechercherInterParId(idInter);
				Route r = new Route(nom,vitesse,longueur,inter);
				
				//association a l'intersection parent
				Node parent = route.getParentNode();
				int idParent= Integer.parseInt(parent.getAttributes().getNamedItem("id").getTextContent());
				Intersection papa = grapheRoutier.rechercherInterParId(idParent);
				papa.ajouterRoute(r);

				
			}
		}
		
		return 0;
	}
	
}