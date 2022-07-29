package in.nsoft.hescomspotbilling;

//Nitish 12-06-2017


import java.math.BigDecimal;
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

import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.telephony.TelephonyManager;
import android.text.StaticLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class MCCHelpActivity extends Activity implements AlertInterface {

	Button btnSave;
	Spinner ddlComplaintType;
	EditText editRemarks;
	DatabaseHelper mDb;
	Handler dh;
	DDLAdapter compList;
	private static final String TAG = MCCHelpActivity.class.getName();
	static String complaintid;
	String IMEINumber = "";	
	String SIMNumber = "";	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mcc_help);

		ddlComplaintType = (Spinner)findViewById(R.id.ddlComplaintType);
		editRemarks = (EditText)findViewById(R.id.editRemarks);
		btnSave = (Button)findViewById(R.id.btnSave);		

		TelephonyManager mgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		IMEINumber = mgr.getDeviceId();	
		SIMNumber = mgr.getSimSerialNumber(); 	

		mDb =new DatabaseHelper(this);
		dh = new Handler();
		complaintid = "0";

		try
		{
			compList = mDb.GetHelpDeskComplaints();
			ddlComplaintType.setAdapter(compList);

		}
		catch(Exception e)
		{

		}

		ddlComplaintType.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				DDLItem k = (DDLItem)  arg0.getItemAtPosition(arg2);
				complaintid = k.getId();

			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		btnSave.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{				
				try 
				{				
					if(ddlComplaintType.getSelectedItem().toString().equals("--SELECT--"))
					{
						CustomToast.makeText(MCCHelpActivity.this, "Select Complaint Type", Toast.LENGTH_SHORT);					
						return;
					}
					else if(editRemarks.toString().trim().length()==0)
					{
						CustomToast.makeText(MCCHelpActivity.this, "Enter Remarks", Toast.LENGTH_SHORT);					
						return;
					}


					if((new ConnectionDetector(MCCHelpActivity.this)).isConnectedToInternet())
					{
						DataFetch();
					}
					else//If Data Connection not ON
					{
						CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
						params.setContext(MCCHelpActivity.this);					
						params.setMainHandler(dh);
						params.setMsg("Please check your data connection. If it turned off then turn on and try again. ");
						params.setButtonOkText("OK");
						params.setTitle("Message");
						params.setFunctionality(-1);
						CustomAlert cAlert  = new CustomAlert(params);  
					}
				} 
				catch (Exception e) {
					// TODO Auto-generated catch block
					CustomToast.makeText(MCCHelpActivity.this, "NO DATA", Toast.LENGTH_SHORT);						
					e.printStackTrace();
				}
			}
		});


	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.		
		//getMenuInflater().inflate(R.menu.spotbillingmenu, menu);
		return true;
	}
	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return OptionsMenu.navigate(item, AboutMCCActivity.this);			
	}*/

	public void DataFetch()
	{
		TelephonyManager mgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		IMEINumber = mgr.getDeviceId();
		try
		{
			if((new ConnectionDetector(MCCHelpActivity.this)).isConnectedToInternet())
			{

				final ProgressDialog ringProgress = ProgressDialog.show(MCCHelpActivity.this, "Please wait..", "Saving...",true);//Spinner
				ringProgress.setCancelable(false);
				Thread thMaster = new Thread(new Runnable() 
				{

					@Override
					public void run() {    
						// TODO Auto-generated method stub
						//1 StaffMaster
						try
						{							
							String currentdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
							HttpClient httpclt = new DefaultHttpClient();//object for HttpClient								
							HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/saveEventLog_Android");//Testing
							List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
							lvp.add(new BasicNameValuePair("Flag","2"));
							lvp.add(new BasicNameValuePair("IMEINO", IMEINumber));
							lvp.add(new BasicNameValuePair("SIMNO", SIMNumber));
							lvp.add(new BasicNameValuePair("EventDesc",editRemarks.getText().toString().trim()));
							lvp.add(new BasicNameValuePair("EventID", complaintid));
							lvp.add(new BasicNameValuePair("CapturedDateTime",currentdate));

							httpPost.setEntity((new UrlEncodedFormEntity(lvp)));
							httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");							
							HttpResponse res = httpclt.execute(httpPost);
							HttpEntity ent = res.getEntity();
							if(ent != null)
							{
								//db.UpdateRcptCnt(CollData.substring(1, 8)+"   ",CollData.substring(33,38));
								String rValue = EntityUtils.toString(ent);
								ReadFileServerResponse rd=new ReadFileServerResponse();
								//rd.ReadString(rValue);
								Log.d(TAG, rValue);
								if(rd.ReadString(rValue).equals("ACK"))
								{
									dh.post(new Runnable() {

										@Override
										public void run() {
											CustomToast.makeText(MCCHelpActivity.this, "Complaint Registered.", Toast.LENGTH_SHORT);
											Intent i = new Intent(MCCHelpActivity.this, OtherListActivity.class);//Redirect to Home Page 
											startActivity(i);	
											return;
										}
									});

								}
								else
								{
									dh.post(new Runnable() {

										@Override
										public void run() {
											CustomToast.makeText(MCCHelpActivity.this, "Failure in Saving Data.", Toast.LENGTH_SHORT);											
											Intent i = new Intent(MCCHelpActivity.this, OtherListActivity.class);//Redirect to Home Page 
											startActivity(i);
											return;
										}
									});

								}
							}
						}
						catch (Exception e) {
							Log.d("SendtoServer", e.toString());
							dh.post(new Runnable() {

								@Override
								public void run() {
									CustomToast.makeText(MCCHelpActivity.this, "Failure in Saving Data.", Toast.LENGTH_SHORT);
									return;
								}
							});
							// TODO: handle exception
						}	

						ringProgress.dismiss();


					}
				});//.start();	
				thMaster.start();
			}
			else
			{
				CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
				params.setContext(MCCHelpActivity.this);					
				params.setMainHandler(dh);
				params.setMsg("Please check your data connection. If turned OFF Turn ON and SYNC to get Latest Updated Data. ");
				params.setButtonOkText("OK");
				params.setTitle("Message");
				params.setFunctionality(1);
				CustomAlert cAlert  = new CustomAlert(params);  
			}

		}
		catch(Exception e)
		{
			Toast toast = Toast.makeText(MCCHelpActivity.this, "Error", Toast.LENGTH_SHORT);
			toast.show();
			e.printStackTrace();
		}			




	}

	@Override
	public void performAction(boolean alertResult, int functionality) {
		// TODO Auto-generated method stub

	}
}
