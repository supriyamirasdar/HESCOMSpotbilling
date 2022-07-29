package in.nsoft.hescomspotbilling;

import java.io.IOException;
import java.math.BigDecimal;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Collection_LatePaymentActivity extends Activity  implements AlertInterface{			

	Button btnconfirm;
	ReadCollection collObj;
	private TextView conid,rrNo,lblRevenueMisc,lblScheduledDate;
	private EditText txtScheduledDate;	
	private Spinner ddlremarks;
	DDLAdapter remarkAdapter;
	private final DatabaseHelper mDb =new DatabaseHelper(this);
	private static final String TAG = CollectionActivity.class.getName();


	//Nitish 24-07-2014
	String rcpttype;
	static final int REQUEST_ENABLE_BT = 0;
	BigDecimal aramt,pamt;
	ProgressDialog ringProgress;
	int result =-1;
	long diff;	
	String IMEINumber = "";	
	BigDecimal cLimit,cMade;	
	CashCounterObject CC ;	
	String remarkId="0";
	String remarkName="";

	//Nitish 04-04-2014
	Handler dh;
	CustomAlert cAlert; 
	boolean alertResult;		
	final static String alerttitle = "Late Payment";
	final static String btnAlertOk = "OK";
	final static String btnAlertCancel = "CANCEL";		
	final static int TwoButtons = 2;
	//Nitish End

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection__late_payment);

		/*TelephonyManager mgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		IMEINumber = mgr.getDeviceId();*/
		
		try
		{
			//Get Mobile Details ==============================================================
			//10-03-2021
			IMEINumber = CommonFunction.getDeviceNo(Collection_LatePaymentActivity.this);
		

		}
		catch(Exception e)
		{
							
		}

		conid =  (TextView)findViewById(R.id.txtConId);
		rrNo =  (TextView)findViewById(R.id.txtRRNo);	
		lblScheduledDate =  (TextView)findViewById(R.id.lblScheduledDate);
		txtScheduledDate = (EditText)findViewById(R.id.txtScheduledDate);		
		lblRevenueMisc= (TextView)findViewById(R.id.lblRevenueMisc);//lbl for Revenue/Misc
		ddlremarks= (Spinner)findViewById(R.id.ddlRemarks);//lbl for Arrears/Balance
		btnconfirm = (Button)findViewById(R.id.btnConfirm);

		lblScheduledDate.setVisibility(View.INVISIBLE);
		txtScheduledDate.setVisibility(View.INVISIBLE);

		dh = new Handler();

		//Nitish 24-07-2014
		if(CollectionStartActivity.isRevenueRcpt)
		{
			lblRevenueMisc.setText(ConstantClass.mRevenueRcpt.toString());
			rcpttype = ConstantClass.mRevenueType; //rcpttype= R			

		}
		else
		{
			lblRevenueMisc.setText(ConstantClass.mMISCRcpt.toString());
			rcpttype = ConstantClass.mMISCType; //rcpttype= M	

		}


		try 
		{
			remarkAdapter = mDb.getLatePaymentReason();
			ddlremarks.setAdapter(remarkAdapter);

			CC = mDb.GetCashCounterDetails();		
			collObj=CollectionObject.GetCollectionObject();
			conid.setText(collObj.getmConnectionNo());
			rrNo.setText(collObj.getmRRNo());
			collObj.setmScheduledDate("0");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		ddlremarks.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				DDLItem k = (DDLItem) arg0.getItemAtPosition(arg2);				
				remarkId=k.getId();
				remarkName = k.getValue();
				if(remarkId.equals("4"))
				{
					lblScheduledDate.setVisibility(View.VISIBLE);
					txtScheduledDate.setVisibility(View.VISIBLE);
					new CustomDatePicker(Collection_LatePaymentActivity.this, R.id.txtScheduledDate,0,null);
					collObj.setmScheduledDate("0");
					
				}
				else
				{
					lblScheduledDate.setVisibility(View.INVISIBLE);
					txtScheduledDate.setVisibility(View.INVISIBLE);
					collObj.setmScheduledDate("0");
				}
				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});	

		btnconfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				collObj.setmScheduledDate("0");
				if(txtScheduledDate.getText().toString().length() > 0)
				{
					collObj.setmScheduledDate(txtScheduledDate.getText().toString().trim());
				}
				else
				{
					collObj.setmScheduledDate("0");
				}

				StringBuilder sb = new StringBuilder();
				sb.append("CONID     : "+ conid.getText().toString() + ConstantClass.eol);	
				sb.append("RRNO      : "+ rrNo.getText().toString() + ConstantClass.eol);
				sb.append("REASON   : "+ remarkName + ConstantClass.eol);
				sb.append("EXPECTED PAYMENT DATE : "+ collObj.getmScheduledDate() + ConstantClass.eol);
				

				// TODO Auto-generated method stub
				CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
				params.setContext(Collection_LatePaymentActivity.this);					
				params.setMainHandler(dh);
				params.setMsg(sb.toString());
				params.setButtonOkText(btnAlertCancel);
				params.setButtonCancelText(btnAlertOk);
				params.setTitle(alerttitle);
				params.setFunctionality(TwoButtons);
				cAlert  = new CustomAlert(params);
			}
		});



	}

	

	@Override
	public void performAction(boolean alertResult, int functionality) {
		// TODO Auto-generated method stub
		if(!alertResult)
		{
			collObj.setmPayment_Mode("0");//Non Payment
			collObj.setmRemarkId(Integer.valueOf(remarkId));
			collObj.setmSBMNumber(IMEINumber); //Set SBMNumber  as  IMEINo			
			result = mDb.CollectionSave();
			if(result == 1)
			{
				CustomToast.makeText(Collection_LatePaymentActivity.this, "Saved Successfully.", Toast.LENGTH_SHORT);
				Intent i = new Intent(Collection_LatePaymentActivity.this, CollectionActivity.class);
				startActivity(i);
				
			}
			else//Tamilselvan on 20-03-2014 
			{
				CustomToast.makeText(Collection_LatePaymentActivity.this, "Problem in Saving data.", Toast.LENGTH_SHORT);
				Intent i = new Intent(Collection_LatePaymentActivity.this, CollectionActivity.class);
				startActivity(i);
			}
		}
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
		return OptionsMenu.navigate(item,Collection_LatePaymentActivity.this);			
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		CustomToast.makeText(Collection_LatePaymentActivity.this, "Please use menu to navigate.", Toast.LENGTH_SHORT);
		return;
	}

}
