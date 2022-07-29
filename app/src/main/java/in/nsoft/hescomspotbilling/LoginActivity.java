package in.nsoft.hescomspotbilling;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author nsoft.user
 * This activity is for login 
 */
public class LoginActivity extends Activity implements AlertInterface {

	private android.os.Handler handler;//=new android.os.Handler();
	private EditText txtUserName, txtPassword;
	private TextView lblSBBlinkText;
	private Button btnLogin,buttonsync;	
	DatabaseHelper db = new DatabaseHelper(this);
	//Tamilselvan on 28-03-2014
	private static final String TAG = LoginActivity.class.getName();
	static String IMEINumber = "";
	static String SimNumber = "";
	private boolean isInternetConnected = false;
	Thread th;
	static volatile boolean contTh = false;
	static volatile boolean GPSThread = false;
	long diff = 0;

	static  int faceRecog_Count = 0;
	//Nitish 30-06-2014
	static GPSTracker gpsTracker ;
	//Nitish 30-06-2014

	//Nitish 30-12-2016
	File file;
	InputStream ipStream;
	OutputStream myOutput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		handler = new android.os.Handler();
		//Modified Nitish 24-02-2014
		txtUserName = (EditText) findViewById(R.id.txtUserName);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		buttonsync = (Button) findViewById(R.id.buttonsync);		
		TextView lblSBBlinkText = (TextView) findViewById(R.id.lblSBBlinkText);


		buttonsync.setVisibility(View.GONE);
		if(gpsTracker == null)
			gpsTracker = new GPSTracker(LoginActivity.this);

