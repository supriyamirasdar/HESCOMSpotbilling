package in.nsoft.hescomspotbilling;

//Nitish 31-10-2015

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class BillingConsumerDetailsActivity extends Activity {

	EditText txtAdharNo,txtVoterId,txtMobileNo;
	TextView lblRRNo;
	Button btnResetAadhar,btnResetVoter,btnResetMobile,btnContinue,btnRefresh;
	DatabaseHelper db = new DatabaseHelper(this);
	private ReadSlabNTarifSbmBillCollection SlabColl;
	Handler dh;

	//06-07-2020
	LinearLayout lytRationCard;
	RadioButton radioRationYes,radioRationNo;
	EditText txtRationCardNo;
	TextView lblRationCardStatus; //05-09-2020
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billing_consumer_details);

		dh= new Handler();
		SlabColl = BillingObject.GetBillingObject();//Get Billing Object

		lblRRNo = (TextView)findViewById(R.id.lblRRNo);

		txtAdharNo = (EditText)findViewById(R.id.txtAdharNo);
		txtVoterId = (EditText)findViewById(R.id.txtVoterId);
		txtMobileNo = (EditText)findViewById(R.id.txtMobileNo);

		btnResetAadhar = (Button)findViewById(R.id.btnResetAadhar);
		btnResetVoter = (Button)findViewById(R.id.btnResetVoter);
		btnResetMobile = (Button)findViewById(R.id.btnResetMobile);

		btnContinue = (Button)findViewById(R.id.btnContinue);
		btnRefresh = (Button)findViewById(R.id.btnRefresh);

		//RR-Flag
		//0 ->Capture All
		//1	->Capture Only AadharId
		//2	->Capture Only VoterId
		//3 ->Capture Only MobileNo
		//4	->Capture AadharId and  VoterId
		//5	->Capture AadharId and  MobileNo
		//6	->Capture VoterId and  MobileNo
		//6	->Capture VoterId and  MobileNo
		//7	->Do Not Capture
		//else Enable All -Nothing is Complulsary

		lblRRNo.setText("RRNO: " + SlabColl.getmRRNo());

		btnResetAadhar.setEnabled(false);
		btnResetVoter.setEnabled(false);
		btnResetMobile.setEnabled(false);

		if(SlabColl.getmRRFlag().equals("0"))
		{
			txtAdharNo.setEnabled(true);
			txtVoterId.setEnabled(true);
			txtMobileNo.setEnabled(true);

			btnResetAadhar.setEnabled(false);
			btnResetVoter.setEnabled(false);
			btnResetMobile.setEnabled(false);

		}
		else if(SlabColl.getmRRFlag().equals("1")) //Only AadharId
		{
			txtAdharNo.setEnabled(true);
			txtVoterId.setEnabled(false);
			txtMobileNo.setEnabled(false);

			btnResetAadhar.setEnabled(false);
			btnResetVoter.setEnabled(true);
			btnResetMobile.setEnabled(true);
		}
		else if(SlabColl.getmRRFlag().equals("2")) //Only VoterId
		{
			txtAdharNo.setEnabled(false);
			txtVoterId.setEnabled(true);
			txtMobileNo.setEnabled(false);

			btnResetAadhar.setEnabled(true);
			btnResetVoter.setEnabled(false);
			btnResetMobile.setEnabled(true);
		}
		else if(SlabColl.getmRRFlag().equals("3")) //Only MobileNo
		{
			txtAdharNo.setEnabled(false);
			txtVoterId.setEnabled(false);
			txtMobileNo.setEnabled(true);

			btnResetAadhar.setEnabled(true);
			btnResetVoter.setEnabled(true);
			btnResetMobile.setEnabled(false);
		}
		else if(SlabColl.getmRRFlag().equals("4")) //AadharId and VoterId
		{
			txtAdharNo.setEnabled(true);
			txtVoterId.setEnabled(true);
			txtMobileNo.setEnabled(false);

			btnResetAadhar.setEnabled(false);
			btnResetVoter.setEnabled(false);
			btnResetMobile.setEnabled(true);
		}
		else if(SlabColl.getmRRFlag().equals("5")) //AadharId and MobileNo
		{
			txtAdharNo.setEnabled(true);
			txtVoterId.setEnabled(false);
			txtMobileNo.setEnabled(true);

			btnResetAadhar.setEnabled(false);
			btnResetVoter.setEnabled(true);
			btnResetMobile.setEnabled(false);
		}
		else if(SlabColl.getmRRFlag().equals("6")) //VoterId and MobileNo
		{
			txtAdharNo.setEnabled(false);
			txtVoterId.setEnabled(true);
			txtMobileNo.setEnabled(true);

			btnResetAadhar.setEnabled(true);
			btnResetVoter.setEnabled(false);
			btnResetMobile.setEnabled(false);
		}
		else if(SlabColl.getmRRFlag().equals("7")) //Disable All
		{
			txtAdharNo.setEnabled(false);
			txtVoterId.setEnabled(false);
			txtMobileNo.setEnabled(false);

			btnResetAadhar.setEnabled(true);
			btnResetVoter.setEnabled(true);
			btnResetMobile.setEnabled(true);
		}

		else
		{
			txtAdharNo.setEnabled(true);
			txtVoterId.setEnabled(true);
			txtMobileNo.setEnabled(true);

			btnResetAadhar.setEnabled(false);
			btnResetVoter.setEnabled(false);
			btnResetMobile.setEnabled(false);

		}
		//23-05-2020
		txtAdharNo.setEnabled(false);
		txtVoterId.setEnabled(false);		

		btnResetAadhar.setEnabled(false);
		btnResetVoter.setEnabled(false);

		txtMobileNo.setEnabled(true);
		btnResetMobile.setEnabled(true);

		//06-07-2020
		lytRationCard= (LinearLayout)findViewById(R.id.lytRationCard);
		radioRationYes= (RadioButton)findViewById(R.id.radioRationYes);
		radioRationNo= (RadioButton)findViewById(R.id.radioRationNo);
		txtRationCardNo= (EditText)findViewById(R.id.txtRationCardNo);
		lblRationCardStatus = (TextView)findViewById(R.id.lblRationCardStatus); //05-09-2020
		loadRationDetails();

		radioRationYes.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(radioRationYes.isChecked())
				{
					txtRationCardNo.setEnabled(true);					
				}


			}
		});
		radioRationNo.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(radioRationNo.isChecked())
				{
					txtRationCardNo.setEnabled(false);
					txtRationCardNo.setText("");
				}


			}
		});
		//End 06-07-2020
		btnResetAadhar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				txtAdharNo.setEnabled(true);
				txtAdharNo.setText("");

			}
		});
		btnResetVoter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				txtVoterId.setEnabled(true);
				txtVoterId.setText("");

			}
		});
		btnResetMobile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				txtMobileNo.setEnabled(true);
				txtMobileNo.setText("");

			}
		});

		btnRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				onCreate(new Bundle()); //Refresh or Reload

			}
		});

		btnContinue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if((txtAdharNo.getText().length()>0 && txtAdharNo.getText().length()!=12) || (txtAdharNo.getText().length()==0))
				{
					SlabColl.setmAadharNo(" ");    
					if((txtAdharNo.getText().length()>0 && txtAdharNo.getText().length()!=12))
					{
						CustomToast.makeText(BillingConsumerDetailsActivity.this, "Invalid Aadhar No", Toast.LENGTH_SHORT);						
						return;
					}								
				}
				else if(txtAdharNo.getText().length()==12)
				{
					SlabColl.setmAadharNo(txtAdharNo.getText().toString());    
				}

				if((txtVoterId.getText().length()>0 && txtVoterId.getText().length()!=10) || (txtVoterId.getText().length()==0))
				{
					SlabColl.setmVoterIdNo(" ");    
					if((txtVoterId.getText().length()>0 && txtVoterId.getText().length()!=10))
					{
						CustomToast.makeText(BillingConsumerDetailsActivity.this, "Invalid VoterID No", Toast.LENGTH_SHORT);						
						return;
					}								
				}
				else if(txtVoterId.getText().length()==10)
				{
					SlabColl.setmVoterIdNo(txtVoterId.getText().toString());    
				}
				//Modified 27-06-2020
				if(txtMobileNo.getText().toString().trim().length()==0)
				{
					SlabColl.setmMobileNo("9999999999"); //27-06-2020
					//CustomToast.makeText(BillingConsumerDetailsActivity.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT);						
					//return;

				}
				else 
				{

					if(txtMobileNo.getText().toString().trim().length()!=10)
					{
						SlabColl.setmMobileNo("9999999999");   
						CustomToast.makeText(BillingConsumerDetailsActivity.this, "Invalid Mobile No", Toast.LENGTH_SHORT);						
						return;

					}
					else if(txtMobileNo.getText().toString().trim().length()==10)
					{
						if(txtMobileNo.getText().toString().startsWith("0")||txtMobileNo.getText().toString().startsWith("1")|| txtMobileNo.getText().toString().startsWith("2") || txtMobileNo.getText().toString().startsWith("3") || txtMobileNo.getText().toString().startsWith("4") || txtMobileNo.getText().toString().startsWith("5"))							
						{
							SlabColl.setmMobileNo("9999999999");  
							CustomToast.makeText(BillingConsumerDetailsActivity.this, "Invalid Mobile No", Toast.LENGTH_SHORT);						
							return;								
						}
						else if(txtMobileNo.getText().toString().trim().contains("+"))
						{
							SlabColl.setmMobileNo("9999999999");    
							CustomToast.makeText(BillingConsumerDetailsActivity.this, "Invalid Mobile No", Toast.LENGTH_SHORT);						
							return;	  
						}
						else
						{
							SlabColl.setmMobileNo(txtMobileNo.getText().toString());    
						}
					}
				}
				//06-07-2020
				try
				{
					BillingObject.GetBillingObject().setmRemarks("");
					if(BillingObject.GetBillingObject().getmTarifString().indexOf("LT-1")!= -1 || BillingObject.GetBillingObject().getmTarifString().indexOf("LT-2")!= -1 )
					{
						if(radioRationYes.isChecked())
						{
							if(txtRationCardNo.getText().toString().trim().length()==0)
							{
								CustomToast.makeText(BillingConsumerDetailsActivity.this,"Enter Ration Card Number." , Toast.LENGTH_SHORT);
								return;
							}
							else if(txtRationCardNo.getText().toString().trim().length()!=12)
							{
								CustomToast.makeText(BillingConsumerDetailsActivity.this,"Invalid Ration Card Number." , Toast.LENGTH_SHORT);
								return;
							}
							else
							{
								BillingObject.GetBillingObject().setmRemarks(txtRationCardNo.getText().toString().trim());
							}
						}
						else
						{
							BillingObject.GetBillingObject().setmRemarks("");
						}
					}					
				}
				catch(Exception e)
				{

				}
				Intent i = new Intent(BillingConsumerDetailsActivity.this, BillingConsumption.class);
				startActivity(i);
				finish(); //08-05-2021

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.spotbillingmenu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		return OptionsMenu.navigate(item,BillingConsumerDetailsActivity.this);			
	}
	//End Nitish 26-02-2014
	@Override
	public void onBackPressed() {		
		return;
	}
	//06-07-2020
	public void loadRationDetails()
	{
		try
		{
			BillingObject.GetBillingObject().setmRemarks("");
			txtRationCardNo.setText("");
			if(BillingObject.GetBillingObject().getmTarifString().indexOf("LT-1")!= -1 || BillingObject.GetBillingObject().getmTarifString().indexOf("LT-2")!= -1 )
			{
				lytRationCard.setVisibility(View.VISIBLE);
				//radioRationYes.setChecked(true);
				//txtRationCardNo.setEnabled(true);
				//Modified 05-09-2020
				if(BillingObject.GetBillingObject().getmTvmPFtype().trim().equals("1"))
				{
					lblRationCardStatus.setText("Ration Card Data Already Captured.");
					radioRationNo.setChecked(true);
					txtRationCardNo.setEnabled(false);
				}
				else
				{
					lblRationCardStatus.setText("Ration Card Data Not Captured.");
					radioRationYes.setChecked(true);
					txtRationCardNo.setEnabled(true);
				}

			}
			else
			{
				lytRationCard.setVisibility(View.GONE);		

			}
		}
		catch(Exception e)
		{

		}
	}

}
