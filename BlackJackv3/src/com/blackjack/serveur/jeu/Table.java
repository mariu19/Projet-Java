package com.blackjack.serveur.jeu;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import com.blackjack.client.callback.IcallbackClient;

public class Table extends UnicastRemoteObject {
	
	private String nomTable;
	private int tailleTable;
	private int tailleTableMax;
	private int tailleTablemin;
	private Hashtable<String, Joueur> listeJoueur = null;
	private Hashtable<String, Joueur> listedAttente = null;
	private Hashtable<String, IcallbackClient> listeEnregistrementClient = null;
	private int numeroTable;
	private boolean partieDebute;
	private Croupier croupier;
	private String typeTable;

	
	public Table(int numeroTable, String nomTable, int tailleTable, String typeTable) throws RemoteException{
		super();
		this.nomTable = nomTable;
		this.tailleTable = tailleTable;
		this.tailleTableMax = 6;
		this.tailleTablemin = 1;
		this.listeJoueur = new Hashtable<String, Joueur>();
		this.listedAttente = new Hashtable<String, Joueur>();
		this.listeEnregistrementClient = new Hashtable<String, IcallbackClient>();
		this.numeroTable = numeroTable;
		this.partieDebute = false;
		this.croupier = new Croupier();
		this.typeTable = typeTable;
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

	
	
	public Hashtable<String, Joueur> getListeJoueur() {
		return listeJoueur;
	}
	
	public int getTotalJoueur() {
		return this.getListeJoueur().size()+this.getListedAttente().size();
	}

	
	public Hashtable<String, IcallbackClient> getListeEnregistrementClient() {
		return listeEnregistrementClient;
	}


	public int getTailleTable() {
		return tailleTable;
	}
	
	public Hashtable<String, Joueur> getListedAttente() {
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
		
		/*for (String key : this.listedAttente.keySet()) {
			Joueur j = this.listedAttente.get(key);
			this.listeJoueur.put(key, j);
			this.listedAttente.remove(key);
		}*/
		
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
		
		//Pour table créer par joueur
		if(this.getTypeTable() == "T") {
			continuerPartieTableTemp();
		}
		
		//Pour table permanente
		if(this.getTypeTable() == "P") {
			//Demande à chaque joueur si il veut continuer la partie
			for (String key : listeJoueur.keySet()) {
				int continuer = 0;
				continuer = this.listeEnregistrementClient.get(key).continuerPartie();
				if (continuer == 2) {
					listeEnregistrementClient.get(key).menu(listeJoueur.get(key).getNom());
					listeEnregistrementClient.remove(key);
					listeJoueur.remove(key);
				}
				
			}
			for (String key : listedAttente.keySet()) {
				int continuer = 0;
				continuer = this.listeEnregistrementClient.get(this.getNomTable()).continuerPartie();
				if (continuer == 2) {
					listeEnregistrementClient.get(key).menu(listeJoueur.get(key).getNom());
					listeEnregistrementClient.remove(key);
					listedAttente.remove(key);
				}
				else {
					this.listeJoueur.put(key, this.listedAttente.get(key));
					listedAttente.remove(key);
				}
			}
			continuerPartieTablePerm();
		}
	
	}
	
	public void continuerPartieTableTemp() throws RemoteException{
		int continuer = 0;
		
		for (String key : listeJoueur.keySet()) {
			this.listeEnregistrementClient.get(key).notificationRejointPartie("Demande au créateur si il veut continuer la partie en cours");
		}
		for (String key : listedAttente.keySet()) {
			this.listeEnregistrementClient.get(key).notificationRejointPartie("Demande au créateur si il veut continuer la partie en cours");
		}
		
		continuer = this.listeEnregistrementClient.get(this.getNomTable()).continuerPartie();
		
		if (continuer == 1) {
			for (String key : listeJoueur.keySet()) {
				if (key != this.getNomTable()) {
					listeEnregistrementClient.get(key).notificationRejointPartie("Une nouvelle partie va démarrer ");
					int continuer2 = 0;
					continuer2 = this.listeEnregistrementClient.get(this.getNomTable()).continuerPartie();
					if (continuer2 == 2) {
						listeEnregistrementClient.get(key).menu(listeJoueur.get(key).getNom());
						listeEnregistrementClient.remove(key);
						listeJoueur.remove(key);
					}
				}
				
			}
			for (String key : listedAttente.keySet()) {
				listeEnregistrementClient.get(key).notificationRejointPartie("Une nouvelle partie va démarrer ");
				if (key != this.getNomTable()) {
					listeEnregistrementClient.get(key).notificationRejointPartie("Une nouvelle partie va démarrer ");
					int continuer2 = 0;
					continuer2 = this.listeEnregistrementClient.get(this.getNomTable()).continuerPartie();
					if (continuer2 == 2) {
						listeEnregistrementClient.get(key).menu(listeJoueur.get(key).getNom());
						listeEnregistrementClient.remove(key);
						listedAttente.remove(key);
					}
				}
			}
			this.initPartie();
		}
		else {
			
			for (String key : listeJoueur.keySet()) {
				listeEnregistrementClient.get(key).notificationRejointPartie("Le créateur supprimme sa table, redirection vers le menu");
				listeEnregistrementClient.get(key).menu(listeJoueur.get(key).getNom());
				listeEnregistrementClient.remove(key);
				listeJoueur.remove(key);
			}
			for (String key : listedAttente.keySet()) {
				listeEnregistrementClient.get(key).notificationRejointPartie("Le créateur supprimme sa table, redirection vers le menu");
				listeEnregistrementClient.get(key).menu(listeJoueur.get(key).getNom());
				listedAttente.remove(key);
				listeEnregistrementClient.remove(key);
			}
		}
	}
	
	public void continuerPartieTablePerm() throws RemoteException {
		if (this.listeJoueur.size() > 0) {
			this.initPartie();
		}
	}
	
}
