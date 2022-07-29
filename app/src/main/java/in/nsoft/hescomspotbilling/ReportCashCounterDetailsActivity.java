package in.nsoft.hescomspotbilling;
//Nitish 06-05-2014
import java.math.BigDecimal;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ReportCashCounterDetailsActivity extends Activity {

	TextView txtCashCounter,txtCCBatchNo,txtStartTime,txtEndTime,txtCashLimit,txtBatchDate,txtCollectionMade;	
	BigDecimal cLimit,cMade;	
	CashCounterObject CC ;	
	String colAmt,bDate,sTime,eTime;	
	private final DatabaseHelper mDb =new DatabaseHelper(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_cash_counter_details);			
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		txtCashCounter = (TextView)findViewById(R.id.txtReportCashCounter);
		txtCCBatchNo =  (TextView)findViewById(R.id.txtReportCCBatchNo);
		txtBatchDate =  (TextView)findViewById(R.id.txtReportBatchDate);
		txtStartTime =  (TextView)findViewById(R.id.txtReportStartTime);
		txtEndTime =  (TextView)findViewById(R.id.txtReportEndTime);
		txtCashLimit =  (TextView)findViewById(R.id.txtReportCashLimit);
		txtCollectionMade =  (TextView)findViewById(R.id.txtReportCollectionMade);		
		try
		{	
		
			CC = mDb.GetCashCounterDetails();		
			
			colAmt = mDb.GetCollectionAmount(CC.getmBatch_No()); //Get Total Collected amount for that batchno		
			bDate = (new CommonFunction()).DateConvertAddChar(CC.getmBatch_Date(), "-"); //Obtain BatchDate
			sTime = (new CommonFunction()).TimeConvertAddChar(CC.getmStartTime(), ":"); //Obtain Cash Counter Start Time
			eTime = (new CommonFunction()).TimeConvertAddChar(CC.getmEndTime(), ":"); //Obtain Cash Counter End Time	
			
			cLimit = new BigDecimal(CC.getmCashLimit());
			cMade = new BigDecimal(colAmt);		
			
			txtCCBatchNo.setText(CC.getmBatch_No());
			txtBatchDate.setText(bDate);
			txtStartTime.setText(sTime);
			txtEndTime.setText(eTime);
			txtCashLimit.setText(CC.getmCashLimit()+ ConstantClass.sPaise );
			txtCollectionMade.setText(colAmt+ ConstantClass.sPaise );
			
			if(mDb.GetStatusMasterDetailsByID("11") == 1)//if equal to 1, CashCounter Open 
			{				
				txtCashCounter.setText(ConstantClass.mOpen);			
			}
			else //else CashCounter Closed 
			{
				txtCashCounter.setText(ConstantClass.mOpen);	
			}		
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			//txtIMEISIMNo.setText();
			CustomToast.makeText(ReportCashCounterDetailsActivity.this, "Collection Data does not Exist ", Toast.LENGTH_SHORT);
			e.printStackTrace();
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
		return OptionsMenu.navigate(item,ReportCashCounterDetailsActivity.this);			
	}

}
