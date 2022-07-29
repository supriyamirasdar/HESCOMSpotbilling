//Created Nitish 08-05-2014
package in.nsoft.hescomspotbilling;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ReportCollectionDetailsActivity extends Activity implements AlertInterface{

	private TextView txtPrintDate,txtDate,txtMRCode,txtBilled,txtTotalAmount,txtTotalRevReceipts,txtTotalMISCReceipts,txtTotalRevAmount,txtTotalMISCAmount;
	private Button btnPrint;
	private TextView txtReceipts,txtCollection; //Nitish 14-02-2017
	ProgressDialog ringProgress;	
	Handler dh ;
	//final static String sPaise = ".00";
	static final int REQUEST_ENABLE_BT = 0;	
	BluetoothPrinting bptr;
	final static String TAG = "in.nsoft.spotBilling.ReportCollectionDetailsActivity";


	CustomAlert cAlert ;
	boolean malertResult;
	final static String alertmsg = "Are you sure you want to Print?";
	final static String alerttitle = "Collection Details Report";
	final static String btnAlertYes = "Yes";
	final static String btnAlertNo = "No";
	final static int SingleButton = 0;
	final static int TwoButtons = 1;


	private final DatabaseHelper mDb =new DatabaseHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_collection_details);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		txtPrintDate = (TextView)findViewById(R.id.txtReportCollectionDetailsPrintDate);	
		txtDate = (TextView)findViewById(R.id.txtReportCollectionDetailsDate);	
		txtMRCode = (TextView)findViewById(R.id.txtReportCollectionDetailsMRCode);
		txtBilled = (TextView)findViewById(R.id.txtReportCollectionDetailsBilled);		
		txtTotalAmount = (TextView)findViewById(R.id.txtReportCollectionDetailsTotalAmt);
		
		txtTotalRevReceipts = (TextView)findViewById(R.id.txtReportCollectionDetailsTotalRevReceipts);
		txtTotalMISCReceipts = (TextView)findViewById(R.id.txtReportCollectionDetailsTotalMISCReceipts);
		txtTotalRevAmount = (TextView)findViewById(R.id.txtReportCollectionDetailsTotalRevAmount);
		txtTotalMISCAmount = (TextView)findViewById(R.id.txtReportCollectionDetailsTotalMISCAmount);
		btnPrint = (Button)findViewById(R.id.btnReportCollectionDetailsPrint);

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
			txtMRCode.setText(mDb.GetMRName()); 
			txtBilled.setText(String.valueOf(mDb.GetCountforBilledConnection())); //Billed Count			
			txtTotalAmount.setText(mDb.GetBillAmount() + ConstantClass.sPaise);  //Total BillAmount
			
			txtTotalRevReceipts.setText(String.valueOf(mDb.GetCountforRevnMISCColl(ConstantClass.mRevenueType))); //Revn Rpt Count
			txtTotalMISCReceipts.setText(String.valueOf(mDb.GetCountforRevnMISCColl(ConstantClass.mMISCType))); //MISC Rpt Count
			txtTotalRevAmount.setText(mDb.GetTotalRevnMISCCollectionAmount(ConstantClass.mRevenueType) + ConstantClass.sPaise); //Revn Amount
			txtTotalMISCAmount.setText(mDb.GetTotalRevnMISCCollectionAmount(ConstantClass.mMISCType) + ConstantClass.sPaise); //MISC Amount		
		
			
			//Nitish 14-02-2017
			String Receipts = "Cash Rpts:";
			Receipts = Receipts + String.valueOf(mDb.GetCashChequeCountforBatchColl("1"));
			Receipts = Receipts + "  Chq/DD Rpts:"+ String.valueOf(mDb.GetCashChequeCountforBatchColl("2")); 

			String Collection = "Cash:";
			Collection = Collection + mDb.GetCashChequeCollectionAmount("1")+ ConstantClass.sPaise;
			Collection = Collection + "  Chq/DD: " + mDb.GetCashChequeCollectionAmount("2")+ ConstantClass.sPaise;

			txtReceipts.setText(Receipts);
			txtCollection.setText(Collection);
			//14-02-2017 End
		}
		catch (Exception e) {
			CustomToast.makeText(ReportCollectionDetailsActivity.this, "Collection Data does not Exist ", Toast.LENGTH_SHORT);
			e.printStackTrace();
		}	

		btnPrint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				//Nitish 21-03-2014
				//Set Parameters of CustomAlert
				CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
				params.setContext(ReportCollectionDetailsActivity.this);					
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
		return OptionsMenu.navigate(item,ReportCollectionDetailsActivity.this);			
	}

	@Override
	public void performAction(boolean alertResult, int functionality) 
	{
		if(!alertResult)
		{		
			bptr = new BluetoothPrinting(ReportCollectionDetailsActivity.this);
			ringProgress = ProgressDialog.show(ReportCollectionDetailsActivity.this, "Please wait..", "Printing...",true);//Spinner
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
				bptr.sendData(pca.CenterAlign("COLLECTION DETAILS REPORT"," ").getBytes());//Title							
				bptr.sendData(pca.LineSeperation().getBytes());	
				bptr.sendData(ConstantClass.linefeed1);	
				bptr.sendData(pca.CenterAlign(timeStamp," ").getBytes());//TimeStamp
				bptr.sendData(ConstantClass.linefeed1);																		
				bptr.sendData(pca.TwoParallelString("SUBDIVISION",mDb.GetSubDivisionName(), ' ', ':').getBytes());//SubDivision
				bptr.sendData(pca.TwoParallelString("DATE",bdate, ' ', ':').getBytes()); //Batch Date
				bptr.sendData(pca.TwoParallelString("MR CODE",mDb.GetMRName(), ' ', ':').getBytes()); //MR Code	
				bptr.sendData(ConstantClass.linefeed1);			
				bptr.sendData(pca.TwoParallelString("BILLED",String.valueOf(mDb.GetCountforBilledConnection()), ' ', ':').getBytes()); //BILLED CONNECTION
				bptr.sendData(pca.TwoParallelString("TOTAL AMOUNT",mDb.GetBillAmount(), ' ', ':').getBytes()); //Total Amount
				bptr.sendData(ConstantClass.linefeed1);
				bptr.sendData(pca.TwoParallelString("REVENUE RECEIPTS",String.valueOf(mDb.GetCountforRevnMISCColl(ConstantClass.mRevenueType)), ' ', ':').getBytes()); //Total Revn Receipts
				bptr.sendData(pca.TwoParallelString("MISC RECEIPTS",String.valueOf(mDb.GetCountforRevnMISCColl(ConstantClass.mMISCType)), ' ', ':').getBytes()); //Total MISC Receipts
				bptr.sendData(pca.TwoParallelString("REVENUE AMOUNT",mDb.GetTotalRevnMISCCollectionAmount(ConstantClass.mRevenueType)+ConstantClass.sPaise, ' ', ':').getBytes()); //Revenue Amount
				bptr.sendData(pca.TwoParallelString("MISC AMOUNT",mDb.GetTotalRevnMISCCollectionAmount(ConstantClass.mMISCType)+ConstantClass.sPaise, ' ', ':').getBytes()); //MISC Amount
				
				bptr.sendData(ConstantClass.linefeed1);				
				
				bptr.sendData(pca.TwoParallelString("CASH RPTS",String.valueOf(mDb.GetCashChequeCountforBatchColl("1")), ' ', ':').getBytes()); //Cash Receipts for this batch
				bptr.sendData(pca.TwoParallelString("CHQ/DD RPTS",String.valueOf(mDb.GetCashChequeCountforBatchColl("2")), ' ', ':').getBytes()); //Cheque Receipts for this batch
				
				bptr.sendData(ConstantClass.linefeed1);
				bptr.sendData(pca.TwoParallelString("CASH AMOUNT",mDb.GetCashChequeCollectionAmount("1") + ConstantClass.sPaise, ' ', ':').getBytes()); //Cash Amount for this batch
				bptr.sendData(pca.TwoParallelString("CHQ/DD AMOUNT",mDb.GetCashChequeCollectionAmount("2") + ConstantClass.sPaise, ' ', ':').getBytes()); //Cheque Amount for this batch
						
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

