
//Nitish 24-02-2014
package in.nsoft.hescomspotbilling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Layout;
import android.text.TextPaint;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BillingConsumption_LandT extends Activity implements AlertInterface{

	Context	mcx; 
	private ArrayList<DDLItem> alConStatusItem;
	private Spinner ddlBillConsumStatus;
	private TextView lblRrNo, lblPrvStatus, lblConsumption, txtBillConsumBillAmount, lblBillConsumptionMtd,edtBillReading,lblwelcome; //MayReadLT	

	//Nitish Modified 24-11-2014 For Alert Dialog
	TextView tvDisplayData,tvInflateMtrType,tvInflateBatteryStatus,tvInflateMeterPlacement,tvInflateMeterCondition,tvInflateContactType,tvInflateContactNo,tvInflateMtrPhase;	
	TextView tvInflateMeterMake,tvInflateMeterPhase,tvInflateMeterReason; //Nitish 31-01-2015 For yalahanka Survey Alert Dialog
	TextView tvInflatorTCName,tvInflateLED,tvInflateMeterNo; //29-02-2016

	Spinner ddlInflatorMeterMake,ddlInflatorMeterPhase,ddlInflatorMeterReason; //Nitish 31-01-2015 For yalahanka Survey Alert Dialog
	Spinner ddlInflatorMeterType,ddlInflatorBatteryStatus,ddlInflatorMeterPlacement,ddlInflatorMeterCondition,ddlInflatorContactType;
	Spinner ddlTCName; //26-12-2015
	EditText txtInflatorContactNo,txtInflatorLED,txtInflatorMeterNo; //29-02-2016
	ArrayList<String> alStdCodes;
	boolean stdvalid = false;

	TextView tvInflateOwnerType,tvInflateEmailId,tvInflateAadharNo; //25-06-2016
	EditText txtInflatorEmailId,txtInflatorAadharNo; //25-06-2016
	Spinner ddlInflatorOwnerType; ///25-06-2016
	//09-06-2015
	public static boolean isRead = false;  //For Old Probe

	AlertDialog.Builder alert;

	private EditText  edtPowerFactor, edtMD;

	private Button btnProcess, btnSave, btnReadLT; //MayReadLT	
	private ConStatusTwoDimArray ConArray;
	Handler mHandler ;
	private ReadSlabNTarifSbmBillCollection SlabColl;
	DatabaseHelper db = new DatabaseHelper(this);
	private static final String TAG = BillingConsumption_LandT.class.getName();
	BigDecimal bgPreReading, bgPrvReading, bgUsed, bgConsumption;
	Checkbox cbCalc;

	//Nitish 05-01-2015
	String IMEINumber = "";
	String SimNumber = "";		
	DDLAdapter meterTypeAdapter,batteryStatusAdapter,meterPlacementAdapter,meterConditionAdapter,contactTypeAdapter,meterreasonAdapter,ownertypeadapter,meterPhaseAdapter;
	DDLAdapter metermakeadapter,meterphaseadapter; //Nitish 07-01-2015 For yalahanka Survey Alert Dialog
	int mReason;

	//Bluethooth variables===============================================
	int i = 0;
	Handler dh;
	BluetoothPrinting bptr;
	static final int REQUEST_ENABLE_BT = 0;
	//END Bluethooth variables============================================

	BigDecimal avgCons;
	int result = -1;
	ProgressDialog ringProgress;
	long diff = 0;

	CustomAlert cAlert;
	boolean malertResult;
	private String alertmsg = "";
	private String alerttitle = "Save/Print Bill";
	private String btnAlertExit = "Exit";
	private String btnAlertPrint = "Save";
	private int SingleButton = 0;
	private int TwoButtons = 1;
	private int Print = 2;	
	String eol;

	//MayReadLT
	private static LandTObject LandT;
	String readingnew = "";
	//private boolean isOk = false;

	//05-09-2015
	static int selecteditempos = 0;

	//29-01-2021
	private TextView lblMterType;

	//25-06-2016
	static String saveDialogOwner,saveDialogPhone,saveDialogEmail,saveDialogAadhar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SlabColl = BillingObject.GetBillingObject();//Get Billing Object
		setContentView(R.layout.activity_billing_consumption_powerfactor_landt);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);	


		lblwelcome = (TextView)findViewById(R.id.lblwelcome);
		lblwelcome.setSelected(true);	

		//Nitish 05-01-2015
		try
		{
			//Get Mobile Details ==============================================================
			//16-01-2021
			IMEINumber = CommonFunction.getDeviceNo(BillingConsumption_LandT.this);
			SimNumber = CommonFunction.getDeviceNo(BillingConsumption_LandT.this);
			SlabColl.setmSBMNumber(IMEINumber);

		}
		catch(Exception e)
		{
			//CustomToast.makeText(BillingConsumption.this, "Please Check if Internet Connection is Enabled.", Toast.LENGTH_SHORT);				
		}

		//29-01-2021
		try
		{		
			lblMterType = (TextView)findViewById(R.id.lblMterType);
			lblMterType.setText(db.getMType(Billing_LandT.LTLG));
		}
		catch(Exception e)
		{

		}

		//25-06-2016
		saveDialogOwner = "0";
		saveDialogPhone="";
		saveDialogEmail="";
		saveDialogAadhar="";

		//isRead = true;	

		ddlBillConsumStatus  = (Spinner)findViewById(R.id.ddlBillConsumStatus);
		lblRrNo = (TextView)findViewById(R.id.txtBillConsumRRNo);
		lblPrvStatus = (TextView)findViewById(R.id.txtBillConsumPrevReason);
		lblConsumption = (TextView)findViewById(R.id.txtBillConsumConsumption); 
		edtBillReading = (TextView)findViewById(R.id.txtBillConsumStatusReading);//MayReadLT	
		edtPowerFactor = (EditText)findViewById(R.id.txtBillConsumPowerFactor);
		edtMD = (EditText)findViewById(R.id.txtBillConsumMD);
		btnReadLT = (Button)findViewById(R.id.btnReadLT);//MayReadLT	
		btnProcess = (Button)findViewById(R.id.btnBillConsumProcess);
		btnSave = (Button)findViewById(R.id.btnBillConsumSavePrint);
		txtBillConsumBillAmount = (TextView)findViewById(R.id.txtBillConsumBillAmount);
		lblBillConsumptionMtd = (TextView)findViewById(R.id.lblBillConsumptionMtd);//Tamilselvan on 20-03-2014

		//05-09-2015
		if(Billing_LandT.isFromBilling)
		{
			selecteditempos = 0;
		}


		mHandler = new Handler();
		dh = new Handler();
		eol = System.getProperty("line.separator");

		//Show Message for Latitude_image and Longitude_image=========================================
		if(LoginActivity.gpsTracker.canGetLocation())
		{
			BillingObject.GetBillingObject().setmGps_Latitude_image(String.valueOf(LoginActivity.gpsTracker.latitude));
			BillingObject.GetBillingObject().setmGps_Longitude_image(String.valueOf(LoginActivity.gpsTracker.longitude));
		}	
		Log.d("Before", SlabColl.getmMtd());
		if(SlabColl.getmMtd().equals("1"))
		{
			lblBillConsumptionMtd.setText("Metered");
			ConArray = new ConStatusTwoDimArray();	
			btnProcess.setText(ConstantClass.strProcessText);
			btnSave.setVisibility(Button.INVISIBLE);		
			LoadInitialDetails();//Loading Initial Data from Billing Object and Bind Dropdownlist
			edtPowerFactor.setEnabled(false);
			edtMD.setEnabled(false);
		}
		else
		{
			lblBillConsumptionMtd.setText("Not Metered");	
			ddlBillConsumStatus.setEnabled(false);
			edtBillReading.setEnabled(false);
			edtPowerFactor.setEnabled(false); 
			edtMD.setEnabled(false);
		}
		Log.d("After", SlabColl.getmMtd());
		btnSave.setVisibility(Button.INVISIBLE);

		//Listener for Selecting Status
		ddlBillConsumStatus.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int[][] FRStatus = ConArray.FRStatus;
				DDLItem k = (DDLItem) arg0.getItemAtPosition(arg2);
				int ItemId = Integer.valueOf(k.getId());
				SlabColl.setmPreStatus(k.getId());
				for(int q = 1; q < FRStatus.length; q++)//For Loop for Rows
				{
					if(FRStatus[q][0] == Integer.valueOf(SlabColl.getmStatus()))
					{
						for(int r = 1; r < FRStatus[r].length; r++)//Column For Loop 
						{
							if(FRStatus[0][r] == ItemId)//Check Selected Item Matching 
							{
								if(FRStatus[q][r] == 0)//if 0 Not allow to enter reading in the textbox
								{
									edtBillReading.setEnabled(false);
									edtPowerFactor.setEnabled(false);
									edtMD.setEnabled(false);
									//09-05-2015
									btnReadLT.setEnabled(false);
								}
								else if(FRStatus[q][r] == 1)//if 1 allow to enter reading in the textbox
								{
									edtBillReading.setEnabled(true);
									//09-05-2015
									btnReadLT.setEnabled(true);
								}
								break;
							}
						}//END Column For Loop 
					}
					//break;
				}//END For Loop for Rows	

				//09-05-2015				
				selecteditempos = ddlBillConsumStatus.getSelectedItemPosition();	

				edtBillReading.setText("");
				edtPowerFactor.setText("");
				edtMD.setText("");
				lblConsumption.setText("");
				txtBillConsumBillAmount.setText("");

				if(ItemId==0||ItemId==2||ItemId==3||ItemId==4||ItemId==9)
				{
					btnReadLT.setEnabled(true);
				}
				else
				{
					btnReadLT.setEnabled(false);
				}


			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		//MAyReadLT
		btnReadLT.setOnClickListener(new OnClickListener() { 

			@Override
			public void onClick(View arg0) 
			{

				try
				{	
					//23-03-2021
					if(Billing_LandT.LTLG==0)
					{
						CustomToast.makeText(BillingConsumption_LandT.this, "Please Bill Again. ", Toast.LENGTH_SHORT);
						Intent i = new Intent(BillingConsumption_LandT.this, Billing_LandT.class);//12-01-2021
						startActivity(i);
						finish();
					}
					else if(Billing_LandT.PROBETYPE==1) //NEW PROBE
					{
						//isRead = false;
						//CustomToast.makeText(BillingConsumption_LandT.this, "Meter Type " + Billing_LandT.LTLG, Toast.LENGTH_SHORT);
						Intent i = new Intent(BillingConsumption_LandT.this, USBSerial_MainActivity.class);//12-01-2021
						startActivity(i);
					}
					else //OLD PROBE
					{					
						if(!isRead)
						{
							isRead = true;					
							Intent i = new Intent(BillingConsumption_LandT.this, UARTLoopbackActivity.class);
							startActivity(i);	
						}
						else
						{						
							CustomAlert.CustomAlertParameters paramslt = new CustomAlert.CustomAlertParameters(); 
							paramslt.setContext(BillingConsumption_LandT.this);					
							paramslt.setMainHandler(dh);
							paramslt.setMsg("Unplug Cable and Continue." );
							paramslt.setButtonOkText("OK");
							paramslt.setTitle("MESSAGE");
							paramslt.setFunctionality(1);
							CustomAlert cAlert  = new CustomAlert(paramslt);						
						}	
					}


				}
				catch(Exception e)
				{
					Log.d(TAG, e.toString()); 
				}
			}
		});	

		btnProcess.setOnClickListener(new OnClickListener() {  
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub		
				try
				{
					if(btnProcess.getText().equals(ConstantClass.strProcessText))
					{

						int a = ddlBillConsumStatus.getSelectedItemPosition();
						DDLItem k = (DDLItem)alConStatusItem.get(a);
						if(isValid(k))//Validation  
						{							
							//CONSUMPTION Calculation====================================================
							Calculate clt =new Calculate(mcx);
							clt.consumption();
							if(SlabColl.getmUnits().trim().indexOf(".") != -1)
							{
								String[] ss = SlabColl.getmUnits().trim().split("\\.");
								lblConsumption.setText(ss[0]);
							}
							else
							{
								lblConsumption.setText(SlabColl.getmUnits().trim());
							}
							//Tamilselvan on 29-04-2014
							//Checking consumption is abnormal or not=================================================
							try
							{
								if(SlabColl.getmBillable().equals("Y") && new BigDecimal(SlabColl.getmUnits().trim()).compareTo(new BigDecimal(100)) > 0)
								{
									if(SlabColl.getmPreStatus().equals("0") || SlabColl.getmPreStatus().equals("3") || SlabColl.getmPreStatus().equals("4"))
									{
										BigDecimal avgCon = new BigDecimal(SlabColl.getmAvgCons().trim()).multiply(new BigDecimal(3)).multiply(new BigDecimal(SlabColl.getmDlCount()));
										if(new BigDecimal(SlabColl.getmUnits().trim()).compareTo(avgCon) > 0)
										{
											CustomToast.makeText(BillingConsumption_LandT.this, "ABNORMAL CONSUMPTION " + eol +" RECORDED.", Toast.LENGTH_SHORT);
											SlabColl.setmAbFlag("1");
										}/**/
									}
								}
							}
							catch(Exception e)
							{
								Log.d(TAG, e.toString()); 
							}
							//Checking consumption is abnormal or not=================================================							
							//END CONSUMPTION Calculation================================================

							//FC Calculation=============================================================
							clt.fc();
							//END FC Calculation=========================================================

							//EC Calculation=============================================================
							SlabColl.setmEcsFlag("4"); //25-06-2016
							cbCalc = new Checkbox(BillingConsumption_LandT.this);//Added by Tamilselvan on 17-03-2014
							cbCalc.EC_Calculation();//Added by Tamilselvan on 17-03-2014
							//END EC Calculation=========================================================

							//PF Calculation=============================================================
							/*BigDecimal pfValue = new BigDecimal(edtPowerFactor.getText().toString().length() == 0 ? "0.00" : edtPowerFactor.getText().toString());
							cbCalc.PF_Calculation(pfValue);//Added by Tamilselvan on 24-03-2014
							if(edtPowerFactor.isEnabled())
							{
								SlabColl.setmPF(edtPowerFactor.getText().toString());
							}*/
							//END PF Calculation=========================================================

							//MD Calculation=============================================================							
							//BigDecimal mdValue = new BigDecimal(edtMD.getText().toString().length() == 0 ? "0.00" : edtMD.getText().toString());
							//cbCalc.MD_Calculation(mdValue);//Added by Tamilselvan on 19-03-2014

							//END MD Calculation=========================================================

							//25-06-2016
							try
							{
								if(cbCalc.pf_required())
								{
									BigDecimal pfValue = new BigDecimal(LandT.getmPF()).setScale(2, RoundingMode.HALF_UP);
									cbCalc.PF_Calculation(pfValue);
								}
								else
								{
									cbCalc.PF_Calculation(new BigDecimal(0.00));
								}

							}
							catch(Exception e)
							{
								cbCalc.PF_Calculation(new BigDecimal(0.00));
							}

							try
							{
								//LandT.setmTarifWiseMD("2.15");
								//CustomToast.makeText(BillingConsumption_LandT.this, "MD: " + LandT.getmTarifWiseMD(), Toast.LENGTH_SHORT);
								//if(cbCalc.md_required())
								{

									/*if(LandT.getmTarifWiseMD().trim().contains("kW"))
									{
										String replacedMD = LandT.getmTarifWiseMD().replace("kW", "");
										BigDecimal mdValue =  new BigDecimal(replacedMD).setScale(2, RoundingMode.HALF_UP);
										cbCalc.MD_Calculation(mdValue);
									}
									else
									{
										cbCalc.MD_Calculation(new BigDecimal(0.00));
									}*/
									
									//Added 08-07-2021
									String replacedMD = LandT.getmTarifWiseMD().trim().toUpperCase().replace("KW", "");
									BigDecimal mdValue =  new BigDecimal(replacedMD).setScale(2, RoundingMode.HALF_UP);
									cbCalc.MD_Calculation(mdValue);
								}
								//else
								//{
								//	cbCalc.MD_Calculation(new BigDecimal(0.00));
								//}
							}
							catch(Exception e)
							{
								cbCalc.MD_Calculation(new BigDecimal(0.00));
							}
							//End 25-06-2016

							//REBATE Calculation=========================================================
							clt.rebate();
							//END REBATE Calculation=====================================================

							//OTHERS Calculation=========================================================
							//END OTHERS Calculation=====================================================

							//TAX Calculation============================================================							
							//clt.tax();
							clt.Tax_Calc_New();
							//END TAX Calculation========================================================

							//BILL TOTAL Calculation=====================================================
							cbCalc = new Checkbox(BillingConsumption_LandT.this);//Added by Tamilselvan on 17-03-2014
							cbCalc.TotBillCal();//Added by Tamilselvan on 19-03-2014
							//END BILL TOTAL Calculation=================================================
							//BigDecimal txtOther = new BigDecimal(SlabColl.getmOthers()).add(SlabColl.getmKVAAssd_Cons().setScale(2, BigDecimal.ROUND_HALF_UP));
							txtBillConsumBillAmount.setText(SlabColl.getmBillTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());//Added by Tamilselvan on 19-03-2014
							btnProcess.setText(ConstantClass.strResetText);
							//btnProcess.setVisibility(Button.INVISIBLE);
							btnSave.setVisibility(Button.VISIBLE);
							ddlBillConsumStatus.setEnabled(false);
							edtBillReading.setEnabled(false);
							edtPowerFactor.setEnabled(false); 
							edtMD.setEnabled(false);
						}
					}
					else if (btnProcess.getText().equals(ConstantClass.strResetText))
					{

						BillingObject.reset(BillingConsumption_LandT.this);				
						BillingObject.GetBillingObject().setmPreStatus(String.valueOf(ddlBillConsumStatus.getId()));						
						LoadInitialDetails();//Load dropdownlist 
						btnProcess.setText(ConstantClass.strProcessText);
						btnSave.setVisibility(Button.INVISIBLE);
						ddlBillConsumStatus.setSelection(0);
						edtBillReading.setText("");
						edtPowerFactor.setText("");
						edtMD.setText("");
						lblConsumption.setText("");
						txtBillConsumBillAmount.setText("");
						ddlBillConsumStatus.setEnabled(true);
						edtBillReading.setEnabled(true);
						edtPowerFactor.setEnabled(false);
						edtMD.setEnabled(false);

						//25-06-2016
						saveDialogOwner = "0";
						saveDialogPhone="";
						saveDialogEmail="";
						saveDialogAadhar="";
						SlabColl.setmPreRead("");//28-02-2015
					}
					//WriteLog.WriteLogError(TAG+" Process btn finished");
				}
				catch(Exception e)
				{
					Log.d(TAG, e.toString()); 
				}
			}
		});

		//Created By Tamilselvan on 28-02-2014
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//Modified 27-07-2016

				//Modified 29-01-2021
				try
				{

					if(BillingObject.GetBillingObject().getmMtd().trim().equals("1"))
					{
						if(BillingObject.GetBillingObject().getmPreStatus().trim().equals("0") ||  BillingObject.GetBillingObject().getmPreStatus().trim().equals("4"))
						{
							if(BillingObject.GetBillingObject().getmPreRead().trim().equals(""))
							{
								CustomToast.makeText(BillingConsumption_LandT.this,"Error in Final Reading.Please Bill Again"  , Toast.LENGTH_SHORT);
								Intent billingPage = new Intent(BillingConsumption_LandT.this, Billing_LandT.class);
								startActivity(billingPage);
								finish();
								return;
							}
							else if(new BigDecimal(BillingObject.GetBillingObject().getmPreRead().trim()).compareTo(new BigDecimal(0.00))<=0)
							{
								CustomToast.makeText(BillingConsumption_LandT.this,"Error in Final Reading.Please Bill Again"  , Toast.LENGTH_SHORT);
								Intent billingPage = new Intent(BillingConsumption_LandT.this, Billing_LandT.class);
								startActivity(billingPage);
								finish();
								return;	
							}						
						}
					}

				}
				catch(Exception e)
				{

				}

				try
				{
					SaveFunction();
				}
				catch(Exception e)
				{
					SaveFunction();
				}					
			}
		});
		/*//17-05-2016
		if(Billing_LandT.LTLG==1)
		{
			ReadLT();
		}
		else
		{			
			ReadLG();
		}*/

		//25-01-2021
		//Billing_LandT.LTLG =9; //Test 15-03-2021
		if(Billing_LandT.LTLG==1 || Billing_LandT.LTLG==7) //04-09-2017
		{
			ReadLT();
		}
		else if(Billing_LandT.LTLG==2)
		{			
			ReadLG();
		}
		/*else if(Billing_LandT.LTLG==7)
							ReadAllLT(); //Read L&T All Parameters
		 */		
		else if(Billing_LandT.LTLG==8) //31-07-2017
		{
			ReadDLMSHPL(); //Read L&T All Parameters
		}
		else
		{
			ReadDLMS(); //Nitish 23-12-2016
		}

	}
	//Modified 12-05-2021 Handshake,MtrslNo(1),KWH(2),MD(2),PF(2)
	public void ReadDLMSHPL()
	{
		try
		{
			/*
				 7E A0 1A 41 03 52 E9 3D E6 E7 00 C4 01 81 00 09 08 42 45 53 37 39 36 33 38 32 30 7E //27
				 7E A0 11 41 03 74 C8 B9 E6 E7 00 C4 01 81 01 03 05 84 7E
				 7E A0 15 41 03 96 38 0F E6 E7 00 C4 01 81 00 06 00 00 02 FD A1 C0 7E
				 7E A0 16 41 03 B8 89 E2 E6 E7 00 C4 01 81 00 02 02 0F F9 16 21 CE 68 7E
				 7E A0 15 41 03 DA 50 87 E6 E7 00 C4 01 81 00 06 00 00 02 A5 6C 1E 7E
				 7E A0 16 41 03 FC A9 E6 E6 E7 00 C4 01 81 00 02 02 0F F9 16 21 CE 68 7E
				 7E A0 15 41 03 1E 78 07 E6 E7 00 C4 01 81 00 06 00 00 04 86 25 59 7E
				 7E A0 16 41 03 30 C9 EA E6 E7 00 C4 01 81 00 02 02 0F F9 16 21 CE 68 7E
				 7E A0 15 41 03 52 10 8F E6 E7 00 C4 01 81 00 06 00 00 EF 3C C5 4E 7E
				 7E A0 16 41 03 74 E9 EE E6 E7 00 C4 01 81 00 02 02 0F FB 16 23 64 FE 7E
				 7E A0 15 41 03 96 38 0F E6 E7 00 C4 01 81 00 06 00 00 F9 A3 FA E3 7E
				 7E A0 16 41 03 B8 89 E2 E6 E7 00 C4 01 81 00 02 02 0F FB 16 23 64 FE 7E
				 7E A0 15 41 03 DA 50 87 E6 E7 00 C4 01 81 00 06 01 4D 78 2C A3 EF 7E
				 7E A0 16 41 03 FC A9 E6 E6 E7 00 C4 01 81 00 02 02 0F FB 16 23 64 FE 7E
				 7E A0 15 41 03 1E 78 07 E6 E7 00 C4 01 81 00 05 00 00 03 E8 99 83 7E
				 7E A0 16 41 03 30 C9 EA E6 E7 00 C4 01 81 00 02 02 0F FD 16 FF 5C 34 7E
				 7E A0 15 41 03 52 10 8F E6 E7 00 C4 01 81 00 06 00 00 C2 D3 77 C2 7E
				 7E A0 16 41 03 74 E9 EE E6 E7 00 C4 01 81 00 02 02 0F FD 16 2C 4A D0 7E
				 7E A0 11 41 03 96 D4 7D E6 E7 00 C4 01 81 01 04 BA F0 7E
				 7E A0 11 41 03 B8 A8 B5 E6 E7 00 C4 01 81 01 04 BA F0 7E
				 7E A0 15 41 03 DA 50 87 E6 E7 00 C4 01 81 00 06 00 00 00 14 DE 89 7E
				 7E A0 16 41 03 FC A9 E6 E6 E7 00 C4 01 81 00 02 02 0F FF 16 1E 63 77 7E
			 */	
			int mcount = 0;
			String val = getIntent().getStringExtra("Key");	
			//String val="LT";
			if(val==null)
			{

			}
			else if(val.equals("LT0"))
			{
				Toast.makeText(BillingConsumption_LandT.this, "Device Not Found.", Toast.LENGTH_SHORT).show();

			}

			else if(val.equals("LT1"))
			{
				Toast.makeText(BillingConsumption_LandT.this, "Error Data 1.", Toast.LENGTH_SHORT).show();
			}
			else if(val.equals("LT2"))
			{
				Toast.makeText(BillingConsumption_LandT.this, "Error Data 2.", Toast.LENGTH_SHORT).show();
			}
			else if(val.equals("LT3"))
			{
				Toast.makeText(BillingConsumption_LandT.this, "Error Data 3.", Toast.LENGTH_SHORT).show();
			}
			else if(val.equals("LT4"))
			{
				Toast.makeText(BillingConsumption_LandT.this, "Error File Write.", Toast.LENGTH_SHORT).show();
			}
			else if(val.equals("LT5") || val.equals("LT6"))
			{
				Toast.makeText(BillingConsumption_LandT.this, "Device Not Connected.", Toast.LENGTH_SHORT).show();
			}

			else if(val.equals("LT"))
			{				
				StringBuilder sbLT=new StringBuilder();
				StringBuilder sbDB = new StringBuilder(); 
				LandT = new LandTObject();
				CommonFunction cFun = new CommonFunction();

				String Filepath = Environment.getExternalStorageDirectory().getPath()+"/Download/DLMS_Output.txt";
				FileInputStream iStreamLT = new FileInputStream(Filepath);
				InputStreamReader ipStreamReaderLT = new InputStreamReader(iStreamLT);		
				BufferedReader buffReaderLT =   new BufferedReader(ipStreamReaderLT);	
				String ReceiveStr;					
				String buff = "";
				String Data = "";
				int bin = 0;
				int loopcount = 0;

				while((ReceiveStr=buffReaderLT.readLine()) != null)//While Loop							
				{
					loopcount = loopcount + 1;
					sbLT.delete(0, sbLT.length());
					sbLT.append(ReceiveStr);

					buff = ReceiveStr.trim();					
					String OBISRes[] = buff.split(" ");
					int[] OB = new int[OBISRes.length];
					for(int i =0;i<OBISRes.length;i++)
					{
						OB[i] = Integer.parseInt(OBISRes[i],16);
					}					
					int nframelength = OB[2];
					int[] Decodedata = new int[nframelength - 14];
					//7E A0 1A 41 03 52 E9 3D E6 E7 00 C4 01 81 00 09 08 42 45 53 37 36 31 31 38 7E
					try
					{
						System.arraycopy(OB, 15, Decodedata, 0, (nframelength - 14));
					}
					catch(Exception e)
					{

						System.arraycopy(OB, 15, Decodedata, 0, (nframelength - 16));
					}
					if(loopcount == 3)//Meter Serial Number
					{

						if(OB[14] == 1)
						{
							//Toast.makeText(BillingConsumption_LandT.this, "Error Frame Recieved.Retry Again.", Toast.LENGTH_SHORT).show();
							//Intent i = new Intent(LoginActivity.this,LoginActivity.class);
							//startActivity(i);
						}
						else
						{
							/*for(int i = 0;i < Decodedata.length - 2;i++)
							{
								Data = Data + (char)Decodedata[i];
							}*/
							bin = 0x00000000;
							bin = Decodedata[1];
							switch (Decodedata[0])
							{
							case 15: // For one byte
								if(bin > 127)
									bin=bin-256;
								break;
							case 17:// For one byte
								break;
							case 16:
								bin = (bin << 8 | Decodedata[2]);
								if(bin > 32768)
									bin= bin-65536;
								break;
							case 18:
								bin = (bin << 8 | Decodedata[2]);
								break;
							default:
								for (int k = 2; k < 5; k++)
									bin = (bin << 8 | Decodedata[k]);
								break;
							}

							LandT.setmMeterSerialNo(String.valueOf(bin).trim());
							Data = "";
						}


					}
					/*else if(loopcount == 5)
					{						
						if(OB[14] == 1)
						{
							Toast.makeText(BillingConsumption_LandT.this, "Error Frame Recieved.Retry Again.", Toast.LENGTH_SHORT).show();
							//Intent i = new Intent(LoginActivity.this,LoginActivity.class);
							//startActivity(i);
						}
						else
						{							
							String MSDate = String.valueOf((Decodedata[2] << 8) | Decodedata[3]);
							//DATE = MSDate;
							MSDate = String.valueOf(Decodedata[5])+ "-" + String.valueOf(Decodedata[4]) + "-" +   MSDate ;
							String MSTime = String.valueOf(Decodedata[7])  + ":" +String.valueOf(Decodedata[8])  + ":" + String.valueOf(Decodedata[9]) ;							
							LandT.setmMeterDate(MSDate.trim());
							LandT.setmMeterTime(MSTime.trim());
							Data = "";
						}

					}*/
					else if(loopcount == 4 || loopcount == 5)//KWh
					{
						if(OB[14] == 1)
						{
							Toast.makeText(BillingConsumption_LandT.this, "Error Frame Recieved.Retry Again.", Toast.LENGTH_SHORT).show();
							//Intent i = new Intent(LoginActivity.this,LoginActivity.class);
							//startActivity(i);
						}
						else if(loopcount == 4)
						{
							Data = "";
							bin = 0x00000000;
							bin = Decodedata[1];
							switch (Decodedata[0])
							{
							case 15: // For one byte
								if(bin > 127)
									bin=bin-256;
								break;
							case 17:// For one byte
								break;
							case 16:
								bin = (bin << 8 | Decodedata[2]);
								if(bin > 32768)
									bin= bin-65536;
								break;
							case 18:
								bin = (bin << 8 | Decodedata[2]);
								break;
							default:
								for (int k = 2; k < 5; k++)
									bin = (bin << 8 | Decodedata[k]);
								break;
							}
							//28-11-2016
							/*if(Decodedata[0]==15 || Decodedata[0]==17)
								{

								}
								else if(Decodedata[0]==16 || Decodedata[0]==18)
								{
									bin = (bin << 8 | Decodedata[2]);
								}
								else
								{
									for (int k = 2; k < 5; k++)
										bin = (bin << 8 | Decodedata[k]);
								}*/
							//End 28-11-2016		
							Data = (String.valueOf(bin));
						}
						else if(loopcount == 5)
						{
							bin = 0;
							int power = Decodedata[3];
							if(power > 127)
								power = power - 256;
							power = power - 3; //Pavan 16-12-2016
							Data = String.valueOf(Integer.valueOf(Data) *  Math.pow(10, power));
							Data = String.valueOf(new BigDecimal(Data).setScale(3, RoundingMode.HALF_UP));
							LandT.setmTotalCummulativeEnergy(Data.toString().trim());
							mcount =1;
						}
					}
					/*else if(loopcount == 9 || loopcount == 10)//KVAH
					{
						if(OB[14] == 1)
						{
							Toast.makeText(BillingConsumption_LandT.this, "Error Frame Recieved.Retry Again.", Toast.LENGTH_SHORT).show();
							//Intent i = new Intent(LoginActivity.this,LoginActivity.class);
							//startActivity(i);
						}
						else if(loopcount == 9)
						{
							Data = "";
							bin = 0x00000000;
							bin = Decodedata[1];
							switch (Decodedata[0])
							{
							case 15: // For one byte
								if(bin > 127)
									bin=bin-256;
								break;
							case 17:// For one byte
								break;
							case 16:
								bin = (bin << 8 | Decodedata[2]);
								if(bin > 32768)
									bin= bin-65536;
								break;
							case 18:
								bin = (bin << 8 | Decodedata[2]);
								break;
							default:
								for (int k = 2; k < 5; k++)
									bin = (bin << 8 | Decodedata[k]);
								break;
							}
							//28-11-2016
							if(Decodedata[0]==15 || Decodedata[0]==17)
								{

								}
								else if(Decodedata[0]==16 || Decodedata[0]==18)
								{
									bin = (bin << 8 | Decodedata[2]);
								}
								else
								{
									for (int k = 2; k < 5; k++)
										bin = (bin << 8 | Decodedata[k]);
								}
							//End 28-11-2016		
							Data = (String.valueOf(bin));
						}
						else if(loopcount == 10)
						{
							bin = 0;
							int power = Decodedata[3];
							if(power > 127)
								power = power - 256;							
							power = power - 3; //Pavan 16-12-2016
							Data = String.valueOf(Integer.valueOf(Data) *  Math.pow(10, power));
							Data = String.valueOf(new BigDecimal(Data).setScale(3, RoundingMode.HALF_UP));
							LandT.setmCummulativeEnergyKVAH(Data.toString().trim());
							mcount =1;
						}
					}*/
					else if(loopcount == 6 || loopcount == 7)//KW
					{
						if(OB[14] == 1)
						{
							Toast.makeText(BillingConsumption_LandT.this, "Error Frame Recieved.Retry Again.", Toast.LENGTH_SHORT).show();
							//Intent i = new Intent(LoginActivity.this,LoginActivity.class);
							//startActivity(i);
						}
						else if(loopcount == 6)
						{
							Data = "";
							bin = 0x00000000;
							bin = Decodedata[1];

							switch (Decodedata[0])
							{
							case 15: // For one byte
								if(bin > 127)
									bin=bin-256;
								break;
							case 17:// For one byte
								break;
							case 16:
								bin = (bin << 8 | Decodedata[2]);
								if(bin > 32768)
									bin= bin-65536;
								break;
							case 18:
								bin = (bin << 8 | Decodedata[2]);
								break;
							default:
								for (int k = 2; k < 5; k++)
									bin = (bin << 8 | Decodedata[k]);
								break;
							}

							//28-11-2016
							/*if(Decodedata[0]==15 || Decodedata[0]==17)
								{

								}
								else if(Decodedata[0]==16 || Decodedata[0]==18)
								{
									bin = (bin << 8 | Decodedata[2]);
								}
								else
								{
									for (int k = 2; k < 5; k++)
										bin = (bin << 8 | Decodedata[k]);
								}*/
							//End 28-11-2016						
							Data = (String.valueOf(bin));
							try
							{
								if(Decodedata[1]==255 && (Decodedata[2]==255 || Decodedata[2]==254))
								{
									Data = "a";									
								}
							}
							catch(Exception e)
							{
								Data = (String.valueOf(bin));
							}
						}
						else if(loopcount == 7)
						{
							if(Data.equals("a"))
							{
								LandT.setmTarifWiseMD(" ");
							}
							else
							{
								bin = 0;
								int power = Decodedata[3];
								if(power > 127)
									power = power - 256;								
								power = power-3; //Pavan 16-12-2016
								Data = String.valueOf(Integer.valueOf(Data) *  Math.pow(10, power));
								Data = String.valueOf(new BigDecimal(Data).setScale(4, RoundingMode.HALF_UP));
								LandT.setmTarifWiseMD(Data.toString().trim());
							}
							mcount =1;
						}
					}
					else if(loopcount == 8 || loopcount == 9)
					{
						if(OB[14] == 1)
						{
							Toast.makeText(BillingConsumption_LandT.this, "Error Frame Recieved.Retry Again.", Toast.LENGTH_SHORT).show();
							//Intent i = new Intent(LoginActivity.this,LoginActivity.class);
							//startActivity(i);
						}
						else if(loopcount ==8)
						{

							bin = 0x00000000;
							bin = Decodedata[1];

							//16-12-2016
							switch (Decodedata[0])
							{
							case 15: // For one byte
								if(bin > 127)
									bin=bin-256;
								break;
							case 17:// For one byte
								break;
							case 16:
								bin = (bin << 8 | Decodedata[2]);
								if(bin > 32768)
									bin= bin-65536;
								break;
							case 18:
								bin = (bin << 8 | Decodedata[2]);
								break;
							default:
								for (int k = 2; k < 5; k++)
									bin = (bin << 8 | Decodedata[k]);
								break;
							}
							//28-11-2016
							/*if(Decodedata[0]==15 || Decodedata[0]==17)
								{

								}
								else if(Decodedata[0]==16 || Decodedata[0]==18)
								{
									bin = (bin << 8 | Decodedata[2]);
								}
								else
								{
								for (int k = 2; k < 5; k++)
												bin = (bin << 8 | Decodedata[k]);

								}*/
							//End 28-11-2016	
							Data = (String.valueOf(bin));
						}
						else if(loopcount == 9)
						{
							bin = 0;
							int power = Decodedata[3];
							if(power > 127)
								power = power - 256;						
							Data = String.valueOf(Integer.valueOf(Data) *  Math.pow(10, power));
							LandT.setmPF(Data.toString().trim());
						}
					}					
				}//END While Loop				
				if(mcount==1)
				{
					try
					{


						String pf = "";								
						try
						{
							if(LandT.getmPF() == null)
							{
								pf = "";
							}
							else
							{

								if(new BigDecimal(LandT.getmPF()).compareTo(new BigDecimal(1.0)) >0)
								{
									pf = "";
								}
								else
								{
									pf = LandT.getmPF();
								}
							}
						}
						catch(Exception e)
						{
							pf = "";
						}
						edtBillReading.setEnabled(true);
						//0.73KWh -->  0.73 * 1000 = 730
						if(LandT.getmTotalCummulativeEnergy()!=null)
						{
							readingnew = new BigDecimal(LandT.getmTotalCummulativeEnergy().substring(0, LandT.getmTotalCummulativeEnergy().length()-3)).setScale(0, RoundingMode.HALF_UP).toString();
						}
						edtBillReading.setText(readingnew); 
						//edtBillReading.setEnabled(false);

						//LandT.setmManufacturer(J2xxHyperTerm.slno);
						//LandT.setmMeterSerialNo(J2xxHyperTerm.slno);
						LandT.setmMeterType(String.valueOf(Billing_LandT.LTLG)); 
						if(Billing_LandT.PROBETYPE!=1) //Old PROBE
						{
							Billing_LandT.LTLG = 0;
						}
						StringBuilder sb = new StringBuilder();
						sb.append("METER ID	: "+ (LandT.getmManufacturer() == null ? " " : LandT.getmManufacturer()) + eol);
						//sb.append(eol);
						sb.append("METER SERIALNO: "+ (LandT.getmMeterSerialNo() == null ? " " : LandT.getmMeterSerialNo()) + eol);
						sb.append("METER DATE: "+ (LandT.getmMeterDate() == null?" ":LandT.getmMeterDate() )  + eol);
						sb.append("METER TIME: "+ (LandT.getmMeterTime() == null ? " " : LandT.getmMeterTime() )+ eol);
						sb.append(eol);						
						sb.append("METER READING: "+ (LandT.getmTotalCummulativeEnergy() == null? " " : LandT.getmTotalCummulativeEnergy())+ eol);
						sb.append("MD: "+( LandT.getmTarifWiseMD() == null ? " " : LandT.getmTarifWiseMD() )+ eol);
						sb.append("PF: "+( LandT.getmPF() == null ? " " : LandT.getmPF())+ eol);


						CustomAlert.CustomAlertParameters paramslt = new CustomAlert.CustomAlertParameters(); 
						paramslt.setContext(BillingConsumption_LandT.this);					
						paramslt.setMainHandler(dh);
						paramslt.setMsg(sb.toString());
						paramslt.setButtonOkText("OK");
						paramslt.setTitle("METER DETAILS");
						paramslt.setFunctionality(-1);
						CustomAlert cAlert  = new CustomAlert(paramslt);				

					}
					catch(Exception e)
					{

						Toast.makeText(BillingConsumption_LandT.this, "Problem in Parsing.", Toast.LENGTH_SHORT).show();
					}
				}
				if(buffReaderLT != null)
				{
					try {
						buffReaderLT.close();									
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(iStreamLT != null)
				{
					try {
						iStreamLT.close();
					} 
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString()); 
		}
		finally
		{
			//04-05-2015
			btnReadLT.setEnabled(false);
			String mFilepath = Environment.getExternalStorageDirectory().getPath()+"/Download/DLMS_Output.txt";
			File f = new File(mFilepath);	
			//File to = new File(mFilepath)
			//f.renameTo(newPath)
			if(f.exists())
				f.delete();
		}
	}

	//Modified 12-05-2021 Handshake,MtrslNo(1),KWH(2),MD(2),PF(2)
	public void ReadDLMS()
	{
		try
		{
			/*
			 7E A0 1A 41 03 52 E9 3D E6 E7 00 C4 01 81 00 09 08 42 45 53 37 39 36 33 38 32 30 7E //27
			 7E A0 11 41 03 74 C8 B9 E6 E7 00 C4 01 81 01 03 05 84 7E
			 7E A0 15 41 03 96 38 0F E6 E7 00 C4 01 81 00 06 00 00 02 FD A1 C0 7E
			 7E A0 16 41 03 B8 89 E2 E6 E7 00 C4 01 81 00 02 02 0F F9 16 21 CE 68 7E
			 7E A0 15 41 03 DA 50 87 E6 E7 00 C4 01 81 00 06 00 00 02 A5 6C 1E 7E
			 7E A0 16 41 03 FC A9 E6 E6 E7 00 C4 01 81 00 02 02 0F F9 16 21 CE 68 7E
			 7E A0 15 41 03 1E 78 07 E6 E7 00 C4 01 81 00 06 00 00 04 86 25 59 7E
			 7E A0 16 41 03 30 C9 EA E6 E7 00 C4 01 81 00 02 02 0F F9 16 21 CE 68 7E
			 7E A0 15 41 03 52 10 8F E6 E7 00 C4 01 81 00 06 00 00 EF 3C C5 4E 7E
			 7E A0 16 41 03 74 E9 EE E6 E7 00 C4 01 81 00 02 02 0F FB 16 23 64 FE 7E
			 7E A0 15 41 03 96 38 0F E6 E7 00 C4 01 81 00 06 00 00 F9 A3 FA E3 7E
			 7E A0 16 41 03 B8 89 E2 E6 E7 00 C4 01 81 00 02 02 0F FB 16 23 64 FE 7E
			 7E A0 15 41 03 DA 50 87 E6 E7 00 C4 01 81 00 06 01 4D 78 2C A3 EF 7E
			 7E A0 16 41 03 FC A9 E6 E6 E7 00 C4 01 81 00 02 02 0F FB 16 23 64 FE 7E
			 7E A0 15 41 03 1E 78 07 E6 E7 00 C4 01 81 00 05 00 00 03 E8 99 83 7E
			 7E A0 16 41 03 30 C9 EA E6 E7 00 C4 01 81 00 02 02 0F FD 16 FF 5C 34 7E
			 7E A0 15 41 03 52 10 8F E6 E7 00 C4 01 81 00 06 00 00 C2 D3 77 C2 7E
			 7E A0 16 41 03 74 E9 EE E6 E7 00 C4 01 81 00 02 02 0F FD 16 2C 4A D0 7E
			 7E A0 11 41 03 96 D4 7D E6 E7 00 C4 01 81 01 04 BA F0 7E
			 7E A0 11 41 03 B8 A8 B5 E6 E7 00 C4 01 81 01 04 BA F0 7E
			 7E A0 15 41 03 DA 50 87 E6 E7 00 C4 01 81 00 06 00 00 00 14 DE 89 7E
			 7E A0 16 41 03 FC A9 E6 E6 E7 00 C4 01 81 00 02 02 0F FF 16 1E 63 77 7E
			 */	
			int mcount = 0;
			String val = getIntent().getStringExtra("Key");	
			//String val="LT"; //Test 15-03-2021
			if(val==null)
			{

			}
			else if(val.equals("LT0"))
			{
				Toast.makeText(BillingConsumption_LandT.this, "Device Not Found.", Toast.LENGTH_SHORT).show();

			}

			else if(val.equals("LT1"))
			{
				Toast.makeText(BillingConsumption_LandT.this, "Error Data 1.", Toast.LENGTH_SHORT).show();
			}
			else if(val.equals("LT2"))
			{
				Toast.makeText(BillingConsumption_LandT.this, "Error Data 2.", Toast.LENGTH_SHORT).show();
			}
			else if(val.equals("LT3"))
			{
				Toast.makeText(BillingConsumption_LandT.this, "Error Data 3.", Toast.LENGTH_SHORT).show();
			}
			else if(val.equals("LT4"))
			{
				Toast.makeText(BillingConsumption_LandT.this, "Error File Write.", Toast.LENGTH_SHORT).show();
			}
			else if(val.equals("LT5") || val.equals("LT6"))
			{
				Toast.makeText(BillingConsumption_LandT.this, "Device Not Connected.", Toast.LENGTH_SHORT).show();
			}

			else if(val.equals("LT"))
			{				
				StringBuilder sbLT=new StringBuilder();
				StringBuilder sbDB = new StringBuilder(); 
				LandT = new LandTObject();
				CommonFunction cFun = new CommonFunction();

				String Filepath = Environment.getExternalStorageDirectory().getPath()+"/Download/DLMS_Output.txt";
				
				//To Check Line Count if line count is less than 9 response not received in proper format //08-07-2021
				FileInputStream iStreamLT1 = null;
				InputStreamReader ipStreamReaderLT1=null;
				BufferedReader buffReaderLT1 = null;
				try
				{

					int lineCount = 0; 
					iStreamLT1 = new FileInputStream(Filepath);
					ipStreamReaderLT1 = new InputStreamReader(iStreamLT1);	
					buffReaderLT1 =   new BufferedReader(ipStreamReaderLT1);	
					while (buffReaderLT1.readLine() != null) {
						lineCount++;

					}
					if(lineCount<9)
					{
						Toast.makeText(BillingConsumption_LandT.this, "Response not in Proper Format.", Toast.LENGTH_LONG).show();
						return;
					}

				}
				catch(Exception e)
				{

				}
				if(buffReaderLT1 != null)
				{
					try {
						buffReaderLT1.close();									
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(iStreamLT1 != null)
				{
					try {
						iStreamLT1.close();
					} 
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				//End 08-07-2021
				
				
				FileInputStream iStreamLT = new FileInputStream(Filepath);
				InputStreamReader ipStreamReaderLT = new InputStreamReader(iStreamLT);		
				BufferedReader buffReaderLT =   new BufferedReader(ipStreamReaderLT);	
				String ReceiveStr;					
				String buff = "";
				String Data = "";
				int bin = 0;
				int loopcount = 0;

				while((ReceiveStr=buffReaderLT.readLine()) != null)//While Loop							
				{
					loopcount = loopcount + 1;
					sbLT.delete(0, sbLT.length());
					sbLT.append(ReceiveStr);

					buff = ReceiveStr.trim();					
					String OBISRes[] = buff.split(" ");
					int[] OB = new int[OBISRes.length];
					for(int i =0;i<OBISRes.length;i++)
					{
						OB[i] = Integer.parseInt(OBISRes[i],16);
					}					
					int nframelength = OB[2];
					int[] Decodedata = new int[nframelength - 14];
					//7E A0 1A 41 03 52 E9 3D E6 E7 00 C4 01 81 00 09 08 42 45 53 37 36 31 31 38 7E
					try
					{
						System.arraycopy(OB, 15, Decodedata, 0, (nframelength - 14));
					}
					catch(Exception e)
					{

						System.arraycopy(OB, 15, Decodedata, 0, (nframelength - 16));
					}
					if(loopcount == 3)//Meter Serial Number
					{
						if(OB[14] == 1)
						{
							Toast.makeText(BillingConsumption_LandT.this, "Error Frame Recieved.Retry Again.", Toast.LENGTH_SHORT).show();
							//Intent i = new Intent(LoginActivity.this,LoginActivity.class);
							//startActivity(i);
						}
						else
						{
							for(int i = 0;i < Decodedata.length - 2;i++)
							{
								Data = Data + (char)Decodedata[i];
							}
							LandT.setmMeterSerialNo(Data.trim());
							Data = "";
						}
					}
					/*else if(loopcount == 5)
					{						
						if(OB[14] == 1)
						{
							Toast.makeText(BillingConsumption_LandT.this, "Error Frame Recieved.Retry Again.", Toast.LENGTH_SHORT).show();
							//Intent i = new Intent(LoginActivity.this,LoginActivity.class);
							//startActivity(i);
						}
						else
						{							
							String MSDate = String.valueOf((Decodedata[2] << 8) | Decodedata[3]);
							//DATE = MSDate;
							MSDate = String.valueOf(Decodedata[5])+ "-" + String.valueOf(Decodedata[4]) + "-" +   MSDate ;
							String MSTime = String.valueOf(Decodedata[7])  + ":" +String.valueOf(Decodedata[8])  + ":" + String.valueOf(Decodedata[9]) ;							
							LandT.setmMeterDate(MSDate.trim());
							LandT.setmMeterTime(MSTime.trim());
							Data = "";
						}

					}*/
					else if(loopcount == 4 || loopcount == 5)//KWh
					{
						if(OB[14] == 1)
						{
							Toast.makeText(BillingConsumption_LandT.this, "Error Frame Recieved.Retry Again.", Toast.LENGTH_SHORT).show();
							//Intent i = new Intent(LoginActivity.this,LoginActivity.class);
							//startActivity(i);
						}
						else if(loopcount == 4)
						{
							Data = "";
							bin = 0x00000000;
							bin = Decodedata[1];
							switch (Decodedata[0])
							{
							case 15: // For one byte
								if(bin > 127)
									bin=bin-256;
								break;
							case 17:// For one byte
								break;
							case 16:
								bin = (bin << 8 | Decodedata[2]);
								if(bin > 32768)
									bin= bin-65536;
								break;
							case 18:
								bin = (bin << 8 | Decodedata[2]);
								break;
							default:
								for (int k = 2; k < 5; k++)
									bin = (bin << 8 | Decodedata[k]);
								break;
							}
							//28-11-2016
							/*if(Decodedata[0]==15 || Decodedata[0]==17)
							{

							}
							else if(Decodedata[0]==16 || Decodedata[0]==18)
							{
								bin = (bin << 8 | Decodedata[2]);
							}
							else
							{
								for (int k = 2; k < 5; k++)
									bin = (bin << 8 | Decodedata[k]);
							}*/
							//End 28-11-2016		
							Data = (String.valueOf(bin));
						}
						else if(loopcount == 5)
						{
							bin = 0;
							int power = Decodedata[3];
							if(power > 127)
								power = power - 256;
							power = power - 3; //Pavan 16-12-2016
							Data = String.valueOf(Integer.valueOf(Data) *  Math.pow(10, power));
							Data = String.valueOf(new BigDecimal(Data).setScale(3, RoundingMode.HALF_UP));
							LandT.setmTotalCummulativeEnergy(Data.toString().trim());
							mcount =1;
						}
					}
					/*else if(loopcount == 9 || loopcount == 10)//KVAH
					{
						if(OB[14] == 1)
						{
							Toast.makeText(BillingConsumption_LandT.this, "Error Frame Recieved.Retry Again.", Toast.LENGTH_SHORT).show();
							//Intent i = new Intent(LoginActivity.this,LoginActivity.class);
							//startActivity(i);
						}
						else if(loopcount == 9)
						{
							Data = "";
							bin = 0x00000000;
							bin = Decodedata[1];
							switch (Decodedata[0])
							{
							case 15: // For one byte
								if(bin > 127)
									bin=bin-256;
								break;
							case 17:// For one byte
								break;
							case 16:
								bin = (bin << 8 | Decodedata[2]);
								if(bin > 32768)
									bin= bin-65536;
								break;
							case 18:
								bin = (bin << 8 | Decodedata[2]);
								break;
							default:
								for (int k = 2; k < 5; k++)
									bin = (bin << 8 | Decodedata[k]);
								break;
							}
							//28-11-2016
							if(Decodedata[0]==15 || Decodedata[0]==17)
							{

							}
							else if(Decodedata[0]==16 || Decodedata[0]==18)
							{
								bin = (bin << 8 | Decodedata[2]);
							}
							else
							{
								for (int k = 2; k < 5; k++)
									bin = (bin << 8 | Decodedata[k]);
							}
							//End 28-11-2016		
							Data = (String.valueOf(bin));
						}
						else if(loopcount == 10)
						{
							bin = 0;
							int power = Decodedata[3];
							if(power > 127)
								power = power - 256;							
							power = power - 3; //Pavan 16-12-2016
							Data = String.valueOf(Integer.valueOf(Data) *  Math.pow(10, power));
							Data = String.valueOf(new BigDecimal(Data).setScale(3, RoundingMode.HALF_UP));
							LandT.setmCummulativeEnergyKVAH(Data.toString().trim());
							mcount =1;
						}
					}*/
					else if(loopcount == 6 || loopcount == 7)//KW
					{
						if(OB[14] == 1)
						{
							Toast.makeText(BillingConsumption_LandT.this, "Error Frame Recieved.Retry Again.", Toast.LENGTH_SHORT).show();
							//Intent i = new Intent(LoginActivity.this,LoginActivity.class);
							//startActivity(i);
						}
						else if(loopcount == 6)
						{
							Data = "";
							bin = 0x00000000;
							bin = Decodedata[1];

							switch (Decodedata[0])
							{
							case 15: // For one byte
								if(bin > 127)
									bin=bin-256;
								break;
							case 17:// For one byte
								break;
							case 16:
								bin = (bin << 8 | Decodedata[2]);
								if(bin > 32768)
									bin= bin-65536;
								break;
							case 18:
								bin = (bin << 8 | Decodedata[2]);
								break;
							default:
								for (int k = 2; k < 5; k++)
									bin = (bin << 8 | Decodedata[k]);
								break;
							}

							//28-11-2016
							/*if(Decodedata[0]==15 || Decodedata[0]==17)
							{

							}
							else if(Decodedata[0]==16 || Decodedata[0]==18)
							{
								bin = (bin << 8 | Decodedata[2]);
							}
							else
							{
								for (int k = 2; k < 5; k++)
									bin = (bin << 8 | Decodedata[k]);
							}*/
							//End 28-11-2016						
							Data = (String.valueOf(bin));
							try
							{
								if(Decodedata[1]==255 && (Decodedata[2]==255 || Decodedata[2]==254))
								{
									Data = "a";									
								}
							}
							catch(Exception e)
							{
								Data = (String.valueOf(bin));
							}
						}
						else if(loopcount == 7)
						{
							if(Data.equals("a"))
							{
								LandT.setmTarifWiseMD(" ");
							}
							else
							{
								bin = 0;
								int power = Decodedata[3];
								if(power > 127)
									power = power - 256;								
								power = power-3; //Pavan 16-12-2016
								Data = String.valueOf(Integer.valueOf(Data) *  Math.pow(10, power));
								Data = String.valueOf(new BigDecimal(Data).setScale(4, RoundingMode.HALF_UP));
								LandT.setmTarifWiseMD(Data.toString().trim());
							}
							mcount =1;
						}
					}
					else if(loopcount == 8 || loopcount == 9)
					{
						if(OB[14] == 1)
						{
							Toast.makeText(BillingConsumption_LandT.this, "Error Frame Recieved.Retry Again.", Toast.LENGTH_SHORT).show();
							//Intent i = new Intent(LoginActivity.this,LoginActivity.class);
							//startActivity(i);
						}
						else if(loopcount ==8)
						{

							bin = 0x00000000;
							bin = Decodedata[1];

							//16-12-2016
							switch (Decodedata[0])
							{
							case 15: // For one byte
								if(bin > 127)
									bin=bin-256;
								break;
							case 17:// For one byte
								break;
							case 16:
								bin = (bin << 8 | Decodedata[2]);
								if(bin > 32768)
									bin= bin-65536;
								break;
							case 18:
								bin = (bin << 8 | Decodedata[2]);
								break;
							default:
								for (int k = 2; k < 5; k++)
									bin = (bin << 8 | Decodedata[k]);
								break;
							}
							//28-11-2016
							/*if(Decodedata[0]==15 || Decodedata[0]==17)
							{

							}
							else if(Decodedata[0]==16 || Decodedata[0]==18)
							{
								bin = (bin << 8 | Decodedata[2]);
							}
							else
							{
							for (int k = 2; k < 5; k++)
											bin = (bin << 8 | Decodedata[k]);

							}*/
							//End 28-11-2016	
							Data = (String.valueOf(bin));
						}
						else if(loopcount == 9)
						{
							bin = 0;
							int power = Decodedata[3];
							if(power > 127)
								power = power - 256;						
							Data = String.valueOf(Integer.valueOf(Data) *  Math.pow(10, power));
							LandT.setmPF(Data.toString().trim());
						}
					}					
				}//END While Loop				
				if(mcount==1)
				{
					try
					{


						String pf = "";								
						try
						{
							if(LandT.getmPF() == null)
							{
								pf = "";
							}
							else
							{

								if(new BigDecimal(LandT.getmPF()).compareTo(new BigDecimal(1.0)) >0)
								{
									pf = "";
								}
								else
								{
									pf = LandT.getmPF();
								}
							}
						}
						catch(Exception e)
						{
							pf = "";
						}
						edtBillReading.setEnabled(true);
						//0.73KWh -->  0.73 * 1000 = 730
						if(LandT.getmTotalCummulativeEnergy()!=null)
						{
							readingnew = new BigDecimal(LandT.getmTotalCummulativeEnergy().substring(0, LandT.getmTotalCummulativeEnergy().length()-3)).setScale(0, RoundingMode.HALF_UP).toString();
						}
						edtBillReading.setText(readingnew); 
						//edtBillReading.setEnabled(false);

						//LandT.setmManufacturer(J2xxHyperTerm.slno);
						//LandT.setmMeterSerialNo(J2xxHyperTerm.slno);
						LandT.setmMeterType(String.valueOf(Billing_LandT.LTLG)); 
						if(Billing_LandT.PROBETYPE!=1) //Old PROBE //07-05-2021
						{
							Billing_LandT.LTLG = 0;
						}
						StringBuilder sb = new StringBuilder();
						sb.append("METER ID	: "+ (LandT.getmManufacturer() == null ? " " : LandT.getmManufacturer()) + eol);
						//sb.append(eol);
						sb.append("METER SERIALNO: "+ (LandT.getmMeterSerialNo() == null ? " " : LandT.getmMeterSerialNo()) + eol);
						sb.append("METER DATE: "+ (LandT.getmMeterDate() == null?" ":LandT.getmMeterDate() )  + eol);
						sb.append("METER TIME: "+ (LandT.getmMeterTime() == null ? " " : LandT.getmMeterTime() )+ eol);
						sb.append(eol);						
						sb.append("METER READING: "+ (LandT.getmTotalCummulativeEnergy() == null? " " : LandT.getmTotalCummulativeEnergy())+ eol);
						sb.append("MD: "+( LandT.getmTarifWiseMD() == null ? " " : LandT.getmTarifWiseMD() )+ eol);
						sb.append("PF: "+( LandT.getmPF() == null ? " " : LandT.getmPF())+ eol);


						CustomAlert.CustomAlertParameters paramslt = new CustomAlert.CustomAlertParameters(); 
						paramslt.setContext(BillingConsumption_LandT.this);					
						paramslt.setMainHandler(dh);
						paramslt.setMsg(sb.toString());
						paramslt.setButtonOkText("OK");
						paramslt.setTitle("METER DETAILS");
						paramslt.setFunctionality(-1);
						CustomAlert cAlert  = new CustomAlert(paramslt);				

					}
					catch(Exception e)
					{

						Toast.makeText(BillingConsumption_LandT.this, "Problem in Parsing.", Toast.LENGTH_SHORT).show();
					}
				}
				if(buffReaderLT != null)
				{
					try {
						buffReaderLT.close();									
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(iStreamLT != null)
				{
					try {
						iStreamLT.close();
					} 
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString()); 
		}
		finally
		{
			//04-05-2015
			btnReadLT.setEnabled(false);
			String mFilepath = Environment.getExternalStorageDirectory().getPath()+"/Download/DLMS_Output.txt";
			File f = new File(mFilepath);	
			//File to = new File(mFilepath)
			//f.renameTo(newPath)
			if(f.exists())
				f.delete();
		}
	}

	//17-05-2016	
	public void ReadLG()
	{
		try
		{

			/*
			 /LT
			 H(Larsen & Tubro......)
			 H(42126952) //MeterSerial No
			 H(271014 115134 1)   //MeterDate MeterTime  1->Data powered by Main Power Supply  2-> Data Powered by battery
			 H(FR03.01)  //Meter Firmware Version
			 II(00000073) //Reading - Total Cummulative Energy
			 JI(00000073)// Tariff Cummulative Energy
			 L(0000 000000 000000) //TarifWiseCurrentMD Date Time
			 U(11550)  //Instantaneous Voltage
			 U(0000 0000)//Instantaneous Current
			 U(0000 000)//Load KW and PF
			 U(5004) //Frequency

			    Reading
			 II(00000073)
			   MD
			 L(0000 000000 000000)
			   Load PF
			 U(0000 000) //Last But One
			 Reading  MD    LoadPF
			 00000073#0000#0000#000
			 */	

			/*/LGC110N10.53XBC1832481       
			C.1(BC1832481       )
			0.2.0(N10.53X)
			0.9.3(01;05;30;240;05:18;22-08-15)
			0.9.1(13:16;20-10-15)
			12.7(231.15V)
			11.7.1(00.00A)
			11.7.2(00.00A)
			13.7(0.00)
			13.8(+0.00)
			13.9(0.00)
			1.7.0(00.00kW)
			1.7.1(00.00kW)
			C.7.0(00091)
			1.8.0(00000.0kWh)
			1.4.0(00.00kW;00:00;00-00-00)
			3.8.3(00000.0kWh)
			2.8.3(00000.0kWh)
			C.8.0(00088h)
			C.8.0.1(0005338m)
			C.50.1
			 */

			//09-05-2015					
			ddlBillConsumStatus.setSelection(selecteditempos);			
			if(isValid((DDLItem)alConStatusItem.get(ddlBillConsumStatus.getSelectedItemPosition())))//Validation 
			{
				Calculate clt =new Calculate(mcx);
				clt.consumption();/**///1. CONSUMPTION Calculation
				PForMDRequired();
			}

			int mcount = 0;
			String val = getIntent().getStringExtra("Key");	
			//String val="LT";
			if(val==null)
			{

			}
			else if(val.equals("LT0"))
			{
				CustomToast.makeText(BillingConsumption_LandT.this, "Device Not Found.", Toast.LENGTH_SHORT);

			}

			else if(val.equals("LT1"))
			{
				CustomToast.makeText(BillingConsumption_LandT.this, "Error Data 1.", Toast.LENGTH_SHORT);
			}
			else if(val.equals("LT2"))
			{
				CustomToast.makeText(BillingConsumption_LandT.this, "Error Data 2.", Toast.LENGTH_SHORT);
			}
			else if(val.equals("LT3"))
			{
				CustomToast.makeText(BillingConsumption_LandT.this, "Error Data 3.", Toast.LENGTH_SHORT);
			}
			else if(val.equals("LT4"))
			{
				CustomToast.makeText(BillingConsumption_LandT.this, "Error File Write.", Toast.LENGTH_SHORT);
			}
			else if(val.equals("LT5") || val.equals("LT6"))
			{
				CustomToast.makeText(BillingConsumption_LandT.this, "Device Not Connected.", Toast.LENGTH_SHORT);
			}

			else if(val.equals("LT"))
			{
				if(Billing_LandT.PROBETYPE!=1) //Old PROBE //07-05-2021
				{
					Billing_LandT.LTLG = 0;
				}
				CustomToast.makeText(BillingConsumption_LandT.this, "File Write Completed.", Toast.LENGTH_SHORT);
				StringBuilder sbLT=new StringBuilder();
				StringBuilder sbDB = new StringBuilder(); 
				LandT = new LandTObject();
				CommonFunction cFun = new CommonFunction();

				String Filepath = Environment.getExternalStorageDirectory().getPath()+"/Download/L&G_Output.txt" ;
				FileInputStream iStreamLT = new FileInputStream(Filepath);
				InputStreamReader ipStreamReaderLT = new InputStreamReader(iStreamLT);		
				BufferedReader buffReaderLT =   new BufferedReader(ipStreamReaderLT);	
				String ReceiveStr;					
				String buff = "";		
				int loopcount = 0;

				while((ReceiveStr=buffReaderLT.readLine()) != null)//While Loop							
				{
					loopcount = loopcount + 1;
					sbLT.delete(0, sbLT.length());
					sbLT.append(ReceiveStr);

					//sbDB.append(ReceiveStr);




					buff = ReceiveStr.trim();
					buff = buff.substring(ReceiveStr.indexOf("(")+1, buff.length()-1); 

					if(sbLT.toString().contains("13.7(")) //PF 13.7(0.00)
					{						
						LandT.setmPF(buff);
					}
					else if(sbLT.toString().contains("1.8.0(")) //Reading 1.8.0(00000.0kWh)
					{
						String Data[] = buff.replace(".", "#").split("#");	
						LandT.setmTotalCummulativeEnergy(Data[0] + "KWh");						

					}
					else if(sbLT.toString().contains("1.4.0(")) //MD  1.4.0(00.00kW;00:00;00-00-00)
					{
						String Data[] = buff.replace(";", "#").split("#");	
						LandT.setmTarifWiseMD(Data[0]);
						mcount=1;
						break;
					}
					//25-06-2016
					else if(sbLT.toString().contains("C.1("))
					{
						buff = sbLT.substring(sbLT.indexOf("(")+1,sbLT.indexOf(")")).toString();
						//CustomToast.makeText(BillingConsumption_LandT.this, "MSN : " + buff , Toast.LENGTH_SHORT);
						LandT.setmMeterSerialNo(buff);
					}
					else if(sbLT.toString().contains("0.9.1("))
					{
						//LandT.setmMeterTime(buff.substring(0, buff.indexOf(";")));
						buff = sbLT.substring(sbLT.indexOf("(")+1,sbLT.indexOf(")")).toString();
						String[] DataTime  = buff.trim().split(";"); 
						//CustomToast.makeText(BillingConsumption_LandT.this, "Date Time : " + buff, Toast.LENGTH_SHORT);
						LandT.setmMeterTime(DataTime[0].toString());
						LandT.setmMeterDate(DataTime[1].toString());
					}
					//End 25-06-2016

					/*if(loopcount==1)
					{
						LandT.setmManufacturer(buff.toString().substring(2,buff.length()-1));
					}
					buff = buff.substring(ReceiveStr.indexOf("(")+1, buff.length()-1);  //buff = "00000073"					
					if(sbLT.toString().contains("!"))
					{
						mcount=1;
						break;
					}
					if(sbLT.toString().startsWith("H") || sbLT.toString().startsWith("I") || sbLT.toString().startsWith("J") || sbLT.toString().startsWith("L") || sbLT.toString().startsWith("U"))
					{
						if(sbLT.toString().startsWith("H"))
						{

							if( buff.length()==15) //MeterDate MeterTime DataPoweredBy  1->Data powered by Main Power Supply  2-> Data Powered by battery
							{
								String Data[] = buff.replace(" ", "#").split("#");										
								LandT.setmMeterDate(cFun.DateConvertAddCharNew(Data[0],"-"));//dd-mm-yy
								LandT.setmMeterTime(cFun.DateConvertAddCharNew(Data[1],":"));//hh:mm:ss
								//Data Powered By
								if(Data[2].trim().equals("1"))
									LandT.setmDataPoweredBy("MAIN");
								else if(Data[2].trim().equals("1"))									
									LandT.setmDataPoweredBy("BATTERY");									

							}
							//if(buff.length()==8 && buff.length()!=15) //MeterSerial No
							else if(buff.length()==8)
							{
								//CustomToast.makeText(BillingConsumption.this, "Set Serial No.", Toast.LENGTH_SHORT);
								LandT.setmMeterSerialNo(buff);
								LandT.setmManufacturer(buff);

							}
							else
							{
								LandT.setmMeterFirmwareVersion(buff);
							}

						}
						else if(sbLT.toString().startsWith("I")) //Total Cummulative Energy
						{
							//strLT = II(00000073)  //Reading		
							buff = (new BigDecimal(buff).multiply(new BigDecimal(0.01)).setScale(2, RoundingMode.HALF_UP)).toString() + "KWh";
							LandT.setmTotalCummulativeEnergy(buff);
						}
						else if( sbLT.toString().startsWith("J")) //Tariff Cummulative Energy
						{
							buff = (new BigDecimal(buff).multiply(new BigDecimal(0.01)).setScale(2, RoundingMode.HALF_UP)).toString() + "KWh";
							LandT.setmTariffCummulativeEnergy(buff);
						}
						else if(sbLT.toString().startsWith("L")) //TarifWiseCurrentMD Date Time
						{
							String Data[] = buff.replace(" ", "#").split("#");
							Data[0] = (new BigDecimal(Data[0]).multiply(new BigDecimal(0.01)).setScale(2, RoundingMode.HALF_UP)).toString()+ "KW";
							LandT.setmTarifWiseMD(Data[0] );//TarifWiseCurrentMD
							LandT.setmDate(cFun.DateConvertAddCharNew(Data[1],"-"));//dd-mm-yy
							LandT.setmTime(cFun.DateConvertAddCharNew(Data[2],":"));//hh:mm:ss

						}
						else if(sbLT.toString().startsWith("U")) 
						{
							if(buff.length()==5) //Instantaneous Voltage
							{
								buff = (new BigDecimal(buff).multiply(new BigDecimal(0.01)).setScale(2, RoundingMode.HALF_UP)).toString() + "V";
								LandT.setmInstantaneousVoltage(buff);
							}
							else if(buff.length()==4) //Frequency
							{
								buff = (new BigDecimal(buff).multiply(new BigDecimal(0.01)).setScale(2, RoundingMode.HALF_UP)).toString() + "Hz";
								LandT.setmFrequency(buff);

							}
							else if(buff.length()==8)// load and PF
							{
								String Data[] = buff.replace(" ", "#").split("#");
								Data[0] = (new BigDecimal(Data[0]).setScale(0, RoundingMode.HALF_UP)).toString()+ "KW";
								LandT.setmLoad(Data[0]); //Load
								LandT.setmPF(Data[1]); //PF
							}
						}								
					}*/

				}//END While Loop
				//if(buffReaderLT.readLine() == null)  //For AlertDialog
				if(mcount==1)
				{
					//int meterstringsave = db.MeterStringSave(SlabColl.getmConnectionNo(),sbDB.toString());
					//int meterdetailssave = db.MeterStringSave(SlabColl.getmConnectionNo(),sbDB.toString());
					//CustomToast.makeText(BillingConsumption.this, "Meter SerialNo:" + LandT.getmMeterSerialNo(), Toast.LENGTH_SHORT);
					edtBillReading.setEnabled(true);
					//0.73KWh -->  0.73 * 1000 = 730
					if(LandT.getmTotalCummulativeEnergy()!=null)
					{
						readingnew = new BigDecimal(LandT.getmTotalCummulativeEnergy().substring(0, LandT.getmTotalCummulativeEnergy().length()-3)).setScale(0, RoundingMode.HALF_UP).toString();
						//readingnew = new BigDecimal(LandT.getmTotalCummulativeEnergy()).toString();
					}
					edtBillReading.setText(readingnew); 
					//edtBillReading.setEnabled(false);

					//LandT.setmManufacturer(J2xxHyperTerm.slno);
					//LandT.setmMeterSerialNo(J2xxHyperTerm.slno);
					LandT.setmMeterType("02"); //25-06-2016 LG
					StringBuilder sb = new StringBuilder();
					sb.append("METER ID	: "+ (LandT.getmManufacturer() == null ? " " : LandT.getmManufacturer()) + eol);
					//sb.append(eol);
					sb.append("METER SERIALNO: "+ (LandT.getmMeterSerialNo() == null ? " " : LandT.getmMeterSerialNo()) + eol);
					sb.append("METER DATE: "+ (LandT.getmMeterDate() == null?" ":LandT.getmMeterDate() )  + eol);
					sb.append("METER TIME: "+ (LandT.getmMeterTime() == null ? " " : LandT.getmMeterTime() )+ eol);
					sb.append(eol);
					//if(LandT.getmDataPoweredBy()!=null)
					//{
					//	sb.append("POWERED BY "+ LandT.getmDataPoweredBy().trim() + " SUPPLY" + eol);							
					//}					
					//sb.append("METER FIRM VER			:"+(LandT.getmMeterFirmwareVersion() == null ? " " : LandT.getmMeterFirmwareVersion())+ eol);
					sb.append("METER READING: "+ (LandT.getmTotalCummulativeEnergy() == null? " " : LandT.getmTotalCummulativeEnergy())+ eol);
					//sb.append("TARIFF CUM ENERGY:"+ (LandT.getmTariffCummulativeEnergy() == null ? " " : LandT.getmTariffCummulativeEnergy()) + eol);
					sb.append("MD: "+( LandT.getmTarifWiseMD() == null ? " " : LandT.getmTarifWiseMD() )+ eol);
					//sb.append("DATE									:"+ (LandT.getmDate() ==null ?  " " : LandT.getmDate() )+ eol);
					//sb.append("TIME									:"+( LandT.getmTime() == null ?" " : LandT.getmTime()) + eol);
					//sb.append("INSTANT VOLT		:"+ (LandT.getmInstantaneousVoltage()==null ? " " :LandT.getmInstantaneousVoltage()) + eol);
					//sb.append("FREQUENCY				:"+( LandT.getmFrequency() == null ? " " : LandT.getmFrequency())+ eol);
					sb.append("PF: "+( LandT.getmPF() == null ? " " : LandT.getmPF())+ eol);


					CustomAlert.CustomAlertParameters paramslt = new CustomAlert.CustomAlertParameters(); 
					paramslt.setContext(BillingConsumption_LandT.this);					
					paramslt.setMainHandler(dh);
					paramslt.setMsg(sb.toString());
					paramslt.setButtonOkText("OK");
					paramslt.setTitle("METER DETAILS");
					paramslt.setFunctionality(-1);
					CustomAlert cAlert  = new CustomAlert(paramslt);			



				}

				if(buffReaderLT != null)
				{
					try {
						buffReaderLT.close();									
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(iStreamLT != null)
				{
					try {
						iStreamLT.close();
					} 
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}


		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString()); 
		}
		finally
		{
			//04-05-2015
			btnReadLT.setEnabled(false);
			String mFilepath = Environment.getExternalStorageDirectory().getPath()+"/Download/L&G_Output.txt" ;
			File f = new File(mFilepath);
			if(f.exists())
				f.delete();
		}
	}

	public void ReadLT()
	{
		try
		{

			/*
			 /LT
			 H(Larsen & Tubro......)
			 H(42126952) //MeterSerial No
			 H(271014 115134 1)   //MeterDate MeterTime  1->Data powered by Main Power Supply  2-> Data Powered by battery
			 H(FR03.01)  //Meter Firmware Version
			 II(00000073) //Reading - Total Cummulative Energy
			 JI(00000073)// Tariff Cummulative Energy
			 L(0000 000000 000000) //TarifWiseCurrentMD Date Time
			 U(11550)  //Instantaneous Voltage
			 U(0000 0000)//Instantaneous Current
			 U(0000 000)//Load KW and PF
			 U(5004) //Frequency

			    Reading
			 II(00000073)
			   MD
			 L(0000 000000 000000)
			   Load PF
			 U(0000 000) //Last But One
			 Reading  MD    LoadPF
			 00000073#0000#0000#000
			 */	


			//09-05-2015					
			ddlBillConsumStatus.setSelection(selecteditempos);			
			if(isValid((DDLItem)alConStatusItem.get(ddlBillConsumStatus.getSelectedItemPosition())))//Validation 
			{
				Calculate clt =new Calculate(mcx);
				clt.consumption();/**///1. CONSUMPTION Calculation
				PForMDRequired();
			}

			int mcount = 0;
			String val = getIntent().getStringExtra("Key");			
			//String val="LT";
			if(val==null)
			{

			}
			else if(val.equals("LT0"))
			{
				CustomToast.makeText(BillingConsumption_LandT.this, "Device Not Found.", Toast.LENGTH_SHORT);

			}

			else if(val.equals("LT1"))
			{
				CustomToast.makeText(BillingConsumption_LandT.this, "Error Data 1.", Toast.LENGTH_SHORT);
			}
			else if(val.equals("LT2"))
			{
				CustomToast.makeText(BillingConsumption_LandT.this, "Error Data 2.", Toast.LENGTH_SHORT);
			}
			else if(val.equals("LT3"))
			{
				CustomToast.makeText(BillingConsumption_LandT.this, "Error Data 3.", Toast.LENGTH_SHORT);
			}
			else if(val.equals("LT4"))
			{
				CustomToast.makeText(BillingConsumption_LandT.this, "Error File Write.", Toast.LENGTH_SHORT);
			}
			else if(val.equals("LT5") || val.equals("LT6"))
			{
				CustomToast.makeText(BillingConsumption_LandT.this, "Device Not Connected.", Toast.LENGTH_SHORT);
			}

			else if(val.equals("LT"))
			{

				if(Billing_LandT.PROBETYPE!=1) //Old Probe
				{
					Billing_LandT.LTLG=0;//17-05-2016
				}
				CustomToast.makeText(BillingConsumption_LandT.this, "File Write Completed.", Toast.LENGTH_SHORT);
				StringBuilder sbLT=new StringBuilder();
				StringBuilder sbDB = new StringBuilder(); 
				LandT = new LandTObject();
				CommonFunction cFun = new CommonFunction();

				String Filepath = Environment.getExternalStorageDirectory().getPath()+"/Download/L&T_Output.txt" ;
				FileInputStream iStreamLT = new FileInputStream(Filepath);
				InputStreamReader ipStreamReaderLT = new InputStreamReader(iStreamLT);		
				BufferedReader buffReaderLT =   new BufferedReader(ipStreamReaderLT);	
				String ReceiveStr;					
				String buff = "";		
				int loopcount = 0;

				while((ReceiveStr=buffReaderLT.readLine()) != null)//While Loop							
				{
					loopcount = loopcount + 1;
					sbLT.delete(0, sbLT.length());
					sbLT.append(ReceiveStr);

					//sbDB.append(ReceiveStr);
					buff = ReceiveStr.trim();	
					if(loopcount==1)
					{
						LandT.setmManufacturer(buff.toString().substring(2,buff.length()-1));

					}
					buff = buff.substring(ReceiveStr.indexOf("(")+1, buff.length()-1);  //buff = "00000073"					
					if(sbLT.toString().contains("!"))
					{
						mcount=1;
						break;
					}
					if(sbLT.toString().startsWith("H") || sbLT.toString().startsWith("I") || sbLT.toString().startsWith("J") || sbLT.toString().startsWith("L") || sbLT.toString().startsWith("U"))
					{
						if(sbLT.toString().startsWith("H"))
						{

							if( buff.length()==15) //MeterDate MeterTime DataPoweredBy  1->Data powered by Main Power Supply  2-> Data Powered by battery
							{
								String Data[] = buff.replace(" ", "#").split("#");										
								LandT.setmMeterDate(cFun.DateConvertAddCharNew(Data[0],"-"));//dd-mm-yy
								LandT.setmMeterTime(cFun.DateConvertAddCharNew(Data[1],":"));//hh:mm:ss
								//Data Powered By
								if(Data[2].trim().equals("1"))
									LandT.setmDataPoweredBy("MAIN");
								else if(Data[2].trim().equals("1"))									
									LandT.setmDataPoweredBy("BATTERY");									

							}
							//if(buff.length()==8 && buff.length()!=15) //MeterSerial No
							else if(buff.length()==8)
							{
								//CustomToast.makeText(BillingConsumption.this, "Set Serial No.", Toast.LENGTH_SHORT);
								LandT.setmMeterSerialNo(buff);
								LandT.setmManufacturer(buff);

							}
							/*else
							{
								LandT.setmMeterFirmwareVersion(buff);
							}*/

						}
						else if(sbLT.toString().startsWith("I")) //Total Cummulative Energy
						{
							//strLT = II(00000073)  //Reading		
							buff = (new BigDecimal(buff).multiply(new BigDecimal(0.01)).setScale(2, RoundingMode.HALF_UP)).toString() + "KWh";
							LandT.setmTotalCummulativeEnergy(buff);
						}
						else if( sbLT.toString().startsWith("J")) //Tariff Cummulative Energy
						{
							buff = (new BigDecimal(buff).multiply(new BigDecimal(0.01)).setScale(2, RoundingMode.HALF_UP)).toString() + "KWh";
							LandT.setmTariffCummulativeEnergy(buff);
						}
						else if(sbLT.toString().startsWith("L")) //TarifWiseCurrentMD Date Time
						{
							String Data[] = buff.replace(" ", "#").split("#");
							Data[0] = (new BigDecimal(Data[0]).multiply(new BigDecimal(0.01)).setScale(2, RoundingMode.HALF_UP)).toString()+ "kW";
							LandT.setmTarifWiseMD(Data[0] );//TarifWiseCurrentMD
							LandT.setmDate(cFun.DateConvertAddCharNew(Data[1],"-"));//dd-mm-yy
							LandT.setmTime(cFun.DateConvertAddCharNew(Data[2],":"));//hh:mm:ss

						}
						else if(sbLT.toString().startsWith("U")) 
						{
							if(buff.length()==5) //Instantaneous Voltage
							{
								buff = (new BigDecimal(buff).multiply(new BigDecimal(0.01)).setScale(2, RoundingMode.HALF_UP)).toString() + "V";
								LandT.setmInstantaneousVoltage(buff);
							}
							else if(buff.length()==4) //Frequency
							{
								buff = (new BigDecimal(buff).multiply(new BigDecimal(0.01)).setScale(2, RoundingMode.HALF_UP)).toString() + "Hz";
								LandT.setmFrequency(buff);

							}
							else if(buff.length()==8)// load and PF
							{
								String Data[] = buff.replace(" ", "#").split("#");
								Data[0] = (new BigDecimal(Data[0]).setScale(0, RoundingMode.HALF_UP)).toString()+ "KW";
								LandT.setmLoad(Data[0]); //Load
								LandT.setmPF(Data[1]); //PF
							}
						}								
					}

				}//END While Loop
				//if(buffReaderLT.readLine() == null)  //For AlertDialog
				if(mcount==1)
				{

					//int meterstringsave = db.MeterStringSave(SlabColl.getmConnectionNo(),sbDB.toString());
					//int meterdetailssave = db.MeterStringSave(SlabColl.getmConnectionNo(),sbDB.toString());
					//CustomToast.makeText(BillingConsumption.this, "Meter SerialNo:" + LandT.getmMeterSerialNo(), Toast.LENGTH_SHORT);
					edtBillReading.setEnabled(true);
					//0.73KWh -->  0.73 * 1000 = 730
					if(LandT.getmTotalCummulativeEnergy()!=null)
					{
						readingnew = new BigDecimal(LandT.getmTotalCummulativeEnergy().substring(0, LandT.getmTotalCummulativeEnergy().length()-3)).setScale(0, RoundingMode.HALF_UP).toString();
					}
					edtBillReading.setText(readingnew); 
					//edtBillReading.setEnabled(false);

					//LandT.setmManufacturer(J2xxHyperTerm.slno);
					//LandT.setmMeterSerialNo(J2xxHyperTerm.slno);
					LandT.setmMeterType("01"); //25-06-2016 LT
					StringBuilder sb = new StringBuilder();
					sb.append("METER ID	: "+ (LandT.getmManufacturer() == null ? " " : LandT.getmManufacturer()) + eol);
					//sb.append(eol);
					sb.append("METER SERIALNO: "+ (LandT.getmMeterSerialNo() == null ? " " : LandT.getmMeterSerialNo()) + eol);
					sb.append("METER DATE: "+ (LandT.getmMeterDate() == null?" ":LandT.getmMeterDate() )  + eol);
					sb.append("METER TIME: "+ (LandT.getmMeterTime() == null ? " " : LandT.getmMeterTime() )+ eol);
					sb.append(eol);
					//if(LandT.getmDataPoweredBy()!=null)
					//{
					//	sb.append("POWERED BY "+ LandT.getmDataPoweredBy().trim() + " SUPPLY" + eol);							
					//}					
					//sb.append("METER FIRM VER			:"+(LandT.getmMeterFirmwareVersion() == null ? " " : LandT.getmMeterFirmwareVersion())+ eol);
					sb.append("METER READING: "+ (LandT.getmTotalCummulativeEnergy() == null? " " : LandT.getmTotalCummulativeEnergy())+ eol);
					//sb.append("TARIFF CUM ENERGY:"+ (LandT.getmTariffCummulativeEnergy() == null ? " " : LandT.getmTariffCummulativeEnergy()) + eol);
					sb.append("MD: "+( LandT.getmTarifWiseMD() == null ? " " : LandT.getmTarifWiseMD() )+ eol);
					//sb.append("DATE									:"+ (LandT.getmDate() ==null ?  " " : LandT.getmDate() )+ eol);
					//sb.append("TIME									:"+( LandT.getmTime() == null ?" " : LandT.getmTime()) + eol);
					//sb.append("INSTANT VOLT		:"+ (LandT.getmInstantaneousVoltage()==null ? " " :LandT.getmInstantaneousVoltage()) + eol);
					//sb.append("FREQUENCY				:"+( LandT.getmFrequency() == null ? " " : LandT.getmFrequency())+ eol);
					sb.append("PF: "+( LandT.getmPF() == null ? " " : LandT.getmPF())+ eol);


					CustomAlert.CustomAlertParameters paramslt = new CustomAlert.CustomAlertParameters(); 
					paramslt.setContext(BillingConsumption_LandT.this);					
					paramslt.setMainHandler(dh);
					paramslt.setMsg(sb.toString());
					paramslt.setButtonOkText("OK");
					paramslt.setTitle("METER DETAILS");
					paramslt.setFunctionality(-1);
					CustomAlert cAlert  = new CustomAlert(paramslt);			



				}

				if(buffReaderLT != null)
				{
					try {
						buffReaderLT.close();									
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(iStreamLT != null)
				{
					try {
						iStreamLT.close();
					} 
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}


		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString()); 
		}
		finally
		{
			//04-05-2015
			btnReadLT.setEnabled(false);
			String mFilepath = Environment.getExternalStorageDirectory().getPath()+"/Download/L&T_Output.txt" ;
			File f = new File(mFilepath);
			if(f.exists())
				f.delete();
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public void SaveFunction()
	{
		//Pop up Window Added by Tamilselvan on 24-03-2014===================================================
		BigDecimal txtTaxamt = SlabColl.getmTaxAmt() == null ? new BigDecimal(0.00) : SlabColl.getmTaxAmt().setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal txtArrers = SlabColl.getmArears() == null ? new BigDecimal(0.00) : SlabColl.getmArears().setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal txtArrersold = SlabColl.getmArrearsOld().setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal txtIntstold = SlabColl.getmIntrstOld().setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal txtOther = new BigDecimal(SlabColl.getmOthers()).add(SlabColl.getmKVAAssd_Cons().setScale(2, BigDecimal.ROUND_HALF_UP));//Add other with RFC 28-06-2014

		BigDecimal sub = txtTaxamt.add(txtArrers.add(txtArrersold.add(txtIntstold.add(txtOther))));
		BigDecimal BillAmount = (SlabColl.getmBillTotal().subtract(sub)).setScale(0, BigDecimal.ROUND_HALF_UP);
		BigDecimal billTotal = (SlabColl.getmBillTotal().setScale(2, BigDecimal.ROUND_HALF_UP)).setScale(0, BigDecimal.ROUND_HALF_UP);
		StringBuilder sb = new StringBuilder();
		sb.append("Consumption	: "+ SlabColl.getmUnits() + eol);
		sb.append("Curr. Bill Amt		: "+ BillAmount.toString() + eol);
		sb.append("Tax							: "+ txtTaxamt.toString() + eol);
		sb.append("Arrears					: "+ txtArrers.add(txtArrersold.add(txtIntstold)).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + eol);
		sb.append("Net Bill Amt		: "+ billTotal.toString());



		//Nitish  30-08-2014 
		alert = new AlertDialog.Builder(BillingConsumption_LandT.this);
		alert.setTitle(alerttitle);
		View convertView = BillingConsumption_LandT.this.getLayoutInflater().inflate(R.layout.billingdialoginflator, null);	
		alert.setView(convertView);
		tvDisplayData = (TextView)convertView.findViewById(R.id.tvInflatorDisplayData);
		tvInflateMtrType = (TextView)convertView.findViewById(R.id.tvInflateMtrType);
		tvInflateBatteryStatus = (TextView)convertView.findViewById(R.id.tvInflateBatteryStatus);
		tvInflateMeterPlacement = (TextView)convertView.findViewById(R.id.tvInflateMeterPlacement);
		tvInflateMeterCondition = (TextView)convertView.findViewById(R.id.tvInflateMeterCondition);
		tvInflateContactType = (TextView)convertView.findViewById(R.id.tvInflateContactType); 
		tvInflateContactNo = (TextView)convertView.findViewById(R.id.tvInflateContactNo);
		txtInflatorContactNo = (EditText)convertView.findViewById(R.id.txtInflatorContactNo);
		//txtRemark = (EditText)convertView.findViewById(R.id.txtRemark); //06-07-2020
		txtInflatorContactNo.setEnabled(true);

		ddlInflatorMeterType = (Spinner)convertView.findViewById(R.id.ddlInflatorMeterType);
		ddlInflatorBatteryStatus = (Spinner)convertView.findViewById(R.id.ddlInflatorBatteryStatus);
		ddlInflatorMeterPlacement = (Spinner)convertView.findViewById(R.id.ddlInflatorMeterPlacement);
		ddlInflatorMeterCondition = (Spinner)convertView.findViewById(R.id.ddlInflatorMeterCondition);
		ddlInflatorContactType = (Spinner)convertView.findViewById(R.id.ddlInflatorContactType);	

		tvDisplayData.setText(sb.toString());

		//Nitish Added 30-09-2014
		tvInflateMtrType.setVisibility(View.GONE);

		tvInflateBatteryStatus.setVisibility(View.GONE);
		tvInflateMeterPlacement.setVisibility(View.GONE);
		tvInflateMeterCondition.setVisibility(View.GONE);
		ddlInflatorMeterType.setVisibility(View.GONE);
		ddlInflatorBatteryStatus.setVisibility(View.GONE);
		ddlInflatorMeterPlacement.setVisibility(View.GONE);
		ddlInflatorMeterCondition.setVisibility(View.GONE);

		tvInflateContactType.setVisibility(View.GONE);
		tvInflateContactNo.setVisibility(View.GONE);			
		txtInflatorContactNo.setVisibility(View.GONE);
		ddlInflatorContactType.setVisibility(View.GONE);

		//29-03-2016
		tvInflateMtrPhase = (TextView)convertView.findViewById(R.id.tvInflateMtrPhase);
		ddlInflatorMeterPhase = (Spinner)convertView.findViewById(R.id.ddlInflatorMeterPhase);
		tvInflateMtrPhase.setVisibility(View.GONE); 
		ddlInflatorMeterPhase.setVisibility(View.GONE); 

		try {

			/*contactTypeAdapter = db.getContactType();
					ddlInflatorContactType.setAdapter(contactTypeAdapter);

					alStdCodes = new ArrayList<String>();				
					alStdCodes = db.GetLandlineSTDCodes();*/

			//29-03-2016
			meterPhaseAdapter = db.getMeterPhase();
			ddlInflatorMeterPhase.setAdapter(meterPhaseAdapter);

		} 


		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//For DL=1,VA = 6,NT =15 Remove dropdowns
		if(BillingObject.GetBillingObject().getmPreStatus().equals("1") || BillingObject.GetBillingObject().getmPreStatus().equals("6") || BillingObject.GetBillingObject().getmPreStatus().equals("15"))
		{
			//Hide Meter Type
			/*tvInflateContactType.setVisibility(View.GONE);
					tvInflateContactNo.setVisibility(View.GONE);			
					txtInflatorContactNo.setVisibility(View.GONE);
					ddlInflatorContactType.setVisibility(View.GONE);*/
			tvInflateMtrPhase.setVisibility(View.GONE);		
			ddlInflatorMeterPhase.setVisibility(View.GONE);
		}
		else
		{
			//show meter placement, meter condition, battery status			
			/*tvInflateContactType.setVisibility(View.VISIBLE);
					tvInflateContactNo.setVisibility(View.VISIBLE);			
					txtInflatorContactNo.setVisibility(View.VISIBLE);
					ddlInflatorContactType.setVisibility(View.VISIBLE);		*/	

			tvInflateMtrPhase.setVisibility(View.VISIBLE);		
			ddlInflatorMeterPhase.setVisibility(View.VISIBLE);
		}



		//Nitish Commented on 30-09-2014
		/*try {
					meterTypeAdapter = db.getMeterType();
					batteryStatusAdapter = db.getBatteryStatus();
					meterPlacementAdapter = db.getMeterPlacement();
					meterConditionAdapter = db.getMeterCondition();

					ddlInflatorMeterType.setAdapter(meterTypeAdapter);
					ddlInflatorBatteryStatus.setAdapter(batteryStatusAdapter);
					ddlInflatorMeterPlacement.setAdapter(meterPlacementAdapter);
					ddlInflatorMeterCondition.setAdapter(meterConditionAdapter);
				} 


				catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if(BillingObject.GetBillingObject().getmTarifString().contains("LT-1"))
				{
					//hide meter placement, meter condition, battery status
					tvInflateBatteryStatus.setVisibility(View.GONE);
					tvInflateMeterPlacement.setVisibility(View.GONE);
					tvInflateMeterCondition.setVisibility(View.GONE);

					ddlInflatorBatteryStatus.setVisibility(View.GONE);
					ddlInflatorMeterPlacement.setVisibility(View.GONE);
					ddlInflatorMeterCondition.setVisibility(View.GONE);

				}
				else
				{
					//show meter placement, meter condition, battery status

					tvInflateBatteryStatus.setVisibility(View.VISIBLE);
					tvInflateMeterPlacement.setVisibility(View.VISIBLE);
					tvInflateMeterCondition.setVisibility(View.VISIBLE);

					ddlInflatorBatteryStatus.setVisibility(View.VISIBLE);
					ddlInflatorMeterPlacement.setVisibility(View.VISIBLE);
					ddlInflatorMeterCondition.setVisibility(View.VISIBLE);
				}

				//For DL=1,VA = 6,DIR = 10 Remove dropdowns
				if(BillingObject.GetBillingObject().getmPreStatus().equals("1") || BillingObject.GetBillingObject().getmPreStatus().equals("10") || BillingObject.GetBillingObject().getmPreStatus().equals("6"))
				{
					//Hide Meter Type
					tvInflateMtrType.setVisibility(View.GONE);
					tvInflateBatteryStatus.setVisibility(View.GONE);
					tvInflateMeterPlacement.setVisibility(View.GONE);
					tvInflateMeterCondition.setVisibility(View.GONE);

					ddlInflatorMeterType.setVisibility(View.GONE);
					ddlInflatorBatteryStatus.setVisibility(View.GONE);
					ddlInflatorMeterPlacement.setVisibility(View.GONE);
					ddlInflatorMeterCondition.setVisibility(View.GONE);


				}
				else
				{
					//Show Meter Type
					tvInflateMtrType.setVisibility(View.VISIBLE);
					tvInflateBatteryStatus.setVisibility(View.VISIBLE);
					tvInflateMeterPlacement.setVisibility(View.VISIBLE);
					tvInflateMeterCondition.setVisibility(View.VISIBLE);

					ddlInflatorMeterType.setVisibility(View.VISIBLE);
					ddlInflatorBatteryStatus.setVisibility(View.VISIBLE);
					ddlInflatorMeterPlacement.setVisibility(View.VISIBLE);
					ddlInflatorMeterCondition.setVisibility(View.VISIBLE);
				}




				ddlInflatorMeterType.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub				
						SlabColl.setmMeter_type(meterTypeAdapter.GetId(arg2).toString());
					}
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

				ddlInflatorBatteryStatus.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub				
						SlabColl.setmMtrDisFlag(Integer.valueOf(batteryStatusAdapter.GetId(arg2)));
					}
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

				ddlInflatorMeterPlacement.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub				
						SlabColl.setmMeter_Present_Flag(Integer.valueOf(meterPlacementAdapter.GetId(arg2)));
					}
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

				ddlInflatorMeterCondition.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub				
						SlabColl.setmMtr_Not_Visible(Integer.valueOf(meterConditionAdapter.GetId(arg2)));
					}
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});	
		 */


		/*ddlInflatorContactType.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub	
						String str = ddlInflatorContactType.getSelectedItem().toString();
						if(str.equals("None"))
						{
							txtInflatorContactNo.setEnabled(false);
						}
						else
						{
							txtInflatorContactNo.setEnabled(true);
							//26-03-2016 To Check Length of ContactNo
							if(str.equals("Mobile"))
							{
								txtInflatorContactNo.setFilters(new InputFilter[]{
										new InputFilter.LengthFilter(10)
								});
							}
							else
							{
								if(str.equals("Landline"))
								{
									txtInflatorContactNo.setFilters(new InputFilter[]{
											new InputFilter.LengthFilter(11)
									});
								}
							}
							//End 26-03-2016 

						}

					}
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});*/

		//29-03-2016
		ddlInflatorMeterPhase.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub				
				SlabColl.setmMtrDisFlag(Integer.valueOf(meterPhaseAdapter.GetId(arg2).toString().trim()));
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Intent i = new Intent(BillingConsumption_LandT.this, Billing_LandT.class);
				startActivity(i);

			}
		});
		alert.setNegativeButton("Save", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {


				/*if(ddlInflatorMeterType.getVisibility() != View.GONE && ddlInflatorMeterType.getVisibility() != View.INVISIBLE)
						{
							String str = ddlInflatorMeterType.getSelectedItem().toString();
							if(str.equals("--SELECT--"))
							{
								CustomToast.makeText(BillingConsumption.this, "Select Meter Type", Toast.LENGTH_SHORT);						
								btnProcess.setVisibility(Button.VISIBLE);
								btnSave.setVisibility(Button.VISIBLE);
								return;
							}
						}
						else
						{
							SlabColl.setmMeter_type("0");
						}*/
				/*if(ddlInflatorBatteryStatus.getVisibility() != View.GONE && ddlInflatorBatteryStatus.getVisibility() != View.INVISIBLE)
						{
							String str = ddlInflatorBatteryStatus.getSelectedItem().toString();
							if(str.equals("--SELECT--"))
							{
								CustomToast.makeText(BillingConsumption.this, "Select Battery Status", Toast.LENGTH_SHORT);						
								btnProcess.setVisibility(Button.VISIBLE);
								btnSave.setVisibility(Button.VISIBLE);
								return;
							}
						}
						else
						{
							SlabColl.setmMtrDisFlag(0);
						}*/
				//29-03-2016
				if(ddlInflatorMeterPhase.getVisibility() != View.GONE && ddlInflatorMeterPhase.getVisibility() != View.INVISIBLE)
				{
					String str = ddlInflatorMeterPhase.getSelectedItem().toString();
					if(str.equals("--SELECT--"))
					{
						CustomToast.makeText(BillingConsumption_LandT.this, "Select Meter Phase", Toast.LENGTH_SHORT);						
						btnProcess.setVisibility(Button.VISIBLE);
						btnSave.setVisibility(Button.VISIBLE);
						return;
					}
				}
				else
				{
					SlabColl.setmMtrDisFlag(0);
				}
				//End 29-03-2016
				if(ddlInflatorMeterPlacement.getVisibility() != View.GONE && ddlInflatorMeterPlacement.getVisibility() != View.INVISIBLE)
				{
					String str = ddlInflatorMeterPlacement.getSelectedItem().toString();
					if(str.equals("--SELECT--"))
					{
						CustomToast.makeText(BillingConsumption_LandT.this, "Select Meter location", Toast.LENGTH_SHORT);						
						btnProcess.setVisibility(Button.VISIBLE);
						btnSave.setVisibility(Button.VISIBLE);
						return;
					}
				}
				else
				{
					SlabColl.setmMeter_Present_Flag(0);
				}
				if(ddlInflatorMeterCondition.getVisibility() != View.GONE && ddlInflatorMeterCondition.getVisibility() != View.INVISIBLE)
				{
					String str = ddlInflatorMeterCondition.getSelectedItem().toString();
					if(str.equals("--SELECT--"))
					{
						CustomToast.makeText(BillingConsumption_LandT.this, "Select Meter Condition", Toast.LENGTH_SHORT);						
						btnProcess.setVisibility(Button.VISIBLE);
						btnSave.setVisibility(Button.VISIBLE);
						return;
					}
				}
				else
				{
					SlabColl.setmMtr_Not_Visible(0);
				}
				//Modified 23-05-2020
				if(ddlInflatorContactType.getVisibility() != View.GONE && ddlInflatorMeterCondition.getVisibility() != View.INVISIBLE)
				{					
					String str = ddlInflatorContactType.getSelectedItem().toString();
					if(str.equals("--SELECT--"))
					{
						CustomToast.makeText(BillingConsumption_LandT.this, "Select Contact Type", Toast.LENGTH_SHORT);						
						btnProcess.setVisibility(Button.VISIBLE);
						btnSave.setVisibility(Button.VISIBLE);
						return;
					}
				}
				
				//08-07-2021
				try
				{
					//If Single Phase then MD should not be greater than 8kW
					if(SlabColl.getmMtrDisFlag()==1) 
					{
						if(SlabColl.getmRecordDmnd().compareTo(new BigDecimal(8.0)) > 0)
						{
							CustomToast.makeText(BillingConsumption_LandT.this, "Recorded Demand cannot be greater than 8kW for Single Phase.", Toast.LENGTH_LONG);	
							btnProcess.setVisibility(Button.VISIBLE);
							btnSave.setVisibility(Button.VISIBLE);
							return;
						}

					}
				}
				catch(Exception e)
				{
					
				}
				
				//06-07-2020
				/*saveRemark=txtRemark.getText().toString().trim();
						if(saveRemark.equals("") || saveRemark==null)
						{
							SlabColl.setmRemarks("");
						}
						else
						{
							SlabColl.setmRemarks(saveRemark);
						}*/
				//Save Code from dialog========================================================				
				//String imgName = ImageSaveinSDCard();
				//BillingObject.GetBillingObject().setmImage_Name(imgName);			
				new Thread(new Runnable() {

					@Override
					public void run() 
					{

						if(LandT==null)
						{
							LandT = new LandTObject();
						}
						db.MeterDetails(LandT);

						BillingObject.GetBillingObject().setmSBMNumber(IMEINumber);//29-07-2016
						result = db.BillSave(); 
						dh.post(new Runnable()
						{
							@Override
							public void run()
							{
								if(result == 1)
								{
									CustomToast.makeText(BillingConsumption_LandT.this, "Bill created successfully.", Toast.LENGTH_SHORT);
									//If Present Reading is 15(NT) Print Should Not Come
									if(!BillingObject.GetBillingObject().getmPreStatus().equals("15"))
									{
										//Bluetooth Code===========================================				
										bptr = new BluetoothPrinting(BillingConsumption_LandT.this);
										try 
										{
											new Thread(new Runnable() {

												@Override
												public void run() {
													// TODO Auto-generated method stub
													try 
													{
														//WriteLog.WriteLogError(TAG+" Save print Connect openBT.");
														Looper.prepare();
														bptr.openBT();
													}
													catch (Exception e) 
													{
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
													ConnectedThread cth = new ConnectedThread(BillingObject.GetBillingObject());
													//WriteLog.WriteLogError(TAG+" Save print started.");
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
											CustomToast.makeText(BillingConsumption_LandT.this, "Problem in printing data. Please check Bluetooth device connected or not", Toast.LENGTH_SHORT);
											Intent i = new Intent(BillingConsumption_LandT.this, Billing_LandT.class);
											startActivity(i);
										}
										//END Bluetooth Code=======================================
									}
									else//For reason(15 - Not Trace No Print )
									{
										Intent i = new Intent(BillingConsumption_LandT.this, Billing_LandT.class);
										startActivity(i);
									}
								}
								else
								{
									CustomToast.makeText(BillingConsumption_LandT.this, "Problem in saving data.", Toast.LENGTH_SHORT);
									Intent i = new Intent(BillingConsumption_LandT.this, Billing_LandT.class);
									startActivity(i);
								}
							}
						});
					}
				}).start();

				ringProgress = ProgressDialog.show(BillingConsumption_LandT.this, "Please wait..", "Data Saving and Printing.., Once Bill Is Generated Switch OFF and Switch ON Cable For Next Bill..",true);//Spinner
				ringProgress.setCancelable(false);

			}
		});		
		alert.setCancelable(false);
		alert.show();

		btnSave.setVisibility(Button.INVISIBLE);
		btnProcess.setVisibility(Button.INVISIBLE);	
	}
	//Validation for Process
	public boolean isValid(DDLItem k)
	{
		boolean isValidCr = true;
		SlabColl.setmPreRead(""); //Nitish 13-02-2015
		if(edtBillReading.isEnabled())//Validation For edtBillReading TextBox
		{
			if(edtBillReading.getText().toString().equals(""))
			{
				//CustomToast.makeText(BillingConsumption_LandT.this, "Reading field should not be empty.", Toast.LENGTH_SHORT);
				isValidCr = false;
			}
			else
			{
				bgPreReading = new BigDecimal(edtBillReading.getText().toString());//Present Reading
				bgPrvReading = new BigDecimal(SlabColl.getmPrevRdg().trim()).setScale(2, BigDecimal.ROUND_HALF_UP);//Previous reading
				SlabColl.setmPreRead(String.valueOf(bgPreReading));
				if(k.getId().equals("3"))//allow current reading less than prev. reading only for DO(Dial Over)
				{
					isValidCr = true; 
				}
				else
				{
					if(bgPreReading.compareTo(bgPrvReading) >= 0)
					{
						isValidCr = true; 
					}				
					else if (bgPreReading.compareTo(bgPrvReading) == -1 ) 
					{
						CustomToast.makeText(BillingConsumption_LandT.this, "Present reading should not less than previous reading.", Toast.LENGTH_SHORT);
						edtBillReading.setText(""); 
						lblConsumption.setText("");
						isValidCr = false;
					}
				}				
			}
		}//END Validation For edtBillReading TextBox
		//Validation For edtPowerFactor TextBox====================================================		
		if(isValidCr == true)
		{
			if(edtPowerFactor.isEnabled())
			{
				if(edtPowerFactor.getText().toString().equals(""))
				{
					//CustomToast.makeText(BillingConsumption.this, "Power factor field should not be empty. ", Toast.LENGTH_SHORT);
					//isValidCr = false;
				}
				else 
				{
					if(new BigDecimal(edtPowerFactor.getText().toString()).compareTo(new BigDecimal(1)) == 1)
					{
						edtPowerFactor.setText("");
						CustomToast.makeText(BillingConsumption_LandT.this, "Power factor field should not be greater than 1. ", Toast.LENGTH_SHORT);
						isValidCr = false;
					}
				}
			}
		}/**/ 
		//END Validation For edtPowerFactor TextBox================================================

		//Validation For edtMD TextBox=============================================================
		if(isValidCr == true)
		{
			if(edtMD.isEnabled())//MD only for LT-6 Tariff
			{
				if(SlabColl.getmTarifString().indexOf("LT-6") == -1)
				{//if IndexOf returns -1 then the character does not occur

				}
				else
				{
					if(edtMD.getText().toString().equals(""))
					{
						CustomToast.makeText(BillingConsumption_LandT.this, "MD field should not be empty for LT-6 Consumers.", Toast.LENGTH_SHORT);
						isValidCr = false;
					}
				}
			}
		}//END Validation For edtMD TextBox=========================================================
		return isValidCr;
	}		
	//Tamilselvan on 27-02-2014
	public int GetIndex(int Str)//Get the Index of alConStatusItem (ArrayList<DDLItem>)
	{
		int x = 0;
		try
		{

			for(x = 0; x < alConStatusItem.size(); x++)
			{
				if(Integer.valueOf(alConStatusItem.get(x).getId()) == Str)
				{
					break;
				}
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return x;
	}
	//Tamilselvan on 28-02-2014
	public String GetPrvStatusName(int prvId)
	{
		String PreValue = "";
		try
		{
			int x = 0;
			for(x = 0; x < alConStatusItem.size(); x++)
			{
				if(Integer.valueOf(alConStatusItem.get(x).getId()) == prvId)
				{				
					break;
				}
			}
			String[] PrvName =  alConStatusItem.get(x).getValue().split(":");
			PreValue = PrvName[0];
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return PreValue;
	}
	//Tamilselvan on 17-02-2014
	public void PForMDRequired()
	{
		try
		{
			cbCalc = new Checkbox(BillingConsumption_LandT.this);
			//PF Required or Not============================================================
			if(cbCalc.pf_required())
			{
				edtPowerFactor.setEnabled(true);
			}
			else
			{
				edtPowerFactor.setEnabled(false);
			}
			//END PF Required or Not========================================================
			//MD Required or Not============================================================
			if(cbCalc.md_required())
			{
				edtMD.setEnabled(true); 
			}
			else
			{
				edtMD.setEnabled(false);	
			}
			//END MD Required or Not========================================================
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}
	//Tamilselvan on 17-03-2014
	public void LoadInitialDetails()
	{
		try
		{
			avgCons = new BigDecimal(SlabColl.getmAvgCons().trim());//Average Consumption
			int[][] ConStatusArray = ConArray.Status;//Get Array of status
			ReadBillingConsumptionStatus ConStatus = new ReadBillingConsumptionStatus(BillingConsumption_LandT.this);
			alConStatusItem = ConStatus.ReadStatus(); //03-09-2015
			DDLItem dNavnew;		
			ArrayList<DDLItem> alItems = new ArrayList<DDLItem>();
			lblRrNo.setText(SlabColl.getmRRNo());//get RR No
			lblPrvStatus.setText(GetPrvStatusName(Integer.valueOf(SlabColl.getmStatus())));//get Previous Status 
			//db.MovetoNextConnectionNo(String.valueOf(SlabColl.getMmUID()));
			if(lblPrvStatus.getText().equals(""))
			{
				CustomToast.makeText(BillingConsumption_LandT.this, "Please check this connection previous status.", Toast.LENGTH_SHORT);
				//return;
			}
			//Loading Reason or Status in Dropdownlist============================================================
			for(int z = 1; z < ConStatusArray.length; z++)//Row For Loop
			{
				//if(ConStatusArray[z][0] == Integer.valueOf(SlabColl.getmStatus()))//Check Previous Status Id ---- 
				{
					for(int y = 1; y < ConStatusArray[z].length; y++)//Column for Loop
					{
						if(ConStatusArray[z][y] == 1)//Check for 1 
						{
							/*if(ConStatusArray[0][y] != 4)//Check 4(MCH)----> tamilselvan on 20-03-2014
							{
								int index = GetIndex(ConStatusArray[0][y]);//Get the Index of the Status Id
								dNavnew = new DDLItem(alConStatusItem.get(index).getId(), alConStatusItem.get(index).getValue());
								alItems.add(dNavnew);//add all Items which are 1
							}
							else
							{
								if(SlabColl.getmMCHFlag().equals("1"))//Check MCHFlag if it is 1 then add MCH in reason(Dropdownlist)
								{
									int index = GetIndex(ConStatusArray[0][y]);//Get the Index of the Status Id
									dNavnew = new DDLItem(alConStatusItem.get(index).getId(), alConStatusItem.get(index).getValue());
									alItems.add(dNavnew);//add all Items which are 1
								}
							}*/
							if(SlabColl.getmMCHFlag().equals("1"))//Check MCHFlag if it is 1 then add MCH in reason(Dropdownlist)
							{
								if(ConStatusArray[0][y] == 4)//Check 4(MCH)----> tamilselvan on 20-03-2014
								{
									int index = GetIndex(ConStatusArray[0][y]);//Get the Index of the Status Id
									dNavnew = new DDLItem(alConStatusItem.get(index).getId(), alConStatusItem.get(index).getValue());
									alItems.add(dNavnew);//add all Items which are 1
								}
							}
							else
							{
								if(ConStatusArray[0][y] != 4)//Check 4(MCH)----> tamilselvan on 20-03-2014
								{
									int index = GetIndex(ConStatusArray[0][y]);//Get the Index of the Status Id
									dNavnew = new DDLItem(alConStatusItem.get(index).getId(), alConStatusItem.get(index).getValue());
									alItems.add(dNavnew);//add all Items which are 1
								}
							}
						}
					}//END Column For Loop
					break;
				}
			}//END Row For Loop		
			//Bind ddlBillConsumStatus(DropDownList)		
			DDLAdapter cList = null;
			if(cList == null)
			{
				cList = new DDLAdapter(BillingConsumption_LandT.this, alItems);
			}

			ddlBillConsumStatus.setAdapter(cList);
			//End of Binding ddlBillConsumStatus(DropDownList)
			//END Loading Reason or Status in Dropdownlist============================================================
			int a = ddlBillConsumStatus.getSelectedItemPosition();		
			SlabColl.setmPreStatus(((DDLItem)alConStatusItem.get(a)).getId());	
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	//Modified Nitish 26-02-2014
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.spotbillingmenu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return OptionsMenu.navigate(item,BillingConsumption_LandT.this);			
	}
	//End Nitish 26-02-2014
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		//super.onBackPressed();
		//CustomToast.makeText(BillingConsumption.this, "Please use menu to navigate.", Toast.LENGTH_SHORT);
		return;
	}

	//Tamilselvan on 24-03-2014 for pop up print and exit 
	@Override
	public void performAction(boolean alertResult, int functionality) {
		edtBillReading.setText(readingnew);		
		if(functionality==1)
		{

		}
	}	
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
			ImageName = BillingObject.GetBillingObject().getmConnectionNo().trim() +"_"+ cFun.GetCurrentTime().replace(" ", "_").replace("-", "").replace(":", "")+".jpg";
			String root = Environment.getExternalStorageDirectory().getPath();//get the sd Card Path

			myInput = new FileInputStream(ConstantClass.MtrImageSavePath + "/MtrImg.jpg");
			File f = new File(root+"/GescomSpotBilling/"+timeStamp+"/Photos", ImageName);
			if(!new File(root+"/GescomSpotBilling/"+timeStamp+"/Photos").exists())
			{
				new File(root+"/GescomSpotBilling/"+timeStamp+"/Photos").mkdirs();
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

	private class TimeSync extends AsyncTask<Void, Void, Void>  //GenericTypes AsyncTask<parametertype, progressparameter, postexecuteparam >
	{
		final ProgressDialog ringProgress = ProgressDialog.show(BillingConsumption_LandT.this, "Please wait..", "Time Sync...",true);
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
				CustomToast.makeText(BillingConsumption_LandT.this, "Error in Time Synchronisation", Toast.LENGTH_SHORT);				
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
						CustomToast.makeText(BillingConsumption_LandT.this,"Mobile Date Time not matching with Server."  , Toast.LENGTH_SHORT);
						TimeChangedReceiver.setValid(false);
						db.UpdateStatusInStatusMaster("3", "0");
					}
					else // If Time Difference within 10 Min
					{
						TimeChangedReceiver.setValid(true);
						CollectionObject.GetCollectionObject().setmIsTimeSync(true); 					
						SaveFunction();
						db.UpdateStatusInStatusMaster("3", "1");
					}
				} 
				catch (Exception e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}				
			else//NACK received or any Error 
			{
				CustomToast.makeText(BillingConsumption_LandT.this, "Error Occured", Toast.LENGTH_SHORT);
			}		
		}	
		//If Exception Occured 
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false);
			ringProgress.dismiss();
			CustomToast.makeText(BillingConsumption_LandT.this, "Time Synchronisation Failed.", Toast.LENGTH_SHORT);			
		}
	}

	//Bluetooth Code =======================================================================================	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)	
	public class ConnectedThread extends Thread //ConnectedThread
	{	
		ReadSlabNTarifSbmBillCollection SlabColl1 = new ReadSlabNTarifSbmBillCollection();
		public ConnectedThread(ReadSlabNTarifSbmBillCollection obj)
		{
			SlabColl1 = obj;

			SlabColl1.setmAbFlag(obj.getmAbFlag());
			SlabColl1.setmAccdRdg(obj.getmAccdRdg());
			SlabColl1.setmAccdRdg_rtn(obj.getmAccdRdg_rtn());
			SlabColl1.setmAddress1(obj.getmAddress1());
			SlabColl1.setmAddress2(obj.getmAddress2());
			SlabColl1.setmApplicationNo(obj.getmApplicationNo());
			SlabColl1.setmArears(obj.getmArears());
			SlabColl1.setmArrearsOld(obj.getmArrearsOld());
			SlabColl1.setmAvgCons(obj.getmAvgCons());
			SlabColl1.setmBankID(obj.getmBankID());
			SlabColl1.setmBatch_No(obj.getmBatch_No());
			SlabColl1.setmBillable(obj.getmBillable()); 
			SlabColl1.setmBillDate(obj.getmBillDate());
			SlabColl1.setmBillFor(obj.getmBillFor());
			SlabColl1.setmBillNo(obj.getmBillNo());
			SlabColl1.setmBillTotal(obj.getmBillTotal());
			SlabColl1.setmBjKj2Lt2(obj.getmBjKj2Lt2());
			SlabColl1.setmBlCnt(obj.getmBlCnt());
			SlabColl1.setmBOBilled_Amount(obj.getmBOBilled_Amount());
			SlabColl1.setmBOBillFlag(obj.getmBOBillFlag());
			SlabColl1.setmCapReb(obj.getmCapReb());
			SlabColl1.setmChargetypeID(obj.getmChargetypeID());
			SlabColl1.setmChequeDDDate(obj.getmChequeDDDate());
			SlabColl1.setmChequeDDNo(obj.getmChequeDDNo());
			SlabColl1.setmConnectionNo(obj.getmConnectionNo());
			SlabColl1.setmConsPayable(obj.getmConsPayable());
			SlabColl1.setmCustomerName(obj.getmCustomerName());
			SlabColl1.setmDateTime(obj.getmDateTime());
			SlabColl1.setmDayWise_Flag(obj.getmDayWise_Flag());
			SlabColl1.setmDemandChrg(obj.getmDemandChrg());
			SlabColl1.setmDLAvgMin(obj.getmDLAvgMin());
			SlabColl1.setmDlCount(obj.getmDlCount());
			SlabColl1.setmDlCount(obj.getmDlCount());
			SlabColl1.setmDLTEc(obj.getmDLTEc());
			SlabColl1.setmDPdate(obj.getmDPdate());
			SlabColl1.setmDrFees(obj.getmDrFees());
			SlabColl1.setmDueDate(obj.getmDueDate());
			SlabColl1.setmECReb(obj.getmECReb());
			SlabColl1.setmEcsFlag(obj.getmEcsFlag());
			SlabColl1.setmExLoad(obj.getmExLoad());
			SlabColl1.setmFC_Slab_2(obj.getmFC_Slab_2());
			SlabColl1.setmFixedCharges(obj.getmFixedCharges());
			SlabColl1.setmFLReb(obj.getmFLReb());
			SlabColl1.setmForMonth(obj.getmForMonth());
			SlabColl1.setmGoKArrears(obj.getmGoKArrears());
			SlabColl1.setmGoKPayable(obj.getmGoKPayable());
			SlabColl1.setmGprs_Flag(obj.getmGprs_Flag());
			SlabColl1.setmGps_Latitude_image(obj.getmGps_Latitude_image());
			SlabColl1.setmGps_Latitude_print(obj.getmGps_Latitude_print());
			SlabColl1.setmGps_LatitudeCardinal_image(obj.getmGps_LatitudeCardinal_image());
			SlabColl1.setmGps_LatitudeCardinal_print(obj.getmGps_LatitudeCardinal_print());
			SlabColl1.setmGps_Longitude_image(obj.getmGps_Longitude_image());
			SlabColl1.setmGps_Longitude_print(obj.getmGps_Longitude_print());
			SlabColl1.setmGps_LongitudeCardinal_image(obj.getmGps_LongitudeCardinal_image());
			SlabColl1.setmGps_LatitudeCardinal_print(obj.getmGps_LatitudeCardinal_print());
			SlabColl1.setmGvpId(obj.getmGvpId());
			SlabColl1.setmHCReb(obj.getmHCReb());
			SlabColl1.setmHLReb(obj.getmHLReb());
			SlabColl1.setmHWCReb(obj.getmHWCReb());
			SlabColl1.setmImage_Cap_Date(obj.getmImage_Cap_Date());
			SlabColl1.setmImage_Cap_Time(obj.getmImage_Cap_Time());
			SlabColl1.setmImage_Name(obj.getmImage_Name());
			SlabColl1.setmImage_Path(obj.getmImage_Path());
			SlabColl1.setmIntrst_Unpaid(obj.getmIntrst_Unpaid());
			SlabColl1.setmIntrstCurnt(obj.getmIntrstCurnt());
			SlabColl1.setmIntrstOld(obj.getmIntrstOld());
			SlabColl1.setmIODD11_Remarks(obj.getmIODD11_Remarks());
			SlabColl1.setmIODRemarks(obj.getmIODRemarks());
			SlabColl1.setmIssueDateTime(obj.getmIssueDateTime());
			SlabColl1.setmKVA_Consumption(obj.getmKVA_Consumption());
			SlabColl1.setmKVAAssd_Cons(obj.getmKVAAssd_Cons());
			SlabColl1.setmKVAFR(obj.getmKVAFR());
			SlabColl1.setmKVAH_OldConsumption(obj.getmKVAH_OldConsumption());
			SlabColl1.setmKVAIR(obj.getmKVAIR());
			SlabColl1.setmLatitude(obj.getmLatitude());
			SlabColl1.setmLatitude_Dir(obj.getmLatitude_Dir());
			SlabColl1.setmLegFol(obj.getmLegFol());
			SlabColl1.setmLinMin(obj.getmLinMin());
			SlabColl1.setmLocationCode(obj.getmLocationCode());
			SlabColl1.setmLongitude(obj.getmLongitude());
			SlabColl1.setmLongitude_Dir(obj.getmLongitude_Dir());
			SlabColl1.setmMCHFlag(obj.getmMCHFlag());
			SlabColl1.setmMeter_serialno(obj.getmMeter_serialno());
			SlabColl1.setmMeter_type(obj.getmMeter_type());
			SlabColl1.setmMF(obj.getmMF());
			SlabColl1.setmMobileNo(obj.getmMobileNo());
			SlabColl1.setmMtd(obj.getmMtd());
			SlabColl1.setmMtrDisFlag(obj.getmMtrDisFlag());
			SlabColl1.setmNewNoofDays(obj.getmNewNoofDays());
			SlabColl1.setmNoOfDays(obj.getmNoOfDays());
			SlabColl1.setmOld_Consumption(obj.getmOld_Consumption());
			SlabColl1.setmOldConnID(obj.getmOldConnID());
			SlabColl1.setmOtherChargeLegend(obj.getmOtherChargeLegend());
			SlabColl1.setmOthers(obj.getmOthers());
			SlabColl1.setmPaid_Amt(obj.getmPaid_Amt());
			SlabColl1.setmPayment_Mode(obj.getmPayment_Mode());
			SlabColl1.setmPenExLd(obj.getmPenExLd());
			SlabColl1.setmPF(obj.getmPF());
			SlabColl1.setmPfPenAmt(obj.getmPfPenAmt());
			SlabColl1.setmPreRead(obj.getmPreRead());
			SlabColl1.setmPreStatus(obj.getmPreStatus());//Current/Present status
			SlabColl1.setmPrevRdg(obj.getmPrevRdg());
			SlabColl1.setmRcptCnt(obj.getmRcptCnt());
			SlabColl1.setmReaderCode(obj.getmReaderCode());
			SlabColl1.setmReadingDay(obj.getmReadingDay());
			SlabColl1.setmRebateFlag(obj.getmRebateFlag());
			SlabColl1.setmReceipt_No(obj.getmReceipt_No());
			SlabColl1.setmReceiptAmnt(obj.getmReceiptAmnt());
			SlabColl1.setmReceiptDate(obj.getmReceiptDate());
			SlabColl1.setmReceipttypeflag(obj.getmReceipttypeflag());
			SlabColl1.setmRecordDmnd(obj.getmRecordDmnd());
			SlabColl1.setmRemarks(obj.getmRemarks());
			SlabColl1.setmRRFlag(obj.getmRRFlag());
			SlabColl1.setmRRNo(obj.getmRRNo());
			SlabColl1.setmSancHp(obj.getmSancHp());
			SlabColl1.setmSancKw(obj.getmSancKw());
			SlabColl1.setmSancLoad(obj.getmSancLoad());
			SlabColl1.setmSBMNumber(obj.getmSBMNumber());
			SlabColl1.setmSectionName(obj.getmSectionName());
			SlabColl1.setmSlowRtnPge(obj.getmSlowRtnPge());
			SlabColl1.setmSpotSerialNo(obj.getmSpotSerialNo());
			SlabColl1.setmStatus(obj.getmStatus());//Previous Status
			SlabColl1.setmSubId(obj.getmSubId());
			SlabColl1.setmSupply_Points(obj.getmSupply_Points());           
			SlabColl1.setmTariffCode(obj.getmTariffCode());
			SlabColl1.setmTarifString(obj.getmTarifString());     
			SlabColl1.setmTaxAmt(obj.getmTaxAmt());
			SlabColl1.setmTaxFlag(obj.getmTaxFlag());
			SlabColl1.setmTcName(obj.getmTcName());
			SlabColl1.setmTEc(obj.getmTEc());
			SlabColl1.setmTFc(obj.getmTFc());
			SlabColl1.setmThirtyFlag(obj.getmThirtyFlag());
			SlabColl1.setmTourplanId(obj.getmTourplanId());
			SlabColl1.setmTvmMtr(obj.getmTvmMtr());
			SlabColl1.setmTvmPFtype(obj.getmTvmPFtype());
			SlabColl1.setmUnits(obj.getmUnits());
			SlabColl1.setMmUID(obj.getMmUID());
			SlabColl1.setMmECRate_Count(obj.getMmECRate_Count());
			SlabColl1.setMmECRate_Row(obj.getMmECRate_Row());
			SlabColl1.setMmFCRate_1(obj.getMmFCRate_1());
			SlabColl1.setMmCOL_FCRate_2(obj.getMmCOL_FCRate_2());
			SlabColl1.setMmUnits_1(obj.getMmUnits_1());
			SlabColl1.setMmUnits_2(obj.getMmUnits_2());
			SlabColl1.setMmUnits_3(obj.getMmUnits_3());
			SlabColl1.setMmUnits_4(obj.getMmUnits_4());
			SlabColl1.setMmUnits_5(obj.getMmUnits_5());
			SlabColl1.setMmUnits_6(obj.getMmUnits_6());
			SlabColl1.setMmEC_Rate_1(obj.getMmEC_Rate_1());
			SlabColl1.setMmEC_Rate_2(obj.getMmEC_Rate_2());
			SlabColl1.setMmEC_Rate_3(obj.getMmEC_Rate_3());
			SlabColl1.setMmEC_Rate_4(obj.getMmEC_Rate_4());
			SlabColl1.setMmEC_Rate_5(obj.getMmEC_Rate_5());
			SlabColl1.setMmEC_Rate_6(obj.getMmEC_Rate_6());
			SlabColl1.setMmEC_1(obj.getMmEC_1());
			SlabColl1.setMmEC_2(obj.getMmEC_2());
			SlabColl1.setMmEC_3(obj.getMmEC_3());
			SlabColl1.setMmEC_4(obj.getMmEC_4());
			SlabColl1.setMmEC_5(obj.getMmEC_5());
			SlabColl1.setMmEC_6(obj.getMmEC_6());
			SlabColl1.setMmFC_1(obj.getMmFC_1());
			SlabColl1.setMmFC_2(obj.getMmFC_2());
			SlabColl1.setMmTEc(obj.getMmTEc());
			SlabColl1.setMmEC_FL_1(obj.getMmEC_FL_1());
			SlabColl1.setMmEC_FL_2(obj.getMmEC_FL_2());
			SlabColl1.setMmEC_FL_3(obj.getMmEC_FL_3());
			SlabColl1.setMmEC_FL_4(obj.getMmEC_FL_4());
			SlabColl1.setMmEC_FL(obj.getMmEC_FL());
			SlabColl1.setMmnew_TEc(obj.getMmnew_TEc());
			SlabColl1.setMmold_TEc(obj.getMmold_TEc());


			//18-08-2015
			SlabColl1.setmMeter_Present_Flag(obj.getmMeter_Present_Flag());
			SlabColl1.setmMtr_Not_Visible(obj.getmMtr_Not_Visible());
			SlabColl1.setmDLTEc_GoK(obj.getmDLTEc_GoK());		
			SlabColl1.setmAadharNo(obj.getmAadharNo());
			SlabColl1.setmVoterIdNo(obj.getmVoterIdNo());				
			SlabColl1.setmMtrMakeFlag(obj.getmMtrMakeFlag());
			SlabColl1.setmMtrBodySealFlag(obj.getmMtr_Not_Visible());
			SlabColl1.setmMtrTerminalCoverFlag(obj.getmMtrTerminalCoverFlag());
			SlabColl1.setmMtrTerminalCoverSealedFlag(obj.getmMtrTerminalCoverSealedFlag());

			//24-06-2021
			SlabColl1.setMmNEWFC_UNIT1(obj.getMmNEWFC_UNIT1());
			SlabColl1.setMmNEWFC_UNIT2(obj.getMmNEWFC_UNIT2());
			SlabColl1.setMmNEWFC_UNIT3(obj.getMmNEWFC_UNIT3());
			SlabColl1.setMmNEWFC_UNIT4(obj.getMmNEWFC_UNIT4());				
			SlabColl1.setMmNEWFC_Rate3(obj.getMmNEWFC_Rate3());
			SlabColl1.setMmNEWFC_Rate4(obj.getMmNEWFC_Rate4());			
			SlabColl1.setMmNEWFC3(obj.getMmNEWFC3());
			SlabColl1.setMmNEWFC4(obj.getMmNEWFC4());
			SlabColl1.setmFC_Slab_3(obj.getmFC_Slab_3());

		}
		public void run(){
			setPriority(Thread.MAX_PRIORITY);
			PrinterCharacterAlign pca = new PrinterCharacterAlign();//Class Alignment for printer 
			CommonFunction cFun = new CommonFunction();			
			try 
			{
				///////////////////////////////////////////////Definition Start/////////////////////////////////////
				String pSubDiv = db.GetSubDivisionName().trim();
				String pFeeder = SlabColl1.getmSectionName()==null?" ":SlabColl1.getmSectionName().trim();
				String pLocationCode= SlabColl1.getmLocationCode()==null?" ":SlabColl1.getmLocationCode().trim();
				String pConnectionNo = SlabColl1.getmConnectionNo()==null?" ":SlabColl1.getmConnectionNo().trim();
				String pRRNo = SlabColl1.getmRRNo().trim();
				String pCustomerName = SlabColl1.getmCustomerName()==null?" ":SlabColl1.getmCustomerName().trim();
				String pAddress1 = SlabColl1.getmAddress1()==null?" ":SlabColl1.getmAddress1().trim();
				String pAddress2 = SlabColl1.getmAddress2()==null?" ":SlabColl1.getmAddress2().trim();
				String pReaderCode = SlabColl1.getmReaderCode()==null?" ":SlabColl1.getmReaderCode().trim();
				String pTariffString  =cFun.SplitTarifString(SlabColl1.getmTarifString()).trim();
				String pBillDate =SlabColl1.getmBillDate()==null?" ":cFun.DateConvertAddChar(SlabColl1.getmBillDate(), "-");				
				String pBillNo = SlabColl1.getmBillNo()==null?" ":SlabColl1.getmBillNo().trim();

				//20-04-2017
				String pBillIssueDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());

				if(SlabColl1.getmTariffCode()==120 || SlabColl1.getmTariffCode()==121) //Nitish 20-04-2017
				{

				}
				else
				{
					pBillIssueDate = cFun.GetCurrentTimeWSPChar(SlabColl1.getmIssueDateTime());	
					if(pBillIssueDate.trim().length()>16)
					{
						pBillIssueDate = pBillIssueDate.trim().substring(0, 16);
					}
				}
				//End 20-04-2017
				String pLineMin = " ";
				String pCTRatio = cFun.GetTwoDecimalPlace(String.valueOf(SlabColl1.getmMF()));
				//14-08-2019
				if(!SlabColl1.getmMtd().trim().equals("1"))
				{
					pCTRatio = "0.00";
				}
				String pFLPartBill = " ";
				String pPresRdg = " ";
				String pPrevRdg = " ";
				String pUnits = " ";
				String pFACUnits = " "; //09-08-2019
				String pPF = " ";
				String pPenaltyChgs =" " ; 
				String pDRFees ="0.00" ; 
				String pPenOnEx =" " ; 
				String pPFPenalty  = " " ; 
				String pRebate  = " " ;
				String pInterest  = " " ;				
				String pBillTotal  = " " ;
				String pTaxOnamount  = " " ;
				String pArrears  = " " ;
				String pCredit = " ";
				String pOthers = " " ;
				String pNetAmtDue = " " ;
				String pDueDate = " " ;

				String pGOkSubsidy = "0.00";
				String pGokPayable = " " ;
				String pCustPayable = " " ;
				String pIodRemark = " " ;
				String pIod11Remark = " " ;
				String pTotalRemarks = "";
				String pbarValue = " "; 

				//12-03-2019
				String newDRFees = " "; 
				String newReadingFees = " "; 
				String newReturnFees = " "; 
				String newCGST = " "; 
				String newSGST = " "; 

				String pprintSancLoad = cFun.GetSancLoadWithHPKW(SlabColl1).trim();
				if(SlabColl1.getmRecordDmnd().compareTo(new BigDecimal(0.00)) > 0)
				{
					//BigDecimal dm = (SlabColl.getmRecordDmnd().divide(SlabColl.getmMF())).multiply(new BigDecimal(0.746));
					pprintSancLoad =  pprintSancLoad+" / "+ SlabColl1.getmRecordDmnd().setScale(2, BigDecimal.ROUND_HALF_UP).toString().trim();
				}
				///////PresRdg/////////////////				
				if(SlabColl1.getmPreStatus().equals("0"))
				{
					pPresRdg = SlabColl1.getmPreRead()==null?" ":SlabColl1.getmPreRead().trim();
				}
				else 
				{
					pPresRdg = cFun.statusAsName(Integer.valueOf(SlabColl1.getmPreStatus())).trim();

				}
				/////////////30-07-2016 Start///////////////
				// Commented 30-07-2016
				//String prevname1[]= SlabColl1.getmPrevRdg().split("\\.");
				//Modified 27-02-2017
				String prevname1[]={ "0","0"};
				try
				{
					prevname1[0]= String.valueOf(new BigDecimal(SlabColl1.getmPrevRdg().trim()).setScale(2, RoundingMode.HALF_UP));
				}
				catch(Exception e)
				{
					prevname1= SlabColl1.getmPrevRdg().split("\\.");
				}								
				// Commented 30-07-2016
				//String getunit1[]= SlabColl1.getmUnits().split("\\.");

				//Added   30-07-2016
				String getunit1[]={ "0","0"};
				try
				{
					getunit1[0]= String.valueOf(new BigDecimal(SlabColl1.getmUnits().trim()).setScale(0, RoundingMode.HALF_UP));
				}
				catch(Exception e)
				{
					getunit1= SlabColl1.getmUnits().split("\\.");
				}
				/////////////30-07-2016 End///////////////
				//////////////PrevRdg/////////////////////
				//String prevname1[]= SlabColl1.getmPrevRdg().split("\\.");
				pPrevRdg = SlabColl1.getmPrevRdg()==null?" ":prevname1[0];
				/////////////////////Units/////////////////////

				//String getunit1[]= SlabColl1.getmUnits().split("\\.");
				boolean isSlowRtn=false;
				if(SlabColl1.getmSlowRtnPge().compareTo(new BigDecimal(0.00)) > 0 && (SlabColl1.getmPreStatus().equals("0") || SlabColl1.getmPreStatus().equals("3")) )
				{
					isSlowRtn=true;

				}
				if(SlabColl1.getmSlowRtnPge().compareTo(new BigDecimal(0.00)) < 0 && (SlabColl1.getmPreStatus().equals("0") || SlabColl1.getmPreStatus().equals("3")))
				{
					isSlowRtn=true;
				}

				//Modified 09-08-2019
				if(isSlowRtn)
				{					
					pUnits = SlabColl1.getmUnits()==null? " ":"(Slow Rotation) " +getunit1[0];//Unit Consumed ---> Check
					pFACUnits = SlabColl1.getmUnits()==null? "0":getunit1[0];//Unit Consumed ---> Check
				}
				//End Slow Rotation
				else
				{
					if(SlabColl1.getmPreStatus().equals("0") || SlabColl1.getmPreStatus().equals("3") || SlabColl1.getmPreStatus().equals("8") || SlabColl1.getmPreStatus().equals("6"))
					{
						pUnits = SlabColl1.getmUnits()==null?" ":getunit1[0];//Unit Consumed ---> Check
						pFACUnits = SlabColl1.getmUnits()==null? "0":getunit1[0];
					}
					else 
					{						
						if(SlabColl1.getmPreStatus().equals("4"))//if it is MCH(Meter changed)
						{
							if(SlabColl1.getmMCHFlag().equals("1"))//Check for MCHFlag(), if it 1 then print normal
							{
								pUnits = SlabColl1.getmUnits()==null?" ":getunit1[0];//Unit Consumed ---> Check
								pFACUnits = SlabColl1.getmUnits()==null? "0":getunit1[0];
							}
							else//else print Average
							{
								pUnits = "ASSD AVG " +  getunit1[0];
								pFACUnits = getunit1[0];
							}
						}
						else
						{
							pUnits = "ASSD AVG " +  getunit1[0];
							pFACUnits = getunit1[0];
						}						
					}
				}
				////////////////////////////PF///////////////////////

				//25-04-2017
				if((SlabColl1.getmPF()!=null || SlabColl1.getmPF()!="" || SlabColl1.getmPF()!="0.85" || SlabColl1.getmPF()!=".85") && SlabColl1.getmPF().trim().length() > 0)
				{
					if(new BigDecimal(SlabColl1.getmPF()).compareTo(new BigDecimal(0.85))==1)
					{
						pPF = SlabColl1.getmPF() == null?" ":SlabColl1.getmPF();						
					}					
				}
				/////////////////////FC ////////////////////////
				BillingParameters bp = pca.getFCEC(SlabColl1);		
				ArrayList<String> pFC = new ArrayList<String>();

				try
				{
					if (SlabColl1.getmTvmMtr().equals("4")) //Nitish 30-05-2015 
					{					
						BigDecimal roundUnits = pca.roundoffQuarter(new BigDecimal(SlabColl1.getmSancLoad().trim()).multiply(new BigDecimal(0.746)));
						String Unit1 = String.valueOf(roundUnits);

						if(bp.getmFixedCharges().size() == 1) 
						{
							//FC Slab 1  Amount is 0 then dont print FC
							if(new BigDecimal(bp.getmFixedCharges().get(0).getAmount()).compareTo(new BigDecimal(0.00)) > 0)
							{

								pFC.add(pca.ThreeParallelStringKannada(cFun.GetTwoDecimalPlace(Unit1),bp.getmFixedCharges().get(0).getRate(),bp.getmFixedCharges().get(0).getAmount(),' '));

							}

						}
						else if(bp.getmFixedCharges().size() == 2)
						{
							if(new BigDecimal(bp.getmFixedCharges().get(1).getAmount()).compareTo(new BigDecimal(0.00)) > 0)
							{
								pFC.add(pca.ThreeParallelStringKannada(cFun.GetTwoDecimalPlace(Unit1),bp.getmFixedCharges().get(0).getRate(),bp.getmFixedCharges().get(0).getAmount(),' '));
								pFC.add(pca.ThreeParallelStringKannada(cFun.GetTwoDecimalPlace(Unit1),bp.getmFixedCharges().get(1).getRate(),bp.getmFixedCharges().get(1).getAmount(),' '));
							}

						}		
						else if(bp.getmFixedCharges().size() == 3) //Added 24-06-2021
						{
							if(new BigDecimal(bp.getmFixedCharges().get(2).getAmount()).compareTo(new BigDecimal(0.00)) > 0)
							{
								pFC.add(pca.ThreeParallelStringKannada(cFun.GetTwoDecimalPlace(bp.getmFixedCharges().get(0).getUnitRange()),bp.getmFixedCharges().get(0).getRate(),bp.getmFixedCharges().get(0).getAmount(),' '));
								pFC.add(pca.ThreeParallelStringKannada(cFun.GetTwoDecimalPlace(bp.getmFixedCharges().get(1).getUnitRange()),bp.getmFixedCharges().get(1).getRate(),bp.getmFixedCharges().get(1).getAmount(),' '));
								pFC.add(pca.ThreeParallelStringKannada(cFun.GetTwoDecimalPlace(bp.getmFixedCharges().get(2).getUnitRange()),bp.getmFixedCharges().get(2).getRate(),bp.getmFixedCharges().get(2).getAmount(),' '));
							}							
						}
					}
					else
					{
						if(bp.getmFixedCharges().size() == 1) 
						{
							//FC Slab 1  Amount is 0 then dont print FC
							if(new BigDecimal(bp.getmFixedCharges().get(0).getAmount()).compareTo(new BigDecimal(0.00)) > 0)
							{								
								pFC.add(pca.ThreeParallelStringKannada(cFun.GetTwoDecimalPlace(bp.getmFixedCharges().get(0).getUnitRange()),bp.getmFixedCharges().get(0).getRate(),bp.getmFixedCharges().get(0).getAmount(),' '));
							}							
						}
						else if(bp.getmFixedCharges().size() == 2)
						{
							if(new BigDecimal(bp.getmFixedCharges().get(1).getAmount()).compareTo(new BigDecimal(0.00)) > 0)
							{
								pFC.add(pca.ThreeParallelStringKannada(cFun.GetTwoDecimalPlace(bp.getmFixedCharges().get(0).getUnitRange()),bp.getmFixedCharges().get(0).getRate(),bp.getmFixedCharges().get(0).getAmount(),' '));
								pFC.add(pca.ThreeParallelStringKannada(cFun.GetTwoDecimalPlace(bp.getmFixedCharges().get(1).getUnitRange()),bp.getmFixedCharges().get(1).getRate(),bp.getmFixedCharges().get(1).getAmount(),' '));
							}							
						}			
					}					
				}
				catch(Exception e)
				{

				}
				//FC Printing End===================================================================	

				/////////////////////EC ////////////////////////
				ArrayList<String> pEC = new ArrayList<String>();
				for(int t = 0; t < bp.getmEnergyCharges().size(); t++)
				{
					if(t == 0)
					{
						pEC.add(pca.ThreeParallelStringKannada(cFun.GetTwoDecimalPlace(bp.getmEnergyCharges().get(t).getUnitRange()),bp.getmEnergyCharges().get(t).getRate(),bp.getmEnergyCharges().get(t).getAmount(),' '));


					}
					else
					{
						if(!cFun.IsEcChargeContainsZero(bp, t))
						{
							pEC.add(pca.ThreeParallelStringKannada(cFun.GetTwoDecimalPlace(bp.getmEnergyCharges().get(t).getUnitRange()),bp.getmEnergyCharges().get(t).getRate(),bp.getmEnergyCharges().get(t).getAmount(),' '));

						}
					}
				}


				//////////////////////EC END//////////////////////////////////////////////


				if(SlabColl1.getmPenExLd().compareTo(new BigDecimal(0.00)) > 0)
				{
					if(SlabColl1.getmTarifString().indexOf("LT-1")!= -1 || SlabColl1.getmTarifString().indexOf("LT-2")!= -1 )
					{
						pPenOnEx = "BMD ( "+SlabColl1.getmExLoad().setScale(2, BigDecimal.ROUND_HALF_UP).toString()+" * 1.5 )"  + " -> " + 
								cFun.GetTwoDecimalPlace(String.valueOf(SlabColl1.getmPenExLd()));
					}
					else
					{
						pPenOnEx = "BMD ( "+SlabColl1.getmExLoad().setScale(2, BigDecimal.ROUND_HALF_UP).toString()+" * 2 )"  + " -> " + 
								cFun.GetTwoDecimalPlace(String.valueOf(SlabColl1.getmPenExLd()));
					}
				}
				else
				{
					pPenOnEx = "0.00";

				}	
				if((SlabColl1.getmPF()!=null || SlabColl1.getmPF()!="" || SlabColl1.getmPF()!="0.85" || SlabColl1.getmPF()!=".85") && SlabColl1.getmPF().trim().length() > 0)
				{

					if(cFun.GetPFPenalty(SlabColl1).trim().equals("0.00"))
					{
						pPFPenalty = "0.00";
					}
					else
					{
						if(SlabColl1.getmTariffCode()>=60 && SlabColl1.getmTariffCode()<=65)
						{
							//String pflt4 =  "Cap.Penalty -> " + String.valueOf(SlabColl1.getmSancHp()) + " *  5 = " +  String.valueOf(SlabColl1.getmPfPenAmt().setScale(2, RoundingMode.HALF_UP).toString());
							//31-05-2018
							String pflt4 =  "Cap.Penalty -> " + String.valueOf(SlabColl1.getmSancHp()) + " *  5.83 = " +  String.valueOf(SlabColl1.getmPfPenAmt().setScale(2, RoundingMode.HALF_UP).toString());
							pPFPenalty = "\n" + pflt4;
						}
						else
						{
							pPFPenalty = cFun.GetPFPenalty(SlabColl1);
						}
					}
				}
				else
				{
					pPFPenalty = "0.00";

				}
				pRebate =cFun.getRebateAndWA(SlabColl1);		
				pInterest = cFun.GetTwoDecimalPlace(SlabColl1.getmIntrstCurnt());
				BigDecimal txtTaxamt = SlabColl1.getmTaxAmt() == null ? new BigDecimal(0.00) : SlabColl1.getmTaxAmt().setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal txtArrers = SlabColl1.getmArears() == null ? new BigDecimal(0.00) : SlabColl1.getmArears().setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal txtArrersold = SlabColl1.getmArrearsOld().setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal txtIntstold = SlabColl1.getmIntrstOld().setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal txtOther = new BigDecimal(SlabColl1.getmOthers());//.add(SlabColl1.getmKVAAssd_Cons().setScale(2, BigDecimal.ROUND_HALF_UP));//Add Others with FEC 28-06-2014
				BigDecimal FEC = SlabColl1.getmKVAAssd_Cons().setScale(2, BigDecimal.ROUND_HALF_UP);

				//BigDecimal sub = txtTaxamt.add(txtArrers.add(txtArrersold.add(txtIntstold.add(txtOther.add(FEC)))));
				BigDecimal sub = txtArrers.add(txtArrersold.add(txtIntstold));
				BigDecimal BillAmount = SlabColl1.getmBillTotal().subtract(sub);

				pBillTotal = String.valueOf(BillAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
				pTaxOnamount = String.valueOf(txtTaxamt);
				pArrears = String.valueOf(txtArrers.add(txtArrersold.add(txtIntstold)));
				if(FEC.compareTo(new BigDecimal(0)) == 1)
				{
					// pCredit = "FAC " +cFun.GetTwoDecimalPlace(FEC.toString());
					pCredit = cFun.GetTwoDecimalPlace(FEC.toString());
				}
				else
				{
					pCredit = cFun.GetTwoDecimalPlace(FEC.toString());

				}

				//////////////////FAC 09-08-2019 /////////////////////////////////
				ArrayList<String> pFAC = new ArrayList<String>();
				pFAC.add(pca.ThreeParallelStringKannada(pFACUnits.trim(),SlabColl1.getmFACValue().toString().trim(),pCredit.trim(),' '));
				try
				{

					//End  Modified 25-11-2020
					if(txtOther.compareTo(new BigDecimal(0)) == 1)
					{
						try
						{
							pOthers = (SlabColl1.getmOtherChargeLegend().trim() + " " +cFun.GetTwoDecimalPlace(txtOther.toString()));
						}
						catch(Exception e)
						{
							txtOther = new BigDecimal(SlabColl1.getmOthers());
						}
					}
					else
					{
						pOthers = cFun.GetTwoDecimalPlace(txtOther.toString());

					}
				}
				catch(Exception e)
				{
					pOthers = cFun.GetTwoDecimalPlace(txtOther.toString());
				}

				BigDecimal bill = (SlabColl1.getmBillTotal().setScale(2, BigDecimal.ROUND_HALF_UP)).setScale(0, BigDecimal.ROUND_HALF_UP);//SlabColl.getmBillTotal().setScale(0, BigDecimal.ROUND_HALF_UP);
				pNetAmtDue = cFun.GetTwoDecimalPlace(bill.toString());
				pDueDate = SlabColl1.getmDueDate()==null?" ":cFun.DateConvertAddChar(SlabColl1.getmDueDate(),"-");
				if(SlabColl1.getmTarifString().indexOf("LT-1")!= -1)//If LT-1 Print "CUST PAYABLE", "GOK PAYABLE"
				{
					//31-12-2014
					BigDecimal gokpayablea_arrears_add = SlabColl1.getmGoKPayable().add(SlabColl1.getmGoKArrears());	
					BigDecimal CustPayable = (SlabColl1.getmBillTotal().setScale(0, BigDecimal.ROUND_HALF_UP)).subtract(gokpayablea_arrears_add.setScale(0, BigDecimal.ROUND_HALF_UP));


					pCustPayable = "CUST PAYABLE: " + cFun.GetTwoDecimalPlace(CustPayable.setScale(0, BigDecimal.ROUND_HALF_UP).toString());
					pGokPayable = "GOK PAYABLE: " + cFun.GetTwoDecimalPlace(String.valueOf(gokpayablea_arrears_add.setScale(0, BigDecimal.ROUND_HALF_EVEN)));
					pGOkSubsidy =  cFun.GetTwoDecimalPlace(String.valueOf(gokpayablea_arrears_add.setScale(0, BigDecimal.ROUND_HALF_EVEN)));
				}
				//Added 13-01-2021 for LT-4
				if(SlabColl1.getmTarifString().indexOf("LT-4")!= -1)//If LT-4 Print "CUST PAYABLE", "GOK PAYABLE"
				{
					if (SlabColl1.getmTaxFlag().equals("Y") && ((SlabColl1.getmSancKw().compareTo(new BigDecimal(10.00))==-1) || (SlabColl1.getmSancKw().compareTo(new BigDecimal(10.00))==0)))
					{						
						BigDecimal gokpayablea_arrears_add = SlabColl1.getmGoKPayable().add(SlabColl1.getmGoKArrears());	
						BigDecimal CustPayable = (SlabColl1.getmBillTotal().setScale(0, BigDecimal.ROUND_HALF_UP)).subtract(gokpayablea_arrears_add.setScale(0, BigDecimal.ROUND_HALF_UP));

						pCustPayable = "CUST PAYABLE: " + cFun.GetTwoDecimalPlace(CustPayable.setScale(0, BigDecimal.ROUND_HALF_UP).toString());
						pGokPayable = "GOK PAYABLE: " + cFun.GetTwoDecimalPlace(String.valueOf(gokpayablea_arrears_add.setScale(0, BigDecimal.ROUND_HALF_EVEN)));
						pGOkSubsidy =  cFun.GetTwoDecimalPlace(String.valueOf(gokpayablea_arrears_add.setScale(0, BigDecimal.ROUND_HALF_EVEN)));
					}				
				}
				if(SlabColl1.getmIODRemarks().trim() != "")
				{
					pIodRemark =SlabColl1.getmIODRemarks();
				}

				if(SlabColl1.getmIODD11_Remarks().trim() != "")
				{
					pIod11Remark = SlabColl1.getmIODD11_Remarks();
				}
				if(pGokPayable.trim().length()>0)
				{
					pTotalRemarks = pTotalRemarks+pGokPayable+"     "+pCustPayable+"\n\n" ;
				}				
				try
				{
					try
					{
						if(pIodRemark.trim().length()>0)
						{			
							pTotalRemarks = pTotalRemarks+ pIodRemark.trim()+"\n\n" ; //28-10-2020
						}
					}
					catch(Exception e)
					{

					}
					try
					{
						if(SlabColl1.getmDlCount()  >1)
						{		
							if(SlabColl1.getmPreStatus().trim().equals("0") || SlabColl1.getmPreStatus().trim().equals("3") || SlabColl1.getmPreStatus().trim().equals("4"))
							{
								pTotalRemarks = pTotalRemarks+ " FC is calculated for 1 month " +"\n\n" ;
								pTotalRemarks = pTotalRemarks+ " EC is calculated for " +  String.valueOf(SlabColl1.getmDlCount()) +" months " +"\n\n" ;
							}
						}						
					}
					catch(Exception e)
					{

					}
					if(SlabColl1.getmPreStatus().trim().equals("1"))
					{
						pTotalRemarks = pTotalRemarks+"\n Average Bill is issued due to door lock. \n\n";						
					}
					else if(SlabColl1.getmPreStatus().trim().equals("2"))
					{
						pTotalRemarks = pTotalRemarks+"\n Average Bill is issued due to MNR. \n\n";						
					}
					else if(SlabColl1.getmPreStatus().trim().equals("14"))
					{
						pTotalRemarks = pTotalRemarks+"\n Average Bill is issued due to MBO. \n\n";						
					}
				}
				catch(Exception e)
				{

				}
				if(pIod11Remark.trim().length()>0)
				{
					pTotalRemarks = pTotalRemarks+pIod11Remark+"\n\n" ;
				}

				//////////////////////ECS Message 29-04-2019////////////////////////////		
				try
				{
					if(SlabColl1.getmTcName().trim().equals("1"))
						pTotalRemarks = pTotalRemarks+ "     Bill is Payable through ECS/NACH" + "\n\n" ;
				}
				catch(Exception e)
				{

				}

				pbarValue= SlabColl1.getmConnectionNo().trim() + SlabColl1.getmBillTotal().setScale(0, BigDecimal.ROUND_HALF_EVEN);
				///////////////////////////////////////////////End Definition/////////////////////////////////////

				AmigosESCCmd pos = new AmigosESCCmd();
				//byte[] amigosFont = pos.POS_S_TextOut("", "UTF-8", 0, 0, 0, 0, 0x00);	
				//bptr.sendData(amigosFont);//			

				Typeface typeface1;
				//typeface1=Typeface.create(Typeface.SANS_SERIF,Typeface.NORMAL);
				//typeface1=Typeface.createFromAsset(getAssets(), "fonts/OpenSans-ExtraBoldItalic.ttf");
				typeface1 = Typeface.create(Typeface.SANS_SERIF,Typeface.BOLD);

				String Print1= "";
				try
				{
					if(SlabColl1.getmForMonth().trim().substring(0, 2).equals("01"))
					{
						Print1 = Print1 +  "\n" + pca.CreateBlankSpaceSequence(10, " ") +"------------- Happy New Year ------------"+"\n";
					}
				}
				catch(Exception e)
				{

				}

				//Print1 = Print1 +  "\n" + pca.CreateBlankSpaceSequence(10, " ") +"------------- Happy New Year ------------"+"\n";
				//Print1 = Print1 +  "\n" + pca.CreateBlankSpaceSequence(16, " ") +"--------------- Re-Print ---------------"+"\n";
				Print1 = Print1 +   "\n"+ pca.CreateBlankSpaceSequence(6, " ")+ "ಹುಬ್ಬಳ್ಳಿ ವಿದ್ಯುತ್ ಸರಬರಾಜು ಕಂಪನಿ ನಿಯಮಿತ"+"\n";
				Print1 = Print1 +  "\n"+pca.CreateBlankSpaceSequence(32, " ")+"ವಿದ್ಯುತ್ ಬಿಲ್" + "\n";
				Print1 = Print1 +  "\n"+pca.CreateBlankSpaceSequence(22, " ")+"GSTIN:" + ConstantClass.mGSTIN + "\n";
				Print1 = Print1 +"\n  "+ pca.CreateBlankSpaceSequence(4, " ")+ pca.CreateBlankSpaceSequence(50,"-")+ "\n";
				//byte[] imData  = pos.createImageFromString(headerPrint, getApplicationContext());



				Print1 += "\n  ಉಪ ವಿಭಾಗ   "+ getRightAlignData(pSubDiv.trim(),5,49) + "\n";			
				Print1 += "\n  ಸ್ಥಳ ಸಂಕೇತ"+ pca.RightAlignInBoxLayoutNew3(pLocationCode.trim()," ",55)+ "\n";	
				Print1 += "\n  ಮಾ.ಓ.ಸಂಕೇತ   "+ getRightAlignData(pReaderCode.trim(),3,48)+ "\n";		


				String Print2= "";
				Print2 += "\n  ಆರ್.ಆರ್.ಸಂಖ್ಯೆ  "+  pca.RightAlignInBoxLayoutNew3(pRRNo.trim()," ",31)+ "\n";	
				Print2 += "\n  ಗ್ರಾಹಕರ ಐ.ಡಿ   "+ pca.RightAlignInBoxLayoutNew3(pConnectionNo.trim()," ",37)+ "\n";				

				String Print3= "";
				Print3 += "\n  ಹೆಸರು ಮತ್ತು ವಿಳಾಸ  \n\n" + pca.CreateBlankSpaceSequence(3," ")+ pCustomerName.trim() + "\n\n" +pca.CreateBlankSpaceSequence(3," ")+ pAddress1.trim() +"\n\n" + pca.CreateBlankSpaceSequence(3," ") +pAddress2.trim()+"\n\n" ;
				if(pTariffString.contains("LT-1"))
					Print3 += "\n  ಜಕಾತಿ   "+getRightAlignData(pTariffString.trim(),7,61)+ "\n";
				else
					Print3 += "\n  ಜಕಾತಿ   "+getRightAlignData(pTariffString.trim(),7,69)+ "\n";
				Print3 += "\n  ಮಂ.ವಿ.ಪ್ರಮಾಣ   "+ getRightAlignData(pprintSancLoad.trim(),5,46)+ "\n";		
				Print3 += "\n  ಓದುವ ದಿನಾಂಕ "+ pca.RightAlignInBoxLayoutNew3(pBillDate.trim()," ",46)+ "\n";		
				//Print3 += "\n  ಬಿಲ್  ದಿನಾಂಕ   "+ pca.RightAlignInBoxLayoutNew3(pBillIssueDate.trim()," ",41)+ "\n";		//31-05-2020
				Print3 += "\n  ಬಿಲ್ ಸಂಖ್ಯೆ   "+ pca.RightAlignInBoxLayoutNew3(pBillNo.trim()," ",52)+ "\n";


				Print3 += "\n  ಹಾಲಿ ಮಾಪನ  "+ getRightAlignData(pPresRdg.trim(),4,55)+ "\n";
				Print3 += "\n  ಹಿಂದಿನ ಮಾಪನ  "+ getRightAlignData(pPrevRdg.trim(),4,52)+ "\n";
				try
				{
					if(pUnits.trim().matches(".*[a-zA-Z]+.*"))
					{
						Print3 += "\n  ಬಳಕೆ  "+ getRightAlignData(pUnits.trim(),1,70)+ "\n";
					}
					else
					{
						Print3 += "\n  ಬಳಕೆ  "+ getRightAlignData(pUnits.trim(),1,72)+ "\n";
					}
				}
				catch(Exception e)
				{
					Print3 += "\n  ಬಳಕೆ  "+ getRightAlignData(pUnits.trim(),1,72)+ "\n";
				}
				//Print3 += "\n  ಬಳಕೆ  "+ getRightAlignData(pUnits.trim(),1,72)+ "\n";
				Print3 += "\n  ಮಾಪಕ ಸ್ಥಿರಾಂಕ  "+ pca.RightAlignInBoxLayoutNew3(pCTRatio.trim()," ",51)+ "\n";
				Print3 += "\n  ಪಿ.ಎಫ್  "+ pca.RightAlignInBoxLayoutNew3(pPF.trim()," ",66)+ "\n";
				Print3 += "\n  " +pca.CreateBlankSpaceSequence(4, " ")+ pca.CreateBlankSpaceSequence(50,"-");

				String Print4= "";
				Print4 +=  "   ನಿಗದಿತ ಶುಲ್ಕ (ಪರಿಮಾಣ,ದರ,ಮೊತ್ತ)" + "\n\n";
				Print4 +=((pFC.size()>0? pca.CreateBlankSpaceSequence(15, " ") +(pFC.get(0))+ "\n\n" : "") +
						(pFC.size()>1? pca.CreateBlankSpaceSequence(15, " ") +(pFC.get(1))+ "\n\n" : "")+
						(pFC.size()>2? pca.CreateBlankSpaceSequence(15, " ") +(pFC.get(2))+ "\n\n" : ""));     //Added 24-06-2021
				Print4 +=  "   ವಿದ್ಯುತ್ ಶುಲ್ಕ (ಪರಿಮಾಣ,ದರ,ಮೊತ್ತ)" + "\n\n";
				Print4 +=((pEC.size()>0?  pca.CreateBlankSpaceSequence(15, " ") +(pEC.get(0))+ "\n\n" : "") +
						(pEC.size()>1?	 pca.CreateBlankSpaceSequence(15, " ") +(pEC.get(1)) + "\n\n": "") +
						(pEC.size()>2?	 pca.CreateBlankSpaceSequence(15, " ") +(pEC.get(2)) + "\n\n": "") +
						(pEC.size()>3?	 pca.CreateBlankSpaceSequence(15, " ") +(pEC.get(3)) + "\n\n": "") +
						(pEC.size()>4?	 pca.CreateBlankSpaceSequence(15, " ") +(pEC.get(4)) + "\n\n": "") +						
						(pEC.size()>5?	 pca.CreateBlankSpaceSequence(15, " ") +(pEC.get(5)) + "\n\n": "") +
						(pEC.size()>6?	 pca.CreateBlankSpaceSequence(15, " ") +(pEC.get(6)) + "\n\n": ""));
				Print4 +=  " ಇಂ.ಹೊ.ಮೊತ್ತ (ಪರಿಮಾಣ,ದರ,ಮೊತ್ತ)" + "\n\n";
				Print4 +=((pFAC.size()>0? pca.CreateBlankSpaceSequence(15, " ") +(pFAC.get(0))+ "\n" : ""));
				Print4 +="\n  "+ pca.CreateBlankSpaceSequence(8, " ")+ pca.CreateBlankSpaceSequence(50,"-")+ "\n\n";

				String Print5=  "";		
				//For OldEC 13-11-2020 To be commented in DEC 2020
				try
				{
					if(SlabColl1.getmOldTecBill().compareTo(new BigDecimal(0.00)) > 0)
					{
						//bptr.sendData(pca.RightAlignInBoxLayout("OLD EC: " + String.valueOf(SlabColl1.getmOldTecBill().setScale(2, BigDecimal.ROUND_HALF_UP))," ").getBytes());
						Print5 += "\n   ವಿದ್ಯುತ್ ಶುಲ್ಕ (OLD EC)"+ pca.CreateBlankSpaceSequence(2, " ") +pca.RightAlignInBoxLayoutNew3(String.valueOf(SlabColl1.getmOldTecBill().setScale(2, BigDecimal.ROUND_HALF_UP))," ",32) + "\n";
					}
					else
					{

					}

				}
				catch(Exception e)
				{

				}
				//End OldEC 13-11-2020

				if(pPFPenalty.trim().length()>15)
				{
					Print5 += "\n  ಪಿ.ಎಫ್.ದಂಡ\n\n"+ pca.CreateBlankSpaceSequence(5, " ") +pca.RightAlignInBoxLayoutNew3(pPFPenalty.trim()," ",60) + "\n"; 
				}
				else
				{
					Print5 += "\n  ಪಿ.ಎಫ್.ದಂಡ  "+ getRightAlignData(pPFPenalty.trim(),4,55)+ "\n";
				}
				if(pPenOnEx.trim().length()>15)
				{
					//Print5 += "\n  ಅಧಿಕ ಪ್ರಮಾಣ ದಂಡ\n\n"+  pca.CreateBlankSpaceSequence(15, " ") +pPenOnEx.trim() + "\n"; 
					Print5 += "\n  ಅಧಿಕ ಪ್ರಮಾಣ ದಂಡ\n\n"+  pca.CreateBlankSpaceSequence(5, " ") +pca.RightAlignInBoxLayoutNew3(pPenOnEx.trim()," ",60) + "\n";
				}
				else
				{
					Print5 += "\n  ಅಧಿಕ ಪ್ರಮಾಣ ದಂಡ  "+ getRightAlignData(pPenOnEx.trim(),4,42)+ "\n";
				}
				Print5 += "\n  ರಿಯಾಯಿತಿ  "+ getRightAlignData(pRebate.trim(),4,60)+ "\n";
				Print5 += "\n  ಬಡ್ಡಿ   "+ getRightAlignData(pInterest.trim(),4,71)+ "\n";

				Print5 += "\n  ಇತರೆ   "+ getRightAlignData(pOthers.trim(),4,69)+ "\n";
				Print5 += "\n  ತೆರಿಗೆ   "+ getRightAlignData(pTaxOnamount.trim(),4,69)+ "\n";


				Print5 += "\n  ಬಿಲ್ ಮೊತ್ತ   "+ getRightAlignData(pBillTotal.trim(),4,57)+ "\n";				
				Print5 += "\n  ಬಾಕಿ  "+ getRightAlignData(pArrears.trim(),4,69)+ "\n";
				Print5 += "\n  ಸರ್ಕಾರದ ಸಾಹಯಧನ  "+ getRightAlignData(pGOkSubsidy.trim(),4,39)+ "\n";
				Print5 +="\n  "+ pca.CreateBlankSpaceSequence(4, " ")+ pca.CreateBlankSpaceSequence(50,"-")+ "\n";

				String Print6 = "";
				Print6 += "   ಪಾವತಿಸಬೇಕಾದ ಮೊತ್ತ"+ pca.RightAlignInBoxLayoutNew3(pNetAmtDue.trim()," ",23)+ "\n";
				Print6 += "\n   ಪಾವತಿಗೆ ಕಡೆ ದಿನಾಂಕ "+ pca.CreateBlankSpaceSequence(13, " ") +  pDueDate.trim()+ "\n\n";

				String Print7 = "";
				//Added 07-05-2021
				try
				{
					String pReceiptDate = SlabColl1.getmReceiptDate()==null?" ":SlabColl1.getmReceiptDate().trim();
					if(pReceiptDate.trim().length()<=8)
						pReceiptDate = cFun.DateConvertAddChar(SlabColl1.getmReceiptDate().trim(), "-");

					Print7 += "\n  ಹಿಂದಿನ ರಸೀದಿ  ದಿನಾಂಕ   "+ pca.RightAlignInBoxLayoutNew3(pReceiptDate," ",32)+ "\n";


				}
				catch(Exception e)
				{

				}
				Print7 += "\n "+ pTotalRemarks.trim()+ "\n\n";

				byte imgData1[] = pos.multiLingualPrint(Print1,25,typeface1,-40); //Last MRCode
				byte imgData2[] = pos.multiLingualPrint(Print2,28,typeface1,-5); //Last AccId
				byte imgData3[] = pos.multiLingualPrint(Print3,25,typeface1,-50);//PF
				byte imgData4[] = pos.multiLingualPrint(Print4,22,typeface1,-75); //FAC
				byte imgData5[] = pos.multiLingualPrint(Print5,25,typeface1,-80); //Last GOK
				byte imgData6[] = pos.multiLingualPrint(Print6,28,typeface1,-20); //Last DueDate
				byte imgData7[] = pos.multiLingualPrint(Print7,24,typeface1,-5); //Last Remarks



				bptr.sendData(imgData1); 							
				bptr.sendData(imgData2);				
				bptr.sendData(imgData3);				
				bptr.sendData(imgData4);				
				bptr.sendData(imgData5);				
				bptr.sendData(imgData6);				
				if(pTotalRemarks.trim().length()>0)
				{
					bptr.sendData(imgData7);
				}
				try
				{
					String barValue = pca.ConnectionNoTrim(SlabColl1.getmConnectionNo().trim()) + SlabColl1.getmBillTotal().setScale(0, BigDecimal.ROUND_HALF_EVEN);


					long addtimebarcode = Calendar.getInstance().getTimeInMillis() + 2000;//1 second delay
					while(addtimebarcode > Calendar.getInstance().getTimeInMillis())
					{										

					}
					byte barCodeData[] = pos.POS_S_SetBarcode(barValue.trim(), 0, 73, 2,96, 0, 2);
					bptr.sendData(barCodeData);
				}
				catch(Exception e)
				{
					Log.d("", "");
				}
				bptr.sendData(ConstantClass.linefeed1);
				bptr.sendData(ConstantClass.linefeed1);
				bptr.sendData(ConstantClass.linefeed1);
				try
				{
					db.EventLogSave("4", LoginActivity.IMEINumber, LoginActivity.SimNumber, "Manual Bill Printing Completed For Connection:  " + SlabColl1.getmConnectionNo().trim() + " BILLAMT:"+ String.valueOf(SlabColl1.getmBillTotal().setScale(2, BigDecimal.ROUND_HALF_UP)));//15-07-2016
				}
				catch(Exception e)
				{

				}
				//30-12-2016
				/*try
				{
					ArrayList<String> alStrColeventlog=db.getEventLogNotSent();
					StringBuilder sb = new StringBuilder();
					for(String str :alStrColeventlog)
					{
						sb.append(str);					
					}
					EncryptDecrypt en = new EncryptDecrypt("nsoft987");										
					en.encryptLogData(sb.toString());
				}
				catch(Exception e)
				{

				}*/
			} 
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				Log.d(TAG,e.toString());				
				dh.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						CustomToast.makeText(BillingConsumption_LandT.this, "Problem in printing data. Please check Bluetooth device connected or not", Toast.LENGTH_SHORT);
					}
				});
			}
			finally
			{
				try 
				{
					if(BluetoothPrinting.isConnected)
						Thread.sleep(5000);
				}
				catch (Exception e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				dh.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try
						{
							Intent i = new Intent(BillingConsumption_LandT.this, Billing_LandT.class);
							startActivity(i);
							finish();
						}
						catch (Exception e) 
						{
							Log.d(TAG,e.toString());							
						}
					}
				});
				try 
				{
					bptr.closeBT();
				} 
				catch (Exception e) 
				{
					Log.d(TAG,e.toString());
					e.printStackTrace();
				}				
			}
		}
		public void cancel() 
		{

		}
	}//END ConnectedThread
	//END Bluetooth Code====================================================================================
	public class myDialog extends ProgressDialog
	{
		public myDialog(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			return;
		}
	}
	public String  getRightAlignData(String inputData,int base,int align)
	{
		PrinterCharacterAlign pca = new PrinterCharacterAlign();
		if(inputData.trim().length()<=base)
			return pca.RightAlignInBoxLayoutNew3(inputData," ",align);
		else
		{
			int dif = inputData.trim().length() - base;
			return pca.RightAlignInBoxLayoutNew3(inputData," ",align-(dif));
		}
	}

}


