package in.nsoft.hescomspotbilling;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
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

public class ReportTPFeederActivity extends Activity implements AlertInterface{

	private TextView txtPrintDate,txtBilledDate;
	private Button btnPrint;
	private ArrayList<DDLItem> alConStatusItem;
	ArrayList<ReportTPFeeder> alRep ;
	ReportAdapter adapter;

	ProgressDialog ringProgress;	
	Handler dh ;
	//final static String sPaise = ".00";
	static final int REQUEST_ENABLE_BT = 0;	
	BluetoothPrinting bptr;
	final static String TAG = "in.nsoft.HescomspotBilling.ReportTPFeederActivity";


	CustomAlert cAlert ;
	boolean malertResult;
	final static String alertmsg = "Are you sure you want to Print?";
	final static String alerttitle = "Billed Report";
	final static String btnAlertYes = "Yes";
	final static String btnAlertNo = "No";

	private final DatabaseHelper mDb =new DatabaseHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_tpfeeder);

		try 
		{
			dh = new Handler();
			txtPrintDate = (TextView)findViewById(R.id.txtReportTPFeederPrintDate);	
			txtBilledDate = (TextView)findViewById(R.id.txtReportTPFeederBilledDate);
			btnPrint = (Button)findViewById(R.id.btnReportTPFeederPrint);

			String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());			
			String printdatetime = new CommonFunction().GetCurrentTime();

			txtPrintDate.setText(printdatetime);
			txtBilledDate.setText(currentDate); 

			//Get All Status List
			ReadBillingConsumptionStatus ConStatus = new ReadBillingConsumptionStatus(ReportTPFeederActivity.this);
			alConStatusItem = ConStatus.ReadStatus();

			alRep = mDb.GetReportTPFeeder(new CommonFunction().DateConvertRemoveChar(currentDate));

			if(alRep.size() ==0)
			{
				CustomToast.makeText(this, "Billing Data does not Exist today.", Toast.LENGTH_SHORT);
				btnPrint.setVisibility(View.INVISIBLE);				
			}
			//To get List Parameters
			adapter = new ReportAdapter(alRep);			
			ListView lv = (ListView) findViewById(R.id.lstReportTPFeeder);		
			lv.setAdapter(adapter);	
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			CustomToast.makeText(this, "Billing Data does not Exist today.", Toast.LENGTH_SHORT);
			e.printStackTrace();
		}

		btnPrint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//Nitish 21-03-2014
				//Set Parameters of CustomAlert
				CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
				params.setContext(ReportTPFeederActivity.this);					
				params.setMainHandler(dh);
				params.setMsg(alertmsg);
				params.setButtonOkText(btnAlertNo);
				params.setButtonCancelText(btnAlertYes);
				params.setTitle(alerttitle);
				params.setFunctionality(1);
				cAlert = new CustomAlert(params);					
				//End Nitish 21-03-2014
				//Cut from here


			}
		});	


	}
	public String GetStatusName(int prvId)
	{
		String PreValue = "";
		try
		{
			int x = 0;
			for(x = 0; x < alConStatusItem.size(); x++)
			{
				if(Integer.valueOf(alConStatusItem.get(x).getId()) == prvId)
				{				
					break;
				}
			}
			String[] PrvName =  alConStatusItem.get(x).getValue().split(":");
			PreValue = PrvName[0];
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return PreValue;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.spotbillingmenu, menu);
		return true;
	}@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return OptionsMenu.navigate(item,ReportTPFeederActivity.this);			
	}

	@Override
	public void performAction(boolean alertResult, int functionality) 
	{
		if(!alertResult)
		{		
			bptr = new BluetoothPrinting(ReportTPFeederActivity.this);
			ringProgress = ProgressDialog.show(ReportTPFeederActivity.this, "Please wait..", "Printing...",true);//Spinner
			ringProgress.setCancelable(false);

			try 
			{
				//Tamilselvan on 19-03-2014
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						//bptr.beginListenForData();
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
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d(TAG,e.toString());
			}	
		}
	}
	//Class for List View
	private class ReportAdapter extends ArrayAdapter<ReportTPFeeder>
	{

		public ReportAdapter(ArrayList<ReportTPFeeder> mReportList) {		
			super(ReportTPFeederActivity.this, R.layout.threeitemlist, mReportList);		

		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) // If we weren't given a view, inflate one
		{
			try
			{		
				ReportTPFeeder rb = getItem(position);
				if (convertView == null) {
					convertView = ReportTPFeederActivity.this.getLayoutInflater().inflate(R.layout.threeitemlist, null);					
				}	
				// Set List View Parameters
				TextView tvConnId = (TextView)convertView.findViewById(R.id.tvConnId);
				TextView tvReading = (TextView)convertView.findViewById(R.id.tvReading);
				TextView tvReason = (TextView)convertView.findViewById(R.id.tvReason);
				
				String con = rb.getmConnId()== null ? "" : rb.getmConnId();
				String reading = rb.getmReading()== null ? "" : rb.getmReading();
				String reason =  GetStatusName(Integer.valueOf(rb.getmReason()));
				
				tvConnId.setText(con);
				tvReading.setText(reading);
				tvReason.setText(reason);

			}		
			catch(Exception e)
			{
				Toast.makeText(ReportTPFeederActivity.this, "Error View " , Toast.LENGTH_LONG).show();			
			}
			return convertView;
		}
	}
	@Override
	public void onResume() {
		super.onResume();	
		adapter.notifyDataSetChanged();
	}	

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)	
	public class ConnectedThread extends Thread //ConnectedThread
	{	
		public void run(){
			PrinterCharacterAlign pca = new PrinterCharacterAlign();//Class Alignment for printer 
			CommonFunction cFun = new CommonFunction();	

			try 
			{


				String timeStamp = cFun.GetCurrentTime();

				String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());		
				alRep = mDb.GetReportTPFeeder(new CommonFunction().DateConvertRemoveChar(currentDate));

				bptr.sendData(ConstantClass.data1);
				bptr.sendData(ConstantClass.linefeed1);
				bptr.sendData(pca.LineSeperation().getBytes());	
				bptr.sendData(pca.CenterAlign("BILLED REPORT"," ").getBytes());//Title					
				bptr.sendData(pca.CenterAlign(timeStamp," ").getBytes());//TimeStamp				
				bptr.sendData(pca.TwoParallelString("BILLED DATE",currentDate, ' ', ':').getBytes());//Billed Date
				bptr.sendData(ConstantClass.linefeed1);	
				bptr.sendData(pca.ThreeParallelString("CONID", "READING", "REASON", ' ').getBytes());//ColumnNames					
				bptr.sendData(pca.LineSeperation().getBytes());	
				for(ReportTPFeeder rb : alRep) //Get Details From List
				{					
					String con = rb.getmConnId()== null ? "" : rb.getmConnId();
					String reading = rb.getmReading()== null ? "" : rb.getmReading();
					String reason =  GetStatusName(Integer.valueOf(rb.getmReason()));
					bptr.sendData(pca.ThreeParallelString(con, reading ,reason, ' ').getBytes());					
				}										
				bptr.sendData(pca.LineSeperation().getBytes());	
				bptr.sendData(ConstantClass.linefeed1);
				bptr.sendData(ConstantClass.linefeed1);									
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
				catch (Exception e) {
					Log.d(TAG,e.toString());
					e.printStackTrace();
				}	
				//Tamilselvan on 19-03-2014		
				ringProgress.dismiss();
			}
		}
		public void cancel()
		{

		}
	}//END ConnectedThread
	//END Bluetooth Code====================================================================================


}
