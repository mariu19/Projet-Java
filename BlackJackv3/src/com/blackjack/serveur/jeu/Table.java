package com.blackjack.serveur.jeu;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.blackjack.client.callback.IcallbackClient;

public class Table extends UnicastRemoteObject {
	
	private String nomTable;
	private int tailleTable;
	private ConcurrentHashMap<String, Joueur> listeJoueur = null;
	private ConcurrentHashMap<String, Joueur> listedAttente = null;
	ConcurrentHashMap<String, IcallbackClient> listeEnregistrementClient = null;
	private int numeroTable, choixProprietaire;
	private boolean partieDebute, partieFinie, bChoixProprietaire;
	private Croupier croupier;
	private String typeTable;

	
	public Table(int numeroTable, String nomTable, int tailleTable, String typeTable, boolean bChoixProprietaire, int choixProprietaire) throws RemoteException{
		super();
		this.nomTable = nomTable;
		this.tailleTable = tailleTable;
		this.listeJoueur = new ConcurrentHashMap<String, Joueur>();
		this.listedAttente = new ConcurrentHashMap<String, Joueur>();
		this.listeEnregistrementClient = new ConcurrentHashMap<String, IcallbackClient>();
		this.numeroTable = numeroTable;
		this.partieDebute = false;
		this.croupier = new Croupier();
		this.typeTable = typeTable;
		this.partieFinie = false;
		this.bChoixProprietaire = bChoixProprietaire;
		this.choixProprietaire = choixProprietaire;
	}
	
	public void initPartie2() {
		while (listeJoueur.size() == 0) {
			
		}
		
		System.out.println("Debut partie");
	}
	
	
	public String getTypeTable() {
		return typeTable;
	}



	public int getNumeroTable() {
		return numeroTable;
	}


	public void setNumeroTable(int numeroTable) {
		this.numeroTable = numeroTable;
	}

	
	
	public ConcurrentHashMap<String, Joueur> getListeJoueur() {
		return listeJoueur;
	}
	
	public int getTotalJoueur() {
		return this.getListeJoueur().size()+this.getListedAttente().size();
	}

	
	public ConcurrentHashMap<String, IcallbackClient> getListeEnregistrementClient() {
		return listeEnregistrementClient;
	}


	public int getTailleTable() {
		return tailleTable;
	}
	
	public ConcurrentHashMap<String, Joueur> getListedAttente() {
		return listedAttente;
	}


	@Override
	public String toString() {
		String liste="";
		String listeAttente="";
		for (String key : this.getListeJoueur().keySet()) {
			liste = liste+key+" | ";
		}
		for (String key : this.getListedAttente().keySet()) {
			listeAttente = listeAttente+key+" | ";
		}
		
		return "Table "+ numeroTable + " || Créateur : " + nomTable + " || Taille: " + tailleTable
				+ " || Liste des joueurs: " + liste +"|| Liste joueurs en attente: "+ listeAttente;
	}


