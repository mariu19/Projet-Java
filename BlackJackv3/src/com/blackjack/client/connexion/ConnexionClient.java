package com.blackjack.client.connexion;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import com.blackjack.client.callback.CallbackClientImpl;
import com.blackjack.client.outils.ControlSaisie;
import com.blackjack.serveur.connexion.IconnexionServer;
import com.blackjack.serveur.jeu.Icasino;

public class ConnexionClient {
	
	private String nom;
	private static ControlSaisie cs = new ControlSaisie();
	private static ConnexionClient cc = new ConnexionClient();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConnexionClient cc = new ConnexionClient();
		try {
			// Récupération d'un proxy sur l'objet
				IconnexionServer serveur = (IconnexionServer) Naming.lookup("//localhost/Serveur");
				System.out.println("Connexion au serveur de jeu réussie");
				Scanner sc = new Scanner(System.in);
				String nom = null;
				System.out.println("Nom joueur?");
				nom = sc.nextLine();
		        CallbackClientImpl client = new CallbackClientImpl(nom);
		        
		        while(!serveur.verifNom(nom)) {
		        	System.out.println("Ce nom a déjà été choisi par un autre joueur, saisir un autre nom");
		        	nom = sc.nextLine();
		        }
		        
		        serveur.enregistrerNotification(nom, client);
		        serveur.connexionJoueur(nom);
				 
		        Icasino casino = (Icasino) Naming.lookup("//localhost/Casino");
				
		        //Debut menu
				/*String saisie = null;
				System.out.println("Pour creer une table appuyez sur 1\nPour rejoindre une table appuyez sur 2");
				Scanner sc1 = new Scanner(System.in);
				saisie = sc1.nextLine();
				//choix = sc.nextInt();
				boolean b= cs.controleEntierMenu(saisie);
			
				while (!b) {
					saisie = sc1.nextLine();
					b = cs.controleEntierMenu(saisie);
				}
				
				int choix = Integer.parseInt(saisie);
				
				if (choix == 1) {
					
					System.out.println("Saisir un entier entre 1 et 6 pour définir la taille de la table");
					saisie = sc1.nextLine();
					b = cs.controleTailleTable(saisie);
					
					while (!b) {
						saisie = sc1.nextLine();
						b = cs.controleTailleTable(saisie);
					}
					choix = Integer.parseInt(saisie);
					casino.creerTable(nom, choix);
					System.out.println("fin partie");
				}
				else {
					System.out.println("Liste des tables saisir le numéro correspondant à la table pour la rejoindre");
					System.out.println(casino.afficherTable());
					saisie = sc1.nextLine();
					b = cs.controleSaisie(saisie);
					while (!b) {
						saisie = sc1.nextLine();
						b = cs.controleSaisie(saisie);
					}
					choix = Integer.parseInt(saisie);
					String tablechoisie = casino.verifPlaceTable(nom, choix-1);
					
					while (tablechoisie == null) {
						System.out.println("Erreur saisie: choisir une table existante où il reste au moins une place de libre");
						saisie = sc1.nextLine();
						b = cs.controleSaisie(saisie);
						while (!b) {
							saisie = sc1.nextLine();
							b = cs.controleSaisie(saisie);
						}
						choix = Integer.parseInt(saisie);
						tablechoisie = casino.verifPlaceTable(nom, choix-1);
					}
					casino.rejoindreTable(nom, choix-1);
					//casino.debutPartie(choix-1);
					System.out.println("fin partie");
					
				}*/
		        //Fonction pour choisir 1:crée 2:rejoindre 3:quit
				//Fonction (ou a la fin de la fonction précédente) -> Voulez vous continuer si oui appel Menu
				//Si créateur appel detruire table -> notif aux autres
				//Pour chaque joueur qui quitte -> notif aux autres
				cc.menu(nom);
				
				} catch (Exception e) {
				e.printStackTrace();
				}
	}

	public void menu(String nom) throws RemoteException {
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
			}
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void continuerPartie() {
		int choix;
		String saisie = null;
		System.out.println("Voulez-vous continuer la partie: 1.Oui || 2.Non");
		
		Scanner sc = new Scanner(System.in);
		saisie = sc.nextLine();
		//choix = sc.nextInt();
		boolean b= cs.controleEntierMenu(saisie);
	
		while (!b) {
			saisie = sc.nextLine();
			b = cs.controleEntierMenu(saisie);
		}
		
	}
	
}
