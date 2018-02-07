package Client;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import Serveur.BlackJackServeurInterface;

public class BlackJackClient implements AffichageClientInterface, Serializable{

	protected BlackJackClient() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String args[]) throws RemoteException, MalformedURLException {
		
		try {
			// Récupération d'un proxy sur l'objet
				BlackJackServeurInterface serveur = (BlackJackServeurInterface) Naming.lookup("//localhost/Serveur");
				System.out.println("Connexion au serveur de jeu réussie");
				Scanner sc = new Scanner(System.in);
				String nom = null;
				System.out.println("Nom joueur?");
				nom = sc.nextLine();
		        BlackJackClient client = new BlackJackClient();
		        serveur.enregistrerNotification(nom, client);
				serveur.connexionJoueur(nom);
		        
				// Appel d'une méthode sur l'objet distant
				} catch (Exception e) {
				e.printStackTrace();
				}
		

	}

	@Override
	public void callback() throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("Appel callback");
		
	}


}
