package in.nsoft.hescomspotbilling;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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
import android.widget.TextView;
import android.widget.Toast;
//Created Nitish 03/03/2014
public class ReportStatusActivity extends Activity implements AlertInterface{

	ArrayList<ReportStatus> alRep ;		
	DatabaseHelper db = new DatabaseHelper(this);
	private Button btnPrint,btnDetails;	
	BluetoothDevice  printerDevice;
	BluetoothAdapter mBlueToothAdapter;
	BluetoothSocket mmSocket;
	ProgressDialog ringProgress;
	int i = 0;
	Handler dh ;
	static final int REQUEST_ENABLE_BT = 0;
	BluetoothPrinting bptr;
	final static String TAG = "in.nsoft.spotBilling.ReportStatusActivity";
	//Nitish 21-03-2014
	CustomAlert cAlert; 
	boolean malertResult;
	final static String alertmsg = "Select type of Printing";
	final static String alerttitle = "Status Report";
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
		setContentView(R.layout.activity_report_status);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		btnPrint = (Button)findViewById(R.id.btnStatusReportMainPrint);
		btnDetails = (Button)findViewById(R.id.btnStatusReportMainDetails);
		dh = new Handler();//Tamilselvan on 19-03-2014
		try
		{
			alRep = new  ArrayList <ReportStatus>();
			alRep = db.GetReportStatusList();				

			//To get Summary	
			String mrname = "";
			String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());			
			int tot=0,bill=0,normal=0,dl=0,mnr=0,dover=0,mch=0,rnf=0,va=0,dis=0,ms=0,dir=0,mb=0,nt=0;

			if(alRep.size() ==0)
			{
				CustomToast.makeText(this, "This Report Does Not Contain Data", Toast.LENGTH_SHORT);
				btnPrint.setVisibility(View.INVISIBLE);
				btnDetails.setVisibility(View.INVISIBLE);
			}			
			for(ReportStatus alsummary : alRep)
			{
				tot = tot + alsummary.getmTotalInstallations();
				bill = bill + alsummary.getmBilled();				
				mrname = alsummary.getmMRName();

				normal = normal + alsummary.getmNormal();
				dl = dl + alsummary.getmDL();
				mnr = mnr + alsummary.getmMNR();
				dover = dover + alsummary.getmDO();
				mch = mch + alsummary.getmMCH();
				rnf = rnf + alsummary.getmRNF();
				va = va + alsummary.getmVA();
				dis = dis + alsummary.getmDIS();
				ms = ms + alsummary.getmMS();
				dir = dir + alsummary.getmDIR();
				mb = mb + alsummary.getmMB();
				nt = nt + alsummary.getmNT();
			}					
			TextView tname = (TextView)findViewById(R.id.txtStatusReportMainMRName);	
			TextView tprintDate = (TextView)findViewById(R.id.txtStatusReportMainPrintDate);	
			TextView ttotalInstallations = (TextView)findViewById(R.id.txtStatusReportMainTotal);
			TextView tbilled = (TextView)findViewById(R.id.txtStatusReportMainBilled);
			TextView tnormal = (TextView)findViewById(R.id.txtStatusReportMainNormal);
			TextView tdl = (TextView)findViewById(R.id.txtStatusReportMainDL);
			TextView tmnr = (TextView)findViewById(R.id.txtStatusReportMainMNR);
			TextView tdover = (TextView)findViewById(R.id.txtStatusReportMainDO);
			TextView tmch = (TextView)findViewById(R.id.txtStatusReportMainMCH);
			TextView trnf = (TextView)findViewById(R.id.txtStatusReportMainRNF);
			TextView tva = (TextView)findViewById(R.id.txtStatusReportMainVA);
			TextView tdis = (TextView)findViewById(R.id.txtStatusReportMainDIS);
			TextView tms = (TextView)findViewById(R.id.txtStatusReportMainMS);
			TextView tdir = (TextView)findViewById(R.id.txtStatusReportMainDIR);
			TextView tmb = (TextView)findViewById(R.id.txtStatusReportMainMB);
			TextView tnt = (TextView)findViewById(R.id.txtStatusReportMainNT);			

