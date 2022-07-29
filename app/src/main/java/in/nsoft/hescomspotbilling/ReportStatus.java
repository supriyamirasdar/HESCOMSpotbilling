package in.nsoft.hescomspotbilling;
//Created Nitish 06-03-2014
public class ReportStatus {
	
	private String mMRName;
	private String mBillDate;	
	private int mTotalInstallations;	
	private int mBilled;
	private int mNotBilled;	
	private int mNormal; //00
	private int mDL; //01
	private int mMNR; //02
	private int mDO;//03
	private int mMCH;//04
	private int mRNF;//05
	private int mVA;//06
	private int mDIS;//08
	private int mMS;//09
	private int mDIR;//10
	private int mMB;//14
	private int mNT;//15		
	
	public int getmNormal() {
		return mNormal;
	}
	public void setmNormal(int mNormal) {
		this.mNormal = mNormal;
	}
	public int getmDL() {
		return mDL;
	}
	public void setmDL(int mDL) {
		this.mDL = mDL;
	}
	public int getmMNR() {
		return mMNR;
	}
	public void setmMNR(int mMNR) {
		this.mMNR = mMNR;
	}
	public int getmDO() {
		return mDO;
	}
	public void setmDO(int mDO) {
		this.mDO = mDO;
	}
	public int getmMCH() {
		return mMCH;
	}
	public void setmMCH(int mMCH) {
		this.mMCH = mMCH;
	}
	public int getmRNF() {
		return mRNF;
	}
	public void setmRNF(int mRNF) {
		this.mRNF = mRNF;
	}
	public int getmVA() {
		return mVA;
	}
	public void setmVA(int mVA) {
		this.mVA = mVA;
	}
	public int getmDIS() {
		return mDIS;
	}
	public void setmDIS(int mDIS) {
		this.mDIS = mDIS;
	}
	public int getmMS() {
		return mMS;
	}
	public void setmMS(int mMS) {
		this.mMS = mMS;
	}
	public int getmDIR() {
		return mDIR;
	}
	public void setmDIR(int mDIR) {
		this.mDIR = mDIR;
	}
	public int getmMB() {
		return mMB;
	}
	public void setmMB(int mMB) {
		this.mMB = mMB;
	}
	public int getmNT() {
		return mNT;
	}
	public void setmNT(int mNT) {
		this.mNT = mNT;
	}
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
