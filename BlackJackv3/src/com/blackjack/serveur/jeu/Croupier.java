package com.blackjack.serveur.jeu;

import java.rmi.RemoteException;
import java.util.Hashtable;

import com.blackjack.client.callback.IcallbackClient;
import com.blackjack.serveur.jeuDeCartes.Carte;
import com.blackjack.serveur.jeuDeCartes.Couleur;
import com.blackjack.serveur.jeuDeCartes.Nom;
import com.blackjack.serveur.jeuDeCartes.Paquet;

public class Croupier {
	
	String nom;
	Main main;
	boolean hit;
	boolean elimine;
	Paquet deck;
	
	public Croupier() {
		this.nom = "Croupier";
		this.main = new Main();
		this.hit = true;
		deck = new Paquet();
		deck.melanger();
		deck.melanger();
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
	
	public void distribuer(Hashtable<String, Joueur> listeJoueur, Hashtable<String, IcallbackClient> listeEnregistrementClient) throws RemoteException {
		
		for (String key : listeJoueur.keySet()) {
			Carte carte = deck.distribuer(listeJoueur.get(key).getMain());
			listeEnregistrementClient.get(key).notificationRejointPartie("Vous piochez "+ carte.toString());
			
			for (String key2 : listeJoueur.keySet()) {
				
				if (listeJoueur.get(key) != listeJoueur.get(key2)) {
					listeEnregistrementClient.get(key2).notificationRejointPartie("Le joueur "+key+" pioche "+carte.toString());
				}
				
			}
			
		}
		Carte carte = deck.distribuer(this.getMain());
		
		for (String key : listeJoueur.keySet()) {
			listeEnregistrementClient.get(key).notificationRejointPartie("Le croupier pioche la carte suivante :"+ carte.toString());
			
		}
		
		
		for (String key : listeJoueur.keySet()) {
			Carte carte2 = deck.distribuer(listeJoueur.get(key).getMain());
			listeEnregistrementClient.get(key).notificationRejointPartie("Vous piochez "+ carte2.toString());
			for (String key2 : listeJoueur.keySet()) {
				
				if (listeJoueur.get(key) != listeJoueur.get(key2)) {
					listeEnregistrementClient.get(key2).notificationRejointPartie("Le joueur "+key+" pioche "+carte2.toString());
				}
				
			}	
		}
		deck.distribuer(this.getMain());
		
		for (String key : listeJoueur.keySet()) {
			listeEnregistrementClient.get(key).notificationRejointPartie("Le croupier pioche une deuxième carte");	
		}
		
	}
	
	public void pioche(Joueur j, IcallbackClient affichageClient) throws RemoteException {
		//Decommenter Pour test valeur As Nom nomCarte = Nom.As;
		//Decommenter Pour test valeur As Couleur couleur = Couleur.Carreau;
		//Decommenter Pour test valeur As Carte CartaAs = new Carte(couleur, nomCarte);
		//Decommenter Pour test valeur As j.getMain().ajouterCarte(CartaAs);
		Carte carte = deck.distribuer(j.getMain());
		affichageClient.notificationRejointPartie("Vous piochez "+ carte.toString());
	}
	
	public void croupierPioche(Hashtable<String, Joueur> listeJoueur, Hashtable<String, IcallbackClient> listeEnregistrementClient) throws RemoteException {
		for (String key : listeJoueur.keySet()) {
			listeEnregistrementClient.get(key).notificationRejointPartie("Main du croupier :"+ this.getMain().toString());
		}
		
		while (this.getMain().getscore() < 16) {
			Carte carte = deck.distribuer(this.getMain());
			for (String key : listeJoueur.keySet()) {
				listeEnregistrementClient.get(key).notificationRejointPartie("Le croupier pioche la carte suivante :"+ carte.toString());
			}
		}
		for (String key : listeJoueur.keySet()) {
			listeEnregistrementClient.get(key).notificationRejointPartie("Fin du tour du croupier\nMain du croupier :"+ this.getMain().toString());
		}
	}
	
	public void calculResultat(Joueur j, IcallbackClient affichageClient) throws RemoteException {
		if (this.getMain().getscore() > 21) {
				//Joueur gagne
			affichageClient.notificationRejointPartie("Vous l'emportez");
		}
		else if ((j.getMain().getscore() > this.getMain().getscore()) && (j.getMain().getscore()<=21)) {
				//Joueur gagne
			affichageClient.notificationRejointPartie("Vous l'emportez");
		}
		else if((j.getMain().getscore() > this.getMain().getscore()) && (j.getMain().getscore()>21)) {
			//Croupier gagne
			affichageClient.notificationRejointPartie("Vous perdez");
		}
		else if (j.getMain().getscore() < this.getMain().getscore()) {
				//Croupier gagne
			affichageClient.notificationRejointPartie("Vous perdez");
		}
		else if (j.getMain().getscore() == this.getMain().getscore()) {
			if (j.getMain().getTailleMain() < this.getMain().getTailleMain()) {
				//Joueur gagne
				affichageClient.notificationRejointPartie("Vous l'emportez");
			}
			else if(j.getMain().getTailleMain() > this.getMain().getTailleMain()) {
				//Croupier gagne
				affichageClient.notificationRejointPartie("Vous perdez");
			}
			else {
				//Egalite
				affichageClient.notificationRejointPartie("Egalité");
			}
			
		}
		
	}
	
	
	@Override
	public String toString() {
		return nom;
	}

}
