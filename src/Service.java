import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Service extends Remote {
	public String echo(String input) throws RemoteException;
	
	public void print(String filename, String printer) throws RemoteException;
	public List<PrintServer.printJob> queue() throws RemoteException;
	public void topQueue(int job) throws RemoteException;
	public void start() throws RemoteException;
	public void stop() throws RemoteException;
	public void restart() throws RemoteException;
	public String status() throws RemoteException;
	public String readConfig(String parameter) throws RemoteException;
	public void setConfig(String parameter, String value) throws RemoteException;
	public Boolean signup(String username, String password) throws RemoteException;
}

/*
print(String filename, String printer);   // prints file filename on the specified printer
queue();   // lists the print queue on the user's display in lines of the form <job number>   <file name>
topQueue(int job);   // moves job to the top of the queue
start();   // starts the print server
stop();   // stops the print server
restart();   // stops the print server, clears the print queue and starts the print server again
status();  // prints status of printer on the user's display
readConfig(String parameter);   // prints the value of the parameter on the user's display
setConfig(String parameter, String value);   // sets the parameter to value
*/