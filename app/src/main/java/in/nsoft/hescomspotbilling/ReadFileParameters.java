package in.nsoft.hescomspotbilling;

import java.util.ArrayList;

//Class created By Tamilselvan on 03-03-2014 for reading columns from file
public class ReadFileParameters {
	public ReadFileParameters()
	{
		mSlabNTariff = new ArrayList<SlabNTariff>();
	}
	private String mMonthId;//1
	private String mYearOfBill;//2
	private String mForMonth;	
	private String mBillDate;//3
	private String mSubId;//4
	private String mConnectionNo;//5
	private String mCustomerName;//6
	private String mTourplanId;//7
	private String mBillNo;//8
	private String mDueDate;//9
	private String mFixedCharges;//10
	private String mRebateFlag;//11
	private String mReaderCode;//12
	private String mTariffCode;//13
	private String mReadingDay;//14
	private String mPF;//15
	private String mMF;//16
	private String mStatus;//17
	private String mAvgCONs;//18
	private String mLinMin;//19
	private String mSancHp;//20
	private String mSancKw;//21
	private String mSancLoad;//22
	private String mPrevRdg;//23
	private String mDlCount;//24
	private String mArears;//25
	private String mIntrstCurnt;//26
	private String mDrFees;//27
	private String mOthers;//28
	private String mBillFor;//29
	private String mBlCnt;//30
	private String mRRNo;//31
	private String mLegFol;//32
	private String mTvmMtr;//33
	private String mTaxFlag;//34
	private String mArrearsOld;//35
	private String mIntrst_unpaid;//36
	private String mIntrstOld;//37
	private String mBillable;//38
	private String mNewNoofDays;//39
	private String mNoOfDays;//40
	private String mHWCReb;//41
	private String mDLAvgMin;//42
	private String mTvmPFtype;//43
	private String mAccdRdg;//44
	private String mKVAIR;//45
	private String mDLTEc;//46
	private String mRRFlag;//47
	private String mMtd;//48
	private String mSlowRtnPge;//49
	private String mOtherChargeLegEND;//50
	private String mGoKArrears;//51
	private String mDPdate;//52
	private String mReceiptAmnt;//53
	private String mReceiptDate;//54
	private String mTcName;//55
	private String mThirtyFlag;//56
	private String mIODRemarks;//57
	private String mDayWise_Flag;//58
	private String mOld_Consumption;//59
	private String mKVAAssd_Cons;//60
	private String mGvpId;//61
	private String mBOBilled_Amount;//62
	private String mKVAH_OldConsumption;//63
	private String mEcsFlag;//64
	private String mSupply_Points;//65
	private String mIODD11_Remarks;//66
	private String mLocationCode;//67
	private String mBOBillFlag;//68
	private String mAddress1;//69
	private String mAddress2;//70
	private String mSectionName;//71
	private String mOldConnID;//72
	private String mMCHFLag;//73
	private String mFC_Slab_2;//74
	private String mMobileNo;//75
	
	private String mVersionNo;
	private String mSubDivName;
	
