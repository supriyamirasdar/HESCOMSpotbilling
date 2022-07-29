
// Created Nitish 07-10-2014
package in.nsoft.hescomspotbilling;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DisconnectionActivity extends Activity {

	private Button btnDisConnection,btnHome,btnPrev,btnNext;
	private TextView conid, rrNo,custname, tariffCode, load, arrears,status;
	TextView tvInflatorDisplayData;
	EditText txtReading;
	Spinner ddlStatus;

	int f;
	RadioGroup radioGroupStatus;
	RadioButton radioButtonStatus;
	int result,Mtr_DisFlag;
	String IMEINumber;
	DDLAdapter disAdapter;
	AlertDialog.Builder alert;
	AutoCompleteTextView ConnIdRRNo;
	AutoDDLAdapter conList;
	private final DatabaseHelper mDb =new DatabaseHelper(this);
	private static final String TAG = Billing.class.getName();

	//Nitish 13-02-2015
	static boolean isFromDisconnectionCamera = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disconnection);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		TelephonyManager mgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		IMEINumber = mgr.getDeviceId();	

		ConnIdRRNo =(AutoCompleteTextView)findViewById(R.id.autoBillingSearch);			
		conid = (TextView)findViewById(R.id.txtBillingConnectionId);
		rrNo = (TextView)findViewById(R.id.txtBillingRRNo);
		tariffCode =  (TextView)findViewById(R.id.txtBillingTariff);
		load = (TextView)findViewById(R.id.txtBillingLoad);
		custname = (TextView)findViewById(R.id.txtBillingCustName);
		arrears = (TextView)findViewById(R.id.txtBillingArrears);
		status = (TextView)findViewById(R.id.txtConnectionStatus);

		btnDisConnection = (Button)findViewById(R.id.btnDisconnection);
		btnHome = (Button)findViewById(R.id.btnHome);
		btnPrev = (Button)findViewById(R.id.btnPrev);
		btnNext = (Button)findViewById(R.id.btnNext);

		/*try 
		{

			//If BillingObject object not contains Connection no Then go and fetch
			//top 1 connection details which is not billed				
			if(BillingObject.GetBillingObject().getmConnectionNo() == null)
			{
				if(mDb.isEveryConnectionBilled())//Check Every connection is billed or not, if yes load 1st Connection
				{
					mDb.GetPrev("1");
					GetAssignNecessaryFields(BillingObject.GetBillingObject());
				}
				else//if no load top 1 connection details which is not billed
				{
					mDb.GetValueOnBillingPage();//Get top 1 connection details which is not billed	
					GetAssignNecessaryFields(BillingObject.GetBillingObject());
				} 
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
					GetAssignNecessaryFields(BillingObject.GetBillingObject());
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
		}*/
		
		//Modified 26-05-2021 Load only Arrears greater than 10
		try 
		{

			
			mDb.GetValueOnBillingPageDisc();//Get top 1 connection details which is not billed	
			GetAssignNecessaryFields(BillingObject.GetBillingObject());			

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try 
		{
			conList = mDb.GetConnIdRRNoDisc(); //Modified 26-05-2021 Load only Arrears greater than 10
			ConnIdRRNo.setThreshold(1);	
			ConnIdRRNo.setAdapter(conList);			

			ConnIdRRNo.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> alist, View arg1,
						int pos, long arg3) {
					// TODO Auto-generated method stub
					try 
					{

						DDLItem k = (DDLItem)  alist.getItemAtPosition(pos);					
						mDb.GetAllDatafromDb(k.getId());
						ConnIdRRNo.setText(k.getValue().substring(5).trim());							
						GetAssignNecessaryFields(BillingObject.GetBillingObject());

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
		btnDisConnection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {				
				int day = Integer.valueOf(CommonFunction.getCurrentDateByPara("Date"));
				BigDecimal txtArrears = BillingObject.GetBillingObject().getmArears() == null ? new BigDecimal(0) : BillingObject.GetBillingObject().getmArears().setScale(0, BigDecimal.ROUND_HALF_UP);
				if(Integer.valueOf(BillingObject.GetBillingObject().getmReadingDay().trim()) > day)
				{
					CustomToast.makeText(DisconnectionActivity.this, "Reading day is greater than current day. You can't bill this connection.", Toast.LENGTH_LONG);					
					return;
				}
				else if(txtArrears.compareTo(new BigDecimal(10)) < 0) //If Arrears Is Less Than 10 do not allow Disconnection
				{
					CustomToast.makeText(DisconnectionActivity.this, "Disconnection not allowed for Arrears less than 10.", Toast.LENGTH_LONG);					
					return;
				}				
				//saveFunction();
				Intent i = new Intent(DisconnectionActivity.this,CameraDisconnectionActivity.class);
				startActivity(i);
			}
		});
		btnHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {				
				/*Intent i = new Intent(DisconnectionActivity.this,HomePage.class);
				startActivity(i);*/
				
				if(BillingObject.GetBillingObject().getmGps_Latitude_image().trim() == null || BillingObject.GetBillingObject().getmGps_Latitude_image().trim().equals("") ||BillingObject.GetBillingObject().getmGps_Latitude_image().trim() == null || BillingObject.GetBillingObject().getmGps_Longitude_image().trim().equals(""))
				{
					CustomToast.makeText(DisconnectionActivity.this, "Latitude and Longitude not in File.", Toast.LENGTH_LONG);
					return;
				}
				if(LoginActivity.gpsTracker.isGPSConnected())
				{
					Intent i = new Intent(DisconnectionActivity.this,MapActivity.class);
					startActivity(i);
				}
				else
				{
					CustomToast.makeText(DisconnectionActivity.this, "GPS Disabled. Please turn on GPS.", Toast.LENGTH_LONG);
				}
			}
		});
		btnPrev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {					
				int flag=0;

				try 
				{
					//billObjprev = BillingObject.GetBillingObject();
					/*if(BillingObject.GetBillingObject().getmConnectionNo() == null)
					{
						CustomToast.makeText(DisconnectionActivity.this, "No Records.", Toast.LENGTH_SHORT);						
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
					
					 */		
					
					//Modified 26-05-2021 Load only Arrears greater than 10
					if(BillingObject.GetBillingObject().getmConnectionNo() == null)
					{
						CustomToast.makeText(DisconnectionActivity.this, "No Records.", Toast.LENGTH_SHORT);						
						return;
					}	
					f = BillingObject.GetBillingObject().getMmUID();
					f--; 					  
					mDb.GetPrevDisc(String.valueOf(f));					
					
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
		//btnpnext --> get Next Connection in then Billing Object
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {					
				int flag = 0;
				try
				{
					/*//billObjprev = BillingObject.GetBillingObject();
					if(BillingObject.GetBillingObject().getmConnectionNo() == null)
					{
						CustomToast.makeText(DisconnectionActivity.this, "No Records.", Toast.LENGTH_SHORT);						
						return;
					}
					if(flag == 0)
					{
						f = BillingObject.GetBillingObject().getMmUID();
						flag++;
					}
					f++; 					  
					mDb.GetPrev(String.valueOf(f));
					//Tamilselvan on 15-05-2014
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
					}*/
					//billObjprev = BillingObject.GetBillingObject();
					
					//Modified 26-05-2021 Load only Arrears greater than 10
					if(BillingObject.GetBillingObject().getmConnectionNo() == null)
					{
						CustomToast.makeText(DisconnectionActivity.this, "No Records.", Toast.LENGTH_SHORT);						
						return;
					}
					
					f = BillingObject.GetBillingObject().getMmUID();					
					f++; 					  
					mDb.GetNextDisc(String.valueOf(f));
					
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

		if(isFromDisconnectionCamera) //Nitish 13-02-2015
		{
			saveFunction();
		}

	}	
	public void saveFunction()
	{		
		alert = new AlertDialog.Builder(DisconnectionActivity.this);
		alert.setTitle(ConstantClass.mDisconnection);
		final View convertView = DisconnectionActivity.this.getLayoutInflater().inflate(R.layout.disconnectiondialog, null);	
		alert.setView(convertView);
		txtReading = (EditText)convertView.findViewById(R.id.txtReading);

		radioGroupStatus = (RadioGroup)convertView.findViewById(R.id.radioGroupStatus);
		tvInflatorDisplayData = (TextView)convertView.findViewById(R.id.tvInflatorDisplayData);

		alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				isFromDisconnectionCamera = false; //Nitish 13-02-2015
				Intent i = new Intent(DisconnectionActivity.this, DisconnectionActivity.class);
				startActivity(i);
				finish();
			}
		});
		alert.setNegativeButton("Save", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				try
				{					
					/*if(txtReading.getText().toString().equals(""))
					{
						CustomToast.makeText(DisconnectionActivity.this, "Reading field should not be empty.", Toast.LENGTH_SHORT);
						saveFunction();
						return;
					}
					BigDecimal bgPreReading = new BigDecimal(txtReading.getText().toString()).setScale(0, BigDecimal.ROUND_HALF_UP);//Present Reading
					BigDecimal bgPrvReading = new BigDecimal(BillingObject.GetBillingObject().getmPrevRdg().trim()).setScale(0, BigDecimal.ROUND_HALF_UP);//Previous reading
					if (bgPreReading.compareTo(bgPrvReading) < 0 ) 
					{
						CustomToast.makeText(DisconnectionActivity.this, "Present reading should not be less than previous reading.", Toast.LENGTH_SHORT);
						saveFunction();
						return;
					}		*/			
					int statusConnectionSelectOption =radioGroupStatus.getCheckedRadioButtonId();
					radioButtonStatus = (RadioButton)convertView.findViewById(statusConnectionSelectOption);

					if(statusConnectionSelectOption == -1)
					{						
						CustomToast.makeText(DisconnectionActivity.this, "Select Disconnection Status.",  Toast.LENGTH_SHORT);
						saveFunction();
						return;
					}
					else
					{
						result=0;
						if(radioButtonStatus.getText().equals(ConstantClass.mDisconnected))
						{

							String imgName = ImageSaveinSDCard();
							BillingObject.GetBillingObject().setmImage_Name(imgName);
							Mtr_DisFlag = 1;
							result = mDb.DisconnectionSave(Mtr_DisFlag,IMEINumber,String.valueOf(txtReading.getText().toString()));
						}
						else if (radioButtonStatus.getText().equals(ConstantClass.mNotDisconnected))
						{
							String imgName = ImageSaveinSDCard();
							BillingObject.GetBillingObject().setmImage_Name(imgName);
							Mtr_DisFlag = 2;
							result = mDb.DisconnectionSave(Mtr_DisFlag,IMEINumber,String.valueOf(txtReading.getText().toString()));
						}	
						if(result==1)
						{
							isFromDisconnectionCamera = false; //Nitish 13-02-2015
							btnDisConnection.setEnabled(false);
							CustomToast.makeText(DisconnectionActivity.this, "Disconnection Data Saved Successsfully.", Toast.LENGTH_SHORT);
							Intent i = new Intent(DisconnectionActivity.this, DisconnectionActivity.class);
							startActivity(i);
						}
						else				
						{
							CustomToast.makeText(DisconnectionActivity.this, "Problem in saving data.", Toast.LENGTH_SHORT);
							return;
						}
					}									
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					CustomToast.makeText(DisconnectionActivity.this, "Problem in saving data.", Toast.LENGTH_SHORT);
				}
			}
		});
		alert.setCancelable(false);
		alert.show();
	}
	//Modified Nitish 27-02-2014
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.		
		getMenuInflater().inflate(R.menu.spotbillingmenu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return OptionsMenu.navigate(item,DisconnectionActivity.this);			
	}	

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		CustomToast.makeText(DisconnectionActivity.this, "Please use menu to navigate.", Toast.LENGTH_SHORT);
		return;
	}
	/**
	 * Nitish on 07-10-2014
	 * Common function for getting billing object and assign value to controls
	 * @param billObj
	 */
	public void GetAssignNecessaryFields(ReadSlabNTarifSbmBillCollection billObj)
	{
		conid.setText(billObj.getmConnectionNo());
		rrNo.setText(billObj.getmRRNo());
		custname.setText(billObj.getmCustomerName());
		tariffCode.setText((new CommonFunction()).SplitTarifString(billObj.getmTarifString()));
		load.setText((new CommonFunction()).GetSancLoadWithHPKW(billObj));	
		arrears.setText(String.valueOf(billObj.getmArears().setScale(2, BigDecimal.ROUND_HALF_UP)));
		if(mDb.GetDisconnectionFlag(billObj.getmConnectionNo())==1)
		{
			status.setText(ConstantClass.mDisconnected);
			btnDisConnection.setEnabled(false);
		}
		else if(mDb.GetDisconnectionFlag(billObj.getmConnectionNo())==2)
		{
			status.setText(ConstantClass.mNotDisconnected);
			btnDisConnection.setEnabled(false);
		}
		else
		{
			status.setText(ConstantClass.mNotCaptured);
			btnDisConnection.setEnabled(true);
		}
	}
	//Nitish 13-02-2015
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
			ImageName = BillingObject.GetBillingObject().getmConnectionNo().trim() +"_"+ cFun.GetCurrentTime().replace(" ", "_").replace("-", "").replace(":", "")+"_DISCONN.jpg";
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
}
