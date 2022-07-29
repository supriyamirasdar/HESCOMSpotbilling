package in.nsoft.hescomspotbilling;

//Nitish 12-06-2017
import java.math.BigDecimal;

import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.StaticLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutMCCActivity extends Activity {

	private TextView lblDesc;
	private static final String TAG = AboutMCCActivity.class.getName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_mcc);

		lblDesc = (TextView)findViewById(R.id.lblDesc);
		lblDesc.setText("MCC AUTHORIZATION " + "\r\n"
+ "\r\n"
+"1.	Before the activity is started the Application Program is loaded on the Android Smart Phone."+ "\r\n"
+"2.	When a SIM is inserted into the Smart Phone, IMEI Number and SIM Number are registered in the database."+ "\r\n"
+"3.	In order to allocate Billing/Collection activity to any Meter Reader  particular Smart Phone is to be allocated to the MR by the subdivision officer  which will be approved by Subdivision Head or as decided by Utility biometrically"+ "\r\n"
+"4.	Hence onwards any activity occurring in the Smart Phone (either billing or Collection ) will be accounted in the Name of the MR. "+ "\r\n"
+"5.	By default all Android Smart Phone will be considered as mobile collection centers. Further, provision to block receipt generation for  any Android Smart Phone is provided in Central Web based Application through a detailed validation process."+ "\r\n"
+"6.	MR has to request to server for initialization of collection process. If authorized, the MR  will receive OTP (One Time Password) to the registered Mobile No (To be registered in the back end which is carried by him (other than the one used for Billing and collection). OTP has to be entered into the mobile application which will be verified at the server end to enable the Collection process."+ "\r\n"
+"7.	Server receives the request from the MCC and validates the required credentials and sends the confirmation to MCC to start the collection activity."+ "\r\n"
+"8.	Once for a session (beginning of the day) MCC will validate the permission of the MR whether he is authorized to generate receipt or not. If not authorized to generate receipt (authorization blocked) then the MR will not be able to generate the receipts."+ "\r\n"
+ "\r\n"
+"CREDENTIAL VALIDATION"+ "\r\n"
+ "\r\n"
+"1.	The server checks whether the MR is authorized to collect revenue or not."+ "\r\n"
+"2.	IMEI No and SIM No registered in database is compared at the server end."+ "\r\n"
+"3.	The server checks whether any receipts of the previous session is yet to be transferred to the central server from the smart phone."+ "\r\n"
+"4.	Checks whether amount collected in the previous session is remitted to subdivision or not."+ "\r\n"
+"5.	If all the above conditions are satisfied then only the MR will be allowed to operate the MCC and draw receipts."+ "\r\n"
+ "\r\n"    
+"MCC INITIALIZATION "+ "\r\n"
+ "\r\n"
+"1.	MCC has the concept of day start and day close for generation of receipts. This timing is centrally defined or as decided by the utility. Receipt generation is allowed only in between this time period."+ "\r\n"
+"2.	MRs are allowed ideally only one session before depositing the collected amount. If required extension of the session for the second day will be allowed with due authorisation from the Utility officer."+ "\r\n"
+"3.	Unique Code (BatchNo) is generated for each session.(or two sessions if allowed). This will be printed in the Receipt."+ "\r\n"
+"4.	Collection can only be done for those consumers which are available in the Smart Phone SQLite Database irrespective of whether they are billed or not. "+ "\r\n"
+"5.	Mode of payment allowed in Smart Phone is Cash/Cheque"+ "\r\n"
+"6.	Financial limit is set for any Meter Reader for Collection process . He can collect only the maximum amount allowed. This is defined in the master in central database. "+ "\r\n"
+"7.	Subdivision officer will decide the max amount one can collect in any session. This will vary from MR to MR. Financial limit is defined in the Masters."+ "\r\n"
+"8.	Unique Code(Batch No ) will  be printed in the receipt.ReceiptNo logic is             CompayID/(BatchNo)/Month/Year/SlNo(Receipt SlNo) "+ "\r\n"   
+ "\r\n"                                                                                           
+"GENERATION OF RECEIPTS"+ "\r\n"
+ "\r\n"
+"1.	The MR will approach a consumer for payment."+ "\r\n"
+"2.	If the consumer is making the payment, the MR will enter the following in to the application."+ "\r\n"
+"		a)	Consumer ID which is used to search the receivable amount from the consumer."+ "\r\n"
+"		b)	After retrieving the consumer information, amount and mode of payment is entered after choosing the head of payment (Revenue / Misc.)"+ "\r\n"
+"		c)	The MR will be required to confirm the generation of receipt after which the hardcopy of the receipt will be printed through a BT enabled printer."+ "\r\n"
+"		d)	The receipt data will be transferred to the Server through the GPRS network."+ "\r\n"
+ "\r\n"
+"CLOSURE OF SESSION AND REMITTANCE"+ "\r\n"
+ "\r\n"
+"1.	MR has to formally close the counter before the max time limit is exceeded. IF not Closed the application software will automatically close the session once the allotted time period is over and any un-transferred records will be updated to the Server on closure of the session."+ "\r\n"
+"2.	MR will not be allowed to collect any amount once the session is closed until the settlement of account in respective Subdivisions."+ "\r\n"
+"3.	MR will physically remit the collected amount to Subdivision Cashier. Subdivision cashier will collect the cash and the report generated in MCC for verification of the amount collected. "+ "\r\n"
+"4.	Cashier will pass remittance entry in the central database through the web based application to acknowledge the receipt of cash/cheque from MR."+ "\r\n"
+"5.	Central database allows the receipt entry only for the closed sessions.");
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.		
		//getMenuInflater().inflate(R.menu.spotbillingmenu, menu);
		return true;
	}
	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return OptionsMenu.navigate(item, AboutMCCActivity.this);			
	}*/
}
