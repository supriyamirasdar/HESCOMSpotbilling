package in.nsoft.hescomspotbilling;

public class ReadEventLog {

	private String UID;
	private String IMEINO;
	private String SIMNO;
	private int Flag;
	private String Description;
	private String DateTime;
	private int GPRSFlag;
	private String GPRSStatus;
	
	
	public String getIMEINO() {
		return IMEINO;
	}
	public void setIMEINO(String iMEINO) {
		IMEINO = iMEINO;
	}
	public String getSIMNO() {
		return SIMNO;
	}
	public void setSIMNO(String sIMNO) {
		SIMNO = sIMNO;
	}
	public int getFlag() {
		return Flag;
	}
	public void setFlag(int flag) {
		Flag = flag;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getDateTime() {
		return DateTime;
	}
	public void setDateTime(String dateTime) {
		DateTime = dateTime;
	}
	public int getGPRSFlag() {
		return GPRSFlag;
	}
	public void setGPRSFlag(int gPRSFlag) {
		GPRSFlag = gPRSFlag;
	}
	public String getGPRSStatus() {
		return GPRSStatus;
	}
	public void setGPRSStatus(String gPRSStatus) {
		GPRSStatus = gPRSStatus;
	}
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}



}
