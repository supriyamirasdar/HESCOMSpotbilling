package in.nsoft.hescomspotbilling;
//Class Created by Tamilselvan on 07-03-2014
public class WriteFileParameters {
	private String mForMonth;
	private String mBillDate;
	private String mSubId;
	private String mConnectionNo;
	private String mTourplanId;
	private String mPF;
	private String mSancLoad;
	private String mDlCount;
	private String mBillFor;
	private String mBlCnt;
	private String mTvmMtr;
	private String mPreRead;
	//----->1 space
	private String mStatus;
	private String mSpotSerialNo;
	private String munits;
	private String mTFc;
	private String mTEc;
	private String mFlReb;
	private String mEcReb;
	private String mTaxAmt;
	private String mPfPenAmt;
	private String mPenExLd;
	private String mHCReb;
	private String mHLReb;
	private String mCapReb;
	private String mExLoad;
	private String mDemandChrg;
	private String mAccdRdg_rtn;
	private String mkVAFR;
	private String mAbFlag;
	private String mBjKj2Lt2;
	private String mRemarks;
	private String mGoKPayable;
	private String mIssueDateTime;
	private String mRecordDmnd;
	private String mKVA_Consumption;
	private String mKVAAssd_Cons;
	//---->add 0 
	//private String mTvmMtr;
	private String mTariffCode;
	private String mLinMin;
	private String mPrevRdg;
	private String mRRNo;
	private String mBillTotal;
	private String mSBMNumber;
	private String mLocationCode;
	private String mEC_1;
	private String mEC_2;
	private String mEC_3;
	private String mEC_4;
	private String mMobileNo;
	private String mMtrDisFlag;
	private String mMeter_type;
	private String mMeter_serialno;
	private String mTcname_present;
	private String mMeter_present_serialno;
	private String mMeter_not_visible;
	private String mVer;
	private String mLatitude;
	private String mLongitude;

	//Nitish 27-03-2014 For Collection_Table
	private String mReceipttypeflag_Coll; //01
	private String mConsNo_Coll; //07
	private String mBatch_No_Coll;  //25
	private String mReceipt_No_Coll; //05
	private String mGvpId_Coll;  //05
	private String mDateTime_Coll; //12
	private String mPayment_Mode_Coll; //1
	private String mPaid_Amt_Coll; //5
	private String mChequeDDNo_Coll; //6
	private String mChequeDDDate_Coll; //6
	private String mBankID_Coll; //3
	private String mSBMNumber_Coll; //10
	private String mLocation_code_Coll; //7
	private String mVer_Coll; //3

	// 30-07-2015
	private String mAadharNo;
	private String mVoterIdNo; 
	private String mMtrMakeFlag;
	private String mMtrBodySealFlag;  
	private String mMtrTerminalCoverFlag;  
	private String mMtrTerminalCoverSealedFlag;  

	//End 30-07-2015


	private String mEC_5; //Nitish 15-04-2017
	private String mEC_6; //Nitish 15-04-2017

	//10-03-2021
	private String mLTSlNo;
	private String mLTReading;
	private String mLTMD;
	private String mLTPF;
	private String mLTMeterType;

	public String getmReceipttypeflag_Coll() {
		return mReceipttypeflag_Coll;
	}

	public void setmReceipttypeflag_Coll(String mReceipttypeflag_Coll) {
		if(mReceipttypeflag_Coll == null)
		{
			this.mReceipttypeflag_Coll = addSpace("", 1, ' ');
		}
		else
		{
			if(mReceipttypeflag_Coll.length() < 1)
			{
				this.mReceipttypeflag_Coll = addSpace(mReceipttypeflag_Coll, (1 - mReceipttypeflag_Coll.length()), ' ');
			}
			else if(mReceipttypeflag_Coll.length()==1)
			{
				this.mReceipttypeflag_Coll = mReceipttypeflag_Coll;
			}
		}
	}

	public String getmConsNo_Coll() {
		return mConsNo_Coll;
	}

	public void setmConsNo_Coll(String mConsNo_Coll) {
		if(mConsNo_Coll == null)
		{
			this.mConsNo_Coll = addSpace("", 7, ' ');
		}
		else
		{
			mConsNo_Coll = mConsNo_Coll.trim();
			if(mConsNo_Coll.length() < 7)
			{
				this.mConsNo_Coll = addSpace(mConsNo_Coll, (7 - mConsNo_Coll.length()), ' ');
			}
			else if(mConsNo_Coll.length()==7)
			{
				this.mConsNo_Coll = mConsNo_Coll;
			}			
		}
	}

	public String getmBatch_No_Coll() {
		return mBatch_No_Coll;
	}

	public void setmBatch_No_Coll(String mBatch_No_Coll) {
		if(mBatch_No_Coll == null)
		{
			this.mBatch_No_Coll = addSpace("", 32, ' ');
		}
		else
		{
			mBatch_No_Coll = mBatch_No_Coll.trim();
			if(mBatch_No_Coll.length() < 32)
			{
				this.mBatch_No_Coll = addSpace(mBatch_No_Coll, (32 - mBatch_No_Coll.length()), ' ');
			}
			else if(mBatch_No_Coll.length()==32)
			{
				this.mBatch_No_Coll = mBatch_No_Coll; 
			}			
		}
	}

	public String getmReceipt_No_Coll() {
		return mReceipt_No_Coll;
	}


	public void setmReceipt_No_Coll(String mReceipt_No_Coll) {
		if(mReceipt_No_Coll == null)
		{
			this.mReceipt_No_Coll = addSpace("", 5, ' ');
		}
		else
		{
			mReceipt_No_Coll = mReceipt_No_Coll.trim();
			if(mReceipt_No_Coll.length() < 5)
			{
				this.mReceipt_No_Coll = addSpace(mReceipt_No_Coll, (5 - mReceipt_No_Coll.length()), ' ');
			}
			else if(mReceipt_No_Coll.length()==5)
			{
				this.mReceipt_No_Coll = mReceipt_No_Coll;
			}
			else if(mReceipt_No_Coll.length()>5) //27-04-2017
			{
				this.mReceipt_No_Coll = mReceipt_No_Coll.substring(0, 5);
			}
		}
	}

	public String getmGvpId_Coll() {
		return mGvpId_Coll;
	}

	public void setmGvpId_Coll(String mGvpId_Coll) {
		if(mGvpId_Coll == null)
		{
			this.mGvpId_Coll = addSpace("", 5, ' ');
		}
		else
		{
			mGvpId_Coll = mGvpId_Coll.trim();
			if(mGvpId_Coll.length() < 5)
			{
				this.mGvpId_Coll = addSpace(mGvpId_Coll, (5 - mGvpId_Coll.length()), ' ');
			}
			else if(mGvpId_Coll.length()==5)
			{
				this.mGvpId_Coll = mGvpId_Coll;
			}
			else if(mGvpId_Coll.length()>5) //27-04-2017
			{
				this.mGvpId_Coll = mGvpId_Coll.substring(0, 5);
			}
		}
	}

	public String getmDateTime_Coll() {
		return mDateTime_Coll;
	}

	public void setmDateTime_Coll(String mDateTime_Coll) {
		if(mDateTime_Coll == null)
		{
			this.mDateTime_Coll = addSpace("", 12, ' ');
		}
		else
		{
			mDateTime_Coll = mDateTime_Coll.trim();
			if(mDateTime_Coll.length() < 12)
			{
				this.mDateTime_Coll = addSpace(mDateTime_Coll, (12 - mDateTime_Coll.length()), ' ');
			}
			else if(mDateTime_Coll.length()==12)
			{
				this.mDateTime_Coll = mDateTime_Coll;
			}
		}
	}

	public String getmPayment_Mode_Coll() {
		return mPayment_Mode_Coll;
	}

	public void setmPayment_Mode_Coll(String mPayment_Mode_Coll) {
		if(mPayment_Mode_Coll == null)
		{
			this.mPayment_Mode_Coll = addSpace("", 1, ' ');
		}
		else
		{
			mPayment_Mode_Coll =mPayment_Mode_Coll.trim();
			if(mPayment_Mode_Coll.length() < 1)
			{
				this.mPayment_Mode_Coll = addSpace(mPayment_Mode_Coll, (1 - mPayment_Mode_Coll.length()), ' ');
			}
			else if(mPayment_Mode_Coll.length()==1)
			{
				this.mPayment_Mode_Coll = mPayment_Mode_Coll;
			}
		}
	}

	public String getmPaid_Amt_Coll() {
		return mPaid_Amt_Coll;
	}

	public void setmPaid_Amt_Coll(String mPaid_Amt_Coll) {
		if(mPayment_Mode_Coll == null)
		{
			this.mPaid_Amt_Coll = addSpace("", 5, ' ');
		}
		else
		{
			mPaid_Amt_Coll =mPaid_Amt_Coll.trim();
			if(mPaid_Amt_Coll.length() < 5)
			{
				this.mPaid_Amt_Coll = addSpace(mPaid_Amt_Coll, (5 - mPaid_Amt_Coll.length()), ' ');
			}
			else if(mPaid_Amt_Coll.length()==5)
			{
				this.mPaid_Amt_Coll = mPaid_Amt_Coll;
			}
		}
	}

	public String getmChequeDDNo_Coll() {
		return mChequeDDNo_Coll;
	}

	public void setmChequeDDNo_Coll(String mChequeDDNo_Coll) {
		if(mChequeDDNo_Coll == null || mChequeDDNo_Coll.equals("0"))
		{
			this.mChequeDDNo_Coll ="000000" ;
		}
		else
		{
			mChequeDDNo_Coll =mChequeDDNo_Coll.trim();
			if(mChequeDDNo_Coll.length() < 6)
			{
				this.mChequeDDNo_Coll = addSpace(mChequeDDNo_Coll, (6 - mChequeDDNo_Coll.length()), ' ');
			}
			else if(mChequeDDNo_Coll.length()==6)
			{
				this.mChequeDDNo_Coll = mChequeDDNo_Coll;
			}
		}
	}

