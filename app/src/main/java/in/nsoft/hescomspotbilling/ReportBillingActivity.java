package in.nsoft.hescomspotbilling;


//Created Nitish 04/03/2014


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ReportBillingActivity extends Activity implements AlertInterface{

	private Button btnPrint;//Tamilselvan on 19-03-2014
	ArrayList<ReportBilling> alRep ;
	ReportAdapter adapter;
	DatabaseHelper db = new DatabaseHelper(this);
	ProgressDialog ringProgress;
	
	int i = 0;
	Handler dh ;
	BluetoothPrinting bptr;
	final static String TAG = "in.nsoft.spotBilling.ReportBillingActivity";

	//Nitish 22-03-2014	
	CustomAlert cAlert;
	boolean malertResult;
	final static String alertmsg = "Select type of Printing";
	final static String alerttitle = "Billing Report";
	final static String btnAlertSummary = "Summary";
	final static String btnAlertDetails = "Details";
	final static String btnAlertCancel = "Cancel";
	final static int SingleButton = 0;
	final static int TwoButtons = 1;
	final static int Print = 2;	
	//Nitish End

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_billing);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		btnPrint  = (Button)findViewById(R.id.btnBillingReportMainPrint);
		String mrname = "";
		dh = new Handler();//Tamilselvan on 19-03-2014
		try 
		{			
			alRep = db.GetReportBillingList();			
			String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());			
			int tot=0,bill=0,nb=0;	
			if(alRep.size() ==0)
			{
				CustomToast.makeText(this, "This Report Does Not Contain Data", Toast.LENGTH_SHORT);
				btnPrint.setVisibility(View.INVISIBLE);
			}					
			for(ReportBilling alsummary : alRep)
			{
				tot = tot + alsummary.getmTotalInstallations();
				bill = bill + alsummary.getmBilled();
				nb = nb + alsummary.getmNotBilled();
				mrname = alsummary.getmMRName();
			}				
			TextView name = (TextView)findViewById(R.id.txtBillingReportMainMRName);	
			TextView printDate = (TextView)findViewById(R.id.txtBillingReportMainPrintDate);	
			TextView totalInstallations = (TextView)findViewById(R.id.txtBillingReportMainTotal);
			TextView billed = (TextView)findViewById(R.id.txtBillingReportMainBilled);
			TextView notBilled = (TextView)findViewById(R.id.txtBillingReportMainNotBilled);

			//Set parameters not in list(Summary)
			name.setText(mrname);
			printDate.setText(timeStamp);
			totalInstallations.setText(String.valueOf(tot));
			billed.setText(String.valueOf(bill));
			notBilled.setText(String.valueOf(nb));

			//To get List Parameters
			adapter = new ReportAdapter(alRep);			
			ListView lv = (ListView) findViewById(R.id.lstBillingReportList);		
			lv.setAdapter(adapter);			

			btnPrint.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//BlueToothPrinter();
					//Nitish 21-03-2014
					//Set Parameters of CustomAlert
					CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
					params.setContext(ReportBillingActivity.this);					
					params.setMainHandler(dh);
					params.setMsg(alertmsg);
					params.setButtonOkText(btnAlertSummary);
					params.setButtonCancelText(btnAlertDetails);
					params.setButtonNeutralText(btnAlertCancel);
					params.setTitle(alerttitle);
					params.setFunctionality(Print);
					cAlert  = new CustomAlert(params);					
					//End Nitish 21-03-2014
					//Cut from here		
				}
			});

		}
		catch (Exception e)
		{			
			e.printStackTrace();
		}

	}
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
	//onActivityResult for checking whether bluetooth enabled
	public class ConnectedThread extends Thread
	{	
		public void run(){
			PrinterCharacterAlign pca = new PrinterCharacterAlign();			
			try 
			{
				//String LineText = "----------------------------------------------------";//52				
				alRep = db.GetReportBillingList();
				String mrname = "";
				String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());				
				int tot=0,bill=0,nb=0;				
				for(ReportBilling alsummary : alRep)
				{
					tot = tot + alsummary.getmTotalInstallations();
					bill = bill + alsummary.getmBilled();
					nb = nb + alsummary.getmNotBilled();
					mrname = alsummary.getmMRName();
				}
				bptr.sendData(ConstantClass.data1);			
				bptr.sendData(ConstantClass.linefeed1);				
				bptr.sendData(pca.CenterAlign("BILLING REPORT"," ").getBytes());//Title			;	
				bptr.sendData(pca.LineSeperation().getBytes());				
				bptr.sendData(pca.CenterAlign("SUMMARY"," ").getBytes());//Summary
				bptr.sendData(pca.LineSeperation().getBytes());			
				bptr.sendData(pca.TwoParallelString("Print Date", timeStamp, ' ', ':').getBytes());//Print Date					
				bptr.sendData(pca.TwoParallelString("MR Name", mrname, ' ', ':').getBytes());//MR Name				
				bptr.sendData(pca.TwoParallelString("Total Inst", String.valueOf(tot), ' ', ':').getBytes());//Total Installations
				bptr.sendData(pca.TwoParallelString("Billed", String.valueOf(bill), ' ', ':').getBytes()); //Billed
				bptr.sendData(pca.TwoParallelString("Not Billed", String.valueOf(nb), ' ', ':').getBytes()); //Not Billed	
				bptr.sendData(pca.LineSeperation().getBytes());	
				if(!malertResult) //Nitish 21-03-2014
				{
					bptr.sendData(pca.CenterAlign("DETAILS"," ").getBytes());//Details
					bptr.sendData(ConstantClass.linefeed1);				
					bptr.sendData(pca.LineSeperation().getBytes());	
					for(ReportBilling rb : alRep)
					{
						bptr.sendData(pca.TwoParallelString("Billed Date",(new CommonFunction()).DateConvertAddChar(rb.getmBillDate().trim(),"-"), ' ', ':').getBytes()); //Billed Date
						bptr.sendData(pca.TwoParallelString("Total Installations", String.valueOf(rb.getmTotalInstallations()), ' ', ':').getBytes()); //Total Installations
						bptr.sendData(pca.TwoParallelString("Billed", String.valueOf(rb.getmBilled()), ' ', ':').getBytes()); // Billed
						bptr.sendData(pca.TwoParallelString("Not Billed", String.valueOf(rb.getmNotBilled()), ' ', ':').getBytes()); //Not Billed
						bptr.sendData(pca.LineSeperation().getBytes());			
						//Thread.sleep(350);

					}
				} //Nitish End 21-03-2014				
				bptr.sendData(ConstantClass.linefeed1);
				bptr.sendData(ConstantClass.linefeed1);					
				//i = 0;
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				Log.d(TAG,e.toString());
				//if printer turn off without of server, then after printer turn on process again start	
			}
			finally
			{
				try 
				{
					bptr.closeBT();
					if(BluetoothPrinting.isConnected)
					{
						Thread.sleep(5000);
					}
				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//Tamilselvan on 19-03-2014
				ringProgress.dismiss();
			}
		}
		public void cancel()
		{
			
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
		return OptionsMenu.navigate(item,ReportBillingActivity.this);			
	}
	//Class for List View
	private class ReportAdapter extends ArrayAdapter<ReportBilling>
	{

		public ReportAdapter(ArrayList<ReportBilling> mReportList) {		
			super(ReportBillingActivity.this, R.layout.report_billing_list, mReportList);		

		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) // If we weren't given a view, inflate one
		{
			try
			{		
				ReportBilling rb = getItem(position);
				if (convertView == null) {
					convertView = ReportBillingActivity.this.getLayoutInflater().inflate(R.layout.report_billing_list, null);					
				}	
				// Set List View Parameters
				TextView txtBilledDate = (TextView)convertView.findViewById(R.id.txtBillingReportBilledDate);				
				txtBilledDate.setText(rb.getmBillDate()=="" ? "" : (new CommonFunction()).DateConvertAddChar(rb.getmBillDate().trim(),"-"));
				TextView txtTotalInstallations = (TextView)convertView.findViewById(R.id.txtBillingReportListTotalInstallations);
				txtTotalInstallations.setText(String.valueOf(rb.getmTotalInstallations()));
				TextView txtBillingReportBilled = (TextView)convertView.findViewById(R.id.txtBillingReportBilled);
				txtBillingReportBilled.setText(String.valueOf(rb.getmBilled()));
				TextView txtBillingReportNotBilled = (TextView)convertView.findViewById(R.id.txtBillingReportNotBilled);
				txtBillingReportNotBilled.setText(String.valueOf(rb.getmNotBilled()));		
			}		
			catch(Exception e)
			{
				Toast.makeText(ReportBillingActivity.this, "Error View " , Toast.LENGTH_LONG).show();			
			}
			return convertView;
		}
	}
	@Override
	public void onResume() {
		super.onResume();	
		adapter.notifyDataSetChanged();
	}	
	// This function is implementing interface AlertInterface is called at CustomAlert class 
	@Override
	public void performAction(boolean alertResult, int functionality)
	{
		malertResult = alertResult;	//If alertResult = true summary else details
		if(functionality == Print)
		{		
			bptr = new BluetoothPrinting(ReportBillingActivity.this);	
			ringProgress = ProgressDialog.show(ReportBillingActivity.this, "Please wait..", "Printing...",true);//Spinner
			ringProgress.setCancelable(false);
			try 
			{
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Looper.prepare();
							bptr.openBT();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ConnectedThread cth = new ConnectedThread();
						cth.start(); 
						Looper.loop();						
					}
				}).start();
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d(TAG,e.toString());
			}	
		}
	}

}
