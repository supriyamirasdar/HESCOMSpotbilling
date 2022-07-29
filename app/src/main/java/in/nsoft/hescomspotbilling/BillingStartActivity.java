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

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//Nitish 15-07-2016
public class BillingStartActivity extends Activity{

	Button btnManualBilling,btnOpticalBilling;
	TextView lblBlutoothDevice;
	TextView txtIMEISIMNo,txtTimeSync,txtSessionAlloc,txtLocationCode;
	ImageView imgIMEISIMNo,imgTimeSync,imgSessionAlloc;
	TextView txtBatchDate,txtStartTime,txtEndTime;
	private final DatabaseHelper mDb =new DatabaseHelper(this);

	String IMEINumber = "";
	String SimNumber = "";
	long diff;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billing_start);
		lblBlutoothDevice= (TextView)findViewById(R.id.lblBlutoothDevice);
		txtIMEISIMNo = (TextView)findViewById(R.id.txtIMEISIMNo);
		txtTimeSync = (TextView)findViewById(R.id.txtTimeSync);
		txtSessionAlloc = (TextView)findViewById(R.id.txtSessionAlloc);
		txtLocationCode = (TextView)findViewById(R.id.txtLocationCode);

		txtBatchDate =(TextView)findViewById(R.id.txtBatchDate);
		txtStartTime =(TextView)findViewById(R.id.txtStartTime);
		txtEndTime =(TextView)findViewById(R.id.txtEndTime);

		btnManualBilling = (Button)findViewById(R.id.btnManualBilling);	
		btnOpticalBilling = (Button)findViewById(R.id.btnOpticalBilling);	
		imgIMEISIMNo = (ImageView)findViewById(R.id.imgIMEISIMNo);	
		imgTimeSync = (ImageView)findViewById(R.id.imgTimeSync);	
		imgSessionAlloc = (ImageView)findViewById(R.id.imgSessionAlloc);
		


		try
		{
			lblBlutoothDevice.setText("Bluetooth Device		:" + DynamicBluetooth.bluetoothDeviceName.toString().trim());
		}
		catch(Exception e)
		{

		}

		

		try
		{
			//Get Mobile Details ==============================================================
			//10-03-2021
			IMEINumber = CommonFunction.getDeviceNo(BillingStartActivity.this);
			SimNumber = CommonFunction.getSIMNo(BillingStartActivity.this);
			

		}
		catch(Exception e)
		{
							
		}
		//Check Internet Connection ==========================================================

		btnManualBilling.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(BillingStartActivity.this, Billing.class);				
				startActivity(i);
			}
		});



		btnOpticalBilling.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(BillingStartActivity.this, Billing_LandT.class);				
				startActivity(i);
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
		return OptionsMenu.navigate(item,BillingStartActivity.this);			
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		//super.onBackPressed();
		CustomToast.makeText(BillingStartActivity.this, "Please use menu to navigate.", Toast.LENGTH_SHORT);
		return;
	}
	private class TimeSync extends AsyncTask<Void, Void, Void>  //GenericTypes AsyncTask<parametertype, progressparameter, postexecuteparam >
	{
		final ProgressDialog ringProgress = ProgressDialog.show(BillingStartActivity.this, "Please wait..", "Time Sync...",true);
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
				CustomToast.makeText(BillingStartActivity.this, "Error in Time Synchronisation", Toast.LENGTH_SHORT);					
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
						CustomToast.makeText(BillingStartActivity.this,"Mobile Date Time not matching with Server."  , Toast.LENGTH_SHORT);
						return;
					}				
					else //On Time Sync Success
					{
						txtTimeSync.setText(ConstantClass.mComplete);
						imgTimeSync.setImageResource(R.drawable.tick_mark);
						CustomToast.makeText(BillingStartActivity.this, "Time synchronisation Successful.", Toast.LENGTH_SHORT);	
						new MRVerification().execute();//call MRverification Asynchronous Task		
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
				CustomToast.makeText(BillingStartActivity.this, "Error Occured", Toast.LENGTH_SHORT);
			}		
		}	
		//If Exception Occured 
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false);
			ringProgress.dismiss();
			CustomToast.makeText(BillingStartActivity.this, "Time Synchronisation Failed.", Toast.LENGTH_SHORT);			
		}
	}

	private class MRVerification extends AsyncTask<Void, Void, Void>  
	{
		final ProgressDialog ringProgress = ProgressDialog.show(BillingStartActivity.this, "Please wait..", "MR Verification...",true);
		String rValue = "";
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try
			{
				CommonFunction cf  = new CommonFunction();
				String curdate = cf.DateConvertFullYear(cf.GetCurrentTime());

				HttpClient httpclt = new DefaultHttpClient();//object for HttpClient
				HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/VerifyBilling_Android");//HttpPost method uri
				List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
				lvp.add(new BasicNameValuePair("Flag","8"));		
				lvp.add(new BasicNameValuePair("IMEINO",IMEINumber));//IMEINO
				lvp.add(new BasicNameValuePair("SIMNO", SimNumber));//SIM Serial No
				lvp.add(new BasicNameValuePair("Date",curdate ));//ddmmyyyy					
				lvp.add(new BasicNameValuePair("Version", ConstantClass.FileVersion));
				lvp.add(new BasicNameValuePair("Password",  mDb.GetLocationCode().trim()));

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
			if(rValue.indexOf("NACKONE")!=-1) //NACKONE received 
			{
				mDb.UpdateStatusInStatusMaster("5", "0");  //Billing Closed
				CustomToast.makeText(BillingStartActivity.this, "Staff IMEINO not registered.", Toast.LENGTH_SHORT);				
				return;
			}
			else if(rValue.indexOf("NACKTWO")!=-1) //NACK2 received 
			{
				mDb.UpdateStatusInStatusMaster("5", "0");  //Billing Closed
				CustomToast.makeText(BillingStartActivity.this, "Staff LocationCode mismatch with File LocationCode.", Toast.LENGTH_SHORT);				
				return;
			}
			else if(rValue.indexOf("NACKTHREE")!=-1) //NACK3 received 
			{
				mDb.UpdateStatusInStatusMaster("5", "0");  //Billing Closed
				CustomToast.makeText(BillingStartActivity.this, "Batch Date Time Not Matching with Server", Toast.LENGTH_SHORT);				
				return;
			}
			else if(rValue.indexOf("NACK")!=-1) //NACK received 
			{
				mDb.UpdateStatusInStatusMaster("5", "0");  //Billing Closed
				CustomToast.makeText(BillingStartActivity.this, "MR Verification Failed.", Toast.LENGTH_SHORT);				
				return;
			}
			else if(rValue.indexOf("NAS")!=-1)
			{
				mDb.UpdateStatusInStatusMaster("5", "0");  //Billing Closed
				txtSessionAlloc.setText(ConstantClass.mNAS);
			}
			else if(rValue.indexOf("NAP")!=-1)
			{
				mDb.UpdateStatusInStatusMaster("5", "0");  //Billing Closed
				txtSessionAlloc.setText(ConstantClass.mNAP);
			}
			else if(rValue.indexOf("SNV")!=-1)//SIM Not Valid
			{
				mDb.UpdateStatusInStatusMaster("5", "0");  //Billing Closed
				txtSessionAlloc.setText(ConstantClass.mSNV);
			}
			else if(rValue.indexOf("VM")!=-1)//VM
			{
				mDb.UpdateStatusInStatusMaster("5", "0");  //Billing Closed
				CustomToast.makeText(BillingStartActivity.this, "Version Mismatch", Toast.LENGTH_SHORT);
			}
			else if(rValue.indexOf("ACK")!=-1)//ACK received 
			{				
				//ACK Format Example
				//ACK15072016|1000|2100|041815322016$
				//sp[0] = 18042014 --BatchDate				
				//sp[1] = 1000 --Start Time
				//sp[2] = 2100 --End Time				
				//sp[3] = 041815322014 -datetime

				//rValue = "ACK15072016|1000|2100|041815322016|5110102$";				
				rValue = rValue.replace("ACK", ""); //Replace ACK
				rValue = rValue.replace("$", "");  //Replace $ 
				rValue = rValue.replace("|","#");  // Split not working with "|" so replace "|" with "#" then split 

				String sp[] = rValue.split("#");		
				mDb.DropCreateBillingDetails();
				int res = mDb.BillingDetailsSave(IMEINumber, SimNumber, sp); // Save Cash Counter Details

				if(res==1)
				{
					mDb.UpdateStatusInStatusMaster("5", "1");  //MR Allocated and Billing Open
					txtSessionAlloc.setText(ConstantClass.mVerified);
					imgSessionAlloc.setImageResource(R.drawable.tick_mark);
					btnManualBilling.setVisibility(View.VISIBLE);
					btnOpticalBilling.setVisibility(View.VISIBLE);

					txtBatchDate.setText((new CommonFunction()).DateConvertAddChar(mDb.GetBillingDetails().getmBatch_Date(),"-"));
					txtStartTime.setText((new CommonFunction()).TimeConvertAddChar(mDb.GetBillingDetails().getmStartTime(),":"));
					txtEndTime.setText((new CommonFunction()).TimeConvertAddChar(mDb.GetBillingDetails().getmEndTime(),":"));
					txtSessionAlloc.setText(ConstantClass.mVerified);
					imgSessionAlloc.setImageResource(R.drawable.tick_mark);
					btnManualBilling.setVisibility(View.VISIBLE);
					btnOpticalBilling.setVisibility(View.VISIBLE);
					try
					{
						txtLocationCode.setText(String.valueOf(mDb.GetBillingDetails().getmLocationCode().toString().trim()));
					}
					catch(Exception e)
					{
						
					}

					CustomToast.makeText(BillingStartActivity.this, "MR Verification Complete.", Toast.LENGTH_SHORT);					
					//onCreate(new Bundle());
				}
			}
			else if(rValue.indexOf("VM")!=-1) //NACK received 
			{
				mDb.UpdateStatusInStatusMaster("5", "0");  //Billing Closed
				CustomToast.makeText(BillingStartActivity.this, "Version Mismatch", Toast.LENGTH_SHORT);				
				return;
			}	
			else //Others
			{
				mDb.UpdateStatusInStatusMaster("5", "0");  //Billing Closed
				CustomToast.makeText(BillingStartActivity.this, "MR Verification Failed", Toast.LENGTH_SHORT);				
				return;
			}
		}
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false);
			ringProgress.dismiss();
			CustomToast.makeText(BillingStartActivity.this, "MR verification Cancelled", Toast.LENGTH_SHORT);			
		}
	}





}
