package controleur;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import modele.Intersection;
import modele.Livraison;
import modele.PlageHoraire;
import modele.GrapheRoutier;
import modele.FeuilleDeRoute;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CommandeChargerLivraisons {
	
	
	private static final DateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm:ss");
	private static FeuilleDeRoute fdr;
	
	/**
	 * Génération de la FeuilleDeRoute à partir d'un document xml passé en paramètre
	 * Le document est parsé 2 fois, une fois pour générer les PlagesHoraires,
	 * une fois pour générer les Livraisons et les lier à la PlageHoraire parente
	 * Si tout se passe bien, renvoie true
	 * Sinon renvoie false avec affichage en console du type de problème
	 * @param livDoc
	 * @return boolean
	 */
	public static FeuilleDeRoute chargerLivraisons(Document livDoc, GrapheRoutier grapheRoutier){
		fdr = new FeuilleDeRoute();
		livDoc.getDocumentElement().normalize();
		
		//check entrepot
		if(!genererEntrepot(livDoc,grapheRoutier)){
			fdr.clean();
			return fdr;
		}
		
		NodeList phNodeList = livDoc.getElementsByTagName("Plage");
		if(phNodeList.getLength()==0){
			System.err.println("Aucune plage horaire ");
			fdr.clean();
			return fdr;
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
							fdr.clean();
							return fdr;
						}else{
							try{
								Date heureDebut = HOUR_FORMAT.parse(heureDeb);
								Date heureFin = HOUR_FORMAT.parse(heureF);
								// Vérification de la cohérence des heures fournies
								if(heureDebut.getHours()>=0 && heureDebut.getHours()<=24
								&& heureFin.getHours()>=0 && heureFin.getHours()<=24
									&& heureDebut.before(heureFin)
									&& fdr.checkHB(heureDebut)){
									PlageHoraire ph = new PlageHoraire(heureDebut,heureFin);
									fdr.ajouterPlageHoraire(ph);
								}else{
									System.err.println("Format d'heure invalide.");
									fdr.clean();
									return fdr;
								}
							}catch (Exception e){
								e.printStackTrace();
								System.err.println("Format d'heure invalide.");
								fdr.clean();
								return fdr;
							}
						}
					}catch(Exception e){
						System.err.println("Format Plage horaire invalide.");
						fdr.clean();
						return fdr;
					}
				}else{
					System.err.println("HasAttributes.");
					fdr.clean();
					return fdr;
				}
			}
		}
		
		/* Deuxième passage avec création des Livraison et mise en relation avec la plage horaire 
		 * correspondante.
		 */
		
		NodeList livNodeList = livDoc.getElementsByTagName("Livraison");
		if(livNodeList.getLength() ==0){
			System.err.println("Aucune livraison");
			fdr.clean();
			return fdr;
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
					phParentObj = fdr.rechercherPHParHD(hDebDate);

					if(phParentObj == null ){
						System.err.println("Plage Horaire Parente non trouvée");
						fdr.clean();
						return fdr;
					}
					
				}catch(Exception e){
					e.printStackTrace();
					System.err.println("Page horaire parente invalide");
					fdr.clean();
					return fdr;
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
						fdr.clean();
						return fdr;
					}
					
				}else{
					System.err.println("Has arrtibute");
					fdr.clean();
					return fdr;
				}
			}
		}
		
		
		return fdr;
	}
	
	public static boolean genererEntrepot(Document livDoc,GrapheRoutier grapheRoutier){
		Node entrepotNode = livDoc.getElementsByTagName("Entrepot").item(0);
		if(entrepotNode.getNodeType()==Node.ELEMENT_NODE){
			Element entreElement = (Element)entrepotNode;
			try{
				int idInter = Integer.parseInt(entreElement.getAttribute("adresse"));
				if(grapheRoutier.interExiste(idInter)){
					fdr.renseignerEntrepot(grapheRoutier.rechercherInterParId(idInter));
					return true;
				}else{
					System.err.println("Entrepot invalide");
					return false;
				}
			}catch(Exception e){
				System.err.println("Entrepot invalide");
				return false;
			}
		}else{
			return false;
		}
		
	}

	@SuppressWarnings("deprecation")
	public static boolean genererPlageHoraire(Document livDoc){
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
									&& heureDebut.before(heureFin)
									&& fdr.checkHB(heureDebut)){
									PlageHoraire ph = new PlageHoraire(heureDebut,heureFin);
									fdr.ajouterPlageHoraire(ph);
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
		return true;
	}
}
