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
		t.ajouterJoueur(listeJoueur.get(nom), this.listeEnregistrementClient.get(nom)); 
		System.out.println(nom+" vient de cr�er une table d'une taille de "+taille+" joueurs");
		this.listeEnregistrementClient.get(nom).notificationRejointPartie("Table cr�ee : "+t.toString());
		t.initPartie();
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
			this.listeEnregistrementClient.get(nom).notificationRejointPartie("Vous avez rejoint la table suivante: "+this.listeTable.get(i).toString());
			if((this.listeTable.get(i).getTypeTable() == "P") && (this.listeTable.get(i).getTotalJoueur() == 1)) {
				this.listeTable.get(i).initPartie();
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
	
	public void debutPartie (int i) throws RemoteException{
		listeTable.get(i).initPartie();
	}
}