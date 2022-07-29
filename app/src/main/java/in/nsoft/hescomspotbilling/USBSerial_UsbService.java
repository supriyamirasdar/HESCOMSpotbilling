package in.nsoft.hescomspotbilling;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.felhr.usbserial.CDCSerialDevice;
import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;



import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;



public class USBSerial_UsbService extends Service {
	private static final String TAG = "UsbService";
	private static final boolean DBG = true;

	public static final String ACTION_USB_NOT_SUPPORTED = "in.nsoft.hescomspotbilling.USB_NOT_SUPPORTED";
	public static final String ACTION_NO_USB = "in.nsoft.hescomspotbilling.NO_USB";
	public static final String ACTION_USB_PERMISSION_GRANTED = "in.nsoft.hescomspotbilling.USB_PERMISSION_GRANTED";
	public static final String ACTION_USB_PERMISSION_NOT_GRANTED = "in.nsoft.hescomspotbilling.USB_PERMISSION_NOT_GRANTED";
	public static final String ACTION_USB_DISCONNECTED = "in.nsoft.hescomspotbilling.USB_DISCONNECTED";
	public static final String ACTION_SERIAL_CONFIG_CHANGED = "in.nsoft.hescomspotbilling.SERIAL_CONFIG_CHANGED";

	private static final String ACTION_USB_READY = "in.nsoft.hescomspotbilling.USB_READY";
	private static final String ACTION_CDC_DRIVER_NOT_WORKING = "in.nsoft.hescomspotbilling.ACTION_CDC_DRIVER_NOT_WORKING";
	private static final String ACTION_USB_DEVICE_NOT_WORKING = "in.nsoft.hescomspotbilling.ACTION_USB_DEVICE_NOT_WORKING";
	private static final String ACTION_USB_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
	private static final String ACTION_USB_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
	private static final String ACTION_USB_PERMISSION = "in.nsoft.hescomspotbilling.USB_PERMISSION";

	public static final int MESSAGE_FROM_SERIAL_PORT = 0;
	public static final int CTS_CHANGE = 1;
	public static final int DSR_CHANGE = 2;

	static boolean SERVICE_CONNECTED = false;

	private final IBinder binder = new UsbBinder();

	private Context context;
	private Handler mHandler;

	private final UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() {
		@Override
		public void onReceivedData(byte[] arg) {
			try {
				/*String data = new String(arg, USBSerial_Constants.CHARSET);
				if (mHandler != null) {
					mHandler.obtainMessage(MESSAGE_FROM_SERIAL_PORT, data).sendToTarget();
				}*/

				//Modified 23-03-2021
				StringBuilder sb = new StringBuilder();
				String data="";

				if(Billing_LandT.LTLG==1 || Billing_LandT.LTLG==2 || Billing_LandT.LTLG==7) //L&T and L&G
				{
					data = new String(arg, USBSerial_Constants.CHARSET);
					if (mHandler != null) {
						mHandler.obtainMessage(MESSAGE_FROM_SERIAL_PORT, data).sendToTarget();
					}
				}
				else //DLMS
				{
					for(int i=0;i<arg.length;i++)
					{
						data = String.format("%02X,",arg[i]);
						sb.append(data.toString());
					}				 
					if (mHandler != null) {
						//mHandler.obtainMessage(BYTE_FROM_SERIAL_PORT, arg).sendToTarget();					 
						mHandler.obtainMessage(MESSAGE_FROM_SERIAL_PORT, sb.toString()).sendToTarget();
					}
				}
			} catch (Exception e) {
				Log.d(TAG, e.toString());
			}
		}
	};

	private final UsbSerialInterface.UsbCTSCallback ctsCallback = new UsbSerialInterface.UsbCTSCallback() {
		@Override
		public void onCTSChanged(boolean state) {
			if (mHandler != null) {
				mHandler.obtainMessage(CTS_CHANGE).sendToTarget();
			}
		}
	};

	private final UsbSerialInterface.UsbDSRCallback dsrCallback = new UsbSerialInterface.UsbDSRCallback() {
		@Override
		public void onDSRChanged(boolean state) {
			if (mHandler != null) {
				mHandler.obtainMessage(DSR_CHANGE).sendToTarget();
			}
		}
	};

	private UsbManager usbManager;
	private UsbDevice device;
	private UsbDeviceConnection connection;
	private UsbSerialDevice serialPort;
	private boolean serialPortConnected;

