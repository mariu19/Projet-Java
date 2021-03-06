package com.blackjack.client.connexion;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.blackjack.client.callback.CallbackClientImpl;
import com.blackjack.client.outils.ControlSaisie;
import com.blackjack.serveur.connexion.IconnexionServer;
import com.blackjack.serveur.jeu.Icasino;

public class ConnexionClient {
	
	private String nom;
	private static ControlSaisie cs = new ControlSaisie();
	private static ConnexionClient cc = new ConnexionClient();
	private int choixProprietaire;
	private int choixJoueur;
	private boolean finPartieP;
	private CallbackClientImpl client;
	private String typeTable;
	
	public ConnexionClient() {
		super();
		this.choixJoueur = 0;
		this.choixProprietaire = 0;
		this.finPartieP = false;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConnexionClient cc = new ConnexionClient();
		
		try {
			// R�cup�ration d'un proxy sur l'objet
				IconnexionServer serveur = (IconnexionServer) Naming.lookup("//localhost/Serveur");
				System.out.println("Connexion au serveur de jeu r�ussie");
				Scanner sc = new Scanner(System.in);
				String nom = null;
				System.out.println("Nom joueur?");
				nom = sc.nextLine();
		        CallbackClientImpl client = new CallbackClientImpl(nom, cc);
		        cc.setClient(client);
		        boolean finPartie = false;
		        boolean proprietaire;
		        int choixProprietaire = 0;
		     
		        
		        while(!serveur.verifNom(nom)) {
		        	System.out.println("Ce nom a d�j� �t� choisi par un autre joueur, saisir un autre nom");
		        	nom = sc.nextLine();
		        }
		        
		        serveur.enregistrerNotification(nom, client);
		        serveur.connexionJoueur(nom);
				 
		        Icasino casino = (Icasino) Naming.lookup("//localhost/Casino");
				
				//proprietaire = cc.menu(nom, casino);
				System.out.println(client);
				cc.menu(nom, casino);
		        
				/*while (finPartie == false) {
					if (casino.isFinPartie(nom) == true) {
						finPartie=true;
					}
				}
				
				System.out.println("Fin de la partie");
					if(proprietaire) {
						choixProprietaire = cc.continuerPartie();
						//envoi choix au serveur
						casino.choixProprietaire(nom, choixProprietaire);
						if (choixProprietaire == 1) {
							System.out.println("La partie va reprendre");
						}
						else {
							cc.menu(nom, casino);
						}
						
						
					}
					else {
						System.out.println("En attente choix cr�ateur");
						while (cc.choixProprietaire == 0) {
							System.out.print("");
						}
						if (cc.choixProprietaire == 2) {
							System.out.println("Le propri�taire a choisi de d�truire sa table, redirection vers le menu");
							cc.menu(nom, casino);
						}
						else{
							System.out.println("La partie va reprendre dans 15 secondes");
							if(cc.continuerPartie() == 2) {
								casino.quitterTable(nom);
								cc.menu(nom, casino);
							}
						}
					}*/

				} catch (Exception e) {
				e.printStackTrace();
				}
	}

	
	public void menu(String nom, Icasino casino) throws RemoteException {
		// TODO Auto-generated method stub
		
			String saisie = null;
			System.out.println("Pour creer une table appuyez sur 1 \nPour rejoindre une table appuyez sur 2");
			Scanner sc = new Scanner(System.in);
			saisie = sc.nextLine();
			//choix = sc.nextInt();
			Boolean b= cs.controleEntierMenu(saisie);
			Boolean proprietaire = false;
			Boolean recommencer = true;
			this.choixJoueur = 0;
			
			while (!b) {
				saisie = sc.nextLine();
				b = cs.controleEntierMenu(saisie);
			}
			
			int choix = Integer.parseInt(saisie);
			
			if (choix == 1) {
				
				System.out.println("Saisir un entier entre 1 et 6 pour d�finir la taille de la table");
				saisie = sc.nextLine();
				b = cs.controleTailleTable(saisie);
				
				while (!b) {
					saisie = sc.nextLine();
					b = cs.controleTailleTable(saisie);
				}
				choix = Integer.parseInt(saisie);
				casino.creerTable(nom, choix);
				proprietaire = true; 
				this.typeTable = "T";
				//cc.finPartie(casino, proprietaire);
			}
			else {
				System.out.println("Liste des tables saisir le num�ro correspondant � la table pour la rejoindre");
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
					System.out.println("Erreur saisie: choisir une table existante o� il reste au moins une place de libre");
					saisie = sc.nextLine();
					b = cs.controleSaisie(saisie);
					while (!b) {
						saisie = sc.nextLine();
						b = cs.controleSaisie(saisie);
					}
					choix = Integer.parseInt(saisie);
					tablechoisie = casino.verifPlaceTable(nom, choix-1);
				}
				this.typeTable = casino.typeTable(choix-1);
				System.out.println(this.typeTable);
				casino.rejoindreTable(nom, choix-1);
				proprietaire = false;
				//cc.finPartie(casino, proprietaire);
			}
			if(this.typeTable.equals("P")) {
				while(recommencer) {
					while (true) {
						if (this.choixJoueur != 0) {
							//System.out.println(this.choixJoueur);
							break;
						}
					}
					if(this.choixJoueur == 2) {
						//cc.menu(nom, casino);
						recommencer = false;
					}
					System.out.println(recommencer);
					this.choixJoueur = 0;	
					
				}
			}
			
