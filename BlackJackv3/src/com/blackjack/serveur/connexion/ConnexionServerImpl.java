package com.blackjack.serveur.connexion;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

import javax.swing.text.html.HTMLDocument.Iterator;

import com.blackjack.client.callback.IcallbackClient;
import com.blackjack.serveur.jeu.Casino;
import com.blackjack.serveur.jeu.Joueur;

public class ConnexionServerImpl extends UnicastRemoteObject implements IconnexionServer, Unreferenced {
	
	//private ArrayList<Joueur> listeJoueur;
	private Hashtable<String, IcallbackClient> listeEnregistrementClient = null;
	private Casino casino;

	
	public ConnexionServerImpl() throws RemoteException, MalformedURLException {
		super();
		listeEnregistrementClient = new Hashtable<String, IcallbackClient>();
		//this.listeJoueur = new ArrayList<Joueur>();
		this.casino = new Casino();
		Naming.rebind("Casino", this.casino);

	}
	

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// D�marre le rmiregistry
		LocateRegistry.createRegistry(1099);
		// Cr�e et installe un gestionnaire de s�curit�
		 // inutile si on ne t�l�charge pas les classes des stubs et parametres
		// System.setSecurityManager(new RMISecurityManager());
		ConnexionServerImpl obj = new ConnexionServerImpl();
		Naming.rebind("Serveur", obj);
		System.out.println("Serveur d�marr�");
		//obj.casino.initCasino();
	}


	@Override
	public void enregistrerNotification(String id, IcallbackClient affichageClient) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("Affichage client enregistr�");
		this.listeEnregistrementClient.put(id, affichageClient);
	}


	@Override
	public synchronized void enleverNotification(String id) throws RemoteException {
		// TODO Auto-generated method stub
		this.listeEnregistrementClient.remove(id);
		
	}
	
	


	@Override
	public void connexionJoueur(String nom) throws RemoteException {
		// TODO Auto-generated method stub
		int i = 1;
		String j = null;
		String nomJoueurs= "Liste des joueurs connect�s: \n" ;
		System.out.println("Un joueur vient de se connecter au serveur");
		casino.ajouterJoueurCasino(nom, this.listeEnregistrementClient.get(nom));
		
		try {
			
			listeEnregistrementClient.get(nom).notification(nom);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Liste des joueurs connect�s: ");
		for (String key : casino.getListeJoueur().keySet()) {
			System.out.println("Joueur "+i+" : "+casino.getListeJoueur().get(key).getNom());
			i++;
		}
		
	/*	try {
			
			listeEnregistrementClient.get(nom).menu(nom);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	
	}


	@Override
	public boolean verifNom(String nom) throws RemoteException {
	
		if (listeEnregistrementClient.get(nom) != null) {
			return false;
		}
		
		return true;
	}
	


	public Hashtable<String, IcallbackClient> getListeEnregistrementClient() {
		return listeEnregistrementClient;
	}


	@Override
	public void unreferenced() {
		// TODO Auto-generated method stub
		System.out.println("Connexion perdue");
	}




}
