
package in.nsoft.hescomspotbilling;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;


public class CustomPhoneListener extends PhoneStateListener{

	int sstrength = 0;
	static String SIGNAL_STRENGTH = "";	


	@Override
	public void onSignalStrengthsChanged(SignalStrength signalStrength) {
		// TODO Auto-generated method stub
		super.onSignalStrengthsChanged(signalStrength);
		sstrength = signalStrength.getGsmSignalStrength();
		sstrength = (2*sstrength) - 113 ; //In dbm

		//High            Normal            Low
		//Above -75		 -100 to  -75 		Below -100

		if(sstrength > -75)
		{
			SIGNAL_STRENGTH = ConstantClass.sHIGH;
		}
		else if(sstrength < -100)
		{
			SIGNAL_STRENGTH = ConstantClass.sLOW;
		}
		else
		{
			SIGNAL_STRENGTH = ConstantClass.sNORMAL;
		}

	}
}