	public void ajouterJoueur(Joueur j, IcallbackClient enregistrementClient) throws RemoteException {
		listeEnregistrementClient.put(j.getNom(), enregistrementClient);
		
		if (partieDebute) {
			listedAttente.put(j.getNom(), j);
			System.out.println(j.getNom()+" vient de rejoindre la table sur liste d'attente "+this.toString());
			System.out.println(this.toString());
			try {
				this.listeEnregistrementClient.get(j.getNom()).notificationRejointPartie("La partie est entamé, vous êtes en file d'attente: "+this.toString());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			listeJoueur.put(j.getNom(),j);
			System.out.println(j.getNom()+" vient de rejoindre la table "+this.toString());
			System.out.println(this.toString());
			this.listeEnregistrementClient.get(j.getNom()).notificationRejointPartie("Vous avez rejoint la table suivante: "+this.toString()+"\nEn attente des autres joueurs");
			
			//Notification de l'arrivée du joueur à tous les autres joueurs de la table
			for (String key : this.getListeEnregistrementClient().keySet()) {
				
				if (this.getListeJoueur().get(j.getNom()) != this.getListeJoueur().get(key)) {
					
					try {
						this.getListeEnregistrementClient().get(key).notificationRejointPartie(j.getNom()+" a rejoint la table");
						
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					}
					
				}
				
			}
			
		}
		
	}
	
	public void viderTable() {
		
		for (String key : this.getListeJoueur().keySet()) {
			listeEnregistrementClient.remove(key);
			listeJoueur.remove(key);
		}
	}
	
	
	
	public void retirerJoueur(String nom) {
		listeEnregistrementClient.remove(nom);
		listeJoueur.remove(nom);
	}
	
	
	public boolean isPartieDebute() {
		return partieDebute;
	}
	

	public String getNomTable() {
		return nomTable;
	}



	public void setPartieDebute(boolean partieDebute) {
		this.partieDebute = partieDebute;
	}
	
	public void initPartie() throws RemoteException{
		this.partieFinie = false;
		
		for(int i=15; i>=0;i--) {
			
			try {
				TimeUnit.SECONDS.sleep(1);
				for (String key : this.listeJoueur.keySet()) {
					
					this.getListeEnregistrementClient().get(key).notificationRejointPartie("Nouvelle partie dans "+i);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		

		
		System.out.println("\nDébut partie ");	
		this.setPartieDebute(true);
		croupier.distribuer(this.listeJoueur, this.listeEnregistrementClient);
		
		//Phase hit or stand
		for (String key : listeJoueur.keySet()) {
			int choix = 1;
			for (String key2 : listeJoueur.keySet()) {
				listeEnregistrementClient.get(key2).notificationRejointPartie("Tour de "+listeJoueur.get(key).getNom());
			}
			while(listeJoueur.get(key).getMain().getscore() < 21 && choix == 1) {
				choix = listeEnregistrementClient.get(key).hit_or_stand(listeJoueur.get(key).getMain().toString());	
				if(choix == 1) {
					croupier.pioche(listeJoueur.get(key), listeEnregistrementClient.get(key));
				}
			this.listeEnregistrementClient.get(key).notificationRejointPartie(listeJoueur.get(key).getMain().toString());
			}
			
			for (String key2 : listeJoueur.keySet()) {
				
				if (listeJoueur.get(key) != listeJoueur.get(key2)) {
					listeEnregistrementClient.get(key2).notificationRejointPartie("Fin du tour pour "+key+", sa main : "+this.getListeJoueur().get(key).getMain().toString());;
				}
				
			}
		}
		croupier.croupierPioche(listeJoueur, listeEnregistrementClient);
		for (String key : listeJoueur.keySet()) {
			croupier.calculResultat(listeJoueur.get(key), listeEnregistrementClient.get(key));
		}
		
		this.setPartieDebute(false);
		
		for (String key : listeJoueur.keySet()) {
			listeJoueur.get(key).getMain().clearMains();
			croupier.getMain().clearMains();
		}
		
		for (String key : this.listedAttente.keySet()) {
			Joueur j = this.listedAttente.get(key);
			this.listeJoueur.put(key, j);
			this.listedAttente.remove(key);
		}
		
		if(this.typeTable == "T"){
			this.setChoixProprietaire(false);
		}
		
		if(this.typeTable == "P"){
			for (String key : this.listeEnregistrementClient.keySet()) {
				
				for (String key3 : this.listeEnregistrementClient.keySet()) {
					this.listeEnregistrementClient.get(key3).notificationRejointPartie("En attente choix joueur "+key);
					}
				int choix = this.listeEnregistrementClient.get(key).continuerPartie();
				this.listeEnregistrementClient.get(key).setChoixJoueur(choix);
				if (choix == 2) {
					this.retirerJoueur(key);
				}
			}
			if(this.listeEnregistrementClient.size() > 0) {
				this.initPartie();
			}
		}
		this.partieFinie = true;
	}

	public boolean isPartieFinie() {
		return partieFinie;
	}

	public void setPartieFinie(boolean partieFinie) {
		this.partieFinie = partieFinie;
	}

	public boolean isChoixProprietaire() {
		return bChoixProprietaire;
	}

	public void setChoixProprietaire(boolean choixProprietaire) {
		this.bChoixProprietaire = choixProprietaire;
	}

	public int getChoixProprietaire() {
		return choixProprietaire;
	}

	public void setChoixProprietaire(int choixProprietaire) {
		this.choixProprietaire = choixProprietaire;
	}
	
	public void gestionFinPartie() {
		
	}

	public void setTypeTable(String typeTable) {
		this.typeTable = typeTable;
	}
	
	
	
	
	
	
}
