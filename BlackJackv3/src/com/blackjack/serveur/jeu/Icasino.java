package com.blackjack.serveur.jeu;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Icasino extends Remote  {
	
	public void creerTable(String nom, int taille) throws RemoteException;
	public String afficherTable() throws RemoteException;
	public void rejoindreTable(String nom, int i) throws RemoteException;
	//public void debutPartie (int i) throws RemoteException;
	public String verifPlaceTable(String nom, int i) throws RemoteException;
}
