package in.nsoft.hescomspotbilling;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class BluetoothPrinting {	
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothSocket mmSocket;
	private BluetoothDevice mmDevice;
	private OutputStream mmOutputStream;
	private InputStream mmInputStream;
	volatile boolean stopWorker;
	private Context mContext;
	private static String TAG="in.nsoft.spotbilling.BluetoothPrinting";
	private boolean isReady = false;
	int Count = 0;
	static boolean isConnected = false;

	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

	public BluetoothPrinting(Context ctx)
	{
		this.mContext=ctx;
	}

	///Finds The Analogic Printer
	public boolean findBT(){
		try {
			boolean isOn=false;
			mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
			if (mBluetoothAdapter==null)
			{
				Toast.makeText(mContext, "Your Device Do Not Support Bluetooth Functionality", Toast.LENGTH_SHORT).show();
				return isOn;
			}
			if(!mBluetoothAdapter.isEnabled())
			{
				Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				((Activity)mContext).startActivityForResult(enableBluetooth, 0);
			}
			else
			{
				
				isOn = true;
			}
			//Tamilselvan on 01-03-2014			
			/*while(!isReady)//For waiting until bluetooth turn on
			{
				//
			}*/
			//BluetoothDevice bthde = mBluetoothAdapter.getRemoteDevice("ANTHERMAL");
			
			/*Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();//get paired or Bonded Device
			
			if(pairedDevices.size()>0)
			{
				for(BluetoothDevice device:pairedDevices)
				{
					if(device.getName().equals("ANTHERMAL"))
					{
						mmDevice = device;
						break;
					}
				}
			}
			else//Added by Tamilselvan on 15-03-2014
			{
				isOn = false;//pairedDevices size 0 then no printer or no Paired devices
			}*/
			mmDevice = DynamicBluetooth.getBth();
			//Added by Tamilselvan on 15-03-2014
			if(mmDevice == null)//mmDevice is null then bluetooth device is not connected
			{
				isOn = false;//
			}
			return isOn;
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
			Log.d(TAG + "1", e.toString());
			return false;
		}
	}



	//open A connection to the bluetooth Printer Device

	@SuppressLint("NewApi")
	public void openBT() throws IOException{
		try
		{
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			if(mBluetoothAdapter.isEnabled())
			{
				if(DynamicBluetooth.getBth() == null)
				{
					CustomToast.makeText(mContext, "Data Saved. Printing problem.", Toast.LENGTH_LONG);
					return;
				}
				mmDevice = DynamicBluetooth.getBth();
				UUID uuid =UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//00001101-0000-1000-8000-00805f9b34fb
				Log.d(TAG, "Executing");
				mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);//Comment for old printer
				//mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(uuid);//27-05-2014
				Log.d(TAG, "Executed");
				//Method m = mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
				//mmSocket = (BluetoothSocket)m.invoke(mmDevice, 1);
				mBluetoothAdapter.cancelDiscovery();
				Log.d(TAG, "Discovery");
				mmSocket.connect();
				/*while(!mmSocket.isConnected()) 
				{
					mmSocket.connect();
				}*/
				isConnected = true;
				Log.d(TAG, "Connect");
				mmOutputStream=mmSocket.getOutputStream();
				mmInputStream = mmSocket.getInputStream();

				if(mmDevice.getBondState() == BluetoothDevice.BOND_BONDED)
				{
					Log.d(TAG,"Bonded");
				}
				else if(mmDevice.getBondState() == BluetoothDevice.BOND_BONDING)
				{
					Log.d(TAG,"Bonding");
				}
				else if(mmDevice.getBondState() == BluetoothDevice.BOND_NONE)
				{
					Log.d(TAG,"None");
				}
			} 
		}
		catch(Exception e)
		{
			Log.d(TAG + "2", e.toString());
			mmSocket.close();
			if(e.toString().contains("Service discovery failed"))//Added by Tamilselvan on 15-03-2014
			{
				if(Count < 2)
				{
					try {
						Thread.sleep(10000);
						isConnected = false;
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Count++;
					openBT();
				}
				else
				{
					CustomToast.makeText(mContext, "Check printer device is ON or OFF", Toast.LENGTH_LONG);
				}
			}
		}

	}


	public void beginListenForData(){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.reset();
		while(true)
		{
			int treshHold = 0;
			int b = 0;
			try
			{
				try
				{
					while(mmInputStream.available() == 0 && treshHold < 3000)
					{
						Thread.sleep(1);
						treshHold++;
					}
				}
				catch(IOException e1)
				{
					e1.printStackTrace();
				}
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			if(treshHold < 3000)
			{
				treshHold = 0;
				try {
					b = mmInputStream.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//readBytes++;
			}
			else 
			{
				break;
			}
			baos.write(b);
		}
	}


	public void sendData(byte[] data)throws IOException{
		try { 
			mmOutputStream.write(data);
			mmOutputStream.flush();

		} catch (Exception e) { 
			// TODO: handle exception
			Log.d(TAG + "3", e.toString());
		}
	}


	public void closeBT() throws IOException{
		try {
			stopWorker=true;
			mmOutputStream.close();
			mmInputStream.close();
			mmSocket.close();

		} catch (Exception e) {
			// TODO: handle exception
			Log.d(TAG +"4", e.toString());
		}
	}
}
