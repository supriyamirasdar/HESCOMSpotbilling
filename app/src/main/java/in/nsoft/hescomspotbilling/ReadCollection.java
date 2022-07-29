package in.nsoft.hescomspotbilling;
//Created Nitish 15-04-2014
import java.math.BigDecimal;

public class ReadCollection 
{	
	private int mmUID ;	
	private String mConnectionNo;	
	private String mDueDate;	
	private int mTariffCode;
	private int mStatus;//Current Status	
	private String mSancLoad;	
	private BigDecimal mArrearsOld;//new
	private BigDecimal mIntrstOld;//new
	private BigDecimal mArears;	
	private String mBlCnt;
	private String mRRNo;	
	private String mCustomerName;	
	private String mGvpId;	
	private String mLocationCode;
	private BigDecimal mBillTotal;
	private String mSBMNumber;
	private int mRcptCnt;
	private String mBatch_No;
	private int mReceipt_No;
	private String mDateTime;
	private String mPayment_Mode;
	private int mPaid_Amt;
	private int mChequeDDNo;
	private String mChequeDDDate;
	private String mReceipttypeflag;	
	private int mBankID;	
	private int mGprs_Flag;	
	private String mTarifString;
	private int mArrearsBill_Flag;	
	private String mReaderCode;	
	
	private boolean mIsTimeSync;
	
	//Nitish 22-05-2014
	private BigDecimal mSancHp;
	private BigDecimal mSancKw;
	
	private int mBOBillFlag;
	private String mBOBilled_Amount;
	
	//Nitish 19-08-2014
	private String mIODRemarks;
	
	private String mImageName;
	private String mLat;
	private String mLong;
	
	private int mRemarkId;
	private String mScheduledDate;
	private String mCreatedDate;
	private int mReprintCount;//08-06-2017
	
	
	public String getmIODRemarks() {
		return mIODRemarks;
	}
	public void setmIODRemarks(String mIODRemarks) {
		this.mIODRemarks = mIODRemarks;
	}
	/**
	 * @return the mmUID
	 */
	public int getMmUID() {
		return mmUID;
	}
	/**
	 * @param mmUID the mmUID to set
	 */
	public void setMmUID(int mmUID) {
		this.mmUID = mmUID;
	}
	
	/**
	 * @return the mConnectionNo
	 */
	public String getmConnectionNo() {
		return mConnectionNo;
	}
	/**
	 * @param mConnectionNo the mConnectionNo to set
	 */
	public void setmConnectionNo(String mConnectionNo) {
		this.mConnectionNo = mConnectionNo;
	}
	
	/**
	 * @return the mDueDate
	 */
	public String getmDueDate() {
		return mDueDate;
	}
	/**
	 * @param mDueDate the mDueDate to set
	 */
	public void setmDueDate(String mDueDate) {
		this.mDueDate = mDueDate;
	}
	
	/**
	 * @return the mTariffCode
	 */
	public int getmTariffCode() {
		return mTariffCode;
	}
	/**
	 * @param mTariffCode the mTariffCode to set
	 */
	public void setmTariffCode(int mTariffCode) {
		this.mTariffCode = mTariffCode;
	}
	
	/**
	 * @return the mStatus
	 */
	public int getmStatus() {
		return mStatus;
	}
	/**
	 * @param mStatus the mStatus to set
	 */
	public void setmStatus(int mStatus) {
		this.mStatus = mStatus;
	}	
	/**
	 * @return the mSancLoad
	 */
	public String getmSancLoad() {
		return mSancLoad;
	}
	/**
	 * @param mSancLoad the mSancLoad to set
	 */
	public void setmSancLoad(String mSancLoad) {
		this.mSancLoad = mSancLoad;
	}
	
	public BigDecimal getmArrearsOld() {
		return mArrearsOld;
	}
	public void setmArrearsOld(BigDecimal mArrearsOld) {
		this.mArrearsOld = mArrearsOld;
	}
	
	public BigDecimal getmIntrstOld() {
		return mIntrstOld;
	}
	public void setmIntrstOld(BigDecimal mIntrstOld) {
		this.mIntrstOld = mIntrstOld;
	}
	
	/**
	 * @return the mArears
	 */
	public BigDecimal getmArears() {
		return mArears;
	}
	/**
	 * @param mArears the mArears to set
	 */
	public void setmArears(BigDecimal mArears) {
		this.mArears = mArears;
	}
	
