package in.nsoft.hescomspotbilling;

import android.content.Context;

//Created By Tamilselvan on 21-02-2014
public class BillingObject {
	private static ReadSlabNTarifSbmBillCollection mBillingObject;
	
	private BillingObject()
	{
		
	}
	
	//Get the Object
	public static ReadSlabNTarifSbmBillCollection GetBillingObject()
	{
		if(mBillingObject == null)
		{
			mBillingObject = new ReadSlabNTarifSbmBillCollection();			
		}
		return mBillingObject;
	}
	
	//Remove Object
	public static void Remove()
	{
		mBillingObject = null;
	}
	public static void reset(Context ctx) throws Exception
	{
		/*mBillingObject.setmUnits(null);
		mBillingObject.setmTFc(null);
		mBillingObject.setMmFCRate_1(null);
		mBillingObject.setMmCOL_FCRate_2(null);
		mBillingObject.setMmFC_2(null);
		mBillingObject.setMmFC_1(null);
		mBillingObject.setmTaxAmt(null);
		mBillingObject.setmGoKPayable(null);
		mBillingObject.setmTEc(null);
		mBillingObject.setmKVAFR(null);
		mBillingObject.setMmUnits_1(null);
		mBillingObject.setMmUnits_2(null);
		mBillingObject.setMmUnits_3(null);
		mBillingObject.setMmUnits_4(null);
		mBillingObject.setMmUnits_5(null);
		mBillingObject.setMmUnits_6(null);
		mBillingObject.setMmEC_Rate_1(null);
		mBillingObject.setMmEC_Rate_2(null);
		mBillingObject.setMmEC_Rate_3(null);
		mBillingObject.setMmEC_Rate_4(null);
		mBillingObject.setMmEC_Rate_5(null);
		mBillingObject.setMmEC_Rate_6(null);
		mBillingObject.setMmEC_1(null);
		mBillingObject.setMmEC_2(null);
		mBillingObject.setMmEC_3(null);
		mBillingObject.setMmEC_4(null);
		mBillingObject.setMmEC_5(null);
		mBillingObject.setMmEC_6(null);
		mBillingObject.setMmEC_FL_1(null);
		mBillingObject.setMmEC_FL_2(null);
		mBillingObject.setMmEC_FL_3(null);
		mBillingObject.setMmEC_FL_4(null);
		mBillingObject.setMmEC_FL(null);
		mBillingObject.setmFLReb(null);
		mBillingObject.setmLinMin(null);

		mBillingObject.setmRecordDmnd(null);
		mBillingObject.setmExLoad(null);
		mBillingObject.setmDemandChrg(null);
		mBillingObject.setmPenExLd(null);		
		mBillingObject.setmSancHp(null);

		mBillingObject.setmBillTotal(null);*/
		DatabaseHelper db = new DatabaseHelper(ctx);
		db.GetAllDatafromDb(mBillingObject.getmConnectionNo());
		
		
	}
}
