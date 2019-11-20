import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;


/*
 * Hardcoded users:
 * 'user1' with pw 'password'
 * 'user2' with pw 'qwerty123'
 * 'user3' with pw 'admin'
 */
public class Client {
	public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException
	{
		Service service = (Service) Naming.lookup("rmi://localhost:5099/PrintServer");
		/*
		//Test scenarios. See Console for results
		service.start("user1", "password");
		service.print("testFile", "printer1", "user1", "password");
		service.queue("user1", "password");
		service.stop("user1", "password");

		int sessionID = service.getAuthenticatedSession("user2", "qwerty123");
		service.start(sessionID);
		service.setConfig("size", "27", sessionID);
		service.readConfig("size", sessionID);
		service.print("aNewFile", "DTU_PRINTER", sessionID);
		service.print("anotherFile", "DTU_Printer", sessionID);
		service.queue(sessionID);
		service.topQueue(2, sessionID);
		service.queue(sessionID);
		service.stop(sessionID);
		
		service.start("user1", "12345");
		service.start(123);*/
		
		service.start("Alice", "password");
		service.print("testfile", "testprinter", "Alice", "password");
		service.print("testfile", "testprinter", "Bob", "qwerty123");	
	}
}
