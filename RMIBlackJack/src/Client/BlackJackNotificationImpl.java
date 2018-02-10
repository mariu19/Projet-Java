package Client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class BlackJackNotificationImpl extends UnicastRemoteObject implements BlackJackNotification {

	private String nom;
	
	
	public BlackJackNotificationImpl(String nom) throws RemoteException {
		super();
		this.nom = nom;
	}


	public void notification(String s) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println(s+" vient d'entrer dans la tiepar callback");
		
	}

}
