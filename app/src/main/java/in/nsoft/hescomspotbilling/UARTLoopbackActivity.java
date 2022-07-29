package in.nsoft.hescomspotbilling;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UARTLoopbackActivity extends Activity {

	boolean uart_configured = false;
	String uartSettings  = "";
	// menu item
	Menu myMenu;
	final int MENU_FORMAT = Menu.FIRST;
	final int MENU_CLEAN = Menu.FIRST+1;
	final String[] formatSettingItems = {"ASCII","Hexadecimal", "Decimal"};

	final int FORMAT_ASCII = 0;
	final int FORMAT_HEX = 1;
	final int FORMAT_DEC = 2;
	//10-06-2015
	static int count1=0,count2 = 0;
	int inputFormat = FORMAT_ASCII;
	StringBuffer readSB = new StringBuffer();
	long addtime = 0;

	/* thread to read the data */
	public handler_thread handlerThread;
	Handler h;
	/* declare a FT311 UART interface variable */
	public FT311UARTInterface uartInterface;
	/* graphical objects */
	TextView readText;
	EditText writeText;
	Spinner baudSpinner;;
	Spinner stopSpinner;
	Spinner dataSpinner;
	Spinner paritySpinner;


	Spinner flowSpinner;
	TextView uartInfo;
	int cnt=0;
	ScrollView scrollView;
	//TextView readText;

	Button writeButton,configButton;

	/* local variables */
	byte[] writeBuffer;
	byte[] readBuffer;
	char[] readBufferToChar;
	int[] actualNumBytes;
	byte[] readDataBuffer;

	int numBytes;
	byte count;
	byte status;
	byte writeIndex = 0;
	byte readIndex = 0;

	int baudRate; /* baud rate */
	byte stopBit; /* 1:1stop bits, 2:2 stop bits */
	byte dataBit; /* 8:8bit, 7: 7bit */
	byte parity; /* 0: none, 1: odd, 2: even, 3: mark, 4: space */
	byte flowControl; /* 0:none, 1: flow control(CTS,RTS) */
	public Context global_context;
	public boolean bConfiged = false;
	public SharedPreferences sharePrefSettings;
	Drawable originalDrawable;
	public String act_string; 
	Button btnAuto;

	String ReceiveStr = null;

	//Nitish 23-12-2016
	String[] DLMSRequestArray_3_Ph_DLMS = {"7E,A0,07,03,41,93,5A,64,7E",
			"7E,A0,48,03,41,10,87,76,E6,E6,00,60,3A,80,02,02,84,A1,09,06,07,60,85,74,05,08,01,01,8A,02,07,80,8B,07,60,85,74,05,08,02,01,AC,0A,80,08,41,42,43,44,30,30,30,31,BE,10,04,0E,01,00,00,00,06,5F,1F,04,00,00,18,1D,FF,FF,8A,C8,7E",
			"7E,A0,19,03,41,32,3A,BD,E6,E6,00,C0,01,81,00,01,00,00,60,01,00,FF,02,00,8C,6D,7E",
			"7E,A0,19,03,41,54,0A,BB,E6,E6,00,C0,01,81,00,01,00,00,60,01,00,FF,03,00,54,74,7E",
			"7E,A0,19,03,41,76,1A,B9,E6,E6,00,C0,01,81,00,08,00,00,01,00,00,FF,02,00,65,D7,7E",
			"7E,A0,19,03,41,98,6A,B7,E6,E6,00,C0,01,81,00,08,00,00,01,00,00,FF,03,00,BD,CE,7E",
			"7E,A0,19,03,41,BA,7A,B5,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,02,00,37,A5,7E",
			"7E,A0,19,03,41,DC,4A,B3,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,03,00,EF,BC,7E",
			"7E,A0,19,03,41,FE,5A,B1,E6,E6,00,C0,01,81,00,03,01,00,09,08,00,FF,02,00,6F,84,7E",
			"7E,A0,19,03,41,10,2A,BF,E6,E6,00,C0,01,81,00,03,01,00,09,08,00,FF,03,00,B7,9D,7E",
			"7E,A0,19,03,41,32,3A,BD,E6,E6,00,C0,01,81,00,03,01,00,01,07,00,FF,02,00,CB,CF,7E",
			"7E,A0,19,03,41,54,0A,BB,E6,E6,00,C0,01,81,00,03,01,00,01,07,00,FF,03,00,13,D6,7E",
			"7E,A0,19,03,41,76,1A,B9,E6,E6,00,C0,01,81,00,03,01,00,21,07,00,FF,02,00,AB,4A,7E",
	"7E,A0,19,03,41,98,6A,B7,E6,E6,00,C0,01,81,00,03,01,00,21,07,00,FF,03,00,73,53,7E"};

	String[] DLMSRequestArray_Saral_DLMS = {"7E,A0,07,03,41,93,5A,64,7E",
			"7E,A0,48,03,41,10,87,76,E6,E6,00,60,3A,80,02,02,84,A1,09,06,07,60,85,74,05,08,01,01,8A,02,07,80,8B,07,60,85,74,05,08,02,01,AC,0A,80,08,41,42,43,44,30,30,30,31,BE,10,04,0E,01,00,00,00,06,5F,1F,04,00,00,18,1D,FF,FF,8A,C8,7E",
			"7E,A0,19,03,41,32,3A,BD,E6,E6,00,C0,01,81,00,01,00,00,60,01,00,FF,02,00,8C,6D,7E",
			"7E,A0,19,03,41,54,0A,BB,E6,E6,00,C0,01,81,00,01,00,00,60,01,00,FF,03,00,54,74,7E",
			"7E,A0,19,03,41,76,1A,B9,E6,E6,00,C0,01,81,00,08,00,00,01,00,00,FF,02,00,65,D7,7E",
			"7E,A0,19,03,41,98,6A,B7,E6,E6,00,C0,01,81,00,08,00,00,01,00,00,FF,03,00,BD,CE,7E",
			"7E,A0,19,03,41,BA,7A,B5,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,02,00,37,A5,7E",
	"7E,A0,19,03,41,DC,4A,B3,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,03,00,EF,BC,7E"};

	String[] DLMSRequestArrayICredit = {"7E,A0,07,03,41,93,5A,64,7E",
			"7E,A0,48,03,41,10,87,76,E6,E6,00,60,3A,80,02,02,84,A1,09,06,07,60,85,74,05,08,01,01,8A,02,07,80,8B,07,60,85,74,05,08,02,01,AC,0A,80,08,41,42,43,44,30,30,30,31,BE,10,04,0E,01,00,00,00,06,5F,1F,04,00,00,18,1D,FF,FF,8A,C8,7E",
			"7E,A0,19,03,41,32,3A,BD,E6,E6,00,C0,01,81,00,01,00,00,60,01,00,FF,02,00,8C,6D,7E",
			"7E,A0,19,03,41,54,0A,BB,E6,E6,00,C0,01,81,00,01,00,00,60,01,00,FF,03,00,54,74,7E",
			"7E,A0,19,03,41,76,1A,B9,E6,E6,00,C0,01,81,00,08,00,00,01,00,00,FF,02,00,65,D7,7E",
			"7E,A0,19,03,41,98,6A,B7,E6,E6,00,C0,01,81,00,08,00,00,01,00,00,FF,03,00,BD,CE,7E",
			"7E,A0,19,03,41,BA,7A,B5,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,02,00,37,A5,7E",
	"7E,A0,19,03,41,DC,4A,B3,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,03,00,EF,BC,7E"};

	static String[] DLMSRequestArrayLG = {"7E,A0,07,03,41,93,5A,64,7E",
		"7E,A0,48,03,41,10,87,76,E6,E6,00,60,3A,80,02,02,84,A1,09,06,07,60,85,74,05,08,01,01,8A,02,07,80,8B,07,60,85,74,05,08,02,01,AC,0A,80,08,31,31,31,31,31,31,31,31,BE,10,04,0E,01,00,00,00,06,5F,1F,04,00,00,18,1D,FF,FF,43,8A,7E",
		"7E,A0,19,03,41,32,3A,BD,E6,E6,00,C0,01,81,00,01,00,00,60,01,00,FF,02,00,8C,6D,7E",
		"7E,A0,19,03,41,54,0A,BB,E6,E6,00,C0,01,81,00,01,00,00,60,01,00,FF,03,00,54,74,7E",
		"7E,A0,19,03,41,76,1A,B9,E6,E6,00,C0,01,81,00,08,00,00,01,00,00,FF,02,00,65,D7,7E",
		"7E,A0,19,03,41,98,6A,B7,E6,E6,00,C0,01,81,00,08,00,00,01,00,00,FF,03,00,BD,CE,7E",
		"7E,A0,19,03,41,BA,7A,B5,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,02,00,37,A5,7E",
	"7E,A0,19,03,41,DC,4A,B3,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,03,00,EF,BC,7E"};
	
	static String[] DLMS_LLS_HPL = {"7E,A0,07,03,41,93,5A,64,7E",
		"7E,A0,50,03,41,10,FE,50,E6,E6,00,60,42,80,02,02,84,A1,09,06,07,60,85,74,05,08,01,01,8A,02,07,80,8B,07,60,85,74,05,08,02,01,AC,12,80,10,31,31,31,31,31,31,31,31,31,31,31,31,31,31,31,31,BE,10,04,0E,01,00,00,00,06,5F,1F,04,00,00,18,1D,FF,FF,A5,ED,7E",
		"7E,A0,19,03,41,32,3A,BD,E6,E6,00,C0,01,81,00,01,00,00,60,01,00,FF,02,00,8C,6D,7E",
		"7E,A0,19,03,41,54,0A,BB,E6,E6,00,C0,01,81,00,01,00,00,60,01,00,FF,03,00,54,74,7E",
		"7E,A0,19,03,41,76,1A,B9,E6,E6,00,C0,01,81,00,08,00,00,01,00,00,FF,02,00,65,D7,7E",
		"7E,A0,19,03,41,98,6A,B7,E6,E6,00,C0,01,81,00,08,00,00,01,00,00,FF,03,00,BD,CE,7E",
		"7E,A0,19,03,41,BA,7A,B5,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,02,00,37,A5,7E",
	"7E,A0,19,03,41,DC,4A,B3,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,03,00,EF,BC,7E"};


	static String[] DLMSRequestArray_LT_1Ph = {"7E,A0,07,03,41,93,5A,64,7E",
		"7E,A0,44,03,41,10,B3,E1,E6,E6,00,60,36,80,02,02,84,A1,09,06,07,60,85,74,05,08,01,01,8A,02,07,80,8B,07,60,85,74,05,08,02,01,AC,06,80,04,6C,6E,74,31,BE,10,04,0E,01,00,00,00,06,5F,1F,04,00,00,18,1D,FF,FF,1F,5E,7E",
		"7E,A0,19,03,41,32,3A,BD,E6,E6,00,C0,01,81,00,01,00,00,60,01,00,FF,02,00,8C,6D,7E",
		"7E,A0,19,03,41,54,0A,BB,E6,E6,00,C0,01,81,00,01,00,00,60,01,00,FF,03,00,54,74,7E",
		"7E,A0,19,03,41,76,1A,B9,E6,E6,00,C0,01,81,00,08,00,00,01,00,00,FF,02,00,65,D7,7E",
		"7E,A0,19,03,41,98,6A,B7,E6,E6,00,C0,01,81,00,08,00,00,01,00,00,FF,03,00,BD,CE,7E",
		"7E,A0,19,03,41,BA,7A,B5,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,02,00,37,A5,7E",
	"7E,A0,19,03,41,DC,4A,B3,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,03,00,EF,BC,7E"};


	static String IMEINumber = "";
	static String SimNumber = "";
	DatabaseHelper db = new DatabaseHelper(this);
	//End 23-12-2016


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uartloopback);
		sharePrefSettings = getSharedPreferences("UARTLBPref", 0);
		cleanPreference();

		//Nitish 23-12-2016
		try
		{
			//Get Mobile Details ==============================================================
			//16-01-2021
			IMEINumber = CommonFunction.getDeviceNo(UARTLoopbackActivity.this);
			SimNumber = CommonFunction.getSIMNo(UARTLoopbackActivity.this);

		}
		catch(Exception e)
		{
							
		}

		h = new Handler();
		global_context = this;
		Billing_LandT.isFromBilling=false; //05-09-2015

		//btnAuto = (Button) findViewById(R.id.btnAuto);
		//configButton=(Button)findViewById(R.id.configButton);
		//readText = (TextView) findViewById(R.id.ReadValues);

		//originalDrawable = configButton.getBackground();

		/* allocate buffer */
		writeBuffer = new byte[64];//64
		readBuffer = new byte[4096];//4096
		readBufferToChar = new char[4096]; //4096
		//readDataBuffer = new byte[65536];
		actualNumBytes = new int[1];
		uartInterface = new FT311UARTInterface(this, sharePrefSettings);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		/*try
		{
		if(false == bConfiged){

			bConfiged = true;
			uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
			savePreference();
		}

		if(true == bConfiged){
			configButton.setBackgroundColor(0xff888888); // color GRAY:0xff888888
			.setText("Configured");
		}

		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		 */		



		/*	 setup the baud rate list 
	baudSpinner = (Spinner) findViewById(R.id.baudRateValue);
		ArrayAdapter<CharSequence> baudAdapter = ArrayAdapter.createFromResource(this, R.array.baud_rate_1,
				R.layout.my_spinner_textview);
		baudAdapter.setDropDownViewResource(R.layout.my_spinner_textview);		
		baudSpinner.setAdapter(baudAdapter);
		baudSpinner.setGravity(0x10);
		baudSpinner.setSelection(3);
		// by default it is 4800 
		baudRate = 4800;

		 //stop bits 
		stopSpinner = (Spinner) findViewById(R.id.stopBitValue);
		ArrayAdapter<CharSequence> stopAdapter = ArrayAdapter.createFromResource(this, R.array.stop_bits,
				R.layout.my_spinner_textview);
		stopAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
		stopSpinner.setAdapter(stopAdapter);
		stopSpinner.setGravity(0x01);
		 //default is stop bit 1 
		stopBit = 1;

		// daat bits 
		dataSpinner = (Spinner) findViewById(R.id.dataBitValue);
		ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this, R.array.data_bits,
				R.layout.my_spinner_textview);
		dataAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
		dataSpinner.setAdapter(dataAdapter);
		dataSpinner.setGravity(0x11);
		dataSpinner.setSelection(0);
		// default data bit is 8 bit 
		dataBit = 7;

		// parity 
		paritySpinner = (Spinner) findViewById(R.id.parityValue);
		ArrayAdapter<CharSequence> parityAdapter = ArrayAdapter.createFromResource(this, R.array.parity,
				R.layout.my_spinner_textview);
		parityAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
		paritySpinner.setAdapter(parityAdapter);
		paritySpinner.setGravity(0x11);
		paritySpinner.setSelection(2);

		 //default is even 
		parity = 2;

		// flow control 
		flowSpinner =  (Spinner)findViewById(R.id.flowControlValue);
		ArrayAdapter<CharSequence> flowAdapter = ArrayAdapter.createFromResource(this, R.array.flow_control,
				R.layout.my_spinner_textview);
		flowAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
		flowSpinner.setAdapter(flowAdapter);
		flowSpinner.setGravity(0x11);
		// default flow control is is none 
		flowControl = 0;

		 */


		/*configButton.setOnClickListener(new View.OnClickListener() {

			// @Override
			public void onClick(View v) {

				if(false == bConfiged){

					bConfiged = true;
					uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
					savePreference();
				}

				if(true == bConfiged){
					configButton.setBackgroundColor(0xff888888); // color GRAY:0xff888888
					configButton.setText("Configured");
				}
			}

		});*/


		handlerThread = new handler_thread(handler);
		handlerThread.start();

		//Nitish 23-12-2016
		stopBit=1;
		dataBit=7;
		parity=2;
		flowControl=0;
		if(Billing_LandT.LTLG == 1 || Billing_LandT.LTLG == 7)
			baudRate=4800;
		else if(Billing_LandT.LTLG == 2)
			baudRate=300;
		else
		{
			baudRate = 9600;
			dataBit=8;
			parity=0;
		}
		//End 23-12-2016

		
		
		

		

		try
		{
			if(false == bConfiged){

				bConfiged = true;
				uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
				savePreference();				
			}

			if(true == bConfiged){
				/*	configButton.setBackgroundColor(0xff888888); // color GRAY:0xff888888
			.setText("Configured");*/
			}

		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		/*btnAuto.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				try
				{
					autoButtonfunc();


				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});*/


	}

	protected void cleanPreference(){
		SharedPreferences.Editor editor = sharePrefSettings.edit();
		editor.remove("configed");
		editor.remove("baudRate");
		editor.remove("stopBit");
		editor.remove("dataBit");
		editor.remove("parity");
		editor.remove("flowControl");
		editor.commit();
	}

	protected void savePreference() {
		if(true == bConfiged){
			sharePrefSettings.edit().putString("configed", "TRUE").commit();
			sharePrefSettings.edit().putInt("baudRate", baudRate).commit();
			sharePrefSettings.edit().putInt("stopBit", stopBit).commit();
			sharePrefSettings.edit().putInt("dataBit", dataBit).commit();
			sharePrefSettings.edit().putInt("parity", parity).commit();			
			sharePrefSettings.edit().putInt("flowControl", flowControl).commit();			
		}
		else{

			sharePrefSettings.edit().putString("configed", "FALSE").commit();
		}
	}

	protected void restorePreference() {
		String key_name = sharePrefSettings.getString("configed", "");
		if(true == key_name.contains("TRUE")){
			bConfiged = true;
		}
		else{
			bConfiged = false;
		}

		baudRate = sharePrefSettings.getInt("baudRate", 4800);
		stopBit = (byte)sharePrefSettings.getInt("stopBit", 1);
		dataBit = (byte)sharePrefSettings.getInt("dataBit", 7);
		parity = (byte)sharePrefSettings.getInt("parity", 2);
		flowControl = (byte)sharePrefSettings.getInt("flowControl", 0);

		if(true == bConfiged)
		{			
			switch(baudRate)
			{
			case 300:baudSpinner.setSelection(0);break;
			case 600:baudSpinner.setSelection(1);break;
			case 1200:baudSpinner.setSelection(2);break;
			case 4800:baudSpinner.setSelection(3);break;
			case 9600:baudSpinner.setSelection(4);break;
			case 19200:baudSpinner.setSelection(5);break;
			case 38400:baudSpinner.setSelection(6);break;
			case 57600:baudSpinner.setSelection(7);break;
			case 115200:baudSpinner.setSelection(8);break;
			case 230400:baudSpinner.setSelection(9);break;
			case 460800:baudSpinner.setSelection(10);break;
			case 921600:baudSpinner.setSelection(11);break;
			default:baudSpinner.setSelection(3);break;
			}

			switch(stopBit)
			{
			case 1:stopSpinner.setSelection(0);break;
			case 2:stopSpinner.setSelection(1);break;
			default:stopSpinner.setSelection(0);break;
			}

			switch(dataBit)
			{
			case 7:dataSpinner.setSelection(0);break;
			case 8:dataSpinner.setSelection(1);break;
			default:dataSpinner.setSelection(0);break;
			}

			switch(parity)
			{
			case 0:paritySpinner.setSelection(0);break;
			case 1:paritySpinner.setSelection(1);break;
			case 2:paritySpinner.setSelection(2);break;
			case 3:paritySpinner.setSelection(3);break;
			case 4:paritySpinner.setSelection(4);break;
			default:paritySpinner.setSelection(2);break;
			}

			switch(flowControl)
			{
			case 0:flowSpinner.setSelection(0);break;
			case 1:flowSpinner.setSelection(1);break;
			default:flowSpinner.setSelection(0);break;
			}
		}

		else{
			baudSpinner.setSelection(3);
			stopSpinner.setSelection(0);
			dataSpinner.setSelection(0);
			paritySpinner.setSelection(2);
			flowSpinner.setSelection(0);

		}
	}


	public class MyOnBaudSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

			baudRate = Integer.parseInt(parent.getItemAtPosition(pos).toString());
		}

		public void onNothingSelected(AdapterView<?> parent) { // Do nothing. }}
		}
	}

	public class MyOnStopSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

			stopBit = (byte) Integer.parseInt(parent.getItemAtPosition(pos).toString());
		}

		public void onNothingSelected(AdapterView<?> parent) { // Do nothing. }}
		}
	}

	public class MyOnDataSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

			dataBit = (byte) Integer.parseInt(parent.getItemAtPosition(pos).toString());
		}

		public void onNothingSelected(AdapterView<?> parent) { // Do nothing. }}
		}
	}

	public class MyOnParitySelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

			String parityString = new String(parent.getItemAtPosition(pos).toString());
			if (parityString.compareTo("None") == 0) {
				parity = 0;
			}

			if (parityString.compareTo("Odd") == 0) {
				parity = 1;
			}

			if (parityString.compareTo("Even") == 0) {
				parity = 2;
			}

			if (parityString.compareTo("Mark") == 0) {
				parity = 3;
			}

			if (parityString.compareTo("Space") == 0) {
				parity = 4;
			}
		}

		public void onNothingSelected(AdapterView<?> parent) { // Do nothing. 
		}
	}

	public class MyOnFlowSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {

			String flowString = new String(parent.getItemAtPosition(pos).toString());
			if (flowString.compareTo("None") == 0) {
				flowControl = 0;
			}

			if (flowString.compareTo("CTS/RTS") == 0) {
				flowControl = 1;
			}
		}

		public void onNothingSelected(AdapterView<?> parent) { // Do nothing. }}
		}
	}

	//@Override
	public void onHomePressed() {
		onBackPressed();
	}	

	public void onBackPressed() {
		super.onBackPressed();
	}	

	@Override
	protected void onResume() {
		// Ideally should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus

		super.onResume();	
		if( 2 == uartInterface.ResumeAccessory() )
		{


			cleanPreference();

			restorePreference();
			uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);

		}
		addtime = Calendar.getInstance().getTimeInMillis() + 100;
		while(addtime > Calendar.getInstance().getTimeInMillis())  
		{										

		}
		//23-12-2016
		if(Billing_LandT.LTLG==1)
			autoButtonfunc();//redirect to billing consumption page
		else if(Billing_LandT.LTLG==2)
			autoButtonfuncLG();
		else if(Billing_LandT.LTLG==3)  //Secure
			autoButtonfunc_DLMS_3_Phase();//autoButtonfunc_3_ph_DLMS();//DLMS 3-Phase Meter Reading Method
		else if(Billing_LandT.LTLG==4)  //Secure
			autoButtonfunc_DLMS_1_Phase();//autoButtonfunc_3_ph_DLMS();//DLMS 1-Phase Meter Reading Method
		else if(Billing_LandT.LTLG==5)  //Secure ICredit
			autoButtonfunc_DLMS_1_Phase_iCredit();//autoButtonfunc_3_ph_DLMS();//DLMS i credit Meter Reading Method
		else if(Billing_LandT.LTLG==6)  //DLMS-L&G
			autoButtonfunc_DLMS_LG();//autoButtonfunc_3_ph_DLMS();//DLMS L&G Meter Reading Method		
		else if(Billing_LandT.LTLG==7)  //L&T All Param
			autoButtonfuncAllLT();//
		else if(Billing_LandT.LTLG==8)  //HPL
			autoButtonfunc_DLMS_LLS_HPL();//
		else if(Billing_LandT.LTLG==9)  //L&T 
			autoButtonfunc_DLMS_LT_1_Ph();
		
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

	@Override
	protected void onDestroy() 
	{
		uartInterface.DestroyAccessory(bConfiged);
		super.onDestroy();

	}



	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			for(int i=0; i<actualNumBytes[0]; i++)
			{
				readBufferToChar[i] = (char)readBuffer[i];
				//Toast.makeText(UARTLoopbackActivity.this, "DataR"  , Toast.LENGTH_SHORT).show();

			}
			appendData(readBufferToChar, actualNumBytes[0]);
		}
	};

	/* usb input data handler */
	private class handler_thread extends Thread {
		Handler mHandler;


		/* constructor */
		handler_thread(Handler h) {
			mHandler = h;
		}

		public void run() {
			Message msg;

			while (true) {

				try {
					Thread.sleep(1800);//300//1600//1500//1800---working properly
				} 
				catch (InterruptedException e) 
				{

				}

				status = uartInterface.ReadData(4096,readBuffer,actualNumBytes);//65536 not working

				if (status == 0x00 && actualNumBytes[0] > 0) {
					msg = mHandler.obtainMessage();
					mHandler.sendMessage(msg);
				}

			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		myMenu = menu;
		myMenu.add(0, MENU_FORMAT, 0, "Format - ASCII");
		myMenu.add(0, MENU_CLEAN, 0, "Clean Read Bytes Field");		
		return super.onCreateOptionsMenu(myMenu);
	}	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case MENU_FORMAT:
			new AlertDialog.Builder(global_context).setTitle("Data Format")
			.setItems(formatSettingItems, new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{	
					MenuItem item = myMenu.findItem(MENU_FORMAT);
					if(0 == which)
					{						
						inputFormat = FORMAT_ASCII;
						item.setTitle("Format - "+ formatSettingItems[0]);
					}
					else if(1 == which)
					{
						inputFormat = FORMAT_HEX;
						item.setTitle("Format - "+ formatSettingItems[1]);
					}
					else
					{
						inputFormat = FORMAT_DEC;
						item.setTitle("Format - "+ formatSettingItems[2]);						
					}
					char[] ch = new char[1];
					appendData(ch, 0);
				}
			}).show();           	

			break;

		case MENU_CLEAN:
		default:        	
			readSB.delete(0, readSB.length());
			//readText.setText(readSB);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	String hexToAscii(String s) throws IllegalArgumentException
	{
		int n = s.length();
		StringBuilder sb = new StringBuilder(n / 2);
		for (int i = 0; i < n; i += 2)
		{
			char a = s.charAt(i);
			char b = s.charAt(i + 1);
			sb.append((char) ((hexToInt(a) << 4) | hexToInt(b)));
		}
		return sb.toString();
	}

	static int hexToInt(char ch)
	{
		if ('a' <= ch && ch <= 'f') { return ch - 'a' + 10; }
		if ('A' <= ch && ch <= 'F') { return ch - 'A' + 10; }
		if ('0' <= ch && ch <= '9') { return ch - '0'; }
		throw new IllegalArgumentException(String.valueOf(ch));
	}

	String decToAscii(String s) throws IllegalArgumentException
	{	
		int n = s.length();
		boolean pause = false;
		StringBuilder sb = new StringBuilder(n / 2);
		for (int i = 0; i < n; i += 3)
		{
			char a = s.charAt(i);
			char b = s.charAt(i + 1);
			char c = s.charAt(i + 2);
			int val = decToInt(a)*100 + decToInt(b)*10 + decToInt(c);
			if(0 <= val && val <= 255)
			{
				sb.append((char) val);
			}
			else
			{
				pause = true;
				break;
			}
		}

		if(false == pause)
			return sb.toString();
		throw new IllegalArgumentException("ex_b");
	}

	static int decToInt(char ch)
	{
		if ('0' <= ch && ch <= '9') { return ch - '0'; }
		throw new IllegalArgumentException("ex_a");
	}

	void msgToast(String str, int showTime)
	{
		Toast.makeText(global_context, str, showTime).show();
	}


	public void appendData(char[] data, int len)
	{
		if(len >= 1)    		
			readSB.append(String.copyValueOf(data, 0, len));

		switch(inputFormat)
		{
		case FORMAT_HEX:
		{
			char[] ch = readSB.toString().toCharArray();
			String temp;
			StringBuilder tmpSB = new StringBuilder();
			for(int i = 0; i < ch.length; i++)
			{
				temp = String.format("%02x", (int) ch[i]);

				if(temp.length() == 4)//4//from 10
				{
					tmpSB.append(temp.substring(2, 4));//2,4//from 10
				}
				else
				{
					tmpSB.append(temp);
				}

				if(i+1 < ch.length)
				{
					tmpSB.append(" ");	
				}
			}    			
			tmpSB.delete(0, tmpSB.length());
		}
		break;

		case FORMAT_DEC:
		{
			char[] ch = readSB.toString().toCharArray();
			String temp;
			StringBuilder tmpSB = new StringBuilder();
			for(int i = 0; i < ch.length; i++)
			{   				
				temp = Integer.toString((int)(ch[i] & 0xff));

				for(int j = 0; j < (3 - temp.length()); j++)
				{
					tmpSB.append("0");
				}
				tmpSB.append(temp);

				if(i+1 < ch.length)
				{
					tmpSB.append(" ");	
				}
			}    			
			tmpSB.delete(0, tmpSB.length());    			
		}
		break;

		case FORMAT_ASCII:    		
		default:
			//readText.setText(readSB);
			break;
		}
	}
	public void autoButtonfunc()
	{
		uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
		StringBuilder sb =new StringBuilder();
		FileOutputStream fos1=null;
		String Stringpath = Environment.getExternalStorageDirectory().getPath()+"/Download" ;
		File Filepath = new File(Stringpath);
		if(!Filepath.exists())//Check directory Exists or not 
		{
			Filepath.mkdirs();//Not Exists Create Directory
		}		

		try {
			fos1 = new FileOutputStream(Stringpath + "/L&T_Output.txt");
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Get OutputStream of New Directory Path

		try
		{

			byte writebuf1[] = new byte[]{47};

			try 
			{
				String swrite=new String(writebuf1);
			} 
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//data 1 send
			uartInterface.SendData(1, writebuf1);			
			uartInterface.SendData(1, writebuf1);
			uartInterface.SendData(1, writebuf1);
			uartInterface.SendData(1, writebuf1);	

		}

		catch (Exception e)
		{
			// TODO Auto-generated catch block
			Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT1" ); 
			startActivity(i);
		}		

		try
		{			
			String sread= new String(readBuffer);	
			Arrays.fill(readBuffer, (byte)0);//clear buffer


			//send 2			
			if(sread.length() > 0)
			{
				//Send 2							
				byte writebuf2[] = new byte[]{47};
				byte sendbuffernew[] = new byte[]{47,63,33,13,10,94,106};	

				for (int i = 0; i < 7; i++) 
				{
					addtime = Calendar.getInstance().getTimeInMillis() + 100;//145
					while(addtime > Calendar.getInstance().getTimeInMillis())  
					{										

					}
					writebuf2[0] = sendbuffernew[i];								
					uartInterface.SendData(1,writebuf2);
				}
			}
			else
			{
				Toast.makeText(UARTLoopbackActivity.this, "Response 1 not received ", Toast.LENGTH_LONG).show();
				//return;
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT2" ); 
			startActivity(i);
		}	
		//send 3
		try
		{
			/***naveen*********/
			addtime = Calendar.getInstance().getTimeInMillis() + 1000;//wait for 1 second
			while(addtime > Calendar.getInstance().getTimeInMillis())  
			{										

			}
			//2nd response
			String sread = new String(readBuffer);	
			if(sread.toString().contains("/"))
			{

				//sb.append(sread);//not require for appending 2nd response
				//Toast.makeText(UARTLoopbackActivity.this, "Read 2 " + sb, Toast.LENGTH_LONG).show();

			}
			else
			{
				//Arrays.fill(readBuffer,(byte)0);//clear buffer
				//sb.delete(0, sb.length());//clear sb
				//autoButtonfunc();				
			}


			//send data 3
			if(sread.toString().length() > 10)	
			{
				byte writebuf3[] = new byte[]{47};
				byte sendbuffernew2[] = new byte[]{6,57,48,49,13,10,94,106};
				//sendData(1,sendbuffernew2);
				for (int i = 0; i < 8; i++)
				{
					addtime = Calendar.getInstance().getTimeInMillis() + 100;
					while(addtime > Calendar.getInstance().getTimeInMillis())  
					{										

					}

					writebuf3[0] = sendbuffernew2[i];							
					uartInterface.SendData(1,writebuf3);
				}
				//Toast.makeText(UARTLoopbackActivity.this, "Data 3 sent ", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(UARTLoopbackActivity.this, "Response 2 not received ", Toast.LENGTH_LONG).show();
				//return;
			}
		}
		catch (Exception e)
		{							
			Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT3" ); 
			startActivity(i);
		}


		try
		{			
			int c = 0;
			/*addtime = Calendar.getInstance().getTimeInMillis() + 1000; //Wait 1 seconds for response
			while(Calendar.getInstance().getTimeInMillis() > addtime)   //Response 3 Received
			{						

			}
			//response 3

			String sread = new String(readBuffer,"UTF-8");*/

			/***********11-03-2016**********/
			String sread = null;
			addtime = Calendar.getInstance().getTimeInMillis() + 2000;
			do{
				if(addtime < Calendar.getInstance().getTimeInMillis())
					break;
				sread = new String(readBuffer,"UTF-8");
				Thread.sleep(50);
			}while(!sread.contains("!"));
			/*********End 11-03-2016************/

			if(sread.contains("!"))
			{
				sb.append(sread);

				//Toast.makeText(UARTLoopbackActivity.this, "Read 3 " + sb, Toast.LENGTH_LONG).show();	

				fos1.write(sb.toString().getBytes()); //write into the file
				fos1.flush();
				fos1.close();
				Billing_LandT.isFromBilling=false; //05-09-2015
				//Added 11-02-2021
				try
				{

					EncryptDecrypt en = new EncryptDecrypt("nsoft987");										
					final String BillingTest = en.encryptData(sb.toString(),IMEINumber ,"L&T");

				}
				catch(Exception e)
				{
					Log.d(" "," ");
				}	
				Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
				i.putExtra("Key","LT" ); 
				startActivity(i);

			}
			else if (count1<=3) 
			{
				count1++;
				Arrays.fill(readBuffer,(byte)0);//clear buffer
				sb.delete(0, sb.length());//clear sb
				//readBuffer = new byte[65536];
				autoButtonfunc();
			}
			else
			{
				Toast.makeText(UARTLoopbackActivity.this, "Unable To Fetch Data. Please take Reading Manually " + sb, Toast.LENGTH_LONG).show();	
				Intent i = new Intent(UARTLoopbackActivity.this, HomePage.class);
				//i.putExtra("Key","LT6");
				startActivity(i);
			}
		}
		catch (Exception e)
		{							
			Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT4");
			startActivity(i);
		}	

	}
	//04-07-2016
		/*public void autoButtonfuncAllLT()
		{
			uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
			StringBuilder sb =new StringBuilder();
			FileOutputStream fos1=null;
			String Stringpath = Environment.getExternalStorageDirectory().getPath()+"/Download" ;
			File Filepath = new File(Stringpath);
			if(!Filepath.exists())//Check directory Exists or not 
			{
				Filepath.mkdirs();//Not Exists Create Directory
			}		

			try {
				fos1 = new FileOutputStream(Stringpath + "/L&T_Output.txt");
			} 
			catch (FileNotFoundException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//Get OutputStream of New Directory Path

			try
			{

				byte writebuf1[] = new byte[]{47};

				try 
				{
					String swrite=new String(writebuf1);
				} 
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//data 1 send
				uartInterface.SendData(1, writebuf1);			
				uartInterface.SendData(1, writebuf1);
				uartInterface.SendData(1, writebuf1);
				uartInterface.SendData(1, writebuf1);	

			}

			catch (Exception e)
			{
				// TODO Auto-generated catch block
				Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
				i.putExtra("Key","LT1" ); 
				startActivity(i);
			}		

			try
			{			
				String sread= new String(readBuffer,"UTF-8");	
				Arrays.fill(readBuffer, (byte)0);//clear buffer


				//send 2			
				if(sread.length() > 0)
				{
					//Send 2							
					byte writebuf2[] = new byte[]{47};
					byte sendbuffernew[] = new byte[]{47,63,33,13,10,94,106};	

					for (int i = 0; i < 7; i++) 
					{
						addtime = Calendar.getInstance().getTimeInMillis() + 100;//145
						while(addtime > Calendar.getInstance().getTimeInMillis())  
						{										

						}
						writebuf2[0] = sendbuffernew[i];								
						uartInterface.SendData(1,writebuf2);
					}
				}
				else
				{
					Toast.makeText(UARTLoopbackActivity.this, "Response 1 not received ", Toast.LENGTH_LONG).show();
					//return;
				}
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
				i.putExtra("Key","LT2" ); 
				startActivity(i);
			}	
			//send 3
			try
			{
				*//***naveen*********//*
				addtime = Calendar.getInstance().getTimeInMillis() + 1000;//wait for 1 second
				while(addtime > Calendar.getInstance().getTimeInMillis())  
				{										

				}
				//2nd response
				String sread = new String(readBuffer,"UTF-8");
				Arrays.fill(readBuffer, (byte)0);//clear buffer
				if(sread.toString().contains("/"))
				{

					//sb.append(sread);//not require for appending 2nd response
					//Toast.makeText(UARTLoopbackActivity.this, "Read 2 " + sb, Toast.LENGTH_LONG).show();

				}
				else
				{
					//Arrays.fill(readBuffer,(byte)0);//clear buffer
					//sb.delete(0, sb.length());//clear sb
					//autoButtonfunc();				
				}


				//send data 3
				if(sread.toString().length() > 10)	
				{
					byte writebuf3[] = new byte[]{47};
					//byte sendbuffernew2[] = new byte[]{6,57,48,49,13,10,94,106};//Instantaneous
					byte sendbuffernew2[] = new byte[]{6,48,52,48,13,10,94,106};//All parameters
					//sendData(1,sendbuffernew2);
					for (int i = 0; i < 8; i++)
					{
						addtime = Calendar.getInstance().getTimeInMillis() + 100;
						while(addtime > Calendar.getInstance().getTimeInMillis())  
						{										

						}

						writebuf3[0] = sendbuffernew2[i];							
						uartInterface.SendData(1,writebuf3);
					}
					//Toast.makeText(UARTLoopbackActivity.this, "Data 3 sent ", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(UARTLoopbackActivity.this, "Response 2 not received ", Toast.LENGTH_LONG).show();
					//return;
				}
			}
			catch (Exception e)
			{							
				Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
				i.putExtra("Key","LT3" ); 
				startActivity(i);
			}


			try
			{			
				int c = 0;
				addtime = Calendar.getInstance().getTimeInMillis() + 1000; //Wait 1 seconds for response
				while(Calendar.getInstance().getTimeInMillis() > addtime)   //Response 3 Received
				{						

				}
				//response 3

				String sread = new String(readBuffer,"UTF-8");

				*//***********11-03-2016**********//*
				String sread = null;
				addtime = Calendar.getInstance().getTimeInMillis() + 10000;
				do{
					if(addtime < Calendar.getInstance().getTimeInMillis())
						break;
					sread = new String(readBuffer,"UTF-8");
					Thread.sleep(50);
				}while(!sread.contains("!"));
				*//*********End 11-03-2016************//*

				Arrays.fill(readBuffer, (byte)0);//clear buffer
				if(sread.contains("!"))
				{
					Toast.makeText(UARTLoopbackActivity.this, "All Data " + FT311UARTInterface.tempsread, Toast.LENGTH_LONG).show();
					//Toast.makeText(UARTLoopbackActivity.this, "All Data " + sread, Toast.LENGTH_LONG).show();
					sb.append(FT311UARTInterface.tempsread);

					//Toast.makeText(UARTLoopbackActivity.this, "Read 3 " + sb, Toast.LENGTH_LONG).show();	

					fos1.write(sb.toString().getBytes()); //write into the file
					fos1.flush();
					fos1.close();
					Billing_LandT.isFromBilling=false; //05-09-2015
					Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
					i.putExtra("Key","LT" ); 
					startActivity(i);

				}
				else if (count1<=3) 
				{
					count1++;
					Arrays.fill(readBuffer,(byte)0);//clear buffer
					sb.delete(0, sb.length());//clear sb
					//readBuffer = new byte[65536];
					autoButtonfuncAllLT();
				}
				else
				{
					Toast.makeText(UARTLoopbackActivity.this, "Unable To Fetch Data. Please take Reading Manually " + sb, Toast.LENGTH_LONG).show();	
					Intent i = new Intent(UARTLoopbackActivity.this, HomePage.class);
					//i.putExtra("Key","LT6");
					startActivity(i);
				}
			}
			catch (Exception e)
			{							
				Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
				i.putExtra("Key","LT4");
				startActivity(i);
			}	

		}*/
	
	//New 04-09-2017
	public void autoButtonfuncAllLT()  //This method will work for 2009,2010,2015 and other L&T meters also
	{
		uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
		StringBuilder sb = new StringBuilder();
		FileOutputStream fos1=null;
		String Stringpath = Environment.getExternalStorageDirectory().getPath()+"/Download";
		File Filepath = new File(Stringpath);
		if(!Filepath.exists())//Check directory Exists or not
		{
			Filepath.mkdirs();//Not Exists Create Directory
		}		

		try {
			fos1 = new FileOutputStream(Stringpath + "/L&T_Output.txt");
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Get OutputStream of New Directory Path

		try
		{

			byte writebuf1[] = new byte[]{58};

			try 
			{
				String swrite=new String(writebuf1);
			} 
			catch (Exception e)
			{
				//TODO Auto-generated catch block
				e.printStackTrace();
			}
			//data 1 send
			uartInterface.SendData(1, writebuf1);			
			uartInterface.SendData(1, writebuf1);
			uartInterface.SendData(1, writebuf1);
			uartInterface.SendData(1, writebuf1);	

		}

		catch (Exception e)
		{
			// TODO Auto-generated catch block
			Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT1" ); 
			startActivity(i);
		}		

		try
		{
			addtime = Calendar.getInstance().getTimeInMillis() + 500;//145
			while(addtime > Calendar.getInstance().getTimeInMillis())
			{

			}

			//String sread= new String(readBuffer,"UTF-8");
			//Arrays.fill(readBuffer, (byte)0);//clear buffer


			int retVal = 0;
			addtime = Calendar.getInstance().getTimeInMillis() + 2000;//145
			do{
				if(addtime < Calendar.getInstance().getTimeInMillis())
					break;
				Arrays.sort(readBuffer);
				retVal = Arrays.binarySearch(readBuffer, (byte)21);
				Thread.sleep(100);
			}while(!(retVal >= 0));
			Arrays.fill(readBuffer, (byte)0);//clear buffer

			//send 2			
			if(retVal >= 0)
			{
				Toast.makeText(UARTLoopbackActivity.this, "GOT 1st RESPONSE : " + String.valueOf(retVal), Toast.LENGTH_SHORT).show();

				//Send 2							
				byte writebuf2[] = new byte[]{47};
				byte sendbuffernew[] = new byte[]{47,63,33,13,10};	

				for (int i = 0; i < 5; i++) 
				{
					addtime = Calendar.getInstance().getTimeInMillis() + 100;//145
					while(addtime > Calendar.getInstance().getTimeInMillis())  
					{										

					}
					writebuf2[0] = sendbuffernew[i];								
					uartInterface.SendData(1,writebuf2);
				}

				/*******************Pavan added on 06-01-2017*********************/
				//send 3
				try
				{
					/***naveen*********/
					addtime = Calendar.getInstance().getTimeInMillis() + 1000; //wait for 1 second
					while(addtime > Calendar.getInstance().getTimeInMillis())  
					{										

					}
					//2nd response
					String sread = new String(readBuffer,"UTF-8");
					Arrays.fill(readBuffer, (byte)0);//clear buffer

					//send data 3
					if(sread.length() > 0)	
					{
						Toast.makeText(UARTLoopbackActivity.this, "GOT 2nd RESPONSE : " + sread, Toast.LENGTH_SHORT).show();
						byte writebuf3[] = new byte[]{47};
						byte sendbuffernew2[] = new byte[]{6,48,52,48,13,10};//All parameters   Others

						for (int i = 0; i < 6; i++)
						{
							addtime = Calendar.getInstance().getTimeInMillis() + 100;
							while(addtime > Calendar.getInstance().getTimeInMillis())  
							{										

							}
							writebuf3[0] = sendbuffernew2[i];							
							uartInterface.SendData(1,writebuf3);
						}

						/*******************Pavan added on 06-01-2017*********************/
						try
						{			
							int c = 0;
							/***********11-03-2016**********/
							String sread3 = null;
							addtime = Calendar.getInstance().getTimeInMillis() + 20000;
							do{
								if(addtime < Calendar.getInstance().getTimeInMillis())
									break;
								sread3 = new String(readBuffer,"UTF-8");
								Thread.sleep(100);
							}while(!sread3.contains("!"));
							/*********End 11-03-2016************/

							/*sb.append(FT311UARTInterface.tempsread);
							sb.append(sread3);
							fos1.write(sb.toString().getBytes()); //write into the file
							fos1.flush();
							fos1.close();*/

							Arrays.fill(readBuffer, (byte)0); //clear buffer
							if(sread3.contains("!"))
							{
								Toast.makeText(UARTLoopbackActivity.this, "All Data " + FT311UARTInterface.tempsread, Toast.LENGTH_LONG).show();
								//Toast.makeText(UARTLoopbackActivity.this, "All Data " + sread, Toast.LENGTH_LONG).show();
								sb.append(FT311UARTInterface.tempsread);

								//Toast.makeText(UARTLoopbackActivity.this, "Read 3 " + sb, Toast.LENGTH_LONG).show();	

								fos1.write(sb.toString().getBytes()); //write into the file
								fos1.flush();
								fos1.close();
								Billing_LandT.isFromBilling=false; 
								Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
								i.putExtra("Key","LT");
								startActivity(i);
							}
							else
							{
								Toast.makeText(UARTLoopbackActivity.this, "Response 3 not received ", Toast.LENGTH_LONG).show();
								return;
							}
						}
						catch (Exception e)
						{							
							Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
							i.putExtra("Key","LT4");
							startActivity(i);
						}
						/*******************Pavan added on 06-01-2017*********************/
					}
					else
					{
						Toast.makeText(UARTLoopbackActivity.this, "Response 2 not received ", Toast.LENGTH_LONG).show();
						return;
					}
				}
				catch (Exception e)
				{							
					Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
					i.putExtra("Key","LT3"); 
					startActivity(i);
				}
				/*******************Pavan added on 06-01-2017*********************/
			}
			else if (count1<3) 
			{
				count1++;
				Arrays.fill(readBuffer,(byte)0);//clear buffer
				sb.delete(0, sb.length());//clear sb
				autoButtonfuncAllLT();
			}
			else
			{				

				Toast.makeText(UARTLoopbackActivity.this, "Unable To Fetch Data. Please take Reading Manually " + sb, Toast.LENGTH_LONG).show();	
				Intent i = new Intent(UARTLoopbackActivity.this, HomePage.class);
				//i.putExtra("Key","LT6");
				startActivity(i);			
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT2" ); 
			startActivity(i);
		}
	}
	public void autoButtonfuncLG()
	{	
		String mFilepath = Environment.getExternalStorageDirectory().getPath()+"/Download/L&G_Output.txt";
		File f = new File(mFilepath);	
		//File to = new File(mFilepath)
		//f.renameTo(newPath)
		if(f.exists())
			f.delete();

		uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
		StringBuilder sb =new StringBuilder();
		FileOutputStream fos1=null;
		String Stringpath = Environment.getExternalStorageDirectory().getPath()+"/Download" ;
		File Filepath = new File(Stringpath);
		if(!Filepath.exists())//Check directory Exists or not 
		{
			Filepath.mkdirs();//Not Exists Create Directory
		}		

		try {
			fos1 = new FileOutputStream(Stringpath + "/L&G_Output.txt");
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Get OutputStream of New Directory Path

		try
		{	
			//Sending 1st request
			byte writebuf2[] = new byte[1];
			byte sendbuffernew[] = new byte[]{0x2F,0x3F,0x21,0x0D,0x0A};  //{47,63,33,13,10};
			for (int i = 0; i < 5; i++) 
			{
				/*addtime = Calendar.getInstance().getTimeInMillis() + 100;//145
					while(addtime > Calendar.getInstance().getTimeInMillis())
					{										

					}*/
				writebuf2[0] = sendbuffernew[i];
				uartInterface.SendData(1,writebuf2,(i + 1));
			}			
		}
		catch (Exception e) {			
			// TODO Auto-generated catch block
			Toast.makeText(UARTLoopbackActivity.this,"Error While Sending 1st Request.Retry again.", Toast.LENGTH_LONG).show();
			//Intent i = new Intent(UARTLoopbackActivity.this, LoginActivity.class);
			Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT2");
			startActivity(i);
		}	
		//Reading 1st response
		try
		{

			addtime = Calendar.getInstance().getTimeInMillis() + 2000;//2 second delay
			while(addtime > Calendar.getInstance().getTimeInMillis())
			{										

			}
			String sread = new String(readBuffer,"UTF-8");
			Arrays.fill(readBuffer,(byte)0);//clear buffer
			/*Toast.makeText(UARTLoopbackActivity.this,"FT311UARTInterface 1 : " + FT311UARTInterface.tempsread, Toast.LENGTH_LONG).show();
			Toast.makeText(UARTLoopbackActivity.this,"1st Response : "+sread, Toast.LENGTH_LONG).show();*/

			if(sread.toString().contains("/"))		//try getting the byte value of readbuffer for '/' - 47
			{
				//Toast.makeText(UARTLoopbackActivity.this,"Got 1st response.", Toast.LENGTH_LONG).show();
				count1 = 0;
				//sb.append(sread.substring(0, 23));//not require for appending 2nd response
				sb.append(sread);
				//Sending 2nd Request
				byte writebuf3[] = new byte[1];
				byte sendbuffernew2[] = new byte[]{0x06,0x30,0x30,0x30,0x0D,0x0A}; //{6,48,53,48,13,10};
				for (int i = 0; i < 6; i++)
				{
					/*addtime = Calendar.getInstance().getTimeInMillis() + 100;
					while(addtime > Calendar.getInstance().getTimeInMillis())
					{										

					}*/
					writebuf3[0] = sendbuffernew2[i];							
					uartInterface.SendData(1,writebuf3,(i + 1)); 
				}				
				Arrays.fill(readBuffer,(byte)0);//clear buffer


				try
				{	
					//Reading 2nd Response
					/*addtime = Calendar.getInstance().getTimeInMillis() + 10000;//Wait 10 seconds for response
					while(Calendar.getInstance().getTimeInMillis() > addtime) //Response 2 Received
					{						

					}*/
					addtime = Calendar.getInstance().getTimeInMillis() + 20000;//Wait 20 seconds for response
					//Toast.makeText(UARTLoopbackActivity.this,"2nd Request Sent.", Toast.LENGTH_LONG).show();
					//String sread = null;
					do{
						//If C.50.1.01 tag didn't received in 20sec after sending 2nd command break the loop.
						if(addtime < Calendar.getInstance().getTimeInMillis())
						{
							Toast.makeText(UARTLoopbackActivity.this,"Connection Timeout.Retry again.", Toast.LENGTH_LONG).show();
							break;
						}
						//sread = new String(readBuffer,"UTF-8");
						sread = FT311UARTInterface.tempsread;
						Thread.sleep(100);
					}while(!sread.contains("C.50.1.01") || !sread.contains("C.50.3.01")); //C.50.1.01

					//Toast.makeText(UARTLoopbackActivity.this,"SREAD -" + sread, Toast.LENGTH_LONG).show();
					/*Toast.makeText(UARTLoopbackActivity.this,"L&G Response : " + FT311UARTInterface.tempsread, Toast.LENGTH_LONG).show();
					sread = null;
					sread = FT311UARTInterface.tempsread;
					Toast.makeText(UARTLoopbackActivity.this,"Got 2nd response : " + sread, Toast.LENGTH_LONG).show();*/

					if(sread.contains("C.50.1.01") || sread.contains("C.50.3.01"))
					{
						count2 = 0;
						sb.append(sread);
						fos1.write(sb.toString().getBytes()); //write into the file
						fos1.flush();
						fos1.close();
						//LoginActivity.isRead = true;
						//Start 18-11-2016 to WriteLog
						try
						{

							EncryptDecrypt en = new EncryptDecrypt("nsoft987");										
							final String BillingTest = en.encryptData(sb.toString(),IMEINumber ,"L&G");

						}
						catch(Exception e)
						{
							Log.d(" "," ");
						}	
						Arrays.fill(readBuffer,(byte)0);
						//End 18-11-2016 to WriteLog
						Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
						i.putExtra("Key","LT" ); 
						startActivity(i);

					}
					else if (count2<3) 	// use other variable for count - count2
					{						
						count2++;
						//Toast.makeText(UARTLoopbackActivity.this, "Response 2 not received ", Toast.LENGTH_LONG).show();
						Arrays.fill(readBuffer,(byte)0);//clear buffer
						sb.delete(0, sb.length());//clear sb
						//readBuffer = new byte[65536];
						//autoButtonfuncLG();

					}
					else
					{						
						Toast.makeText(UARTLoopbackActivity.this,"Unable to read data from the meter.Take reading Manually.", Toast.LENGTH_LONG).show();
						//Start 18-11-2016 to WriteLog
						try
						{

							EncryptDecrypt en = new EncryptDecrypt("nsoft987");										
							final String BillingTest = en.encryptData(sb.toString(),IMEINumber ,"L&G");

						}
						catch(Exception e1)
						{
							Log.d(" "," ");
						}
						//End 18-11-2016 to WriteLog
						Intent i = new Intent(UARTLoopbackActivity.this, HomePage.class);
						//i.putExtra("Key","LT6" ); 
						startActivity(i);
					}                                                                               

				}
				catch (Exception e)
				{					
					//Intent i = new Intent(UARTLoopbackActivity.this, LoginActivity.class);
					Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
					Toast.makeText(UARTLoopbackActivity.this, "LT4 Exec : " + e.toString(), Toast.LENGTH_LONG).show();
					i.putExtra("Key","LT4" );
					startActivity(i);
				}
			}
			else if(count1 < 3)
			{

				//Toast.makeText(UARTLoopbackActivity.this,"Sending 1st Request " + String.valueOf(count1) + "time.", Toast.LENGTH_LONG).show();
				count1++;
				Arrays.fill(readBuffer,(byte)0);//clear buffer
				sb.delete(0, sb.length());//clear sb
				autoButtonfuncLG();				
			}
			else{
				count1 = 0;
				Toast.makeText(UARTLoopbackActivity.this,"Unable to read data from the meter.Take reading Manually.", Toast.LENGTH_LONG).show();
				Intent i = new Intent(UARTLoopbackActivity.this, HomePage.class);
				//i.putExtra("Key","LT3" ); 
				startActivity(i);
			}
		}
		catch (Exception e)
		{
			Toast.makeText(UARTLoopbackActivity.this,"Error While Sending 2nd Request.Retry again.", Toast.LENGTH_LONG).show();
			//Intent i = new Intent(UARTLoopbackActivity.this, LoginActivity.class);
			Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT3" ); 
			startActivity(i);
		}
	}
	public void autoButtonfunc_DLMS_1_Phase()
	{
		String mFilepath = Environment.getExternalStorageDirectory().getPath()+"/Download/DLMS_Output.txt";
		File f = new File(mFilepath);	
		//File to = new File(mFilepath)
		//f.renameTo(newPath)
		if(f.exists())
			f.delete();
		//Nitish For LogData 28-11-2016
		String writeData = "";		
		uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
		StringBuilder sb =new StringBuilder();
		FileOutputStream fos1=null;
		String Stringpath = Environment.getExternalStorageDirectory().getPath()+"/Download" ;		
		File Filepath = new File(Stringpath);
		
		FileOutputStream fos2=null;
		
		String sRoot = Environment.getExternalStorageDirectory().getPath();
		String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
		String currentdatetime = new SimpleDateFormat("ddMMyyyyHHmmss").format(Calendar.getInstance().getTime());
		String StringpathLog = sRoot+"/OpticalBilling/"+timeStamp+"/"+"Secure-1Phase";
		File filepathLog = new File(StringpathLog);
		
		byte[] readbytes = null;
		int SevenECount = 0;
		if(!Filepath.exists())//Check directory Exists or not 
		{
			Filepath.mkdirs();//Not Exists Create Directory
		}
		if(!filepathLog.exists())//Check directory Exists or not 
		{
			filepathLog.mkdirs();//Not Exists Create Directory
		}

		try {
			fos1 = new FileOutputStream(Stringpath + "/DLMS_Output.txt",true);
			fos2 = new FileOutputStream(StringpathLog + "/Secure-1Phase_Log_" +IMEINumber+ "_" +currentdatetime+".txt",true);
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Get OutputStream of New Directory Path

		try
		{	
			for(int ReqCount = 0;ReqCount < DLMSRequestArray_Saral_DLMS.length;ReqCount++)
			{
				int lc = 0;
				int a = 0;
				fos2.write(("1 ").getBytes());
				//db.insertLOGDATA("I", "Saral", "1");
				byte writebuf[] = new byte[1];
				String[] StringSplit = DLMSRequestArray_Saral_DLMS[ReqCount].split(",");
				int[] ConverttoInt = new int[StringSplit.length];
				byte[] ConverttoByte = new byte[StringSplit.length];
				for(int i = 0;i<StringSplit.length;i++)
				{
					ConverttoInt[i] = Integer.parseInt(StringSplit[i],16);
					ConverttoByte[i] = (byte)(ConverttoInt[i]);
				}
				for (int j = 0; j < ConverttoByte.length; j++)
				{
					writebuf[0] = ConverttoByte[j];							
					uartInterface.SendData(1,writebuf,(j + 1));
				}
				//Toast.makeText(UARTLoopbackActivity.this, String.valueOf(ReqCount) + "Sent", Toast.LENGTH_LONG).show();
				addtime = Calendar.getInstance().getTimeInMillis() + 4000;//4 second delay
				while(addtime > Calendar.getInstance().getTimeInMillis())
				{										

				}
				readbytes = readBuffer;
				if(readbytes.length > 1)
				{
					lc = 0;
					for(int k = 0;k<=readbytes.length-1;k++)
					{
						fos2.write(("2 ").getBytes());
						//db.insertLOGDATA("I", "Saral", "2");
						String SByte2 = String.format(" %02X",readbytes[k]);
						//if(SByte2.equals(" 7E"))
						if(SByte2.contains("7E"))
						{
							fos2.write(("3 ").getBytes());
							SevenECount++;
						}
						if(SevenECount > 2)//Writing only response
						{							
							fos2.write(("4 ").getBytes());
							if(SevenECount == 3 && lc == 0)
							{
								a = readbytes[k+2];
								if(a == 0)
									break;
							}
							lc = lc + 1;
							//db.insertLOGDATA("I", "Saral", "4");
							fos1.write(SByte2.toString().getBytes());
							writeData = writeData + SByte2.toString(); //Nitish 28-11-2016 For Backup Log
						}
						if(SevenECount == 4 || lc == a+2)
						{
							fos2.write(("5 ").getBytes());
							//db.insertLOGDATA("I", "Saral", "5");
							String newline = String.format("%s", "\r\n");
							fos1.write(newline.toString().getBytes());
							writeData = writeData + newline.toString(); //Nitish 28-11-2016 For Backup Log		
							SevenECount = 0;
							lc = 0;
							a = 0;
							break;
						}
						/*else if(SevenECount == 2)
						{
							String newline = String.format("%s", "\r\n");
							fos1.write(newline.toString().getBytes());
						}*/
					}
					fos2.write(("6 ").getBytes());
					//db.insertLOGDATA("I", "Saral", "6");
					Arrays.fill(readbytes,(byte)0);
				}
				else if(count1 < 3)
				{
					fos2.write(("7 ").getBytes());
					//db.insertLOGDATA("I", "Saral", "7");
					count1++;
					Arrays.fill(readBuffer,(byte)0);//clear buffer
					sb.delete(0, sb.length());//clear sb
					autoButtonfunc_DLMS_1_Phase();
				}
				else{
					fos2.write(("8 ").getBytes());
					//db.insertLOGDATA("I", "Saral", "8");
					count1 = 0;
					Toast.makeText(UARTLoopbackActivity.this,"Unable to read data from the meter.Take reading Manually.", Toast.LENGTH_LONG).show();
					Intent i = new Intent(UARTLoopbackActivity.this, HomePage.class);
					i.putExtra("Key","LT3");
					startActivity(i);
				}
			}
			fos2.write(("9 ").getBytes());
			//db.insertLOGDATA("I", "Saral", "9");
			Arrays.fill(readBuffer,(byte)0);
			Toast.makeText(UARTLoopbackActivity.this,"Data Read Completed.", Toast.LENGTH_LONG).show();
			Arrays.fill(readbytes,(byte)0);
			fos1.flush();
			fos1.close();	
			fos2.flush();
			fos2.close();
			//Start 28-11-2016 to WriteLog
			try
			{

				EncryptDecrypt en = new EncryptDecrypt("nsoft987");										
				final String BillingTest = en.encryptData(writeData.toString(),IMEINumber ,"Secure-1Phase");

			}
			catch(Exception e1)
			{
				Log.d(" "," ");
			}
			//End 28-11-2016 to WriteLog
			Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT" ); 
			startActivity(i);
		}
		catch (Exception e) {
			//db.insertLOGDATA("I", "Saral", "10");
			Toast.makeText(UARTLoopbackActivity.this,e.toString(), Toast.LENGTH_LONG).show();
			Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT2");
			startActivity(i);
		}	                   
	}
	public void autoButtonfunc_DLMS_1_Phase_iCredit()
	{
		String mFilepath = Environment.getExternalStorageDirectory().getPath()+"/Download/DLMS_Output.txt";
		File f = new File(mFilepath);	
		//File to = new File(mFilepath)
		//f.renameTo(newPath)
		if(f.exists())
			f.delete();
		//Nitish For LogData 28-11-2016
		String writeData = "";		
		uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
		StringBuilder sb =new StringBuilder();
		FileOutputStream writefile=null;
		FileOutputStream writelength=null;
		String Stringpath = Environment.getExternalStorageDirectory().getPath()+"/Download" ;
		String sRoot = Environment.getExternalStorageDirectory().getPath();
		String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
		String currentdatetime = new SimpleDateFormat("ddMMyyyyHHmmss").format(Calendar.getInstance().getTime());
		String StringpathLog = sRoot+"/OpticalBilling/"+timeStamp+"/"+"DLMS-ICredit";
		
		File Filepath = new File(Stringpath);
		File filepathLog = new File(StringpathLog);
		byte[] readbytes = null;
		int SevenECount = 0;
		
		if(!Filepath.exists())//Check directory Exists or not 
		{
			Filepath.mkdirs();//Not Exists Create Directory
		}	
		if(!filepathLog.exists())//Check directory Exists or not 
		{
			filepathLog.mkdirs();//Not Exists Create Directory
		}

		try {
			writefile = new FileOutputStream(Stringpath + "/DLMS_Output.txt",true);
			writelength =new FileOutputStream(StringpathLog + "/DLMS-ICredit_Log_" +IMEINumber+ "_" +currentdatetime+".txt",true);
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Get OutputStream of New Directory Path
		try
		{	
			for(int ReqCount = 0;ReqCount < DLMSRequestArrayICredit.length;ReqCount++)
			{
				int a = 0;
				int lc=0;
				SevenECount = 0;
				String s = null;
				byte writebuf[] = new byte[1];
				String[] StringSplit = DLMSRequestArrayICredit[ReqCount].split(",");
				int[] ConverttoInt = new int[StringSplit.length];
				byte[] ConverttoByte = new byte[StringSplit.length];
				for(int i = 0;i<StringSplit.length;i++)
				{
					ConverttoInt[i] = Integer.parseInt(StringSplit[i],16);
					ConverttoByte[i] = (byte)(ConverttoInt[i]);
				}
				for (int j = 0; j < ConverttoByte.length; j++)
				{
					writebuf[0] = ConverttoByte[j];							
					uartInterface.SendData(1,writebuf,(j + 1));
				}
				//Toast.makeText(UARTLoopbackActivity.this, String.valueOf(ReqCount) + "Sent", Toast.LENGTH_LONG).show();
				addtime = Calendar.getInstance().getTimeInMillis() + 3750;//4 second delay
				while(addtime > Calendar.getInstance().getTimeInMillis())
				{										

				}
				readbytes = readBuffer;
				if(readbytes.length > 1)
				{
					//s = "FRAME STARTED" + "\r\n";
					s="1 ";
					writelength.write(s.getBytes());
					for(int k = 0;k<=readbytes.length-1;k++)
					{
						String SByte2 = String.format(" %02X",readbytes[k]);
						lc=lc+1;

						//s = "lc : " + String.valueOf(lc) + "\r\n";
						s="2 ";
						writelength.write(s.getBytes());

						if(SByte2.contains("7E"))
						{							
							SevenECount++;
							writefile.write(SByte2.toString().getBytes());
							writeData = writeData + SByte2.toString(); //Nitish 28-11-2016 For Backup Log
							if(SevenECount == 1)
							{
								a = readbytes[k+2];
								s="3 ";
								//s = "GOT FRAME LENGHT:" + String.valueOf(a) + "\r\n";
								writelength.write(s.getBytes());
								if(a == 0)
									break;
							}
							s="4 ";
							//s = "GOT 7E : " + String.valueOf(lc) + " 7E COUNT " + String.valueOf(SevenECount) +  "\r\n";
							writelength.write(s.getBytes());
						}
						else if(lc == 1)
						{
							s="5 ";
							//s = "GOT 00 AS 1ST BYTE  K : " + String.valueOf(k) + "\r\n";
							writelength.write(s.getBytes());
							lc = 0;
							continue;
						}
						else{
							writefile.write(SByte2.toString().getBytes());
							writeData = writeData + SByte2.toString(); //Nitish 28-11-2016 For Backup Log
							s="6 ";
							//s = "READING BYTES  " + "7E COUNT : " + String.valueOf(SevenECount) + " lc : " +  String.valueOf(lc) + "\r\n";
							writelength.write(s.getBytes());
						}

						if(lc==a+2)						
						{
							s="7 ";
							//s = "GOT CLOSING TAG AT lc : " + String.valueOf(lc) + "7E COUNT : " + String.valueOf(SevenECount) + "\r\n";
							writelength.write(s.getBytes());

							String newline = String.format("%s", "\r\n");
							writefile.write(newline.toString().getBytes());
							SevenECount = 0;
							lc = 0;
							a = 0;
							writeData = writeData + newline.toString(); //Nitish 28-11-2016 For Backup Log							
							break;
						}										
					}
					Arrays.fill(readBuffer,(byte)0);
					Arrays.fill(readbytes,(byte)0);

					s="8 ";
					//s = "READING COMPLETED" + "\r\n";
					writelength.write(s.getBytes());

				}
				/*else if(count1 < 3)
				{

					count1++;
					Arrays.fill(readBuffer,(byte)0);//clear buffer
					sb.delete(0, sb.length());//clear sb
					//autoButtonfunc_DLMS_3_Phase();
				}*/
				else{

					count1 = 0;
					Toast.makeText(UARTLoopbackActivity.this,"Unable to read data from the meter.Take reading Manually.", Toast.LENGTH_LONG).show();
					Intent i = new Intent(UARTLoopbackActivity.this, HomePage.class);
					i.putExtra("Key","LT3");
					startActivity(i);
				}
			}
			Arrays.fill(readBuffer,(byte)0);
			Toast.makeText(UARTLoopbackActivity.this,"Data Read Completed.", Toast.LENGTH_LONG).show();
			Arrays.fill(readbytes,(byte)0);
			writefile.flush();
			writefile.close();	
			writelength.flush();
			writelength.close();	
			//Start 28-11-2016 to WriteLog
			try
			{
				EncryptDecrypt en = new EncryptDecrypt("nsoft987");										
				final String BillingTest = en.encryptData(writeData.toString(),IMEINumber ,"DLMS-ICredit");
			}
			catch(Exception e1)
			{
				Log.d(" "," ");
			}
			//End 28-11-2016 to WriteLog
			Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT"); 
			startActivity(i);
		}
		catch (Exception e) {
			Toast.makeText(UARTLoopbackActivity.this,e.toString(), Toast.LENGTH_LONG).show();
			Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT2");
			startActivity(i);
		}	              
	}
	public void autoButtonfunc_DLMS_LG()
	{
		String mFilepath = Environment.getExternalStorageDirectory().getPath()+"/Download/DLMS_Output.txt";
		File f = new File(mFilepath);	
		//File to = new File(mFilepath)
		//f.renameTo(newPath)
		if(f.exists())
			f.delete();
		//Nitish For LogData 28-11-2016
		String writeData = "";		
		uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
		StringBuilder sb =new StringBuilder();
		FileOutputStream writefile=null;
		FileOutputStream writelength=null;
		String Stringpath = Environment.getExternalStorageDirectory().getPath()+"/Download" ;
		String sRoot = Environment.getExternalStorageDirectory().getPath();
		String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
		String currentdatetime = new SimpleDateFormat("ddMMyyyyHHmmss").format(Calendar.getInstance().getTime());
		String StringpathLog = sRoot+"/OpticalBilling/"+timeStamp+"/"+"DLMS-L&G";
		
		File Filepath = new File(Stringpath);
		File filepathLog = new File(StringpathLog);
		byte[] readbytes = null;
		int SevenECount = 0;
		
		if(!Filepath.exists())//Check directory Exists or not 
		{
			Filepath.mkdirs();//Not Exists Create Directory
		}	
		if(!filepathLog.exists())//Check directory Exists or not 
		{
			filepathLog.mkdirs();//Not Exists Create Directory
		}

		try {
			writefile = new FileOutputStream(Stringpath + "/DLMS_Output.txt",true);
			writelength =new FileOutputStream(StringpathLog + "/DLMS-L&G_Log_" +IMEINumber+ "_" +currentdatetime+".txt",true);
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Get OutputStream of New Directory Path
		try
		{	
			for(int ReqCount = 0;ReqCount < DLMSRequestArrayLG.length;ReqCount++)
			{
				int a = 0;
				int lc=0;
				SevenECount = 0;
				String s = null;
				byte writebuf[] = new byte[1];
				String[] StringSplit = DLMSRequestArrayLG[ReqCount].split(",");
				int[] ConverttoInt = new int[StringSplit.length];
				byte[] ConverttoByte = new byte[StringSplit.length];
				for(int i = 0;i<StringSplit.length;i++)
				{
					ConverttoInt[i] = Integer.parseInt(StringSplit[i],16);
					ConverttoByte[i] = (byte)(ConverttoInt[i]);
				}
				for (int j = 0; j < ConverttoByte.length; j++)
				{
					writebuf[0] = ConverttoByte[j];							
					uartInterface.SendData(1,writebuf,(j + 1));
				}
				//Toast.makeText(UARTLoopbackActivity.this, String.valueOf(ReqCount) + "Sent", Toast.LENGTH_LONG).show();
				addtime = Calendar.getInstance().getTimeInMillis() + 3750;//4 second delay
				while(addtime > Calendar.getInstance().getTimeInMillis())
				{										

				}
				readbytes = readBuffer;
				if(readbytes.length > 1)
				{
					//s = "FRAME STARTED" + "\r\n";
					s="1 ";
					writelength.write(s.getBytes());
					for(int k = 0;k<=readbytes.length-1;k++)
					{
						String SByte2 = String.format(" %02X",readbytes[k]);
						lc=lc+1;

						//s = "lc : " + String.valueOf(lc) + "\r\n";
						s="2 ";
						writelength.write(s.getBytes());

						if(SByte2.contains("7E"))
						{							
							SevenECount++;
							writefile.write(SByte2.toString().getBytes());
							writeData = writeData + SByte2.toString(); //Nitish 28-11-2016 For Backup Log
							if(SevenECount == 1)
							{
								a = readbytes[k+2];
								s="3 ";
								//s = "GOT FRAME LENGHT:" + String.valueOf(a) + "\r\n";
								writelength.write(s.getBytes());
								if(a == 0)
									break;
							}
							s="4 ";
							//s = "GOT 7E : " + String.valueOf(lc) + " 7E COUNT " + String.valueOf(SevenECount) +  "\r\n";
							writelength.write(s.getBytes());
						}
						else if(lc == 1)
						{
							s="5 ";
							//s = "GOT 00 AS 1ST BYTE  K : " + String.valueOf(k) + "\r\n";
							writelength.write(s.getBytes());
							lc = 0;
							continue;
						}
						else{
							writefile.write(SByte2.toString().getBytes());
							writeData = writeData + SByte2.toString(); //Nitish 28-11-2016 For Backup Log
							s="6 ";
							//s = "READING BYTES  " + "7E COUNT : " + String.valueOf(SevenECount) + " lc : " +  String.valueOf(lc) + "\r\n";
							writelength.write(s.getBytes());
						}

						if(lc==a+2)						
						{
							s="7 ";
							//s = "GOT CLOSING TAG AT lc : " + String.valueOf(lc) + "7E COUNT : " + String.valueOf(SevenECount) + "\r\n";
							writelength.write(s.getBytes());

							String newline = String.format("%s", "\r\n");
							writefile.write(newline.toString().getBytes());
							SevenECount = 0;
							lc = 0;
							a = 0;
							writeData = writeData + newline.toString(); //Nitish 28-11-2016 For Backup Log							
							break;
						}										
					}
					Arrays.fill(readBuffer,(byte)0);
					Arrays.fill(readbytes,(byte)0);

					s="8 ";
					//s = "READING COMPLETED" + "\r\n";
					writelength.write(s.getBytes());

				}
				/*else if(count1 < 3)
				{

					count1++;
					Arrays.fill(readBuffer,(byte)0);//clear buffer
					sb.delete(0, sb.length());//clear sb
					//autoButtonfunc_DLMS_3_Phase();
				}*/
				else{

					count1 = 0;
					Toast.makeText(UARTLoopbackActivity.this,"Unable to read data from the meter.Take reading Manually.", Toast.LENGTH_LONG).show();
					Intent i = new Intent(UARTLoopbackActivity.this, HomePage.class);
					i.putExtra("Key","LT3");
					startActivity(i);
				}
			}
			Arrays.fill(readBuffer,(byte)0);
			Toast.makeText(UARTLoopbackActivity.this,"Data Read Completed.", Toast.LENGTH_LONG).show();
			Arrays.fill(readbytes,(byte)0);
			writefile.flush();
			writefile.close();	
			writelength.flush();
			writelength.close();	
			//Start 28-11-2016 to WriteLog
			try
			{
				EncryptDecrypt en = new EncryptDecrypt("nsoft987");										
				final String BillingTest = en.encryptData(writeData.toString(),IMEINumber ,"DLMS-L&G");
			}
			catch(Exception e1)
			{
				Log.d(" "," ");
			}
			//End 28-11-2016 to WriteLog
			Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT"); 
			startActivity(i);
		}
		catch (Exception e) {
			Toast.makeText(UARTLoopbackActivity.this,e.toString(), Toast.LENGTH_LONG).show();
			Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT2");
			startActivity(i);
		}	                   
	}	
	public void autoButtonfunc_DLMS_3_Phase()
	{
		String mFilepath = Environment.getExternalStorageDirectory().getPath()+"/Download/DLMS_Output.txt";
		File f = new File(mFilepath);	
		//File to = new File(mFilepath)
		//f.renameTo(newPath)
		if(f.exists())
			f.delete();
		//Nitish For LogData 28-11-2016
		String writeData = "";		
		uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
		StringBuilder sb =new StringBuilder();
		FileOutputStream writefile=null;
		FileOutputStream writelength=null;
		String Stringpath = Environment.getExternalStorageDirectory().getPath()+"/Download" ;
		String sRoot = Environment.getExternalStorageDirectory().getPath();
		String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
		String currentdatetime = new SimpleDateFormat("ddMMyyyyHHmmss").format(Calendar.getInstance().getTime());
		String StringpathLog = sRoot+"/OpticalBilling/"+timeStamp+"/"+"DLMS-3Phase";
		
		File Filepath = new File(Stringpath);
		File filepathLog = new File(StringpathLog);
		byte[] readbytes = null;
		int SevenECount = 0;
		
		if(!Filepath.exists())//Check directory Exists or not 
		{
			Filepath.mkdirs();//Not Exists Create Directory
		}	
		if(!filepathLog.exists())//Check directory Exists or not 
		{
			filepathLog.mkdirs();//Not Exists Create Directory
		}

		try {
			writefile = new FileOutputStream(Stringpath + "/DLMS_Output.txt",true);
			writelength =new FileOutputStream(StringpathLog + "/DLMS-3Phase_Log_" +IMEINumber+ "_" +currentdatetime+".txt",true);
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Get OutputStream of New Directory Path
		try
		{	
			for(int ReqCount = 0;ReqCount < DLMSRequestArray_3_Ph_DLMS.length;ReqCount++)
			{
				int a = 0;
				int lc=0;
				SevenECount = 0;
				String s = null;
				byte writebuf[] = new byte[1];
				String[] StringSplit = DLMSRequestArray_3_Ph_DLMS[ReqCount].split(",");
				int[] ConverttoInt = new int[StringSplit.length];
				byte[] ConverttoByte = new byte[StringSplit.length];
				for(int i = 0;i<StringSplit.length;i++)
				{
					ConverttoInt[i] = Integer.parseInt(StringSplit[i],16);
					ConverttoByte[i] = (byte)(ConverttoInt[i]);
				}
				for (int j = 0; j < ConverttoByte.length; j++)
				{
					writebuf[0] = ConverttoByte[j];							
					uartInterface.SendData(1,writebuf,(j + 1));
				}
				//Toast.makeText(UARTLoopbackActivity.this, String.valueOf(ReqCount) + "Sent", Toast.LENGTH_LONG).show();
				addtime = Calendar.getInstance().getTimeInMillis() + 3750;//4 second delay
				while(addtime > Calendar.getInstance().getTimeInMillis())
				{										

				}
				readbytes = readBuffer;
				if(readbytes.length > 1)
				{
					//s = "FRAME STARTED" + "\r\n";
					s="1 ";
					writelength.write(s.getBytes());
					for(int k = 0;k<=readbytes.length-1;k++)
					{
						String SByte2 = String.format(" %02X",readbytes[k]);
						lc=lc+1;

						//s = "lc : " + String.valueOf(lc) + "\r\n";
						s="2 ";
						writelength.write(s.getBytes());

						if(SByte2.contains("7E"))
						{							
							SevenECount++;
							writefile.write(SByte2.toString().getBytes());
							writeData = writeData + SByte2.toString(); //Nitish 28-11-2016 For Backup Log
							if(SevenECount == 1)
							{
								a = readbytes[k+2];
								s="3 ";
								//s = "GOT FRAME LENGHT:" + String.valueOf(a) + "\r\n";
								writelength.write(s.getBytes());
								if(a == 0)
									break;
							}
							s="4 ";
							//s = "GOT 7E : " + String.valueOf(lc) + " 7E COUNT " + String.valueOf(SevenECount) +  "\r\n";
							writelength.write(s.getBytes());
						}
						else if(lc == 1)
						{
							s="5 ";
							//s = "GOT 00 AS 1ST BYTE  K : " + String.valueOf(k) + "\r\n";
							writelength.write(s.getBytes());
							lc = 0;
							continue;
						}
						else{
							writefile.write(SByte2.toString().getBytes());
							writeData = writeData + SByte2.toString(); //Nitish 28-11-2016 For Backup Log
							s="6 ";
							//s = "READING BYTES  " + "7E COUNT : " + String.valueOf(SevenECount) + " lc : " +  String.valueOf(lc) + "\r\n";
							writelength.write(s.getBytes());
						}

						if(lc==a+2)						
						{
							s="7 ";
							//s = "GOT CLOSING TAG AT lc : " + String.valueOf(lc) + "7E COUNT : " + String.valueOf(SevenECount) + "\r\n";
							writelength.write(s.getBytes());

							String newline = String.format("%s", "\r\n");
							writefile.write(newline.toString().getBytes());
							SevenECount = 0;
							lc = 0;
							a = 0;
							writeData = writeData + newline.toString(); //Nitish 28-11-2016 For Backup Log							
							break;
						}										
					}
					Arrays.fill(readBuffer,(byte)0);
					Arrays.fill(readbytes,(byte)0);

					s="8 ";
					//s = "READING COMPLETED" + "\r\n";
					writelength.write(s.getBytes());

				}
				/*else if(count1 < 3)
				{

					count1++;
					Arrays.fill(readBuffer,(byte)0);//clear buffer
					sb.delete(0, sb.length());//clear sb
					//autoButtonfunc_DLMS_3_Phase();
				}*/
				else{

					count1 = 0;
					Toast.makeText(UARTLoopbackActivity.this,"Unable to read data from the meter.Take reading Manually.", Toast.LENGTH_LONG).show();
					Intent i = new Intent(UARTLoopbackActivity.this, HomePage.class);
					i.putExtra("Key","LT3");
					startActivity(i);
				}
			}
			Arrays.fill(readBuffer,(byte)0);
			Toast.makeText(UARTLoopbackActivity.this,"Data Read Completed.", Toast.LENGTH_LONG).show();
			Arrays.fill(readbytes,(byte)0);
			writefile.flush();
			writefile.close();	
			writelength.flush();
			writelength.close();	
			//Start 28-11-2016 to WriteLog
			try
			{
				EncryptDecrypt en = new EncryptDecrypt("nsoft987");										
				final String BillingTest = en.encryptData(writeData.toString(),IMEINumber ,"DLMS-3Phase");
			}
			catch(Exception e1)
			{
				Log.d(" "," ");
			}
			//End 28-11-2016 to WriteLog
			Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT"); 
			startActivity(i);
		}
		catch (Exception e) {
			Toast.makeText(UARTLoopbackActivity.this,e.toString(), Toast.LENGTH_LONG).show();
			Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT2");
			startActivity(i);
		}	                   
	}
	
	public void autoButtonfunc_DLMS_LLS_HPL()
	{
		String mFilepath = Environment.getExternalStorageDirectory().getPath()+"/Download/DLMS_Output.txt";
		File f = new File(mFilepath);	
		//File to = new File(mFilepath)
		//f.renameTo(newPath)
		if(f.exists())
			f.delete();
		//Nitish For LogData 28-11-2016
		String writeData = "";		
		uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
		StringBuilder sb =new StringBuilder();
		FileOutputStream writefile=null;
		FileOutputStream writelength=null;
		String Stringpath = Environment.getExternalStorageDirectory().getPath()+"/Download" ;
		String sRoot = Environment.getExternalStorageDirectory().getPath();
		String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
		String currentdatetime = new SimpleDateFormat("ddMMyyyyHHmmss").format(Calendar.getInstance().getTime());
		String StringpathLog = sRoot+"/OpticalBilling/"+timeStamp+"/"+"DLMS-HPL";

		File Filepath = new File(Stringpath);
		File filepathLog = new File(StringpathLog);
		byte[] readbytes = null;
		int SevenECount = 0;

		if(!Filepath.exists())//Check directory Exists or not 
		{
			Filepath.mkdirs();//Not Exists Create Directory
		}	
		if(!filepathLog.exists())//Check directory Exists or not 
		{
			filepathLog.mkdirs();//Not Exists Create Directory
		}

		try {
			writefile = new FileOutputStream(Stringpath + "/DLMS_Output.txt",true);
			writelength =new FileOutputStream(StringpathLog + "/DLMS-HPL_Log_" +IMEINumber+ "_" +currentdatetime+".txt",true);
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Get OutputStream of New Directory Path
		try
		{	
			for(int ReqCount = 0;ReqCount < DLMS_LLS_HPL.length;ReqCount++)
			{
				int a = 0;
				int lc=0;
				SevenECount = 0;
				String s = null;
				byte writebuf[] = new byte[1];
				String[] StringSplit = DLMS_LLS_HPL[ReqCount].split(",");
				int[] ConverttoInt = new int[StringSplit.length];
				byte[] ConverttoByte = new byte[StringSplit.length];
				for(int i = 0;i<StringSplit.length;i++)
				{
					ConverttoInt[i] = Integer.parseInt(StringSplit[i],16);
					ConverttoByte[i] = (byte)(ConverttoInt[i]);
				}
				for (int j = 0; j < ConverttoByte.length; j++)
				{
					writebuf[0] = ConverttoByte[j];							
					uartInterface.SendData(1,writebuf,(j + 1));
				}
				//Toast.makeText(UARTLoopbackActivity.this, String.valueOf(ReqCount) + "Sent", Toast.LENGTH_LONG).show();
				addtime = Calendar.getInstance().getTimeInMillis() + 3750;//4 second delay
				while(addtime > Calendar.getInstance().getTimeInMillis())
				{										

				}
				readbytes = readBuffer;
				if(readbytes.length > 1)
				{
					//s = "FRAME STARTED" + "\r\n";
					s="1 ";
					writelength.write(s.getBytes());
					for(int k = 0;k<=readbytes.length-1;k++)
					{
						String SByte2 = String.format(" %02X",readbytes[k]);
						lc=lc+1;

						//s = "lc : " + String.valueOf(lc) + "\r\n";
						s="2 ";
						writelength.write(s.getBytes());

						if(SByte2.contains("7E"))
						{							
							SevenECount++;
							writefile.write(SByte2.toString().getBytes());
							writeData = writeData + SByte2.toString(); //Nitish 28-11-2016 For Backup Log
							if(SevenECount == 1)
							{
								a = readbytes[k+2];
								s="3 ";
								//s = "GOT FRAME LENGHT:" + String.valueOf(a) + "\r\n";
								writelength.write(s.getBytes());
								if(a == 0)
									break;
							}
							s="4 ";
							//s = "GOT 7E : " + String.valueOf(lc) + " 7E COUNT " + String.valueOf(SevenECount) +  "\r\n";
							writelength.write(s.getBytes());
						}
						else if(lc == 1)
						{
							s="5 ";
							//s = "GOT 00 AS 1ST BYTE  K : " + String.valueOf(k) + "\r\n";
							writelength.write(s.getBytes());
							lc = 0;
							continue;
						}
						else{
							writefile.write(SByte2.toString().getBytes());
							writeData = writeData + SByte2.toString(); //Nitish 28-11-2016 For Backup Log
							s="6 ";
							//s = "READING BYTES  " + "7E COUNT : " + String.valueOf(SevenECount) + " lc : " +  String.valueOf(lc) + "\r\n";
							writelength.write(s.getBytes());
						}

						if(lc==a+2)						
						{
							s="7 ";
							//s = "GOT CLOSING TAG AT lc : " + String.valueOf(lc) + "7E COUNT : " + String.valueOf(SevenECount) + "\r\n";
							writelength.write(s.getBytes());

							String newline = String.format("%s", "\r\n");
							writefile.write(newline.toString().getBytes());
							SevenECount = 0;
							lc = 0;
							a = 0;
							writeData = writeData + newline.toString(); //Nitish 28-11-2016 For Backup Log							
							break;
						}										
					}
					Arrays.fill(readBuffer,(byte)0);
					Arrays.fill(readbytes,(byte)0);

					s="8 ";
					//s = "READING COMPLETED" + "\r\n";
					writelength.write(s.getBytes());

				}
				/*else if(count1 < 3)
				{

					count1++;
					Arrays.fill(readBuffer,(byte)0);//clear buffer
					sb.delete(0, sb.length());//clear sb
					//autoButtonfunc_DLMS_3_Phase();
				}*/
				else{

					count1 = 0;
					Toast.makeText(UARTLoopbackActivity.this,"Unable to read data from the meter.Take reading Manually.", Toast.LENGTH_LONG).show();
					Intent i = new Intent(UARTLoopbackActivity.this, HomePage.class);
					i.putExtra("Key","LT3");
					startActivity(i);
				}
			}
			Arrays.fill(readBuffer,(byte)0);
			Toast.makeText(UARTLoopbackActivity.this,"Data Read Completed.", Toast.LENGTH_LONG).show();
			Arrays.fill(readbytes,(byte)0);
			writefile.flush();
			writefile.close();	
			writelength.flush();
			writelength.close();	
			//Start 28-11-2016 to WriteLog
			try
			{
				EncryptDecrypt en = new EncryptDecrypt("nsoft987");										
				final String BillingTest = en.encryptData(writeData.toString(),IMEINumber ,"DLMS-HPL");
			}
			catch(Exception e1)
			{
				Log.d(" "," ");
			}
			//End 28-11-2016 to WriteLog
			Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT"); 
			startActivity(i);
		}
		catch (Exception e) {
			Toast.makeText(UARTLoopbackActivity.this,e.toString(), Toast.LENGTH_LONG).show();
			Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT2");
			startActivity(i);
		}	                   
	}
	public void autoButtonfunc_DLMS_LT_1_Ph()
	{
		String mFilepath = Environment.getExternalStorageDirectory().getPath()+"/Download/DLMS_Output.txt";
		File f = new File(mFilepath);	
		//File to = new File(mFilepath)
		//f.renameTo(newPath)
		if(f.exists())
			f.delete();
		//Nitish For LogData 28-11-2016
		String writeData = "";		
		uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
		StringBuilder sb =new StringBuilder();
		FileOutputStream writefile=null;
		FileOutputStream writelength=null;
		String Stringpath = Environment.getExternalStorageDirectory().getPath()+"/Download" ;
		String sRoot = Environment.getExternalStorageDirectory().getPath();
		String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
		String currentdatetime = new SimpleDateFormat("ddMMyyyyHHmmss").format(Calendar.getInstance().getTime());
		String StringpathLog = sRoot+"/OpticalBilling/"+timeStamp+"/"+"DLMS-L&T_1PH";
		
		File Filepath = new File(Stringpath);
		File filepathLog = new File(StringpathLog);
		byte[] readbytes = null;
		int SevenECount = 0;
		
		if(!Filepath.exists())//Check directory Exists or not 
		{
			Filepath.mkdirs();//Not Exists Create Directory
		}	
		if(!filepathLog.exists())//Check directory Exists or not 
		{
			filepathLog.mkdirs();//Not Exists Create Directory
		}

		try {
			writefile = new FileOutputStream(Stringpath + "/DLMS_Output.txt",true);
			writelength =new FileOutputStream(StringpathLog + "/DLMS-L&T_1PH_Log_" +IMEINumber+ "_" +currentdatetime+".txt",true);
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Get OutputStream of New Directory Path
		try
		{	
			for(int ReqCount = 0;ReqCount < DLMSRequestArray_LT_1Ph.length;ReqCount++)
			{
				int a = 0;
				int lc=0;
				SevenECount = 0;
				String s = null;
				byte writebuf[] = new byte[1];
				String[] StringSplit = DLMSRequestArray_LT_1Ph[ReqCount].split(",");
				int[] ConverttoInt = new int[StringSplit.length];
				byte[] ConverttoByte = new byte[StringSplit.length];
				for(int i = 0;i<StringSplit.length;i++)
				{
					ConverttoInt[i] = Integer.parseInt(StringSplit[i],16);
					ConverttoByte[i] = (byte)(ConverttoInt[i]);
				}
				for (int j = 0; j < ConverttoByte.length; j++)
				{
					writebuf[0] = ConverttoByte[j];							
					uartInterface.SendData(1,writebuf,(j + 1));
				}
				//Toast.makeText(UARTLoopbackActivity.this, String.valueOf(ReqCount) + "Sent", Toast.LENGTH_LONG).show();
				addtime = Calendar.getInstance().getTimeInMillis() + 3750;//4 second delay
				while(addtime > Calendar.getInstance().getTimeInMillis())
				{										

				}
				readbytes = readBuffer;
				if(readbytes.length > 1)
				{
					//s = "FRAME STARTED" + "\r\n";
					s="1 ";
					writelength.write(s.getBytes());
					for(int k = 0;k<=readbytes.length-1;k++)
					{
						String SByte2 = String.format(" %02X",readbytes[k]);
						lc=lc+1;

						//s = "lc : " + String.valueOf(lc) + "\r\n";
						s="2 ";
						writelength.write(s.getBytes());

						if(SByte2.contains("7E"))
						{							
							SevenECount++;
							writefile.write(SByte2.toString().getBytes());
							writeData = writeData + SByte2.toString(); //Nitish 28-11-2016 For Backup Log
							if(SevenECount == 1)
							{
								a = readbytes[k+2];
								s="3 ";
								//s = "GOT FRAME LENGHT:" + String.valueOf(a) + "\r\n";
								writelength.write(s.getBytes());
								if(a == 0)
									break;
							}
							s="4 ";
							//s = "GOT 7E : " + String.valueOf(lc) + " 7E COUNT " + String.valueOf(SevenECount) +  "\r\n";
							writelength.write(s.getBytes());
						}
						else if(lc == 1)
						{
							s="5 ";
							//s = "GOT 00 AS 1ST BYTE  K : " + String.valueOf(k) + "\r\n";
							writelength.write(s.getBytes());
							lc = 0;
							continue;
						}
						else{
							writefile.write(SByte2.toString().getBytes());
							writeData = writeData + SByte2.toString(); //Nitish 28-11-2016 For Backup Log
							s="6 ";
							//s = "READING BYTES  " + "7E COUNT : " + String.valueOf(SevenECount) + " lc : " +  String.valueOf(lc) + "\r\n";
							writelength.write(s.getBytes());
						}

						if(lc==a+2)						
						{
							s="7 ";
							//s = "GOT CLOSING TAG AT lc : " + String.valueOf(lc) + "7E COUNT : " + String.valueOf(SevenECount) + "\r\n";
							writelength.write(s.getBytes());

							String newline = String.format("%s", "\r\n");
							writefile.write(newline.toString().getBytes());
							SevenECount = 0;
							lc = 0;
							a = 0;
							writeData = writeData + newline.toString(); //Nitish 28-11-2016 For Backup Log							
							break;
						}										
					}
					Arrays.fill(readBuffer,(byte)0);
					Arrays.fill(readbytes,(byte)0);

					s="8 ";
					//s = "READING COMPLETED" + "\r\n";
					writelength.write(s.getBytes());

				}
				/*else if(count1 < 3)
				{

					count1++;
					Arrays.fill(readBuffer,(byte)0);//clear buffer
					sb.delete(0, sb.length());//clear sb
					//autoButtonfunc_DLMS_3_Phase();
				}*/
				else{

					count1 = 0;
					Toast.makeText(UARTLoopbackActivity.this,"Unable to read data from the meter.Take reading Manually.", Toast.LENGTH_LONG).show();
					Intent i = new Intent(UARTLoopbackActivity.this, HomePage.class);
					i.putExtra("Key","LT3");
					startActivity(i);
				}
			}
			Arrays.fill(readBuffer,(byte)0);
			Toast.makeText(UARTLoopbackActivity.this,"Data Read Completed.", Toast.LENGTH_LONG).show();
			Arrays.fill(readbytes,(byte)0);
			writefile.flush();
			writefile.close();	
			writelength.flush();
			writelength.close();	
			//Start 28-11-2016 to WriteLog
			try
			{
				EncryptDecrypt en = new EncryptDecrypt("nsoft987");										
				final String BillingTest = en.encryptData(writeData.toString(),IMEINumber ,"DLMS-L&T_1PH");
			}
			catch(Exception e1)
			{
				Log.d(" "," ");
			}
			//End 28-11-2016 to WriteLog
			Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT"); 
			startActivity(i);
		}
		catch (Exception e) {
			Toast.makeText(UARTLoopbackActivity.this,e.toString(), Toast.LENGTH_LONG).show();
			Intent i = new Intent(UARTLoopbackActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT2");
			startActivity(i);
		}	                   
	}
	
}
