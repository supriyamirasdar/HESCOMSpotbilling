package in.nsoft.hescomspotbilling;


import java.math.BigDecimal;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.TrafficStats;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MobileStatusActivity extends Activity {
	private static final String TAG = MobileStatusActivity.class.getName();
	TextView lblMobileStatus,lblDCstatus,lblUploadSpeed,lblBatteryLevel,lblDownloadSpeed,lblDataStrength,lblSignalStrength;
	Button btnRefresh;
	Handler mHandler;
	BigDecimal up,down;
	String[] testing = {"", ""};
	ProgressDialog ringProgress;
	CustomPhoneListener ph;
	TelephonyManager mgr1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mobile_status);
		lblMobileStatus = (TextView)findViewById(R.id.lblMobileStatus);
		lblDCstatus = (TextView)findViewById(R.id.lblDCstatus);
		lblUploadSpeed = (TextView)findViewById(R.id.lblUploadSpeed);
		lblBatteryLevel = (TextView)findViewById(R.id.lblBatteryLevel);
		lblDownloadSpeed = (TextView)findViewById(R.id.lblDownloadSpeed);		
		lblDataStrength = (TextView)findViewById(R.id.lblDataStrength);
		lblSignalStrength = (TextView)findViewById(R.id.lblSignalStrength);
		btnRefresh = (Button)findViewById(R.id.btnRefresh);
		mHandler = new Handler();
		//For Signal Strength
		mgr1 = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		ph = new CustomPhoneListener();				
		mgr1.listen(ph, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		if(new ConnectionDetector(MobileStatusActivity.this).isConnectedToInternet())
		{
			//For Data Speed
			getSpeed();
		}
		else
		{
			lblDCstatus.setText("Data Connection		: Inactive");
			lblBatteryLevel.setText("Battery Level 				: "+BatteryLevel()+"%");
			lblUploadSpeed.setText("Upload Speed 				: "+"0 kbps");
			lblDownloadSpeed.setText("Download Speed 		: "+"0 kbps");
			lblDataStrength.setText("Data Strength 				: Offline");
			lblSignalStrength.setText("Signal Strength 			: "+CustomPhoneListener.SIGNAL_STRENGTH);
		}

		btnRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onCreate(new Bundle()); //Refresh or Load Again			
			}
		});


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.spotbillingmenu, menu);
		return true;
	}	
	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return OptionsMenu.navigate(item, MobileStatusActivity.this);		
	}*/
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

	public void getSpeed()
	{
		try 
		{
			ringProgress = ProgressDialog.show(MobileStatusActivity.this, "Please wait..", "Calculating download and upload speed...",true);
			new Thread(new Runnable() {

				@Override
				public void run() {
					try 
					{
						if(new ConnectionDetector(MobileStatusActivity.this).isConnectedToInternet())
						{

							HttpClient httpclt = new DefaultHttpClient();//object for HttpClient
							HttpPost httpPost = new HttpPost("http://www.google.com");//HttpPost method uri
							httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");		
							//HttpPost httpPost = new HttpPost(ConstantClass.IPAddress);//HttpPost method uri
							//HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/getCurrentDateTime");//HttpPost method uri
							//long TotalRxBeforeTest = TrafficStats.getTotalTxBytes();
							//long TotalTxBeforeTest = TrafficStats.getTotalRxBytes();
							long TotalRxBeforeTest = TrafficStats.getTotalRxBytes();
							long TotalTxBeforeTest = TrafficStats.getTotalTxBytes();
							long BeforeTime = System.currentTimeMillis();

							
							
											
							HttpResponse res = httpclt.execute(httpPost);//execute post method
							HttpResponse res1 = httpclt.execute(httpPost);//execute post method
							HttpResponse res2 = httpclt.execute(httpPost);//execute post method
							HttpResponse res3 = httpclt.execute(httpPost);//execute post method
							HttpResponse res4 = httpclt.execute(httpPost);//execute post method				
							
							
							//long TotalRxAfterTest = TrafficStats.getTotalTxBytes();
							//long TotalTxAfterTest = TrafficStats.getTotalRxBytes();
							long AfterTime = System.currentTimeMillis();
							long TotalRxAfterTest = TrafficStats.getTotalRxBytes();
							long TotalTxAfterTest = TrafficStats.getTotalTxBytes();
						

							double TimeDifference = AfterTime-BeforeTime;
							double rxDiff = TotalRxAfterTest- TotalRxBeforeTest;
							double txDiff = TotalTxAfterTest- TotalTxBeforeTest;
							if((rxDiff!=0) && (txDiff!=0))
							{
								double rxBPs = (rxDiff/(TimeDifference/1000));//total rx bytes per second

								double txBPs = (txDiff/(TimeDifference/1000));//total tx bytes per second


								/*testing[0] = String.valueOf(rxBPs) + "bps.Total rx = " + rxDiff;
								testing[1] = String.valueOf(txBPs) + "bps.Total tx = " + txDiff;*/
								up = (new BigDecimal(txBPs).divide(new BigDecimal(1024)));//.divide(new BigDecimal(1024));
								down = (new BigDecimal(rxBPs).divide(new BigDecimal(1024)));//.divide(new BigDecimal(1024));
								testing[0] = String.valueOf(up.setScale(2, BigDecimal.ROUND_HALF_UP)) + " kbps";
								testing[1] = String.valueOf(down.setScale(2, BigDecimal.ROUND_HALF_UP)) + " kbps";
								
							}
							else
							{
								testing[0] = "No uploaded or downloaded bytes";
								testing[1] = "No uploaded or downloaded bytes";
							}

							mHandler.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									try
									{
										lblDCstatus.setText("Data Connection		: Active");
										lblBatteryLevel.setText("Battery Level 				: "+BatteryLevel()+"%");
										lblUploadSpeed.setText("Upload Speed 				: "+testing[0]);
										lblDownloadSpeed.setText("Download Speed 		: "+testing[1]);
										if(down.compareTo(new BigDecimal(3.0)) > 0)
											lblDataStrength.setText("Data Strength 				: " + ConstantClass.sHIGH);
										else if(down.compareTo(new BigDecimal(1.0)) > 0)
											lblDataStrength.setText("Data Strength 				: "+ ConstantClass.sNORMAL);
										else
											lblDataStrength.setText("Data Strength 				: "+ ConstantClass.sLOW);
										
										lblSignalStrength.setText("Signal Strength 			: "+CustomPhoneListener.SIGNAL_STRENGTH);
										ringProgress.dismiss();
									}
									catch(Exception e)
									{
										Log.d(TAG, e.toString());
									}
								}
							});
						}
						else
						{
							mHandler.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									try
									{
										lblDCstatus.setText("Data Connection		: Inactive");
										lblBatteryLevel.setText("Battery Level 				: "+BatteryLevel()+"%");
										lblUploadSpeed.setText("Upload Speed 				: "+"0 kbps");
										lblDownloadSpeed.setText("Download Speed 		: "+"0 kbps");
										lblDataStrength.setText("Data Strength 			: Offline");
										lblSignalStrength.setText("Signal Strength 			: "+CustomPhoneListener.SIGNAL_STRENGTH);
										CustomToast.makeText(MobileStatusActivity.this, "Data Connection Problem.", Toast.LENGTH_SHORT);
										ringProgress.dismiss();
									}
									catch(Exception e)
									{
										Log.d(TAG, e.toString());
									}
								}
							});
						}
					} 
					catch (Exception e) 
					{
						Log.d(TAG, e.toString());
						//ringProgress.dismiss();
						if(e.toString().contains("timeout"))
						{
							mHandler.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									try
									{
										lblDCstatus.setText("Data Connection		: Inactive");
										lblBatteryLevel.setText("Battery Level 				: "+BatteryLevel()+"%");
										lblUploadSpeed.setText("Upload Speed 				: "+"0 kbps");
										lblDownloadSpeed.setText("Download Speed 		: "+"0 kbps");
										lblDataStrength.setText("Data Strength 			: Offline");
										lblSignalStrength.setText("Signal Strength 			: "+CustomPhoneListener.SIGNAL_STRENGTH);
										CustomToast.makeText(MobileStatusActivity.this, "Data Connection Problem.", Toast.LENGTH_SHORT);
										ringProgress.dismiss();
									}
									catch(Exception e)
									{
										Log.d(TAG, e.toString());
									}
								}
							});
						}
					}
				}
			}).start();
		} 
		catch (Exception e) 
		{
			Log.d(TAG, e.toString());
			//ringProgress.dismiss();
		}
	}

	public String[] DownloadUploadSpeed()
	{
		String[] testing ={""};
		long BeforeTime = System.currentTimeMillis();
		long TotalRxBeforeTest = TrafficStats.getTotalTxBytes();
		long TotalTxBeforeTest = TrafficStats.getTotalRxBytes();


		long TotalRxAfterTest = TrafficStats.getTotalTxBytes();
		long TotalTxAfterTest = TrafficStats.getTotalRxBytes();
		long AfterTime = System.currentTimeMillis();

		double TimeDifference = AfterTime-BeforeTime;
		double rxDiff = TotalRxAfterTest- TotalRxBeforeTest;
		double txDiff = TotalTxAfterTest- TotalTxBeforeTest;
		if((rxDiff!=0) && (txDiff!=0))
		{
			double rxBPs = (rxDiff/(TimeDifference/1000));//total rx bytes per second

			double txBPs = (txDiff/(TimeDifference/1000));//total tx bytes per second


			testing[0] = String.valueOf(rxBPs) + "bps.Total rx = " + rxDiff;
			testing[1] = String.valueOf(txBPs) + "bps.Total tx = " + txDiff;
		}
		else
		{
			testing[0] = "No uploaded or downloaded bytes";
		}

		return testing;
	}
	/*public String UploadSpeed()
	{
		String[] testing1 = null;
		long BeforeTime = System.currentTimeMillis();
		long TotalRxBeforeTest = TrafficStats.getTotalTxBytes();
		long TotalTxBeforeTest = TrafficStats.getTotalRxBytes();
		long TotalRxAfterTest = TrafficStats.getTotalTxBytes();
		long TotalTxAfterTest = TrafficStats.getTotalRxBytes();
		long AfterTime = System.currentTimeMillis();

		double TimeDifference = AfterTime-BeforeTime;
		double rxDiff = TotalRxAfterTest- TotalRxBeforeTest;
		double txDiff = TotalTxAfterTest- TotalTxBeforeTest;
		if((rxDiff!=0) && (txDiff!=0))
		{
			double rxBPs = (rxDiff/(TimeDifference/1000));//total rx bytes per second

			double txBPs = (txDiff/(TimeDifference/1000));//total tx bytes per second


			testing1[0] = String.valueOf(rxBPs) + "bps.Total rx = " + rxDiff;
			testing1[1] = String.valueOf(txBPs) + "bps.Total tx = " + txDiff;
		}
		else
		{
			testing1[0] = "No uploaded or downloaded bytes";
		}

		return testing1[0];
	}*/
}
