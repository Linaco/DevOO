package controleur;

import modele.GrapheRoutier;
import modele.Intersection;
import modele.Route;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CommandeChargerPlan {
	
	private static GrapheRoutier gr;

	
	
	
	/**
	 * Génération du GrapheRoutier à partir d'un document xml passé en paramètre
	 * Le document est parsé 2 fois, une fois pour générer les Intersections,
	 * une fois pour générer les Routes et les lier aux Intersections correspondantes
	 * Si le document est invalide, on vide le graphe et on le renvoie.
	 * Sinon le graph est envoyé avec toutes ses intersetions
	 * @param plan
	 * @return grapheRoutier
	 */
	public static GrapheRoutier chargerPlan(Document plan){
		
		//remise à zéro du graphe routier
		gr = new GrapheRoutier();
		plan.getDocumentElement().normalize();
		
		//premier passage avec création des intersections
		NodeList intersections = plan.getElementsByTagName("Noeud");
		
		if(intersections.getLength() == 0){
			System.err.println("Aucune intersections");
			gr.clean();
			return gr;
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
						if(gr.interExiste(id)){
							System.err.println("Intersection déjà existante");
							gr.clean();
							return gr;
						}
						int x = Integer.parseInt(attr.getNamedItem("x").getTextContent());
						if(x<0){
							System.err.println("x < 0");
							gr.clean();
							return gr;
						}
						int y = Integer.parseInt(attr.getNamedItem("y").getTextContent());
						if(y<0){
							System.err.println("y < 0");
							gr.clean();
							return gr;
						}
						Intersection inter = new Intersection(id,x,y);
						gr.ajouterIntersection(inter);
						}
					catch(Exception e){
						e.printStackTrace();
						gr.clean();
						return gr;
					}
				}else{
					System.err.println("hasAttribute...");
					gr.clean();
					return gr;
				}
			}
		}

		//deuxième passage avec création des Routes
		NodeList routes = plan.getElementsByTagName("LeTronconSortant");
		if(routes.getLength()==0){
			gr.clean();
			return gr;
		}
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
							gr.clean();
							return gr;
						}
						double longueur = Double.parseDouble(attr.getNamedItem("longueur").getTextContent().replace(",", "."));
						if(longueur<0){
							System.err.println("longueur <0");
							gr.clean();
							return gr;
						}
						/* Vérification de l'existence de la destination
						 * Récupération de l'intersection de provenance pour lui ajouter la nouvelle route
						 */
						int idInter = Integer.parseInt(attr.getNamedItem("idNoeudDestination").getTextContent());
						if(gr.interExiste(idInter)){
							Intersection inter = gr.rechercherInterParId(idInter);
							Route routeObj = new Route(nom,vitesse,longueur,inter);
							int  idParent = Integer.parseInt(elementRoute.getParentNode().getAttributes().getNamedItem("id").getTextContent());
							Intersection parent = gr.rechercherInterParId(idParent);
							parent.addTroncSortant(routeObj);
						}else{
							System.err.println("Erreur sur destination");
							gr.clean();
							return gr;
						}
					}catch(Exception e){
						e.printStackTrace();
						gr.clean();
						return gr;
					}
					
				}else{
					gr.clean();
					return gr;
				}
			}
		}
		
		return gr;
	}

	
}