	private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			switch (intent.getAction()) {
			case ACTION_USB_PERMISSION:
				boolean granted =
				intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
				if (granted) {
					Intent in = new Intent(ACTION_USB_PERMISSION_GRANTED);
					context.sendBroadcast(in);
					connection = usbManager.openDevice(device);
					new ConnectionThread().start();
				} else {
					Intent in = new Intent(ACTION_USB_PERMISSION_NOT_GRANTED);
					context.sendBroadcast(in);
				}
				break;
			case ACTION_USB_ATTACHED:
				if (!serialPortConnected) {
					findSerialPortDevice();
				}
				break;
			case ACTION_USB_DETACHED:
				Intent in = new Intent(ACTION_USB_DISCONNECTED);
				context.sendBroadcast(in);
				if (serialPortConnected) {
					serialPort.close();
				}
				serialPortConnected = false;
				break;
			case ACTION_SERIAL_CONFIG_CHANGED:
				// new ConnectionThread().start();
				if (serialPortConnected) {
					Log.d(TAG, "Restart Connection");
					serialPort.close();
					connection = usbManager.openDevice(device);
					new ConnectionThread().start();
				}
				else
				{
					Handler handler = new Handler(Looper.getMainLooper());
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), " NEED TO CONNECT SERIAL PORT  " ,
									Toast.LENGTH_SHORT).show();
						}
					});
				}
				break;
			default:
				Log.e(TAG, "Unknown action");
				break;
			}
		}
	};

	@Override
	public void onCreate() {
		this.context = this;

		serialPortConnected = false;
		USBSerial_UsbService.SERVICE_CONNECTED = true;
		setFilter();
		usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
		findSerialPortDevice();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		USBSerial_UsbService.SERVICE_CONNECTED = false;
	}

	public void write(byte[] data) {
		if (serialPort != null) {
			serialPort.write(data);
		}
	}

	public void setHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	private void findSerialPortDevice() {
		HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();

		if (usbDevices.isEmpty()) {
			Intent intent = new Intent(ACTION_NO_USB);
			sendBroadcast(intent);
			return;
		}

		boolean keep = true;
		for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
			device = entry.getValue();
			int deviceVID = device.getVendorId();
			int devicePID = device.getProductId();

			if (DBG) {
				Log.d(TAG, "VendorID: " + deviceVID + ", ProductID: " + devicePID);
			}

			if (deviceVID != 0x1d6b
					&&(devicePID != 0x0001 && devicePID != 0x0002 && devicePID != 0x0003)) {
				requestUserPermission();
				keep = false;
			} else {
				connection = null;
				device = null;
			}

			if (!keep) {
				break;
			}
		}

		if (!keep) {
			Intent intent = new Intent(ACTION_NO_USB);
			sendBroadcast(intent);
		}
	}

	private void setFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_USB_PERMISSION);
		filter.addAction(ACTION_USB_DETACHED);
		filter.addAction(ACTION_USB_ATTACHED);
		filter.addAction(ACTION_SERIAL_CONFIG_CHANGED);
		registerReceiver(usbReceiver, filter);
	}

	private void requestUserPermission() {
		PendingIntent mPendingIntent =
				PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
		usbManager.requestPermission(device, mPendingIntent);
	}

	public class UsbBinder extends Binder {
		public USBSerial_UsbService getService() {
			return USBSerial_UsbService.this;
		}
	}

	private class ConnectionThread extends Thread {
		@Override
		public void run() {
			serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
			if (serialPort == null) {
				Intent intent = new Intent(ACTION_USB_NOT_SUPPORTED);
				context.sendBroadcast(intent);
				return;
			}

			if (serialPort.open()) {
				serialPortConnected = true;

				SharedPreferences pref =
						PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

				/* serialPort.setBaudRate(Integer.parseInt(pref.getString(getString(R.string.baudrate_key),
                        getResources().getString(R.string.baudrate_default))));
                serialPort.setDataBits(Integer.parseInt(pref.getString(getString(R.string.databits_key),
                        getResources().getString(R.string.databits_default))));
                serialPort.setStopBits(Integer.parseInt(pref.getString(getString(R.string.stopbits_key),
                        getResources().getString(R.string.stopbits_default))));
                serialPort.setParity(Integer.parseInt(pref.getString(getString(R.string.parity_key),
                        getResources().getString(R.string.parity_default))));
                serialPort.setFlowControl(Integer.parseInt(pref.getString(getString(R.string.flowcontrol_key),
                        getResources().getString(R.string.flowcontrol_default))));*/
				try
				{
					/*serialPort.setBaudRate(SettingsActivity.baudRate);
					serialPort.setDataBits(SettingsActivity.dataBit);
					serialPort.setStopBits(SettingsActivity.stopBit);
					serialPort.setParity(SettingsActivity.parity);
					serialPort.setFlowControl(SettingsActivity.flowControl);*/

					//Modified 25-01-2021
					int stopBit=1;
					int dataBit=7;
					int parity=2;
					int flowControl=0;
					int baudRate=9600;

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

					serialPort.setBaudRate(baudRate);
					serialPort.setDataBits(dataBit);
					serialPort.setStopBits(stopBit);
					serialPort.setParity(parity);
					serialPort.setFlowControl(flowControl);

					//End Modified 25-01-2021

					serialPort.read(mCallback);
					serialPort.getCTS(ctsCallback);
					serialPort.getDSR(dsrCallback);

					Handler handler = new Handler(Looper.getMainLooper());
					handler.post(new Runnable() {
						@Override
						public void run() {
							//BillingConsumption_LandT.isRead = true;
							Toast.makeText(getApplicationContext(), " SETTINGS CONFIGURED " ,
									Toast.LENGTH_SHORT).show();
						}
					});
				}
				catch(Exception e)
				{

				}

				Intent intent = new Intent(ACTION_USB_READY);
				context.sendBroadcast(intent);
			} else {
				if (serialPort instanceof CDCSerialDevice) {
					Intent intent = new Intent(ACTION_CDC_DRIVER_NOT_WORKING);
					context.sendBroadcast(intent);
				} else {
					Intent intent = new Intent(ACTION_USB_DEVICE_NOT_WORKING);
					context.sendBroadcast(intent);
				}
			}
		}
	}
}