	/**
	 * @return the mBlCnt
	 */
	public String getmBlCnt() {
		return mBlCnt;
	}
	/**
	 * @param mBlCnt the mBlCnt to set
	 */
	public void setmBlCnt(String mBlCnt) {
		this.mBlCnt = mBlCnt;
	}
	/**
	 * @return the mRRNo
	 */
	public String getmRRNo() {
		return mRRNo;
	}
	/**
	 * @param mRRNo the mRRNo to set
	 */
	public void setmRRNo(String mRRNo) {
		this.mRRNo = mRRNo;
	}
	/**
	 * @return the mCustomerName
	 */
	
	public String getmCustomerName() {
		return mCustomerName;
	}
	/**
	 * @param mCustomerName the mRRNo to set
	 */
	public void setmCustomerName(String mCustomerName) {
		this.mCustomerName = mCustomerName;
	}
	
	/**
	 * @return the mGvpId
	 */
	public String getmGvpId() {
		return mGvpId;
	}
	/**
	 * @param mGvpId the mGvpId to set
	 */
	public void setmGvpId(String mGvpId) {
		this.mGvpId = mGvpId;
	}	
	/**
	 * @return the mLocationCode
	 */
	public String getmLocationCode() {
		return mLocationCode;
	}
	/**
	 * @param mLocationCode the mLocationCode to set
	 */
	public void setmLocationCode(String mLocationCode) {
		this.mLocationCode = mLocationCode;
	}
	
	
	/**
	 * @return the mBillTotal
	 */
	public BigDecimal getmBillTotal() {
		return mBillTotal;
	}
	/**
	 * @param mBillTotal the mBillTotal to set
	 */
	public void setmBillTotal(BigDecimal mBillTotal) {
		this.mBillTotal = mBillTotal;
	}
	/**
	 * @return the mSBMNumber
	 */
	public String getmSBMNumber() {
		return mSBMNumber;
	}
	/**
	 * @param mSBMNumber the mSBMNumber to set
	 */
	public void setmSBMNumber(String mSBMNumber) {
		this.mSBMNumber = mSBMNumber;
	}
	/**
	 * @return the mRcptCnt
	 */
	public int getmRcptCnt() {
		return mRcptCnt;
	}
	/**
	 * @param mRcptCnt the mRcptCnt to set
	 */
	public void setmRcptCnt(int mRcptCnt) {
		this.mRcptCnt = mRcptCnt;
	}
	/**
	 * @return the mBatch_No
	 */
	public String getmBatch_No() {
		return mBatch_No;
	}
	/**
	 * @param mBatch_No the mBatch_No to set
	 */
	public void setmBatch_No(String mBatch_No) {
		this.mBatch_No = mBatch_No;
	}
	/**
	 * @return the mReceipt_No
	 */
	public int getmReceipt_No() {
		return mReceipt_No;
	}
	/**
	 * @param mReceipt_No the mReceipt_No to set
	 */
	public void setmReceipt_No(int mReceipt_No) {
		this.mReceipt_No = mReceipt_No;
	}
	/**
	 * @return the mDateTime
	 */
	public String getmDateTime() {
		return mDateTime;
	}
	/**
	 * @param mDateTime the mDateTime to set
	 */
	public void setmDateTime(String mDateTime) {
		this.mDateTime = mDateTime;
	}
	/**
	 * @return the mPayment_Mode
	 */
	public String getmPayment_Mode() {
		return mPayment_Mode;
	}
	/**
	 * @param mPayment_Mode the mPayment_Mode to set
	 */
	public void setmPayment_Mode(String mPayment_Mode) {
		this.mPayment_Mode = mPayment_Mode;
	}
	/**
	 * @return the mPaid_Amt
	 */
	public int getmPaid_Amt() {
		return mPaid_Amt;
	}
	/**
	 * @param mPaid_Amt the mPaid_Amt to set
	 */
	public void setmPaid_Amt(int mPaid_Amt) {
		this.mPaid_Amt = mPaid_Amt;
	}
	/**
	 * @return the mChequeDDNo
	 */
	public int getmChequeDDNo() {
		return mChequeDDNo;
	}
	/**
	 * @param mChequeDDNo the mChequeDDNo to set
	 */
	public void setmChequeDDNo(int mChequeDDNo) {
		this.mChequeDDNo = mChequeDDNo;
	}
	/**
	 * @return the mChequeDDDate
	 */
	public String getmChequeDDDate() {
		return mChequeDDDate;
	}
	/**
	 * @param mChequeDDDate the mChequeDDDate to set
	 */
	public void setmChequeDDDate(String mChequeDDDate) {
		this.mChequeDDDate = mChequeDDDate;
	}
	/**
	 * @return the mReceipttypeflag
	 */
	public String getmReceipttypeflag() {
		return mReceipttypeflag;
	}
	/**
	 * @param mReceipttypeflag the mReceipttypeflag to set
	 */
	public void setmReceipttypeflag(String mReceipttypeflag) {
		this.mReceipttypeflag = mReceipttypeflag;
	}
	/**
	 * @return the mApplicationNo
	 */
	
