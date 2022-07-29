package in.nsoft.hescomspotbilling;

import java.lang.reflect.Method;
import java.util.Set;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BluethoothDiscoveryActivity extends Activity implements AlertInterface{

	Button btnPairBluetooth; 
	private BluetoothAdapter mBluetoothAdapter;
	Handler mHandler;
	ProgressDialog ringProgress;
	int counter = 0, OverallCount = 0;  
	static final int REQUEST_ENABLE_BT = 0;
	private static final String TAG = BluethoothDiscoveryActivity.class.getName();
	BluetoothPrinting btpr;

	TextView lblSBBlinkText;
	DatabaseHelper db = new DatabaseHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluethooth_discovery);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		btnPairBluetooth = (Button)findViewById(R.id.btnPairBluetooth);
		lblSBBlinkText = (TextView)findViewById(R.id.lblSBBlinkText);
		mHandler = new Handler();

		//#################livetest TextView  for blinking  and color#########################
		lblSBBlinkText.setTextColor(Color.parseColor("#FF0000")); //red color
		lblSBBlinkText.setTextSize(14); 		
		/*Animation anim=new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(800); //blinkig duration
		anim.setStartOffset(10);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(Animation.INFINITE);
		lblSBBlinkText.startAnimation(anim);*/
		//#####Live and Test  Validation ########
		//#####Live and Test  Validation ########
		if(ConstantClass.IPAddress.equals("http://123.201.131.113:8112/Hescom.asmx"))
		{
			lblSBBlinkText.setText(ConstantClass.mTest+ ConstantClass.FileVersion);
		}
		else
		{
			lblSBBlinkText.setText(ConstantClass.mLive+ ConstantClass.FileVersion);
		}
		//###############################################################################################

		btnPairBluetooth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
				if(!mBluetoothAdapter.isEnabled())
				{
					//CustomToast.makeText(BluethoothDiscoveryActivity.this, "Check bluetooth in your device. If it is off then on bluetooth.", Toast.LENGTH_SHORT);
					Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					BluethoothDiscoveryActivity.this.startActivityForResult(enableBluetooth, 0);
					return;
				}
				else
				{
					//RemovePairedDevices();
					CheckBluetooth();
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
		return OptionsMenu.navigate(item,BluethoothDiscoveryActivity.this);		
	}	
	//Tamilselvan on 18-03-2014
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		CustomToast.makeText(BluethoothDiscoveryActivity.this, "Please use menu to navigate.", Toast.LENGTH_SHORT);
		return;
	}

	@Override
	public void performAction(boolean alertResult, int functionality) {
		// TODO Auto-generated method stub

	}

	protected void onActivityResult(int requestCode,int resultCode,Intent Data) {
		if(requestCode==REQUEST_ENABLE_BT)
		{
			if(resultCode == RESULT_OK)
			{
				CheckBluetooth();
			}
			else
			{
				CustomToast.makeText(this, "You have cancelled bluetooth request hence pairing not possible", Toast.LENGTH_SHORT);
			}
		}		
	}
	/**
	 * Remove Paired Bluetooth device 
	 * added on 31-07-2014
	 */
	public void RemovePairedDevices()
	{
		try
		{
			//get all paired bluetooth device
			Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
			if(pairedDevices.size() > 0)
			{
				for(BluetoothDevice bth : pairedDevices)
				{
					try 
					{
						//if(bth.getName().equals("ANTHERMAL"))
						if(bth.getName().contains("AMIGOS") || bth.getName().contains("amigos")  || bth.getName().contains("Amigos") || bth.getName().contains("MTP")  || bth.getName().contains("MPT") || bth.getName().contains("GVM") ||  bth.getName().contains("G-8BT") || bth.getName().toUpperCase().startsWith("AM")) //20-11-2020
						{
							Method mth = bth.getClass().getMethod("removeBond", (Class[])null);
							mth.invoke(bth, (Object[])null);
						}
					}
					catch (Exception e) 
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
	}
	public void CheckBluetooth()
	{
		ringProgress = ProgressDialog.show(BluethoothDiscoveryActivity.this, "Please wait..", "Pairing Bluetooth Printer...",true);//Spinner
		ringProgress.setCancelable(false);


		mBluetoothAdapter.startDiscovery();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(!DynamicBluetooth.isIsgot() && counter < 15)
				{
					try 
					{
						counter++;
						Thread.sleep(2000);

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				mBluetoothAdapter.cancelDiscovery();
				//Comment for Old Printer
				btpr = new BluetoothPrinting(BluethoothDiscoveryActivity.this);
				try
				{
					if(DynamicBluetooth.isIsgot())
					{ 
						btpr.openBT();
						btpr.closeBT();
					}
				}
				catch(Exception e)
				{
					Log.d(TAG, e.toString());
				}
				//Comment for Old Printer End
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						ringProgress.dismiss();
						if(DynamicBluetooth.isIsgot())
						{ 
							//11-07-2016									
							CustomToast.makeText(BluethoothDiscoveryActivity.this, "Bluetooth Device " +  DynamicBluetooth.bluetoothDeviceName + " Connected" , Toast.LENGTH_LONG);
							try
							{
								db.EventLogSave("2", LoginActivity.IMEINumber, LoginActivity.SimNumber, "Bluetooth Pairing: " +  DynamicBluetooth.bluetoothDeviceName.toString().trim()); //15-07-2016
							}
							catch(Exception e)
							{
								
							}
							Intent i = new Intent(BluethoothDiscoveryActivity.this, HomePage.class);//
							startActivity(i);
						}
						else
						{
							OverallCount++;
							counter = 0;
							if(OverallCount<2)
							{
								CheckBluetooth();
							}
							else
							{
								OverallCount = 0;
								CustomToast.makeText(BluethoothDiscoveryActivity.this, "Bluetooth device not found. Please check whether bluethooth device is ON.", Toast.LENGTH_SHORT);
							}
						}
					}
				});
			}
		}).start();
	}
}
