package in.nsoft.hescomspotbilling;
// Nitish 04-03-2014

public class ReportBilling {
	private String mMRName;
	private String mBillDate;	
	private int mTotalInstallations;	
	private int mBilled;
	private int mNotBilled;		
	
	public String getmMRName() {
		return mMRName;
	}
	public void setmMRName(String mMRName) {
		this.mMRName = mMRName;
	}
	public String getmBillDate() {
		return mBillDate;
	}
	public void setmBillDate(String mBillDate) {
		this.mBillDate = mBillDate;
	}	
	public int getmTotalInstallations() {
		return mTotalInstallations;
	}
	public void setmTotalInstallations(int mTotalInstallations) {
		this.mTotalInstallations = mTotalInstallations;
	}
	public int getmBilled() {
		return mBilled;
	}
	public void setmBilled(int mBilled) {
		this.mBilled = mBilled;
	}
	public int getmNotBilled() {
		return mNotBilled;
	}
	public void setmNotBilled(int mNotBilled) {
		this.mNotBilled = mNotBilled;
	}
}
