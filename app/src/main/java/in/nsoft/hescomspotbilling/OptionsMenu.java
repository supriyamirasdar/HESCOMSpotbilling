package in.nsoft.hescomspotbilling;


import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

//Created Nitish 26/02/2014	
public class OptionsMenu  {	
	//Based on Menu Item Clicked move to Logout or Print Order or Take Order Screen.
	public static boolean navigate(MenuItem item, Context context)
	{	
		//Remove BillingObject and CollectionObject
		//BillingObject.Remove();
		DatabaseHelper mDb = new DatabaseHelper(context); //Nitish 12-05-2014
		//CollectionObject.Remove();
		switch(item.getItemId())
		{
		//Logout Move to LogIn Screen
		case R.id.menuLogout:								
			Intent logout = new Intent(context,LoginActivity.class);
			context.startActivity(logout);					
			break;					

			//Move to Home Screen
		case R.id.menuHome:				
			Intent home = new Intent(context,HomePage.class);
			context.startActivity(home);
			break;
		case R.id.menuBilling:				
			Intent billing = new Intent(context,BillingStartActivity.class); //10-03-2021
			context.startActivity(billing);
			break;

		/*	//Move to Billing Screen
		case R.id.menuBilling:
			Intent billing = new Intent(context,Billing.class);
			context.startActivity(billing);
			break;	*/

			//Move to Collection Screen
		case R.id.menuCollections: 
			//Nitish 12-05-2014
			//CustomToast.makeText(context, "Collection Process under construction." , Toast.LENGTH_SHORT);
			if(mDb.GetStatusMasterDetailsByID("7") != 1)//if not equal to 1, SBM File is not Loaded	 
			{
				CustomToast.makeText(context,"Please Load SBM File to Database." , Toast.LENGTH_SHORT);

			}			
			else
			{
				Intent collections = new Intent(context,CashCounterSyncActivity.class);
				context.startActivity(collections);
			}	
			break;	

		case R.id.menuReports:
			Intent report = new Intent(context,ReportListActivity.class);
			context.startActivity(report);
			break;	

			//Move to Others Screen
		case R.id.menuOthers:
			Intent others = new Intent(context,OtherListActivity.class);
			context.startActivity(others);
			break;

		case R.id.menuAbout:
			Intent About = new Intent(context, AboutAppActivity.class);
			context.startActivity(About);
			break;
		
		case R.id.menuMobileStatus:
			Intent mobilestatus = new Intent(context, MobileStatusActivity.class);
			context.startActivity(mobilestatus);
			break;
		default: break;
		}	
		return true;
	}
}



