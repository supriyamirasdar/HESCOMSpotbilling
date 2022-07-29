package in.nsoft.hescomspotbilling;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings.Global;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CollectionCashActivity extends Activity implements AlertInterface {	

	Button btnconfirm;
	ReadCollection collObj;
	private TextView conid,rrNo,arrears,billamt,lblRevenueMisc,lblArrears;
	private EditText payamt;
	private final DatabaseHelper mDb =new DatabaseHelper(this);
	private static final String TAG = CollectionActivity.class.getName();
	BluetoothPrinting bptr;	

	//Nitish 24-07-2014
	String rcpttype;
	static final int REQUEST_ENABLE_BT = 0;
	BigDecimal aramt,pamt,blamt;//18-07-2015
	ProgressDialog ringProgress;
	int result =-1;
	long diff;	
	BigDecimal cLimit,cMade;	
	CashCounterObject CC ;	

	//Nitish 04-04-2014
	Handler dh;
	CustomAlert cAlert; 
	boolean alertResult;		
	final static String alerttitle = "Cash Payment";
	final static String btnAlertOk = "Pay";
	final static String btnAlertCancel = "Cancel";		
	final static int TwoButtons = 2;
	//Nitish End

	static String IMEINumber = "";	
	static String SimNumber = "";	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection_cash);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);	

		/*TelephonyManager mgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		IMEINumber = mgr.getDeviceId();
		SimNumber = mgr.getSimSerialNumber();*/
		
		try
		{
			//Get Mobile Details ==============================================================
			//10-03-2021
			IMEINumber = CommonFunction.getDeviceNo(CollectionCashActivity.this);
			SimNumber = CommonFunction.getSIMNo(CollectionCashActivity.this);

		}
		catch(Exception e)
		{
							
		}

		conid =  (TextView)findViewById(R.id.txtCollectionCashConnectionId);
		rrNo =  (TextView)findViewById(R.id.txtCollectionCashRRNo);		
		arrears = (TextView)findViewById(R.id.txtCollectionCashArrears);
		billamt = (TextView)findViewById(R.id.txtCollectionCashBillAmount);	
		payamt =  (EditText)findViewById(R.id.txtCollectionCashPaymentAmount);
		btnconfirm = (Button)findViewById(R.id.btnCollectionCashConfirm);	
		lblRevenueMisc= (TextView)findViewById(R.id.lblRevenueMisc);//lbl for Revenue/Misc
		lblArrears= (TextView)findViewById(R.id.lblCollectionCashArrears);//lbl for Arrears/Balance

		dh = new Handler();

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


		//Nitish 22-04-2014
		CC = mDb.GetCashCounterDetails();
		String colAmt = mDb.GetCollectionAmount(CC.getmBatch_No()); //Get Total Collected amount for that batchno	
		cLimit = new BigDecimal(CC.getmCashLimit());
		cMade = new BigDecimal(colAmt);
		//Nitish 22-04-2014

		try
		{
			collObj=CollectionObject.GetCollectionObject();
			conid.setText(collObj.getmConnectionNo());
			rrNo.setText(collObj.getmRRNo());

			BigDecimal txtArrers = collObj.getmArears() == null ? new BigDecimal(0.00) : collObj.getmArears().setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal txtArrersold = collObj.getmArrearsOld().setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal txtIntstold = collObj.getmIntrstOld().setScale(2, BigDecimal.ROUND_HALF_UP);	

			if(CollectionStartActivity.isRevenueRcpt)
			{
				arrears.setText(String.valueOf(txtArrers.add(txtArrersold.add(txtIntstold))));
			}
			else  //For MISC Receipt Set Balance
			{
				arrears.setText((new CommonFunction()).GetBalanceForMISCReceipt(collObj.getmIODRemarks()));
			}
			billamt.setText(collObj.getmBillTotal()==null?(new BigDecimal(0.00)).toString():collObj.getmBillTotal().setScale(2,RoundingMode.HALF_EVEN).setScale(0, BigDecimal.ROUND_HALF_UP).toString() + ConstantClass.sPaise);

			//Modified 11-06-2014
			if(collObj.getmBOBillFlag()==1)
			{
				String bobAmount[] = collObj.getmBOBilled_Amount().replace('.','#').split("#");				
				billamt.setText(bobAmount[0]+ ConstantClass.sPaise);
				//billamt.setText(String.valueOf((new BigDecimal(CollObj.getmBOBilled_Amount())).setScale(2,RoundingMode.HALF_EVEN).setScale(0, BigDecimal.ROUND_HALF_UP).toString()) + sPaise);
			}

			//22-06-2017
			try	
			{
				if(collObj.getmBlCnt().equals("1") || collObj.getmBOBillFlag()==1)	
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
			//End 22-06-2017

		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d(TAG, e.toString());
			e.printStackTrace();
		}
		btnconfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try
				{
					//25-01-2016
					/*boolean mobileDataEnabled = android.provider.Settings.Secure.getInt(getContentResolver(),"mobile_data",1) == 1;
					if(!mobileDataEnabled)
					{
						CustomToast.makeText(CollectionCashActivity.this,"Please Enable Data Connection!!"  , Toast.LENGTH_SHORT);
						Intent i = new Intent(CollectionCashActivity.this,CollectionStartActivity.class);				
						startActivity(i);
						return;
					}*/
					//28-07-2016
					try
					{
						//Modified Nitish 16-03-2017
						if(!CC.getmSIMNo().trim().equals(SimNumber))
						{
							CustomToast.makeText(CollectionCashActivity.this, "Sim Number Not Matching Contact NSOFT.", Toast.LENGTH_SHORT);
							return;
						}

					}
					catch(Exception e)
					{

					}
					//27-06-2017
					if(Global.getInt(getContentResolver(), Global.AUTO_TIME) == 0)
					{
						CustomToast.makeText(CollectionCashActivity.this,"Please Enable AutoDateTime Feature." , Toast.LENGTH_SHORT);
						return;
					}


					String bDate = (new CommonFunction()).DateConvertAddChar( mDb.GetCashCounterDetails().getmBatch_Date(), "-"); //Obtain BatchDate
					String currentdate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");					

					//Modified 09-02-2018
					String CurDate = new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime());
					String CurMonthYear = new SimpleDateFormat("MMyyyy").format(Calendar.getInstance().getTime()); //New
					String batchMonthYear = mDb.GetCashCounterDetails().getmBatch_Date().substring(2, 8); //New
					int curDay = Integer.valueOf(CurDate.substring(0, 2)); //New
					int nextBatchDay = Integer.valueOf(mDb.GetCashCounterDetails().getmBatch_Date().substring(0, 2)) + 1; //New
					if(sdf.parse(currentdate).after(sdf.parse(bDate)))  //If Current Date is greater than Batch date.
					{
						if(!(CurMonthYear.equals(batchMonthYear) && (curDay == nextBatchDay)))//If Current Date not equal to next batch date do not allow receipts.
						{
							CustomToast.makeText(CollectionCashActivity.this,"Current Date exceeds alloted Batch Date " + bDate , Toast.LENGTH_SHORT);
							Intent i = new Intent(CollectionCashActivity.this,CollectionStartActivity.class);				
							startActivity(i);
							return;
						}						
					}
					//End 09-02-2018


					//27-11-2014
					if(sdf.parse(currentdate).before(sdf.parse(bDate)))  //If Current Date is Less then do not allow to generate Collection Receipts.
					{
						CustomToast.makeText(CollectionCashActivity.this,"Current Date Less than alloted Batch Date " + bDate , Toast.LENGTH_SHORT);
						Intent i = new Intent(CollectionCashActivity.this,CollectionStartActivity.class);				
						startActivity(i);
						return;
					}
					if(payamt.length() == 0)
					{
						CustomToast.makeText(CollectionCashActivity.this,"Enter Payment Amount." , Toast.LENGTH_SHORT);
						return;
					}
					else if(Integer.parseInt(payamt.getText().toString()) == 0 )
					{
						CustomToast.makeText(CollectionCashActivity.this,"Payment Amount cannot be Zero." , Toast.LENGTH_SHORT);
						return;
					}
					else if(Integer.parseInt(payamt.getText().toString()) > 10000 )
					{
						CustomToast.makeText(CollectionCashActivity.this,"Cash Payment cannot be greater than 10000." , Toast.LENGTH_SHORT);
						return;
					}
					aramt = new BigDecimal(arrears.getText().toString());
					pamt = new BigDecimal(payamt.getText().toString());				
					BigDecimal addpamtcMade = pamt.add(cMade);				
					//Nitish 22-04-2014
					if(addpamtcMade.compareTo(cLimit) > 0 )
					{
						CustomToast.makeText(CollectionCashActivity.this,"Cash Limit Exceeded." , Toast.LENGTH_SHORT);
						return;
					}						
					//22-06-2017
					if(CC.getmPartPayFlag().equals("1"))
					{
						BigDecimal parpaymentperc = new BigDecimal(CC.getmPartPayPerc().toString().trim());
						parpaymentperc = parpaymentperc.multiply(new BigDecimal(0.01));
						if((aramt.multiply(parpaymentperc)).compareTo(pamt) > 0)
						{
							CustomToast.makeText(CollectionCashActivity.this,"Payment Amount must be atleast " + CC.getmPartPayPerc().trim() +" Percent of Arrears Amount." , Toast.LENGTH_LONG);
							//CustomToast.makeText(CollectionCashActivity.this,"Payment Amount cannot be less than Arrears/Balance Amount." , Toast.LENGTH_LONG);
							payamt.setText("");
							return;
						}

					}
					else 
					{
						if(aramt.compareTo(pamt) > 0 )
						{
							CustomToast.makeText(CollectionCashActivity.this,"Payment Amount cannot be less than Arrear Amount." , Toast.LENGTH_LONG);
							payamt.setText("");
							return;
						}
					}
					//End 22-06-2017
					/*if(CollectionStartActivity.isRevenueRcpt)
					{
						if((aramt.multiply(new BigDecimal(0.25))).compareTo(pamt) > 0)
						{
							CustomToast.makeText(CollectionCashActivity.this,"Payment Amount must be atleast 25 Percent of Arrears Amount." , Toast.LENGTH_LONG);
							//CustomToast.makeText(CollectionCashActivity.this,"Payment Amount cannot be less than Arrears/Balance Amount." , Toast.LENGTH_LONG);
							payamt.setText("");
							return;
						}	
						//23-06-2015
						if(aramt.compareTo(pamt) > 0 && !(CollectionObject.GetCollectionObject().getmTariffCode()==1))
						{							
							CustomToast.makeText(CollectionCashActivity.this,"Payment Amount cannot be less than Arrears/Balance Amount." , Toast.LENGTH_LONG);
							payamt.setText("");
							return;
						}

						//18-07-2015
						blamt= new BigDecimal(billamt.getText().toString());
						//If BillAmt is greater than or equal to Arrears  or
						//If BillAmt is Zero then payment amt must be greater than Arrear
						if(blamt.compareTo(aramt)>0 || (new BigDecimal(0.00)).compareTo(blamt)>=0)						
						{
							if(CollectionObject.GetCollectionObject().getmTariffCode()==1)  //LT-1 
							{
								if((aramt.multiply(new BigDecimal(0.25))).compareTo(pamt) > 0)
								{
									CustomToast.makeText(CollectionCashActivity.this,"Payment Amount must be atleast 25 Percent of Arrears Amount." , Toast.LENGTH_LONG);
									//CustomToast.makeText(CollectionCashActivity.this,"Payment Amount cannot be less than Arrears/Balance Amount." , Toast.LENGTH_LONG);
									payamt.setText("");
									return;
								}

							}
							//17-06-2017
							if(CC.getmPartPayFlag().equals("1"))
							{
								BigDecimal parpaymentperc = new BigDecimal(CC.getmPartPayPerc().toString().trim());
								parpaymentperc = parpaymentperc.multiply(new BigDecimal(0.01));
								if((aramt.multiply(parpaymentperc)).compareTo(pamt) > 0)
								{
									CustomToast.makeText(CollectionCashActivity.this,"Payment Amount must be atleast " + CC.getmPartPayPerc().trim() +" Percent of Arrears Amount." , Toast.LENGTH_LONG);
									//CustomToast.makeText(CollectionCashActivity.this,"Payment Amount cannot be less than Arrears/Balance Amount." , Toast.LENGTH_LONG);
									payamt.setText("");
									return;
								}

							}
							else 
							{
								if(aramt.compareTo(pamt) > 0 )
								{
									CustomToast.makeText(CollectionCashActivity.this,"Payment Amount cannot be less than Arrear Amount." , Toast.LENGTH_LONG);
									payamt.setText("");
									return;
								}
							}

						}
						else if(aramt.compareTo(blamt)>0) //If Arrears is greater than Bill Amt then payment amt must be greater than billamt
						{
							if(blamt.compareTo(pamt) > 0)
							{	

								CustomToast.makeText(CollectionCashActivity.this,"Payment Amount cannot be less than Bill Amount." , Toast.LENGTH_LONG);
								payamt.setText("");
								return;
							}
						}

						//End 18-07-2015
					}*/
					//else
					//{
					/*if(aramt.compareTo(pamt) > 0)
						{
							//CustomToast.makeText(CollectionCashActivity.this,"Payment Amount must be atleast 25 Percent of Arrears/Balance Amount." , Toast.LENGTH_LONG);
							CustomToast.makeText(CollectionCashActivity.this,"Payment Amount cannot be less than Balance Amount." , Toast.LENGTH_LONG);
							payamt.setText("");
							return;
						}*/
					//}			
					//Time Synchronisation
					/*if(!TimeChangedReceiver.isValid())
				{
					new TimeSync().execute();					
				}
				else
				{
					SaveFunction();			
				}*/
					SaveFunction();	
				}
				catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}				
		});		
	}
	public void SaveFunction()
	{	
		StringBuilder sb = new StringBuilder();	
		BigDecimal txtArrers = collObj.getmArears() == null ? new BigDecimal(0.00) : collObj.getmArears().setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal txtArrersold = collObj.getmArrearsOld().setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal txtIntstold = collObj.getmIntrstOld().setScale(2, BigDecimal.ROUND_HALF_UP);	

		//sb.append("Arrears			    : "+ String.valueOf(txtArrers.add(txtArrersold.add(txtIntstold))) + ConstantClass.eol);
		//Modified 11-06-2014
		/*if(collObj.getmBOBillFlag()==1)
		{
			String bobAmount[] = collObj.getmBOBilled_Amount().replace('.','#').split("#");				
			sb.append("Total Bill Amt		     : "+bobAmount[0]+ ConstantClass.sPaise + ConstantClass.eol);
			//billamt.setText(String.valueOf((new BigDecimal(CollObj.getmBOBilled_Amount())).setScale(2,RoundingMode.HALF_EVEN).setScale(0, BigDecimal.ROUND_HALF_UP).toString()) + sPaise);
		}
		else
		{
			sb.append("Total Bill Amt		     : "+ collObj.getmBillTotal().setScale(2,RoundingMode.HALF_EVEN).setScale(0, BigDecimal.ROUND_HALF_UP).toString() + ConstantClass.sPaise+ ConstantClass.eol);
		}*/
		sb.append("Payment Amt : "+ pamt.setScale(0,RoundingMode.HALF_UP).toString() +  ConstantClass.sPaise + ConstantClass.eol);	

		CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
		params.setContext(CollectionCashActivity.this);					
		params.setMainHandler(dh);
		params.setMsg(sb.toString());
		params.setButtonOkText(btnAlertCancel);
		params.setButtonCancelText(btnAlertOk);
		params.setTitle(alerttitle);
		params.setFunctionality(TwoButtons);
		cAlert  = new CustomAlert(params);
	}
	//Executes in background
	private class TimeSync extends AsyncTask<Void, Void, Void>  //GenericTypes AsyncTask<parametertype, progressparameter, postexecuteparam >
	{
		final ProgressDialog ringProgress = ProgressDialog.show(CollectionCashActivity.this, "Please wait..", "Time Sync...",true);
		String rValue = "";
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub	
			try
			{
				HttpClient httpclt = new DefaultHttpClient();//object for HttpClient								
				//HttpPost httpPost = new HttpPost("http://123.201.131.113:8118/service.asmx/GetSyncVerifyCode");//HttpPost method uri
				HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/getCurrentDateTime");//HttpPost method uri
				httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");							
				HttpResponse res = httpclt.execute(httpPost);//execute post method
				HttpResponse k = res;
				HttpEntity ent = k.getEntity();
				if(ent != null)
				{
					rValue = EntityUtils.toString(ent);		

				}
			}
			catch (Exception e)
			{				
				cancel(true);				
				e.printStackTrace();
			}		
			return null;		
		}		
		// On Completion of Time Sync  Process 
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

			ringProgress.setCancelable(false);
			ringProgress.dismiss();	
			if(rValue.indexOf("NACK")!=-1) //NACK received 
			{					
				CustomToast.makeText(CollectionCashActivity.this, "Error in Time Synchronisation", Toast.LENGTH_SHORT);				
				return;
			}
			else if(rValue.indexOf("ACK")!=-1)//ACK received 
			{				
				rValue = rValue.replace("ACK", ""); //Replace ACK				
				try
				{
					String currentdate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
					String currentTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());					

					SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
					SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");

					String serDate = (new CommonFunction()).DateConvertAddChar(rValue.substring(0, 8), "-"); // ddmmyyyy to dd-mm-yyyy
					String serTime = (new CommonFunction()).TimeConvertAddChar(rValue.substring(8,12), ":"); // hhmm to hh:mm

					serTime = serTime + ConstantClass.zeroSec; //Concatenate seconds	


					if(serTime.compareTo(currentTime) > 0)
						diff =   sdfTime.parse(serTime).getTime() - sdfTime.parse(currentTime).getTime();
					else
						diff =   sdfTime.parse(currentTime).getTime() - sdfTime.parse(serTime).getTime();				

					//Chech Current Date matches Server Date // 1s -> 1000ms  1min -> 60000ms  10min ->600000ms  -> 10 Min
					if(sdfDate.parse(currentdate).after(sdfDate.parse(serDate)) || sdfDate.parse(currentdate).before(sdfDate.parse(serDate)) || (diff > ConstantClass.difftime) )
					{							
						CustomToast.makeText(CollectionCashActivity.this,"Mobile Date Time not matching with Server."  , Toast.LENGTH_SHORT);
						return;
					}						
					else // If Time Difference within 10 Min
					{
						TimeChangedReceiver.setValid(true);
						CollectionObject.GetCollectionObject().setmIsTimeSync(true); 					
						SaveFunction();
						return;
					}
				} 
				catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}				
			else//NACK received or any Error 
			{
				CustomToast.makeText(CollectionCashActivity.this, "Error Occured", Toast.LENGTH_SHORT);
			}		
		}	
		//If Exception Occured 
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false);
			ringProgress.dismiss();
			CustomToast.makeText(CollectionCashActivity.this, "Time Synchronisation Failed.", Toast.LENGTH_SHORT);			
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
		return OptionsMenu.navigate(item,CollectionCashActivity.this);			
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		CustomToast.makeText(CollectionCashActivity.this, "Please use menu to navigate.", Toast.LENGTH_SHORT);
		return;
	}

	@Override
	public void performAction(boolean alertResult, int functionality) {
		// TODO Auto-generated method stub
		//If alertReult is false Make Payment else cancel
		if(!alertResult)
		{
			//22-02-2018
			try 
			{				
				if(Global.getInt(getContentResolver(), Global.AUTO_TIME) == 0)
				{
					CustomToast.makeText(CollectionCashActivity.this,"Please Enable AutoDateTime Feature." , Toast.LENGTH_SHORT);
					return;
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//End 22-01-2018
			payamt =  (EditText)findViewById(R.id.txtCollectionCashPaymentAmount);
			collObj.setmPaid_Amt(Integer.valueOf(payamt.getText().toString()));	
			collObj.setmPayment_Mode("1");  //1 -> Cash Payment
			collObj.setmSBMNumber(IMEINumber); //Set SBMNumber as IMEINo

			collObj.setmReceipttypeflag(rcpttype); //24-07-2014
			collObj.setmRemarkId(0);
			collObj.setmScheduledDate("0");

			//Modified Date 11-06-2014
			if(collObj.getmBlCnt().equals("0") && collObj.getmBOBillFlag()==0) //If Not Billed then Payment is for Arrears. Set ArrearsBill_Flag=1
			{
				collObj.setmArrearsBill_Flag(1);
			}
			else //If Billed then Payment is for BillTotal. Set ArrearsBill_Flag=2
			{
				collObj.setmArrearsBill_Flag(2);
			}
			new Thread(new Runnable() {

				@Override
				public void run() 
				{
					//result = mDb.CollectionSave();
					//Nitish 06-02-2018
					try
					{					
						result = mDb.CollectionSave();						
						if(mDb.getConnCountForRpt(collObj.getmConnectionNo(),rcpttype) > 1) //Obtain Duplicate Receipt
						{
							//Delete all Duplicate Receipts and perform collection again
							mDb.deleteDuplicateCollection(collObj.getmConnectionNo(),rcpttype);
							result = 0;
						}						
					}
					catch(Exception e)
					{
						result = 0;
					}
					//End 06-02-2018
					dh.post(new Runnable() 
					{
						@Override
						public void run() 
						{
							if(result == 1)
							{
								CustomToast.makeText(CollectionCashActivity.this, "Payment completed Successfully.", Toast.LENGTH_SHORT);

								//Bluetooth Code===========================================				
								bptr = new BluetoothPrinting(CollectionCashActivity.this);

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
									CustomToast.makeText(CollectionCashActivity.this, "Problem in Printing.", Toast.LENGTH_SHORT);
									Intent i = new Intent(CollectionCashActivity.this, CollectionActivity.class);
									startActivity(i);
								}	
								//END Bluetooth Code=======================================

								//Nitish 19-05-2014
								/*try {
									Thread.sleep(12000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}*/
								//CollectionObject.Remove(); //Clear Collection Object
								//Intent i = new Intent(CollectionCashActivity.this, CollectionActivity.class);
								//startActivity(i);
							}
							else//Tamilselvan on 20-03-2014 
							{
								CustomToast.makeText(CollectionCashActivity.this, "Problem in saving data.", Toast.LENGTH_SHORT);
							}
						}
					});
				}
			}).start();
			ringProgress = ProgressDialog.show(CollectionCashActivity.this, "Please wait..", "Data Saving and Printing..",true);//Spinner
			ringProgress.setCancelable(false);	
		}
	}

	//Bluetooth Code=======================================================================================	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)	
	public class ConnectedThread extends Thread //ConnectedThread
	{	
		public void run(){
			PrinterCharacterAlign pca = new PrinterCharacterAlign();//Class Alignment for printer 
			CommonFunction cFun = new CommonFunction();

			try 
			{
				String pSubDiv = mDb.GetSubDivisionName();				
				//String pBatchNo = pca.CreateBlankSpaceSequence(16, "*")+ mDb.GetCashCounterDetails().getmBatch_No().substring(16, 20); //Commented on 31-05-2019
				//Added 31-05-2019
				int batchlen = mDb.GetCashCounterDetails().getmBatch_No().trim().length();
				String pBatchNo = pca.CreateBlankSpaceSequence(12, "*")+ mDb.GetCashCounterDetails().getmBatch_No().trim().substring(batchlen-6, batchlen); 
				String pCustName = collObj.getmCustomerName();				
				String pReceiptNo ="";
				
				try
				{
					String currentYear = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());//22-06-2017
					pReceiptNo = CommonFunction.getActualReceiptNo(String.valueOf(collObj.getmReceipt_No()));
					pReceiptNo = currentYear+collObj.getmGvpId().toString().trim().substring(0,5) + pReceiptNo;							
					
				}catch(Exception e)
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
					pArrearsBillAmt= "(ASD Balance)" + (new CommonFunction()).GetBalanceForMISCReceipt(collObj.getmIODRemarks());
				}*/
				
				//String timeStamp = cFun.GetCurrentTime();
				bptr.sendData(ConstantClass.data1);
				AmigosESCCmd pos = new AmigosESCCmd();
				//byte[] amigosFont = pos.POS_S_TextOut("", "UTF-8", 0, 1, 1, 0, 0x00);	
				for(int i=1; i< 3 ; i++)
				{
					
					

					Typeface typeface1;


					//typeface1=Typeface.create(Typeface.SANS_SERIF,Typeface.NORMAL);
					//typeface1=Typeface.createFromAsset(getAssets(), "fonts/OpenSans-ExtraBoldItalic.ttf");
					typeface1 = Typeface.create(Typeface.SANS_SERIF,Typeface.BOLD);
					//bptr.sendData(amigosFont);////Reprint duplicate copy			
					bptr.sendData(ConstantClass.linefeed1);				
					

					String cPrint=  "";			
				
					if(i==1)
					{
						cPrint = cPrint +   "\n"+ pca.CreateBlankSpaceSequence(40, " ")+ "HESCOM COPY"+"\n";
					}
					else
					{
						cPrint = cPrint +   "\n"+ pca.CreateBlankSpaceSequence(40, " ")+ "CUSTOMER COPY"+"\n";
					}	
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
					cPrint += "\n  ಪಾವತಿಸಿದ ವಿಧಾನ /Pmt Mode     :"+" CASH" + "\n";					
					cPrint += "\n  ಪಾವತಿಸಿದ ಮೊತ್ತ/Paid Amt           :"+ pPaidAmt.trim()  + "\n";	
					cPrint += "\n  ರಸೀದಿ  ದಿನಾಂಕ/Receipt Date       :"+ pRptDate.trim()  + "\n";
					cPrint += "\n  ಮಾ.ಓ.ಸಂಕೇತ/MR Name             :"+ pMRName+ "\n\n";	
					
					try
					{
						cPrint += "\n  DEVNO - "+ LoginActivity.IMEINumber;	 			
					}
					catch(Exception e)
					{

					}
					
					byte[] imgData = pos.multiLingualPrint(cPrint,22,typeface1,-5);
					bptr.sendData(imgData); 
					
					//END Extra Fields=============================================================================================

					
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
					
					bptr.sendData(ConstantClass.linefeed1);
					
				}
				//bptr.sendData(amigosFont);
				bptr.sendData(ConstantClass.linefeed1);
				bptr.sendData(ConstantClass.linefeed1);
				bptr.sendData(ConstantClass.linefeed1); 
				bptr.sendData(ConstantClass.linefeed1); 
				try
				{
					mDb.EventLogSave("7", LoginActivity.IMEINumber, LoginActivity.SimNumber, "Cash Receipt Printing Completed For Connection: " + collObj.getmConnectionNo().trim() + " RPTNO:" + String.valueOf(mDb.GetMaxReceiptNo(mDb.GetCashCounterDetails().getmBatch_No())) + " COLLAMT:" + String.valueOf(collObj.getmPaid_Amt()));//31-12-2016
				}
				catch(Exception e)
				{

				}

				//31-12-2016
				try
				{
					StringBuilder sb = new StringBuilder();
					sb=mDb.GetCollDataLog();					
					EncryptDecrypt en = new EncryptDecrypt("nsoft987");	
					String filename = mDb.GetStatusMasterValue("7");
					en.encryptLogData(sb.toString(),filename);
				}
				catch(Exception e)
				{

				}
				//End 31-12-2016

			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				Log.d(TAG,e.toString());
				//if printer turn off without of server, then after printer turn on process again start
				dh.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						CustomToast.makeText(CollectionCashActivity.this, "Problem in Printing.", Toast.LENGTH_SHORT);
					}
				});

			}
			finally
			{
				try {
					if(BluetoothPrinting.isConnected)
					{
						Thread.sleep(5000);

					}
					dh.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub							
							Intent i = new Intent(CollectionCashActivity.this, CollectionActivity.class);
							startActivity(i);
							CollectionCashActivity.this.finish(); //21-02-2018
						}
					});
					bptr.closeBT();					
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

		}
	}//END ConnectedThread
	//END Bluetooth Code====================================================================================
}
