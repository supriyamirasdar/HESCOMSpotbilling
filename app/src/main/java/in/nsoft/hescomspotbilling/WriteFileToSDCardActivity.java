package in.nsoft.hescomspotbilling;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
//Created By Tamilselvan on 07-03-2014 
import android.widget.TextView;
public class WriteFileToSDCardActivity extends Activity {

	private Button btnWriteFile;
	private ProgressBar pbWriteFileProgress;
	private TextView lblWriteFilePBStatus;
	Handler mainThreadHandler;
	static int BillingRecords = 0, CollectionRecords = 0;
	String eol;
	DatabaseHelper db = new DatabaseHelper(this);
	ArrayList<WriteFileParameters> alwfp;
	StringBuilder sbBillingWrite, sbCollectionWrite;
	private static final String TAG = WriteFileToSDCardActivity.class.getName();
	int p = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_file_to_sdcard);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		btnWriteFile = (Button)findViewById(R.id.btnWriteFile);
		pbWriteFileProgress = (ProgressBar)findViewById(R.id.pbWriteFileProgress);
		lblWriteFilePBStatus = (TextView)findViewById(R.id.lblWriteFilePBStatus);
		mainThreadHandler = new Handler();
		sbBillingWrite = new StringBuilder();
		sbCollectionWrite = new StringBuilder();
		eol = System.getProperty("line.separator");
		pbWriteFileProgress.setVisibility(ProgressBar.INVISIBLE);
		lblWriteFilePBStatus.setVisibility(TextView.INVISIBLE);

		btnWriteFile.setOnClickListener(new OnClickListener() {//btnWriteFile setOnClickListener

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try		
				{
					int k = db.getCountofRecods();
					if(k <= 0)
					{
						CustomToast.makeText(WriteFileToSDCardActivity.this, "No records.", Toast.LENGTH_SHORT);
						return;
					}
					//created by tamilselvan 0n 10-03-2014
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
										//btnWriteFile.setEnabled(true);
										btnWriteFile.setVisibility(Button.INVISIBLE);
										pbWriteFileProgress.setVisibility(ProgressBar.VISIBLE);
										lblWriteFilePBStatus.setVisibility(TextView.VISIBLE);
									}
								});
								try
								{
									sbBillingWrite = db.GetDataToWrite(mainThreadHandler, pbWriteFileProgress, lblWriteFilePBStatus, 0);
									sbCollectionWrite = db.GetCollDataWriteToFile(mainThreadHandler, pbWriteFileProgress, lblWriteFilePBStatus, 0);
								}
								catch(Exception e)
								{
									Log.d(TAG, e.toString());
								}
								if((sbBillingWrite.length() > 0) || (sbCollectionWrite.length() > 0))
								{
									TelephonyManager mgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
									if(sbBillingWrite.length() > 0)
									{
										EncryptDecrypt en = new EncryptDecrypt("nsoft987");										
										final String BillingTest = en.encryptWithHandlerBilling(sbBillingWrite.toString(), mainThreadHandler, pbWriteFileProgress, lblWriteFilePBStatus, LoginActivity.IMEINumber, BillingRecords, 0); //10-03-2021
									}
									try
									{
										if(sbCollectionWrite.length() > 0)
										{
											EncryptDecrypt en = new EncryptDecrypt("nsoft987");
											final String CollectionTest = en.encryptWithHandlerCollection(sbCollectionWrite.toString(), mainThreadHandler, pbWriteFileProgress, lblWriteFilePBStatus,  LoginActivity.IMEINumber, CollectionRecords, 0);
										}
										else
										{
											mainThreadHandler.post(new Runnable() {

												@Override
												public void run() {
													lblWriteFilePBStatus.setText("Writing into file please wait...100%");
													pbWriteFileProgress.setProgress(100);
												}
											});
										}
									}
									catch(Exception e)
									{
										Log.d(TAG, e.toString());
									}
									db.UpdateStatusMasterDetailsByID("9", "1", "");//Update StatusMaster Table Status = 1 where statusId = 9 for File Download
									mainThreadHandler.post(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											try
											{
												db.EventLogSave("13", LoginActivity.IMEINumber,  LoginActivity.SimNumber, "File Write Completed");//15-07-2016
											}
											catch(Exception e)
											{

											}
											btnWriteFile.setVisibility(Button.VISIBLE);
											pbWriteFileProgress.setVisibility(ProgressBar.INVISIBLE);
											lblWriteFilePBStatus.setVisibility(TextView.INVISIBLE);
											CustomToast.makeText(WriteFileToSDCardActivity.this, "File write completed.", Toast.LENGTH_SHORT);
										}
									});
								}
								else if(sbBillingWrite.length() == 0 && sbCollectionWrite.length() == 0)
								{
									mainThreadHandler.post(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											btnWriteFile.setVisibility(Button.VISIBLE);
											pbWriteFileProgress.setVisibility(ProgressBar.INVISIBLE);
											lblWriteFilePBStatus.setVisibility(TextView.INVISIBLE);
											CustomToast.makeText(WriteFileToSDCardActivity.this, "No records to write file.", Toast.LENGTH_SHORT);
										}
									});
								}
							}
							catch(Exception e)
							{
								Log.d(TAG, e.toString());
							}
						}
					}).start();//END Thread
				}
				catch(Exception e)
				{
					Log.d(TAG, e.toString());
				}
			}
		});//END btnWriteFile setOnClickListener
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
		return OptionsMenu.navigate(item,WriteFileToSDCardActivity.this);			
	}	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub			
		return;
	}
}
