import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class RegistryServer
{
	public static void main(String[] args) throws RemoteException
	{
		Registry registry;

		//Tries to create a registry on local port 5099. If one already exist get that instead.
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
