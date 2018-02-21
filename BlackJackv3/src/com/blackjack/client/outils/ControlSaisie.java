package com.blackjack.client.outils;

public class ControlSaisie {
	
	
	public boolean controleEntierMenu(String s) {
		int i;
		try {
			 i = Integer.parseInt(s);
			
			if((i != 1) && (i != 2)) {
				System.out.println("Erreur de saisie\nPour creer une table appuyez sur 1\nPour rejoindre une table appuyez sur 2");
				return false;
			}
			
		} catch (NumberFormatException e) {
			System.out.println("Erreur de saisie veuillez entrer un entier\nPour creer une table appuyez sur 1\nPour rejoindre une table appuyez sur 2");
			return false;
		}
		
		return true;
	}
	
	public boolean controleTailleTable(String s) {
		int i;
		try {
			 i = Integer.parseInt(s);
			
			if((i < 1) || (i > 6)) {
				System.out.println("Erreur de saisie\nSaisir un entier entre 1 et 6");
				return false;
			}
			
		} catch (NumberFormatException e) {
			System.out.println("Erreur de saisie veuillez entrer un entier entre 1 et 6");
			return false;
		}
		
		return true;
	}

	public boolean controleSaisie(String saisie) {
		int i;
		try {
			 i = Integer.parseInt(saisie);
			 
		} catch (NumberFormatException e) {
			System.out.println("Erreur de saisie veuillez entrer un entier");
			return false;
		}
		
		return true;
	}
	
	public boolean controleHitStand(String s) {
		int i;
		try {
			 i = Integer.parseInt(s);
			
			if((i != 1) && (i != 2)) {
				System.out.println("Erreur de saisie\nPour piocher une carte supplémentaire appuyez sur 1\nSinon appuyez sur 2");
				return false;
			}
			
		} catch (NumberFormatException e) {
			System.out.println("Erreur de saisie veuillez entrer un entier\nPour piocher une carte supplémentaire appuyez sur 1\\nSinon appuyez sur 2");
			return false;
		}
		
		return true;
	}
	
	public boolean controleContinuerPartie(String s) {
		int i;
		try {
			 i = Integer.parseInt(s);
			
			if((i != 1) && (i != 2)) {
				System.out.println("Erreur de saisie\nPour pour demarrer une nouvelle partie appuyez sur 1\nSinon appuyez sur 2");
				return false;
			}
			
		} catch (NumberFormatException e) {
			System.out.println("Erreur de saisie veuillez entrer un entier\nPour pour demarrer une nouvelle partie appuyez sur 1\\nSinon appuyez sur 2");
			return false;
		}
		
		return true;
	}

}
