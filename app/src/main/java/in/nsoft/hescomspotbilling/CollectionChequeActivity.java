package in.nsoft.hescomspotbilling;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings.Global;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CollectionChequeActivity extends Activity  implements AlertInterface{		

	AutoCompleteTextView autoBank;
	Button btnconfirm;
	ReadCollection collObj;	
	private EditText payamt,chqddno,chqdddate;	
	private TextView conid,rrNo,arrears,billamt,lblRevenueMisc,lblArrears;
	private ImageView imgClearBank;
	private final DatabaseHelper mDb =new DatabaseHelper(this);	
	private static final String TAG = CollectionChequeActivity.class.getName();
	BigDecimal aramt,pamt,blamt;//18-07-2015
	private int bankId;
	ProgressDialog ringProgress;
	int result =-1;
	private String bankName;
	//Nitish 04-04-2014
	Handler dh;
	CustomAlert cAlert; 
	boolean alertResult,autoselectBank;		
	final static String alerttitle = "Cheque Payment";
	final static String btnAlertOk = "Pay";
	final static String btnAlertCancel = "Cancel";	
	static final int REQUEST_ENABLE_BT = 0;
	String IMEINumber = "";
	String SimNumber = "";
	final static int TwoButtons = 2;
	long diff;	
	BigDecimal cLimit,cMade;	
	CashCounterObject CC ;	
	BluetoothPrinting bptr;

	//Nitish 24-07-2014
	String rcpttype;

	//Nitish End
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection_cheque);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		dh = new Handler();
		autoselectBank = false;

		/*TelephonyManager mgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		IMEINumber = mgr.getDeviceId();
		SimNumber = mgr.getSimSerialNumber();*/
		
		try
		{
			//Get Mobile Details ==============================================================
			//10-03-2021
			IMEINumber = CommonFunction.getDeviceNo(CollectionChequeActivity.this);
			SimNumber = CommonFunction.getSIMNo(CollectionChequeActivity.this);

		}
		catch(Exception e)
		{
							
		}


		conid =  (TextView)findViewById(R.id.txtCollectionChequeConnectionId);
		rrNo =  (TextView)findViewById(R.id.txtCollectionChequeRRNo);		
		arrears = (TextView)findViewById(R.id.txtCollectionChequeArrears);
		billamt = (TextView)findViewById(R.id.txtCollectionChequeBillAmount);
		payamt =  (EditText)findViewById(R.id.txtCollectionChequePaymentAmount);
		chqddno = (EditText)findViewById(R.id.txtCollectionChequeNo);
		autoBank  = (AutoCompleteTextView)findViewById(R.id.autoCollectionChequeBankSearch);
		chqdddate = (EditText)findViewById(R.id.txtCollectionChequeDate);		
		btnconfirm = (Button)findViewById(R.id.btnCollectionChequeConfirm);	
		lblRevenueMisc = (TextView)findViewById(R.id.lblRevenueMisc);	
		lblArrears = (TextView)findViewById(R.id.lblCollectionChequeArrears);	
		imgClearBank = (ImageView)findViewById(R.id.imgClearBank);
		imgClearBank.setVisibility(View.INVISIBLE);

		//Nitish 24-07-2014
		if(CollectionStartActivity.isRevenueRcpt)
		{
			lblRevenueMisc.setText(ConstantClass.mRevenueRcpt.toString());
			rcpttype = ConstantClass.mRevenueType;  //rcpttype= R
		}
		else
		{
			lblRevenueMisc.setText(ConstantClass.mMISCRcpt.toString());
			rcpttype = ConstantClass.mMISCType;  //rcpttype= M
			lblArrears.setText(ConstantClass.mBalance.toString());
		}

		
		CC = mDb.GetCashCounterDetails();	
		//Modified 17-06-2017
		cLimit = new BigDecimal(CC.getmCDLimit());
		cMade = new BigDecimal( mDb.GetCashChequeCollectionAmount(CC.getmBatch_No(),"2")); //Get Total Collected amount for that batchno	
		//End Nitish

		try {

			collObj=CollectionObject.GetCollectionObject();						
			conid.setText(collObj.getmConnectionNo());
			rrNo.setText(collObj.getmRRNo());

			BigDecimal txtArrers = collObj.getmArears() == null ? new BigDecimal(0.00) : collObj.getmArears().setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal txtArrersold = collObj.getmArrearsOld().setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal txtIntstold = collObj.getmIntrstOld().setScale(2, BigDecimal.ROUND_HALF_UP);		
			arrears.setText(String.valueOf(txtArrers.add(txtArrersold.add(txtIntstold))));		

			billamt.setText(collObj.getmBillTotal()==null?"":collObj.getmBillTotal().setScale(2,RoundingMode.HALF_EVEN).setScale(0, BigDecimal.ROUND_HALF_UP).toString() + ConstantClass.sPaise);	

			if(CollectionStartActivity.isRevenueRcpt) //For Revenue Receipt
			{
				arrears.setText(String.valueOf(txtArrers.add(txtArrersold.add(txtIntstold))));
			}
			else  //For MISC Receipt Set Balance
			{
				arrears.setText((new CommonFunction()).GetBalanceForMISCReceipt(collObj.getmIODRemarks())); //Balance
			}			
			//Modified 11-06-2014
			if(collObj.getmBOBillFlag()==1)
			{
				String bobAmount[] = collObj.getmBOBilled_Amount().replace('.','#').split("#");						
				billamt.setText(bobAmount[0]+ ConstantClass.sPaise);				
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
			
			AutoDDLAdapter bankList = mDb.GetBankList();				
			autoBank.setThreshold(1);		
			autoBank.setAdapter(bankList);	
			new CustomDatePicker(CollectionChequeActivity.this, R.id.txtCollectionChequeDate,90,0);			 


			autoBank.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> alist, View arg1,
						int pos, long arg3) {
					autoselectBank = true;
					DDLItem k = (DDLItem)  alist.getItemAtPosition(pos);					
					bankId = Integer.valueOf(k.getId());					
					bankName = k.getValue();
					autoBank.setEnabled(false);
					imgClearBank.setVisibility(View.VISIBLE);	
					//To Hide Soft KeyBoard
					InputMethodManager in = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);


				}
			});

			autoBank.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View arg0, boolean focusSelect) {


					if(focusSelect)
					{
						imgClearBank.setVisibility(View.VISIBLE);	
						autoBank.setEnabled(true);		
						return;							
					}

					//If Bank name is not autoselected 
					//or if Bank name string length is 0 
					//or If Bank name is not autoselected but typed
					if( !focusSelect && (autoBank.length() == 0 || !autoselectBank || !autoBank.getText().toString().equals(bankName)))
					{
						CustomToast.makeText(CollectionChequeActivity.this,"Select Bank from AutoComplete List" , Toast.LENGTH_SHORT);
						autoselectBank = false;							
						autoBank.setText("");
						autoBank.setEnabled(true);	
						imgClearBank.setVisibility(View.INVISIBLE);
						return;							
					}	

				}
			});

			imgClearBank.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					autoselectBank = false;					
					autoBank.setText("");
					autoBank.setEnabled(true);	
					imgClearBank.setVisibility(View.INVISIBLE);
				}
			});


			btnconfirm.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					try
					{	
						//25-01-2016
						/*boolean mobileDataEnabled = android.provider.Settings.Secure.getInt(getContentResolver(),"mobile_data",1) == 1;
						if(!mobileDataEnabled)
						{
							CustomToast.makeText(CollectionChequeActivity.this,"Please Enable Data Connection!!"  , Toast.LENGTH_SHORT);
							Intent i = new Intent(CollectionChequeActivity.this,CollectionStartActivity.class);				
							startActivity(i);
							return;
						}*/
						//28-07-2016
						try
						{
							//Modified Nitish 16-03-2017
							if(!CC.getmSIMNo().trim().equals(SimNumber))
							{
								CustomToast.makeText(CollectionChequeActivity.this, "Sim Number Not Matching Contact NSOFT.", Toast.LENGTH_SHORT);
								return;
							}

						}
						catch(Exception e)
						{

						}
						//27-06-2017
						if(Global.getInt(getContentResolver(), Global.AUTO_TIME) == 0)
						{
							CustomToast.makeText(CollectionChequeActivity.this,"Please Enable AutoDateTime Feature." , Toast.LENGTH_SHORT);
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
							if(!(CurMonthYear.equals(batchMonthYear) && (curDay == nextBatchDay))) //If Current Date not equal to next batch date do not allow receipts.
							{
								CustomToast.makeText(CollectionChequeActivity.this,"Current Date exceeds alloted Batch Date " + bDate , Toast.LENGTH_SHORT);
								Intent i = new Intent(CollectionChequeActivity.this,CollectionStartActivity.class);				
								startActivity(i);
								return;
							}						
						}
						//End 09-02-2018

						//27-11-2014
						if(sdf.parse(currentdate).before(sdf.parse(bDate)))  //If Current Date is before then do not allow to generate Collection Receipts.
						{
							CustomToast.makeText(CollectionChequeActivity.this,"Current Date is less than Batch Date " + bDate , Toast.LENGTH_SHORT);
							Intent i = new Intent(CollectionChequeActivity.this,CollectionStartActivity.class);				
							startActivity(i);	
							return;
						}

						if(chqddno.length() == 0)
						{
							CustomToast.makeText(CollectionChequeActivity.this,"Enter Cheque/DD Number." , Toast.LENGTH_SHORT);
							chqddno.setText("");
							return;					
						}					
						else if(Integer.parseInt(chqddno.getText().toString()) == 0 )
						{
							CustomToast.makeText(CollectionChequeActivity.this,"Invalid Cheque/DD Number." , Toast.LENGTH_SHORT);
							chqddno.setText("");
							return;
						}	
						else if(chqddno.length() < 6)
						{
							CustomToast.makeText(CollectionChequeActivity.this,"Cheque/DD Number should be 6 digits." , Toast.LENGTH_SHORT);
							chqddno.setText("");
							return;
						}
						//If Bank name is not autoselected 
						//or if Bank name string length is 0 
						//or If Bank name is not autoselected but typed 
						else if(autoBank.length() == 0 || !autoselectBank || !autoBank.getText().toString().equals(bankName) )
						{
							CustomToast.makeText(CollectionChequeActivity.this,"Select Bank from AutoComplete List." , Toast.LENGTH_SHORT);
							autoBank.setText("");

							return;
						}					

						else if(payamt.length() == 0)
						{
							CustomToast.makeText(CollectionChequeActivity.this,"Enter Payment Amount." , Toast.LENGTH_SHORT);
							payamt.setText("");
							return;
						}	
						else if(Integer.parseInt(payamt.getText().toString()) == 0 )
						{
							CustomToast.makeText(CollectionChequeActivity.this,"Payment Amount cannot be Zero." , Toast.LENGTH_SHORT);
							payamt.setText("");
							return;
						}					

						aramt = new BigDecimal(arrears.getText().toString());
						pamt = new BigDecimal(payamt.getText().toString());

						//Nitish 22-04-2014					
						BigDecimal addpamtcMade = pamt.add(cMade);

						if(addpamtcMade.compareTo(cLimit) > 0 )
						{
							CustomToast.makeText(CollectionChequeActivity.this,"Cheque/DD Limit Exceeded." , Toast.LENGTH_SHORT);
							return;
						}
						//22-06-2017
						if(CC.getmPartPayFlag().equals("1"))
						{
							BigDecimal parpaymentperc = new BigDecimal(CC.getmPartPayPerc().toString().trim());
							parpaymentperc = parpaymentperc.multiply(new BigDecimal(0.01));
							if((aramt.multiply(parpaymentperc)).compareTo(pamt) > 0)
							{
								CustomToast.makeText(CollectionChequeActivity.this,"Payment Amount must be atleast " + CC.getmPartPayPerc().trim() +" Percent of Arrears Amount." , Toast.LENGTH_LONG);
								//CustomToast.makeText(CollectionCashActivity.this,"Payment Amount cannot be less than Arrears/Balance Amount." , Toast.LENGTH_LONG);
								payamt.setText("");
								return;
							}
							
						}
						else 
						{
							if(aramt.compareTo(pamt) > 0 )
							{
								CustomToast.makeText(CollectionChequeActivity.this,"Payment Amount cannot be less than Arrear Amount." , Toast.LENGTH_LONG);
								payamt.setText("");
								return;
							}
						}
						//End 22-06-2017
						
						//End Nitish 22-04-2014

						//22-01-2015
/*						if(CollectionStartActivity.isRevenueRcpt)
						{
							if((aramt.multiply(new BigDecimal(0.25))).compareTo(pamt) > 0)
							{
								CustomToast.makeText(CollectionChequeActivity.this,"Payment Amount must be atleast 25 Percent of Arrears Amount." , Toast.LENGTH_LONG);
								//CustomToast.makeText(CollectionCashActivity.this,"Payment Amount cannot be less than Arrears/Balance Amount." , Toast.LENGTH_LONG);
								payamt.setText("");
								return;
							}	
							//23-06-2015
							if(aramt.compareTo(pamt) > 0 && !(CollectionObject.GetCollectionObject().getmTariffCode()==1))
							{							
								CustomToast.makeText(CollectionChequeActivity.this,"Payment Amount cannot be less than Arrears/Balance Amount." , Toast.LENGTH_LONG);
								payamt.setText("");
								return;
							}
							//19-11-2015
							if((CollectionObject.GetCollectionObject().getmTariffCode()>=20 && CollectionObject.GetCollectionObject().getmTariffCode()<=27) ) 
							{
								//Part Payment for Arrears must be atleast 1/6 of arrear amount
								if((aramt.divide(new BigDecimal(6), MathContext.DECIMAL128).setScale(0, RoundingMode.HALF_UP)).compareTo(pamt) > 0)
								{
									CustomToast.makeText(CollectionChequeActivity.this,"Payment Amount must be atleast 1/6th of Arrear Amount." , Toast.LENGTH_LONG);
									return;
								}

							}
							else
							{

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
											CustomToast.makeText(CollectionChequeActivity.this,"Payment Amount must be atleast 25 Percent of Arrears Amount." , Toast.LENGTH_LONG);
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
											CustomToast.makeText(CollectionChequeActivity.this,"Payment Amount must be atleast " + CC.getmPartPayPerc().trim() +" Percent of Arrears Amount." , Toast.LENGTH_LONG);
											//CustomToast.makeText(CollectionCashActivity.this,"Payment Amount cannot be less than Arrears/Balance Amount." , Toast.LENGTH_LONG);
											payamt.setText("");
											return;
										}
										
									}
									else 
									{
										if(aramt.compareTo(pamt) > 0 )
										{
											CustomToast.makeText(CollectionChequeActivity.this,"Payment Amount cannot be less than Arrear Amount." , Toast.LENGTH_LONG);
											payamt.setText("");
											return;
										}
									}

								}
								else if(aramt.compareTo(blamt)>0) //If Arrears is greater than Bill Amt then payment amt must be greater than billamt
								{

									if(blamt.compareTo(pamt) > 0)
									{	

										CustomToast.makeText(CollectionChequeActivity.this,"Payment Amount cannot be less than Bill Amount." , Toast.LENGTH_LONG);
										payamt.setText("");
										return;
									}
								}

							}//End 18-07-2015

						}*/
						//else
						//{
							/*if(aramt.compareTo(pamt) > 0)
							{
								//CustomToast.makeText(CollectionCashActivity.this,"Payment Amount must be atleast 25 Percent of Arrears/Balance Amount." , Toast.LENGTH_LONG);
								CustomToast.makeText(CollectionChequeActivity.this,"Payment Amount cannot be less than Balance Amount." , Toast.LENGTH_LONG);
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
		catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d(TAG, e.toString());
			e.printStackTrace();
		}		
	}
	public void SaveFunction()
	{

		BigDecimal txtArrers = collObj.getmArears() == null ? new BigDecimal(0.00) : collObj.getmArears().setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal txtArrersold = collObj.getmArrearsOld().setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal txtIntstold = collObj.getmIntrstOld().setScale(2, BigDecimal.ROUND_HALF_UP);						

		StringBuilder sb = new StringBuilder();
		sb.append("Chq/DDNO     : "+ chqddno.getText().toString() + ConstantClass.eol);	
		sb.append("Chq/DD Date : "+ chqdddate.getText().toString() + ConstantClass.eol);
		sb.append("Bank               : "+ autoBank.getText().toString() + ConstantClass.eol);
		//sb.append("Arrears			    : "+ String.valueOf(txtArrers.add(txtArrersold.add(txtIntstold))) + ConstantClass.eol);
		//Modified 11-06-2014
		/*if(collObj.getmBOBillFlag()==1)
		{
			String bobAmount[] = collObj.getmBOBilled_Amount().replace('.','#').split("#");						
			sb.append("Bill Amt          : "+bobAmount[0]+ ConstantClass.sPaise + ConstantClass.eol);			
		}
		else
		{
			sb.append("Bill Amt          : "+ collObj.getmBillTotal().setScale(2,RoundingMode.HALF_EVEN).setScale(0, BigDecimal.ROUND_HALF_UP).toString() + ConstantClass.sPaise + ConstantClass.eol);
		}*/
		sb.append("Payment Amt: "+ pamt.setScale(0,RoundingMode.HALF_UP).toString() + ConstantClass.sPaise + ConstantClass.eol);	


		CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
		params.setContext(CollectionChequeActivity.this);					
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
		final ProgressDialog ringProgress = ProgressDialog.show(CollectionChequeActivity.this, "Please wait..", "Time Sync...",true);
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
				CustomToast.makeText(CollectionChequeActivity.this, "Error in Time Synchronisation", Toast.LENGTH_SHORT);				
				return;
			}
			else if(rValue.indexOf("ACK")!=-1)//ACK received 
			{	
				//Assume rValue  = ACK150520141505$
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
						CustomToast.makeText(CollectionChequeActivity.this,"Mobile Date Time not matching Server date time."  , Toast.LENGTH_SHORT);
						return;
					}						
					else // If Time Difference within 10 Min
					{
						//TimeChangedReceiver.isValid = true;
						TimeChangedReceiver.setValid(true);
						CollectionObject.GetCollectionObject().setmIsTimeSync(true); 						
						//CustomToast.makeText(CollectionChequeActivity.this, "Time synchronisation Successful.", Toast.LENGTH_SHORT);
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
				CustomToast.makeText(CollectionChequeActivity.this, "Error Occured", Toast.LENGTH_SHORT);
			}		
		}	
		//If Exception Occured 
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false);
			ringProgress.dismiss();
			CustomToast.makeText(CollectionChequeActivity.this, "Time Synchronisation Failed.", Toast.LENGTH_SHORT);			
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
		return OptionsMenu.navigate(item,CollectionChequeActivity.this);			
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		CustomToast.makeText(CollectionChequeActivity.this, "Please use menu to navigate.", Toast.LENGTH_SHORT);
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
					CustomToast.makeText(CollectionChequeActivity.this,"Please Enable AutoDateTime Feature." , Toast.LENGTH_SHORT);
					return;
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//End 22-01-2018
			payamt =  (EditText)findViewById(R.id.txtCollectionChequePaymentAmount);	
			chqddno = (EditText)findViewById(R.id.txtCollectionChequeNo);
			chqdddate = (EditText)findViewById(R.id.txtCollectionChequeDate);

			collObj.setmPaid_Amt(Integer.valueOf(payamt.getText().toString()));	
			collObj.setmPayment_Mode("2");  //2 -> Cheque Payment
			collObj.setmSBMNumber(IMEINumber); //Set SBMNumber  as  IMEINo

			collObj.setmReceipttypeflag(rcpttype); //24-07-2014

			collObj.setmBankID(bankId);
			collObj.setmChequeDDNo(Integer.valueOf(chqddno.getText().toString()));				
			collObj.setmChequeDDDate((new CommonFunction()).DateConvertRemoveChar(chqdddate.getText().toString()));

			collObj.setmRemarkId(0);
			collObj.setmScheduledDate("0");


			//Modified Date 11-06-2014
			if(collObj.getmBlCnt().equals("0") && collObj.getmBOBillFlag()==0)  //If Not Billed then Payment is for Arrears. Set ArrearsBill_Flag=1
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
					/*String imgname = ImageSaveinSDCard();
					CollectionObject.GetCollectionObject().setmImageName(imgname);
					result = mDb.CollectionSave();*/					
					//07-02-2018
					try
					{					
						String imgname = ImageSaveinSDCard();
						CollectionObject.GetCollectionObject().setmImageName(imgname);
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
					//07-02-2018
					
					dh.post(new Runnable() 
					{
						@Override
						public void run() 
						{							
							if(result == 1)
							{
								CustomToast.makeText(CollectionChequeActivity.this, "Payment completed Successfully.", Toast.LENGTH_SHORT);

								//Bluetooth Code===========================================				
								bptr = new BluetoothPrinting(CollectionChequeActivity.this);

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
									CustomToast.makeText(CollectionChequeActivity.this, "Problem in Printing.", Toast.LENGTH_SHORT);
									Intent i = new Intent(CollectionChequeActivity.this, CollectionActivity.class);
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
								CustomToast.makeText(CollectionChequeActivity.this, "Problem in saving data.", Toast.LENGTH_SHORT);
							}
						}
					});
				}
			}).start();
			ringProgress = ProgressDialog.show(CollectionChequeActivity.this, "Please wait..", "Data Saving and Printing..",true);//Spinner
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
					int batchlen = mDb.GetCashCounterDetails().getmBatch_No().trim().length();
					String pBatchNo = pca.CreateBlankSpaceSequence(12, "*")+ mDb.GetCashCounterDetails().getmBatch_No().trim().substring(batchlen-6, batchlen); 
					String pCustName = collObj.getmCustomerName();				
					String pReceiptNo ="";
					try
					{
						String currentYear = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
						pReceiptNo = CommonFunction.getActualReceiptNo(String.valueOf(collObj.getmReceipt_No()));
						pReceiptNo = currentYear+collObj.getmGvpId().toString().trim().substring(0,5) + pReceiptNo;							
						
					}catch(Exception e)
					{

					}
					
					
					String pConnectionNo=collObj.getmConnectionNo();
					String pRRNO =collObj.getmRRNo();
					String pReceiptType = CollectionStartActivity.isRevenueRcpt ? ConstantClass.mRevenueRcpt : (ConstantClass.mMISCRcpt  + ConstantClass.mMiscASD);

					
					
					String psemicolon = ":";
					
					//String pArrearsBillAmt = " ";
					String pPaidAmt = String.valueOf(collObj.getmPaid_Amt())+ ConstantClass.sPaise;
					String pRptDate = cFun.DateTimeConvertAddChar(collObj.getmDateTime().trim(),"/",":");
					String pMRName = collObj.getmReaderCode().trim();

					/*//24-07-2014
					if(CollectionStartActivity.isRevenueRcpt) //For Revenue Rpt
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
					
					String pPaymentMode = "CHEQUE/DD";
					String pChequeDDNo =String.valueOf(collObj.getmChequeDDNo());
					String pChequeDDDate =  cFun.DateConvertAddCharNew(collObj.getmChequeDDDate().trim(),"/");
					String pBank =  mDb.GetBankName(collObj.getmBankID());//02-03-2021
					//String timeStamp = cFun.GetCurrentTime();
					bptr.sendData(ConstantClass.data1);
					AmigosESCCmd pos = new AmigosESCCmd();
					//byte[] amigosFont = pos.POS_S_TextOut("", "UTF-8", 0, 1, 1, 0, 0x00);	
					for(int i=1; i< 3 ; i++)
					{
						

						Typeface typeface1;
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
						cPrint = cPrint +   "\n"+ pca.CreateBlankSpaceSequence(35, " ")+ "HESCOM CASH RECEIPT (MCC)"+"\n";
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
						cPrint += "\n  ಪಾವತಿಸಿದ ವಿಧಾನ /Pmt Mode     :"+"CHEQUE/DD" + "\n";		
						cPrint += "\n  ಚೆಕ್/ಡಿ.ಡಿ ಸಂಖ್ಯೆ /CHQDD NO       :"+pChequeDDNo.trim() + "\n";	
						cPrint += "\n  ಚೆಕ್/ಡಿ.ಡಿ ದಿನಾಂಕ /CHQDD Date :"+pChequeDDDate.trim() + "\n";
						//02-03-2021
						if(pBank.trim().length() > 15)
							cPrint += "\n  ಬ್ಯಾಂಕ್ /Bank                                   :"+pBank.trim().substring(0, 15) + "\n";					
						else
							cPrint += "\n  ಬ್ಯಾಂಕ್ /Bank                                   :"+pBank.trim() + "\n";
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
					mDb.EventLogSave("8", LoginActivity.IMEINumber, LoginActivity.SimNumber, "Cheque Receipt Printing Completed For Connection:  " + collObj.getmConnectionNo().trim());//15-07-2016
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

			}  catch (Exception e)
			{
				// TODO Auto-generated catch block
				Log.d(TAG,e.toString());
				//if printer turn off without of server, then after printer turn on process again start
				dh.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						CustomToast.makeText(CollectionChequeActivity.this, "Problem in Printing.", Toast.LENGTH_SHORT);
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
							Intent i = new Intent(CollectionChequeActivity.this, CollectionActivity.class);
							startActivity(i);
							CollectionChequeActivity.this.finish(); //21-02-2018
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
	//Save Camera Image for Current Connection
	public String ImageSaveinSDCard()
	{
		InputStream myInput = null;
		OutputStream myOutput = null;
		String ImageName = "";
		try
		{
			CommonFunction cFun = new CommonFunction();
			String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
			ImageName = CollectionObject.GetCollectionObject().getmConnectionNo().trim() +"_"+ cFun.GetCurrentTime().replace(" ", "_").replace("-", "").replace(":", "")+"_CHEQUEDD.jpg";
			String root = Environment.getExternalStorageDirectory().getPath();//get the sd Card Path

			myInput = new FileInputStream(ConstantClass.MtrImageSavePath + "/MtrImg.jpg");
			File f = new File(root+"/HescomSpotBilling/"+timeStamp+"/Photos", ImageName);
			if(!new File(root+"/HescomSpotBilling/"+timeStamp+"/Photos").exists())
			{
				new File(root+"/HescomSpotBilling/"+timeStamp+"/Photos").mkdirs();
			}
			myOutput = new FileOutputStream(f);
			byte[] buffer = new byte[1024];
			int length;
			while((length = myInput.read(buffer))>0)
			{
				myOutput.write(buffer, 0, length);
			}
		}
		catch (Exception e)
		{
			Log.d("pushBtn", e.toString());
			ImageName = "Exception";//Added on 09-06-2014
		}
		finally
		{
			if(myInput != null)
			{
				try 
				{
					myInput.close();
				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(myOutput != null)
			{
				try 
				{
					myOutput.close();
				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return ImageName;
	}
}



