package in.nsoft.hescomspotbilling;
//29-07-2015
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BillingMeterDetailsCaptureActivity extends Activity implements AlertInterface {
	RadioGroup radioMeterLocation,radioBodySealed,radioTerminalCover,radioTerminalCoverSealed;
	RadioButton radioButtonMeterLocationYes,radioButtonMeterLocationNo , radioButtonBodySealedYes,radioButtonBodySealedNo,
				radioButtonTerminalCoverYes,radioButtonTerminalCoverNo,radioButtonTerminalCoverSealedYes,radioButtonTerminalCoverSealedNo;
	Spinner ddlInstallation,ddlMeterFixed,ddlMeterType,ddlInstallationStatus,ddlMeterMake;
	EditText txtAdharNo,txtVoterId,txtMobileNo,txtInstallationStatus,editSerialNo;
	DDLAdapter installationstatusadapter,installationAdapter,meterFixedAdapter,meterTypeAdapter,meterMakeAdapter;
	TextView txtRRNo,txtInstallation,txtMeterFixed,txtMeterType,lblConsumerDetails,txtMeterMake,txtSerialNo,txtMeterLocation,
			 txtBodySealed,txtTerminalCover,txtTerminalCoverSealed;
	Button btnContinue;
	DatabaseHelper db = new DatabaseHelper(this);
	private ReadSlabNTarifSbmBillCollection SlabColl;
	Handler dh;
	static String selectedstatus = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billing_meter_details_capture);

		dh= new Handler();
		SlabColl = BillingObject.GetBillingObject();//Get Billing Object

		txtRRNo = (TextView)findViewById(R.id.txtRRNo);

		txtInstallation = (TextView)findViewById(R.id.txtInstallation);
		txtMeterFixed = (TextView)findViewById(R.id.txtMeterFixed);
		txtMeterType = (TextView)findViewById(R.id.txtMeterType);
		lblConsumerDetails = (TextView)findViewById(R.id.lblConsumerDetails);
		
		txtMeterMake = (TextView)findViewById(R.id.txtMeterMake);
		txtSerialNo = (TextView)findViewById(R.id.txtSerialNo);
		txtMeterLocation = (TextView)findViewById(R.id.txtMeterLocation);
		txtBodySealed = (TextView)findViewById(R.id.txtBodySealed);
		txtTerminalCover = (TextView)findViewById(R.id.txtTerminalCover);
		txtTerminalCoverSealed = (TextView)findViewById(R.id.txtTerminalCoverSealed);



		ddlInstallation = (Spinner)findViewById(R.id.ddlInstallation);
		ddlMeterFixed = (Spinner)findViewById(R.id.ddlMeterFixed);
		ddlMeterType = (Spinner)findViewById(R.id.ddlMeterType);
		ddlInstallationStatus = (Spinner)findViewById(R.id.ddlInstallationStatus);
		
		ddlMeterMake = (Spinner)findViewById(R.id.ddlMeterMake);



		txtAdharNo = (EditText)findViewById(R.id.txtAdharNo);
		txtVoterId = (EditText)findViewById(R.id.txtVoterId);
		txtMobileNo = (EditText)findViewById(R.id.txtMobileNo);
	
		editSerialNo = (EditText)findViewById(R.id.editSerialNo);
		radioMeterLocation=(RadioGroup)findViewById(R.id.radioMeterLocation);
		radioBodySealed=(RadioGroup)findViewById(R.id.radioBodySealed);
		radioTerminalCover=(RadioGroup)findViewById(R.id.radioTerminalCover);
		radioTerminalCoverSealed=(RadioGroup)findViewById(R.id.radioTerminalCoverSealed);


		radioButtonMeterLocationYes=(RadioButton)findViewById(R.id.radioButtonMeterLocationYes);
		radioButtonMeterLocationNo=(RadioButton)findViewById(R.id.radioButtonMeterLocationNo);
		radioButtonBodySealedYes=(RadioButton)findViewById(R.id.radioButtonBodySealedYes);
		radioButtonBodySealedNo=(RadioButton)findViewById(R.id.radioButtonBodySealedNo);
		radioButtonTerminalCoverYes=(RadioButton)findViewById(R.id.radioButtonTerminalCoverYes);
		radioButtonTerminalCoverNo=(RadioButton)findViewById(R.id.radioButtonTerminalCoverNo);
		radioButtonTerminalCoverSealedYes=(RadioButton)findViewById(R.id.radioButtonTerminalCoverSealedYes);
		radioButtonTerminalCoverSealedNo=(RadioButton)findViewById(R.id.radioButtonTerminalCoverSealedNo);


		btnContinue = (Button)findViewById(R.id.btnContinue);

		try {

			txtRRNo.setText("RRNO:" + String.valueOf(SlabColl.getmRRNo()));


			installationstatusadapter = db.getInstallationStatusHescom();
			installationAdapter = db.getInstallationDetailsHescom();
			meterFixedAdapter = db.getMeterFixedHESCOM();
			meterTypeAdapter = db.getMeterTypeHESCOM();
			meterMakeAdapter=db.getMeterMakeHESCOM();


			ddlInstallationStatus.setAdapter(installationstatusadapter);
			ddlInstallation.setAdapter(installationAdapter);
			ddlMeterFixed.setAdapter(meterFixedAdapter);
			ddlMeterType.setAdapter(meterTypeAdapter);
			ddlMeterMake.setAdapter(meterMakeAdapter);

			txtInstallation.setVisibility(View.GONE);
			txtMeterFixed.setVisibility(View.GONE);
			txtMeterType.setVisibility(View.GONE);
			txtAdharNo.setVisibility(View.GONE);
			txtVoterId.setVisibility(View.GONE);
			txtMobileNo.setVisibility(View.GONE);
			lblConsumerDetails.setVisibility(View.GONE);
			ddlMeterFixed.setVisibility(View.GONE);
			ddlInstallation.setVisibility(View.GONE);
			ddlMeterType.setVisibility(View.GONE);




		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ddlInstallationStatus.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				try {	
					selectedstatus = installationstatusadapter.GetValue(arg2).toString();
					if(installationstatusadapter.GetValue(arg2).toString().equals("Accessible"))
					{
						txtInstallation.setVisibility(View.VISIBLE);
						txtMeterFixed.setVisibility(View.VISIBLE);
						txtMeterType.setVisibility(View.VISIBLE);
						lblConsumerDetails.setVisibility(View.VISIBLE);
						ddlMeterFixed.setVisibility(View.VISIBLE);
						ddlInstallation.setVisibility(View.VISIBLE);
						ddlMeterType.setVisibility(View.VISIBLE);
						txtAdharNo.setVisibility(View.VISIBLE);
						txtVoterId.setVisibility(View.VISIBLE);
						txtMobileNo.setVisibility(View.VISIBLE);


					
						txtMeterMake.setVisibility(View.VISIBLE);
						txtSerialNo.setVisibility(View.VISIBLE);
						txtMeterLocation.setVisibility(View.VISIBLE);
						txtBodySealed.setVisibility(View.VISIBLE);
						txtTerminalCover.setVisibility(View.VISIBLE);
						txtTerminalCoverSealed.setVisibility(View.VISIBLE);
						editSerialNo.setVisibility(View.VISIBLE);
						ddlMeterMake.setVisibility(View.VISIBLE);
						
						radioMeterLocation.setVisibility(View.VISIBLE);
						radioBodySealed.setVisibility(View.VISIBLE);
						radioTerminalCover.setVisibility(View.VISIBLE);
						radioTerminalCoverSealed.setVisibility(View.VISIBLE);


					}
					else
					{
						txtInstallation.setVisibility(View.GONE);
						txtMeterFixed.setVisibility(View.GONE);
						txtMeterType.setVisibility(View.GONE);
						lblConsumerDetails.setVisibility(View.GONE);
						ddlMeterFixed.setVisibility(View.GONE);
						ddlInstallation.setVisibility(View.GONE);
						ddlMeterType.setVisibility(View.GONE);
						txtAdharNo.setVisibility(View.GONE);
						txtVoterId.setVisibility(View.GONE);
						txtMobileNo.setVisibility(View.GONE);

					
						txtMeterMake.setVisibility(View.GONE);
						txtSerialNo.setVisibility(View.GONE);
						txtMeterLocation.setVisibility(View.GONE);
						txtBodySealed.setVisibility(View.GONE);
						txtTerminalCover.setVisibility(View.GONE);
						txtTerminalCoverSealed.setVisibility(View.GONE);
						editSerialNo.setVisibility(View.GONE);
						ddlMeterMake.setVisibility(View.GONE);
						radioMeterLocation.setVisibility(View.GONE);
						radioBodySealed.setVisibility(View.GONE);
						radioTerminalCover.setVisibility(View.GONE);
						radioTerminalCoverSealed.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		ddlInstallation.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				try {					
					SlabColl.setmMtrDisFlag(Integer.valueOf(installationAdapter.GetId(arg2)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				SlabColl.setmMtrDisFlag(0);
			}
		});

		ddlMeterFixed.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				try {					
					SlabColl.setmMeter_Present_Flag(Integer.valueOf(meterFixedAdapter.GetId(arg2)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				SlabColl.setmMeter_Present_Flag(0);
			}
		});

		ddlMeterType.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				try {					
					SlabColl.setmMeter_type(String.valueOf(meterTypeAdapter.GetId(arg2)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				SlabColl.setmMeter_type("0");                                                                                              
			}
		});
		//punit 06-08-2015
		ddlMeterMake.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				try {					
					SlabColl.setmMtrMakeFlag(Integer.valueOf(meterMakeAdapter.GetId(arg2)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				SlabColl.setmMtrMakeFlag(0);                                                                                             
			}
		});



		btnContinue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {


				SlabColl.setmMobileNo("00000000000000000"); 
				SlabColl.setmVoterIdNo(" ");   
				SlabColl.setmAadharNo(" ");    


				if(selectedstatus.equals("--SELECT--"))
				{
					CustomToast.makeText(BillingMeterDetailsCaptureActivity.this, "Select Installation Status", Toast.LENGTH_SHORT);						
					return;
				}
				else if(selectedstatus.equals("Accessible"))
				{
					try
					{
						//01-09-2015
						/*if(ddlInstallation.getSelectedItem().toString().equals("--SELECT--"))
						{
							CustomToast.makeText(BillingMeterDetailsCaptureActivity.this, "Select Installation", Toast.LENGTH_SHORT);						
							return;
						}
						else if(ddlMeterFixed.getSelectedItem().toString().equals("--SELECT--"))
						{
							CustomToast.makeText(BillingMeterDetailsCaptureActivity.this, "Select Meter Fixed Details", Toast.LENGTH_SHORT);						
							return;
						}
						else if(ddlMeterType.getSelectedItem().toString().equals("--SELECT--"))
						{
							CustomToast.makeText(BillingMeterDetailsCaptureActivity.this, "Select Meter Type", Toast.LENGTH_SHORT);						
							return;
						}*/
						if(ddlMeterMake.getSelectedItem().toString().equals("--SELECT--"))
						{
							CustomToast.makeText(BillingMeterDetailsCaptureActivity.this, "Select MeterMake", Toast.LENGTH_SHORT);						
							return;
						}
						else
						{

							if(ddlMeterMake.getSelectedItem().toString().equals("DC/NOMETER"))
							{								
								SlabColl.setmMeter_serialno("0000000000")	;

							}
							else
							{
								if(editSerialNo.getText().length()==0)
								{
									CustomToast.makeText(BillingMeterDetailsCaptureActivity.this, "Enter SerialNo", Toast.LENGTH_SHORT);						
									return;
								}
								else
								{
									SlabColl.setmMeter_serialno(editSerialNo.getText().toString())	;
								}
							}					

							//1############################################# Radio Button for Location##########################							
							int locationConnectionSelectOption =radioMeterLocation.getCheckedRadioButtonId();
							radioButtonMeterLocationYes =(RadioButton)findViewById(locationConnectionSelectOption);							
							if(locationConnectionSelectOption == -1)
							{

								CustomToast.makeText(BillingMeterDetailsCaptureActivity.this, " Select Meter Location.",  Toast.LENGTH_SHORT);
								return;
							}
							else
							{								
								if(radioButtonMeterLocationYes.getText().equals("Inside"))
								{
									SlabColl.setmMtr_Not_Visible(1);
								}
								
								else if (radioButtonMeterLocationYes.getText().equals("Outside"))
								{
									SlabColl.setmMtr_Not_Visible(2);
								}								
							}

							//############################################# End ############################################################
							//2############################################# Radio Button for BodySealed##########################							
							int statusBodySealed =radioBodySealed.getCheckedRadioButtonId();
							radioButtonBodySealedYes =(RadioButton)findViewById(statusBodySealed);							
							if(statusBodySealed == -1)
							{
								CustomToast.makeText(BillingMeterDetailsCaptureActivity.this, " Select Status Of BodySealed.",  Toast.LENGTH_SHORT);
								return;
							}
							else
							{								
								if(radioButtonBodySealedYes.getText().equals("Yes"))
								{
									SlabColl.setmMtrBodySealFlag(1);									
								}
								else if (radioButtonBodySealedYes.getText().equals("No"))
								{
									SlabColl.setmMtrBodySealFlag(2);									
								}
							}
							//############################################# End ############################################################
							//3############################################# Radio Button for Terminal Cover##########################							
							int statusradioTerminalCover =radioTerminalCover.getCheckedRadioButtonId();
							radioButtonTerminalCoverYes =(RadioButton)findViewById(statusradioTerminalCover);							
							if(statusradioTerminalCover == -1)
							{

								CustomToast.makeText(BillingMeterDetailsCaptureActivity.this, " Select Status Of Terminal Cover.",  Toast.LENGTH_SHORT);
								return;
							}
							else
							{								
								if(radioButtonTerminalCoverYes.getText().equals("Yes"))
								{
									SlabColl.setmMtrTerminalCoverFlag(1);									
								}
								else if (radioButtonTerminalCoverYes.getText().equals("No"))
								{
									SlabColl.setmMtrTerminalCoverFlag(2);								
								}
							}
							//############################################# End ############################################################
							//4############################################# Radio Button for Terminal Cover Sealed##########################							
							int statusradioTerminalCoverSealed =radioTerminalCoverSealed.getCheckedRadioButtonId();
							radioButtonTerminalCoverSealedYes =(RadioButton)findViewById(statusradioTerminalCoverSealed);							
							if(statusradioTerminalCoverSealed == -1)
							{
								CustomToast.makeText(BillingMeterDetailsCaptureActivity.this, " Select Status Of Terminal Cover Sealed",  Toast.LENGTH_SHORT);
								return;
							}
							else
							{								
								if(radioButtonTerminalCoverSealedYes.getText().equals("Yes"))
								{
									SlabColl.setmMtrTerminalCoverSealedFlag(1);									
								}
								else if (radioButtonTerminalCoverSealedYes.getText().equals("No"))
								{
									SlabColl.setmMtrTerminalCoverSealedFlag(2);									
								}                  
							}
							//############################################# End ############################################################							
							if((txtAdharNo.getText().length()>0 && txtAdharNo.getText().length()!=12) || (txtAdharNo.getText().length()==0))
							{
								SlabColl.setmAadharNo(" ");    
								if((txtAdharNo.getText().length()>0 && txtAdharNo.getText().length()!=12))
								{
									CustomToast.makeText(BillingMeterDetailsCaptureActivity.this, "Invalid Aadhar No", Toast.LENGTH_SHORT);						
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
									CustomToast.makeText(BillingMeterDetailsCaptureActivity.this, "Invalid VoterID No", Toast.LENGTH_SHORT);						
									return;
								}								
							}
							else if(txtVoterId.getText().length()==10)
							{
								SlabColl.setmVoterIdNo(txtVoterId.getText().toString());    
							}
							if((txtMobileNo.getText().length()>0 && txtMobileNo.getText().length()!=10) || (txtMobileNo.getText().length()==0))
							{
								SlabColl.setmMobileNo("00000000000000000");    
								if((txtMobileNo.getText().length()>0 && txtMobileNo.getText().length()!=10))
								{
									CustomToast.makeText(BillingMeterDetailsCaptureActivity.this, "Invalid Mobile No", Toast.LENGTH_SHORT);						
									return;
								}								
							}
							else if(txtMobileNo.getText().length()==10)
							{
								if(txtMobileNo.getText().toString().startsWith("0")||txtMobileNo.getText().toString().startsWith("1")|| txtMobileNo.getText().toString().startsWith("2") || txtMobileNo.getText().toString().startsWith("3") || txtMobileNo.getText().toString().startsWith("4") || txtMobileNo.getText().toString().startsWith("5"))							
								{
									SlabColl.setmMobileNo("00000000000000000");    
									CustomToast.makeText(BillingMeterDetailsCaptureActivity.this, "Invalid Mobile No", Toast.LENGTH_SHORT);						
									return;								
								}
								else
								{
									SlabColl.setmMobileNo(txtMobileNo.getText().toString());    
								}
							}
							Intent i = new Intent(BillingMeterDetailsCaptureActivity.this, BillingConsumption.class);
							startActivity(i);

							/*StringBuilder sb = new StringBuilder();	
						sb.append("Installation		       : " + ddlInstallation.getSelectedItem().toString());
						sb.append("Meter Fixed		       : " + ddlMeterFixed.getSelectedItem().toString());
						sb.append("Meter Type		       : " + ddlMeterType.getSelectedItem().toString());
						sb.append("Aadhar No		       : " + SlabColl.getmAadharNo());
						sb.append("Voter ID No		       : " + SlabColl.getmVoterIdNo());
						sb.append("MobileNo		       	   : " + SlabColl.getmMobileNo());
						CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
						params.setContext(BillingMeterDetailsCaptureActivity.this);					
						params.setMainHandler(dh);
						params.setMsg(sb.toString());
						params.setButtonOkText("OK");						
						params.setTitle("Meter Details");
						params.setFunctionality(1);
						CustomAlert cAlert  = new CustomAlert(params);*/
						}
					} 
					catch (Exception e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					SlabColl.setmMtrDisFlag(0);
					SlabColl.setmMeter_Present_Flag(0);
					SlabColl.setmMeter_type("0");             
					SlabColl.setmMobileNo("00000000000000000"); 					
					SlabColl.setmMeter_serialno("0000000000"); 
					SlabColl.setmMtrTerminalCoverFlag(0);	
					SlabColl.setmMtr_Not_Visible(0);
					SlabColl.setmMtrBodySealFlag(0);	
					SlabColl.setmMtrTerminalCoverSealedFlag(0);			
					Intent i = new Intent(BillingMeterDetailsCaptureActivity.this, BillingConsumption.class);
					startActivity(i);
				}



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
		return OptionsMenu.navigate(item,BillingMeterDetailsCaptureActivity.this);			
	}
	//End Nitish 26-02-2014
	@Override
	public void onBackPressed() {		
		return;
	}

	@Override
	public void performAction(boolean alertResult, int functionality) {
		// TODO Auto-generated method stub
		if(functionality==1)
		{		
			Intent i = new Intent(BillingMeterDetailsCaptureActivity.this, BillingConsumption.class);
			startActivity(i);	
		}
	}


}
