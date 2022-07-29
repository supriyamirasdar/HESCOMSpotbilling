package in.nsoft.hescomspotbilling;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ReportDayWiseCollectionListActivity extends Activity {

	ArrayList<String> alRep ;
	ReportAdapter adapter;	
	DatabaseHelper db = new DatabaseHelper(this);	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_day_wise_collection_list);

		try
		{
			alRep = new  ArrayList <String>();

			alRep = db.GetCashCounterBatchNos();
			if(alRep.size() ==0)
			{
				CustomToast.makeText(this, "Day Wise Collection Data does not exist.", Toast.LENGTH_SHORT);

			}			
			//To get List Parameters
			adapter = new ReportAdapter(alRep);			
			ListView lv = (ListView) findViewById(R.id.lstReportDayWiseCollection);		
			lv.setAdapter(adapter);
		}
		catch(Exception e)
		{
			Toast.makeText(ReportDayWiseCollectionListActivity.this, "Collection Data does not exist." , Toast.LENGTH_LONG).show();			
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
		return OptionsMenu.navigate(item,ReportDayWiseCollectionListActivity.this);			
	}
	//Class for List View
	private class ReportAdapter extends ArrayAdapter<String>
	{

		public ReportAdapter(ArrayList<String> mReportList) {		
			super(ReportDayWiseCollectionListActivity.this, R.layout.report_daywisecollection_list, mReportList);		

		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) // If we weren't given a view, inflate one
		{
			try
			{			

				String nBatchNoDate[] = getItem(position).split("#"); 
				//Gets BatchNo and Batch Date seperated by #    nBatchNoDate[0] = BatchNo     nBatchNoDate[1] = BatchDate				
				String nBatchNo = nBatchNoDate[0];
				String nBatchDate = nBatchNoDate[1];				

				if (convertView == null) {
					convertView = ReportDayWiseCollectionListActivity.this.getLayoutInflater().inflate(R.layout.report_daywisecollection_list, null);					
				}			
				// Set List View Parameter ( Set Not Sent ConnId)
				TextView tBatchNo = (TextView)convertView.findViewById(R.id.txtReportDayWiseCollectionListBatchNo);	
				TextView tBatchDate = (TextView)convertView.findViewById(R.id.txtReportDayWiseCollectionListDate);	
				TextView tMRName = (TextView)convertView.findViewById(R.id.txtReportDayWiseCollectionListMRName);	
				TextView tRpts = (TextView)convertView.findViewById(R.id.txtReportDayWiseCollectionListReceipts);	
				TextView tCollAmt = (TextView)convertView.findViewById(R.id.txtReportDayWiseCollectionListCollAmt);	

				tBatchNo.setText(nBatchNo);	 //Batch No
				tBatchDate.setText((new CommonFunction()).DateConvertAddChar(nBatchDate, "-")); //Batch Date				
				tMRName.setText(db.GetMRName()); //MR Name 	
				tRpts.setText(String.valueOf(db.GetCountforBatchColl(nBatchNo)));//Total Receipt for this batch
				tCollAmt.setText(db.GetCollectionAmount(nBatchNo)+ ConstantClass.sPaise);//Total Collection amount for this batch

			}		
			catch(Exception e)
			{
				CustomToast.makeText(ReportDayWiseCollectionListActivity.this, "Error in List View.", Toast.LENGTH_SHORT);			
			}
			return convertView;
		}
	}
	@Override
	public void onResume() {
		super.onResume();	
		adapter.notifyDataSetChanged();
	}		

}
