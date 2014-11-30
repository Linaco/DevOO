package test;
import controleur.Controleur;

public class Test {
	public static void main(String[] args){
		System.out.println("--------- Test ---------");
		System.out.println("Test parsage plan.xml");
		Controleur contJav = new Controleur();
		String pathPlan = "C:/Users/bastien/Desktop/DevOO/xml/plan10x10.xml";
		if(contJav.chargerPlan(pathPlan)){
			System.out.println("Génération ok");
		}else{
			System.out.println("caca");
		}
		System.out.println("Test parsage liv.xml");
		String pathLiv = "C:/Users/bastien/Desktop/DevOO/xml/livraison10x10-2.xml";
		if(contJav.chargerLivraisons(pathLiv)){
			System.out.println("Génération liv ok ");
		}else{
			System.out.println("pipi");
		
		}
		
		
	}

}
