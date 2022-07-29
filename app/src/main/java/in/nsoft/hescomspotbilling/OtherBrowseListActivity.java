//Created Nitish 01/03/2014
package in.nsoft.hescomspotbilling;

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OtherBrowseListActivity extends Activity {

	private ArrayList<RowItem> item ;
	private ArrayList<String> path ;
	private TextView textViewsearch;
	private String root;	
	SingleImageItemList ItemListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_browse_list);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		textViewsearch = (TextView)findViewById(R.id.textViewsearch);
		// Get Root path 			
		root = Environment.getExternalStorageDirectory().getPath(); //For Device
		getDir(root);		   
	}	
	// Function for listing files and directories in specified path
	private void getDir(String dirpath)
	{		   		
		textViewsearch.setText(dirpath);
		item = new ArrayList<RowItem>();
		path = new ArrayList<String>();		 

		File f = new File(dirpath);				 
		File[] files = f.listFiles();
		//Nitish 13-03-2014
		if(f.listFiles() == null)  // If Storage Directory does not Exist in device or virtual Device
		{

			CustomToast.makeText(this, "Storage Directory not found", Toast.LENGTH_SHORT);					
			ItemListAdapter = new SingleImageItemList(item);
			ListView lv = (ListView) findViewById(R.id.listViewsearch);				
			lv.setAdapter(ItemListAdapter);	
			return;

		}		
		//Nitish End
		if(files.length == 0)
		{
			textViewsearch.setText("No File Present.");				
		}			

		for(int i=0 ;i <files.length ; i++)
		{
			RowItem d = new RowItem();

			File file = files[i];
			if(!file.isHidden() && file.canRead())
			{
				path.add(file.getPath());
				d.setValue(file.getName());
				if(file.isDirectory())  // If File is a directory then directory image
					d.setId(R.drawable.ic_folder);						
				else                    // If File is a File then File image
					d.setId(R.drawable.ic_file); 
				item.add(d);

			}				
		}	
		// Back Path 
		if(!dirpath.equals(root))
		{				
			RowItem d = new RowItem();				
			d.setValue("Go Back");
			item.add(d);				
			path.add(f.getParent()); //For Device
		}		
		ItemListAdapter = new SingleImageItemList(item);
		ListView lv = (ListView) findViewById(R.id.listViewsearch);				
		lv.setAdapter(ItemListAdapter);	

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {			
			@Override
			public void onItemClick(AdapterView<?> alist, View arg1, int pos,
					long arg3) {	

				File file = new File(path.get(pos));
				if(file.isDirectory())  //If Item clicked is a directory find sub directories and files present in that directory
				{
					if(file.canRead())
						getDir(path.get(pos));
					else
						textViewsearch.setText( file.getName() + "  Cannot be read. ");					

				}	
				else //If Item clicked is a file move to OtherBrowseActivity Page
				{
					RowItem r = (RowItem) alist.getItemAtPosition(pos);	
					Intent i = new Intent(OtherBrowseListActivity.this,OtherBrowseActivity.class);	
					i.putExtra("Key",textViewsearch.getText() + "/" + r.getValue() );  
					startActivity(i);
				}				
			}
		});				
	}	
	private class SingleImageItemList extends ArrayAdapter<RowItem>
	{

		public SingleImageItemList(ArrayList<RowItem> item) {
			super(OtherBrowseListActivity.this,R.layout.other_browse_list_items,item);		

		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) // If we weren't given a view, inflate one
		{			
			RowItem ritem  = getItem(position);	
			if (convertView == null) {
				convertView = OtherBrowseListActivity.this.getLayoutInflater().inflate(R.layout.other_browse_list_items, null);	
			}			
			TextView txtview = (TextView)convertView.findViewById(R.id.txtOtherBrowseList);
			ImageView imgview = (ImageView)convertView.findViewById(R.id.imgOtherBrowseList);		

			imgview.setImageResource(ritem.getId());
			txtview.setText(ritem.getValue());	

			return convertView;
		}		
	}
	@Override
	public void onResume() {
		super.onResume();	
		ItemListAdapter.notifyDataSetChanged();
	}	
	//Tamilselvan on 18-03-2014
	
}
