
// Created Nitish 24-02-2014
package in.nsoft.hescomspotbilling;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;



import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.text.TextPaint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Billing_LandT extends Activity implements AlertInterface {

	int f;
	private Button btn, btnprev, btnpnext, btnCollection;	//Nitish 31-01-2015
	private TextView conid, rrNo, tariffCode, load, billed, txtBillingMtd,txtCustomerName,txtAddress1,txtAddress2;	//Nitish 31-01-2015

	Boolean autoSelected;
	AutoCompleteTextView ConnIdRRNo;
	AutoDDLAdapter conList;
	private final DatabaseHelper mDb =new DatabaseHelper(this);
	private static final String TAG = Billing_LandT.class.getName();
	int CharCount = 0;
	Intent indent = null;
	ProgressDialog ringProgress, ringGpsCheck;
	//Bluethooth variables===============================================
	int i = 0;
	Handler dh ;
	BluetoothPrinting bptr;
	static final int REQUEST_ENABLE_BT = 0;
	//END Bluethooth variables============================================

	static boolean isFromBilling = false; //05-09-2015

	static int LTLG = 0; //17-05-2016
	static int PROBETYPE = 0; //01-04-2021
	//29-01-2021
	AlertDialog.Builder alert;	
	private TextView tvInflatorMeterSelection,tvInflatorProbeSelection; //New Probe added 23-03-2021
	private Spinner ddlMeterSelection,ddlProbeSelection; //New Probe added 23-03-2021
	DDLAdapter meterTypeAdapter,probeTypeAdapter; //New Probe added 23-03-2021

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billing_landt);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		ConnIdRRNo =(AutoCompleteTextView)findViewById(R.id.autoBillingSearch);	
		final AutoCompleteTextView ConnIdRRNo =(AutoCompleteTextView)findViewById(R.id.autoBillingSearch);	
		btn = (Button)findViewById(R.id.btnBillingOk);
		btnCollection = (Button)findViewById(R.id.btnBillingCollection);
		conid = (TextView)findViewById(R.id.txtBillingConnectionId);
		rrNo = (TextView)findViewById(R.id.txtBillingRRNo);
		tariffCode =  (TextView)findViewById(R.id.txtBillingTariff);
		load = (TextView)findViewById(R.id.txtBillingLoad);
		txtBillingMtd = (TextView)findViewById(R.id.txtBillingMtd);
		billed = (TextView)findViewById(R.id.txtBillingStatus);

		//Nitish 31-01-2015
		txtCustomerName = (TextView)findViewById(R.id.txtCustomerName);
		txtAddress1 = (TextView)findViewById(R.id.txtAddress1);
		txtAddress2 = (TextView)findViewById(R.id.txtAddress2);
		btnprev	 = (Button)findViewById(R.id.btnBillingPrev);
		btnpnext = (Button)findViewById(R.id.btnBillingNext);	

		dh = new Handler();
		try 
		{			
			//If BillingObject object not contains Connection no Then go and fetch
			//top 1 connection details which is not billed				
			if(BillingObject.GetBillingObject().getmConnectionNo() == null)
			{
				if(mDb.isEveryConnectionBilled())//Check Every connection is billed or not, if yes load 1st Connection
				{
					mDb.GetPrev("1");
					GetAssignNecessaryFields(BillingObject.GetBillingObject());//Added By Tamilselvan on 02-04-2014
				}
				else//if no load top 1 connection details which is not billed
				{
					mDb.GetValueOnBillingPage();//Get top 1 connection details which is not billed	
					GetAssignNecessaryFields(BillingObject.GetBillingObject());//Added By Tamilselvan on 02-04-2014
				}/**/ 
			}
			else//Based on Previous object(if it is billed) get the next connection details
			{
				int uid = 0;
				if(BillingObject.GetBillingObject().getmBlCnt().equals("1"))//check the Current Object is billed or not 
				{//getmBlCnt is 1 then that connection is billed 
					uid = BillingObject.GetBillingObject().getMmUID() + 1;//get next uid by incrementing 1 
				}
				else//if getmBlCnt is 0 then get the same connection no
				{
					uid = BillingObject.GetBillingObject().getMmUID();
				}
				if(mDb.IsUIDExists(uid))//check uid exists in db or not, if exists then get the billing object
				{
					mDb.GetPrev(String.valueOf(uid));
					GetAssignNecessaryFields(BillingObject.GetBillingObject());//Added By Tamilselvan on 02-04-2014
				}
				else
				{
					mDb.GetPrev(String.valueOf(BillingObject.GetBillingObject().getMmUID()));
					GetAssignNecessaryFields(BillingObject.GetBillingObject());
				}
			}			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		try 
		{
			conList = mDb.GetConnIdRRNo();
			ConnIdRRNo.setThreshold(1);	
			ConnIdRRNo.setAdapter(conList);			
			//conList.notifyDataSetChanged();//Added By Tamilselvan on 25-03-2014
			autoSelected=false;

			ConnIdRRNo.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> alist, View arg1,
						int pos, long arg3) {
					// TODO Auto-generated method stub
					try 
					{
						//Nitish  27/02/2014
						DDLItem k = (DDLItem)  alist.getItemAtPosition(pos);					
						mDb.GetAllDatafromDb(k.getId());
						ConnIdRRNo.setText(k.getValue().substring(5).trim());						
						//billObj=	BillingObject.GetBillingObject();	
						GetAssignNecessaryFields(BillingObject.GetBillingObject());//Added By Tamilselvan on 02-04-2014	

						//By Nitish 08-04-2014
						//To Hide Soft KeyBoard
						InputMethodManager in = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
						ConnIdRRNo.setText("");
					} 
					catch (Exception e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		//Billing or Reprint Button --> if the connection is not Billed then this button becomes Billing
		//OR if it is billed then 
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				btn.setEnabled(false);
				btnprev.setEnabled(false); 
				btnpnext.setEnabled(false); 
				btnCollection.setEnabled(false);

				try
				{	
					isFromBilling = false; //05-09-2015
					if(btn.getText().equals(ConstantClass.btnOKText))
					{
						if(LoginActivity.gpsTracker.isGPSConnected())
						{
							//WriteLog.WriteLogError(TAG+" Billing btn clicked.");
							if(BillingObject.GetBillingObject().getmConnectionNo() == null)
							{
								CustomToast.makeText(Billing_LandT.this, "No Records.", Toast.LENGTH_SHORT);					
								return;
							}
							//Added on 09-09-2014 for checking Reading day if Reading day greater than current day
							//should not allow to bill that connection
							if(BillingObject.GetBillingObject().getmReadingDay().trim().length() > 0)
							{
								int day = Integer.valueOf(CommonFunction.getCurrentDateByPara("Date"));
								if(Integer.valueOf(BillingObject.GetBillingObject().getmReadingDay().trim()) > day)
								{
									CustomToast.makeText(Billing_LandT.this, "Reading day is greater than current day. You can't bill this connection.", Toast.LENGTH_LONG);					
									return;
								}
							}

							//17-05-2016
							btn.setEnabled(true);
							btnprev.setEnabled(true); 
							btnpnext.setEnabled(true); 
							btnCollection.setEnabled(true);
							isFromBilling = true; //05-09-2015
							LTLG = 0;

							PROBETYPE  = 0; //23-03-2021
							//29-01-2021
							MeterSelection();
							/*CustomAlert.CustomAlertParameters paramslt = new CustomAlert.CustomAlertParameters(); 
							paramslt.setContext(Billing_LandT.this);					
							paramslt.setMainHandler(dh);
							paramslt.setMsg("Select Meter Type");
							paramslt.setButtonOkText("L&G");
							paramslt.setButtonNeutralText("Cancel");
							paramslt.setButtonCancelText("L&T");
							paramslt.setTitle("MESSAGE");
							paramslt.setFunctionality(1);					
							CustomAlert cAlert   = new CustomAlert(paramslt);*/


							/*indent = new Intent(Billing.this, CameraActivity.class);
							startActivity(indent);/**/

							/*Intent i = new Intent(Billing_LandT.this, BillingConsumption_LandT.class);
							startActivity(i);*/
						}
						else
						{
							CustomToast.makeText(Billing_LandT.this, "GPS Disabled. Please turn on GPS.", Toast.LENGTH_LONG);
						}/**/
					}
					else if(btn.getText().equals(ConstantClass.btnReprintText))//Re-Print Start
					{
						//If Present Reading is 15(NT) Print Should Not Come
						if(!BillingObject.GetBillingObject().getmPreStatus().equals("15"))
						{
							//Bluetooth Code===========================================			
							bptr = new BluetoothPrinting(Billing_LandT.this);
							try 
							{
								ringProgress = ProgressDialog.show(Billing_LandT.this, "Please wait..", "Re-Printing...",true);//Spinner
								ringProgress.setCancelable(false);
								new Thread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										try 
										{
											Looper.prepare();
											bptr.openBT();
										} 
										catch (Exception e) 
										{
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										ConnectedThread cth = new ConnectedThread();
										cth.start(); 
										//Looper.loop();
									}
								}).start();

							}
							catch (Exception e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
								Log.d(TAG,e.toString());
								if(ringProgress != null)
									ringProgress.dismiss();
							}						
							//END Bluetooth Code=======================================
						}
						else
						{
							btn.setEnabled(true);
							btnprev.setEnabled(true); 
							btnpnext.setEnabled(true); 
							btnCollection.setEnabled(true);
						}
					}//END Re-Print
				}
				catch(Exception e)
				{
					Log.d(TAG, e.toString());
				}

			}
		});		
		btnCollection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {


			}
		});		
		btnprev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {					
				int flag=0;

				try 
				{
					//billObjprev = BillingObject.GetBillingObject();
					if(BillingObject.GetBillingObject().getmConnectionNo() == null)
					{
						CustomToast.makeText(Billing_LandT.this, "No Records.", Toast.LENGTH_SHORT);						
						return;
					}
					if(flag == 0)
					{
						f = BillingObject.GetBillingObject().getMmUID();//punit 25022014
						flag++;
					}
					f--; 					  
					mDb.GetPrev(String.valueOf(f));

					while(f != BillingObject.GetBillingObject().getMmUID())
					{
						if(f > 0)
						{
							f--;
							mDb.GetPrev(String.valueOf(f));
						}
						else
						{
							break;
						}
					}					
					ConnIdRRNo.setText("");
					GetAssignNecessaryFields(BillingObject.GetBillingObject());//Added By Tamilselvan on 02-04-2014					
				} 
				catch (Exception e) 
				{
					Log.d(TAG, e.toString());
					e.printStackTrace();
				}
			}
		});
		//btnpnext --> get Next Connection in then Billing Object
		btnpnext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {					
				int flag = 0;
				try
				{
					//billObjprev = BillingObject.GetBillingObject();
					if(BillingObject.GetBillingObject().getmConnectionNo() == null)
					{
						CustomToast.makeText(Billing_LandT.this, "No Records.", Toast.LENGTH_SHORT);						
						return;
					}
					if(flag == 0)
					{
						f = BillingObject.GetBillingObject().getMmUID();
						flag++;
					}
					f++; 					  
					mDb.GetPrev(String.valueOf(f));					
					int TotalConn = mDb.getCountofRecods();
					while(f != BillingObject.GetBillingObject().getMmUID())
					{
						if(f <= TotalConn)
						{
							f++;
							mDb.GetPrev(String.valueOf(f));
						}
						else
						{
							break;
						}
					}				
					ConnIdRRNo.setText("");
					GetAssignNecessaryFields(BillingObject.GetBillingObject());			
				}
				catch (Exception e) 
				{
					Log.d(TAG, e.toString());
					e.printStackTrace();
				}
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
		return OptionsMenu.navigate(item,Billing_LandT.this);			
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		//super.onBackPressed();
		CustomToast.makeText(Billing_LandT.this, "Please use menu to navigate.", Toast.LENGTH_SHORT);
		return;
	}

	/**
	 * Tamilselvan on 03-04-2014
	 * Common function for getting billing object and assign value to controls
	 * @param billObj
	 */
	public void GetAssignNecessaryFields(ReadSlabNTarifSbmBillCollection billObj)
	{
		conid.setText(billObj.getmConnectionNo());
		rrNo.setText(billObj.getmRRNo());
		tariffCode.setText((new CommonFunction()).SplitTarifString(billObj.getmTarifString()));
		load.setText((new CommonFunction()).GetSancLoadWithHPKW(billObj));
		if(billObj.getmBlCnt().equals("0") && billObj.getmBOBillFlag()  == 0)//if getmBlCnt is 0 then Connection is not Billed
		{			
			billed.setText(ConstantClass.sNB);//set text as Not Billed
			btn.setText(ConstantClass.btnOKText);//Set Text as Billing
			btn.setEnabled(true);
		}
		else if((billObj.getmBlCnt().equals("1") && billObj.getmBOBillFlag() == 0) || (billObj.getmBlCnt().equals("0") && billObj.getmBOBillFlag() == 1) || (billObj.getmBlCnt().equals("1") && billObj.getmBOBillFlag() == 1))//if getmBlCnt is 1 then Connection is Billed
		{
			if(billObj.getmBOBillFlag() == 1)
			{
				btn.setEnabled(false);
			}
			else
			{
				btn.setEnabled(true);
			}
			billed.setText(ConstantClass.sBilled);//set text as Billed 
			btn.setText(ConstantClass.btnReprintText);//Set Text as Re-Print
		}
		//31-01-2015
		try
		{

			txtCustomerName.setText(billObj.getmCustomerName().trim().equals("")? "--" : String.valueOf(billObj.getmCustomerName()));
			txtAddress1.setText(billObj.getmAddress1().trim().equals("")? "--" : String.valueOf(billObj.getmAddress1()));
			txtAddress2.setText(billObj.getmAddress2().trim().equals("")? "--" : String.valueOf(billObj.getmAddress2()));


		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			Log.d(TAG,e.toString());
		}

		//Show Connection is Metered or Not Metered
		txtBillingMtd.setText(billObj.getmMtd().equals("1") ? "Metered" : "Not Metered");//getmMtd is 1 then Metered
	}

	//Bluetooth Code=======================================================================================
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)	
	public class ConnectedThread extends Thread //ConnectedThread
	{	
		public void run()
		{
			//Nitish 02-04-2018
			setPriority(Thread.MAX_PRIORITY);
			//Looper.prepare();
			PrinterCharacterAlign pca = new PrinterCharacterAlign();//Class Alignment for printer 
			CommonFunction cFun = new CommonFunction();
			ReadSlabNTarifSbmBillCollection SlabColl1 = BillingObject.GetBillingObject();//Get Billing Object

			try 
			{
				///////////////////////////////////////////////Definition Start/////////////////////////////////////
				String pSubDiv = mDb.GetSubDivisionName().trim();
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
				Print1 = Print1 +  "\n" + pca.CreateBlankSpaceSequence(16, " ") +"--------------- Re-Print ---------------"+"\n";
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
				//Added 07-05-2021
				try
				{
					String pReceiptDate = SlabColl1.getmReceiptDate()==null?" ":SlabColl1.getmReceiptDate().trim();
					if(pReceiptDate.trim().length()<=8)
						pReceiptDate = cFun.DateConvertAddChar(SlabColl1.getmReceiptDate().trim(), "-");

					Print3 += "\n  ಹಿಂದಿನ ರಸೀದಿ  ದಿನಾಂಕ   "+ pca.RightAlignInBoxLayoutNew3(pReceiptDate," ",32)+ "\n";

					String pReceiptAmount = SlabColl1.getmReceiptAmnt() == null? "" : String.valueOf(SlabColl1.getmReceiptAmnt().setScale(2, BigDecimal.ROUND_HALF_UP));
					Print3 += "\n  ಹಿಂದಿನ ರಸೀದಿ  ಮೊತ್ತ   "+ getRightAlignData(pReceiptAmount.trim(),4,42)+ "\n";
				}
				catch(Exception e)
				{

				}
				
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
					mDb.EventLogSave("6", LoginActivity.IMEINumber, LoginActivity.SimNumber, "Bill Re-Print Completed For Connection:  " + SlabColl1.getmConnectionNo().trim());//15-07-2016
				}
				catch(Exception e)
				{

				}

			} 
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				Log.d(TAG,e.toString());
			}			
			finally
			{
				try 
				{
					bptr.closeBT();
					if(BluetoothPrinting.isConnected)
					{
						Thread.sleep(9000);
						//isException = false;
					}
				}
				catch (Exception e) 
				{
					Log.d(TAG,e.toString());
					e.printStackTrace();
				}	
				ringProgress.dismiss();
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						//bptr.beginListenForData();
						dh.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								//Toast.makeText(Billing.this, "Printing finish.", Toast.LENGTH_SHORT).show();
								//btn.setVisibility(Button.VISIBLE);
								btn.setEnabled(true);								
								btnprev.setEnabled(true); 
								btnpnext.setEnabled(true); 
								btnCollection.setEnabled(true);
							}
						});
					}
				}).start();/**/
				//Looper.loop();
			}
		}
		public void cancel()
		{

		}
	}//END ConnectedThread
	//END Bluetooth Code====================================================================================
	@Override
	public void performAction(boolean alertResult, int functionality)
	{
		if(!alertResult)
		{
			LTLG = 1;
			Intent i = new Intent(Billing_LandT.this, BillingConsumption_LandT.class);				
			startActivity(i);
		}
		else if(alertResult)
		{	
			LTLG = 2;
			Intent i = new Intent(Billing_LandT.this, BillingConsumption_LandT.class);				
			startActivity(i);
		}		
	}	
	@Override
	protected void onResume() {
		// Ideally should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onResume();
	}
	@Override
	protected void onPause() {
		// Ideally should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onPause();
	}
	@Override
	protected void onStop()
	{
		// Ideally should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onStop();
	}
	public void MeterSelection()
	{
		alert = new AlertDialog.Builder(Billing_LandT.this);
		alert.setTitle("Meter Type Selection");
		View convertView = Billing_LandT.this.getLayoutInflater().inflate(R.layout.billingdialoginflator_lt, null);	
		alert.setView(convertView);

		tvInflatorMeterSelection = (TextView)convertView.findViewById(R.id.tvInflatorMeterSelection);

		ddlMeterSelection = (Spinner)convertView.findViewById(R.id.ddlMeterSelection);

		tvInflatorProbeSelection = (TextView)convertView.findViewById(R.id.tvInflatorProbeSelection); //23-03-2021
		ddlProbeSelection = (Spinner)convertView.findViewById(R.id.ddlProbeSelection); //23-03-2021


		try {
			meterTypeAdapter = mDb.getMType();
			ddlMeterSelection.setAdapter(meterTypeAdapter);

			//23-03-2021
			probeTypeAdapter = mDb.getProbeType();
			ddlProbeSelection.setAdapter(probeTypeAdapter);

			ddlMeterSelection.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub				
					LTLG= Integer.valueOf(meterTypeAdapter.GetId(arg2).toString());
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});
			ddlProbeSelection.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub				
					PROBETYPE= Integer.valueOf(probeTypeAdapter.GetId(arg2).toString());
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Intent i = new Intent(Billing_LandT.this, Billing_LandT.class);
				startActivity(i);


			}
		});
		alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {

				Intent i = new Intent(Billing_LandT.this, BillingConsumption_LandT.class);				
				startActivity(i);

			}
		});

		alert.setCancelable(false);
		alert.show();
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
