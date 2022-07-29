package in.nsoft.hescomspotbilling;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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
//Created Nitish 03/03/2014
public class ReportNotBilledActivity extends Activity implements AlertInterface
{

	ArrayList<ReportNotBilled> alRep ;
	ReportAdapter adapter;	
	private Button btnPrint;//Tamilselvan on 19-03-2014
	DatabaseHelper db = new DatabaseHelper(this);
	BluetoothDevice  printerDevice;
	BluetoothAdapter mBlueToothAdapter;
	BluetoothSocket mmSocket;
	ProgressDialog ringProgress;
	int i = 0;
	Handler dh ;
	static final int REQUEST_ENABLE_BT = 0;
	BluetoothPrinting bptr;
	final static String TAG = "in.nsoft.spotBilling.ReportNotBilledActivity";

	//Nitish 22-03-2014	
	CustomAlert cAlert ;
	boolean malertResult;
	final static String alertmsg = "Are you sure you want to Print?";
	final static String alerttitle = "Not Billed Report";
	final static String btnAlertYes = "Yes";
	final static String btnAlertNo = "No";
	final static int SingleButton = 0;
	final static int TwoButtons = 1;
	final static int Print = 2;	
	//Nitish End
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_not_billed);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		try 
		{			
			alRep = db.GetReportNotBilledList(); 
			btnPrint  = (Button)findViewById(R.id.btnReportNotBilledPrint);
			dh = new Handler();//Tamilselvan on 19-03-2014
			TextView txtDetails = (TextView)findViewById(R.id.lblReportNotBilledDetails);
			//If ArrayList alRep is null add a new empty object to ArrayList
			if(alRep.size() ==0)
			{
				CustomToast.makeText(this, "This Report Does Not Contain Data", Toast.LENGTH_SHORT);
				btnPrint.setVisibility(View.INVISIBLE);
				txtDetails.setVisibility(View.INVISIBLE);
			}
			//To get List Parameters
			adapter = new ReportAdapter(alRep);			
			ListView lv = (ListView) findViewById(R.id.lstReportNotBilledList);		
			lv.setAdapter(adapter);				

			btnPrint.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//Nitish 21-03-2014
					//Set Parameters of CustomAlert
					CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
					params.setContext(ReportNotBilledActivity.this);					
					params.setMainHandler(dh);
					params.setMsg(alertmsg);
					params.setButtonOkText(btnAlertNo);
					params.setButtonCancelText(btnAlertYes);
					params.setTitle(alerttitle);
					params.setFunctionality(Print);
					cAlert = new CustomAlert(params);					
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
	public class ConnectedThread extends Thread
	{		

		public void run(){
			PrinterCharacterAlign pca = new PrinterCharacterAlign();
			try 
			{

				//String LineText = "----------------------------------------------------";//52 0x0D				
				alRep = db.GetReportNotBilledList();				
				String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());				
				bptr.sendData(ConstantClass.data1);				
				bptr.sendData(ConstantClass.linefeed1);				
				bptr.sendData(pca.CenterAlign("NOT BILLED REPORT"," ").getBytes());//Title				
				bptr.sendData(pca.LineSeperation().getBytes());				
				bptr.sendData(pca.TwoParallelString("Print Date", timeStamp, ' ', ':').getBytes());//Print Date				
			    bptr.sendData(pca.CenterAlign("Details"," ").getBytes());//Details
				bptr.sendData(pca.ThreeParallelString("ConnectionNo", "RRNo", "TourPlanId", ' ').getBytes());//ColumnNames				
				bptr.sendData(pca.LineSeperation().getBytes());	
				for(ReportNotBilled rb : alRep)
				{					
					bptr.sendData(pca.ThreeParallelString(rb.getmConNo(), rb.getmRRNo().trim(), rb.getmTourplanId().trim(), ' ').getBytes());
					//bptr.sendData(ConstantClass.linefeed1);
					//Thread.sleep(300);	//Not Used as it is top 20 records
				}				
				bptr.sendData(pca.LineSeperation().getBytes());				
				bptr.sendData(ConstantClass.linefeed1);
				//bptr.sendData(ConstantClass.linefeed1);
							
				

			} catch (Exception e)
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
				catch (Exception e) {
					Log.d(TAG,e.toString());
					e.printStackTrace();
				}	
				ringProgress.dismiss();
			}
		}
		public void cancel()
		{
			try 
			{
				mmSocket.close();
			} 
			catch (Exception e) 
			{
				// TODO: handle exception
				Log.d(TAG,e.toString());
			}
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
		return OptionsMenu.navigate(item,ReportNotBilledActivity.this);			
	}
	//Class for List View
	private class ReportAdapter extends ArrayAdapter<ReportNotBilled>
	{

		public ReportAdapter(ArrayList<ReportNotBilled> mReportList) {		
			super(ReportNotBilledActivity.this, R.layout.report_notbilled_list, mReportList);		

		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) // If we weren't given a view, inflate one
		{
			try
			{			
				ReportNotBilled rb = getItem(position);
				if (convertView == null) {
					convertView = ReportNotBilledActivity.this.getLayoutInflater().inflate(R.layout.report_notbilled_list, null);					
				}	
				// Set List View Parameters
				TextView tcoid = (TextView)convertView.findViewById(R.id.txtNotBilledReportListConId);			
				tcoid.setText(String.valueOf(rb.getmConNo()));
				TextView tRRNo = (TextView)convertView.findViewById(R.id.txtNotBilledReportListRRNo);
				tRRNo.setText(String.valueOf(rb.getmRRNo()));
				TextView tTourPlanId = (TextView)convertView.findViewById(R.id.txtNotBilledReportListTourPlanId);
				tTourPlanId.setText(String.valueOf(rb.getmTourplanId()));					
			}		
			catch(Exception e)
			{
				Toast.makeText(ReportNotBilledActivity.this, "Error View " , Toast.LENGTH_LONG).show();

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
	public void performAction(boolean alertResult,int functionality) {

		//If alertResult = false Yes else No
		if(functionality==Print && !alertResult)
		{
			bptr = new BluetoothPrinting(ReportNotBilledActivity.this);
			ringProgress = ProgressDialog.show(ReportNotBilledActivity.this, "Please wait..", "Printing...",true);//Spinner
			ringProgress.setCancelable(false);
			try 
			{
				//Tamilselvan on 19-03-2014
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
				}).start();/**///Tamilselvan on 19-03-2014
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d(TAG,e.toString());
			}
		}
	}
}

