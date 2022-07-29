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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ReportGPRSNotSentList extends Activity {

	ArrayList<GPRSReportNotSent> alRep ;
	ReportAdapter adapter;	
	TextView lblDetails,lblConId,lblStatus;
	DatabaseHelper db = new DatabaseHelper(this);	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_gprsnot_sent_list);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		try
		{
			lblDetails = (TextView)findViewById(R.id.lblReportGPRSNotSentDetails);
			lblConId = (TextView)findViewById(R.id.lblReportGPRSNotSentConnId);
			lblStatus = (TextView)findViewById(R.id.lblReportGPRSNotSentStatus);
			alRep = new  ArrayList <GPRSReportNotSent>();			
			Intent newintent = getIntent();
			int val = newintent.getIntExtra("Key", 0);
			if(val == ConstantClass.REQUEST_BILL_NOTSENT)  //ConnId of Bills whose GPRS is not Sent
			{			
				alRep = db.GetBillingConnIdGPRSNotSent();				
			}
			else if(val == ConstantClass.REQUEST_RPT_NOTSENT)  //ConnId of Bills whose GPRS is not Sent
			{			
				alRep = db.GetReceiptsConnIdGPRSNotSent();				
			}
			else if(val == ConstantClass.REQUEST_DIS_NOTSENT)  //ConnId of DisconnectionBills whose GPRS is not Sent
			{			
				alRep = db.GetDisconnConnIdGPRSNotSent();				
			}
			if(alRep.size() ==0)
			{
				lblConId.setVisibility(View.INVISIBLE);
				lblDetails.setVisibility(View.INVISIBLE);
				lblStatus.setVisibility(View.INVISIBLE);
				CustomToast.makeText(this, "All Records sent through GPRS.", Toast.LENGTH_SHORT);

			}
			//To get List Parameters
			adapter = new ReportAdapter(alRep);			
			ListView lv = (ListView) findViewById(R.id.lstReportGPRSNotSentConnId);		
			lv.setAdapter(adapter);
		}
		catch(Exception e)
		{
			Toast.makeText(ReportGPRSNotSentList.this, "GPRS Data Does not Exist." , Toast.LENGTH_LONG).show();			
		}
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
		return OptionsMenu.navigate(item,ReportGPRSNotSentList.this);			
	}

	//Class for List View
	private class ReportAdapter extends ArrayAdapter<GPRSReportNotSent>
	{

		public ReportAdapter(ArrayList<GPRSReportNotSent> mReportList) {		
			super(ReportGPRSNotSentList.this, R.layout.report_gprs_notsent_list_items, mReportList);		

		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) // If we weren't given a view, inflate one
		{
			try
			{			
				GPRSReportNotSent rb = getItem(position);
				if (convertView == null) {
					convertView = ReportGPRSNotSentList.this.getLayoutInflater().inflate(R.layout.report_gprs_notsent_list_items, null);					
				}			
				// Set List View Parameter ( Set Not Sent ConnId)
				TextView tConIdNotSent = (TextView)convertView.findViewById(R.id.txtReportGPRSNotSentItemsConId);			
				tConIdNotSent.setText(String.valueOf(rb.getmConNo()));
				TextView tConIdNotSentStatus = (TextView)convertView.findViewById(R.id.txtReportGPRSNotSentItemsStatus);	
				if(rb.getmGPRSNotsent() == null)
				{
					tConIdNotSentStatus.setText("Not Sent");

				}
				else
				{
					tConIdNotSentStatus.setText(String.valueOf(rb.getmGPRSNotsent()));
				}				
			}		
			catch(Exception e)
			{
				CustomToast.makeText(ReportGPRSNotSentList.this, "Error in List View.", Toast.LENGTH_SHORT);			
			}
			return convertView;
		}
	}
	@Override
	public void onResume() {
		super.onResume();	
		adapter.notifyDataSetChanged();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		Intent i = new Intent(ReportGPRSNotSentList.this,ReportGPRSActivity.class);		
		startActivity(i);
	}

}

