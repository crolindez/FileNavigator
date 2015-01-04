package es.carlosrolindez.filenavigator;

import java.util.ArrayList;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	loadPreferences();
    	setContentView(R.layout.activity_main);
    	

    	
      	if (savedInstanceState != null) 
      	{
      		fileList = savedInstanceState.getParcelableArrayList(FileTool.FILE_LIST_KEY);
      	}
      	else
      	{
        	lm = getLoaderManager(); 
		    Bundle pathString = new Bundle();
		    pathString.putString(FileTool.PATH, "");  	
		    lm.restartLoader(FileTool.LOADER_BROWSE, pathString, this);	

      	}
		
      	listView=(ListView)findViewById(R.id.list);  
    	listAdapter = new FileListAdapter(this,fileList);
    	listView.setAdapter(listAdapter);
    	listView.setOnItemClickListener(new OnItemClickListener() {
    		@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
        	{ 			
//    	    	Intent intent = new Intent (view.getContext(), FileListActivity.class);
//    	        Product product = (Product)parent.getItemAtPosition(position);    	
//            	intent.putExtra(NavisionTool.LAUNCH_DESCRIPTION, product.description);  
//            	intent.putExtra(NavisionTool.LAUNCH_INFO_MODE, NavisionTool.INFO_MODE_SUMMARY);
//            	startActivity(intent);
            	
            	Toast.makeText(view.getContext(), "List Item", Toast.LENGTH_SHORT).show();
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