	public String getmChequeDDDate_Coll() {
		return mChequeDDDate_Coll;
	}

	public void setmChequeDDDate_Coll(String mChequeDDDate_Coll) {
		if(mChequeDDDate_Coll == null)
		{
			//this.mChequeDDDate_Coll = addSpace("", 6, ' ');
			this.mChequeDDDate_Coll ="000000" ;
		}
		else
		{
			mChequeDDDate_Coll =mChequeDDDate_Coll.trim();
			if(mChequeDDDate_Coll.length() < 6)
			{
				this.mChequeDDDate_Coll = addSpace(mChequeDDDate_Coll, (6 - mChequeDDDate_Coll.length()), ' ');
			}
			else if(mChequeDDDate_Coll.length()==6)
			{
				this.mChequeDDDate_Coll = mChequeDDDate_Coll;
			}
		}
	}

	public String getmBankID_Coll() {
		return mBankID_Coll;
	}

	public void setmBankID_Coll(String mBankID_Coll) {
		if(mBankID_Coll == null || mBankID_Coll.equals("0"))
		{
			this.mBankID_Coll ="000" ;
		}
		else
		{
			mBankID_Coll =mBankID_Coll.trim();
			if(mBankID_Coll.length() < 3)
			{
				this.mBankID_Coll = addSpace(mBankID_Coll, (3 - mBankID_Coll.length()), ' ');
			}
			else if(mBankID_Coll.length()==3)
			{
				this.mBankID_Coll = mBankID_Coll;
			}
		}
	}

	public String getmSBMNumber_Coll() {
		return mSBMNumber_Coll;
	}

	public void setmSBMNumber_Coll(String mSBMNumber_Coll) {
		if(mSBMNumber_Coll == null)
		{
			this.mSBMNumber_Coll = addSpace("", 15, ' ');
		}
		else
		{
			mSBMNumber_Coll =mSBMNumber_Coll.trim();
			if(mSBMNumber_Coll.length() < 15)
			{
				this.mSBMNumber_Coll = addSpace(mSBMNumber_Coll, (15 - mSBMNumber_Coll.length()), ' ');
			}
			else if(mSBMNumber_Coll.length()==15)
			{
				this.mSBMNumber_Coll = mSBMNumber_Coll;
			}
		}
	}

	public String getmLocation_code_Coll() {
		return mLocation_code_Coll;
	}

	public void setmLocation_code_Coll(String mLocation_code_Coll) {
		if(mLocation_code_Coll == null)
		{
			this.mLocation_code_Coll = addSpace("", 7, ' ');
		}
		else
		{
			mLocation_code_Coll =mLocation_code_Coll.trim();
			if(mLocation_code_Coll.length() < 7)
			{
				this.mLocation_code_Coll = addSpace(mLocation_code_Coll, (7 - mLocation_code_Coll.length()), ' ');
			}
			else if(mLocation_code_Coll.length()==7)
			{
				this.mLocation_code_Coll = mLocation_code_Coll;
			}
		}
	}

	public String getmVer_Coll() {
		return mVer_Coll;
	}

	public void setmVer_Coll(String mVer_Coll) {
		if(mVer_Coll == null)
		{
			this.mVer_Coll = addSpace("", 3, ' ');
		}
		else
		{
			mVer_Coll =mVer_Coll.trim();
			if(mVer_Coll.length() < 3)
			{
				this.mVer_Coll = addSpace(mVer_Coll, (3 - mVer_Coll.length()), ' ');
			}
			else if(mVer_Coll.length()==3)
			{
				this.mVer_Coll = mVer_Coll;
			}
		}
	}
	//Nitish End

	//-----------------------------------------Nitish 01-07-2014 For GPS_Details--------------------------------------------------------

	private String mIMEINoGPS;//15 
	private String mSIMNoGPS;//20 
	private String mCurrentDateTimeGPS;//12 
	private String mMRIDGPS;//11 
	private String mLatitudeGPS;//15 
	private String mLongitudeGPS;//15 
	private String mLocationCodeGPS;//10		

	public String getmLocationCodeGPS() {
		return mLocationCodeGPS;
	}
	public void setmLocationCodeGPS(String mLocationCodeGPS) {
		if(mLocationCodeGPS == null)
		{
			this.mLocationCodeGPS = addSpaceRight("", 7, ' ');
		}
		else
		{
			mLocationCodeGPS = mLocationCodeGPS.trim();
			if(mLocationCodeGPS.length() < 7 )
			{
				this.mLocationCodeGPS = addSpaceRight(mLocationCodeGPS, (7 - mLocationCodeGPS.length()), ' ');
			}
			else if(mLocationCodeGPS.length()==7)
			{
				this.mLocationCodeGPS = mLocationCodeGPS;
			}			
		}
	}
	public String getmIMEINoGPS() {
		return mIMEINoGPS;
	}
	public void setmIMEINoGPS(String mIMEINoGPS) {
		if(mIMEINoGPS == null)
		{
			this.mIMEINoGPS = addSpaceRight("", 15, ' ');
		}
		else
		{
			mIMEINoGPS = mIMEINoGPS.trim();
			if(mIMEINoGPS.length() < 15)
			{
				this.mIMEINoGPS = addSpaceRight(mIMEINoGPS, (15 - mIMEINoGPS.length()), ' ');
			}
			else if(mIMEINoGPS.length()==15)
			{
				this.mIMEINoGPS = mIMEINoGPS;
			}			
		}
	}
	public String getmSIMNoGPS() {
		return mSIMNoGPS;
	}
	public void setmSIMNoGPS(String mSIMNoGPS) {
		if(mSIMNoGPS == null)
		{
			this.mSIMNoGPS = addSpaceRight("", 20, ' ');
		}
		else
		{
			mSIMNoGPS = mSIMNoGPS.trim();
			if(mSIMNoGPS.length() < 20)
			{
				this.mSIMNoGPS = addSpaceRight(mSIMNoGPS, (20 - mSIMNoGPS.length()), ' ');
			}
			else if(mSIMNoGPS.length()==20)
			{
				this.mSIMNoGPS = mSIMNoGPS;
			}			
		}
	}
	public String getmCurrentDateTimeGPS() {
		return mCurrentDateTimeGPS;
	}
	public void setmCurrentDateTimeGPS(String mCurrentDateTimeGPS) {
		if(mCurrentDateTimeGPS == null)
		{
			this.mCurrentDateTimeGPS = addSpaceRight("", 14, ' ');
		}
		else
		{
			mCurrentDateTimeGPS = mCurrentDateTimeGPS.trim();
			if(mCurrentDateTimeGPS.length() < 14)
			{
				this.mCurrentDateTimeGPS = addSpaceRight(mCurrentDateTimeGPS, (14 - mCurrentDateTimeGPS.length()), ' ');
			}
			else if(mCurrentDateTimeGPS.length()==14)
			{
				this.mCurrentDateTimeGPS = mCurrentDateTimeGPS;
			}			
		}
	}
	public String getmMRIDGPS() {
		return mMRIDGPS;
	}
	public void setmMRIDGPS(String mMRIDGPS) {
		if(mMRIDGPS == null)
		{
			this.mMRIDGPS = addSpaceRight("", 11, ' ');
		}
		else
		{
			mMRIDGPS = mMRIDGPS.trim();
			if(mMRIDGPS.length() < 11)
			{
				this.mMRIDGPS = addSpaceRight(mMRIDGPS, (11 - mMRIDGPS.length()), ' ');
			}
			else if(mMRIDGPS.length()==11)
			{
				this.mMRIDGPS = mMRIDGPS;
			}			
		}
	}
	public String getmLatitudeGPS() {
		return mLatitudeGPS;
	}
	public void setmLatitudeGPS(String mLatitudeGPS) {
		if(mLatitudeGPS == null || mLatitudeGPS.equals("null"))
		{
			this.mLatitudeGPS = addSpaceRight("", 15, ' ');
		}
		else if(mLatitudeGPS.trim().equals("0.0"))
		{
			this.mLatitudeGPS = addSpaceRight("", 15, ' ');
		}
		else
		{
			if(mLatitudeGPS.length() < 15)
			{
				this.mLatitudeGPS = addSpaceRight(mLatitudeGPS, (15 - mLatitudeGPS.length()), ' ');
			}
			else if(mLatitudeGPS.length() == 15)
			{
				this.mLatitudeGPS = mLatitudeGPS;
			}
			else
			{
				this.mLatitudeGPS = mLatitudeGPS.substring(0, 15);
			}
		}
	}
	public String getmLongitudeGPS() {
		return mLongitudeGPS;
	}	
	public void setmLongitudeGPS(String mLongitudeGPS) {
		if(mLongitudeGPS == null || mLongitudeGPS.equals("null"))
		{
			this.mLongitudeGPS = addSpaceRight("", 15, ' ');
		}
		else if(mLongitudeGPS.trim().equals("0.0"))
		{
			this.mLongitudeGPS = addSpaceRight("", 15, ' ');
		}
		else
		{
			if(mLongitudeGPS.length() < 15)
			{
				this.mLongitudeGPS = addSpaceRight(mLongitudeGPS, (15 - mLongitudeGPS.length()), ' ');
			}
			else if(mLongitudeGPS.length() == 15)
			{
				this.mLongitudeGPS = mLongitudeGPS;
			}
			else
			{
				this.mLongitudeGPS = mLongitudeGPS.substring(0, 15);
			}
		}
	}


	//End GPS Details----------------------------------------------------------------------------------------------------





