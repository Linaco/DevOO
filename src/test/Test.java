package test;
import modele.*;
import controleur.Controleur;

public class Test {
	public static void main(String[] args){
		System.out.println("--------- Test ---------");
		System.out.println("Test parsage plan.xml");
		Controleur contJav = new Controleur();
		String pathPlan = "C:/Users/bastien/Desktop/DevOO/xml/plan10x10.xml";
		if(contJav.chargerPlan(pathPlan)){
			System.out.println("G�n�ration ok");
		}else{
			System.out.println("caca");
		}
		System.out.println("Test parsage liv.xml");
		String pathLiv = "C:/Users/bastien/Desktop/DevOO/xml/livraison10x10-2.xml";
		if(contJav.chargerLivraisons(pathLiv)){
			System.out.println("G�n�ration liv ok ");
		}else{
			System.out.println("pipi");
		
		}
		
		
	}

	public static void displayIntersection(Intersection item){
		
	}
	
	public static void displayRoute(Route item){
		System.out.println(item);
		
	}
	
	public static void displayLivraison(Livraison item){
		
	}

}
