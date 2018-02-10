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


	public int notification() throws RemoteException {
		// TODO Auto-generated method stub
		int i = 0;
		Scanner sc = new Scanner(System.in);
		System.out.println("int?");
		i = sc.nextInt();
		
		return i;
	}

}
