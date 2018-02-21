package com.blackjack.serveur.jeu;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import com.blackjack.client.callback.IcallbackClient;
import com.blackjack.serveur.connexion.IconnexionServer;

public class Casino extends UnicastRemoteObject implements Icasino {
	
	private ArrayList<Table> listeTable;
	//private ArrayList<Joueur> listeJoueur = null;
	private Hashtable<String, Joueur> listeJoueur = null;
	private Hashtable<String, IcallbackClient> listeEnregistrementClient = null;
	
	public Casino() throws RemoteException{
		super();
		this.listeJoueur = new Hashtable<String, Joueur>();
		this.listeEnregistrementClient = new Hashtable<String, IcallbackClient>();
		this.listeTable = new ArrayList<Table>();
		this.listeTable.add(new Table(listeTable.size()+1,"Casino",6,"P"));
		this.listeTable.add(new Table(listeTable.size()+1,"Casino",6,"P"));
		this.listeTable.add(new Table(listeTable.size()+1,"Casino",6,"P"));
	}
	
	public void ajouterJoueurCasino(String nom, IcallbackClient enregistrementClient) {
		listeJoueur.put(nom, new Joueur(nom));
		listeEnregistrementClient.put(nom, enregistrementClient);
	}

	@Override
	public void creerTable(String nom, int taille) throws RemoteException {
		// TODO Auto-generated method stub
		Table t;
		listeTable.add(t = new Table(listeTable.size()+1,nom , taille,"T"));
		System.out.println(nom+" vient de créer une table d'une taille de "+taille+" joueurs");
		this.listeEnregistrementClient.get(nom).notificationRejointPartie("Table créee : "+t.toString());
		t.ajouterJoueur(listeJoueur.get(nom), this.listeEnregistrementClient.get(nom)); 
		this.debutPartie(t);
		//t.initPartie();
		
	} 

	public ArrayList<Table> getListeTable() {
		return listeTable;
	}


	public Hashtable<String, Joueur> getListeJoueur() {
		return listeJoueur;
	}

	@Override
	public String afficherTable(){
		// TODO Auto-generated method stub
		String affichageListeTable = "";
		int i =1;
		
		for (Table table : this.getListeTable()) {
				affichageListeTable = affichageListeTable+"\n"+i+" "+table.toString();
				i++;
		}
		
		return affichageListeTable;
	}
	
	public void rejoindreTable(String nom, int i) throws RemoteException {
		// TODO Auto-generated method stub

		try {
			this.listeTable.get(i).ajouterJoueur(listeJoueur.get(nom), this.listeEnregistrementClient.get(nom));
			if((this.listeTable.get(i).getTypeTable() == "P") && (this.listeTable.get(i).getTotalJoueur() == 1)) {
				//this.listeTable.get(i).initPartie();
				this.debutPartie(this.listeTable.get(i));
			}
			
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
	}
	
	public String verifPlaceTable(String nom, int i) {
		
		try{
			if (listeTable.get(i).getTotalJoueur() == listeTable.get(i).getTailleTable()){
				return null;
		}
			
		}catch(IndexOutOfBoundsException  e){	
			return null;
		}
	
		return this.listeTable.get(i).toString();
	}
	
	public void debutPartie (Table t) throws RemoteException{
		t.initPartie();
		this.finPartie(t);
	}
	
	public void finPartie (Table t) throws RemoteException {
		//Si table créee par joueur, on demande au joueur si il veut continuer, on notifie les autres, sinon on appel détruire table
		Hashtable<String, IcallbackClient> listeEnregistrementJoueurTable = t.getListeEnregistrementClient();
		
		if(t.getTypeTable() == "T") {
			
			for (String key : listeEnregistrementJoueurTable.keySet()) {
				listeEnregistrementJoueurTable.get(key).notificationRejointPartie("Partie terminé, en attente du choix du créateur");
			}
			
			int choix = this.listeEnregistrementClient.get(t.getNomTable()).continuerPartie();
			
			if(choix == 2) {
				this.detruireTable(t);
				for (String key : listeEnregistrementJoueurTable.keySet()) {
					listeEnregistrementJoueurTable.get(key).notificationRejointPartie("Le créateur de la partie a supprimé sa table. Redirection vers le menu");
				}

			}
			else {
				this.debutPartie(t);
			}
			
		}
		
	}
	
	public void detruireTable(Table t) {
		System.out.println(t.getNomTable()+ " a détruit sa table: "+t.toString());
		this.listeTable.remove(t.getNumeroTable()-1);
		System.out.println(this.afficherTable());
	}

}
