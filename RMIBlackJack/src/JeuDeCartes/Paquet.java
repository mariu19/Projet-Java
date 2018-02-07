package JeuDeCartes;

import java.util.ArrayList;
import java.util.Collections;

public class Paquet {
	
	private ArrayList<Carte> paquet = new ArrayList<Carte>();
	public Paquet() {
		for (Couleur couleur : Couleur.values())
            for (Nom nom : Nom.values())
                paquet.add(new Carte(couleur, nom));
	}
	
	public void melanger() {
		Collections.shuffle(paquet);
	}
	
	
	public Carte distribuer(Main main){
		if (paquet.size()==0){
			System.out.println("Paquet vide");
			return null;
		}else{
			Carte carte = (Carte) paquet.get(paquet.size()-1);
			paquet.remove(paquet.size()-1);
			main.ajouterCarte(carte);
			System.out.println("Carte piochée \n");
			return carte;
		}
	}

	
	public String toString() {
		return "Taille "+paquet.size()+ "||  Paquet [listeCarte=" + paquet + "]";
	}

}
