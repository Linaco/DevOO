package modele;

import java.util.*;
import modele.GrapheRoutier.*;

import tsp.TSP;

/**
 * 
 */
public class GrapheLivraison implements Graph {

	int maxArcCost;
	int minArcCost;
	int nbSommets;
	int[][] matriceAdjacence;
	List<Livraison> livraisons;
	
	/**
	 * 
	 * @param matriceAdjacence Matrice contenant les couts des plus courts chemins entre chaque livraison. Le dépôt doit être à l'indice 0.
	 * @param lLivraisons Liste des livraisons pour la tournée à calculer, ordonnée selon le même ordre que les colonnes de la matrice d'adjacence.
	 */
	public GrapheLivraison(int[][] matriceAdjacence, List<Livraison> lLivraisons){
		this.nbSommets = matriceAdjacence[0].length;
		this.matriceAdjacence=matriceAdjacence;
		
		int coutMinimum = Integer.MAX_VALUE;
		int coutMaximum = Integer.MIN_VALUE;
		int coutCourant;
		
		for(int i=0; i<nbSommets;i++){
			for(int j=0;j<nbSommets;j++){
				coutCourant=matriceAdjacence[i][j];
				if(coutCourant<coutMinimum && coutCourant!= -1 ){
					coutMinimum=coutCourant;
				}
				if(coutCourant>coutMaximum){
					coutMaximum=coutCourant;
				}
			}
		}
		livraisons = lLivraisons;
	}

<<<<<<< HEAD
	private List<Livraison> listeLivraison;
	private Intersection entrepot;
	private String heureDebut,heureFin;
	private int id,idClient,adresse;
    
    public GrapheLivraison(Intersection entrepot) {
    	listeLivraison = = new ArrayList<Livraison>();
    	this.entrepot = entrepot;
    }

    public void calculerOrdreLivraisons() {
        // TODO implement here
    }
    
    public void AjouterLivraison(Livraison livraison) {
    	this.listeLivraison.add(livraison.getId(),livraison);
    }
    
    public void SupprimerLivraison(int idLivraison) {
    	this.listeLivraison.add(idLivraison, null);
    }
=======
	/**
	 * 
	 * @return La list ordonnée des livraisons si la tournée spécifiée est réalisable, null sinon
	 */
    public List<Livraison> calculerOrdreLivraisons() {
    	List<Livraison> lListe = new ArrayList<>();
    	int[] solution;
    	TSP resolveur = new TSP(this);
    	resolveur.solve(Integer.MAX_VALUE, Integer.MAX_VALUE);
    	try{
    		solution = resolveur.getNext();
    	}
    	catch (NullPointerException e){
    		return null;
    	}
    	
    	int livraisonCourante = solution[0];
    	while(livraisonCourante != 0){
    		lListe.add(livraisons.get(livraisonCourante));
    		livraisonCourante=solution[livraisonCourante];
    	}
    	
    	return lListe;
    }



	@Override
	public int getMaxArcCost() {
		return maxArcCost;
	}

	@Override
	public int getMinArcCost() {
		return minArcCost;
	}

	@Override
	public int getNbVertices() {
		return nbSommets;
	}

	@Override
	public int[][] getCost() {
		int[][] retour = matriceAdjacence;
		for(int i=0;i<nbSommets;i++){
			for(int j=0;j<nbSommets;j++){
				if(retour[i][j]==-1) retour[i][j]=maxArcCost+1;
			}
		}
		return retour;
	}

	@Override
	public int[] getSucc(int i) throws ArrayIndexOutOfBoundsException {
		List<Integer> liste = new ArrayList<>();
		for(int j=0;j<nbSommets;j++){
			if(matriceAdjacence[i][j]!=-1) liste.add(j);
		}
		int[] retour = new int[liste.size()];
		int k=0;
		for(Integer entier : liste){
			retour[k++]=entier;
		}
		return retour;
	}

	@Override
	public int getNbSucc(int i) throws ArrayIndexOutOfBoundsException {
		List<Integer> liste = new ArrayList<>();
		for(int j=0;j<nbSommets;j++){
			if(matriceAdjacence[i][j]!=-1) liste.add(j);
		}
		return liste.size();
	}
>>>>>>> 9665d5aa406cb27ff1de3eccc7e0f79b6e64d349

    
}