package in.nsoft.hescomspotbilling;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ReportTariffwiseActivity extends Activity implements AlertInterface {

	TextView txtDate,lbltotal,lblInstStatus,lblNoOfCons,lblTotalDemand,lbltotalconsum;
	private Button btntariffPrint;
	private ArrayList<DDLItem> alConStatusItem;
	ArrayList<ReportTariff> alRep ;
	ReportAdapter adapter;
	ProgressDialog ringProgress;	
	DDLAdapter DaywiseAdapter;
	Handler dh ;
	Spinner spinnerDaywise;
	//final static String sPaise = ".00";
	static final int REQUEST_ENABLE_BT = 0;	
	BluetoothPrinting bptr;
	final static String TAG = "in.nsoft.spotBilling.ReportTariffwiseActivity";
	//int sumConsumption = 0;
	BigDecimal sumConsumption = new BigDecimal(0.00);
	BigDecimal sumDemand =new BigDecimal(0.00);
	int sumConsumers = 0; //30-08-2016

	CustomAlert cAlert ;
	boolean malertResult;
	final static String alertmsg = "Are you sure you want to Print?";
	final static String alerttitle = "Billed Report";
	final static String btnAlertYes = "Yes";
	final static String btnAlertNo = "No";

	private final DatabaseHelper mDb =new DatabaseHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_tariffwise);
		try {

			dh = new Handler();
			lblNoOfCons=(TextView)findViewById(R.id.lblNoOfCons);
			lblTotalDemand=(TextView)findViewById(R.id.lblTotalDemand);
			lbltotalconsum=(TextView)findViewById(R.id.lbltotalconsum);
			spinnerDaywise=(Spinner)findViewById(R.id.spinnerDaywise);
			//txtDate=(TextView)findViewById(R.id.txtDate);
			btntariffPrint=(Button)findViewById(R.id.btntariffPrint);
			String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());			
			//txtDate.setText(currentDate);
			DaywiseAdapter = mDb.GetTariffDaywise();
			spinnerDaywise.setAdapter(DaywiseAdapter);

			alRep = mDb.GetReportTarriff(DaywiseAdapter.getItem(0).getValue());

			if(alRep.size() ==0)
			{
				CustomToast.makeText(this, "Tarrif Data does not Exist", Toast.LENGTH_SHORT);
				//btntariffPrint.setVisibility(View.INVISIBLE);				
			}

			else
			{
				for(int i = 0;i<alRep.size() ; i++)
				{

					sumConsumers = sumConsumers + Integer.valueOf(alRep.get(i).getConnectionNo());//30-08-2016
					/*int consumption = Integer.valueOf(alRep.get(i).getUnits());
					sumConsumption = sumConsumption + consumption;				

				//BigDecimal bsum =((alRep.get(i).getBillTotal()));
					sumDemand =  sumDemand.add(((alRep.get(i).getBillTotal())));*/			
					BigDecimal consumption = ((new BigDecimal(alRep.get(i).getUnits()).setScale(0, BigDecimal.ROUND_HALF_UP)));
					sumConsumption = sumConsumption.add(consumption);				

					//BigDecimal bsum =((alRep.get(i).getBillTotal()));
					sumDemand =  sumDemand.add(alRep.get(i).getBillTotal().setScale(0, BigDecimal.ROUND_HALF_UP));

				}
				//int Demand= Integer.valueOf((sumDemand).toString());
				lblNoOfCons.setText(String.valueOf(sumConsumers));//30-08-2016
				lblTotalDemand.setText(sumDemand.toString());
				lbltotalconsum.setText(sumConsumption.toString());
			}
			//To get List Parameters
			adapter = new ReportAdapter(alRep);			
			ListView lv = (ListView) findViewById(R.id.lsttariff);		
			lv.setAdapter(adapter);

		} catch (Exception e) {
			// TODO: handle exception
			if(e.toString().length()>15)
			{
				e.toString().substring(0,13);
				mDb.EventLogSave("15", LoginActivity.IMEINumber, LoginActivity.SimNumber, "ReportTariffwiseActivity:1"+ e.toString().substring(0,13)); //15-07-2016

			}
			else
			{			
				mDb.EventLogSave("15", LoginActivity.IMEINumber, LoginActivity.SimNumber, "ReportTariffwiseActivity:1 OnCreate"); //15-07-2016

			}
			e.printStackTrace();
		}


		spinnerDaywise.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				try
				{
					alRep = mDb.GetReportTarriff(spinnerDaywise.getSelectedItem().toString());

					sumConsumers = 0; //30-08-2016
					sumConsumption=new BigDecimal(0.00);
					sumDemand=new BigDecimal(0.00);
					for(int i = 0;i<alRep.size() ; i++)
					{

						sumConsumers = sumConsumers + Integer.valueOf(alRep.get(i).getConnectionNo());
						/*int consumption = Integer.valueOf(alRep.get(i).getUnits());
						sumConsumption = sumConsumption + consumption;				

					//BigDecimal bsum =((alRep.get(i).getBillTotal()));
						sumDemand =  sumDemand.add(((alRep.get(i).getBillTotal())));*/			
						BigDecimal consumption = ((new BigDecimal(alRep.get(i).getUnits()).setScale(0, BigDecimal.ROUND_HALF_UP)));
						sumConsumption = sumConsumption.add(consumption);				

						//BigDecimal bsum =((alRep.get(i).getBillTotal()));
						sumDemand =  sumDemand.add(alRep.get(i).getBillTotal().setScale(0, BigDecimal.ROUND_HALF_UP));


					}
					//int Demand= Integer.valueOf((sumDemand).toString());

					lblNoOfCons.setText(String.valueOf(sumConsumers)); //30-08-2016
					lblTotalDemand.setText(sumDemand.toString());
					lbltotalconsum.setText(sumConsumption.toString());

					adapter = new ReportAdapter(alRep);	
					ListView lv = (ListView) findViewById(R.id.lsttariff);		
					lv.setAdapter(adapter);
				} catch (Exception e) {
					// TODO: handle exception
					if(e.toString().length()>15)
					{
						e.toString().substring(0,13);
						mDb.EventLogSave("15", LoginActivity.IMEINumber, LoginActivity.SimNumber, "ReportTariffwiseActivity:2"+ e.toString().substring(0,13)); //15-07-2016

					}
					else
					{			
						mDb.EventLogSave("15", LoginActivity.IMEINumber, LoginActivity.SimNumber, "ReportTariffwiseActivity:2 spinnerDaywise block"); //15-07-2016

					}
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		btntariffPrint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//Nitish 21-03-2014
				//Set Parameters of CustomAlert
				CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
				params.setContext(ReportTariffwiseActivity.this);					
				params.setMainHandler(dh);
				params.setMsg(alertmsg);
				params.setButtonOkText(btnAlertNo);
				params.setButtonCancelText(btnAlertYes);
				params.setTitle(alerttitle);
				params.setFunctionality(1);
				cAlert = new CustomAlert(params);					
				//End Nitish 21-03-2014
				//Cut from here


			}
		});	


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.spotbillingmenu, menu);
		return true;
	}@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return OptionsMenu.navigate(item,ReportTariffwiseActivity.this);			
	}


	//Class for List View
	private class ReportAdapter extends ArrayAdapter<ReportTariff>
	{

		public ReportAdapter(ArrayList<ReportTariff> mReportList) {		
			super(ReportTariffwiseActivity.this, R.layout.fourtariffitemlist, mReportList);		

		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) // If we weren't given a view, inflate one
		{
			try
			{		
				ReportTariff rb = getItem(position);
				if (convertView == null) {
					convertView = ReportTariffwiseActivity.this.getLayoutInflater().inflate(R.layout.fourtariffitemlist, null);					
				}
				// Set List View Parameters
				TextView lblNoofCons=(TextView)convertView.findViewById(R.id.lblNoofCons);
				TextView Rtariff = (TextView)convertView.findViewById(R.id.lbltariff);
				TextView RConsumption = (TextView)convertView.findViewById(R.id.lblConsumption);
				TextView RDemand = (TextView)convertView.findViewById(R.id.lblDemand);

				String Noofcons=rb.getConnectionNo()== null ? "" : rb.getConnectionNo();
				String tariffCode = rb.getTariffCode()== null ? "" : rb.getTariffCode();
				String consumption = rb.getUnits()== null ? "" : rb.getUnits();
				String demand =  String.valueOf(rb.getBillTotal()== null ? "" : rb.getBillTotal().setScale(0, BigDecimal.ROUND_HALF_UP).toString());

				lblNoofCons.setText(Noofcons);
				Rtariff.setText(mDb.getTariffString(tariffCode).split("#")[0].split("%%")[1]);
				RConsumption.setText(consumption);
				RDemand.setText(demand);

			}		
			catch(Exception e)
			{
				if(e.toString().length()>15)
				{
					e.toString().substring(0,13);
					mDb.EventLogSave("15", LoginActivity.IMEINumber, LoginActivity.SimNumber, "ReportTariffwiseActivity:3"+ e.toString().substring(0,13)); //15-07-2016

				}
				else
				{			
					mDb.EventLogSave("15", LoginActivity.IMEINumber, LoginActivity.SimNumber, "ReportTariffwiseActivity:3 Error view"); //15-07-2016

				}
				Toast.makeText(ReportTariffwiseActivity.this, "Error View " , Toast.LENGTH_LONG).show();			
			}
			return convertView;
		}
	}


	@Override
	public void performAction(boolean alertResult, int functionality) {
		// TODO Auto-generated method stub
		if(!alertResult)
		{		
			bptr = new BluetoothPrinting(ReportTariffwiseActivity.this);
			ringProgress = ProgressDialog.show(ReportTariffwiseActivity.this, "Please wait..", "Printing...",true);//Spinner
			ringProgress.setCancelable(false);
			try 
			{
				//Tamilselvan on 19-03-2014
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Looper.prepare();
							bptr.openBT();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ConnectedThread cth = new ConnectedThread();
						cth.start(); 
						Looper.loop(); 
					}
				}).start();/**///Tamilselvan on 19-03-2014
			} 
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d(TAG,e.toString());
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)	
	public class ConnectedThread extends Thread //ConnectedThread
	{	
		public void run(){
			PrinterCharacterAlign pca = new PrinterCharacterAlign();//Class Alignment for printer 
			CommonFunction cFun = new CommonFunction();	

			try 
			{
				String timeStamp = cFun.GetCurrentTime();

				String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());		
				alRep = mDb.GetReportTarriff(spinnerDaywise.getSelectedItem().toString());

				bptr.sendData(ConstantClass.data1);					
				bptr.sendData(ConstantClass.linefeed1);		
				bptr.sendData(pca.CenterAlign("TARIFFWISE REPORT"," ").getBytes());//Title					
				bptr.sendData(pca.CenterAlign(timeStamp," ").getBytes());//TimeStamp				
				bptr.sendData(pca.TwoParallelString("READING DAY",spinnerDaywise.getSelectedItem().toString(), ' ', ':').getBytes());//30-08-2016
				bptr.sendData(ConstantClass.linefeed1);	
				bptr.sendData(pca.FourParallelStringRRNoCollReport("Tariff","No.Of","Consumption","Demand", ' ').getBytes());//ColumnNames				
				bptr.sendData(pca.FourParallelStringRRNoCollReport("","Cons","","", ' ').getBytes());//ColumnNames	

				bptr.sendData(pca.LineSeperation().getBytes());					
				for(ReportTariff rb : alRep) //Get Details From List
				{	
					String NoofCons = rb.getConnectionNo()== null ? "" : rb.getConnectionNo();
					String Tariff = rb.getTariffCode()== null ? "" : rb.getTariffCode();
					String PTariff=mDb.getTariffString(Tariff).split("#")[0].split("%%")[1];
					String Consumption = rb.getUnits()== null ? "" : rb.getUnits();
					String demand =  String.valueOf(rb.getBillTotal()== null ? "" : rb.getBillTotal().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
					bptr.sendData(pca.FourParallelStringRRNoCollReport(PTariff,NoofCons,Consumption,demand, ' ').getBytes());
					
				}
				bptr.sendData(pca.LineSeperation().getBytes());					
				bptr.sendData(pca.FourParallelStringRRNoCollReport("Total", String.valueOf(sumConsumers), String.valueOf(sumConsumption), String.valueOf(sumDemand), ' ').getBytes());//ColumnNames				

				bptr.sendData(pca.LineSeperation().getBytes());	
				bptr.sendData(ConstantClass.linefeed1);
				bptr.sendData(ConstantClass.linefeed1);			
								
			}
			catch (Exception e)
			{
				if(e.toString().length()>15)
				{
					e.toString().substring(0,13);
					mDb.EventLogSave("15", LoginActivity.IMEINumber, LoginActivity.SimNumber, "ReportTariffwiseActivity:6"+ e.toString().substring(0,13)); //15-07-2016

				}
				else if(e.toString().length()<15) 
				{			
					mDb.EventLogSave("15", LoginActivity.IMEINumber, LoginActivity.SimNumber, "ReportTariffwiseActivity:6"+ e.toString()); //1507-2016
				}
				// TODO Auto-generated catch block
				Log.d(TAG,e.toString());
				//if printer turn off without of server, then after printer turn on process again start
			}
			finally
			{
				try 
				{
					bptr.closeBT();
					if(BluetoothPrinting.isConnected)
					{
						Thread.sleep(5000);
					}
				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//Tamilselvan on 19-03-2014
				ringProgress.dismiss();
			}
		}
		public void cancel()
		{

		}
	}//END ConnectedThread
	//END Bluetooth Code====================================================================================

}
