
//Nitish 24-02-2014
package in.nsoft.hescomspotbilling;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
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
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
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

public class BillingConsumption extends Activity implements AlertInterface{

	Context	mcx; 
	private ArrayList<DDLItem> alConStatusItem;
	private Spinner ddlBillConsumStatus;
	private TextView lblRrNo, lblPrvStatus, lblConsumption, txtBillConsumBillAmount, lblBillConsumptionMtd;	

	//Nitish Modified 24-11-2014 For Alert Dialog
	TextView tvDisplayData,tvInflateMtrType,tvInflateBatteryStatus,tvInflateMeterPlacement,tvInflateMeterCondition,tvInflateContactType,tvInflateContactNo,tvInflateMtrPhase;	
	Spinner ddlInflatorMeterType,ddlInflatorBatteryStatus,ddlInflatorMeterPlacement,ddlInflatorMeterCondition,ddlInflatorContactType,ddlInflatorMeterPhase;
	EditText txtInflatorContactNo,txtRemark; //30-08-2016
	ArrayList<String> alStdCodes;
	boolean stdvalid = false;

	AlertDialog.Builder alert;

	private EditText edtBillReading, edtPowerFactor, edtMD;
	private Button btnProcess, btnSave;
	private ConStatusTwoDimArray ConArray;
	Handler mHandler ;
	private ReadSlabNTarifSbmBillCollection SlabColl;
	DatabaseHelper db = new DatabaseHelper(this);
	private static final String TAG = BillingConsumption.class.getName();
	BigDecimal bgPreReading, bgPrvReading, bgUsed, bgConsumption;
	Checkbox cbCalc;

	//Nitish  30-08-2014 	
	//Spinner ddlBillConsumReason;//ddlInflatorMeterType,ddlInflatorBatteryStatus,ddlInflatorMeterLocation,ddlInflatorMeterCondition;
	DDLAdapter meterTypeAdapter,batteryStatusAdapter,meterPlacementAdapter,meterConditionAdapter,contactTypeAdapter,meterPhaseAdapter; //29-03-2016
	int mReason;

	//Nitish  29-03-2016
	Spinner ddlMainMtrType;
	DDLAdapter mainmtrtypeadapter;

	//Bluethooth variables===============================================
	int i = 0;
	Handler dh ;
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
	//private boolean isOk = false;

	//Nitish 11-09-2015
	static String IMEINumber = "";
	static String SimNumber = "";		

