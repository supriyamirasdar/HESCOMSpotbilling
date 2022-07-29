package in.nsoft.hescomspotbilling;

import java.math.BigDecimal;

public class ReadSlabNTarifSbmBillCollection 
{


	//SBM_BillCollDataMain

	private int mmUID ; 
	private String mForMonth;
	private String mBillDate;
	private String mSubId;
	private String mConnectionNo;
	private String mCustomerName;
	private String mTourplanId;
	private String mBillNo;
	private String mDueDate;
	private BigDecimal mFixedCharges;
	private String mRebateFlag;
	private String mReaderCode;
	private int mTariffCode;//#########
	private String mReadingDay;
	private String mPF;
	private BigDecimal mMF;
	private int mStatus;//Current Status
	private String mAvgCons;
	private BigDecimal mLinMin;
	private BigDecimal mSancHp;
	private BigDecimal mSancKw;
	private String mSancLoad;
	private String mPrevRdg;
	private int mDlCount;
	private BigDecimal mArears;
	private String mIntrstCurnt;//in db REAL
	private String mDrFees;
	private String mOthers;
	private String mBillFor;
	private String mBlCnt;//############
	private String mRRNo;//#######
	private String mLegFol;                           
	private String mTvmMtr;
	private String mTaxFlag;
	private BigDecimal mArrearsOld;
	private BigDecimal mIntrst_Unpaid;
	private BigDecimal mIntrstOld;
	private String mBillable;
	private String mNewNoofDays;
	private String mNoOfDays;
	private String mHWCReb;
	private String mDLAvgMin;
	private String mTvmPFtype;
	private String mAccdRdg;
	private String mKVAIR;
	private BigDecimal mDLTEc;
	private String mRRFlag;
	private String mMtd;
	private BigDecimal mSlowRtnPge;
	private String mOtherChargeLegend;
	private BigDecimal mGoKArrears;
	private String mDPdate;
	private BigDecimal mReceiptAmnt;
	private String mReceiptDate;
	private String mTcName;
	private String mThirtyFlag;
	private String mIODRemarks;
	private String mDayWise_Flag;
	private String mOld_Consumption;
	private BigDecimal mKVAAssd_Cons;//FEC
	private String mGvpId;
	private String mBOBilled_Amount;
	private BigDecimal mKVAH_OldConsumption;
	private String mEcsFlag;
	private int mSupply_Points;
	private String mIODD11_Remarks;
	private String mLocationCode;
	private int mBOBillFlag;
	private String mAddress1;
	private String mAddress2;
	private String mSectionName;
	private String mOldConnID;//#############3
	private String mMCHFlag;
	private BigDecimal mFC_Slab_2; //
	private String mMobileNo;
	private String mPreRead;
	private String mPreStatus;//Previous Status
	private int mSpotSerialNo;
	private String mUnits;
	private BigDecimal mTFc;
	private BigDecimal mTEc;
	private BigDecimal mFLReb;
	private BigDecimal mECReb;
	private BigDecimal mTaxAmt;
	private BigDecimal mPfPenAmt;
	private BigDecimal mPenExLd;
	private BigDecimal mHCReb;
	private BigDecimal mHLReb;
	private BigDecimal mCapReb;
	private BigDecimal mExLoad;
	private BigDecimal mDemandChrg;
	private String mAccdRdg_rtn;
	private String mKVAFR;
	private String mAbFlag;
	private String mBjKj2Lt2;
	private String mRemarks;
	private BigDecimal mGoKPayable;
	private String mIssueDateTime;
	private BigDecimal mRecordDmnd;
	private BigDecimal mKVA_Consumption;
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
	private String mApplicationNo;
	private int mChargetypeID;
	private int mBankID;
	private String mLatitude;
	private String mLatitude_Dir;
	private String mLongitude;
	private String mLongitude_Dir;
	private int mGprs_Flag;
	private String mConsPayable;
	private int mMtrDisFlag;
	private String mMeter_type;
	private String mMeter_serialno;
	private String mGps_Latitude_image;
	private String mGps_LatitudeCardinal_image;
	private String mGps_Longitude_image;
	private String mGps_LongitudeCardinal_image;
	private String mGps_Latitude_print;
	private String mGps_LatitudeCardinal_print;
	private String mGps_Longitude_print;
	private String mGps_LongitudeCardinal_print;
	private String mImage_Name;
	private String mImage_Path;
	private String mImage_Cap_Date;
	private String mImage_Cap_Time;
	private BigDecimal mTax_Ec;//30-06-2014
	private int mMeter_Present_Flag;//Added on 30-08-2014
	private int mMtr_Not_Visible;//Added on 30-08-2014

	private BigDecimal mDLTEc_GoK;//Added on 31-08-2014

	private BigDecimal mOldTecBill;//Nitish 21-04-2016

	//Nitish 25-07-2014
	private int mReasonId;

	//punit 25022014
	//	TABLE_EC_FC_Slab
	//###########################################################################################
	private int mUID;
	private String mmConnectionNo; 
	private String mmRRNo; 
	private int mmECRate_Count;
	private int mmECRate_Row; 
	private BigDecimal mmFCRate_1; //
	private BigDecimal mmCOL_FCRate_2;
	private BigDecimal mmUnits_1;
	private BigDecimal mmUnits_2;
	private BigDecimal mmUnits_3;
	private BigDecimal mmUnits_4;
	private BigDecimal mmUnits_5;
	private BigDecimal mmUnits_6;
	private BigDecimal mmEC_Rate_1;
	private BigDecimal mmEC_Rate_2;
	private BigDecimal mmEC_Rate_3;
	private BigDecimal mmEC_Rate_4;
	private BigDecimal mmEC_Rate_5;
	private BigDecimal mmEC_Rate_6;
	private BigDecimal mmEC_1;
	private BigDecimal mmEC_2;
	private BigDecimal mmEC_3;
	private BigDecimal mmEC_4;
	private BigDecimal mmEC_5;
	private BigDecimal mmEC_6;
	private BigDecimal mmFC_1;
	private BigDecimal mmFC_2;
	private BigDecimal mmTEc;
	private BigDecimal mmEC_FL_1;
	private BigDecimal mmEC_FL_2;
	private BigDecimal mmEC_FL_3;
	private BigDecimal mmEC_FL_4;
	private BigDecimal mmEC_FL;
	private BigDecimal mmnew_TEc;
	private BigDecimal mmold_TEc;
	//punit end

	private int mUIDR_Slab; 
	//private int mTarifCode; 


	//30-07-2015
	private String mAadharNo;
	private String mVoterIdNo; 
	private int mMtrMakeFlag;
	private int mMtrBodySealFlag;  
	private int mMtrTerminalCoverFlag;  
	private int mMtrTerminalCoverSealedFlag;  



	//End 30-07-2015

