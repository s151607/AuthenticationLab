import java.io.Serializable;
import java.util.ArrayList;

public class AccessControlList implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String[] users;
	private String[] methods;
	private ArrayList<Boolean[]> access;
	
	public AccessControlList(String[] users, String[] methods, ArrayList<Boolean[]> access)
	{
		super();
		
		this.users = users.clone();
		this.methods = methods.clone();
		this.access = new ArrayList<Boolean[]>(access);
	}
	
	public AccessControlList() {}
	
	public String[] getUsers()
	{
		return users;
	}
	
	public String[] getMethods()
	{
		return methods;
	}
	
	public ArrayList<Boolean[]> getAccess()
	{
		return access;
	}
	
	public void setUsers(String[] users)
	{
		this.users = users.clone();
	}
	
	public void setMethods(String[] methods)
	{
		this.methods = methods.clone();
	}
	
	public void setAccess(ArrayList<Boolean[]> access)
	{
		this.access = new ArrayList<Boolean[]>(access);
	}
}
