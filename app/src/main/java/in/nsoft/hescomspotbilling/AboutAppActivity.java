package in.nsoft.hescomspotbilling;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import java.math.BigDecimal;



public class AboutAppActivity extends Activity {

	private TextView lblAppVersionCode, lblAppVersionName, lblIMEINo, lblSIMSerialNo, lblFileVersion, lblBatteryLevel;
	private static final String TAG = AboutAppActivity.class.getName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_app);

		lblAppVersionCode = (TextView)findViewById(R.id.lblAppVersionCode);
		lblAppVersionName = (TextView)findViewById(R.id.lblAppVersionName);
		lblIMEINo = (TextView)findViewById(R.id.lblIMEINo); 
		lblSIMSerialNo = (TextView)findViewById(R.id.lblSIMSerialNo);
		lblFileVersion = (TextView)findViewById(R.id.lblFileVersion);
		lblBatteryLevel = (TextView)findViewById(R.id.lblBatteryLevel);
		try
		{
			PackageManager mgr= this.getPackageManager();
			PackageInfo pkgInfo = mgr.getPackageInfo(this.getPackageName(), 0);
			lblAppVersionCode.setText("App Version Code 		: "+pkgInfo.versionCode);
			lblAppVersionName.setText("App Version Name 	: "+pkgInfo.versionName);
			//TelephonyManager teleMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
			lblIMEINo.setText("DEV NO. 							: "+LoginActivity.IMEINumber); //10-03-2021
			lblSIMSerialNo.setText("SERIAL NO  : "+LoginActivity.SimNumber); //10-03-2021
			lblFileVersion.setText("File version 					: "+ConstantClass.FileVersion);
			lblBatteryLevel.setText("Battery Level 				: "+BatteryLevel()+"%");
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}
//Tamilselvan on 13-05-2014
	/**
	 * Get Battery Percent
	 * @return
	 */
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
	}/**/
	
	//Modified Nitish 27-02-2014
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.		
		//getMenuInflater().inflate(R.menu.spotbillingmenu, menu);
		return true;
	}
	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return OptionsMenu.navigate(item, AboutAppActivity.this);			
	}*/
}
