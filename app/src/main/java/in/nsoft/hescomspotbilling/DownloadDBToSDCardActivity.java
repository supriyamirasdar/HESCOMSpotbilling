package in.nsoft.hescomspotbilling;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadDBToSDCardActivity extends Activity {

	private Button btnDownloadMainDB, btnDownloadBackupDB;
	private ProgressBar pbDownloadDBStatusBar;
	private TextView lblDownloadDBStatus;
	private static final String TAG = BillingConsumption.class.getName();
	DatabaseHelper db = new DatabaseHelper(this);
	File file;
	Handler mainThreadHandler;
	InputStream ipStream;
	OutputStream myOutput;
	int count = 0, Total = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download_dbto_sdcard);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		btnDownloadMainDB = (Button)findViewById(R.id.btnDownloadMainDB);
		btnDownloadBackupDB = (Button)findViewById(R.id.btnDownloadBackUpDB);
		pbDownloadDBStatusBar = (ProgressBar)findViewById(R.id.pbDownloadDBStatusBar);
		lblDownloadDBStatus = (TextView)findViewById(R.id.lblDownloadDBStatus);
		mainThreadHandler = new Handler();	

		pbDownloadDBStatusBar.setVisibility(ProgressBar.INVISIBLE);
		lblDownloadDBStatus.setVisibility(ProgressBar.INVISIBLE);

		//Tamilselvan on 12-03-2014
		btnDownloadMainDB.setOnClickListener(new OnClickListener() {//btnDownloadMainDB 

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try
				{
					//Created By Tamilselvan on 06-03-2014--->Pull Out DataBase from Device
					file = new File("/data/data/in.nsoft.hescomspotbilling/databases/TRM_Hescom.dat");//Get DB Path
					if(!file.exists())
					{
						CustomToast.makeText(DownloadDBToSDCardActivity.this, "Database file not found...", Toast.LENGTH_SHORT);
						return;	
					}		
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try
							{
								mainThreadHandler.post(new Runnable() {									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										btnDownloadMainDB.setVisibility(ProgressBar.INVISIBLE);
										btnDownloadBackupDB.setVisibility(ProgressBar.INVISIBLE);
										pbDownloadDBStatusBar.setVisibility(ProgressBar.VISIBLE);
										lblDownloadDBStatus.setVisibility(ProgressBar.VISIBLE);
									}
								});
								String root = Environment.getExternalStorageDirectory().getPath();//Get External Storage Path 
								String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());					
								File filepath;
								String filePh = root+"/HescomSpotBilling";
								filepath = new File(filePh);
								if(!filepath.exists())
								{
									filepath.mkdir();
								}//Not Exists Create Directory
								filePh = filePh+"/"+timeStamp;
								filepath = new File(filePh);//External Storage Path + additional Folder
								if(!filepath.exists())
								{
									filepath.mkdir();
								}//Not Exists Create Directory
								if(filepath.exists())//If Directory Exists
								{
									ipStream = new FileInputStream(file);//get Input Stream of DataBase Path
									myOutput = new FileOutputStream(filePh+"/TRM_Hescom.dat");//Get OutputStream of New Directory Path					
									byte[] buffer = new byte[1024];//Variable
									int length;//Variable
									Total = ipStream.available();
									pbDownloadDBStatusBar.setMax(Total);
									
									
									while((length = ipStream.read(buffer))>0)
									{
										myOutput.write(buffer, 0, length);//write byte by byte to OutputStream Directory
										count = count + length;

										mainThreadHandler.post(new Runnable() {//Handler for set Progress								
											@Override
											public void run() {
												// TODO Auto-generated method stub

												pbDownloadDBStatusBar.setProgress(count);
												lblDownloadDBStatus.setText("Downloading file...");
											}
										});//END Handler for set Progress
									}	

								}/**/								
							}
							catch(Exception e)
							{
								Log.d(TAG, e.toString());
							}
							finally
							{
								try {
									myOutput.flush();
									myOutput.close();//Close OutputStream
									ipStream.close();//Close InputStream
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}//flush OutputStream
							}
							mainThreadHandler.post(new Runnable() {									
								@Override
								public void run() { 
									// TODO Auto-generated method stub
									btnDownloadMainDB.setVisibility(ProgressBar.VISIBLE);
									btnDownloadBackupDB.setVisibility(ProgressBar.VISIBLE);
									pbDownloadDBStatusBar.setVisibility(ProgressBar.INVISIBLE);
									lblDownloadDBStatus.setVisibility(ProgressBar.INVISIBLE);
									CustomToast.makeText(DownloadDBToSDCardActivity.this, "Download completed.", Toast.LENGTH_SHORT);
								}
							});
						}
					}).start();

				}
				catch(Exception e)
				{
					Log.d(TAG, e.toString());
				}				
			}
		});//END btnDownloadMainDB 
		//Tamilselvan on 12-03-2014
		btnDownloadBackupDB.setOnClickListener(new OnClickListener() {//btnDownloadBackupDB

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try
				{
					//Created By Tamilselvan on 06-03-2014--->Pull Out DataBase from Device
					file = new File("/data/data/in.nsoft.hescomspotbilling/databases/TRM_Hescom_bak.dat");//Get DB Path
					if(!file.exists())
					{
						CustomToast.makeText(DownloadDBToSDCardActivity.this, "Database file not found...", Toast.LENGTH_SHORT);
						return;	
					}		
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try
							{
								mainThreadHandler.post(new Runnable() {									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										btnDownloadMainDB.setVisibility(ProgressBar.INVISIBLE);
										btnDownloadBackupDB.setVisibility(ProgressBar.INVISIBLE);
										pbDownloadDBStatusBar.setVisibility(ProgressBar.VISIBLE);
										lblDownloadDBStatus.setVisibility(ProgressBar.VISIBLE);
									}
								});
								String root = Environment.getExternalStorageDirectory().getPath();//Get External Storage Path 
								String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());					
								File filepath;
								String filePh = root+"/HescomSpotBilling";
								filepath = new File(filePh);
								if(!filepath.exists())
								{
									filepath.mkdir();
								}//Not Exists Create Directory
								filePh = filePh+"/"+timeStamp;
								filepath = new File(filePh);//External Storage Path + additional Folder
								if(!filepath.exists())
								{
									filepath.mkdir();
								}//Not Exists Create Directory
								if(filepath.exists())//If Directory Exists
								{
									ipStream = new FileInputStream(file);//get Input Stream of DataBase Path
									myOutput = new FileOutputStream(filePh+"/TRM_Hescom_bak.dat");//Get OutputStream of New Directory Path					
									byte[] buffer = new byte[1024];//Variable
									int length;//Variable
									Total = ipStream.available();
									pbDownloadDBStatusBar.setMax(Total);
									
									
									while((length = ipStream.read(buffer))>0)
									{
										myOutput.write(buffer, 0, length);//write byte by byte to OutputStream Directory
										count = count + length;

										mainThreadHandler.post(new Runnable() {//Handler for set Progress								
											@Override
											public void run() {
												// TODO Auto-generated method stub

												pbDownloadDBStatusBar.setProgress(count);
												lblDownloadDBStatus.setText("Downloading file...");
											}
										});//END Handler for set Progress
									}	

								}/**/								
							}
							catch(Exception e)
							{
								Log.d(TAG, e.toString());
							}
							finally
							{
								try {
									myOutput.flush();
									myOutput.close();//Close OutputStream
									ipStream.close();//Close InputStream
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}//flush OutputStream
							}
							mainThreadHandler.post(new Runnable() {									
								@Override
								public void run() { 
									try
									{
										db.EventLogSave("12", LoginActivity.IMEINumber, LoginActivity.SimNumber, "Database Download Completed");//15-07-2016
									}
									catch(Exception e)
									{
										
									}
									btnDownloadMainDB.setVisibility(ProgressBar.VISIBLE);
									btnDownloadBackupDB.setVisibility(ProgressBar.VISIBLE);
									pbDownloadDBStatusBar.setVisibility(ProgressBar.INVISIBLE);
									lblDownloadDBStatus.setVisibility(ProgressBar.INVISIBLE);
									CustomToast.makeText(DownloadDBToSDCardActivity.this, "Download completed.", Toast.LENGTH_SHORT);
								}
							});
						}
					}).start();

				}
				catch(Exception e)
				{
					Log.d(TAG, e.toString());
				}				
			}
		});//END btnDownloadBackupDB
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.spotbillingmenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return OptionsMenu.navigate(item,DownloadDBToSDCardActivity.this);			
	}
}