	public String addSpace(String str, int count, char spacingChar) 
	{
		int k = 0;
		while(k < count)
		{
			//str = str + spacingChar;
			str = spacingChar + str;
			k++;
		}
		return str;
	}
	public String addSpaceRight(String str, int count, char spacingChar) 
	{
		int k = 0;
		while(k < count)
		{
			//str = str + spacingChar;
			str =  str + spacingChar;
			k++;
		}
		return str;
	}
	public String GetTwoDecimalPlace(String str)
	{
		//if(str == null)
		if(str == null || str.equals("null"))//Nitish 25-01-2017
		{
			str = "0.00";
		}
		if(str.trim().length() > 0)
		{
			if(str.indexOf(".") == -1)//if . not there then add .00 to the string
			{
				str = str + ".00";
			}
			else
			{
				String[] dotStr = str.split("\\.");
				if(dotStr.length > 0)
				{
					if(dotStr.length == 1)
					{
						str = dotStr[0] + ".00";
					}
					else
					{
						if(dotStr[1].length() == 0)
						{
							str = str + "00";
						}
						else if(dotStr[1].length() == 1)
						{
							str = str + "0";
						}
						else if(dotStr[1].length() > 2)
						{
							str = dotStr[0] + "." + dotStr[1].substring(0, 2);
						}
					}
				}
			}
		}
		else
		{
			str = "0.00";
		}
		return str;
	}
	//GET and SET for ForMonth=================================
	public String getmForMonth() {
		return mForMonth;
	}
	public void setmForMonth(String mForMonth) 
	{
		if(mForMonth == null)
		{
			this.mForMonth = addSpace("", 4, ' ');
		}
		else
		{
			if(mForMonth.length() < 4)
			{
				this.mForMonth = addSpace(mForMonth, (4 - mForMonth.length()), ' ');
			}
			else if(mForMonth.length()==4)
			{
				this.mForMonth = mForMonth;
			}
		}
	}
	//END GET and SET for ForMonth=============================

	//GET and SET for BillDate=================================
	public String getmBillDate() {
		return mBillDate;
	}
	public void setmBillDate(String mBillDate) {
		if(mBillDate == null)
		{
			this.mBillDate = addSpace("", 8, ' ');
		}
		else
		{
			if(mBillDate.length() < 8)
			{
				this.mBillDate = addSpace(mBillDate, (8 - mBillDate.length()), ' ');
			}
			else if(mBillDate.length() == 8)
			{
				this.mBillDate = mBillDate;
			}
		}
	}
	//END GET and SET for BillDate=============================

	//GET and SET for SubId====================================
	public String getmSubId() {
		return mSubId;
	}
	public void setmSubId(String mSubId) {
		if(mSubId == null)
		{
			this.mSubId = addSpace("", 2, ' ');
		}
		else
		{
			if(mSubId.length() < 2)
			{
				this.mSubId = addSpace(mSubId, (2 - mSubId.length()), ' ');
			}
			else if(mSubId.length() == 2)
			{
				this.mSubId = mSubId;
			}
		}
	}
	//END GET and SET for SubId================================

	//GET and SET for mConnectionNo============================
	public String getmConnectionNo() {
		return mConnectionNo;
	}
	public void setmConnectionNo(String mConnectionNo) {
		if(mConnectionNo == null)
		{
			this.mConnectionNo = addSpace("", 10, ' ');
		}
		else
		{
			if(mConnectionNo.length() < 10)
			{
				this.mConnectionNo = addSpace(mConnectionNo, (10 - mConnectionNo.length()), ' ');
			}
			else if(mConnectionNo.length() == 10)
			{
				this.mConnectionNo = mConnectionNo;
			}
		}
	}
	//END GET and SET for mConnectionNo========================

	//GET and SET for mTourplanId==============================
	public String getmTourplanId() {
		return mTourplanId;
	}
	public void setmTourplanId(String mTourplanId) {
		if(mTourplanId == null)
		{
			this.mTourplanId = addSpace("", 9, ' ');
		}
		else
		{
			if(mTourplanId.length() < 9)
			{
				this.mTourplanId = addSpace(mTourplanId, (9 - mTourplanId.length()), ' ');
			}
			else if(mTourplanId.length() == 9)
			{
				this.mTourplanId = mTourplanId;
			}
		}
	}
	//END GET and SET for mTourplanId==========================

	//GET and SET for mPF======================================
	public String getmPF() 
	{
		return mPF;
	}
	public void setmPF(String mPF) 
	{
		if(mPF.trim().length() < 3)
		{
			if(mPF.trim().length() == 0)
			{
				this.mPF = ".00";
			}
			else 
			{
				this.mPF = addSpace(mPF.trim(), (3 - mPF.trim().length()), ' ');
			}
		}
		else if(mPF.trim().length() == 3)
		{
			this.mPF = mPF.trim();
		}		
		else//Added by Tamilselvan on 29-03-2014
		{
			if(mPF.equals("null"))
			{
				this.mPF = ".00";
			}
			else
			{
				String[] str = mPF.split("\\.");
				this.mPF = "."+str[1].substring(0, 2).toString();//Nitish 20-11-2014
			}
		}
	}
	//END GET and SET for mPF==================================

	//GET and SET for mSancLoad================================
	public String getmSancLoad() {
		return mSancLoad;
	}
	public void setmSancLoad(String mSancLoad) {
		if(mSancLoad == null)
		{
			this.mSancLoad = addSpace("", 5, ' ');
		}
		else
		{
			if(mSancLoad.length() < 5)
			{
				this.mSancLoad = addSpace(mSancLoad, (5 - mSancLoad.length()), ' ');
			}
			else if(mSancLoad.length() == 5)
			{
				this.mSancLoad = mSancLoad;
			}
		}
	}
	//END GET and SET for mSancLoad============================

	//GET and SET for mDlCount=================================
	/*public String getmDlCount() {
		return mDlCount;
	}
	public void setmDlCount(String mDlCount) {
		if(mDlCount == null)
		{
			this.mDlCount = addSpace("", 1, ' ');
		}
		else
		{
			if(mDlCount.length() < 1)
			{
				this.mDlCount = addSpace(mDlCount, (1 - mDlCount.length()), ' ');
			}
			else if(mDlCount.length() == 1)
			{
				this.mDlCount = mDlCount;
			}
		}
	}*/
	//END GET and SET for mDlCount=============================

	//Start 23-03-2017

	public String getmDlCount() {
		return mDlCount;
	}
	public void setmDlCount(String mDlCount) {
		if(mDlCount == null)
		{
			this.mDlCount = addSpace("", 1, ' ');
		}
		else
		{
			if(mDlCount.length() < 1)
			{
				this.mDlCount = addSpace(mDlCount, (1 - mDlCount.length()), ' ');
			}
			else if(mDlCount.length() == 1)
			{
				this.mDlCount = mDlCount;
			}
			else if(mDlCount.length() > 1)
			{
				this.mDlCount = "1";
			}
		}
	}
	//END 23-03-2017 for mDlCount=============================	
	//GET and SET for mBillFor=================================
	public String getmBillFor() {
		return mBillFor;
	}
	public void setmBillFor(String mBillFor) {
		if(mBillFor == null)
		{
			this.mBillFor = addSpace("", 1, ' ');
		}
		else
		{
			if(mBillFor.length() < 1)
			{
				this.mBillFor = addSpace(mBillFor, (1 - mBillFor.length()), ' ');
			}
			else if(mBillFor.length() == 1)
			{
				this.mBillFor = mBillFor;
			}
		}
	}
	//END GET and SET for mBillFor=============================

	//GET and SET for mBlCnt===================================
	public String getmBlCnt() {
		return mBlCnt;
	}
	public void setmBlCnt(String mBlCnt) {
		if(mBlCnt == null)
		{
			this.mBlCnt = addSpace("", 1, ' ');
		}
		else
		{
			if(mBlCnt.length() < 1)
			{
				this.mBlCnt = addSpace(mBlCnt, (1 - mBlCnt.length()), ' ');
			}
			else if(mBlCnt.length() == 1)
			{
				this.mBlCnt = mBlCnt;
			}
		}
	}
	//END GET and SET for mBlCnt===============================

	//GET and SET for mBlCnt===================================
	public String getmTvmMtr() {
		return mTvmMtr;
	}
	public void setmTvmMtr(String mTvmMtr) {
		if(mTvmMtr == null)
		{
			this.mTvmMtr = addSpace("", 1, ' ');
		}
		else
		{
			if(mBlCnt.length() < 1)
			{
				this.mBlCnt = addSpace(mBlCnt, (1 - mBlCnt.length()), ' ');
			}
			else if(mBlCnt.length() == 1)
			{
				this.mTvmMtr = mTvmMtr;
			}
		}
	}
	//END GET and SET for mBlCnt===============================

	//GET and SET for mBlCnt===================================
	public String getmPreRead() {
		return mPreRead;
	}
	public void setmPreRead(String mPreRead) {
		if(mPreRead == null)
		{
			this.mPreRead = addSpace("", 12, ' ');
		}
		else
		{
			mPreRead = GetTwoDecimalPlace(mPreRead.trim());
			if(mPreRead.trim().length() < 12)
			{
				this.mPreRead = addSpace(mPreRead, (12 - mPreRead.trim().length()), ' ');
			}
			else if(mPreRead.trim().length() == 12)
			{
				this.mPreRead = mPreRead.trim();
			}
			else if(mPreRead.trim().length() > 12)//23-02-2017
			{
				this.mPreRead = mPreRead.trim().substring(0, 12);
			}
		}
	}
	//END GET and SET for mBlCnt===============================

	//GET and SET for mBlCnt===================================
	public String getmStatus() {
		return mStatus;
	}
	public void setmStatus(String mStatus) {
		if(mStatus == null)
		{
			this.mStatus = addSpace("", 2, ' ');
		}
		else
		{
			if(mStatus.length() < 2)
			{
				this.mStatus = addSpace(mStatus, (2 - mStatus.length()), '0');
			}
			else if(mStatus.length() == 2)
			{
				this.mStatus = mStatus;
			}
		}
	}
	//END GET and SET for mBlCnt===============================

