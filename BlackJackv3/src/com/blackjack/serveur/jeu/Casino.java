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
		this.listeTable.add(new Table(listeTable.size()+1,"Casino1",6,"P",true,1));
		this.listeTable.add(new Table(listeTable.size()+1,"Casino2",6,"P",true,1));
		this.listeTable.add(new Table(listeTable.size()+1,"Casino3",6,"P",true,1));
	}
	
	public void ajouterJoueurCasino(String nom, IcallbackClient enregistrementClient) {
		listeJoueur.put(nom, new Joueur(nom));
		listeEnregistrementClient.put(nom, enregistrementClient);
	}

	@Override
	public void creerTable(String nom, int taille) throws RemoteException {
		// TODO Auto-generated method stub
		Table t;
		listeTable.add(t = new Table(listeTable.size()+1,nom , taille,"T",false,0));
		System.out.println(nom+" vient de cr�er une table d'une taille de "+taille+" joueurs");
		this.listeEnregistrementClient.get(nom).notificationRejointPartie("Table cr�ee : "+t.toString());
		t.ajouterJoueur(listeJoueur.get(nom), this.listeEnregistrementClient.get(nom)); 
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
	
	public void debutPartie (String nj) throws RemoteException{
		
		for (int i=0; i<this.listeTable.size();i++) {
			for (String key : this.listeTable.get(i).getListeJoueur().keySet()) {
				if(key.equals(nj)) {
					this.listeTable.get(i).initPartie();
				}
			}
			
			
		}
		
	}
	
	/*public int finPartie (Table t) throws RemoteException {
		//Si table cr�ee par joueur, on demande au joueur si il veut continuer, on notifie les autres, sinon on appel d�truire table
		Hashtable<String, IcallbackClient> listeEnregistrementJoueurTable = t.getListeEnregistrementClient();
		
		if(t.getTypeTable() == "T") {
			
			for (String key : listeEnregistrementJoueurTable.keySet()) {
				listeEnregistrementJoueurTable.get(key).notificationRejointPartie("Partie termin�, en attente du choix du cr�ateur");
			}
			
			int choix = this.listeEnregistrementClient.get(t.getNomTable()).continuerPartie();
			
			if(choix == 2) {
				this.detruireTable(t);
				for (String key : listeEnregistrementJoueurTable.keySet()) {
					listeEnregistrementJoueurTable.get(key).notificationRejointPartie("Le cr�ateur de la partie a supprim� sa table. Redirection vers le menu");
				}
				return 0;
			}
			else {
				this.debutPartie(t);
			}
			
		}
	System.out.println("Fin fin partie");	
	return 0;
	
	}*/
	
	public void detruireTable(String nomTable) throws RemoteException {
		Table t = null;
		int numeroTable=0;
		for (int i = 0; i < listeTable.size(); i++) {
			if (listeTable.get(i).getNomTable().equals(nomTable)) {
				t = listeTable.get(i);
				numeroTable = t.getNumeroTable();
			}
		}
		this.listeTable.remove(numeroTable-1);
		System.out.println(this.afficherTable());
	}

	@Override
	public void quitterTable(String nom) throws RemoteException {
		// TODO Auto-generated method stub
		Table t = null;
		for (int i=0; i<this.listeTable.size();i++) {
			for (String key : this.listeTable.get(i).getListeJoueur().keySet()) {
				if(key.equals(nom)) {
					t = this.listeTable.get(i);
				}
			}
		}
		t.retirerJoueur(nom);
		System.out.println(nom+" a quitt� la table "+t.getNomTable());
	}
	
	public void initCasino() throws RemoteException {
		for (int i =0; i<listeTable.size();i++) {
			listeTable.get(i).initPartie();
		}
	}

	@Override
	public boolean isFinPartie(String n) throws RemoteException {
		boolean finPartie = false;
		for (int i=0; i<this.listeTable.size();i++) {
			for (String key : this.listeTable.get(i).getListeEnregistrementClient().keySet()) {
				if(key.equals(n)) {
					 finPartie = this.listeTable.get(i).isPartieFinie();
				}
			}
		}
		return finPartie;
	}
	
	@Override
	public boolean isChoixProprietaireSet(String n) throws RemoteException {
		boolean choixProp = false;
		Table t = null;
		for (int i=0; i<this.listeTable.size();i++) {
			for (String key : this.listeTable.get(i).getListeJoueur().keySet()) {
				if(key.equals(n)) {
					 t = this.listeTable.get(i);
				}
			}
		}
		//System.out.println(t.isChoixProprietaire());
		//System.out.println(t.getChoixProprietaire());
		choixProp = t.isChoixProprietaire();
		
		return choixProp;
	}
	public int getChoixProprietaire(String n) throws RemoteException {
		int choixProp = 0;
		for (int i=0; i<this.listeTable.size();i++) {
			for (String key : this.listeTable.get(i).getListeJoueur().keySet()) {
				if(key.equals(n)) {
					 choixProp = this.listeTable.get(i).getChoixProprietaire();
				}
			}
		}
		return choixProp;
	}
	
	
	public void choixProprietaire(String nom, int choix) throws RemoteException{
		Table t = null;
		for (int i=0; i<this.listeTable.size();i++) {
			for (String key : this.listeTable.get(i).getListeJoueur().keySet()) {
				if(key.equals(nom)) {
					t = this.listeTable.get(i);
				}
			}
		}
		for (String key2 : t.getListeEnregistrementClient().keySet()) {
			t.listeEnregistrementClient.get(key2).setChoix(choix);
		}		
		t.setChoixProprietaire(choix);
		t.setChoixProprietaire(true);

		if (choix == 2) {
			System.out.println("Le proprietaire a choisi de detruire sa table");
			this.detruireTable(nom);
		}
		else {
			for (String key2 : t.getListeEnregistrementClient().keySet()) {
				
				for (String key3 : t.getListeEnregistrementClient().keySet()) {
					if (!key2.equalsIgnoreCase(t.getNomTable())){
						t.getListeEnregistrementClient().get(key3).notificationRejointPartie("En attente choix joueur "+key2);
					}
					
				}
				System.out.println(key2);
				System.out.println(t.getNomTable());
				
				if (!key2.equalsIgnoreCase(t.getNomTable())){
					int choixJoueur = t.getListeEnregistrementClient().get(key2).continuerPartie();
					t.getListeEnregistrementClient().get(key2).setChoixJoueur(choixJoueur);
					
					System.out.println(t.getListeEnregistrementClient().get(key2).getChoixJoueur());
					if(choixJoueur == 2) {
						t.retirerJoueur(key2);
					}
					
				}
			}
			
			this.debutPartie(nom);
		}
	}
	
	public String typeTable(int num) throws RemoteException {
		String type = null;
		type = this.listeTable.get(num).getTypeTable();
		return type;
	}
	
	
}
