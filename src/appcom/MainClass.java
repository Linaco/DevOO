package appcom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class MainClass {

	public static void main(String[] args) {
		
		ServeurLivraison serveur = new ServeurLivraison();
		int port = serveur.Demarrer();
		
		ServiceControleur serviceChargerPlan = new ServiceControleur(null,"charger-plan",serveur){
			protected String getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    //factory.setNamespaceAware(true);
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						Document doc = builder.parse(in);
						// appel au service
						return doc.toString();
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/*String planXml = "";
				String buf;
				try{
					while( (buf = in.readLine()) != null){
						planXml += buf;
					}
				} catch (IOException e){
					e.printStackTrace();
				}
				return planXml;*/
				return null;
			}
		};
		
	    
		
		
	}

}