	//GET and SET for mSpotSerialNo============================
	public String getmSpotSerialNo() {
		return mSpotSerialNo;
	}

	public void setmSpotSerialNo(String mSpotSerialNo) {
		if(mSpotSerialNo == null)
		{
			this.mSpotSerialNo = addSpace("", 5, ' ');
		}
		else
		{
			if(mSpotSerialNo.length() < 5)
			{
				this.mSpotSerialNo = addSpace(mSpotSerialNo, (5 - mSpotSerialNo.length()), '0');
			}
			else if(mSpotSerialNo.length() == 5)
			{
				this.mSpotSerialNo = mSpotSerialNo;
			}
		}
	}
	//END GET and SET for mSpotSerialNo========================

	//GET and SET for munits===================================
	public String getMunits() {
		return munits;
	}
	public void setMunits(String munits) {
		if(munits == null)
		{
			this.munits = addSpace("", 10, ' ');
		}
		else
		{
			munits = GetTwoDecimalPlace(munits.trim());
			if(munits.trim().length() < 10)
			{
				this.munits = addSpace(munits.trim(), (10 - munits.trim().length()), ' ');
			}
			else if(munits.trim().length() == 10)
			{
				this.munits = munits.trim();
			}
			else if(munits.trim().length() > 10) //23-02-2017
			{
				this.munits = munits.substring(0,10);
			}
		}
	}
	//END GET and SET for munits===============================

	//GET and SET for mTFc=====================================
	public String getmTFc() {
		return mTFc;
	}
	public void setmTFc(String mTFc) {
		if(mTFc == null)
		{
			this.mTFc = addSpace("", 12, ' ');
		}
		else
		{
			mTFc = GetTwoDecimalPlace(mTFc.trim());
			if(mTFc.trim().length() < 12)
			{
				this.mTFc = addSpace(mTFc.trim(), (12 - mTFc.trim().length()), ' ');
			}
			else if(mTFc.trim().length() == 12)
			{
				this.mTFc = mTFc.trim();
			}

		}
	}
	//END GET and SET for mTFc=================================

	//GET and SET for mTEc=====================================
	public String getmTEc() {
		return mTEc;
	}
	public void setmTEc(String mTEc) {
		if(mTEc == null)
		{
			this.mTEc = addSpace("", 12, ' ');
		}
		else
		{
			mTEc = GetTwoDecimalPlace(mTEc.trim());
			if(mTEc.length() < 12)
			{
				this.mTEc = addSpace(mTEc, (12 - mTEc.length()), ' ');
			}
			else if(mTEc.length() == 12)
			{
				this.mTEc = mTEc;
			}
			//23-02-2017
			else if(mTEc.length() >  12)
			{
				this.mTEc = mTEc.trim().substring(0, 12);
			}
		}
	}
	//END GET and SET for mTEc=================================

	//GET and SET for mFlReb===================================
	public String getmFlReb() {
		return mFlReb;
	}
	public void setmFlReb(String mFlReb) {
		if(mFlReb == null)
		{
			this.mFlReb = addSpace("", 9, ' ');
		}
		else
		{
			mFlReb = GetTwoDecimalPlace(mFlReb.trim());
			if(mFlReb.length() < 9)
			{
				this.mFlReb = addSpace(mFlReb, (9 - mFlReb.length()), ' ');
			}
			else if(mFlReb.length() == 9)
			{
				this.mFlReb = mFlReb;
			}
		}
	}
	//END GET and SET for mFlReb===============================

	//GET and SET for mBlCnt===================================
	public String getmEcReb() {
		return mEcReb;
	}
	public void setmEcReb(String mEcReb) {
		if(mEcReb == null)
		{
			this.mEcReb = addSpace("", 9, ' ');
		}
		else
		{
			mEcReb = GetTwoDecimalPlace(mEcReb.trim());
			if(mEcReb.length() < 9)
			{
				this.mEcReb = addSpace(mEcReb, (9 - mEcReb.length()), ' ');
			}
			else if(mEcReb.length() == 9)
			{
				this.mEcReb = mEcReb;
			}
		}
	}
	//END GET and SET for mBlCnt===============================

	//GET and SET for mTaxAmt==================================
	public String getmTaxAmt() {
		return mTaxAmt;
	}
	public void setmTaxAmt(String mTaxAmt) {
		if(mTaxAmt == null)
		{
			this.mTaxAmt = addSpace("", 10, ' ');
		}
		else
		{
			mTaxAmt = GetTwoDecimalPlace(mTaxAmt.trim());
			if(mTaxAmt.length() < 10)
			{
				this.mTaxAmt = addSpace(mTaxAmt, (10 - mTaxAmt.length()), ' ');
			}
			else if(mTaxAmt.length() == 10)
			{
				this.mTaxAmt = mTaxAmt;
			}
		}
	}
	//END GET and SET for mTaxAmt==============================

	//GET and SET for mPfPenAmt================================
	public String getmPfPenAmt() {
		return mPfPenAmt;
	}
	public void setmPfPenAmt(String mPfPenAmt) {
		if(mPfPenAmt == null)
		{
			this.mPfPenAmt = addSpace("", 11, ' ');
		}
		else
		{
			mPfPenAmt = GetTwoDecimalPlace(mPfPenAmt.trim());
			if(mPfPenAmt.length() < 11)
			{
				this.mPfPenAmt = addSpace(mPfPenAmt, (11 - mPfPenAmt.length()), ' ');
			}
			else if(mPfPenAmt.length() == 11)
			{
				this.mPfPenAmt = mPfPenAmt;
			}
		}
	}
	//END GET and SET for mPfPenAmt============================

	//GET and SET for mPenExLd=================================
	public String getmPenExLd() {
		return mPenExLd;
	}
	public void setmPenExLd(String mPenExLd) {
		if(mPenExLd == null)
		{
			this.mPenExLd = addSpace("", 11, ' ');
		}
		else
		{
			mPenExLd = GetTwoDecimalPlace(mPenExLd.trim());
			if(mPenExLd.length() < 11)
			{
				this.mPenExLd = addSpace(mPenExLd, (11 - mPenExLd.length()), ' ');
			}
			else if(mPenExLd.length() == 11)
			{
				this.mPenExLd = mPenExLd;
			}
		}
	}
	//END GET and SET for mPenExLd=============================

	//GET and SET for mHCReb===================================
	public String getmHCReb() {
		return mHCReb;
	}
	public void setmHCReb(String mHCReb) {
		if(mHCReb == null)
		{
			this.mHCReb = addSpace("", 9, ' ');
		}
		else
		{
			mHCReb = GetTwoDecimalPlace(mHCReb.trim());
			if(mHCReb.length() < 9)
			{
				this.mHCReb = addSpace(mHCReb, (9 - mHCReb.length()), ' ');
			}
			else if(mHCReb.length() == 9)
			{
				this.mHCReb = mHCReb;
			}
		}
	}
	//END GET and SET for mHCReb===============================

	//GET and SET for mHLReb===================================
	public String getmHLReb() {
		return mHLReb;
	}
	public void setmHLReb(String mHLReb) {
		if(mHLReb == null)
		{
			this.mHLReb = addSpace("", 9, ' ');
		}
		else
		{
			mHLReb = GetTwoDecimalPlace(mHLReb.trim());
			if(mHLReb.length() < 9)
			{
				this.mHLReb = addSpace(mHLReb, (9 - mHLReb.length()), ' ');
			}
			else if(mHLReb.length() == 9)
			{
				this.mHLReb = mHLReb;
			}
		}
	}
	//END GET and SET for mHLReb===============================

	//GET and SET for mCapReb==================================
	public String getmCapReb() {
		return mCapReb;
	}
	public void setmCapReb(String mCapReb) {
		if(mCapReb == null)
		{
			this.mCapReb = addSpace("", 9, ' ');
		}
		else
		{
			mCapReb = GetTwoDecimalPlace(mCapReb.trim());
			if(mCapReb.length() < 9)
			{
				this.mCapReb = addSpace(mCapReb, (9 - mCapReb.length()), ' ');
			}
			else if(mCapReb.length() == 9)
			{
				this.mCapReb = mCapReb;
			}
		}
	}
	//END GET and SET for mCapReb==============================

	//GET and SET for mExLoad==================================
	public String getmExLoad() {
		return mExLoad;
	}
	public void setmExLoad(String mExLoad) {
		if(mExLoad == null)
		{
			this.mExLoad = addSpace("", 9, ' ');
		}
		else
		{
			mExLoad = GetTwoDecimalPlace(mExLoad.trim());
			if(mExLoad.length() < 9)
			{
				this.mExLoad = addSpace(mExLoad, (9 - mExLoad.length()), ' ');
			}
			else if(mExLoad.length() == 9)
			{
				this.mExLoad = mExLoad;
			}
		}
	}
	//END GET and SET for mExLoad==============================

	//GET and SET for mDemandChrg==============================
	public String getmDemandChrg() {
		return mDemandChrg;
	}
	public void setmDemandChrg(String mDemandChrg) {
		if(mDemandChrg == null)
		{
			this.mDemandChrg = addSpace("", 11, ' ');
		}
		else
		{
			mDemandChrg = GetTwoDecimalPlace(mDemandChrg.trim());
			if(mDemandChrg.length() < 11)
			{
				this.mDemandChrg = addSpace(mDemandChrg, (11 - mDemandChrg.length()), ' ');
			}
			else if(mDemandChrg.length() == 11)
			{
				this.mDemandChrg = mDemandChrg;
			}
		}
	}
	//END GET and SET for mDemandChrg==========================

