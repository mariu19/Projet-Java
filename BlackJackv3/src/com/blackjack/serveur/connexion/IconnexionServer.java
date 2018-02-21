package com.blackjack.serveur.connexion;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.blackjack.client.callback.IcallbackClient;


public interface IconnexionServer extends Remote {
	
	public void enregistrerNotification(String id, IcallbackClient affichageClient) throws RemoteException;
	public void enleverNotification(String id) throws RemoteException;
	public void connexionJoueur(String nom) throws RemoteException;
	public boolean verifNom(String nom) throws RemoteException;
}
