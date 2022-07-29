package in.nsoft.hescomspotbilling;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TimeChangedReceiver extends BroadcastReceiver
{
	Thread th;
	private static final String TAG = TimeChangedReceiver.class.getName();
	Context ctx;
	long diff;
	Notification nm ;
	private static boolean isValid=false;
	
	public synchronized static boolean isValid() {
		return isValid;
	}

	public synchronized static void setValid(boolean isValid) {
		TimeChangedReceiver.isValid = isValid;
	}

	//static volatile boolean contTh = true;
	@Override
	public void onReceive(Context arg0, Intent arg1)
	{
		try
		{
			isValid = false;
			ctx = arg0;
			CollectionObject.GetCollectionObject().setmIsTimeSync(false);
			DatabaseHelper db = new DatabaseHelper(arg0);
			db.UpdateStatusInStatusMaster("3", "0");
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}	

}


