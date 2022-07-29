package in.nsoft.hescomspotbilling;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//Created Nitish 03/03/2014
public class ReportListActivity extends Activity {

	SingleItemList ItemListAdapter; 
	private String xml = "reportnavigation.xml" ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_list);	
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		ReadDynamicNavigation rdynamiclist = new ReadDynamicNavigation(ReportListActivity.this);
		ArrayList<DDLItem> alitems = rdynamiclist.Read(xml);		
		ItemListAdapter = new SingleItemList(alitems);	
		ListView lv = (ListView) findViewById(R.id.listViewOtherList);		
		lv.setAdapter(ItemListAdapter);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {			
			@Override
			public void onItemClick(AdapterView<?> alist, View arg1, int pos,
					long arg3) {	
				try
				{
					DDLItem k = (DDLItem)  alist.getItemAtPosition(pos);				
					Intent i = new Intent(ReportListActivity.this,Class.forName(k.getId()));					
					startActivity(i);
				}
				catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		});		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.spotbillingmenu, menu);
		return true;
	}	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return OptionsMenu.navigate(item,ReportListActivity.this);		
	}
	private class SingleItemList extends ArrayAdapter<DDLItem>
	{	
		public SingleItemList(ArrayList<DDLItem> otherlist) {
			super(ReportListActivity.this,R.layout.other_list_items ,otherlist);
			// TODO Auto-generated constructor stub
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) // If we weren't given a view, inflate one
		{
			if (convertView == null) {
				convertView = ReportListActivity.this.getLayoutInflater().inflate(R.layout.other_list_items, null);					
			}	
			DDLItem olist  = getItem(position);
			
			TextView lblOtherMenuItem = (TextView)convertView.findViewById(R.id.lblOtherMenuItem);
			lblOtherMenuItem.setText(olist.getValue());
			return convertView;
		}		
	}	
	@Override
	public void onResume() {
	super.onResume();	
	ItemListAdapter.notifyDataSetChanged();
	}	
	//Tamilselvan on 18-03-2014
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		//super.onBackPressed();
		CustomToast.makeText(ReportListActivity.this, "Please use menu to navigate.", Toast.LENGTH_SHORT);
		return;
	}
}
