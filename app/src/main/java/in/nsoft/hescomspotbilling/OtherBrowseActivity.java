
//Created Nitish 26-02-2014
package in.nsoft.hescomspotbilling;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class OtherBrowseActivity extends Activity implements AlertInterface{

	private Button btnBrowse, btnRead;	
	private EditText txtBrowse;		
	private static final String TAG = OtherBrowseActivity.class.getName();
	Handler mainThreadHandler;	
	DatabaseHelper db = new DatabaseHelper(this);
	String ReceiveStr = "";
	//Added on 06-03-2014
	ProgressBar pbReadStatus;
	TextView lblProgressBarStatus;
	File file = null;
	String eol;
	private long contentLength = 0, totalLength = 0;//Added by Tamilselvan on 01-04-2014
	InputStream ipStream;
	OutputStream myOutput;
	int count = 0, Total = 0, p = 0, progressTotal = 0;
	ArrayList<WriteFileParameters> alwfp;
	StringBuilder sbWrite, fileWrite;
	boolean isReadRunning = false;
	boolean isMonthMismatch = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_browse);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		eol = System.getProperty("line.separator");
		mainThreadHandler = new Handler();
		btnBrowse = (Button)findViewById(R.id.btnOtherBrowse);
		btnRead = (Button)findViewById(R.id.btnOtherBrowseRead);//Added by Tamilselvan on 01-03-2014
		txtBrowse = (EditText)findViewById(R.id.txtOtherBrowse);
		txtBrowse.setEnabled(false);

		pbReadStatus = (ProgressBar)findViewById(R.id.progressBarOtherBrowserFileLoad);//Added on 06-03-2014
		lblProgressBarStatus = (TextView)findViewById(R.id.lblProgressBarOtherBrowserStatus);//Added on 06-03-2014
		pbReadStatus.setVisibility(ProgressBar.INVISIBLE);
		lblProgressBarStatus.setVisibility(ProgressBar.INVISIBLE);
		sbWrite = new StringBuilder();
		fileWrite = new StringBuilder();
		alwfp = new ArrayList<WriteFileParameters>();

		if(txtBrowse.toString()!= "")
		{
			Intent newintent = getIntent();
			String val = newintent.getStringExtra("Key");
			txtBrowse.setText(val);
		}
		else
		{
			txtBrowse.setText("Browse");
		}	

		//Browse Button onClickListener==================================================================
		btnBrowse.setOnClickListener(new OnClickListener() { 

			@Override
			public void onClick(View arg0) 
			{				
				Intent i = new Intent(OtherBrowseActivity.this, OtherBrowseListActivity.class);//Browse Window
				startActivity(i);
			}
		});		
		//END Browse Button onClickListener==============================================================

		//Created By Tamilselvan on 01-03-2014
		btnRead.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(txtBrowse.getText().toString().equals(""))
				{
					CustomToast.makeText(OtherBrowseActivity.this, "Browse file to read.", Toast.LENGTH_SHORT);
					return;
				}
				if(txtBrowse.getText().toString().indexOf(".ToSbmEnc") != -1)
				{
					if(!txtBrowse.getText().toString().substring(txtBrowse.getText().toString().length() - 3, txtBrowse.getText().toString().length()).equals("Enc"))
					{
						CustomToast.makeText(OtherBrowseActivity.this, "File extension not supported.", Toast.LENGTH_SHORT);
						txtBrowse.setText("");
						return;
					}
				}
				else
				{
					CustomToast.makeText(OtherBrowseActivity.this, "File extension not supported.", Toast.LENGTH_SHORT);
					txtBrowse.setText("");
					return;
				}
				//Check DataBase is Empty or not and if not empty check any connection billed or not
				if(db.isAnyConnectionBilledOrEmpty())
				{
					int getVal= db.CheckFlagToLoadFile();
					if(getVal == 3)
					{
						CustomToast.makeText(OtherBrowseActivity.this, "Download the File and then Load new File.", Toast.LENGTH_SHORT);
						//txtBrowse.setText("");
						return;
					}
					else if(getVal == 4)
					{
						CustomToast.makeText(OtherBrowseActivity.this, "Check GPRS Data whether every billed data sent to server or not.", Toast.LENGTH_SHORT);
						//txtBrowse.setText("");
						return;
					}
				}
				//Added on 06-03-2014

				//Nitish 03-05-2014
				if(db.GetStatusMasterDetailsByID("11") == 1)//if equal to 1, CashCounter Open Do Not Allow to Download File 
				{					
					CustomToast.makeText(OtherBrowseActivity.this, "Please Close Cash Counter to download new file.", Toast.LENGTH_SHORT);						
					return;
				}
				//if(db.GetStatusMasterDetailsByID("13") == 1 || (db.GetCountforColl() > db.GetCountGPRSSentConnectionColl()))
				if(db.GetCountforColl() > db.GetCountGPRSSentConnectionColl())
				{					
					CustomToast.makeText(OtherBrowseActivity.this, "!Warning: Receipts data are not sent through GPRS.", Toast.LENGTH_SHORT);
					return;
				}
				//Nitish 27-04-2021
				if(db.GetCountforDisconnection() > db.GetCountGPRSSentConnectionDisconnection())
				{					
					CustomToast.makeText(OtherBrowseActivity.this, "!Warning: Disconnection data are not sent through GPRS.", Toast.LENGTH_SHORT);
					return;
				}	
				//Nitish 04-07-2014
				if(db.GetCountforGPSDetails() == db.GetCountGPSDetailsSent())
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
				//Added on 06-03-2014
				//Thread for Import Data from File to Database
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mainThreadHandler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								btnRead.setVisibility(Button.INVISIBLE);
								btnBrowse.setVisibility(Button.INVISIBLE);
								pbReadStatus.setVisibility(ProgressBar.VISIBLE);
								lblProgressBarStatus.setVisibility(ProgressBar.VISIBLE);
								isReadRunning = true;
								//lblProgressBarStatus.setText("Reading data from file...");
							}
						});
						//Backup Copy=====================================================================================
						try
						{
							if(db.isAnyConnectionBilledOrEmpty())
							{
								//DataBase Backup=============================================================================
								String root = Environment.getExternalStorageDirectory().getPath();//Get External Storage Path 										
								File filepath;
								String filePh = root + "/NSoft/HescomSpotBilling/Data/" + CommonFunction.GetTimeStampName();
								filepath = new File(filePh);
								if(!filepath.exists()){filepath.mkdirs();}//Not Exists Create Directory
								if(filepath.exists())//If Directory Exists
								{
									file = new File("/data/data/in.nsoft.hescomspotbilling/databases/TRM_Hescom.dat");//Current Databse file
									ipStream = new FileInputStream(file);//get Input Stream of DataBase Path
									myOutput = new FileOutputStream(filePh+"/TRM_Hescom.dat");//Get OutputStream of New Directory Path	
									byte[] buffer = new byte[1024];//Variable
									int length;//Variable
									contentLength = file.length();
									Total = 100;
									pbReadStatus.setMax(Total);

									while((length = ipStream.read(buffer)) > 0)
									{
										myOutput.write(buffer, 0, length);//write byte by byte to OutputStream Directory
										count = count + length;

										mainThreadHandler.post(new Runnable() {//Handler for set Progress								
											@Override
											public void run() {
												// TODO Auto-generated method stub
												progressTotal = ((int)((count * 100)/contentLength))/5;
												pbReadStatus.setProgress(progressTotal);
												lblProgressBarStatus.setText("Please Wait... Creating Backup "+ String.valueOf(progressTotal) + "%");
											}
										});//END Handler for set Progress
									}
								}
								//END DataBase Backup=========================================================================

								//Write File =================================================================================
								StringBuilder sbBillingWrite = db.GetDataToWrite(mainThreadHandler, pbReadStatus, lblProgressBarStatus, 20);
								StringBuilder sbCollectionWrite = db.GetCollDataWriteToFile(mainThreadHandler, pbReadStatus, lblProgressBarStatus, 40);

								if((sbBillingWrite.length() > 0) || (sbCollectionWrite.length() > 0))
								{
									TelephonyManager mgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
									if(sbBillingWrite.length() > 0)
									{
										EncryptDecrypt en = new EncryptDecrypt("nsoft987");										
										final String BillingTest = en.encryptWithHandlerBilling(sbBillingWrite.toString(), mainThreadHandler, pbReadStatus, lblProgressBarStatus, LoginActivity.IMEINumber, WriteFileToSDCardActivity.BillingRecords, 60); //10-03-2021
									}
									if(sbCollectionWrite.length() > 0)
									{
										EncryptDecrypt en = new EncryptDecrypt("nsoft987");										
										final String CollectionTest = en.encryptWithHandlerCollection(sbCollectionWrite.toString(), mainThreadHandler, pbReadStatus, lblProgressBarStatus,LoginActivity.IMEINumber, WriteFileToSDCardActivity.CollectionRecords, 80); //10-03-2021
									}
								}
								//END Write File =============================================================================
							}
						}
						catch(Exception e)
						{
							Log.d(TAG, e.toString());
						}
						//END Backup Copy=================================================================================

						InputStream ipStream = null;
						InputStreamReader ipStrReader = null;
						BufferedReader buffReader = null;
						Scanner sc = null;
						//int iou = 0;
						boolean isVersionMatch = true; 
						try
						{
							//Decript the file ======================================
							StringBuilder sb = new StringBuilder();								
							EncryptDecrypt en = new EncryptDecrypt("nsoft987");
							final String strDecrypt = en.decryptWithHandler(txtBrowse.getText().toString(), mainThreadHandler, pbReadStatus, lblProgressBarStatus);
							ipStream = new ByteArrayInputStream(strDecrypt.getBytes());
							
							//16-03-2017
							String encfilename = "";							
							try
							{
								String str1[] = txtBrowse.getText().toString().replace("/", "#").split("#");		
								encfilename =str1[str1.length-1];
								encfilename=encfilename.split(".ToSbmEnc")[0];
							}
							catch(Exception e)
							{
								Log.d(TAG, e.toString());
							}
							//End 16-03-2017
							//contentLength = strDecrypt.length();
							//END Decript the file ==================================
							if(ipStream != null)
							{
								ipStrReader = new InputStreamReader(ipStream);								
								sc = new Scanner(ipStrReader); 
								buffReader = new BufferedReader(ipStrReader);
								totalLength = 0;
								while((ReceiveStr = buffReader.readLine()) != null)//While Loop							
								{
									sb.delete(0, sb.length());							
									sb.append(ReceiveStr);

									if(sb.toString().contains("^^"))
									{

									}
									else if(sb.toString().contains("@@"))
									{
										if(sb.toString().contains("|"))
										{										
											String[] arrSubD1 = sb.toString().split("\\|");
											if(!(arrSubD1[0].replace("@", "")).equals(ConstantClass.FileVersion))//Check Version number
											{//if Version not match then stop importing file 
												isVersionMatch = false;
												return;
											}
										}
									}     
									else if(sb.toString().contains("%%"))
									{

									}
									else
									{
										//if(sb.toString().length() == 653)
										//if(sb.toString().length() == 685)//String should contain 685 characters  //23-03-2017
										if(sb.toString().length() == 697)//String should contain 697 characters  //23-03-2017
										{
											String mMonth = sb.toString().substring(0, 2);
											String mCurrentMonth = CommonFunction.getCurrentDateByPara("Month");
											if(!mMonth.equals(mCurrentMonth))
											{
												isMonthMismatch = true;
												return;
											}
											totalLength++;
										}
									}
								}//END While Loop
								if(isVersionMatch)//If version no. match then insert into database
								{
									int a = db.InsertSBFromFile(strDecrypt, mainThreadHandler, pbReadStatus, lblProgressBarStatus, totalLength,encfilename);//16-03-2017
									if(a > 0)
									{
										mainThreadHandler.post(new Runnable() {

											@Override
											public void run() {
												// TODO Auto-generated method stub
												txtBrowse.setText("");
												CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
												params.setContext(OtherBrowseActivity.this);					
												params.setMainHandler(mainThreadHandler);
												params.setMsg("Importing data from file completed successfully. ");
												params.setButtonOkText("OK");
												params.setTitle("Message");
												params.setFunctionality(-1);
												CustomAlert cAlert  = new CustomAlert(params);
												isReadRunning = false;
												//CustomToast.makeText(OtherBrowseActivity.this, "Importing datas from file completed successfully.", Toast.LENGTH_SHORT);
											}
										});

									}
									else
									{
										mainThreadHandler.post(new Runnable() {

											@Override
											public void run() {
												// TODO Auto-generated method stub
												txtBrowse.setText("");
												CustomToast.makeText(OtherBrowseActivity.this, "Importing data from file failed.", Toast.LENGTH_SHORT);
											}
										});

									}
								}
								else//If Version not match
								{
									mainThreadHandler.post(new Runnable() {

										@Override
										public void run() {
											CustomToast.makeText(OtherBrowseActivity.this, "Importing Data from file failed. Due to Version No. Mismatch.", Toast.LENGTH_SHORT);
											isReadRunning = false;
										}
									});
								}
							}
							mainThreadHandler.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub

									pbReadStatus.setVisibility(ProgressBar.INVISIBLE);
									lblProgressBarStatus.setVisibility(ProgressBar.INVISIBLE);
								}
							});
						}
						catch(Exception e)
						{
							Log.d(TAG, e.toString());
						}
						finally
						{
							isReadRunning = false;
							if(!isVersionMatch)
							{
								mainThreadHandler.post(new Runnable() {

									@Override
									public void run() {
										CustomToast.makeText(OtherBrowseActivity.this, "Importing Data from file failed. Due to Version No. Mismatch.", Toast.LENGTH_SHORT);
										btnRead.setVisibility(Button.VISIBLE);
										btnBrowse.setVisibility(Button.VISIBLE);
										pbReadStatus.setVisibility(ProgressBar.INVISIBLE);
										lblProgressBarStatus.setVisibility(ProgressBar.INVISIBLE);
									}
								});
							}
							if(isMonthMismatch)
							{
								mainThreadHandler.post(new Runnable() {

									@Override
									public void run() {
										CustomToast.makeText(OtherBrowseActivity.this, "Importing Data from file failed. Please load current month file.", Toast.LENGTH_SHORT);
										btnRead.setVisibility(Button.VISIBLE);
										btnBrowse.setVisibility(Button.VISIBLE);
										pbReadStatus.setVisibility(ProgressBar.INVISIBLE);
										lblProgressBarStatus.setVisibility(ProgressBar.INVISIBLE);
									}
								});
								isMonthMismatch = false;;
							}
							sc.close();
							if(buffReader != null)
							{
								try {
									buffReader.close();									
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							if(ipStream != null)
							{
								try {
									ipStream.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}).start();


			}
		});
	}
	//Tamilselvan on 04-03-2014
	public boolean CheckforSpecialChar(String str)
	{
		boolean rt = false;
		if(str.contains("^^"))
		{
			rt = true;
		}
		else if(str.contains("@@"))
		{
			rt = true;
		}
		else if(str.contains("%%"))
		{
			rt = true;
		}
		return rt;
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
		if(!isReadRunning)
		{
			return OptionsMenu.navigate(item, OtherBrowseActivity.this);	
		}
		else
		{
			CustomToast.makeText(OtherBrowseActivity.this, "You cant navigate until file reading is completed.", Toast.LENGTH_SHORT);
			return false;
		}
	}
	//Tamilselvan on 01-04-2014
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub			
		return;
	}
	//Tamilselvan on 05-04-2014
	@Override
	public void performAction(boolean alertResult, int functionality) {
		// TODO Auto-generated method stub
		btnRead.setVisibility(Button.VISIBLE);
		btnBrowse.setVisibility(Button.VISIBLE);
		BillingObject.Remove();
		Intent i = new Intent(OtherBrowseActivity.this, BillingStartActivity.class);
		startActivity(i);
		finish();
		/*if(LoginActivity.gpsTracker == null || !LoginActivity.gpsTracker.isGPSConnected())
		{
			Intent i = new Intent(OtherBrowseActivity.this, HomePage.class);
			startActivity(i);
		}
		else
		{
			Intent i = new Intent(OtherBrowseActivity.this, Billing.class);
			startActivity(i);
		}*/
	}
}