	static String saveRemark; //30-08-2016

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SlabColl = BillingObject.GetBillingObject();//Get Billing Object
		setContentView(R.layout.activity_billing_consumption_powerfactor);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);			

		ddlBillConsumStatus  = (Spinner)findViewById(R.id.ddlBillConsumStatus);
		lblRrNo = (TextView)findViewById(R.id.txtBillConsumRRNo);
		lblPrvStatus = (TextView)findViewById(R.id.txtBillConsumPrevReason);
		lblConsumption = (TextView)findViewById(R.id.txtBillConsumConsumption); 
		edtBillReading = (EditText)findViewById(R.id.txtBillConsumStatusReading);
		edtPowerFactor = (EditText)findViewById(R.id.txtBillConsumPowerFactor);
		edtMD = (EditText)findViewById(R.id.txtBillConsumMD);
		btnProcess = (Button)findViewById(R.id.btnBillConsumProcess);
		btnSave = (Button)findViewById(R.id.btnBillConsumSavePrint);
		txtBillConsumBillAmount = (TextView)findViewById(R.id.txtBillConsumBillAmount);
		lblBillConsumptionMtd = (TextView)findViewById(R.id.lblBillConsumptionMtd);

		//11-09-2015
		//Get Mobile Details ==============================================================
		/*TelephonyManager mgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		IMEINumber = mgr.getDeviceId();
		SimNumber = mgr.getSimSerialNumber();*/

		try
		{
			//Get Mobile Details ==============================================================
			//10-03-2021
			IMEINumber = CommonFunction.getDeviceNo(BillingConsumption.this);
			SimNumber = CommonFunction.getSIMNo(BillingConsumption.this);
			SlabColl.setmSBMNumber(IMEINumber);

		}
		catch(Exception e)
		{

		}



		//END Get Mobile Details ==========================================================


		ddlMainMtrType = (Spinner)findViewById(R.id.ddlMainMtrType);
		try {
			//Modified Nitish  04-10-2017		
			mainmtrtypeadapter = db.getMainMeterType(0);
			mainmtrtypeadapter = db.getMainMeterType(BillingObject.GetBillingObject().getmSupply_Points());
			//End 04-10-2017
			//mainmtrtypeadapter = db.getMainMeterType();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		ddlMainMtrType.setAdapter(mainmtrtypeadapter);



		mHandler = new Handler();
		dh = new Handler();
		eol = System.getProperty("line.separator");


		//Show Message for Latitude_image and Longitude_image=========================================
		if(LoginActivity.gpsTracker.canGetLocation())
		{
			BillingObject.GetBillingObject().setmGps_Latitude_image(String.valueOf(LoginActivity.gpsTracker.latitude));
			BillingObject.GetBillingObject().setmGps_Longitude_image(String.valueOf(LoginActivity.gpsTracker.longitude));
		}

		/*CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
		params.setContext(BillingConsumption.this);					
		params.setMainHandler(mHandler);
		params.setMsg("Latitude : "+SlabColl.getmGps_Latitude_image()+"\r\n"+"Longitude : "+SlabColl.getmGps_Longitude_image());
		params.setButtonOkText("OK");
		params.setTitle("Image");
		params.setFunctionality(-1);
		CustomAlert cAlert12  = new CustomAlert(params);*/
		//END Show Message for Latitude_image and Longitude_image=====================================
		//Tamilselvan on 20-03-2014
		Log.d("Before", SlabColl.getmMtd());
		if(SlabColl.getmMtd().equals("1"))
		{
			lblBillConsumptionMtd.setText("Metered");
			ConArray = new ConStatusTwoDimArray();	
			btnProcess.setText(ConstantClass.strProcessText);
			btnSave.setVisibility(Button.INVISIBLE);				
			edtPowerFactor.setEnabled(false);
			edtMD.setEnabled(false);
			LoadInitialDetails();//Loading Initial Data from Billing Object and Bind Dropdownlist
		}
		else//Tamilselvan on 20-03-2014
		{
			lblBillConsumptionMtd.setText("Not Metered");	
			//ddlBillConsumStatus.setEnabled(false);  //02-02-2016
			edtBillReading.setEnabled(false);
			edtPowerFactor.setEnabled(false); 
			edtMD.setEnabled(false);
			forUnmetered(); //02-02-2016


		}
		Log.d("After", SlabColl.getmMtd());
		btnSave.setVisibility(Button.INVISIBLE);

		//Nitish  29-03-2016
		ddlMainMtrType.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				//Commented 31-05-2016
				/*if(mainmtrtypeadapter.GetId(arg2).toString().equals("1")) //For Mechanical Meter Disable MD and PF
				{
					edtMD.setEnabled(false);
					edtPowerFactor.setEnabled(false); 
				}
				else
				{
					edtMD.setEnabled(true);
					edtPowerFactor.setEnabled(true); 
				}*/
				//31-05-2016
				edtMD.setEnabled(true);
				//edtPowerFactor.setEnabled(true); 
				//End 31-05-2016
				edtPowerFactor.setText("");
				edtMD.setText("");				
				SlabColl.setmMeter_type(mainmtrtypeadapter.GetId(arg2));
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		//Listener for Selecting Status 
		ddlBillConsumStatus.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				//Added Condition 02-02-2016
				if(SlabColl.getmMtd().equals("1"))
				{
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
									}
									else if(FRStatus[q][r] == 1)//if 1 allow to enter reading in the textbox
									{
										edtBillReading.setEnabled(true);									
									}
									break;
								}
							}//END Column For Loop 
						}
						//break;
					}//END For Loop for Rows				
					edtBillReading.setText("");
					edtPowerFactor.setText("");
					edtMD.setText("");
					lblConsumption.setText("");
					txtBillConsumBillAmount.setText("");
				}
				//29-03-2016
				try
				{
					//For DL=1,VA = 6,NT =15 Remove dropdowns
					if(BillingObject.GetBillingObject().getmPreStatus().equals("1") || BillingObject.GetBillingObject().getmPreStatus().equals("6") || BillingObject.GetBillingObject().getmPreStatus().equals("15"))
					{
						ddlMainMtrType.setEnabled(false);						
						SlabColl.setmMeter_type("0");
					}
					else
					{
						ddlMainMtrType.setEnabled(true);
					}
				}
				catch(Exception e)
				{

				}
				//End 29-03-2016
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		//Commented 30-07-2019
		/*edtBillReading.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				try
				{
					if(!arg1)//false for OUT Focus 
					{
						//06-04-2017
						//if(isValid((DDLItem)alConStatusItem.get(Integer.valueOf(((DDLItem)ddlBillConsumStatus.getSelectedItem()).getId()))))//Validation 
						//Uncommented 29-08-2017
						if(isValid((DDLItem)alConStatusItem.get(ddlBillConsumStatus.getSelectedItemPosition())))//Validation 
						{
							Calculate clt =new Calculate(mcx);
							clt.consumption();//1. CONSUMPTION Calculation
							PForMDRequired(); //31-08-2016
						}
					}
				}
				catch(Exception e)
				{
					Log.d(TAG, e.toString()); 
				}
			}
		});*/

		btnProcess.setOnClickListener(new OnClickListener() {  
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub		
				try
				{
					if(btnProcess.getText().equals(ConstantClass.strProcessText))
					{		

						//Modified 16-05-2017
						//int a = ddlBillConsumStatus.getSelectedItemPosition();
						//Commented 29-08-2017
						/*if(!SlabColl.getmMCHFlag().equals("1"))
						{
							if(Integer.valueOf(SlabColl.getmStatus())==2 || Integer.valueOf(SlabColl.getmStatus())==9 || Integer.valueOf(SlabColl.getmStatus())==10 || Integer.valueOf(SlabColl.getmStatus())==14)
							{
								CustomToast.makeText(BillingConsumption.this, "This Connection Cannot be Billed.", Toast.LENGTH_SHORT);
								return;
							}	
						}*/
						//int a =Integer.valueOf(((DDLItem)ddlBillConsumStatus.getSelectedItem()).getId());
						int a = ddlBillConsumStatus.getSelectedItemPosition();
						DDLItem k = (DDLItem)alConStatusItem.get(a);
						//End Commented 29-08-2017
						//DDLItem k = (DDLItem)alConStatusItem.get(a);
						if(isValid(k))//Validation  
						{
							//29-08-2017 Make Meter type Selection mandatory
							if(BillingObject.GetBillingObject().getmPreStatus().equals("1") || BillingObject.GetBillingObject().getmPreStatus().equals("6") || BillingObject.GetBillingObject().getmPreStatus().equals("15"))
							{
								ddlMainMtrType.setEnabled(false);						
								SlabColl.setmMeter_type("0");
							}
							else
							{
								String str = ddlMainMtrType.getSelectedItem().toString();
								if(str.equals("--SELECT--"))
								{
									CustomToast.makeText(BillingConsumption.this, "Select Meter Type", Toast.LENGTH_SHORT);								
									return;
								}
							}
							//End 29-08-2017
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
											CustomToast.makeText(BillingConsumption.this, "Abnormal consumption recorded.", Toast.LENGTH_SHORT);
											SlabColl.setmAbFlag("1");
										}/**/
									}
								}
							}
							catch(Exception e)
							{
								Log.d(TAG, e.toString()); 
							}
							//30-08-2016
							if(edtPowerFactor.isEnabled())
							{
								if((edtPowerFactor.getText().toString().trim().length()==0) && BillingObject.GetBillingObject().getmEcsFlag().equals("4"))
								{
									CustomToast.makeText(BillingConsumption.this, "Enter PF.", Toast.LENGTH_SHORT);
									return;
								}								
								try
								{
									String enteredPF = edtPowerFactor.getText().toString().trim();
									if(enteredPF.startsWith("."))
									{										
										if(enteredPF.length()>=4)
										{
											CustomToast.makeText(BillingConsumption.this, "PF Value must not be greater than 2 digits after Decimal Point", Toast.LENGTH_SHORT);
											return;
										}
									}
								}
								catch(Exception e)
								{
									Log.d(TAG, e.toString()); 
								}

							}
							//30-08-2016
							try
							{
								SlabColl.setMmEC_1(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
								SlabColl.setMmEC_2(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
								SlabColl.setMmEC_3(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
								SlabColl.setMmEC_4(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
								//21-02-2018
								if(SlabColl.getmTariffCode()>=1 &&  SlabColl.getmTariffCode() <= 4)
									SlabColl.setmTEc(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)); 
								//End 21-02-2018
							}
							catch(Exception e)
							{

							}
							//END CONSUMPTION Calculation================================================

							//FC Calculation=============================================================
							clt.fc();
							//END FC Calculation=========================================================

							//EC Calculation=============================================================
							cbCalc = new Checkbox(BillingConsumption.this);//Added by Tamilselvan on 17-03-2014
							cbCalc.EC_Calculation();//Added by Tamilselvan on 17-03-2014
							//END EC Calculation=========================================================

							//PF Calculation=============================================================
							//Nitish Modified 29-03-2016 for PF (if PF is not entered Take PF from file else take entered pf)
							/*BigDecimal pfValue = new BigDecimal(edtPowerFactor.getText().toString().length() == 0 ? "0.00" : edtPowerFactor.getText().toString());
							cbCalc.PF_Calculation(pfValue);
							if(edtPowerFactor.isEnabled())
							{
								SlabColl.setmPF(edtPowerFactor.getText().toString());
							}*/

							BigDecimal pfValue = new BigDecimal(edtPowerFactor.getText().toString().length() == 0 ?SlabColl.getmPF() : edtPowerFactor.getText().toString());
							cbCalc.PF_Calculation(pfValue);
							//END PF Calculation=========================================================

							//MD Calculation=============================================================							
							BigDecimal mdValue = new BigDecimal(edtMD.getText().toString().length() == 0 ? "0.00" : edtMD.getText().toString());
							cbCalc.MD_Calculation(mdValue);
							//End 29-03-2016
							//END MD Calculation=========================================================

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
							cbCalc = new Checkbox(BillingConsumption.this);
							cbCalc.TotBillCal();
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

							ddlMainMtrType.setEnabled(false); //29-03-2016
						}
					}

					else if (btnProcess.getText().equals(ConstantClass.strResetText))
					{
						//20-08-2015 Obtain Meter Details
						try
						{
							/*int mMtrDisFlag= BillingObject.GetBillingObject().getmMtrDisFlag();
							int mMeter_Present_Flag= BillingObject.GetBillingObject().getmMeter_Present_Flag();
							String mMeter_type= BillingObject.GetBillingObject().getmMeter_type();
							String mMobileNo= BillingObject.GetBillingObject().getmMobileNo();
							String mMeter_serialno= BillingObject.GetBillingObject().getmMeter_serialno();
							int mMtrTerminalCoverFlag= BillingObject.GetBillingObject().getmMtrTerminalCoverFlag();
							int mMtr_Not_Visible= BillingObject.GetBillingObject().getmMtr_Not_Visible();
							int mMtrBodySealFlag= BillingObject.GetBillingObject().getmMtrBodySealFlag();
							int mMtrTerminalCoverSealedFlag= BillingObject.GetBillingObject().getmMtrTerminalCoverSealedFlag();
							int mMeterMake= BillingObject.GetBillingObject().getmMtrMakeFlag();
							String mAadharNo= BillingObject.GetBillingObject().getmAadharNo();
							String mVoterIdNo= BillingObject.GetBillingObject().getmVoterIdNo();


							//Reset Object
							BillingObject.reset(BillingConsumption.this);	

							//20-08-2015 Assign Meter Details to Object 					
							BillingObject.GetBillingObject().setmMtrDisFlag(mMtrDisFlag);
							BillingObject.GetBillingObject().setmMeter_Present_Flag(mMeter_Present_Flag);
							BillingObject.GetBillingObject().setmMeter_type(mMeter_type);             
							BillingObject.GetBillingObject().setmMobileNo(mMobileNo); 					
							BillingObject.GetBillingObject().setmMeter_serialno(mMeter_serialno); 
							BillingObject.GetBillingObject().setmMtrTerminalCoverFlag(mMtrTerminalCoverFlag);	
							BillingObject.GetBillingObject().setmMtr_Not_Visible(mMtr_Not_Visible);
							BillingObject.GetBillingObject().setmMtrBodySealFlag(mMtrBodySealFlag);	
							BillingObject.GetBillingObject().setmMtrTerminalCoverSealedFlag(mMtrTerminalCoverSealedFlag);	
							BillingObject.GetBillingObject().setmVoterIdNo(mVoterIdNo);   
							BillingObject.GetBillingObject().setmAadharNo(mAadharNo);
							BillingObject.GetBillingObject().setmMtrMakeFlag(mMeterMake);*/


							//Added 31-10-2015
							String mAadharNo= BillingObject.GetBillingObject().getmAadharNo();
							String mVoterIdNo= BillingObject.GetBillingObject().getmVoterIdNo();
							String mMobileNo= BillingObject.GetBillingObject().getmMobileNo();
							String mRationCard= BillingObject.GetBillingObject().getmRemarks(); //06-07-2020

							BillingObject.reset(BillingConsumption.this);	

							BillingObject.GetBillingObject().setmAadharNo(mAadharNo);
							BillingObject.GetBillingObject().setmVoterIdNo(mVoterIdNo); 
							BillingObject.GetBillingObject().setmMobileNo(mMobileNo); 	
							BillingObject.GetBillingObject().setmRemarks(mRationCard); 	 //06-07-2020

						}
						catch(Exception e)
						{
							CustomToast.makeText(BillingConsumption.this, "RESET ERROR FOR METER DETAILS", Toast.LENGTH_SHORT);	
							Intent i = new Intent(BillingConsumption.this, Billing.class);
							startActivity(i);
							finish();//08-05-2021
							Log.d(TAG, e.toString()); 
						}


						//BillingObject.reset(BillingConsumption.this);
						BillingObject.GetBillingObject().setmPreStatus(String.valueOf(ddlBillConsumStatus.getId()));						

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

						ddlMainMtrType.setAdapter(mainmtrtypeadapter); //29-03-2016
						ddlMainMtrType.setEnabled(true);//29-03-2016
						LoadInitialDetails();//Load dropdownlist 
					}

					//WriteLog.WriteLogError(TAG+" Process btn finished");
				}
				catch(Exception e)
				{
					Log.d(TAG, e.toString()); 
				}
			}
		});		
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				//Modified 27-07-2016

				/*if(!db.GetBillingDetails().getmSIMNo().trim().equals(SimNumber))
					{
						CustomToast.makeText(BillingConsumption.this, "Sim Number Not Matching Contact NSOFT.", Toast.LENGTH_SHORT);
						return;
					}	
					String CurDate = new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime());
					String sTime = ((new CommonFunction()).TimeConvertAddChar(db.GetBillingDetails().getmStartTime(),":"));
					String eTime = ((new CommonFunction()).TimeConvertAddChar(db.GetBillingDetails().getmEndTime(),":"));
					String currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());	
					if(CurDate.equals(db.GetBillingDetails().getmBatch_Date()))  
					{
						//if(sTime.compareTo(currentTime) > 0) //Start Time Not Reached
						//{
						//	CustomToast.makeText(BillingConsumption.this,"Billing Start Time " + sTime + " not Reached." , Toast.LENGTH_SHORT);
						//	return;
						//}
						//if(currentTime.compareTo(eTime) > 0) //End Time Exceeded
						//{
						//	CustomToast.makeText(BillingConsumption.this,"Billing End Time " + eTime + " Exceeded."  , Toast.LENGTH_SHORT);
						//	return;
						//}
						SaveFunction();	
					}
					else
					{
						CustomToast.makeText(BillingConsumption.this,"Billing Closed for Today." , Toast.LENGTH_SHORT);
					}*/	
				//30-08-2016
				int mCount = 0;
				try
				{					
					BigDecimal EC1 = BillingObject.GetBillingObject().getMmEC_1().setScale(2, RoundingMode.HALF_UP);
					BigDecimal EC2 = BillingObject.GetBillingObject().getMmEC_2().setScale(2, RoundingMode.HALF_UP);
					BigDecimal EC3 = BillingObject.GetBillingObject().getMmEC_3().setScale(2, RoundingMode.HALF_UP);
					BigDecimal EC4 = BillingObject.GetBillingObject().getMmEC_4().setScale(2, RoundingMode.HALF_UP);

					BigDecimal ECTotal = EC1.add(EC2).add(EC3).add(EC4);
					//BigDecimal BillEC = new BigDecimal(100742.30).setScale(2,  RoundingMode.HALF_UP);//
					BigDecimal BillEC = (BillingObject.GetBillingObject().getmTEc()).setScale(2, RoundingMode.HALF_UP);
					if(BillEC.compareTo(ECTotal)!=0)
					{
						for(int i=2;i<=10;i++)
						{
							if(BillEC.compareTo(ECTotal.multiply(new BigDecimal(i)))==0)
							{
								CustomToast.makeText(BillingConsumption.this, "Integrity Error Please Bill Again.", Toast.LENGTH_SHORT);
								try
								{
									db.EventLogSave("15", LoginActivity.IMEINumber, LoginActivity.SimNumber, "Data Integrity Error for ConnectionNo:" + BillingObject.GetBillingObject().getmConnectionNo() );//15-07-2016
								}
								catch(Exception e)
								{

								}
								mCount=1;
								Intent billingPage = new Intent(BillingConsumption.this, Billing.class);
								startActivity(billingPage);
								finish();//08-05-2021
							}
							else
							{
								mCount=0;
							}
						}

					}
				}
				catch(Exception e)
				{

				}
				//End 30-08-2016
				//Modified 29-01-2021
				try
				{

					if(BillingObject.GetBillingObject().getmMtd().trim().equals("1"))
					{
						if(BillingObject.GetBillingObject().getmPreStatus().trim().equals("0") ||  BillingObject.GetBillingObject().getmPreStatus().trim().equals("4"))
						{
							if(BillingObject.GetBillingObject().getmPreRead().trim().equals(""))
							{
								CustomToast.makeText(BillingConsumption.this,"Error in Final Reading.Please Bill Again"  , Toast.LENGTH_SHORT);
								Intent billingPage = new Intent(BillingConsumption.this, Billing.class);
								startActivity(billingPage);
								finish();
								return;
							}
							else if(new BigDecimal(BillingObject.GetBillingObject().getmPreRead().trim()).compareTo(new BigDecimal(0.00))<=0)
							{
								CustomToast.makeText(BillingConsumption.this,"Error in Final Reading.Please Bill Again"  , Toast.LENGTH_SHORT);
								Intent billingPage = new Intent(BillingConsumption.this, Billing.class);
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
				if(mCount==0)
				{
					try
					{
						SaveFunction();	
					}
					catch(Exception e)
					{

					}
				}

				// TODO Auto-generated method stub							
			}
		});
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
		alert = new AlertDialog.Builder(BillingConsumption.this);
		alert.setTitle(alerttitle);
		View convertView = BillingConsumption.this.getLayoutInflater().inflate(R.layout.billingdialoginflator, null);	
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
				Intent i = new Intent(BillingConsumption.this, Billing.class);
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
						CustomToast.makeText(BillingConsumption.this, "Select Meter Phase", Toast.LENGTH_SHORT);						
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
						CustomToast.makeText(BillingConsumption.this, "Select Meter location", Toast.LENGTH_SHORT);						
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
						CustomToast.makeText(BillingConsumption.this, "Select Meter Condition", Toast.LENGTH_SHORT);						
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
						CustomToast.makeText(BillingConsumption.this, "Select Contact Type", Toast.LENGTH_SHORT);						
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
							CustomToast.makeText(BillingConsumption.this, "Recorded Demand cannot be greater than 8kW for Single Phase.", Toast.LENGTH_LONG);	
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
				try
				{
					//24-07-2017


					String imgName = ImageSaveinSDCard();
					BillingObject.GetBillingObject().setmImage_Name(imgName);


				}
				catch(Exception e) 
				{

				}
				//BillingObject.GetBillingObject().setmImage_Name(imgName);
				new Thread(new Runnable() {

					@Override
					public void run() 
					{
						BillingObject.GetBillingObject().setmSBMNumber(IMEINumber);//29-07-2016
						//result = db.BillSave(); 
						//Modified 10-09-2020
						boolean isFRCorrect = true;

						try
						{

							if(BillingObject.GetBillingObject().getmPreRead().trim().equals("") && BillingObject.GetBillingObject().getmPreStatus().trim().equals("0"))
							{
								BigDecimal units = new BigDecimal(BillingObject.GetBillingObject().getmUnits().trim());
								if(units.compareTo(new BigDecimal(1.00)) > 0)
								{
									//isFRCorrect=false;
								}								
							}

						}
						catch(Exception e)
						{

						}
						if(isFRCorrect)
							result = db.BillSave(); 
						else
							result=0;

						//End Modified 10-09-2020
						dh.post(new Runnable()
						{
							@Override
							public void run()
							{
								if(result == 1)
								{
									CustomToast.makeText(BillingConsumption.this, "Bill created successfully.", Toast.LENGTH_SHORT);
									//If Present Reading is 15(NT) Print Should Not Come
									if(!BillingObject.GetBillingObject().getmPreStatus().equals("15"))
									{
										//Bluetooth Code===========================================				
										bptr = new BluetoothPrinting(BillingConsumption.this);
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
											CustomToast.makeText(BillingConsumption.this, "Problem in printing data. Please check Bluetooth device connected or not", Toast.LENGTH_SHORT);
											Intent i = new Intent(BillingConsumption.this, Billing.class);
											startActivity(i);
											finish();
										}
										//END Bluetooth Code=======================================
									}
									else//For reason(15 - Not Trace No Print )
									{
										Intent i = new Intent(BillingConsumption.this, Billing.class);
										startActivity(i);
										finish();
									}
								}
								else//Tamilselvan on 20-03-2014 
								{
									CustomToast.makeText(BillingConsumption.this, "Problem in saving data.", Toast.LENGTH_SHORT);
									Intent i = new Intent(BillingConsumption.this, Billing.class);
									startActivity(i);
								}
							}
						});
					}
				}).start();

				ringProgress = ProgressDialog.show(BillingConsumption.this, "Please wait..", "Data Saving and Printing..",true);//Spinner
				ringProgress.setCancelable(false);

			}
		});		
		alert.setCancelable(false);
		alert.show();
		//WriteLog.WriteLogError(TAG+" Save dialog opened.");
		/*CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
		params.setContext(BillingConsumption.this);					
		params.setMainHandler(dh);
		params.setMsg(sb.toString());
		params.setButtonOkText(btnAlertExit);
		params.setButtonCancelText(btnAlertPrint);
		params.setTitle(alerttitle);
		params.setFunctionality(Print);
		cAlert  = new CustomAlert(params);*/
		//END Pop up Window Added by Tamilselvan on 24-03-2014===============================================
		btnSave.setVisibility(Button.INVISIBLE);
		btnProcess.setVisibility(Button.INVISIBLE);
	}
	//Validation for Process
	public boolean isValid(DDLItem k)
	{
		boolean isValidCr = true;
		//Nitish 30-07-2019
		if(!SlabColl.getmPreStatus().equals("5"))//For RNF/No Display
		{
			SlabColl.setmPreRead("");
		}
		if(edtBillReading.isEnabled())//Validation For edtBillReading TextBox
		{
			if(edtBillReading.getText().toString().equals(""))
			{
				CustomToast.makeText(BillingConsumption.this, "Reading field should not be empty.", Toast.LENGTH_SHORT);
				isValidCr = false;
			}
			else
			{
				//Nitish 23-02-2017
				bgPreReading = new BigDecimal(edtBillReading.getText().toString()).setScale(2, RoundingMode.HALF_UP);//Present Reading
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
						CustomToast.makeText(BillingConsumption.this, "Present reading should not less than previous reading.", Toast.LENGTH_SHORT);
						edtBillReading.setText(""); 
						lblConsumption.setText("");
						isValidCr = false;
					}
				}				
			}
		}//END Validation For edtBillReading TextBox

		//Validation For edtPowerFactor TextBox====================================================

		try
		{
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
						//Commented 31-05-2016
						/*if(new BigDecimal(edtPowerFactor.getText().toString()).compareTo(new BigDecimal(1)) == 1)
						{
							edtPowerFactor.setText("");
							CustomToast.makeText(BillingConsumption.this, "Power factor field should not be greater than 1. ", Toast.LENGTH_SHORT);
							isValidCr = false;
						}*/
						//Modified 31-05-2016
						if(edtPowerFactor.getText().toString().trim().length()>0)	
						{
							if(new BigDecimal(edtPowerFactor.getText().toString()).compareTo(new BigDecimal(1)) == 1)
							{
								edtPowerFactor.setText("");
								CustomToast.makeText(BillingConsumption.this, "Power factor field should not be greater than 1. ", Toast.LENGTH_SHORT);
								isValidCr = false;
							}
							else if(new BigDecimal(edtPowerFactor.getText().toString()).compareTo(new BigDecimal(0.00)) == 0)
							{
								edtPowerFactor.setText("");
								CustomToast.makeText(BillingConsumption.this, "Power factor field should not be zero. ", Toast.LENGTH_SHORT);
								isValidCr = false;
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			isValidCr = false;
		}
		//END Validation For edtPowerFactor TextBox================================================

		//Validation For edtMD TextBox=============================================================
		try
		{
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
							CustomToast.makeText(BillingConsumption.this, "MD field should not be empty for LT-6 Consumers.", Toast.LENGTH_SHORT);
							isValidCr = false;
						}
					}
					//Commented 31-05-2016
					/*if(new BigDecimal(edtMD.getText().toString()).compareTo(new BigDecimal(999)) == 1)
					{
						edtMD.setText("");
						CustomToast.makeText(BillingConsumption.this, "MD field should not be greater than 999. ", Toast.LENGTH_SHORT);
						isValidCr = false;
					}

					else if(edtMD.getText().toString().contains("."))
					{
						String editMDCheck = edtMD.getText().toString().trim().split("\\.")[1];
						if(editMDCheck.length() > 4)
						{
							edtMD.setText("");
							CustomToast.makeText(BillingConsumption.this, "MD field should not contain Decimal digits greater than 4. ", Toast.LENGTH_SHORT);
							isValidCr = false;
						}
					}*/
					//Modified 31-05-2016
					if(edtMD.getText().toString().trim().length()>0)	
					{
						if(new BigDecimal(edtMD.getText().toString()).compareTo(new BigDecimal(999)) == 1)
						{
							edtMD.setText("");
							CustomToast.makeText(BillingConsumption.this, "MD field should not be greater than 999. ", Toast.LENGTH_SHORT);
							isValidCr = false;
						}
						else if(new BigDecimal(edtMD.getText().toString()).compareTo(new BigDecimal(0.00)) == 0)
						{
							edtMD.setText("");
							CustomToast.makeText(BillingConsumption.this, "MD field should not be zero. ", Toast.LENGTH_SHORT);
							isValidCr = false;
						}
						else if(edtMD.getText().toString().contains("."))
						{
							String editMDCheck = edtMD.getText().toString().trim().split("\\.")[1];
							if(editMDCheck.length() > 4)
							{
								edtMD.setText("");
								CustomToast.makeText(BillingConsumption.this, "MD field should not contain Decimal digits greater than 4. ", Toast.LENGTH_SHORT);
								isValidCr = false;
							}
						}
					}
					//End 29-03-2016

				}
			}//END Validation For edtMD TextBox=========================================================
		}
		catch(Exception e)
		{
			isValidCr = false;
		}
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
			cbCalc = new Checkbox(BillingConsumption.this);
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
			ReadBillingConsumptionStatus ConStatus = new ReadBillingConsumptionStatus(BillingConsumption.this);
			alConStatusItem = ConStatus.ReadStatus();
			DDLItem dNavnew;		
			ArrayList<DDLItem> alItems = new ArrayList<DDLItem>();
			lblRrNo.setText(SlabColl.getmRRNo());//get RR No
			lblPrvStatus.setText(GetPrvStatusName(Integer.valueOf(SlabColl.getmStatus())));//get Previous Status 
			//db.MovetoNextConnectionNo(String.valueOf(SlabColl.getMmUID()));
			if(lblPrvStatus.getText().equals(""))
			{
				CustomToast.makeText(BillingConsumption.this, "Please check this connection previous status.", Toast.LENGTH_SHORT);
				//return;
			}
			//Loading Reason or Status in Dropdownlist============================================================
			for(int z = 1; z < ConStatusArray.length; z++)//Row For Loop
			{
				if(ConStatusArray[z][0] == Integer.valueOf(SlabColl.getmStatus()))//Check Previous Status Id ---- 
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
							//29-09-2016 For New Connection if DayWiseFlag=Y then allow Normal(0),MNR(2),MB(14)
							else if(SlabColl.getmDayWise_Flag().equals("Y"))
							{
								if(ConStatusArray[0][y] != 4)//Check 4(MCH)----> tamilselvan on 20-03-2014
								{
									int index = GetIndex(ConStatusArray[0][y]);//Get the Index of the Status Id
									if(alConStatusItem.get(index).getId().equals("0") || alConStatusItem.get(index).getId().equals("2") || alConStatusItem.get(index).getId().equals("6")|| alConStatusItem.get(index).getId().equals("9") || alConStatusItem.get(index).getId().equals("10") ||  alConStatusItem.get(index).getId().equals("14"))
									{
										dNavnew = new DDLItem(alConStatusItem.get(index).getId(), alConStatusItem.get(index).getValue());
										alItems.add(dNavnew);//add all Items which are 1
									}
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
			//Added Nitish 06-04-2017
			//Commented Nitish 29-08-2017
			/*ArrayList<DDLItem> alItems1 = new ArrayList<DDLItem>();

			if(SlabColl.getmMCHFlag().equals("1"))
			{

				alItems1.add( new DDLItem("4", "MCH : Meter Change"));
			}
			else if(Integer.valueOf(SlabColl.getmStatus())==2 || Integer.valueOf(SlabColl.getmStatus())==9 || Integer.valueOf(SlabColl.getmStatus())==10 || Integer.valueOf(SlabColl.getmStatus())==14)
			{
				alItems1.add( new DDLItem("0", "--NOT ALLOWED--"));
			}
			else
			{
				alItems1.add( new DDLItem("0", "Normal"));
				alItems1.add( new DDLItem("3", "DO  : Dial Over"));
			}
			DDLAdapter cList1 = null;
			if(cList1 == null)
			{
				cList1 = new DDLAdapter(BillingConsumption.this, alItems1);
			}
			ddlBillConsumStatus.setAdapter(cList1);*/

			//Commented Nitish 05-04-2017
			/*DDLAdapter cList = null;
			if(cList == null)
			{
				cList = new DDLAdapter(BillingConsumption.this, alItems);
			}

			ddlBillConsumStatus.setAdapter(cList);*/
			//Commented Nitish End 05-04-2017
			//Nitish 05-04-2017 End
			//End of Binding ddlBillConsumStatus(DropDownList)
			//END Loading Reason or Status in Dropdownlist============================================================
			//UnCommented Nitish 29-08-2017 to add All Reasons
			DDLAdapter cList = null;
			if(cList == null)
			{
				cList = new DDLAdapter(BillingConsumption.this, alItems);
			}
			ddlBillConsumStatus.setAdapter(cList);			
			//06-04-2017
			//int a = ddlBillConsumStatus.getSelectedItemPosition();	
			/*int a = Integer.valueOf(((DDLItem)ddlBillConsumStatus.getSelectedItem()).getId());
			SlabColl.setmPreStatus(((DDLItem)alConStatusItem.get(a)).getId());*/
			//UnCommented 29-08-2017
			int a = ddlBillConsumStatus.getSelectedItemPosition();		
			SlabColl.setmPreStatus(((DDLItem)alConStatusItem.get(a)).getId());	

			PForMDRequired(); //31-08-2016
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	//02-02-2016
	public void forUnmetered()
	{
		try 
		{
			DDLItem dNavnew;		
			ArrayList<DDLItem> alItems = new ArrayList<DDLItem>();
			dNavnew = new DDLItem("0", "Unmetered");
			alItems.add(dNavnew);
			DDLAdapter cList = null;
			if(cList == null)
			{
				cList = new DDLAdapter(BillingConsumption.this, alItems);
			}
			alConStatusItem = alItems;
			ddlBillConsumStatus.setAdapter(cList);
			ddlMainMtrType.setAdapter(cList);
			SlabColl.setmMtrDisFlag(0);
			SlabColl.setmPreStatus("0");

			//29-03-2016
			SlabColl.setmMeter_type("0");
		}
		catch (Exception e)
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
		return OptionsMenu.navigate(item,BillingConsumption.this);			
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
		// TODO Auto-generated method stub
		try
		{
			if(functionality == -1)
			{
				return;
			}
			if(alertResult)//alertResult is true ---> Exit
			{
				Intent i = new Intent(BillingConsumption.this, Billing.class);
				startActivity(i);
			}
			else
			{//Save Code from dialog========================================================
				//String imgName = ImageSaveinSDCard();
				//BillingObject.GetBillingObject().setmImage_Name(imgName);
				new Thread(new Runnable() {

					@Override
					public void run() 
					{
						//WriteLog.WriteLogError(TAG+" Save thread started.");
						result = db.BillSave(); 
						dh.post(new Runnable()
						{
							@Override
							public void run()
							{
								if(result == 1)
								{
									CustomToast.makeText(BillingConsumption.this, "Bill created successfully.", Toast.LENGTH_SHORT);
									//If Present Reading is 15(NT) Print Should Not Come
									if(!BillingObject.GetBillingObject().getmPreStatus().equals("15"))
									{
										//Bluetooth Code===========================================				
										bptr = new BluetoothPrinting(BillingConsumption.this);
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
											CustomToast.makeText(BillingConsumption.this, "Problem in printing data. Please check Bluetooth device connected or not", Toast.LENGTH_SHORT);
											Intent i = new Intent(BillingConsumption.this, Billing.class);
											startActivity(i);
										}
										//END Bluetooth Code=======================================
									}
									else//For reason(15 - Not Trace No Print )
									{
										Intent i = new Intent(BillingConsumption.this, Billing.class);
										startActivity(i);
									}
								}
								else//Tamilselvan on 20-03-2014 
								{
									CustomToast.makeText(BillingConsumption.this, "Problem in saving data.", Toast.LENGTH_SHORT);
									Intent i = new Intent(BillingConsumption.this, Billing.class);
									startActivity(i);
									finish();//08-05-2021
								}
							}
						});
					}
				}).start();

				ringProgress = ProgressDialog.show(BillingConsumption.this, "Please wait..", "Data Saving and Printing..",true);//Spinner
				ringProgress.setCancelable(false);
			}
		}
		catch (Exception e)
		{
			Log.d(TAG, e.toString());
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
	private class TimeSync extends AsyncTask<Void, Void, Void>  //GenericTypes AsyncTask<parametertype, progressparameter, postexecuteparam >
	{
		final ProgressDialog ringProgress = ProgressDialog.show(BillingConsumption.this, "Please wait..", "Time Sync...",true);
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
				CustomToast.makeText(BillingConsumption.this, "Error in Time Synchronisation", Toast.LENGTH_SHORT);				
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
						CustomToast.makeText(BillingConsumption.this,"Mobile Date Time not matching with Server."  , Toast.LENGTH_SHORT);
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
				CustomToast.makeText(BillingConsumption.this, "Error Occured", Toast.LENGTH_SHORT);
			}		
		}	
		//If Exception Occured 
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false);
			ringProgress.dismiss();
			CustomToast.makeText(BillingConsumption.this, "Time Synchronisation Failed.", Toast.LENGTH_SHORT);			
		}
	}


	//Bluetooth Code=======================================================================================	
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
						CustomToast.makeText(BillingConsumption.this, "Problem in printing data. Please check Bluetooth device connected or not", Toast.LENGTH_SHORT);
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
							Intent i = new Intent(BillingConsumption.this, Billing.class);
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
