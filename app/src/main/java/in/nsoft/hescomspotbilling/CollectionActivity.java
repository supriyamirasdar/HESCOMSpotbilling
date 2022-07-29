package in.nsoft.hescomspotbilling;
//Created Nitish 21-03-2014


import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings.Global;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CollectionActivity extends Activity implements AlertInterface {	
	int f,nextActivity;
	private Button btnCash,btnCheque,btnprev,btnnext,btnCollectionReprint,btnCollectionLatePayment;//btnCollectionOnline;//Nitish 30-03-2015
	private TextView conid,rrNo,tariffCode,load,billed,duedate,arrears,billamt,lblCollectionPaymentMode,lblRevenueMisc,lblArrears; //Nitish 03-05-2014	
	AutoCompleteTextView ConnIdRRNo;

	AutoDDLAdapter conList;
	BigDecimal  sancLoad;
	Boolean autoSelected;
	ReadCollection CollObj;
	CashCounterObject cc;

	private final DatabaseHelper mDb =new DatabaseHelper(this);
	private static final String TAG = CollectionActivity.class.getName();

	static boolean isFromBilling = false;	
	BluetoothPrinting bptr;
	static final int REQUEST_ENABLE_BT = 0;

	BluetoothDevice  printerDevice;
	BluetoothAdapter mBlueToothAdapter;
	BluetoothSocket mmSocket;

	ProgressDialog ringProgress;


	long diff;
	Handler dh;
	CustomAlert cAlert; 
	boolean alertResult;		
	final static String alerttitleReprint = "Collection Reprint";
	final static String alerttitleOnlinePay = "Pay Online";
	final static String btnAlertOk = "Yes";
	final static String btnAlertCancel = "No";		
	final static int REPRINT = 2;
	final static int PAYONLINE = 1;

	//Nitish 24-07-2014
	String rcpttype;	
	String bDate;

	String rrnoPayOnline = "";
	Thread th;
	String IMEINumber = "";
	String SimNumber = "";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		dh = new Handler();
		ConnIdRRNo =(AutoCompleteTextView)findViewById(R.id.autoCollectionSearch);
		conid =  (TextView)findViewById(R.id.txtCollectionConnectionId);
		rrNo =  (TextView)findViewById(R.id.txtCollectionRRNo);
		duedate = (TextView)findViewById(R.id.txtCollectionDueDate); //COL_DueDate
		arrears = (TextView)findViewById(R.id.txtCollectionArrears); //COL_Arears
		billamt = (TextView)findViewById(R.id.txtCollectionBillAmount); // COL_BillTotal
		tariffCode =  (TextView)findViewById(R.id.txtCollectionTariff);
		load = (TextView)findViewById(R.id.txtCollectionLoad);
		billed = (TextView)findViewById(R.id.txtCollectionStatus);
		btnCash = (Button)findViewById(R.id.btnCollectionCash);
		btnCheque =  (Button)findViewById(R.id.btnCollectionCheque);	
		btnprev = (Button)findViewById(R.id.btnCollectionPrev);	
		btnnext = (Button)findViewById(R.id.btnCollectionNext);	
		btnCollectionReprint = (Button)findViewById(R.id.btnCollectionReprint);
		lblCollectionPaymentMode= (TextView)findViewById(R.id.lblCollectionPaymentMode);//lbl
		lblRevenueMisc= (TextView)findViewById(R.id.lblRevenueMisc);//lbl for Revenue/Misc
		lblArrears= (TextView)findViewById(R.id.lblCollectionArrears);//lbl for Revenue/Misc
		//btnCollectionOnline = (Button)findViewById(R.id.btnCollectionOnline);//lbl for Revenue/Misc

		btnCollectionLatePayment= (Button)findViewById(R.id.btnCollectionLatePayment);//lbl for Revenue/Misc

		bDate = (new CommonFunction()).DateConvertAddChar( mDb.GetCashCounterDetails().getmBatch_Date(), "-"); //Obtain BatchDate

	/*	//Get Mobile Details ==============================================================
		TelephonyManager mgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		IMEINumber = mgr.getDeviceId();
		SimNumber = mgr.getSimSerialNumber(); 
		//END Get Mobile Details ==========================================================	
	 */
		
		try
		{
			//Get Mobile Details ==============================================================
			//10-03-2021
			IMEINumber = CommonFunction.getDeviceNo(CollectionActivity.this);
			SimNumber = CommonFunction.getSIMNo(CollectionActivity.this);

		}
		catch(Exception e)
		{
							
		}
		
		//Nitish 14-08-2015
		btnCollectionLatePayment.setVisibility(View.GONE);
		//Nitish 24-07-2014
		if(CollectionStartActivity.isRevenueRcpt)
		{
			lblRevenueMisc.setText(ConstantClass.mRevenueRcpt.toString());
			rcpttype = ConstantClass.mRevenueType; //rcpttype= R
		}
		else
		{
			lblRevenueMisc.setText(ConstantClass.mMISCRcpt.toString());
			rcpttype = ConstantClass.mMISCType; //rcpttype= M			
			lblArrears.setText(ConstantClass.mBalance.toString());
		}

		try {			
			//If CollectionObject object not contains Connection no Then go and fetch
			//top 1 connection details which is not Collected	
			if(CollectionObject.GetCollectionObject().getmConnectionNo() == null)
			{
				if(mDb.isEveryConnectionCollected())//Check Every connection is Collected or not, if yes load 1st Connection
				{
					mDb.GetPrevColl("1");
					GetAssignNecessaryFields(CollectionObject.GetCollectionObject());
				}
				else//if no load top 1 connection details which is not Collected
				{
					//Nitish 24-07-2014
					mDb.GetValueOnBillingPageColl(rcpttype);//Get top 1 connection details which is not Collected	
					GetAssignNecessaryFields(CollectionObject.GetCollectionObject());
				}/**/ 
			}
			//Check if navigation is from Billing screen
			else if(CollectionActivity.isFromBilling)
			{
				int uid = 0;
				CollectionActivity.isFromBilling = false;
				uid = BillingObject.GetBillingObject().getMmUID();

				if(mDb.IsUIDExists(uid))//check uid exists in db or not, if exists then get the collection object
				{
					mDb.GetPrevColl(String.valueOf(uid));
					GetAssignNecessaryFields(CollectionObject.GetCollectionObject());
				}
				else
				{
					mDb.GetPrevColl(String.valueOf(CollectionObject.GetCollectionObject().getMmUID()));
					GetAssignNecessaryFields(CollectionObject.GetCollectionObject());
				}
			}
			else//Based on Previous object(if it is Collected) get the next connection details
			{
				int uid = 0;
				if(CollectionObject.GetCollectionObject().getmRcptCnt() == 1)//check the Current Object is Collected or not 
				{//getmBlCnt is 1 then that connection is billed 
					uid = CollectionObject.GetCollectionObject().getMmUID() + 1;//get next uid by incrementing 1 
				}
				else//if getmBlCnt is 0 then get the same connection no
				{
					uid = CollectionObject.GetCollectionObject().getMmUID();
				}
				if(mDb.IsUIDExists(uid))//check uid exists in db or not, if exists then get the collection object
				{
					mDb.GetPrevColl(String.valueOf(uid));
					GetAssignNecessaryFields(CollectionObject.GetCollectionObject());
				}
				else
				{
					mDb.GetPrevColl(String.valueOf(CollectionObject.GetCollectionObject().getMmUID()));
					GetAssignNecessaryFields(CollectionObject.GetCollectionObject());
				}
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {	
			conList = mDb.GetConnIdRRNo();			
			ConnIdRRNo.setThreshold(1);			
			ConnIdRRNo.setAdapter(conList);			


			ConnIdRRNo.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> alist, View arg1,
						int pos, long arg3) {
					// TODO Auto-generated method stub
					try {						
						DDLItem k = (DDLItem)  alist.getItemAtPosition(pos);					
						mDb.GetAllDatafromDbColl(k.getId());
						ConnIdRRNo.setText(k.getValue().substring(5).trim());						
						GetAssignNecessaryFields(CollectionObject.GetCollectionObject());

						//To Hide Soft KeyBoard
						InputMethodManager in = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
						ConnIdRRNo.setText("");

					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.d(TAG, e.toString());
						e.printStackTrace();
					}
				}
			});		
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d(TAG, e.toString());
			e.printStackTrace();
		}		
		btnCash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					String currentdate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");						
					//Commented 09-02-2018
					/*if(sdf.parse(currentdate).after(sdf.parse(bDate)))  //If Current Date is greater then do not allow to generate Collection Receipts.
					{
						CustomToast.makeText(CollectionActivity.this,"Current Date exceeds alloted Batch Date " + bDate , Toast.LENGTH_SHORT);
						Intent i = new Intent(CollectionActivity.this,CollectionStartActivity.class);				
						startActivity(i);	
						return;

					}*/
					if(sdf.parse(currentdate).before(sdf.parse(bDate)))  //If Current Date is less then do not allow to generate Collection Receipts.
					{
						CustomToast.makeText(CollectionActivity.this,"Current Date is less than alloted Batch Date " + bDate , Toast.LENGTH_SHORT);
						Intent i = new Intent(CollectionActivity.this,CollectionStartActivity.class);				
						startActivity(i);	
						return;

					}					
					else
					{
						//15-06-2017
						try
						{
							if(Global.getInt(getContentResolver(), Global.AUTO_TIME) == 0)
							{
								CustomToast.makeText(CollectionActivity.this,"Please Enable AutoDateTime Feature." , Toast.LENGTH_SHORT);
								return;
							}
							else
							{
								Intent i = new Intent(CollectionActivity.this,CollectionCashActivity.class);				
								startActivity(i);	
							}

							/*String lastRptDateTime = mDb.getLastReceiptDate(mDb.GetCashCounterDetails().getmBatch_No());
							String curtime = new SimpleDateFormat("ddMMyyHHmmss").format(Calendar.getInstance().getTime());
							SimpleDateFormat sdf1 = new SimpleDateFormat("ddMMyyHHmmss");		
							if(sdf1.parse(lastRptDateTime).after(sdf1.parse(curtime)))
							{
								CustomToast.makeText(CollectionActivity.this,"Last Receipt Withdrawal Time Exceeded. " + bDate , Toast.LENGTH_SHORT);
								return;
							}
							else
							{
								Intent i = new Intent(CollectionActivity.this,CollectionCashActivity.class);				
								startActivity(i);	
							}*/
						}
						catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
						//End 15-06-2017

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnCheque.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				try {					
					String currentdate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");						
					//Commented 09-02-2018
					/*if(sdf.parse(currentdate).after(sdf.parse(bDate)))  //If Current Date is greater then do not allow to generate Collection Receipts.
					{
						CustomToast.makeText(CollectionActivity.this,"Current Date exceeds alloted Batch Date " + bDate , Toast.LENGTH_SHORT);
						Intent i = new Intent(CollectionActivity.this,CollectionStartActivity.class);				
						startActivity(i);	
						return;

					}*/
					//End 09-02-2018
					if(sdf.parse(currentdate).before(sdf.parse(bDate)))  //If Current Date is less then do not allow to generate Collection Receipts.
					{
						CustomToast.makeText(CollectionActivity.this,"Current Date is less than alloted Batch Date " + bDate , Toast.LENGTH_SHORT);
						Intent i = new Intent(CollectionActivity.this,CollectionStartActivity.class);				
						startActivity(i);	
						return;

					}
					//17-06-2017
					if(mDb.GetCashCounterDetails().getmChqFlag().equals("0")) //CHQDD Authorization
					{
						CustomToast.makeText(CollectionActivity.this,"Cheque/DD Payment not allowed."  , Toast.LENGTH_SHORT);
						return;
					}
					else
					{
						try
						{
							if(Global.getInt(getContentResolver(), Global.AUTO_TIME) == 0)
							{
								CustomToast.makeText(CollectionActivity.this,"Please Enable AutoDateTime Feature." , Toast.LENGTH_SHORT);
								return;
							}
							else
							{
								Intent i = new Intent(CollectionActivity.this,CameraChequeActivity.class);			
								startActivity(i);	
							}
						}
						catch(Exception e)
						{

						}
						//15-06-2017
						/*try
						{
							String lastRptDateTime = mDb.getLastReceiptDate(mDb.GetCashCounterDetails().getmBatch_No());
							String curtime = new SimpleDateFormat("ddMMyyHHmmss").format(Calendar.getInstance().getTime());
							SimpleDateFormat sdf1 = new SimpleDateFormat("ddMMyyHHmmss");		
							if(sdf1.parse(lastRptDateTime).after(sdf1.parse(curtime)))
							{
								CustomToast.makeText(CollectionActivity.this,"Last Receipt Withdrawal Time Exceeded. " + bDate , Toast.LENGTH_SHORT);
								return;
							}
							else
							{

							}

						}
						catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	*/
						//End 15-06-2017	
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	

			}
		});
		btnCollectionReprint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				try {
					mDb.GetCollectionForConnId(CollectionObject.GetCollectionObject().getmConnectionNo(),CollectionObject.GetCollectionObject().getmReceipttypeflag());//24-07-2014
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
				//08-06-2017
				/*if(CollectionObject.GetCollectionObject().getmReprintCount() > 0)
				{
					CustomToast.makeText(CollectionActivity.this, "Reprint Count Exceeded.", Toast.LENGTH_SHORT);
					return;
				}*/
				if(mDb.GetCashCounterDetails().getmReprintFlag().equals("0"))
				{
					CustomToast.makeText(CollectionActivity.this, "Reprint Not Allowed.", Toast.LENGTH_SHORT);
					return;
				}
				else
				{
					// TODO Auto-generated method stub
					CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
					params.setContext(CollectionActivity.this);					
					params.setMainHandler(dh);				
					params.setButtonOkText(btnAlertCancel);
					params.setButtonCancelText(btnAlertOk);
					params.setTitle(alerttitleReprint);
					params.setFunctionality(REPRINT);
					cAlert  = new CustomAlert(params);	
				}
			}
		});
		btnCollectionLatePayment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {			

				Intent i = new Intent(CollectionActivity.this,Collection_LatePaymentActivity.class);				
				startActivity(i);				
			}
		});

		//btnprev --> get Previous Connection in the Billing Object
		btnprev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {					
				int flag=0;
				try 
				{					
					if(CollectionObject.GetCollectionObject().getmConnectionNo() == null)
					{
						CustomToast.makeText(CollectionActivity.this, "No Records.", Toast.LENGTH_SHORT);						
						return;
					}
					if(flag == 0)
					{
						f = CollectionObject.GetCollectionObject().getMmUID();
						flag++;
					}
					f--; 					  
					mDb.GetPrevColl(String.valueOf(f));						
					//Nitish on 22-05-2014
					while(f != CollectionObject.GetCollectionObject().getMmUID())
					{
						if(f > 0)
						{
							f--;
							mDb.GetPrevColl(String.valueOf(f));
						}
						else
						{
							break;
						}
					}
					//END Nitish on 22-05-2014
					ConnIdRRNo.setText("");
					GetAssignNecessaryFields(CollectionObject.GetCollectionObject());				
				} 
				catch (Exception e) 
				{
					Log.d(TAG, e.toString());
					e.printStackTrace();
				}
			}
		});
		//btnnext --> get Next Connection in the Billing Object
		btnnext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {					
				int flag = 0;
				try
				{

					if(CollectionObject.GetCollectionObject().getmConnectionNo() == null)
					{
						CustomToast.makeText(CollectionActivity.this, "No Records.", Toast.LENGTH_SHORT);						
						return;
					}
					if(flag==0)
					{
						f = CollectionObject.GetCollectionObject().getMmUID();
						flag++;
					}
					f++; 					  
					mDb.GetPrevColl(String.valueOf(f));	
					//Nitish on 22-05-2014
					int TotalConn = mDb.getCountofRecods();
					while(f != CollectionObject.GetCollectionObject().getMmUID())
					{
						if(f <= TotalConn)
						{
							f++;
							mDb.GetPrevColl(String.valueOf(f));
						}
						else
						{
							break;
						}
					}
					//Nitish on 22-05-2014
					ConnIdRRNo.setText("");
					GetAssignNecessaryFields(CollectionObject.GetCollectionObject());//Added By Tamilselvan on 02-04-2014					
				}
				catch (Exception e) 
				{
					Log.d(TAG, e.toString());
					e.printStackTrace();
				}
			}
		});

		/*btnCollectionOnline.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub


				if(ConnIdRRNo.getText().toString().length()==0)
				{
					CustomToast.makeText(CollectionActivity.this, "Enter RRNO.", Toast.LENGTH_SHORT);
					return;
				}

				if(mDb.isRRNoPresent(ConnIdRRNo.getText().toString().trim())==1) //If RRNO already Exists Do not allow Online payment
				{
					CustomToast.makeText(CollectionActivity.this, "RRNO exists in File.Online Payment Not Allowed", Toast.LENGTH_SHORT);
					return;
				}

				if(!new ConnectionDetector(CollectionActivity.this).isConnectedToInternet())
				{
					CustomToast.makeText(CollectionActivity.this, "Please Check Data Connection.", Toast.LENGTH_SHORT);
					return;
				}	

				rrnoPayOnline = ConnIdRRNo.getText().toString().trim();
				CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
				params.setContext(CollectionActivity.this);					
				params.setMainHandler(dh);				
				params.setButtonOkText(btnAlertCancel);
				params.setButtonCancelText(btnAlertOk);
				params.setTitle(alerttitleOnlinePay);
				params.setMsg("Pay Online?");
				params.setFunctionality(1);
				cAlert  = new CustomAlert(params);	
			}
		});*/
	}	
	//Common function for getting Collection object and assign value to controls 
	public void GetAssignNecessaryFields(ReadCollection CollObj)
	{
		try
		{
			mDb.GetCollectionForConnId(CollObj.getmConnectionNo(),rcpttype);//24-07-2014			
			conid.setText(CollObj.getmConnectionNo());
			rrNo.setText(CollObj.getmRRNo());						
			tariffCode.setText((new CommonFunction()).SplitTarifString(CollObj.getmTarifString()));
			load.setText((new CommonFunction()).GetSancLoadWithHPKWColl(CollObj));
			cc = mDb.GetCashCounterDetails(); //Set Cash Counter Object Parameters


			billed.setText(CollObj.getmBlCnt().equals("0")?ConstantClass.sNB:ConstantClass.sBilled);//if getmBlCnt is 0 then Connection is not Billed		

			BigDecimal txtArrers = CollObj.getmArears() == null ? new BigDecimal(0.00) : CollObj.getmArears().setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal txtArrersold = CollObj.getmArrearsOld().setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal txtIntstold = CollObj.getmIntrstOld().setScale(2, BigDecimal.ROUND_HALF_UP);		

			billamt.setText(CollObj.getmBillTotal()==null?"":CollObj.getmBillTotal().setScale(2,RoundingMode.HALF_EVEN).setScale(0, BigDecimal.ROUND_HALF_UP).toString() + ConstantClass.sPaise);
			duedate.setText((new CommonFunction()).DateConvertAddChar(CollObj.getmDueDate().trim(),"-"));	

			//24-07-2014		
			if(mDb.GetRcptCntForConnection(CollObj.getmConnectionNo(),rcpttype) == 0)//if RcptCnt is 0 then Collection is pending for connection 
			{
				btnCash.setVisibility(View.VISIBLE);	
				btnCheque.setVisibility(View.VISIBLE);	
				btnCollectionReprint.setVisibility(View.INVISIBLE);	//Set Text as Billing
				//btnCollectionLatePayment.setVisibility(View.VISIBLE);	//30-03-2015
				lblCollectionPaymentMode.setText(ConstantClass.sPaidAmt + ConstantClass.sNotPaid);

				if(CollectionStartActivity.isRevenueRcpt) //For Revenue Rpt
				{
					//Arrears = Arrears + ArrearsOld + IntrstOld
					arrears.setText(String.valueOf(txtArrers.add(txtArrersold.add(txtIntstold))));
				}
				else  //For MISC Receipt Set Balance
				{
					arrears.setText((new CommonFunction()).GetBalanceForMISCReceipt(CollObj.getmIODRemarks()));
				}			
			}
			else//if RcptCnt is 1 then Collection is Complete for connection and allow for reprint
			{
				btnCash.setVisibility(View.INVISIBLE);	
				btnCheque.setVisibility(View.INVISIBLE);	
				btnCollectionReprint.setVisibility(View.VISIBLE);	//Set Text as Billing
				//btnCollectionLatePayment.setVisibility(View.INVISIBLE);	//30-03-2015
				lblCollectionPaymentMode.setText(ConstantClass.sPaidAmt + CollObj.getmPaid_Amt() + ConstantClass.sPaise);
				if(CollectionStartActivity.isRevenueRcpt) //For Revenue Rpt
				{
					//Arrears is already added
					arrears.setText(String.valueOf(txtArrers));	
				}
				else  //For MISC Receipt Set Balance
				{
					arrears.setText((new CommonFunction()).GetBalanceForMISCReceipt(CollObj.getmIODRemarks()));
				}				
			}
			//Modified 11-06-2014
			if(CollObj.getmBOBillFlag()==1)
			{
				String bobAmount[] = CollObj.getmBOBilled_Amount().replace('.','#').split("#");				
				billed.setText(ConstantClass.sBilled);
				billamt.setText(bobAmount[0]+ ConstantClass.sPaise);
				//billamt.setText(String.valueOf((new BigDecimal(CollObj.getmBOBilled_Amount())).setScale(2,RoundingMode.HALF_EVEN).setScale(0, BigDecimal.ROUND_HALF_UP).toString()) + sPaise);
			}
			//22-06-2017
			try	
			{
				if(CollObj.getmBlCnt().equals("1") || CollObj.getmBOBillFlag()==1)	
				{
					
				}
				else
				{
					billamt.setText(arrears.getText().toString());
					/*BigDecimal nArreras  = new BigDecimal(arrears.getText().toString());
					BigDecimal nbillamt  = new BigDecimal(billamt.getText().toString());				
					nbillamt = nArreras.add(nbillamt).setScale(0,  BigDecimal.ROUND_HALF_UP);
					billamt.setText(String.valueOf(nbillamt)+ ConstantClass.sPaise);*/
				}
			}
			catch(Exception e){

			}
			//End 22-06-2017

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
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return OptionsMenu.navigate(item,CollectionActivity.this);			
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		CustomToast.makeText(CollectionActivity.this, "Please use menu to navigate.", Toast.LENGTH_SHORT);
		return;
	}
	@Override
	public void performAction(boolean alertResult, int functionality) {
		// TODO Auto-generated method stub
		//If alertReult is false Make Reprint else cancel
		if(!alertResult && functionality==REPRINT)
		{	

			bptr = new BluetoothPrinting(CollectionActivity.this);
			ringProgress = ProgressDialog.show(CollectionActivity.this, "Please wait..", "Printing...",true);//Spinner
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
		//If alertReult is false and for Online Payment Using RRNo
		/*if(!alertResult && functionality==PAYONLINE)
		{			
			try
			{			
				//Sent To Server for IMEI Verification=====================================================================================
				ringProgress = ProgressDialog.show(CollectionActivity.this, "Please wait..", "Fetching Details....",true);//Spinner
				ringProgress.setCancelable(false);
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try
						{
							//Thread.sleep(10000);
							HttpClient httpclt = new DefaultHttpClient();//object for HttpClient								
							HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/FileCreation_Android");//HttpPost method uri

							List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
							lvp.add(new BasicNameValuePair("RRNo", rrnoPayOnline));//SIM Serial No
							lvp.add(new BasicNameValuePair("IMEINumber", IMEINumber));//IMEI
							lvp.add(new BasicNameValuePair("SimNumber", SimNumber));//SIM Serial No							
							httpPost.setEntity((new UrlEncodedFormEntity(lvp)));
							httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");							
							HttpResponse res = httpclt.execute(httpPost);//execute post method
							HttpResponse k = res;
							HttpEntity ent = k.getEntity();
							if(ent != null)

							{
								String rValue = EntityUtils.toString(ent);//"ACK";//

								if(rValue.toString().length()==673)
								{
									int inserted = mDb.InsertSBOnlineCollection(rValue.toString());
									if(inserted!=1)
									{
										dh.post(new Runnable() {

											@Override
											public void run() {
												// TODO Auto-generated method stub
												//CustomToast.makeText(LoginActivity.this, "Your Mobile No. not registered in the server. Please register IMEI No. in the server.", Toast.LENGTH_LONG);
												CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
												params.setContext(CollectionActivity.this);					
												params.setMainHandler(dh);
												params.setMsg("Synchronisation Data Insertion Failed.");
												params.setButtonOkText("OK");
												//params.setButtonCancelText(btnAlertDetails);
												params.setTitle("Message");
												params.setFunctionality(-1);
												CustomAlert cAlert  = new CustomAlert(params);	
												return;
											}
										});
									}
								}								
								else
								{
									dh.post(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											//CustomToast.makeText(LoginActivity.this, "Your Mobile No. not registered in the server. Please register IMEI No. in the server.", Toast.LENGTH_LONG);
											CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
											params.setContext(CollectionActivity.this);					
											params.setMainHandler(dh);
											params.setMsg("Server Data Length Mismatch.");
											params.setButtonOkText("OK");
											//params.setButtonCancelText(btnAlertDetails);
											params.setTitle("Message");
											params.setFunctionality(-1);
											CustomAlert cAlert  = new CustomAlert(params);	
											return;
										}
									});
								}

							}
						}
						catch(Exception e)
						{
							dh.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									//CustomToast.makeText(LoginActivity.this, "Your Mobile No. not registered in the server. Please register IMEI No. in the server.", Toast.LENGTH_LONG);
									CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
									params.setContext(CollectionActivity.this);					
									params.setMainHandler(dh);
									params.setMsg("Failed To Obtain Data From Server.");
									params.setButtonOkText("OK");
									//params.setButtonCancelText(btnAlertDetails);
									params.setTitle("Message");
									params.setFunctionality(-1);
									CustomAlert cAlert  = new CustomAlert(params);	
									return;
								}
							});
						}
						ringProgress.dismiss();
					}
				}).start();

			}
			catch(Exception e)
			{
				ringProgress.dismiss();
				dh.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						//CustomToast.makeText(LoginActivity.this, "Your Mobile No. not registered in the server. Please register IMEI No. in the server.", Toast.LENGTH_LONG);
						CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
						params.setContext(CollectionActivity.this);					
						params.setMainHandler(dh);
						params.setMsg("Failed To Obtain Data From Server.");
						params.setButtonOkText("OK");
						//params.setButtonCancelText(btnAlertDetails);
						params.setTitle("Message");
						params.setFunctionality(-1);
						CustomAlert cAlert  = new CustomAlert(params);	
						return;
					}
				});
			}			
		}*/
	}
	//Bluetooth Code=======================================================================================	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)	
	public class ConnectedThread extends Thread //ConnectedThread
	{	
		public void run(){
			PrinterCharacterAlign pca = new PrinterCharacterAlign();//Class Alignment for printer 
			CommonFunction cFun = new CommonFunction();
			ReadCollection collObj = CollectionObject.GetCollectionObject();	

			try {
				mDb.GetCollectionForConnId(collObj.getmConnectionNo(),collObj.getmReceipttypeflag());//24-07-2014
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
			
			try 
			{
				
				int rprintcnt = CollectionObject.GetCollectionObject().getmReprintCount() +1; //02-04-2021				
				String pSubDiv = mDb.GetSubDivisionName().trim();				
				//String pBatchNo = pca.CreateBlankSpaceSequence(16, "*")+ mDb.GetCashCounterDetails().getmBatch_No().substring(16, 20); //Commented on 31-05-2019
				//Added 31-05-2019
				int batchlen = mDb.GetCashCounterDetails().getmBatch_No().trim().length();
				String pBatchNo = pca.CreateBlankSpaceSequence(12, "*")+ mDb.GetCashCounterDetails().getmBatch_No().trim().substring(batchlen-6, batchlen); 
				String pCustName = collObj.getmCustomerName().trim();				
				String pReceiptNo ="";
				try
				{
					String currentYear = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());//22-06-2017
					pReceiptNo =String.valueOf(collObj.getmReceipt_No());
					pReceiptNo = CommonFunction.getActualReceiptNo(pReceiptNo);
					pReceiptNo = currentYear+collObj.getmGvpId().toString().trim().substring(0,5) + pReceiptNo;
					
				}
				catch(Exception e)
				{

				}
				String pConnectionNo=collObj.getmConnectionNo();
				String pRRNO =collObj.getmRRNo();
				String pReceiptType = CollectionStartActivity.isRevenueRcpt ? ConstantClass.mRevenueRcpt : (ConstantClass.mMISCRcpt  + ConstantClass.mMiscASD);

				//String pArrearsBillAmt = " ";				
				String pPaidAmt = String.valueOf(collObj.getmPaid_Amt())+ ConstantClass.sPaise;
				String pRptDate = cFun.DateTimeConvertAddChar(collObj.getmDateTime().trim(),"/",":");
				String pMRName = collObj.getmReaderCode().trim();

				//24-07-2014
				/*if(CollectionStartActivity.isRevenueRcpt) //For Revenue Rpt
				{
					if(collObj.getmArrearsBill_Flag()==1) //Payment for Arrears
					{
						BigDecimal txtArrers = collObj.getmArears() == null ? new BigDecimal(0.00) : collObj.getmArears().setScale(2, BigDecimal.ROUND_HALF_UP);
						BigDecimal txtArrersold = collObj.getmArrearsOld().setScale(2, BigDecimal.ROUND_HALF_UP);
						BigDecimal txtIntstold = collObj.getmIntrstOld().setScale(2, BigDecimal.ROUND_HALF_UP);						
						pArrearsBillAmt = "(Arrears)   " + String.valueOf(txtArrers.add(txtArrersold.add(txtIntstold)));
					}
					else //If Billed then Payment for BillAmount
					{
						//Modified 11-06-2014
						if(collObj.getmBOBillFlag()==1) 
						{
							String bobAmount[] = collObj.getmBOBilled_Amount().replace('.','#').split("#");							
							pArrearsBillAmt = "(BillAmt)   "+bobAmount[0].trim()+ ConstantClass.sPaise;
						}
						else 							
							pArrearsBillAmt= "(BillAmt)   "+String.valueOf(collObj.getmBillTotal().setScale(2,RoundingMode.HALF_EVEN).setScale(0, BigDecimal.ROUND_HALF_UP).toString()) + ConstantClass.sPaise;
					}
				}					
				else  //For MISC Receipt Set Balance
				{
					//29-03-2016					
					pArrearsBillAmt= "(ASD Balance) " + (new CommonFunction()).GetBalanceForMISCReceipt(collObj.getmIODRemarks());
				}*/

				AmigosESCCmd pos = new AmigosESCCmd();
				//byte[] amigosFont = pos.POS_S_TextOut("", "UTF-8", 0, 1, 1, 0, 0x00);	

				Typeface typeface,typeface1;


				//typeface1=Typeface.create(Typeface.SANS_SERIF,Typeface.NORMAL);
				//typeface1=Typeface.createFromAsset(getAssets(), "fonts/OpenSans-ExtraBoldItalic.ttf");
				typeface1 = Typeface.create(Typeface.SANS_SERIF,Typeface.BOLD);

				bptr.sendData(ConstantClass.linefeed1);				


				String cPrint=  "";		
				cPrint = cPrint +   "\n"+ pca.CreateBlankSpaceSequence(24, " ")+ "CUSTOMER COPY/ DUPLICATE -" + String.valueOf(rprintcnt)+"\n";
				cPrint = cPrint +   "\n"+ pca.CreateBlankSpaceSequence(25, " ")+ "HESCOM CASH RECEIPT (MCC)"+"\n";
				cPrint += "\n  "+pca.CreateBlankSpaceSequence(4, " ")+ pca.CreateBlankSpaceSequence(50,"-")+"\n";


				cPrint += "\n  ಉಪ ವಿಭಾಗ /Sub Division:"+ pSubDiv.trim() + "\n";
				cPrint += "\n  ಬ್ಯಾಚ್ ಸಂಖ್ಯೆ/BatchNo     :" + pBatchNo.trim() + "\n";	
				cPrint += "\n";	
				if(pCustName.trim().length()>15)
					cPrint += "\n  ಗ್ರಾಹಕರ ಹೆಸರು/Cust Name          :"+ pCustName.trim().substring(0,15) + "\n";	
				else
					cPrint += "\n  ಗ್ರಾಹಕರ ಹೆಸರು/Cust Name          :"+ pCustName + "\n";			
				cPrint += "\n  ರಸೀದಿ ಸಂಖ್ಯೆ /Receipt No             :"+ pReceiptNo.trim() + "\n";	
				cPrint += "\n  ಆರ್.ಆರ್.ಸಂಖ್ಯೆ /RR No               :"+ pRRNO.trim() + "\n";	
				cPrint += "\n  ಗ್ರಾಹಕರ ಐ.ಡಿ /Acc Id                      :"+ pConnectionNo.trim() + "\n";	
				//cPrint += "\n  ಮೊತ್ತ  /AMT                                      :"+ pArrearsBillAmt.trim() + "\n";		
				cPrint += "\n  ರಸೀದಿ ವಿಧ /Receipt Type             :"+ pReceiptType.trim()  + "\n";

				if(collObj.getmPayment_Mode().equals("1")) //Cash payment
				{
					cPrint += "\n  ಪಾವತಿಸಿದ ವಿಧಾನ /Pmt Mode     :"+" CASH" + "\n";					
				}
				else
				{
					String pPaymentMode = "CHEQUE/DD";
					String pChequeDDNo =String.valueOf(collObj.getmChequeDDNo());
					String pChequeDDDate =  cFun.DateConvertAddCharNew(collObj.getmChequeDDDate().trim(),"/");
					String pBank =  mDb.GetBankName(collObj.getmBankID());	//02-03-2021			


					cPrint += "\n  ಪಾವತಿಸಿದ ವಿಧಾನ /Pmt Mode     :"+ pPaymentMode.trim()+ "\n";		
					cPrint += "\n  ಚೆಕ್/ಡಿ.ಡಿ ಸಂಖ್ಯೆ /CHQDD NO       :"+pChequeDDNo.trim() + "\n";	
					cPrint += "\n  ಚೆಕ್/ಡಿ.ಡಿ ದಿನಾಂಕ /CHQDD Date :"+ pChequeDDDate.trim() + "\n";	
					//02-03-2021
					if(pBank.trim().length() > 15)
						cPrint += "\n  ಬ್ಯಾಂಕ್ /Bank                                   :"+pBank.trim().substring(0, 15) + "\n";					
					else
						cPrint += "\n  ಬ್ಯಾಂಕ್ /Bank                                   :"+pBank.trim() + "\n";	

				}
				cPrint += "\n  ಪಾವತಿಸಿದ ಮೊತ್ತ/Paid Amt           :"+ pPaidAmt.trim()  + "\n";	
				cPrint += "\n  ರಸೀದಿ  ದಿನಾಂಕ/Receipt Date       :"+ pRptDate.trim()  + "\n";
				cPrint += "\n  ಮಾ.ಓ.ಸಂಕೇತ/MR Name             :"+ pMRName  + "\n\n";	

				try
				{
					//BatchNo = LocCode(7)+Date(8)+SBMNo(8)+StaffId(4)+SessionId(4)
					//BatchNo = 4210401310320213e10e27450221411
					String batchno = mDb.GetCashCounterDetails().getmBatch_No().trim();
					String mStaffId = batchno.substring(batchno.length()-8, batchno.length()-4);
					String mSessionId = batchno.substring(batchno.length()-4, batchno.length());
					cPrint += "\n  UNIQUEID-  " + mStaffId + "/" + mSessionId + "/" + pReceiptNo + "\n\n"; //Added 31-03-2021
					cPrint += "\n  DEVNO - "+ LoginActivity.IMEINumber;	 		 			
				}
				catch(Exception e)
				{

				}
				byte[] imgData = pos.multiLingualPrint(cPrint,22,typeface1,-5);
				bptr.sendData(imgData); 				

				//barvalue = connno + receiptno + paidamt with padding( 7 + 5 + 7 )
				String barValue = cFun.GetConnectionIdPadding(collObj.getmConnectionNo().trim()) + cFun.GetReceiptNoPadding(String.valueOf(mDb.GetMaxReceiptNo(mDb.GetCashCounterDetails().getmBatch_No()))) + collObj.getmPaid_Amt() ;
				//int barlen = (cFun.GetConnectionIdPadding(collObj.getmConnectionNo().trim()) + cFun.GetReceiptNoPadding(String.valueOf(mDb.GetMaxReceiptNo(mDb.GetCashCounterDetails().getmBatch_No()))) + collObj.getmPaid_Amt()).length() ;
				try
				{
					long addtime = Calendar.getInstance().getTimeInMillis() + 2000;//1 second delay
					while(addtime > Calendar.getInstance().getTimeInMillis())
					{										

					}						
					byte barCodeData[] = pos.POS_S_SetBarcode(barValue.trim(), 0, 73, 2,48, 0, 2);
					bptr.sendData(barCodeData);	
				}
				catch(Exception e)
				{

				}				
				//bptr.sendData(amigosFont);
				bptr.sendData(ConstantClass.linefeed1);					
				bptr.sendData(ConstantClass.linefeed1);
				bptr.sendData(ConstantClass.linefeed1); 
				bptr.sendData(ConstantClass.linefeed1); 			
				try
				{
					mDb.UpdateCollectionReprintCount(CommonFunction.getActualReceiptNo(String.valueOf(collObj.getmReceipt_No()).trim()),String.valueOf(rprintcnt)); //08-06-2017
					mDb.EventLogSave("8", LoginActivity.IMEINumber, LoginActivity.SimNumber, "Receipt Reprint Completed For Connection:  " + collObj.getmConnectionNo().trim());//15-07-2016
				}
				catch(Exception e)
				{

				}

			} 
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				Log.d(TAG,e.toString());
				//if printer turn off without of server, then after printer turn on process again start
			}
			finally
			{
				try {
					bptr.closeBT();
					if(BluetoothPrinting.isConnected)
					{
						Thread.sleep(6000);
						//isException = false;
					}	
				} 	/**/	
				catch (Exception e) {
					Log.d(TAG,e.toString());
					e.printStackTrace();
				}	
				ringProgress.dismiss();
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						//bptr.beginListenForData();
						dh.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								//Toast.makeText(Billing.this, "Printing finish.", Toast.LENGTH_SHORT).show();
								//btn.setVisibility(Button.VISIBLE);
								btnCollectionReprint.setEnabled(true);
							}
						});
					}
				}).start();/**/
			}
		}
		public void cancel()
		{

		}
	}//END ConnectedThread
	//END Bluetooth Code====================================================================================
}



