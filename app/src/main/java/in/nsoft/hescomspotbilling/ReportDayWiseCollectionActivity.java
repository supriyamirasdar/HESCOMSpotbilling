//Nitish 08-05-2014
package in.nsoft.hescomspotbilling;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
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

public class ReportDayWiseCollectionActivity extends Activity implements AlertInterface{

	private TextView txtPrintDate,txtDate,txtMRCode,txtSubDiv,txtTotalReceipts,txtBatchNo,txtPaidAmount;
	private Button btnPrint,btnDetails;
	private TextView txtReceipts,txtCollection; //Nitish 14-02-2017
	Handler dh ;	
	static final int REQUEST_ENABLE_BT = 0;	
	BluetoothPrinting bptr;
	BluetoothSocket mmSocket;
	final static String TAG = "in.nsoft.spotBilling.ReportDayWiseCollectionActivity";

	ProgressDialog ringProgress;
	CustomAlert cAlert;
	boolean malertResult;
	final static String alertmsg = "Are you sure you want to Print?";
	final static String alerttitle = "Day Wise Collection Report";
	final static String btnAlertYes = "Yes";
	final static String btnAlertNo = "No";
	final static int SingleButton = 0;
	final static int TwoButtons = 1;

	private final DatabaseHelper mDb =new DatabaseHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_day_wise_collection);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		txtBatchNo = (TextView)findViewById(R.id.txtReportDayWiseCollectionBatchNo);
		txtSubDiv = (TextView)findViewById(R.id.txtReportDayWiseCollectionSubDiv);
		txtPrintDate = (TextView)findViewById(R.id.txtReportDayWiseCollectionPrintDate);	
		txtDate = (TextView)findViewById(R.id.txtReportDayWiseCollectionDate);	
		txtMRCode = (TextView)findViewById(R.id.txtReportDayWiseCollectionMRCode);		
		txtTotalReceipts = (TextView)findViewById(R.id.txtReportDayWiseCollectionTotalReceipts);		
		txtPaidAmount = (TextView)findViewById(R.id.txtReportDayWiseCollectionPaidAmt);
		btnPrint = (Button)findViewById(R.id.btnReportDayWiseCollectionPrint);
		btnDetails = (Button)findViewById(R.id.btnReportDayWiseCollectionDetails);	

		//Nitish 17-02-2017
		txtReceipts= (TextView)findViewById(R.id.txtReceipts);		
		txtCollection= (TextView)findViewById(R.id.txtCollection);

		dh = new Handler();
		try 
		{	
			String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
			txtPrintDate.setText(timeStamp);
			String bdate = (new CommonFunction()).DateConvertAddChar(mDb.GetCashCounterDetails().getmBatch_Date(), "-"); //Obtain BatchDate
			txtDate.setText(bdate); 
			txtBatchNo.setText(mDb.GetCashCounterDetails().getmBatch_No());
			txtSubDiv.setText(mDb.GetSubDivisionName());
			txtMRCode.setText(mDb.GetMRName());	
			txtTotalReceipts.setText(String.valueOf(mDb.GetCountforBatchColl(mDb.GetCashCounterDetails().getmBatch_No())));//Total Receipt for this batch
			txtPaidAmount.setText(mDb.GetCollectionAmount(mDb.GetCashCounterDetails().getmBatch_No())+ ConstantClass.sPaise);//Total Collection amount for this batch

			//Nitish 14-02-2017
			String Receipts = "Cash:";
			Receipts = Receipts + String.valueOf(mDb.GetCashChequeCountforBatchColl(mDb.GetCashCounterDetails().getmBatch_No(),"1"));
			Receipts = Receipts + "  Cheque/DD:"+ String.valueOf(mDb.GetCashChequeCountforBatchColl(mDb.GetCashCounterDetails().getmBatch_No(),"2")); 

			String Collection = "Cash:";
			Collection = Collection + mDb.GetCashChequeCollectionAmount(mDb.GetCashCounterDetails().getmBatch_No(),"1")+ ConstantClass.sPaise;
			Collection = Collection + "  Cheque/DD: " + mDb.GetCashChequeCollectionAmount(mDb.GetCashCounterDetails().getmBatch_No(),"2")+ ConstantClass.sPaise;

			txtReceipts.setText(Receipts);
			txtCollection.setText(Collection);
			//14-02-2017 End
			
			btnPrint.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					//Nitish 21-03-2014
					//Set Parameters of CustomAlert
					CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
					params.setContext(ReportDayWiseCollectionActivity.this);					
					params.setMainHandler(dh);
					params.setMsg(alertmsg);
					params.setButtonOkText(btnAlertNo);
					params.setButtonCancelText(btnAlertYes);
					params.setTitle(alerttitle);					
					cAlert  = new CustomAlert(params);					
					//End Nitish 21-03-2014
					//Cut from here	
				}
			});			
			btnDetails.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent i = new Intent(ReportDayWiseCollectionActivity.this,ReportDayWiseCollectionListActivity.class);
					startActivity(i);
				}
			});
		}
		catch (Exception e) {			
			CustomToast.makeText(ReportDayWiseCollectionActivity.this, "Collection Data does not Exist ", Toast.LENGTH_SHORT);
			e.printStackTrace();
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
		return OptionsMenu.navigate(item,ReportDayWiseCollectionActivity.this);			
	}
	@Override
	public void performAction(boolean alertResult, int functionality) 
	{
		if(!alertResult)
		{		
			bptr = new BluetoothPrinting(ReportDayWiseCollectionActivity.this);
			ringProgress = ProgressDialog.show(ReportDayWiseCollectionActivity.this, "Please wait..", "Printing...",true);//Spinner
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
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)	
	public class ConnectedThread extends Thread //ConnectedThread
	{	
		public void run(){
			PrinterCharacterAlign pca = new PrinterCharacterAlign();//Class Alignment for printer 
			CommonFunction cFun = new CommonFunction();	

			try 
			{
				String timeStamp = cFun.GetCurrentTime();
				String bdate = (new CommonFunction()).DateConvertAddChar(mDb.GetCashCounterDetails().getmBatch_Date(), "-"); //Obtain BatchDate
				bptr.sendData(ConstantClass.data1);
				bptr.sendData(ConstantClass.linefeed1);				
				bptr.sendData(pca.CenterAlign("DAY WISE COLLECTION REPORT"," ").getBytes());//Title						
				bptr.sendData(pca.LineSeperation().getBytes());					
				bptr.sendData(pca.CenterAlign(timeStamp," ").getBytes());//TimeStamp
				bptr.sendData(ConstantClass.linefeed1);
				bptr.sendData(pca.LeftAlign("MCC TR NO:  " + mDb.GetCashCounterDetails().getmBatch_No(), " ").getBytes());//BATCH NO //22-06-2017
				bptr.sendData(ConstantClass.linefeed1);					
				bptr.sendData(pca.TwoParallelString("SUBDIVISION",mDb.GetSubDivisionName(), ' ', ':').getBytes());//SubDivision
				bptr.sendData(pca.TwoParallelString("DATE",bdate, ' ', ':').getBytes()); //Batch Date
				bptr.sendData(pca.TwoParallelString("MR CODE",mDb.GetMRName(), ' ', ':').getBytes()); //MR Code					
				bptr.sendData(ConstantClass.linefeed1);				
				//Nitish 14-02-2017
				bptr.sendData(pca.TwoParallelString("CASH RPTS",String.valueOf(mDb.GetCashChequeCountforBatchColl(mDb.GetCashCounterDetails().getmBatch_No(),"1")), ' ', ':').getBytes()); //Cash Receipts for this batch
				bptr.sendData(pca.TwoParallelString("CHQ/DD RPTS",String.valueOf(mDb.GetCashChequeCountforBatchColl(mDb.GetCashCounterDetails().getmBatch_No(),"2")), ' ', ':').getBytes()); //Cheque Receipts for this batch
				bptr.sendData(pca.TwoParallelString("TOTAL RPTS",String.valueOf(mDb.GetCountforBatchColl(mDb.GetCashCounterDetails().getmBatch_No())), ' ', ':').getBytes()); //Total Receipts for this batch
				bptr.sendData(ConstantClass.linefeed1);
				bptr.sendData(pca.TwoParallelString("CASH AMOUNT",mDb.GetCashChequeCollectionAmount(mDb.GetCashCounterDetails().getmBatch_No(),"1") + ConstantClass.sPaise, ' ', ':').getBytes()); //Cash Amount for this batch
				bptr.sendData(pca.TwoParallelString("CHQ/DD AMOUNT",mDb.GetCashChequeCollectionAmount(mDb.GetCashCounterDetails().getmBatch_No(),"2") + ConstantClass.sPaise, ' ', ':').getBytes()); //Cheque Amount for this batch
				bptr.sendData(pca.TwoParallelString("TOTAL COLL AMOUNT",mDb.GetCollectionAmount(mDb.GetCashCounterDetails().getmBatch_No()) + ConstantClass.sPaise, ' ', ':').getBytes()); //Paid Amount for this batch
				//End 14-02-2017
				
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
	}//END ConnectedThread
	//END Bluetooth Code====================================================================================
}


