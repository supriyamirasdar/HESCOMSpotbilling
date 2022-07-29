package in.nsoft.hescomspotbilling;



import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class DownloadAPKActivity extends Activity {
	
	private Button btnDownloadAPK;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download_apk);
		btnDownloadAPK = (Button)findViewById(R.id.btnDownloadAPK);	
		
		btnDownloadAPK.setOnClickListener(new OnClickListener() {//btnDownloadMainDB 

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try
				{
					if(new ConnectionDetector(DownloadAPKActivity.this).isConnectedToInternet())
					{	
						new VersionCheckSync().execute();	
						/*Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);				
						startActivity(i);*/
					}
					else
					{
						CustomToast.makeText(DownloadAPKActivity.this, "Please Check if Internet Connection is Enabled.", Toast.LENGTH_SHORT);				
						return;
					}

				}
				catch(Exception e)
				{
					Log.d("", e.toString());
				}				
			}
		});//END btnDownloadMainDB 	
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
		return OptionsMenu.navigate(item,DownloadAPKActivity.this);			
	}
	
	private class VersionCheckSync extends AsyncTask<Void, Void, Void> {
		final ProgressDialog ringProgress =	ProgressDialog.show(DownloadAPKActivity.this, "Please wait..",	"Checking App Version...",true);//Spinner
		String rValue = "";
		ArrayList<String> lststr;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				HttpClient httpclt = new DefaultHttpClient();// object for
				// HttpClient
				HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/VersionCheckHescomSpotBilling");// HttpPost method uri
				List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
				lvp.add(new BasicNameValuePair("IMEINO", LoginActivity.IMEINumber));
				lvp.add(new BasicNameValuePair("Version",ConstantClass.AppVersion));
				httpPost.setEntity((new UrlEncodedFormEntity(lvp)));
				httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");
				HttpResponse res = httpclt.execute(httpPost);// execute post
				// method

				HttpEntity ent = res.getEntity();
				if (ent != null) {
					rValue = EntityUtils.toString(ent);
				}
			} catch (Exception e) {
				cancel(true);
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			ringProgress.setCancelable(false); 
			ringProgress.dismiss();
			try
			{
				if (rValue.equals("ACK")) // ACK received
				{
					CustomToast.makeText(DownloadAPKActivity.this, "Version Match",Toast.LENGTH_SHORT);				

				} 
				else
				{				
					if (rValue.equals("NACK")) // NACK received
					{
						CustomToast.makeText(DownloadAPKActivity.this,"Version Mismatch.Download latest app..",
								Toast.LENGTH_SHORT);
						new GetAPKFile().execute();
					} else {
						CustomToast.makeText(DownloadAPKActivity.this,"Version Check Failed", Toast.LENGTH_SHORT);
						return;
					}
				}
			}
			catch(Exception e)
			{
				CustomToast.makeText(DownloadAPKActivity.this,"Error Occured", Toast.LENGTH_SHORT);
			}

		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false);
			ringProgress.dismiss();			 
			CustomToast.makeText(DownloadAPKActivity.this,"Error Occured/No Response from server", Toast.LENGTH_LONG);

		}
	}
	private class GetAPKFile extends AsyncTask<Void, Void, Void> {
		final ProgressDialog ringProgress =
				ProgressDialog.show(DownloadAPKActivity.this, "Please wait..","Downloading Latest APP...",true);
		String rValue = "";
		ArrayList<String> lststr;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String root = Environment.getExternalStorageDirectory()
						.getPath();// get the sd Card Path
				File f = new File(root + "/HescomSpotBilling/");
				if (!f.exists()) {
					f.mkdir();
				}
				OutputStream myOutput = new FileOutputStream(f
						+ "/HescomSpotBilling.apk");
				HttpClient httpclt = new DefaultHttpClient();// object for
				// HttpClient
				HttpPost httpPost = new HttpPost(ConstantClass.IPAddress+ "/GetApkFileHescomSpotBilling");// HttpPost method uri
				List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
				lvp.add(new BasicNameValuePair("IMEINO", LoginActivity.IMEINumber));
				lvp.add(new BasicNameValuePair("Version",ConstantClass.AppVersion));
				httpPost.setEntity((new UrlEncodedFormEntity(lvp)));
				httpPost.setHeader("Content-Type",	"application/x-www-form-urlencoded");
				HttpResponse res = httpclt.execute(httpPost);
				HttpEntity ent = res.getEntity();
				if (ent != null) {
					String rValue = EntityUtils.toString(ent);
					ReadFileServerResponse rfsr = new ReadFileServerResponse();
					FileDownload fd = rfsr.Read(rValue);
					int length = Integer.valueOf(fd.getMfileLength());
					byte[] buffer = new byte[1024];
					byte[] sd = fd.getmBytes().getBytes("UTF-8");
					byte[] sd1 = Base64.decode(sd, Base64.DEFAULT);
					if (length == sd1.length) {
						ByteArrayInputStream ipStream = new ByteArrayInputStream(
								sd1);
						while ((length = ipStream.read(buffer)) > 0) {
							myOutput.write(buffer, 0, length);
						}
						myOutput.flush();
						myOutput.close();
						// Auto Installation of an APK
						String vsName = Environment.getExternalStorageDirectory().getAbsolutePath()	+ "/GescomSpotBilling/";
						File file = new File(vsName, "GescomSpotBilling.apk");
						//Intent intent = new Intent(Intent.ACTION_VIEW);
						//intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
						//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						//startActivity(intent);
						// End Auto Installation of an APK

						if(android.os.Build.VERSION.SDK_INT >= 29){
							//Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
							//intent.setData(Uri.fromFile(file));
							//intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
							//startActivity(intent);   
							
							Uri urifromFile = FileProvider.getUriForFile(DownloadAPKActivity.this,"in.nsoft.gescom.fileProvider", file);
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setDataAndType(urifromFile, "application/vnd.android.package-archive");
							intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
							startActivity(intent);
							
							
							
						}else{
							Intent intent = new Intent(Intent.ACTION_VIEW);
							//output file is the apk downloaded earlier
							intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						}

						//Uri urifromFile = FileProvider.getUriForFile(SplashScreenActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);

					} else {
						cancel(true);
					}
				}
			} catch (Exception e) {
				cancel(true);
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

			ringProgress.setCancelable(false); 
			ringProgress.dismiss();

		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false); 
			ringProgress.dismiss();
			CustomToast.makeText(DownloadAPKActivity.this,"App could not be downloaded", Toast.LENGTH_SHORT);
		}
	}
	

	
}
