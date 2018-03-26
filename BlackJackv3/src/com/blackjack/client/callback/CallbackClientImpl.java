package com.blackjack.client.callback;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.blackjack.client.connexion.ConnexionClient;
import com.blackjack.client.outils.CompteARebours;
import com.blackjack.client.outils.ControlSaisie;
import com.blackjack.serveur.connexion.IconnexionServer;
import com.blackjack.serveur.jeu.Icasino;


public class CallbackClientImpl extends UnicastRemoteObject implements IcallbackClient, Unreferenced {
	
	private String nom;
	private ControlSaisie cs = new ControlSaisie();
	private CompteARebours car = new CompteARebours();
	private boolean finPartie;
	private ConnexionClient cc;
	
	public CallbackClientImpl(String nom, ConnexionClient cc) throws RemoteException {
		super();
		this.nom = nom;
		this.finPartie = false;
		this.cc = cc;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void notification(String s) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println(s+", chargement du menu");
		
	}

	@Override
	public void menu(String s) throws RemoteException {
		// TODO Auto-generated method stub
		try {
			Icasino casino = (Icasino) Naming.lookup("//localhost/Casino");
			
			String saisie = null;
			System.out.println("Pour creer une table appuyez sur 1\nPour rejoindre une table appuyez sur 2");
			Scanner sc = new Scanner(System.in);
			saisie = sc.nextLine();
			//choix = sc.nextInt();
			boolean b= cs.controleEntierMenu(saisie);
		
			while (!b) {
				saisie = sc.nextLine();
				b = cs.controleEntierMenu(saisie);
			}
			
			int choix = Integer.parseInt(saisie);
			
			if (choix == 1) {
				
				System.out.println("Saisir un entier entre 1 et 6 pour définir la taille de la table");
				saisie = sc.nextLine();
				b = cs.controleTailleTable(saisie);
				
				while (!b) {
					saisie = sc.nextLine();
					b = cs.controleTailleTable(saisie);
				}
				choix = Integer.parseInt(saisie);
				casino.creerTable(nom, choix);
			}
			else {
				System.out.println("Liste des tables saisir le numéro correspondant à la table pour la rejoindre");
				System.out.println(casino.afficherTable());
				
				saisie = sc.nextLine();
				b = cs.controleSaisie(saisie);
				while (!b) {
					saisie = sc.nextLine();
					b = cs.controleSaisie(saisie);
				}
				choix = Integer.parseInt(saisie);
				String tablechoisie = casino.verifPlaceTable(nom, choix-1);
				
				while (tablechoisie == null) {
					System.out.println("Erreur saisie: choisir une table existante où il reste au moins une place de libre");
					saisie = sc.nextLine();
					b = cs.controleSaisie(saisie);
					while (!b) {
						saisie = sc.nextLine();
						b = cs.controleSaisie(saisie);
					}
					choix = Integer.parseInt(saisie);
					tablechoisie = casino.verifPlaceTable(nom, choix-1);
				}
				casino.rejoindreTable(nom, choix-1);
				//casino.debutPartie(choix-1);
			}
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void notificationRejointPartie(String s) throws RemoteException{
		System.out.println(s);
	}
	
	public void compte(int temps){
		for(int i= temps; i>=0;i--) {
			
			try {
				TimeUnit.SECONDS.sleep(1);
				System.out.print(i+" |");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
	}
	
	public int hit_or_stand(String s){
		int choix = 0;
		System.out.println("Votre main "+s);
		System.out.println("Entrez 1 pour prendre une carte supplémentaires ou 2 pour ne prendre aucune carte");
		Scanner sc = new Scanner(System.in);
		String saisie = sc.nextLine();
		boolean b = cs.controleHitStand(saisie);
		while (!b) {
			saisie = sc.nextLine();
			b = cs.controleHitStand(saisie);
		}
		choix = Integer.parseInt(saisie);
		return choix;
	}
	
	public int continuerPartie(){
		int choix = 0;
		
		System.out.println("Entrez 1 pour continuer la partie ou 2 pour retourner au menu");
		Scanner sc = new Scanner(System.in);
		String saisie = sc.nextLine();
		boolean b = cs.controleContinuerPartie(saisie);
		while (!b) {
			saisie = sc.nextLine();
			b = cs.controleContinuerPartie(saisie);
		}
		choix = Integer.parseInt(saisie);
		
		return choix;
	}
	
	public void unreferenced() {
		System.out.println("connexion perdue");
	}

	

	public synchronized void setFinPartie(boolean finPartie) throws RemoteException{
		this.finPartie = finPartie;
	}

	public boolean isFinPartie() {
		return finPartie;
	}

	@Override
	public synchronized void setChoix(int i) throws RemoteException {
		cc.setChoixProprietaire(i);
	}
	@Override
	public synchronized void setChoixJoueur(int i) throws RemoteException {
		cc.setChoixJoueur(i);
	}
	@Override
	public synchronized int getChoixJoueur() throws RemoteException {
		int i = cc.getChoixJoueur();
		return i;
	}

	@Override
	public synchronized int getChoix() throws RemoteException {
		int i = cc.getChoixProprietaire();
		return i;
	}

}