	//23-06-2021 for Extra FC
	private BigDecimal mmNEWFC_UNIT1;
	private BigDecimal mmNEWFC_UNIT2;
	private BigDecimal mmNEWFC_UNIT3;
	private BigDecimal mmNEWFC_UNIT4;
	private BigDecimal mmNEWFC_Rate3;
	private BigDecimal mmNEWFC_Rate4;
	private BigDecimal mmNEWFC3;
	private BigDecimal mmNEWFC4;
	
	private BigDecimal mFC_Slab_3;
	
	public BigDecimal getMmNEWFC_UNIT1() {
		return mmNEWFC_UNIT1;
	}
	public void setMmNEWFC_UNIT1(BigDecimal mmNEWFC_UNIT1) {
		this.mmNEWFC_UNIT1 = mmNEWFC_UNIT1;
	}
	public BigDecimal getMmNEWFC_UNIT2() {
		return mmNEWFC_UNIT2;
	}
	public void setMmNEWFC_UNIT2(BigDecimal mmNEWFC_UNIT2) {
		this.mmNEWFC_UNIT2 = mmNEWFC_UNIT2;
	}
	public BigDecimal getMmNEWFC_UNIT3() {
		return mmNEWFC_UNIT3;
	}
	public void setMmNEWFC_UNIT3(BigDecimal mmNEWFC_UNIT3) {
		this.mmNEWFC_UNIT3 = mmNEWFC_UNIT3;
	}
	public BigDecimal getMmNEWFC_UNIT4() {
		return mmNEWFC_UNIT4;
	}
	public void setMmNEWFC_UNIT4(BigDecimal mmNEWFC_UNIT4) {
		this.mmNEWFC_UNIT4 = mmNEWFC_UNIT4;
	}
	public BigDecimal getMmNEWFC_Rate3() {
		return mmNEWFC_Rate3;
	}
	public void setMmNEWFC_Rate3(BigDecimal mmNEWFC_Rate3) {
		this.mmNEWFC_Rate3 = mmNEWFC_Rate3;
	}
	public BigDecimal getMmNEWFC_Rate4() {
		return mmNEWFC_Rate4;
	}
	public void setMmNEWFC_Rate4(BigDecimal mmNEWFC_Rate4) {
		this.mmNEWFC_Rate4 = mmNEWFC_Rate4;
	}
	public BigDecimal getMmNEWFC3() {
		return mmNEWFC3;
	}
	public void setMmNEWFC3(BigDecimal mmNEWFC3) {
		this.mmNEWFC3 = mmNEWFC3;
	}
	public BigDecimal getMmNEWFC4() {
		return mmNEWFC4;
	}
	public void setMmNEWFC4(BigDecimal mmNEWFC4) {
		this.mmNEWFC4 = mmNEWFC4;
	}
	



