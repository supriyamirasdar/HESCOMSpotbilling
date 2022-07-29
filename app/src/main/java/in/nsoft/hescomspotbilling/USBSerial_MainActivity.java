package in.nsoft.hescomspotbilling;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;





import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class USBSerial_MainActivity extends Activity implements AlertInterface
{

	private static final String TAG = "MainActivity";
	private static final String RECEIVED_TEXT_VIEW_STR = "RECEIVED_TEXT_VIEW_STR";

	private USBSerial_UsbService usbService;

	private Button sendBtn;


	private TextView receivedMsgView,lblheader;
	private ScrollView scrollView;



	private MyHandler mHandler;


	private boolean isUSBReady = false;
	private boolean isConnect = false;
	static int count1=0,count2 = 0;
	DatabaseHelper mdb = new DatabaseHelper(this);

	//12-05-2021
	String[] DLMSRequestArray_3_Ph_DLMS = {"7E,A0,07,03,41,93,5A,64,7E",
			"7E,A0,48,03,41,10,87,76,E6,E6,00,60,3A,80,02,02,84,A1,09,06,07,60,85,74,05,08,01,01,8A,02,07,80,8B,07,60,85,74,05,08,02,01,AC,0A,80,08,41,42,43,44,30,30,30,31,BE,10,04,0E,01,00,00,00,06,5F,1F,04,00,00,18,1D,FF,FF,8A,C8,7E",
			"7E,A0,19,03,41,32,3A,BD,E6,E6,00,C0,01,81,00,01,00,00,60,01,00,FF,02,00,8C,6D,7E", //MtrSlNo
			"7E,A0,19,03,41,34,0C,D8,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,02,00,37,A5,7E", //KWH1
			"7E,A0,19,03,41,36,1E,FB,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,03,00,EF,BC,7E", //KWH2
			"7E,A0,19,03,41,38,60,12,E6,E6,00,C0,01,81,00,04,01,00,01,06,00,FF,02,00,6D,2D,7E", //MD1
			"7E,A0,19,03,41,3A,72,31,E6,E6,00,C0,01,81,00,04,01,00,01,06,00,FF,03,00,B5,34,7E", //MD2
			"7E,A0,19,03,41,3C,44,54,E6,E6,00,C0,01,81,00,03,01,00,0D,07,00,FF,02,00,3F,FE,7E", //PF1
			"7E,A0,19,03,41,3E,56,77,E6,E6,00,C0,01,81,00,03,01,00,0D,07,00,FF,03,00,E7,E7,7E"}; //PF2

	String[] DLMSRequestArray_Saral_DLMS = {"7E,A0,07,03,41,93,5A,64,7E",
			"7E,A0,48,03,41,10,87,76,E6,E6,00,60,3A,80,02,02,84,A1,09,06,07,60,85,74,05,08,01,01,8A,02,07,80,8B,07,60,85,74,05,08,02,01,AC,0A,80,08,41,42,43,44,30,30,30,31,BE,10,04,0E,01,00,00,00,06,5F,1F,04,00,00,18,1D,FF,FF,8A,C8,7E",
			"7E,A0,19,03,41,32,3A,BD,E6,E6,00,C0,01,81,00,01,00,00,60,01,00,FF,02,00,8C,6D,7E", //MtrSlNo
			"7E,A0,19,03,41,34,0C,D8,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,02,00,37,A5,7E", //KWH1
			"7E,A0,19,03,41,36,1E,FB,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,03,00,EF,BC,7E", //KWH2
			"7E,A0,19,03,41,38,60,12,E6,E6,00,C0,01,81,00,04,01,00,01,06,00,FF,02,00,6D,2D,7E", //MD1
			"7E,A0,19,03,41,3A,72,31,E6,E6,00,C0,01,81,00,04,01,00,01,06,00,FF,03,00,B5,34,7E", //MD2
			"7E,A0,19,03,41,3C,44,54,E6,E6,00,C0,01,81,00,03,01,00,0D,07,00,FF,02,00,3F,FE,7E", //PF1
			"7E,A0,19,03,41,3E,56,77,E6,E6,00,C0,01,81,00,03,01,00,0D,07,00,FF,03,00,E7,E7,7E"}; //PF2

	String[] DLMSRequestArrayICredit = {"7E,A0,07,03,41,93,5A,64,7E",
			"7E,A0,48,03,41,10,87,76,E6,E6,00,60,3A,80,02,02,84,A1,09,06,07,60,85,74,05,08,01,01,8A,02,07,80,8B,07,60,85,74,05,08,02,01,AC,0A,80,08,41,42,43,44,30,30,30,31,BE,10,04,0E,01,00,00,00,06,5F,1F,04,00,00,18,1D,FF,FF,8A,C8,7E",
			"7E,A0,19,03,41,32,3A,BD,E6,E6,00,C0,01,81,00,01,00,00,60,01,00,FF,02,00,8C,6D,7E", //MtrSlNo
			"7E,A0,19,03,41,34,0C,D8,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,02,00,37,A5,7E", //KWH1
			"7E,A0,19,03,41,36,1E,FB,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,03,00,EF,BC,7E", //KWH2
			"7E,A0,19,03,41,38,60,12,E6,E6,00,C0,01,81,00,04,01,00,01,06,00,FF,02,00,6D,2D,7E", //MD1
			"7E,A0,19,03,41,3A,72,31,E6,E6,00,C0,01,81,00,04,01,00,01,06,00,FF,03,00,B5,34,7E", //MD2
			"7E,A0,19,03,41,3C,44,54,E6,E6,00,C0,01,81,00,03,01,00,0D,07,00,FF,02,00,3F,FE,7E", //PF1
			"7E,A0,19,03,41,3E,56,77,E6,E6,00,C0,01,81,00,03,01,00,0D,07,00,FF,03,00,E7,E7,7E"}; //PF2

	static String[] DLMSRequestArrayLG = {"7E,A0,07,03,41,93,5A,64,7E",
		"7E,A0,48,03,41,10,87,76,E6,E6,00,60,3A,80,02,02,84,A1,09,06,07,60,85,74,05,08,01,01,8A,02,07,80,8B,07,60,85,74,05,08,02,01,AC,0A,80,08,31,31,31,31,31,31,31,31,BE,10,04,0E,01,00,00,00,06,5F,1F,04,00,00,18,1D,FF,FF,43,8A,7E",
		"7E,A0,19,03,41,32,3A,BD,E6,E6,00,C0,01,81,00,01,00,00,60,01,00,FF,02,00,8C,6D,7E", //MtrSlNo
		"7E,A0,19,03,41,34,0C,D8,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,02,00,37,A5,7E", //KWH1
		"7E,A0,19,03,41,36,1E,FB,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,03,00,EF,BC,7E", //KWH2
		"7E,A0,19,03,41,38,60,12,E6,E6,00,C0,01,81,00,04,01,00,01,06,00,FF,02,00,6D,2D,7E", //MD1
		"7E,A0,19,03,41,3A,72,31,E6,E6,00,C0,01,81,00,04,01,00,01,06,00,FF,03,00,B5,34,7E", //MD2
		"7E,A0,19,03,41,3C,44,54,E6,E6,00,C0,01,81,00,03,01,00,0D,07,00,FF,02,00,3F,FE,7E", //PF1
		"7E,A0,19,03,41,3E,56,77,E6,E6,00,C0,01,81,00,03,01,00,0D,07,00,FF,03,00,E7,E7,7E"}; //PF2

	static String[] DLMS_LLS_HPL = {"7E,A0,07,03,41,93,5A,64,7E",
		"7E,A0,50,03,41,10,FE,50,E6,E6,00,60,42,80,02,02,84,A1,09,06,07,60,85,74,05,08,01,01,8A,02,07,80,8B,07,60,85,74,05,08,02,01,AC,12,80,10,31,31,31,31,31,31,31,31,31,31,31,31,31,31,31,31,BE,10,04,0E,01,00,00,00,06,5F,1F,04,00,00,18,1D,FF,FF,A5,ED,7E",
		"7E,A0,19,03,41,32,3A,BD,E6,E6,00,C0,01,81,00,01,00,00,60,01,00,FF,02,00,8C,6D,7E", //MtrSlNo
		"7E,A0,19,03,41,34,0C,D8,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,02,00,37,A5,7E", //KWH1
		"7E,A0,19,03,41,36,1E,FB,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,03,00,EF,BC,7E", //KWH2
		"7E,A0,19,03,41,38,60,12,E6,E6,00,C0,01,81,00,04,01,00,01,06,00,FF,02,00,6D,2D,7E", //MD1
		"7E,A0,19,03,41,3A,72,31,E6,E6,00,C0,01,81,00,04,01,00,01,06,00,FF,03,00,B5,34,7E", //MD2
		"7E,A0,19,03,41,3C,44,54,E6,E6,00,C0,01,81,00,03,01,00,0D,07,00,FF,02,00,3F,FE,7E", //PF1
		"7E,A0,19,03,41,3E,56,77,E6,E6,00,C0,01,81,00,03,01,00,0D,07,00,FF,03,00,E7,E7,7E"}; //PF2


	static String[] DLMSRequestArray_LT_1Ph = {"7E,A0,07,03,41,93,5A,64,7E",
		"7E,A0,44,03,41,10,B3,E1,E6,E6,00,60,36,80,02,02,84,A1,09,06,07,60,85,74,05,08,01,01,8A,02,07,80,8B,07,60,85,74,05,08,02,01,AC,06,80,04,6C,6E,74,31,BE,10,04,0E,01,00,00,00,06,5F,1F,04,00,00,18,1D,FF,FF,1F,5E,7E",
		"7E,A0,19,03,41,32,3A,BD,E6,E6,00,C0,01,81,00,01,00,00,60,01,00,FF,02,00,8C,6D,7E", //MtrSlNo
		"7E,A0,19,03,41,34,0C,D8,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,02,00,37,A5,7E", //KWH1
		"7E,A0,19,03,41,36,1E,FB,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,03,00,EF,BC,7E", //KWH2
		"7E,A0,19,03,41,38,60,12,E6,E6,00,C0,01,81,00,04,01,00,01,06,00,FF,02,00,6D,2D,7E", //MD1
		"7E,A0,19,03,41,3A,72,31,E6,E6,00,C0,01,81,00,04,01,00,01,06,00,FF,03,00,B5,34,7E", //MD2
		"7E,A0,19,03,41,3C,44,54,E6,E6,00,C0,01,81,00,03,01,00,0D,07,00,FF,02,00,3F,FE,7E", //PF1
		"7E,A0,19,03,41,3E,56,77,E6,E6,00,C0,01,81,00,03,01,00,0D,07,00,FF,03,00,E7,E7,7E"}; //PF2
	
	//GENUS 10-05-2021
	static String[] DLMSRequestArray_GENUS = {"7E,A0,07,03,41,93,5A,64,7E",		
		"7E,A0,48,03,41,10,87,76,E6,E6,00,60,3A,80,02,02,84,A1,09,06,07,60,85,74,05,08,01,01,8A,02,07,80,8B,07,60,85,74,05,08,02,01,AC,0A,80,08,31,41,32,42,33,43,34,44,BE,10,04,0E,01,00,00,00,06,5F,1F,04,00,00,1E,1D,FF,FF,D3,58,7E",
		"7E,A0,19,03,41,32,3A,BD,E6,E6,00,C0,01,81,00,01,00,00,60,01,00,FF,02,00,8C,6D,7E", //MtrSlNo
		"7E,A0,19,03,41,34,0C,D8,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,02,00,37,A5,7E", //KWH1
		"7E,A0,19,03,41,36,1E,FB,E6,E6,00,C0,01,81,00,03,01,00,01,08,00,FF,03,00,EF,BC,7E", //KWH2
		"7E,A0,19,03,41,38,60,12,E6,E6,00,C0,01,81,00,04,01,00,01,06,00,FF,02,00,6D,2D,7E", //MD1
		"7E,A0,19,03,41,3A,72,31,E6,E6,00,C0,01,81,00,04,01,00,01,06,00,FF,03,00,B5,34,7E", //MD2
		"7E,A0,19,03,41,3C,44,54,E6,E6,00,C0,01,81,00,03,01,00,0D,07,00,FF,02,00,3F,FE,7E", //PF1
		"7E,A0,19,03,41,3E,56,77,E6,E6,00,C0,01,81,00,03,01,00,0D,07,00,FF,03,00,E7,E7,7E"}; //PF2

	static String IMEINumber = "";
	static String SimNumber = "";
	DatabaseHelper db = new DatabaseHelper(this);
	long addtime=0;



	/*private ServiceConnection usbConnection = new ServiceConnection() 
	{
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			usbService = ((USBSerial_UsbService.UsbBinder) arg1).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			usbService = null;
		}
	};*/

	private ServiceConnection usbConnection;
	private BroadcastReceiver mUsbReceiver;

	/*private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			switch (intent.getAction()) {
			case USBSerial_UsbService.ACTION_USB_PERMISSION_GRANTED:
				Toast.makeText(context,
						getString(R.string.usb_permission_granted),
						Toast.LENGTH_SHORT).show();
				isUSBReady = true;				
				requestConnection();
				break;
			case USBSerial_UsbService.ACTION_USB_PERMISSION_NOT_GRANTED:
				Toast.makeText(context,
						getString(R.string.usb_permission_not_granted),
						Toast.LENGTH_SHORT).show();
				break;
			case USBSerial_UsbService.ACTION_NO_USB:
				Toast.makeText(context,
						getString(R.string.no_usb),
						Toast.LENGTH_SHORT).show();
				break;
			case USBSerial_UsbService.ACTION_USB_DISCONNECTED:
				Toast.makeText(context,
						getString(R.string.usb_disconnected),
						Toast.LENGTH_SHORT).show();
				isUSBReady = false;
				stopConnection();
				break;
			case USBSerial_UsbService.ACTION_USB_NOT_SUPPORTED:
				Toast.makeText(context, getString(R.string.usb_not_supported),
						Toast.LENGTH_SHORT).show();
				break;
			default:
				Log.e(TAG, "Unknown action");
				break;
			}
		}
	};*/

	private void requestConnection() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(USBSerial_MainActivity.this);
		alertDialog.setMessage(getString(R.string.confirm_connect));
		alertDialog.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				startConnection();
			}
		});
		alertDialog.setNegativeButton(getString(android.R.string.cancel), null);
		alertDialog.create().show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		mHandler = new MyHandler(this);

		setContentView(R.layout.activity_usbserial_main);


		receivedMsgView = (TextView) findViewById(R.id.receivedMsgView);
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		sendBtn = (Button) findViewById(R.id.sendBtn);

		sendBtn.setVisibility(View.VISIBLE);
		lblheader = (TextView) findViewById(R.id.lblheader);
		lblheader.setText("METER TYPE: " + mdb.getMType(Billing_LandT.LTLG));
		
		sendBtn.setVisibility(View.VISIBLE);
		/*ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setIcon(R.drawable.ic_launcher);
		actionBar.setTitle("LORA NODE CONFIG");*/

		mUsbReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				switch (intent.getAction()) {
				case USBSerial_UsbService.ACTION_USB_PERMISSION_GRANTED:
					Toast.makeText(context,
							getString(R.string.usb_permission_granted),
							Toast.LENGTH_SHORT).show();
					isUSBReady = true;				
					requestConnection();
					break;
				case USBSerial_UsbService.ACTION_USB_PERMISSION_NOT_GRANTED:
					Toast.makeText(context,
							getString(R.string.usb_permission_not_granted),
							Toast.LENGTH_SHORT).show();
					break;
				case USBSerial_UsbService.ACTION_NO_USB:
					Toast.makeText(context,
							getString(R.string.no_usb),
							Toast.LENGTH_SHORT).show();
					break;
				case USBSerial_UsbService.ACTION_USB_DISCONNECTED:
					Toast.makeText(context,
							getString(R.string.usb_disconnected),
							Toast.LENGTH_SHORT).show();
					isUSBReady = false;
					stopConnection();
					break;
				case USBSerial_UsbService.ACTION_USB_NOT_SUPPORTED:
					Toast.makeText(context, getString(R.string.usb_not_supported),
							Toast.LENGTH_SHORT).show();
					break;
				default:
					Log.e(TAG, "Unknown action");
					break;
				}
			}
		};

		usbConnection = new ServiceConnection() 
		{
			@Override
			public void onServiceConnected(ComponentName arg0, IBinder arg1) {
				usbService = ((USBSerial_UsbService.UsbBinder) arg1).getService();
			}

			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				usbService = null;
			}
		};

		sendBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				count1 = 0;
				//07-05-2021
				try
				{
					LandTObject LandTObj = new LandTObject();
					LandTObj.setmMeterType(String.valueOf(Billing_LandT.LTLG));
					mdb.MeterDetails(LandTObj);
					Toast.makeText(USBSerial_MainActivity.this, "Probe Reading Initiated. ", Toast.LENGTH_SHORT).show();
				}
				catch(Exception e)
				{

				}
				sendBtn.setVisibility(View.GONE);
				lblheader.setText("Please Wait Fetching Data...");
				thDLMSControl thDLMS =  new thDLMSControl();
				thDLMS.start();
			}
		});

	}
	public class thDLMSControl extends Thread 
	{

		public thDLMSControl()
		{

		}
		public void run() 
		{ 
			try
			{		
				
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
					//else if(Billing_LandT.LTLG==7)  //L&T All Param
					//	autoButtonfuncAllLT();//
					else if(Billing_LandT.LTLG==8)  //HPL
						autoButtonfunc_DLMS_LLS_HPL();//
					else if(Billing_LandT.LTLG==9)  //L&T 
						autoButtonfunc_DLMS_LT_1_Ph();
					
					else if(Billing_LandT.LTLG==10)  //GENUS 10-05-2021
						autoButtonfunc_DLMS_GENUS();
			}
			catch (final Exception e) {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(USBSerial_MainActivity.this, "EXCEPTION: " + e.toString(), Toast.LENGTH_SHORT).show();
					}
				});		
			}
		} 
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putString(RECEIVED_TEXT_VIEW_STR, receivedMsgView.getText().toString());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		receivedMsgView.setText(savedInstanceState.getString(RECEIVED_TEXT_VIEW_STR));
	}

	@Override
	public void onResume() {
		super.onResume();		
		setFilters();
		startService(USBSerial_UsbService.class, usbConnection);

	}
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		sendBtn.setVisibility(View.VISIBLE);
		lblheader.setText("METER TYPE: " + mdb.getMType(Billing_LandT.LTLG));
		Intent intent1 = new Intent(USBSerial_UsbService.ACTION_SERIAL_CONFIG_CHANGED); //10-05-2021
		sendBroadcast(intent1);
	}
	@Override
	public void onDestroy() {
		if(isConnect) {
			stopConnection();
		}
		unregisterReceiver(mUsbReceiver);
		unbindService(usbConnection);
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(USBSerial_MainActivity.this);
			alertDialog.setMessage(getString(R.string.confirm_finish_text));
			alertDialog.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					//finish();
				}
			});
			alertDialog.setNegativeButton(getString(android.R.string.cancel), null);
			alertDialog.create().show();
			return true;
		}
		return false;
	}

	private void startService(Class<?> service, ServiceConnection serviceConnection) {
		if (!USBSerial_UsbService.SERVICE_CONNECTED) {
			Intent startService = new Intent(this, service);
			startService(startService);
		}
		Intent bindingIntent = new Intent(this, service);
		bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	private void setFilters() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(USBSerial_UsbService.ACTION_USB_PERMISSION_GRANTED);
		filter.addAction(USBSerial_UsbService.ACTION_NO_USB);
		filter.addAction(USBSerial_UsbService.ACTION_USB_DISCONNECTED);
		filter.addAction(USBSerial_UsbService.ACTION_USB_NOT_SUPPORTED);
		filter.addAction(USBSerial_UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
		registerReceiver(mUsbReceiver, filter);
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
		return OptionsMenu.navigate(item,USBSerial_MainActivity.this);	


	}



	private void sendMessage(byte[]  msg) {
		/*Pattern pattern = Pattern.compile("\n$");
		Matcher matcher = pattern.matcher(msg);
		String strResult = matcher.replaceAll("") + lineFeedCode;*/
		try {
			//usbService.write(strResult.getBytes(Constants.CHARSET));
			usbService.write(msg);
			//addReceivedData(new String(msg));
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	private void startConnection() {
		usbService.setHandler(mHandler);
		isConnect = true;
		Toast.makeText(getApplicationContext(),
				getString(R.string.start_connection),Toast.LENGTH_SHORT).show();

	}

	private void stopConnection() {
		usbService.setHandler(null);
		isConnect = false;
		Toast.makeText(getApplicationContext(),
				getString(R.string.stop_connection),Toast.LENGTH_SHORT).show();

	}

	private void addReceivedData(String data) {
		/*if (showTimeStamp) {
            addReceivedDataWithTime(data);
        } else {
            addTextView(data);
        }*/
		addTextView(data);
	}

	private void addTextView(String data) {
		receivedMsgView.append(data);
		scrollView.scrollTo(0, receivedMsgView.getBottom());
	}

	private static class MyHandler extends Handler {
		private final WeakReference<USBSerial_MainActivity> mActivity;

		public MyHandler(USBSerial_MainActivity activity) {
			mActivity = new WeakReference<>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case USBSerial_UsbService.MESSAGE_FROM_SERIAL_PORT:
				String data = (String) msg.obj;
				if (data != null) {
					mActivity.get().addReceivedData(data);
				}
				break;
			case USBSerial_UsbService.CTS_CHANGE:
				Log.d(TAG, "CTS_CHANGE");
				Toast.makeText(mActivity.get(), "CTS_CHANGE", Toast.LENGTH_LONG).show();
				break;
			case USBSerial_UsbService.DSR_CHANGE:
				Log.d(TAG, "DSR_CHANGE");
				Toast.makeText(mActivity.get(), "DSR_CHANGE", Toast.LENGTH_LONG).show();
				break;
			default:
				Log.e(TAG, "Unknown message");
				break;
			}
		}
	}

	public void autoButtonfunc()
	{

		String sread = "";
		//uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
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

			//byte writebuf1[] = new byte[]{47};
			byte writebuf1[] = new byte[]{58}; //Added 10-05-2021
			/*try 
			{
				String swrite=new String(writebuf1);
			} 
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			//data 1 send
			/*uartInterface.SendData(1, writebuf1);			
			uartInterface.SendData(1, writebuf1);
			uartInterface.SendData(1, writebuf1);
			uartInterface.SendData(1, writebuf1);	
			 */
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					receivedMsgView.setText("");
					//Toast.makeText(MainActivityGenusConDis.this,"SNRM",Toast.LENGTH_SHORT).show();
				}
			});	
			sendMessage(writebuf1);
			sendMessage(writebuf1);
			sendMessage(writebuf1);
			sendMessage(writebuf1);

		}

		catch (Exception e)
		{
			// TODO Auto-generated catch block
			Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT1" ); 
			startActivity(i);
			//finish();
		}		

		try
		{			
			//String sread= new String(readBuffer);	
			//Arrays.fill(readBuffer, (byte)0);//clear buffer


			addtime = Calendar.getInstance().getTimeInMillis() + 100;// second delay
			while(addtime > Calendar.getInstance().getTimeInMillis())
			{										

			}
			sread = receivedMsgView.getText().toString();

			//send 2			
			if(sread.length() > 0)
			{
				//Send 2							
				byte writebuf2[] = new byte[]{47};
				//byte sendbuffernew[] = new byte[]{47,63,33,13,10,94,106};	
				byte sendbuffernew[] = new byte[]{47,63,33,13,10};	  //Modified 10-05-2021
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						receivedMsgView.setText("");
						//Toast.makeText(MainActivityGenusConDis.this,"SNRM",Toast.LENGTH_SHORT).show();
					}
				});	
				//for (int i = 0; i < 7; i++) 
				for (int i = 0; i < 5; i++) //Modified 10-05-2021
				{
					addtime = Calendar.getInstance().getTimeInMillis() + 100;//145
					while(addtime > Calendar.getInstance().getTimeInMillis())  
					{										

					}
					writebuf2[0] = sendbuffernew[i];								
					//uartInterface.SendData(1,writebuf2);
					sendMessage(writebuf2);
				}
			}
			else
			{
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(USBSerial_MainActivity.this, "Response 1 not received ", Toast.LENGTH_LONG).show();
					}
				});	

				//return;
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT2" ); 
			startActivity(i);
			//finish();
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
			//String sread = new String(readBuffer);	
			sread = receivedMsgView.getText().toString();



			if(sread.toString().contains("/"))
			{

				//sb.append(sread);//not require for appending 2nd response
				//Toast.makeText(USBSerial_MainActivity.this, "Read 2 " + sb, Toast.LENGTH_LONG).show();

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
					//uartInterface.SendData(1,writebuf3);
					sendMessage(writebuf3);
				}
				//Toast.makeText(USBSerial_MainActivity.this, "Data 3 sent ", Toast.LENGTH_SHORT).show();
			}
			else
			{
				//Toast.makeText(USBSerial_MainActivity.this, "Response 2 not received ", Toast.LENGTH_LONG).show();
				//return;
			}
		}
		catch (Exception e)
		{							
			Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT3" ); 
			startActivity(i);
			//finish();
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
			sread = "";
			addtime = Calendar.getInstance().getTimeInMillis() + 2000;
			do{
				if(addtime < Calendar.getInstance().getTimeInMillis())
					break;
				sread =  receivedMsgView.getText().toString();
				Thread.sleep(50);
			}while(!sread.contains("!"));
			/*********End 11-03-2016************/

			if(sread.contains("!"))
			{
				sb.append(sread);

				//Toast.makeText(USBSerial_MainActivity.this, "Read 3 " + sb, Toast.LENGTH_LONG).show();	

				fos1.write(sb.toString().getBytes()); //write into the file
				fos1.flush();
				fos1.close();
				//Start 18-11-2016 to WriteLog
				try
				{

					EncryptDecrypt en = new EncryptDecrypt("nsoft987");										
					final String BillingTest = en.encryptData(sb.toString(),IMEINumber ,"L&T");

				}
				catch(Exception e)
				{
					Log.d(" "," ");
				}
				Billing_LandT.isFromBilling=false; //05-09-2015
				Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
				i.putExtra("Key","LT" ); 
				startActivity(i);
				//finish();

			}
			else if (count1<=3) 
			{
				count1++;
				//Arrays.fill(readBuffer,(byte)0);//clear buffer

				//readBuffer = new byte[65536];
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						receivedMsgView.setText("");
						//Toast.makeText(MainActivityGenusConDis.this,"SNRM",Toast.LENGTH_SHORT).show();
					}
				});	
				sb.delete(0, sb.length());//clear sb
				autoButtonfunc();
			}
			else
			{
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(USBSerial_MainActivity.this, "Unable To Fetch Data. Please take Reading Manually ",  Toast.LENGTH_LONG).show();
					}
				});				
				Intent i = new Intent(USBSerial_MainActivity.this, HomePage.class);
				//i.putExtra("Key","LT6");
				startActivity(i);
				//finish();
			}
		}
		catch (Exception e)
		{							
			Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT4");
			startActivity(i);
			//finish();
		}	

	}


	public void autoButtonfuncLG()
	{	
		String sread = "";
		String mFilepath = Environment.getExternalStorageDirectory().getPath()+"/Download/L&G_Output.txt";
		File f = new File(mFilepath);	
		//File to = new File(mFilepath)
		//f.renameTo(newPath)
		if(f.exists())
			f.delete();

		//uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
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
				//uartInterface.SendData(1,writebuf2,(i + 1));
				sendMessage(writebuf2);//25-01-2021
			}			
		}
		catch (Exception e) {			
			// TODO Auto-generated catch block

			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(USBSerial_MainActivity.this,"Error While Sending 1st Request.Retry again.", Toast.LENGTH_LONG).show();
					//Toast.makeText(MainActivityGenusConDis.this,"SNRM",Toast.LENGTH_SHORT).show();
				}
			});	


			//Intent i = new Intent(UARTLoopbackActivity.this, LoginActivity.class);
			Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
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
			sread = receivedMsgView.getText().toString(); //25-01-2021
			//receivedMsgView.setText(""); //25-01-2021
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					receivedMsgView.setText("");
					//Toast.makeText(MainActivityGenusConDis.this,"SNRM",Toast.LENGTH_SHORT).show();
				}
			});	

			//Arrays.fill(readBuffer,(byte)0);//clear buffer
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
					//uartInterface.SendData(1,writebuf3,(i + 1)); 
					sendMessage(writebuf3);//25-01-2021
				}				
				//Arrays.fill(readBuffer,(byte)0);//clear buffer
				//receivedMsgView.setText(""); //25-01-2021

				mHandler.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						receivedMsgView.setText("");
						//Toast.makeText(MainActivityGenusConDis.this,"SNRM",Toast.LENGTH_SHORT).show();
					}
				});	

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
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									receivedMsgView.setText("");
									Toast.makeText(USBSerial_MainActivity.this,"Connection Timeout.Retry again.", Toast.LENGTH_LONG).show();
								}
							});	
							//Toast.makeText(USBSerial_MainActivity.this,"Connection Timeout.Retry again.", Toast.LENGTH_LONG).show();
							break;
						}
						//sread = new String(readBuffer,"UTF-8");
						//sread = FT311UARTInterface.tempsread;
						sread = receivedMsgView.getText().toString(); //23-03-2021
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
						//Arrays.fill(readBuffer,(byte)0);
						//receivedMsgView.setText(""); //25-01-2021

						mHandler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								receivedMsgView.setText("");
								//Toast.makeText(MainActivityGenusConDis.this,"SNRM",Toast.LENGTH_SHORT).show();
							}
						});	

						//End 18-11-2016 to WriteLog
						Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
						i.putExtra("Key","LT" ); 
						startActivity(i);

					}
					else if (count2<3) 	// use other variable for count - count2
					{						
						count2++;
						//Toast.makeText(UARTLoopbackActivity.this, "Response 2 not received ", Toast.LENGTH_LONG).show();
						//Arrays.fill(readBuffer,(byte)0);//clear buffer
						//receivedMsgView.setText(""); //25-01-2021
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(USBSerial_MainActivity.this,"Data Read Completed.", Toast.LENGTH_LONG).show();
							}
						});
						sb.delete(0, sb.length());//clear sb
						//readBuffer = new byte[65536];
						//autoButtonfuncLG();

					}
					else
					{	
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(USBSerial_MainActivity.this,"Unable to read data from the meter.Take reading Manually.", Toast.LENGTH_LONG).show();
								//Toast.makeText(MainActivityGenusConDis.this,"SNRM",Toast.LENGTH_SHORT).show();
							}
						});	

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
						Intent i = new Intent(USBSerial_MainActivity.this, HomePage.class);
						//i.putExtra("Key","LT6" ); 
						startActivity(i);
						//finish();
					}                                                                               

				}
				catch (Exception e)
				{					
					//Intent i = new Intent(UARTLoopbackActivity.this, LoginActivity.class);
					Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
					//Toast.makeText(USBSerial_MainActivity.this, "LT4 Exec : " + e.toString(), Toast.LENGTH_LONG).show();
					i.putExtra("Key","LT4" );
					startActivity(i);
				}
			}
			else if(count1 < 3)
			{

				//Toast.makeText(UARTLoopbackActivity.this,"Sending 1st Request " + String.valueOf(count1) + "time.", Toast.LENGTH_LONG).show();
				count1++;
				//Arrays.fill(readBuffer,(byte)0);//clear buffer
				//receivedMsgView.setText(""); //25-01-2021
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						receivedMsgView.setText("");
						//Toast.makeText(MainActivityGenusConDis.this,"SNRM",Toast.LENGTH_SHORT).show();
					}
				});
				sb.delete(0, sb.length());//clear sb
				autoButtonfuncLG();				
			}
			else{
				count1 = 0;
				//Toast.makeText(USBSerial_MainActivity.this,"Unable to read data from the meter.Take reading Manually.", Toast.LENGTH_LONG).show();
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(USBSerial_MainActivity.this,"Unable to read data from the meter.Take reading Manually.", Toast.LENGTH_LONG).show();
					}
				});

				Intent i = new Intent(USBSerial_MainActivity.this, HomePage.class);
				//i.putExtra("Key","LT3" ); 
				startActivity(i);
			}
		}
		catch (Exception e)
		{
			//Toast.makeText(USBSerial_MainActivity.this,"Error While Sending 2nd Request.Retry again.", Toast.LENGTH_LONG).show();
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(USBSerial_MainActivity.this,"Error While Sending 2nd Request.Retry again.", Toast.LENGTH_LONG).show();
				}
			});
			//Intent i = new Intent(UARTLoopbackActivity.this, LoginActivity.class);
			Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
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
		//uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
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

			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					receivedMsgView.setText("");
					//Toast.makeText(MainActivityGenusConDis.this,"SNRM",Toast.LENGTH_SHORT).show();
				}
			});
			String readData[] = null; //23-03-2021
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
					//uartInterface.SendData(1,writebuf,(j + 1));
					sendMessage(writebuf); //25-01-2021

				}
				//Toast.makeText(UARTLoopbackActivity.this, String.valueOf(ReqCount) + "Sent", Toast.LENGTH_LONG).show();
				addtime = Calendar.getInstance().getTimeInMillis() + 4000;//4 second delay
				while(addtime > Calendar.getInstance().getTimeInMillis())
				{										

				}
				//readbytes = receivedMsgView.getText().toString().getBytes(); //25-01-2021
				readData = receivedMsgView.getText().toString().trim().split(","); //23-03-2021
				if(readData.length > 1)
				{
					lc = 0;
					for(int k = 0;k<=readData.length-1;k++)
					{
						fos2.write(("2 ").getBytes());
						//db.insertLOGDATA("I", "Saral", "2");
						//String SByte2 = String.format(" %02X",readbytes[k]);
						String SByte2 = " " +readData[k]; //23-03-2021
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
								a = Integer.parseInt(readData[k+2],16); //23-03-2021
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
					//Arrays.fill(readbytes,(byte)0);
				}
				else if(count1 < 3)
				{
					fos2.write(("7 ").getBytes());
					//db.insertLOGDATA("I", "Saral", "7");
					count1++;
					//Arrays.fill(readBuffer,(byte)0);//clear buffer
					//receivedMsgView.setText("");//25-01-2021
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							receivedMsgView.setText("");
							//Toast.makeText(MainActivityGenusConDis.this,"SNRM",Toast.LENGTH_SHORT).show();
						}
					});

					sb.delete(0, sb.length());//clear sb
					autoButtonfunc_DLMS_1_Phase();
				}
				else{
					fos2.write(("8 ").getBytes());
					//db.insertLOGDATA("I", "Saral", "8");
					count1 = 0;

					mHandler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							//receivedMsgView.setText("");
							Toast.makeText(USBSerial_MainActivity.this,"Unable to read data from the meter.Take reading Manually.", Toast.LENGTH_LONG).show();
						}
					});	
					Intent i = new Intent(USBSerial_MainActivity.this, HomePage.class);
					i.putExtra("Key","LT3");
					startActivity(i);
					//finish();
				}
			}
			fos2.write(("9 ").getBytes());
			//db.insertLOGDATA("I", "Saral", "9");
			//Arrays.fill(readBuffer,(byte)0);
			//receivedMsgView.setText("");//25-01-2021
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(USBSerial_MainActivity.this,"Data Read Completed.", Toast.LENGTH_LONG).show();
				}
			});
			//Arrays.fill(readbytes,(byte)0);
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
			Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT" ); 
			startActivity(i);
			//finish();
		}
		catch (Exception e) {
			//db.insertLOGDATA("I", "Saral", "10");
			//Toast.makeText(USBSerial_MainActivity.this,e.toString(), Toast.LENGTH_LONG).show();

			Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT2");
			startActivity(i);
			//finish();
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
		//uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
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
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					receivedMsgView.setText("");
					//Toast.makeText(MainActivityGenusConDis.this,"SNRM",Toast.LENGTH_SHORT).show();
				}
			});
			String readData[] = null; //23-03-2021
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
					//uartInterface.SendData(1,writebuf,(j + 1));
					sendMessage(writebuf); //25-01-2021
				}
				//Toast.makeText(UARTLoopbackActivity.this, String.valueOf(ReqCount) + "Sent", Toast.LENGTH_LONG).show();
				addtime = Calendar.getInstance().getTimeInMillis() + 3750;//4 second delay
				while(addtime > Calendar.getInstance().getTimeInMillis())
				{										

				}
				readData = receivedMsgView.getText().toString().trim().split(","); //23-03-2021
				//writefile.write((receivedMsgView.getText().toString() + "\r\n").getBytes());
				//writelength.write(("TEST").getBytes());
				//writeData = writeData+receivedMsgView.getText().toString()  + "\r\n";


				if(readData.length > 1)
				{
					//s = "FRAME STARTED" + "\r\n";
					s="1 ";
					writelength.write(s.getBytes());
					for(int k = 0;k<=readData.length-1;k++)
					{
						//String SByte2 = String.format(" %02X",readbytes[k]);
						String SByte2 = " " +readData[k]; //23-03-2021
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
								//a = readData[k+2];
								a= Integer.parseInt(readData[k+2],16); //23-03-2021 Convert HexString to int 
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
					//Arrays.fill(readBuffer,(byte)0);
					//Arrays.fill(readbytes,(byte)0);
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							receivedMsgView.setText("");//25-01-2021
						}
					});

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
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(USBSerial_MainActivity.this,"Unable to read data from the meter.Take reading Manually.", Toast.LENGTH_LONG).show();
						}
					});


					Intent i = new Intent(USBSerial_MainActivity.this, HomePage.class);
					i.putExtra("Key","LT3");
					startActivity(i);
				}
			}
			//Arrays.fill(readBuffer,(byte)0);
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(USBSerial_MainActivity.this,"Data Read Completed.", Toast.LENGTH_LONG).show();
				}
			});

			//Toast.makeText(USBSerial_MainActivity.this,"Data Read Completed.", Toast.LENGTH_LONG).show();
			//Arrays.fill(readbytes,(byte)0);
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
			Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT"); 
			startActivity(i);
			//finish();
		}
		catch (Exception e) {
			Toast.makeText(USBSerial_MainActivity.this,e.toString(), Toast.LENGTH_LONG).show();
			Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT2");
			startActivity(i);
			//finish();
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
		//uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
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
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				receivedMsgView.setText("");
				//Toast.makeText(MainActivityGenusConDis.this,"SNRM",Toast.LENGTH_SHORT).show();
			}
		});
		try
		{	
			String readData[] = null; //23-03-2021
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
					//uartInterface.SendData(1,writebuf,(j + 1));
					sendMessage(writebuf); //25-01-2021
				}
				//Toast.makeText(UARTLoopbackActivity.this, String.valueOf(ReqCount) + "Sent", Toast.LENGTH_LONG).show();
				addtime = Calendar.getInstance().getTimeInMillis() + 3750;//4 second delay
				while(addtime > Calendar.getInstance().getTimeInMillis())
				{										

				}
				readData = receivedMsgView.getText().toString().trim().split(","); //23-03-2021
				//writefile.write((receivedMsgView.getText().toString() + "\r\n").getBytes());
				//writelength.write(("TEST").getBytes());
				//writeData = writeData+receivedMsgView.getText().toString()  + "\r\n";


				if(readData.length > 1)
				{
					//s = "FRAME STARTED" + "\r\n";
					s="1 ";
					writelength.write(s.getBytes());
					for(int k = 0;k<=readData.length-1;k++)
					{
						//String SByte2 = String.format(" %02X",readbytes[k]);
						String SByte2 = " " +readData[k]; //23-03-2021
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
								//a = readData[k+2];
								a= Integer.parseInt(readData[k+2],16); //23-03-2021 Convert HexString to int 
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
					//Arrays.fill(readBuffer,(byte)0);
					//Arrays.fill(readbytes,(byte)0);
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							receivedMsgView.setText("");//25-01-2021
						}
					});

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
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(USBSerial_MainActivity.this,"Unable to read data from the meter.Take reading Manually.", Toast.LENGTH_LONG).show();
						}
					});


					Intent i = new Intent(USBSerial_MainActivity.this, HomePage.class);
					i.putExtra("Key","LT3");
					startActivity(i);
				}
			}
			//Arrays.fill(readBuffer,(byte)0);
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(USBSerial_MainActivity.this,"Data Read Completed.", Toast.LENGTH_LONG).show();
				}
			});

			//Toast.makeText(USBSerial_MainActivity.this,"Data Read Completed.", Toast.LENGTH_LONG).show();
			//Arrays.fill(readbytes,(byte)0);
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
			Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT"); 
			startActivity(i);
			//finish();
		}
		catch (Exception e) {
			Toast.makeText(USBSerial_MainActivity.this,e.toString(), Toast.LENGTH_LONG).show();
			Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
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
		//uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
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
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					receivedMsgView.setText("");
					//Toast.makeText(USBSerial_MainActivity.this,"Unable to read data from the meter.Take reading Manually",Toast.LENGTH_SHORT).show();
				}
			});
			String readData[] = null;
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
					//uartInterface.SendData(1,writebuf,(j + 1));
					sendMessage(writebuf); //25-01-2021
				}
				//Toast.makeText(UARTLoopbackActivity.this, String.valueOf(ReqCount) + "Sent", Toast.LENGTH_LONG).show();
				addtime = Calendar.getInstance().getTimeInMillis() + 3750;//4 second delay
				while(addtime > Calendar.getInstance().getTimeInMillis())
				{										

				}
				readData = receivedMsgView.getText().toString().trim().split(","); //23-03-2021
				//writefile.write((receivedMsgView.getText().toString() + "\r\n").getBytes());
				//writelength.write(("TEST").getBytes());
				//writeData = writeData+receivedMsgView.getText().toString()  + "\r\n";


				if(readData.length > 1)
				{
					//s = "FRAME STARTED" + "\r\n";
					s="1 ";
					writelength.write(s.getBytes());
					for(int k = 0;k<=readData.length-1;k++)
					{
						//String SByte2 = String.format(" %02X",readbytes[k]);
						String SByte2 = " " +readData[k]; //23-03-2021
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
								//a = readData[k+2];
								a= Integer.parseInt(readData[k+2],16); //23-03-2021 Convert HexString to int 
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
					//Arrays.fill(readBuffer,(byte)0);
					//Arrays.fill(readbytes,(byte)0);
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							receivedMsgView.setText("");//25-01-2021
						}
					});

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
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(USBSerial_MainActivity.this,"Unable to read data from the meter.Take reading Manually.", Toast.LENGTH_LONG).show();
						}
					});


					Intent i = new Intent(USBSerial_MainActivity.this, HomePage.class);
					i.putExtra("Key","LT3");
					startActivity(i);
				}
			}
			//Arrays.fill(readBuffer,(byte)0);
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(USBSerial_MainActivity.this,"Data Read Completed.", Toast.LENGTH_LONG).show();
				}
			});

			//Toast.makeText(USBSerial_MainActivity.this,"Data Read Completed.", Toast.LENGTH_LONG).show();
			//Arrays.fill(readbytes,(byte)0);
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
			Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT"); 
			startActivity(i);
		}
		catch (Exception e) {
			Toast.makeText(USBSerial_MainActivity.this,e.toString(), Toast.LENGTH_LONG).show();
			Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
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
		//uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
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

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				receivedMsgView.setText("");//25-01-2021
			}
		});
		try
		{	String readData[] = null; //23-03-2021
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
				//uartInterface.SendData(1,writebuf,(j + 1));
				sendMessage(writebuf); //25-01-2021
			}
			//Toast.makeText(UARTLoopbackActivity.this, String.valueOf(ReqCount) + "Sent", Toast.LENGTH_LONG).show();
			addtime = Calendar.getInstance().getTimeInMillis() + 3750;//4 second delay
			while(addtime > Calendar.getInstance().getTimeInMillis())
			{										

			}
			readData = receivedMsgView.getText().toString().trim().split(","); //23-03-2021
			//writefile.write((receivedMsgView.getText().toString() + "\r\n").getBytes());
			//writelength.write(("TEST").getBytes());
			//writeData = writeData+receivedMsgView.getText().toString()  + "\r\n";


			if(readData.length > 1)
			{
				//s = "FRAME STARTED" + "\r\n";
				s="1 ";
				writelength.write(s.getBytes());
				for(int k = 0;k<=readData.length-1;k++)
				{
					//String SByte2 = String.format(" %02X",readbytes[k]);
					String SByte2 = " " +readData[k]; //23-03-2021
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
							//a = readData[k+2];
							a= Integer.parseInt(readData[k+2],16); //23-03-2021 Convert HexString to int 
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
				//Arrays.fill(readBuffer,(byte)0);
				//Arrays.fill(readbytes,(byte)0);
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						receivedMsgView.setText("");//25-01-2021
					}
				});

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
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(USBSerial_MainActivity.this,"Unable to read data from the meter.Take reading Manually.", Toast.LENGTH_LONG).show();
					}
				});


				Intent i = new Intent(USBSerial_MainActivity.this, HomePage.class);
				i.putExtra("Key","LT3");
				startActivity(i);
			}
		}
		//Arrays.fill(readBuffer,(byte)0);
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(USBSerial_MainActivity.this,"Data Read Completed.", Toast.LENGTH_LONG).show();
			}
		});

		//Toast.makeText(USBSerial_MainActivity.this,"Data Read Completed.", Toast.LENGTH_LONG).show();
		//Arrays.fill(readbytes,(byte)0);
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
		Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
		i.putExtra("Key","LT"); 
		startActivity(i);
		}
		catch (Exception e) {
			//Toast.makeText(USBSerial_MainActivity.this,e.toString(), Toast.LENGTH_LONG).show();
			Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
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
		//uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
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
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				receivedMsgView.setText("");//25-01-2021
			}
		});

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
			String readData[] = null; //23-03-2021
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
					//uartInterface.SendData(1,writebuf,(j + 1));
					sendMessage(writebuf); //25-01-2021
				}
				//Toast.makeText(UARTLoopbackActivity.this, String.valueOf(ReqCount) + "Sent", Toast.LENGTH_LONG).show();
				addtime = Calendar.getInstance().getTimeInMillis() + 3750;//4 second delay
				while(addtime > Calendar.getInstance().getTimeInMillis())
				{										

				}
				//readbytes = readBuffer;
				//readbytes = receivedMsgView.getText().toString().getBytes(); //25-01-2021

				//readbytes = receivedMsgView.getText().toString().getBytes();

				readData = receivedMsgView.getText().toString().trim().split(","); //23-03-2021
				//writefile.write((receivedMsgView.getText().toString() + "\r\n").getBytes());
				//writelength.write(("TEST").getBytes());
				//writeData = writeData+receivedMsgView.getText().toString()  + "\r\n";


				if(readData.length > 1)
				{
					//s = "FRAME STARTED" + "\r\n";
					s="1 ";
					writelength.write(s.getBytes());
					for(int k = 0;k<=readData.length-1;k++)
					{
						//String SByte2 = String.format(" %02X",readbytes[k]);
						String SByte2 = " " +readData[k]; //23-03-2021
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
								//a = readData[k+2];
								a= Integer.parseInt(readData[k+2],16); //23-03-2021 Convert HexString to int 
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
					//Arrays.fill(readBuffer,(byte)0);
					//Arrays.fill(readbytes,(byte)0);
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							receivedMsgView.setText("");//25-01-2021
						}
					});

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
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(USBSerial_MainActivity.this,"Unable to read data from the meter.Take reading Manually.", Toast.LENGTH_LONG).show();
						}
					});


					Intent i = new Intent(USBSerial_MainActivity.this, HomePage.class);
					i.putExtra("Key","LT3");
					startActivity(i);
				}
			}
			//Arrays.fill(readBuffer,(byte)0);
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(USBSerial_MainActivity.this,"Data Read Completed.", Toast.LENGTH_LONG).show();
				}
			});

			//Toast.makeText(USBSerial_MainActivity.this,"Data Read Completed.", Toast.LENGTH_LONG).show();
			//Arrays.fill(readbytes,(byte)0);
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
			Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT"); 
			startActivity(i);
			//finish();
		}
		catch (final Exception e) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(USBSerial_MainActivity.this,"Exception." + e.toString(), Toast.LENGTH_LONG).show();
				}
			});

			Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT1");
			startActivity(i);
			//finish();
		}	                   
	}
	//10-05-2021
	public void autoButtonfunc_DLMS_GENUS()
	{
		String mFilepath = Environment.getExternalStorageDirectory().getPath()+"/Download/DLMS_Output.txt";
		File f = new File(mFilepath);	
		//File to = new File(mFilepath)
		//f.renameTo(newPath)
		if(f.exists())
			f.delete();
		//Nitish For LogData 28-11-2016
		String writeData = "";		
		//uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
		StringBuilder sb =new StringBuilder();
		FileOutputStream writefile=null;
		FileOutputStream writelength=null;
		String Stringpath = Environment.getExternalStorageDirectory().getPath()+"/Download" ;
		String sRoot = Environment.getExternalStorageDirectory().getPath();
		String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
		String currentdatetime = new SimpleDateFormat("ddMMyyyyHHmmss").format(Calendar.getInstance().getTime());
		String StringpathLog = sRoot+"/OpticalBilling/"+timeStamp+"/"+"DLMS-GENUS";

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
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				receivedMsgView.setText("");//25-01-2021
			}
		});

		try {
			writefile = new FileOutputStream(Stringpath + "/DLMS_Output.txt",true);
			writelength =new FileOutputStream(StringpathLog + "/DLMS-GENUS_Log_" +IMEINumber+ "_" +currentdatetime+".txt",true);
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Get OutputStream of New Directory Path
		try
		{
			String readData[] = null; //23-03-2021
			for(int ReqCount = 0;ReqCount < DLMSRequestArray_GENUS.length;ReqCount++)
			{
				int a = 0;
				int lc=0;
				SevenECount = 0;
				String s = null;
				byte writebuf[] = new byte[1];
				String[] StringSplit = DLMSRequestArray_GENUS[ReqCount].split(",");
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
					//uartInterface.SendData(1,writebuf,(j + 1));
					sendMessage(writebuf); //25-01-2021
				}
				//Toast.makeText(UARTLoopbackActivity.this, String.valueOf(ReqCount) + "Sent", Toast.LENGTH_LONG).show();
				addtime = Calendar.getInstance().getTimeInMillis() + 3750;//4 second delay
				while(addtime > Calendar.getInstance().getTimeInMillis())
				{										

				}
				//readbytes = readBuffer;
				//readbytes = receivedMsgView.getText().toString().getBytes(); //25-01-2021

				//readbytes = receivedMsgView.getText().toString().getBytes();

				readData = receivedMsgView.getText().toString().trim().split(","); //23-03-2021
				//writefile.write((receivedMsgView.getText().toString() + "\r\n").getBytes());
				//writelength.write(("TEST").getBytes());
				//writeData = writeData+receivedMsgView.getText().toString()  + "\r\n";


				if(readData.length > 1)
				{
					//s = "FRAME STARTED" + "\r\n";
					s="1 ";
					writelength.write(s.getBytes());
					for(int k = 0;k<=readData.length-1;k++)
					{
						//String SByte2 = String.format(" %02X",readbytes[k]);
						String SByte2 = " " +readData[k]; //23-03-2021
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
								//a = readData[k+2];
								a= Integer.parseInt(readData[k+2],16); //23-03-2021 Convert HexString to int 
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
					//Arrays.fill(readBuffer,(byte)0);
					//Arrays.fill(readbytes,(byte)0);
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							receivedMsgView.setText("");//25-01-2021
						}
					});

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
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(USBSerial_MainActivity.this,"Unable to read data from the meter.Take reading Manually.", Toast.LENGTH_LONG).show();
						}
					});


					Intent i = new Intent(USBSerial_MainActivity.this, HomePage.class);
					i.putExtra("Key","LT3");
					startActivity(i);
				}
			}
			//Arrays.fill(readBuffer,(byte)0);
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(USBSerial_MainActivity.this,"Data Read Completed.", Toast.LENGTH_LONG).show();
				}
			});

			//Toast.makeText(USBSerial_MainActivity.this,"Data Read Completed.", Toast.LENGTH_LONG).show();
			//Arrays.fill(readbytes,(byte)0);
			writefile.flush();
			writefile.close();	
			writelength.flush();
			writelength.close();	
			//Start 28-11-2016 to WriteLog
			try
			{
				EncryptDecrypt en = new EncryptDecrypt("nsoft987");										
				final String BillingTest = en.encryptData(writeData.toString(),IMEINumber ,"DLMS-GENUS");
			}
			catch(Exception e1)
			{
				Log.d(" "," ");
			}
			//End 28-11-2016 to WriteLog
			Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT"); 
			startActivity(i);
			//finish();
		}
		catch (final Exception e) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(USBSerial_MainActivity.this,"Exception." + e.toString(), Toast.LENGTH_LONG).show();
				}
			});

			Intent i = new Intent(USBSerial_MainActivity.this, BillingConsumption_LandT.class);
			i.putExtra("Key","LT1");
			startActivity(i);
			//finish();
		}	                   
	}
	
	@Override
	public void performAction(boolean alertResult, int functionality) {
		// TODO Auto-generated method stub

	}

	public static int getByteToIntBytes(byte b) {
		return b & 0xFF;
	}



}
