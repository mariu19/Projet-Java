package com.blackjack.client.connexion;

import java.rmi.Naming;
import java.util.Scanner;

import com.blackjack.client.callback.CallbackClientImpl;
import com.blackjack.serveur.connexion.IconnexionServer;

public class ConnexionClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
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
				
				} catch (Exception e) {
				e.printStackTrace();
				}
		

	}

}
