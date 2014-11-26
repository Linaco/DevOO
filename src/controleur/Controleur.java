package controleur;

import org.w3c.dom.*;
import modele.*;

public class Controleur {
	
	public Controleur(){
		
	}
	
	public int chargerPlan(Document plan){
		//creation du graphe
		GrapheRoutier graphe = new GrapheRoutier();
		//premier passage et creation des intersections
		NodeList intersections = plan.getElementsByTagName("Noeud");
		
		for (int i = 0; i<intersections.getLength()-1;i++){
			Node noeud = intersections.item(i);
			if(noeud.hasAttributes()){
				NamedNodeMap attr= noeud.getAttributes();
				int id = Integer.parseInt(attr.getNamedItem("id").getTextContent());
				int x = Integer.parseInt(attr.getNamedItem("x").getTextContent());
				int y = Integer.parseInt(attr.getNamedItem("y").getTextContent());
				Intersection inter = new Intersection(id,x,y);
				graphe.AjouterIntersection(inter);
			}
		}
		
		//deuxieme passage 
		NodeList routes = plan.getElementsByTagName("LeTronconSortant");
		for(int j = 0 ; j<routes.getLength()-1 ; j++){
			Node route = routes.item(j);
			if(route.hasAttributes()){
				NamedNodeMap attr = route.getAttributes();
				String nom = attr.getNamedItem("nomRue").getTextContent();
				double vitesse = Double.parseDouble(attr.getNamedItem("vitesse").getTextContent());
				double longueur = Double.parseDouble(attr.getNamedItem("longueur").getTextContent());
				
			}
		}
		
		return 0;
	}
	
}