package appcom;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import controleur.Controleur;

public class ServeurBuilder {

	private ServeurLivraison serveur;
	
	private ServeurBuilder(ServeurLivraison s){
		this.serveur = s;
	}
	
	public static ServeurBuilder nouveauServeur(){
		ServeurLivraison serveur = new ServeurLivraison();
		ServeurBuilder sb = new ServeurBuilder(serveur);
		serveur.Demarrer();
		return sb;
	}
	
	/**
	 * methodes pas à jour car format de sortie non specifie
	 * @param c
	 * @return
	 */
	public ServeurBuilder deployerServicesControleur(Controleur c){
		
		new ServiceControleur(c,"charger-plan",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						
						if (this.getControleur().chargerPlan(doc)) {
							return Reponse.succes("Le plan a bien été chargé.");
						} else {
							return Reponse.erreur("Le service de chargement du plan a échoué.");
						}
						
					} catch (SAXException e) {
						e.printStackTrace();
						return Reponse.erreur("Woops,\nnous n'avons pas pu interpréter le fichier transmis.\n"
								+ "Veuillez vous assurer qu'il ne contient aucune erreur.");
					} catch (IOException e) {
						e.printStackTrace();
						return Reponse.erreur("Erreur lors de la lecture du fichier.");
					}
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					return Reponse.erreur("Problème d'initialisation dans la gestion du service");
				}
			}
		};
		
		new ServiceControleur(c,"charger-livraisons",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
			
						if (this.getControleur().chargerLivraisons(doc)) {
							return Reponse.succes("Les livraisons ont été chargées.");
						} else {
							return Reponse.erreur("Le service de chargement des livraisons a échoué.");	
						}
						
					} catch (SAXException e) {
						e.printStackTrace();
						return Reponse.erreur("Woops,\nnous n'avons pas pu interpréter le fichier transmis.\n"
								+ "Veuillez vous assurer qu'il ne contient aucune erreur.");
					} catch (IOException e) {
						e.printStackTrace();
						return Reponse.erreur("Erreur lors de la lecture du fichier.");
					}
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					return Reponse.erreur("Problème d'initialisation dans la gestion du service");
				}
			}
		};
		
		new ServiceControleur(c,"ajouter-livraison",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						//a changer
						//this.getControleur().chargerPlan(doc);
						
						return Reponse.succes("La communication s'est bien déroulée.");
						
					} catch (SAXException e) {
						e.printStackTrace();
						return Reponse.erreur("Woops,\nnous n'avons pas pu interpréter le fichier transmis.\n"
								+ "Veuillez vous assurer qu'il ne contient aucune erreur.");
					} catch (IOException e) {
						e.printStackTrace();
						return Reponse.erreur("Erreur lors de la lecture du fichier.");
					}
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					return Reponse.erreur("Problème d'initialisation dans la gestion du service");
				}
			}
		};
		
		new ServiceControleur(c,"supprimer-livraison",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						//a changer
						//this.getControleur().chargerPlan(doc);
						
						return Reponse.succes("La communication s'est bien déroulée.");
						
					} catch (SAXException e) {
						e.printStackTrace();
						return Reponse.erreur("Woops,\nnous n'avons pas pu interpréter le fichier transmis.\n"
								+ "Veuillez vous assurer qu'il ne contient aucune erreur.");
					} catch (IOException e) {
						e.printStackTrace();
						return Reponse.erreur("Erreur lors de la lecture du fichier.");
					}
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					return Reponse.erreur("Problème d'initialisation dans la gestion du service");
				}
			}
		};
		
		new ServiceControleur(c,"annuler",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						//a changer
						//this.getControleur().chargerPlan(doc);
						
						return Reponse.succes("La communication s'est bien déroulée.");
						
					} catch (SAXException e) {
						e.printStackTrace();
						return Reponse.erreur("Woops,\nnous n'avons pas pu interpréter le fichier transmis.\n"
								+ "Veuillez vous assurer qu'il ne contient aucune erreur.");
					} catch (IOException e) {
						e.printStackTrace();
						return Reponse.erreur("Erreur lors de la lecture du fichier.");
					}
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					return Reponse.erreur("Problème d'initialisation dans la gestion du service");
				}
			}
		};
		
		new ServiceControleur(c,"retablir",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						//a changer
						//this.getControleur().chargerPlan(doc);
						
						return Reponse.succes("La communication s'est bien déroulée.");
						
					} catch (SAXException e) {
						e.printStackTrace();
						return Reponse.erreur("Woops,\nnous n'avons pas pu interpréter le fichier transmis.\n"
								+ "Veuillez vous assurer qu'il ne contient aucune erreur.");
					} catch (IOException e) {
						e.printStackTrace();
						return Reponse.erreur("Erreur lors de la lecture du fichier.");
					}
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					return Reponse.erreur("Problème d'initialisation dans la gestion du service");
				}
			}
		};
		
		new ServiceControleur(c,"calculer-itineraire",this.serveur){
			protected Reponse getReponse(InputStream in){
				this.getControleur().getFeuilleDeRoute().calculerParcours(this.getControleur().getGrapheRoutier());
				return Reponse.succes("Itineraire calculé !");			
			}
		};
		
		return this;
	}
	
	
	/**
	 * itineraire toString pas complet dans Etape
	 * @param c
	 * @return
	 */
	public ServeurBuilder deployerServicesVue(Controleur c){
		
		new ServiceModele(c,"itineraire",this.serveur){
			protected Reponse getReponse(InputStream in){
				String itineraire = this.getControleur().getFeuilleDeRoute().getItineraireXML();
				return Reponse.succes(itineraire);
			}
		};
		
		new ServiceModele(c,"plan",this.serveur){
			protected Reponse getReponse(InputStream in){
				String plan = this.getControleur().getGrapheRoutier().getPlanXML();
				return Reponse.succes(plan);
			}  
		};
		
		new ServiceModele(c,"livraisons",this.serveur){
			protected Reponse getReponse(InputStream in){
				String livraisons = this.getControleur().getFeuilleDeRoute().getLivraisonsXML();
				return Reponse.succes(livraisons);
			}  
		};
		
		return this;
	}
}
