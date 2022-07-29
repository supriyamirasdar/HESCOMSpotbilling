//Nitish 11-04-2014
package in.nsoft.hescomspotbilling;

public class ReportGPRS {
	private String mMRName;	
	private int mTotalInstallations;	
	private int mBilled;
	private int mNotBilled;	
	private int mGPRS_Sent;
	
	public String getmMRName() {
		return mMRName;
	}
	public void setmMRName(String mMRName) {
		this.mMRName = mMRName;
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
	public int getmGPRS_Sent() {
		return mGPRS_Sent;
	}
	public void setmGPRS_Sent(int mGPRS_Sent) {
		this.mGPRS_Sent = mGPRS_Sent;
	}
}