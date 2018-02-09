package Serveur;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Casino.JoueurInterface;
import Client.BlackJackNotification;

public interface BlackJackServeurInterface extends Remote{
		
		public void enregistrerNotification(String id, BlackJackNotification affichageClient) throws RemoteException;
		public void enleverNotification(String id) throws RemoteException;
		public void connexionJoueur(String nom) throws RemoteException;
		
}
