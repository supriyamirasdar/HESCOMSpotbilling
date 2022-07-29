
//Nitish 08-05-2014
package in.nsoft.hescomspotbilling;

import java.io.IOException;
import java.util.ArrayList;

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

public class ReportRRNoWiseCollectionActivity extends Activity implements AlertInterface {

	private TextView txtBatchNo,txtDate,txtMRCode,txtTotalReceipts,txtTotPaidAmount;
	private Button btnPrint;
	final static String sCash = "CASH";
	final static String sChequrDD = "CHQ/DD";
	final static String sPayMode = "Payment Mode: ";
	final static String sPaidAmt = "Paid Amount:    ";	
	ArrayList<ReadCollection> alRep ;
	ReportAdapter adapter;
	DatabaseHelper mDb = new DatabaseHelper(this);

	ProgressDialog ringProgress;

	int i = 0;
	Handler dh ;
	BluetoothPrinting bptr;
	static final int REQUEST_ENABLE_BT = 0;
	final static String TAG = "in.nsoft.spotBilling.ReportRRNoWiseCollectionActivity";

	//Nitish 22-03-2014	
	CustomAlert cAlert;
	boolean malertResult;
	final static String alertmsg = "Are You Sure You Want to Print?";
	final static String alerttitle = "RRNo Wise Collection Report";		
	final static String btnAlertOk = "Ok";
	final static String btnAlertCancel = "Cancel";		
	final static int TwoButtons = 1;

