package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BlackJackNotification extends Remote {
	public void notification(String s) throws RemoteException;
}
