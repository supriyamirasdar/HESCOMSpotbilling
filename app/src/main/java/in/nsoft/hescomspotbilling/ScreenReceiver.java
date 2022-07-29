package in.nsoft.hescomspotbilling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
/**
 * 
 * @author Tamilselvan on 02-05-2014
 *
 */
public class ScreenReceiver extends BroadcastReceiver{
	
	private static final String TAG = ScreenReceiver.class.getName();
	
	@Override
	public void onReceive(Context ctx, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
		{
			Log.d(TAG,"ACTION_SCREEN_OFF");
		}
		else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON))
		{
			Log.d(TAG,"ACTION_SCREEN_ON");
			//Intent i = new Intent(ctx, BillingConsumption.class);
			//startActivity(i);
		}
		else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT))
		{
			Log.d(TAG,"ACTION_USER_PRESENT");
		}
	}

}
