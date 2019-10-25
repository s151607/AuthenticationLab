import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class ApplicationServer {
	

	
	
	
	
	public static void main(String[] args) throws RemoteException
	{
		Registry registry;
		
		try
		{
			registry = LocateRegistry.createRegistry(5099);
		} catch (RemoteException e)
		{
			registry = LocateRegistry.getRegistry(5099);
		}
		registry.rebind("PrintServer", new PrintServer());
	}
}