		LoginActivity.faceRecog_Count = 0;
		if(LoginActivity.faceRecog_Count >= 5)
		{			
			Intent intentLogout = new Intent(Intent.ACTION_MAIN);			
			intentLogout.addCategory(Intent.CATEGORY_HOME);			
			intentLogout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intentLogout);			
		}
		btnLogin.setEnabled(false);
		//#################livetest TextView  for blinking  and color#########################
		lblSBBlinkText.setTextColor(Color.parseColor("#FF0000")); //red color
		lblSBBlinkText.setTextSize(14);
		Animation anim=new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(80); //blinkig duration
		anim.setStartOffset(40);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(Animation.INFINITE);
		lblSBBlinkText.startAnimation(anim);
		//#####Live and Test  Validation ########
		if(ConstantClass.IPAddress.equals("http://123.201.131.113:8112/Hescom.asmx") )
		{
			lblSBBlinkText.setText(ConstantClass.mTest+ ConstantClass.FileVersion);
		}
		else
		{
			lblSBBlinkText.setText(ConstantClass.mLive+ ConstantClass.FileVersion);
		}

		btnLogin.setEnabled(false);
		//Master Image Exist Validation  30-06-2015
		/*String root2 = Environment.getExternalStorageDirectory().getPath();			
		root2 =root2+"/MasterImage/"+"FaceImgMaster.jpg";
		if(new File(root2).exists())
		{
			buttonsync.setEnabled(false);
		}
		else
		{
			buttonsync.setEnabled(true);
		}*/


		//###############################################################################################
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
			IMEINumber = CommonFunction.getDeviceNo(LoginActivity.this);
			SimNumber = CommonFunction.getSIMNo(LoginActivity.this);

		}
		catch(Exception e)
		{

		}

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
			}
		}
		//db.UpdateMeter_DetailsTest("1");
		//Tamilselvan on 28-03-2014
		//At First Time Installation 
		SharedPreferences shpre = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isfirstRun = shpre.getBoolean("FirstRun", true);
		if(isfirstRun)//If it True, then first time installation of App.
		{
			InputStream myInput =null;
			try
			{
				db.VerifyUser("", "");
			}
			catch (Exception e)
			{
				Log.d("btnTest", e.toString());
			}
			try
			{
				//If first run, Push database (sqlite) to root(Android os folder) folder of spotbilling
				myInput = this.getAssets().open("TRM_Hescom.dat");					
				OutputStream myOutput = new FileOutputStream("/data/data/in.nsoft.hescomspotbilling/databases/TRM_Hescom.dat");
				OutputStream myOutputBackup = new FileOutputStream("/data/data/in.nsoft.hescomspotbilling/databases/TRM_Hescom_bak.dat");
				if(!new File("/data/data/in.nsoft.hescomspotbilling/databases").exists())
				{
					new File("/data/data/in.nsoft.hescomspotbilling/databases").mkdirs();
				}
				byte[] buffer = new byte[1024];
				int length;
				while((length = myInput.read(buffer))>0)
				{
					myOutput.write(buffer, 0, length);
					myOutputBackup.write(buffer, 0, length);
				}							
				myOutput.flush();
				myOutput.close();
				myOutputBackup.flush();
				myOutputBackup.close();
				myInput.close();/**/
				//Toast.makeText(this, "First Run", Toast.LENGTH_SHORT).show();
				SharedPreferences.Editor edt = shpre.edit();
				edt.putBoolean("FirstRun", false);
				edt.commit();				
				db.CreateIfNotExistsConfigurationTable(); //29-07-2016
				//30-12-2016
				try
				{
					db.EventLogSave("16", LoginActivity.IMEINumber, LoginActivity.SimNumber, "APP INSTALLED"); 					

				}
				catch(Exception e)
				{
					Log.d(TAG, e.toString());
				}
				VerifyIMEI_SIMNo();//Verifying IMEI and SIM No.
			}
			catch (Exception e)
			{
				Log.d(TAG, e.toString());
			}
			finally
			{
				if(myInput != null)
				{
					try {
						myInput.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}/**/
				}
			}
		}
		else//Other Not First Time 
		{
			if(ConstantClass.IPAddress.equals("http://123.201.131.113:8112/Hescom.asmx") )//Testing
			{
				db.UpdateStatusInStatusMaster("3", "1");
				db.UpdateStatusInStatusMaster("16", "1");
			}
			db.CreateIfNotExistsConfigurationTable(); //28-05-2016
			int rtrValue = db.GetStatusMasterDetailsByID("16");//get status where statusid = 16 from StatusMaster
			if(rtrValue == 1)//if equal to 1, IMEI number already verified 
			{
				btnLogin.setEnabled(true);
			}
			else if(rtrValue == 0)//if equal to 0, IMEI number need to check again
			{
				btnLogin.setEnabled(true);
				VerifyIMEI_SIMNo();//Verifying IMEI and SIM No.
			}
		}
		try
		{
			int rtrValue = db.GetStatusMasterDetailsByID("3");
			if(rtrValue == 0)
			{
				new TimeSync().execute();//call Time Synchronisation Asynchronous Task
			}
			else
			{
				CollectionObject.GetCollectionObject().setmIsTimeSync(true);	
				TimeChangedReceiver.setValid(true);	
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}/**/

		/*if(IMEINumber.equals(ConstantClass.imeinos[0]))
		{
			btnLogin.setEnabled(true);
		}
		else
		{
			CustomToast.makeText(LoginActivity.this, "Your IMEI number is not authorized to use this app.", Toast.LENGTH_LONG);
			btnLogin.setEnabled(false);
		}*/
		//THREAD for Sending Data to Server and Sending Time ======================================================================
		//Nitish 30-06-2014	
		/*if(!GPSThread)
			GPRS_Sync();*/
		//Nitish 30-06-2014
		if(!contTh)
			threadforSendtoserver();

		//30-12-2016
		try
		{
			db.EventLogSave("1", LoginActivity.IMEINumber, LoginActivity.SimNumber, "Login succussful"); //15-07-2016
			db.EventLogSave("14",LoginActivity.IMEINumber, LoginActivity.SimNumber, "Mobile Battery Percentage: " + BatteryLevel() + "%"); //15-07-2016
			downloadDB();
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		Intent i = new Intent(LoginActivity.this, HomePage.class);//Redirect to Pair Bluetooth
		startActivity(i);

		//END THREAD for Sending Data to Server and Sending Time ==================================================================
		//Nitish 16-01-2015
		buttonsync.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0)
			{ 
				Intent i = new Intent(LoginActivity.this, CameraActivityFacialMaster.class);
				startActivity(i);
			}
		});


		/**
		 * code for login
		 */
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				//Nitish 25-01-2016
				/*boolean mobileDataEnabled = android.provider.Settings.Secure.getInt(getContentResolver(),"mobile_data",1) == 1;
				if(!mobileDataEnabled)
				{
					CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
					params.setContext(LoginActivity.this);					
					params.setMainHandler(handler);
					params.setMsg("Please Check Your Mobile Data Connection. If it is turned off then turn on and try again. ");
					params.setButtonOkText("OK");
					params.setTitle("Message");
					params.setFunctionality(-1);
					CustomAlert cAlert  = new CustomAlert(params);
				}

				else*/
				{

					String userName= txtUserName.getText().toString();
					String password =txtPassword.getText().toString();
					//punit 24022014
					if(userName.equals(""))
					{
						CustomToast.makeText(LoginActivity.this, "Enter User Name", Toast.LENGTH_SHORT);					
						return;
					}
					else if (password.equals(""))
					{
						CustomToast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT);					
						return;
					}
					try
					{
						//Modified by Tamilselvan on 29-03-2014
						int rtrValue = db.GetStatusMasterDetailsByID("16");//get status where statusid = 16 from StatusMaster
						if(rtrValue == 1)//if equal to 1, IMEI number already verified 
						{
							boolean result = db.VerifyUser(userName, password);//verify Username, Password in DB
							if(result == true)//IF True, then go to home page
							{
								//30-07-2016
								try
								{
									db.EventLogSave("1", LoginActivity.IMEINumber, LoginActivity.SimNumber, "Login succussful"); //15-07-2016
									db.EventLogSave("14",LoginActivity.IMEINumber, LoginActivity.SimNumber, "Mobile Battery Percentage: " + BatteryLevel() + "%"); //15-07-2016
								}
								catch(Exception e)
								{
									Log.d(TAG, e.toString());
								}
								//Intent i = new Intent(LoginActivity.this, HomePage.class);//Redirect to Home Page 
								Intent i = new Intent(LoginActivity.this, BluethoothDiscoveryActivity.class);//Redirect to Pair Bluetooth
								//Intent i = new Intent(LoginActivity.this, CameraActivityFacial.class);
								startActivity(i);

							}
							else//if false, Invalid Username and/or Password
							{
								CustomToast.makeText(LoginActivity.this, "Invalid Username and/or Password", Toast.LENGTH_SHORT);					
							}
							btnLogin.setEnabled(true);
						}
						else if(rtrValue == 0)//if equal to 0, IMEI number need to check again
						{
							VerifyIMEI_SIMNo();//Verifying IMEI and SIM No.
						}
					} 
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						CustomToast.makeText(LoginActivity.this, "error Occured", Toast.LENGTH_SHORT);
						e.printStackTrace();
					}
				}
			}
		});		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return OptionsMenu.navigate(item, LoginActivity.this);			
	}

	//Tamilselvan on 13-03-2014
	//Code for closing app
	@Override	
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);			
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);			
		return;
	}/**/

	/**
	 * Tamilselvan on 29-03-2014
	 * verification of IMEI number and SIM serial number
	 */
	public void VerifyIMEI_SIMNo()
	{
		if(isInternetConnected)
		{
			//Sent To Server for IMEI Verification=====================================================================================
			final ProgressDialog ringProgress = ProgressDialog.show(LoginActivity.this, "Please wait..", "Verifying IMEI...",true);//Spinner
			ringProgress.setCancelable(false);
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try
					{
						//Thread.sleep(10000);
						HttpClient httpclt = new DefaultHttpClient();//object for HttpClient								
						HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Sbm_IMEI_Sim_Number");//HttpPost method uri

						List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
						lvp.add(new BasicNameValuePair("IMEINumber", IMEINumber));//INEI
						lvp.add(new BasicNameValuePair("SimNumber", SimNumber));//SIM Serial No
						httpPost.setEntity((new UrlEncodedFormEntity(lvp)));
						httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");							
						HttpResponse res = httpclt.execute(httpPost);//execute post method
						HttpResponse k = res;
						HttpEntity ent = k.getEntity();
						if(ent != null)/**/
							//String Imei = "353743052178157";
							//if(IMEINumber.equals(Imei))
						{
							String rValue = EntityUtils.toString(ent);//"ACK";//
							Log.d(TAG, rValue);
							if(rValue.equals("ACK"))//ACK received then update flag 1
							{
								db.UpdateStatusInStatusMaster("16", "1");//Update status = 1 Where StatusId = 16 in Table StatusMaster
								btnLogin.setEnabled(true);
							}
							else//NACK received then update flag 0
							{
								db.UpdateStatusInStatusMaster("16", "0");//Update status = 0 Where StatusId = 16 in Table StatusMaster
								btnLogin.setEnabled(false);
								//Show AlertDialog====================================================================================
								handler.post(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										//CustomToast.makeText(LoginActivity.this, "Your Mobile No. not registered in the server. Please register IMEI No. in the server.", Toast.LENGTH_LONG);
										CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
										params.setContext(LoginActivity.this);					
										params.setMainHandler(handler);
										params.setMsg("Your IMEI number is not registered in the server. To know IMEI number dial *#06# . Please register in staff master.");
										params.setButtonOkText("OK");
										//params.setButtonCancelText(btnAlertDetails);
										params.setTitle("Message");
										params.setFunctionality(-1);
										CustomAlert cAlert  = new CustomAlert(params);	
									}
								});
								//ENS Show AlertDialog================================================================================
							}
						}/**/
					}
					catch(Exception e)
					{
						Log.d(TAG, e.toString());
					}
					ringProgress.dismiss();
				}
			}).start();

			//END Sent To Server for IMEI Verification================================================================================
			Log.d(TAG, IMEINumber+SimNumber);
		}
		else//If Data Connection not ON
		{
			CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
			params.setContext(LoginActivity.this);					
			params.setMainHandler(handler);
			params.setMsg("Please check your data connection. If it turned off then turn on and try again. ");
			params.setButtonOkText("OK");
			params.setTitle("Message");
			params.setFunctionality(-1);
			CustomAlert cAlert  = new CustomAlert(params);
		}
	}
	@Override
	public void performAction(boolean alertResult, int functionality) {
		// TODO Auto-generated method stub

	}
	//Nitish 04-07-2014
	/*public void GPRS_Sync()
	{
		final Context ctx = LoginActivity.this;
		try
		{	
			gpsTracker = new GPSTracker(LoginActivity.this);

			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try
					{
						GPSThread = true;
						while(true)//While Loop start==============================================
						{
							if(db.GetStatusMasterDetailsByID("7") == 1)//if not equal to 1, SBM File is not Loaded	 
							{
								if(!LoginActivity.gpsTracker.isGPSConnected() || LoginActivity.gpsTracker.latitude == 0 )  //If GPS Location not obtained
								{									
									Thread.sleep(60000);//1 min
								}
								else  //On Getting GPS Information
								{
									try
									{
										String currenthour = new SimpleDateFormat("HH").format(Calendar.getInstance().getTime()); //Get Current Hour
										int hour = Integer.parseInt(currenthour);
										if(hour > 5 && hour < 20) //6:00 to 19:00 
										{
											String currentdate = new SimpleDateFormat("ddMMyyyyHHmm").format(Calendar.getInstance().getTime());
											GPSDetailsObject Gp = new GPSDetailsObject();
											Gp.setmIMEINo(IMEINumber);
											Gp.setmSIMNo(SimNumber);
											Gp.setmDateTime(currentdate);
											Gp.setmMRID(db.GetMRNameFromSBMBillCollData().trim());
											Gp.setmLatitude(String.valueOf(LoginActivity.gpsTracker.latitude));
											Gp.setmLongitude(String.valueOf(LoginActivity.gpsTracker.longitude));
											Gp.setmLocationCode(db.GetLocationCode().trim());
											int result = db.GPSDetailsSave(Gp);
											if(result == 1)
											{
												Thread.sleep(300000);//Send Every 5min-
											}
										}
										else if(hour > 20) //
										{
											//check any rows is there are not
											if(db.GetCountforGPSDetails() > 0)
											{
												if(db.GetCountforGPSDetails() == db.GetCountGPSDetailsSent())//If all LatLongData Sent
												{
													try
													{
														db.DropCreateGPSTable(); //Drop and Recreate GPS Table
													}
													catch(Exception e)
													{
														Log.d(TAG, e.toString());
													}				
												}
											}

										}
									}
									catch(Exception e)
									{
										Thread.sleep(60000);//1 min
										Log.d(TAG, e.toString());
									}
								}
							}
							else
							{
								Thread.sleep(60000);//1 min
							}
						}//While Loop END==========================================================================================================
						//1000 ->1s  10000->10s  60000 -> 1min   600000 ->10min
					}
					catch(Exception e)
					{
						Log.d(TAG, e.toString());
					}
				}
			}).start();

		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}*/
	//End Nitish 04-07-2014
	/**
	 * Thread for sending billed data to server
	 */
	public void threadforSendtoserver()
	{
		final Context ctx = LoginActivity.this;
		try
		{
			if(contTh)
			{
				return;
			}
			th = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try
					{
						contTh = true;
						DatabaseHelper db = new DatabaseHelper(ctx);
						th.setName(String.valueOf(R.string.ThreadName));
						//while( new ConnectionDetector(ctx).isConnectedToInternet())
						/*if(new ConnectionDetector(ctx).isConnectedToInternet())
						{*/
						while(true)
						{
							if(new ConnectionDetector(ctx).isConnectedToInternet())
							{
								//Billed Connection send to server.====================================================================
								ArrayList<String> alStr = db.GetDataSendToServer();
								for(String str :alStr)
								{
									try
									{
										String BillingData = str;//get string
										HttpClient httpclt = new DefaultHttpClient();//object for HttpClient

										//Photo Capture Added 01-04-2021
										String ImgName = db.GetPhotoNameByConnectionNo(BillingData.substring(14, 24));


										HttpPost httpPostImage = new HttpPost(ConstantClass.IPAddress + "/Hescom_Photo_Transfer");
										List<NameValuePair> lvpImage = new ArrayList<NameValuePair>(1);
										lvpImage.add(new BasicNameValuePair("photo", CommonFunction.getPhotoStream(ImgName)));
										lvpImage.add(new BasicNameValuePair("FileName", ImgName));
										httpPostImage.setEntity((new UrlEncodedFormEntity(lvpImage)));
										httpPostImage.setHeader("Content-Type","application/x-www-form-urlencoded");	
										//httpPostImage.setParams(httpParams);
										HttpResponse resImg = httpclt.execute(httpPostImage);
										HttpEntity entImg = resImg.getEntity();
										if(entImg != null)
										{
											String rtrImgValue = EntityUtils.toString(entImg);
											if(rtrImgValue.equals("ACK")||rtrImgValue.equals("NACK")) //ACK->WITHCAMERA  NACK->WITHOUTCAMERA
											{												
												//Sending billing data to server 
												HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Hescom_GPRS_Data_Android");
												List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
												lvp.add(new BasicNameValuePair("BillingData", BillingData));
												httpPost.setEntity((new UrlEncodedFormEntity(lvp)));
												httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");							
												HttpResponse res = httpclt.execute(httpPost);
												HttpEntity ent = res.getEntity();
												if(ent != null)
												{
													String rValue = EntityUtils.toString(ent);
													Log.d(TAG, rValue);
													if(rValue.equals("ACK"))
													{

														String value = db.UpdateBlCnt(BillingData.substring(14, 24));
														String[] splitvalue = value.split("/");
														if(!splitvalue[0].equals(splitvalue[1]))
														{
															db.UpdateStatusMasterDetailsByID("10", "1", value);
														}
														else if(splitvalue[0].equals(splitvalue[1])) 
														{
															db.UpdateStatusMasterDetailsByID("10", "0", value);
														}
													}
													else
													{
														try
														{
															db.UpdateGPRSStausBilling(BillingData.substring(14, 24), rValue);
														}
														catch(Exception e)
														{
															Log.d(TAG, e.toString());
														}
													}
												}//End of Sending billing data to server 
											}
										}
										//End Sending MtrPhoto to server
									}
									catch(Exception e)
									{
										Log.d(TAG, e.toString());
									}
								}//END Billed Connection send to server.================================================================

								//Collection Data send to Server========================================================================
								ArrayList<String> alStrColl = db.GetDataSendToServerColl();
								for(String str :alStrColl) 
								{
									try
									{
										String CollData = str;//get string
										HttpClient httpclt = new DefaultHttpClient();//object for HttpClient								
										HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Hescom_GPRS_ReceiptData_Android");//Testing
										List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
										lvp.add(new BasicNameValuePair("ReceiptData", CollData));
										httpPost.setEntity((new UrlEncodedFormEntity(lvp)));
										httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");							
										HttpResponse res = httpclt.execute(httpPost);
										HttpEntity ent = res.getEntity();
										if(ent != null)
										{
											//db.UpdateRcptCnt(CollData.substring(1, 8)+"   ",CollData.substring(33,38));
											String rValue = EntityUtils.toString(ent);
											Log.d(TAG, rValue);
											if(rValue.equals("ACK"))
											{
												//Nitish 08-05-2014
												CommonFunction cf = new CommonFunction();
												//Add Spaces towards right to ConnectionNo so that its length is 10
												String updateConnId = cf.addSpaceRight(CollData.substring(1, 8).trim() ,10 - CollData.substring(1, 8).trim().length() , ' ');
												String value = db.UpdateRcptCnt(updateConnId ,CollData.substring(40, 45));// ConnectionNo and ReceiptNo are parameters												
												String[] splitvalue = value.split("/");
												if(!splitvalue[0].equals(splitvalue[1])) //If GPRS not sent
												{
													db.UpdateStatusMasterDetailsByID("13", "1", value);
												}
												else if(splitvalue[0].equals(splitvalue[1])) //If all Collection GPRS sent
												{
													db.UpdateStatusMasterDetailsByID("13", "0", value);
												}
											}
											else
											{
												try
												{
													CommonFunction cf = new CommonFunction();
													//Add Spaces towards right to ConnectionNo so that its length is 10
													String updateConnId = cf.addSpaceRight(CollData.substring(1, 8).trim() ,10 - CollData.substring(1, 8).trim().length() , ' ');
													db.UpdateGPRSStausColl(updateConnId,CollData.substring(40, 45), rValue); // ConnectionNo,ReceiptNo,Status as parameters
												}
												catch(Exception e)
												{
													Log.d(TAG, e.toString());
												}
											}

										}
									}									
									catch(Exception e)
									{
										Log.d(TAG, e.toString());
									}
								}//END Collection Data send to Server====================================================================


								/*ArrayList<String> alStrGPS = db.GetGPSDataSendToServer();
								for(String str : alStrGPS)
								{												
									try
									{
										String GPSData = str;//get string
										HttpClient httpclt = new DefaultHttpClient();//object for HttpClient								
										HttpPost httpPost = new HttpPost(ConstantClass.IPAddress +"/Identify_LatLong");//Testing
										List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
										lvp.add(new BasicNameValuePair("LatLongData", GPSData));
										httpPost.setEntity((new UrlEncodedFormEntity(lvp)));
										httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");							
										HttpResponse res = httpclt.execute(httpPost);
										HttpEntity ent = res.getEntity();
										if(ent != null)
										{
											//db.UpdateRcptCnt(CollData.substring(1, 8)+"   ",CollData.substring(33,38));
											String rValue = EntityUtils.toString(ent);
											Log.d(TAG, rValue);
											if(rValue.equals("ACK"))
											{															
												db.UpdateGPSDetails(GPSData.substring(35, 47));//Send DateTime as parameter
											}
										}
									}
									catch(Exception e)
									{
										Log.d(TAG, e.toString());
									}

									//db.UpdateGPSDetails(str.substring(35, 49));//Send DateTime as parameter
								}*/
								//FaceDetection Data Send To Server
								/*ArrayList<String> alStrFac = db.GetDataSendToServerFaceRecognition();
								for(String str :alStrFac) 
								{
									try
									{
										String FaceData[] = str.split("##");//get string
										//FaceData[0] = Id,FaceData[1] = CreatedDate,FaceData[2] = PhotoName
										HttpClient httpclt = new DefaultHttpClient();//object for HttpClient	
										HttpPost httpPostImage = new HttpPost(ConstantClass.IPAddress + "/Hescom_Photo_Transfer");
										List<NameValuePair> lvpImage = new ArrayList<NameValuePair>(1);
										lvpImage.add(new BasicNameValuePair("photo", CommonFunction.getPhotoStreamFaceRecognition(FaceData[1].toString(),FaceData[2].toString()))); //CreatedDate and PhotoName
										lvpImage.add(new BasicNameValuePair("FileName", FaceData[2].toString())); //Photoname

										httpPostImage.setEntity((new UrlEncodedFormEntity(lvpImage)));
										httpPostImage.setHeader("Content-Type","application/x-www-form-urlencoded");	
										//httpPostImage.setParams(httpParams);
										HttpResponse resImg = httpclt.execute(httpPostImage);
										HttpEntity entImg = resImg.getEntity();
										if(entImg != null)
										{
											String rtrImgValue = EntityUtils.toString(entImg);
											if(rtrImgValue.equals("ACK"))
											{
												db.UpdateGPRSFlagFaceRecognition(FaceData[0].toString());//Send id as parameter
											}

										}
									}

									catch(Exception e)
									{
										Log.d(TAG, e.toString());
									}
								}*/
								//END FaceDetection Data send to Server====================================================================
								//CollChqImage
								ArrayList<String> alStrChqDdImg = db.GetDataSendToServerChqImg();
								for(String str :alStrChqDdImg) 
								{
									try
									{
										String ChqData[] = str.split("##");//get string
										//DisData[0] = Id,DisData[1] = CreatedDate,DisData[2] = PhotoName
										HttpClient httpclt = new DefaultHttpClient();//object for HttpClient	
										HttpPost httpPostImage = new HttpPost(ConstantClass.IPAddress + "/Android_Photo_Transfer");
										List<NameValuePair> lvpImage = new ArrayList<NameValuePair>(1);
										lvpImage.add(new BasicNameValuePair("photo", CommonFunction.getPhotoStreamDisConnectionCheque(ChqData[1].toString(),ChqData[2].toString()))); //CreatedDate and PhotoName
										lvpImage.add(new BasicNameValuePair("FileName", ChqData[2].toString())); //Photoname
										httpPostImage.setEntity((new UrlEncodedFormEntity(lvpImage)));
										httpPostImage.setHeader("Content-Type","application/x-www-form-urlencoded");	
										//httpPostImage.setParams(httpParams);
										HttpResponse resImg = httpclt.execute(httpPostImage);
										HttpEntity entImg = resImg.getEntity();
										if(entImg != null)
										{
											String rtrImgValue = EntityUtils.toString(entImg);
											if(rtrImgValue.equals("ACK"))
											{
												db.UpdateGPRSFlagChqImg(ChqData[0].toString());//Send id as parameter
											}

										}
									}

									catch(Exception e)
									{
										Log.d(TAG, e.toString());
									}
								}
								/**///END Collection Image send to Server====================================================================


								//start EventLog Data send to server 
								ArrayList<String> alStrColeventlog=db.getEventLogstatus();
								for(String str :alStrColeventlog)
								{									
									try
									{
										String sData[] = str.split("##");//get string
										HttpClient httpclt = new DefaultHttpClient();//object for HttpClient								
										HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/saveEventLog_Android");//Testing
										List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
										lvp.add(new BasicNameValuePair("Flag","1"));
										lvp.add(new BasicNameValuePair("IMEINO", sData[1].toString()));
										lvp.add(new BasicNameValuePair("SIMNO", sData[2].toString()));
										lvp.add(new BasicNameValuePair("EventDesc",sData[3].toString()));
										lvp.add(new BasicNameValuePair("EventID", sData[4].toString()));
										lvp.add(new BasicNameValuePair("CapturedDateTime",sData[5].toString()));

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
												db.UpdateEventLogStatus(sData[0].toString());
											}
											else
											{

											}
										}
									}									
									catch(Exception e)
									{
										Log.d(TAG, e.toString());
									}
								}
								//start EventLog Data send to server 
								ArrayList<LandTObject> alMet=db.getMeterTypedata();
								for(LandTObject ltObj :alMet)
								{									
									try
									{

										HttpClient httpclt = new DefaultHttpClient();//object for HttpClient								
										HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/android_OpticalProbeData");//Testing
										List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
										lvp.add(new BasicNameValuePair("Flag","1"));
										lvp.add(new BasicNameValuePair("IMEINO", LoginActivity.IMEINumber));
										lvp.add(new BasicNameValuePair("ConnectionId", ltObj.getmConnectionNo().trim()));
										lvp.add(new BasicNameValuePair("ForMonth",ltObj.getmForMonth()));
										lvp.add(new BasicNameValuePair("CapturedDate", ltObj.getmCreatedDate()));
										lvp.add(new BasicNameValuePair("MeterSerialNumber", ltObj.getmMeterSerialNo()));
										lvp.add(new BasicNameValuePair("MeterReading", ltObj.getmCummulativeEnergyKVAH()));
										lvp.add(new BasicNameValuePair("MD", ltObj.getmTarifWiseMD()));
										lvp.add(new BasicNameValuePair("PF", ltObj.getmPF()));
										lvp.add(new BasicNameValuePair("MeterType",ltObj.getmMeterType()));
										lvp.add(new BasicNameValuePair("Param1",ConstantClass.AppVersion));
										lvp.add(new BasicNameValuePair("Param2","0"));
										lvp.add(new BasicNameValuePair("Param3","0"));

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
											if(rValue.equals("ACK"))
											{
												db.UpdateMeter_Details(String.valueOf(ltObj.getmUID()));
											}
											else
											{

											}
										}
									}									
									catch(Exception e)
									{
										Log.d(TAG, e.toString());
									}
								}
								//Disconnection Data send to Server========================================================================
								ArrayList<String> alStrDis = db.GetDataSentToServerDisconnection();
								for(String str :alStrDis) 
								{
									try
									{
										String DisData = str;//get string
										HttpClient httpclt = new DefaultHttpClient();//object for HttpClient								
										HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Disconnection");//Testing
										List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
										lvp.add(new BasicNameValuePair("Dis_Data", DisData));
										httpPost.setEntity((new UrlEncodedFormEntity(lvp)));
										httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");							
										HttpResponse res = httpclt.execute(httpPost);
										HttpEntity ent = res.getEntity();
										if(ent != null)
										{
											//db.UpdateRcptCnt(CollData.substring(1, 8)+"   ",CollData.substring(33,38));
											String rValue = EntityUtils.toString(ent);											
											if(rValue.equals("ACK"))
											{												
												db.UpdateGPRSFlagDisconnection(DisData.substring(12, 22).trim(),rValue);//Send ConnectionNo as parameter
											}
											else
											{
												try
												{													
													db.UpdateGPRSStausDisconnection(DisData.substring(12, 22), rValue); // ConnectionNo,Status as parameters
												}
												catch(Exception e)
												{
													Log.d(TAG, e.toString());
												}
											}

										}
									}									
									catch(Exception e)
									{
										Log.d(TAG, e.toString());
									}
								}/**///END Disconnection Data send to Server====================================================================
								//Disconnection Image send to Server========================================================================
								ArrayList<String> alStrDisConnImg = db.GetDataSendToServerDisConnImg();
								for(String str :alStrDisConnImg) 
								{
									try
									{
										String DisData[] = str.split("##");//get string
										//DisData[0] = Id,DisData[1] = CreatedDate,DisData[2] = PhotoName
										HttpClient httpclt = new DefaultHttpClient();//object for HttpClient	
										HttpPost httpPostImage = new HttpPost(ConstantClass.IPAddress + "/Hescom_Photo_Transfer");
										List<NameValuePair> lvpImage = new ArrayList<NameValuePair>(1);
										lvpImage.add(new BasicNameValuePair("photo", CommonFunction.getPhotoStreamDisConnectionCheque(DisData[1].toString(),DisData[2].toString()))); //CreatedDate and PhotoName
										lvpImage.add(new BasicNameValuePair("FileName", DisData[2].toString())); //Photoname
										httpPostImage.setEntity((new UrlEncodedFormEntity(lvpImage)));
										httpPostImage.setHeader("Content-Type","application/x-www-form-urlencoded");	
										//httpPostImage.setParams(httpParams);
										HttpResponse resImg = httpclt.execute(httpPostImage);
										HttpEntity entImg = resImg.getEntity();
										if(entImg != null)
										{
											String rtrImgValue = EntityUtils.toString(entImg);
											if(rtrImgValue.equals("ACK"))
											{
												db.UpdateGPRSFlagDisConnImg(DisData[0].toString());//Send id as parameter
											}

										}
									}

									catch(Exception e)
									{
										Log.d(TAG, e.toString());
									}
								}
								
								//Colection Pay status/latLong New - 11-05-2021
								ArrayList<String> alStrColPayStatus=db.getCollectionPaymentStatusNew();
								for(String str :alStrColPayStatus)
								{
									try
									{
										String sData[] = str.split("##");//get string
										HttpClient httpclt = new DefaultHttpClient();//object for HttpClient								
										HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Hescom_CollectionPaymentStatus");//Testing
										List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
										lvp.add(new BasicNameValuePair("IMEINo", sData[0].toString()));
										lvp.add(new BasicNameValuePair("Batch_No", sData[1].toString()));
										lvp.add(new BasicNameValuePair("ConnectionNo",sData[2].toString()));
										lvp.add(new BasicNameValuePair("Lat", sData[3].toString()));
										lvp.add(new BasicNameValuePair("Long", sData[4].toString()));
										lvp.add(new BasicNameValuePair("RemarkId", sData[5].toString()));
										lvp.add(new BasicNameValuePair("ScheduledDate", sData[6].toString()));
										lvp.add(new BasicNameValuePair("GeneratedDate", sData[7].toString()));
										httpPost.setEntity((new UrlEncodedFormEntity(lvp)));
										httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");							
										HttpResponse res = httpclt.execute(httpPost);
										HttpEntity ent = res.getEntity();
										if(ent != null)
										{
											//db.UpdateRcptCnt(CollData.substring(1, 8)+"   ",CollData.substring(33,38));
											String rValue = EntityUtils.toString(ent);
											Log.d(TAG, rValue);
											if(rValue.equals("ACK"))
											{
												db.UpdateCollLatLong(sData[2].toString() ,sData[1].toString());
											}
											else
											{

											}
										}
									}									
									catch(Exception e)
									{
										Log.d(TAG, e.toString());
									}
								}
								//End Colection Pay status/latLong New	
								
								Thread.sleep(30000);
							}
							else
							{
								Thread.sleep(30000);
							}
						}
						/*else
						{
							Thread.sleep(30000);
						}*/

					}
					catch(Exception e)
					{
						Log.d(TAG, e.toString());
					}
				}
			});
			th.setPriority(Thread.NORM_PRIORITY);
			th.start();

		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	private class TimeSync extends AsyncTask<Void, Void, Void>  //GenericTypes AsyncTask<parametertype, progressparameter, postexecuteparam >
	{
		final ProgressDialog ringProgress = ProgressDialog.show(LoginActivity.this, "Please wait..", "Time Sync...",true);
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
				CustomToast.makeText(LoginActivity.this, "Error in Time Synchronisation", Toast.LENGTH_SHORT);					
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
						CustomToast.makeText(LoginActivity.this,"Mobile Date Time not matching with Server."  , Toast.LENGTH_SHORT);
						TimeChangedReceiver.setValid(false);
						btnLogin.setEnabled(false);
						db.UpdateStatusInStatusMaster("3", "0");
					}
					else //On Time Sync Success
					{

						CollectionObject.GetCollectionObject().setmIsTimeSync(true);	
						TimeChangedReceiver.setValid(true);					
						btnLogin.setEnabled(true);
						db.UpdateStatusInStatusMaster("3", "1");
					}
				} 
				catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else//NACK received or any Error 
			{
				CustomToast.makeText(LoginActivity.this, "Error Occured", Toast.LENGTH_SHORT);
			}		
		}	
		//If Exception Occured 
		@Override
		protected void onCancelled() 
		{
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false);
			ringProgress.dismiss();
			CustomToast.makeText(LoginActivity.this, "Time Synchronisation Failed.", Toast.LENGTH_SHORT);			
		}
	}
	//27-07-2016
	public String BatteryLevel()
	{
		float battery = 0;
		try
		{
			Intent batteryLevel = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
			float level = batteryLevel.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			float scale = batteryLevel.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			if(level == -1 || scale == -1)
			{
				battery = 50.0f;
			}
			else
			{
				battery = (level/scale)* 100.0f;
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		String Level = String.valueOf(new BigDecimal(battery).setScale(0, BigDecimal.ROUND_HALF_EVEN));
		return Level;
	}
	public void downloadDB()
	{
		try
		{
			//Pull Out DataBase from Device
			file = new File("/data/data/in.nsoft.hescomspotbilling/databases/TRM_Hescom.dat");//Get DB Path				
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try
					{

						String root = Environment.getExternalStorageDirectory().getPath();//Get External Storage Path 
						String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());					
						File filepath;
						String filePh = root+"/HescomSpotBilling";
						filepath = new File(filePh);
						if(!filepath.exists())
						{
							filepath.mkdir();
						}//Not Exists Create Directory						
						if(filepath.exists())//If Directory Exists
						{
							ipStream = new FileInputStream(file);//get Input Stream of DataBase Path
							myOutput = new FileOutputStream(filePh+"/Hescom_Log.dat");//Get OutputStream of New Directory Path					
							byte[] buffer = new byte[1024];//Variable
							int length;//Variable


							while((length = ipStream.read(buffer))>0)
							{
								myOutput.write(buffer, 0, length);//write byte by byte to OutputStream Directory

							}	

						}/**/								
					}
					catch(Exception e)
					{
						Log.d(TAG, e.toString());
					}
					finally
					{
						try {
							myOutput.flush();
							myOutput.close();//Close OutputStream
							ipStream.close();//Close InputStream
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}//flush OutputStream
					}					
				}
			}).start();

		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}	
	}
}

