import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.commons.codec.binary.Base64;




public class PrintServer extends UnicastRemoteObject implements Service 
{
	public class printJob
	{
		private int jobNr;
		private String fileName;
		
		public printJob(int jobNr, String fileName) 
		{
			this.jobNr = jobNr;
			this.fileName = fileName;
		}
		
		public int getJobNr() 
		{
			return jobNr;
		}
		
		public String getfileName() 
		{
			return fileName;
		}
	}
	private static List<printJob> printQueue;
	
	//Constructor
	public PrintServer() throws RemoteException
	{
		super();
		printQueue = new ArrayList<printJob>();
		
	}
	//testmethod
	public String echo(String input) throws RemoteException
	{
		return "From server: " + hashPassword("test") + "pw";
	}
	
	//Printer Methods -----------------------------------------------
	public void print(String filename, String printer)
	{
		System.out.println("File " + filename + "printed on " + printer);
	}

	//Return print queue
	public List<printJob> queue() throws RemoteException {
		return printQueue;
	}

	//Looks for job with the given jobnr. Moves it to the head of the queue.
	public void topQueue(int job) throws RemoteException {
		for(int i = 0; i < printQueue.size(); i++) {
			if(printQueue.get(i).getJobNr() == job) {
				printQueue.add(0, printQueue.get(i));
				printQueue.remove(i+1);
			}
		}
	}

	//"Starts the printserver" by initializing the printqueue.
	public void start() throws RemoteException {
		printQueue = new ArrayList<printJob>();
	}

	//"Stops the printserver by deleting the queue.
	public void stop() throws RemoteException {
		//Set reference to null and let GC take it. Simulating turning off.
		printQueue = null; 
	}

	//stop -> start
	@Override
	public void restart() throws RemoteException {
		stop();
		start();
		
	}

	//
	public String status() throws RemoteException {
		return "There are currently " + printQueue + " jobs in the queue";
	}

	@Override
	public String readConfig(String parameter) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setConfig(String parameter, String value) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	//----------------------------------------------------------------
	
	@Override
	public Boolean signup(String username, String password) throws RemoteException
	{
		try {
			storeLogin(username,password);
		} catch (IOException e) {
			return false;
		}
		return true;		
	}
	
	private String hashPassword(String password)
	{
		/* Lav hash... det her kaster åbenbart en ClassNotFoundException..
		 * tror jeg har lavet noget bøvl med classpath
		final int iterations = 20000;
		final int desiredKeyLen = 256;
		final String salt = "FineSaltShouldBeIllegal";
		
		try {
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(
            password.toCharArray(), salt.getBytes(), iterations, desiredKeyLen));
        return new String(Base64.encodeBase64(key.getEncoded()));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			//Do things
		}
		return null;*/
		return "dethererenhashetstring";
	}
	
	//TODO hash password before writing
	private void storeLogin(String userName, String password) throws IOException
	{
		File file = new File(System.getProperty("user.dir") +
				"\\AuthenticationLab" + "PrinterLogins.txt");
		FileOutputStream fop = new FileOutputStream(file, false);
		file.createNewFile();
		
		byte[] toWrite = (userName + " " + password).getBytes();
		fop.write(toWrite);
		fop.flush();
		fop.close();
	}

	//Check whether the given password matches
	private Boolean checkLogin(String userName, String password)
	{
		
		return true;
	}
	
	
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