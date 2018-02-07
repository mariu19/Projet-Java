package Serveur;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Casino.JoueurInterface;
import Client.AffichageClientInterface;

public interface BlackJackServeurInterface extends Remote{
		
		public int enregistrerNotification(String id, AffichageClientInterface affichageClient) throws RemoteException;
		public int enleverNotification(String id) throws RemoteException;
		public void connexionJoueur(String nom) throws RemoteException;
		
}