			else {
				
				while (recommencer) {
					Boolean finPartie = false;
					System.out.println("Entr�e while recommencer");
					/*while (finPartie.equals(false)) {
						if (casino.isFinPartie(nom) == true) {
							finPartie = true;
						}
					}*/
					while (true) {
						if (casino.isFinPartie(nom) == true) {
							break;
						}
					}
					
					System.out.println("d�but fin partie");
					System.out.println("Fin de la partie");
					if(proprietaire) {
						choixProprietaire = this.continuerPartie();
						//envoi choix au serveur
						if (choixProprietaire == 1) {
							System.out.println("La partie va reprendre");
						}
						else {
							recommencer =false;
							//cc.menu(nom, casino);
						}
						casino.choixProprietaire(nom, choixProprietaire);
						
					}
					else {

						System.out.println("En attente choix cr�ateur");
						//System.out.println(casino.isChoixProprietaireSet(nom));
						Boolean choixProp = false;
						
						while (true) {
							if (casino.isChoixProprietaireSet(nom) ==true) {
								break;
							}
						}
						System.out.println("Le cr�ateur a choisi, choix joueur");

						if ( this.choixProprietaire == 2) {
							System.out.println("Le propri�taire a choisi de d�truire sa table, redirection vers le menu");
							//cc.menu(nom, casino);
							recommencer = false;
						}
						else{
							while (true) {
								System.out.print("");
								if (this.choixJoueur != 0) {
									break;
								}
							}
							if(this.choixJoueur == 2) {
								//cc.menu(nom, casino);
								recommencer = false;
							}
							System.out.println(recommencer);
						}
						this.choixJoueur = 0;
					}
					
				}
				
				
			}
		
			

			/*while (recommencer) {
				Boolean finPartie = false;
				System.out.println("Entr�e while recommencer");
				while (finPartie.equals(false)) {
					if (casino.isFinPartie(nom) == true) {
						finPartie = true;
					}
				}
				System.out.println("d�but fin partie");
				System.out.println("Fin de la partie");
				if(proprietaire) {
					choixProprietaire = this.continuerPartie();
					//envoi choix au serveur
					if (choixProprietaire == 1) {
						System.out.println("La partie va reprendre");
					}
					else {
						recommencer =false;
						//cc.menu(nom, casino);
					}
					casino.choixProprietaire(nom, choixProprietaire);
					
				}
				else {

					System.out.println("En attente choix cr�ateur");
					System.out.println(casino.isChoixProprietaireSet(nom));
					Boolean choixProp = false;
					
					while (true) {
						if (casino.isChoixProprietaireSet(nom) == true) {
							break;
						}
					}

					if ( this.choixProprietaire == 2) {
						System.out.println("Le propri�taire a choisi de d�truire sa table, redirection vers le menu");
						//cc.menu(nom, casino);
						recommencer = false;
					}
					else{
						System.out.println("La partie va reprendre dans 15 secondes");
						if(this.continuerPartie() == 2) {
							casino.quitterTable(nom);
							//cc.menu(nom, casino);
							recommencer = false;
						}
						System.out.println(recommencer);
					}
				}
				
			}*/
			
			this.menu(nom, casino);
	}
	
	public int continuerPartie() {
		int choix;
		String saisie = null;
		System.out.println("Voulez-vous continuer la partie: 1.Oui || 2.Non");
		
		Scanner sc = new Scanner(System.in);
		saisie = sc.nextLine();
		boolean b= cs.controleContinuerPartie(saisie);
	
		while (!b) {
			saisie = sc.nextLine();
			b = cs.controleContinuerPartie(saisie);
		}
		choix = Integer.parseInt(saisie);
		return choix;
		
		
	}
	
	
	public int getChoixProprietaire() {
		return choixProprietaire;
	}


	public void setChoixProprietaire(int choixProprietaire){
		this.choixProprietaire = choixProprietaire;
	}


	public CallbackClientImpl getClient() {
		return client;
	}


	public void setClient(CallbackClientImpl client) {
		this.client = client;
	}


	public int getChoixJoueur() {
		return choixJoueur;
	}


	public void setChoixJoueur(int choixJoueur) {
		this.choixJoueur = choixJoueur;
	}
	
	
}
