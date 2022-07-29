//Created Nitish 21-03-2014
package in.nsoft.hescomspotbilling;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public final class CustomDatePicker {

	private  int month,day,year,mNoofBackDays, mNoofPostDays;
	private  EditText mEditText;
	private  Context mContext;
	private  Dialog dl;

	public CustomDatePicker(Context context,int EditTextId,Integer NoofBackDays,Integer NoofPostDays)
	{
		//Get Current Date
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);			 
		day = c.get(Calendar.DAY_OF_MONTH);
		mContext = context;	
		
		mNoofBackDays = NoofBackDays!=null?NoofBackDays:-1;
		mNoofPostDays =  NoofPostDays!=null?NoofPostDays:-1;
		
		String mMonth = month < 9?"0" :"";		
		String mDay = day < 10 ? "0" : "";
		
		mEditText = (EditText)((Activity)context).findViewById(EditTextId);
		mEditText.setText(new StringBuilder().append(mDay).append(day).append("-").append(mMonth).append(month +1).append("-").append(year).append(""));

		mEditText.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				arg0.setEnabled(false);
				onCreateDialog().show();
				return false;
			}
		});	
	}
	private  DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {		

		@Override
		public void onDateSet(DatePicker arg0, int selectedYear, int selectedMonth, int selectedDay) {
			// TODO Auto-generated method stub
			Calendar calpres= Calendar.getInstance();
			Calendar calpast= Calendar.getInstance();
			Calendar calfut= Calendar.getInstance();
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;	
			String mMonth = month < 9?"0" :"";		
			String mDay = day < 10 ? "0" : "";
			
			if(mNoofBackDays ==-1 && mNoofPostDays == -1 ) //No Restriction on FutureDays and PresentDays
			{				
				mEditText.setText(new StringBuilder().append(mDay).append(day).append("-").append(mMonth).append(month +1).append("-").append(year).append(""));
			}
			else if(mNoofBackDays ==-1 && mNoofPostDays != -1)  //Restriction on Future
			{
				calfut.add(Calendar.DATE, mNoofPostDays);
				calpres.set(selectedYear, selectedMonth, selectedDay);
				if(!(calpres.compareTo(calfut) <=0))
				{
					CustomToast.makeText(mContext, "Cheque/DD date should not be more than " + mNoofPostDays + " days.", Toast.LENGTH_SHORT);
				}
				else
				{
					mEditText.setText(new StringBuilder().append(mDay).append(day).append("-").append(mMonth).append(month +1).append("-").append(year).append(""));
				}
				
			}
			else if(mNoofBackDays !=-1 && mNoofPostDays == -1)  //Restriction on Past
			{
				calpast.add(Calendar.DATE, -mNoofBackDays);
				calpres.set(selectedYear, selectedMonth, selectedDay);
				if(!(calpres.compareTo(calpast) >=0))
				{
					CustomToast.makeText(mContext, "Cheque/DD date should not be less than " + mNoofBackDays + " days.", Toast.LENGTH_SHORT);
				}
				else
				{
					mEditText.setText(new StringBuilder().append(mDay).append(day).append("-").append(mMonth).append(month +1).append("-").append(year).append(""));
				}
				
			}
			
			else //Restriction on FutureDays and PastDays
			{
				calfut.add(Calendar.DATE, mNoofPostDays);
				calpast.add(Calendar.DATE, -mNoofBackDays);
				calpres.set(selectedYear, selectedMonth, selectedDay);
				
				if(calpres.compareTo(calpast) >=0 && calpres.compareTo(calfut) <=0)
				{
					mEditText.setText(new StringBuilder().append(mDay).append(day).append("-").append(mMonth).append(month +1).append("-").append(year).append(""));
				}
				else if(!(calpres.compareTo(calpast) >=0))
				{
					CustomToast.makeText(mContext, "Selected Date cannot be Less than " + mNoofBackDays + " days.", Toast.LENGTH_SHORT);
				}
				else if(!(calpres.compareTo(calfut) <=0))
				{
					CustomToast.makeText(mContext, "Selected Date cannot be more than " + mNoofPostDays + " days.", Toast.LENGTH_SHORT);
				}
			}			
			mEditText.setEnabled(true);			
		}		
	};
	private  Dialog onCreateDialog()
	{	
		//Get Current Date
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);			 
		day = c.get(Calendar.DAY_OF_MONTH);
		dl = new DatePickerDialog(mContext, datePickerListener, year, month, day);	
		dl.setCancelable(false);
		return dl;		
	}
}
