package in.nsoft.hescomspotbilling;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

//Created By: Tamilselvan on 27-01-2014 for Network connection Check
public class ConnectionDetector {
	private Context _context;
	public ConnectionDetector(Context context)
	{
		this._context = context;
	}
	//Created By: Tamilselvan on 27-01-2014
	public synchronized boolean isConnectedToInternet()
	{
		ConnectivityManager connectivity =(ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if(info != null)
			{
				for(int i = 0; i < info.length; i++)
				{
					if(info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
}
