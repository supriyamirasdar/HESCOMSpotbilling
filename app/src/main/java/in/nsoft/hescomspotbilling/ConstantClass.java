package in.nsoft.hescomspotbilling;


public class ConstantClass {
	//***************************With Camera****************************************************************
	public final static int difftime = 600000; //10min
	public final static String zeroSec = ":00"; //10min
	public final static String sPaise = ".00"; //.00   //13-06-2014
	public final static int difftimeOTP = 180000; //3min Added on 23-02-2018

	public static final int REQUEST_BILL_NOTSENT = 1;
	public static final int REQUEST_RPT_NOTSENT = 2;
	public static final int REQUEST_DIS_NOTSENT = 3;

	public final static String FileVersion = "148";//File Version //103  431
	public final static String AppVersion = "6.92";//App Version 10-03-2021
	public final static String IPAddress = "http://123.201.131.113:8112/Hescom.asmx";//Testing IP Address
	//public final static String IPAddress = "http://123.201.131.113:8112/Test.asmx";//Testing IP Address  
	//public final static String IPAddress =  "http://124.153.117.121:8185/Hescom.asmx";//Live IP Address

	//public final static String IPAddress =  "https://gprs-webservices.nsoft.in/Hescom.asmx";//Live  New IP Address


	public final static int height = 288; //Camera Image Height
	public final static int width = 352; //Camera Image Width
	public final static String LogFileName = "LogError.txt"; 
	public final static String eol = System.getProperty("line.separator");//New Line

	//Billing class===================================================================================================
	public final static String sNB= "NB";
	public final static String sBilled = "Billed";
	public final static String btnOKText = "Billing"; //Nitish 17-04-2014
	public final static String btnReprintText = "Re-Print";
	//END Billing class===============================================================================================

	//BillingConsumption class========================================================================================
	public final static String strProcessText = "Process";
	public final static String strResetText = "Reset";
	public final static String MtrImageSavePath = "/data/data/in.nsoft.hescomspotbilling/databases/photo";
	//END BillingConsumption class====================================================================================

	//Print Byte Code=================================================================================================
	public final static byte [] data1 ={0x1B,0x4B,0x07};//28-->01, 30 -->02,36-->03, 38 --> 04 , 41-->05,44-->06,48-->07,52-->08,57-->0E, 52-->0D, 52-->08, 57-->09, 72-->0B
	public final static byte [] data2 ={0x1B,0x4B,0x02};//Punit on 24-03-2014
	public final static byte [] data3 ={0x1B,0x4B,0x01};//Punit on 24-03-2014
	public final static byte [] data57 ={0x1B,0x4B,0x0E};

	//public final static byte [] doubleFontON ={0x12,0x44};
	//public final static byte [] doubleFontOFF ={0x12,0x64};

	public static final int FONTSTYLE_NORMAL = 0x00;
	public static final int FONTSTYLE_BOLD = 0x08;
	//byte [] data28char ={0x1B,0x4B,0x01};
	public final static byte[] linefeed1={0x0A,0x0D};
	//public final static byte[] linefeed1={0x0A};

	public final static byte[] barCode ={0x1B, 0x7A, 0x32, 0x14, 0x37, 0x59}; 
	//public final static byte[] barCode ={0x1B, 0x7A, 0x32, 0x14, 0x41};  //Analogic New
	//syntax: ESC-0x1B a-0x7A t-0x32 n-0x14(length-20) h-0x37(height) 
	//END Print Byte Code=============================================================================================

	//Collection ===================================================================================================
	public final static String sPaidAmt = "Paid Amount:"; 
	public final static String sNotPaid = "Not Paid"; 	
	public final static String mOpen = "Cash Counter Open";	
	public final static String mclose = "Cash Counter Closed";	

	public final static String mVerified = "Verified"; 
	public final static String mNotVerified = "Not Verified"; 
	public final static String mNAS = "Not Assigned"; 
	public final static String mNAP = "Not Approved";
	public final static String mSNV = "SIM Not Valid";	
	public final static String mSNA = "Session Not Allowed";	
	public final static String mAPP = "Approved";	
	public final static String mPending = "Pending";
	public final static String mComplete = "Completed";
	public final static String mbtnGetBatchNo = "Get Current Batch Number";
	public final static String mbtnPrevClose =  "Close Prev Cash Counter";
	//END Collection===============================================================================================

	//Collection Receipt Type 
	public final static String mRevenueRcpt= "REVENUE";
	public final static String mMISCRcpt= "MISC";
	public final static String mRevenueType= "R";
	public final static String mMISCType= "M";
	public final static String mMiscASD= " (ASD)";
	public final static String mBalance= "ASD Balance";
	//END Receipt Type===============================================================================================

	public final static String[] imeinos = {"911375053925727"};

	//Signal Strength 17-10-2014
	public final static String sLOW = "LOW"; 
	public final static String sNORMAL = "NORMAL"; 
	public final static String sHIGH = "HIGH"; 	


	//public final static String mEventMsg = " Double Batch Session "; //27-02-2018
	
	public final static String mGSTIN = "29AABCH3176JEZZ"; //03-04-2021
	
	//Disconnection Module
	public final static String mDisconnection= "Disconnection";
	public final static String mDisconnected= "Disconnected";  //If Mtr_DisFlag = 1
	public final static String mNotDisconnected= "Not Disconnected"; //If Mtr_DisFlag = 2
	public final static String mNotCaptured= "Not Captured"; 
	//END Disconnection===============================================================================================
	
	

	public final static String mTest = "Test AMIGOS KANNADA PILOT APPVER: " + AppVersion +  " FILE VERSION :";
	public final static String mLive = "Live AMIGOS KANNADA PILOT APPVER: " + AppVersion +  " FILE VERSION :";

}
