package es.carlosrolindez.filenavigator;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;





public class FileTool 
{
	
	public static final String PATH = "PATH";
	
	public static final int LOADER_BROWSE = 0;
	
	public static final String FILE_LIST_KEY = "FileListKey";
 
    private static String ipaddress;
    private static String domain;
    private static String username;
    private static String password;
    
    private static Connection conn;

	static {
		conn = null;
	
		ipaddress = "";
		domain = "";
		username = "";
		password = "";
	}
	

	
	static public void setServerConnection(String server, String port, String ipaddress, String domain, String username, String password)
	{
	
		FileTool.ipaddress = ipaddress;
		FileTool.domain = domain;
		FileTool.username = username;
		FileTool.password = password;
	}
	
	static public Connection openConnection()
	{
	    if (conn!= null)  
	    {
	    	try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	conn = null;
	    }

	    return null;
	}
	
	static public void closeConnection()
	{
		if (conn!=null) 
		{
			conn = null;
		}
		
	}
	
	static public ResultSet queryList(String filterString)
	{
		return null;
	}

}