	//GET and SET for mAccdRdg_rtn=============================
	public String getmAccdRdg_rtn() {
		return mAccdRdg_rtn;
	}
	public void setmAccdRdg_rtn(String mAccdRdg_rtn) {
		if(mAccdRdg_rtn == null)
		{
			this.mAccdRdg_rtn = addSpace("", 12, ' ');
		}
		else
		{
			if(mAccdRdg_rtn.length() < 12)
			{
				this.mAccdRdg_rtn = addSpace(mAccdRdg_rtn, (12 - mAccdRdg_rtn.length()), ' ');
			}
			else if(mAccdRdg_rtn.length() == 12)
			{
				this.mAccdRdg_rtn = mAccdRdg_rtn;
			}
		}
	}
	//END GET and SET for mAccdRdg_rtn=========================

	//GET and SET for mkVAFR===================================
	public String getMkVAFR() {
		return mkVAFR;
	}
	public void setMkVAFR(String mkVAFR) {
		if(mkVAFR == null)
		{
			this.mkVAFR = addSpace("", 12, ' ');
		}
		else
		{
			mkVAFR = GetTwoDecimalPlace(mkVAFR.trim());
			if(mkVAFR.length() < 12)
			{
				this.mkVAFR = addSpace(mkVAFR, (12 - mkVAFR.length()), ' ');
			}
			else if(mkVAFR.length() == 12)
			{
				this.mkVAFR = mkVAFR;
			}
		}
	}
	//END GET and SET for mkVAFR===============================

	//GET and SET for mAbFlag==================================
	public String getmAbFlag() {
		return mAbFlag;
	}
	public void setmAbFlag(String mAbFlag) {
		if(mAbFlag == null)
		{
			this.mAbFlag = addSpace("", 1, ' ');
		}
		else
		{
			if(mAbFlag.length() < 1)
			{
				this.mAbFlag = addSpace(mAbFlag, (1 - mAbFlag.length()), ' ');
			}
			else if(mAbFlag.length() == 1)
			{
				this.mAbFlag = mAbFlag;
			}
		}
	}
	//END GET and SET for mAbFlag==============================

	//GET and SET for mBjKj2Lt2================================
	public String getmBjKj2Lt2() {
		return mBjKj2Lt2;
	}
	public void setmBjKj2Lt2(String mBjKj2Lt2) {
		if(mBjKj2Lt2 == null)
		{
			this.mBjKj2Lt2 = addSpace("", 1, ' ');
		}
		else
		{
			if(mBjKj2Lt2.length() < 1)
			{
				this.mBjKj2Lt2 = addSpace(mBjKj2Lt2, (1 - mBjKj2Lt2.length()), ' ');
			}
			else if(mBjKj2Lt2.length() == 1)
			{
				this.mBjKj2Lt2 = mBjKj2Lt2;
			}
		}
	}
	//END GET and SET for mBjKj2Lt2============================

	//GET and SET for mRemarks=================================
	public String getmRemarks() {
		return mRemarks;
	}
	public void setmRemarks(String mRemarks) {
		if(mRemarks == null)
		{
			this.mRemarks = addSpace("", 30, ' ');
		}
		else
		{
			if(mRemarks.length() < 30)
			{
				this.mRemarks = addSpace(mRemarks, (30 - mRemarks.length()), ' ');
			}
			else if(mRemarks.length() == 30)
			{
				this.mRemarks = mRemarks;
			}
			else  //30-08-2016
			{				
				this.mRemarks = mRemarks.substring(0, 30);

			}
		}
	}
	//END GET and SET for mRemarks=============================

	//GET and SET for mGoKPayable==============================
	public String getmGoKPayable() {
		return mGoKPayable;
	}
	public void setmGoKPayable(String mGoKPayable) {
		if(mGoKPayable == null)
		{
			this.mGoKPayable = addSpace("", 9, ' ');
		}
		else
		{
			mGoKPayable = GetTwoDecimalPlace(mGoKPayable.trim());
			if(mGoKPayable.length() < 9)
			{
				this.mGoKPayable = addSpace(mGoKPayable, (9 - mGoKPayable.length()), ' ');
			}
			else if(mGoKPayable.length() == 9)
			{
				this.mGoKPayable = mGoKPayable;
			}
		}
	}
	//END GET and SET for mGoKPayable==========================

	//GET and SET for mIssueDateTime===========================
	public String getmIssueDateTime() {
		return mIssueDateTime;
	}
	public void setmIssueDateTime(String mIssueDateTime) {
		if(mIssueDateTime == null)
		{
			this.mIssueDateTime = addSpace("", 12, ' ');
		}
		else
		{
			if(mIssueDateTime.length() < 12)
			{
				this.mIssueDateTime = addSpace(mIssueDateTime, (12 - mIssueDateTime.length()), ' ');
			}
			else if(mIssueDateTime.length() == 12)
			{
				this.mIssueDateTime = mIssueDateTime;
			}
			else if(mIssueDateTime.length() > 12) //27-06-2020
			{
				this.mIssueDateTime = mIssueDateTime.substring(0,12);
			}
		}
	}
	//END GET and SET for mIssueDateTime=======================

	//GET and SET for mRecordDmnd==============================
	public String getmRecordDmnd() {
		return mRecordDmnd;
	}
	public void setmRecordDmnd(String mRecordDmnd) {
		if(mRecordDmnd == null)
		{
			this.mRecordDmnd = addSpace("", 6, ' ');
		}
		else
		{
			mRecordDmnd = GetTwoDecimalPlace(mRecordDmnd.trim());
			if(mRecordDmnd.length() < 6)
			{
				this.mRecordDmnd = addSpace(mRecordDmnd, (6 - mRecordDmnd.length()), ' ');
			}
			else if(mRecordDmnd.length() == 6)
			{
				this.mRecordDmnd = mRecordDmnd;
			}
		}
	}
	//END GET and SET for mRecordDmnd==========================

	//GET and SET for mKVA_Consumption=========================
	public String getmKVA_Consumption() {
		return mKVA_Consumption;
	}
	public void setmKVA_Consumption(String mKVA_Consumption) {
		if(mKVA_Consumption == null)
		{
			this.mKVA_Consumption = addSpace("", 6, ' ');
		}
		else
		{
			mKVA_Consumption = GetTwoDecimalPlace(mKVA_Consumption.trim());
			if(mKVA_Consumption.length() < 6)
			{
				this.mKVA_Consumption = addSpace(mKVA_Consumption, (6 - mKVA_Consumption.length()), ' ');
			}
			else if(mKVA_Consumption.length() == 6)
			{
				this.mKVA_Consumption = mKVA_Consumption;
			}
		}
	}
	//END GET and SET for mKVA_Consumption=====================

	//GET and SET for mKVAAssd_Cons============================
	public String getmKVAAssd_Cons() {
		return mKVAAssd_Cons;
	}
	public void setmKVAAssd_Cons(String mKVAAssd_Cons) {
		if(mKVAAssd_Cons == null)
		{
			this.mKVAAssd_Cons = addSpace("", 10, ' ');
		}
		else
		{
			mKVAAssd_Cons = GetTwoDecimalPlace(mKVAAssd_Cons.trim());
			if(mKVAAssd_Cons.length() < 10)
			{
				this.mKVAAssd_Cons = addSpace(mKVAAssd_Cons, (10 - mKVAAssd_Cons.length()), ' ');
			}
			else if(mKVAAssd_Cons.length() == 10)
			{
				this.mKVAAssd_Cons = mKVAAssd_Cons;
			}
		}
	}
	//END GET and SET for mKVAAssd_Cons========================

	//GET and SET for mTariffCode==============================
	public String getmTariffCode() {
		return mTariffCode;
	}
	public void setmTariffCode(String mTariffCode) {
		if(mTariffCode == null)
		{
			this.mTariffCode = addSpace("", 3, ' ');
		}
		else
		{
			if(mTariffCode.length() < 3)
			{
				this.mTariffCode = addSpace(mTariffCode, (3 - mTariffCode.length()), ' ');
			}
			else if(mTariffCode.length() == 3)
			{
				this.mTariffCode = mTariffCode;
			}
		}
	}
	//END GET and SET for mTariffCode==========================

	//GET and SET for mLinMin==================================
	public String getmLinMin() {
		return mLinMin;
	}
	public void setmLinMin(String mLinMin) {
		if(mLinMin == null)
		{
			this.mLinMin = addSpace("", 5, ' ');
		}
		else
		{
			mLinMin = GetTwoDecimalPlace(mLinMin.trim());
			if(mLinMin.length() < 5)
			{
				this.mLinMin = addSpace(mLinMin, (5 - mLinMin.length()), ' ');
			}
			else if(mLinMin.length() == 5)
			{
				this.mLinMin = mLinMin;
			}
		}
	}
	//END GET and SET for mLinMin==============================

	//GET and SET for mPrevRdg=================================
	public String getmPrevRdg() {
		return mPrevRdg;
	}
	public void setmPrevRdg(String mPrevRdg) {
		if(mPrevRdg == null)
		{
			this.mPrevRdg = addSpace("", 12, ' ');
		}
		else
		{
			mPrevRdg = GetTwoDecimalPlace(mPrevRdg.trim());
			if(mPrevRdg.length() < 12)
			{
				this.mPrevRdg = addSpace(mPrevRdg, (12 - mPrevRdg.length()), ' ');
			}
			else if(mPrevRdg.length() == 12)
			{
				this.mPrevRdg = mPrevRdg;
			}
		}
	}
	//END GET and SET for mPrevRdg=============================

	//GET and SET for mRRNo====================================
	public String getmRRNo() {
		return mRRNo;
	}

