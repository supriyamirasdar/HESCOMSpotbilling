//Nitish 10-06-2015
package in.nsoft.hescomspotbilling;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;

public class ExitUSBActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exit_usb);
		
		String val = getIntent().getStringExtra("Exit");	
		CustomToast.makeText(ExitUSBActivity.this, "Device Not Attached/Unplugged" , Toast.LENGTH_SHORT);
		
		Intent intent = new Intent(Intent.ACTION_MAIN);			
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		
		Intent i = new Intent(ExitUSBActivity.this,LoginActivity.class);
		startActivity(i);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.spotbillingmenu, menu);
		return true;
	}

}
