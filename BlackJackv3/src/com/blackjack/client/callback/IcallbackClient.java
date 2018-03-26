package com.blackjack.client.callback;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IcallbackClient extends Remote {
	public void notification(String s) throws RemoteException;
	public void menu(String s) throws RemoteException; 
	public void notificationRejointPartie(String s) throws RemoteException;
	public void compte(int temps) throws RemoteException;
	public int hit_or_stand(String s) throws RemoteException;
	public int continuerPartie() throws RemoteException;
	public void setFinPartie(boolean finPartie) throws RemoteException;
	public void setChoix(int i) throws RemoteException;
	public int getChoix() throws RemoteException;
	void setChoixJoueur(int i) throws RemoteException;
	int getChoixJoueur() throws RemoteException;
}