	public String getmMonthId() {
		return mMonthId;
	}
	public void setmMonthId(String mMonthId) {
		this.mMonthId = mMonthId;
	}
	public String getmYearOfBill() {
		return mYearOfBill;
	}
	public void setmYearOfBill(String mYearOfBill) {
		this.mYearOfBill = mYearOfBill;
	}
	public String getmForMonth() {
		return mForMonth;
	}
	public void setmForMonth(String mForMonth) {
		this.mForMonth = mForMonth;
	}
	public String getmBillDate() {
		return mBillDate;
	}
	public void setmBillDate(String mBillDate) {
		this.mBillDate = mBillDate;
	}
	public String getmSubId() {
		return mSubId;
	}
	public void setmSubId(String mSubId) {
		this.mSubId = mSubId;
	}
	public String getmConnectionNo() {
		return mConnectionNo;
	}
	public void setmConnectionNo(String mConnectionNo) {
		this.mConnectionNo = mConnectionNo;
	}
	public String getmCustomerName() {
		return mCustomerName;
	}
	public void setmCustomerName(String mCustomerName) {
		this.mCustomerName = mCustomerName;
	}
	public String getmTourplanId() {
		return mTourplanId;
	}
	public void setmTourplanId(String mTourplanId) {
		this.mTourplanId = mTourplanId;
	}
	public String getmBillNo() {
		return mBillNo;
	}
	public void setmBillNo(String mBillNo) {
		this.mBillNo = mBillNo;
	}
	public String getmDueDate() {
		return mDueDate;
	}
	public void setmDueDate(String mDueDate) {
		this.mDueDate = mDueDate;
	}
	public String getmFixedCharges() {
		return mFixedCharges;
	}
	public void setmFixedCharges(String mFixedCharges) {
		this.mFixedCharges = mFixedCharges;
	}
	public String getmRebateFlag() {
		return mRebateFlag;
	}
	public void setmRebateFlag(String mRebateFlag) {
		this.mRebateFlag = mRebateFlag;
	}
	public String getmReaderCode() {
		return mReaderCode;
	}
	public void setmReaderCode(String mReaderCode) {
		this.mReaderCode = mReaderCode;
	}
	public String getmTariffCode() {
		return mTariffCode;
	}
	public void setmTariffCode(String mTariffCode) {
		this.mTariffCode = mTariffCode;
	}
	public String getmReadingDay() {
		return mReadingDay;
	}
	public void setmReadingDay(String mReadingDay) {
		this.mReadingDay = mReadingDay;
	}
	public String getmPF() {
		return mPF;
	}
	public void setmPF(String mPF) {
		this.mPF = mPF;
	}
	public String getmMF() {
		return mMF;
	}
	public void setmMF(String mMF) {
		this.mMF = mMF;
	}
	public String getmStatus() {
		return mStatus;
	}
	public void setmStatus(String mStatus) {
		this.mStatus = mStatus;
	}
	public String getmAvgCONs() {
		return mAvgCONs;
	}
	public void setmAvgCONs(String mAvgCONs) {
		this.mAvgCONs = mAvgCONs;
	}
	public String getmLinMin() {
		return mLinMin;
	}
	public void setmLinMin(String mLinMin) {
		this.mLinMin = mLinMin;
	}
	public String getmSancHp() {
		return mSancHp;
	}
	public void setmSancHp(String mSancHp) {
		this.mSancHp = mSancHp;
	}
	public String getmSancKw() {
		return mSancKw;
	}
	public void setmSancKw(String mSancKw) {
		this.mSancKw = mSancKw;
	}
	public String getmSancLoad() {
		return mSancLoad;
	}
	public void setmSancLoad(String mSancLoad) {
		this.mSancLoad = mSancLoad;
	}
	public String getmPrevRdg() {
		return mPrevRdg;
	}
	public void setmPrevRdg(String mPrevRdg) {
		this.mPrevRdg = mPrevRdg;
	}
	public String getmDlCount() {
		return mDlCount;
	}
	public void setmDlCount(String mDlCount) {
		this.mDlCount = mDlCount;
	}
	public String getmArears() {
		return mArears;
	}
	public void setmArears(String mArears) {
		this.mArears = mArears;
	}
	public String getmIntrstCurnt() {
		return mIntrstCurnt;
	}
	public void setmIntrstCurnt(String mIntrstCurnt) {
		this.mIntrstCurnt = mIntrstCurnt;
	}
	public String getmDrFees() {
		return mDrFees;
	}
	public void setmDrFees(String mDrFees) {
		this.mDrFees = mDrFees;
	}
	public String getmOthers() {
		return mOthers;
	}
	public void setmOthers(String mOthers) {
		this.mOthers = mOthers;
	}
	public String getmBillFor() {
		return mBillFor;
	}
	public void setmBillFor(String mBillFor) {
		this.mBillFor = mBillFor;
	}
	public String getmBlCnt() {
		return mBlCnt;
	}
	public void setmBlCnt(String mBlCnt) {
		this.mBlCnt = mBlCnt;
	}
	public String getmRRNo() {
		return mRRNo;
	}
	public void setmRRNo(String mRRNo) {
		this.mRRNo = mRRNo;
	}
	public String getmLegFol() {
		return mLegFol;
	}
	public void setmLegFol(String mLegFol) {
		this.mLegFol = mLegFol;
	}
	public String getmTvmMtr() {
		return mTvmMtr;
	}
	public void setmTvmMtr(String mTvmMtr) {
		this.mTvmMtr = mTvmMtr;
	}
	public String getmTaxFlag() {
		return mTaxFlag;
	}
	public void setmTaxFlag(String mTaxFlag) {
		this.mTaxFlag = mTaxFlag;
	}
	public String getmArrearsOld() {
		return mArrearsOld;
	}
	public void setmArrearsOld(String mArrearsOld) {
		this.mArrearsOld = mArrearsOld;
	}
	public String getmIntrst_unpaid() {
		return mIntrst_unpaid;
	}
	public void setmIntrst_unpaid(String mIntrst_unpaid) {
		this.mIntrst_unpaid = mIntrst_unpaid;
	}
	public String getmIntrstOld() {
		return mIntrstOld;
	}
	public void setmIntrstOld(String mIntrstOld) {
		this.mIntrstOld = mIntrstOld;
	}
	public String getmBillable() {
		return mBillable;
	}
	public void setmBillable(String mBillable) {
		this.mBillable = mBillable;
	}
	public String getmNewNoofDays() {
		return mNewNoofDays;
	}
	public void setmNewNoofDays(String mNewNoofDays) {
		this.mNewNoofDays = mNewNoofDays;
	}
	public String getmNoOfDays() {
		return mNoOfDays;
	}
	public void setmNoOfDays(String mNoOfDays) {
		this.mNoOfDays = mNoOfDays;
	}
	public String getmHWCReb() {
		return mHWCReb;
	}
	public void setmHWCReb(String mHWCReb) {
		this.mHWCReb = mHWCReb;
	}
	public String getmDLAvgMin() {
		return mDLAvgMin;
	}
	public void setmDLAvgMin(String mDLAvgMin) {
		this.mDLAvgMin = mDLAvgMin;
	}
	public String getmTvmPFtype() {
		return mTvmPFtype;
	}
	public void setmTvmPFtype(String mTvmPFtype) {
		this.mTvmPFtype = mTvmPFtype;
	}
	public String getmAccdRdg() {
		return mAccdRdg;
	}
	public void setmAccdRdg(String mAccdRdg) {
		this.mAccdRdg = mAccdRdg;
	}
	public String getmKVAIR() {
		return mKVAIR;
	}
	public void setmKVAIR(String mKVAIR) {
		this.mKVAIR = mKVAIR;
	}
	public String getmDLTEc() {
		return mDLTEc;
	}
	public void setmDLTEc(String mDLTEc) {
		this.mDLTEc = mDLTEc;
	}
	public String getmRRFlag() {
		return mRRFlag;
	}
	public void setmRRFlag(String mRRFlag) {
		this.mRRFlag = mRRFlag;
	}
	public String getmMtd() {
		return mMtd;
	}
	public void setmMtd(String mMtd) {
		this.mMtd = mMtd;
	}
	public String getmSlowRtnPge() {
		return mSlowRtnPge;
	}
	public void setmSlowRtnPge(String mSlowRtnPge) {
		this.mSlowRtnPge = mSlowRtnPge;
	}
	public String getmOtherChargeLegEND() {
		return mOtherChargeLegEND;
	}
	public void setmOtherChargeLegEND(String mOtherChargeLegEND) {
		this.mOtherChargeLegEND = mOtherChargeLegEND;
	}
	public String getmGoKArrears() {
		return mGoKArrears;
	}
	public void setmGoKArrears(String mGoKArrears) {
		this.mGoKArrears = mGoKArrears;
	}
	public String getmDPdate() {
		return mDPdate;
	}
	public void setmDPdate(String mDPdate) {
		this.mDPdate = mDPdate;
	}
	public String getmReceiptAmnt() {
		return mReceiptAmnt;
	}
	public void setmReceiptAmnt(String mReceiptAmnt) {
		this.mReceiptAmnt = mReceiptAmnt;
	}
	public String getmReceiptDate() {
		return mReceiptDate;
	}
	public void setmReceiptDate(String mReceiptDate) {
		this.mReceiptDate = mReceiptDate;
	}
	public String getmTcName() {
		return mTcName;
	}
	public void setmTcName(String mTcName) {
		this.mTcName = mTcName;
	}
	public String getmThirtyFlag() {
		return mThirtyFlag;
	}
	public void setmThirtyFlag(String mThirtyFlag) {
		this.mThirtyFlag = mThirtyFlag;
	}
	public String getmIODRemarks() {
		return mIODRemarks;
	}
	public void setmIODRemarks(String mIODRemarks) {
		this.mIODRemarks = mIODRemarks;
	}
	public String getmDayWise_Flag() {
		return mDayWise_Flag;
	}
	public void setmDayWise_Flag(String mDayWise_Flag) {
		this.mDayWise_Flag = mDayWise_Flag;
	}
	public String getmOld_Consumption() {
		return mOld_Consumption;
	}
	public void setmOld_Consumption(String mOld_Consumption) {
		this.mOld_Consumption = mOld_Consumption;
	}
	public String getmKVAAssd_Cons() {
		return mKVAAssd_Cons;
	}
	public void setmKVAAssd_Cons(String mKVAAssd_Cons) {
		this.mKVAAssd_Cons = mKVAAssd_Cons;
	}
	public String getmGvpId() {
		return mGvpId;
	}
	public void setmGvpId(String mGvpId) {
		this.mGvpId = mGvpId;
	}
	public String getmBOBilled_Amount() {
		return mBOBilled_Amount;
	}
	public void setmBOBilled_Amount(String mBOBilled_Amount) {
		this.mBOBilled_Amount = mBOBilled_Amount;
	}
	public String getmKVAH_OldConsumption() {
		return mKVAH_OldConsumption;
	}
	public void setmKVAH_OldConsumption(String mKVAH_OldConsumption) {
		this.mKVAH_OldConsumption = mKVAH_OldConsumption;
	}
	public String getmEcsFlag() {
		return mEcsFlag;
	}
	public void setmEcsFlag(String mEcsFlag) {
		this.mEcsFlag = mEcsFlag;
	}
	public String getmSupply_Points() {
		return mSupply_Points;
	}
	public void setmSupply_Points(String mSupply_Points) {
		this.mSupply_Points = mSupply_Points;
	}
	public String getmIODD11_Remarks() {
		return mIODD11_Remarks;
	}
	public void setmIODD11_Remarks(String mIODD11_Remarks) {
		this.mIODD11_Remarks = mIODD11_Remarks;
	}
	public String getmLocationCode() {
		return mLocationCode;
	}
	public void setmLocationCode(String mLocationCode) {
		this.mLocationCode = mLocationCode;
	}
	public String getmBOBillFlag() {
		return mBOBillFlag;
	}
	public void setmBOBillFlag(String mBOBillFlag) {
		this.mBOBillFlag = mBOBillFlag;
	}
	public String getmAddress1() {
		return mAddress1;
	}
	public void setmAddress1(String mAddress1) {
		this.mAddress1 = mAddress1;
	}
	public String getmAddress2() {
		return mAddress2;
	}
	public void setmAddress2(String mAddress2) {
		this.mAddress2 = mAddress2;
	}
	public String getmSectionName() {
		return mSectionName;
	}
	public void setmSectionName(String mSectionName) {
		this.mSectionName = mSectionName;
	}
	public String getmOldConnID() {
		return mOldConnID;
	}
	public void setmOldConnID(String mOldConnID) {
		this.mOldConnID = mOldConnID;
	}
	public String getmMCHFLag() {
		return mMCHFLag;
	}
	public void setmMCHFLag(String mMCHFLag) {
		this.mMCHFLag = mMCHFLag;
	}
	public String getmFC_Slab_2() {
		return mFC_Slab_2;
	}
	public void setmFC_Slab_2(String mFC_Slab_2) {
		this.mFC_Slab_2 = mFC_Slab_2;
	}
	public String getmMobileNo() {
		return mMobileNo;
	}
	public void setmMobileNo(String mMobileNo) {
		this.mMobileNo = mMobileNo;
	}
	
	public String getmVersionNo() {
		return mVersionNo;
	}
	public void setmVersionNo(String mVersionNo) {
		this.mVersionNo = mVersionNo;
	}
	public String getmSubDivName() {
		return mSubDivName;
	}
	public void setmSubDivName(String mSubDivName) {
		this.mSubDivName = mSubDivName;
	}
	
	//Slab Tariff=====================================================
	private ArrayList<SlabNTariff> mSlabNTariff;
	
	public ArrayList<SlabNTariff> getmSlabNTariff() {
		return mSlabNTariff;
	}
	public void setmSlabNTariff(ArrayList<SlabNTariff> mSlabNTariff) {
		this.mSlabNTariff = mSlabNTariff;
	}
	
	public class SlabNTariff
	{		
		private String TarifCode;
		private String TarifString;
		
		public String getTarifCode() {
			return TarifCode;
		}
		public void setTarifCode(String tarifCode) {
			TarifCode = tarifCode;
		}
		public String getTarifString() {
			return TarifString;
		}
		public void setTarifString(String tarifString) {
			TarifString = tarifString;
		}		
	}
	//END Slab Tariff=====================================================
}
