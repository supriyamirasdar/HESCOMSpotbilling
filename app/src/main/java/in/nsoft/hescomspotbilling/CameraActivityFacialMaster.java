package in.nsoft.hescomspotbilling;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CameraActivityFacialMaster extends Activity implements SensorEventListener {
	private final String TAG = CameraActivity.class.getName();
	CameraPreviewFacial mPreview;
	Camera mCamera;
	//Button btnCameraCapture;
	SurfaceView SVCamera;
	FrameLayout FLCamera;
	private Button btnCameraTake, btnRetake, btnSave;
	private TextView tvTestSensorX, tvTestSensorY;
	private boolean isCaptured = false;

	//Tamilselvan on 14-07-2014
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
		setContentView(R.layout.portraitcamera);
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

		mPreview = new CameraPreviewFacial(CameraActivityFacialMaster.this, SVCamera);
		mPreview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		FLCamera.addView(mPreview);
		mPreview.setKeepScreenOn(true);

		//Added By Tamilselvan on 14-07-2014
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
				try
				{
					if(!isCaptured)
					{
						CustomToast.makeText(CameraActivityFacialMaster.this, "Please Capture Photo to Proceed.", Toast.LENGTH_SHORT);
						return;
					}
					else
					{
						long addtime = Calendar.getInstance().getTimeInMillis() + 1000;
						while(addtime > Calendar.getInstance().getTimeInMillis())  
						{										

						}
						
						InputStream myInput = null;
						OutputStream myOutput = null;
						String ImageName = "";
						try
						{  
							//CommonFunction cFun = new CommonFunction();
							String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
							//ImageName = BillingObject.GetBillingObject().getmConnectionNo().trim() +"_"+ cFun.GetCurrentTime().replace(" ", "_").replace("-", "").replace(":", "")+".jpg";
							String root = Environment.getExternalStorageDirectory().getPath();//get the sd Card Path

							myInput = new FileInputStream("/data/data/in.nsoft.hescomspotbilling/databases/photo/FaceImgMaster.jpg");					
							File f = new File(root+"/MasterImage/" ,"FaceImgMaster.jpg");

							if(new File(root+"/MasterImage/").exists())
							{         
								new File(root+"/MasterImage/").deleteOnExit();
							}
							if(!new File(root+"/MasterImage/").exists())
							{
								new File(root+"/MasterImage/").mkdirs();
							}

							myOutput = new FileOutputStream(f);
							byte[] buffer = new byte[1024];
							int length;
							while((length = myInput.read(buffer))>0)
							{
								myOutput.write(buffer, 0, length);
							}
							CustomToast.makeText(CameraActivityFacialMaster.this, "Master Image Saved.", Toast.LENGTH_SHORT);//13-02-2015
						}
						catch (Exception e)
						{
							Log.d("pushBtn", e.toString());
							ImageName = "Exception";//Added on 09-06-2014
						}
						finally
						{
							if(myInput != null)
							{
								try 
								{
									myInput.close();
								} 
								catch (Exception e) 
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							if(myOutput != null)
							{
								try 
								{
									myOutput.close();
								} 
								catch (Exception e) 
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}                               
						}
						
						Intent i = new Intent(CameraActivityFacialMaster.this, LoginActivity.class);
						startActivity(i);
					}
				}
				catch(Exception e)
				{
					Log.d(TAG, e.toString());
				}
			}
		});

		btnRetake.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {				
				if(!isCaptured)
				{
					CustomToast.makeText(CameraActivityFacialMaster.this, "Please Capture Photo to Proceed.", Toast.LENGTH_SHORT);
					Intent i = new Intent(CameraActivityFacialMaster.this, CameraActivityFacialMaster.class);
					startActivity(i);
					return;
				}
				else
				{
					Intent i = new Intent(CameraActivityFacialMaster.this, LoginActivity.class);
					startActivity(i);
				}			
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(Camera.getNumberOfCameras() >=2)
		{
			mCamera=Camera.open(CameraInfo.CAMERA_FACING_FRONT);
		}
		//mCamera = Camera.open(0);                        
		mCamera.startPreview();
		mCamera.setDisplayOrientation(90);			
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
		Intent i = new Intent(CameraActivityFacialMaster.this, LoginActivity.class);
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

			String filen = "FaceImgMaster.jpg";			
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
			//CustomToast.makeText(CameraActivityFacialMaster.this, "Take Portrait Image.", Toast.LENGTH_LONG); 
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

				//0-90,90-180,180-270,270,0

			}

			if(isCaptured)
			{			
				//17-01-2015
				if((mSensorX > 5 || mSensorX < -5))
				{
					CustomToast.makeText(CameraActivityFacialMaster.this, "Take Portrait image. Other Positions not allowed", Toast.LENGTH_LONG);
					isCaptured = false;
				}
				
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

}
