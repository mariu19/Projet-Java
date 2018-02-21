package com.blackjack.serveur.jeu;

import java.util.ArrayList;

import com.blackjack.serveur.jeuDeCartes.Carte;
import com.blackjack.serveur.jeuDeCartes.Nom;

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
		Carte carteAs = null;
		
		/*for (Carte carte : hand ) {
			if(score > 21 && carte.getNom() == Nom.As) {
				carte.setValeur(1);
			}
			score += carte.getValeur();
			if (carte.getNom() == Nom.As) {
				as = true;
				carteAs = carte;
			}
		}*/
		for (Carte carte : hand ) {
			score += carte.getValeur();
			if (carte.getNom() == Nom.As) {
				as = true;
				carteAs = carte;
			}
		}
		if (score > 21 && carteAs!=null) {
			score = 0;
			carteAs.setValeur(1);
			for (Carte carte: hand) {
			score += carte.getValeur();
			}
		}

					
		/*if (hand.size() == 2 && score == 21) {
			for (Carte carte : hand ) {
				if(carte.getNom() == Nom.As)
					this.blackjack = true;
			}
		}*/
		
		return score;
	}
	public int getTailleMain() {
		return hand.size();
	}
	
	public void clearMains() {
		this.hand.clear();
	}
	
	@Override
	public String toString() {
		String main = "Main: ";
		int score = this.getscore();
		return "Main=" + hand + " score :"+score+  "]";
	}
	

}
