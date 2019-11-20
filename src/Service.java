import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Service extends Remote {
	public void print(String filename, String printer, String username, String password) throws RemoteException;
	public void print(String filename, String printer, int sessionID) throws RemoteException;
	
	public void queue(String username, String password) throws RemoteException;
	public void queue(int sessionID) throws RemoteException;
	
	public void topQueue(int job, String username, String password) throws RemoteException;
	public void topQueue(int job, int sessionID) throws RemoteException;
	
	public void start(String username, String password) throws RemoteException;
	public void start(int sessionID) throws RemoteException;
	
	public void stop(String username, String password) throws RemoteException;
	public void stop(int sessionID) throws RemoteException;
	
	public void restart(String username, String password) throws RemoteException;
	public void restart(int sessionID) throws RemoteException;
	
	public String status(String username, String password) throws RemoteException;
	public String status(int sessionID) throws RemoteException;
	
	public String readConfig(String parameter, String username, String password) throws RemoteException;
	public String readConfig(String parameter, int sessionID) throws RemoteException;
	
	public void setConfig(String parameter, String value, String username, String password) throws RemoteException;
	public void setConfig(String parameter, String value, int sessionID) throws RemoteException;
	
	public int getAuthenticatedSession(String username, String password) throws RemoteException;
}