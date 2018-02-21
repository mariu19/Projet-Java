package com.blackjack.serveur.jeu;

public class Joueur {
	
	String nom;
	Main main;
	boolean hit;
	boolean elimine;
	
	public Joueur(String nom) {
		this.nom = nom;
		this.main = new Main();
		this.hit = true;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Main getMain() {
		return main;
	}

	public void setMain(Main main) {
		this.main = main;
	}
	
	
	public boolean isHit() {
		
		if (this.main.getscore()>=21) {
			hit = false;
		}
		
		return hit;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}

	public boolean hit_stand(int choix) {
		
		if(this.hit = true) {
			
				if(choix == 2) {
					hit = false;
				}
			
		}
	
		return hit;
	}

	@Override
	public String toString() {
		return nom;
	}
	
	

}
