package in.nsoft.hescomspotbilling;
//Class Created by Tamilselvan on 20-03-2014
import java.math.RoundingMode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelperBackupDB extends SQLiteOpenHelper implements Schema {
	private static final String DB_NAME_BAK = "TRM_Hescom_bak.dat";
	private static final int VERSION = 1;//1,2
	private Context mcntx;
	private Cursor cr;
	int i = 0, Total = 0;
	private static final String TAG = DatabaseHelperBackupDB.class.getName();
	DatabaseHelperBackupDB dhBackUpDB;
	SQLiteDatabase sldbBackUp;

	public DatabaseHelperBackupDB(Context context) {
		super(context, DB_NAME_BAK, null, VERSION);
		mcntx = context;			
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	//Tamilselvan on 20-03-2014 //Modified 30-08-2014 added ReasonId
	public void ReCreateTablesBackUpDB()
	{		
		dhBackUpDB = new DatabaseHelperBackupDB(mcntx);
		sldbBackUp = dhBackUpDB.getReadableDatabase();
		//DROP and RE-CREATE Tables in SBM_BillCollDataMain==============================================			
		sldbBackUp.execSQL("DROP TABLE IF EXISTS " + TABLE_SBM_BillCollDataMain);
		sldbBackUp.execSQL("DROP TABLE IF EXISTS " + TABLE_EC_FC_Slab);
		sldbBackUp.execSQL("DROP TABLE IF EXISTS " + TABLE_ReadSlabNTariff);
		sldbBackUp.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTION_TABLE);	
		sldbBackUp.execSQL("DROP TABLE IF EXISTS " + TABLE_CASHCOUNTER_DETAILS);

		sldbBackUp.execSQL("CREATE TABLE SBM_BillCollDataMain(UID INTEGER PRIMARY KEY, ForMonth TEXT, BillDate TEXT, "+
				"SubId TEXT, ConnectionNo TEXT, CustomerName TEXT, TourplanId TEXT, BillNo TEXT, DueDate TEXT, "+
				"FixedCharges REAL, RebateFlag TEXT, ReaderCode TEXT, TariffCode INTEGER, ReadingDay TEXT, "+
				"PF TEXT, MF REAL, Status INTEGER, AvgCons TEXT, LinMin REAL, SancHp REAL, SancKw REAL,"+
				"SancLoad TEXT, PrevRdg TEXT, DlCount INTEGER, Arears REAL, IntrstCurnt REAL, DrFees REAL, "+
				"Others REAL, BillFor TEXT, BlCnt TEXT, RRNo TEXT, LegFol TEXT, TvmMtr TEXT, TaxFlag TEXT, "+
				"ArrearsOld REAL, Intrst_Unpaid REAL, IntrstOld REAL, Billable TEXT, NewNoofDays TEXT, "+
				"NoOfDays TEXT, HWCReb TEXT, DLAvgMin TEXT, TvmPFtype TEXT, AccdRdg TEXT, KVAIR TEXT, "+
				"DLTEc REAL, RRFlag TEXT, Mtd TEXT, SlowRtnPge REAL, OtherChargeLegend TEXT, GoKArrears REAL, "+
				"DPdate TEXT,  ReceiptAmnt REAL, ReceiptDate TEXT, TcName TEXT, ThirtyFlag TEXT, IODRemarks TEXT, "+
				"DayWise_Flag TEXT,  Old_Consumption TEXT, KVAAssd_Cons REAL, GvpId TEXT, BOBilled_Amount TEXT, "+
				"KVAH_OldConsumption REAL, EcsFlag TEXT, Supply_Points INTEGER, IODD11_Remarks TEXT, LocationCode TEXT, "+
				"BOBillFlag INTEGER, Address1 TEXT, Address2 TEXT, SectionName TEXT, OldConnID TEXT, MCHFlag TEXT, "+
				"FC_Slab_2 REAL, MobileNo TEXT, PreRead TEXT, PreStatus TEXT, SpotSerialNo INTEGER, Units TEXT, TFc REAL, "+
				"TEc REAL, FLReb REAL, ECReb REAL, TaxAmt REAL, PfPenAmt REAL, PenExLd REAL, HCReb REAL, HLReb REAL, "+
				"CapReb REAL, ExLoad REAL, DemandChrg REAL, AccdRdg_rtn TEXT, KVAFR TEXT, AbFlag TEXT, BjKj2Lt2 TEXT, "+
				"Remarks TEXT, GoKPayable REAL, IssueDateTime TEXT, RecordDmnd REAL, KVA_Consumption REAL, BillTotal REAL, "+
				"SBMNumber TEXT, RcptCnt INTEGER, Batch_No TEXT, Receipt_No INTEGER, DateTime TEXT, Payment_Mode TEXT, "+
				"Paid_Amt INTEGER, ChequeDDNo INTEGER, ChequeDDDate TEXT, Receipttypeflag TEXT, ApplicationNo TEXT, "+
				"ChargetypeID INTEGER, BankID INTEGER, Latitude TEXT, Latitude_Dir TEXT, Longitude TEXT, Longitude_Dir TEXT,"+ 
				"Gprs_Flag INTEGER, ConsPayable TEXT,MtrDisFlag INTEGER, Meter_type TEXT, Meter_serialno TEXT, "+
				"Gps_Latitude_image TEXT, Gps_LatitudeCardinal_image TEXT,  Gps_Longitude_image TEXT, "+
				"Gps_LongitudeCardinal_image TEXT, Gps_Latitude_print TEXT, Gps_LatitudeCardinal_print TEXT, "+
				"Gps_Longitude_print TEXT, Gps_LongitudeCardinal_print TEXT, Image_Name TEXT, Image_Path TEXT, "+
				"Image_Cap_Date TEXT, Image_Cap_Time TEXT , GPRS_Status TEXT, ReasonId INTEGER, Meter_Present_Flag INTEGER, " +
				"Mtr_Not_Visible INTEGER);");/**/	

		sldbBackUp.execSQL("CREATE TABLE EC_FC_Slab(UID INTEGER PRIMARY KEY, ConnectionNo TEXT, "+
				"RRNo TEXT, ECRate_Count INTEGER, ECRate_Row INTEGER, FCRate_1 REAL, "+
				"FCRate_2 REAL, Units_1 REAL, Units_2 REAL, Units_3 REAL, Units_4 REAL, "+
				"Units_5 REAL, Units_6 REAL, EC_Rate_1 REAL, EC_Rate_2 REAL, "+
				"EC_Rate_3 REAL, EC_Rate_4 REAL, EC_Rate_5 REAL, EC_Rate_6 REAL, "+
				"EC_1 REAL, EC_2 REAL, EC_3 REAL, EC_4 REAL, EC_5 REAL, EC_6 REAl, "+
				"FC_1 REAL, FC_2 REAL, TEc REAL, EC_FL_1 REAL, EC_FL_2 REAL, "+
				"EC_FL_3 REAL, EC_FL_4 REAL, EC_FL REAL, new_TEc REAL, old_TEc REAL);");

		sldbBackUp.execSQL("CREATE TABLE ReadSlabNTariff(UID INTEGER PRIMARY KEY, TarifCode INTEGER, TarifString TEXT);");

		sldbBackUp.execSQL("CREATE TABLE Collection_TABLE(UID INTEGER PRIMARY KEY,ConnectionNo TEXT,RRNo TEXT,CustomerName TEXT,RcptCnt INTEGER," +
				"Batch_No TEXT,Receipt_No TEXT,DateTime TEXT,Payment_Mode TEXT,Arrears TEXT,BillTotal TEXT,Paid_Amt INTEGER," +
				"BankID INTEGER,ChequeDDNo INTEGER,ChequeDDDate TEXT,Receipttypeflag TEXT," +
				"GvpId TEXT,SBMNumber TEXT,LocationCode TEXT,Gprs_Flag INTEGER, ArrearsBill_Flag INTEGER," +
				"ReaderCode TEXT, GPRS_Status TEXT , IODRemarks TEXT);");/**/	
		
		sldbBackUp.execSQL("CREATE TABLE CashCounter_Details(UID INTEGER PRIMARY KEY,IMEINo TEXT,SIMNo TEXT,BatchDate TEXT,CashLimit TEXT," +
				"StartTime TEXT,EndTime TEXT,Batch_No TEXT,DateTime Text,LocationCode Text,CashCounterOpen TEXT,CounterCloseDateTime TEXT);");
		
		

		//DROP and RE-CREATE Tables in SBM_BillCollDataMain==============================================
	}
	//Tamilselvan on 20-03-2014
	public void InsertSlabNTariffBackUpDB(String TCode, String TName)
	{
		try
		{
			dhBackUpDB = new DatabaseHelperBackupDB(mcntx);
			sldbBackUp = dhBackUpDB.getReadableDatabase();

			ContentValues valuesSNT = new ContentValues();
			valuesSNT.put("TarifCode", TCode);
			valuesSNT.put("TarifString", TName);
			sldbBackUp.insert("ReadSlabNTariff", null, valuesSNT);//
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}
	//Tamilselvan on 20-03-2014
	public void InsertBillCollDataMainBackUpDB(String sb)
	{
		try
		{
			dhBackUpDB = new DatabaseHelperBackupDB(mcntx);
			sldbBackUp = dhBackUpDB.getReadableDatabase();

			ContentValues values = new ContentValues();//1
			values.put("ForMonth", sb.substring(0, 4));//2
			values.put("BillDate", sb.substring(4, 12));//3
			values.put("SubId", sb.substring(12, 14));//4
			values.put("ConnectionNo", sb.substring(14, 24));//5
			values.put("CustomerName", sb.substring(24, 54));//6
			values.put("TourplanId", sb.substring(54, 63));//7
			values.put("BillNo", sb.substring(63, 74));//8
			values.put("DueDate", sb.substring(74, 82));//9
			values.put("FixedCharges", sb.substring(82, 89));//10
			values.put("RebateFlag", sb.substring(89, 90));//11
			values.put("ReaderCode", sb.substring(90, 100));//12
			values.put("TariffCode", sb.substring(100, 103));//13
			values.put("ReadingDay", sb.substring(103, 105));//14
			values.put("PF", sb.substring(105, 108));//15
			values.put("MF", sb.substring(108, 114));//16
			values.put("Status", sb.substring(114, 116));//17
			values.put("AvgCONs", sb.substring(116, 126));//18
			values.put("LinMin", sb.substring(126, 131));//19
			values.put("SancHp", sb.substring(131, 136));//20
			values.put("SancKw", sb.substring(136, 141));//21
			values.put("SancLoad", sb.substring(141, 146));//22
			values.put("PrevRdg", sb.substring(146, 158));//23
			values.put("DlCount", sb.substring(158, 159));//24
			values.put("Arears", sb.substring(159, 173));//25
			values.put("IntrstCurnt", sb.substring(173, 182));//26
			values.put("DrFees", sb.substring(182, 188));//27
			values.put("Others", sb.substring(188, 194));//28
			values.put("BillFor", sb.substring(194, 195));//29
			values.put("BlCnt", sb.substring(195, 196));//30
			values.put("RRNo", sb.substring(196, 209));//31
			values.put("LegFol", sb.substring(209, 217));//32
			values.put("TvmMtr", sb.substring(217, 218));//33
			values.put("TaxFlag", sb.substring(218, 219));//34
			values.put("ArrearsOld", sb.substring(219, 228));//35
			values.put("Intrst_unpaid", sb.substring(228, 234));//36
			values.put("IntrstOld", sb.substring(234, 248));//37
			values.put("Billable", sb.substring(248, 249));//38
			values.put("NewNoofDays", sb.substring(249, 253));//39
			values.put("NoOfDays", sb.substring(253, 255));//40
			values.put("HWCReb", sb.substring(255, 258));//41
			values.put("DLAvgMin", sb.substring(258, 259));//42
			values.put("TvmPFtype", sb.substring(259, 260));//43
			values.put("AccdRdg", sb.substring(260, 272));//44
			values.put("KVAIR", sb.substring(272, 284));//45
			values.put("DLTEc", sb.substring(284, 296));//46
			values.put("RRFlag", sb.substring(296, 297));//47
			values.put("Mtd", sb.substring(297, 298));//48
			values.put("SlowRtnPge", sb.substring(298, 304));//49
			values.put("OtherChargeLegEND", sb.substring(304, 319));//50
			values.put("GoKArrears", sb.substring(319, 328));//51
			values.put("DPdate", sb.substring(328, 336));//52
			values.put("ReceiptAmnt", sb.substring(336, 348));//53
			values.put("ReceiptDate", sb.substring(348, 356));//54
			values.put("TcName", sb.substring(356, 373));//55
			values.put("ThirtyFlag", sb.substring(373, 374));//56
			values.put("IODRemarks", sb.substring(374, 424));//57
			values.put("DayWise_Flag", sb.substring(424, 425));//58
			values.put("Old_CONSUMptiON", sb.substring(425, 435));//59
			values.put("KVAAssd_Cons", sb.substring(435, 445));//60
			values.put("GvpId", sb.substring(445, 459));//61
			values.put("BOBilled_Amount", sb.substring(459, 471));//62
			values.put("KVAH_OldConsumption", sb.substring(471, 481));//63
			values.put("EcsFlag", sb.substring(481, 482));//64
			values.put("Supply_Points", sb.substring(482, 486));//65
			values.put("IODD11_Remarks", sb.substring(486, 516));//66
			values.put("LocationCode", sb.substring(516, 526));//67
			values.put("BOBillFlag", sb.substring(526, 527));//68
			values.put("Address1", sb.substring(527, 547));//69
			values.put("Address2", sb.substring(547, 577));//70
			values.put("SectionName", sb.substring(577, 597));//71
			values.put("OldConnID", sb.substring(597, 604));//72
			values.put("MCHFLag", sb.substring(604, 605));//73
			values.put("FC_Slab_2", sb.substring(605, 612));//74
			values.put("MobileNo", sb.substring(612, sb.length()));//75

			sldbBackUp.insert("SBM_BillCollDataMain", null, values);//
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}
	//Tamilselvan on 20-03-2014
	public int UpdatestatusMaster(String StatusId, String Value)
	{
		dhBackUpDB = new DatabaseHelperBackupDB(mcntx);
		sldbBackUp = dhBackUpDB.getReadableDatabase();
		int update = 0;
		try
		{
			ContentValues valuesUpdateSubDiv = new ContentValues();
			valuesUpdateSubDiv.put("Value", Value);
			update = sldbBackUp.update("StatusMaster", valuesUpdateSubDiv, " StatusID = ? ", new String[]{StatusId});
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return update;
	}

	public int BackupDBBillSave()
	{
		dhBackUpDB = new DatabaseHelperBackupDB(mcntx);
		sldbBackUp = dhBackUpDB.getReadableDatabase();
		ReadSlabNTarifSbmBillCollection sbc = BillingObject.GetBillingObject();
		try
		{
			sldbBackUp.beginTransaction();
			ContentValues valuesUpdateMaintbl = new ContentValues();
			valuesUpdateMaintbl.put("SancHp", String.valueOf(sbc.getmSancHp()));
			valuesUpdateMaintbl.put("PF", sbc.getmPF());
			valuesUpdateMaintbl.put("LinMin", String.valueOf(sbc.getmLinMin()));
			valuesUpdateMaintbl.put("BlCnt", "1");//set BlCnt as 1 (it is billed) 
			valuesUpdateMaintbl.put("PreRead", sbc.getmPreRead());//Present Reading
			valuesUpdateMaintbl.put("PreStatus", sbc.getmPreStatus());//Present Status
			valuesUpdateMaintbl.put("spotserialno", sbc.getmSpotSerialNo());
			valuesUpdateMaintbl.put("units", sbc.getmUnits());
			valuesUpdateMaintbl.put("TFc", String.valueOf(sbc.getmTFc()));
			valuesUpdateMaintbl.put("TEc", String.valueOf(sbc.getmTEc()));
			valuesUpdateMaintbl.put("FlReb", String.valueOf(sbc.getmFLReb()));
			valuesUpdateMaintbl.put("EcReb", String.valueOf(sbc.getmECReb()));
			valuesUpdateMaintbl.put("TaxAmt", String.valueOf(sbc.getmTaxAmt()));
			valuesUpdateMaintbl.put("PfPenAmt", String.valueOf(sbc.getmPfPenAmt()));
			valuesUpdateMaintbl.put("PenExLd", String.valueOf(sbc.getmPenExLd()));
			valuesUpdateMaintbl.put("HCReb", String.valueOf(sbc.getmHCReb()));
			valuesUpdateMaintbl.put("HLReb", String.valueOf(sbc.getmHLReb()));
			valuesUpdateMaintbl.put("CapReb", String.valueOf(sbc.getmCapReb()));
			valuesUpdateMaintbl.put("ExLoad", String.valueOf(sbc.getmExLoad()));
			valuesUpdateMaintbl.put("DemandChrg", String.valueOf(sbc.getmDemandChrg()));
			valuesUpdateMaintbl.put("AccdRdg_rtn", sbc.getmAccdRdg_rtn());
			valuesUpdateMaintbl.put("kVAFR", sbc.getmKVAFR());
			valuesUpdateMaintbl.put("AbFlag", sbc.getmAbFlag());
			valuesUpdateMaintbl.put("BjKj2Lt2", sbc.getmBjKj2Lt2());
			valuesUpdateMaintbl.put("Remarks", sbc.getmRemarks());
			valuesUpdateMaintbl.put("GoKPayable", String.valueOf(sbc.getmGoKPayable()));
			valuesUpdateMaintbl.put("IssueDateTime", sbc.getmIssueDateTime());
			valuesUpdateMaintbl.put("RecordDmnd", String.valueOf(sbc.getmRecordDmnd()));
			valuesUpdateMaintbl.put("KVA_Consumption", String.valueOf(sbc.getmKVA_Consumption()));
			valuesUpdateMaintbl.put("KVAAssd_Cons", String.valueOf(sbc.getmKVAAssd_Cons()));
			valuesUpdateMaintbl.put("TvmMtr", sbc.getmTvmMtr());
			valuesUpdateMaintbl.put("BillTotal", String.valueOf(sbc.getmBillTotal()));
			valuesUpdateMaintbl.put("SBMNumber", sbc.getmSBMNumber());
			valuesUpdateMaintbl.put("ConsPayable", sbc.getmConsPayable());
			valuesUpdateMaintbl.put("MtrDisFlag", sbc.getmMtrDisFlag());
			valuesUpdateMaintbl.put("Meter_type", sbc.getmMeter_type());
			valuesUpdateMaintbl.put("Meter_serialno", sbc.getmMeter_serialno());
			valuesUpdateMaintbl.put("Gps_Latitude_image", sbc.getmGps_Latitude_image());
			valuesUpdateMaintbl.put("Gps_Longitude_image", sbc.getmGps_Longitude_image());
			valuesUpdateMaintbl.put("Image_Name", sbc.getmImage_Name());
			/*valuesUpdateMaintbl.put("TcName_Present", "");
			valuesUpdateMaintbl.put("Meter_Present_flag", "");
			valuesUpdateMaintbl.put("Mtr_not_visible", "");*///Comment By Tamilselvan on 13-02-2014 
			
			//30-08-2014
			//valuesUpdateMaintbl.put("ReasonId", sbc.getmReasonId());
			valuesUpdateMaintbl.put("Meter_Type", sbc.getmMeter_type());
			valuesUpdateMaintbl.put("MtrDisFlag", sbc.getmMtrDisFlag());
			valuesUpdateMaintbl.put("Meter_Present_Flag", sbc.getmMeter_Present_Flag());
			valuesUpdateMaintbl.put("Mtr_Not_Visible", sbc.getmMtr_Not_Visible());
			
			int updateResult = sldbBackUp.update("SBM_BillCollDataMain", valuesUpdateMaintbl, "ConnectionNo = ?", new String[]{sbc.getmConnectionNo()});
			if(updateResult <= 0)
			{				
				sldbBackUp.endTransaction();
				throw new Exception("Updation Failed for SBM_BillCollDataMain");
			}

			ContentValues valuesInsertSlabtbl = new ContentValues();
			valuesInsertSlabtbl.put("ConnectionNo", sbc.getmConnectionNo());
			valuesInsertSlabtbl.put("RRNo", sbc.getmRRNo());
			valuesInsertSlabtbl.put("ECRate_Count", sbc.getMmECRate_Count());
			valuesInsertSlabtbl.put("ECRate_Row", sbc.getMmECRate_Row());
			valuesInsertSlabtbl.put("FCRate_1", String.valueOf(sbc.getMmFCRate_1())); 
			valuesInsertSlabtbl.put("FCRate_2", String.valueOf(sbc.getMmCOL_FCRate_2())); 
			valuesInsertSlabtbl.put("Units_1", String.valueOf(sbc.getMmUnits_1()));
			valuesInsertSlabtbl.put("Units_2", String.valueOf(sbc.getMmUnits_2()));
			valuesInsertSlabtbl.put("Units_3", String.valueOf(sbc.getMmUnits_3()));
			valuesInsertSlabtbl.put("Units_4", String.valueOf(sbc.getMmUnits_4()));
			valuesInsertSlabtbl.put("Units_5", String.valueOf(sbc.getMmUnits_5()));
			valuesInsertSlabtbl.put("Units_6", String.valueOf(sbc.getMmUnits_6()));
			valuesInsertSlabtbl.put("EC_Rate_1", String.valueOf(sbc.getMmEC_Rate_1()));
			valuesInsertSlabtbl.put("EC_Rate_2", String.valueOf(sbc.getMmEC_Rate_2()));
			valuesInsertSlabtbl.put("EC_Rate_3", String.valueOf(sbc.getMmEC_Rate_3()));
			valuesInsertSlabtbl.put("EC_Rate_4", String.valueOf(sbc.getMmEC_Rate_4()));
			valuesInsertSlabtbl.put("EC_Rate_5", String.valueOf(sbc.getMmEC_Rate_5()));
			valuesInsertSlabtbl.put("EC_Rate_6", String.valueOf(sbc.getMmEC_Rate_6()));
			valuesInsertSlabtbl.put("EC_1", String.valueOf(sbc.getMmEC_1()));
			valuesInsertSlabtbl.put("EC_2", String.valueOf(sbc.getMmEC_2()));
			valuesInsertSlabtbl.put("EC_3", String.valueOf(sbc.getMmEC_3()));
			valuesInsertSlabtbl.put("EC_4", String.valueOf(sbc.getMmEC_4()));
			valuesInsertSlabtbl.put("EC_5", String.valueOf(sbc.getMmEC_5()));
			valuesInsertSlabtbl.put("EC_6", String.valueOf(sbc.getMmEC_6()));
			valuesInsertSlabtbl.put("FC_1", String.valueOf(sbc.getMmFC_1()));
			valuesInsertSlabtbl.put("FC_2", String.valueOf(sbc.getMmFC_2()));
			valuesInsertSlabtbl.put("TEc", String.valueOf(sbc.getMmTEc()));
			valuesInsertSlabtbl.put("EC_FL_1", String.valueOf(sbc.getMmEC_FL_1()));
			valuesInsertSlabtbl.put("EC_FL_2", String.valueOf(sbc.getMmEC_FL_1()));
			valuesInsertSlabtbl.put("EC_FL_3", String.valueOf(sbc.getMmEC_FL_1()));
			valuesInsertSlabtbl.put("EC_FL_4", String.valueOf(sbc.getMmEC_FL_1()));
			valuesInsertSlabtbl.put("EC_FL", String.valueOf(sbc.getMmEC_FL()));
			valuesInsertSlabtbl.put("new_TEc",  String.valueOf(sbc.getMmnew_TEc()));
			valuesInsertSlabtbl.put("old_TEc", String.valueOf(sbc.getMmold_TEc()));
			long insertResult = sldbBackUp.insert("EC_FC_Slab", null, valuesInsertSlabtbl);
			if(insertResult <= 0)
			{	
				sldbBackUp.endTransaction();
				throw new Exception("Insertion failed for EC_FC_Slab");
			}
			sldbBackUp.setTransactionSuccessful();			
			return 1;
		}
		catch(Exception e)
		{
			Log.d("", e.toString());
			return 0;
		}
		finally
		{
			sldbBackUp.endTransaction();
		}
	}
	//Modified Nitish 12-06-2014
	public int BackupDBCollectionSave() 
	{
		dhBackUpDB = new DatabaseHelperBackupDB(mcntx);		
		sldbBackUp = dhBackUpDB.getReadableDatabase();		
		ReadCollection sbc = CollectionObject.GetCollectionObject();			
		try
		{

			sldbBackUp.beginTransaction();
			ContentValues valuesInsertCollectiontbl = new ContentValues();

			valuesInsertCollectiontbl.put("ConnectionNo", sbc.getmConnectionNo());
			valuesInsertCollectiontbl.put("RRNo", sbc.getmRRNo());
			valuesInsertCollectiontbl.put("CustomerName", sbc.getmCustomerName());
			valuesInsertCollectiontbl.put("RcptCnt", sbc.getmRcptCnt());
			valuesInsertCollectiontbl.put("Batch_No", sbc.getmBatch_No());
			valuesInsertCollectiontbl.put("Receipt_No", sbc.getmReceipt_No());
			valuesInsertCollectiontbl.put("DateTime", sbc.getmDateTime());
			valuesInsertCollectiontbl.put("Payment_Mode ", sbc.getmPayment_Mode());
			valuesInsertCollectiontbl.put("Arrears", String.valueOf(sbc.getmArears().add(sbc.getmIntrstOld()).add(sbc.getmArrearsOld()).setScale(2, RoundingMode.HALF_EVEN)));
			valuesInsertCollectiontbl.put("BillTotal", String.valueOf(sbc.getmBillTotal().setScale(2,RoundingMode.HALF_EVEN)));
			valuesInsertCollectiontbl.put("Paid_Amt", sbc.getmPaid_Amt());
			valuesInsertCollectiontbl.put("BankID", sbc.getmBankID());			
			valuesInsertCollectiontbl.put("ChequeDDNo", sbc.getmChequeDDNo());
			valuesInsertCollectiontbl.put("ChequeDDDate", sbc.getmChequeDDDate());
			valuesInsertCollectiontbl.put("Receipttypeflag",sbc.getmReceipttypeflag());	
			valuesInsertCollectiontbl.put("GvpId",sbc.getmGvpId());	
			valuesInsertCollectiontbl.put("SBMNumber",sbc.getmSBMNumber());	
			valuesInsertCollectiontbl.put("LocationCode",sbc.getmLocationCode());	
			valuesInsertCollectiontbl.put("ArrearsBill_Flag",sbc.getmArrearsBill_Flag());	
			valuesInsertCollectiontbl.put("ReaderCode",sbc.getmReaderCode());

			long insertResult = sldbBackUp.insert("Collection_TABLE", null, valuesInsertCollectiontbl);
			if(insertResult <= 0)
			{	
				sldbBackUp.endTransaction();
				throw new Exception("Insertion failed for Collection_TABLE");
			}				
			sldbBackUp.setTransactionSuccessful();			
			return 1;
		}
		catch(Exception e)
		{
			Log.d("", e.toString());
			return 0;
		}
		finally
		{
			sldbBackUp.endTransaction();
		}
	}
	//End Nitish 07/04/2014	

	//Nitish 19/04/2014
	public int BackupDBCashCounterDetailsSave(String imeino, String simno,String loccode, String str[]) 
	{
		dhBackUpDB = new DatabaseHelperBackupDB(mcntx);
		sldbBackUp = dhBackUpDB.getReadableDatabase();
		try
		{			
			sldbBackUp.beginTransaction();
			ContentValues valuesInsertCashCounterDetails = new ContentValues();
			ReadCollection sbc = CollectionObject.GetCollectionObject();

			valuesInsertCashCounterDetails.put("IMEINo", imeino);
			valuesInsertCashCounterDetails.put("SIMNo", simno);	
			valuesInsertCashCounterDetails.put("BatchDate", str[0]);
			valuesInsertCashCounterDetails.put("CashLimit", str[1]);			
			valuesInsertCashCounterDetails.put("StartTime", str[2]);
			valuesInsertCashCounterDetails.put("EndTime ", str[3]);
			valuesInsertCashCounterDetails.put("Batch_No", str[4]);
			valuesInsertCashCounterDetails.put("DateTime", str[5]);

			valuesInsertCashCounterDetails.put("LocationCode",loccode );			
			valuesInsertCashCounterDetails.put("CashCounterOpen", "1");				

			long insertResult = sldbBackUp.insert("CashCounter_Details", null, valuesInsertCashCounterDetails);
			if(insertResult <= 0)
			{	
				sldbBackUp.endTransaction();
				throw new Exception("Insertion failed for CashCounter_Details Table");
			}				
			sldbBackUp.setTransactionSuccessful();							
			return 1;
		}
		catch(Exception e)
		{
			Log.d("", e.toString());
			return 0;
		}	
		finally
		{
			sldbBackUp.endTransaction();
		}	
	}
	//End Nitish 19/04/2014	
}
