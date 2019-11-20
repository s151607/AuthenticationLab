import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class RoleAccessControl implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Map<String, Integer> roleToListIndex;
	private Map<String, Integer> taskToArrayIndex;
	private ArrayList<Boolean[]> access;
	
	
	
}
