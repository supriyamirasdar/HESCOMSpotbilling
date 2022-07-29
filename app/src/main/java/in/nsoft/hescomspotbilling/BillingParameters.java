package in.nsoft.hescomspotbilling;

import java.math.BigDecimal;
import java.util.ArrayList;

//Created by Tamilselvan on 24-02-2014
public class BillingParameters {	

	public BillingParameters()
	{
		mFixedCharges = new ArrayList<FixedCharges>();
		mEnergyCharges = new ArrayList<EnergyCharges>();
	}
	private ArrayList<FixedCharges> mFixedCharges; 
	private ArrayList<EnergyCharges> mEnergyCharges;


	public ArrayList<FixedCharges> getmFixedCharges() {
		return mFixedCharges;
	}
	public void setmFixedCharges(ArrayList<FixedCharges> mFixedCharges) {
		this.mFixedCharges = mFixedCharges;
	}

	public ArrayList<EnergyCharges> getmEnergyCharges() {
		return mEnergyCharges;
	}
	public void setmEnergyCharges(ArrayList<EnergyCharges> mEnergyCharges) {
		this.mEnergyCharges = mEnergyCharges;
	}
	
	//Created by Tamilselvan on 26-02-2014
	public class EnergyCharges
	{
		private String UnitRange;
		private String Rate;
		private String Amount;
		
		public String getUnitRange() {
			return UnitRange;
		}
		public void setUnitRange(String unitRange) {
			UnitRange = unitRange;
		}
		
		public String getRate() {
			return Rate;
		}
		public void setRate(String rate) {
			Rate = rate;
		}
		
		public String getAmount() {
			return Amount;
		}
		public void setAmount(String amount) {
			Amount = amount;
		}
		
	}
	//Created by Tamilselvan on 26-02-2014
	public class FixedCharges
	{
		private String UnitRange;
		private String Rate;
		private String Amount;
		
		public String getUnitRange() {
			return UnitRange;
		}
		public void setUnitRange(String unitRange) {
			UnitRange = unitRange;
		}
		
		public String getRate() {
			return Rate;
		}
		public void setRate(String rate) {
			Rate = rate;
		}
		
		public String getAmount() {
			return Amount;
		}
		public void setAmount(String amount) {
			Amount = amount;
		}
		
	}
	//Created By Tamilselvan on 25-06-2014
	public static String CalculateRebate(ReadSlabNTarifSbmBillCollection sbc)
	{
		String rebateAmt = "";			
		//By Tamilselvan on 25-02-2014
		String strCapReb = "CH";
		String strHLReb = "W";
		String strHCReb = "H";
		String strECReb = "S";
		String strFLReb = "F";
		String strHWCReb = "WA";
		//By Tamilselvan

		BigDecimal capReb = sbc.getmCapReb();
		BigDecimal hwcReb = new BigDecimal(0.00);
		BigDecimal flReb = sbc.getmFLReb();
		BigDecimal ecReb = sbc.getmECReb();
		BigDecimal hlReb = sbc.getmHLReb();
		BigDecimal hcReb = sbc.getmHCReb();
		if(capReb.compareTo(new BigDecimal(0.00)) == 1)
			rebateAmt = rebateAmt + strCapReb;
		if(hwcReb.compareTo(new BigDecimal(0.00)) == 1)
			rebateAmt = rebateAmt + strHWCReb;
		if(flReb.compareTo(new BigDecimal(0.00)) == 1)
			rebateAmt = rebateAmt + strFLReb;
		if(ecReb.compareTo(new BigDecimal(0.00)) == 1)
			rebateAmt = rebateAmt + strECReb;
		if(hlReb.compareTo(new BigDecimal(0.00)) == 1)
			rebateAmt = rebateAmt + strHLReb;
		if(hcReb.compareTo(new BigDecimal(0.00)) == 1)
			rebateAmt = rebateAmt + strHCReb;

		rebateAmt = rebateAmt+" "+ String.valueOf(capReb.add(hwcReb.add(flReb.add(ecReb.add(hlReb.add(hcReb))))));
		return rebateAmt;
	}
}
