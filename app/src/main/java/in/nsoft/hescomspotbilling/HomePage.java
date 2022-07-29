//Nitish 24-02-2014
package in.nsoft.hescomspotbilling;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class HomePage extends Activity {
	private Button btnHomeBilling, btnHomeCollections, btnHomeReports, btnHomeOthers;
	//static GPSTracker gpsTracker ;
	GridView gridView;
	ArrayList<Item> gridArray = new ArrayList<Item>();
	CustomGridViewAdapter mCustomGridViewAdapter;
	DatabaseHelper mDb = new DatabaseHelper(this);
	TextView lblSBBlinkText; //25-05-2020

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_home_page);
		setContentView(R.layout.gridhome);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		/*if(gpsTracker == null)
			gpsTracker = new GPSTracker(HomePage.this);*/

		//25-05-2020
		lblSBBlinkText = (TextView)findViewById(R.id.lblSBBlinkText);	
		lblSBBlinkText.setTextColor(Color.parseColor("#FF0000")); //red color
		lblSBBlinkText.setTextSize(14);

		//#####Live and Test  Validation ########
		if(ConstantClass.IPAddress.equals("http://123.201.131.113:8112/Hescom.asmx"))
		{
			lblSBBlinkText.setText(ConstantClass.mTest + ConstantClass.FileVersion);
		}
		else
		{
			lblSBBlinkText.setText(ConstantClass.mLive + ConstantClass.FileVersion);
		}

		Bitmap billing = BitmapFactory.decodeResource(this.getResources(), R.drawable.billing1);//billing
		Bitmap collection = BitmapFactory.decodeResource(this.getResources(), R.drawable.collection1);//collection
		Bitmap report = BitmapFactory.decodeResource(this.getResources(), R.drawable.report);//report
		Bitmap others = BitmapFactory.decodeResource(this.getResources(), R.drawable.others1);//Other
		Bitmap disconnection = BitmapFactory.decodeResource(this.getResources(), R.drawable.disc);//Other

		gridArray.add(new Item(billing, "Billing"));
		gridArray.add(new Item(collection, "Collection"));
		gridArray.add(new Item(report, "Reports"));
		gridArray.add(new Item(others, "Others"));
		gridArray.add(new Item(disconnection, "Disconnection"));

		gridView = (GridView)findViewById(R.id.grdHomePage);
		mCustomGridViewAdapter = new CustomGridViewAdapter(HomePage.this, R.layout.row_grid, gridArray);
		gridView.setAdapter(mCustomGridViewAdapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String Name = gridArray.get(arg2).Name;
				if(Name.equals("Billing"))
				{   

					if(LoginActivity.gpsTracker.isGPSConnected())
					{
						LoginActivity.gpsTracker = new GPSTracker(HomePage.this);
						//Intent i = new Intent(HomePage.this, Billing.class);
						//Intent i = new Intent(HomePage.this, FragmentBillingActivity.class);
						//25-07-2016		
						try
						{
							/*mDb.GetValueOnBillingPage();//Get top 1 connection details which is not billed	
							if(BillingObject.GetBillingObject().getmLocationCode()==null)
							{
								CustomToast.makeText(HomePage.this, "Please Load File and Proceed.", Toast.LENGTH_SHORT);
							}
							else*/
							{
								Intent i = new Intent(HomePage.this, BillingStartActivity.class);		
								startActivity(i);

								/*Intent i = new Intent(HomePage.this, Billing.class);		
								startActivity(i);*/
							}
						}
						catch(Exception e)
						{
							CustomToast.makeText(HomePage.this, "Please Load File and Proceed.", Toast.LENGTH_SHORT);
						}


					}
					else
					{
						CustomToast.makeText(HomePage.this, "GPS disabled. Check GPS settings.", Toast.LENGTH_SHORT);
					}					
				}
				else if(Name.equals("Collection"))
				{
					try {

						if(LoginActivity.gpsTracker.isGPSConnected())
						{
							if(mDb.GetStatusMasterDetailsByID("7") != 1)//if not equal to 1, SBM File is not Loaded	 
							{
								CustomToast.makeText(HomePage.this,"Please Load SBM File to Database." , Toast.LENGTH_SHORT);
								return;		
							}					
							Intent i = new Intent(HomePage.this,CashCounterSyncActivity.class);
							startActivity(i);
						}
						else
						{
							CustomToast.makeText(HomePage.this, "GPS disabled. Check GPS settings.", Toast.LENGTH_SHORT);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Toast toast = Toast.makeText(HomePage.this, "Collections Error", Toast.LENGTH_SHORT);
						toast.show();
						e.printStackTrace();
					}

					//CustomToast.makeText(HomePage.this, "Under Construction.", Toast.LENGTH_SHORT);
				}
				else if(Name.equals("Reports"))
				{
					try {			
						Intent i = new Intent(HomePage.this,ReportListActivity.class);
						startActivity(i);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						Toast toast = Toast.makeText(HomePage.this, "Reports Error", Toast.LENGTH_SHORT);
						toast.show();
						e.printStackTrace();
					}
				}
				else if(Name.equals("Others"))
				{
					try {			
						Intent i = new Intent(HomePage.this,OtherListActivity.class);
						startActivity(i);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						Toast toast = Toast.makeText(HomePage.this, "Others Error", Toast.LENGTH_SHORT);
						toast.show();
						e.printStackTrace();
					}	
				}
				else if(Name.equals("Disconnection"))
				{
					try {	
						if(mDb.GetStatusMasterDetailsByID("7") != 1)//if not equal to 1, SBM File is not Loaded	 
						{
							CustomToast.makeText(HomePage.this,"Please Load SBM File to Database." , Toast.LENGTH_SHORT);
							return;		
						}		
						Intent i = new Intent(HomePage.this,DisconnectionActivity.class);
						startActivity(i);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						Toast toast = Toast.makeText(HomePage.this, "Disconnection Error", Toast.LENGTH_SHORT);
						toast.show();
						e.printStackTrace();
					}	
				}
			}
		});
	}
	//Modified Nitish 26-02-2014
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.spotbillingmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return OptionsMenu.navigate(item,HomePage.this);		
	}
	//End Nitish 26-02-2014	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();

		CustomToast.makeText(HomePage.this, "Back Button Disabled in this Screen.", Toast.LENGTH_SHORT);
		return;
	}
}
