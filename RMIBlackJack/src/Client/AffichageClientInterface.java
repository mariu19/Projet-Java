package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Casino.JoueurInterface;

public interface AffichageClientInterface extends Remote{
	
	public void callback() throws RemoteException;

}
