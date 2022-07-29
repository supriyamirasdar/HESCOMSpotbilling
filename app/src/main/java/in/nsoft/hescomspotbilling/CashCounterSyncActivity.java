//Created Nitish 05-04-2014
package in.nsoft.hescomspotbilling;


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
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CashCounterSyncActivity extends Activity {

	String IMEINumber = "";
	String SimNumber = "";
	String mOTPText="";
	private boolean isInternetConnected;	

	long diff;
	private Button btnSendOTP,btnGetClose; 	
	private TextView txtIMEISIMNo,txtTimeSync,txtSessionAlloc,txtOTPVer,lblEnterOTP;
	private EditText txtEnterOTP;
	private ImageView imgIMEISIMNo,imgTimeSync,imgSessionAlloc,imgOTPVer;
	private final DatabaseHelper mDb =new DatabaseHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cash_counter_sync);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		isInternetConnected = false;

		txtIMEISIMNo = (TextView)findViewById(R.id.txtIMEISIMNo);
		txtTimeSync = (TextView)findViewById(R.id.txtTimeSync);
		txtSessionAlloc = (TextView)findViewById(R.id.txtSessionAlloc);
		txtOTPVer = (TextView)findViewById(R.id.txtOTPVer);	

		lblEnterOTP = (TextView)findViewById(R.id.lblEnterOTP);		
		txtEnterOTP = (EditText)findViewById(R.id.txtEnterOTP);	
		btnSendOTP = (Button)findViewById(R.id.btnSendOTP);	
		btnGetClose = (Button)findViewById(R.id.btnGetBatchNo);	//Modified 11-06-2014

		imgIMEISIMNo = (ImageView)findViewById(R.id.imgIMEISIMNo);	
		imgTimeSync = (ImageView)findViewById(R.id.imgTimeSync);	
		imgSessionAlloc = (ImageView)findViewById(R.id.imgSessionAlloc);	
		imgOTPVer = (ImageView)findViewById(R.id.imgOTPVer);

		/*//Get Mobile Details ==============================================================
		TelephonyManager mgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		IMEINumber = mgr.getDeviceId();
		SimNumber = mgr.getSimSerialNumber(); 
		//END Get Mobile Details ==========================================================	
		 */
		
		try
		{
			//Get Mobile Details ==============================================================
			//10-03-2021
			IMEINumber = CommonFunction.getDeviceNo(CashCounterSyncActivity.this);
			SimNumber = CommonFunction.getSIMNo(CashCounterSyncActivity.this);
			

		}
		catch(Exception e)
		{
							
		}


		visibleOTP(false);  //OTP Details should not be visible	
		btnGetClose.setVisibility(View.INVISIBLE); //Modified 11-06-2014

		//Check SBMFile Loaded
		try
		{
			//get status where statusid = 9 from StatusMaster to check if  SBM File is Loaded			
			if(mDb.GetStatusMasterDetailsByID("7") != 1)//if equal to 1, SBM File is Loaded	 
			{
				CustomToast.makeText(CashCounterSyncActivity.this,"Please Load SBM File to Database." , Toast.LENGTH_SHORT);
				return;				
			}
		}
		catch (Exception e)
		{			
			e.printStackTrace();
		}
		//End SBM File Loaded
		//Check cashcounter is open==============================================================
		try
		{			
			//get status where statusid = 11 from StatusMaster to check if cashcounter is open			
			if(mDb.GetStatusMasterDetailsByID("11") == 1)//if equal to 1, CashCounter Open Move To CollectionStart Screen directly
			{	
				//23-02-2018 Test Check
				//String currentdatetime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
				//mDb.UpdateStatusMasterDetailsByID("4", "1", currentdatetime);  
				//Check end
				mDb.UpdateStatusInStatusMaster("4", "0"); //23-02-2018 OTP Generation Completed So Reset 
				Intent i = new Intent(CashCounterSyncActivity.this, CollectionStartActivity.class);
				startActivity(i); 
				return;
			}
			//If cashcounter is Closed for that day do not allow new Batch generation
			else if((mDb.GetStatusMasterDetailsByID("14") == 1))//if equal to 1, CashCounter Closed=1 Move To CollectionStart Screen directly
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
					mDb.UpdateStatusInStatusMaster("4", "0"); //23-02-2018 OTP Generation Completed So Reset
					Intent i = new Intent(CashCounterSyncActivity.this, CollectionStartActivity.class);
					startActivity(i); 
					return;
				}
				//Commented 05-10-2020 Enable for Second session
				/*else if(CurMonthYear.equals(batchMonthYear) && (curDay == nextBatchDay)) //New
				{
					mDb.UpdateStatusInStatusMaster("4", "0"); //23-02-2018 OTP Generation Completed So Reset
					Intent i = new Intent(CashCounterSyncActivity.this, CollectionStartActivity.class);
					startActivity(i); 
					return;
				}*/
				//End Nitish 03-02-2018
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			//txtIMEISIMNo.setText();
			e.printStackTrace();
		}
		//End  cashcounter Check==============================================================		
		//Check Internet Connection ==========================================================
		ConnectionDetector conn = new ConnectionDetector(this);
		if(!isInternetConnected)
		{
			if(conn.isConnectedToInternet())
			{
				isInternetConnected = true;
			}
			else
			{
				isInternetConnected = false;
				CustomToast.makeText(CashCounterSyncActivity.this,"Please Enable Inernet Connection " , Toast.LENGTH_SHORT);
				return;	
			}
		}	
		//End Check Internet Connection ==========================================================

		//IMEI and SIMNo Verification ==========================================================		
		try
		{
			txtIMEISIMNo.setText(mDb.GetStatusMasterDetailsByID("16") == 1? ConstantClass.mVerified: ConstantClass.mNotVerified);
			imgIMEISIMNo.setImageResource(R.drawable.tick_mark);			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			//txtIMEISIMNo.setText();
			e.printStackTrace();
		}
		//End IMEI and SIMNo Verification ==========================================================

		//Check Time Synchronisation ==========================================================
		try
		{
			CollectionObject.GetCollectionObject().setmIsTimeSync(false); //Set Time Sync as False
			txtTimeSync.setText(ConstantClass.mPending);
			imgTimeSync.setImageResource(R.drawable.cross_mark);
			new TimeSync().execute();//call Time Synchronisation Asynchronous Task
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			//txtIMEISIMNo.setText();
			e.printStackTrace();
		}		
		//End  Time Synchronisation  Check==========================================================
		btnSendOTP.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(txtEnterOTP.length() == 0)
				{
					CustomToast.makeText(CashCounterSyncActivity.this,"Enter OTP." , Toast.LENGTH_SHORT);
					return;
				}
				mOTPText = String.valueOf(txtEnterOTP.getText().toString());
				new OTPVerification().execute();
			}
		});	
		//Modified 11-06-2014
		btnGetClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(btnGetClose.getText().toString().matches(ConstantClass.mbtnGetBatchNo)) //Getting Generated Batch No
				{
					new GetExistingBatchNo().execute();
				}
				else //if(btnGetClose.getText().toString().matches(mbtnPrevClose)) Closing Previous Batch
				{
					new ClosePrevCounter().execute();
				}
			}
		});	
	}
	//Time Synchronisation
	//Executes in background
	private class TimeSync extends AsyncTask<Void, Void, Void>  //GenericTypes AsyncTask<parametertype, progressparameter, postexecuteparam >
	{
		final ProgressDialog ringProgress = ProgressDialog.show(CashCounterSyncActivity.this, "Please wait..", "Time Sync...",true);
		String rValue = "";
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub	
			try
			{	
				HttpClient httpclt = new DefaultHttpClient();//object for HttpClient
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
				CustomToast.makeText(CashCounterSyncActivity.this, "Error in Time Synchronisation", Toast.LENGTH_SHORT);					
				return;
			}
			else if(rValue.indexOf("ACK")!=-1)//ACK received 
			{	
				//Assume rValue  = ACK150520141505
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

					//Check DateTime 1s -> 1000ms  1min -> 60000ms  10min ->600000ms
					if(sdfDate.parse(currentdate).after(sdfDate.parse(serDate)) || sdfDate.parse(currentdate).before(sdfDate.parse(serDate)) || diff > ConstantClass.difftime)
					{
						CustomToast.makeText(CashCounterSyncActivity.this,"Mobile Date Time not matching with Server."  , Toast.LENGTH_SHORT);
						TimeChangedReceiver.setValid(false);
						return;
					}				
					else //On Time Sync Success
					{
						txtTimeSync.setText(ConstantClass.mComplete);
						imgTimeSync.setImageResource(R.drawable.tick_mark);
						CollectionObject.GetCollectionObject().setmIsTimeSync(true);	
						TimeChangedReceiver.setValid(true);				
						CustomToast.makeText(CashCounterSyncActivity.this, "Time synchronisation Successful.", Toast.LENGTH_SHORT);	
						new MRAssignment().execute();//call MRAssignment Asynchronous Task		
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
				CustomToast.makeText(CashCounterSyncActivity.this, "Error Occured", Toast.LENGTH_SHORT);
			}		
		}	
		//If Exception Occured 
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false);
			ringProgress.dismiss();
			CustomToast.makeText(CashCounterSyncActivity.this, "Time Synchronisation Failed.", Toast.LENGTH_SHORT);			
		}
	}
	//Executes in background
	private class MRAssignment extends AsyncTask<Void, Void, Void>  //GenericTypes AsyncTask<parametertype, progressparameter, postexecuteparam >
	{
		final ProgressDialog ringProgress = ProgressDialog.show(CashCounterSyncActivity.this, "Please wait..", "MR Assignment...",true);
		String rValue = "";
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub	
			try
			{
				//23-02-2018				
				boolean checkOTP = isOTPTimeCheckValid();
				if(checkOTP)
				{
					rValue = "OTPGENERATED";
				}
				else
				{
					mDb.UpdateStatusInStatusMaster("4", "0"); //23-02-2018 OTP Generation Expired/Invalid So Reset 
					CommonFunction cf  = new CommonFunction();
					String curdate = cf.DateConvertFullYear(cf.GetCurrentTime());

					HttpClient httpclt = new DefaultHttpClient();//object for HttpClient
					HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/GetSyncVerifyCode");//HttpPost method uri
					List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
					lvp.add(new BasicNameValuePair("SBMNO",IMEINumber));//IMEI
					lvp.add(new BasicNameValuePair("SIMNO", SimNumber));//SIM Serial No
					lvp.add(new BasicNameValuePair("Date", curdate));//ddmmyyyy
					lvp.add(new BasicNameValuePair("IP", "0"));
					lvp.add(new BasicNameValuePair("Version", ConstantClass.FileVersion));//Version
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
				//rValue = "ACK";
			}
			catch (Exception e)
			{				
				cancel(true);				
				e.printStackTrace();
			}		
			return null;		
		}		
		// On Completion of MR Assignment  Process 
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			ringProgress.setCancelable(false);
			ringProgress.dismiss();	
			if(rValue.indexOf("NAS")!=-1)
			{
				txtSessionAlloc.setText(ConstantClass.mNAS);
			}
			else if(rValue.indexOf("NAP")!=-1)
			{
				txtSessionAlloc.setText(ConstantClass.mNAP);
			}
			else if(rValue.indexOf("SNV")!=-1)//SIM Not Valid
			{
				txtSessionAlloc.setText(ConstantClass.mSNV);
			}
			else if(rValue.indexOf("SNA")!=-1)//Session Not Allowed
			{
				txtSessionAlloc.setText(ConstantClass.mSNA);
			}
			else if(rValue.indexOf("VM")!=-1)//VM
			{
				CustomToast.makeText(CashCounterSyncActivity.this, "Version Mismatch", Toast.LENGTH_SHORT);
			}
			else if(rValue.indexOf("SNC")!=-1) //Session Not Closed
			{
				//Modified 11-06-2014
				//SNC#20140612
				String bdate[] = rValue.split("#");		
				String batchdate = new CommonFunction().YearMMDDReversal(bdate[1], "-");
				String currentdate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
				btnGetClose.setVisibility(View.VISIBLE); 
				if(batchdate.matches(currentdate))  //If BatchDate equals Current Date
				{					
					btnGetClose.setText(ConstantClass.mbtnGetBatchNo); 
				}
				// If Cash counter details does not exist || If Prev Cash counter not closed
				else if(mDb.GetCashCounterDetails().getmCashCounterOpen() == null || mDb.GetCashCounterDetails().getmCashCounterOpen().equals("0"))  
				{					
					btnGetClose.setText(ConstantClass.mbtnPrevClose);
				}
				else
				{
					btnGetClose.setVisibility(View.INVISIBLE); 
				}
				CustomToast.makeText(CashCounterSyncActivity.this, "Session Not Approved for " + new CommonFunction().YearMMDDReversal(bdate[1], "-"), Toast.LENGTH_SHORT);
			}
			else if(rValue.indexOf("NACK")!=-1)//NACK received or any Error 
			{
				CustomToast.makeText(CashCounterSyncActivity.this, "Error Occured", Toast.LENGTH_SHORT);
			}	
			else if(rValue.indexOf("ACK")!=-1)//ACK received 
			{
				txtSessionAlloc.setText(ConstantClass.mAPP);				
				imgSessionAlloc.setImageResource(R.drawable.tick_mark);
				visibleOTP(true); //OTP Details Should be visible
				btnGetClose.setVisibility(View.INVISIBLE); //Modified 11-06-2014
				CustomToast.makeText(CashCounterSyncActivity.this, "MR Assignment  Process Completed", Toast.LENGTH_SHORT);	

				//23-02-2018 OTP Generation Valid So Set Status and DateTime 
				String currentdatetime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
				mDb.UpdateStatusMasterDetailsByID("4", "1", currentdatetime);  
			}
			else if(rValue.indexOf("OTP")!=-1)//OTP Already Generated  //23-02-2018
			{
				txtSessionAlloc.setText(ConstantClass.mAPP);				
				imgSessionAlloc.setImageResource(R.drawable.tick_mark);
				visibleOTP(true); //OTP Details Should be visible
				btnGetClose.setVisibility(View.INVISIBLE); //Modified 11-06-2014
				CustomToast.makeText(CashCounterSyncActivity.this, "OTP Generated.OTP Request is valid for 3 min", Toast.LENGTH_LONG);					
			}
			else//any Error 
			{
				CustomToast.makeText(CashCounterSyncActivity.this, "Error Occured", Toast.LENGTH_SHORT);
			}		
		}	
		//If Exception Occured 
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false);
			ringProgress.dismiss();
			CustomToast.makeText(CashCounterSyncActivity.this, "MR Assignment Failed.", Toast.LENGTH_SHORT);
		}
	}
	private class OTPVerification extends AsyncTask<Void, Void, Void>  
	{
		final ProgressDialog ringProgress = ProgressDialog.show(CashCounterSyncActivity.this, "Please wait..", "OTP Verification...",true);
		String rValue = "";
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try
			{
				CommonFunction cf  = new CommonFunction();
				String curdate = cf.DateConvertFullYear(cf.GetCurrentTime());

				HttpClient httpclt = new DefaultHttpClient();//object for HttpClient
				HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/GetSyncData_Android");//HttpPost method uri
				List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
				lvp.add(new BasicNameValuePair("SBMNO", mDb.GetLocationCode().trim() + IMEINumber));//LocationCode + IMEI
				lvp.add(new BasicNameValuePair("SIMNO", SimNumber));//SIM Serial No
				lvp.add(new BasicNameValuePair("Date",curdate ));//ddmmyyyy
				lvp.add(new BasicNameValuePair("IP", "0"));
				lvp.add(new BasicNameValuePair("Version", ConstantClass.FileVersion));
				lvp.add(new BasicNameValuePair("Password", mOTPText));
				httpPost.setEntity((new UrlEncodedFormEntity(lvp)));
				httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");							
				HttpResponse res = httpclt.execute(httpPost);//execute post method
				HttpResponse k = res;
				HttpEntity ent = k.getEntity();
				if(ent != null)
				{
					rValue = EntityUtils.toString(ent);				
				}
				//rValue ="ACK18072017|50000|1000|2100|51101051804148628090215465290006|041815322014|0001000001|1|0|0|10|40000|10000|MRName|0$";
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
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
			if(rValue.indexOf("NACK")!=-1) //NACK received 
			{
				//mDb.UpdateStatusInStatusMaster("11", "0");  //Cash Counter Open=0
				//mDb.UpdateStatusInStatusMaster("12", "0");  //BatchNo Not Received
				//mDb.UpdateStatusInStatusMaster("14", "1");  //Cash Counter Closed =1
				CustomToast.makeText(CashCounterSyncActivity.this, "Error in OTP Verification", Toast.LENGTH_SHORT);				
				return;
			}
			else if(rValue.indexOf("ACK")!=-1)//ACK received 
			{	
				//27-04-2017
				//ACK Format Example
				//ACK18042014|50000|1000|2100|51101051804148628090215465290006|041815322014|0001000001|1|0|0|10|40000|10000|MRName|0$
				//sp[0] = 18042014 --BatchDate
				//sp[1] = 50000 --Cash Limit
				//sp[2] = 1000 --Start Time
				//sp[3] = 2100 --End Time
				//sp[4] = 51101051804148628090215465290006 --BatchNo
				//sp[5] = 041815322014 -datetime
				//sp[6] = 0001000001 -LastReceiptNo
				//sp[7] = 1 -RevenueFlag
				//sp[8] = 0 -MiscFlag
				//sp[9] = 0 -ChqFlag
				//sp[10] = 10 -PartPaymentPerc
				//sp[11] = 40000 -CLimit
				//sp[12] = 10000 -CDLimit
				//sp[13] = MRName -MRName
				//sp[14] = 0 -ReprintFlag

				//rValue = "ACK18042014|500000|1000|2100|51101051804148628090215465290006|041815322014|0001000001"$				
				rValue = rValue.replace("ACK", ""); //Replace ACK
				rValue = rValue.replace("$", "");  //Replace $ 
				rValue = rValue.replace("|","#");  // Split not working with "|" so replace "|" with "#" then split 
				String sp[] = rValue.split("#");				
				int res = mDb.CashCounterDetailsSave(IMEINumber, SimNumber, sp); // Save Cash Counter Details
				if(res==1)
				{
					mDb.UpdateStatusInStatusMaster("11", "1");  //Cash Counter Open
					mDb.UpdateStatusInStatusMaster("12", "1");  //BatchNo Received
					mDb.UpdateStatusInStatusMaster("14", "0");  //Cash Counter Closed
					txtOTPVer.setText(ConstantClass.mVerified);
					imgOTPVer.setImageResource(R.drawable.tick_mark);
					CustomToast.makeText(CashCounterSyncActivity.this, "OTP Verification Complete. Cash Counter Opened", Toast.LENGTH_SHORT);
					Intent i = new Intent(CashCounterSyncActivity.this, CollectionStartActivity.class);
					startActivity(i);
				}
			}
			else if(rValue.indexOf("TOTP")!=-1) //TOTP received 
			{
				mDb.UpdateStatusInStatusMaster("11", "0");  //Cash Counter Open=0
				mDb.UpdateStatusInStatusMaster("12", "0");  //BatchNo Not Received
				mDb.UpdateStatusInStatusMaster("14", "1");  //Cash Counter Closed =1
				CustomToast.makeText(CashCounterSyncActivity.this, "OTP Time Limit Expired", Toast.LENGTH_SHORT);				
				return;
			}
			else //Others
			{
				//mDb.UpdateStatusInStatusMaster("11", "0");  //Cash Counter Open=0
				//mDb.UpdateStatusInStatusMaster("12", "0");  //BatchNo Not Received
				//mDb.UpdateStatusInStatusMaster("14", "1");  //Cash Counter Closed =1
				CustomToast.makeText(CashCounterSyncActivity.this, "Error in OTP Verification", Toast.LENGTH_SHORT);				
				return;
			}			
		}
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false);
			ringProgress.dismiss();
			CustomToast.makeText(CashCounterSyncActivity.this, "OTP Verification failed.", Toast.LENGTH_SHORT);
		}
	}
	//Modified Nitish 11-06-2014
	private class GetExistingBatchNo extends AsyncTask<Void, Void, Void>  
	{
		final ProgressDialog ringProgress = ProgressDialog.show(CashCounterSyncActivity.this, "Please wait..", "Getting Existing BatchNo...",true);
		String rValue = "";
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try
			{

				CommonFunction cf  = new CommonFunction();
				String curdate = cf.DateConvertFullYear(cf.GetCurrentTime());

				HttpClient httpclt = new DefaultHttpClient();//object for HttpClient
				HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Get_ExistingBatchNo_Android");//HttpPost method uri
				List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
				lvp.add(new BasicNameValuePair("SBMNo", mDb.GetLocationCode().trim() + IMEINumber));// IMEI
				lvp.add(new BasicNameValuePair("SIMNo", SimNumber));//SIM Serial No
				lvp.add(new BasicNameValuePair("Date",curdate));//ddmmyyyy				
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
				// TODO Auto-generated catch block
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
			if(rValue.indexOf("NACK")!=-1) //NACK received 
			{
				mDb.UpdateStatusInStatusMaster("11", "0");  //Cash Counter Open=0
				mDb.UpdateStatusInStatusMaster("12", "0");  //BatchNo Not Received
				mDb.UpdateStatusInStatusMaster("14", "1");  //Cash Counter Closed =1
				CustomToast.makeText(CashCounterSyncActivity.this, "Error in Getting Existing Batch Number.", Toast.LENGTH_SHORT);				
				return;
			}
			else if(rValue.indexOf("ACK")!=-1)//ACK received 
			{				
				//ACK Format Example
				//ACK11062014|658000|1000|2200|51204011106148628090215465290458|061117042014$
				//ACK18042014|500000|1000|2100|51101051804148628090215465290006|041815322014$
				//sp[0] = 18042014 --BatchDate
				//sp[1] = 500000 --Cash Limit
				//sp[2] = 1000 --Start Time
				//sp[3] = 2100 --End Time
				//sp[4] = 51101051804148628090215465290006 --BatchNo
				//sp[5] = 041815322014 -datetime

				//rValue = "ACK18042014|500000|1000|2100|51101051804148628090215465290006|041815322014$";				
				rValue = rValue.replace("ACK", ""); //Replace ACK
				rValue = rValue.replace("$", "");  //Replace $ 
				rValue = rValue.replace("|","#");  // Split not working with "|" so replace "|" with "#" then split 

				String sp[] = rValue.split("#");				
				int res = mDb.CashCounterDetailsSave(IMEINumber, SimNumber, sp); // Save Cash Counter Details

				if(res==1)
				{
					mDb.UpdateStatusInStatusMaster("11", "1");  //Cash Counter Open
					mDb.UpdateStatusInStatusMaster("12", "1");  //BatchNo Received
					mDb.UpdateStatusInStatusMaster("14", "0");  //Cash Counter Closed
					txtOTPVer.setText(ConstantClass.mVerified);
					imgOTPVer.setImageResource(R.drawable.tick_mark);
					CustomToast.makeText(CashCounterSyncActivity.this, "Batch No Received. Cash Counter Opened", Toast.LENGTH_SHORT);
					Intent i = new Intent(CashCounterSyncActivity.this, CollectionStartActivity.class);
					startActivity(i); 
				}
			}
			else //Others
			{
				mDb.UpdateStatusInStatusMaster("11", "0");  //Cash Counter Open=0
				mDb.UpdateStatusInStatusMaster("12", "0");  //BatchNo Not Received
				mDb.UpdateStatusInStatusMaster("14", "1");  //Cash Counter Closed =1
				CustomToast.makeText(CashCounterSyncActivity.this, "Error in Getting Existing Batch Number", Toast.LENGTH_SHORT);				
				return;
			}			
		}
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false);
			ringProgress.dismiss();
			CustomToast.makeText(CashCounterSyncActivity.this, "Error in Getting Existing Batch Number.", Toast.LENGTH_SHORT);
		}
	}
	//Modified Nitish 11-06-2014
	private class ClosePrevCounter extends AsyncTask<Void, Void, Void> 	
	{
		final ProgressDialog ringProgress = ProgressDialog.show(CashCounterSyncActivity.this, "Please wait..", "Closing Previous Cash Counter Manually...",true);
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
				
				try
				{
					//Get Mobile Details ==============================================================
					//10-03-2021
					IMEINumber = CommonFunction.getDeviceNo(CashCounterSyncActivity.this);
					SimNumber = CommonFunction.getSIMNo(CashCounterSyncActivity.this);

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
				lvp.add(new BasicNameValuePair("Batch_No","0"));
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
				CustomToast.makeText(CashCounterSyncActivity.this, "Previous Cash Counter Closed", Toast.LENGTH_SHORT);
			}
			else//NACK received then update flag 0
			{				
				CustomToast.makeText(CashCounterSyncActivity.this, "Cash Counter Already Closed/No Response from server .", Toast.LENGTH_SHORT);
			}
		}
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false);
			ringProgress.dismiss();
			CustomToast.makeText(CashCounterSyncActivity.this, "Error Occured/No Response from server", Toast.LENGTH_SHORT);
		}
	}	
	private void visibleOTP(boolean visible)
	{
		if(!visible)  //OTP Details should not be visible
		{
			lblEnterOTP.setVisibility(View.INVISIBLE);			
			txtEnterOTP.setVisibility(View.INVISIBLE); 			
			btnSendOTP.setVisibility(View.INVISIBLE);			
		}
		else  //OTP Details should  be visible
		{
			lblEnterOTP.setVisibility(View.VISIBLE);				
			txtEnterOTP.setVisibility(View.VISIBLE); 								 
			btnSendOTP.setVisibility(View.VISIBLE);	
			lblEnterOTP.setText("(OTP Request is valid for 3 minutes)"); //23-02-2017
		}				
	}
	//23-02-2018
	private boolean isOTPTimeCheckValid()
	{
		try
		{
			if(mDb.GetStatusMasterDetailsByID("4")==1)
			{
				String otpDateTime = mDb.GetStatusMasterValue("4");
				String currentdateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
				SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
				diff =   sdfDateTime.parse(currentdateTime).getTime() - sdfDateTime.parse(otpDateTime).getTime();
				if( diff < ConstantClass.difftimeOTP)
					return true;
				else
					return false;
			}
			else 
			{
				return false;
			}
		}
		catch(Exception e)
		{
			return false;
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
		return OptionsMenu.navigate(item,CashCounterSyncActivity.this);			
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub	
		CustomToast.makeText(CashCounterSyncActivity.this, "Please use menu to navigate.", Toast.LENGTH_SHORT);
		return;
	}	
}
