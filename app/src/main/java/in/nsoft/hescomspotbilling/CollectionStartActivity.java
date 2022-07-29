//Created Nitish 05-04-2014
package in.nsoft.hescomspotbilling;


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

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Global;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CollectionStartActivity extends Activity implements AlertInterface {	

	private Button btnCloseCashCounter,btnRevenueReceipt,btnMiscReceipt,btnExtendLimit,btnExtendSend,btnExtendLimitVerify;
	TextView txtCashCounter,txtCCBatchNo,txtStartTime,txtEndTime,txtCashLimit,txtBatchDate,txtCollectionMade,txtCCDLimit;
	BigDecimal cLimit,cMade;	
	CashCounterObject CC ;
	EditText txtEnterCashLimit,txtEnterChequeLimit,txtEnterTimeLimit;
	String mSecondSession="";

	private final DatabaseHelper mDb =new DatabaseHelper(this);

	//Nitish 19-04-2014
	Handler dh;
	CustomAlert cAlert; 
	boolean alertResult;		
	final static String alerttitle1 = "Close Cash Counter";
	final static String alertmsg1= "Are you Sure you want to Close Cash Counter";
	final static int Button1 = 1;

	final static String alerttitle2 = "Session Extension";
	final static String alertmsg2= "Are you Sure you want to Request/Verify Session Extension ?";
	final static int Button2 = 2;


	final static String btnAlertOk = "Yes";
	final static String btnAlertCancel = "No";		

	long diff;

	String colAmt,bDate,sTime,eTime;
	static boolean isRevenueRcpt=true;
	String sFlag = "0";
	//Nitish End

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection_start);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		btnRevenueReceipt = (Button)findViewById(R.id.btnRevenueReceipt);	
		btnMiscReceipt = (Button)findViewById(R.id.btnMiscReceipt);	
		btnCloseCashCounter = (Button)findViewById(R.id.btnCloseCashCounter);

		txtCashCounter = (TextView)findViewById(R.id.txtCashCounter);
		txtCCBatchNo =  (TextView)findViewById(R.id.txtCCBatchNo);
		txtBatchDate =  (TextView)findViewById(R.id.txtBatchDate);
		txtStartTime =  (TextView)findViewById(R.id.txtStartTime);
		txtEndTime =  (TextView)findViewById(R.id.txtEndTime);
		txtCashLimit =  (TextView)findViewById(R.id.txtCashLimit);
		txtCCDLimit = (TextView)findViewById(R.id.txtCCDLimit);

		txtCollectionMade =  (TextView)findViewById(R.id.txtCollectionMade);

		btnExtendLimit = (Button)findViewById(R.id.btnExtendLimit);		
		btnExtendLimitVerify = (Button)findViewById(R.id.btnExtendLimitVerify);		//21-07-2017
		txtEnterCashLimit = (EditText)findViewById(R.id.txtEnterCashLimit); //10-07-2017
		txtEnterChequeLimit =  (EditText)findViewById(R.id.txtEnterChequeLimit);//18-07-2017
		txtEnterTimeLimit = (EditText)findViewById(R.id.txtEnterTimeLimit); //10-07-2017
		btnExtendSend = (Button)findViewById(R.id.btnExtendSend);		

		dh = new Handler();	
		CC = mDb.GetCashCounterDetails();	

		colAmt = mDb.GetCollectionAmount(CC.getmBatch_No()); //Get Total Collected amount for that batchno		
		bDate = (new CommonFunction()).DateConvertAddChar(CC.getmBatch_Date(), "-"); //Obtain BatchDate
		sTime = (new CommonFunction()).TimeConvertAddChar(CC.getmStartTime(), ":"); //Obtain Cash Counter Start Time
		eTime = (new CommonFunction()).TimeConvertAddChar(CC.getmEndTime(), ":"); //Obtain Cash Counter End Time	

		cLimit = new BigDecimal(CC.getmCashLimit());
		cMade = new BigDecimal(colAmt);			
		txtCCBatchNo.setText(CC.getmBatch_No());
		txtBatchDate.setText(bDate);
		txtStartTime.setText(sTime);
		txtEndTime.setText(eTime);
		txtCashLimit.setText(CC.getmCashLimit() + ConstantClass.sPaise );
		txtCollectionMade.setText(colAmt + ConstantClass.sPaise );
		txtCCDLimit.setText("CHQ/DD Limit:"+CC.getmCDLimit() +ConstantClass.sPaise );
		txtEnterCashLimit.setVisibility(View.INVISIBLE); //10-07-2017
		txtEnterChequeLimit.setVisibility(View.INVISIBLE);
		txtEnterTimeLimit.setVisibility(View.INVISIBLE);//10-07-2017
		btnExtendSend.setVisibility(View.INVISIBLE); //24-03-2015		
		//Check cashcounter is Closed==============================================================
		try
		{
			//get status where statusid = 14 from StatusMaster to check if cashcounter is Closed			
			if(mDb.GetStatusMasterDetailsByID("14") == 1)//if equal to 1, CashCounter Closed 
			{
				btnCloseCashCounter.setVisibility(View.INVISIBLE);
				btnRevenueReceipt.setVisibility(View.INVISIBLE);
				btnMiscReceipt.setVisibility(View.INVISIBLE);
				btnExtendLimit.setVisibility(View.INVISIBLE); //24-03-2015			
				btnExtendLimitVerify.setVisibility(View.INVISIBLE); //24-03-2015			
				txtEnterCashLimit.setVisibility(View.INVISIBLE); //10-07-2017
				txtEnterChequeLimit.setVisibility(View.INVISIBLE); //10-07-2017
				txtEnterTimeLimit.setVisibility(View.INVISIBLE);//10-07-2017
				btnExtendSend.setVisibility(View.INVISIBLE); //24-03-2015
				txtCashCounter.setText(ConstantClass.mclose);					
				CustomToast.makeText(CollectionStartActivity.this, "Cash Counter Closed.Please Try Tomorrow", Toast.LENGTH_SHORT);
			}
			/*if(CC.getmExtensionFlag().equals("1")) //Allow Only One Extension
			{
				btnExtendLimit.setVisibility(View.INVISIBLE); //24-03-2015
				btnExtendLimitVerify.setVisibility(View.INVISIBLE); //24-03-2015
			}*/
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block			
			e.printStackTrace();
		}
		//End cashcounter Check==============================================================

		btnRevenueReceipt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try 
				{
					//17-06-2017
					if(CC.getmRevFlag().equals("0")) //Check Revenue Authorization
					{
						CustomToast.makeText(CollectionStartActivity.this,"Revenue Receipts cannot be drawn."  , Toast.LENGTH_SHORT);
						return;
					}
					else
					{
						isRevenueRcpt = true;
						cashCounterValidations();		
					}

				} 
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnMiscReceipt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try 
				{
					//17-06-2017
					if(CC.getmMiscFlag().equals("0")) //Check Revenue Authorization
					{
						CustomToast.makeText(CollectionStartActivity.this,"ASD Receipts cannot be drawn."  , Toast.LENGTH_SHORT);
						return;
					}
					else
					{
						isRevenueRcpt = false;
						cashCounterValidations();	
					}
				} 
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});	
		btnCloseCashCounter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				//if Status of GPRS Sending(ID=13) equal to 1, GPRS sending pending for Collection.CashCounter cannot be Closed 
				//if Collection count > GPRS Sent Count for collection, GPRS sending pending for Collection.CashCounter cannot be Closed 

				String value = mDb.GPRSFlagColl();
				String[] splitvalue = value.split("/");
				if(!splitvalue[0].equals(splitvalue[1])) //If GPRS not sent
				{
					mDb.UpdateStatusMasterDetailsByID("13", "1", value);
				}
				else if(splitvalue[0].equals(splitvalue[1])) //If all Collection GPRS sent
				{
					mDb.UpdateStatusMasterDetailsByID("13", "0", value);
				}

				if(mDb.GetStatusMasterDetailsByID("13") == 1 && (mDb.GetCountforColl() > mDb.GetCountGPRSSentConnectionColl()))				
				{					
					CustomToast.makeText(CollectionStartActivity.this, "!Warning: Receipts data are not sent through GPRS.", Toast.LENGTH_SHORT);
					return;
				}
				CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
				params.setContext(CollectionStartActivity.this);					
				params.setMainHandler(dh);
				params.setMsg(alertmsg1);
				params.setButtonOkText(btnAlertCancel);
				params.setButtonCancelText(btnAlertOk);				
				params.setTitle(alerttitle1);
				params.setFunctionality(Button1);
				cAlert  = new CustomAlert(params);					
			}
		});		

		btnExtendLimit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {	
				try
				{
					//21-02-2018
					if(Global.getInt(getContentResolver(), Global.AUTO_TIME) == 0)
					{
						CustomToast.makeText(CollectionStartActivity.this,"Please Enable AutoDateTime Feature." , Toast.LENGTH_SHORT);
						return;
					}

					//Modified 03-02-2018
					String CurDate = new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime());
					String CurMonthYear = new SimpleDateFormat("MMyyyy").format(Calendar.getInstance().getTime()); //New
					String batchMonthYear = mDb.GetCashCounterDetails().getmBatch_Date().substring(2, 8); //New
					int curDay = Integer.valueOf(CurDate.substring(0, 2)); //New
					int nextBatchDay = Integer.valueOf(mDb.GetCashCounterDetails().getmBatch_Date().substring(0, 2)) + 1; //New
					//Compare current date with batch date.If CurDate=BatchDate do not allow batch generation 
					if(CurDate.equals(mDb.GetCashCounterDetails().getmBatch_Date()) ||(CurMonthYear.equals(batchMonthYear) && (curDay == nextBatchDay)) )  
					{
						sFlag = "7";
						if(mDb.GetCashCounterDetails().getmChqFlag().equals("0")) //CHQDD Authorization
						{
							txtEnterChequeLimit.setEnabled(false);
						}
						else
						{
							txtEnterChequeLimit.setEnabled(true);
						}

						CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
						params.setContext(CollectionStartActivity.this);					
						params.setMainHandler(dh);
						params.setMsg(alertmsg2);
						params.setButtonOkText(btnAlertCancel);
						params.setButtonCancelText(btnAlertOk);				
						params.setTitle(alerttitle2);
						params.setFunctionality(Button2);
						cAlert  = new CustomAlert(params);	
					}
					else
					{
						CustomToast.makeText(CollectionStartActivity.this,"Batch Expired."  , Toast.LENGTH_LONG);
						return;
					}
					//End 03-02-2018
				}
				catch(Exception e)
				{

				}
			}

		});

		btnExtendSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {	
				try
				{
					String currentdate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");	
					//21-02-2018
					if(Global.getInt(getContentResolver(), Global.AUTO_TIME) == 0)
					{
						CustomToast.makeText(CollectionStartActivity.this,"Please Enable AutoDateTime Feature." , Toast.LENGTH_SHORT);
						return;
					}

					//Commented 03-02-
					/*if(sdf.parse(currentdate).after(sdf.parse(bDate)))  //If Current Date is greater then do not allow to generate Collection Receipts.
					{
						CustomToast.makeText(CollectionStartActivity.this,"Current Date exceeds alloted Batch Date " + bDate , Toast.LENGTH_LONG);
						return;
					}	
					else*/ 
					if(sdf.parse(currentdate).before(sdf.parse(bDate)))  //If Current Date is less then do not allow to generate Collection Receipts.
					{
						CustomToast.makeText(CollectionStartActivity.this,"Current Date is less than Batch Date " + bDate , Toast.LENGTH_LONG);
						return;
					}					
					else if(txtEnterCashLimit.length() == 0 && txtEnterChequeLimit.length() ==0 && txtEnterTimeLimit.length()==0)
					{
						CustomToast.makeText(CollectionStartActivity.this,"Enter Limit for Extension." , Toast.LENGTH_LONG);
						return;
					}					
					else if(txtEnterTimeLimit.length()!=0 && eTime.compareTo((txtEnterTimeLimit.getText().toString())) > 0) //End Time Exceeded
					{
						CustomToast.makeText(CollectionStartActivity.this,"Invalid Extension Time."  , Toast.LENGTH_LONG);
						return;
					}					

					/*if(txtEnterTimeLimit.getText().toString().length() != 0)
					{
						String enteredtime = txtEnterTimeLimit.getText().toString();
						int coloncount = 0;
						for(int i=0;i<enteredtime.length();i++)
						{
							if(enteredtime.charAt(i) == ':')
								coloncount++;
						}
						if(coloncount!=1)
						{
							CustomToast.makeText(CollectionStartActivity.this,"Invalid Extension Time."  , Toast.LENGTH_SHORT);
							return;
						}
					}*/	

					String extCash =txtEnterCashLimit.getText().toString().length()==0?"0" :String.valueOf(txtEnterCashLimit.getText().toString());
					String extCheque =txtEnterChequeLimit.getText().toString().length()==0?"0" :String.valueOf(txtEnterChequeLimit.getText().toString());
					String exttime =txtEnterTimeLimit.getText().toString().length()==0?"0" :String.valueOf(txtEnterTimeLimit.getText().toString());

					//Modified 03-02-2018
					String CurDate = new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime());
					String CurMonthYear = new SimpleDateFormat("MMyyyy").format(Calendar.getInstance().getTime()); //New
					String batchMonthYear = mDb.GetCashCounterDetails().getmBatch_Date().substring(2, 8); //New
					int curDay = Integer.valueOf(CurDate.substring(0, 2)); //New
					int nextBatchDay = Integer.valueOf(mDb.GetCashCounterDetails().getmBatch_Date().substring(0, 2)) + 1; //New
					//Compare current date with batch date.If CurDate=BatchDate do not allow batch generation 
					if(CurDate.equals(mDb.GetCashCounterDetails().getmBatch_Date()) ||(CurMonthYear.equals(batchMonthYear) && (curDay == nextBatchDay)) )  
					{
						mSecondSession = extCash + "#" +extCheque + "#" + exttime;
						sFlag = "8";
						new ExtendCashLimit().execute();	
					}
					//End 03-02-2018
				}
				catch(Exception e)
				{

				}
			}
		});

		btnExtendLimitVerify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try
				{
					//21-02-2018
					if(Global.getInt(getContentResolver(), Global.AUTO_TIME) == 0)
					{
						CustomToast.makeText(CollectionStartActivity.this,"Please Enable AutoDateTime Feature." , Toast.LENGTH_SHORT);
						return;
					}

					//Modified 03-02-2018
					String CurDate = new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime());
					String CurMonthYear = new SimpleDateFormat("MMyyyy").format(Calendar.getInstance().getTime()); //New
					String batchMonthYear = mDb.GetCashCounterDetails().getmBatch_Date().substring(2, 8); //New
					int curDay = Integer.valueOf(CurDate.substring(0, 2)); //New
					int nextBatchDay = Integer.valueOf(mDb.GetCashCounterDetails().getmBatch_Date().substring(0, 2)) + 1; //New
					//Compare current date with batch date.If CurDate=BatchDate do not allow batch generation 
					if(CurDate.equals(mDb.GetCashCounterDetails().getmBatch_Date()) ||(CurMonthYear.equals(batchMonthYear) && (curDay == nextBatchDay)) )  
					{
						sFlag = "9";				
						CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
						params.setContext(CollectionStartActivity.this);					
						params.setMainHandler(dh);
						params.setMsg(alertmsg2);
						params.setButtonOkText(btnAlertCancel);
						params.setButtonCancelText(btnAlertOk);				
						params.setTitle(alerttitle2);
						params.setFunctionality(Button2);
						cAlert  = new CustomAlert(params);	
					}

					else
					{
						CustomToast.makeText(CollectionStartActivity.this,"Batch Expired."  , Toast.LENGTH_LONG);
						return;
					}
					//End 03-02-2018

				}
				catch(Exception e)
				{

				}

			}
		});

		txtEnterTimeLimit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar mcurrentTime = Calendar.getInstance();
				int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
				int minute = mcurrentTime.get(Calendar.MINUTE);
				TimePickerDialog mTimePicker;
				mTimePicker = new TimePickerDialog(CollectionStartActivity.this, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
						txtEnterTimeLimit.setText( selectedHour + ":" + selectedMinute);
					}
				}, hour, minute, true);//Yes 24 hour time
				mTimePicker.setTitle("Select Time");
				mTimePicker.show();
			}
		});
	}
	public void cashCounterValidations()
	{
		String currentdate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");	
		String currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());					
		try 
		{
			if(Global.getInt(getContentResolver(), Global.AUTO_TIME) == 0)
			{
				CustomToast.makeText(CollectionStartActivity.this,"Please Enable AutoDateTime Feature." , Toast.LENGTH_SHORT);
				return;
			}
			/*if(sdf.parse(currentdate).after(sdf.parse(bDate)))  //If Current Date is greater then do not allow to generate Collection Receipts.
			{
				CustomToast.makeText(CollectionStartActivity.this,"Current Date exceeds alloted Batch Date " + bDate , Toast.LENGTH_SHORT);
				return;
			}*/	
			if(sdf.parse(currentdate).before(sdf.parse(bDate)))  //If Current Date is less then do not allow to generate Collection Receipts.
			{
				CustomToast.makeText(CollectionStartActivity.this,"Current Date is less than Batch Date " + bDate , Toast.LENGTH_SHORT);
				return;
			}
			else if(sTime.compareTo(currentTime) > 0) //Start Time Not Reached
			{
				CustomToast.makeText(CollectionStartActivity.this,"Cash Counter Start Time " + sTime + " not Reached." , Toast.LENGTH_SHORT);
				return;
			}
			else if(currentTime.compareTo(eTime) > 0) //End Time Exceeded
			{
				CustomToast.makeText(CollectionStartActivity.this,"Cash Counter End Time " + eTime + " Exceeded."  , Toast.LENGTH_SHORT);
				return;
			}
			/*else if(cMade.compareTo(cLimit) >= 0  ) //Cash Limit Reached
			{
				CustomToast.makeText(CollectionStartActivity.this,"Cash Limit Reached."  , Toast.LENGTH_SHORT);
				return;
			}*/
			else
			{
				//Modified Nitish 03-02-2018
				String CurDate = new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime());
				String CurMonthYear = new SimpleDateFormat("MMyyyy").format(Calendar.getInstance().getTime()); //New
				String batchMonthYear = mDb.GetCashCounterDetails().getmBatch_Date().substring(2, 8); //New
				int curDay = Integer.valueOf(CurDate.substring(0, 2)); //New
				int nextBatchDay = Integer.valueOf(mDb.GetCashCounterDetails().getmBatch_Date().substring(0, 2)) + 1; //New
				//Compare current date with batch date.If CurDate=BatchDate do not allow batch generation 
				if(CurDate.equals(mDb.GetCashCounterDetails().getmBatch_Date()))  
				{
					Intent i = new Intent(CollectionStartActivity.this, CollectionActivity.class);
					startActivity(i);
				}
				else if(CurMonthYear.equals(batchMonthYear) && (curDay == nextBatchDay)) //New
				{
					Intent i = new Intent(CollectionStartActivity.this, CollectionActivity.class);
					startActivity(i);
				}
				else
				{
					CustomToast.makeText(CollectionStartActivity.this,"Batch Date has expired." , Toast.LENGTH_LONG);
					return;
				}
				//End Nitish 03-02-2018
				//Intent i = new Intent(CollectionStartActivity.this, CollectionActivity.class);
				//startActivity(i);
			}
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}	
	private class CashCounterClose extends AsyncTask<Void, Void, Void>  
	{
		final ProgressDialog ringProgress = ProgressDialog.show(CollectionStartActivity.this, "Please wait..", "Closing Cash Counter Manually...",true);
		String rValue = "";
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try
			{
				CommonFunction cf  = new CommonFunction();
				String curdate = cf.DateConvertFullYear(cf.GetCurrentTime());

				/*//Get Mobile Details ==============================================================
				TelephonyManager mgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
				String IMEINumber = mgr.getDeviceId();
				String SimNumber = mgr.getSimSerialNumber(); 
				//END Get Mobile Details ==========================================================
				 */
				String IMEINumber ="";
				String SimNumber = "";
				try
				{
					//Get Mobile Details ==============================================================
					//10-03-2021
					IMEINumber = CommonFunction.getDeviceNo(CollectionStartActivity.this);
					SimNumber = CommonFunction.getSIMNo(CollectionStartActivity.this);

				}
				catch(Exception e)
				{
									
				}

				
				HttpClient httpclt = new DefaultHttpClient();//object for HttpClient								
				HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Manual_CC_Close");//HttpPost method uri

				List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
				lvp.add(new BasicNameValuePair("SBMNO",IMEINumber));//IMEI No
				lvp.add(new BasicNameValuePair("SIMNO",SimNumber));//SIM Serial No
				lvp.add(new BasicNameValuePair("Date",curdate ));//ddmmyyyy
				//lvp.add(new BasicNameValuePair("Date",curdate ));//ddmmyyyy
				lvp.add(new BasicNameValuePair("CC_flag", "2"));
				lvp.add(new BasicNameValuePair("Batch_No",CC.getmBatch_No().trim()));
				lvp.add(new BasicNameValuePair("Version", ConstantClass.FileVersion));
				httpPost.setEntity((new UrlEncodedFormEntity(lvp)));				
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
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			ringProgress.setCancelable(false);
			ringProgress.dismiss();
			if(rValue.equals("ACK"))//ACK received 
			{
				btnCloseCashCounter.setVisibility(View.INVISIBLE);
				btnRevenueReceipt.setVisibility(View.INVISIBLE);
				btnMiscReceipt.setVisibility(View.INVISIBLE);				
				btnExtendSend.setVisibility(View.INVISIBLE); //24-03-2015
				btnExtendLimit.setVisibility(View.INVISIBLE); //24-03-2015
				btnExtendLimitVerify.setVisibility(View.INVISIBLE); //24-03-2015
				txtCashCounter.setText(ConstantClass.mclose);					
				mDb.UpdateStatusInStatusMaster("11", "0");  //update Cash Counter opened = 0
				mDb.UpdateStatusInStatusMaster("12", "0");  //update Batch No Received = 0
				mDb.UpdateStatusInStatusMaster("14", "1");  //update Cash Counter Closed = 1
				mDb.CloseCashCounterFlag(CC.getmBatch_No().trim());//Close CashCounterDetails
				CustomToast.makeText(CollectionStartActivity.this, "Cash Counter Closed.", Toast.LENGTH_SHORT);
			}
			else//NACK received then update flag 0
			{
				mDb.UpdateStatusInStatusMaster("14", "0");  //Cash Counter Closed = 0
				CustomToast.makeText(CollectionStartActivity.this, "Error in Closing Cash Counter.", Toast.LENGTH_SHORT);
			}
		}
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false);
			ringProgress.dismiss();
			CustomToast.makeText(CollectionStartActivity.this, "Closing Cash Counter Failed.", Toast.LENGTH_SHORT);
		}
	}
	//24-03-2015
	private class ExtendCashLimitRequest extends AsyncTask<Void, Void, Void>  
	{
		final ProgressDialog ringProgress = ProgressDialog.show(CollectionStartActivity.this, "Please wait..", "Cash Counter Extension...",true);
		String rValue = "";
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try
			{
				CommonFunction cf  = new CommonFunction();
				String curdate = cf.DateConvertFullYear(cf.GetCurrentTime());

				String IMEINumber ="";
				String SimNumber = "";
				try
				{
					//Get Mobile Details ==============================================================
					//10-03-2021
					IMEINumber = CommonFunction.getDeviceNo(CollectionStartActivity.this);
					SimNumber = CommonFunction.getSIMNo(CollectionStartActivity.this);

				}
				catch(Exception e)
				{
									
				}

				HttpClient httpclt = new DefaultHttpClient();//object for HttpClient								
				HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Cash_Extension_Android");//HttpPost method uri

				List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
				lvp.add(new BasicNameValuePair("Flag",sFlag));
				lvp.add(new BasicNameValuePair("SBMNo",IMEINumber));//IMEI No				
				lvp.add(new BasicNameValuePair("BatchNo",CC.getmBatch_No().trim()));		
				lvp.add(new BasicNameValuePair("ExtendedCashLimit","0"));	
				lvp.add(new BasicNameValuePair("ExtendedChequeLimit","0"));	
				lvp.add(new BasicNameValuePair("ExtendedTimeLimit","0"));
				httpPost.setEntity((new UrlEncodedFormEntity(lvp)));				
				httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");							
				HttpResponse res = httpclt.execute(httpPost);//execute post method
				HttpResponse k = res;
				HttpEntity ent = k.getEntity();
				if(ent != null)
				{
					rValue = EntityUtils.toString(ent);				
				}
				//rValue = "ACK";
				//rValue="APP$51101051804148628090215465290006$60000$10000$20:00";
			}
			catch (Exception e)
			{
				cancel(true);		
				e.printStackTrace();
			}
			return null;	
		}		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			ringProgress.setCancelable(false);
			ringProgress.dismiss();
			//ACK$BatchNo$CahLimit$ChequeLimit$TimeLimit
			//rValue="APP$61104012407178672740245125950011$0$0$19:00" ;
			try
			{

				if(rValue.indexOf("NACK1")!=-1) //NACK1 received 
				{					
					CustomToast.makeText(CollectionStartActivity.this, "Batch Already Extended.", Toast.LENGTH_SHORT);					
					return;
				}
				else if(rValue.indexOf("NACK")!=-1) //NACK received 
				{	
					if(sFlag=="7")
					{
						CustomToast.makeText(CollectionStartActivity.this, "Session Pending for Approval.", Toast.LENGTH_SHORT);					
						return;
					}
					else
					{
						CustomToast.makeText(CollectionStartActivity.this, "Session Pending for Approval.", Toast.LENGTH_SHORT);					
						return;
					}
				}
				else if(rValue.indexOf("ACK")!=-1)//ACK received 
				{
					CustomToast.makeText(CollectionStartActivity.this, "Enter Details for Cash Limit Extension", Toast.LENGTH_SHORT);
					btnExtendLimit.setVisibility(View.INVISIBLE);
					btnExtendLimitVerify.setVisibility(View.INVISIBLE);
					btnExtendSend.setVisibility(View.VISIBLE);
					txtEnterCashLimit.setVisibility(View.VISIBLE); //10-07-2017
					txtEnterChequeLimit.setVisibility(View.VISIBLE); //10-07-2017
					txtEnterTimeLimit.setVisibility(View.VISIBLE);//10-07-2017				


				}
				else if(rValue.indexOf("APP")!=-1)//APP received 
				{
					String strArr[] = rValue.toString().trim().split("\\$");
					if(strArr[1].trim().equals(CC.getmBatch_No().trim()))
					{
						btnCloseCashCounter.setVisibility(View.INVISIBLE);
						btnRevenueReceipt.setVisibility(View.INVISIBLE);
						btnMiscReceipt.setVisibility(View.INVISIBLE);
						btnExtendLimit.setVisibility(View.INVISIBLE);				
						btnExtendLimitVerify.setVisibility(View.INVISIBLE);		
						btnExtendSend.setVisibility(View.INVISIBLE);
						txtEnterCashLimit.setVisibility(View.INVISIBLE); //10-07-2017
						txtEnterChequeLimit.setVisibility(View.INVISIBLE); //10-07-2017
						txtEnterTimeLimit.setVisibility(View.INVISIBLE);//10-07-2017
						//Extend_CC_Close(batchno,cashlimit,endtime)
						//String sCash = new BigDecimal(strArr[2].trim()).add(augend)
						strArr[2] = strArr[2].trim().equals("0")? CC.getmCashLimit(): String.valueOf((new BigDecimal(strArr[2].trim()).add(new BigDecimal(CC.getmCashLimit()))).setScale(0, RoundingMode.HALF_DOWN));
						strArr[3] = strArr[3].trim().equals("0")? CC.getmCDLimit(): String.valueOf((new BigDecimal(strArr[3].trim()).add(new BigDecimal(CC.getmCDLimit()))).setScale(0, RoundingMode.HALF_DOWN));
						strArr[4] = strArr[4].trim().equals("0")? eTime: strArr[4].trim();
						int val = mDb.Extend_CC_Close(strArr[1].trim(),strArr[2].trim(),strArr[3].trim(),strArr[4].trim().replace(":",""));					
						CustomToast.makeText(CollectionStartActivity.this, "Cash Counter Limit Extended.", Toast.LENGTH_SHORT);
						Intent i = new Intent(CollectionStartActivity.this, CollectionStartActivity.class);
						startActivity(i); 
					}
					else
					{
						CustomToast.makeText(CollectionStartActivity.this, "Batch No Mismatch.Contact NSOFT", Toast.LENGTH_SHORT);
					}	
				}
				else//ERR received 
				{				
					CustomToast.makeText(CollectionStartActivity.this, "Cash Counter Extension Failed.Contact NSOFT", Toast.LENGTH_SHORT);
				}
			}
			catch(Exception e)
			{
				CustomToast.makeText(CollectionStartActivity.this, "Cash Counter Extension Failed.Contact NSOFT", Toast.LENGTH_SHORT);
				cancel(true);	
				e.printStackTrace();				
			}
		}
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false);
			ringProgress.dismiss();
			CustomToast.makeText(CollectionStartActivity.this, "Cash Counter Extension Failed.Contact NSOFT", Toast.LENGTH_SHORT);
		}
	}
	//24-03-2015
	private class ExtendCashLimit extends AsyncTask<Void, Void, Void>  
	{
		final ProgressDialog ringProgress = ProgressDialog.show(CollectionStartActivity.this, "Please wait..", "Cash Counter Extension...",true);
		String rValue = "";
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try
			{
				CommonFunction cf  = new CommonFunction();
				String curdate = cf.DateConvertFullYear(cf.GetCurrentTime());

				String IMEINumber ="";
				String SimNumber = "";
				try
				{
					//Get Mobile Details ==============================================================
					//10-03-2021
					IMEINumber = CommonFunction.getDeviceNo(CollectionStartActivity.this);
					SimNumber = CommonFunction.getSIMNo(CollectionStartActivity.this);

				}
				catch(Exception e)
				{
									
				}

				HttpClient httpclt = new DefaultHttpClient();//object for HttpClient								
				HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Cash_Extension_Android");//HttpPost method uri

				List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
				lvp.add(new BasicNameValuePair("Flag","8"));//Flag
				lvp.add(new BasicNameValuePair("SBMNo",IMEINumber));//IMEI No				
				lvp.add(new BasicNameValuePair("BatchNo",CC.getmBatch_No().trim()));
				lvp.add(new BasicNameValuePair("ExtendedCashLimit",mSecondSession.split("#")[0]));
				lvp.add(new BasicNameValuePair("ExtendedChequeLimit",mSecondSession.split("#")[1]));
				lvp.add(new BasicNameValuePair("ExtendedTimeLimit",mSecondSession.split("#")[2]));
				httpPost.setEntity((new UrlEncodedFormEntity(lvp)));				
				httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");							
				HttpResponse res = httpclt.execute(httpPost);//execute post method
				HttpResponse k = res;
				HttpEntity ent = k.getEntity();
				if(ent != null)
				{
					rValue = EntityUtils.toString(ent);				
				}
				//rValue = "ACK";
			}
			catch (Exception e)
			{
				cancel(true);		
				e.printStackTrace();
			}
			return null;	
		}		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			ringProgress.setCancelable(false);
			ringProgress.dismiss();			
			try
			{
				if(rValue.indexOf("NACK")!=-1) //NACK received 
				{					
					CustomToast.makeText(CollectionStartActivity.this, "Currently there is no Extended Cash Limit for this BatchNo", Toast.LENGTH_SHORT);					
					return;
				}
				else if(rValue.indexOf("ACK")!=-1)//ACK received 
				{
					CustomToast.makeText(CollectionStartActivity.this, "Extension Requested Successfully.", Toast.LENGTH_LONG);					
					Intent i = new Intent(CollectionStartActivity.this, CollectionStartActivity.class);
					startActivity(i); 
				}
				else//ERR received 
				{				
					CustomToast.makeText(CollectionStartActivity.this, "Error in Cash Extension.Contact NSOFT", Toast.LENGTH_SHORT);
				}
			}
			catch(Exception e)
			{
				CustomToast.makeText(CollectionStartActivity.this, "Error in Cash Extension.Contact NSOFT", Toast.LENGTH_SHORT);
				cancel(true);	
				e.printStackTrace();				
			}
		}
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false);
			ringProgress.dismiss();
			CustomToast.makeText(CollectionStartActivity.this, "Error in Cash Extension.Contact NSOFT", Toast.LENGTH_SHORT);
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
		return OptionsMenu.navigate(item,CollectionStartActivity.this);			
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		CustomToast.makeText(CollectionStartActivity.this, "Please use menu to navigate.", Toast.LENGTH_SHORT);
		return;
	}
	@Override
	public void performAction(boolean alertResult, int functionality) {
		//If alertReult is false  and functionality = 1 Close Cash Counter 
		if(!alertResult && functionality==Button1) 
		{			
			new CashCounterClose().execute();			
		}	
		//If alertReult is false  and functionality = 2 Extend Limit
		//24-03-2015
		else if(!alertResult && functionality==Button2) 
		{
			new ExtendCashLimitRequest().execute();			
		}
	}	
}
