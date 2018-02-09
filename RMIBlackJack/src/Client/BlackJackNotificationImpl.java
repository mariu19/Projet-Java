package Client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class BlackJackNotificationImpl extends UnicastRemoteObject implements BlackJackNotification {

	private String nom;
	
	
	public BlackJackNotificationImpl(String nom) throws RemoteException {
		super();
		this.nom = nom;
	}


	public void notification() throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("Appel callback");
	}

}
