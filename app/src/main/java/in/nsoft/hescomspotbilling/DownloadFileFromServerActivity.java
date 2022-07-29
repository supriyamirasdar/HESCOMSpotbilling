package in.nsoft.hescomspotbilling;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadFileFromServerActivity extends Activity implements AlertInterface {
	Button btnsendverificationcode;//btnsenduniqueid,
	EditText editverificationcode;//edituniqueid,
	TextView textverificationcode,lblProgressBarStatus, tvListViewCaption;//textuniqueid,

	ConnectionDetector conn;
	Handler hndl,alerthandler,mainThreadHandler;
	DDLAdapter da;
	FileAdapter fileAdapter;
	//Context mcntx;
	String uniqueid ;
	String verfCode ;
	DatabaseHelper dh=new DatabaseHelper(DownloadFileFromServerActivity.this);
	private long contentLength = 0, totalLength = 0;
	String rValue;
	String sBMNumber;
	ListView lv;
	InputStream ipStream;
	String filename1;
	ProgressBar pbReadStatus;
	DatabaseHelper db = new DatabaseHelper(this);

	String ReceiveStr = "";
	StringBuilder sb = new StringBuilder();
	boolean isVersionMatch = true;
	InputMethodManager inputmngr;
	private String btnAlertExit = "Cancel";
	private String btnAlertOk = "Ok";
	boolean isMonthMismatch = false;
	BigDecimal ar;
	BigDecimal ir;
	BigDecimal intrst;
	BigDecimal totalar=new BigDecimal("0");
	BigDecimal totalir=new BigDecimal("0");
	BigDecimal totalintrst=new BigDecimal("0");
	ProgressDialog progressDialog; 

	int allBilled = 0;

	String IMEINumber = "";
	String SimNumber = "";		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download_file_frome_server);

		btnsendverificationcode = (Button)findViewById(R.id.buttonsendverificationcode);
		editverificationcode = (EditText)findViewById(R.id.editTextverificationcode);
		textverificationcode = (TextView)findViewById(R.id.textverificationcode);
		lblProgressBarStatus = (TextView)findViewById(R.id.textViewprogressbar);
		tvListViewCaption = (TextView)findViewById(R.id.tvListViewCaption);
		pbReadStatus = (ProgressBar)findViewById(R.id.progressBar1);
		lv = (ListView)findViewById(R.id.LVSubdivList);

		lblProgressBarStatus.setVisibility(TextView.INVISIBLE);
		tvListViewCaption.setVisibility(TextView.INVISIBLE);
		lv.setVisibility(ListView.INVISIBLE);
		pbReadStatus.setVisibility(ProgressBar.INVISIBLE);


		conn = new ConnectionDetector(DownloadFileFromServerActivity.this);
		hndl = new Handler();
		alerthandler = new Handler();
		mainThreadHandler = new Handler();
		inputmngr = (InputMethodManager)getSystemService(DownloadFileFromServerActivity.INPUT_METHOD_SERVICE);

		btnsendverificationcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(db.isAnyConnectionBilledOrEmpty())
				{
					int getVal= db.CheckFlagToLoadFile();
					if(getVal == 3)
					{
						CustomToast.makeText(DownloadFileFromServerActivity.this, "Download the File and then Load new File.", Toast.LENGTH_SHORT);
						//txtBrowse.setText("");
						return;
					}
					else if(getVal == 4)
					{
						CustomToast.makeText(DownloadFileFromServerActivity.this, "Check GPRS Data whether every billed data sent to server or not.", Toast.LENGTH_SHORT);
						//txtBrowse.setText("");
						return;
					}
				}
				//25-06-2016 - Start
				if(db.GetStatusMasterDetailsByID("11") == 1)//if equal to 1, CashCounter Open Do Not Allow to Download File 
				{					
					CustomToast.makeText(DownloadFileFromServerActivity.this, "Please Close Cash Counter to download new file.", Toast.LENGTH_SHORT);						
					return;
				}
				//if(db.GetStatusMasterDetailsByID("13") == 1 || (db.GetCountforColl() > db.GetCountGPRSSentConnectionColl()))
				if(db.GetCountforColl() > db.GetCountGPRSSentConnectionColl())
				{					
					CustomToast.makeText(DownloadFileFromServerActivity.this, "!Warning: Receipts data are not sent through GPRS.", Toast.LENGTH_SHORT);
					return;
				}			
				
				if(db.GetCountforGPSDetails() == db.GetCountGPSDetailsSent())
				{
					try
					{
						db.DropCreateGPSTable(); //Drop and Recreate GPS Table
					}
					catch(Exception e)
					{
						Log.d(" ", e.toString());
					}				
				}
				//End 25-06-2016
				//28-03-2015
				if(db.CheckIfAllConnectionBilled())
				{					
					allBilled = 1;
				}
				else
				{
					CustomToast.makeText(DownloadFileFromServerActivity.this, "Check if all connections are Billed or Not.", Toast.LENGTH_LONG);
					allBilled = 0;
				}
				inputmngr.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				if(conn.isConnectedToInternet())
				{
					//final TelephonyManager mgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
					verfCode = editverificationcode.getText().toString();
					
					if(editverificationcode.length() == 0)
					{
						CustomToast.makeText(DownloadFileFromServerActivity.this, "Please Enter Reading Day", Toast.LENGTH_LONG);
						return;
					}			

					try
					{
						//Get Mobile Details ==============================================================
						//10-03-2021
						IMEINumber = CommonFunction.getDeviceNo(DownloadFileFromServerActivity.this);
						SimNumber = CommonFunction.getSIMNo(DownloadFileFromServerActivity.this);

					}
					catch(Exception e)
					{
										
					}

					progressDialog = ProgressDialog.show(DownloadFileFromServerActivity.this, "Please wait..", "Listing files from server.",true);//Spinner
					progressDialog.setCancelable(false);
					new Thread(new Runnable() {
						@SuppressLint("NewApi")
						public void run() {
							try {   

								//Modified Nitish 10-08-2017
								//HttpClient httpclt= new DefaultHttpClient();
								//HttpGet httpGet = new HttpGet(ConstantClass.IPAddress +"/FileName?ReadingDate=" + verfCode + "&IMEINo=" + "867203021543733" + "&Version=" + ConstantClass.FileVersion + "&allBilled=" + String.valueOf(allBilled));
								//httpGet.addHeader("userName","NSOFT");
								//HttpResponse res = httpclt.execute(httpGet);
								//HttpEntity ent = res.getEntity();								
								
								HttpClient httpclt = new DefaultHttpClient();//object for HttpClient								
								HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/getFileName");//Testing
								List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
								lvp.add(new BasicNameValuePair("ReadingDate", verfCode));
								//lvp.add(new BasicNameValuePair("IMEINo", "867203021543733"));
								lvp.add(new BasicNameValuePair("IMEINo", IMEINumber));
								lvp.add(new BasicNameValuePair("Version", ConstantClass.FileVersion));
								lvp.add(new BasicNameValuePair("allBilled", String.valueOf(allBilled)));
								httpPost.setEntity((new UrlEncodedFormEntity(lvp)));
								httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");							
								HttpResponse res = httpclt.execute(httpPost);
								HttpEntity ent = res.getEntity();
								//End Modified Nitish 10-08-2017
								if(ent != null)
								{
									rValue = EntityUtils.toString(ent);
									ReadFileServerResponse rfsr = new ReadFileServerResponse();

									ArrayList<String> fd = rfsr.Readfilename(rValue);
									ArrayList<DDLItem> adl = new ArrayList<DDLItem>();
									fd.get(0);
									if(fd.get(0).equals("FPNF"))
									{
										hndl.post(new Runnable() {

											@Override
											public void run() {
												//edituniqueid.setText(rValue);
												editverificationcode.setText("");
												CustomToast.makeText(DownloadFileFromServerActivity.this, "File Path Not Found", Toast.LENGTH_LONG);

											}
										});
									}
									else if(fd.get(0).equals("NB"))
									{
										hndl.post(new Runnable() {

											@Override
											public void run() {
												//edituniqueid.setText(rValue);
												editverificationcode.setText("");
												CustomToast.makeText(DownloadFileFromServerActivity.this, "100% Billing Not Completed. Contact NSOFT Support", Toast.LENGTH_LONG);

											}
										});
									}
									else if(fd.get(0).equals("ERR"))//31-08-2017
									{
										hndl.post(new Runnable() {

											@Override
											public void run() {
												//edituniqueid.setText(rValue);
												editverificationcode.setText("");
												CustomToast.makeText(DownloadFileFromServerActivity.this, "Error in File Download. Contact NSOFT Support", Toast.LENGTH_LONG);

											}
										});
									}
									else
									{
										if(fd.get(0).equals("VM"))
										{
											hndl.post(new Runnable() {

												@Override
												public void run() {
													//edituniqueid.setText(rValue);
													CustomToast.makeText(DownloadFileFromServerActivity.this, "VM", Toast.LENGTH_LONG);

												}
											});
										}
										else
										{
											hndl.post(new Runnable() {

												@Override
												public void run() {
													tvListViewCaption.setVisibility(TextView.VISIBLE);
													lv.setVisibility(ProgressBar.VISIBLE);

													btnsendverificationcode.setVisibility(Button.INVISIBLE);
													editverificationcode.setVisibility(EditText.INVISIBLE);													
													textverificationcode.setVisibility(TextView.INVISIBLE);													
													lblProgressBarStatus.setVisibility(TextView.INVISIBLE);													
													pbReadStatus.setVisibility(ProgressBar.INVISIBLE);
												}
											});

											for(int i=0;i<fd.size();i++)
											{
												DDLItem dlitem=new DDLItem(String.valueOf(i), fd.get(i));
												adl.add(dlitem);
											}
											//da = new DDLAdapter(DownloadFileFromServerActivity.this, adl );
											fileAdapter = new FileAdapter(adl);
											hndl.post(new Runnable() {

												@Override
												public void run() {
													// TODO Auto-generated method stub
													//lv.setAdapter(da);
													lv.setAdapter(fileAdapter);
												}
											});
										}
									}
								}

							} 
							catch (Exception e)
							{
								hndl.post(new Runnable() {

									@Override
									public void run() {
										//edituniqueid.setText(rValue);
										CustomToast.makeText(DownloadFileFromServerActivity.this, "Error in Obtaining File.", Toast.LENGTH_LONG);

									}
								});
							}
							progressDialog.dismiss();
						}
					}).start();

				}
				else   
				{

					// TODO Auto-generated method stub
					CustomToast.makeText(DownloadFileFromServerActivity.this, "Please check internet connection", Toast.LENGTH_LONG);
					CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
					params.setContext(DownloadFileFromServerActivity.this);					
					params.setMainHandler(mainThreadHandler);
					params.setMsg("Please check your data connection. If turned off then turn on and try again. ");
					params.setButtonOkText("OK");
					params.setTitle("Message");
					params.setFunctionality(-1);
					CustomAlert cAlert  = new CustomAlert(params);  


				}


				/*btnsenduniqueid.setVisibility(Button.INVISIBLE);
				btnsendverificationcode.setVisibility(Button.INVISIBLE);
				editverificationcode.setVisibility(EditText.INVISIBLE);
				edituniqueid.setVisibility(EditText.INVISIBLE);
				textverificationcode.setVisibility(TextView.INVISIBLE);
				textuniqueid.setVisibility(TextView.INVISIBLE);*/
			}
		});

		//######################### Buttons Stop ##############################

		//######################### lv Start ##############################
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {			
			@Override
			public void onItemClick(AdapterView<?> alist, View arg1, int pos,
					long arg3) {
				DDLItem k = (DDLItem)  alist.getItemAtPosition(pos);
				filename1=k.getValue();
				if(conn.isConnectedToInternet())    
				{
					if(filename1.toString().equals(""))
					{
						CustomToast.makeText(DownloadFileFromServerActivity.this, "Browse file to read.", Toast.LENGTH_SHORT);
						return;
					}
					if(filename1.toString().indexOf(".ToSbmEnc") != -1)
					{
						if(!filename1.toString().substring(filename1.toString().length() - 3, filename1.toString().length()).equals("Enc"))
						{
							CustomToast.makeText(DownloadFileFromServerActivity.this, "File extension not supported.", Toast.LENGTH_SHORT);
							return;
						}
					}
					else
					{
						CustomToast.makeText(DownloadFileFromServerActivity.this, "File extension not supported.", Toast.LENGTH_SHORT);
						return;
					}
					//Check DataBase is Empty or not and if not empty check any connection billed or not
					/*if(dh.isAnyConnectionBilledOrEmpty())
					{
						int getVal= dh.CheckFlagToLoadFile();
						if(getVal == 3)
						{
							CustomToast.makeText(DownloadFileFromServerActivity.this, "Download the File and then Load new File.", Toast.LENGTH_SHORT);
							//txtBrowse.setText("");
							return;
						}
						else if(getVal == 4)
						{
							CustomToast.makeText(DownloadFileFromServerActivity.this, "Check GPRS Data whether every billed data sent to server or not.", Toast.LENGTH_SHORT);
							//txtBrowse.setText("");
							return;
						}
					}*/
					//Added on 06-03-2014

					/*if(db.getCountofRecods() > db.GetCountMFFlag())
					{					
						CustomToast.makeText(DownloadFileFromServerActivity.this, "!Warning: Survey not completed.", Toast.LENGTH_SHORT);
						return;
					}*/					
					if(dh.GetCountforGPSDetails() == dh.GetCountGPSDetailsSent())
					{
						try
						{
							dh.DropCreateGPSTable(); //Drop and Recreate GPS Table
						}
						catch(Exception e)
						{

						}				
					}
					CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
					params.setContext(DownloadFileFromServerActivity.this);					
					params.setMainHandler(alerthandler);
					params.setMsg("Do you want to Download " + k.getValue());
					params.setButtonOkText(btnAlertExit);
					params.setButtonCancelText(btnAlertOk);
					params.setTitle("Download File from Server");
					params.setFunctionality(-1);
					CustomAlert cAlert  = new CustomAlert(params);
				}
				else
				{
					hndl.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							CustomToast.makeText(DownloadFileFromServerActivity.this, "Please check internet connection", Toast.LENGTH_LONG);
						}
					});
				}
			}
		});
		//######################### lv Stop ##############################
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		return;
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
		return OptionsMenu.navigate(item,DownloadFileFromServerActivity.this);		
	}

	@Override
	public void performAction(boolean alertResult, int functionality) {
		// TODO Auto-generated method stub

		if(conn.isConnectedToInternet())
		{

			if(!alertResult)
			{
				final ProgressDialog ringProgress = ProgressDialog.show(DownloadFileFromServerActivity.this, "Please wait..", "Downloading from Server...",true);//Spinner
				ringProgress.setCancelable(false);


				btnsendverificationcode.setVisibility(Button.INVISIBLE);
				editverificationcode.setVisibility(EditText.INVISIBLE);												
				textverificationcode.setVisibility(TextView.INVISIBLE);	
				tvListViewCaption.setVisibility(TextView.INVISIBLE);
				lv.setVisibility(ProgressBar.INVISIBLE);
				lblProgressBarStatus.setVisibility(TextView.VISIBLE);													
				pbReadStatus.setVisibility(ProgressBar.VISIBLE);

				new Thread(new Runnable() {

					@SuppressLint("NewApi")
					@Override
					public void run() {
						try  
						{
							//Modified Nitish 10-08-2017
							/*String filename = ConvertToXML.DownloadStringToXML(filename1);
							HttpClient httpclt = new DefaultHttpClient();//object for HttpClient
							HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/DownloadFile");
							StringEntity sa = new StringEntity(filename, HTTP.UTF_8);
							sa.setContentEncoding("UTF-8");
							sa.setContentType("application/xml");
							httpPost.setEntity(sa);
							httpPost.addHeader("userName","NSOFT");
							HttpResponse res = httpclt.execute(httpPost);
							HttpEntity ent = res.getEntity();*/
							HttpClient httpclt = new DefaultHttpClient();//object for HttpClient								
							HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Hescom_DownloadFile");//Testing
							List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
							lvp.add(new BasicNameValuePair("filename", filename1));							
							httpPost.setEntity((new UrlEncodedFormEntity(lvp)));
							httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");							
							HttpResponse res = httpclt.execute(httpPost);
							HttpEntity ent = res.getEntity();
							//End Modified Nitish 10-08-2017
							if(ent != null)
							{               
								String rValue = EntityUtils.toString(ent);
								ReadFileServerResponse rfsr = new ReadFileServerResponse();
								FileDownload fd = rfsr.Read(rValue);								
								int length= Integer.valueOf(fd.getMfileLength());
								//31-12-2016
								String encfilename = "";							
								try
								{
									String str1[] = fd.getmFileName().trim().toString().replace("/", "#").split("#");		
									encfilename =str1[str1.length-1];
									encfilename=encfilename.split(".ToSbmEnc")[0];
								}
								catch(Exception e)
								{
									Log.d("", e.toString());
								}
								//End 31-12-2016
								byte[] sd = fd.getmBytes().getBytes("UTF-8");
								byte[] sd1 = Base64.decode(sd, Base64.DEFAULT);
								//	BigDecimal lnth=(new BigDecimal(sd1.length).divide(new BigDecimal(1024), MathContext.DECIMAL64)).setScale(0, BigDecimal.ROUND_UP);
								if(length == sd1.length)
								{ 

									EncryptDecrypt en = new EncryptDecrypt("nsoft987");
									final String strDecrypt = en.decrypt(sd1);
									//ringProgress.dismiss();

									ipStream = new ByteArrayInputStream(strDecrypt.getBytes());
									InputStreamReader ipStrReader = new InputStreamReader(ipStream);
									BufferedReader buffReader = new BufferedReader(ipStrReader);

									while((ReceiveStr = buffReader.readLine()) != null)				
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
													//return;
												}
											}
										}          
										else if(sb.toString().contains("%%"))
										{

										}   
										else if(sb.toString().contains("$$"))
										{

										}   
										else
										{

											//if(sb.toString().length() == 685 || sb.toString().length() == 1022)//String should contain 653 characters //Nitish 29-09-2016
											//if(sb.toString().length() == 685 || sb.toString().length() == 1022)//String should contain 653 characters //Nitish 12-03-2019
											if(sb.toString().length() == 697 || sb.toString().length() == 1022)//String should contain 697 characters //Nitish 29-06-2021
											{
												totalLength++;
											}
										}
									}
									if(isVersionMatch)//Version Matches
									{
										dh.InsertSBFromserver(strDecrypt, mainThreadHandler, pbReadStatus, lblProgressBarStatus, totalLength,encfilename);
										//myOutput.write(sd1);

										hndl.post(new Runnable() {

											@Override
											public void run() {
												try
												{
													db.EventLogSave("11", LoginActivity.IMEINumber, LoginActivity.SimNumber, "Online Data Download ");//15-07-2016

												}
												catch(Exception e)
												{

												}
												AlertDialog.Builder alert = new AlertDialog.Builder(DownloadFileFromServerActivity.this);
												alert.setCancelable(false);
												alert.setTitle("Success");
												alert.setMessage("Download and Data Insertion Completed.");
												alert.setPositiveButton("GOTO Billing", new DialogInterface.OnClickListener() {

													@Override
													public void onClick(DialogInterface arg0, int arg1) {
														Intent i = new Intent(DownloadFileFromServerActivity.this, HomePage.class);
														startActivity(i);
														finish();
													}
												});
												alert.show();
											}
										});
									}
									else//If version mismatch
									{
										hndl.post(new Runnable() {

											@Override
											public void run() {

												CustomToast.makeText(DownloadFileFromServerActivity.this, "File could not be downloaded. Due to file version mismatch.", Toast.LENGTH_LONG);
												Intent i = new Intent(DownloadFileFromServerActivity.this, DownloadFileFromServerActivity.class);
												startActivity(i);
												return;
											}
										});
									}
								}
								else
								{
									hndl.post(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											CustomToast.makeText(DownloadFileFromServerActivity.this, "File could not be downloaded. Please download again", Toast.LENGTH_LONG);
											Intent i = new Intent(DownloadFileFromServerActivity.this, DownloadFileFromServerActivity.class);
											startActivity(i);
											return;
										}
									});

								}

							}
						}
						catch(Exception e)
						{
							hndl.post(new Runnable() {

								@Override
								public void run() {
									CustomToast.makeText(DownloadFileFromServerActivity.this, "File Not Found. Contact NSOFT", Toast.LENGTH_LONG);
									Intent i = new Intent(DownloadFileFromServerActivity.this, DownloadFileFromServerActivity.class);
									startActivity(i);
									return;
								}
							});
						}
						ringProgress.dismiss();

					}					
				}).start();
			}
		}
		else   
		{
			hndl.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					CustomToast.makeText(DownloadFileFromServerActivity.this, "Please check internet connection", Toast.LENGTH_LONG);
				}
			});

		}
	}


	/*public boolean checksum(byte[] sd1)
	{      

		InputStream ipStream = null;
		InputStreamReader ipStrReader = null;
		BufferedReader buffReader = null;
		Scanner sc = null;
		boolean tf=false;
		//int iou = 0;
		boolean isVersionMatch = true; 
		try   
		{    
			//Decript the file ======================================

			StringBuilder sb = new StringBuilder();	//23072014							
			EncryptDecrypt en = new EncryptDecrypt("nsoft987");

			//	final String strDecrypt = en.decryptWithHandler(sd1.toString(), mainThreadHandler, pbReadStatus, lblProgressBarStatus);
			final String strDecrypt = en.decrypt(sd1);
			//strDecrypt.length();
			//ipStream = new ByteArrayInputStream(strDecrypt);
			ipStream = new ByteArrayInputStream(strDecrypt.getBytes());
			//contentLength = strDecrypt.length();
			//END Decript the file ==================================
			if(ipStream != null)
			{   
				ipStrReader = new InputStreamReader(ipStream);								
				sc = new Scanner(ipStrReader); 
				buffReader = new BufferedReader(ipStrReader);

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
								//return;
							}
						}
					}          
					else if(sb.toString().contains("%%"))
					{

					}   
					else if(sb.toString().contains("$$"))
					{
						String[] arrSubD2=sb.toString().split("#");
						String str= arrSubD2[1];
						String[] arrSubD3=	str.split("\\|");
						ar=new BigDecimal(arrSubD3[0].trim());
						ir=new BigDecimal(arrSubD3[1].trim());
						String str2= arrSubD3[2];
						char[] ch=new char[10];
						for(int i=0;str2.charAt(i) != '$';i++)
						{
							ch[i] = str2.charAt(i);
						}
						String	str4 = String.valueOf(ch);
						//String[] arrSubD4= str2.split("$");
						intrst=new BigDecimal(str4.trim());


					}   
					else
					{
						if(sb.toString().length() == 673)//String should contain 673 characters
						{
							String mMonth = sb.toString().substring(0, 2);
							String mCurrentMonth = CommonFunction.getCurrentDateByPara("Month");
							if(!mMonth.equals(mCurrentMonth))
							{
								isMonthMismatch = true;
								//return;      
							}         
							totalLength++;
							if(new BigDecimal(sb.toString().substring(159, 173).trim()).compareTo(new BigDecimal("0"))== -1)
							{
								String[] str=	sb.toString().substring(159, 173).trim().split("\\-");
								totalar = totalar.subtract(new BigDecimal(str[1].trim()));
							}     
							else  
							{      
								totalar = totalar.add(new BigDecimal(sb.toString().substring(159, 173).trim()));
							}
							totalir =totalir.add(new BigDecimal(sb.toString().substring(146, 158).trim()) ) ;
							totalintrst = totalintrst.add(new BigDecimal(sb.toString().substring(173, 182).trim()));


						}
					}
				}//END While Loop

			}

			if(ar.equals(totalar))
			{
				if(ir.equals(totalir))
				{

					if(intrst.equals(totalintrst))
					{

						tf= true;     
					}
					else
					{
						tf= false;
					}
				}
				else 
				{
					tf= false;
				}
			}
			else 
			{
				tf= false;
			}
			sc.close();
		}

		catch(Exception e)
		{
			Log.d("", e.toString());
		}


		return tf;
	}*/

	public class FileAdapter extends ArrayAdapter<DDLItem>
	{
		private ArrayList<DDLItem> malddl = null;
		public FileAdapter(ArrayList<DDLItem> alddl) {
			super(DownloadFileFromServerActivity.this, R.layout.simplelist, alddl);
			this.malddl = alddl;
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View v = null;
			try 
			{
				/*LayoutInflater inflater = (DownloadFileFromServerActivity.this).getLayoutInflater();
				v = inflater.inflate(R.layout.simplelist, parent, false);*/
				v = DownloadFileFromServerActivity.this.getLayoutInflater().inflate(R.layout.simplelist, null);
				TextView tvlist = (TextView)v.findViewById(R.id.tvsimplelist);
				tvlist.setText(this.malddl.get(position).getValue());
			} 
			catch (Exception e) 
			{
				// TODO: handle exception
			}
			return v;
		}
	}

}
