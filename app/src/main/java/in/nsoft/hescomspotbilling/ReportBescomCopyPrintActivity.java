//Created Nitish 08-05-2014
package in.nsoft.hescomspotbilling;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ReportBescomCopyPrintActivity extends Activity implements AlertInterface{

	DatabaseHelper mDb = new DatabaseHelper(this);
	ArrayList<ReadCollection> alRep ;

	private Button btnPrint;
	private TextView batchno, batchdate;
	ProgressDialog ringProgress;
	Handler dh ;	
	static final int REQUEST_ENABLE_BT = 0;	
	BluetoothPrinting bptr;
	final static String TAG = "in.nsoft.spotBilling.ReportBescomCopyPrint";

	//Nitish 24/04/2014	
	CustomAlert cAlert ;
	boolean malertResult;
	final static String alertmsg = "Are you sure you want to Print?";
	final static String alerttitle = "HESCOM Copy Report";
	final static String btnAlertYes = "Yes";
	final static String btnAlertNo = "No";
	final static int SingleButton = 0;
	final static int TwoButtons = 1;
	final static int Print = 2;	
	//Nitish End


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_bescom_copy_print);	
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);	

		batchno  = (TextView)findViewById(R.id.txtReportBescomCopyPrintCCBatchNo);
		batchdate  = (TextView)findViewById(R.id.txtReportBescomCopyPrintBatchDate);
		btnPrint  = (Button)findViewById(R.id.btnReportBescomCopyPrint);

		dh = new Handler();
		try 
		{	
			batchno.setText(mDb.GetCashCounterDetails().getmBatch_No());
			String bdate = (new CommonFunction()).DateConvertAddChar(mDb.GetCashCounterDetails().getmBatch_Date(), "-"); //Obtain BatchDate
			batchdate.setText(bdate);
			//Get all details of receipts for last BatchNo and BatchDate
			alRep = mDb.GetReportBescomCopyPrint(mDb.GetCashCounterDetails().getmBatch_No());

			if(alRep.size() ==0)
			{
				CustomToast.makeText(this, "Collection Data does not Exist", Toast.LENGTH_SHORT);
				btnPrint.setVisibility(View.INVISIBLE);
			}
		}
		catch (Exception e) {
			CustomToast.makeText(ReportBescomCopyPrintActivity.this, "Collection Data does not Exist ", Toast.LENGTH_SHORT);
			e.printStackTrace();
		}	

		btnPrint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				//Nitish 21-03-2014
				//Set Parameters of CustomAlert
				CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
				params.setContext(ReportBescomCopyPrintActivity.this);					
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
		return OptionsMenu.navigate(item,ReportBescomCopyPrintActivity.this);			
	}

	@Override
	public void performAction(boolean alertResult, int functionality) 
	{
		if(!alertResult)
		{		
			bptr = new BluetoothPrinting(ReportBescomCopyPrintActivity.this);
			ringProgress = ProgressDialog.show(ReportBescomCopyPrintActivity.this, "Please wait..", "Printing...",true);//Spinner
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

			try {
				alRep = mDb.GetReportBescomCopyPrint(mDb.GetCashCounterDetails().getmBatch_No());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try 
			{				
				bptr.sendData(ConstantClass.data1);
				for(ReadCollection rc : alRep)
				{
					bptr.sendData(ConstantClass.linefeed1);
					bptr.sendData(ConstantClass.linefeed1);
					bptr.sendData(pca.CenterAlign("HESCOM COPY"," ").getBytes());//Title
					bptr.sendData(ConstantClass.linefeed1);						
					bptr.sendData(pca.CenterAlign("CASH RECEIPT (MCC)"," ").getBytes());
					bptr.sendData(pca.LineSeperation().getBytes());	
					bptr.sendData(ConstantClass.linefeed1);	
					bptr.sendData(pca.CenterAlign("SUBDIVISION: " + mDb.GetSubDivisionName(), " ").getBytes());//SubDiv	
					bptr.sendData(ConstantClass.linefeed1);	
					bptr.sendData(pca.LeftAlign("BATCH NO: " + rc.getmBatch_No(), " ").getBytes());//Batch No	
					bptr.sendData(ConstantClass.linefeed1);
					bptr.sendData(pca.LeftAlign("CUSTOMER NAME: " + rc.getmCustomerName(), " ").getBytes()); //Name
					bptr.sendData(ConstantClass.linefeed1);
					bptr.sendData(ConstantClass.linefeed1);					
					bptr.sendData(pca.TwoParallelString("RECEIPT NO",String.valueOf(rc.getmReceipt_No()), ' ', ':').getBytes());//RECEIPT NO
					bptr.sendData(pca.TwoParallelString("CONNECTION ID", rc.getmConnectionNo(), ' ', ':').getBytes()); //CONNECTION NO
					bptr.sendData(pca.TwoParallelString("RR NO", rc.getmRRNo(), ' ', ':').getBytes()); //RR NO							
					//If rc.getmReceipttypeflag() == "R" then Revenue Receipt. if  rc.getmReceipttypeflag() == "M" then MISC Receipt
					
					if(rc.getmReceipttypeflag().equals(ConstantClass.mRevenueType.toString()))
						bptr.sendData(pca.TwoParallelString("RECEIPT TYPE",ConstantClass.mRevenueRcpt, ' ', ':').getBytes()); //24-07-2014		
					else
						bptr.sendData(pca.TwoParallelString("RECEIPT TYPE",ConstantClass.mMISCRcpt + ConstantClass.mMiscASD, ' ', ':').getBytes()); //24-07-2014		

					//24-07-2014
					if(rc.getmReceipttypeflag().equals(ConstantClass.mRevenueType.toString()))
					{
						if(rc.getmArrearsBill_Flag()==1) //Payment for Arrears
						{
							BigDecimal txtArrers = rc.getmArears() == null ? new BigDecimal(0.00) : rc.getmArears().setScale(2, BigDecimal.ROUND_HALF_UP);											
							bptr.sendData(pca.TwoParallelString("ARREARS",String.valueOf(txtArrers), ' ', ':').getBytes()); //Arrears
						}
						else //If Billed then Payment for BillAmount
						{
							if(rc.getmBOBillFlag()==1) 
							{
								String bobAmount[] = rc.getmBOBilled_Amount().replace('.','#').split("#");						
								bptr.sendData(pca.TwoParallelString("BILL AMT", bobAmount[0]+ ConstantClass.sPaise, ' ', ':').getBytes()); //BillTotal
							}
							else
								bptr.sendData(pca.TwoParallelString("BILL AMT", String.valueOf(rc.getmBillTotal().setScale(2,RoundingMode.HALF_EVEN).setScale(0, BigDecimal.ROUND_HALF_UP).toString()) + ConstantClass.sPaise, ' ', ':').getBytes()); //BillTotal	
						}
					}
					else  //For MISC Receipt Set Balance
					{
						bptr.sendData(pca.TwoParallelString("BALANCE",(new CommonFunction()).GetBalanceForMISCReceipt(rc.getmIODRemarks()), ' ', ':').getBytes()); //Balance
					}

					if(rc.getmPayment_Mode().equals("1")) //Cash payment
					{
						bptr.sendData(pca.TwoParallelString("PAYMENT MODE", "CASH", ' ', ':').getBytes()); //Mode	
						bptr.sendData(pca.TwoParallelString("PAID AMT", String.valueOf(rc.getmPaid_Amt()), ' ', ':').getBytes()); //PaidAmt
					}
					else //Cheque Payment
					{
						bptr.sendData(pca.TwoParallelString("PAYMENT MODE", "CHEQUE/DD", ' ', ':').getBytes()); //Mode
						bptr.sendData(pca.TwoParallelString("PAID AMT", String.valueOf(rc.getmPaid_Amt()) + ConstantClass.sPaise, ' ', ':').getBytes()); //PaidAmt
						bptr.sendData(pca.TwoParallelString("CHEQUE/DD NO", String.valueOf(rc.getmChequeDDNo()), ' ', ':').getBytes()); //CHEQUE/DD NO
						bptr.sendData(pca.TwoParallelString("CHEQUE/DD DATE", cFun.DateConvertAddCharNew(rc.getmChequeDDDate().trim(),"/"), ' ', ':').getBytes()); //CHEQUE DD DATE
						bptr.sendData(pca.TwoParallelString("BANK", mDb.GetBankName(rc.getmBankID()).substring(3), ' ', ':').getBytes()); //BANK NAME
					}						
					bptr.sendData(pca.TwoParallelString("RECEIPT DATE", cFun.DateTimeConvertAddChar(rc.getmDateTime().trim(),"/",":"), ' ', ':').getBytes()); //Receipt DateTime
					bptr.sendData(ConstantClass.linefeed1);

					//END Extra Fields=============================================================================================

					//barvalue = connno + receiptno + paidamt with padding( 7 + 5 + 7 )
					String barValue = cFun.GetConnectionIdPadding(rc.getmConnectionNo().trim()) + cFun.GetReceiptNoPadding(String.valueOf(rc.getmReceipt_No())) + rc.getmPaid_Amt() ;
					int barlen = (cFun.GetConnectionIdPadding(rc.getmConnectionNo().trim()) + cFun.GetReceiptNoPadding(String.valueOf(rc.getmReceipt_No())) + rc.getmPaid_Amt()).length() ;
					//String barValue = SlabColl.getmConnectionNo().trim() +SlabColl.getmBillTotal().setScale(0, BigDecimal.ROUND_HALF_EVEN);//punit start ###barcode 26032014

					if(barlen <= 19)
					{
						barValue = barValue + pca.CreateBlankSpaceSequence((19 - barlen), " ");
						/*int z = barValue.getBytes().length;
						byte[] barcodewithVal= new byte[barCode.length+z];
						for(int k=0;k<barCode.length;++k){
							barcodewithVal[k]=barCode[k];
						}

						for(int k=barCode.length,j=0;k<barValue.getBytes().length;++k,++j){
							barcodewithVal[k]=barValue.getBytes()[j];
						}*/
						bptr.sendData(ConstantClass.barCode);
						bptr.sendData(barValue.getBytes());
					}
					else
					{
						bptr.sendData(ConstantClass.barCode);
						bptr.sendData(barValue.substring(0, 19).getBytes());
						//bptr.sendData(linefeed1);
					}	
					bptr.sendData(ConstantClass.linefeed1);
					bptr.sendData(pca.LeftAlign("MR Name: " + rc.getmReaderCode(), " ").getBytes());
					bptr.sendData(ConstantClass.linefeed1);
					bptr.sendData(pca.LineSeperation().getBytes());	
					bptr.sendData(ConstantClass.linefeed1);
					bptr.sendData(ConstantClass.linefeed1);
				}
				bptr.sendData(ConstantClass.linefeed1);
				bptr.sendData(ConstantClass.linefeed1);
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
						Thread.sleep(8000);
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
	}//END ConnectedThread
	//END Bluetooth Code====================================================================================
}