			//Set parameters not in list(Summary)
			tname.setText(mrname);
			tprintDate.setText(timeStamp);
			ttotalInstallations.setText(String.valueOf(tot));
			tbilled.setText(String.valueOf(bill));
			tnormal.setText(String.valueOf(normal));
			tdl.setText(String.valueOf(dl));
			tmnr.setText(String.valueOf(mnr));
			tdover.setText(String.valueOf(dover));
			tmch.setText(String.valueOf(mch));
			trnf.setText(String.valueOf(rnf));
			tva.setText(String.valueOf(va));
			tdis.setText(String.valueOf(dis));
			tms.setText(String.valueOf(ms));
			tdir.setText(String.valueOf(dir));
			tmb.setText(String.valueOf(mb));
			tnt.setText(String.valueOf(nt));		

			btnPrint.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//Nitish 21-03-2014
					CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
					params.setContext(ReportStatusActivity.this);					
					params.setMainHandler(dh);
					params.setMsg(alertmsg);
					params.setButtonOkText(btnAlertSummary);
					params.setButtonCancelText(btnAlertDetails);
					params.setButtonNeutralText(btnAlertCancel);					
					params.setTitle(alerttitle);
					params.setFunctionality(Print);
					cAlert = new CustomAlert(params);				
					//End Nitish 21-03-2014
					//Cut From Here
				}
			});
			btnDetails.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent i = new Intent(ReportStatusActivity.this,ReportStatusListActivity.class);
					startActivity(i);
				}
			});

		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}	

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
	public class ConnectedThread extends Thread
	{
		private final OutputStream mmOutStream;
		public ConnectedThread(){
			OutputStream tmpOut=null;
			try {
				tmpOut = mmSocket.getOutputStream();
			} catch (Exception e) {
				// TODO: handle exception
				Log.d(TAG,e.toString());
			}
			mmOutStream=tmpOut;
		}		
		public void run(){
			PrinterCharacterAlign pca = new PrinterCharacterAlign();
			
			try 
			{

				//String LineText = "----------------------------------------------------";//52				
				alRep = db.GetReportStatusList();
				String mrname = "";
				String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());			
				int tot=0,bill=0,normal=0,dl=0,mnr=0,dover=0,mch=0,rnf=0,va=0,dis=0,ms=0,dir=0,mb=0,nt=0;				
				for(ReportStatus alsummary : alRep)
				{
					tot = tot + alsummary.getmTotalInstallations();
					bill = bill + alsummary.getmBilled();				
					mrname = alsummary.getmMRName();					
					normal = normal + alsummary.getmNormal();
					dl = dl + alsummary.getmDL();
					mnr = mnr + alsummary.getmMNR();
					dover = dover + alsummary.getmDO();
					mch = mch + alsummary.getmMCH();
					rnf = rnf + alsummary.getmRNF();
					va = va + alsummary.getmVA();
					dis = dis + alsummary.getmDIS();
					ms = ms + alsummary.getmMS();
					dir = dir + alsummary.getmDIR();
					mb = mb + alsummary.getmMB();
					nt = nt + alsummary.getmNT();
					Thread.sleep(350);
				}				
				bptr.sendData(ConstantClass.data1);				
				bptr.sendData(ConstantClass.linefeed1);				
				bptr.sendData(pca.CenterAlign("STATUS REPORT"," ").getBytes());//Title				
				bptr.sendData(pca.LineSeperation().getBytes());				
				bptr.sendData(pca.CenterAlign("SUMMARY"," ").getBytes());//Summary
				bptr.sendData(pca.LineSeperation().getBytes());			
				bptr.sendData(pca.TwoParallelString("Print Date", timeStamp, ' ', ':').getBytes());//Print Date	
				bptr.sendData(pca.TwoParallelString("MR Name", mrname, ' ', ':').getBytes());//MR Name
				bptr.sendData(pca.TwoParallelString("Total Inst", String.valueOf(tot), ' ', ':').getBytes());//Total Installations	
				bptr.sendData(pca.TwoParallelString("Billed", String.valueOf(bill), ' ', ':').getBytes());//Billed				
				bptr.sendData(pca.TwoParallelString("Normal", String.valueOf(normal), ' ', ':').getBytes());//Normal				
				bptr.sendData(pca.TwoParallelString("DL", String.valueOf(dl), ' ', ':').getBytes());//DL				
				bptr.sendData(pca.TwoParallelString("MNR", String.valueOf(mnr), ' ', ':').getBytes());//MNR				
				bptr.sendData(pca.TwoParallelString("DO", String.valueOf(dover), ' ', ':').getBytes());//DO					
				bptr.sendData(pca.TwoParallelString("MCH", String.valueOf(mch), ' ', ':').getBytes());//MCH			
				bptr.sendData(pca.TwoParallelString("RNF", String.valueOf(rnf), ' ', ':').getBytes());//RNF				
				bptr.sendData(pca.TwoParallelString("VA", String.valueOf(va), ' ', ':').getBytes());//VA				
				bptr.sendData(pca.TwoParallelString("DIS", String.valueOf(dis), ' ', ':').getBytes());//DIS				
				bptr.sendData(pca.TwoParallelString("MS", String.valueOf(ms), ' ', ':').getBytes());//MS				
				bptr.sendData(pca.TwoParallelString("DIR", String.valueOf(dir), ' ', ':').getBytes());//DIR				
				bptr.sendData(pca.TwoParallelString("MB", String.valueOf(mb), ' ', ':').getBytes());//MB				
				bptr.sendData(pca.TwoParallelString("NT", String.valueOf(nt), ' ', ':').getBytes());//NT				
				bptr.sendData(pca.LineSeperation().getBytes());
				if(!malertResult) //Nitish 21-03-2014
				{				
					bptr.sendData(pca.CenterAlign("DETAILS"," ").getBytes());//Details
					bptr.sendData(ConstantClass.linefeed1);	
					bptr.sendData(pca.LineSeperation().getBytes());				
					for(ReportStatus rb : alRep)
					{					
						bptr.sendData(pca.TwoParallelString("Billed Date",(new CommonFunction()).DateConvertAddChar(rb.getmBillDate().trim(),"-"), ' ', ':').getBytes());//Print Date
						bptr.sendData(pca.TwoParallelString("Billed", String.valueOf(rb.getmBilled()), ' ', ':').getBytes());//Print Date					
						bptr.sendData(pca.TwoParallelString("Normal", String.valueOf(rb.getmNormal()), ' ', ':').getBytes());//Normal					
						bptr.sendData(pca.TwoParallelString("DL", String.valueOf(rb.getmDL()), ' ', ':').getBytes());//DL					
						bptr.sendData(pca.TwoParallelString("MNR", String.valueOf(rb.getmMNR()), ' ', ':').getBytes());//MNR						
						bptr.sendData(pca.TwoParallelString("DO", String.valueOf(rb.getmDO()), ' ', ':').getBytes());//DO					
						bptr.sendData(pca.TwoParallelString("MCH", String.valueOf(rb.getmMCH()), ' ', ':').getBytes());//MCH					
						bptr.sendData(pca.TwoParallelString("RNF", String.valueOf(rb.getmRNF()), ' ', ':').getBytes());//RNF						
						bptr.sendData(pca.TwoParallelString("VA", String.valueOf(rb.getmVA()), ' ', ':').getBytes());//VA					
						bptr.sendData(pca.TwoParallelString("DIS", String.valueOf(rb.getmDIS()), ' ', ':').getBytes());//DIS					
						bptr.sendData(pca.TwoParallelString("MS", String.valueOf(rb.getmMS()), ' ', ':').getBytes());//MS						
						bptr.sendData(pca.TwoParallelString("DIR", String.valueOf(rb.getmDIR()), ' ', ':').getBytes());//DIR					
						bptr.sendData(pca.TwoParallelString("MB", String.valueOf(rb.getmMB()), ' ', ':').getBytes());//MB					
						bptr.sendData(pca.TwoParallelString("NT", String.valueOf(rb.getmNT()), ' ', ':').getBytes());//NT					
						bptr.sendData(pca.LineSeperation().getBytes());											
					}
				}			
				bptr.sendData(ConstantClass.linefeed1);
				bptr.sendData(ConstantClass.linefeed1);				
				

			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				Log.d("in.nsoft.dmsThread2",e.toString());
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
		return OptionsMenu.navigate(item,ReportStatusActivity.this);			
	}
	//Nitish 24-03-2014
	@Override
	public void performAction(boolean alertResult,int functionality) {
		malertResult = alertResult; //If alertResult = true summary else details
		if(functionality == Print)
		{
			bptr = new BluetoothPrinting(ReportStatusActivity.this);
			ringProgress = ProgressDialog.show(ReportStatusActivity.this, "Please wait..", "Printing...",true);//Spinner
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
			} 
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d(TAG,e.toString());
			}
		}		
	}
}