	private String mTarifString;
	private String mFACValue; //Nitish 19-12-2015 
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
	 * @return the mForMonth
	 */
	public String getmForMonth() {
		return mForMonth;
	}
	/**
	 * @param mForMonth the mForMonth to set
	 */
	public void setmForMonth(String mForMonth) {
		this.mForMonth = mForMonth;
	}
	/**
	 * @return the mBillDate
	 */
	public String getmBillDate() {
		return mBillDate;
	}
	/**
	 * @param mBillDate the mBillDate to set
	 */
	public void setmBillDate(String mBillDate) {
		this.mBillDate = mBillDate;
	}
	/**
	 * @return the mSubId
	 */
	public String getmSubId() {
		return mSubId;
	}
	/**
	 * @param mSubId the mSubId to set
	 */
	public void setmSubId(String mSubId) {
		this.mSubId = mSubId;
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
	 * @return the mCustomerName
	 */
	public String getmCustomerName() {
		return mCustomerName;
	}
	/**
	 * @param mCustomerName the mCustomerName to set
	 */
	public void setmCustomerName(String mCustomerName) {
		this.mCustomerName = mCustomerName;
	}
	/**
	 * @return the mTourplanId
	 */
	public String getmTourplanId() {
		return mTourplanId;
	}
	/**
	 * @param mTourplanId the mTourplanId to set
	 */
	public void setmTourplanId(String mTourplanId) {
		this.mTourplanId = mTourplanId;
	}
	/**
	 * @return the mBillNo
	 */
	public String getmBillNo() {
		return mBillNo;
	}
	/**
	 * @param mBillNo the mBillNo to set
	 */
	public void setmBillNo(String mBillNo) {
		this.mBillNo = mBillNo;
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
	 * @return the mFixedCharges
	 */
	public BigDecimal getmFixedCharges() {
		return mFixedCharges;
	}
	/**
	 * @param mFixedCharges the mFixedCharges to set
	 */
	public void setmFixedCharges(BigDecimal mFixedCharges) {
		this.mFixedCharges = mFixedCharges;
	}
	/**
	 * @return the mRebateFlag
	 */
	public String getmRebateFlag() {
		return mRebateFlag;
	}
	/**
	 * @param mRebateFlag the mRebateFlag to set
	 */
	public void setmRebateFlag(String mRebateFlag) {
		this.mRebateFlag = mRebateFlag;
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
	 * @return the mReadingDay
	 */
	public String getmReadingDay() {
		return mReadingDay;
	}
	/**
	 * @param mReadingDay the mReadingDay to set
	 */
	public void setmReadingDay(String mReadingDay) {
		this.mReadingDay = mReadingDay;
	}
	/**
	 * @return the mPF
	 */
	public String getmPF() {
		return mPF;
	}
	/**
	 * @param mPF the mPF to set
	 */
	public void setmPF(String mPF) {
		this.mPF = mPF;
	}
	/**
	 * @return the mMF
	 */
	public BigDecimal getmMF() {
		return mMF;
	}
	/**
	 * @param mMF the mMF to set
	 */
	public void setmMF(BigDecimal mMF) {
		this.mMF = mMF;
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
	 * @return the mAvgCons
	 */
	public String getmAvgCons() {
		return mAvgCons;
	}
	/**
	 * @param mAvgCons the mAvgCons to set
	 */
	public void setmAvgCons(String mAvgCons) {
		this.mAvgCons = mAvgCons;
	}
	/**
	 * @return the mLinMin
	 */
	public BigDecimal getmLinMin() {
		return mLinMin;
	}
	/**
	 * @param mLinMin the mLinMin to set
	 */
	public void setmLinMin(BigDecimal mLinMin) {
		this.mLinMin = mLinMin;
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
	/**
	 * @return the mPrevRdg
	 */
	public String getmPrevRdg() {
		return mPrevRdg;
	}
	/**
	 * @param mPrevRdg the mPrevRdg to set
	 */
	public void setmPrevRdg(String mPrevRdg) {
		this.mPrevRdg = mPrevRdg;
	}
	/**
	 * @return the mDlCount
	 */
	public int getmDlCount() {
		return mDlCount;
	}
	/**
	 * @param mDlCount the mDlCount to set
	 */
	public void setmDlCount(int mDlCount) {
		this.mDlCount = mDlCount;
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
	 * @return the mIntrstCurnt
	 */
	public String getmIntrstCurnt() {
		return mIntrstCurnt;
	}
	/**
	 * @param mIntrstCurnt the mIntrstCurnt to set
	 */
	public void setmIntrstCurnt(String mIntrstCurnt) {
		this.mIntrstCurnt = mIntrstCurnt;
	}
	/**
	 * @return the mDrFees
	 */
	public String getmDrFees() {
		return mDrFees;
	}
	/**
	 * @param mDrFees the mDrFees to set
	 */
	public void setmDrFees(String mDrFees) {
		this.mDrFees = mDrFees;
	}
	/**
	 * @return the mOthers
	 */
	public String getmOthers() {
		return mOthers;
	}
	/**
	 * @param mOthers the mOthers to set
	 */
	public void setmOthers(String mOthers) {
		this.mOthers = mOthers;
	}
	/**
	 * @return the mBillFor
	 */
	public String getmBillFor() {
		return mBillFor;
	}
	/**
	 * @param mBillFor the mBillFor to set
	 */
	public void setmBillFor(String mBillFor) {
		this.mBillFor = mBillFor;
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
	 * @return the mLegFol
	 */
	public String getmLegFol() {
		return mLegFol;
	}
	/**
	 * @param mLegFol the mLegFol to set
	 */
	public void setmLegFol(String mLegFol) {
		this.mLegFol = mLegFol;
	}
	/**
	 * @return the mTvmMtr
	 */
	public String getmTvmMtr() {
		return mTvmMtr;
	}
	/**
	 * @param mTvmMtr the mTvmMtr to set
	 */
	public void setmTvmMtr(String mTvmMtr) {
		this.mTvmMtr = mTvmMtr;
	}
	/**
	 * @return the mTaxFlag
	 */
	public String getmTaxFlag() {
		return mTaxFlag;
	}
	/**
	 * @param mTaxFlag the mTaxFlag to set
	 */
	public void setmTaxFlag(String mTaxFlag) {
		this.mTaxFlag = mTaxFlag;
	}
	/**
	 * @return the mArrearsOld
	 */
	public BigDecimal getmArrearsOld() {
		return mArrearsOld;
	}
	/**
	 * @param mArrearsOld the mArrearsOld to set
	 */
	public void setmArrearsOld(BigDecimal mArrearsOld) {
		this.mArrearsOld = mArrearsOld;
	}
	/**
	 * @return the mIntrst_Unpaid
	 */
	public BigDecimal getmIntrst_Unpaid() {
		return mIntrst_Unpaid;
	}
	/**
	 * @param mIntrst_Unpaid the mIntrst_Unpaid to set
	 */
	public void setmIntrst_Unpaid(BigDecimal mIntrst_Unpaid) {
		this.mIntrst_Unpaid = mIntrst_Unpaid;
	}
	/**
	 * @return the mIntrstOld
	 */
	public BigDecimal getmIntrstOld() {
		return mIntrstOld;
	}
	/**
	 * @param mIntrstOld the mIntrstOld to set
	 */
	public void setmIntrstOld(BigDecimal mIntrstOld) {
		this.mIntrstOld = mIntrstOld;
	}
	/**
	 * @return the mBillable
	 */
	public String getmBillable() {
		return mBillable;
	}
	/**
	 * @param mBillable the mBillable to set
	 */
	public void setmBillable(String mBillable) {
		this.mBillable = mBillable;
	}
	/**
	 * @return the mNewNoofDays
	 */
	public String getmNewNoofDays() {
		return mNewNoofDays;
	}
	/**
	 * @param mNewNoofDays the mNewNoofDays to set
	 */
	public void setmNewNoofDays(String mNewNoofDays) {
		this.mNewNoofDays = mNewNoofDays;
	}
	/**
	 * @return the mNoOfDays
	 */
	public String getmNoOfDays() {
		return mNoOfDays;
	}
	/**
	 * @param mNoOfDays the mNoOfDays to set
	 */
	public void setmNoOfDays(String mNoOfDays) {
		this.mNoOfDays = mNoOfDays;
	}
	/**
	 * @return the mHWCReb
	 */
	public String getmHWCReb() {
		return mHWCReb;
	}
	/**
	 * @param mHWCReb the mHWCReb to set
	 */
	public void setmHWCReb(String mHWCReb) {
		this.mHWCReb = mHWCReb;
	}
	/**
	 * @return the mDLAvgMin
	 */
	public String getmDLAvgMin() {
		return mDLAvgMin;
	}
	/**
	 * @param mDLAvgMin the mDLAvgMin to set
	 */
	public void setmDLAvgMin(String mDLAvgMin) {
		this.mDLAvgMin = mDLAvgMin;
	}
	/**
	 * @return the mTvmPFtype
	 */
	public String getmTvmPFtype() {
		return mTvmPFtype;
	}
	/**
	 * @param mTvmPFtype the mTvmPFtype to set
	 */
	public void setmTvmPFtype(String mTvmPFtype) {
		this.mTvmPFtype = mTvmPFtype;
	}
	/**
	 * @return the mAccdRdg
	 */
	public String getmAccdRdg() {
		return mAccdRdg;
	}
	/**
	 * @param mAccdRdg the mAccdRdg to set
	 */
	public void setmAccdRdg(String mAccdRdg) {
		this.mAccdRdg = mAccdRdg;
	}
	/**
	 * @return the mKVAIR
	 */
	public String getmKVAIR() {
		return mKVAIR;
	}
	/**
	 * @param mKVAIR the mKVAIR to set
	 */
	public void setmKVAIR(String mKVAIR) {
		this.mKVAIR = mKVAIR;
	}
	/**
	 * @return the mDLTEc
	 */
	public BigDecimal getmDLTEc() {
		return mDLTEc;
	}
	/**
	 * @param mDLTEc the mDLTEc to set
	 */
	public void setmDLTEc(BigDecimal mDLTEc) {
		this.mDLTEc = mDLTEc;
	}
	/**
	 * @return the mRRFlag
	 */
	public String getmRRFlag() {
		return mRRFlag;
	}
	/**
	 * @param mRRFlag the mRRFlag to set
	 */
	public void setmRRFlag(String mRRFlag) {
		this.mRRFlag = mRRFlag;
	}
	/**
	 * @return the mMtd
	 */
	public String getmMtd() {
		return mMtd;
	}
	/**
	 * @param mMtd the mMtd to set
	 */
	public void setmMtd(String mMtd) {
		this.mMtd = mMtd;
	}
	/**
	 * @return the mSlowRtnPge
	 */
	public BigDecimal getmSlowRtnPge() {
		return mSlowRtnPge;
	}
	/**
	 * @param mSlowRtnPge the mSlowRtnPge to set
	 */
	public void setmSlowRtnPge(BigDecimal mSlowRtnPge) {
		this.mSlowRtnPge = mSlowRtnPge;
	}
	/**
	 * @return the mOtherChargeLegend
	 */
	public String getmOtherChargeLegend() {
		return mOtherChargeLegend;
	}
	/**
	 * @param mOtherChargeLegend the mOtherChargeLegend to set
	 */
	public void setmOtherChargeLegend(String mOtherChargeLegend) {
		this.mOtherChargeLegend = mOtherChargeLegend;
	}
	/**
	 * @return the mGoKArrears
	 */
	public BigDecimal getmGoKArrears() {
		return mGoKArrears;
	}
	/**
	 * @param mGoKArrears the mGoKArrears to set
	 */
	public void setmGoKArrears(BigDecimal mGoKArrears) {
		this.mGoKArrears = mGoKArrears;
	}
	/**
	 * @return the mDPdate
	 */
	public String getmDPdate() {
		return mDPdate;
	}
	/**
	 * @param mDPdate the mDPdate to set
	 */
	public void setmDPdate(String mDPdate) {
		this.mDPdate = mDPdate;
	}
	/**
	 * @return the mReceiptAmnt
	 */
	public BigDecimal getmReceiptAmnt() {
		return mReceiptAmnt;
	}
	/**
	 * @param mReceiptAmnt the mReceiptAmnt to set
	 */
	public void setmReceiptAmnt(BigDecimal mReceiptAmnt) {
		this.mReceiptAmnt = mReceiptAmnt;
	}
	/**
	 * @return the mReceiptDate
	 */
	public String getmReceiptDate() {
		return mReceiptDate;
	}
	/**
	 * @param mReceiptDate the mReceiptDate to set
	 */
	public void setmReceiptDate(String mReceiptDate) {
		this.mReceiptDate = mReceiptDate;
	}
	/**
	 * @return the mTcName
	 */
	public String getmTcName() {
		return mTcName;
	}
	/**
	 * @param mTcName the mTcName to set
	 */
	public void setmTcName(String mTcName) {
		this.mTcName = mTcName;
	}
	/**
	 * @return the mThirtyFlag
	 */
	public String getmThirtyFlag() {
		return mThirtyFlag;
	}
	/**
	 * @param mThirtyFlag the mThirtyFlag to set
	 */
	public void setmThirtyFlag(String mThirtyFlag) {
		this.mThirtyFlag = mThirtyFlag;
	}
	/**
	 * @return the mIODRemarks
	 */
	public String getmIODRemarks() {
		return mIODRemarks;
	}
	/**
	 * @param mIODRemarks the mIODRemarks to set
	 */
	public void setmIODRemarks(String mIODRemarks) {
		this.mIODRemarks = mIODRemarks;
	}
	/**
	 * @return the mDayWise_Flag
	 */
	public String getmDayWise_Flag() {
		return mDayWise_Flag;
	}
	/**
	 * @param mDayWise_Flag the mDayWise_Flag to set
	 */
	public void setmDayWise_Flag(String mDayWise_Flag) {
		this.mDayWise_Flag = mDayWise_Flag;
	}
	/**
	 * @return the mOld_Consumption
	 */
	public String getmOld_Consumption() {
		return mOld_Consumption;
	}
	/**
	 * @param mOld_Consumption the mOld_Consumption to set
	 */
	public void setmOld_Consumption(String mOld_Consumption) {
		this.mOld_Consumption = mOld_Consumption;
	}
	/**
	 * @return the mKVAAssd_Cons
	 */
	public BigDecimal getmKVAAssd_Cons() {
		return mKVAAssd_Cons;
	}
	/**
	 * @param mKVAAssd_Cons the mKVAAssd_Cons to set
	 */
	public void setmKVAAssd_Cons(BigDecimal mKVAAssd_Cons) {
		this.mKVAAssd_Cons = mKVAAssd_Cons;
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
	 * @return the mBOBilled_Amount
	 */
	public String getmBOBilled_Amount() {
		return mBOBilled_Amount;
	}
	/**
	 * @param mBOBilled_Amount the mBOBilled_Amount to set
	 */
	public void setmBOBilled_Amount(String mBOBilled_Amount) {
		this.mBOBilled_Amount = mBOBilled_Amount;
	}
	/**
	 * @return the mKVAH_OldConsumption
	 */
	public BigDecimal getmKVAH_OldConsumption() {
		return mKVAH_OldConsumption;
	}
	/**
	 * @param mKVAH_OldConsumption the mKVAH_OldConsumption to set
	 */
	public void setmKVAH_OldConsumption(BigDecimal mKVAH_OldConsumption) {
		this.mKVAH_OldConsumption = mKVAH_OldConsumption;
	}
	/**
	 * @return the mEcsFlag
	 */
	public String getmEcsFlag() {
		return mEcsFlag;
	}
	/**
	 * @param mEcsFlag the mEcsFlag to set
	 */
	public void setmEcsFlag(String mEcsFlag) {
		this.mEcsFlag = mEcsFlag;
	}
	/**
	 * @return the mSupply_Points
	 */
	public int getmSupply_Points() {
		return mSupply_Points;
	}
	/**
	 * @param mSupply_Points the mSupply_Points to set
	 */
	public void setmSupply_Points(int mSupply_Points) {
		this.mSupply_Points = mSupply_Points;
	}
	/**
	 * @return the mIODD11_Remarks
	 */
	public String getmIODD11_Remarks() {
		return mIODD11_Remarks;
	}
	/**
	 * @param mIODD11_Remarks the mIODD11_Remarks to set
	 */
	public void setmIODD11_Remarks(String mIODD11_Remarks) {
		this.mIODD11_Remarks = mIODD11_Remarks;
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
	 * @return the mBOBillFlag
	 */
	public int getmBOBillFlag() {
		return mBOBillFlag;
	}
	/**
	 * @param mBOBillFlag the mBOBillFlag to set
	 */
	public void setmBOBillFlag(int mBOBillFlag) {
		this.mBOBillFlag = mBOBillFlag;
	}
	/**
	 * @return the mAddress1
	 */
	public String getmAddress1() {
		return mAddress1;
	}
	/**
	 * @param mAddress1 the mAddress1 to set
	 */
	public void setmAddress1(String mAddress1) {
		this.mAddress1 = mAddress1;
	}
	/**
	 * @return the mAddress2
	 */
	public String getmAddress2() {
		return mAddress2;
	}
	/**
	 * @param mAddress2 the mAddress2 to set
	 */
	public void setmAddress2(String mAddress2) {
		this.mAddress2 = mAddress2;
	}
	/**
	 * @return the mSectionName
	 */
	public String getmSectionName() {
		return mSectionName;
	}
	/**
	 * @param mSectionName the mSectionName to set
	 */
	public void setmSectionName(String mSectionName) {
		this.mSectionName = mSectionName;
	}
	/**
	 * @return the mOldConnID
	 */
	public String getmOldConnID() {
		return mOldConnID;
	}
	/**
	 * @param mOldConnID the mOldConnID to set
	 */
	public void setmOldConnID(String mOldConnID) {
		this.mOldConnID = mOldConnID;
	}
	/**
	 * @return the mMCHFlag
	 */
	public String getmMCHFlag() {
		return mMCHFlag;
	}
	/**
	 * @param mMCHFlag the mMCHFlag to set
	 */
	public void setmMCHFlag(String mMCHFlag) {
		this.mMCHFlag = mMCHFlag;
	}
	
	public BigDecimal getmFC_Slab_2() {
		return mFC_Slab_2;
	}
	
	public void setmFC_Slab_2(BigDecimal mFC_Slab_2) {
		this.mFC_Slab_2 = mFC_Slab_2;
	}
	/**
	 * @return the mMobileNo
	 */
	public String getmMobileNo() {
		return mMobileNo;
	}
	/**
	 * @param mMobileNo the mMobileNo to set
	 */
	public void setmMobileNo(String mMobileNo) {
		this.mMobileNo = mMobileNo;
	}
	/**
	 * @return the mPreRead
	 */
	public String getmPreRead() {
		return mPreRead;
	}
	/**
	 * @param mPreRead the mPreRead to set
	 */
	public void setmPreRead(String mPreRead) {
		this.mPreRead = mPreRead;
	}
	/**
	 * @return the mPreStatus
	 */
	public String getmPreStatus() {
		return mPreStatus;
	}
	/**
	 * @param mPreStatus the mPreStatus to set
	 */
	public void setmPreStatus(String mPreStatus) {
		this.mPreStatus = mPreStatus;
	}
	/**
	 * @return the mSpotSerialNo
	 */
	public int getmSpotSerialNo() {
		return mSpotSerialNo;
	}
	/**
	 * @param mSpotSerialNo the mSpotSerialNo to set
	 */
	public void setmSpotSerialNo(int mSpotSerialNo) {
		this.mSpotSerialNo = mSpotSerialNo;
	}
	/**
	 * @return the mUnits
	 */
	public String getmUnits() {
		return mUnits;
	}
	/**
	 * @param mUnits the mUnits to set
	 */
	public void setmUnits(String mUnits) {
		this.mUnits = mUnits;
	}
	/**
	 * @return the mTFc
	 */
	public BigDecimal getmTFc() {
		return mTFc;
	}
	/**
	 * @param mTFc the mTFc to set
	 */
	public void setmTFc(BigDecimal mTFc) {
		this.mTFc = mTFc;
	}
	/**
	 * @return the mTEc
	 */
	public BigDecimal getmTEc() {
		return mTEc;
	}
	/**
	 * @param mTEc the mTEc to set
	 */
	public void setmTEc(BigDecimal mTEc) {
		this.mTEc = mTEc;
	}
	/**
	 * @return the mFLReb
	 */
	public BigDecimal getmFLReb() {
		return mFLReb;
	}
	/**
	 * @param mFLReb the mFLReb to set
	 */
	public void setmFLReb(BigDecimal mFLReb) {
		this.mFLReb = mFLReb;
	}
	/**
	 * @return the mECReb
	 */
	public BigDecimal getmECReb() {
		return mECReb;
	}
	/**
	 * @param mECReb the mECReb to set
	 */
	public void setmECReb(BigDecimal mECReb) {
		this.mECReb = mECReb;
	}
	/**
	 * @return the mTaxAmt
	 */
	public BigDecimal getmTaxAmt() {
		return mTaxAmt;
	}
	/**
	 * @param mTaxAmt the mTaxAmt to set
	 */
	public void setmTaxAmt(BigDecimal mTaxAmt) {
		this.mTaxAmt = mTaxAmt;
	}
	/**
	 * @return the mPfPenAmt
	 */
	public BigDecimal getmPfPenAmt() {
		return mPfPenAmt;
	}
	/**
	 * @param mPfPenAmt the mPfPenAmt to set
	 */
	public void setmPfPenAmt(BigDecimal mPfPenAmt) {
		this.mPfPenAmt = mPfPenAmt;
	}
	/**
	 * @return the mPenExLd
	 */
	public BigDecimal getmPenExLd() {
		return mPenExLd;
	}
	/**
	 * @param mPenExLd the mPenExLd to set
	 */
	public void setmPenExLd(BigDecimal mPenExLd) {
		this.mPenExLd = mPenExLd;
	}
	/**
	 * @return the mHCReb
	 */
	public BigDecimal getmHCReb() {
		return mHCReb;
	}
	/**
	 * @param mHCReb the mHCReb to set
	 */
	public void setmHCReb(BigDecimal mHCReb) {
		this.mHCReb = mHCReb;
	}
	/**
	 * @return the mHLReb
	 */
	public BigDecimal getmHLReb() {
		return mHLReb;
	}
	/**
	 * @param mHLReb the mHLReb to set
	 */
	public void setmHLReb(BigDecimal mHLReb) {
		this.mHLReb = mHLReb;
	}
	/**
	 * @return the mCapReb
	 */
	public BigDecimal getmCapReb() {
		return mCapReb;
	}
	/**
	 * @param mCapReb the mCapReb to set
	 */
	public void setmCapReb(BigDecimal mCapReb) {
		this.mCapReb = mCapReb;
	}
	/**
	 * @return the mExLoad
	 */
	public BigDecimal getmExLoad() {
		return mExLoad;
	}
	/**
	 * @param mExLoad the mExLoad to set
	 */
	public void setmExLoad(BigDecimal mExLoad) {
		this.mExLoad = mExLoad;
	}
	/**
	 * @return the mDemandChrg
	 */
	public BigDecimal getmDemandChrg() {
		return mDemandChrg;
	}
	/**
	 * @param mDemandChrg the mDemandChrg to set
	 */
	public void setmDemandChrg(BigDecimal mDemandChrg) {
		this.mDemandChrg = mDemandChrg;
	}
	/**
	 * @return the mAccdRdg_rtn
	 */
	public String getmAccdRdg_rtn() {
		return mAccdRdg_rtn;
	}
	/**
	 * @param mAccdRdg_rtn the mAccdRdg_rtn to set
	 */
	public void setmAccdRdg_rtn(String mAccdRdg_rtn) {
		this.mAccdRdg_rtn = mAccdRdg_rtn;
	}
	/**
	 * @return the mKVAFR
	 */
	public String getmKVAFR() {
		return mKVAFR;
	}
	/**
	 * @param mKVAFR the mKVAFR to set
	 */
	public void setmKVAFR(String mKVAFR) {
		this.mKVAFR = mKVAFR;
	}
	/**
	 * @return the mAbFlag
	 */
	public String getmAbFlag() {
		return mAbFlag;
	}
	/**
	 * @param mAbFlag the mAbFlag to set
	 */
	public void setmAbFlag(String mAbFlag) {
		this.mAbFlag = mAbFlag;
	}
	/**
	 * @return the mBjKj2Lt2
	 */
	public String getmBjKj2Lt2() {
		return mBjKj2Lt2;
	}
	/**
	 * @param mBjKj2Lt2 the mBjKj2Lt2 to set
	 */
	public void setmBjKj2Lt2(String mBjKj2Lt2) {
		this.mBjKj2Lt2 = mBjKj2Lt2;
	}
	/**
	 * @return the mRemarks
	 */
	public String getmRemarks() {
		return mRemarks;
	}
	/**
	 * @param mRemarks the mRemarks to set
	 */
	public void setmRemarks(String mRemarks) {
		this.mRemarks = mRemarks;
	}
	/**
	 * @return the mGoKPayable
	 */
	public BigDecimal getmGoKPayable() {
		return mGoKPayable;
	}
	/**
	 * @param mGoKPayable the mGoKPayable to set
	 */
	public void setmGoKPayable(BigDecimal mGoKPayable) {
		this.mGoKPayable = mGoKPayable;
	}
	/**
	 * @return the mIssueDateTime
	 */
	public String getmIssueDateTime() {
		return mIssueDateTime;
	}
	/**
	 * @param mIssueDateTime the mIssueDateTime to set
	 */
	public void setmIssueDateTime(String mIssueDateTime) {
		this.mIssueDateTime = mIssueDateTime;
	}
	/**
	 * @return the mRecordDmnd
	 */
	public BigDecimal getmRecordDmnd() {
		return mRecordDmnd;
	}
	/**
	 * @param mRecordDmnd the mRecordDmnd to set
	 */
	public void setmRecordDmnd(BigDecimal mRecordDmnd) {
		this.mRecordDmnd = mRecordDmnd;
	}
	/**
	 * @return the mKVA_Consumption
	 */
	public BigDecimal getmKVA_Consumption() {
		return mKVA_Consumption;
	}
	/**
	 * @param mKVA_Consumption the mKVA_Consumption to set
	 */
	public void setmKVA_Consumption(BigDecimal mKVA_Consumption) {
		this.mKVA_Consumption = mKVA_Consumption;
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
	public String getmApplicationNo() {
		return mApplicationNo;
	}
	/**
	 * @param mApplicationNo the mApplicationNo to set
	 */
	public void setmApplicationNo(String mApplicationNo) {
		this.mApplicationNo = mApplicationNo;
	}
	/**
	 * @return the mChargetypeID
	 */
	public int getmChargetypeID() {
		return mChargetypeID;
	}
	/**
	 * @param mChargetypeID the mChargetypeID to set
	 */
	public void setmChargetypeID(int mChargetypeID) {
		this.mChargetypeID = mChargetypeID;
	}
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
	 * @return the mLatitude
	 */
	public String getmLatitude() {
		return mLatitude;
	}
	/**
	 * @param mLatitude the mLatitude to set
	 */
	public void setmLatitude(String mLatitude) {
		this.mLatitude = mLatitude;
	}
	/**
	 * @return the mLatitude_Dir
	 */
	public String getmLatitude_Dir() {
		return mLatitude_Dir;
	}
	/**
	 * @param mLatitude_Dir the mLatitude_Dir to set
	 */
	public void setmLatitude_Dir(String mLatitude_Dir) {
		this.mLatitude_Dir = mLatitude_Dir;
	}
	/**
	 * @return the mLongitude
	 */
	public String getmLongitude() {
		return mLongitude;
	}
	/**
	 * @param mLongitude the mLongitude to set
	 */
	public void setmLongitude(String mLongitude) {
		this.mLongitude = mLongitude;
	}
	/**
	 * @return the mLongitude_Dir
	 */
	public String getmLongitude_Dir() {
		return mLongitude_Dir;
	}
	/**
	 * @param mLongitude_Dir the mLongitude_Dir to set
	 */
	public void setmLongitude_Dir(String mLongitude_Dir) {
		this.mLongitude_Dir = mLongitude_Dir;
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
	 * @return the mConsPayable
	 */
	public String getmConsPayable() {
		return mConsPayable;
	}
	/**
	 * @param mConsPayable the mConsPayable to set
	 */
	public void setmConsPayable(String mConsPayable) {
		this.mConsPayable = mConsPayable;
	}
	/**
	 * @return the mMtrDisFlag
	 */
	public int getmMtrDisFlag() {
		return mMtrDisFlag;
	}
	/**
	 * @param mMtrDisFlag the mMtrDisFlag to set
	 */
	public void setmMtrDisFlag(int mMtrDisFlag) {
		this.mMtrDisFlag = mMtrDisFlag;
	}
	/**
	 * @return the mMeter_type
	 */
	public String getmMeter_type() {
		return mMeter_type;
	}
	/**
	 * @param mMeter_type the mMeter_type to set
	 */
	public void setmMeter_type(String mMeter_type) {
		this.mMeter_type = mMeter_type;
	}
	/**
	 * @return the mMeter_serialno
	 */
	public String getmMeter_serialno() {
		return mMeter_serialno;
	}
	/**
	 * @param mMeter_serialno the mMeter_serialno to set
	 */
	public void setmMeter_serialno(String mMeter_serialno) {
		this.mMeter_serialno = mMeter_serialno;
	}
	/**
	 * @return the mGps_Latitude_image
	 */
	public String getmGps_Latitude_image() {
		return mGps_Latitude_image;
	}
	/**
	 * @param mGps_Latitude_image the mGps_Latitude_image to set
	 */
	public void setmGps_Latitude_image(String mGps_Latitude_image) {
		this.mGps_Latitude_image = mGps_Latitude_image;
	}
	/**
	 * @return the mGps_LatitudeCardinal_image
	 */
	public String getmGps_LatitudeCardinal_image() {
		return mGps_LatitudeCardinal_image;
	}
	/**
	 * @param mGps_LatitudeCardinal_image the mGps_LatitudeCardinal_image to set
	 */
	public void setmGps_LatitudeCardinal_image(String mGps_LatitudeCardinal_image) {
		this.mGps_LatitudeCardinal_image = mGps_LatitudeCardinal_image;
	}
	/**
	 * @return the mGps_Longitude_image
	 */
	public String getmGps_Longitude_image() {
		return mGps_Longitude_image;
	}
	/**
	 * @param mGps_Longitude_image the mGps_Longitude_image to set
	 */
	public void setmGps_Longitude_image(String mGps_Longitude_image) {
		this.mGps_Longitude_image = mGps_Longitude_image;
	}
	/**
	 * @return the mGps_LongitudeCardinal_image
	 */
	public String getmGps_LongitudeCardinal_image() {
		return mGps_LongitudeCardinal_image;
	}
	/**
	 * @param mGps_LongitudeCardinal_image the mGps_LongitudeCardinal_image to set
	 */
	public void setmGps_LongitudeCardinal_image(String mGps_LongitudeCardinal_image) {
		this.mGps_LongitudeCardinal_image = mGps_LongitudeCardinal_image;
	}
	/**
	 * @return the mGps_Latitude_print
	 */
	public String getmGps_Latitude_print() {
		return mGps_Latitude_print;
	}
	/**
	 * @param mGps_Latitude_print the mGps_Latitude_print to set
	 */
	public void setmGps_Latitude_print(String mGps_Latitude_print) {
		this.mGps_Latitude_print = mGps_Latitude_print;
	}
	/**
	 * @return the mGps_LatitudeCardinal_print
	 */
	public String getmGps_LatitudeCardinal_print() {
		return mGps_LatitudeCardinal_print;
	}
	/**
	 * @param mGps_LatitudeCardinal_print the mGps_LatitudeCardinal_print to set
	 */
	public void setmGps_LatitudeCardinal_print(String mGps_LatitudeCardinal_print) {
		this.mGps_LatitudeCardinal_print = mGps_LatitudeCardinal_print;
	}
	/**
	 * @return the mGps_Longitude_print
	 */
	public String getmGps_Longitude_print() {
		return mGps_Longitude_print;
	}
	/**
	 * @param mGps_Longitude_print the mGps_Longitude_print to set
	 */
	public void setmGps_Longitude_print(String mGps_Longitude_print) {
		this.mGps_Longitude_print = mGps_Longitude_print;
	}
	/**
	 * @return the mGps_LongitudeCardinal_print
	 */
	public String getmGps_LongitudeCardinal_print() {
		return mGps_LongitudeCardinal_print;
	}
	/**
	 * @param mGps_LongitudeCardinal_print the mGps_LongitudeCardinal_print to set
	 */
	public void setmGps_LongitudeCardinal_print(String mGps_LongitudeCardinal_print) {
		this.mGps_LongitudeCardinal_print = mGps_LongitudeCardinal_print;
	}
	/**
	 * @return the mImage_Name
	 */
	public String getmImage_Name() {
		return mImage_Name;
	}
	/**
	 * @param mImage_Name the mImage_Name to set
	 */
	public void setmImage_Name(String mImage_Name) {
		this.mImage_Name = mImage_Name;
	}
	/**
	 * @return the mImage_Path
	 */
	public String getmImage_Path() {
		return mImage_Path;
	}
	/**
	 * @param mImage_Path the mImage_Path to set
	 */
	public void setmImage_Path(String mImage_Path) {
		this.mImage_Path = mImage_Path;
	}
	/**
	 * @return the mImage_Cap_Date
	 */
	public String getmImage_Cap_Date() {
		return mImage_Cap_Date;
	}
	/**
	 * @param mImage_Cap_Date the mImage_Cap_Date to set
	 */
	public void setmImage_Cap_Date(String mImage_Cap_Date) {
		this.mImage_Cap_Date = mImage_Cap_Date;
	}
	/**
	 * @return the mImage_Cap_Time
	 */
	public String getmImage_Cap_Time() {
		return mImage_Cap_Time;
	}
	/**
	 * @param mImage_Cap_Time the mImage_Cap_Time to set
	 */
	public void setmImage_Cap_Time(String mImage_Cap_Time) {
		this.mImage_Cap_Time = mImage_Cap_Time;
	}
	//30-06-2014
	public BigDecimal getmTax_Ec() {
		return mTax_Ec;
	}
	public void setmTax_Ec(BigDecimal mTax_Ec) {
		this.mTax_Ec = mTax_Ec;
	}
	//Added on 30-08-2014
	public int getmMeter_Present_Flag() {
		return mMeter_Present_Flag;
	}
	public void setmMeter_Present_Flag(int mMeter_Present_Flag) {
		this.mMeter_Present_Flag = mMeter_Present_Flag;
	}
	//Added on 30-08-2014
	public int getmMtr_Not_Visible() {
		return mMtr_Not_Visible;
	}
	public void setmMtr_Not_Visible(int mMtr_Not_Visible) {
		this.mMtr_Not_Visible = mMtr_Not_Visible;
	}
	/**
	 * @return the mUIDR_Slab
	 */
	public int getmUIDR_Slab() {
		return mUIDR_Slab;
	}
	/**
	 * @param mUIDR_Slab the mUIDR_Slab to set
	 */
	public void setmUIDR_Slab(int mUIDR_Slab) {
		this.mUIDR_Slab = mUIDR_Slab;
	}
	/**
	 * @return the mTarifCode
	 */
	/*public int getmTarifCode() {
		return mTarifCode;
	}*/
	/**
	 * @param mTarifCode the mTarifCode to set
	 */
	/*public void setmTarifCode(int mTarifCode) {
		this.mTarifCode = mTarifCode;
	}*/
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

	public int getmReasonId() {
		return mReasonId;
	}
	public void setmReasonId(int mReasonId) {
		this.mReasonId = mReasonId;
	}


	//##############################################################################################3
	public int getmUID() {
		return mUID;
	}
	public void setmUID(int mUID) {
		this.mUID = mUID;
	}
	public String getMmConnectionNo() {
		return mmConnectionNo;
	}
	public void setMmConnectionNo(String mmConnectionNo) {
		this.mmConnectionNo = mmConnectionNo;
	}
	public String getMmRRNo() {
		return mmRRNo;
	}
	public void setMmRRNo(String mmRRNo) {
		this.mmRRNo = mmRRNo;
	}
	public int getMmECRate_Count() {
		return mmECRate_Count;
	}
	public void setMmECRate_Count(int mmECRate_Count) {
		this.mmECRate_Count = mmECRate_Count;
	}
	public int getMmECRate_Row() {
		return mmECRate_Row;
	}
	public void setMmECRate_Row(int mmECRate_Row) {
		this.mmECRate_Row = mmECRate_Row;
	}
	public BigDecimal getMmFCRate_1() {
		return mmFCRate_1;
	}
	public void setMmFCRate_1(BigDecimal mmFCRate_1) {
		this.mmFCRate_1 = mmFCRate_1;
	}
	public BigDecimal getMmCOL_FCRate_2() {
		return mmCOL_FCRate_2;
	}
	public void setMmCOL_FCRate_2(BigDecimal mmCOL_FCRate_2) {
		this.mmCOL_FCRate_2 = mmCOL_FCRate_2;
	}
	public BigDecimal getMmUnits_1() {
		return mmUnits_1;
	}
	public void setMmUnits_1(BigDecimal mmUnits_1) {
		this.mmUnits_1 = mmUnits_1;
	}
	public BigDecimal getMmUnits_2() {
		return mmUnits_2;
	}
	public void setMmUnits_2(BigDecimal mmUnits_2) {
		this.mmUnits_2 = mmUnits_2;
	}
	public BigDecimal getMmUnits_3() {
		return mmUnits_3;
	}
	public void setMmUnits_3(BigDecimal mmUnits_3) {
		this.mmUnits_3 = mmUnits_3;
	}
	public BigDecimal getMmUnits_4() {
		return mmUnits_4;
	}
	public void setMmUnits_4(BigDecimal mmUnits_4) {
		this.mmUnits_4 = mmUnits_4;
	}
	public BigDecimal getMmUnits_5() {
		return mmUnits_5;
	}
	public void setMmUnits_5(BigDecimal mmUnits_5) {
		this.mmUnits_5 = mmUnits_5;
	}
	public BigDecimal getMmUnits_6() {
		return mmUnits_6;
	}
	public void setMmUnits_6(BigDecimal mmUnits_6) {
		this.mmUnits_6 = mmUnits_6;
	}
	public BigDecimal getMmEC_Rate_1() {
		return mmEC_Rate_1;
	}
	public void setMmEC_Rate_1(BigDecimal mmEC_Rate_1) {
		this.mmEC_Rate_1 = mmEC_Rate_1;
	}
	public BigDecimal getMmEC_Rate_2() {
		return mmEC_Rate_2;
	}
	public void setMmEC_Rate_2(BigDecimal mmEC_Rate_2) {
		this.mmEC_Rate_2 = mmEC_Rate_2;
	}
	public BigDecimal getMmEC_Rate_3() {
		return mmEC_Rate_3;
	}
	public void setMmEC_Rate_3(BigDecimal mmEC_Rate_3) {
		this.mmEC_Rate_3 = mmEC_Rate_3;
	}
	public BigDecimal getMmEC_Rate_4() {
		return mmEC_Rate_4;
	}
	public void setMmEC_Rate_4(BigDecimal mmEC_Rate_4) {
		this.mmEC_Rate_4 = mmEC_Rate_4;
	}
	public BigDecimal getMmEC_Rate_5() {
		return mmEC_Rate_5;
	}
	public void setMmEC_Rate_5(BigDecimal mmEC_Rate_5) {
		this.mmEC_Rate_5 = mmEC_Rate_5;
	}
	public BigDecimal getMmEC_Rate_6() {
		return mmEC_Rate_6;
	}
	public void setMmEC_Rate_6(BigDecimal mmEC_Rate_6) {
		this.mmEC_Rate_6 = mmEC_Rate_6;
	}
	public BigDecimal getMmEC_1() {
		return mmEC_1;
	}
	public void setMmEC_1(BigDecimal mmEC_1) {
		this.mmEC_1 = mmEC_1;
	}
	public BigDecimal getMmEC_2() {
		return mmEC_2;
	}
	public void setMmEC_2(BigDecimal mmEC_2) {
		this.mmEC_2 = mmEC_2;
	}
	public BigDecimal getMmEC_3() {
		return mmEC_3;
	}
	public void setMmEC_3(BigDecimal mmEC_3) {
		this.mmEC_3 = mmEC_3;
	}
	public BigDecimal getMmEC_4() {
		return mmEC_4;
	}
	public void setMmEC_4(BigDecimal mmEC_4) {
		this.mmEC_4 = mmEC_4;
	}
	public BigDecimal getMmEC_5() {
		return mmEC_5;
	}
	public void setMmEC_5(BigDecimal mmEC_5) {
		this.mmEC_5 = mmEC_5;
	}
	public BigDecimal getMmEC_6() {
		return mmEC_6;
	}
	public void setMmEC_6(BigDecimal mmEC_6) {
		this.mmEC_6 = mmEC_6;
	}
	public BigDecimal getMmFC_1() {
		return mmFC_1;
	}
	public void setMmFC_1(BigDecimal mmFC_1) {
		this.mmFC_1 = mmFC_1;
	}
	public BigDecimal getMmFC_2() {
		return mmFC_2;
	}
	public void setMmFC_2(BigDecimal mmFC_2) {
		this.mmFC_2 = mmFC_2;
	}
	public BigDecimal getMmTEc() {
		return mmTEc;
	}
	public void setMmTEc(BigDecimal mmTEc) {
		this.mmTEc = mmTEc;
	}
	public BigDecimal getMmEC_FL_1() {
		return mmEC_FL_1;
	}
	public void setMmEC_FL_1(BigDecimal mmEC_FL_1) {
		this.mmEC_FL_1 = mmEC_FL_1;
	}
	public BigDecimal getMmEC_FL_2() {
		return mmEC_FL_2;
	}
	public void setMmEC_FL_2(BigDecimal mmEC_FL_2) {
		this.mmEC_FL_2 = mmEC_FL_2;
	}
	public BigDecimal getMmEC_FL_3() {
		return mmEC_FL_3;
	}
	public void setMmEC_FL_3(BigDecimal mmEC_FL_3) {
		this.mmEC_FL_3 = mmEC_FL_3;
	}
	public BigDecimal getMmEC_FL_4() {
		return mmEC_FL_4;
	}
	public void setMmEC_FL_4(BigDecimal mmEC_FL_4) {
		this.mmEC_FL_4 = mmEC_FL_4;
	}
	public BigDecimal getMmEC_FL() {
		return mmEC_FL;
	}
	public void setMmEC_FL(BigDecimal mmEC_FL) {
		this.mmEC_FL = mmEC_FL;
	}
	public BigDecimal getMmnew_TEc() {
		return mmnew_TEc;
	}
	public void setMmnew_TEc(BigDecimal mmnew_TEc) {//
		this.mmnew_TEc = mmnew_TEc;
	}
	public BigDecimal getMmold_TEc() {
		return mmold_TEc;
	}
	public void setMmold_TEc(BigDecimal mmold_TEc) {
		this.mmold_TEc = mmold_TEc;
	}
	public BigDecimal getmDLTEc_GoK() {
		return mDLTEc_GoK;
	}
	public void setmDLTEc_GoK(BigDecimal mDLTEc_GoK) {
		this.mDLTEc_GoK = mDLTEc_GoK;
	}
	public String getmAadharNo() {
		return mAadharNo;
	}
	public void setmAadharNo(String mAadharNo) {
		this.mAadharNo = mAadharNo;
	}
	public String getmVoterIdNo() {
		return mVoterIdNo;
	}
	public void setmVoterIdNo(String mVoterIdNo) {
		this.mVoterIdNo = mVoterIdNo;
	}
	public int getmMtrMakeFlag() {
		return mMtrMakeFlag;
	}
	public void setmMtrMakeFlag(int mMtrMakeFlag) {
		this.mMtrMakeFlag = mMtrMakeFlag;
	}
	public int getmMtrBodySealFlag() {
		return mMtrBodySealFlag;
	}
	public void setmMtrBodySealFlag(int mMtrBodySealFlag) {
		this.mMtrBodySealFlag = mMtrBodySealFlag;
	}
	public int getmMtrTerminalCoverFlag() {
		return mMtrTerminalCoverFlag;
	}
	public void setmMtrTerminalCoverFlag(int mMtrTerminalCoverFlag) {
		this.mMtrTerminalCoverFlag = mMtrTerminalCoverFlag;
	}
	public int getmMtrTerminalCoverSealedFlag() {
		return mMtrTerminalCoverSealedFlag;
	}
	public void setmMtrTerminalCoverSealedFlag(int mMtrTerminalCoverSealedFlag) {
		this.mMtrTerminalCoverSealedFlag = mMtrTerminalCoverSealedFlag;
	}
	public String getmFACValue() {
		return mFACValue;
	}
	public void setmFACValue(String mFACValue) {
		this.mFACValue = mFACValue;
	}
	public BigDecimal getmOldTecBill() {
		return mOldTecBill;
	}
	public void setmOldTecBill(BigDecimal mOldTecBill) {
		this.mOldTecBill = mOldTecBill;
	}
	public BigDecimal getmFC_Slab_3() {
		return mFC_Slab_3;
	}
	public void setmFC_Slab_3(BigDecimal mFC_Slab_3) {
		this.mFC_Slab_3 = mFC_Slab_3;
	}






}



//punit end
