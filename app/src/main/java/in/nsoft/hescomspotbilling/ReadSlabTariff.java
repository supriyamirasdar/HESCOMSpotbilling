package in.nsoft.hescomspotbilling;

import java.util.Collections;
import java.util.List;

import android.content.Context;

public final class ReadSlabTariff {
	
	private Context ctx;
	public ReadSlabTariff(Context cx)
	{
		ctx = cx; 
	}
	
	private  List<String> TariffName;	
	
	
	public  List<String> getTariffName() {
		
		if(TariffName == null){
			DatabaseHelper db = new DatabaseHelper(ctx);
			TariffName = db.calldb();
		}
		return  Collections.unmodifiableList(TariffName) ;
	}
}