	/**
	 * @return the mBankID
	 */
	public int getmBankID() {
		return mBankID;
	}
	/**
	 * @param mBankID the mBankID to set
	 */
	public void setmBankID(int mBankID) {
		this.mBankID = mBankID;
	}
	
	/**
	 * @return the mGprs_Flag
	 */
	public int getmGprs_Flag() {
		return mGprs_Flag;
	}
	/**
	 * @param mGprs_Flag the mGprs_Flag to set
	 */
	public void setmGprs_Flag(int mGprs_Flag) {
		this.mGprs_Flag = mGprs_Flag;
	}
	/**
	 * @return the mTarifString
	 */
	public String getmTarifString() {
		return mTarifString;
	}
	
	/**
	 * @param mTarifString the mTarifString to set
	 */
	public void setmTarifString(String mTarifString) {
		this.mTarifString = mTarifString;
	}
	
	/**
	 * @return the mArrearsBill_Flag
	 */
	public int getmArrearsBill_Flag() {
		return mArrearsBill_Flag;
	}
	
	/**
	 * @param mTarifString the mArrearsBill_Flag to set
	 */
	public void setmArrearsBill_Flag(int mArrearsBill_Flag) {
		this.mArrearsBill_Flag = mArrearsBill_Flag;
	}
	

	/**
	 * @return the mReaderCode
	 */
	public String getmReaderCode() {
		return mReaderCode;
	}
	
	/**
	 * @param mReaderCode the mReaderCode to set
	 */
	public void setmReaderCode(String mReaderCode) {
		this.mReaderCode = mReaderCode;
	}
	/**
	 * @return the mIsTimeSync
	 */
	public boolean ismIsTimeSync() {
		return mIsTimeSync;
	}
	/**
	 * @param mTarifString the mIsTimeSync to set
	 */
	public void setmIsTimeSync(boolean mIsTimeSync) {
		this.mIsTimeSync = mIsTimeSync;
	}
	
	/**
	 * @return the mSancHp
	 */
	public BigDecimal getmSancHp() {
		return mSancHp;
	}
	/**
	 * @param mSancHp the mSancHp to set
	 */
	public void setmSancHp(BigDecimal mSancHp) {
		this.mSancHp = mSancHp;
	}
	/**
	 * @return the mSancKw
	 */
	public BigDecimal getmSancKw() {
		return mSancKw;
	}
	/**
	 * @param mSancKw the mSancKw to set
	 */
	public void setmSancKw(BigDecimal mSancKw) {
		this.mSancKw = mSancKw;
	}
	
	public int getmBOBillFlag() {
		return mBOBillFlag;
	}
	public void setmBOBillFlag(int mBOBillFlag) {
		this.mBOBillFlag = mBOBillFlag;
	}
	
	public String getmBOBilled_Amount() {
		return mBOBilled_Amount;
	}
	public void setmBOBilled_Amount(String mBOBilled_Amount) {
		this.mBOBilled_Amount = mBOBilled_Amount;
	}
	public String getmImageName() {
		return mImageName;
	}
	public void setmImageName(String mImageName) {
		this.mImageName = mImageName;
	}
	public String getmLat() {
		return mLat;
	}
	public void setmLat(String mLat) {
		this.mLat = mLat;
	}
	public String getmLong() {
		return mLong;
	}
	public void setmLong(String mLong) {
		this.mLong = mLong;
	}
	public int getmRemarkId() {
		return mRemarkId;
	}
	public void setmRemarkId(int mRemarkId) {
		this.mRemarkId = mRemarkId;
	}
	public String getmScheduledDate() {
		return mScheduledDate;
	}
	public void setmScheduledDate(String mScheduledDate) {
		this.mScheduledDate = mScheduledDate;
	}
	public String getmCreatedDate() {
		return mCreatedDate;
	}
	public void setmCreatedDate(String mCreatedDate) {
		this.mCreatedDate = mCreatedDate;
	}
	public int getmReprintCount() {
		return mReprintCount;
	}
	public void setmReprintCount(int mReprintCount) {
		this.mReprintCount = mReprintCount;
	}
	
	
	
}




