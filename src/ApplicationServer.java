import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class ApplicationServer {
	

	
	
	
	
	public static void main(String[] args) throws RemoteException
	{
		Registry registry;
		if(LocateRegistry.getRegistry(5099) != null)
		{
			registry = LocateRegistry.getRegistry(5099);
		}
		else
		{
			registry = LocateRegistry.createRegistry(5099);
		}
		registry.rebind("PrintServer", new PrintServer());
	}
}
