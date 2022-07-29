package in.nsoft.hescomspotbilling;

import java.io.Serializable;

public class LandTObject implements Serializable {

	private String mMeterSerialNo;
	private String mMeterDate;	
	private String mMeterTime;	
	private String mDataPoweredBy;
	private String mMeterFirmwareVersion;	
	
	private String mTotalCummulativeEnergy;
	private String mTariffCummulativeEnergy;	
	private String mTarifWiseMD;	
	private String mDate;
	private String mTime;	
	private String mInstantaneousVoltage;
	private String mInstantaneousCurrent1;	
	private String mInstantaneousCurrent2;	
	private String mLoad ;	
	private String mPF;
	private String mFrequency;	
	private String mManufacturer;	
	private String mMeterType;	
	private String mCummulativeEnergyKVAH;
	private int mUID; //12-03-2021
	private String mForMonth;
	private String mConnectionNo;
	private String mCreatedDate;
	
	
	public String getmForMonth() {
		return mForMonth;
	}
	public void setmForMonth(String mForMonth) {
		this.mForMonth = mForMonth;
	}
	public String getmConnectionNo() {
		return mConnectionNo;
	}
	public void setmConnectionNo(String mConnectionNo) {
		this.mConnectionNo = mConnectionNo;
	}
	public String getmCreatedDate() {
		return mCreatedDate;
	}
	public void setmCreatedDate(String mCreatedDate) {
		this.mCreatedDate = mCreatedDate;
	}
	public String getmMeterSerialNo() {
		return mMeterSerialNo;
	}
	public void setmMeterSerialNo(String mMeterSerialNo) {
		this.mMeterSerialNo = mMeterSerialNo;
	}
	public String getmMeterDate() {
		return mMeterDate;
	}
	public void setmMeterDate(String mMeterDate) {
		this.mMeterDate = mMeterDate;
	}
	public String getmMeterTime() {
		return mMeterTime;
	}
	public void setmMeterTime(String mMeterTime) {
		this.mMeterTime = mMeterTime;
	}
	public String getmDataPoweredBy() {
		return mDataPoweredBy;
	}
	public void setmDataPoweredBy(String mDataPoweredBy) {
		this.mDataPoweredBy = mDataPoweredBy;
	}
	public String getmMeterFirmwareVersion() {
		return mMeterFirmwareVersion;
	}
	public void setmMeterFirmwareVersion(String mMeterFirmwareVersion) {
		this.mMeterFirmwareVersion = mMeterFirmwareVersion;
	}
	public String getmTotalCummulativeEnergy() {
		return mTotalCummulativeEnergy;
	}
	public void setmTotalCummulativeEnergy(String mTotalCummulativeEnergy) {
		this.mTotalCummulativeEnergy = mTotalCummulativeEnergy;
	}
	public String getmTariffCummulativeEnergy() {
		return mTariffCummulativeEnergy;
	}
	public void setmTariffCummulativeEnergy(String mTariffCummulativeEnergy) {
		this.mTariffCummulativeEnergy = mTariffCummulativeEnergy;
	}
	public String getmTarifWiseMD() {
		return mTarifWiseMD;
	}
	public void setmTarifWiseMD(String mTarifWiseMD) {
		this.mTarifWiseMD = mTarifWiseMD;
	}
	public String getmDate() {
		return mDate;
	}
	public void setmDate(String mDate) {
		this.mDate = mDate;
	}
	public String getmTime() {
		return mTime;
	}
	public void setmTime(String mTime) {
		this.mTime = mTime;
	}
	public String getmInstantaneousVoltage() {
		return mInstantaneousVoltage;
	}
	public void setmInstantaneousVoltage(String mInstantaneousVoltage) {
		this.mInstantaneousVoltage = mInstantaneousVoltage;
	}
	public String getmInstantaneousCurrent1() {
		return mInstantaneousCurrent1;
	}
	public void setmInstantaneousCurrent1(String mInstantaneousCurrent1) {
		this.mInstantaneousCurrent1 = mInstantaneousCurrent1;
	}
	public String getmInstantaneousCurrent2() {
		return mInstantaneousCurrent2;
	}
	public void setmInstantaneousCurrent2(String mInstantaneousCurrent2) {
		this.mInstantaneousCurrent2 = mInstantaneousCurrent2;
	}
	public String getmLoad() {
		return mLoad;
	}
	public void setmLoad(String mLoad) {
		this.mLoad = mLoad;
	}
	public String getmPF() {
		return mPF;
	}
	public void setmPF(String mPF) {
		this.mPF = mPF;
	}
	public String getmFrequency() {
		return mFrequency;
	}
	public void setmFrequency(String mFrequency) {
		this.mFrequency = mFrequency;
	}
	public String getmManufacturer() {
		return mManufacturer;
	}
	public void setmManufacturer(String mManufacturer) {
		this.mManufacturer = mManufacturer;
	}
	public String getmMeterType() {
		return mMeterType;
	}
	public void setmMeterType(String mMeterType) {
		this.mMeterType = mMeterType;
	}
	public String getmCummulativeEnergyKVAH() {
		return mCummulativeEnergyKVAH;
	}
	public void setmCummulativeEnergyKVAH(String mCummulativeEnergyKVAH) {
		this.mCummulativeEnergyKVAH = mCummulativeEnergyKVAH;
	}
	public int getmUID() {
		return mUID;
	}
	public void setmUID(int mUID) {
		this.mUID = mUID;
	}
	
}
