package in.nsoft.hescomspotbilling;
import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
// Created Nitish 06-03-2014
public class ReportStatusListActivity extends Activity {
	
	ArrayList<ReportStatus> alRep ;
	ReportAdapter adapter;	
	DatabaseHelper db = new DatabaseHelper(this);	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_status_list);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		try
		{
			alRep = new  ArrayList <ReportStatus>();
			alRep = db.GetReportStatusList();				
			//To get List Parameters
			adapter = new ReportAdapter(alRep);			
			ListView lv = (ListView) findViewById(R.id.lstReportStatusList);		
			lv.setAdapter(adapter);
			
		} 
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.spotbillingmenu, menu);
		return true;
	}
	//Class for List View
		private class ReportAdapter extends ArrayAdapter<ReportStatus>
		{
			
			public ReportAdapter(ArrayList<ReportStatus> mReportList) {		
			super(ReportStatusListActivity.this, R.layout.report_status_list_items, mReportList);		
			
			}
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) // If we weren't given a view, inflate one
			{
			try
			{			
				ReportStatus rb = getItem(position);
				if (convertView == null) {
					convertView = ReportStatusListActivity.this.getLayoutInflater().inflate(R.layout.report_status_list_items, null);					
				}
				String mDate = (new CommonFunction()).DateConvertAddChar(rb.getmBillDate().trim(),"-");
				// Set List View Parameters
				TextView tBilledDate = (TextView)convertView.findViewById(R.id.txtStatusReportListBilledDate);				
				TextView tBilled = (TextView)convertView.findViewById(R.id.txtStatusReportListBilled);	
				TextView tnormal = (TextView)convertView.findViewById(R.id.txtStatusReportListNormal);
				TextView tdl = (TextView)convertView.findViewById(R.id.txtStatusReportListDL);
				TextView tmnr = (TextView)convertView.findViewById(R.id.txtStatusReportListMNR);
				TextView tdover = (TextView)convertView.findViewById(R.id.txtStatusReportListDO);
				TextView tmch = (TextView)convertView.findViewById(R.id.txtStatusReportListMCH);
				TextView trnf = (TextView)convertView.findViewById(R.id.txtStatusReportListRNF);
				TextView tva = (TextView)convertView.findViewById(R.id.txtStatusReportListVA);
				TextView tdis = (TextView)convertView.findViewById(R.id.txtStatusReportListDIS);
				TextView tms = (TextView)convertView.findViewById(R.id.txtStatusReportListMS);
				TextView tdir = (TextView)convertView.findViewById(R.id.txtStatusReportListDIR);
				TextView tmb = (TextView)convertView.findViewById(R.id.txtStatusReportListMB);
				TextView tnt = (TextView)convertView.findViewById(R.id.txtStatusReportListNT);		
				
				tBilledDate.setText(mDate);				
				tBilled.setText(String.valueOf(rb.getmBilled()));				
				tnormal.setText(String.valueOf(rb.getmNormal()));
				tdl.setText(String.valueOf(rb.getmDL()));
				tmnr.setText(String.valueOf(rb.getmMNR()));
				tdover.setText(String.valueOf(rb.getmDO()));
				tmch.setText(String.valueOf(rb.getmMCH()));
				trnf.setText(String.valueOf(rb.getmRNF()));
				tva.setText(String.valueOf(rb.getmVA()));
				tdis.setText(String.valueOf(rb.getmDIS()));
				tms.setText(String.valueOf(rb.getmMS()));
				tdir.setText(String.valueOf(rb.getmDIR()));
				tmb.setText(String.valueOf(rb.getmMB()));
				tnt.setText(String.valueOf(rb.getmNT()));			
			}		
			catch(Exception e)
			{
				Toast.makeText(ReportStatusListActivity.this, "Error View " , Toast.LENGTH_LONG).show();
				
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


