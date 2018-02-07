package Serveur;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;

import Casino.Joueur;
import Casino.JoueurInterface;
import Client.AffichageClientInterface;
import JeuDeCartes.Paquet;

public class BlackJackServeur extends UnicastRemoteObject implements BlackJackServeurInterface {
	
	private Paquet paquet = new Paquet();
	private ArrayList<AffichageClientInterface> enregistrementClient = null;
	private ArrayList<Joueur> listeJoueur;
	private Paquet deck;
	
	
	protected BlackJackServeur() throws RemoteException {
		super();
		enregistrementClient = new ArrayList<AffichageClientInterface>();
		this.listeJoueur = new ArrayList<Joueur>();
		deck = new Paquet();
		deck.melanger();
		deck.melanger();
		// TODO Auto-generated constructor stub
	}
	
	 public static void main(String args[]) throws Exception {
			// Démarre le rmiregistry
			LocateRegistry.createRegistry(1099);
			// Crée et installe un gestionnaire de sécurité
			 // inutile si on ne télécharge pas les classes des stubs et parametres
			// System.setSecurityManager(new RMISecurityManager());
			BlackJackServeur obj = new BlackJackServeur();
			Naming.rebind("Serveur", obj);
			System.out.println("Serveur démarré");

			
		 }

	@Override
	public int enregistrerNotification(String id, AffichageClientInterface affichageClient) throws RemoteException {
		// TODO Auto-generated method stub
		enregistrementClient.add(affichageClient);
		System.out.println("Enregistrement Client");
		int n = enregistrementClient.indexOf(affichageClient);
		
		return n;
	}

	@Override
	public int enleverNotification(String id) throws RemoteException {
		// TODO Auto-generated method stub
		enregistrementClient.remove(0);
		
		return 0;
	}

	@Override
	public void connexionJoueur(String nom) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				int i = 1;
				String nomJoueurs= "Liste des joueurs connectés: \n" ;
				
				System.out.println("Un joueur vient de se connecter au serveur");
				listeJoueur.add(new Joueur(nom));
				
				try {
					enregistrementClient.get(0).callback();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("Liste des joueurs connectés: ");
				for (Joueur joueur : listeJoueur ) {
					System.out.println("Joueur "+i+" : "+joueur.getNom());
					nomJoueurs = nomJoueurs+"Joueur "+i+" : "+joueur.getNom()+"\n";
					i++;
				}
	}
}
