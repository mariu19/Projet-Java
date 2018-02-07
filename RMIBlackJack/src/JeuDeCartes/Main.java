package JeuDeCartes;

import java.util.ArrayList;

public class Main {
	
	private ArrayList<Carte> hand = new ArrayList<Carte>();
	private int valeur;
	private boolean blackjack;
	
	
	public Main() {
		this.blackjack = false;
	}
	
	
	//Méthode d'ajout de carte à la main
	public void ajouterCarte (Carte carte) {
		hand.add(carte);
	}
	
	
	//Méthode pour compter les points de la main
	public int getscore() {
		int score = 0;
		int scorepond = 0;
		boolean as = false;
		
		for (Carte carte : hand ) {
			if(score > 21 && carte.getNom() == Nom.As) {
				carte.setValeur(1);
			}
			score += carte.getValeur();
			if (carte.getNom() == Nom.As) {
				as = true;
			}
		}
		
			if ((score > 21) && (as)) {
				score = score-10;
			}		
		
		if (hand.size() == 2 && score == 21) {
			for (Carte carte : hand ) {
				if(carte.getNom() == Nom.As)
					this.blackjack = true;
			}
		}
		
		return score;
	}


	@Override
	public String toString() {
		return "Main [hand=" + hand + " score :"+this.getscore()+  "]";
	}
	
	

}