	//26-09-2015
	/*public void setmRRNo(String mRRNo) {
		if(mRRNo == null)
		{
			this.mRRNo = addSpace("", 13, ' ');
		}
		else
		{
			if(mRRNo.length() < 13)
			{
				this.mRRNo = addSpace(mRRNo, (13 - mRRNo.length()), ' ');
			else if(mRRNo.length() == 13)
			{
				this.mRRNo = mRRNo;
			}
		}
	}*/
	//26-09-2015
	public void setmRRNo(String mRRNo) {
		if(mRRNo == null)
		{
			this.mRRNo = addSpace("", 21, ' ');
		}
		else
		{
			if(mRRNo.length() < 21)
			{
				this.mRRNo = addSpace(mRRNo, (21 - mRRNo.length()), ' ');
			}
			else if(mRRNo.length() == 21)
			{
				this.mRRNo = mRRNo;
			}
		}
	}
	//END GET and SET for mRRNo================================

	//GET and SET for mBillTotal===============================
	public String getmBillTotal() {
		return mBillTotal;
	}
	public void setmBillTotal(String mBillTotal) {
		if(mBillTotal == null)
		{
			this.mBillTotal = addSpace("", 11, ' ');
		}
		else
		{
			mBillTotal = GetTwoDecimalPlace(mBillTotal.trim());
			if(mBillTotal.length() < 11)
			{
				this.mBillTotal = addSpace(mBillTotal, (11 - mBillTotal.length()), ' ');
			}
			else if(mBillTotal.length() == 11)
			{
				this.mBillTotal = mBillTotal;
			}
			//23-02-2017
			else if(mBillTotal.length() > 11)
			{
				this.mBillTotal = mBillTotal.substring(0, 11);
			}
		}
	}
	//END GET and SET for mBillTotal===========================

	//GET and SET for mSBMNumber===============================
	public String getmSBMNumber() {
		return mSBMNumber;
	}
	public void setmSBMNumber(String mSBMNumber) {
		if(mSBMNumber == null)
		{
			this.mSBMNumber = addSpace("", 10, ' ');
		}
		else
		{
			if(mSBMNumber.length() < 10)
			{
				this.mSBMNumber = addSpace(mSBMNumber, (10 - mSBMNumber.length()), ' ');
			}
			else if(mSBMNumber.length() == 10)
			{
				this.mSBMNumber = mSBMNumber;
			}
			else if(mSBMNumber.length() > 10) //26-09-2015
			{
				this.mSBMNumber = mSBMNumber.substring(5, 15);
			}
		}
	}
	//END GET and SET for mSBMNumber===========================

	//GET and SET for mLocationCode============================
	public String getmLocationCode() {
		return mLocationCode;
	}
	public void setmLocationCode(String mLocationCode) {
		if(mLocationCode == null)
		{
			this.mLocationCode = addSpace("", 10, ' ');
		}
		else
		{
			if(mLocationCode.length() < 10)
			{
				this.mLocationCode = addSpace(mLocationCode, (10 - mLocationCode.length()), ' ');
			}
			else if(mLocationCode.length() == 10)
			{
				this.mLocationCode = mLocationCode;
			}
		}
	}
	//END GET and SET for mLocationCode========================

	//GET and SET for mEC_1====================================
	public String getmEC_1() {
		return mEC_1;
	}
	public void setmEC_1(String mEC_1) {
		if(mEC_1 == null)
		{
			this.mEC_1 = addSpace("", 12, ' ');
		}
		else
		{
			mEC_1 = GetTwoDecimalPlace(mEC_1.trim());
			if(mEC_1.length() < 12)
			{
				this.mEC_1 = addSpace(mEC_1, (12 - mEC_1.length()), ' ');
			}
			else if(mEC_1.length() == 12)
			{
				this.mEC_1 = mEC_1;
			}
			//23-02-2017
			else if(mEC_1.length() > 12)
			{
				this.mEC_1 = mEC_1.substring(0,12);
			}
		}
	}
	//END GET and SET for mEC_1================================

	//GET and SET for mEC_2====================================
	public String getmEC_2() {
		return mEC_2;
	}
	public void setmEC_2(String mEC_2) {
		if(mEC_2 == null)
		{
			this.mEC_2 = addSpace("", 12, ' ');
		}
		else
		{
			mEC_2 = GetTwoDecimalPlace(mEC_2.trim());
			if(mEC_2.length() < 12)
			{
				this.mEC_2 = addSpace(mEC_2, (12 - mEC_2.length()), ' ');
			}
			else if(mEC_2.length() == 12)
			{
				this.mEC_2 = mEC_2;
			}
			//23-02-2017
			else if(mEC_2.length() > 12)
			{
				this.mEC_2 = mEC_2.substring(0,12);
			}
		}
	}
	//END GET and SET for mEC_2================================

	//GET and SET for mEC_3====================================
	public String getmEC_3() {
		return mEC_3;
	}
	public void setmEC_3(String mEC_3) {
		if(mEC_3 == null)
		{
			this.mEC_3 = addSpace("", 12, ' ');
		}
		else
		{
			mEC_3 = GetTwoDecimalPlace(mEC_3.trim());
			if(mEC_3.length() < 12)
			{
				this.mEC_3 = addSpace(mEC_3, (12 - mEC_3.length()), ' ');
			}
			else if(mEC_3.length() == 12)
			{
				this.mEC_3 = mEC_3;
			}
			//23-02-2017
			else if(mEC_3.length() > 12)
			{
				this.mEC_3 = mEC_3.substring(0,12);
			}
		}
	}
	//END GET and SET for mEC_3================================

	//GET and SET for mEC_4====================================
	public String getmEC_4() {
		return mEC_4;
	}
	public void setmEC_4(String mEC_4) {
		if(mEC_4 == null)
		{
			this.mEC_4 = addSpace("", 12, ' ');
		}
		else
		{
			mEC_4 = GetTwoDecimalPlace(mEC_4.trim());
			if(mEC_4.length() < 12)
			{
				this.mEC_4 = addSpace(mEC_4, (12 - mEC_4.length()), ' ');
			}
			else if(mEC_4.length() == 12)
			{
				this.mEC_4 = mEC_4;
			}
			//23-02-2017
			else if(mEC_4.length() > 12)
			{
				this.mEC_4 = mEC_4.substring(0,12);
			}
		}
	}
	//END GET and SET for mEC_4================================

	//GET and SET for mMobileNo================================
	public String getmMobileNo() {
		return mMobileNo;
	}
	public void setmMobileNo(String mMobileNo) {
		if(mMobileNo == null)
		{
			this.mMobileNo = addSpace("", 17, ' ');
		}
		else
		{
			if(mMobileNo.length() < 17)
			{
				this.mMobileNo = addSpace(mMobileNo, (17 - mMobileNo.length()), ' ');
			}
			else if(mMobileNo.length() == 17)
			{
				this.mMobileNo = mMobileNo;
			}
			else if(mMobileNo.length() > 17)
			{
				this.mMobileNo = mMobileNo.substring(0, 17);
			}
		}
		//this.mMobileNo = "00000000000000000";
	}
	//END GET and SET for mMobileNo============================

	//GET and SET for mMtrDisFlag==============================
	public String getmMtrDisFlag() {
		return mMtrDisFlag;
	}
	public void setmMtrDisFlag(String mMtrDisFlag) {
		if(mMtrDisFlag == null)
		{
			this.mMtrDisFlag = addSpace("", 1, ' ');
		}
		else
		{
			if(mMtrDisFlag.length() < 1)
			{
				this.mMtrDisFlag = addSpace(mMtrDisFlag, (1 - mMtrDisFlag.length()), ' ');
			}
			else if(mMtrDisFlag.length() == 1)
			{
				this.mMtrDisFlag = mMtrDisFlag;
			}
		}
	}
	//END GET and SET for mMtrDisFlag==========================

	//GET and SET for mMeter_type==============================
	public String getmMeter_type() {
		return mMeter_type;
	}
	public void setmMeter_type(String mMeter_type) {
		if(mMeter_type == null)
		{
			this.mMeter_type = addSpace("", 1, ' ');
		}
		else
		{
			if(mMeter_type.length() < 1)
			{
				this.mMeter_type = addSpace(mMeter_type, (1 - mMeter_type.length()), ' ');
			}
			else if(mMeter_type.length() == 1)
			{
				this.mMeter_type = mMeter_type;
			}
			else if(mMeter_type.length() > 1)
			{
				this.mMeter_type = "0";
			}
		}
	}
	//END GET and SET for mMeter_type==========================

	//GET and SET for mBillTotal===============================
	public String getmMeter_serialno() {
		return mMeter_serialno;
	}
	public void setmMeter_serialno(String mMeter_serialno) {
		if(mMeter_serialno == null)
		{
			this.mMeter_serialno = addSpace("", 10, ' ');
		}
		else
		{
			if(mMeter_serialno.length() < 10)
			{
				this.mMeter_serialno = addSpace(mMeter_serialno, (10 - mMeter_serialno.length()), ' ');
			}
			else if(mMeter_serialno.length() == 10)
			{
				this.mMeter_serialno = mMeter_serialno;
			}
		}
	}
	//END GET and SET for mBillTotal===========================

	//GET and SET for mTcname_present==========================
	public String getmTcname_present() {
		return mTcname_present;
	}
	public void setmTcname_present(String mTcname_present) {
		if(mTcname_present == null)
		{
			this.mTcname_present = addSpace("", 1, ' ');
		}
		else
		{
			if(mTcname_present.length() < 1)
			{
				this.mTcname_present = addSpace(mTcname_present, (1 - mTcname_present.length()), ' ');
			}
			else if(mTcname_present.length() == 1)
			{
				this.mTcname_present = mTcname_present;
			}
		}
	}
	//END GET and SET for mTcname_present======================

