package test;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import modele.*;

public class Test {
	public static void main(String[] args){
		Intersection inter = new Intersection(1,0,2);
		if(ConstrucIntersection(1,0,2)){
			System.out.println("V - Création d'une intersection");
			if(ConstrucRoute("h1",12,12,inter)){
				System.out.println("V - Route à partir de Intersec marche");
				Route route = new Route("h1",12,12,inter);
				if(addTroncIntersection(inter,route)){
					System.out.println("V - Ajout d'un tronc sortant");
				} else {
					System.out.println("X - Ajout d'un tronc sortant");
				}
			} else {
				System.out.println("X - Route à partir de Intersec marche");
			}
			 DateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm:ss");
			 Date dateBis = new Date();
			 Date date = new Date();
			 try{
				 date = HOUR_FORMAT.parse("9:0:0");
				 dateBis = HOUR_FORMAT.parse("8:0:0");
			 }catch(Exception e){
				 e.printStackTrace();
			 }
			 
			
			if(ConstrucEtape(dateBis,inter)){
				System.out.println("V - Création étape");
				Etape etape = new Etape(dateBis, inter);
				etape.setHeurePassagePrevue(date);
				
				
				if(etape.getHeurePassagePrevue()==date){
					System.out.println("V - Etape - Modification heure");
				} else {
					System.out.println("X - Etape - Modification heure");
				}
			} else {
				System.out.println("X - Création étape");
			}
			
		} else {
			System.out.println("X - Création d'une intersection");
		}
		
	}

	public static boolean ConstrucIntersection(int id, int x, int y){
		Intersection inter = new Intersection(id,x,y);
		if( (x==inter.getX()) && (y==inter.getY()) && (id==inter.getId())){
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean ConstrucRoute(String nom, double vitesse, double longueur, Intersection inter1  ){
		Route route = new Route(nom, vitesse, longueur, inter1);
		if ( (nom == route.getName()) &&
				(vitesse== route.getVitesse()) &&
				(longueur==route.getLongueur()) &&
				(inter1 == route.getInter())
				){
			return true;
		} else {
			return false;
		}
	}
	
	
	public static boolean addTroncIntersection(Intersection inter, Route route){
		inter.addTroncSortant(route);
		if( (inter.getTroncsSortants().size() == 1) &&
				(inter.getTroncsSortants().get(0) == route)
				){
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean ConstrucEtape(Date heurePassage, Intersection adresse){
		Etape etape = new Etape(heurePassage, adresse);
		if( (heurePassage == etape.getHeurePassagePrevue()) &&
				(adresse == etape.getAdresse())
				){
			return true;
		} else {
			return false;
		}
	}
	
	
	
	
	public static void displayRoute(Route item){
		System.out.println(item);
		
	}
	
	public static void displayLivraison(Livraison item){
		
	}

}
