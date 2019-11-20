import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class FileGenerator
{
	public static final String accessControlListPath = PrintServer.path + "//access.xml";
		
	public static void main(String[] args)
	{
		String[] users = new String[] {"Alice", "Bob", "Cecilia", "David", "Erica", 
				"Fred", "George"};
		String[] methods = new String[] {"Print", "Queue", "TopQueue", "Start", "Stop",
				"Restart", "Status", "ReadConfig", "SetConfig"};
		ArrayList<Boolean[]> access = new ArrayList<Boolean[]>();
		access.add(new Boolean[] {true,true,true,true,true,true,true,true,true}); //Alice
		access.add(new Boolean[] {false,false,false,true,true,true,true,true,true}); //Bob
		access.add(new Boolean[] {true,true,true,false,false,true,false,false,false}); //Cecilia
		access.add(new Boolean[] {true,true,false,false,false,false,false,false,false}); //David
		access.add(new Boolean[] {true,true,false,false,false,false,false,false,false}); //Erica
		access.add(new Boolean[] {true,true,false,false,false,false,false,false,false}); //Fred
		access.add(new Boolean[] {true,true,false,false,false,false,false,false,false}); //George
		AccessControlList accessList = new AccessControlList(users, methods, access);
		serializeObject(accessList, accessControlListPath);
	}
	
	public static void serializeObject (Object o, String path)
	{
		try
		{
			FileOutputStream fos = new FileOutputStream(new File(path));
			XMLEncoder encoder = new XMLEncoder(fos);
			encoder.writeObject(o);
			encoder.close();
			fos.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static Object deSerializeObject (String path)
	{
		try
		{
			FileInputStream fis = new FileInputStream(new File(path));
			XMLDecoder decoder = new XMLDecoder(fis);
			Object o = decoder.readObject();
			decoder.close();
			fis.close();
			return o;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	


}