	//GET and SET for mMeter_present_serialno==================
	public String getmMeter_present_serialno() {
		return mMeter_present_serialno;
	}
	public void setmMeter_present_serialno(String mMeter_present_serialno) {
		if(mMeter_present_serialno == null)
		{
			this.mMeter_present_serialno = addSpace("", 1, ' ');
		}
		else
		{
			if(mMeter_present_serialno.length() < 1)
			{
				this.mMeter_present_serialno = addSpace(mMeter_present_serialno, (1 - mMeter_present_serialno.length()), ' ');
			}
			else if(mMeter_present_serialno.length() == 1)
			{
				this.mMeter_present_serialno = mMeter_present_serialno;
			}
		}
	}
	//END GET and SET for mMeter_present_serialno==============

	//GET and SET for mMeter_not_visible=======================
	public String getmMeter_not_visible() {
		return mMeter_not_visible;
	}
	public void setmMeter_not_visible(String mMeter_not_visible) {
		if(mMeter_not_visible == null)
		{
			this.mMeter_not_visible = addSpace("", 1, ' ');
		}
		else
		{
			if(mMeter_not_visible.length() < 1)
			{
				this.mMeter_not_visible = addSpace(mMeter_not_visible, (1 - mMeter_not_visible.length()), ' ');
			}
			else if(mMeter_not_visible.length() == 1)
			{
				this.mMeter_not_visible = mMeter_not_visible;
			}
		}
	}
	//END GET and SET for mMeter_not_visible===================

	//GET and SET for mVer===============================
	public String getmVer() {
		return mVer;
	}

	public void setmVer(String mVer) {
		if(mVer == null)
		{
			this.mVer = addSpace("", 3, ' ');
		}
		else
		{
			if(mVer.length() < 3)
			{
				this.mVer = addSpace(mVer, (3 - mVer.length()), ' ');
			}
			else if(mVer.length() == 3)
			{
				this.mVer = mVer;
			}
		}
	}
	//END GET and SET for mVer===========================
	public String getmLatitude() {
		return mLatitude;
	}
	public void setmLatitude(String mLatitude) {
		if(mLatitude == null || mLatitude.equals("null"))
		{
			this.mLatitude = addSpaceRight("", 9, ' ');
		}
		else if(mLatitude.trim().equals("0.0"))
		{
			this.mLatitude = addSpaceRight("", 9, ' ');
		}
		else
		{
			if(mLatitude.length() < 9)
			{
				this.mLatitude = addSpaceRight(mLatitude, (9 - mLatitude.length()), ' ');
			}
			else if(mLatitude.length() == 9)
			{
				this.mLatitude = mLatitude;
			}
			else
			{
				this.mLatitude = mLatitude.substring(0, 9);
			}
		}
	}
	public String getmLongitude() {
		return mLongitude;
	}
	public void setmLongitude(String mLongitude) {
		if(mLongitude == null || mLongitude.equals("null"))
		{
			this.mLongitude = addSpaceRight("", 9, ' ');
		}
		else if(mLongitude.trim().equals("0.0"))
		{
			this.mLongitude = addSpaceRight("", 9, ' ');
		}
		else
		{
			if(mLongitude.length() < 9)
			{
				this.mLongitude = addSpaceRight(mLongitude, (9 - mLongitude.length()), ' ');
			}
			else if(mLongitude.length() == 9)
			{
				this.mLongitude = mLongitude;
			}
			else
			{
				this.mLongitude = mLongitude.substring(0, 9);
			}
		}
	}

	public String getmAadharNo() {
		return mAadharNo;
	}

	public void setmAadharNo(String mAadharNo) {
		if(mAadharNo == null)
		{
			this.mAadharNo = addSpace("", 12, ' ');
		}
		else
		{
			if(mAadharNo.length() < 12)
			{
				this.mAadharNo = addSpace(mAadharNo, (12 - mAadharNo.length()), ' ');
			}
			else if(mAadharNo.length() == 12)
			{
				this.mAadharNo = mAadharNo;
			}
		}
	}

	public String getmVoterIdNo() {
		return mVoterIdNo;
	}

	public void setmVoterIdNo(String mVoterIdNo) {
		if(mVoterIdNo == null)
		{
			this.mVoterIdNo = addSpace("", 12, ' ');
		}
		else
		{
			if(mVoterIdNo.length() < 12)
			{
				this.mVoterIdNo = addSpace(mVoterIdNo, (12 - mVoterIdNo.length()), ' ');
			}
			else if(mVoterIdNo.length() == 12)
			{
				this.mVoterIdNo = mVoterIdNo;
			}
		}
	}

	public String getmMtrMakeFlag() {
		return mMtrMakeFlag;
	}

	public void setmMtrMakeFlag(String mMtrMakeFlag) {
		if(mMtrMakeFlag == null)
		{
			this.mMtrMakeFlag = addSpace("", 2, ' ');
		}
		else
		{
			if(mMtrMakeFlag.length() < 2)
			{
				this.mMtrMakeFlag = addSpace(mMtrMakeFlag, (2 - mMtrMakeFlag.length()), ' ');
			}
			else if(mMtrMakeFlag.length() == 2)
			{
				this.mMtrMakeFlag = mMtrMakeFlag;
			}
		}
	}

	public String getmMtrBodySealFlag() {
		return mMtrBodySealFlag;
	}

	public void setmMtrBodySealFlag(String mMtrBodySealFlag) {
		if(mMtrBodySealFlag == null)
		{
			this.mMtrBodySealFlag = addSpace("", 1, ' ');
		}
		else
		{
			if(mMtrBodySealFlag.length() < 1)
			{
				this.mMtrBodySealFlag = addSpace(mMtrBodySealFlag, (1 - mMtrBodySealFlag.length()), ' ');
			}
			else if(mMtrBodySealFlag.length() == 1)
			{
				this.mMtrBodySealFlag = mMtrBodySealFlag;
			}
		}
	}

	public String getmMtrTerminalCoverFlag() {
		return mMtrTerminalCoverFlag;
	}

	public void setmMtrTerminalCoverFlag(String mMtrTerminalCoverFlag) {
		if(mMtrTerminalCoverFlag == null)
		{
			this.mMtrTerminalCoverFlag = addSpace("", 1, ' ');
		}
		else
		{
			if(mMtrTerminalCoverFlag.length() < 1)
			{
				this.mMtrTerminalCoverFlag = addSpace(mMtrTerminalCoverFlag, (1 - mMtrTerminalCoverFlag.length()), ' ');
			}
			else if(mMtrTerminalCoverFlag.length() == 1)
			{
				this.mMtrTerminalCoverFlag = mMtrTerminalCoverFlag;
			}
		}
	}

	public String getmMtrTerminalCoverSealedFlag() {
		return mMtrTerminalCoverSealedFlag;
	}

	public void setmMtrTerminalCoverSealedFlag(	String mMtrTerminalCoverSealedFlag) {
		if(mMtrTerminalCoverSealedFlag == null)
		{
			this.mMtrTerminalCoverSealedFlag = addSpace("", 1, ' ');
		}
		else
		{
			if(mMtrTerminalCoverSealedFlag.length() < 1)
			{
				this.mMtrTerminalCoverSealedFlag = addSpace(mMtrTerminalCoverSealedFlag, (1 - mMtrTerminalCoverSealedFlag.length()), ' ');
			}
			else if(mMtrTerminalCoverSealedFlag.length() == 1)
			{
				this.mMtrTerminalCoverSealedFlag = mMtrTerminalCoverSealedFlag;
			}
		}
	}

	////Nitish 15-04-2017
	//GET and SET for mEC_5====================================
	public String getmEC_5() {
		return mEC_5;
	}
	public void setmEC_5(String mEC_5) {
		if(mEC_5 == null)
		{
			this.mEC_5 = addSpace("", 12, ' ');
		}
		else
		{
			mEC_5 = GetTwoDecimalPlace(mEC_5.trim());
			if(mEC_5.length() < 12)
			{
				this.mEC_5 = addSpace(mEC_5, (12 - mEC_5.length()), ' ');
			}
			else if(mEC_5.length() == 12)
			{
				this.mEC_5 = mEC_5;
			}
			//23-02-2017
			else if(mEC_5.length() > 12)
			{
				this.mEC_5 = mEC_5.substring(0,12);
			}
		}
	}
	//END GET and SET for mEC_5================================
	//GET and SET for mEC_6====================================
	public String getmEC_6() {
		return mEC_6;
	}
	public void setmEC_6(String mEC_6) {
		if(mEC_6 == null)
		{
			this.mEC_6 = addSpace("", 12, ' ');
		}
		else
		{
			mEC_6 = GetTwoDecimalPlace(mEC_6.trim());
			if(mEC_6.length() < 12)
			{
				this.mEC_6 = addSpace(mEC_6, (12 - mEC_6.length()), ' ');
			}
			else if(mEC_6.length() == 12)
			{
				this.mEC_6 = mEC_6;
			}
			//23-02-2017
			else if(mEC_6.length() > 12)
			{
				this.mEC_6 = mEC_6.substring(0,12);
			}
		}
	}
	//END GET and SET for mEC_6================================
	//End Nitish 15-04-2017



