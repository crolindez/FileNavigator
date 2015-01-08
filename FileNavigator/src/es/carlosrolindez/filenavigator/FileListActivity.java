package es.carlosrolindez.filenavigator;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class FileListActivity extends Activity implements LoaderManager.LoaderCallbacks<ArrayList<FileDescription>>
{
	private ListView listView;
	private FileListAdapter listAdapter;
	private ArrayList<FileDescription> fileList;
	private LoaderManager lm;
	private String path;
	private FileListActivity fileListActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		fileListActivity = this;
    	loadPreferences();
    	setContentView(R.layout.activity_main);
    	    	
      	if (savedInstanceState != null) 
      	{

      		fileList = savedInstanceState.getParcelableArrayList(FileTool.FILE_LIST_KEY);
      		path = savedInstanceState.getString(FileTool.PATH_KEY,"");
      	}
      	else
      	{	
      		Intent intent = getIntent();
      		path = intent.getStringExtra(FileTool.PATH_KEY);
      		if (path==null)	path = "";
        	lm = getLoaderManager(); 
		    Bundle pathString = new Bundle();
		    pathString.putString(FileTool.PATH_KEY, path);  	
		    lm.restartLoader(FileTool.LOADER_BROWSE, pathString, fileListActivity);	

      	}
		
      	listView=(ListView)findViewById(R.id.list);  
    	listAdapter = new FileListAdapter(this,fileList);
    	listView.setAdapter(listAdapter);
    	listView.setOnItemClickListener(new OnItemClickListener() {
    		@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
        	{ 			
    			FileDescription fileDescription = (FileDescription) parent.getItemAtPosition(position);

    			if (fileDescription.isFolder)
    			{
       				Intent intent;				
  
    			    intent = new Intent (view.getContext(), FileListActivity.class);
    		        intent.putExtra(FileTool.PATH_KEY, path + fileDescription.fileName);        	
    	        	startActivity(intent);	
    				
    /*  				path = path + fileDescription.fileName;
    		      	lm = getLoaderManager(); 
    			    Bundle pathString = new Bundle();
    			    pathString.putString(FileTool.PATH_KEY, path);  	
    			    lm.restartLoader(FileTool.LOADER_BROWSE, pathString, fileListActivity);	
*/
    				
    			}
    			else
    			{   				
    				File file=FileTool.copyToPhone(path + fileDescription.fileName);
	    	        Uri uri = Uri.fromFile(file);
	    	        
	    	        Intent intent = new Intent(Intent.ACTION_VIEW);
	    	        // Check what kind of file you are trying to open, by comparing the url with extensions.
	    	        // When the if condition is matched, plugin sets the correct intent (mime) type, 
	    	        // so Android knew what application to use to open the file
	    	        if (file.toString().contains(".doc") || file.toString().contains(".docx")) {
	    	            // Word document
	    	            intent.setDataAndType(uri, "application/msword");
	    	        } else if(file.toString().toLowerCase().contains(".pdf")) {
	    	            // PDF file
	    	            intent.setDataAndType(uri, "application/pdf");
	    	        } else if(file.toString().toLowerCase().contains(".xls") || file.toString().toLowerCase().contains(".xlsx")) {
	    	            // Excel file
	    	            intent.setDataAndType(uri, "application/vnd.ms-excel");
	    	        } else if(file.toString().toLowerCase().contains(".zip") || file.toString().toLowerCase().contains(".rar"))  {
	    	            // ZIP Files
	    	            intent.setDataAndType(uri, "application/zip");
	    	        } else if(file.toString().toLowerCase().contains(".rtf")) {
	    	            // RTF file
	    	            intent.setDataAndType(uri, "application/rtf");
	    	        } else if(file.toString().toLowerCase().contains(".gif")) {
	    	            // GIF file
	    	            intent.setDataAndType(uri, "image/gif");
	    	        } else if(file.toString().toLowerCase().contains(".jpg") || file.toString().toLowerCase().contains(".jpeg") || file.toString().toLowerCase().contains(".png")) {
	    	            // JPG file
	    	            intent.setDataAndType(uri, "image/jpeg");
	    	        } else if(file.toString().toLowerCase().contains(".txt")) {
	    	            // Text file
	    	            intent.setDataAndType(uri, "text/plain");
	    	        } else if(file.toString().toLowerCase().contains(".3gp") || file.toString().toLowerCase().contains(".mpg") || file.toString().toLowerCase().contains(".mpeg") || file.toString().toLowerCase().contains(".mpe") || file.toString().toLowerCase().contains(".mp4") || file.toString().toLowerCase().contains(".avi")) {
	    	            // Video files
	    	            intent.setDataAndType(uri, "video/*");
	    	        } else {
	    	            //if you want you can also define the intent type for any other file
	    	            
	    	            //additionally use else clause below, to manage other unknown extensions
	    	            //in this case, Android will show all applications installed on the device
	    	            //so you can choose which application to use
	    	            intent.setDataAndType(uri, "*/*");
	    	        }
	    	        
	    	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	    	        startActivity(intent);
//    	    	Intent intent = new Intent (view.getContext(), FileListActivity.class);
//    	        Product product = (Product)parent.getItemAtPosition(position);    	
//            	intent.putExtra(NavisionTool.LAUNCH_DESCRIPTION, product.description);  
//            	intent.putExtra(NavisionTool.LAUNCH_INFO_MODE, NavisionTool.INFO_MODE_SUMMARY);
//            	startActivity(intent);
    			}

        	}
    	});  


     
        if (fileList!=null) 
        {
       		showResultSet(fileList);
        }    	
	}

	private void loadPreferences()
	{
	    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
	   	SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		FileTool.setServerConnection( sharedPref.getString("shared_resource_address", ""),
    			sharedPref.getString("domain_name", ""), 
    			sharedPref.getString("user_name", ""), 
    			sharedPref.getString("password", ""));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
 	    	Intent intent = new Intent (this, SettingsActivity.class);
        	startActivity(intent);    
			return true;
		}
		return super.onOptionsItemSelected(item);		
	}
	
    @Override
    public void onSaveInstanceState(Bundle savedState) 
    {
	   	super.onSaveInstanceState(savedState);
        savedState.putParcelableArrayList(FileTool.FILE_LIST_KEY, fileList);
        savedState.putString(FileTool.PATH_KEY, path);
	}   
    
	void showResultSet(ArrayList<FileDescription> fileListLoaded)
	{
		if (fileListLoaded == null) 
			fileList = null;
		else
		{
			fileList = new ArrayList<FileDescription>();
			for (FileDescription item : fileListLoaded)
			{
				fileList.add(item);
			}
		}
		if (listAdapter!=null) 
			listAdapter.showResultSet(fileList);
	}

	@Override
	public Loader<ArrayList<FileDescription>> onCreateLoader(int id, Bundle filter)
	{
		return new FileListLoader(this,id,filter);
	}
	
	@Override
	public void onLoadFinished(Loader<ArrayList<FileDescription>> loader,ArrayList<FileDescription> fileList)
	{
	    if (fileList==null)
	    {			    
        	Toast.makeText(this, "Shared folder not found", Toast.LENGTH_SHORT).show();
	    }
	    else
	    	showResultSet(fileList);
	}
	
	@Override 
	public void onLoaderReset(Loader<ArrayList<FileDescription>> loader)
	{

	}

}
