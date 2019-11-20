import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class PrintServer extends UnicastRemoteObject implements Service
{
	public static final String path = System.getProperty("user.dir");
	private Boolean isStarted = false;
	private int newestJobNr = 0;
	private int sessionID;
	private Boolean sessionON = false;
	private static List<printJob> printQueue;
	private static List<config> configs;

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

	public class config
	{
		private String parameter;
		private String value;

		public config(String parameter, String value)
		{
			this.parameter = parameter;
			this.value = value;
		}

		public void setParameter(String parameter)
		{
			this.parameter = parameter;
		}

		public void setValue(String value)
		{
			this.value = value;
		}

		public String getParamater()
		{
			return parameter;
		}

		public String getValue()
		{
			return value;
		}
	}
	
	// Constructor
	public PrintServer() throws RemoteException
	{
		super();
		generatePwFile();
	}

	// Printer Methods
	public void print(String filename, String printer, String username, String password) throws RemoteException
	{
		if (!testIsStarted() || !verifyPassword(username, password))
			return;
		
		if (!verifyAccess("Print", username))
		{
			System.out.println(username + " does not have access to Print");
			return;
		}

		printQueue.add(new printJob(newestJobNr + 1, filename));
		newestJobNr++;
		System.out.println("File " + filename + "is now queued to print on " + printer);
	}
	
	public void print(String filename, String printer, int sessionID) throws RemoteException
	{
		if (!testIsStarted() || !verifySessionID(sessionID))
			return;

		printQueue.add(new printJob(newestJobNr + 1, filename));
		newestJobNr++;
		System.out.println("File " + filename + "is now queued to print on " + printer);
	}
	
	public void queue(String username, String password) throws RemoteException
	{
		if (!testIsStarted() || !verifyPassword(username, password))
			return;
		
		System.out.println("Printer queue:");
		for (printJob job : printQueue)
		{
			System.out.println(job.getJobNr() + " " + job.getfileName());
		}
	}
	
	public void queue(int sessionID) throws RemoteException
	{
		if (!testIsStarted() || !verifySessionID(sessionID))
			return;
		
		System.out.println("Printer queue:");
		for (printJob job : printQueue)
		{
			System.out.println(job.getJobNr() + " " + job.getfileName());
		}
	}

	public void topQueue(int job, String username, String password) throws RemoteException
	{
		if (!testIsStarted() || !verifyPassword(username, password))
			return;
		for (int i = 0; i < printQueue.size(); i++)
		{
			if (printQueue.get(i).getJobNr() == job)
			{
				printQueue.add(0, printQueue.get(i));
				printQueue.remove(i + 1);
			}
		}
	}
	
	public void topQueue(int job, int sessionID) throws RemoteException
	{
		if (!testIsStarted() || !verifySessionID(sessionID))
			return;
		for (int i = 0; i < printQueue.size(); i++)
		{
			if (printQueue.get(i).getJobNr() == job)
			{
				printQueue.add(0, printQueue.get(i));
				printQueue.remove(i + 1);
			}
		}
	}

	public void start(String username, String password) throws RemoteException
	{
		if (!verifyPassword(username, password))
			return;
		printQueue = new ArrayList<printJob>();
		configs = new ArrayList<config>();
		isStarted = true;
	}
	
	public void start(int sessionID) throws RemoteException
	{
		if (!verifySessionID(sessionID))
			return;
		printQueue = new ArrayList<printJob>();
		configs = new ArrayList<config>();
		isStarted = true;
	}

	public void stop(String username, String password) throws RemoteException
	{
		if (!testIsStarted() || !verifyPassword(username, password))
			return;
		
		// 'Resets the memory' to simulate turning it off.
		printQueue = null;
		configs = null;
		isStarted = false;
		sessionON = false;
		newestJobNr = 0;
	}
	
	public void stop(int sessionID) throws RemoteException
	{
		if (!testIsStarted() || !verifySessionID(sessionID))
			return;
		
		// 'Resets the memory' to simulate turning it off.
		printQueue = null;
		configs = null;
		isStarted = false;
		sessionON = false;
		newestJobNr = 0;
	}

	public void restart(String username, String password) throws RemoteException
	{
		stop(username, password);
		start(username, password);
	}
	
	public void restart(int sessionID) throws RemoteException
	{
		stop(sessionID);
		start(sessionID);
	}

	public String status(String username, String password) throws RemoteException
	{
		if (!testIsStarted() || !verifyPassword(username, password))
			return null;
		return "There are currently " + printQueue.size() + " jobs in the queue";
	}
	
	public String status(int sessionID) throws RemoteException
	{
		if (!testIsStarted() || !verifySessionID(sessionID))
			return null;
		return "There are currently " + printQueue.size() + " jobs in the queue";
	}

	public String readConfig(String parameter, String username, String password) throws RemoteException {
		if(!testIsStarted() || !verifyPassword(username, password))
			return null;
		
		for (config con : configs) 
		{
			if (con.getParamater().equals(parameter))
				return con.getValue();
		}
		return null;
	}
	
	public String readConfig(String parameter, int sessionID) throws RemoteException {
		if(!testIsStarted() || !verifySessionID(sessionID))
			return null;
		
		for (config con : configs) 
		{
			if (con.getParamater().equals(parameter))
				return con.getValue();
		}
		return null;
	}

	public void setConfig(String parameter, String value, String username, String password) throws RemoteException
	{
		if (!testIsStarted() || !verifyPassword(username, password))
			return;
		configs.add(new config(parameter, value));
	}
	
	public void setConfig(String parameter, String value, int sessionID) throws RemoteException
	{
		if (!testIsStarted() || !verifySessionID(sessionID))
			return;
		configs.add(new config(parameter, value));
	}

	//Methods for verifying if given pw/sessionID matches the stored one
	private Boolean verifyPassword(String userName, String password)
	{
		String[] loadedUsers = loadPwFile();
		String[] user = new String[3];
		byte[] userSalt;
		byte[] storedPw;
		
		//very scalable
		if (userName.equals("Alice"))
		{
			user = loadedUsers[0].split(":");
			userSalt = Base64.getDecoder().decode(user[2]);
			storedPw = Base64.getDecoder().decode(user[1]);
			
			if (Arrays.equals(hashPassword(password, userSalt), storedPw))
			{
				System.out.println("Password accepted: user1 authenticated");
				return true;
			}
			System.out.println("Password for user1 is incorrect");
		}
		else if (userName.equals("Bob"))
		{
			user = loadedUsers[1].split(":");
			userSalt = Base64.getDecoder().decode(user[2]);
			storedPw = Base64.getDecoder().decode(user[1]);
			
			if (Arrays.equals(hashPassword(password, userSalt), storedPw))
			{
				System.out.println("Password accepted: user2 authenticated");
				return true;
			}
			System.out.println("Password for user2 is incorrect");
		}
		else if (userName.equals("user3"))
		{
			user = loadedUsers[2].split(":");
			userSalt = Base64.getDecoder().decode(user[2]);
			storedPw = Base64.getDecoder().decode(user[1]);
			
			if (Arrays.equals(hashPassword(password, userSalt), storedPw))
			{
				System.out.println("Password accepted: user3 authenticated");
				return true;
			}
			System.out.println("Password for user3 is incorrect");
		}
		else
		{
			System.out.println("Unknown username");
		}
		return false;
	}
	
	private Boolean verifySessionID(int sessionID)
	{
		if (sessionON && sessionID == this.sessionID)
		{
			System.out.println("Session authenticated");
			return true;
		}
		System.out.println("Invalid session ID");
		return false;
	}
	
	// Checks whether the given user has access to the requested functionality
	// Used for Task 1
	private Boolean verifyAccess(String task, String user)
	{
		int counter = 0;
		int taskIndex = -1;
		int userIndex = -1;
		
		AccessControlList list = (AccessControlList) FileGenerator.deSerializeObject(FileGenerator.accessControlListPath);
		for(String m : list.getMethods())
		{
			if (m.equals(task))
			{
				taskIndex = counter;
				break;
			}
			counter++;
		}
		counter = 0;
		for(String u : list.getUsers())
		{
			if (u.equals(user))
			{
				userIndex = counter;
				break;
			}
			counter++;
		}
		
		try
		{
			if (list.getAccess().get(userIndex)[taskIndex]) return true;
		}
		catch (IndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	//Hashing methods
	private byte[] generateSalt()
	{
		byte[] values = new byte[20];
		try
		{
			SecureRandom random = SecureRandom.getInstanceStrong();
			random.nextBytes(values);
			return values;
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	private byte[] hashPassword(String password, byte[] salt)
	{
		try
		{
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 50000, 256);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return factory.generateSecret(spec).getEncoded();
		} catch (InvalidKeySpecException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	//Test whether the server is started
	private Boolean testIsStarted()
	{
		if (!isStarted)
		{
			System.out.println("You must start the print server first");
			return false;
		}
		return true;
	}

	public int getAuthenticatedSession(String username, String password)
	{
		if (!verifyPassword(username, password) )
		{
			return -1;
		}
		
		SecureRandom random;
		try
		{
			random = SecureRandom.getInstanceStrong();
			sessionID = random.nextInt(100000); 
			sessionON = true;
			return sessionID;
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return -1;
	}

	private void generatePwFile()
	{
		byte[][] salts = new byte[3][20];
		String[] usernames = {"Alice", "Bob", "user3"};
		String[] passwords = {"password", "qwerty123", "admin"};
		salts[0] = generateSalt();
		salts[1] = generateSalt();
		salts[2] = generateSalt();
		String[] hashedPasswords = new String[3];
		
		for (int i = 0; i < 3; i++)
		{
			hashedPasswords[i] = Base64.getEncoder().encodeToString(hashPassword(passwords[i], salts[i]));
		}
		
		String toWrite = "";
		for (int i = 0; i < 3; i++)
		{
			toWrite += usernames[i] + ":" + hashedPasswords[i] + ":" + 
					Base64.getEncoder().encodeToString(salts[i]) +
					System.getProperty("line.separator");
		}
		
		try
		{
			FileOutputStream fo;
			fo = new FileOutputStream(new File(path + "//pw.txt"));
			fo.write(toWrite.getBytes());
			fo.close();
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String[] loadPwFile()
	{
		try
		{
			FileInputStream fis = new FileInputStream(path + "//pw.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			String[] readLines = new String[3];
			
			for (int i = 0; i < 3; i++)
			{
				readLines[i] = reader.readLine();
			}
			
			return readLines;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