	//Nitish 10-03-2021
	public String getmLTSlNo() {
		return mLTSlNo;
	}
	public void setmLTSlNo(String mLTSlNo) {
		if(mLTSlNo == null)
		{
			this.mLTSlNo = addSpace("", 15, ' ');
		}
		else
		{
			if(mLTSlNo.length() < 15)
			{
				this.mLTSlNo = addSpace(mLTSlNo, (15 - mLTSlNo.length()), ' ');
			}
			else if(mLTSlNo.length() == 15)
			{
				this.mLTSlNo = mLTSlNo;
			}
		}
	}
	public String getmLTReading() {
		return mLTReading;
	}
	public void setmLTReading(String mLTReading) {
		if(mLTReading == null)
		{
			this.mLTReading = addSpace("", 15, ' ');
		}
		else
		{
			if(mLTReading.length() < 15)
			{
				this.mLTReading = addSpace(mLTReading, (15 - mLTReading.length()), ' ');
			}
			else if(mLTReading.length() == 15)
			{
				this.mLTReading = mLTReading;
			}
		}
	}
	public String getmLTMD() {
		return mLTMD;
	}
	public void setmLTMD(String mLTMD) {
		if(mLTMD == null)
		{
			this.mLTMD = addSpace("", 15, ' ');
		}
		else
		{
			if(mLTMD.length() < 15)
			{
				this.mLTMD = addSpace(mLTMD, (15 - mLTMD.length()), ' ');
			}
			else if(mLTMD.length() == 15)
			{
				this.mLTMD = mLTMD;
			}
		}
	}
	public String getmLTPF() {
		return mLTPF;
	}
	public void setmLTPF(String mLTPF) {
		if(mLTPF == null)
		{
			this.mLTPF = addSpace("", 15, ' ');
		}
		else
		{
			if(mLTPF.length() < 15)
			{
				this.mLTPF = addSpace(mLTPF, (15 - mLTPF.length()), ' ');
			}
			else if(mLTPF.length() == 15)
			{
				this.mLTPF = mLTPF;
			}
		}
	}

	public String getmLTMeterType() {
		return mLTMeterType;
	}
	public void setmLTMeterType(String mLTMeterType) {
		if(mLTMeterType == null)
		{
			this.mLTMeterType = addSpace("", 2, ' ');
		}
		else
		{
			if(mLTMeterType.length() < 2)
			{
				this.mLTMeterType = addSpace(mLTMeterType, (2 - mLTMeterType.length()), ' ');
			}
			else if(mLTMeterType.length() == 2)
			{
				this.mLTMeterType = mLTMeterType;
			}
		}
	}
	
	//Nitish 27-10-2014 For Disconnection
		private String mForMonth_Disc; //04
		private String mBillDate_Disc; //08
		private String mConnectionNo_Disc;  //10
		private String mTourplanId_Disc; //09
		private String mPreRead_Disc;  //12
		private String mTariffCode_Disc; //03
		private String mIssueDateTime_Disc; //12
		private String mRRNo_Disc; //13
		private String mLocationCode_Disc; //10
		private String mMtrDisFlag_Disc; //1	
		private String mVer_Disc; //3

		public String getmForMonth_Disc() {
			return mForMonth_Disc;
		}

		public void setmForMonth_Disc(String mForMonth_Disc) {			
			if(mForMonth_Disc == null)
			{
				this.mForMonth_Disc = addSpaceRight("", 4, ' ');
			}
			else
			{
				if(mForMonth_Disc.length() < 4)
				{
					this.mForMonth_Disc = addSpaceRight(mForMonth_Disc, (4 - mForMonth_Disc.length()), ' ');
				}
				else if(mForMonth_Disc.length()==4)
				{
					this.mForMonth_Disc = mForMonth_Disc;
				}
			}
		}

		public String getmBillDate_Disc() {
			return mBillDate_Disc;
		}

		public void setmBillDate_Disc(String mBillDate_Disc) {
			if(mBillDate_Disc == null)
			{
				this.mBillDate_Disc = addSpaceRight("", 8, ' ');
			}
			else
			{
				if(mBillDate_Disc.length() < 8)
				{
					this.mBillDate_Disc = addSpaceRight(mBillDate_Disc, (8 - mBillDate_Disc.length()), ' ');
				}
				else if(mBillDate_Disc.length() == 8)
				{
					this.mBillDate_Disc = mBillDate_Disc;
				}
			}
		}

		public String getmConnectionNo_Disc() {
			return mConnectionNo_Disc;
		}

		public void setmConnectionNo_Disc(String mConnectionNo_Disc) {
			if(mConnectionNo_Disc == null)
			{
				this.mConnectionNo_Disc = addSpaceRight("", 10, ' ');
			}
			else
			{
				if(mConnectionNo_Disc.length() < 10)
				{
					this.mConnectionNo_Disc = addSpaceRight(mConnectionNo_Disc, (10 - mConnectionNo_Disc.length()), ' ');
				}
				else if(mConnectionNo_Disc.length() == 10)
				{
					this.mConnectionNo_Disc = mConnectionNo_Disc;
				}
			}
		}

		public String getmTourplanId_Disc() {
			return mTourplanId_Disc;
		}

		public void setmTourplanId_Disc(String mTourplanId_Disc) {
			if(mTourplanId_Disc == null)
			{
				this.mTourplanId_Disc = addSpaceRight("", 9, ' ');
			}
			else
			{
				if(mTourplanId_Disc.length() < 9)
				{
					this.mTourplanId_Disc = addSpaceRight(mTourplanId_Disc, (9 - mTourplanId_Disc.length()), ' ');
				}
				else if(mTourplanId_Disc.length() == 9)
				{
					this.mTourplanId_Disc = mTourplanId_Disc;
				}
			}
		}

		public String getmPreRead_Disc() {
			return mPreRead_Disc;
		}

		public void setmPreRead_Disc(String mPreRead_Disc) {
			if(mPreRead_Disc == null)
			{
				this.mPreRead_Disc = addSpace("", 12, ' ');
			}
			else
			{
				mPreRead_Disc = GetTwoDecimalPlace(mPreRead_Disc.trim());
				if(mPreRead_Disc.trim().length() < 12)
				{
					this.mPreRead_Disc = addSpace(mPreRead_Disc, (12 - mPreRead_Disc.trim().length()), ' ');
				}
				else if(mPreRead_Disc.trim().length() == 12)
				{
					this.mPreRead_Disc = mPreRead_Disc.trim();
				}
			}
		}

		public String getmTariffCode_Disc() {
			return mTariffCode_Disc;
		}

		public void setmTariffCode_Disc(String mTariffCode_Disc) {
			if(mTariffCode_Disc == null)
			{
				this.mTariffCode_Disc = addSpace("", 3, '0');
			}
			else
			{
				if(mTariffCode_Disc.length() < 3)
				{
					this.mTariffCode_Disc = addSpace(mTariffCode_Disc, (3 - mTariffCode_Disc.length()), '0');
				}
				else if(mTariffCode_Disc.length() == 3)
				{
					this.mTariffCode_Disc = mTariffCode_Disc;
				}
			}
		}

		public String getmIssueDateTime_Disc() {
			return mIssueDateTime_Disc;
		}

		public void setmIssueDateTime_Disc(String mIssueDateTime_Disc) {
			if(mIssueDateTime_Disc == null)
			{
				this.mIssueDateTime_Disc = addSpaceRight("", 12, ' ');
			}
			else
			{
				if(mIssueDateTime_Disc.length() < 12)
				{
					this.mIssueDateTime_Disc = addSpaceRight(mIssueDateTime_Disc, (12 - mIssueDateTime_Disc.length()), ' ');
				}
				else if(mIssueDateTime_Disc.length() == 12)
				{
					this.mIssueDateTime_Disc = mIssueDateTime_Disc;
				}
			}
		}

		public String getmRRNo_Disc() {
			return mRRNo_Disc;
		}

		public void setmRRNo_Disc(String mRRNo_Disc) {
			if(mRRNo_Disc == null)
			{
				this.mRRNo_Disc = addSpaceRight("", 13, ' ');
			}
			else
			{
				if(mRRNo_Disc.length() < 13)
				{
					this.mRRNo_Disc = addSpaceRight(mRRNo_Disc, (13 - mRRNo_Disc.length()), ' ');
				}
				else if(mRRNo_Disc.length() == 13)
				{
					this.mRRNo_Disc = mRRNo_Disc;
				}
				else if(mRRNo_Disc.length() > 13) //21-04-2017
				{
					this.mRRNo_Disc = mRRNo_Disc.substring(0, 13);
				}
				
			}
		}

		public String getmLocationCode_Disc() {
			return mLocationCode_Disc;
		}

		public void setmLocationCode_Disc(String mLocationCode_Disc) {
			if(mLocationCode_Disc == null)
			{
				this.mLocationCode_Disc = addSpaceRight("", 10, ' ');
			}
			else
			{
				if(mLocationCode_Disc.length() < 10)
				{
					this.mLocationCode_Disc = addSpaceRight(mLocationCode_Disc, (10 - mLocationCode_Disc.length()), ' ');
				}
				else if(mLocationCode_Disc.length() == 10)
				{
					this.mLocationCode_Disc = mLocationCode_Disc;
				}
			}
		}

		public String getmMtrDisFlag_Disc() {
			return mMtrDisFlag_Disc;
		}

		public void setmMtrDisFlag_Disc(String mMtrDisFlag_Disc) {
			if(mMtrDisFlag_Disc == null)
			{
				this.mMtrDisFlag_Disc = addSpaceRight("", 1, ' ');
			}
			else
			{
				if(mMtrDisFlag_Disc.length() < 1)
				{
					this.mMtrDisFlag_Disc = addSpaceRight(mMtrDisFlag_Disc, (1 - mMtrDisFlag_Disc.length()), ' ');
				}
				else if(mMtrDisFlag_Disc.length() == 1)
				{
					this.mMtrDisFlag_Disc = mMtrDisFlag_Disc;
				}
			}
		}

		public String getmVer_Disc() {
			return mVer_Disc;
		}

		public void setmVer_Disc(String mVer_Disc) {
			if(mVer_Disc == null)
			{
				this.mVer_Disc = addSpaceRight("", 3, ' ');
			}
			else
			{
				if(mVer_Disc.length() < 3)
				{
					this.mVer_Disc = addSpaceRight(mVer_Disc, (3 - mVer_Disc.length()), ' ');
				}
				else if(mVer_Disc.length() == 3)
				{
					this.mVer_Disc = mVer_Disc;
				}
			}
		}

		//End Disconnection

}
