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
		
		new ServiceControleur(c,"genererPlan",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						
						this.getControleur().chargerPlan(doc);
						
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
		
		new ServiceControleur(c,"genererLivraisons",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						//xml
						this.getControleur().chargerPlan(doc);
						
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
		
		new ServiceModele(c,"creerLivraison",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						//a changer
						this.getControleur().chargerPlan(doc);
						
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
		
		new ServiceModele(c,"undo",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						//a changer
						this.getControleur().chargerPlan(doc);
						
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
		
		new ServiceModele(c,"redo",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						//a changer
						this.getControleur().chargerPlan(doc);
						
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
		
		new ServiceModele(c,"calculFeuilleDeRoute",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						//a changer
						this.getControleur().chargerPlan(doc);
						
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
		
		return this;
	}
	
	
	/**
	 * Services pas complets memes raisons qu'en haut
	 * @param c
	 * @return
	 */
	public ServeurBuilder deployerServicesVue(Controleur c){
		
		new ServiceModele(c,"SelectionnerRoute",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						//route
						this.getControleur().chargerPlan(doc);
						
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
		
		new ServiceModele(c,"SelectionnerIntersection",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						//intersection
						this.getControleur().chargerPlan(doc);
						
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
		
		new ServiceModele(c,"creerGrapheRoutier",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						//list <intersection>
						this.getControleur().chargerPlan(doc);
						
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
		
		new ServiceModele(c,"selectionnerLivraison",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						//livraison
						this.getControleur().chargerPlan(doc);
						
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
		
		new ServiceModele(c,"creerGrapheLivraisons",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						//list <livraison>
						this.getControleur().chargerPlan(doc);
						
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
		
		
		return this;
	}
}
