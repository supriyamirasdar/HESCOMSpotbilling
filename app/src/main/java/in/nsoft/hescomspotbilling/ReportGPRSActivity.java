//Nitish 11-04-2014
package in.nsoft.hescomspotbilling;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ReportGPRSActivity extends Activity implements AlertInterface{

	ReportGPRS rb;
	DatabaseHelper db = new DatabaseHelper(this);
	private Button btnPrint,btnBillNotSent,btnRptsNotSent,btnDisNotSent;
	private ImageView imgRefresh;
	ProgressDialog ringProgress;
	int i = 0;
	Handler dh ;
	static final int REQUEST_ENABLE_BT = 0;	
	static final int REQUEST_BILL_NOTSENT = 1;	
	static final int REQUEST_RPT_NOTSENT = 2;	
	BluetoothPrinting bptr;
	final static String TAG = "in.nsoft.spotBilling.ReportGPRSActivity";

	//Nitish 24/04/2014	
	CustomAlert cAlert ;
	boolean malertResult;
	final static String alertmsg = "Are you sure you want to Print?";
	final static String alerttitle = "GPRS Report";
	final static String btnAlertYes = "Yes";
	final static String btnAlertNo = "No";
	final static int SingleButton = 0;
	final static int TwoButtons = 1;
	final static int Print = 2;	
	//Nitish End

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_report_gprs);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		try 
		{			
			rb = db.GetReportGPRSStatus();
			btnPrint  = (Button)findViewById(R.id.btnGPRSReportMainGPRSStatus);
			btnBillNotSent  = (Button)findViewById(R.id.btnGPRSReportMainBillsNotSent);
			btnRptsNotSent  = (Button)findViewById(R.id.btnGPRSReportMainReceiptsNotSent);
			btnDisNotSent = (Button)findViewById(R.id.btnGPRSReportMainDisconnNotSent);//22-04-2021
			imgRefresh = (ImageView)findViewById(R.id.imgRefresh);

			dh = new Handler();
			if(rb.getmTotalInstallations() == 0)
			{				
				CustomToast.makeText(ReportGPRSActivity.this, "This Report Does Not Contain Data", Toast.LENGTH_SHORT);
				btnPrint.setVisibility(View.INVISIBLE);
			}						
			String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
			TextView name = (TextView)findViewById(R.id.txtGPRSReportMainMRName);	
			TextView printDate = (TextView)findViewById(R.id.txtGPRSReportMainPrintDate);	
			TextView totalInstallations = (TextView)findViewById(R.id.txtGPRSReportMainTotal);
			TextView billed = (TextView)findViewById(R.id.txtGPRSReportMainBilled);						
			TextView gprssent = (TextView)findViewById(R.id.txtGPRSReportMainGPRSSent);
			TextView gprsnotsent = (TextView)findViewById(R.id.txtGPRSReportMainGPRSNotSent);

			//Nitish 06-06-2014
			TextView gprsReceipts = (TextView)findViewById(R.id.txtGPRSReportMainGPRSReceipts);  
			TextView gprsReceiptsSent = (TextView)findViewById(R.id.txtGPRSReportMainGPRSReceiptsGPRSSent); 
			TextView gprsReceiptsNotSent = (TextView)findViewById(R.id.txtGPRSReportMainGPRSReceiptsGPRSNotSent); 
			
			//Nitish 22-04-2021
			TextView txtGPRSReportMainGPRSDisconnBilled = (TextView)findViewById(R.id.txtGPRSReportMainGPRSDisconnBilled);  
			TextView txtGPRSReportMainGPRSDisconnGPRSSent = (TextView)findViewById(R.id.txtGPRSReportMainGPRSDisconnGPRSSent); 
			TextView txtGPRSReportMainGPRSDisconnGPRSNotSent = (TextView)findViewById(R.id.txtGPRSReportMainGPRSDisconnGPRSNotSent); 


			name.setText(rb.getmMRName());
			printDate.setText(timeStamp);
			totalInstallations.setText(String.valueOf(rb.getmTotalInstallations()));
			billed.setText(String.valueOf(rb.getmBilled()));			
			gprssent.setText(String.valueOf(rb.getmGPRS_Sent()));			
			gprsnotsent.setText(String.valueOf(rb.getmBilled() - rb.getmGPRS_Sent()));  // GPRS Not Sent = (Billed - GPRS Sent)

			//Nitish 06-06-2014
			String value = db.GPRSFlagColl();			
			String[] splitvalue = value.split("/");
			gprsReceipts.setText(splitvalue[1]);   //Total Receipts
			gprsReceiptsSent.setText(splitvalue[0]);  //GPRS Sent
			gprsReceiptsNotSent.setText(String.valueOf(Integer.parseInt(splitvalue[1]) - Integer.parseInt(splitvalue[0]))); //GPRS Not Sent = (Receipts - GPRS Sent)

			//Nitish 06-06-2014
			String valuedis = db.GPRSFlagDisConn();			
			String[] splitvaluedis = valuedis.split("/");
			txtGPRSReportMainGPRSDisconnBilled.setText(splitvaluedis[1]);   //Total DisconnBilled
			txtGPRSReportMainGPRSDisconnGPRSSent.setText(splitvaluedis[0]);  //GPRS Sent
			txtGPRSReportMainGPRSDisconnGPRSNotSent.setText(String.valueOf(Integer.parseInt(splitvaluedis[1]) - Integer.parseInt(splitvaluedis[0]))); //GPRS Not Sent = (DisconnBilled - GPRS Sent)
			
			btnPrint.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					//Nitish 21-03-2014
					//Set Parameters of CustomAlert
					CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
					params.setContext(ReportGPRSActivity.this);					
					params.setMainHandler(dh);
					params.setMsg(alertmsg);
					params.setButtonOkText(btnAlertNo);
					params.setButtonCancelText(btnAlertYes);
					params.setTitle(alerttitle);
					params.setFunctionality(Print);
					cAlert  = new CustomAlert(params);					
					//End Nitish 21-03-2014
					//Cut from here				

				}
			});	
			btnBillNotSent.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent i = new Intent(ReportGPRSActivity.this,ReportGPRSNotSentList.class);
					i.putExtra("Key", ConstantClass.REQUEST_BILL_NOTSENT);
					startActivity(i);
				}
			});

			btnRptsNotSent.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent i = new Intent(ReportGPRSActivity.this,ReportGPRSNotSentList.class);
					i.putExtra("Key", ConstantClass.REQUEST_RPT_NOTSENT);
					startActivity(i);
				}
			});
			btnDisNotSent.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent i = new Intent(ReportGPRSActivity.this,ReportGPRSNotSentList.class);
					i.putExtra("Key", ConstantClass.REQUEST_DIS_NOTSENT);
					startActivity(i);
				}
			});

			imgRefresh.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub	
					CustomToast.makeText(ReportGPRSActivity.this, "Refresh of GPRS Report Complete.", Toast.LENGTH_SHORT);
					onCreate(new Bundle()); //Refresh or Load Again					
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
				rb = db.GetReportGPRSStatus();				
				String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());				
				bptr.sendData(ConstantClass.data1);				
				bptr.sendData(ConstantClass.linefeed1);								
				bptr.sendData(pca.CenterAlign("GPRS STATUS REPORT"," ").getBytes());//Title	
				bptr.sendData(pca.LineSeperation().getBytes());				
				bptr.sendData(pca.CenterAlign("SUMMARY"," ").getBytes());//Summary
				bptr.sendData(pca.LineSeperation().getBytes());				
				bptr.sendData(pca.TwoParallelString("Print Date", timeStamp, ' ', ':').getBytes());//Print Date				
				bptr.sendData(pca.TwoParallelString("MR Name", rb.getmMRName(), ' ', ':').getBytes());//MR Name				
				bptr.sendData(pca.TwoParallelString("Total Inst", String.valueOf(rb.getmTotalInstallations()), ' ', ':').getBytes());//Total Installations

				//Nitish 06-05-2014				
				bptr.sendData(ConstantClass.linefeed1);
				bptr.sendData(pca.TwoParallelString("Billed", String.valueOf(rb.getmBilled()), ' ', ':').getBytes()); //Billed					
				bptr.sendData(pca.TwoParallelString("GPRS Sent", String.valueOf(rb.getmGPRS_Sent()), ' ', ':').getBytes()); //GPRS Sent	
				bptr.sendData(pca.TwoParallelString("GPRS Not Sent", String.valueOf(rb.getmBilled() - rb.getmGPRS_Sent()), ' ', ':').getBytes()); //GPRS Not Sent = Billed - 	GPRS Sent	
				bptr.sendData(ConstantClass.linefeed1);

				String value = db.GPRSFlagColl();			
				String[] splitvalue = value.split("/");

				bptr.sendData(pca.TwoParallelString("Receipts", splitvalue[1], ' ', ':').getBytes()); //Receipts					
				bptr.sendData(pca.TwoParallelString("GPRS Sent", splitvalue[0], ' ', ':').getBytes()); //GPRS Sent	
				bptr.sendData(pca.TwoParallelString("GPRS Not Sent", String.valueOf(Integer.parseInt(splitvalue[1]) - Integer.parseInt(splitvalue[0])), ' ', ':').getBytes()); //GPRS Not Sent = Receipts - 	GPRS Sent	
				bptr.sendData(pca.LineSeperation().getBytes());								
				bptr.sendData(ConstantClass.linefeed1);
				

				//i = 0;

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
		return OptionsMenu.navigate(item,ReportGPRSActivity.this);			
	}
	@Override
	//Nitish 24/04/2014
	public void performAction(boolean alertResult,int functionality) {
		// TODO Auto-generated method stub
		//If alertResult = false Yes else No
		if(!alertResult)
		{
			bptr = new BluetoothPrinting(ReportGPRSActivity.this);
			ringProgress = ProgressDialog.show(ReportGPRSActivity.this, "Please wait..", "Printing...",true);//Spinner
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
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		CustomToast.makeText(ReportGPRSActivity.this, "Please use menu to navigate..", Toast.LENGTH_SHORT);
		return;
	}
}
