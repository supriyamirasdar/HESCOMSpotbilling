package in.nsoft.hescomspotbilling;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DynamicBluetooth extends BroadcastReceiver {

	private static BluetoothDevice bth = null;
	private static boolean isgot = false;
	private static final String TAG = "Blue";//DynamicBluetooth.class.getName();
	static String bluetoothDeviceName = "";  //11-07-2016
	
	//Added on 09-06-2014
	public synchronized static BluetoothDevice getBth() {
		return bth;
	}


	public synchronized static void setBth(BluetoothDevice bth) {
		DynamicBluetooth.bth = bth;
	}


	public synchronized static boolean isIsgot() {
		return isgot;
		
	}


	public synchronized static void setIsgot(boolean isgot) {
		DynamicBluetooth.isgot = isgot;
		
		
	}


	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		try
		{
			
			String action = arg1.getAction();
			if(BluetoothDevice.ACTION_FOUND.equals(action))
			{
				BluetoothDevice bthde = arg1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Log.d(TAG, bthde.getAddress());
				Log.d(TAG, bthde.getName());
				if(bthde != null && bthde.getName() != null)
				{
					//if(!isgot && bthde.getName().equals("ANTHERMAL"))
					if(!isgot && ( bthde.getName().contains("AMIGOS") || bthde.getName().contains("amigos") || bthde.getName().contains("Amigos") || bthde.getName().contains("MTP")  || bthde.getName().contains("MPT") || bthde.getName().contains("GVM")||  bthde.getName().contains("G-8BT") || bthde.getName().toUpperCase().startsWith("AM"))) //20-11-2020	
					{
						bth = bthde;
						isgot = true;
						bluetoothDeviceName = bthde.getName().toString().trim(); //11-07-2016
						Log.d(TAG, String.valueOf(isgot));
					}
					if(isgot && (bthde.getName().contains("AMIGOS") || bthde.getName().contains("amigos") ||  bthde.getName().contains("Amigos") || bthde.getName().contains("MTP") || bthde.getName().contains("MPT") || bthde.getName().contains("GVM") ||  bthde.getName().contains("G-8BT") || bthde.getName().toUpperCase().startsWith("AM"))) //20-11-2020	
					{
						bluetoothDeviceName = bthde.getName().toString().trim(); 
					}				
				}
			}
			/*else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action))
			{
				BluetoothDevice bthde = arg1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Log.d(TAG, bthde.getAddress());
				Log.d(TAG, bthde.getName());
				if(bthde != null && bthde.getName() != null)
				{
					//if(!isgot && bthde.getName().equals("BMV2"))
					//if(!isgot && bthde.getName().equals("SEWOO PRINTER"))
					int b = bthde.getBondState();
					//if(!isgot && (bthde.getName().startsWith("CIE-EXEL") || bthde.getName().startsWith("Dual_SPP")))//05-12-2015	
					if((bthde.getName().startsWith("CIE-EXEL") || bthde.getName().startsWith("Dual_SPP")))//05-12-2015	
					{
						bth = bthde;
						isgot = true;
						bluetoothDeviceName = bthde.getName().toString().trim(); //11-07-2016
						Log.d(TAG, String.valueOf(isgot));
					}					
				}
			}*/
			
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

}