	//Nitish End

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_rrno_wise_collection);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		txtDate = (TextView)findViewById(R.id.txtReportRRNOWiseCollectionDate);
		txtBatchNo = (TextView)findViewById(R.id.txtReportRRNOWiseCollectionBatchNo);
		txtMRCode = (TextView)findViewById(R.id.txtReportRRNOWiseCollectionMRCode);
		txtTotalReceipts = (TextView)findViewById(R.id.txtReportRRNOWiseCollectionReceipts);
		txtTotPaidAmount = (TextView)findViewById(R.id.txtReportRRNOWiseCollectionTotColl);
		btnPrint  = (Button)findViewById(R.id.btnPrintReportDayWiseCollection);
		dh = new Handler();

		try 
		{
			String bdate = (new CommonFunction()).DateConvertAddChar(mDb.GetCashCounterDetails().getmBatch_Date(), "-"); //Obtain BatchDate
			alRep = mDb.GetReportRRNOWiseColl(mDb.GetCashCounterDetails().getmBatch_No());			
			
			txtBatchNo.setText(mDb.GetCashCounterDetails().getmBatch_No());			
			txtDate.setText(bdate); 		
			txtMRCode.setText(mDb.GetMRName());	
			txtTotalReceipts.setText(String.valueOf(mDb.GetCountforBatchColl(mDb.GetCashCounterDetails().getmBatch_No())));//Total Receipt for this batch
			txtTotPaidAmount.setText(mDb.GetCollectionAmount(mDb.GetCashCounterDetails().getmBatch_No()) + ConstantClass.sPaise);//Total Collection amount for this batch
			
			if(alRep.size() ==0)
			{
				CustomToast.makeText(this, "Collection Data does not Exist", Toast.LENGTH_SHORT);
				btnPrint.setVisibility(View.INVISIBLE);
				return;
			}
			//To get List Parameters
			adapter = new ReportAdapter(alRep);			
			ListView lv = (ListView) findViewById(R.id.lstReportRRNOWiseCollectionList);		
			lv.setAdapter(adapter);	
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			CustomToast.makeText(this, "Collection Data does not Exist", Toast.LENGTH_SHORT);
			e.printStackTrace();
		}
		btnPrint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				//Nitish 21-03-2014
				//Set Parameters of CustomAlert
				CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
				params.setContext(ReportRRNoWiseCollectionActivity.this);					
				params.setMainHandler(dh);
				params.setMsg(alertmsg);
				params.setButtonOkText(btnAlertCancel);
				params.setButtonCancelText(btnAlertOk);
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
		return OptionsMenu.navigate(item,ReportRRNoWiseCollectionActivity.this);			
	}
	//Class for List View
	private class ReportAdapter extends ArrayAdapter<ReadCollection>
	{

		public ReportAdapter(ArrayList<ReadCollection> mReportList) {		
			super(ReportRRNoWiseCollectionActivity.this, R.layout.report_rrnowise_collection, mReportList);		

		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) // If we weren't given a view, inflate one
		{
			try
			{		
				ReadCollection rb = getItem(position);
				if (convertView == null) {
					convertView = ReportRRNoWiseCollectionActivity.this.getLayoutInflater().inflate(R.layout.report_rrnowise_collection, null);					
				}	
				// Set List View Parameters
				TextView txtRRNo = (TextView)convertView.findViewById(R.id.txtRRNoWiseCollectionReportListRRNo);
				TextView txtMode = (TextView)convertView.findViewById(R.id.txtRRNoWiseCollectionReportListMode);
				TextView txtPaidAmt = (TextView)convertView.findViewById(R.id.txtRRNoWiseCollectionReportListPaidAmount);

				String rpttype;
				if(rb.getmReceipttypeflag().equals(ConstantClass.mRevenueType.toString()))
				{
					rpttype = ConstantClass.mRevenueRcpt;
				}
				else
				{
					rpttype = ConstantClass.mMISCRcpt;
				}
				//22-04-2017
				txtRRNo.setText(rb.getmRRNo().toString().trim()+ "(" + rpttype + ")");
				txtMode.setText(sPayMode +( rb.getmPayment_Mode().equals("1")? sCash : sChequrDD ));
				txtPaidAmt.setText(sPaidAmt +String.valueOf(rb.getmPaid_Amt()) + ConstantClass.sPaise);	
			}		
			catch(Exception e)
			{
				Toast.makeText(ReportRRNoWiseCollectionActivity.this, "Error View " , Toast.LENGTH_LONG).show();			
			}
			return convertView;
		}
	}

	@Override
	public void performAction(boolean alertResult, int functionality) 
	{
		if(!alertResult)
		{		
			bptr = new BluetoothPrinting(ReportRRNoWiseCollectionActivity.this);
			ringProgress = ProgressDialog.show(ReportRRNoWiseCollectionActivity.this, "Please wait..", "Printing...",true);//Spinner
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
				bptr.sendData(pca.CenterAlign("RRNO WISE COLLECTION REPORT"," ").getBytes());//Title						
				bptr.sendData(pca.LineSeperation().getBytes());				
				bptr.sendData(pca.CenterAlign(timeStamp," ").getBytes());//TimeStamp												
				bptr.sendData(pca.LeftAlign("MCC TR NO:  " + mDb.GetCashCounterDetails().getmBatch_No()," ").getBytes());//BATCH NO
				bptr.sendData(ConstantClass.linefeed1);					
				bptr.sendData(pca.TwoParallelString("DATE",bdate, ' ', ':').getBytes()); //Batch Date
				bptr.sendData(pca.TwoParallelString("MR CODE",mDb.GetMRName(), ' ', ':').getBytes()); //MR Code					
				bptr.sendData(ConstantClass.linefeed1);
				bptr.sendData(pca.FourParallelStringRRNoCollReport("RRNO", "Mode","RptType", "Paid Amount", ' ').getBytes());//ColumnNames				
				bptr.sendData(pca.LineSeperation().getBytes());
				String rpttype;
				int revnamt,miscamt,revrptcount,miscrptcount;
				revnamt=miscamt=revrptcount=miscrptcount=0;
				for(ReadCollection rb : alRep)
				{					
					if(rb.getmReceipttypeflag().equals(ConstantClass.mRevenueType.toString()))
					{
						rpttype = ConstantClass.mRevenueRcpt;
						revnamt = revnamt + rb.getmPaid_Amt();
						revrptcount = revrptcount + 1;
					}
					else
					{
						rpttype = ConstantClass.mMISCRcpt;
						miscamt = miscamt + rb.getmPaid_Amt();
						miscrptcount = miscrptcount + 1;
					}
					//22-04-2017
					bptr.sendData(pca.FourParallelStringRRNoCollReport(rb.getmRRNo().toString().trim(),(rb.getmPayment_Mode().equals("1")? sCash : sChequrDD ),rpttype, String.valueOf(rb.getmPaid_Amt()) + ConstantClass.sPaise, ' ').getBytes());
										
				}				
				bptr.sendData(ConstantClass.linefeed1);
				bptr.sendData(pca.TwoParallelString("REVENUE RECEIPTS",String.valueOf(revrptcount), ' ', ':').getBytes()); //Revenue Receipts for this batch
				bptr.sendData(pca.TwoParallelString("MISC RECEIPTS",String.valueOf(miscrptcount), ' ', ':').getBytes()); //Revenue Receipts for this batch				
				bptr.sendData(pca.TwoParallelString("TOTAL RECEIPTS",String.valueOf(mDb.GetCountforBatchColl(mDb.GetCashCounterDetails().getmBatch_No())), ' ', ':').getBytes()); //Total Receipts for this batch
				bptr.sendData(ConstantClass.linefeed1);
				bptr.sendData(pca.TwoParallelString("REVENUE AMOUNT",String.valueOf(revnamt)+ ConstantClass.sPaise, ' ', ':').getBytes()); //Revenue Receipts for this batch
				bptr.sendData(pca.TwoParallelString("MISC AMOUNT",String.valueOf(miscamt)+ ConstantClass.sPaise, ' ', ':').getBytes()); //Revenue Receipts for this batch				
				bptr.sendData(pca.TwoParallelString("TOTAL COLL AMOUNT",mDb.GetCollectionAmount(mDb.GetCashCounterDetails().getmBatch_No()) + ConstantClass.sPaise, ' ', ':').getBytes()); //Paid Amount for this batch
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
					Thread.sleep(5000);
					bptr.closeBT();
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
	}//END ConnectedThread
	//END Bluetooth Code====================================================================================
}

