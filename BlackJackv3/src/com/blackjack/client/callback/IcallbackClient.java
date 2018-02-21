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
}
