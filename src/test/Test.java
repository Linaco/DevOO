package test;
import controleur.Controleur;

public class Test {
	public static void main(String[] args){
		System.out.println("--------- Test ---------");
		System.out.println("Test parsage plan.xml");
		Controleur contJav = new Controleur();
		String path = "C:/Users/bastien/Desktop/DevOO/xml/plan10x10.xml";
		if(contJav.chargerPlan(path)){
			System.out.println("Génération ok");
			System.out.println(contJav.getGrapheRoutier().toString());
		}else{
			System.out.println("caca");
		}
		
		
	}

}
