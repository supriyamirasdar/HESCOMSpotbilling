package in.nsoft.hescomspotbilling;
//Nitish 13-02-2015
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CameraDisconnectionActivity extends Activity implements SensorEventListener {
	private final String TAG = CameraActivity.class.getName();
	CameraPreview mPreview;
	Camera mCamera;
	//Button btnCameraCapture;
	SurfaceView SVCamera;
	FrameLayout FLCamera;
	private Button btnCameraTake, btnRetake, btnSave;
	private TextView tvTestSensorX, tvTestSensorY;
	
	private boolean isCaptured = false;//13-02-2015
	
	//SensorManager Parameters==================================================
	private float mSensorX;
	private float mSensorY;
	private Display mDisplay;
	private SensorManager sm;
	private PowerManager mPowerManager;
	private WindowManager mWindowManager;
	//END SensorManager Parameters==============================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);//it will remove title
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//get the full screen mode
		setContentView(R.layout.landscapecamera);
		SVCamera = (SurfaceView)findViewById(R.id.SVCamera);
		FLCamera = (FrameLayout)findViewById(R.id.FLCamera);
		btnCameraTake = (Button)findViewById(R.id.btnLandscapeTake);
		btnRetake = (Button)findViewById(R.id.btnLandscapeRetake);
		btnSave = (Button)findViewById(R.id.btnLandscapeOk);		
		tvTestSensorX = (TextView)findViewById(R.id.tvTestSensorX);
		tvTestSensorY = (TextView)findViewById(R.id.tvTestSensorY);

		
		//btnRetake.setVisibility(Button.INVISIBLE);
		//btnSave.setVisibility(Button.INVISIBLE);
		//btnCameraTake.setVisibility(Button.VISIBLE);

		mPreview = new CameraPreview(CameraDisconnectionActivity.this, SVCamera);
		mPreview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		FLCamera.addView(mPreview);
		mPreview.setKeepScreenOn(true);
		
		//Sensor Manager========================================================
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		if(sm.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0)
		{
			Sensor s = sm.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
			sm.registerListener(this,s, SensorManager.SENSOR_DELAY_NORMAL);
		}

		// Get an instance of the PowerManager
		mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);

		// Get an instance of the WindowManager
		mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		mDisplay = mWindowManager.getDefaultDisplay();
		//END Sensor Manager====================================================

		btnCameraTake.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try
				{
					isCaptured = true;
				
					mCamera.takePicture(null, null, jpegCallback);
					//btnCameraTake.setVisibility(Button.INVISIBLE);
				}
				catch(Exception e)
				{
					Log.d(TAG, e.toString());
				}
			}
		});

		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!isCaptured)
				{
					CustomToast.makeText(CameraDisconnectionActivity.this, "Please Capture Photo to Proceed.", Toast.LENGTH_SHORT);
					return;
				}
				else
				{
					long addtime = Calendar.getInstance().getTimeInMillis() + 1000;
					while(addtime > Calendar.getInstance().getTimeInMillis())  
					{										

					}
					
					DisconnectionActivity.isFromDisconnectionCamera = true; //Nitish 13-02-2015
					Intent i = new Intent(CameraDisconnectionActivity.this, DisconnectionActivity.class);
					startActivity(i);
				}
			}
		});

		btnRetake.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!isCaptured)
				{
					CustomToast.makeText(CameraDisconnectionActivity.this, "Please Capture Photo to Proceed.", Toast.LENGTH_SHORT);
					return;
				}
				else
				{
					Intent i = new Intent(CameraDisconnectionActivity.this, CameraDisconnectionActivity.class);
					startActivity(i);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mCamera = Camera.open();
		mCamera.startPreview();
		mPreview.setCamera(mCamera);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if(mCamera != null)
		{
			mCamera.stopPreview();
			mPreview.setCamera(null);
			mCamera.release();
			mCamera = null;
		}
		super.onPause();
	}

	private void resetCam()
	{
		mCamera.startPreview();
		mPreview.setCamera(mCamera);
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(CameraDisconnectionActivity.this, DisconnectionActivity.class);
		startActivity(i);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}

	PictureCallback jpegCallback = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			String filen = "MtrImg.jpg";			
			FileOutputStream os = null;
			try 
			{
				File fileo = new File("/data/data/in.nsoft.hescomspotbilling/databases/photo");
				if(!fileo.exists())
				{
					fileo.mkdirs();
				}
				fileo = new File("/data/data/in.nsoft.hescomspotbilling/databases/photo/", filen);
				os = new FileOutputStream(fileo);
				os.write(data);

			}
			catch(Exception e)
			{
				Log.d(TAG,e.toString());
			}
			finally 
			{
				try
				{
					if(os!=null)
						os.close(); 

				}
				catch(Exception e)
				{
					Log.d(TAG,e.toString());
				}
			}
			//btnRetake.setVisibility(Button.VISIBLE);
			//btnSave.setVisibility(Button.VISIBLE);
			//CustomToast.makeText(CameraDisconnectionActivity.this, "If READING is Not Visible Please Retake Photo", Toast.LENGTH_LONG);
		}
	};

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		try
		{
			if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
				return;

			switch (mDisplay.getRotation())
			{
			case Surface.ROTATION_0:
				mSensorX = event.values[0];
				mSensorY = event.values[1];
				//tvTestSensorX.setText(String.valueOf(mSensorX));
				//tvTestSensorY.setText(String.valueOf(mSensorY));
				break;
			case Surface.ROTATION_90:
				mSensorX = -event.values[1];
				mSensorY = event.values[0];
				//tvTestSensorX.setText(String.valueOf(mSensorX));
				//tvTestSensorY.setText(String.valueOf(mSensorY));
				break;
			case Surface.ROTATION_180:
				mSensorX = -event.values[0];
				mSensorY = -event.values[1];
				//tvTestSensorX.setText(String.valueOf(mSensorX));
				//tvTestSensorY.setText(String.valueOf(mSensorY));
				break;
			case Surface.ROTATION_270:
				mSensorX = event.values[1];
				mSensorY = -event.values[0];
				//tvTestSensorX.setText(String.valueOf(mSensorX));
				//tvTestSensorY.setText(String.valueOf(mSensorY));
				break;
			}
			/*if(!isCaptured)
			{
				if((mSensorX > -2 && mSensorX < 2) && (mSensorY > 8 && mSensorY < 10.8))
				{
					if(!btnCameraTake.isShown())
					{
						btnCameraTake.setVisibility(Button.VISIBLE);
					}
				}
				else
				{
					if(btnCameraTake.isShown())
					{
						btnCameraTake.setVisibility(Button.INVISIBLE);
						CustomToast.makeText(CameraActivity.this, "Only landscape is allowed to capture image. Other position not allowed", Toast.LENGTH_SHORT);
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
