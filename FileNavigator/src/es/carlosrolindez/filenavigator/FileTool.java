package es.carlosrolindez.filenavigator;

import java.util.ArrayList;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;


public class FileTool 
{
	public static final int LOADER_BROWSE = 0;
	
	public static final String FILE_LIST_KEY = "FileListKey";
	public static final String PATH_KEY = "PATH";
 
    private static String address;
    private static String domain;
    private static String username;
    private static String password;
    

	static {	
		address = "";
		domain = "";
		username = "";
		password = "";
	}
	

	
	static public void setServerConnection(String address, String domain, String username, String password)
	{
	
		FileTool.address = address;
		FileTool.domain = domain;
		FileTool.username = username;
		FileTool.password = password;
	}
	
  	
	static public ArrayList<FileDescription> readFolder(String path)
	{	
		FileDescription fileDescription;
		ArrayList<FileDescription> fileList = null;
		
	  	try 
	  	{
//	      	String sSambaFolder =  "192.168.1.4/Users";
			String url = "smb://" + address + '/' + path;
			
			NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain, username, password);
			
			SmbFile file = new SmbFile(url, auth);		
			SmbFile[] fileArray = file.listFiles();

			fileList = new ArrayList<FileDescription>();
			for(SmbFile item :  fileArray)
			{	                	
				fileDescription = new FileDescription(); 	
			
				fileDescription.fileName = item.getName();
				fileDescription.isFolder = item.isDirectory();
				fileDescription.size = item.length();
				
				fileList.add(fileDescription);	
			}
			 
			return fileList;
        
	    } catch (Exception e) {
	        e.printStackTrace();
	        android.util.Log.e("loadInBackground",e.getMessage());
	        return null;
	    }
	}

}
