package in.nsoft.hescomspotbilling;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper implements Schema {
	private static final String DB_NAME = "TRM_Hescom.dat";
	private static final int VERSION = 1;//1,2
	private Context mcntx;
	private Cursor cr;
	int i = 0, Total = 0;
	private long contentLength = 0, totalLength = 0;
	private static final String TAG = DatabaseHelper.class.getName();
	String ReceiveStr = "";
	int proTotal = 0;

	public DatabaseHelper(Context context) {
		super(context,DB_NAME,null,VERSION);
		mcntx=context;
		// TODO Auto-generated constructor stub		
	}



	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	@Override //Added 29-06-2020
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
		if (android.os.Build.VERSION.SDK_INT >= 28)
			db.disableWriteAheadLogging();
	}
	//Function to verify user credential
	public boolean VerifyUser( String id,String pwd ) throws Exception
	{
		//Cursor cr =getReadableDatabase().query(TABLE_USERS, null, null, null, null, null, null);//getReadableDatabase().rawQuery(sql,null) ;

		cr=null ;
		boolean result=false;
		try {
			QueryParameters qParam=StoredProcedure.GetUserDetails(id, pwd);

			cr=getReadableDatabase().rawQuery(qParam.getSql(),qParam.getSelectionArgs());

			if(cr.getCount()==1)
			{
				result= true;
				cr.moveToFirst();
				/*	UserDetails.SetUserDetails(cr.getInt(cr.getColumnIndex(COL_USERID_USERS)), cr.getString(cr.getColumnIndex(COL_USERNAME_USERS))
							,cr.getString(cr.getColumnIndex(COL_FULLNAME_USERS)) , cr.getString(cr.getColumnIndex(COL_MOBILENO_USERS))
							,cr.getInt(cr.getColumnIndex(COL_ROLEID_USERS)), cr.getString(cr.getColumnIndex(COL_LOCATIONCODE_USERS))
							, cr.getInt(cr.getColumnIndex(COL_BLOCKED_USERS)));*/
			}
		} 
		catch (Exception e) {
			// TODO: handle exception
			throw e;

		}
		finally
		{
			if(cr!=null)
				cr.close();

		}
		return result;
	}
	//punit 15022014
	public AutoDDLAdapter getAllConnectionNo() throws Exception
	{
		AutoDDLAdapter pList = null;
		cr=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetUserId();
			cr = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());		
			for(int i=1; i<=cr.getCount();++i)
			{
				if(pList==null)
				{
					pList=new AutoDDLAdapter(mcntx,new ArrayList<DDLItem>());
				}
				if(i==1)
					cr.moveToFirst();
				else
					cr.moveToNext();	
				pList.AddItem(String.valueOf(cr.getInt(cr.getColumnIndex(COL_CustomerName))), cr.getString(cr.getColumnIndex(COL_ConnectionNo)));


			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return pList;
	}

	//Modified 30-07-2015
	public void GetAllDatafromDb(String custId ) throws Exception
	{
		ReadSlabNTarifSbmBillCollection CustDetails = BillingObject.GetBillingObject();


		cr=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetAllDatafromDb(custId);
			cr = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(cr.getCount() > 0)//Cursor IF Start
			{
				cr.moveToFirst();				
				CustDetails.setmAbFlag(cr.getString(cr.getColumnIndex(COL_AbFlag)));
				CustDetails.setmAccdRdg(cr.getString(cr.getColumnIndex(COL_AccdRdg)));
				CustDetails.setmAccdRdg_rtn(cr.getString(cr.getColumnIndex(COL_AccdRdg_rtn)));
				CustDetails.setmAddress1(cr.getString(cr.getColumnIndex(COL_Address1)));
				CustDetails.setmAddress2(cr.getString(cr.getColumnIndex(COL_Address2)));
				CustDetails.setmApplicationNo(cr.getString(cr.getColumnIndex(COL_ApplicationNo)));
				CustDetails.setmArears(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Arears))));
				CustDetails.setmArrearsOld(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ArrearsOld))));
				CustDetails.setmAvgCons(cr.getString(cr.getColumnIndex(COL_AvgCons)));
				CustDetails.setmBankID(cr.getInt(cr.getColumnIndex(COL_BankID)));
				CustDetails.setmBatch_No(cr.getString(cr.getColumnIndex(COL_Batch_No)));
				CustDetails.setmBillable(cr.getString(cr.getColumnIndex(COL_Billable))); 
				CustDetails.setmBillDate(cr.getString(cr.getColumnIndex(COL_BillDate)));
				CustDetails.setmBillFor(cr.getString(cr.getColumnIndex(COL_BillFor)));
				CustDetails.setmBillNo(cr.getString(cr.getColumnIndex(COL_BillNo)));
				CustDetails.setmBillTotal(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_BillTotal))));
				CustDetails.setmBjKj2Lt2(cr.getString(cr.getColumnIndex(COL_BjKj2Lt2)));
				CustDetails.setmBlCnt(cr.getString(cr.getColumnIndex(COL_BlCnt)));
				CustDetails.setmBOBilled_Amount(cr.getString(cr.getColumnIndex(COL_BOBilled_Amount)));
				CustDetails.setmBOBillFlag(cr.getInt(cr.getColumnIndex(COL_BOBillFlag)));
				CustDetails.setmCapReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_CapReb))));
				CustDetails.setmChargetypeID(cr.getInt(cr.getColumnIndex(COL_ChargetypeID)));
				CustDetails.setmChequeDDDate(cr.getString(cr.getColumnIndex(COL_ChequeDDDate)));
				CustDetails.setmChequeDDNo(cr.getInt(cr.getColumnIndex(COL_ChequeDDNo)));
				CustDetails.setmConnectionNo(cr.getString(cr.getColumnIndex(COL_ConnectionNo)));
				CustDetails.setmConsPayable(cr.getString(cr.getColumnIndex(COL_ConsPayable)));
				CustDetails.setmCustomerName(cr.getString(cr.getColumnIndex(COL_CustomerName)));
				CustDetails.setmDateTime(cr.getString(cr.getColumnIndex(COL_DateTime)));
				CustDetails.setmDayWise_Flag(cr.getString(cr.getColumnIndex(COL_DayWise_Flag)));
				CustDetails.setmDemandChrg(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_DemandChrg))));
				CustDetails.setmDLAvgMin(cr.getString(cr.getColumnIndex(COL_DLAvgMin)));
				CustDetails.setmDlCount(cr.getInt(cr.getColumnIndex(COL_DlCount)));				
				CustDetails.setmDLTEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_DLTEc))));
				CustDetails.setmDPdate(cr.getString(cr.getColumnIndex(COL_DPdate)));
				CustDetails.setmDrFees(cr.getString(cr.getColumnIndex(COL_DrFees)));
				CustDetails.setmDueDate(cr.getString(cr.getColumnIndex(COL_DueDate)));
				CustDetails.setmECReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ECReb))));
				CustDetails.setmEcsFlag(cr.getString(cr.getColumnIndex(COL_EcsFlag)));
				CustDetails.setmExLoad(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ExLoad))));
				CustDetails.setmFC_Slab_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_Slab_2))));
				CustDetails.setmFixedCharges(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FixedCharges))));
				CustDetails.setmFLReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FLReb))));
				CustDetails.setmForMonth(cr.getString(cr.getColumnIndex(COL_ForMonth)));
				CustDetails.setmGoKArrears(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_GoKArrears))));
				CustDetails.setmGoKPayable(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_GoKPayable))));
				CustDetails.setmGprs_Flag(cr.getInt(cr.getColumnIndex(COL_Gprs_Flag)));
				CustDetails.setmGps_Latitude_image(cr.getString(cr.getColumnIndex(COL_Gps_Latitude_image)));
				CustDetails.setmGps_Latitude_print(cr.getString(cr.getColumnIndex(COL_Gps_Latitude_print)));
				CustDetails.setmGps_LatitudeCardinal_image(cr.getString(cr.getColumnIndex(COL_Gps_LatitudeCardinal_image)));
				CustDetails.setmGps_LatitudeCardinal_print(cr.getString(cr.getColumnIndex(COL_Gps_LatitudeCardinal_print)));
				CustDetails.setmGps_Longitude_image(cr.getString(cr.getColumnIndex(COL_Gps_Longitude_image)));
				CustDetails.setmGps_Longitude_print(cr.getString(cr.getColumnIndex(COL_Gps_Longitude_print)));
				CustDetails.setmGps_LongitudeCardinal_image(cr.getString(cr.getColumnIndex(COL_Gps_LongitudeCardinal_image)));
				CustDetails.setmGps_LatitudeCardinal_print(cr.getString(cr.getColumnIndex(COL_Gps_LatitudeCardinal_print)));
				CustDetails.setmGvpId(cr.getString(cr.getColumnIndex(COL_GvpId)));
				CustDetails.setmHCReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_HCReb))));
				CustDetails.setmHLReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_HLReb))));
				CustDetails.setmHWCReb(cr.getString(cr.getColumnIndex(COL_HWCReb)));
				CustDetails.setmImage_Cap_Date(cr.getString(cr.getColumnIndex(COL_Image_Cap_Date)));
				CustDetails.setmImage_Cap_Time(cr.getString(cr.getColumnIndex(COL_Image_Cap_Time)));
				CustDetails.setmImage_Name(cr.getString(cr.getColumnIndex(COL_Image_Name)));
				CustDetails.setmImage_Path(cr.getString(cr.getColumnIndex(COL_Image_Path)));
				CustDetails.setmIntrst_Unpaid(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Intrst_Unpaid))));
				CustDetails.setmIntrstCurnt(cr.getString(cr.getColumnIndex(COL_IntrstCurnt)));
				CustDetails.setmIntrstOld(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_IntrstOld))));
				CustDetails.setmIODD11_Remarks(cr.getString(cr.getColumnIndex(COL_IODD11_Remarks)));
				CustDetails.setmIODRemarks(cr.getString(cr.getColumnIndex(COL_IODRemarks)));
				CustDetails.setmIssueDateTime(cr.getString(cr.getColumnIndex(COL_IssueDateTime)));
				CustDetails.setmKVA_Consumption(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_KVA_Consumption))));
				CustDetails.setmKVAAssd_Cons(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_KVAAssd_Cons))));
				CustDetails.setmKVAFR(cr.getString(cr.getColumnIndex(COL_KVAFR)));
				CustDetails.setmKVAH_OldConsumption(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_KVAFR))));
				CustDetails.setmKVAIR(cr.getString(cr.getColumnIndex(COL_KVAIR)));
				CustDetails.setmLatitude(cr.getString(cr.getColumnIndex(COL_Latitude)));
				CustDetails.setmLatitude_Dir(cr.getString(cr.getColumnIndex(COL_Latitude_Dir)));
				CustDetails.setmLegFol(cr.getString(cr.getColumnIndex(COL_LegFol)));
				CustDetails.setmLinMin(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_LinMin))));
				CustDetails.setmLocationCode(cr.getString(cr.getColumnIndex(COL_LocationCode)));
				CustDetails.setmLongitude(cr.getString(cr.getColumnIndex(COL_Longitude)));
				CustDetails.setmLongitude_Dir(cr.getString(cr.getColumnIndex(COL_Longitude_Dir)));
				CustDetails.setmMCHFlag(cr.getString(cr.getColumnIndex(COL_MCHFlag)));
				CustDetails.setmMeter_serialno(cr.getString(cr.getColumnIndex(COL_Meter_serialno)));
				CustDetails.setmMeter_type(cr.getString(cr.getColumnIndex(COL_Meter_type)));
				CustDetails.setmMF(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_MF))));
				CustDetails.setmMobileNo(cr.getString(cr.getColumnIndex(COL_MobileNo)));
				CustDetails.setmMtd(cr.getString(cr.getColumnIndex(COL_Mtd)));
				CustDetails.setmMtrDisFlag(cr.getInt(cr.getColumnIndex(COL_MtrDisFlag)));
				CustDetails.setmNewNoofDays(cr.getString(cr.getColumnIndex(COL_NewNoofDays)));
				CustDetails.setmNoOfDays(cr.getString(cr.getColumnIndex(COL_NoOfDays)));
				CustDetails.setmOld_Consumption(cr.getString(cr.getColumnIndex(COL_Old_Consumption)));
				CustDetails.setmOldConnID(cr.getString(cr.getColumnIndex(COL_OldConnID)));
				CustDetails.setmOtherChargeLegend(cr.getString(cr.getColumnIndex(COL_OtherChargeLegend)));
				CustDetails.setmOthers(cr.getString(cr.getColumnIndex(COL_Others)));
				CustDetails.setmPaid_Amt(cr.getInt(cr.getColumnIndex(COL_Paid_Amt)));
				CustDetails.setmPayment_Mode(cr.getString(cr.getColumnIndex(COL_Payment_Mode)));
				CustDetails.setmPenExLd(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_PenExLd))));
				CustDetails.setmPF(cr.getString(cr.getColumnIndex(COL_PF)));
				CustDetails.setmPfPenAmt(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_PfPenAmt))));
				CustDetails.setmPreRead(cr.getString(cr.getColumnIndex(COL_PreRead)));
				CustDetails.setmPreStatus(cr.getString(cr.getColumnIndex(COL_PreStatus)));//Current/Present status
				CustDetails.setmPrevRdg(cr.getString(cr.getColumnIndex(COL_PrevRdg)));
				CustDetails.setmRcptCnt(cr.getInt(cr.getColumnIndex(COL_RcptCnt)));
				CustDetails.setmReaderCode(cr.getString(cr.getColumnIndex(COL_ReaderCode)));
				CustDetails.setmReadingDay(cr.getString(cr.getColumnIndex(COL_ReadingDay)));
				CustDetails.setmRebateFlag(cr.getString(cr.getColumnIndex(COL_RebateFlag)));
				CustDetails.setmReceipt_No(cr.getInt(cr.getColumnIndex(COL_Receipt_No)));
				CustDetails.setmReceiptAmnt(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ReceiptAmnt))));
				CustDetails.setmReceiptDate(cr.getString(cr.getColumnIndex(COL_ReceiptDate)));
				CustDetails.setmReceipttypeflag(cr.getString(cr.getColumnIndex(COL_Receipttypeflag)));
				CustDetails.setmRecordDmnd(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_RecordDmnd))));
				CustDetails.setmRemarks(cr.getString(cr.getColumnIndex(COL_Remarks)));
				CustDetails.setmRRFlag(cr.getString(cr.getColumnIndex(COL_RRFlag)));
				CustDetails.setmRRNo(cr.getString(cr.getColumnIndex(COL_RRNo)));
				CustDetails.setmSancHp(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SancHp))));
				CustDetails.setmSancKw(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SancKw))));
				CustDetails.setmSancLoad(cr.getString(cr.getColumnIndex(COL_SancLoad)));
				CustDetails.setmSBMNumber(cr.getString(cr.getColumnIndex(COL_SBMNumber)));
				CustDetails.setmSectionName(cr.getString(cr.getColumnIndex(COL_SectionName)));
				CustDetails.setmSlowRtnPge(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SlowRtnPge))));
				CustDetails.setmSpotSerialNo(cr.getInt(cr.getColumnIndex(COL_SpotSerialNo)));
				CustDetails.setmStatus(cr.getInt(cr.getColumnIndex(COL_Status)));//Previous Status
				CustDetails.setmSubId(cr.getString(cr.getColumnIndex(COL_SubId)));
				CustDetails.setmSupply_Points(cr.getInt(cr.getColumnIndex(COL_Supply_Points)));           
				CustDetails.setmTariffCode(cr.getInt(cr.getColumnIndex(COL_TariffCode)));
				CustDetails.setmTarifString(cr.getString(cr.getColumnIndex(COL_TarifString)));     
				CustDetails.setmTaxAmt(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TaxAmt))));
				CustDetails.setmTaxFlag(cr.getString(cr.getColumnIndex(COL_TaxFlag)));
				CustDetails.setmTcName(cr.getString(cr.getColumnIndex(COL_TcName)));
				CustDetails.setmTEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TEc))));
				CustDetails.setmTFc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TFc))));
				CustDetails.setmThirtyFlag(cr.getString(cr.getColumnIndex(COL_ThirtyFlag)));
				CustDetails.setmTourplanId(cr.getString(cr.getColumnIndex(COL_TourplanId)));
				CustDetails.setmTvmMtr(cr.getString(cr.getColumnIndex(COL_TvmMtr)));
				CustDetails.setmTvmPFtype(cr.getString(cr.getColumnIndex(COL_TvmPFtype)));
				//CustDetails.setmUIDR_Slab(cr.getInt(cr.getColumnIndex(COL_UID_Slab)));
				CustDetails.setmUnits(cr.getString(cr.getColumnIndex(COL_Units)));
				CustDetails.setMmUID(cr.getInt(cr.getColumnIndex(COL_UID_S_BcData)));//punit 25022014
				CustDetails.setmMeter_Present_Flag(cr.getString(cr.getColumnIndex(COL_METER_PRESENT_FLAG)) == null ? 0 : cr.getInt(cr.getColumnIndex(COL_METER_PRESENT_FLAG)));
				CustDetails.setmMtr_Not_Visible(cr.getString(cr.getColumnIndex(COL_MTR_NOT_VISIBLE)) == null ? 0 : cr.getInt(cr.getColumnIndex(COL_MTR_NOT_VISIBLE)));

				CustDetails.setmDLTEc_GoK(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_DLTEc_GoK))));//31-12-2014
				CustDetails.setmOldTecBill(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_OldTecBill))));//Nitish on 21-04-2016

				//30-07-2015
				CustDetails.setmAadharNo(cr.getString(cr.getColumnIndex(COL_AadharNo)));
				CustDetails.setmVoterIdNo(cr.getString(cr.getColumnIndex(COL_VoterIdNo)));				
				CustDetails.setmMtrMakeFlag(cr.getInt(cr.getColumnIndex(COL_MtrMakeFlag)));
				CustDetails.setmMtrBodySealFlag(cr.getInt(cr.getColumnIndex(COL_MtrBodySealFlag)));
				CustDetails.setmMtrTerminalCoverFlag(cr.getInt(cr.getColumnIndex(COL_MtrTerminalCoverFlag)));
				CustDetails.setmMtrTerminalCoverSealedFlag(cr.getInt(cr.getColumnIndex(COL_MtrTerminalCoverSealedFlag)));


				CustDetails.setmFACValue(cr.getString(cr.getColumnIndex(COL_FACValue)));//19-12-2015
				//CustDetails.setmUID(cr.getInt(cr.getColumnIndex(COL_UID_Slab)));
				//CustDetails.setMmConnectionNo(cr.getString(cr.getColumnIndex(COL_ConnectionNo_Slab)));
				//CustDetails.setMmRRNo(cr.getString(cr.getColumnIndex(COL_RRNo_Slab)));
				CustDetails.setMmECRate_Count(cr.getInt(cr.getColumnIndex(COL_ECRate_Count)));
				CustDetails.setMmECRate_Row(cr.getInt(cr.getColumnIndex(COL_ECRate_Row)));
				CustDetails.setMmFCRate_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FCRate_1))));
				CustDetails.setMmCOL_FCRate_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FCRate_2))));
				CustDetails.setMmUnits_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_1))));
				CustDetails.setMmUnits_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_2))));
				CustDetails.setMmUnits_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_3))));
				CustDetails.setMmUnits_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_4))));
				CustDetails.setMmUnits_5(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_5))));
				CustDetails.setMmUnits_6(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_6))));
				CustDetails.setMmEC_Rate_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_1))));
				CustDetails.setMmEC_Rate_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_2))));
				CustDetails.setMmEC_Rate_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_3))));
				CustDetails.setMmEC_Rate_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_4))));
				CustDetails.setMmEC_Rate_5(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_5))));
				CustDetails.setMmEC_Rate_6(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_6))));
				CustDetails.setMmEC_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_1))));
				CustDetails.setMmEC_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_2))));
				CustDetails.setMmEC_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_3))));
				CustDetails.setMmEC_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_4))));
				CustDetails.setMmEC_5(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_5))));
				CustDetails.setMmEC_6(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_6))));
				CustDetails.setMmFC_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_1))));
				CustDetails.setMmFC_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_2))));
				CustDetails.setMmTEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TEc_Slab))));
				CustDetails.setMmEC_FL_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_1))));
				CustDetails.setMmEC_FL_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_2))));
				CustDetails.setMmEC_FL_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_3))));
				CustDetails.setMmEC_FL_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_4))));
				CustDetails.setMmEC_FL(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL))));
				CustDetails.setMmnew_TEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_new_TEc))));
				CustDetails.setMmold_TEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_old_TEc))));
				
				//23-06-2021 for FC
				CustDetails.setMmNEWFC_UNIT1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT1))));
				CustDetails.setMmNEWFC_UNIT2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT2))));
				CustDetails.setMmNEWFC_UNIT3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT3))));
				CustDetails.setMmNEWFC_UNIT4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT4))));
				CustDetails.setMmNEWFC_Rate3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_Rate3))));
				CustDetails.setMmNEWFC_Rate4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_Rate4))));
				CustDetails.setMmNEWFC3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC3))));
				CustDetails.setMmNEWFC4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC4))));
				CustDetails.setmFC_Slab_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_Slab_3))));


			}//Cursor IF END
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("punit", e.toString());
			//throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}

	}
	//Nitish 24-02-2014 //Get ConnectionId and RRNo
	public AutoDDLAdapter GetConnIdRRNo() throws Exception
	{
		AutoDDLAdapter conList = null;
		cr=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetConnIdRRNo();
			cr = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());		
			for(int i=1; i<=cr.getCount();++i)
			{
				if(conList == null)
				{
					conList = new AutoDDLAdapter(mcntx, new ArrayList<DDLItem>(), 5);
				}
				if(i==1)
					cr.moveToFirst();
				else
					cr.moveToNext();		
				conList.AddItem(cr.getString(cr.getColumnIndex("ConnId")), cr.getString(cr.getColumnIndex("ConnIdRRNo")));

			}
		} catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return conList;
	}
	//Nitish 05-03-2014
	public ArrayList<ReportBilling> GetReportBillingList() throws Exception
	{
		ArrayList<ReportBilling> mList = new  ArrayList<ReportBilling>();

		Cursor crRep=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetReportBillingList();
			crRep = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(crRep!=null)
			{
				for(int i=1; i<=crRep.getCount();++i)
				{

					if(mList==null)
					{
						mList=new ArrayList<ReportBilling>();
					}
					if(i==1)				
						crRep.moveToFirst();				
					else
						crRep.moveToNext();

					ReportBilling rb = new ReportBilling();			
					rb.setmBillDate(crRep.getString(crRep.getColumnIndex("BilledDate")));
					rb.setmMRName(crRep.getString(crRep.getColumnIndex("MRName")));
					rb.setmTotalInstallations(crRep.getInt(crRep.getColumnIndex("TotalInstallations")));
					rb.setmBilled(crRep.getInt(crRep.getColumnIndex("Billed")));
					rb.setmNotBilled(crRep.getInt(crRep.getColumnIndex("NotBilled")));
					mList.add(rb);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(crRep!=null)
			{
				crRep.close();
			}
		}
		return mList;
	}	
	//End Nitish 05-03-2014	
	//Nitish 07-03-2014
	public ArrayList<ReportStatus> GetReportStatusList() throws Exception
	{
		ArrayList<ReportStatus> mList = new ArrayList<ReportStatus>();
		Cursor crRep=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetReportStatusList();
			crRep = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(crRep!=null)
			{
				for(int i=1; i<=crRep.getCount();++i)
				{

					if(mList==null)
					{
						mList=new ArrayList<ReportStatus>();
					}
					if(i==1)				
						crRep.moveToFirst();				
					else
						crRep.moveToNext();

					ReportStatus rb = new ReportStatus();								
					rb.setmBillDate(crRep.getString(crRep.getColumnIndex("BilledDate")));
					rb.setmMRName(crRep.getString(crRep.getColumnIndex("MRName")));
					rb.setmTotalInstallations(crRep.getInt(crRep.getColumnIndex("TotalInstallations")));
					rb.setmBilled(crRep.getInt(crRep.getColumnIndex("Billed")));
					rb.setmNotBilled(crRep.getInt(crRep.getColumnIndex("NotBilled")));
					rb.setmNormal(crRep.getInt(crRep.getColumnIndex("Normal")));
					rb.setmDL(crRep.getInt(crRep.getColumnIndex("DL")));
					rb.setmMNR(crRep.getInt(crRep.getColumnIndex("MNR")));
					rb.setmDO(crRep.getInt(crRep.getColumnIndex("DO")));
					rb.setmMCH(crRep.getInt(crRep.getColumnIndex("MCH")));
					rb.setmRNF(crRep.getInt(crRep.getColumnIndex("RNF")));
					rb.setmVA(crRep.getInt(crRep.getColumnIndex("VA")));
					rb.setmDIS(crRep.getInt(crRep.getColumnIndex("DIS")));
					rb.setmMS(crRep.getInt(crRep.getColumnIndex("MS")));
					rb.setmDIR(crRep.getInt(crRep.getColumnIndex("DIR")));
					rb.setmMB(crRep.getInt(crRep.getColumnIndex("MB")));
					rb.setmNT(crRep.getInt(crRep.getColumnIndex("NT")));

					mList.add(rb);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(crRep!=null)
			{
				crRep.close();
			}
		}
		return mList;
	}	
	public ArrayList<ReportNotBilled> GetReportNotBilledList() throws Exception
	{
		ArrayList<ReportNotBilled> mList = new ArrayList<ReportNotBilled>();
		Cursor crRep=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetReportNotBilledList();
			crRep = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());
			if(crRep!=null)
			{
				for(int i=1; i<=crRep.getCount();++i)
				{

					if(mList==null)
					{
						mList=new ArrayList<ReportNotBilled>();
					}
					if(i==1)				
						crRep.moveToFirst();				
					else
						crRep.moveToNext();

					ReportNotBilled rb = new ReportNotBilled();								
					rb.setmConNo(crRep.getString(crRep.getColumnIndex("ConnectionNo")));
					rb.setmRRNo(crRep.getString(crRep.getColumnIndex("RRNo")));
					rb.setmTourplanId(crRep.getString(crRep.getColumnIndex("TourPlanId")));


					mList.add(rb);
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(crRep!=null)
			{
				crRep.close();
			}
		}
		return mList;
	}	
	//Nitish 12-03-2014	
	public ReportGPRS GetReportGPRSStatus() throws Exception
	{
		ReportGPRS rb = new ReportGPRS();	
		Cursor crRep=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetReportGPRSStatus();
			crRep = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(crRep!=null)
			{
				if(crRep.getCount() > 0)//Cursor IF Start
				{							
					crRep.moveToFirst();												

					rb.setmMRName(crRep.getString(crRep.getColumnIndex("MRName")));
					rb.setmTotalInstallations(crRep.getInt(crRep.getColumnIndex("TotalInstallations")));
					rb.setmBilled(crRep.getInt(crRep.getColumnIndex("Billed")));
					rb.setmNotBilled(crRep.getInt(crRep.getColumnIndex("NotBilled")));
					rb.setmGPRS_Sent(crRep.getInt(crRep.getColumnIndex("GPRS_Sent")));				

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(crRep!=null)
			{
				crRep.close();
			}
		}
		return rb;
	}
	//End Nitish 12-03-2014	//Get all Bank List
	public AutoDDLAdapter GetBankList() throws Exception
	{
		AutoDDLAdapter bankList = null;
		Cursor crBank=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetBankList();
			crBank = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());		
			for(int i=1; i<=crBank.getCount();++i)
			{
				if(bankList==null)
				{
					bankList=new AutoDDLAdapter(mcntx,new ArrayList<DDLItem>());
				}
				if(i==1)
					crBank.moveToFirst();
				else
					crBank.moveToNext();					
				bankList.AddItem(String.valueOf(crBank.getInt(crBank.getColumnIndex(COL_BID_BANKLIST))), (crBank.getString(crBank.getColumnIndex(COL_BANKID_BANKLIST)).substring(3)));

			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(crBank!=null)
			{
				crBank.close();
			}
		}
		return bankList;
	}
	//Modified Nitish 27-04-2017 //Save Collection Details for Connection in Collection_Table //Modified 26-09-2014
	public int CollectionSave() 
	{
		int rtvalue = 0;
		int sReceiptNo ;
		Cursor crReceiptNo = null;
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();
		CommonFunction cFun = new CommonFunction();
		ReadCollection sbc = CollectionObject.GetCollectionObject();
		CashCounterObject cc = dh.GetCashCounterDetails();

		try
		{	

			//Get maximum of ReceiptNo
			QueryParameters qParamReceiptNo = StoredProcedure.GetMaxReceiptNo(cc.getmBatch_No());			
			crReceiptNo=getReadableDatabase().rawQuery(qParamReceiptNo.getSql(), qParamReceiptNo.getSelectionArgs());				

			//ReceiptNo Creation Logic
			if( crReceiptNo.getCount()==0) //If Collection_Table  is empty
			{			
				sReceiptNo = Integer.valueOf(cc.getmLastBatchReceiptNo().trim().substring(5, 10)) + 1;
			}	
			else  //If Collection_Table  is not empty
			{			
				crReceiptNo.moveToFirst();
				sReceiptNo = Integer.valueOf(crReceiptNo.getString(crReceiptNo.getColumnIndex("MaxReceiptNo")));	
				sReceiptNo = sReceiptNo + 1; //Increment ReceiptNo	
			}			
			//sbc.setmReceipttypeflag("R");  24-07-2014
			sbc.setmRcptCnt(1);
			sbc.setmDateTime(cFun.GetCurrentTimeWOSPChar());
			sbc.setmReceipt_No(sReceiptNo);
			sbc.setmBatch_No(cc.getmBatch_No());
			sbc.setmGvpId(cc.getmLastBatchReceiptNo().trim().substring(0,5)); //24-04-2017

			//sbc.setmBatch_No("1111122222333334444455555");
			//sbc.setmSBMNumber("1111122222");
			//sbc.setmGvpId("11111");
			sldb.beginTransaction();
			ContentValues valuesInsertCollectiontbl = new ContentValues();

			String currentdate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());//24-03-2017
			String aReceiptNo = CommonFunction.getActualReceiptNo(String.valueOf(sbc.getmReceipt_No())); //28-04-2017
			valuesInsertCollectiontbl.put("ConnectionNo", sbc.getmConnectionNo());
			valuesInsertCollectiontbl.put("RRNo", sbc.getmRRNo());
			valuesInsertCollectiontbl.put("CustomerName", sbc.getmCustomerName());
			valuesInsertCollectiontbl.put("RcptCnt", sbc.getmRcptCnt());
			valuesInsertCollectiontbl.put("Batch_No", cc.getmBatch_No());
			valuesInsertCollectiontbl.put("Receipt_No", aReceiptNo);
			valuesInsertCollectiontbl.put("DateTime", sbc.getmDateTime());
			valuesInsertCollectiontbl.put("Payment_Mode ", sbc.getmPayment_Mode());
			valuesInsertCollectiontbl.put("Arrears", String.valueOf(sbc.getmArears().add(sbc.getmIntrstOld()).add(sbc.getmArrearsOld()).setScale(2,RoundingMode.HALF_EVEN)));
			valuesInsertCollectiontbl.put("BillTotal", String.valueOf(sbc.getmBillTotal().setScale(2,RoundingMode.HALF_EVEN)));
			valuesInsertCollectiontbl.put("Paid_Amt", sbc.getmPaid_Amt());
			valuesInsertCollectiontbl.put("BankID", sbc.getmBankID());			
			valuesInsertCollectiontbl.put("ChequeDDNo", sbc.getmChequeDDNo());
			valuesInsertCollectiontbl.put("ChequeDDDate", sbc.getmChequeDDDate());
			valuesInsertCollectiontbl.put("Receipttypeflag",sbc.getmReceipttypeflag());	
			valuesInsertCollectiontbl.put("GvpId",sbc.getmGvpId());	
			valuesInsertCollectiontbl.put("SBMNumber",sbc.getmSBMNumber());	
			valuesInsertCollectiontbl.put("LocationCode",sbc.getmLocationCode());	

			//Modified Nitish 05-05-2014
			valuesInsertCollectiontbl.put("ArrearsBill_Flag",sbc.getmArrearsBill_Flag());	
			valuesInsertCollectiontbl.put("ReaderCode",sbc.getmReaderCode());

			//Modified Nitish 19-08-2014
			valuesInsertCollectiontbl.put("IODRemarks",sbc.getmIODRemarks());
			valuesInsertCollectiontbl.put("ReprintCount","0"); //08-06-2017

			long insertResult = sldb.insert("Collection_TABLE", null, valuesInsertCollectiontbl);
			if(insertResult <= 0)
			{	
				sldb.endTransaction();
				throw new Exception("Insertion failed for Collection_TABLE");
			}
			//16-03-2017
			try
			{
				if(!sbc.getmPayment_Mode().equals("1"))
				{
					String currentdate1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
					//Insert into SBM_ChequeDDImage Table			
					valuesInsertCollectiontbl.clear();
					valuesInsertCollectiontbl.put("IMEINo", sbc.getmSBMNumber());
					valuesInsertCollectiontbl.put("BatchNo", sbc.getmBatch_No());
					valuesInsertCollectiontbl.put("ReceiptNo", sbc.getmReceipt_No());
					valuesInsertCollectiontbl.put("ConnectionNo",sbc.getmConnectionNo().trim());			
					valuesInsertCollectiontbl.put("Receipttypeflag", sbc.getmReceipttypeflag());	//Present Reading Entered			
					valuesInsertCollectiontbl.put("PhotoName", sbc.getmImageName().trim());
					valuesInsertCollectiontbl.put("CreatedDate",currentdate1);			

					long insertResultImage = sldb.insert("SBM_ChequeDDImage", null, valuesInsertCollectiontbl);
					if(insertResultImage <= 0)
					{	
						sldb.endTransaction();
						throw new Exception("Insertion failed for SBM_ChequeDDImage");
					}	
				}
			}
			catch(Exception e)
			{

			}
			
			//11-05-2021 for Collection payment Status
			sbc.setmLat(String.valueOf(LoginActivity.gpsTracker.latitude));
			sbc.setmLong(String.valueOf(LoginActivity.gpsTracker.longitude));


			valuesInsertCollectiontbl.clear();

			valuesInsertCollectiontbl.put("ConId", sbc.getmConnectionNo());
			valuesInsertCollectiontbl.put("BatchNo", sbc.getmBatch_No());
			valuesInsertCollectiontbl.put("ImeiNo", sbc.getmSBMNumber());
			valuesInsertCollectiontbl.put("Lat",sbc.getmLat().trim());			
			valuesInsertCollectiontbl.put("Long", sbc.getmLong());	//Present Reading Entered			
			valuesInsertCollectiontbl.put("PaymentReasonId", sbc.getmRemarkId());
			valuesInsertCollectiontbl.put("ScheduledPayDate", sbc.getmScheduledDate());
			valuesInsertCollectiontbl.put("CreatedDate", currentdate);

			long insertResultPaymentStatus = sldb.insert("Collection_Payment_Status", null, valuesInsertCollectiontbl);
			if(insertResultPaymentStatus <= 0)
			{	
				sldb.endTransaction();
				throw new Exception("Insertion failed for Collection_Payment_Status");
			}
			//End 11-05-2021
			
			sldb.setTransactionSuccessful();
			rtvalue = 1;
			/*try
			{
				DatabaseHelperBackupDB bdBackup = new DatabaseHelperBackupDB(mcntx);
				bdBackup.BackupDBCollectionSave();//Back Up database save
			}
			catch(Exception e)
			{
				Log.d("", e.toString());
			}*/
			//return 1;

		}
		catch(Exception e)
		{
			Log.d("", e.toString());
			//return 0;
			rtvalue = 0;
		}	
		finally
		{
			sldb.endTransaction();
		}	
		return rtvalue;
	}

	//Modified Nitish 12-06-2014 //Get Max Receipt No from Collection_Table
	public int GetMaxReceiptNo(String batchno)
	{
		Cursor crRec = null;
		int recno = 0;
		try
		{
			QueryParameters qParam = StoredProcedure.GetMaxReceiptNo(batchno);
			crRec = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(crRec != null && crRec.getCount() > 0)//Cursor 1
			{
				crRec.moveToFirst();
				recno = crRec.getInt(crRec.getColumnIndex("MaxReceiptNo"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crRec!=null)
			{
				crRec.close();
			}
		}		
		return recno;
	}


	//Nitish 08-04-2014	//Get ArrayList of Data to be sent to Server
	public ArrayList<String> GetDataSendToServerColl()
	{
		ArrayList<String> alStr = new ArrayList<String>();
		Cursor crWriteColl = null;
		StringBuilder sb = new StringBuilder();
		try
		{
			QueryParameters qParam = StoredProcedure.GetDataSentToServerColl();
			crWriteColl = getReadableDatabase().rawQuery(qParam.getSql(), null);		
			if(crWriteColl != null && crWriteColl.getCount() > 0)
			{
				crWriteColl.moveToFirst();
				do 
				{
					try
					{
						WriteFileParameters wfp = new WriteFileParameters();
						String s = crWriteColl.getString(crWriteColl.getColumnIndex(COL_ConnectionNo_COLLECTION_TABLE));						
						wfp.setmReceipttypeflag_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_Receipttypeflag_COLLECTION_TABLE)));
						wfp.setmConsNo_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_ConnectionNo_COLLECTION_TABLE)));
						wfp.setmBatch_No_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_Batch_No_COLLECTION_TABLE)));
						wfp.setmReceipt_No_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_Receipt_No_COLLECTION_TABLE)));
						wfp.setmGvpId_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_GvpId_COLLECTION_TABLE)));
						wfp.setmDateTime_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_DateTime_COLLECTION_TABLE)));
						wfp.setmPayment_Mode_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_Payment_Mode_COLLECTION_TABLE)));
						wfp.setmPaid_Amt_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_Paid_Amt_COLLECTION_TABLE)));
						wfp.setmChequeDDNo_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_ChequeDDNo_COLLECTION_TABLE)));
						wfp.setmChequeDDDate_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_ChequeDDDate_COLLECTION_TABLE)));;
						wfp.setmBankID_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_BankID_COLLECTION_TABLE)));
						wfp.setmSBMNumber_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_SBMNumber_COLLECTION_TABLE)));	
						wfp.setmLocation_code_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_LocationCode_COLLECTION_TABLE)));
						//wfp.setmVer_Coll(R.string.FileVersion);
						wfp.setmVer_Coll(ConstantClass.FileVersion);//version may change make it global	
						//wfp.setmVer(((Activity)mcntx).getResources().getString(R.string.FileVersion));//version may change make it global

						sb.delete(0, sb.length());
						sb.append(wfp.getmReceipttypeflag_Coll());											
						sb.append(wfp.getmConsNo_Coll());
						sb.append(wfp.getmBatch_No_Coll());
						sb.append(wfp.getmReceipt_No_Coll());
						sb.append(wfp.getmGvpId_Coll());
						sb.append(wfp.getmDateTime_Coll());
						sb.append(wfp.getmPayment_Mode_Coll());
						sb.append(wfp.getmPaid_Amt_Coll());
						sb.append(wfp.getmChequeDDNo_Coll());
						sb.append(wfp.getmChequeDDDate_Coll());
						sb.append(wfp.getmBankID_Coll());
						sb.append(wfp.getmSBMNumber_Coll());
						sb.append(wfp.getmLocation_code_Coll());
						sb.append(wfp.getmVer_Coll());

						alStr.add(sb.toString());
					}
					catch(Exception e)
					{
						Log.d(TAG, e.toString());
					}
				}while(crWriteColl.moveToNext());
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return alStr;
	}

	//Modified Nitish 28-04-2017 //Update RcptCnt =1 once GPRS Sent
	public String UpdateRcptCnt(String ConnectionNo,String ReceiptNo)
	{
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();
		String strValue = "";
		try
		{
			ReceiptNo =ReceiptNo.trim();
			ContentValues valuesUpdateColltbl = new ContentValues();
			valuesUpdateColltbl.put("Gprs_Flag", "1");
			//sldb.update("Collection_TABLE", valuesUpdateColltbl, "ConnectionNo = ?", new String[]{ConnectionNo});
			sldb.update("Collection_TABLE", valuesUpdateColltbl, "ConnectionNo = ? and Receipt_No = ? ", new String[]{ConnectionNo,ReceiptNo});
			strValue = GPRSFlagColl();
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return strValue;
	}
	//Nitish 15-04-2014 
	public void GetPrevColl(String custId ) throws Exception
	{
		ReadCollection CustDetails = CollectionObject.GetCollectionObject();

		cr=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetPrev(custId);
			cr = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(cr.getCount() > 0)//Cursor IF Start
			{				
				cr.moveToFirst();	
				CustDetails.setmCustomerName(cr.getString(cr.getColumnIndex(COL_CustomerName)));
				CustDetails.setmArrearsOld(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ArrearsOld))));//Arrears Old
				CustDetails.setmIntrstOld(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_IntrstOld))));//Interest Old
				CustDetails.setmArears(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Arears))));	//Arrears					
				CustDetails.setmBankID(cr.getInt(cr.getColumnIndex(COL_BankID)));
				CustDetails.setmBatch_No(cr.getString(cr.getColumnIndex(COL_Batch_No)));				
				CustDetails.setmBillTotal(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_BillTotal))));			
				CustDetails.setmBlCnt(cr.getString(cr.getColumnIndex(COL_BlCnt)));//5				
				CustDetails.setmChequeDDDate(cr.getString(cr.getColumnIndex(COL_ChequeDDDate)));
				CustDetails.setmChequeDDNo(cr.getInt(cr.getColumnIndex(COL_ChequeDDNo)));
				CustDetails.setmConnectionNo(cr.getString(cr.getColumnIndex(COL_ConnectionNo)));				
				CustDetails.setmDateTime(cr.getString(cr.getColumnIndex(COL_DateTime)));				
				CustDetails.setmDueDate(cr.getString(cr.getColumnIndex(COL_DueDate)));	//5			
				CustDetails.setmGprs_Flag(cr.getInt(cr.getColumnIndex(COL_Gprs_Flag)));				
				CustDetails.setmGvpId(cr.getString(cr.getColumnIndex(COL_GvpId)));				
				CustDetails.setmLocationCode(cr.getString(cr.getColumnIndex(COL_LocationCode)));				
				CustDetails.setmPaid_Amt(cr.getInt(cr.getColumnIndex(COL_Paid_Amt)));
				CustDetails.setmPayment_Mode(cr.getString(cr.getColumnIndex(COL_Payment_Mode)));//5				
				CustDetails.setmRcptCnt(cr.getInt(cr.getColumnIndex(COL_RcptCnt)));				
				CustDetails.setmReceipt_No(cr.getInt(cr.getColumnIndex(COL_Receipt_No)));				
				CustDetails.setmReceipttypeflag(cr.getString(cr.getColumnIndex(COL_Receipttypeflag)));		
				CustDetails.setmRRNo(cr.getString(cr.getColumnIndex(COL_RRNo)));				
				CustDetails.setmSancLoad(cr.getString(cr.getColumnIndex(COL_SancLoad)));//5
				CustDetails.setmSBMNumber(cr.getString(cr.getColumnIndex(COL_SBMNumber)));				
				CustDetails.setmStatus(cr.getInt(cr.getColumnIndex(COL_Status)));//previous status				
				CustDetails.setmTariffCode(cr.getInt(cr.getColumnIndex(COL_TariffCode)));
				CustDetails.setmTarifString(cr.getString(cr.getColumnIndex(COL_TarifString)));
				CustDetails.setMmUID(cr.getInt(cr.getColumnIndex(COL_UID_S_BcData)));
				//Nitish 05-05-2015
				CustDetails.setmReaderCode(cr.getString(cr.getColumnIndex(COL_ReaderCode)));

				//Nitish 22-05-2015
				CustDetails.setmSancHp(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SancHp))));
				CustDetails.setmSancKw(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SancKw))));

				//Nitish 11-06-2014
				CustDetails.setmBOBilled_Amount(cr.getString(cr.getColumnIndex(COL_BOBilled_Amount)));
				CustDetails.setmBOBillFlag(cr.getInt(cr.getColumnIndex(COL_BOBillFlag)));

				//Nitish 19-08-2014
				//CustDetails.setmIODRemarks(cr.getString(cr.getColumnIndex(COL_IODRemarks)));	

				//Nitish 29-03-2016
				CustDetails.setmIODRemarks(cr.getString(cr.getColumnIndex(COL_OldConnID)));				


			}//Cursor IF END
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("punit", e.toString());
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}

	}
	//Nitish 15-04-2014 Modified 24-07-2014
	public void GetValueOnBillingPageColl(String rcpttype) throws Exception
	{
		ReadCollection CustDetails = CollectionObject.GetCollectionObject();

		cr=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetValueOnBillingPageColl(rcpttype);
			cr = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(cr.getCount() > 0)//Cursor IF Start
			{				
				cr.moveToFirst();					


				CustDetails.setmCustomerName(cr.getString(cr.getColumnIndex(COL_CustomerName)));
				CustDetails.setmArrearsOld(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ArrearsOld))));//Arrears Old
				CustDetails.setmIntrstOld(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_IntrstOld))));//Interest Old
				CustDetails.setmArears(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Arears))));	//Arrears					
				CustDetails.setmBankID(cr.getInt(cr.getColumnIndex(COL_BankID)));
				CustDetails.setmBatch_No(cr.getString(cr.getColumnIndex(COL_Batch_No)));				
				CustDetails.setmBillTotal(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_BillTotal))));				
				CustDetails.setmBlCnt(cr.getString(cr.getColumnIndex(COL_BlCnt)));//5				
				CustDetails.setmChequeDDDate(cr.getString(cr.getColumnIndex(COL_ChequeDDDate)));
				CustDetails.setmChequeDDNo(cr.getInt(cr.getColumnIndex(COL_ChequeDDNo)));
				CustDetails.setmConnectionNo(cr.getString(cr.getColumnIndex(COL_ConnectionNo)));			
				CustDetails.setmDateTime(cr.getString(cr.getColumnIndex(COL_DateTime)));				
				CustDetails.setmDueDate(cr.getString(cr.getColumnIndex(COL_DueDate)));//5				
				CustDetails.setmGprs_Flag(cr.getInt(cr.getColumnIndex(COL_Gprs_Flag)));				
				CustDetails.setmGvpId(cr.getString(cr.getColumnIndex(COL_GvpId)));				
				CustDetails.setmLocationCode(cr.getString(cr.getColumnIndex(COL_LocationCode)));				
				CustDetails.setmPaid_Amt(cr.getInt(cr.getColumnIndex(COL_Paid_Amt)));
				CustDetails.setmPayment_Mode(cr.getString(cr.getColumnIndex(COL_Payment_Mode)));//5				
				CustDetails.setmRcptCnt(cr.getInt(cr.getColumnIndex(COL_RcptCnt)));				
				CustDetails.setmReceipt_No(cr.getInt(cr.getColumnIndex(COL_Receipt_No)));				
				CustDetails.setmReceipttypeflag(cr.getString(cr.getColumnIndex(COL_Receipttypeflag)));				
				CustDetails.setmRRNo(cr.getString(cr.getColumnIndex(COL_RRNo)));				
				CustDetails.setmSancLoad(cr.getString(cr.getColumnIndex(COL_SancLoad)));//5
				CustDetails.setmSBMNumber(cr.getString(cr.getColumnIndex(COL_SBMNumber)));				
				CustDetails.setmStatus(cr.getInt(cr.getColumnIndex(COL_Status)));				        
				CustDetails.setmTariffCode(cr.getInt(cr.getColumnIndex(COL_TariffCode)));
				CustDetails.setmTarifString(cr.getString(cr.getColumnIndex(COL_TarifString)));  
				CustDetails.setMmUID(cr.getInt(cr.getColumnIndex(COL_UID_S_BcData)));
				//Nitish 05-05-2015
				CustDetails.setmReaderCode(cr.getString(cr.getColumnIndex(COL_ReaderCode)));

				//Nitish 22-05-2015
				CustDetails.setmSancHp(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SancHp))));
				CustDetails.setmSancKw(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SancKw))));

				//Nitish 11-06-2014
				CustDetails.setmBOBilled_Amount(cr.getString(cr.getColumnIndex(COL_BOBilled_Amount)));
				CustDetails.setmBOBillFlag(cr.getInt(cr.getColumnIndex(COL_BOBillFlag)));

				//Nitish 19-08-2014
				//CustDetails.setmIODRemarks(cr.getString(cr.getColumnIndex(COL_IODRemarks)));		
				//Nitish 29-03-2016
				CustDetails.setmIODRemarks(cr.getString(cr.getColumnIndex(COL_OldConnID)));


			}//Cursor IF END
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("punit", e.toString());
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}

	}
	//Nitish 15-04-2014
	public void GetAllDatafromDbColl(String custId ) throws Exception
	{
		ReadCollection CustDetails = CollectionObject.GetCollectionObject();


		cr=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetAllDatafromDb(custId);
			cr = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(cr.getCount() > 0)//Cursor IF Start
			{
				cr.moveToFirst();	

				CustDetails.setmCustomerName(cr.getString(cr.getColumnIndex(COL_CustomerName)));
				CustDetails.setmArrearsOld(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ArrearsOld))));//Arrears Old
				CustDetails.setmIntrstOld(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_IntrstOld))));//Interest Old
				CustDetails.setmArears(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Arears))));	//Arrears				
				CustDetails.setmBankID(cr.getInt(cr.getColumnIndex(COL_BankID)));
				CustDetails.setmBatch_No(cr.getString(cr.getColumnIndex(COL_Batch_No)));				
				CustDetails.setmBillTotal(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_BillTotal))));				
				CustDetails.setmBlCnt(cr.getString(cr.getColumnIndex(COL_BlCnt)));	//5			
				CustDetails.setmChequeDDDate(cr.getString(cr.getColumnIndex(COL_ChequeDDDate)));
				CustDetails.setmChequeDDNo(cr.getInt(cr.getColumnIndex(COL_ChequeDDNo)));
				CustDetails.setmConnectionNo(cr.getString(cr.getColumnIndex(COL_ConnectionNo)));				
				CustDetails.setmDateTime(cr.getString(cr.getColumnIndex(COL_DateTime)));				
				CustDetails.setmDueDate(cr.getString(cr.getColumnIndex(COL_DueDate)));//5				
				CustDetails.setmGprs_Flag(cr.getInt(cr.getColumnIndex(COL_Gprs_Flag)));				
				CustDetails.setmGvpId(cr.getString(cr.getColumnIndex(COL_GvpId)));				
				CustDetails.setmLocationCode(cr.getString(cr.getColumnIndex(COL_LocationCode)));				
				CustDetails.setmPaid_Amt(cr.getInt(cr.getColumnIndex(COL_Paid_Amt)));
				CustDetails.setmPayment_Mode(cr.getString(cr.getColumnIndex(COL_Payment_Mode)));//5			
				CustDetails.setmRcptCnt(cr.getInt(cr.getColumnIndex(COL_RcptCnt)));				
				CustDetails.setmReceipt_No(cr.getInt(cr.getColumnIndex(COL_Receipt_No)));				
				CustDetails.setmReceipttypeflag(cr.getString(cr.getColumnIndex(COL_Receipttypeflag)));				
				CustDetails.setmRRNo(cr.getString(cr.getColumnIndex(COL_RRNo)));				
				CustDetails.setmSancLoad(cr.getString(cr.getColumnIndex(COL_SancLoad)));//5
				CustDetails.setmSBMNumber(cr.getString(cr.getColumnIndex(COL_SBMNumber)));				
				CustDetails.setmStatus(cr.getInt(cr.getColumnIndex(COL_Status)));//Previous Status				        
				CustDetails.setmTariffCode(cr.getInt(cr.getColumnIndex(COL_TariffCode)));
				CustDetails.setmTarifString(cr.getString(cr.getColumnIndex(COL_TarifString)));		
				CustDetails.setMmUID(cr.getInt(cr.getColumnIndex(COL_UID_S_BcData)));
				//Nitish 05-05-2015
				CustDetails.setmReaderCode(cr.getString(cr.getColumnIndex(COL_ReaderCode)));

				//Nitish 22-05-2015
				CustDetails.setmSancHp(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SancHp))));
				CustDetails.setmSancKw(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SancKw))));

				//Nitish 11-06-2014
				CustDetails.setmBOBilled_Amount(cr.getString(cr.getColumnIndex(COL_BOBilled_Amount)));
				CustDetails.setmBOBillFlag(cr.getInt(cr.getColumnIndex(COL_BOBillFlag)));

				//Nitish 19-08-2014
				//CustDetails.setmIODRemarks(cr.getString(cr.getColumnIndex(COL_IODRemarks)));		
				//Nitish 29-03-2016
				CustDetails.setmIODRemarks(cr.getString(cr.getColumnIndex(COL_OldConnID)));


			}//Cursor IF END
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("punit", e.toString());
			//throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
	}	
	//Nitish 19-04-2014	//Get Collection Amount for that batch
	public String GetCollectionAmount(String batchno)
	{
		Cursor crBat = null;
		String bat = "";
		try
		{
			QueryParameters qParam = StoredProcedure.GetCollectionAmount(batchno);
			crBat = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				bat = crBat.getString(crBat.getColumnIndex("CollectionAmt"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return bat;
	}
	//Nitish 19-04-2014 // Get Location Code
	public String GetLocationCode()
	{
		Cursor crLoc = null;
		String loccode = "";
		try
		{
			QueryParameters qParam = StoredProcedure.GetLocationCode();
			crLoc = getReadableDatabase().rawQuery(qParam.getSql(), null);	
			if(crLoc != null && crLoc.getCount() > 0)//Cursor 1
			{
				crLoc.moveToFirst();
				loccode = crLoc.getString(crLoc.getColumnIndex("LocationCode"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crLoc!=null)
			{
				crLoc.close();
			}
		}		
		return loccode;
	}

	//Modified Nitish 17-06-2017 //Save CashCounterDetails in CASHCOUNTER_DETAILS table //Modified 26-09-2014
	public int CashCounterDetailsSave(String imeino, String simno,String str[] )
	{		
		int rtvalue = 0;
		DatabaseHelper dh1 = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb1 = dh1.getWritableDatabase();
		String loc = dh1.GetLocationCode();
		try
		{
			sldb1.beginTransaction();
			ContentValues valuesInsertCashCounterDetails = new ContentValues();	
			//18042014|500000|1000|2100|51101051804148628090215465290006|041815322014|0001000001
			//sp[0] = 18042014 --BatchDate
			//sp[1] = 500000 --Cash Limit
			//sp[2] = 1000 --Start Time
			//sp[3] = 2100 --End Time
			//sp[4] = 51101051804148628090215465290006 --BatchNo
			//sp[5] = 041815322014 -datetime
			valuesInsertCashCounterDetails.put("IMEINo", imeino);
			valuesInsertCashCounterDetails.put("SIMNo", simno);	
			valuesInsertCashCounterDetails.put("BatchDate", str[0]);
			valuesInsertCashCounterDetails.put("CashLimit", str[1]);			
			valuesInsertCashCounterDetails.put("StartTime", str[2]);
			valuesInsertCashCounterDetails.put("EndTime ", str[3]);
			valuesInsertCashCounterDetails.put("Batch_No", str[4]);
			valuesInsertCashCounterDetails.put("DateTime", str[5]);	
			if(str[6].trim().toString().length()==14)
				valuesInsertCashCounterDetails.put("LastBatchReceiptNo", str[6].substring(4, 14));		//30-06-2017
			else
				valuesInsertCashCounterDetails.put("LastBatchReceiptNo", str[6]);		
			valuesInsertCashCounterDetails.put("RevenueFlag", str[7]);	
			valuesInsertCashCounterDetails.put("MiscFlag", str[8]);	
			valuesInsertCashCounterDetails.put("ChqFlag", str[9]);				
			valuesInsertCashCounterDetails.put("PartPaymentFlag", str[10]);		
			valuesInsertCashCounterDetails.put("PartPaymentPerc", str[11]);	
			valuesInsertCashCounterDetails.put("CDLimit", str[12]);	
			valuesInsertCashCounterDetails.put("MRName", str[13]); //26-06-2017	
			valuesInsertCashCounterDetails.put("ReprintFlag", str[14]); //26-06-2017	
			valuesInsertCashCounterDetails.put("LocationCode",loc);			
			valuesInsertCashCounterDetails.put("CashCounterOpen", "1");				

			long insertResult = sldb1.insert("CashCounter_Details", null, valuesInsertCashCounterDetails);
			if(insertResult <= 0)
			{	
				sldb1.endTransaction();
				throw new Exception("Insertion failed for CashCounter_Details Table");
			}				
			sldb1.setTransactionSuccessful();
			try
			{
				DatabaseHelperBackupDB bdBackup = new DatabaseHelperBackupDB(mcntx);
				bdBackup.BackupDBCashCounterDetailsSave(imeino,simno,loc,str);//Back Up database save	
			}
			catch(Exception e)
			{
				Log.d("", e.toString());
			}
			rtvalue = 1;

		}
		catch(Exception e)
		{
			Log.d("", e.toString());
			rtvalue = 0;			
		}	
		finally
		{
			sldb1.endTransaction();
		}
		return rtvalue;		
	}		
	//Nitish Modified 17-06-2017 // Get CashCounterDetails for active Batch(last record of  CASHCOUNTER_DETAILS table)
	public CashCounterObject GetCashCounterDetails() 
	{
		CashCounterObject cc = new CashCounterObject();	
		Cursor crCC=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetCashCounterDetails();
			crCC = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(crCC!=null)
			{
				if(crCC.getCount() > 0)//Cursor IF Start
				{							
					crCC.moveToFirst();												

					cc.setmBatch_No(crCC.getString(crCC.getColumnIndex(COL_Batch_No_CASHCOUNTER_DETAILS)));
					cc.setmBatch_Date(crCC.getString(crCC.getColumnIndex(COL_BatchDate_CASHCOUNTER_DETAILS)));
					cc.setmCashLimit(crCC.getString(crCC.getColumnIndex(COL_CashLimit_CASHCOUNTER_DETAILS)));
					cc.setmStartTime(crCC.getString(crCC.getColumnIndex(COL_StartTime_CASHCOUNTER_DETAILS)));
					cc.setmEndTime(crCC.getString(crCC.getColumnIndex(COL_EndTime_CASHCOUNTER_DETAILS)));
					cc.setmSIMNo(crCC.getString(crCC.getColumnIndex(COL_SIMNo_CASHCOUNTER_DETAILS)));
					cc.setmExtensionFlag(crCC.getString(crCC.getColumnIndex("ExtFlag")));
					cc.setmLastBatchReceiptNo(crCC.getString(crCC.getColumnIndex(COL_LastBatchReceiptNo_CASHCOUNTER_DETAILS))); //27-04-2017
					cc.setmRevFlag(crCC.getString(crCC.getColumnIndex(COL_RevenueFlag_CASHCOUNTER_DETAILS)));
					cc.setmMiscFlag(crCC.getString(crCC.getColumnIndex(COL_MiscFlag_CASHCOUNTER_DETAILS)));
					cc.setmChqFlag(crCC.getString(crCC.getColumnIndex(COL_ChqFlag_CASHCOUNTER_DETAILS)));
					cc.setmPartPayFlag(crCC.getString(crCC.getColumnIndex(COL_PartPaymentFlag_CASHCOUNTER_DETAILS)));
					cc.setmPartPayPerc(crCC.getString(crCC.getColumnIndex(COL_PartPaymentPerc_CASHCOUNTER_DETAILS)));					
					cc.setmCDLimit(crCC.getString(crCC.getColumnIndex(COL_CDLimit_CASHCOUNTER_DETAILS)));
					cc.setmMRName(crCC.getString(crCC.getColumnIndex(COL_MRName_CASHCOUNTER_DETAILS)));
					cc.setmReprintFlag(crCC.getString(crCC.getColumnIndex(COL_ReprintFlag_CASHCOUNTER_DETAILS)));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception

		}
		finally
		{
			if(crCC!=null)
			{
				crCC.close();
			}
		}
		return cc;
	}
	//Nitish 03-05-2014 //Set CashCounterOpen=0 for that BatchNo in CashCounter_Details table //Modified 26-09-2014
	public void CloseCashCounterFlag(String BatchNo)
	{		
		String currentdatetime = CommonFunction.GetCurrentDateTime(); 	
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();
		try
		{
			BatchNo = BatchNo.trim();
			ContentValues valuesUpdateColltbl = new ContentValues();
			valuesUpdateColltbl.put("CashCounterOpen", "0");
			valuesUpdateColltbl.put("CounterCloseDateTime", currentdatetime);
			//sldb.update("Collection_TABLE", valuesUpdateColltbl, "ConnectionNo = ?", new String[]{ConnectionNo});
			sldb.update("CashCounter_Details", valuesUpdateColltbl, "Batch_No = ? ", new String[]{BatchNo});

		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	//Nitish 03-05-2014 Modified //24-07-2014		
	//Get Receipt Count For Connection. returns 1 if collection made else returns 0 
	public int GetRcptCntForConnection(String connid,String rcpttype)
	{
		Cursor crRec = null;
		int reccnt = 0;
		try
		{
			QueryParameters qParam = StoredProcedure.GetRcptCntForConnection(connid,rcpttype);
			crRec = getReadableDatabase().rawQuery(qParam.getSql(),  qParam.getSelectionArgs());
			if(crRec != null && crRec.getCount() > 0)//Cursor 1
			{
				crRec.moveToFirst();
				reccnt = crRec.getInt(crRec.getColumnIndex(COL_RcptCnt_COLLECTION_TABLE));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crRec!=null)
			{
				crRec.close();
			}
		}		
		return reccnt;
	}
	//Nitish 03-05-2014 //Get BankName (BANKID) from BID
	public String GetBankName(int bid)
	{
		Cursor crLoc = null;
		String bank = "";
		try
		{
			QueryParameters qParam = StoredProcedure.GetBankName(bid);
			crLoc = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());
			if(crLoc != null && crLoc.getCount() > 0)//Cursor 1
			{
				crLoc.moveToFirst();
				bank = crLoc.getString(crLoc.getColumnIndex(COL_BANKID_BANKLIST));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crLoc!=null)
			{
				crLoc.close();
			}
		}		
		return bank;
	}
	//Modified Nitish 29-04-2017 //Get Collection Details for Connection No //Modified 19-08-2014
	public void GetCollectionForConnId(String custId,String rcpttype) throws Exception
	{
		ReadCollection CustDetails = CollectionObject.GetCollectionObject();

		cr=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetCollectionForConnId(custId,rcpttype);
			cr = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(cr.getCount() > 0)//Cursor IF Start
			{				
				cr.moveToFirst();		


				CustDetails.setmConnectionNo(cr.getString(cr.getColumnIndex(COL_ConnectionNo_COLLECTION_TABLE)));
				CustDetails.setmRRNo(cr.getString(cr.getColumnIndex(COL_RRNo_COLLECTION_TABLE)));			
				CustDetails.setmCustomerName(cr.getString(cr.getColumnIndex(COL_CustomerName_COLLECTION_TABLE)));				
				CustDetails.setmRcptCnt(cr.getInt(cr.getColumnIndex(COL_RcptCnt_COLLECTION_TABLE)));				
				CustDetails.setmBatch_No(cr.getString(cr.getColumnIndex(COL_Batch_No_COLLECTION_TABLE)));	
				CustDetails.setmReceipt_No(Integer.valueOf(cr.getString(cr.getColumnIndex(COL_Receipt_No_COLLECTION_TABLE)))); //29-04-2017
				CustDetails.setmDateTime(cr.getString(cr.getColumnIndex(COL_DateTime_COLLECTION_TABLE)));
				CustDetails.setmPayment_Mode(cr.getString(cr.getColumnIndex(COL_Payment_Mode_COLLECTION_TABLE)));//5
				CustDetails.setmArears(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Arrears_COLLECTION_TABLE))));	//Arrears					
				CustDetails.setmBillTotal(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_BillTotal_COLLECTION_TABLE))));
				CustDetails.setmPaid_Amt(cr.getInt(cr.getColumnIndex(COL_Paid_Amt_COLLECTION_TABLE)));
				CustDetails.setmBankID(cr.getInt(cr.getColumnIndex(COL_BankID_COLLECTION_TABLE)));
				CustDetails.setmChequeDDNo(cr.getInt(cr.getColumnIndex(COL_ChequeDDNo_COLLECTION_TABLE)));
				CustDetails.setmChequeDDDate(cr.getString(cr.getColumnIndex(COL_ChequeDDDate_COLLECTION_TABLE)));
				CustDetails.setmReceipttypeflag(cr.getString(cr.getColumnIndex(COL_Receipttypeflag_COLLECTION_TABLE)));				
				CustDetails.setmGvpId(cr.getString(cr.getColumnIndex(COL_GvpId_COLLECTION_TABLE)));	
				CustDetails.setmSBMNumber(cr.getString(cr.getColumnIndex(COL_SBMNumber_COLLECTION_TABLE)));	
				CustDetails.setmLocationCode(cr.getString(cr.getColumnIndex(COL_LocationCode_COLLECTION_TABLE)));	
				CustDetails.setmGprs_Flag(cr.getInt(cr.getColumnIndex(COL_Gprs_Flag_COLLECTION_TABLE)));	
				CustDetails.setmArrearsBill_Flag(cr.getInt(cr.getColumnIndex(COL_ArrearsBill_Flag_COLLECTION_TABLE)));
				CustDetails.setmReaderCode(cr.getString(cr.getColumnIndex(COL_ReaderCode_COLLECTION_TABLE)));
				CustDetails.setmIODRemarks(cr.getString(cr.getColumnIndex(COL_IODRemarks_COLLECTION_TABLE)));

				CustDetails.setmReprintCount(cr.getInt(cr.getColumnIndex(COL_ReprintCount_COLLECTION_TABLE)));//08-06-2017
			}//Cursor IF END			
			else //24-07-2014  If rcpt is not collected Reload Object
			{
				this.GetPrevColl(String.valueOf(CustDetails.getMmUID()));

			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("Nitish", e.toString());
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
	}
	//Nitish 05-05-2014 //GET GPRS Collection count and GPRS SENT Count
	public String GPRSFlagColl()
	{

		Cursor crCount = null;
		String strValue = "";
		try	
		{
			QueryParameters qParam1 = StoredProcedure.GetCountGPRSSentConnectionColl();
			crCount = getReadableDatabase().rawQuery(qParam1.getSql(), null);
			if(crCount != null && crCount.getCount() > 0)
			{
				crCount.moveToFirst();
				strValue = crCount.getString(crCount.getColumnIndex("COUNT"));
			}
			else
			{
				strValue = "0";
			}
			strValue += "/";
			QueryParameters qParam = StoredProcedure.GetCountforColl();
			crCount = getReadableDatabase().rawQuery(qParam.getSql(), null);
			if(crCount != null && crCount.getCount() > 0)
			{
				crCount.moveToFirst();
				strValue += crCount.getString(crCount.getColumnIndex("COUNT"));
			}
			else
			{
				strValue += "0";
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return strValue;
	}
	//Nitish 06-05-2014 //Get all details of receipts for active BatchNo and BatchDate
	public ArrayList<ReadCollection> GetReportBescomCopyPrint(String batchno) throws Exception
	{
		ArrayList<ReadCollection> mList = new ArrayList<ReadCollection>();
		Cursor cr=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetReportBescomCopyPrint(batchno);
			cr = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(cr!=null)
			{
				for(int i=1; i<=cr.getCount();++i)
				{

					if(mList==null)
					{
						mList=new ArrayList<ReadCollection>();
					}
					if(i==1)				
						cr.moveToFirst();				
					else
						cr.moveToNext();

					ReadCollection CustDetails = new ReadCollection();								
					CustDetails.setmConnectionNo(cr.getString(cr.getColumnIndex(COL_ConnectionNo_COLLECTION_TABLE)));
					CustDetails.setmRRNo(cr.getString(cr.getColumnIndex(COL_RRNo_COLLECTION_TABLE)));			
					CustDetails.setmCustomerName(cr.getString(cr.getColumnIndex(COL_CustomerName_COLLECTION_TABLE)));				
					CustDetails.setmRcptCnt(cr.getInt(cr.getColumnIndex(COL_RcptCnt_COLLECTION_TABLE)));	
					CustDetails.setmBatch_No(cr.getString(cr.getColumnIndex(COL_Batch_No_COLLECTION_TABLE)));	
					CustDetails.setmReceipt_No(Integer.valueOf(cr.getString(cr.getColumnIndex(COL_Receipt_No_COLLECTION_TABLE)))); //29-04-2017
					CustDetails.setmDateTime(cr.getString(cr.getColumnIndex(COL_DateTime_COLLECTION_TABLE)));
					CustDetails.setmPayment_Mode(cr.getString(cr.getColumnIndex(COL_Payment_Mode_COLLECTION_TABLE)));//5
					CustDetails.setmArears(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Arrears_COLLECTION_TABLE))));	//Arrears					
					CustDetails.setmBillTotal(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_BillTotal_COLLECTION_TABLE))));
					CustDetails.setmPaid_Amt(cr.getInt(cr.getColumnIndex(COL_Paid_Amt_COLLECTION_TABLE)));
					CustDetails.setmBankID(cr.getInt(cr.getColumnIndex(COL_BankID_COLLECTION_TABLE)));
					CustDetails.setmChequeDDNo(cr.getInt(cr.getColumnIndex(COL_ChequeDDNo_COLLECTION_TABLE)));
					CustDetails.setmChequeDDDate(cr.getString(cr.getColumnIndex(COL_ChequeDDDate_COLLECTION_TABLE)));
					CustDetails.setmReceipttypeflag(cr.getString(cr.getColumnIndex(COL_Receipttypeflag_COLLECTION_TABLE)));				
					CustDetails.setmGvpId(cr.getString(cr.getColumnIndex(COL_GvpId_COLLECTION_TABLE)));	
					CustDetails.setmSBMNumber(cr.getString(cr.getColumnIndex(COL_SBMNumber_COLLECTION_TABLE)));	
					CustDetails.setmLocationCode(cr.getString(cr.getColumnIndex(COL_LocationCode_COLLECTION_TABLE)));	
					CustDetails.setmGprs_Flag(cr.getInt(cr.getColumnIndex(COL_Gprs_Flag_COLLECTION_TABLE)));	
					CustDetails.setmArrearsBill_Flag(cr.getInt(cr.getColumnIndex(COL_ArrearsBill_Flag_COLLECTION_TABLE)));
					CustDetails.setmReaderCode(cr.getString(cr.getColumnIndex(COL_ReaderCode_COLLECTION_TABLE)));

					//Modified 19-08-2014
					CustDetails.setmIODRemarks(cr.getString(cr.getColumnIndex(COL_IODRemarks_COLLECTION_TABLE)));

					CustDetails.setmReprintCount(cr.getInt(cr.getColumnIndex(COL_ReprintCount_COLLECTION_TABLE)));//08-06-2017
					mList.add(CustDetails);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return mList;
	}



	//Nitish 07-05-2014  //Total Billed Amount
	public String GetBillAmount()
	{
		Cursor crBat = null;
		String bat = "";
		try
		{
			QueryParameters qParam = StoredProcedure.GetBillAmount();
			crBat = getReadableDatabase().rawQuery(qParam.getSql(), null);	
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				bat = crBat.getString(crBat.getColumnIndex("BillAmt"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return bat;
	}
	//Nitish 07-05-2014 //Billed Count
	public int GetCountforBilledConnection()
	{
		Cursor crBat = null;
		int count=0;
		try
		{
			QueryParameters qParam = StoredProcedure.GetCountforBilledConnection();
			crBat = getReadableDatabase().rawQuery(qParam.getSql(), null);	
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				count = crBat.getInt(crBat.getColumnIndex("COUNT"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return count;
	}
	//Nitish 07-05-2014 //Meter Reader Name
	public String GetMRName()
	{
		Cursor crBat = null;
		String bat = "";
		try
		{
			QueryParameters qParam = StoredProcedure.GetMRName();
			crBat = getReadableDatabase().rawQuery(qParam.getSql(), null);	
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				bat = crBat.getString(crBat.getColumnIndex("MRName"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return bat;
	}	
	//Nitish 07-05-2014 //Total Collection Amount
	public String GetTotalCollectionAmount()
	{
		Cursor crBat = null;
		String bat = "";
		try
		{
			QueryParameters qParam = StoredProcedure.GetTotalCollectionAmount();
			crBat = getReadableDatabase().rawQuery(qParam.getSql(), null);	
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				bat = crBat.getString(crBat.getColumnIndex("CollectionAmt"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return bat;
	}
	//Nitish 07-05-2014 //Total Receipt Count
	public int GetCountforColl()
	{
		Cursor crBat = null;
		int count=0;
		try
		{
			QueryParameters qParam = StoredProcedure.GetCountforColl();
			crBat = getReadableDatabase().rawQuery(qParam.getSql(), null);	
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				count = crBat.getInt(crBat.getColumnIndex("COUNT"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return count;
	}
	//Nitish 30-05-2014 //Total GPRS Count
	public int GetCountGPRSSentConnectionColl()
	{
		Cursor crBat = null;
		int count=0;
		try
		{
			QueryParameters qParam = StoredProcedure.GetCountGPRSSentConnectionColl();
			crBat = getReadableDatabase().rawQuery(qParam.getSql(), null);	
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				count = crBat.getInt(crBat.getColumnIndex("COUNT"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return count;
	}
	//Nitish 08-05-2014 //Total Receipt Count
	public int GetCountforBatchColl(String batchno)
	{
		Cursor crBat = null;
		int count = 0;
		try
		{
			QueryParameters qParam = StoredProcedure.GetCountforBatchColl(batchno);
			crBat = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				count = crBat.getInt(crBat.getColumnIndex("COUNT"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return count;
	}
	//Nitish 08-05-2014 //Get all details of receipts for active BatchNo orderBy RRNo
	public ArrayList<ReadCollection> GetReportRRNOWiseColl(String batchno) throws Exception
	{
		ArrayList<ReadCollection> mList = new ArrayList<ReadCollection>();
		Cursor cr=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetReportRRNOWiseColl(batchno);
			cr = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(cr!=null)
			{
				for(int i=1; i<=cr.getCount();++i)
				{

					if(mList==null)
					{
						mList=new ArrayList<ReadCollection>();
					}
					if(i==1)				
						cr.moveToFirst();				
					else
						cr.moveToNext();

					ReadCollection CustDetails = new ReadCollection();								
					CustDetails.setmConnectionNo(cr.getString(cr.getColumnIndex(COL_ConnectionNo_COLLECTION_TABLE)));
					CustDetails.setmRRNo(cr.getString(cr.getColumnIndex(COL_RRNo_COLLECTION_TABLE)));			
					CustDetails.setmCustomerName(cr.getString(cr.getColumnIndex(COL_CustomerName_COLLECTION_TABLE)));				
					CustDetails.setmRcptCnt(cr.getInt(cr.getColumnIndex(COL_RcptCnt_COLLECTION_TABLE)));	
					CustDetails.setmBatch_No(cr.getString(cr.getColumnIndex(COL_Batch_No_COLLECTION_TABLE)));	
					CustDetails.setmReceipt_No(Integer.valueOf(cr.getString(cr.getColumnIndex(COL_Receipt_No_COLLECTION_TABLE)))); //29-04-2017
					CustDetails.setmDateTime(cr.getString(cr.getColumnIndex(COL_DateTime_COLLECTION_TABLE)));
					CustDetails.setmPayment_Mode(cr.getString(cr.getColumnIndex(COL_Payment_Mode_COLLECTION_TABLE)));//5
					CustDetails.setmArears(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Arrears_COLLECTION_TABLE))));	//Arrears					
					CustDetails.setmBillTotal(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_BillTotal_COLLECTION_TABLE))));
					CustDetails.setmPaid_Amt(cr.getInt(cr.getColumnIndex(COL_Paid_Amt_COLLECTION_TABLE)));
					CustDetails.setmBankID(cr.getInt(cr.getColumnIndex(COL_BankID_COLLECTION_TABLE)));
					CustDetails.setmChequeDDNo(cr.getInt(cr.getColumnIndex(COL_ChequeDDNo_COLLECTION_TABLE)));
					CustDetails.setmChequeDDDate(cr.getString(cr.getColumnIndex(COL_ChequeDDDate_COLLECTION_TABLE)));
					CustDetails.setmReceipttypeflag(cr.getString(cr.getColumnIndex(COL_Receipttypeflag_COLLECTION_TABLE)));				
					CustDetails.setmGvpId(cr.getString(cr.getColumnIndex(COL_GvpId_COLLECTION_TABLE)));	
					CustDetails.setmSBMNumber(cr.getString(cr.getColumnIndex(COL_SBMNumber_COLLECTION_TABLE)));	
					CustDetails.setmLocationCode(cr.getString(cr.getColumnIndex(COL_LocationCode_COLLECTION_TABLE)));	
					CustDetails.setmGprs_Flag(cr.getInt(cr.getColumnIndex(COL_Gprs_Flag_COLLECTION_TABLE)));	
					CustDetails.setmArrearsBill_Flag(cr.getInt(cr.getColumnIndex(COL_ArrearsBill_Flag_COLLECTION_TABLE)));
					CustDetails.setmReaderCode(cr.getString(cr.getColumnIndex(COL_ReaderCode_COLLECTION_TABLE)));

					CustDetails.setmReprintCount(cr.getInt(cr.getColumnIndex(COL_ReprintCount_COLLECTION_TABLE)));//08-06-2017

					mList.add(CustDetails);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return mList;
	}
	//Nitish 29-05-2014
	public boolean isEveryConnectionCollected()
	{
		Cursor crBillCount = null, crCollCount = null;
		boolean isColl = false;
		int TotalBill = 0, TotalCollected = 0;
		try
		{
			//Get Total Connection===========================================================
			QueryParameters qParam = StoredProcedure.GetCountBillCollDataMain();
			crBillCount = getReadableDatabase().rawQuery(qParam.getSql(), null);		
			if(crBillCount != null && crBillCount.getCount() > 0)
			{
				crBillCount.moveToFirst();
				TotalBill = crBillCount.getInt(crBillCount.getColumnIndex("COUNT"));
			}
			//END Get Total Connection=======================================================

			//Get Total Collected Connection===========================================================
			QueryParameters qParam1 = StoredProcedure.GetCountforColl();
			crCollCount = getReadableDatabase().rawQuery(qParam1.getSql(), null);		
			if(crCollCount != null && crCollCount.getCount() > 0)
			{
				crCollCount.moveToFirst();
				TotalCollected = crCollCount.getInt(crCollCount.getColumnIndex("COUNT"));
			}
			//END Get Total Collected Connection=======================================================
			if(TotalBill == TotalCollected)
			{
				isColl = true;
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return isColl;
	}

	//Nitish Modified 04-07-2014 Get connid of billed connections whose GPRS is not sent
	public ArrayList<GPRSReportNotSent> GetBillingConnIdGPRSNotSent() 
	{
		ArrayList<GPRSReportNotSent> mList = new  ArrayList<GPRSReportNotSent>();

		Cursor crRep=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetBillingConnIdGPRSNotSent();
			crRep = getReadableDatabase().rawQuery(qParam.getSql(), null);	
			if(crRep!=null)
			{
				for(int i=1; i<=crRep.getCount();++i)
				{

					if(mList==null)
					{
						mList=new ArrayList<GPRSReportNotSent>();
					}
					if(i==1)				
						crRep.moveToFirst();				
					else
						crRep.moveToNext();

					GPRSReportNotSent rb = new GPRSReportNotSent();	
					rb.setmConNo(crRep.getString(crRep.getColumnIndex("NOTSENT")));
					rb.setmGPRSNotsent(crRep.getString(crRep.getColumnIndex("GPRSSTATUS")));					
					mList.add(rb);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception

		}
		finally
		{
			if(crRep!=null)
			{
				crRep.close();
			}
		}
		return mList;
	}
	//Nitish Modified 24-07-2014 Get connid of billed connections whose GPRS is not sent
	public ArrayList<GPRSReportNotSent> GetReceiptsConnIdGPRSNotSent() 
	{
		ArrayList<GPRSReportNotSent> mList = new  ArrayList<GPRSReportNotSent>();

		Cursor crRep=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetReceiptsConnIdGPRSNotSent();
			crRep = getReadableDatabase().rawQuery(qParam.getSql(),null);	
			if(crRep!=null)
			{
				for(int i=1; i<=crRep.getCount();++i)
				{

					if(mList==null)
					{
						mList=new ArrayList<GPRSReportNotSent>();
					}
					if(i==1)				
						crRep.moveToFirst();				
					else
						crRep.moveToNext();

					GPRSReportNotSent rb = new GPRSReportNotSent();						
					//Get ConNo Which are not sent along with rcpttype
					rb.setmConNo(crRep.getString(crRep.getColumnIndex("NOTSENT")).trim() + "(" + crRep.getString(crRep.getColumnIndex(COL_Receipttypeflag_COLLECTION_TABLE)) + ")");
					rb.setmGPRSNotsent(crRep.getString(crRep.getColumnIndex("GPRSSTATUS")));					
					mList.add(rb);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception

		}
		finally
		{
			if(crRep!=null)
			{
				crRep.close();
			}
		}
		return mList;
	}	
	//Nitish 18-06-2014  Get Batch Nos in Cash Counter
	public ArrayList<String> GetCashCounterBatchNos()
	{
		Cursor crBatch = null;
		ArrayList<String> l = new ArrayList<String>();
		QueryParameters qParam = StoredProcedure.GetCashCounterBatchNos();
		crBatch = getReadableDatabase().rawQuery(qParam.getSql(), null);
		if(crBatch != null && crBatch.getCount() >0)
		{
			crBatch.moveToFirst();
			do{
				l.add(crBatch.getString(crBatch.getColumnIndex("BATCHNO")) + "#" + crBatch.getString(crBatch.getColumnIndex("BATCHDATE")));
			}while(crBatch.moveToNext());			
		}
		return l; 
	}	

	//Nitish 01/07/2014 //Save GPS Details 
	public int GPSDetailsSave(GPSDetailsObject Gp) 
	{		
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();
		try
		{	
			sldb.beginTransaction();
			ContentValues valuesInsertGPStbl = new ContentValues();
			valuesInsertGPStbl.put("IMEINo", Gp.getmIMEINo());
			valuesInsertGPStbl.put("SIMNo", Gp.getmSIMNo());	
			valuesInsertGPStbl.put("DateTime", Gp.getmDateTime());
			valuesInsertGPStbl.put("MRID", Gp.getmMRID());			
			valuesInsertGPStbl.put("Latitude", Gp.getmLatitude());
			valuesInsertGPStbl.put("Longitude", Gp.getmLongitude());
			valuesInsertGPStbl.put("LocationCode", Gp.getmLocationCode());	
			long insertResult = sldb.insert("GPS_Details", null, valuesInsertGPStbl);
			if(insertResult <= 0)
			{	
				sldb.endTransaction();
				throw new Exception("Insertion failed for GPS_Details");
			}				
			sldb.setTransactionSuccessful();
			return 1;
		}
		catch(Exception e)
		{
			Log.d("", e.toString());
			return 0;
		}	
		finally
		{
			sldb.endTransaction();
		}	
	}
	//Nitish 04-07-2014 //Meter Reader Name
	public String GetMRNameFromSBMBillCollData()
	{
		Cursor crBat = null;
		String bat = "";
		try
		{
			QueryParameters qParam = StoredProcedure.GetMRNameFromSBMBillCollData();
			crBat = getReadableDatabase().rawQuery(qParam.getSql(), null);	
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				bat = crBat.getString(crBat.getColumnIndex("MRName"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return bat;
	}	

	//Created By Nitish 02-07-2014
	public ArrayList<String> GetGPSDataSendToServer()
	{
		ArrayList<String> alStr = new ArrayList<String>();
		CommonFunction cFun = new CommonFunction();
		Cursor crWrite = null;
		StringBuilder sb = new StringBuilder();

		QueryParameters qParam = StoredProcedure.GetGPSDataSentToServer();
		crWrite = getReadableDatabase().rawQuery(qParam.getSql(), null);		
		if(crWrite != null && crWrite.getCount() > 0)
		{
			crWrite.moveToFirst();
			do
			{
				try
				{
					WriteFileParameters wfp = new WriteFileParameters();					
					wfp.setmIMEINoGPS(crWrite.getString(crWrite.getColumnIndex(COL_IMEINo_GPS_DETAILS)));
					wfp.setmSIMNoGPS(crWrite.getString(crWrite.getColumnIndex(COL_SIMNo_GPS_DETAILS)));					
					wfp.setmCurrentDateTimeGPS(crWrite.getString(crWrite.getColumnIndex(COL_DateTime_GPS_DETAILS)));
					wfp.setmMRIDGPS(crWrite.getString(crWrite.getColumnIndex(COL_MRID_GPS_DETAILS)));
					wfp.setmLatitudeGPS(crWrite.getString(crWrite.getColumnIndex(COL_Latitude_GPS_DETAILS)));
					wfp.setmLongitudeGPS(crWrite.getString(crWrite.getColumnIndex(COL_Longitude_GPS_DETAILS)));					
					wfp.setmLocationCodeGPS(crWrite.getString(crWrite.getColumnIndex(COL_LocationCode_GPS_DETAILS)));					

					sb.delete(0, sb.length());
					sb.append(wfp.getmIMEINoGPS());		
					sb.append(wfp.getmSIMNoGPS());					
					sb.append(wfp.getmCurrentDateTimeGPS());
					sb.append(wfp.getmMRIDGPS());
					sb.append(wfp.getmLatitudeGPS());
					sb.append(wfp.getmLongitudeGPS());
					sb.append(wfp.getmLocationCodeGPS());					
					alStr.add(sb.toString());
				}
				catch(Exception e)
				{
					Log.d(TAG, e.toString());
				}
			}while(crWrite.moveToNext());
		}
		return alStr;
	}/**/
	public int UpdateGPSDetails(String datetime)
	{
		int upStatus = 0;
		try
		{
			ContentValues valuesUpdateGPStbl = new ContentValues();
			valuesUpdateGPStbl.put("Gprs_Flag", 1);
			upStatus = getWritableDatabase().update("GPS_Details", valuesUpdateGPStbl, "trim(DateTime) = ?", new String[]{datetime.trim()});
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return upStatus;
	}
	//Nitish 07-05-2014 //Total Receipt Count
	public int GetCountforGPSDetails()
	{
		Cursor crBat = null;
		int count=0;
		try
		{
			QueryParameters qParam = StoredProcedure.GetCountforGPSDetails();
			crBat = getReadableDatabase().rawQuery(qParam.getSql(), null);	
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				count = crBat.getInt(crBat.getColumnIndex("COUNT"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return count;
	}
	//Nitish 30-05-2014 //Total GPRS Count
	public int GetCountGPSDetailsSent()
	{
		Cursor crBat = null;
		int count=0;
		try
		{
			QueryParameters qParam = StoredProcedure.GetCountGPSDetailsSent();
			crBat = getReadableDatabase().rawQuery(qParam.getSql(), null);	
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				count = crBat.getInt(crBat.getColumnIndex("COUNT"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return count;
	}

	//Nitish 04-07-2014 //Total GPRS Count
	public void DropCreateGPSTable()
	{
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();

		sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_GPS_DETAILS);		

		sldb.execSQL("CREATE TABLE GPS_Details(UID INTEGER PRIMARY KEY,IMEINo TEXT,SIMNo TEXT,DateTime TEXT, " +
				" MRID TEXT,Latitude TEXT,Longitude TEXT,LocationCode TEXT,GPRS_Flag INTEGER);");
	}
	//Nitish 04-07-2014 //Update GPRS Count
	public int UpdateGPRSStausBilling(String ConnectionNo, String status)
	{
		int upStatus = 0;
		try
		{
			ContentValues valuesUpdateMaintbl = new ContentValues();
			valuesUpdateMaintbl.put("GPRS_Status", status);
			upStatus = getWritableDatabase().update("SBM_BillCollDataMain", valuesUpdateMaintbl, "trim(ConnectionNo) = ?", new String[]{ConnectionNo.trim()});
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return upStatus;
	}
	//Nitish 10-04-2014 //Update RcptCnt =1 once GPRS Sent
	public int UpdateGPRSStausColl(String ConnectionNo,String ReceiptNo,String status)
	{
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();
		int upStatus = 0;
		try
		{
			ReceiptNo = ReceiptNo.trim();
			ContentValues valuesUpdateColltbl = new ContentValues();
			valuesUpdateColltbl.put("GPRS_Status", status);
			//sldb.update("Collection_TABLE", valuesUpdateColltbl, "ConnectionNo = ?", new String[]{ConnectionNo});
			sldb.update("Collection_TABLE", valuesUpdateColltbl, "ConnectionNo = ? and Receipt_No = ? ", new String[]{ConnectionNo,ReceiptNo});			
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return upStatus;
	}
	//Nitish 26-08-2014 //Total Receipt Count
	public int GetCountforRevnMISCColl(String rpttype)
	{
		Cursor crBat = null;
		int count=0;
		try
		{
			QueryParameters qParam = StoredProcedure.GetCountforRevnMISCColl(rpttype);
			crBat = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());		
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				count = crBat.getInt(crBat.getColumnIndex("COUNT"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return count;
	}


	//Nitish 26-08-2014 //Total Collection Amount
	public String GetTotalRevnMISCCollectionAmount(String rpttype)
	{
		Cursor crBat = null;
		String bat = "";
		try
		{
			QueryParameters qParam = StoredProcedure.GetTotalRevnMISCCollectionAmount(rpttype);
			crBat = getReadableDatabase().rawQuery(qParam.getSql(),qParam.getSelectionArgs());	
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				bat = crBat.getString(crBat.getColumnIndex("CollectionAmt"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return bat;
	}



	//punit 25022014
	public void GetPrev(String custId ) throws Exception
	{
		ReadSlabNTarifSbmBillCollection CustDetails = BillingObject.GetBillingObject();

		cr=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetPrev(custId);
			cr = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(cr.getCount() > 0)//Cursor IF Start
			{
				//for(int i=1; i<=cr.getCount();++i)
				//{

				//if(i==1){ 
				cr.moveToFirst();



				CustDetails.setmAbFlag(cr.getString(cr.getColumnIndex(COL_AbFlag)));
				CustDetails.setmAccdRdg(cr.getString(cr.getColumnIndex(COL_AccdRdg)));
				CustDetails.setmAccdRdg_rtn(cr.getString(cr.getColumnIndex(COL_AccdRdg_rtn)));
				CustDetails.setmAddress1(cr.getString(cr.getColumnIndex(COL_Address1)));
				CustDetails.setmAddress2(cr.getString(cr.getColumnIndex(COL_Address2)));
				CustDetails.setmApplicationNo(cr.getString(cr.getColumnIndex(COL_ApplicationNo)));
				CustDetails.setmArears(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Arears))));
				CustDetails.setmArrearsOld(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ArrearsOld))));
				CustDetails.setmAvgCons(cr.getString(cr.getColumnIndex(COL_AvgCons)));
				CustDetails.setmBankID(cr.getInt(cr.getColumnIndex(COL_BankID)));
				CustDetails.setmBatch_No(cr.getString(cr.getColumnIndex(COL_Batch_No)));
				CustDetails.setmBillable(cr.getString(cr.getColumnIndex(COL_Billable))); 
				CustDetails.setmBillDate(cr.getString(cr.getColumnIndex(COL_BillDate)));
				CustDetails.setmBillFor(cr.getString(cr.getColumnIndex(COL_BillFor)));
				CustDetails.setmBillNo(cr.getString(cr.getColumnIndex(COL_BillNo)));
				CustDetails.setmBillTotal(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_BillTotal))));
				CustDetails.setmBjKj2Lt2(cr.getString(cr.getColumnIndex(COL_BjKj2Lt2)));
				CustDetails.setmBlCnt(cr.getString(cr.getColumnIndex(COL_BlCnt)));
				CustDetails.setmBOBilled_Amount(cr.getString(cr.getColumnIndex(COL_BOBilled_Amount)));
				CustDetails.setmBOBillFlag(cr.getInt(cr.getColumnIndex(COL_BOBillFlag)));
				CustDetails.setmCapReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_CapReb))));
				CustDetails.setmChargetypeID(cr.getInt(cr.getColumnIndex(COL_ChargetypeID)));
				CustDetails.setmChequeDDDate(cr.getString(cr.getColumnIndex(COL_ChequeDDDate)));
				CustDetails.setmChequeDDNo(cr.getInt(cr.getColumnIndex(COL_ChequeDDNo)));
				CustDetails.setmConnectionNo(cr.getString(cr.getColumnIndex(COL_ConnectionNo)));
				CustDetails.setmConsPayable(cr.getString(cr.getColumnIndex(COL_ConsPayable)));
				CustDetails.setmCustomerName(cr.getString(cr.getColumnIndex(COL_CustomerName)));
				CustDetails.setmDateTime(cr.getString(cr.getColumnIndex(COL_DateTime)));
				CustDetails.setmDayWise_Flag(cr.getString(cr.getColumnIndex(COL_DayWise_Flag)));
				CustDetails.setmDemandChrg(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_DemandChrg))));
				CustDetails.setmDLAvgMin(cr.getString(cr.getColumnIndex(COL_DLAvgMin)));
				CustDetails.setmDlCount(cr.getInt(cr.getColumnIndex(COL_DlCount)));
				CustDetails.setmDlCount(cr.getInt(cr.getColumnIndex(COL_DlCount)));
				CustDetails.setmDLTEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_DLTEc))));
				CustDetails.setmDPdate(cr.getString(cr.getColumnIndex(COL_DPdate)));
				CustDetails.setmDrFees(cr.getString(cr.getColumnIndex(COL_DrFees)));
				CustDetails.setmDueDate(cr.getString(cr.getColumnIndex(COL_DueDate)));
				CustDetails.setmECReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ECReb))));
				CustDetails.setmEcsFlag(cr.getString(cr.getColumnIndex(COL_EcsFlag)));
				CustDetails.setmExLoad(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ExLoad))));
				CustDetails.setmFC_Slab_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_Slab_2))));
				CustDetails.setmFixedCharges(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FixedCharges))));
				CustDetails.setmFLReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FLReb))));
				CustDetails.setmForMonth(cr.getString(cr.getColumnIndex(COL_ForMonth)));
				CustDetails.setmGoKArrears(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_GoKArrears))));
				CustDetails.setmGoKPayable(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_GoKPayable))));
				CustDetails.setmGprs_Flag(cr.getInt(cr.getColumnIndex(COL_Gprs_Flag)));
				CustDetails.setmGps_Latitude_image(cr.getString(cr.getColumnIndex(COL_Gps_Latitude_image)));
				CustDetails.setmGps_Latitude_print(cr.getString(cr.getColumnIndex(COL_Gps_Latitude_print)));
				CustDetails.setmGps_LatitudeCardinal_image(cr.getString(cr.getColumnIndex(COL_Gps_LatitudeCardinal_image)));
				CustDetails.setmGps_LatitudeCardinal_print(cr.getString(cr.getColumnIndex(COL_Gps_LatitudeCardinal_print)));
				CustDetails.setmGps_Longitude_image(cr.getString(cr.getColumnIndex(COL_Gps_Longitude_image)));
				CustDetails.setmGps_Longitude_print(cr.getString(cr.getColumnIndex(COL_Gps_Longitude_print)));
				CustDetails.setmGps_LongitudeCardinal_image(cr.getString(cr.getColumnIndex(COL_Gps_LongitudeCardinal_image)));
				CustDetails.setmGps_LatitudeCardinal_print(cr.getString(cr.getColumnIndex(COL_Gps_LatitudeCardinal_print)));
				CustDetails.setmGvpId(cr.getString(cr.getColumnIndex(COL_GvpId)));
				CustDetails.setmHCReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_HCReb))));
				CustDetails.setmHLReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_HLReb))));
				CustDetails.setmHWCReb(cr.getString(cr.getColumnIndex(COL_HWCReb)));
				CustDetails.setmImage_Cap_Date(cr.getString(cr.getColumnIndex(COL_Image_Cap_Date)));
				CustDetails.setmImage_Cap_Time(cr.getString(cr.getColumnIndex(COL_Image_Cap_Time)));
				CustDetails.setmImage_Name(cr.getString(cr.getColumnIndex(COL_Image_Name)));
				CustDetails.setmImage_Path(cr.getString(cr.getColumnIndex(COL_Image_Path)));
				CustDetails.setmIntrst_Unpaid(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Intrst_Unpaid))));
				CustDetails.setmIntrstCurnt(cr.getString(cr.getColumnIndex(COL_IntrstCurnt)));
				CustDetails.setmIntrstOld(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_IntrstOld))));
				CustDetails.setmIODD11_Remarks(cr.getString(cr.getColumnIndex(COL_IODD11_Remarks)));
				CustDetails.setmIODRemarks(cr.getString(cr.getColumnIndex(COL_IODRemarks)));
				CustDetails.setmIssueDateTime(cr.getString(cr.getColumnIndex(COL_IssueDateTime)));
				CustDetails.setmKVA_Consumption(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_KVA_Consumption))));
				CustDetails.setmKVAAssd_Cons(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_KVAAssd_Cons))));
				CustDetails.setmKVAFR(cr.getString(cr.getColumnIndex(COL_KVAFR)));
				CustDetails.setmKVAH_OldConsumption(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_KVAFR))));
				CustDetails.setmKVAIR(cr.getString(cr.getColumnIndex(COL_KVAIR)));
				CustDetails.setmLatitude(cr.getString(cr.getColumnIndex(COL_Latitude)));
				CustDetails.setmLatitude_Dir(cr.getString(cr.getColumnIndex(COL_Latitude_Dir)));
				CustDetails.setmLegFol(cr.getString(cr.getColumnIndex(COL_LegFol)));
				CustDetails.setmLinMin(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_LinMin))));
				CustDetails.setmLocationCode(cr.getString(cr.getColumnIndex(COL_LocationCode)));
				CustDetails.setmLongitude(cr.getString(cr.getColumnIndex(COL_Longitude)));
				CustDetails.setmLongitude_Dir(cr.getString(cr.getColumnIndex(COL_Longitude_Dir)));
				CustDetails.setmMCHFlag(cr.getString(cr.getColumnIndex(COL_MCHFlag)));
				CustDetails.setmMeter_serialno(cr.getString(cr.getColumnIndex(COL_Meter_serialno)));
				CustDetails.setmMeter_type(cr.getString(cr.getColumnIndex(COL_Meter_type)));
				CustDetails.setmMF(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_MF))));
				CustDetails.setmMobileNo(cr.getString(cr.getColumnIndex(COL_MobileNo)));
				CustDetails.setmMtd(cr.getString(cr.getColumnIndex(COL_Mtd)));
				CustDetails.setmMtrDisFlag(cr.getInt(cr.getColumnIndex(COL_MtrDisFlag)));
				CustDetails.setmNewNoofDays(cr.getString(cr.getColumnIndex(COL_NewNoofDays)));
				CustDetails.setmNoOfDays(cr.getString(cr.getColumnIndex(COL_NoOfDays)));
				CustDetails.setmOld_Consumption(cr.getString(cr.getColumnIndex(COL_Old_Consumption)));
				CustDetails.setmOldConnID(cr.getString(cr.getColumnIndex(COL_OldConnID)));
				CustDetails.setmOtherChargeLegend(cr.getString(cr.getColumnIndex(COL_OtherChargeLegend)));
				CustDetails.setmOthers(cr.getString(cr.getColumnIndex(COL_Others)));
				CustDetails.setmPaid_Amt(cr.getInt(cr.getColumnIndex(COL_Paid_Amt)));
				CustDetails.setmPayment_Mode(cr.getString(cr.getColumnIndex(COL_Payment_Mode)));
				CustDetails.setmPenExLd(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_PenExLd))));
				CustDetails.setmPF(cr.getString(cr.getColumnIndex(COL_PF)));
				CustDetails.setmPfPenAmt(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_PfPenAmt))));
				CustDetails.setmPreRead(cr.getString(cr.getColumnIndex(COL_PreRead)));
				CustDetails.setmPreStatus(cr.getString(cr.getColumnIndex(COL_PreStatus)));//current status
				CustDetails.setmPrevRdg(cr.getString(cr.getColumnIndex(COL_PrevRdg)));
				CustDetails.setmRcptCnt(cr.getInt(cr.getColumnIndex(COL_RcptCnt)));
				CustDetails.setmReaderCode(cr.getString(cr.getColumnIndex(COL_ReaderCode)));
				CustDetails.setmReadingDay(cr.getString(cr.getColumnIndex(COL_ReadingDay)));
				CustDetails.setmRebateFlag(cr.getString(cr.getColumnIndex(COL_RebateFlag)));
				CustDetails.setmReceipt_No(cr.getInt(cr.getColumnIndex(COL_Receipt_No)));
				CustDetails.setmReceiptAmnt(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ReceiptAmnt))));
				CustDetails.setmReceiptDate(cr.getString(cr.getColumnIndex(COL_ReceiptDate)));
				CustDetails.setmReceipttypeflag(cr.getString(cr.getColumnIndex(COL_Receipttypeflag)));
				CustDetails.setmRecordDmnd(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_RecordDmnd))));
				CustDetails.setmRemarks(cr.getString(cr.getColumnIndex(COL_Remarks)));
				CustDetails.setmRRFlag(cr.getString(cr.getColumnIndex(COL_RRFlag)));
				CustDetails.setmRRNo(cr.getString(cr.getColumnIndex(COL_RRNo)));
				CustDetails.setmSancHp(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SancHp))));
				CustDetails.setmSancKw(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SancKw))));
				CustDetails.setmSancLoad(cr.getString(cr.getColumnIndex(COL_SancLoad)));
				CustDetails.setmSBMNumber(cr.getString(cr.getColumnIndex(COL_SBMNumber)));
				CustDetails.setmSectionName(cr.getString(cr.getColumnIndex(COL_SectionName)));
				CustDetails.setmSlowRtnPge(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SlowRtnPge))));
				CustDetails.setmSpotSerialNo(cr.getInt(cr.getColumnIndex(COL_SpotSerialNo)));
				CustDetails.setmStatus(cr.getInt(cr.getColumnIndex(COL_Status)));//previous status
				CustDetails.setmSubId(cr.getString(cr.getColumnIndex(COL_SubId)));
				CustDetails.setmSupply_Points(cr.getInt(cr.getColumnIndex(COL_Supply_Points)));
				CustDetails.setmTariffCode(cr.getInt(cr.getColumnIndex(COL_TariffCode)));
				CustDetails.setmTarifString(cr.getString(cr.getColumnIndex(COL_TarifString)));     
				CustDetails.setmTaxAmt(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TaxAmt))));
				CustDetails.setmTaxFlag(cr.getString(cr.getColumnIndex(COL_TaxFlag)));
				CustDetails.setmTcName(cr.getString(cr.getColumnIndex(COL_TcName)));
				CustDetails.setmTEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TEc))));
				CustDetails.setmTFc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TFc))));
				CustDetails.setmThirtyFlag(cr.getString(cr.getColumnIndex(COL_ThirtyFlag)));
				CustDetails.setmTourplanId(cr.getString(cr.getColumnIndex(COL_TourplanId)));
				CustDetails.setmTvmMtr(cr.getString(cr.getColumnIndex(COL_TvmMtr)));
				CustDetails.setmTvmPFtype(cr.getString(cr.getColumnIndex(COL_TvmPFtype)));
				//CustDetails.setmUIDR_Slab(cr.getInt(cr.getColumnIndex(COL_UID_Slab)));
				CustDetails.setmUnits(cr.getString(cr.getColumnIndex(COL_Units)));

				CustDetails.setMmUID(cr.getInt(cr.getColumnIndex(COL_UID_S_BcData)));//punit 25022014
				CustDetails.setmMeter_Present_Flag(cr.getString(cr.getColumnIndex(COL_METER_PRESENT_FLAG)) == null ? 0 : cr.getInt(cr.getColumnIndex(COL_METER_PRESENT_FLAG)));
				CustDetails.setmMtr_Not_Visible(cr.getString(cr.getColumnIndex(COL_MTR_NOT_VISIBLE)) == null ? 0 : cr.getInt(cr.getColumnIndex(COL_MTR_NOT_VISIBLE)));

				CustDetails.setmDLTEc_GoK(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_DLTEc_GoK))));//31-12-2014
				CustDetails.setmOldTecBill(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_OldTecBill))));//Nitish on 21-04-2016

				//30-07-2015
				CustDetails.setmAadharNo(cr.getString(cr.getColumnIndex(COL_AadharNo)));
				CustDetails.setmVoterIdNo(cr.getString(cr.getColumnIndex(COL_VoterIdNo)));				
				CustDetails.setmMtrMakeFlag(cr.getInt(cr.getColumnIndex(COL_MtrMakeFlag)));
				CustDetails.setmMtrBodySealFlag(cr.getInt(cr.getColumnIndex(COL_MtrBodySealFlag)));
				CustDetails.setmMtrTerminalCoverFlag(cr.getInt(cr.getColumnIndex(COL_MtrTerminalCoverFlag)));
				CustDetails.setmMtrTerminalCoverSealedFlag(cr.getInt(cr.getColumnIndex(COL_MtrTerminalCoverSealedFlag)));

				CustDetails.setmFACValue(cr.getString(cr.getColumnIndex(COL_FACValue)));//19-12-2015
				//CustDetails.setmUID(cr.getInt(cr.getColumnIndex(COL_UID_Slab)));
				//CustDetails.setMmConnectionNo(cr.getString(cr.getColumnIndex(COL_ConnectionNo_Slab)));
				//CustDetails.setMmRRNo(cr.getString(cr.getColumnIndex(COL_RRNo_Slab)));
				CustDetails.setMmECRate_Count(cr.getInt(cr.getColumnIndex(COL_ECRate_Count)));
				CustDetails.setMmECRate_Row(cr.getInt(cr.getColumnIndex(COL_ECRate_Row)));
				CustDetails.setMmFCRate_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FCRate_1))));
				CustDetails.setMmCOL_FCRate_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FCRate_2))));
				CustDetails.setMmUnits_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_1))));
				CustDetails.setMmUnits_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_2))));
				CustDetails.setMmUnits_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_3))));
				CustDetails.setMmUnits_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_4))));
				CustDetails.setMmUnits_5(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_5))));
				CustDetails.setMmUnits_6(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_6))));
				CustDetails.setMmEC_Rate_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_1))));
				CustDetails.setMmEC_Rate_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_2))));
				CustDetails.setMmEC_Rate_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_3))));
				CustDetails.setMmEC_Rate_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_4))));
				CustDetails.setMmEC_Rate_5(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_5))));
				CustDetails.setMmEC_Rate_6(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_6))));
				CustDetails.setMmEC_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_1))));
				CustDetails.setMmEC_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_2))));
				CustDetails.setMmEC_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_3))));
				CustDetails.setMmEC_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_4))));
				CustDetails.setMmEC_5(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_5))));
				CustDetails.setMmEC_6(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_6))));
				CustDetails.setMmFC_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_1))));
				CustDetails.setMmFC_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_2))));
				CustDetails.setMmTEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TEc_Slab))));
				CustDetails.setMmEC_FL_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_1))));
				CustDetails.setMmEC_FL_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_2))));
				CustDetails.setMmEC_FL_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_3))));
				CustDetails.setMmEC_FL_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_4))));
				CustDetails.setMmEC_FL(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL))));
				CustDetails.setMmnew_TEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_new_TEc))));
				CustDetails.setMmold_TEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_old_TEc))));

				//23-06-2021 for FC
				CustDetails.setMmNEWFC_UNIT1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT1))));
				CustDetails.setMmNEWFC_UNIT2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT2))));
				CustDetails.setMmNEWFC_UNIT3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT3))));
				CustDetails.setMmNEWFC_UNIT4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT4))));
				CustDetails.setMmNEWFC_Rate3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_Rate3))));
				CustDetails.setMmNEWFC_Rate4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_Rate4))));
				CustDetails.setMmNEWFC3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC3))));
				CustDetails.setMmNEWFC4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC4))));
				
				CustDetails.setmFC_Slab_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_Slab_3))));
			}//Cursor IF END
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("punit", e.toString());
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
	}
	//punit end
	public void GetValueOnBillingPage() throws Exception
	{
		ReadSlabNTarifSbmBillCollection CustDetails = BillingObject.GetBillingObject();

		cr=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetValueOnBillingPage();
			cr = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(cr.getCount() > 0)//Cursor IF Start
			{
				//for(int i=1; i<=cr.getCount();++i)
				//{

				//if(i==1){ 
				cr.moveToFirst();



				CustDetails.setmAbFlag(cr.getString(cr.getColumnIndex(COL_AbFlag)));
				CustDetails.setmAccdRdg(cr.getString(cr.getColumnIndex(COL_AccdRdg)));
				CustDetails.setmAccdRdg_rtn(cr.getString(cr.getColumnIndex(COL_AccdRdg_rtn)));
				CustDetails.setmAddress1(cr.getString(cr.getColumnIndex(COL_Address1)));
				CustDetails.setmAddress2(cr.getString(cr.getColumnIndex(COL_Address2)));
				CustDetails.setmApplicationNo(cr.getString(cr.getColumnIndex(COL_ApplicationNo)));
				CustDetails.setmArears(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Arears))));
				CustDetails.setmArrearsOld(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ArrearsOld))));
				CustDetails.setmAvgCons(cr.getString(cr.getColumnIndex(COL_AvgCons)));
				CustDetails.setmBankID(cr.getInt(cr.getColumnIndex(COL_BankID)));
				CustDetails.setmBatch_No(cr.getString(cr.getColumnIndex(COL_Batch_No)));
				CustDetails.setmBillable(cr.getString(cr.getColumnIndex(COL_Billable))); 
				CustDetails.setmBillDate(cr.getString(cr.getColumnIndex(COL_BillDate)));
				CustDetails.setmBillFor(cr.getString(cr.getColumnIndex(COL_BillFor)));
				CustDetails.setmBillNo(cr.getString(cr.getColumnIndex(COL_BillNo)));
				CustDetails.setmBillTotal(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_BillTotal))));
				CustDetails.setmBjKj2Lt2(cr.getString(cr.getColumnIndex(COL_BjKj2Lt2)));
				CustDetails.setmBlCnt(cr.getString(cr.getColumnIndex(COL_BlCnt)));
				CustDetails.setmBOBilled_Amount(cr.getString(cr.getColumnIndex(COL_BOBilled_Amount)));
				CustDetails.setmBOBillFlag(cr.getInt(cr.getColumnIndex(COL_BOBillFlag)));
				CustDetails.setmCapReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_CapReb))));
				CustDetails.setmChargetypeID(cr.getInt(cr.getColumnIndex(COL_ChargetypeID)));
				CustDetails.setmChequeDDDate(cr.getString(cr.getColumnIndex(COL_ChequeDDDate)));
				CustDetails.setmChequeDDNo(cr.getInt(cr.getColumnIndex(COL_ChequeDDNo)));
				CustDetails.setmConnectionNo(cr.getString(cr.getColumnIndex(COL_ConnectionNo)));
				CustDetails.setmConsPayable(cr.getString(cr.getColumnIndex(COL_ConsPayable)));
				CustDetails.setmCustomerName(cr.getString(cr.getColumnIndex(COL_CustomerName)));
				CustDetails.setmDateTime(cr.getString(cr.getColumnIndex(COL_DateTime)));
				CustDetails.setmDayWise_Flag(cr.getString(cr.getColumnIndex(COL_DayWise_Flag)));
				CustDetails.setmDemandChrg(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_DemandChrg))));
				CustDetails.setmDLAvgMin(cr.getString(cr.getColumnIndex(COL_DLAvgMin)));
				CustDetails.setmDlCount(cr.getInt(cr.getColumnIndex(COL_DlCount)));
				CustDetails.setmDlCount(cr.getInt(cr.getColumnIndex(COL_DlCount)));
				CustDetails.setmDLTEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_DLTEc))));
				CustDetails.setmDPdate(cr.getString(cr.getColumnIndex(COL_DPdate)));
				CustDetails.setmDrFees(cr.getString(cr.getColumnIndex(COL_DrFees)));
				CustDetails.setmDueDate(cr.getString(cr.getColumnIndex(COL_DueDate)));
				CustDetails.setmECReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ECReb))));
				CustDetails.setmEcsFlag(cr.getString(cr.getColumnIndex(COL_EcsFlag)));
				CustDetails.setmExLoad(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ExLoad))));
				CustDetails.setmFC_Slab_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_Slab_2))));
				CustDetails.setmFixedCharges(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FixedCharges))));
				CustDetails.setmFLReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FLReb))));
				CustDetails.setmForMonth(cr.getString(cr.getColumnIndex(COL_ForMonth)));
				CustDetails.setmGoKArrears(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_GoKArrears))));
				CustDetails.setmGoKPayable(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_GoKPayable))));
				CustDetails.setmGprs_Flag(cr.getInt(cr.getColumnIndex(COL_Gprs_Flag)));
				CustDetails.setmGps_Latitude_image(cr.getString(cr.getColumnIndex(COL_Gps_Latitude_image)));
				CustDetails.setmGps_Latitude_print(cr.getString(cr.getColumnIndex(COL_Gps_Latitude_print)));
				CustDetails.setmGps_LatitudeCardinal_image(cr.getString(cr.getColumnIndex(COL_Gps_LatitudeCardinal_image)));
				CustDetails.setmGps_LatitudeCardinal_print(cr.getString(cr.getColumnIndex(COL_Gps_LatitudeCardinal_print)));
				CustDetails.setmGps_Longitude_image(cr.getString(cr.getColumnIndex(COL_Gps_Longitude_image)));
				CustDetails.setmGps_Longitude_print(cr.getString(cr.getColumnIndex(COL_Gps_Longitude_print)));
				CustDetails.setmGps_LongitudeCardinal_image(cr.getString(cr.getColumnIndex(COL_Gps_LongitudeCardinal_image)));
				CustDetails.setmGps_LatitudeCardinal_print(cr.getString(cr.getColumnIndex(COL_Gps_LatitudeCardinal_print)));
				CustDetails.setmGvpId(cr.getString(cr.getColumnIndex(COL_GvpId)));
				CustDetails.setmHCReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_HCReb))));
				CustDetails.setmHLReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_HLReb))));
				CustDetails.setmHWCReb(cr.getString(cr.getColumnIndex(COL_HWCReb)));
				CustDetails.setmImage_Cap_Date(cr.getString(cr.getColumnIndex(COL_Image_Cap_Date)));
				CustDetails.setmImage_Cap_Time(cr.getString(cr.getColumnIndex(COL_Image_Cap_Time)));
				CustDetails.setmImage_Name(cr.getString(cr.getColumnIndex(COL_Image_Name)));
				CustDetails.setmImage_Path(cr.getString(cr.getColumnIndex(COL_Image_Path)));
				CustDetails.setmIntrst_Unpaid(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Intrst_Unpaid))));
				CustDetails.setmIntrstCurnt(cr.getString(cr.getColumnIndex(COL_IntrstCurnt)));
				CustDetails.setmIntrstOld(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_IntrstOld))));
				CustDetails.setmIODD11_Remarks(cr.getString(cr.getColumnIndex(COL_IODD11_Remarks)));
				CustDetails.setmIODRemarks(cr.getString(cr.getColumnIndex(COL_IODRemarks)));
				CustDetails.setmIssueDateTime(cr.getString(cr.getColumnIndex(COL_IssueDateTime)));
				CustDetails.setmKVA_Consumption(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_KVA_Consumption))));
				CustDetails.setmKVAAssd_Cons(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_KVAAssd_Cons))));
				CustDetails.setmKVAFR(cr.getString(cr.getColumnIndex(COL_KVAFR)));
				CustDetails.setmKVAH_OldConsumption(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_KVAFR))));
				CustDetails.setmKVAIR(cr.getString(cr.getColumnIndex(COL_KVAIR)));
				CustDetails.setmLatitude(cr.getString(cr.getColumnIndex(COL_Latitude)));
				CustDetails.setmLatitude_Dir(cr.getString(cr.getColumnIndex(COL_Latitude_Dir)));
				CustDetails.setmLegFol(cr.getString(cr.getColumnIndex(COL_LegFol)));
				CustDetails.setmLinMin(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_LinMin))));
				CustDetails.setmLocationCode(cr.getString(cr.getColumnIndex(COL_LocationCode)));
				CustDetails.setmLongitude(cr.getString(cr.getColumnIndex(COL_Longitude)));
				CustDetails.setmLongitude_Dir(cr.getString(cr.getColumnIndex(COL_Longitude_Dir)));
				CustDetails.setmMCHFlag(cr.getString(cr.getColumnIndex(COL_MCHFlag)));
				CustDetails.setmMeter_serialno(cr.getString(cr.getColumnIndex(COL_Meter_serialno)));
				CustDetails.setmMeter_type(cr.getString(cr.getColumnIndex(COL_Meter_type)));
				CustDetails.setmMF(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_MF))));
				CustDetails.setmMobileNo(cr.getString(cr.getColumnIndex(COL_MobileNo)));
				CustDetails.setmMtd(cr.getString(cr.getColumnIndex(COL_Mtd)));
				CustDetails.setmMtrDisFlag(cr.getInt(cr.getColumnIndex(COL_MtrDisFlag)));
				CustDetails.setmNewNoofDays(cr.getString(cr.getColumnIndex(COL_NewNoofDays)));
				CustDetails.setmNoOfDays(cr.getString(cr.getColumnIndex(COL_NoOfDays)));
				CustDetails.setmOld_Consumption(cr.getString(cr.getColumnIndex(COL_Old_Consumption)));
				CustDetails.setmOldConnID(cr.getString(cr.getColumnIndex(COL_OldConnID)));
				CustDetails.setmOtherChargeLegend(cr.getString(cr.getColumnIndex(COL_OtherChargeLegend)));
				CustDetails.setmOthers(cr.getString(cr.getColumnIndex(COL_Others)));
				CustDetails.setmPaid_Amt(cr.getInt(cr.getColumnIndex(COL_Paid_Amt)));
				CustDetails.setmPayment_Mode(cr.getString(cr.getColumnIndex(COL_Payment_Mode)));
				CustDetails.setmPenExLd(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_PenExLd))));
				CustDetails.setmPF(cr.getString(cr.getColumnIndex(COL_PF)));
				CustDetails.setmPfPenAmt(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_PfPenAmt))));
				CustDetails.setmPreRead(cr.getString(cr.getColumnIndex(COL_PreRead)));
				CustDetails.setmPreStatus(cr.getString(cr.getColumnIndex(COL_Status)));
				CustDetails.setmPrevRdg(cr.getString(cr.getColumnIndex(COL_PrevRdg)));
				CustDetails.setmRcptCnt(cr.getInt(cr.getColumnIndex(COL_RcptCnt)));
				CustDetails.setmReaderCode(cr.getString(cr.getColumnIndex(COL_ReaderCode)));
				CustDetails.setmReadingDay(cr.getString(cr.getColumnIndex(COL_ReadingDay)));
				CustDetails.setmRebateFlag(cr.getString(cr.getColumnIndex(COL_RebateFlag)));
				CustDetails.setmReceipt_No(cr.getInt(cr.getColumnIndex(COL_Receipt_No)));
				CustDetails.setmReceiptAmnt(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ReceiptAmnt))));
				CustDetails.setmReceiptDate(cr.getString(cr.getColumnIndex(COL_ReceiptDate)));
				CustDetails.setmReceipttypeflag(cr.getString(cr.getColumnIndex(COL_Receipttypeflag)));
				CustDetails.setmRecordDmnd(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_RecordDmnd))));
				CustDetails.setmRemarks(cr.getString(cr.getColumnIndex(COL_Remarks)));
				CustDetails.setmRRFlag(cr.getString(cr.getColumnIndex(COL_RRFlag)));
				CustDetails.setmRRNo(cr.getString(cr.getColumnIndex(COL_RRNo)));
				CustDetails.setmSancHp(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SancHp))));
				CustDetails.setmSancKw(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SancKw))));
				CustDetails.setmSancLoad(cr.getString(cr.getColumnIndex(COL_SancLoad)));
				CustDetails.setmSBMNumber(cr.getString(cr.getColumnIndex(COL_SBMNumber)));
				CustDetails.setmSectionName(cr.getString(cr.getColumnIndex(COL_SectionName)));
				CustDetails.setmSlowRtnPge(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SlowRtnPge))));
				CustDetails.setmSpotSerialNo(cr.getInt(cr.getColumnIndex(COL_SpotSerialNo)));
				CustDetails.setmStatus(cr.getInt(cr.getColumnIndex(COL_Status)));
				CustDetails.setmSubId(cr.getString(cr.getColumnIndex(COL_SubId)));
				CustDetails.setmSupply_Points(cr.getInt(cr.getColumnIndex(COL_Supply_Points)));           
				CustDetails.setmTariffCode(cr.getInt(cr.getColumnIndex(COL_TariffCode)));
				CustDetails.setmTarifString(cr.getString(cr.getColumnIndex(COL_TarifString)));     
				CustDetails.setmTaxAmt(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TaxAmt))));
				CustDetails.setmTaxFlag(cr.getString(cr.getColumnIndex(COL_TaxFlag)));
				CustDetails.setmTcName(cr.getString(cr.getColumnIndex(COL_TcName)));
				CustDetails.setmTEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TEc))));
				CustDetails.setmTFc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TFc))));
				CustDetails.setmThirtyFlag(cr.getString(cr.getColumnIndex(COL_ThirtyFlag)));
				CustDetails.setmTourplanId(cr.getString(cr.getColumnIndex(COL_TourplanId)));
				CustDetails.setmTvmMtr(cr.getString(cr.getColumnIndex(COL_TvmMtr)));
				CustDetails.setmTvmPFtype(cr.getString(cr.getColumnIndex(COL_TvmPFtype)));
				//CustDetails.setmUIDR_Slab(cr.getInt(cr.getColumnIndex(COL_UID_Slab)));
				CustDetails.setmUnits(cr.getString(cr.getColumnIndex(COL_Units)));

				CustDetails.setMmUID(cr.getInt(cr.getColumnIndex(COL_UID_S_BcData)));//punit 25022014
				CustDetails.setmMeter_Present_Flag(cr.getString(cr.getColumnIndex(COL_METER_PRESENT_FLAG)) == null ? 0 : cr.getInt(cr.getColumnIndex(COL_METER_PRESENT_FLAG)));
				CustDetails.setmMtr_Not_Visible(cr.getString(cr.getColumnIndex(COL_MTR_NOT_VISIBLE)) == null ? 0 : cr.getInt(cr.getColumnIndex(COL_MTR_NOT_VISIBLE)));

				CustDetails.setmDLTEc_GoK(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_DLTEc_GoK))));//31-12-2014
				CustDetails.setmOldTecBill(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_OldTecBill))));//Nitish on 21-04-2016
				//30-07-2015
				CustDetails.setmAadharNo(cr.getString(cr.getColumnIndex(COL_AadharNo)));
				CustDetails.setmVoterIdNo(cr.getString(cr.getColumnIndex(COL_VoterIdNo)));				
				CustDetails.setmMtrMakeFlag(cr.getInt(cr.getColumnIndex(COL_MtrMakeFlag)));
				CustDetails.setmMtrBodySealFlag(cr.getInt(cr.getColumnIndex(COL_MtrBodySealFlag)));
				CustDetails.setmMtrTerminalCoverFlag(cr.getInt(cr.getColumnIndex(COL_MtrTerminalCoverFlag)));
				CustDetails.setmMtrTerminalCoverSealedFlag(cr.getInt(cr.getColumnIndex(COL_MtrTerminalCoverSealedFlag)));
				CustDetails.setmFACValue(cr.getString(cr.getColumnIndex(COL_FACValue)));//19-12-2015
				//CustDetails.setmUID(cr.getInt(cr.getColumnIndex(COL_UID_Slab)));
				//CustDetails.setMmConnectionNo(cr.getString(cr.getColumnIndex(COL_ConnectionNo_Slab)));
				//CustDetails.setMmRRNo(cr.getString(cr.getColumnIndex(COL_RRNo_Slab)));
				CustDetails.setMmECRate_Count(cr.getInt(cr.getColumnIndex(COL_ECRate_Count)));
				CustDetails.setMmECRate_Row(cr.getInt(cr.getColumnIndex(COL_ECRate_Row)));
				CustDetails.setMmFCRate_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FCRate_1))));
				CustDetails.setMmCOL_FCRate_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FCRate_2))));
				CustDetails.setMmUnits_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_1))));
				CustDetails.setMmUnits_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_2))));
				CustDetails.setMmUnits_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_3))));
				CustDetails.setMmUnits_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_4))));
				CustDetails.setMmUnits_5(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_5))));
				CustDetails.setMmUnits_6(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_6))));
				CustDetails.setMmEC_Rate_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_1))));
				CustDetails.setMmEC_Rate_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_2))));
				CustDetails.setMmEC_Rate_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_3))));
				CustDetails.setMmEC_Rate_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_4))));
				CustDetails.setMmEC_Rate_5(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_5))));
				CustDetails.setMmEC_Rate_6(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_6))));
				CustDetails.setMmEC_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_1))));
				CustDetails.setMmEC_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_2))));
				CustDetails.setMmEC_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_3))));
				CustDetails.setMmEC_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_4))));
				CustDetails.setMmEC_5(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_5))));
				CustDetails.setMmEC_6(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_6))));
				CustDetails.setMmFC_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_1))));
				CustDetails.setMmFC_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_2))));
				CustDetails.setMmTEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TEc_Slab))));
				CustDetails.setMmEC_FL_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_1))));
				CustDetails.setMmEC_FL_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_2))));
				CustDetails.setMmEC_FL_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_3))));
				CustDetails.setMmEC_FL_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_4))));
				CustDetails.setMmEC_FL(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL))));
				CustDetails.setMmnew_TEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_new_TEc))));
				CustDetails.setMmold_TEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_old_TEc))));

				//23-06-2021 for FC
				CustDetails.setMmNEWFC_UNIT1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT1))));
				CustDetails.setMmNEWFC_UNIT2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT2))));
				CustDetails.setMmNEWFC_UNIT3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT3))));
				CustDetails.setMmNEWFC_UNIT4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT4))));
				CustDetails.setMmNEWFC_Rate3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_Rate3))));
				CustDetails.setMmNEWFC_Rate4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_Rate4))));
				CustDetails.setMmNEWFC3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC3))));
				CustDetails.setMmNEWFC4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC4))));
				
				CustDetails.setmFC_Slab_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_Slab_3))));
			}//Cursor IF END
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("punit", e.toString());
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}

	}
	//punit end

	//Tamilselvan on 14-03-2014
	public List<String> calldb()
	{
		List<String> alStr = new ArrayList<String>();
		Cursor crSlab = null;
		try
		{
			QueryParameters qParam = StoredProcedure.GetTariffSlab();
			crSlab = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());
			if(crSlab != null && crSlab.getCount() > 0)
			{
				crSlab.moveToFirst();
				do
				{
					alStr.add(crSlab.getString(crSlab.getColumnIndex(COL_TarifString)));
				}while(crSlab.moveToNext());
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return alStr;
	}
	/**
	 * Tamilselvan on 21-04-2014
	 * Check DataBase is Empty or not and If not empty then check any connection billed or not
	 * @return
	 */
	public boolean isAnyConnectionBilledOrEmpty()
	{
		boolean isBilledorEmpty = false;
		Cursor crValue = null;
		try
		{
			//Query to check Count of Connection
			QueryParameters qParam = StoredProcedure.GetCountBillCollDataMain();
			crValue = getReadableDatabase().rawQuery(qParam.getSql(), null);
			if(crValue != null && crValue.getCount() > 0)
			{
				crValue.moveToFirst();
				//if the count is not 0
				if(crValue.getInt(crValue.getColumnIndex("COUNT")) != 0)
				{
					//Check billed count
					QueryParameters qParamBill = StoredProcedure.GetCountforBilledConnection();
					crValue = getReadableDatabase().rawQuery(qParamBill.getSql(), null);
					if(crValue != null && crValue.getCount() > 0)
					{
						crValue.moveToFirst();
						//Billed count is > 0 
						if((crValue.getInt(crValue.getColumnIndex("COUNT"))) > 0)
						{
							isBilledorEmpty = true;
						}
						else if((crValue.getInt(crValue.getColumnIndex("COUNT"))) == 0)//billed count is 0
						{
							isBilledorEmpty = false;	
						}
					}
				}
				else//if the count is 0 then DB empty
				{
					isBilledorEmpty = false;				
				}
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return isBilledorEmpty;
	}
	/**
	 * Tamilselvan on 19-04-2014
	 * @return
	 */
	public int CheckFlagToLoadFile()
	{
		int rtrVal = 0, billedTotal = 0, gprsTotal = 0;
		boolean isValid = true;
		Cursor crValue = null;
		try
		{
			//Check status 9(File Downloaded)======================================================================
			if(isValid)
			{
				QueryParameters qParamFileDown = StoredProcedure.GetStatusMasterDetailsByIDCheck("9");//GetStatusMasterDetailsByID("9");
				crValue = getReadableDatabase().rawQuery(qParamFileDown.getSql(), qParamFileDown.getSelectionArgs());
				if(crValue != null && crValue.getCount() > 0)
				{
					crValue.moveToFirst();
					String downloadStatus = crValue.getString(crValue.getColumnIndex("Status"));
					if(downloadStatus == null)
					{
						rtrVal = 3;
						isValid = false;
					}
					else if(!downloadStatus.equals("1"))
					{
						rtrVal = 3;
						isValid = false;
					}
				}
			}
			//END Check status 9(File Downloaded)==================================================================
			crValue = null;
			//Check GPRS Sent all billed connection or not=========================================================
			if(isValid)
			{
				QueryParameters qParamBilled = StoredProcedure.GetCountforBilledConnection();
				crValue = getReadableDatabase().rawQuery(qParamBilled.getSql(), null);
				if(crValue != null && crValue.getCount() > 0)
				{
					crValue.moveToFirst();
					billedTotal = crValue.getInt(crValue.getColumnIndex("COUNT"));
				}
				crValue = null;
				QueryParameters qParamGPRSSent = StoredProcedure.GetCountGPRSSentConnection();
				crValue = getReadableDatabase().rawQuery(qParamGPRSSent.getSql(), null);
				if(crValue != null && crValue.getCount() > 0)
				{
					crValue.moveToFirst();
					gprsTotal = crValue.getInt(crValue.getColumnIndex("COUNT"));
				}
				if(gprsTotal < billedTotal)
				{
					rtrVal = 4;
					isValid = false;
				}
			}
			//END Check GPRS Sent all billed connection or not=====================================================
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return rtrVal;
	}

	//Nitish Modified 16-03-2017
	public int InsertSBFromFile(String strDecrypt, Handler mainHan, final ProgressBar pb, final TextView lblStatus, long totalRecord,String filename)
	{
		Cursor crCheckCount = null;
		int Count = 0;
		long rtValue = 0;
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();
		//SQLiteDatabase sldb1 = dh.getWritableDatabase();
		InputStream ipStream = null;
		InputStreamReader ipStrReader = null;
		BufferedReader buffReader = null;
		try
		{
			ContentValues values = new ContentValues();//1
			pb.setProgress(0);
			pb.setMax((int)totalRecord);
			contentLength = totalRecord;
			mainHan.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					lblStatus.setText("");
				}
			});

			//DROP and RE-CREATE Tables in SBM_BillCollDataMain==============================================			
			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_SBM_BillCollDataMain);
			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_EC_FC_Slab);
			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_ReadSlabNTariff);
			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTION_TABLE);
			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_CASHCOUNTER_DETAILS);			

			//sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_Billing_Details);//15-07-2016
			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_EventLog);//15-07-2016

			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_SBM_ChequeDDImage);//16-03-2017

			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_Meter_Details);//10-03-2021
			
			//22-04-2021
			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_SBM_DISCONNECION);
			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_SBM_DISCONNECIONIMAGE);//13-02-2015
			
			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_Collection_Payment_Status);//11-05-2021

			sldb.execSQL("CREATE TABLE SBM_BillCollDataMain(UID INTEGER PRIMARY KEY, ForMonth TEXT, BillDate TEXT, "+
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
					"Mtr_Not_Visible INTEGER, DLTEc_GoK REAL ,AadharNo TEXT, VoterIdNo TEXT ," +
					"MtrLocFlag INTEGER,MtrMakeFlag INTEGER, MtrBodySealFlag INTEGER, MtrTerminalCoverFlag INTEGER , MtrTerminalCoverSealedFlag INTEGER,  FACValue TEXT, OldTecBill REAL, FC_Slab_3 REAL);");/**/ // Modified 29-06-2021

			sldb.execSQL("CREATE TABLE EC_FC_Slab(UID INTEGER PRIMARY KEY, ConnectionNo TEXT, "+
					"RRNo TEXT, ECRate_Count INTEGER, ECRate_Row INTEGER, FCRate_1 REAL, "+
					"FCRate_2 REAL, Units_1 REAL, Units_2 REAL, Units_3 REAL, Units_4 REAL, "+
					"Units_5 REAL, Units_6 REAL, EC_Rate_1 REAL, EC_Rate_2 REAL, "+
					"EC_Rate_3 REAL, EC_Rate_4 REAL, EC_Rate_5 REAL, EC_Rate_6 REAL, "+
					"EC_1 REAL, EC_2 REAL, EC_3 REAL, EC_4 REAL, EC_5 REAL, EC_6 REAl, "+
					"FC_1 REAL, FC_2 REAL, TEc REAL, EC_FL_1 REAL, EC_FL_2 REAL, "+
					"EC_FL_3 REAL, EC_FL_4 REAL, EC_FL REAL, new_TEc REAL, old_TEc REAL , "+
					"NEWFC_UNIT1 REAL, NEWFC_UNIT2 REAL, NEWFC_UNIT3 REAL, NEWFC_UNIT4 REAL , NEWFC_Rate3 REAL, NEWFC_Rate4 REAL, NEWFC3 REAL , NEWFC4 REAL);"); //23-06-2021

			sldb.execSQL("CREATE TABLE ReadSlabNTariff(UID INTEGER PRIMARY KEY, TarifCode INTEGER, TarifString TEXT);");

			sldb.execSQL("CREATE TABLE Collection_TABLE(UID INTEGER PRIMARY KEY,ConnectionNo TEXT,RRNo TEXT,CustomerName TEXT,RcptCnt INTEGER," +
					"Batch_No TEXT,Receipt_No TEXT,DateTime TEXT,Payment_Mode TEXT,Arrears TEXT,BillTotal TEXT,Paid_Amt INTEGER," +
					"BankID INTEGER,ChequeDDNo INTEGER,ChequeDDDate TEXT,Receipttypeflag TEXT," +
					"GvpId TEXT,SBMNumber TEXT,LocationCode TEXT,Gprs_Flag INTEGER,ArrearsBill_Flag INTEGER," +
					"ReaderCode TEXT,GPRS_Status TEXT,IODRemarks TEXT , ReprintCount INTEGER);"); //08-06-2017

			//Modified 26-06-2017
			sldb.execSQL("CREATE TABLE CashCounter_Details(UID INTEGER PRIMARY KEY,IMEINo TEXT,SIMNo TEXT,BatchDate TEXT,CashLimit TEXT," +
					"StartTime TEXT,EndTime TEXT,Batch_No TEXT,DateTime Text,LocationCode Text,CashCounterOpen TEXT,CounterCloseDateTime TEXT, ExtensionFlag TEXT , ExtensionDateTime TEXT,LastBatchReceiptNo TEXT," +
					"RevenueFlag TEXT,MiscFlag TEXT,ChqFlag TEXT,PartPaymentFlag TEXT,PartPaymentPerc TEXT,CDLimit TEXT , MRName TEXT, ReprintFlag TEXT);"); 


			//22-04-2021
			sldb.execSQL("CREATE TABLE SBM_Disconnection(UID INTEGER PRIMARY KEY, ForMnth TEXT, BillDate TEXT , ConnectionNo TEXT, TourPlanId Text, PreRead Text," +
					"TarifCode TEXT, IssueDateTime TEXT, RRNo TEXT, LocationCode TEXT, Mtr_DisFlag INTEGER, IMEINo TEXT, Gprs_Flag INTEGER, Gprs_Status TEXT );");

			sldb.execSQL("CREATE TABLE SBM_DisconnectionImage(UID INTEGER PRIMARY KEY, IMEINo TEXT ,ForMnth TEXT, BillDate TEXT, ConnectionNo TEXT, Mtr_DisFlag INTEGER,"+
					"PhotoName TEXT, Gprs_Flag INTEGER, CreatedDate TEXT );"); //13-02-2015

			
			
			sldb.execSQL("CREATE TABLE Meter_Details(UID INTEGER PRIMARY KEY, Meter_Id TEXT ,Meter_SerialNo TEXT, Meter_Date TEXT, Meter_Time TEXT, Meter_Reading TEXT,"+
					"Md TEXT, Pf TEXT, CreatedDate TEXT ,ConnectionNo TEXT , ForMonth TEXT ,MeterType TEXT , GPRS_Flag INTEGER );"); //10-03-2021

			//sldb.execSQL("CREATE TABLE Billing_Details(UID INTEGER PRIMARY KEY,IMEINo TEXT,SIMNo TEXT,BatchDate TEXT," +
			//		"StartTime TEXT,EndTime TEXT,Batch_No TEXT,DateTime Text,LocationCode TEXT,BillingOpen Text,BillingCloseDateTime TEXT , ExtensionFlag TEXT , ExtensionDateTime TEXT);"); //15-07-2016

			//25-07-2016
			sldb.execSQL("CREATE TABLE TABLE_EventLog(UID INTEGER PRIMARY KEY,IMEINO TEXT,SIMNO TEXT,Flag TEXT,Description TEXT,DateTime TEXT , GPRSFlag INTEGER, GPRS TEXT);");

			//16-03-2017
			sldb.execSQL("CREATE TABLE SBM_ChequeDDImage(UID INTEGER PRIMARY KEY, IMEINo TEXT ,BatchNo TEXT, ReceiptNo TEXT, ConnectionNo TEXT, Receipttypeflag TEXT,"+
					"PhotoName TEXT, Gprs_Flag INTEGER, CreatedDate TEXT );"); //16-03-2017

			sldb.execSQL("CREATE TABLE Collection_Payment_Status(UID INTEGER PRIMARY KEY, ConId TEXT ,BatchNo TEXT, ImeiNo TEXT, Lat TEXT, Long TEXT,"+
					"PaymentReasonId TEXT, ScheduledPayDate INTEGER, CreatedDate TEXT ,GprsFlag INTEGER ,GPRSStatus TEXT );"); //11-05-2021

			
			
			sldb.beginTransaction();
			String FACValue = "";
			StringBuilder sb = new StringBuilder();
			ipStream = new ByteArrayInputStream(strDecrypt.getBytes());
			ipStrReader = new InputStreamReader(ipStream);
			buffReader = new BufferedReader(ipStrReader);
			Total = 0;
			int lastLineCount = 0; //28-12-2017
			//DROP and RE-CREATE Tables in SBM_BillCollDataMain==============================================		
			while((ReceiveStr = buffReader.readLine()) != null)					
			{
				//ReceiveStr = scStrDec.useDelimiter("\r\n").next();
				sb.delete(0, sb.length());							
				sb.append(ReceiveStr);

				if(sb.toString().contains("^^"))
				{
					//rt = true;
				}
				else if(sb.toString().contains("@@"))
				{
					if(sb.toString().contains("|"))
					{										
						String[] arrSubD1 = sb.toString().split("\\|");						
						//Update Rows in StatusMaster====================================================================

						ContentValues valuesUpdateVersion = new ContentValues();
						valuesUpdateVersion.put("Value", arrSubD1[0].replace("@", ""));
						int upVer = sldb.update("StatusMaster", valuesUpdateVersion, " StatusID = ? ", new String[]{"2"});//Version

						ContentValues valuesUpdateSubDivision = new ContentValues();
						valuesUpdateSubDivision.put("Value", (arrSubD1[1].replace("$", "")).trim());
						int upSubDiv = sldb.update("StatusMaster", valuesUpdateSubDivision, " StatusID = ? ", new String[]{"15"});//SubDivision

						//16-03-2017
						ContentValues valuesUpdateFileInserted = new ContentValues();
						valuesUpdateFileInserted.put("Status", "1");
						valuesUpdateFileInserted.put("Value", filename);
						sldb.update("StatusMaster", valuesUpdateFileInserted, " StatusID in (?)", new String[]{"7"});

						//int upVer = UpdatestatusMaster("2", arrSubD1[0].replace("@", ""));
						//bdBackup.UpdatestatusMaster("2", arrSubD1[0].replace("@", ""));//Insert into Backup Database
						//int upSubDiv = UpdatestatusMaster("15", (arrSubD1[1].replace("$", "")).trim());
						//bdBackup.UpdatestatusMaster("15", (arrSubD1[1].replace("$", "")).trim());//Insert into Backup Database
						//END Update Rows in StatusMaster================================================================
						//Modified 19-12-2015
						//Example @@442|DEVANAHALLI#0.04$ 
						try
						{
							if(sb.toString().contains("#"))
							{
								ContentValues valuesUpdateSubDivisionnew = new ContentValues();
								valuesUpdateSubDivisionnew.put("Value", (arrSubD1[1].split("#")[0].trim()));
								int upSubDivnew = sldb.update("StatusMaster", valuesUpdateSubDivisionnew, " StatusID = ? ", new String[]{"15"});//SubDivision

								FACValue = arrSubD1[1].split("#")[1].replace("$", "").trim();	
							}
						}
						catch(Exception e)
						{
							Log.d(TAG, e.toString());
						}
					}


				}
				else if(sb.toString().contains("%%"))
				{
					ReadFileParameters.SlabNTariff snt = (new ReadFileParameters()).new SlabNTariff();
					String[] sntSplit = sb.toString().split("~");
					String[] sntSubSplit = sntSplit[0].toString().split("#");
					//INSERT Rows in ReadSlabNTariff=================================================================
					ContentValues valuesSNT = new ContentValues();
					valuesSNT.put("TarifCode", sntSubSplit[1]);
					valuesSNT.put("TarifString", sb.toString().trim());
					long tariffCount= sldb.insert("ReadSlabNTariff", null, valuesSNT);//
					//bdBackup.InsertSlabNTariffBackUpDB(sntSubSplit[1], sb.toString().trim());//Insert into Backup Database
					if(tariffCount <= 0)
					{	
						throw new Exception("Problem in Inserting ReadSlabNTariff");
					}
					//END INSERT Rows in ReadSlabNTariff=============================================================
				}
				else//Connection Insert
				{
					//if(sb.toString().length() == 653)//String should contain 653 characters 31-12-2014
					//if(sb.toString().length() == 685)//String should contain 685 characters 23-03-2017
					//Modified 28-12-2017
					//if((sb.toString().length() > 685))
					if((sb.toString().length() > 697)) //29-06-2021
						lastLineCount = lastLineCount +1;
					if(sb.toString().length() == 697 || sb.toString().length() == 1022 || (sb.toString().length() > 697 && lastLineCount==1)) //End  29-06-2021
					{

						Total++;
						/*QueryParameters qParam = StoredProcedure.GetConnectionCount(sb.substring(14, 24));
						crCheckCount = sldb.rawQuery(qParam.getSql(), qParam.getSelectionArgs());
						if(crCheckCount != null && crCheckCount.getCount() > 0)
						{
							crCheckCount.moveToFirst();
							Count = crCheckCount.getInt(crCheckCount.getColumnIndex("COUNT"));
							if(Count == 0)//Count If 
							{*/
						values.clear();
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

						//26-09-2015
						/*values.put("RRNo", sb.substring(196, 209));//31
						values.put("LegFol", sb.substring(209, 217));//32*/						
						values.put("RRNo", sb.substring(196, 217));//31 //13+8=21


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
						values.put("MobileNo", sb.substring(612, 642));//75

						//values.put("DLTEc_GoK", sb.substring(642,  sb.length()));//76 //31-12-2014
						//Start 23-03-2017
						values.put("DLTEc_GoK", sb.substring(642,  653));//77 //31-12-2014
						values.put("Gps_Latitude_image", sb.substring(653,  663));//78 //31-12-2014						
						values.put("Gps_Longitude_image", sb.substring(663,  673));//29-09-2016 //80
						values.put("DlCount", sb.substring(673, 675));//29-09-2016 //81
						values.put("MobileNo", sb.substring(675,  685));//29-09-2016 //82 //10 digits
						//End 23-03-2017

						values.put("FACValue", FACValue.trim().toString());//79 //19-12-2015
						
						//28-06-2021
						try
						{
							if(sb.substring(685,  697).trim().contains(" "))
							{
								values.put("FC_Slab_3", "0.00"); 
							}
							else
							{
								values.put("FC_Slab_3", sb.substring(685,  697).trim().length()==0 ? "0.00" : sb.substring(685,  697).trim()); //28-06-2021

							}
						}
						catch(Exception e)
						{
							values.put("FC_Slab_3", "0.00"); //28-06-2021
						}

						rtValue = sldb.insert("SBM_BillCollDataMain", null, values);//Insert into Main DB
						//bdBackup.InsertBillCollDataMainBackUpDB(sb.toString());//Insert into Backup Database
						if(rtValue <= 0)
						{	
							throw new Exception("Problem in Inserting SBM_BillCollDataMain");
						}
						if(rtValue <= 0)
						{
							mainHan.post(new Runnable() {								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//getWritableDatabase().endTransaction();
									CustomToast.makeText(mcntx, "Problem in Importing Data from file to DataBase." , Toast.LENGTH_SHORT);
									//throw new Exception("Insertion failed for SBM_BillCollDataMain");
									try {
										throw new Exception();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});

						}
						mainHan.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								pb.setProgress((int)(Total));												
								lblStatus.setText("Records Inserted..." + Total+" out of "+contentLength);//((totalLength*100)/contentLength)+"%");
								//pb.setProgress(i);
								//lblStatus.setText("Records Inserted :"+i+" out of "+Total);
							}
						});
						//}//END Count If 
						//}
					} 
				}//END Connection Insert
			}//END While Loop /**/

			ContentValues valuesUpdateConnBilled = new ContentValues();
			valuesUpdateConnBilled.putNull("Status");
			sldb.update("StatusMaster", valuesUpdateConnBilled, " StatusID in (?)", new String[]{"8"});

			ContentValues valuesUpdateFileDownload = new ContentValues();
			valuesUpdateFileDownload.putNull("Status");
			sldb.update("StatusMaster", valuesUpdateFileDownload, " StatusID in (?)", new String[]{"9"});

			ContentValues valuesUpdateGPRS = new ContentValues();
			valuesUpdateGPRS.putNull("Status");
			sldb.update("StatusMaster", valuesUpdateGPRS, " StatusID in (?)", new String[]{"10"});

			ContentValues valuesUpdateFileInserted = new ContentValues();
			valuesUpdateFileInserted.put("Status", "1");
			sldb.update("StatusMaster", valuesUpdateFileInserted, " StatusID in (?)", new String[]{"7"});

			//UpdateStatusMasterDetailsByID("8", null, "");
			//UpdateStatusMasterDetailsByID("9", null, "");
			//UpdateStatusMasterDetailsByID("10", null, "");
			//UpdateStatusMasterDetailsByID("7", "1", "");//Update new file Loaded in StatusMaster Status = 1 where StatusID = 7

			sldb.setTransactionSuccessful();
			return 1;
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
			//sldb.endTransaction();
			return 0;
		}
		finally
		{
			if(buffReader != null)
			{
				try {
					buffReader.close();									
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(ipStream != null)
			{
				try {
					ipStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			sldb.endTransaction();
		}
		//return rtValue;
	}
	//Created by Tamilselvan on 10-03-2014
	public int getCountofRecods()
	{
		Cursor crCount = null;
		int count = 0;
		QueryParameters qParam = StoredProcedure.GetCountBillCollDataMain();
		crCount = getReadableDatabase().rawQuery(qParam.getSql(), null);	
		if(crCount != null && crCount.getCount() > 0)//Cursor 1
		{
			crCount.moveToFirst();
			count = crCount.getInt(crCount.getColumnIndex("COUNT"));
		}
		return count;
	}
	//Created by Tamilselvan on 07-03-2014
	public StringBuilder GetDataToWrite(Handler mainHan, final ProgressBar pb, final TextView lblStatus, int progressTotal)
	{
		//ArrayList<WriteFileParameters> alwfp = new ArrayList<WriteFileParameters>();
		Cursor crWrite = null;
		StringBuilder sbWrite = new StringBuilder();
		StringBuilder fileWrite = new StringBuilder();
		try
		{
			QueryParameters qParam1 = StoredProcedure.GetDataToWriteFile();
			crWrite = getReadableDatabase().rawQuery(qParam1.getSql(), null);	
			if(crWrite != null && crWrite.getCount() > 0)//Cursor 2
			{
				if(progressTotal == 0)
				{
					pb.setProgress(0);
					pb.setMax(100);								
				}
				Total = crWrite.getCount();
				proTotal = progressTotal;
				crWrite.moveToFirst();
				do
				{
					WriteFileParameters wfp = new WriteFileParameters();
					wfp.setmForMonth(crWrite.getString(crWrite.getColumnIndex(COL_ForMonth)));//4
					wfp.setmBillDate(crWrite.getString(crWrite.getColumnIndex(COL_BillDate)));//8
					wfp.setmSubId(crWrite.getString(crWrite.getColumnIndex(COL_SubId)));//2
					wfp.setmConnectionNo(crWrite.getString(crWrite.getColumnIndex(COL_ConnectionNo)));//10
					wfp.setmTourplanId(crWrite.getString(crWrite.getColumnIndex(COL_TourplanId)));//9
					wfp.setmPF(crWrite.getString(crWrite.getColumnIndex(COL_PF)));//3
					wfp.setmSancLoad(crWrite.getString(crWrite.getColumnIndex(COL_SancLoad)));//5
					wfp.setmDlCount(crWrite.getString(crWrite.getColumnIndex(COL_DlCount)));//1
					wfp.setmBillFor(crWrite.getString(crWrite.getColumnIndex(COL_BillFor)));//1
					wfp.setmBlCnt(crWrite.getString(crWrite.getColumnIndex(COL_BlCnt)));//1
					wfp.setmTvmMtr(crWrite.getString(crWrite.getColumnIndex(COL_TvmMtr)));//1
					//Modified by Tamilselvan on 28-04-2014
					if(crWrite.getString(crWrite.getColumnIndex(COL_PreStatus)) == null || crWrite.getString(crWrite.getColumnIndex(COL_PreStatus)).equals("2") || crWrite.getString(crWrite.getColumnIndex(COL_PreStatus)).equals("9"))
					{
						wfp.setmPreRead("");//12
					}
					else
					{
						wfp.setmPreRead(crWrite.getString(crWrite.getColumnIndex(COL_PreRead)));//12
					}//END Modified by Tamilselvan on 28-04-2014
					//Space  //1
					//wfp.setmPreRead(crWrite.getString(crWrite.getColumnIndex(COL_PreRead)));//Present/Current Reading
					wfp.setmStatus(crWrite.getString(crWrite.getColumnIndex(COL_PreStatus)));//Present/Current Status//2
					wfp.setmSpotSerialNo(crWrite.getString(crWrite.getColumnIndex(COL_SpotSerialNo)));//5
					wfp.setMunits(crWrite.getString(crWrite.getColumnIndex(COL_Units)));//10
					wfp.setmTFc(crWrite.getString(crWrite.getColumnIndex(COL_TFc)));//12
					//wfp.setmTEc(crWrite.getString(crWrite.getColumnIndex(COL_TEc)));
					wfp.setmTEc(CommonFunction.GetStringforExpo(crWrite.getDouble(crWrite.getColumnIndex(COL_TEc))));//Modified on 02-04-2014//12
					wfp.setmFlReb(crWrite.getString(crWrite.getColumnIndex(COL_FLReb)));//9
					wfp.setmEcReb(crWrite.getString(crWrite.getColumnIndex(COL_ECReb)));//9
					wfp.setmTaxAmt(crWrite.getString(crWrite.getColumnIndex(COL_TaxAmt)));//10
					wfp.setmPfPenAmt(crWrite.getString(crWrite.getColumnIndex(COL_PfPenAmt)));//11
					wfp.setmPenExLd(crWrite.getString(crWrite.getColumnIndex(COL_PenExLd)));//11
					wfp.setmHCReb(crWrite.getString(crWrite.getColumnIndex(COL_HCReb)));//9
					wfp.setmHLReb(crWrite.getString(crWrite.getColumnIndex(COL_HLReb)));//9
					wfp.setmCapReb(crWrite.getString(crWrite.getColumnIndex(COL_CapReb)));//9
					wfp.setmExLoad(crWrite.getString(crWrite.getColumnIndex(COL_ExLoad)));//9
					wfp.setmDemandChrg(crWrite.getString(crWrite.getColumnIndex(COL_DemandChrg)));//11
					wfp.setmAccdRdg_rtn(crWrite.getString(crWrite.getColumnIndex(COL_AccdRdg_rtn)));//12
					wfp.setMkVAFR(crWrite.getString(crWrite.getColumnIndex(COL_KVAFR)));//12
					wfp.setmAbFlag(crWrite.getString(crWrite.getColumnIndex(COL_AbFlag)));//1
					wfp.setmBjKj2Lt2(crWrite.getString(crWrite.getColumnIndex(COL_BjKj2Lt2)));//1
					//wfp.setmRemarks(crWrite.getString(crWrite.getColumnIndex(COL_Remarks)));
					//Modified by Tamilselvan on 28-04-2014
					if(crWrite.getString(crWrite.getColumnIndex(COL_PreStatus)) != null)
					{
						if(crWrite.getString(crWrite.getColumnIndex(COL_PreStatus)).equals("2"))
						{
							wfp.setmRemarks(crWrite.getString(crWrite.getColumnIndex(COL_Remarks)));//30 //31-08-2016
						}
						else
						{
							wfp.setmRemarks(crWrite.getString(crWrite.getColumnIndex(COL_Remarks)));//30
						}//END Modified by Tamilselvan on 28-04-2014
					}
					else
					{
						wfp.setmRemarks("");//30
					}
					wfp.setmGoKPayable(crWrite.getString(crWrite.getColumnIndex(COL_GoKPayable)));//9
					wfp.setmIssueDateTime(crWrite.getString(crWrite.getColumnIndex(COL_IssueDateTime)));//12
					wfp.setmRecordDmnd(crWrite.getString(crWrite.getColumnIndex(COL_RecordDmnd)));//6
					wfp.setmKVA_Consumption(crWrite.getString(crWrite.getColumnIndex(COL_KVA_Consumption)));//6
					wfp.setmKVAAssd_Cons(crWrite.getString(crWrite.getColumnIndex(COL_KVAAssd_Cons)));//10
					wfp.setmTariffCode(crWrite.getString(crWrite.getColumnIndex(COL_TariffCode)));//3
					wfp.setmLinMin(crWrite.getString(crWrite.getColumnIndex(COL_LinMin)));//5
					wfp.setmPrevRdg(crWrite.getString(crWrite.getColumnIndex(COL_PrevRdg)));//12
					wfp.setmRRNo(crWrite.getString(crWrite.getColumnIndex(COL_RRNo)));//13
					//wfp.setmBillTotal(CommonFunction.GetStringforExpo(crWrite.getDouble(crWrite.getColumnIndex(COL_BillTotal))));//Modified on 02-04-2014
					if(crWrite.getString(crWrite.getColumnIndex(COL_BillTotal)) != null)
					{
						BigDecimal billTotal = (new BigDecimal(crWrite.getString(crWrite.getColumnIndex(COL_BillTotal))).setScale(2, BigDecimal.ROUND_HALF_UP)).setScale(0, BigDecimal.ROUND_HALF_UP);
						wfp.setmBillTotal(String.valueOf(billTotal));//Modified on 28-04-2014//11
					}
					else
					{
						wfp.setmBillTotal("");//11
					}
					wfp.setmSBMNumber(crWrite.getString(crWrite.getColumnIndex(COL_SBMNumber)));//10
					wfp.setmLocationCode(crWrite.getString(crWrite.getColumnIndex(COL_LocationCode)));//10
					wfp.setmEC_1(CommonFunction.GetStringforExpo(crWrite.getDouble(crWrite.getColumnIndex(COL_EC_1))));//Modified on 02-04-2014 //12
					wfp.setmEC_2(CommonFunction.GetStringforExpo(crWrite.getDouble(crWrite.getColumnIndex(COL_EC_2))));//Modified on 02-04-2014 //12
					wfp.setmEC_3(CommonFunction.GetStringforExpo(crWrite.getDouble(crWrite.getColumnIndex(COL_EC_3))));//Modified on 02-04-2014 //12
					wfp.setmEC_4(CommonFunction.GetStringforExpo(crWrite.getDouble(crWrite.getColumnIndex(COL_EC_4))));//Modified on 02-04-2014 //12
					wfp.setmMobileNo(crWrite.getString(crWrite.getColumnIndex(COL_MobileNo)));//17//410
					wfp.setmMtrDisFlag(crWrite.getString(crWrite.getColumnIndex(COL_MtrDisFlag)));//Battery Status //1//427
					wfp.setmMeter_type(crWrite.getString(crWrite.getColumnIndex(COL_Meter_type)));//Meter type//1//428
					wfp.setmMeter_serialno(crWrite.getString(crWrite.getColumnIndex(COL_Meter_serialno)));//10//429
					wfp.setmTcname_present(" "); //1//439
					wfp.setmMeter_present_serialno(crWrite.getString(crWrite.getColumnIndex(COL_METER_PRESENT_FLAG)));//Meter Plcaement //1//440
					wfp.setmMeter_not_visible(crWrite.getString(crWrite.getColumnIndex(COL_MTR_NOT_VISIBLE)));//Meter condition//1//441
					wfp.setmLatitude(crWrite.getString(crWrite.getColumnIndex(COL_Gps_Latitude_image)));//9//442
					wfp.setmLongitude(crWrite.getString(crWrite.getColumnIndex(COL_Gps_Longitude_image)));//9//451


					//30-07-2015
					wfp.setmAadharNo(crWrite.getString(crWrite.getColumnIndex(COL_AadharNo))); //12//460
					wfp.setmVoterIdNo(crWrite.getString(crWrite.getColumnIndex(COL_VoterIdNo))); //12//472				
					wfp.setmMtrMakeFlag(crWrite.getString(crWrite.getColumnIndex(COL_MtrMakeFlag))); //02//484
					wfp.setmMtrBodySealFlag(crWrite.getString(crWrite.getColumnIndex(COL_MtrBodySealFlag))); //1//486
					wfp.setmMtrTerminalCoverFlag(crWrite.getString(crWrite.getColumnIndex(COL_MtrTerminalCoverFlag))); //1//487
					wfp.setmMtrTerminalCoverSealedFlag(crWrite.getString(crWrite.getColumnIndex(COL_MtrTerminalCoverSealedFlag))); //1//488

					wfp.setmVer(ConstantClass.FileVersion); //3	//489			//version may change make it global		
					//wfp.setmEC_5(CommonFunction.GetStringforExpo(crWrite.getDouble(crWrite.getColumnIndex(COL_EC_5))));//Nitish on 15-04-2017
					//wfp.setmEC_6(CommonFunction.GetStringforExpo(crWrite.getDouble(crWrite.getColumnIndex(COL_EC_6))));	//Nitish on 15-04-2017



					//10-03-2021
					/*wfp.setmLTSlNo(crWrite.getString(crWrite.getColumnIndex(COL_Meter_Serialno_Meter_Details)) == null ? " " : crWrite.getString(crWrite.getColumnIndex(COL_Meter_Serialno_Meter_Details)).trim());	
					wfp.setmLTReading(crWrite.getString(crWrite.getColumnIndex(COL_Meter_Reading_Meter_Details))== null ? " " :crWrite.getString(crWrite.getColumnIndex(COL_Meter_Reading_Meter_Details)).trim());	
					wfp.setmLTMD(crWrite.getString(crWrite.getColumnIndex(COL_Md_Meter_Details))== null ? " " :crWrite.getString(crWrite.getColumnIndex(COL_Md_Meter_Details)).trim());	
					wfp.setmLTPF(crWrite.getString(crWrite.getColumnIndex(COL_Pf_Meter_Details))== null ? " " :crWrite.getString(crWrite.getColumnIndex(COL_Pf_Meter_Details)).trim());		
					wfp.setmLTMeterType(crWrite.getString(crWrite.getColumnIndex(COL_MeterType_Meter_Details)));	
					 */


					sbWrite.delete(0, sbWrite.length());
					sbWrite.append(wfp.getmForMonth());											
					sbWrite.append(wfp.getmBillDate());
					sbWrite.append(wfp.getmSubId());
					sbWrite.append(wfp.getmConnectionNo());
					sbWrite.append(wfp.getmTourplanId());
					sbWrite.append(wfp.getmPF());
					sbWrite.append(wfp.getmSancLoad());
					sbWrite.append(wfp.getmDlCount());
					sbWrite.append(wfp.getmBillFor());
					sbWrite.append(wfp.getmBlCnt());
					sbWrite.append(wfp.getmTvmMtr());
					sbWrite.append(wfp.getmPreRead());
					sbWrite.append(" ");// 1- space
					sbWrite.append(wfp.getmStatus());
					sbWrite.append(wfp.getmSpotSerialNo());
					sbWrite.append(wfp.getMunits());
					sbWrite.append(wfp.getmTFc());
					sbWrite.append(wfp.getmTEc());
					sbWrite.append(wfp.getmFlReb());
					sbWrite.append(wfp.getmEcReb());
					sbWrite.append(wfp.getmTaxAmt());
					sbWrite.append(wfp.getmPfPenAmt());
					sbWrite.append(wfp.getmPenExLd());
					sbWrite.append(wfp.getmHCReb());
					sbWrite.append(wfp.getmHLReb());
					sbWrite.append(wfp.getmCapReb());
					sbWrite.append(wfp.getmExLoad());
					sbWrite.append(wfp.getmDemandChrg());
					sbWrite.append(wfp.getmAccdRdg_rtn());
					sbWrite.append(wfp.getMkVAFR());
					sbWrite.append(wfp.getmAbFlag());
					sbWrite.append(wfp.getmBjKj2Lt2());
					sbWrite.append(wfp.getmRemarks());
					sbWrite.append(wfp.getmGoKPayable());
					sbWrite.append(wfp.getmIssueDateTime());
					sbWrite.append(wfp.getmRecordDmnd());
					sbWrite.append(wfp.getmKVA_Consumption());
					sbWrite.append(wfp.getmKVAAssd_Cons());
					sbWrite.append("0");//0
					sbWrite.append(wfp.getmTvmMtr());//TvmMtr again
					sbWrite.append(wfp.getmTariffCode());
					sbWrite.append(wfp.getmLinMin());
					sbWrite.append(wfp.getmPrevRdg());
					sbWrite.append(wfp.getmRRNo());
					sbWrite.append(wfp.getmBillTotal());
					sbWrite.append(wfp.getmSBMNumber());
					sbWrite.append(wfp.getmLocationCode());
					sbWrite.append(wfp.getmEC_1());
					sbWrite.append(wfp.getmEC_2());
					sbWrite.append(wfp.getmEC_3());
					sbWrite.append(wfp.getmEC_4());
					sbWrite.append(wfp.getmMobileNo());							
					sbWrite.append(wfp.getmMtrDisFlag());
					sbWrite.append(wfp.getmMeter_type());
					sbWrite.append(wfp.getmMeter_serialno());
					sbWrite.append(wfp.getmTcname_present());
					sbWrite.append(wfp.getmMeter_present_serialno());
					sbWrite.append(wfp.getmMeter_not_visible());
					sbWrite.append(wfp.getmLatitude());
					sbWrite.append(wfp.getmLongitude());

					//30-07-2015
					sbWrite.append(wfp.getmAadharNo());
					sbWrite.append(wfp.getmVoterIdNo());
					sbWrite.append(wfp.getmMtrMakeFlag());
					sbWrite.append(wfp.getmMtrBodySealFlag());
					sbWrite.append(wfp.getmMtrTerminalCoverFlag());
					sbWrite.append(wfp.getmMtrTerminalCoverSealedFlag());
					sbWrite.append(wfp.getmVer());
					//sbWrite.append(wfp.getmEC_5()); //Nitish 15-04-2017
					//sbWrite.append(wfp.getmEC_6()); //Nitish 15-04-2017

					//10-03-2021
					/*sbWrite.append(wfp.getmLTSlNo());
					sbWrite.append(wfp.getmLTReading());
					sbWrite.append(wfp.getmLTMD());
					sbWrite.append(wfp.getmLTPF());
					sbWrite.append(wfp.getmLTMeterType());*/

					fileWrite.append(sbWrite.toString());
					fileWrite.append("\r\n");//Tamilselvan on 29-03-2014

					i++;
					if(progressTotal == 0)
					{
						mainHan.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								pb.setProgress(((i/Total)*100)/4);
								lblStatus.setText("Reading Records..." + (((i/Total)*100)/4) + "%");
							}
						});
					}
					else
					{
						mainHan.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								pb.setProgress(proTotal + ((int)((i * 100)/Total))/5);
								lblStatus.setText("Please Wait... Creating Backup "+ String.valueOf(proTotal + ((int)((i * 100)/Total))/5) + "%");
							}
						});
					}
				}while(crWrite.moveToNext());
			}//END Cursor 2
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		WriteFileToSDCardActivity.BillingRecords = i;
		return fileWrite;
	}

	//Created By Tamilselvan 14-03-2014
	public ArrayList<String> GetDataSendToServer()
	{
		ArrayList<String> alStr = new ArrayList<String>();
		Cursor crWrite = null;
		StringBuilder sb = new StringBuilder();
		try
		{
			QueryParameters qParam = StoredProcedure.GetDataSentToServer();
			crWrite = getReadableDatabase().rawQuery(qParam.getSql(), null);
			if(crWrite != null && crWrite.getCount() > 0)
			{
				crWrite.moveToFirst();
				do
				{
					try
					{
						WriteFileParameters wfp = new WriteFileParameters();						
						wfp.setmForMonth(crWrite.getString(crWrite.getColumnIndex(COL_ForMonth)));//4
						wfp.setmBillDate(crWrite.getString(crWrite.getColumnIndex(COL_BillDate)));//8
						wfp.setmSubId(crWrite.getString(crWrite.getColumnIndex(COL_SubId)));//2
						wfp.setmConnectionNo(crWrite.getString(crWrite.getColumnIndex(COL_ConnectionNo)));//10
						wfp.setmTourplanId(crWrite.getString(crWrite.getColumnIndex(COL_TourplanId)));//9
						wfp.setmPF(crWrite.getString(crWrite.getColumnIndex(COL_PF)));//3
						wfp.setmSancLoad(crWrite.getString(crWrite.getColumnIndex(COL_SancLoad)));//5
						wfp.setmDlCount(crWrite.getString(crWrite.getColumnIndex(COL_DlCount)));//1
						wfp.setmBillFor(crWrite.getString(crWrite.getColumnIndex(COL_BillFor)));//1
						wfp.setmBlCnt(crWrite.getString(crWrite.getColumnIndex(COL_BlCnt)));//1
						wfp.setmTvmMtr(crWrite.getString(crWrite.getColumnIndex(COL_TvmMtr)));//1
						//Modified by Tamilselvan on 28-04-2014
						if(crWrite.getString(crWrite.getColumnIndex(COL_PreStatus)) == null || crWrite.getString(crWrite.getColumnIndex(COL_PreStatus)).equals("2") || crWrite.getString(crWrite.getColumnIndex(COL_PreStatus)).equals("9"))
						{
							wfp.setmPreRead("");//12
						}
						else
						{
							wfp.setmPreRead(crWrite.getString(crWrite.getColumnIndex(COL_PreRead)));//12
						}//END Modified by Tamilselvan on 28-04-2014
						//Space  //1
						//wfp.setmPreRead(crWrite.getString(crWrite.getColumnIndex(COL_PreRead)));//Present/Current Reading
						wfp.setmStatus(crWrite.getString(crWrite.getColumnIndex(COL_PreStatus)));//Present/Current Status//2
						wfp.setmSpotSerialNo(crWrite.getString(crWrite.getColumnIndex(COL_SpotSerialNo)));//5
						wfp.setMunits(crWrite.getString(crWrite.getColumnIndex(COL_Units)));//10
						wfp.setmTFc(crWrite.getString(crWrite.getColumnIndex(COL_TFc)));//12
						//wfp.setmTEc(crWrite.getString(crWrite.getColumnIndex(COL_TEc)));
						wfp.setmTEc(CommonFunction.GetStringforExpo(crWrite.getDouble(crWrite.getColumnIndex(COL_TEc))));//Modified on 02-04-2014//12
						wfp.setmFlReb(crWrite.getString(crWrite.getColumnIndex(COL_FLReb)));//9
						wfp.setmEcReb(crWrite.getString(crWrite.getColumnIndex(COL_ECReb)));//9
						wfp.setmTaxAmt(crWrite.getString(crWrite.getColumnIndex(COL_TaxAmt)));//10
						wfp.setmPfPenAmt(crWrite.getString(crWrite.getColumnIndex(COL_PfPenAmt)));//11
						wfp.setmPenExLd(crWrite.getString(crWrite.getColumnIndex(COL_PenExLd)));//11
						wfp.setmHCReb(crWrite.getString(crWrite.getColumnIndex(COL_HCReb)));//9
						wfp.setmHLReb(crWrite.getString(crWrite.getColumnIndex(COL_HLReb)));//9
						wfp.setmCapReb(crWrite.getString(crWrite.getColumnIndex(COL_CapReb)));//9
						wfp.setmExLoad(crWrite.getString(crWrite.getColumnIndex(COL_ExLoad)));//9
						wfp.setmDemandChrg(crWrite.getString(crWrite.getColumnIndex(COL_DemandChrg)));//11
						wfp.setmAccdRdg_rtn(crWrite.getString(crWrite.getColumnIndex(COL_AccdRdg_rtn)));//12
						wfp.setMkVAFR(crWrite.getString(crWrite.getColumnIndex(COL_KVAFR)));//12
						wfp.setmAbFlag(crWrite.getString(crWrite.getColumnIndex(COL_AbFlag)));//1
						wfp.setmBjKj2Lt2(crWrite.getString(crWrite.getColumnIndex(COL_BjKj2Lt2)));//1
						//wfp.setmRemarks(crWrite.getString(crWrite.getColumnIndex(COL_Remarks)));
						//Modified by Tamilselvan on 28-04-2014
						if(crWrite.getString(crWrite.getColumnIndex(COL_PreStatus)) != null)
						{
							if(crWrite.getString(crWrite.getColumnIndex(COL_PreStatus)).equals("2"))
							{
								wfp.setmRemarks(crWrite.getString(crWrite.getColumnIndex(COL_Remarks)));//30 //30-08-2016
							}
							else
							{
								wfp.setmRemarks(crWrite.getString(crWrite.getColumnIndex(COL_Remarks)));//30
							}//END Modified by Tamilselvan on 28-04-2014
						}
						else
						{
							wfp.setmRemarks("");//30
						}
						wfp.setmGoKPayable(crWrite.getString(crWrite.getColumnIndex(COL_GoKPayable)));//9
						wfp.setmIssueDateTime(crWrite.getString(crWrite.getColumnIndex(COL_IssueDateTime)));//12
						wfp.setmRecordDmnd(crWrite.getString(crWrite.getColumnIndex(COL_RecordDmnd)));//6
						wfp.setmKVA_Consumption(crWrite.getString(crWrite.getColumnIndex(COL_KVA_Consumption)));//6
						wfp.setmKVAAssd_Cons(crWrite.getString(crWrite.getColumnIndex(COL_KVAAssd_Cons)));//10
						wfp.setmTariffCode(crWrite.getString(crWrite.getColumnIndex(COL_TariffCode)));//3
						wfp.setmLinMin(crWrite.getString(crWrite.getColumnIndex(COL_LinMin)));//5
						wfp.setmPrevRdg(crWrite.getString(crWrite.getColumnIndex(COL_PrevRdg)));//12
						wfp.setmRRNo(crWrite.getString(crWrite.getColumnIndex(COL_RRNo)));//13
						//wfp.setmBillTotal(CommonFunction.GetStringforExpo(crWrite.getDouble(crWrite.getColumnIndex(COL_BillTotal))));//Modified on 02-04-2014
						if(crWrite.getString(crWrite.getColumnIndex(COL_BillTotal)) != null)
						{
							BigDecimal billTotal = (new BigDecimal(crWrite.getString(crWrite.getColumnIndex(COL_BillTotal))).setScale(2, BigDecimal.ROUND_HALF_UP)).setScale(0, BigDecimal.ROUND_HALF_UP);
							wfp.setmBillTotal(String.valueOf(billTotal));//Modified on 28-04-2014//11
						}
						else
						{
							wfp.setmBillTotal("");//11
						}
						wfp.setmSBMNumber(crWrite.getString(crWrite.getColumnIndex(COL_SBMNumber)));//10
						wfp.setmLocationCode(crWrite.getString(crWrite.getColumnIndex(COL_LocationCode)));//10
						wfp.setmEC_1(CommonFunction.GetStringforExpo(crWrite.getDouble(crWrite.getColumnIndex(COL_EC_1))));//Modified on 02-04-2014 //12
						wfp.setmEC_2(CommonFunction.GetStringforExpo(crWrite.getDouble(crWrite.getColumnIndex(COL_EC_2))));//Modified on 02-04-2014 //12
						wfp.setmEC_3(CommonFunction.GetStringforExpo(crWrite.getDouble(crWrite.getColumnIndex(COL_EC_3))));//Modified on 02-04-2014 //12
						wfp.setmEC_4(CommonFunction.GetStringforExpo(crWrite.getDouble(crWrite.getColumnIndex(COL_EC_4))));//Modified on 02-04-2014 //12
						wfp.setmMobileNo(crWrite.getString(crWrite.getColumnIndex(COL_MobileNo)));//17
						wfp.setmMtrDisFlag(crWrite.getString(crWrite.getColumnIndex(COL_MtrDisFlag)));//Installation //1
						wfp.setmMeter_type(crWrite.getString(crWrite.getColumnIndex(COL_Meter_type)));//Meter type//1
						wfp.setmMeter_serialno(crWrite.getString(crWrite.getColumnIndex(COL_Meter_serialno)));//10
						wfp.setmTcname_present(" "); //1
						wfp.setmMeter_present_serialno(crWrite.getString(crWrite.getColumnIndex(COL_METER_PRESENT_FLAG)));//Meter Fixed //1
						wfp.setmMeter_not_visible(crWrite.getString(crWrite.getColumnIndex(COL_MTR_NOT_VISIBLE)));//Meter condition//1//441
						wfp.setmLatitude(crWrite.getString(crWrite.getColumnIndex(COL_Gps_Latitude_image)));//9
						wfp.setmLongitude(crWrite.getString(crWrite.getColumnIndex(COL_Gps_Longitude_image)));//9


						//30-07-2015
						wfp.setmAadharNo(crWrite.getString(crWrite.getColumnIndex(COL_AadharNo))); //12
						wfp.setmVoterIdNo(crWrite.getString(crWrite.getColumnIndex(COL_VoterIdNo)));//12				
						wfp.setmMtrMakeFlag(crWrite.getString(crWrite.getColumnIndex(COL_MtrMakeFlag)));//2
						wfp.setmMtrBodySealFlag(crWrite.getString(crWrite.getColumnIndex(COL_MtrBodySealFlag)));//1
						wfp.setmMtrTerminalCoverFlag(crWrite.getString(crWrite.getColumnIndex(COL_MtrTerminalCoverFlag)));//1
						wfp.setmMtrTerminalCoverSealedFlag(crWrite.getString(crWrite.getColumnIndex(COL_MtrTerminalCoverSealedFlag)));//1

						wfp.setmVer(ConstantClass.FileVersion);//version may change make it global 3

						//wfp.setmEC_5(CommonFunction.GetStringforExpo(crWrite.getDouble(crWrite.getColumnIndex(COL_EC_5))));//Nitish on 15-04-2017
						//wfp.setmEC_6(CommonFunction.GetStringforExpo(crWrite.getDouble(crWrite.getColumnIndex(COL_EC_6))));	//Nitish on 15-04-2017

						//10-03-2021
						/*wfp.setmLTSlNo(crWrite.getString(crWrite.getColumnIndex(COL_Meter_Serialno_Meter_Details)) == null ? " " : crWrite.getString(crWrite.getColumnIndex(COL_Meter_Serialno_Meter_Details)).trim());	
						wfp.setmLTReading(crWrite.getString(crWrite.getColumnIndex(COL_Meter_Reading_Meter_Details))== null ? " " :crWrite.getString(crWrite.getColumnIndex(COL_Meter_Reading_Meter_Details)).trim());	
						wfp.setmLTMD(crWrite.getString(crWrite.getColumnIndex(COL_Md_Meter_Details))== null ? " " :crWrite.getString(crWrite.getColumnIndex(COL_Md_Meter_Details)).trim());	
						wfp.setmLTPF(crWrite.getString(crWrite.getColumnIndex(COL_Pf_Meter_Details))== null ? " " :crWrite.getString(crWrite.getColumnIndex(COL_Pf_Meter_Details)).trim());		
						wfp.setmLTMeterType(crWrite.getString(crWrite.getColumnIndex(COL_MeterType_Meter_Details)));	
						 */

						sb.delete(0, sb.length());
						sb.append(wfp.getmForMonth());											
						sb.append(wfp.getmBillDate());
						sb.append(wfp.getmSubId());
						sb.append(wfp.getmConnectionNo());
						sb.append(wfp.getmTourplanId());
						sb.append(wfp.getmPF());
						sb.append(wfp.getmSancLoad());
						sb.append(wfp.getmDlCount());
						sb.append(wfp.getmBillFor());
						sb.append(wfp.getmBlCnt());
						sb.append(wfp.getmTvmMtr());
						sb.append(wfp.getmPreRead());
						sb.append(" ");// 1- space
						sb.append(wfp.getmStatus());
						sb.append(wfp.getmSpotSerialNo());
						sb.append(wfp.getMunits());
						sb.append(wfp.getmTFc());
						sb.append(wfp.getmTEc());
						sb.append(wfp.getmFlReb());
						sb.append(wfp.getmEcReb());
						sb.append(wfp.getmTaxAmt());
						sb.append(wfp.getmPfPenAmt());
						sb.append(wfp.getmPenExLd());
						sb.append(wfp.getmHCReb());
						sb.append(wfp.getmHLReb());
						sb.append(wfp.getmCapReb());
						sb.append(wfp.getmExLoad());
						sb.append(wfp.getmDemandChrg());
						sb.append(wfp.getmAccdRdg_rtn());
						sb.append(wfp.getMkVAFR());
						sb.append(wfp.getmAbFlag());
						sb.append(wfp.getmBjKj2Lt2());
						sb.append(wfp.getmRemarks());
						sb.append(wfp.getmGoKPayable());
						sb.append(wfp.getmIssueDateTime());
						sb.append(wfp.getmRecordDmnd());
						sb.append(wfp.getmKVA_Consumption());
						sb.append(wfp.getmKVAAssd_Cons());
						sb.append("0");//0
						sb.append(wfp.getmTvmMtr());//TvmMtr again
						sb.append(wfp.getmTariffCode());
						sb.append(wfp.getmLinMin());
						sb.append(wfp.getmPrevRdg());
						sb.append(wfp.getmRRNo());
						sb.append(wfp.getmBillTotal());
						sb.append(wfp.getmSBMNumber());
						sb.append(wfp.getmLocationCode());
						sb.append(wfp.getmEC_1());
						sb.append(wfp.getmEC_2());
						sb.append(wfp.getmEC_3());
						sb.append(wfp.getmEC_4());
						sb.append(wfp.getmMobileNo());
						sb.append(wfp.getmMtrDisFlag());
						sb.append(wfp.getmMeter_type());
						sb.append(wfp.getmMeter_serialno());
						sb.append(wfp.getmTcname_present());
						sb.append(wfp.getmMeter_present_serialno());
						sb.append(wfp.getmMeter_not_visible());
						sb.append(wfp.getmLatitude());
						sb.append(wfp.getmLongitude());

						//30-07-2015
						sb.append(wfp.getmAadharNo());
						sb.append(wfp.getmVoterIdNo());
						sb.append(wfp.getmMtrMakeFlag());
						sb.append(wfp.getmMtrBodySealFlag());
						sb.append(wfp.getmMtrTerminalCoverFlag());
						sb.append(wfp.getmMtrTerminalCoverSealedFlag());
						sb.append(wfp.getmVer());	


						//10-03-2021
						/*sb.append(wfp.getmLTSlNo());
						sb.append(wfp.getmLTReading());
						sb.append(wfp.getmLTMD());
						sb.append(wfp.getmLTPF());						
						sb.append(wfp.getmLTMeterType());*/

						//sb.append(wfp.getmEC_5()); //Nitish 15-04-2017
						//sb.append(wfp.getmEC_6()); //Nitish 15-04-2017
						alStr.add(sb.toString());
					}
					catch(Exception e)
					{
						Log.d(TAG, e.toString());
					}

				}while(crWrite.moveToNext());
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return alStr;
	}/**/

	//Modified Nitish 27-04-2017
	public StringBuilder GetCollDataWriteToFile(Handler mainHan, final ProgressBar pb, final TextView lblStatus, int progressTotal)
	{
		Cursor crWriteColl = null;
		StringBuilder sb = new StringBuilder();
		StringBuilder sbCollection = new StringBuilder();
		try
		{
			QueryParameters qParam = StoredProcedure.GetCollDataWriteToFile();
			crWriteColl = getReadableDatabase().rawQuery(qParam.getSql(), null);		
			if(crWriteColl != null && crWriteColl.getCount() > 0)
			{
				if(progressTotal == 0)
				{
					Total = crWriteColl.getCount();
					proTotal = 25;
					i = 0;
				}
				else
				{
					Total = crWriteColl.getCount();
					proTotal = progressTotal;
					i = 0;
				}
				crWriteColl.moveToFirst();
				do
				{

					WriteFileParameters wfp = new WriteFileParameters();	

					wfp.setmReceipttypeflag_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_Receipttypeflag_COLLECTION_TABLE)));
					wfp.setmConsNo_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_ConnectionNo_COLLECTION_TABLE)));
					wfp.setmBatch_No_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_Batch_No_COLLECTION_TABLE)));					
					wfp.setmReceipt_No_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_Receipt_No_COLLECTION_TABLE)));
					wfp.setmGvpId_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_GvpId_COLLECTION_TABLE)));
					wfp.setmDateTime_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_DateTime_COLLECTION_TABLE)));
					wfp.setmPayment_Mode_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_Payment_Mode_COLLECTION_TABLE)));
					wfp.setmPaid_Amt_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_Paid_Amt_COLLECTION_TABLE)));
					wfp.setmChequeDDNo_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_ChequeDDNo_COLLECTION_TABLE)));
					wfp.setmChequeDDDate_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_ChequeDDDate_COLLECTION_TABLE)));;
					wfp.setmBankID_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_BankID_COLLECTION_TABLE)));
					wfp.setmSBMNumber_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_SBMNumber_COLLECTION_TABLE)));	
					wfp.setmLocation_code_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_LocationCode_COLLECTION_TABLE)));
					wfp.setmVer_Coll(ConstantClass.FileVersion);//version may change make it global	

					sb.delete(0, sb.length());
					sb.append(wfp.getmReceipttypeflag_Coll());											
					sb.append(wfp.getmConsNo_Coll());
					sb.append(wfp.getmBatch_No_Coll());
					sb.append(wfp.getmReceipt_No_Coll());
					sb.append(wfp.getmGvpId_Coll());
					sb.append(wfp.getmDateTime_Coll());
					sb.append(wfp.getmPayment_Mode_Coll());
					sb.append(wfp.getmPaid_Amt_Coll());
					sb.append(wfp.getmChequeDDNo_Coll());
					sb.append(wfp.getmChequeDDDate_Coll());
					sb.append(wfp.getmBankID_Coll());
					sb.append(wfp.getmSBMNumber_Coll());
					sb.append(wfp.getmLocation_code_Coll());
					sb.append(wfp.getmVer_Coll());

					sbCollection.append(sb.toString());
					sbCollection.append("\r\n");
					i++;
					if(progressTotal == 0)
					{
						mainHan.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								int sum = (proTotal + ((i/Total)*100)/4);
								pb.setProgress(sum);
								lblStatus.setText("Reading Records..." + sum + "%");
							}
						});
					}
					else
					{
						mainHan.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								int sum = (proTotal + ((i/Total)*100)/5);
								pb.setProgress(sum);
								lblStatus.setText("Please Wait... Creating Backup " + sum + "%");
							}
						});
					}
				}while(crWriteColl.moveToNext());
			}
			else//No Record in Collection
			{
				if(progressTotal == 0)
				{
					proTotal = 25;

					mainHan.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							int sum = (proTotal + 25);
							pb.setProgress(sum);
							lblStatus.setText("Reading Records..." + (sum) + "%");
						}
					});
				}
				else
				{
					proTotal = progressTotal;

					mainHan.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							int sum = (proTotal + 20);
							pb.setProgress(sum);
							lblStatus.setText("Please Wait... Creating Backup " + (sum)  + "%");
						}
					});
				}
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		WriteFileToSDCardActivity.CollectionRecords = i;
		return sbCollection;
	}


	/**
	 * Tamilselvan on 14-03-2014
	 * Update Gprs_Flag = 1 in SBM_BillCollDataMain by ConnectionNo
	 * @param ConnectionNo
	 * @return
	 */
	public String UpdateBlCnt(String ConnectionNo)
	{	
		String strValue = "";
		try
		{
			ContentValues valuesUpdateMaintbl = new ContentValues();
			valuesUpdateMaintbl.put("Gprs_Flag", "1");
			int up = getWritableDatabase().update("SBM_BillCollDataMain", valuesUpdateMaintbl, "ConnectionNo = ?", new String[]{ConnectionNo});
			strValue = GPRSFlag();
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return strValue;
	}

	/**
	 * Tamilselvan on 19-04-2014
	 * GET GPRS ===> Billed count and GPRS SENT Count
	 * @return
	 */
	public String GPRSFlag()
	{
		Cursor crCount = null;
		String strValue = "";
		try	
		{
			QueryParameters qParam1 = StoredProcedure.GetCountGPRSSentConnection();
			crCount = getReadableDatabase().rawQuery(qParam1.getSql(), null);
			if(crCount != null && crCount.getCount() > 0)
			{
				crCount.moveToFirst();
				strValue = crCount.getString(crCount.getColumnIndex("COUNT"));
			}
			strValue += "/";
			QueryParameters qParam = StoredProcedure.GetCountforBilledConnection();
			crCount = getReadableDatabase().rawQuery(qParam.getSql(), null);
			if(crCount != null && crCount.getCount() > 0)
			{
				crCount.moveToFirst();
				strValue += crCount.getString(crCount.getColumnIndex("COUNT"));
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return strValue;
	}
	/**
	 * Tamilselvan on 19-04-2014
	 * GET Billed Count and Total Connection Count
	 * @return
	 */
	public String ConnectionBilledFlag()
	{
		Cursor crCount = null;
		String strValue = "";
		try	
		{
			QueryParameters qParam = StoredProcedure.GetCountforBilledConnection();
			crCount = getReadableDatabase().rawQuery(qParam.getSql(), null);
			if(crCount != null && crCount.getCount() > 0)
			{
				strValue = crCount.getString(crCount.getColumnIndex("COUNT"));
			}
			strValue += "/";
			QueryParameters qParam1 = StoredProcedure.GetCountBillCollDataMain();
			crCount = getReadableDatabase().rawQuery(qParam1.getSql(), null);
			if(crCount != null && crCount.getCount() > 0)
			{
				strValue += crCount.getString(crCount.getColumnIndex("COUNT"));
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return strValue;		
	}
	//Tamilselvan on 15-03-2014
	public void UpdateRePrint()//Check reprint count and Increment the count for each reprint 
	{
		Cursor crCount = null;
		int count = 0;
		try
		{
			QueryParameters qParam = StoredProcedure.GetReprintCount();
			crCount = getReadableDatabase().rawQuery(qParam.getSql(), null);		
			if(crCount != null && crCount.getCount() > 0)
			{
				crCount.moveToFirst();
				count = Integer.valueOf(crCount.getString(crCount.getColumnIndex(COL_Count)));
			}
			else
			{
				count = 1;
			}
			ContentValues valuesUpdateMaintbl = new ContentValues();
			valuesUpdateMaintbl.put("Gprs_Flag", count);
			getWritableDatabase().update("SBM_BillCollDataMain", valuesUpdateMaintbl, null, null);
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}
	//Tamilselvan on 17-03-2014
	public boolean MovetoNextConnectionNo(String Uid)
	{
		Cursor crCount = null;
		int count = 0;
		boolean boolConn = true;
		try	
		{
			QueryParameters qParamCount = StoredProcedure.GetCountforNotBilledConnection();//Get Not Billed count
			crCount = getReadableDatabase().rawQuery(qParamCount.getSql(), null);	
			if(crCount != null && crCount.getCount() > 0)
			{
				crCount.moveToFirst();
				count = crCount.getInt(crCount.getColumnIndex("COUNT"));//Get Not Billed count
				if(count > 0)//Count Not Billed 
				{
					crCount = null;
					QueryParameters qParam = StoredProcedure.GetNextConnectionDetails(Uid);
					crCount = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());		
					if(crCount != null && crCount.getCount() > 0)
					{
						crCount.moveToFirst();
						GetAllDatafromDb(crCount.getString(crCount.getColumnIndex(COL_ConnectionNo)));
						boolConn = true;
					}
				}
				else//Get Not Billed count
				{
					boolConn = false;
				}
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return boolConn;
	}
	//Tamilselvan on 18-03-2014
	public int UpdatestatusMaster(String StatusId, String Value)
	{
		int update = 0, count = 0;
		Cursor crCount = null;
		try
		{			
			QueryParameters qParam = StoredProcedure.GetStatusMasterCountByID(StatusId);
			crCount = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());		
			if(crCount != null && crCount.getCount() > 0)
			{
				crCount.moveToFirst();
				count = crCount.getInt(crCount.getColumnIndex("COUNT"));
				if(count > 0)
				{
					ContentValues valuesUpdateSubDiv = new ContentValues();
					valuesUpdateSubDiv.put("Value", Value);
					update = getWritableDatabase().update("StatusMaster", valuesUpdateSubDiv, " StatusID = ? ", new String[]{StatusId});
				}
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return update;
	}
	//Tamilselvan on 29-03-2014
	public int UpdateStatusInStatusMaster(String StatusId, String Status)
	{
		int UpStatus = 0;
		try
		{
			ContentValues valuesUpdateSubDiv = new ContentValues();
			valuesUpdateSubDiv.put("Status", Status);
			UpStatus = getWritableDatabase().update("StatusMaster", valuesUpdateSubDiv, " StatusID in (?)", new String[]{StatusId});
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return UpStatus;
	}
	//Tamilselvan on 29-03-2014
	public int GetStatusMasterDetailsByID(String StatusId)
	{
		int Status = 0;
		Cursor crDetails = null;
		try
		{
			QueryParameters qParam = StoredProcedure.GetStatusMasterDetailsByID(StatusId);
			crDetails = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());
			if(crDetails != null && crDetails.getCount() > 0)
			{
				crDetails.moveToFirst();
				Status = crDetails.getInt(crDetails.getColumnIndex("Status"));
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return Status;
	}
	/**
	 * Update Status Master -- Status and Value by StatusID
	 * @param StatusId
	 * @param status
	 * @param value
	 * @return 
	 */
	public int UpdateStatusMasterDetailsByID(String StatusId, String status, String value)
	{
		int UpStatus = 0;
		try
		{
			ContentValues valuesUpdateSubDiv = new ContentValues();
			valuesUpdateSubDiv.put("Status", status);
			valuesUpdateSubDiv.put("Value", value);
			UpStatus = getWritableDatabase().update("StatusMaster", valuesUpdateSubDiv, " StatusID in (?)", new String[]{StatusId});
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return UpStatus;
	}

	/**
	 * Tamilselvan on 18-03-2014
	 * Get Subdivision Name
	 * @return subDiv
	 */
	public String GetSubDivisionName()
	{
		Cursor crCount = null;
		String subDiv = "";
		try
		{
			QueryParameters qParam = StoredProcedure.GetStatusMasterDetailsByID("15");
			crCount = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());		
			if(crCount != null && crCount.getCount() > 0)
			{
				crCount.moveToFirst();
				//crCount.moveToLast();
				subDiv = crCount.getString(crCount.getColumnIndex(COL_VALUE_STATUSMASTER));
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return subDiv;
	}
	//Tamilselvan on 02-04-2014
	//Checking UId exists in Database or not
	public boolean IsUIDExists(int uid)
	{
		Cursor crCount = null;
		boolean isExits = false;
		try
		{
			QueryParameters qParam = StoredProcedure.CheckUIDExists(uid);
			crCount = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());		
			if(crCount != null && crCount.getCount() > 0)
			{
				crCount.moveToFirst();
				if(crCount.getInt(crCount.getColumnIndex("COUNT")) > 0)
				{
					isExits = true;
				}
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return isExits;
	}
	//Tamilselvan on 05-04-2014
	//Check Every Connection Billed or Not
	public boolean isEveryConnectionBilled()
	{
		Cursor crBillCount = null, crBilledCount = null;
		boolean isBilled = false;
		int TotalBill = 0, TotalBilled = 0;
		try
		{
			//Get Total Connection===========================================================
			QueryParameters qParam = StoredProcedure.GetCountBillCollDataMain();
			crBillCount = getReadableDatabase().rawQuery(qParam.getSql(), null);		
			if(crBillCount != null && crBillCount.getCount() > 0)
			{
				crBillCount.moveToFirst();
				TotalBill = crBillCount.getInt(crBillCount.getColumnIndex("COUNT"));
			}
			//END Get Total Connection=======================================================

			//Get Total Connection===========================================================
			QueryParameters qParam1 = StoredProcedure.GetCountforBilledConnection();
			crBilledCount = getReadableDatabase().rawQuery(qParam1.getSql(), null);		
			if(crBilledCount != null && crBilledCount.getCount() > 0)
			{
				crBilledCount.moveToFirst();
				TotalBilled = crBilledCount.getInt(crBilledCount.getColumnIndex("COUNT"));
			}
			//END Get Total Connection=======================================================
			if(TotalBill == TotalBilled)
			{
				isBilled = true;
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return isBilled;
	}
	/**
	 * Tamilselvan on 29-04-2014
	 * get Photo name form DB by ConnectionNo
	 * @return
	 */
	public String GetPhotoNameByConnectionNo(String ConnectionNo)
	{
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getReadableDatabase();
		Cursor crImgName = null;
		String ImgName = "";
		try
		{
			QueryParameters qParam = StoredProcedure.GetPhotoNameByConnectionNo(ConnectionNo);//sql query for get count for billed connection 
			crImgName = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());
			if(crImgName != null && crImgName.getCount() > 0)
			{
				crImgName.moveToFirst();
				ImgName = crImgName.getString(crImgName.getColumnIndex("PhotoName"));
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return ImgName;
	}
	/**
	 * Tamilselvan on 12-03-2014 for Bill Save 
	 * @return 1 as Saved succussfully, 0 as problem
	 */
	public int BillSave()
	{
		int rtrValue = 0;
		try
		{
			DatabaseHelper dh = new DatabaseHelper(mcntx);
			SQLiteDatabase sldb = dh.getWritableDatabase();
			CommonFunction cFun = new CommonFunction();
			ReadSlabNTarifSbmBillCollection sbc = BillingObject.GetBillingObject();
			try
			{
				Cursor check = null;
				//Check For Serial Number===========================================================================
				QueryParameters qParam = StoredProcedure.GetCountforBilledConnection();//sql query for get count for billed connection 
				check = getReadableDatabase().rawQuery(qParam.getSql(), null);			
				if(check != null && check.getCount() > 0)
				{
					check.moveToFirst();
					if(check.getInt(check.getColumnIndex("COUNT")) == 0)//Nothing is Billed then 
					{//hard code 1 as serial number.
						sbc.setmSpotSerialNo(1);
					}
					else if(check.getInt(check.getColumnIndex("COUNT")) > 0)//get the max spot serial number make + 1 and assign
					{
						qParam = StoredProcedure.GetMaxSpotSerialNumber();//sql query for get count for billed connection 
						check = getReadableDatabase().rawQuery(qParam.getSql(), null);
						if(check != null && check.getCount() > 0)
						{
							check.moveToFirst();
							sbc.setmSpotSerialNo(check.getInt(check.getColumnIndex("SpotSerialNo")) + 1);
						}
					}
				}
				//END Check For Serial Number=======================================================================
				sbc.setmIssueDateTime(cFun.GetCurrentTimeWOSPChar());
				sbc.setmBlCnt("1");//Added on 09-06-2014 
				sldb.beginTransaction();
				ContentValues valuesUpdateMaintbl = new ContentValues();
				valuesUpdateMaintbl.put("SancHp", String.valueOf(sbc.getmSancHp()));
				valuesUpdateMaintbl.put("PF", sbc.getmPF());
				valuesUpdateMaintbl.put("LinMin", String.valueOf(sbc.getmLinMin()));
				valuesUpdateMaintbl.put("BlCnt", sbc.getmBlCnt());//set BlCnt as 1 (it is billed) 
				valuesUpdateMaintbl.put("PreRead", sbc.getmPreRead());//Present Reading
				valuesUpdateMaintbl.put("PreStatus", sbc.getmPreStatus());//Present Status
				valuesUpdateMaintbl.put("spotserialno", sbc.getmSpotSerialNo());//-->SpotSerialNo
				valuesUpdateMaintbl.put("units", sbc.getmUnits());//-->Consumption
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

				//21-04-2016
				valuesUpdateMaintbl.put("OldTecBill", String.valueOf(sbc.getmOldTecBill().setScale(2, RoundingMode.HALF_UP))); //30-04-2015
				//30-08-2014				
				valuesUpdateMaintbl.put("Meter_Type", sbc.getmMeter_type());//Meter Type
				valuesUpdateMaintbl.put("MtrDisFlag", sbc.getmMtrDisFlag());//Meter Fixed
				valuesUpdateMaintbl.put("Meter_Present_Flag", sbc.getmMeter_Present_Flag()); //Installation
				valuesUpdateMaintbl.put("MtrMakeFlag", sbc.getmMtrMakeFlag());	//Meter Make
				valuesUpdateMaintbl.put("Mtr_Not_Visible", sbc.getmMtr_Not_Visible());	//Meter Location Inside or Outside					
				//30-07-2015				
				valuesUpdateMaintbl.put("AadharNo", sbc.getmAadharNo());
				valuesUpdateMaintbl.put("VoterIdNo", sbc.getmVoterIdNo());
				valuesUpdateMaintbl.put("MobileNo", sbc.getmMobileNo());
				valuesUpdateMaintbl.put("MtrBodySealFlag", sbc.getmMtrBodySealFlag()); //Yes or No --1 or 2
				valuesUpdateMaintbl.put("MtrTerminalCoverFlag", sbc.getmMtrTerminalCoverFlag());//Yes or No --1 or 2
				valuesUpdateMaintbl.put("MtrTerminalCoverSealedFlag", sbc.getmMtrTerminalCoverSealedFlag());//Yes or No --1 or 2

				int updateResult = sldb.update("SBM_BillCollDataMain", valuesUpdateMaintbl, "ConnectionNo = ?", new String[]{sbc.getmConnectionNo()});
				if(updateResult <= 0)
				{				
					sldb.endTransaction();
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
				
				//23-06-2021
				valuesInsertSlabtbl.put("NEWFC_UNIT1", String.valueOf(sbc.getMmNEWFC_UNIT1()));
				valuesInsertSlabtbl.put("NEWFC_UNIT2", String.valueOf(sbc.getMmNEWFC_UNIT2()));
				valuesInsertSlabtbl.put("NEWFC_UNIT3", String.valueOf(sbc.getMmNEWFC_UNIT3()));
				valuesInsertSlabtbl.put("NEWFC_UNIT4", String.valueOf(sbc.getMmNEWFC_UNIT4()));
				
				valuesInsertSlabtbl.put("NEWFC_Rate3", String.valueOf(sbc.getMmNEWFC_Rate3()));
				valuesInsertSlabtbl.put("NEWFC_Rate4", String.valueOf(sbc.getMmNEWFC_Rate4()));
				valuesInsertSlabtbl.put("NEWFC3", String.valueOf(sbc.getMmNEWFC3()));
				valuesInsertSlabtbl.put("NEWFC4", String.valueOf(sbc.getMmNEWFC4()));
				
				long insertResult = sldb.insert("EC_FC_Slab", null, valuesInsertSlabtbl);
				if(insertResult <= 0)
				{	
					sldb.endTransaction();
					throw new Exception("Insertion failed for EC_FC_Slab");
				}
				ContentValues valuesUpdateSubDiv = new ContentValues();
				valuesUpdateSubDiv.put("Status", "1");
				sldb.update("StatusMaster", valuesUpdateSubDiv, " StatusID in (?)", new String[]{"8"});
				ContentValues valuesUpdateFileDownload = new ContentValues();
				valuesUpdateFileDownload.put("Status", "");
				sldb.update("StatusMaster", valuesUpdateFileDownload, " StatusID in (?)", new String[]{"9"});
				//UpdateStatusMasterDetailsByID("8", "1", "");
				sldb.setTransactionSuccessful();
				//DatabaseHelperBackupDB bdBackup = new DatabaseHelperBackupDB(mcntx);
				//bdBackup.BackupDBBillSave();//Back Up database save
				rtrValue = 1;
				//return 1;
			}
			catch(Exception e)
			{
				Log.d("", e.toString());
				//return 0;
				rtrValue = 0;
			}	
			finally
			{
				sldb.endTransaction();
			}
		}
		catch(Exception e)
		{
			Log.d("", e.toString());
		}	
		return rtrValue;
	}
	//Nitish 30-08-2014
	public DDLAdapter GetReason() throws Exception
	{
		DDLAdapter lList=null;
		cr=null ;
		try {
			QueryParameters qParam=StoredProcedure.GetReason();		
			cr=getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());
			for(int i=1; i<=cr.getCount();++i)
			{
				if(lList==null)
				{
					lList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());
				}
				if(i==1)
					cr.moveToFirst();
				else
					cr.moveToNext();
				lList.AddItem(String.valueOf(cr.getInt(cr.getColumnIndex(COL_REASONMASTER_REASONID))), cr.getString(cr.getColumnIndex(COL_REASONMASTER_REASON)));
			}
		} catch (Exception e) {			
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return lList;
	}
	//Modified 30-08-2014 added ReasonId
	public int InsertSBFromserver(String strDecrypt, Handler mainHan, final ProgressBar pb, final TextView lblStatus, long totalRecord,String filename) //Modified 10-03-2021
	{

		Cursor crCheckCount = null;
		int Count = 0;
		long rtValue = 0;
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();
		//SQLiteDatabase sldb1 = dh.getWritableDatabase();
		InputStream ipStream = null;
		InputStreamReader ipStrReader = null;
		BufferedReader buffReader = null;

		try    
		{
			ContentValues values = new ContentValues();//1
			pb.setProgress(0);
			pb.setMax((int)totalRecord);
			contentLength = totalRecord;
			mainHan.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					lblStatus.setText("");
				}
			});
			//DROP and RE-CREATE Tables in SBM_BillCollDataMain==============================================			
			//DROP and RE-CREATE Tables in SBM_BillCollDataMain==============================================			
			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_SBM_BillCollDataMain);
			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_EC_FC_Slab);
			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_ReadSlabNTariff);
			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTION_TABLE);
			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_CASHCOUNTER_DETAILS);	
			//22-04-2021
			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_SBM_DISCONNECION);
			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_SBM_DISCONNECIONIMAGE);//13-02-2015
			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_EventLog);//15-07-2016

			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_Meter_Details);//10-03-2021
			
			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_Collection_Payment_Status);//11-05-2021

			sldb.execSQL("CREATE TABLE SBM_BillCollDataMain(UID INTEGER PRIMARY KEY, ForMonth TEXT, BillDate TEXT, "+
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
					"Mtr_Not_Visible INTEGER, DLTEc_GoK REAL,AadharNo TEXT, VoterIdNo TEXT ," +
					"MtrLocFlag INTEGER,MtrMakeFlag INTEGER, MtrBodySealFlag INTEGER, MtrTerminalCoverFlag INTEGER , MtrTerminalCoverSealedFlag INTEGER ,FACValue TEXT , OldTecBill REAL, FC_Slab_3 REAL);");/**/ // Modified 29-06-2021

			sldb.execSQL("CREATE TABLE EC_FC_Slab(UID INTEGER PRIMARY KEY, ConnectionNo TEXT, "+
					"RRNo TEXT, ECRate_Count INTEGER, ECRate_Row INTEGER, FCRate_1 REAL, "+
					"FCRate_2 REAL, Units_1 REAL, Units_2 REAL, Units_3 REAL, Units_4 REAL, "+
					"Units_5 REAL, Units_6 REAL, EC_Rate_1 REAL, EC_Rate_2 REAL, "+
					"EC_Rate_3 REAL, EC_Rate_4 REAL, EC_Rate_5 REAL, EC_Rate_6 REAL, "+
					"EC_1 REAL, EC_2 REAL, EC_3 REAL, EC_4 REAL, EC_5 REAL, EC_6 REAl, "+
					"FC_1 REAL, FC_2 REAL, TEc REAL, EC_FL_1 REAL, EC_FL_2 REAL, "+
					"EC_FL_3 REAL, EC_FL_4 REAL, EC_FL REAL, new_TEc REAL, old_TEc REAL , " +
					"NEWFC_UNIT1 REAL, NEWFC_UNIT2 REAL, NEWFC_UNIT3 REAL, NEWFC_UNIT4 REAL , NEWFC_Rate3 REAL, NEWFC_Rate4 REAL, NEWFC3 REAL , NEWFC4 REAL);"); //23-06-2021

			sldb.execSQL("CREATE TABLE ReadSlabNTariff(UID INTEGER PRIMARY KEY, TarifCode INTEGER, TarifString TEXT);");

			sldb.execSQL("CREATE TABLE Collection_TABLE(UID INTEGER PRIMARY KEY,ConnectionNo TEXT,RRNo TEXT,CustomerName TEXT,RcptCnt INTEGER," +
					"Batch_No TEXT,Receipt_No TEXT,DateTime TEXT,Payment_Mode TEXT,Arrears TEXT,BillTotal TEXT,Paid_Amt INTEGER," +
					"BankID INTEGER,ChequeDDNo INTEGER,ChequeDDDate TEXT,Receipttypeflag TEXT," +
					"GvpId TEXT,SBMNumber TEXT,LocationCode TEXT,Gprs_Flag INTEGER,ArrearsBill_Flag INTEGER," +
					"ReaderCode TEXT,GPRS_Status TEXT,IODRemarks TEXT ,ReprintCount INTEGER);");//08-06-2017

			//Modified 26-06-2017
			sldb.execSQL("CREATE TABLE CashCounter_Details(UID INTEGER PRIMARY KEY,IMEINo TEXT,SIMNo TEXT,BatchDate TEXT,CashLimit TEXT," +
					"StartTime TEXT,EndTime TEXT,Batch_No TEXT,DateTime Text,LocationCode Text,CashCounterOpen TEXT,CounterCloseDateTime TEXT, ExtensionFlag TEXT , ExtensionDateTime TEXT,LastBatchReceiptNo TEXT," +
					"RevenueFlag TEXT,MiscFlag TEXT,ChqFlag TEXT,PartPaymentFlag TEXT,PartPaymentPerc TEXT,CDLimit TEXT , MRName TEXT, ReprintFlag TEXT);"); 

			//22-04-2021
			sldb.execSQL("CREATE TABLE SBM_Disconnection(UID INTEGER PRIMARY KEY, ForMnth TEXT, BillDate TEXT , ConnectionNo TEXT, TourPlanId Text, PreRead Text," +
					"TarifCode TEXT, IssueDateTime TEXT, RRNo TEXT, LocationCode TEXT, Mtr_DisFlag INTEGER, IMEINo TEXT, Gprs_Flag INTEGER, Gprs_Status TEXT );");

			sldb.execSQL("CREATE TABLE SBM_DisconnectionImage(UID INTEGER PRIMARY KEY, IMEINo TEXT ,ForMnth TEXT, BillDate TEXT, ConnectionNo TEXT, Mtr_DisFlag INTEGER,"+
					"PhotoName TEXT, Gprs_Flag INTEGER, CreatedDate TEXT );"); 
			
			sldb.execSQL("CREATE TABLE Meter_Details(UID INTEGER PRIMARY KEY, Meter_Id TEXT ,Meter_SerialNo TEXT, Meter_Date TEXT, Meter_Time TEXT, Meter_Reading TEXT,"+
					"Md TEXT, Pf TEXT, CreatedDate TEXT ,ConnectionNo TEXT , ForMonth TEXT , MeterType TEXT , GPRS_Flag INTEGER );"); //10-03-2021

			//sldb.execSQL("CREATE TABLE Billing_Details(UID INTEGER PRIMARY KEY,IMEINo TEXT,SIMNo TEXT,BatchDate TEXT," +
			//		"StartTime TEXT,EndTime TEXT,Batch_No TEXT,DateTime Text,LocationCode TEXT,BillingOpen Text,BillingCloseDateTime TEXT , ExtensionFlag TEXT , ExtensionDateTime TEXT);"); //15-07-2016

			//25-07-2016
			sldb.execSQL("CREATE TABLE TABLE_EventLog(UID INTEGER PRIMARY KEY,IMEINO TEXT,SIMNO TEXT,Flag TEXT,Description TEXT,DateTime TEXT , GPRSFlag INTEGER, GPRS TEXT);");

			sldb.execSQL("CREATE TABLE Collection_Payment_Status(UID INTEGER PRIMARY KEY, ConId TEXT ,BatchNo TEXT, ImeiNo TEXT, Lat TEXT, Long TEXT,"+
					"PaymentReasonId TEXT, ScheduledPayDate INTEGER, CreatedDate TEXT ,GprsFlag INTEGER , GPRSStatus TEXT );"); //11-05-2021

			
			sldb.beginTransaction();
			String FACValue = "";
			StringBuilder sb = new StringBuilder();
			ipStream = new ByteArrayInputStream(strDecrypt.getBytes());
			ipStrReader = new InputStreamReader(ipStream);
			buffReader = new BufferedReader(ipStrReader);
			Total = 0;
			//DROP and RE-CREATE Tables in SBM_BillCollDataMain==============================================		
			while((ReceiveStr = buffReader.readLine()) != null)					
			{
				//ReceiveStr = scStrDec.useDelimiter("\r\n").next();
				sb.delete(0, sb.length());							
				sb.append(ReceiveStr);

				if(sb.toString().contains("^^"))
				{
					//rt = true;
				}
				else if(sb.toString().contains("@@"))
				{
					if(sb.toString().contains("|"))
					{										
						String[] arrSubD1 = sb.toString().split("\\|");						
						//Update Rows in StatusMaster====================================================================

						ContentValues valuesUpdateVersion = new ContentValues();
						valuesUpdateVersion.put("Value", arrSubD1[0].replace("@", ""));
						int upVer = sldb.update("StatusMaster", valuesUpdateVersion, " StatusID = ? ", new String[]{"2"});//Version

						ContentValues valuesUpdateSubDivision = new ContentValues();
						valuesUpdateSubDivision.put("Value", (arrSubD1[1].replace("$", "")).trim());
						int upSubDiv = sldb.update("StatusMaster", valuesUpdateSubDivision, " StatusID = ? ", new String[]{"15"});//SubDivision

						//int upVer = UpdatestatusMaster("2", arrSubD1[0].replace("@", ""));
						//bdBackup.UpdatestatusMaster("2", arrSubD1[0].replace("@", ""));//Insert into Backup Database
						//int upSubDiv = UpdatestatusMaster("15", (arrSubD1[1].replace("$", "")).trim());
						//bdBackup.UpdatestatusMaster("15", (arrSubD1[1].replace("$", "")).trim());//Insert into Backup Database
						//END Update Rows in StatusMaster================================================================
						//Modified 19-12-2015
						//Example @@442|DEVANAHALLI#0.04$ 
						try
						{
							if(sb.toString().contains("#"))
							{
								ContentValues valuesUpdateSubDivisionnew = new ContentValues();
								valuesUpdateSubDivisionnew.put("Value", (arrSubD1[1].split("#")[0].trim()));
								int upSubDivnew = sldb.update("StatusMaster", valuesUpdateSubDivisionnew, " StatusID = ? ", new String[]{"15"});//SubDivision

								FACValue = arrSubD1[1].split("#")[1].replace("$", "").trim();	
							}
						}
						catch(Exception e)
						{
							Log.d(TAG, e.toString());
						}

					}

				}
				else if(sb.toString().contains("%%"))
				{
					ReadFileParameters.SlabNTariff snt = (new ReadFileParameters()).new SlabNTariff();
					String[] sntSplit = sb.toString().split("~");
					String[] sntSubSplit = sntSplit[0].toString().split("#");
					//INSERT Rows in ReadSlabNTariff=================================================================
					ContentValues valuesSNT = new ContentValues();
					valuesSNT.put("TarifCode", sntSubSplit[1]);
					valuesSNT.put("TarifString", sb.toString().trim());
					long tariffCount= sldb.insert("ReadSlabNTariff", null, valuesSNT);//
					//bdBackup.InsertSlabNTariffBackUpDB(sntSubSplit[1], sb.toString().trim());//Insert into Backup Database
					if(tariffCount <= 0)
					{	
						throw new Exception("Problem in Inserting ReadSlabNTariff");
					}
					//END INSERT Rows in ReadSlabNTariff=============================================================
				}
				else//Connection Insert
				{
					//if(sb.toString().length() == 653)//String should contain 653 characters 31-12-2014
					if(sb.toString().length() == 697)//String should contain 685 characters 29-06-2021
					{

						Total++;
						/*QueryParameters qParam = StoredProcedure.GetConnectionCount(sb.substring(14, 24));
						crCheckCount = sldb.rawQuery(qParam.getSql(), qParam.getSelectionArgs());
						if(crCheckCount != null && crCheckCount.getCount() > 0)
						{
							crCheckCount.moveToFirst();
							Count = crCheckCount.getInt(crCheckCount.getColumnIndex("COUNT"));
							if(Count == 0)//Count If 
							{*/
						values.clear();
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

						//26-09-2015
						/*values.put("RRNo", sb.substring(196, 209));//31
						values.put("LegFol", sb.substring(209, 217));//32*/						
						values.put("RRNo", sb.substring(196, 217));//31  //13+8 = 21


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
						values.put("MobileNo", sb.substring(612, 642));//75

						//values.put("DLTEc_GoK", sb.substring(642,  sb.length()));//76 //31-12-2014
						//Start 23-03-2017
						values.put("DLTEc_GoK", sb.substring(642,  653));//77 //31-12-2014
						values.put("Gps_Latitude_image", sb.substring(653,  663));//78 //31-12-2014	
						values.put("Gps_Longitude_image", sb.substring(663,  673));//29-09-2016 //80
						values.put("DlCount", sb.substring(673, 675));//29-09-2016 //81
						values.put("MobileNo", sb.substring(675,  685));//29-09-2016 //82 //10 digits
						//End 23-03-2017
						values.put("FACValue", FACValue.trim().toString());//79 //19-12-2015
						
						try
						{
							if(sb.substring(685,  697).trim().contains(" "))
							{
								values.put("FC_Slab_3", "0.00"); 
							}
							else
							{
								values.put("FC_Slab_3", sb.substring(685,  697).trim().length()==0 ? "0.00" : sb.substring(685,  697).trim()); //28-06-2021

							}
						}
						catch(Exception e)
						{
							values.put("FC_Slab_3", "0.00"); //28-06-2021
						}

						rtValue = sldb.insert("SBM_BillCollDataMain", null, values);//Insert into Main DB
						//bdBackup.InsertBillCollDataMainBackUpDB(sb.toString());//Insert into Backup Database
						if(rtValue <= 0)
						{	
							throw new Exception("Problem in Inserting SBM_BillCollDataMain");
						}
						if(rtValue <= 0)
						{
							mainHan.post(new Runnable() {								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//getWritableDatabase().endTransaction();
									CustomToast.makeText(mcntx, "Problem in Importing Data from file to DataBase." , Toast.LENGTH_SHORT);
									//throw new Exception("Insertion failed for SBM_BillCollDataMain");
									try {
										throw new Exception();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});

						}
						mainHan.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								pb.setProgress((int)(Total));												
								lblStatus.setText("Records Inserted..." + Total+" out of "+contentLength);//((totalLength*100)/contentLength)+"%");
								//pb.setProgress(i);
								//lblStatus.setText("Records Inserted :"+i+" out of "+Total);
							}
						});
						//}//END Count If 
						//}
					} 
				}//END Connection Insert
			}//END While Loop /**/

			ContentValues valuesUpdateConnBilled = new ContentValues();
			valuesUpdateConnBilled.putNull("Status");
			sldb.update("StatusMaster", valuesUpdateConnBilled, " StatusID in (?)", new String[]{"8"});

			ContentValues valuesUpdateFileDownload = new ContentValues();
			valuesUpdateFileDownload.putNull("Status");
			sldb.update("StatusMaster", valuesUpdateFileDownload, " StatusID in (?)", new String[]{"9"});

			ContentValues valuesUpdateGPRS = new ContentValues();
			valuesUpdateGPRS.putNull("Status");
			sldb.update("StatusMaster", valuesUpdateGPRS, " StatusID in (?)", new String[]{"10"});

			ContentValues valuesUpdateFileInserted = new ContentValues();
			valuesUpdateFileInserted.put("Status", "1");
			valuesUpdateFileInserted.put("Value", filename); //31-12-2016
			sldb.update("StatusMaster", valuesUpdateFileInserted, " StatusID in (?)", new String[]{"7"});

			//UpdateStatusMasterDetailsByID("8", null, "");
			//UpdateStatusMasterDetailsByID("9", null, "");
			//UpdateStatusMasterDetailsByID("10", null, "");
			//UpdateStatusMasterDetailsByID("7", "1", "");//Update new file Loaded in StatusMaster Status = 1 where StatusID = 7

			sldb.setTransactionSuccessful();
			return 1;
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
			//sldb.endTransaction();
			return 0;
		}
		finally
		{
			if(buffReader != null)
			{
				try {
					buffReader.close();									
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(ipStream != null)
			{
				try {
					ipStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			sldb.endTransaction();
		}
		//return rtValue;
	}
	//Added 30-08-2014
	public DDLAdapter getMeterType() throws Exception
	{
		DDLAdapter lList=null;
		cr=null ;
		try {
			QueryParameters qParam=StoredProcedure.getMeterType();		
			cr=getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());
			for(int i=1; i<=cr.getCount();++i)
			{
				if(lList==null)
				{
					lList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());
				}
				if(i==1)
				{
					cr.moveToFirst();
					lList.AddItem("-1", "--SELECT--");
				}
				else
				{
					cr.moveToNext();
				}

				lList.AddItem(String.valueOf(cr.getInt(cr.getColumnIndex(COL_METERTYPEMASTER_METERTYPEID))), cr.getString(cr.getColumnIndex(COL_METERTYPEMASTER_METERTYPE)));

			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return lList;
	}
	//Added 30-08-2014
	public DDLAdapter getMeterPlacement() throws Exception
	{
		DDLAdapter lList=null;
		cr=null ;
		try {
			QueryParameters qParam=StoredProcedure.getMeterPlacement();		
			cr=getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());
			for(int i=1; i<=cr.getCount();++i)
			{
				if(lList==null)
				{
					lList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());
				}
				if(i==1)
				{
					cr.moveToFirst();
					lList.AddItem("-1", "--SELECT--");
				}
				else
				{
					cr.moveToNext();
				}

				lList.AddItem(String.valueOf(cr.getInt(cr.getColumnIndex(COL_METERPLACEMENTMASTER_UNIQUEID))), cr.getString(cr.getColumnIndex(COL_METERPLACEMENTMASTER_METERPLACEMENT)));

			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return lList;
	}
	//Added 30-08-2014
	public DDLAdapter getMeterCondition() throws Exception
	{
		DDLAdapter lList=null;
		cr=null ;
		try {
			QueryParameters qParam=StoredProcedure.getMeterCondition();		
			cr=getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());
			for(int i=1; i<=cr.getCount();++i)
			{
				if(lList==null)
				{
					lList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());
				}
				if(i==1)
				{
					cr.moveToFirst();
					lList.AddItem("-1", "--SELECT--");
				}
				else
				{
					cr.moveToNext();
				}

				lList.AddItem(String.valueOf(cr.getInt(cr.getColumnIndex(COL_METERCONDITION_UNIQUEID))), cr.getString(cr.getColumnIndex(COL_METERCONDITION_METERCONDITION)));

			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return lList;
	}
	//Added 30-08-2014
	public DDLAdapter getBatteryStatus() throws Exception
	{
		DDLAdapter lList=null;
		cr=null ;
		try {
			QueryParameters qParam=StoredProcedure.getBatteryStatus();		
			cr=getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());
			for(int i=1; i<=cr.getCount();++i)
			{
				if(lList==null)
				{
					lList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());
				}
				if(i==1)
				{
					cr.moveToFirst();
					lList.AddItem("-1", "--SELECT--");
				}
				else
				{
					cr.moveToNext();
				}

				lList.AddItem(String.valueOf(cr.getInt(cr.getColumnIndex(COL_BATTERYSTATUS_STATUSID))), cr.getString(cr.getColumnIndex(COL_BATTERYSTATUS_STATUS)));

			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return lList;
	}
	//Nitish 02-10-2014
	public ArrayList<ReportTPFeeder> GetReportTPFeeder(String currentDate) throws Exception
	{
		ArrayList<ReportTPFeeder> mList = new ArrayList<ReportTPFeeder>();
		Cursor crTPFeeder=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetReportTPFeeder(currentDate.trim());
			//QueryParameters qParam = StoredProcedure.GetReportTPFeeder("300914");

			crTPFeeder = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(crTPFeeder!=null)
			{
				for(int i=1; i<=crTPFeeder.getCount();++i)
				{

					if(mList==null)
					{
						mList=new ArrayList<ReportTPFeeder>();
					}
					if(i==1)				
						crTPFeeder.moveToFirst();				
					else
						crTPFeeder.moveToNext();

					ReportTPFeeder TPDetails = new ReportTPFeeder();								
					TPDetails.setmConnId(crTPFeeder.getString(crTPFeeder.getColumnIndex(COL_ConnectionNo)));
					TPDetails.setmReading(crTPFeeder.getString(crTPFeeder.getColumnIndex(COL_PreRead)));
					TPDetails.setmReason(crTPFeeder.getString(crTPFeeder.getColumnIndex(COL_PreStatus)));	

					mList.add(TPDetails);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(crTPFeeder!=null)
			{
				crTPFeeder.close();
			}
		}
		return mList;
	}
	//Nitish 30-08-2014
	public DDLAdapter getLatePaymentReason() throws Exception
	{
		DDLAdapter lList=null;
		cr=null ;
		try {


			if(lList==null)
			{
				lList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());
			}

			lList.AddItem("1", "Already Paid");		
			lList.AddItem("2", "DisConnected");
			lList.AddItem("3", "Door Locked");
			lList.AddItem("4", "Payment By Next Visit");			
			lList.AddItem("5", "Visited Not Paid");

		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return lList;
	}

	//24-03-2015
	public int Extend_CC_Close(String batchno,String cashlimit,String chequeLimit,String endtime)
	{
		int UpStatus = 0;
		String currentdatetime = CommonFunction.GetCurrentDateTime(); 	
		try
		{
			ContentValues valuesUpdateCC = new ContentValues();
			valuesUpdateCC.put("CashLimit", cashlimit);
			valuesUpdateCC.put("CDLimit", chequeLimit);
			valuesUpdateCC.put("EndTime", endtime);
			valuesUpdateCC.put("ExtensionFlag", "1");
			valuesUpdateCC.put("ExtensionDateTime", currentdatetime);
			UpStatus = getWritableDatabase().update("CashCounter_Details", valuesUpdateCC, " Batch_No = ? ", new String[]{batchno});

		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return UpStatus;
	}	

	///////////////////Face Recognition 30-06-2015////////////////////////////////
	public int insertFaceRecognition(String imeino,String simno,String similarity,String ImageName) 
	{		
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();
		String currentdate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
		try
		{	
			sldb.beginTransaction();
			ContentValues valuesInserttbl = new ContentValues();
			valuesInserttbl.put("ImeiNumber", imeino);
			valuesInserttbl.put("SimNumber", simno);
			valuesInserttbl.put("Similarity", similarity);				
			valuesInserttbl.put("CreatedDate", currentdate);
			valuesInserttbl.put("PhotoName", ImageName);

			long insertResult = sldb.insert("TBL_FaceRecognition", null, valuesInserttbl);
			if(insertResult <= 0)
			{	
				sldb.endTransaction();
				throw new Exception("Insertion failed for TBL_FaceRecognition");
			}				
			sldb.setTransactionSuccessful();
			return 1;
		}
		catch(Exception e)
		{
			Log.d("", e.toString());
			return 0;
		}	
		finally
		{
			sldb.endTransaction();
		}	
	}		
	//Nitish 29-01-2015	//Get ArrayList of Data to be sent to Server
	public ArrayList<String> GetDataSendToServerFaceRecognition()
	{
		ArrayList<String> alStr = new ArrayList<String>();
		Cursor crWriteFac = null;
		StringBuilder sb = new StringBuilder();
		try
		{
			QueryParameters qParam = StoredProcedure.GetDataSendToServerFaceRecognition();
			crWriteFac = getReadableDatabase().rawQuery(qParam.getSql(), null);		
			if(crWriteFac != null && crWriteFac.getCount() > 0)
			{
				crWriteFac.moveToFirst();
				do 
				{
					try
					{
						sb.delete(0, sb.length());
						sb.append(crWriteFac.getString(crWriteFac.getColumnIndex(COL_Id_TABLE_FaceRecognition)));
						sb.append("##");							
						sb.append(crWriteFac.getString(crWriteFac.getColumnIndex(COL_CreatedDate_TABLE_FaceRecognition)));	
						sb.append("##");
						sb.append(crWriteFac.getString(crWriteFac.getColumnIndex(COL_PhotoName_TABLE_FaceRecognition)));	
						alStr.add(sb.toString());
					}
					catch(Exception e)
					{
						Log.d(TAG, e.toString());
					}
				}while(crWriteFac.moveToNext());
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return alStr;
	}		
	//Nitish 29-01-2015
	public int UpdateGPRSFlagFaceRecognition(String id)
	{
		int upStatus = 0;
		try
		{
			ContentValues valuesUpdateGPStbl = new ContentValues();
			valuesUpdateGPStbl.put("GprsFlag", 1);				
			upStatus = getWritableDatabase().update("TBL_FaceRecognition", valuesUpdateGPStbl, "trim(Id) = ?", new String[]{id.trim()});
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return upStatus;
	}

	/////////////////////////////End face Recognition//////////////////////////////////////////////
	//25-07-2015
	public boolean CheckIfAllConnectionBilled()
	{
		Cursor crValue = null;
		int total=0,billtotal = 0;
		boolean billed = false;
		try
		{		

			QueryParameters qParamBilled = StoredProcedure.GetCountBillCollDataMainWithoutTC();
			crValue = getReadableDatabase().rawQuery(qParamBilled.getSql(), null);
			if(crValue != null && crValue.getCount() > 0)
			{
				crValue.moveToFirst();
				total = crValue.getInt(crValue.getColumnIndex("COUNT"));
			}
			crValue = null;
			QueryParameters qParamGPRSSent = StoredProcedure.GetCountforBilledBOBConnection();
			crValue = getReadableDatabase().rawQuery(qParamGPRSSent.getSql(), null);
			if(crValue != null && crValue.getCount() > 0)
			{
				crValue.moveToFirst();
				billtotal = crValue.getInt(crValue.getColumnIndex("COUNT"));
			}
			if(billtotal == total)
			{
				billed = true;
			}
			else
			{
				billed = false;
			}	

		}
		catch (Exception e) {
			// TODO: handle exception

		}
		finally
		{
			if(crValue!=null)
			{
				crValue.close();
			}
		}		
		return billed;
	}
	//29-07-2015
	public DDLAdapter getMeterTypeHESCOM() throws Exception
	{
		DDLAdapter lList=null;
		cr=null ;
		try {


			if(lList==null)
			{
				lList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());
			}

			lList.AddItem("0", "--SELECT--");		
			lList.AddItem("1", "ELECTRO MECHANICAL METER");		
			lList.AddItem("2", "HIGH PRECESSION METER");
			lList.AddItem("3", "STATIC/ELECTRONIC/ETV  METER");
			lList.AddItem("4", "DC/NO  METER");


		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return lList;
	}

	public DDLAdapter getMeterFixedHESCOM() throws Exception
	{
		DDLAdapter lList=null;
		cr=null ;
		try {


			if(lList==null)
			{
				lList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());
			}

			lList.AddItem("0", "--SELECT--");		
			lList.AddItem("1", "SINGLE PHASE METER");		
			lList.AddItem("2", "THREE PHASE METER");
			lList.AddItem("3", "DC/NO METER");


		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return lList;
	}
	public DDLAdapter getInstallationDetailsHescom() throws Exception
	{
		DDLAdapter lList=null;
		cr=null ;
		try {


			if(lList==null)
			{
				lList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());
			}

			lList.AddItem("0", "--SELECT--");		
			lList.AddItem("1", "SINGLE PHASE INSTALLATION");		
			lList.AddItem("2", "THREE PHASE INSTALLATION");


		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return lList;
	}

	public DDLAdapter getInstallationStatusHescom() throws Exception
	{
		DDLAdapter lList=null;
		cr=null ;
		try {


			if(lList==null)
			{
				lList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());
			}

			lList.AddItem("0", "--SELECT--");		
			lList.AddItem("1", "Accessible");		
			lList.AddItem("2", "Inaccessible");


		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return lList;
	}
	//06-08-2015
	public DDLAdapter getMeterMakeHESCOM() throws Exception
	{
		DDLAdapter lList=null;
		cr=null ;
		try {


			if(lList==null)
			{
				lList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());
			}
			lList.AddItem("0", "--SELECT--");
			lList.AddItem("1",	"DC/NOMETER");
			lList.AddItem("2",	"L & T");
			lList.AddItem("3",	"SECURE");
			lList.AddItem("4",	"BHEL");
			lList.AddItem("5",	"ACTARIS");
			lList.AddItem("7",	"AVON");
			lList.AddItem("8",	"L & G");
			lList.AddItem("9",	"ISKRA");
			lList.AddItem("10",	"I M");
			lList.AddItem("11",	"SIEMENS");
			lList.AddItem("12",	"R.C");
			lList.AddItem("13",	"T.T.L");
			lList.AddItem("14",	"PRECESITION");
			lList.AddItem("15",	"LANDIS");
			lList.AddItem("19",	"ELYMER");
			lList.AddItem("17",	"CAPITAL");
			lList.AddItem("18",	"HAVELLS");
			lList.AddItem("21",	"OMANI");
			lList.AddItem("22",	"ACCURATE");
			lList.AddItem("24",	"BHEK:()");
			lList.AddItem("26",	"OLAY");
			lList.AddItem("27",	"DATAK");
			lList.AddItem("28",	"Alstom");
			lList.AddItem("29",	"EMCO");
			lList.AddItem("31",	"HIL");
			lList.AddItem("32",	"INDOTECK");
			lList.AddItem("33",	"INDOTECH");
			lList.AddItem("34",	"GENUS");
			lList.AddItem("35",	"HTL");
			lList.AddItem("36",	"ZCE");
			lList.AddItem("37",	"RAMCO");


		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return lList;
	}
	//29-03-2016
	public DDLAdapter getMeterPhase() throws Exception
	{
		DDLAdapter lList=null;
		cr=null ;
		try {
			/*QueryParameters qParam=StoredProcedure.getMeterCondition();		
						cr=getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());
						for(int i=1; i<=cr.getCount();++i)
						{
							if(lList==null)
							{
								lList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());
							}
							if(i==1)
							{
								cr.moveToFirst();
								lList.AddItem("-1", "--SELECT--");
							}
							else
							{
								cr.moveToNext();
							}

							lList.AddItem(String.valueOf(cr.getInt(cr.getColumnIndex(COL_METERCONDITION_UNIQUEID))), cr.getString(cr.getColumnIndex(COL_METERCONDITION_METERCONDITION)));

						}*/

			if(lList==null)
			{
				lList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());
			}

			lList.AddItem("-1", "--SELECT--");
			lList.AddItem("1", "Single");
			lList.AddItem("3", "Three");



		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return lList;
	}
	//Modified 04-10-2017
	public DDLAdapter getMainMeterType(int prevSel) throws Exception
	{
		DDLAdapter lList=null;
		cr=null ;
		try {			

			if(lList==null)
			{
				lList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());
			}	
			if(prevSel==1)
			{
				lList.AddItem("1", "High Precision");
				lList.AddItem("2", "Electro Static");
				lList.AddItem("3", "Electro Mechanical");

			}
			else if(prevSel==2)
			{				
				lList.AddItem("2", "Electro Static");
				lList.AddItem("3", "Electro Mechanical");
				lList.AddItem("1", "High Precision");

			}
			else if(prevSel==3)
			{				
				lList.AddItem("3", "Electro Mechanical");
				lList.AddItem("1", "High Precision");
				lList.AddItem("2", "Electro Static");

			}
			else
			{
				lList.AddItem("-1", "--SELECT--"); //Added 29-08-2017
				lList.AddItem("3", "Electro Mechanical");
				lList.AddItem("2", "Electro Static");
				lList.AddItem("1", "High Precision");
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return lList;
	}

	////////////////////////////////Batch Generation///////////////////////////////
	//15-07-2016 Get Billing Details
	public CashCounterObject GetBillingDetails() 
	{
		CashCounterObject cc = new CashCounterObject();	
		Cursor crCC=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetBillingDetails();
			crCC = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(crCC!=null)
			{
				if(crCC.getCount() > 0)//Cursor IF Start
				{							
					crCC.moveToFirst();			
					cc.setmIMEINo(crCC.getString(crCC.getColumnIndex(COL_IMEINo_Billing_Details)));		
					cc.setmSIMNo(crCC.getString(crCC.getColumnIndex(COL_SIMNo_Billing_Details)));		
					cc.setmBatch_Date(crCC.getString(crCC.getColumnIndex(COL_BatchDate_Billing_Details)));										
					cc.setmStartTime(crCC.getString(crCC.getColumnIndex(COL_StartTime_Billing_Details)));
					cc.setmEndTime(crCC.getString(crCC.getColumnIndex(COL_EndTime_Billing_Details)));
					cc.setmLocationCode(crCC.getString(crCC.getColumnIndex(COL_LocationCode_Billing_Details)));
					cc.setmExtensionFlag(crCC.getString(crCC.getColumnIndex("ExtFlag")));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception

		}
		finally
		{
			if(crCC!=null)
			{
				crCC.close();
			}
		}
		return cc;
	}

	//Nitish 15-07-2016 //Billing_Details Save 
	public int BillingDetailsSave(String imeino, String simno,String str[] )
	{		
		int rtvalue = 0;
		DatabaseHelper dh1 = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb1 = dh1.getWritableDatabase();	

		try
		{		

			sldb1.beginTransaction();
			ContentValues valuesInsertBillingDetails = new ContentValues();				

			//15072016|1000|2100|041815322016
			//sp[0] = 18042014 --BatchDate			
			//sp[1] = 1000 --Start Time
			//sp[2] = 2100 --End Time			
			//sp[3] = 041815322014 -datetime

			valuesInsertBillingDetails.put("IMEINo", imeino);
			valuesInsertBillingDetails.put("SIMNo", simno);	
			valuesInsertBillingDetails.put("BatchDate", str[0]);				
			valuesInsertBillingDetails.put("StartTime", str[1]);
			valuesInsertBillingDetails.put("EndTime ", str[2]);			
			valuesInsertBillingDetails.put("DateTime", str[3]);			
			valuesInsertBillingDetails.put("LocationCode",str[4]);			
			valuesInsertBillingDetails.put("BillingOpen", "1");				

			long insertResult = sldb1.insert("Billing_Details", null, valuesInsertBillingDetails);
			if(insertResult <= 0)
			{	
				sldb1.endTransaction();
				throw new Exception("Insertion failed for Billing_Details Table");
			}				
			sldb1.setTransactionSuccessful();
			rtvalue = 1;

		}
		catch(Exception e)
		{
			Log.d("", e.toString());
			rtvalue = 0;			
		}	
		finally
		{
			sldb1.endTransaction();
		}
		return rtvalue;		
	}	
	//Nitish 15-07-2016 //Set BillingOpen=0 for that BatchDate in Billing_Details table 
	public void CloseBillingDetailsFlag(String BatchDate)
	{		
		String currentdatetime = CommonFunction.GetCurrentDateTime(); 	
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();
		try
		{
			BatchDate = BatchDate.trim();
			ContentValues valuesUpdateColltbl = new ContentValues();
			valuesUpdateColltbl.put("BillingOpen", "0");
			valuesUpdateColltbl.put("BillingCloseDateTime", currentdatetime);
			//sldb.update("Collection_TABLE", valuesUpdateColltbl, "ConnectionNo = ?", new String[]{ConnectionNo});
			sldb.update("Billing_Details", valuesUpdateColltbl, "BatchDate = ? ", new String[]{BatchDate});

		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}
	//Nitish 29-07-2014 
	public void DropCreateBillingDetails()
	{
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();

		sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_Billing_Details);		

		sldb.execSQL("CREATE TABLE Billing_Details(UID INTEGER PRIMARY KEY,IMEINo TEXT,SIMNo TEXT,BatchDate TEXT," +
				"StartTime TEXT,EndTime TEXT,Batch_No TEXT,DateTime Text,LocationCode TEXT,BillingOpen Text,BillingCloseDateTime TEXT , ExtensionFlag TEXT , ExtensionDateTime TEXT);"); //15-07-2016
	}

	//////////////////////////////////////End Batch Generation///////////////////////////

	//15-07-2016
	public void CreateIfNotExistsConfigurationTable()
	{
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();

		sldb.execSQL("CREATE TABLE IF NOT EXISTS TABLE_CONFIGURATION(UID INTEGER PRIMARY KEY,IMEINO TEXT,SIMNO TEXT,COMPANY TEXT,LOCATIONCODE TEXT,SUBDIVISION TEXT,STAFFNAME TEXT, " +
				" CAMERAFLAG TEXT,PRINTERFLAG TEXT,CONFIGFLAG TEXT,VERSIONNO TEXT,ENABLEDISABLE TEXT,CREATEDDATE TEXT);");

		sldb.execSQL("CREATE TABLE IF NOT EXISTS Billing_Details(UID INTEGER PRIMARY KEY,IMEINo TEXT,SIMNo TEXT,BatchDate TEXT," +
				"StartTime TEXT,EndTime TEXT,Batch_No TEXT,DateTime Text,LocationCode TEXT,BillingOpen Text,BillingCloseDateTime TEXT , ExtensionFlag TEXT , ExtensionDateTime TEXT);"); //15-07-2016

		//15-07-2016
		sldb.execSQL("CREATE TABLE IF NOT EXISTS TABLE_EventLog(UID INTEGER PRIMARY KEY,IMEINO TEXT,SIMNO TEXT,Flag TEXT,Description TEXT,DateTime TEXT , GPRSFlag INTEGER, GPRS TEXT);");

		sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_LogMaster);

		sldb.execSQL("CREATE TABLE IF NOT EXISTS LogMaster(UID INTEGER PRIMARY KEY,ID TEXT,Event_Timestamp TEXT);");

		try
		{
			LogMaster();
		}
		catch(Exception e)
		{

		}

	}

	///////////////////////////////////////////////////Event Log Start///////////////////////////////////////////////////////
	//Nitish 15-07-2016 //Event_Log Save 
	public int EventLogSave(String Flag,String imeino, String simno,String msg)
	{		
		int rtvalue = 0;
		DatabaseHelper dh1 = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb1 = dh1.getWritableDatabase();
		String loc = dh1.GetLocationCode();
		String currentdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		try
		{	
			sldb1.beginTransaction();
			ContentValues valuesInsertEventLog = new ContentValues();
			valuesInsertEventLog.put("IMEINo", imeino);
			valuesInsertEventLog.put("SIMNo", simno);	
			valuesInsertEventLog.put("Flag", Flag);				
			valuesInsertEventLog.put("Description", msg);	//23-02-2018			
			valuesInsertEventLog.put("DateTime", currentdate);			
			valuesInsertEventLog.put("GPRSFlag", "0");
			long insertResult = sldb1.insert("TABLE_EventLog", null, valuesInsertEventLog);
			if(insertResult <= 0)
			{	
				sldb1.endTransaction();
				throw new Exception("Insertion failed for TABLE_EventLog Table");
			}				
			sldb1.setTransactionSuccessful();				
			rtvalue = 1;
		}
		catch(Exception e)
		{
			Log.d("", e.toString());
			rtvalue = 0;			
		}	
		finally
		{
			sldb1.endTransaction();
		}
		return rtvalue;		
	}
	//15-07-2016
	public int LogMaster()
	{		
		int rtvalue = 0;
		DatabaseHelper dh1 = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb1 = dh1.getWritableDatabase();
		ArrayList<String> lststr=new ArrayList<String>();
		//String loc = dh1.GetLocationCode();
		String currentdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		lststr.add("1#Login"); 
		lststr.add("2#Bluetooth"); 
		lststr.add("3#Internet Availability");  
		lststr.add("4#Manual Bill Printing Completed");
		lststr.add("5#Optical Bill Printing Completed");
		lststr.add("6#Bill Reprint Completed");
		lststr.add("7#Cash Receipt Printing Completed");	
		lststr.add("8#Cheque Receipt Printing  Completed");
		lststr.add("9#Receipt Reprint Completed");
		lststr.add("10#USB Data downloaded");//Manual
		lststr.add("11#Online Data downloaded");// Online
		lststr.add("12#Database backup Completed");
		lststr.add("13#File Write Completed");
		lststr.add("14#Mobile battey Percentage");
		lststr.add("15#Error Log");


		try                             

		{
			sldb1.beginTransaction();

			if(lststr.size()>0)                  
			{
				try {
					for (String stringdata : lststr) 
					{
						ContentValues valuesInsertLogmaster = new ContentValues();
						String[] str=	stringdata.toString().split("#");
						valuesInsertLogmaster.put("ID", str[0].trim());
						valuesInsertLogmaster.put("Event_Timestamp", str[1].trim());						


						long insertResult = sldb1.insert("LogMaster", null, valuesInsertLogmaster);					
						if(insertResult <= 0)
						{	
							sldb1.endTransaction();
							throw new Exception("Insertion failed for LogMaster");
						}
					}
				}
				catch (Exception e)
				{
					Log.d("", e.toString());
				}
				sldb1.setTransactionSuccessful();
				rtvalue = 1;
			}			
		}
		catch(Exception e)
		{			
			Log.d("", e.toString());
			rtvalue = 0;		

		}	
		finally         
		{
			sldb1.endTransaction();                                     
		}	

		return rtvalue;		
	}	
	public ArrayList<String> getEventLogstatus()
	{
		ArrayList<String> eventlogstatus =new ArrayList<String>();
		ReadEventLog  rdevent=new ReadEventLog();
		Cursor csrpaysts;
		StringBuilder sb =new StringBuilder();
		try 
		{
			QueryParameters qParam = StoredProcedure.getEventlogStatus();
			csrpaysts = getReadableDatabase().rawQuery(qParam.getSql(), null);
			if(csrpaysts !=null && csrpaysts.getCount() >0)
			{
				csrpaysts.moveToFirst();
				do 
				{
					rdevent.setUID(csrpaysts.getString(csrpaysts.getColumnIndex(COL_UID_TABLE_EventLog)));
					rdevent.setIMEINO(csrpaysts.getString(csrpaysts.getColumnIndex(COL_IMEINO_TABLE_EventLog)));
					rdevent.setSIMNO(csrpaysts.getString(csrpaysts.getColumnIndex(COL_SIMNO_TABLE_EventLog)));
					rdevent.setDescription(csrpaysts.getString(csrpaysts.getColumnIndex(COL_Description_TABLE_EventLog)));
					rdevent.setFlag(csrpaysts.getInt(csrpaysts.getColumnIndex(COL_Flag_TABLE_EventLog)));
					rdevent.setDateTime(csrpaysts.getString(csrpaysts.getColumnIndex(COL_DateTime_TABLE_EventLog)));
					rdevent.setGPRSFlag(csrpaysts.getInt(csrpaysts.getColumnIndex(COL_GPRSFlag_TABLE_EventLog)));
					rdevent.setGPRSStatus(csrpaysts.getString(csrpaysts.getColumnIndex(COL_GPRSStatus_TABLE_EventLog)));

					sb.delete(0, sb.length());
					sb.append(rdevent.getUID()+"##");
					sb.append(rdevent.getIMEINO()+"##");											
					sb.append(rdevent.getSIMNO()+"##");	
					sb.append(rdevent.getDescription()+"##");	
					sb.append(String.valueOf(rdevent.getFlag())+"##");	
					sb.append(rdevent.getDateTime()+"##");	
					sb.append(String.valueOf(rdevent.getGPRSFlag())+"##");	
					sb.append(rdevent.getGPRSStatus());	
					eventlogstatus.add(sb.toString());	
				} 
				while (csrpaysts.moveToNext());	
			}
		}
		catch (Exception e)
		{

			Log.d("", e.toString());
		}
		return eventlogstatus;
	}
	public int UpdateEventLogStatus(String uid)
	{
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();
		int upStatus = 0;
		try
		{
			ContentValues valuesUpdateColltbl = new ContentValues();
			valuesUpdateColltbl.put("GPRSFlag", "1");
			//sldb.update("Collection_TABLE", valuesUpdateColltbl, "ConnectionNo = ?", new String[]{ConnectionNo});
			upStatus = sldb.update("TABLE_EventLog", valuesUpdateColltbl, " UID = ? ", new String[]{uid});

		}
		catch(Exception e)
		{

			Log.d(TAG, e.toString());
		}
		return upStatus;
	}
	////////////////////////////////////////Event Log End///////////////////////////////////////////////
	public ArrayList<ReportTariff> GetReportTarriff(String readingday) throws Exception
	{
		ArrayList<ReportTariff> mList = new ArrayList<ReportTariff>();
		Cursor crTPFeeder=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.getTariffreport(readingday);
			//QueryParameters qParam = StoredProcedure.GetReportTPFeeder("300914");

			crTPFeeder = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(crTPFeeder!=null)
			{
				for(int i=1; i<=crTPFeeder.getCount();++i)
				{

					if(mList==null)
					{
						mList=new ArrayList<ReportTariff>();
					}
					if(i==1)
						crTPFeeder.moveToFirst();				
					else
						crTPFeeder.moveToNext();

					ReportTariff TPDetails = new ReportTariff();	
					TPDetails.setConnectionNo(crTPFeeder.getString(crTPFeeder.getColumnIndex("NoOfConsumers")));

					TPDetails.setTariffCode(crTPFeeder.getString(crTPFeeder.getColumnIndex(COL_TariffCode)));
					TPDetails.setUnits(crTPFeeder.getString(crTPFeeder.getColumnIndex("Units")));
					//TPDetails.setBillTotal(crTPFeeder.getString(crTPFeeder.getColumnIndex(COL_BillTotal)));	
					TPDetails.setBillTotal(new BigDecimal(crTPFeeder.getDouble(crTPFeeder.getColumnIndex("Billtotal"))));

					mList.add(TPDetails);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(crTPFeeder!=null)
			{
				crTPFeeder.close();
			}
		}
		return mList;
	}
	public DDLAdapter GetTariffDaywise() throws Exception
	{
		DDLAdapter lList=null;
		cr=null ;
		try {
			QueryParameters qParam=StoredProcedure.getTariffDaywise();		
			cr=getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());
			for(int i=1; i<=cr.getCount();++i)
			{
				if(lList==null)
				{
					lList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());
				}
				if(i==1)
				{
					cr.moveToFirst();					
					//lList.AddItem("-1", "--SELECT--");
				}
				else
					cr.moveToNext();

				lList.AddItem(String.valueOf(cr.getString(cr.getColumnIndex(COL_ReadingDay))),String.valueOf(cr.getString(cr.getColumnIndex(COL_ReadingDay))));

			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return lList;
	}

	public String getTariffString(String Tarifcode)
	{
		Cursor crBat = null;
		String bat = "";
		try
		{
			QueryParameters qParam = StoredProcedure.getTariffString(Tarifcode);
			crBat = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				bat = crBat.getString(crBat.getColumnIndex("TarifString"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return bat;
	}
	//Nitish 30-12-2016
	public ArrayList<String> getEventLogNotSent()
	{
		ArrayList<String> eventlogstatus =new ArrayList<String>();
		ReadEventLog  rdevent=new ReadEventLog();
		Cursor csrpaysts;
		StringBuilder sb =new StringBuilder();
		try 
		{
			QueryParameters qParam = StoredProcedure.getEventLogNotSent();
			csrpaysts = getReadableDatabase().rawQuery(qParam.getSql(), null);
			if(csrpaysts !=null && csrpaysts.getCount() >0)
			{
				csrpaysts.moveToFirst();
				do 
				{
					rdevent.setUID(csrpaysts.getString(csrpaysts.getColumnIndex(COL_UID_TABLE_EventLog)));
					rdevent.setIMEINO(csrpaysts.getString(csrpaysts.getColumnIndex(COL_IMEINO_TABLE_EventLog)));
					rdevent.setSIMNO(csrpaysts.getString(csrpaysts.getColumnIndex(COL_SIMNO_TABLE_EventLog)));
					rdevent.setDescription(csrpaysts.getString(csrpaysts.getColumnIndex(COL_Description_TABLE_EventLog)));
					rdevent.setFlag(csrpaysts.getInt(csrpaysts.getColumnIndex(COL_Flag_TABLE_EventLog)));
					rdevent.setDateTime(csrpaysts.getString(csrpaysts.getColumnIndex(COL_DateTime_TABLE_EventLog)));
					rdevent.setGPRSFlag(csrpaysts.getInt(csrpaysts.getColumnIndex(COL_GPRSFlag_TABLE_EventLog)));
					rdevent.setGPRSStatus(csrpaysts.getString(csrpaysts.getColumnIndex(COL_GPRSStatus_TABLE_EventLog)));

					sb.delete(0, sb.length());
					sb.append(rdevent.getUID()+"##");
					sb.append(rdevent.getIMEINO()+"##");											
					sb.append(rdevent.getSIMNO()+"##");	
					sb.append(rdevent.getDescription()+"##");	
					sb.append(String.valueOf(rdevent.getFlag())+"##");	
					sb.append(rdevent.getDateTime()+"##");	
					sb.append(String.valueOf(rdevent.getGPRSFlag()));					
					sb.append("\r\n");	
					eventlogstatus.add(sb.toString());	
				} 
				while (csrpaysts.moveToNext());	


			}
		}
		catch (Exception e)
		{

			Log.d("", e.toString());
		}
		return eventlogstatus;
	}
	/////////////////////////////////Nitish Collection Reports Cash-Cheque Split 17-02-2017//////////////////////////////
	public int GetCashChequeCountforBatchColl(String batchno,String type)
	{
		Cursor crBat = null;
		int count = 0;
		try
		{
			QueryParameters qParam = StoredProcedure.GetCashChequeCountforBatchColl(batchno,type);
			crBat = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				count = crBat.getInt(crBat.getColumnIndex("COUNT"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return count;
	}	
	public String GetCashChequeCollectionAmount(String batchno,String type)
	{
		Cursor crBat = null;
		String bat = "";
		try
		{
			QueryParameters qParam = StoredProcedure.GetCashChequeCollectionAmount(batchno,type);
			crBat = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				bat = crBat.getString(crBat.getColumnIndex("CollectionAmt"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return bat;
	}	
	public int GetCashChequeCountforBatchColl(String type)
	{
		Cursor crBat = null;
		int count = 0;
		try
		{
			QueryParameters qParam = StoredProcedure.GetCashChequeCountforBatchColl(type);
			crBat = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				count = crBat.getInt(crBat.getColumnIndex("COUNT"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return count;
	}	
	public String GetCashChequeCollectionAmount(String type)
	{
		Cursor crBat = null;
		String bat = "";
		try
		{
			QueryParameters qParam = StoredProcedure.GetCashChequeCollectionAmount(type);
			crBat = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				bat = crBat.getString(crBat.getColumnIndex("CollectionAmt"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return bat;
	}
	/////////////////////////////////End Nitish Collection Reports Cash-Cheque Split 17-02-2017//////////////////////////////

	//Nitish 16-03-2017	//Get ArrayList of Cheque image Data
	public ArrayList<String> GetDataSendToServerChqImg()
	{
		ArrayList<String> alStr = new ArrayList<String>();
		Cursor crWriteFac = null;
		StringBuilder sb = new StringBuilder();
		try
		{
			QueryParameters qParam = StoredProcedure.GetDataSendToServerChqImg();
			crWriteFac = getReadableDatabase().rawQuery(qParam.getSql(), null);		
			if(crWriteFac != null && crWriteFac.getCount() > 0)
			{
				crWriteFac.moveToFirst();
				do 
				{
					try
					{
						sb.delete(0, sb.length());
						sb.append(crWriteFac.getString(crWriteFac.getColumnIndex(COL_UID_SBM_ChequeDDImage)));
						sb.append("##");							
						sb.append(crWriteFac.getString(crWriteFac.getColumnIndex(COL_CreatedDate_TABLE_SBM_ChequeDDImage)));	
						sb.append("##");
						sb.append(crWriteFac.getString(crWriteFac.getColumnIndex(COL_PhotoName_TABLE_SBM_ChequeDDImage)));	
						alStr.add(sb.toString());
					}
					catch(Exception e)
					{
						Log.d(TAG, e.toString());
					}
				}while(crWriteFac.moveToNext());
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return alStr;
	}

	//Nitish 16-03-2017
	public int UpdateGPRSFlagChqImg(String id)
	{
		int upStatus = 0;
		try
		{
			ContentValues valuesUpdateGPStbl = new ContentValues();
			valuesUpdateGPStbl.put("Gprs_Flag", 1);				
			upStatus = getWritableDatabase().update("SBM_ChequeDDImage", valuesUpdateGPStbl, "trim(UID) = ?", new String[]{id.trim()});
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return upStatus;
	}

	public StringBuilder GetCollDataLog()
	{
		Cursor crWriteColl = null;
		StringBuilder sb = new StringBuilder();
		StringBuilder sbCollection = new StringBuilder();
		try
		{
			QueryParameters qParam = StoredProcedure.GetCollDataWriteToFile();
			crWriteColl = getReadableDatabase().rawQuery(qParam.getSql(), null);		
			if(crWriteColl != null && crWriteColl.getCount() > 0)
			{

				crWriteColl.moveToFirst();
				do
				{
					WriteFileParameters wfp = new WriteFileParameters();			

					//27-04-2017
					String pActualReceiptNo = crWriteColl.getString(crWriteColl.getColumnIndex(COL_Receipt_No_COLLECTION_TABLE));
					pActualReceiptNo = CommonFunction.getActualReceiptNo(pActualReceiptNo);
					//End 27-04-2017
					wfp.setmReceipttypeflag_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_Receipttypeflag_COLLECTION_TABLE)));
					wfp.setmConsNo_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_ConnectionNo_COLLECTION_TABLE)));
					wfp.setmBatch_No_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_Batch_No_COLLECTION_TABLE)));
					wfp.setmReceipt_No_Coll(pActualReceiptNo);//27-04-2017
					//wfp.setmReceipt_No_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_Receipt_No_COLLECTION_TABLE)));
					wfp.setmGvpId_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_GvpId_COLLECTION_TABLE)));
					wfp.setmDateTime_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_DateTime_COLLECTION_TABLE)));
					wfp.setmPayment_Mode_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_Payment_Mode_COLLECTION_TABLE)));
					wfp.setmPaid_Amt_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_Paid_Amt_COLLECTION_TABLE)));
					wfp.setmChequeDDNo_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_ChequeDDNo_COLLECTION_TABLE)));
					wfp.setmChequeDDDate_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_ChequeDDDate_COLLECTION_TABLE)));;
					wfp.setmBankID_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_BankID_COLLECTION_TABLE)));
					wfp.setmSBMNumber_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_SBMNumber_COLLECTION_TABLE)));	
					wfp.setmLocation_code_Coll(crWriteColl.getString(crWriteColl.getColumnIndex(COL_LocationCode_COLLECTION_TABLE)));
					wfp.setmVer_Coll(ConstantClass.FileVersion);//version may change make it global	

					sb.delete(0, sb.length());
					sb.append(wfp.getmReceipttypeflag_Coll());											
					sb.append(wfp.getmConsNo_Coll());
					sb.append(wfp.getmBatch_No_Coll());
					sb.append(wfp.getmReceipt_No_Coll());
					sb.append(wfp.getmGvpId_Coll());
					sb.append(wfp.getmDateTime_Coll());
					sb.append(wfp.getmPayment_Mode_Coll());
					sb.append(wfp.getmPaid_Amt_Coll());
					sb.append(wfp.getmChequeDDNo_Coll());
					sb.append(wfp.getmChequeDDDate_Coll());
					sb.append(wfp.getmBankID_Coll());
					sb.append(wfp.getmSBMNumber_Coll());
					sb.append(wfp.getmLocation_code_Coll());
					sb.append(wfp.getmVer_Coll());

					sbCollection.append(sb.toString());
					sbCollection.append("\r\n");

				}while(crWriteColl.moveToNext());
			}
			else//No Record in Collection
			{

			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}		
		return sbCollection;
	}

	//31-12-2016
	public String GetStatusMasterValue(String id)
	{
		Cursor crCount = null;
		String value = "";
		try
		{
			QueryParameters qParam = StoredProcedure.GetStatusMasterDetailsByID(id);
			crCount = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());		
			if(crCount != null && crCount.getCount() > 0)
			{
				crCount.moveToFirst();
				//crCount.moveToLast();
				value = crCount.getString(crCount.getColumnIndex(COL_VALUE_STATUSMASTER));
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return value;
	}
	//12-06-2017
	public String UpdateCollectionReprintCount(String ReceiptNo,String reprintcount)
	{
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();
		String strValue = "";
		try
		{
			ReceiptNo =ReceiptNo.trim();
			ContentValues valuesUpdateColltbl = new ContentValues();
			valuesUpdateColltbl.put("ReprintCount", reprintcount);
			//sldb.update("Collection_TABLE", valuesUpdateColltbl, "ConnectionNo = ?", new String[]{ConnectionNo});
			int update = sldb.update("Collection_TABLE", valuesUpdateColltbl, "Receipt_No = ? ", new String[]{ReceiptNo});
			strValue = String.valueOf(update);
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return strValue;
	}
	public DDLAdapter GetHelpDeskComplaints() throws Exception
	{
		DDLAdapter conList = null;
		cr=null ;
		try
		{			

			if(conList==null)
			{
				conList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());
				conList.AddItem("-1", "--SELECT--");
			}
			conList.AddItem("1", "PRINTER NOT PAIRING");
			conList.AddItem("2", "NO CONNECTIVITY");
			conList.AddItem("3", "MCC CLOSED");	
			conList.AddItem("4", "OTP NOT RECEIVED");				
			conList.AddItem("5", "RECEIPT NOT PRINTING");
			conList.AddItem("6", "CONSUMER NOT FOUND");			
			conList.AddItem("7", "OTHERS");	

		} 
		catch (Exception e) {

			Log.d("", e.toString());     
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return conList;
	}
	//17-06-2017
	public String getLastReceiptDate(String batchno)
	{
		Cursor crCount = null;
		String value = "0";
		try
		{
			QueryParameters qParam = StoredProcedure.getLastReceiptDate(batchno);
			crCount = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());		
			if(crCount != null && crCount.getCount() > 0)
			{
				crCount.moveToFirst();
				//crCount.moveToLast();
				value = crCount.getString(crCount.getColumnIndex(COL_DateTime_COLLECTION_TABLE));
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return value;
	}

	//05-02-2018
	public int getConnCountForRpt(String connid,String type)
	{
		Cursor crBat = null;
		int count = 0;
		try
		{
			QueryParameters qParam = StoredProcedure.getConnCountForRpt(connid,type);
			crBat = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				count = crBat.getInt(crBat.getColumnIndex("COUNT"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return count;
	}
	//Nitish 29-01-2021
	public void deleteDuplicateCollection(String connid,String rpttype)
	{
		try
		{
			DatabaseHelper dh = new DatabaseHelper(mcntx);
			SQLiteDatabase sldb = dh.getWritableDatabase();

			sldb.execSQL("Delete From " + TABLE_COLLECTION_TABLE + "  WHERE "+COL_ConnectionNo_COLLECTION_TABLE+ " = '"+ connid+  "' and " + COL_Receipttypeflag_COLLECTION_TABLE + " =  '" + rpttype + "'"
					+ " and UID in ( Select MIN(UID) from Collection_TABLE where " + COL_ConnectionNo_COLLECTION_TABLE + " = '"+ connid+  "' and " + COL_Receipttypeflag_COLLECTION_TABLE + " =  '" + rpttype + "' )" 
					+ " and ConnectionNo in (Select ConnectionNo from Collection_TABLE where " + COL_ConnectionNo_COLLECTION_TABLE + " = '"+ connid+  "' and " + COL_Receipttypeflag_COLLECTION_TABLE + " =  '" + rpttype + "' " 
					+ " group by ConnectionNo having Count('"+connid+"') > 1 )" );	
			Log.d("a", "a");
		}
		catch(Exception e)
		{

		}

	}

	//Nitish 10-03-2021
	public int MeterDetails(LandTObject LT)
	{
		int mtvalue = 0;
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();
		ReadSlabNTarifSbmBillCollection sbc = BillingObject.GetBillingObject();
		String currentdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		try
		{
			sldb.beginTransaction();
			ContentValues valuesInsertCollectiontbl = new ContentValues();
			valuesInsertCollectiontbl.put("Meter_Id",  LT.getmManufacturer());
			valuesInsertCollectiontbl.put("Meter_Serialno",  LT.getmMeterSerialNo());
			valuesInsertCollectiontbl.put("Meter_Date",  LT.getmMeterDate());
			valuesInsertCollectiontbl.put("Meter_Time",  LT.getmMeterTime());
			valuesInsertCollectiontbl.put("Meter_Reading",  LT.getmTotalCummulativeEnergy());
			valuesInsertCollectiontbl.put("Md",  LT.getmTarifWiseMD());
			valuesInsertCollectiontbl.put("Pf",  LT.getmPF());
			valuesInsertCollectiontbl.put("ConnectionNo", sbc.getmConnectionNo());
			valuesInsertCollectiontbl.put("CreatedDate", currentdate);
			valuesInsertCollectiontbl.put("ForMonth", sbc.getmForMonth());
			valuesInsertCollectiontbl.put("MeterType", LT.getmMeterType()); //25-06-2016
			valuesInsertCollectiontbl.put("GPRS_Flag", "0"); //25-06-2016
			long insertResult = sldb.insert("Meter_Details", null, valuesInsertCollectiontbl);
			if(insertResult <= 0)
			{	
				sldb.endTransaction();
				throw new Exception("Insertion failed for Meter_Details_Table");
			}	
			sldb.setTransactionSuccessful();
			mtvalue = 1;
		} 
		catch (Exception e) 
		{
			Log.d("", e.toString());
			mtvalue = 1;
		}
		finally
		{
			sldb.endTransaction();
		}
		return  mtvalue;
	}
	//Nitish 10-03-2021
	public DDLAdapter getMType() throws Exception
	{
		DDLAdapter lList=null;
		cr=null ;
		try {


			if(lList==null)
			{
				lList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());
			}

			lList.AddItem("1", "L&T");	
			lList.AddItem("2", "L&G");	
			lList.AddItem("3", "Secure - 3Phase");		
			lList.AddItem("4", "Secure - Saral");		
			lList.AddItem("5", "Secure - ICredit");		
			lList.AddItem("6", "L&G-DLMS");				
			lList.AddItem("8", "HPL");
			lList.AddItem("9", "L&T-DLMS");	
			lList.AddItem("10", "GENUS-DLMS");	//10-05-2021


		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return lList;
	}
	//Nitish 10-03-2021
	public String getMType(int meter) 
	{
		switch(meter)
		{
		case 1:
			return "L&T";
		case 2:
			return "L&G";
		case 3:
			return "Secure - 3Phase";		
		case 4:
			return "Secure - Saral";
		case 5:
			return "Secure - I CREDIT";
		case 6:
			return "L&G-DLMS";	
		case 7:
			return "L&T-AllParam";	
		case 8:
			return "HPL-DLMS";	
		case 9:
			return "L&T-DLMS";
		case 10:
			return "GENUS-DLMS";

		default:
			return "NO METER SELECTED";
		}
	}
	//Nitish 12-03-2021
	public ArrayList<LandTObject> getMeterTypedata()
	{
		ArrayList<LandTObject> alLandT =new ArrayList<LandTObject>();

		Cursor csrpaysts;

		try 
		{
			QueryParameters qParam = StoredProcedure.getMeterTypedata();
			csrpaysts = getReadableDatabase().rawQuery(qParam.getSql(), null);
			if(csrpaysts !=null && csrpaysts.getCount() >0)
			{
				csrpaysts.moveToFirst();
				do 
				{
					LandTObject  ltObj = new LandTObject();




					ltObj.setmUID(csrpaysts.getInt(csrpaysts.getColumnIndex("UID")));
					ltObj.setmMeterSerialNo(csrpaysts.getString(csrpaysts.getColumnIndex("Meter_SerialNo")));
					ltObj.setmCummulativeEnergyKVAH(csrpaysts.getString(csrpaysts.getColumnIndex("Meter_Reading")));
					ltObj.setmTarifWiseMD(csrpaysts.getString(csrpaysts.getColumnIndex("Md")));
					ltObj.setmPF(csrpaysts.getString(csrpaysts.getColumnIndex("Pf")));
					ltObj.setmMeterType(csrpaysts.getString(csrpaysts.getColumnIndex("MeterType")));
					ltObj.setmConnectionNo(csrpaysts.getString(csrpaysts.getColumnIndex("ConnectionNo")));
					ltObj.setmForMonth(csrpaysts.getString(csrpaysts.getColumnIndex("ForMonth")));
					ltObj.setmCreatedDate(csrpaysts.getString(csrpaysts.getColumnIndex("CreatedDate")));

					alLandT.add(ltObj);
				} 
				while (csrpaysts.moveToNext());	
			}
		}
		catch (Exception e)
		{

			Log.d("", e.toString());
		}
		return alLandT;
	}
	public int UpdateMeter_Details(String uid)
	{
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();
		int upStatus = 0;
		try
		{
			ContentValues valuesUpdateColltbl = new ContentValues();
			valuesUpdateColltbl.put("Gprs_Flag", "1");
			//sldb.update("Collection_TABLE", valuesUpdateColltbl, "ConnectionNo = ?", new String[]{ConnectionNo});
			upStatus = sldb.update("Meter_Details", valuesUpdateColltbl, " UID = ? ", new String[]{uid});

		}
		catch(Exception e)
		{

			Log.d(TAG, e.toString());
		}
		return upStatus;
	}

	public int UpdateMeter_DetailsTest(String uid)
	{
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();
		int upStatus = 0;
		try
		{
			ContentValues valuesUpdateColltbl = new ContentValues();
			valuesUpdateColltbl.put("Gprs_Flag", "0");
			//sldb.update("Collection_TABLE", valuesUpdateColltbl, "ConnectionNo = ?", new String[]{ConnectionNo});
			upStatus = sldb.update("Meter_Details", valuesUpdateColltbl, " UID = ? ", new String[]{uid});

		}
		catch(Exception e)
		{

			Log.d(TAG, e.toString());
		}
		return upStatus;
	}
	//01-04-2021
	public DDLAdapter getProbeType() throws Exception
	{
		DDLAdapter lList=null;
		cr=null ;
		try {


			if(lList==null)
			{
				lList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());
			}

			lList.AddItem("1", "NEW PROBE");	
			lList.AddItem("2", "OLD PROBE");	



		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(cr!=null)
			{
				cr.close();
			}
		}
		return lList;
	}

	///22-04-2021 Disconnection
	public int DisconnectionSave(int Mtr_DisFlag,String imeino,String PreRead) 
	{
		int rtvalue = 0;

		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();
		CommonFunction cFun = new CommonFunction();
		ReadSlabNTarifSbmBillCollection sbc = BillingObject.GetBillingObject();

		try
		{				
			sldb.beginTransaction();
			sbc.setmIssueDateTime(cFun.GetCurrentTimeWOSPChar());		
			//Insert into SBM_Disconnection Table
			ContentValues valuesInsertMaintbl = new ContentValues();			
			valuesInsertMaintbl.put("ForMnth", sbc.getmForMonth());
			valuesInsertMaintbl.put("BillDate", sbc.getmBillDate());
			valuesInsertMaintbl.put("ConnectionNo",sbc.getmConnectionNo().trim());
			valuesInsertMaintbl.put("TourPlanId", sbc.getmTourplanId());
			valuesInsertMaintbl.put("PreRead", PreRead);	//Present Reading Entered			
			valuesInsertMaintbl.put("TarifCode", sbc.getmTariffCode());
			valuesInsertMaintbl.put("IssueDateTime", sbc.getmIssueDateTime());
			valuesInsertMaintbl.put("RRNo", sbc.getmRRNo());
			valuesInsertMaintbl.put("LocationCode", sbc.getmLocationCode());
			valuesInsertMaintbl.put("Mtr_DisFlag", Mtr_DisFlag);  
			valuesInsertMaintbl.put("IMEINo", imeino);		


			long insertResult = sldb.insert("SBM_Disconnection", null, valuesInsertMaintbl);
			if(insertResult <= 0)
			{	
				sldb.endTransaction();
				throw new Exception("Insertion failed for SBM_Disconnection");
			}	

			String currentdate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
			//Insert into SBM_DisconnectionImage Table
			valuesInsertMaintbl.clear();
			valuesInsertMaintbl.put("IMEINo", imeino);
			valuesInsertMaintbl.put("ForMnth", sbc.getmForMonth());
			valuesInsertMaintbl.put("BillDate", sbc.getmBillDate());
			valuesInsertMaintbl.put("ConnectionNo",sbc.getmConnectionNo().trim());			
			valuesInsertMaintbl.put("Mtr_DisFlag", Mtr_DisFlag);	//Present Reading Entered			
			valuesInsertMaintbl.put("PhotoName", sbc.getmImage_Name().trim());
			valuesInsertMaintbl.put("CreatedDate",currentdate);			

			long insertResultImage = sldb.insert("SBM_DisconnectionImage", null, valuesInsertMaintbl);
			if(insertResultImage <= 0)
			{	
				sldb.endTransaction();
				throw new Exception("Insertion failed for SBM_DisconnectionImage");
			}	
			sldb.setTransactionSuccessful();

			rtvalue = 1;
			/*try
				{
					DatabaseHelperBackupDB bdBackup = new DatabaseHelperBackupDB(mcntx);
					bdBackup.BackupDBCollectionSave();//Back Up database save
				}
				catch(Exception e)
				{
					Log.d("", e.toString());
				}*/
			//return 1;

		}
		catch(Exception e)
		{
			Log.d("", e.toString());
			//return 0;
			rtvalue = 0;
		}	
		finally
		{
			sldb.endTransaction();
		}	
		return rtvalue;
	}
	//Get Count For Disconnection. returns -> 1 if Mtr Disconnected ->2 if Mtr NotDisConnected  else returns 0 
	public int GetDisconnectionFlag(String connid)
	{
		Cursor crDis = null;
		int count=0;
		try
		{
			QueryParameters qParam = StoredProcedure.GetDisconnectionFlag(connid.trim());
			crDis = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());		
			if(crDis != null && crDis.getCount() > 0)//Cursor 1
			{
				crDis.moveToFirst();
				count = crDis.getInt(crDis.getColumnIndex(COL_SBM_DISCONNECION_MTRDISFLAG));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crDis!=null)
			{
				crDis.close();
			}
		}		
		return count;
	}
	//Nitish 08-10-2014	//Get ArrayList of Disconnection Data to be sent to Server
	public ArrayList<String> GetDataSentToServerDisconnection()
	{
		ArrayList<String> alStr = new ArrayList<String>();
		Cursor crWriteDis = null;
		StringBuilder sb = new StringBuilder();
		try
		{
			QueryParameters qParam = StoredProcedure.GetDataSentToServerDisconnection();
			crWriteDis = getReadableDatabase().rawQuery(qParam.getSql(), null);		
			if(crWriteDis != null && crWriteDis.getCount() > 0)
			{
				crWriteDis.moveToFirst();
				do 
				{
					try
					{
						WriteFileParameters wfp = new WriteFileParameters();
						//85
						/*wfp.setmForMonth(crWriteDis.getString(crWriteDis.getColumnIndex(COL_SBM_DISCONNECION_FORMNTH)));//04
							wfp.setmBillDate(crWriteDis.getString(crWriteDis.getColumnIndex(COL_SBM_DISCONNECION_BILLDATE)));//08
							wfp.setmConnectionNo(crWriteDis.getString(crWriteDis.getColumnIndex(COL_SBM_DISCONNECION_CONNECTIONNO)));//10
							wfp.setmTourplanId(crWriteDis.getString(crWriteDis.getColumnIndex(COL_SBM_DISCONNECION_TOURPLANID)));//09
							wfp.setmPreRead(crWriteDis.getString(crWriteDis.getColumnIndex(COL_SBM_DISCONNECION_PREREAD)));//12
							wfp.setmTariffCode(crWriteDis.getString(crWriteDis.getColumnIndex(COL_SBM_DISCONNECION_TARIFCODE)));//03
							wfp.setmIssueDateTime(crWriteDis.getString(crWriteDis.getColumnIndex(COL_SBM_DISCONNECION_ISSUEDATETIME)));//12
							wfp.setmRRNo(crWriteDis.getString(crWriteDis.getColumnIndex(COL_SBM_DISCONNECION_RRNO)));//13
							wfp.setmLocationCode(crWriteDis.getString(crWriteDis.getColumnIndex(COL_SBM_DISCONNECION_LOCATIONCODE)));//10
							wfp.setmMtrDisFlag(crWriteDis.getString(crWriteDis.getColumnIndex(COL_SBM_DISCONNECION_MTRDISFLAG)));//	1					
							wfp.setmVer(ConstantClass.FileVersion);//version may change make it global	//3
							//wfp.setmVer(((Activity)mcntx).getResources().getString(R.string.FileVersion));//version may change make it global

							sb.delete(0, sb.length());
							sb.append(wfp.getmForMonth());											
							sb.append(wfp.getmBillDate());
							sb.append(wfp.getmConnectionNo());
							sb.append(wfp.getmTourplanId());
							sb.append(wfp.getmPreRead());
							sb.append(wfp.getmTariffCode());
							sb.append(wfp.getmIssueDateTime());
							sb.append(wfp.getmRRNo());
							sb.append(wfp.getmLocationCode());
							sb.append(wfp.getmMtrDisFlag());
							sb.append(wfp.getmVer());
							alStr.add(sb.toString());
						 */

						//85
						wfp.setmForMonth_Disc(crWriteDis.getString(crWriteDis.getColumnIndex(COL_SBM_DISCONNECION_FORMNTH)));//04
						wfp.setmBillDate_Disc(crWriteDis.getString(crWriteDis.getColumnIndex(COL_SBM_DISCONNECION_BILLDATE)));//08
						wfp.setmConnectionNo_Disc(crWriteDis.getString(crWriteDis.getColumnIndex(COL_SBM_DISCONNECION_CONNECTIONNO)));//10
						wfp.setmTourplanId_Disc(crWriteDis.getString(crWriteDis.getColumnIndex(COL_SBM_DISCONNECION_TOURPLANID)));//09
						wfp.setmPreRead_Disc(crWriteDis.getString(crWriteDis.getColumnIndex(COL_SBM_DISCONNECION_PREREAD)));//12
						wfp.setmTariffCode_Disc(crWriteDis.getString(crWriteDis.getColumnIndex(COL_SBM_DISCONNECION_TARIFCODE)));//03
						wfp.setmIssueDateTime_Disc(crWriteDis.getString(crWriteDis.getColumnIndex(COL_SBM_DISCONNECION_ISSUEDATETIME)));//12
						wfp.setmRRNo_Disc(crWriteDis.getString(crWriteDis.getColumnIndex(COL_SBM_DISCONNECION_RRNO)));//13
						wfp.setmLocationCode_Disc(crWriteDis.getString(crWriteDis.getColumnIndex(COL_SBM_DISCONNECION_LOCATIONCODE)));//10
						wfp.setmMtrDisFlag_Disc(crWriteDis.getString(crWriteDis.getColumnIndex(COL_SBM_DISCONNECION_MTRDISFLAG)));//	1					
						wfp.setmVer_Disc(ConstantClass.FileVersion);//version may change make it global	//3
						//wfp.setmVer(((Activity)mcntx).getResources().getString(R.string.FileVersion));//version may change make it global

						sb.delete(0, sb.length());
						sb.append(wfp.getmForMonth_Disc());											
						sb.append(wfp.getmBillDate_Disc());
						sb.append(wfp.getmConnectionNo_Disc());
						sb.append(wfp.getmTourplanId_Disc());
						sb.append(wfp.getmPreRead_Disc());
						sb.append(wfp.getmTariffCode_Disc());
						sb.append(wfp.getmIssueDateTime_Disc());
						sb.append(wfp.getmRRNo_Disc());
						sb.append(wfp.getmLocationCode_Disc());
						sb.append(wfp.getmMtrDisFlag_Disc());
						sb.append(wfp.getmVer_Disc());

						alStr.add(sb.toString());
					}
					catch(Exception e)
					{
						Log.d(TAG, e.toString());
					}
				}while(crWriteDis.moveToNext());
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return alStr;
	}
	//Nitish 10-10-2014
	public int UpdateGPRSFlagDisconnection(String connno,String GPRSStatus)
	{
		int upStatus = 0;
		try
		{
			ContentValues valuesUpdateGPStbl = new ContentValues();
			valuesUpdateGPStbl.put("Gprs_Flag", 1);	
			valuesUpdateGPStbl.put("Gprs_Status", GPRSStatus);	
			upStatus = getWritableDatabase().update("SBM_Disconnection", valuesUpdateGPStbl, "trim(ConnectionNo) = ?", new String[]{connno.trim()});
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return upStatus;
	}
	//Nitish 10-10-2014
	public int UpdateGPRSStausDisconnection(String connno,String GPRSStatus)
	{
		int upStatus = 0;
		try
		{
			ContentValues valuesUpdateGPStbl = new ContentValues();
			valuesUpdateGPStbl.put("Gprs_Status", GPRSStatus);				
			upStatus = getWritableDatabase().update("SBM_Disconnection", valuesUpdateGPStbl, "trim(ConnectionNo) = ?", new String[]{connno.trim()});
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return upStatus;
	}
	//Nitish 10-10-2014 //GET GPRS Disconnection count and GPRS SENT Count
	public String GPRSFlagDisConn()
	{

		Cursor crDisCount = null;
		String strValue = "";
		try	
		{
			QueryParameters qParam1 = StoredProcedure.GetCountGPRSSentConnectionDisconnection();
			crDisCount = getReadableDatabase().rawQuery(qParam1.getSql(), null);
			if(crDisCount != null && crDisCount.getCount() > 0)
			{
				crDisCount.moveToFirst();
				strValue = crDisCount.getString(crDisCount.getColumnIndex("COUNT"));
			}
			else
			{
				strValue = "0";
			}
			strValue += "/";
			QueryParameters qParam = StoredProcedure.GetCountforDisconnection();
			crDisCount = getReadableDatabase().rawQuery(qParam.getSql(), null);
			if(crDisCount != null && crDisCount.getCount() > 0)
			{
				crDisCount.moveToFirst();
				strValue += crDisCount.getString(crDisCount.getColumnIndex("COUNT"));
			}
			else
			{
				strValue += "0";
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return strValue;
	}
	//Nitish 10-10-2014  Get connid of Disconnected connections whose GPRS is not sent
	public ArrayList<GPRSReportNotSent> GetDisconnConnIdGPRSNotSent() 
	{
		ArrayList<GPRSReportNotSent> mList = new  ArrayList<GPRSReportNotSent>();

		Cursor crRep=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.GetDisconnConnIdGPRSNotSent();
			crRep = getReadableDatabase().rawQuery(qParam.getSql(),null);	
			if(crRep!=null)
			{
				for(int i=1; i<=crRep.getCount();++i)
				{

					if(mList==null)
					{
						mList=new ArrayList<GPRSReportNotSent>();
					}
					if(i==1)				
						crRep.moveToFirst();				
					else
						crRep.moveToNext();

					GPRSReportNotSent rb = new GPRSReportNotSent();						
					//Get ConNo Which are not sent 
					rb.setmConNo(crRep.getString(crRep.getColumnIndex("NOTSENT")));
					rb.setmGPRSNotsent(crRep.getString(crRep.getColumnIndex("GPRSSTATUS")));					
					mList.add(rb);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception

		}
		finally
		{
			if(crRep!=null)
			{
				crRep.close();
			}
		}
		return mList;
	}	
	//Nitish 13-10-2014 //Total Disconnection Count
	public int GetCountforDisconnection()
	{
		Cursor crBat = null;
		int count=0;
		try
		{
			QueryParameters qParam = StoredProcedure.GetCountforDisconnection();
			crBat = getReadableDatabase().rawQuery(qParam.getSql(), null);	
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				count = crBat.getInt(crBat.getColumnIndex("COUNT"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return count;
	}
	
	public int GetCountGPRSSentConnectionDisconnection()
	{
		Cursor crBat = null;
		int count=0;
		try
		{
			QueryParameters qParam = StoredProcedure.GetCountGPRSSentConnectionDisconnection();
			crBat = getReadableDatabase().rawQuery(qParam.getSql(), null);	
			if(crBat != null && crBat.getCount() > 0)//Cursor 1
			{
				crBat.moveToFirst();
				count = crBat.getInt(crBat.getColumnIndex("COUNT"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//throw e;
		}
		finally
		{
			if(crBat!=null)
			{
				crBat.close();
			}
		}		
		return count;
	}
	//Nitish 13-02-2015	//Get ArrayList of DisConnection image Data
		public ArrayList<String> GetDataSendToServerDisConnImg()
		{
			ArrayList<String> alStr = new ArrayList<String>();
			Cursor crWriteFac = null;
			StringBuilder sb = new StringBuilder();
			try
			{
				QueryParameters qParam = StoredProcedure.GetDataSendToServerDisConnImg();
				crWriteFac = getReadableDatabase().rawQuery(qParam.getSql(), null);		
				if(crWriteFac != null && crWriteFac.getCount() > 0)
				{
					crWriteFac.moveToFirst();
					do 
					{
						try
						{
							sb.delete(0, sb.length());
							sb.append(crWriteFac.getString(crWriteFac.getColumnIndex(COL_Id_TABLE_SBM_DISCONNECIONIMAGE)));
							sb.append("##");							
							sb.append(crWriteFac.getString(crWriteFac.getColumnIndex(COL_CreatedDate_TABLE_SBM_DISCONNECIONIMAGE)));	
							sb.append("##");
							sb.append(crWriteFac.getString(crWriteFac.getColumnIndex(COL_PhotoName_TABLE_SBM_DISCONNECIONIMAGE)));	
							alStr.add(sb.toString());
						}
						catch(Exception e)
						{
							Log.d(TAG, e.toString());
						}
					}while(crWriteFac.moveToNext());
				}
			}
			catch(Exception e)
			{
				Log.d(TAG, e.toString());
			}
			return alStr;
		}


		//Nitish 13-02-2015
		public int UpdateGPRSFlagDisConnImg(String id)
		{
			int upStatus = 0;
			try
			{
				ContentValues valuesUpdateGPStbl = new ContentValues();
				valuesUpdateGPStbl.put("Gprs_Flag", 1);				
				upStatus = getWritableDatabase().update("SBM_DisconnectionImage", valuesUpdateGPStbl, "trim(UID) = ?", new String[]{id.trim()});
			}
			catch(Exception e)
			{
				Log.d(TAG, e.toString());
			}
			return upStatus;
		}
		//End Disconnection ////////////////////////////
		
		//25-06-2016 for ColLatLong as String
		public ArrayList<String> getCollectionPaymentStatusNew()
		{
			ArrayList<String> collpaystatus =new ArrayList<String>();
			ReadCollection  rdcoll=new ReadCollection();
			Cursor csrpaysts;
			StringBuilder sb =new StringBuilder();
			try 
			{
				QueryParameters qParam = StoredProcedure.getCollectionPaymentStatus();
				csrpaysts = getReadableDatabase().rawQuery(qParam.getSql(), null);
				if(csrpaysts !=null && csrpaysts.getCount() >0)
				{
					csrpaysts.moveToFirst();
					do 
					{

						rdcoll.setmSBMNumber(csrpaysts.getString(csrpaysts.getColumnIndex(COL_ImeiNo_Collection_Payment_Status)));
						rdcoll.setmBatch_No(csrpaysts.getString(csrpaysts.getColumnIndex(COL_BatchNo_Collection_Payment_Status)));
						rdcoll.setmConnectionNo(csrpaysts.getString(csrpaysts.getColumnIndex(COL_ConId_Collection_Payment_Status)));
						rdcoll.setmLat(csrpaysts.getString(csrpaysts.getColumnIndex(COL_Lat_Collection_Payment_Status)));
						rdcoll.setmLong(csrpaysts.getString(csrpaysts.getColumnIndex(COL_Long_Collection_Payment_Status)));
						rdcoll.setmRemarkId(csrpaysts.getInt(csrpaysts.getColumnIndex(COL_PaymentReasonId_Collection_Payment_Status)));
						rdcoll.setmScheduledDate(csrpaysts.getString(csrpaysts.getColumnIndex(COL_ScheduledPayDate_Collection_Payment_Status)));
						rdcoll.setmCreatedDate(csrpaysts.getString(csrpaysts.getColumnIndex(COL_CreatedDate_Collection_Payment_Status)));

						sb.delete(0, sb.length());
						sb.append(rdcoll.getmSBMNumber()+"##");											
						sb.append(rdcoll.getmBatch_No()+"##");	
						sb.append(rdcoll.getmConnectionNo()+"##");	
						sb.append(rdcoll.getmLat()+"##");	
						sb.append(rdcoll.getmLong()+"##");	
						sb.append(String.valueOf(rdcoll.getmRemarkId())+"##");	
						sb.append(rdcoll.getmScheduledDate()+"##");	
						sb.append(rdcoll.getmCreatedDate());	
						collpaystatus.add(sb.toString());	
					} 
					while (csrpaysts.moveToNext());	


				}
			}
			catch (Exception e)
			{
				Log.d("", e.toString());
			}
			return collpaystatus;
		}	

		//11-05-2021 for Collection payment Status
		public int UpdateCollLatLong(String ConId,String BatchNo)
		{
			DatabaseHelper dh = new DatabaseHelper(mcntx);
			SQLiteDatabase sldb = dh.getWritableDatabase();
			int upStatus = 0;
			try
			{

				ContentValues valuesUpdateColltbl = new ContentValues();
				valuesUpdateColltbl.put("GPRSFlag", "1");
				//sldb.update("Collection_TABLE", valuesUpdateColltbl, "ConnectionNo = ?", new String[]{ConnectionNo});
				upStatus = sldb.update("Collection_Payment_Status", valuesUpdateColltbl, "ConId = ? and BatchNo = ? ", new String[]{ConId,BatchNo});

			}
			catch(Exception e)
			{
				Log.d(TAG, e.toString());
			}
			return upStatus;
		}
		
		//26-05-2021 Disconnection For Arrears greater than 10
		public void GetValueOnBillingPageDisc() throws Exception
		{
			ReadSlabNTarifSbmBillCollection CustDetails = BillingObject.GetBillingObject();

			cr=null ;
			try
			{
				QueryParameters qParam = StoredProcedure.GetValueOnBillingPageDisc();
				cr = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
				if(cr.getCount() > 0)//Cursor IF Start
				{
					//for(int i=1; i<=cr.getCount();++i)
					//{

					//if(i==1){ 
					cr.moveToFirst();



					CustDetails.setmAbFlag(cr.getString(cr.getColumnIndex(COL_AbFlag)));
					CustDetails.setmAccdRdg(cr.getString(cr.getColumnIndex(COL_AccdRdg)));
					CustDetails.setmAccdRdg_rtn(cr.getString(cr.getColumnIndex(COL_AccdRdg_rtn)));
					CustDetails.setmAddress1(cr.getString(cr.getColumnIndex(COL_Address1)));
					CustDetails.setmAddress2(cr.getString(cr.getColumnIndex(COL_Address2)));
					CustDetails.setmApplicationNo(cr.getString(cr.getColumnIndex(COL_ApplicationNo)));
					CustDetails.setmArears(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Arears))));
					CustDetails.setmArrearsOld(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ArrearsOld))));
					CustDetails.setmAvgCons(cr.getString(cr.getColumnIndex(COL_AvgCons)));
					CustDetails.setmBankID(cr.getInt(cr.getColumnIndex(COL_BankID)));
					CustDetails.setmBatch_No(cr.getString(cr.getColumnIndex(COL_Batch_No)));
					CustDetails.setmBillable(cr.getString(cr.getColumnIndex(COL_Billable))); 
					CustDetails.setmBillDate(cr.getString(cr.getColumnIndex(COL_BillDate)));
					CustDetails.setmBillFor(cr.getString(cr.getColumnIndex(COL_BillFor)));
					CustDetails.setmBillNo(cr.getString(cr.getColumnIndex(COL_BillNo)));
					CustDetails.setmBillTotal(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_BillTotal))));
					CustDetails.setmBjKj2Lt2(cr.getString(cr.getColumnIndex(COL_BjKj2Lt2)));
					CustDetails.setmBlCnt(cr.getString(cr.getColumnIndex(COL_BlCnt)));
					CustDetails.setmBOBilled_Amount(cr.getString(cr.getColumnIndex(COL_BOBilled_Amount)));
					CustDetails.setmBOBillFlag(cr.getInt(cr.getColumnIndex(COL_BOBillFlag)));
					CustDetails.setmCapReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_CapReb))));
					CustDetails.setmChargetypeID(cr.getInt(cr.getColumnIndex(COL_ChargetypeID)));
					CustDetails.setmChequeDDDate(cr.getString(cr.getColumnIndex(COL_ChequeDDDate)));
					CustDetails.setmChequeDDNo(cr.getInt(cr.getColumnIndex(COL_ChequeDDNo)));
					CustDetails.setmConnectionNo(cr.getString(cr.getColumnIndex(COL_ConnectionNo)));
					CustDetails.setmConsPayable(cr.getString(cr.getColumnIndex(COL_ConsPayable)));
					CustDetails.setmCustomerName(cr.getString(cr.getColumnIndex(COL_CustomerName)));
					CustDetails.setmDateTime(cr.getString(cr.getColumnIndex(COL_DateTime)));
					CustDetails.setmDayWise_Flag(cr.getString(cr.getColumnIndex(COL_DayWise_Flag)));
					CustDetails.setmDemandChrg(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_DemandChrg))));
					CustDetails.setmDLAvgMin(cr.getString(cr.getColumnIndex(COL_DLAvgMin)));
					CustDetails.setmDlCount(cr.getInt(cr.getColumnIndex(COL_DlCount)));
					CustDetails.setmDlCount(cr.getInt(cr.getColumnIndex(COL_DlCount)));
					CustDetails.setmDLTEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_DLTEc))));
					CustDetails.setmDPdate(cr.getString(cr.getColumnIndex(COL_DPdate)));
					CustDetails.setmDrFees(cr.getString(cr.getColumnIndex(COL_DrFees)));
					CustDetails.setmDueDate(cr.getString(cr.getColumnIndex(COL_DueDate)));
					CustDetails.setmECReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ECReb))));
					CustDetails.setmEcsFlag(cr.getString(cr.getColumnIndex(COL_EcsFlag)));
					CustDetails.setmExLoad(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ExLoad))));
					CustDetails.setmFC_Slab_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_Slab_2))));
					CustDetails.setmFixedCharges(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FixedCharges))));
					CustDetails.setmFLReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FLReb))));
					CustDetails.setmForMonth(cr.getString(cr.getColumnIndex(COL_ForMonth)));
					CustDetails.setmGoKArrears(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_GoKArrears))));
					CustDetails.setmGoKPayable(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_GoKPayable))));
					CustDetails.setmGprs_Flag(cr.getInt(cr.getColumnIndex(COL_Gprs_Flag)));
					CustDetails.setmGps_Latitude_image(cr.getString(cr.getColumnIndex(COL_Gps_Latitude_image)));
					CustDetails.setmGps_Latitude_print(cr.getString(cr.getColumnIndex(COL_Gps_Latitude_print)));
					CustDetails.setmGps_LatitudeCardinal_image(cr.getString(cr.getColumnIndex(COL_Gps_LatitudeCardinal_image)));
					CustDetails.setmGps_LatitudeCardinal_print(cr.getString(cr.getColumnIndex(COL_Gps_LatitudeCardinal_print)));
					CustDetails.setmGps_Longitude_image(cr.getString(cr.getColumnIndex(COL_Gps_Longitude_image)));
					CustDetails.setmGps_Longitude_print(cr.getString(cr.getColumnIndex(COL_Gps_Longitude_print)));
					CustDetails.setmGps_LongitudeCardinal_image(cr.getString(cr.getColumnIndex(COL_Gps_LongitudeCardinal_image)));
					CustDetails.setmGps_LatitudeCardinal_print(cr.getString(cr.getColumnIndex(COL_Gps_LatitudeCardinal_print)));
					CustDetails.setmGvpId(cr.getString(cr.getColumnIndex(COL_GvpId)));
					CustDetails.setmHCReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_HCReb))));
					CustDetails.setmHLReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_HLReb))));
					CustDetails.setmHWCReb(cr.getString(cr.getColumnIndex(COL_HWCReb)));
					CustDetails.setmImage_Cap_Date(cr.getString(cr.getColumnIndex(COL_Image_Cap_Date)));
					CustDetails.setmImage_Cap_Time(cr.getString(cr.getColumnIndex(COL_Image_Cap_Time)));
					CustDetails.setmImage_Name(cr.getString(cr.getColumnIndex(COL_Image_Name)));
					CustDetails.setmImage_Path(cr.getString(cr.getColumnIndex(COL_Image_Path)));
					CustDetails.setmIntrst_Unpaid(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Intrst_Unpaid))));
					CustDetails.setmIntrstCurnt(cr.getString(cr.getColumnIndex(COL_IntrstCurnt)));
					CustDetails.setmIntrstOld(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_IntrstOld))));
					CustDetails.setmIODD11_Remarks(cr.getString(cr.getColumnIndex(COL_IODD11_Remarks)));
					CustDetails.setmIODRemarks(cr.getString(cr.getColumnIndex(COL_IODRemarks)));
					CustDetails.setmIssueDateTime(cr.getString(cr.getColumnIndex(COL_IssueDateTime)));
					CustDetails.setmKVA_Consumption(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_KVA_Consumption))));
					CustDetails.setmKVAAssd_Cons(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_KVAAssd_Cons))));
					CustDetails.setmKVAFR(cr.getString(cr.getColumnIndex(COL_KVAFR)));
					CustDetails.setmKVAH_OldConsumption(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_KVAFR))));
					CustDetails.setmKVAIR(cr.getString(cr.getColumnIndex(COL_KVAIR)));
					CustDetails.setmLatitude(cr.getString(cr.getColumnIndex(COL_Latitude)));
					CustDetails.setmLatitude_Dir(cr.getString(cr.getColumnIndex(COL_Latitude_Dir)));
					CustDetails.setmLegFol(cr.getString(cr.getColumnIndex(COL_LegFol)));
					CustDetails.setmLinMin(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_LinMin))));
					CustDetails.setmLocationCode(cr.getString(cr.getColumnIndex(COL_LocationCode)));
					CustDetails.setmLongitude(cr.getString(cr.getColumnIndex(COL_Longitude)));
					CustDetails.setmLongitude_Dir(cr.getString(cr.getColumnIndex(COL_Longitude_Dir)));
					CustDetails.setmMCHFlag(cr.getString(cr.getColumnIndex(COL_MCHFlag)));
					CustDetails.setmMeter_serialno(cr.getString(cr.getColumnIndex(COL_Meter_serialno)));
					CustDetails.setmMeter_type(cr.getString(cr.getColumnIndex(COL_Meter_type)));
					CustDetails.setmMF(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_MF))));
					CustDetails.setmMobileNo(cr.getString(cr.getColumnIndex(COL_MobileNo)));
					CustDetails.setmMtd(cr.getString(cr.getColumnIndex(COL_Mtd)));
					CustDetails.setmMtrDisFlag(cr.getInt(cr.getColumnIndex(COL_MtrDisFlag)));
					CustDetails.setmNewNoofDays(cr.getString(cr.getColumnIndex(COL_NewNoofDays)));
					CustDetails.setmNoOfDays(cr.getString(cr.getColumnIndex(COL_NoOfDays)));
					CustDetails.setmOld_Consumption(cr.getString(cr.getColumnIndex(COL_Old_Consumption)));
					CustDetails.setmOldConnID(cr.getString(cr.getColumnIndex(COL_OldConnID)));
					CustDetails.setmOtherChargeLegend(cr.getString(cr.getColumnIndex(COL_OtherChargeLegend)));
					CustDetails.setmOthers(cr.getString(cr.getColumnIndex(COL_Others)));
					CustDetails.setmPaid_Amt(cr.getInt(cr.getColumnIndex(COL_Paid_Amt)));
					CustDetails.setmPayment_Mode(cr.getString(cr.getColumnIndex(COL_Payment_Mode)));
					CustDetails.setmPenExLd(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_PenExLd))));
					CustDetails.setmPF(cr.getString(cr.getColumnIndex(COL_PF)));
					CustDetails.setmPfPenAmt(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_PfPenAmt))));
					CustDetails.setmPreRead(cr.getString(cr.getColumnIndex(COL_PreRead)));
					CustDetails.setmPreStatus(cr.getString(cr.getColumnIndex(COL_Status)));
					CustDetails.setmPrevRdg(cr.getString(cr.getColumnIndex(COL_PrevRdg)));
					CustDetails.setmRcptCnt(cr.getInt(cr.getColumnIndex(COL_RcptCnt)));
					CustDetails.setmReaderCode(cr.getString(cr.getColumnIndex(COL_ReaderCode)));
					CustDetails.setmReadingDay(cr.getString(cr.getColumnIndex(COL_ReadingDay)));
					CustDetails.setmRebateFlag(cr.getString(cr.getColumnIndex(COL_RebateFlag)));
					CustDetails.setmReceipt_No(cr.getInt(cr.getColumnIndex(COL_Receipt_No)));
					CustDetails.setmReceiptAmnt(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ReceiptAmnt))));
					CustDetails.setmReceiptDate(cr.getString(cr.getColumnIndex(COL_ReceiptDate)));
					CustDetails.setmReceipttypeflag(cr.getString(cr.getColumnIndex(COL_Receipttypeflag)));
					CustDetails.setmRecordDmnd(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_RecordDmnd))));
					CustDetails.setmRemarks(cr.getString(cr.getColumnIndex(COL_Remarks)));
					CustDetails.setmRRFlag(cr.getString(cr.getColumnIndex(COL_RRFlag)));
					CustDetails.setmRRNo(cr.getString(cr.getColumnIndex(COL_RRNo)));
					CustDetails.setmSancHp(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SancHp))));
					CustDetails.setmSancKw(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SancKw))));
					CustDetails.setmSancLoad(cr.getString(cr.getColumnIndex(COL_SancLoad)));
					CustDetails.setmSBMNumber(cr.getString(cr.getColumnIndex(COL_SBMNumber)));
					CustDetails.setmSectionName(cr.getString(cr.getColumnIndex(COL_SectionName)));
					CustDetails.setmSlowRtnPge(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SlowRtnPge))));
					CustDetails.setmSpotSerialNo(cr.getInt(cr.getColumnIndex(COL_SpotSerialNo)));
					CustDetails.setmStatus(cr.getInt(cr.getColumnIndex(COL_Status)));
					CustDetails.setmSubId(cr.getString(cr.getColumnIndex(COL_SubId)));
					CustDetails.setmSupply_Points(cr.getInt(cr.getColumnIndex(COL_Supply_Points)));           
					CustDetails.setmTariffCode(cr.getInt(cr.getColumnIndex(COL_TariffCode)));
					CustDetails.setmTarifString(cr.getString(cr.getColumnIndex(COL_TarifString)));     
					CustDetails.setmTaxAmt(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TaxAmt))));
					CustDetails.setmTaxFlag(cr.getString(cr.getColumnIndex(COL_TaxFlag)));
					CustDetails.setmTcName(cr.getString(cr.getColumnIndex(COL_TcName)));
					CustDetails.setmTEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TEc))));
					CustDetails.setmTFc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TFc))));
					CustDetails.setmThirtyFlag(cr.getString(cr.getColumnIndex(COL_ThirtyFlag)));
					CustDetails.setmTourplanId(cr.getString(cr.getColumnIndex(COL_TourplanId)));
					CustDetails.setmTvmMtr(cr.getString(cr.getColumnIndex(COL_TvmMtr)));
					CustDetails.setmTvmPFtype(cr.getString(cr.getColumnIndex(COL_TvmPFtype)));
					//CustDetails.setmUIDR_Slab(cr.getInt(cr.getColumnIndex(COL_UID_Slab)));
					CustDetails.setmUnits(cr.getString(cr.getColumnIndex(COL_Units)));

					CustDetails.setMmUID(cr.getInt(cr.getColumnIndex(COL_UID_S_BcData)));//punit 25022014
					CustDetails.setmMeter_Present_Flag(cr.getString(cr.getColumnIndex(COL_METER_PRESENT_FLAG)) == null ? 0 : cr.getInt(cr.getColumnIndex(COL_METER_PRESENT_FLAG)));
					CustDetails.setmMtr_Not_Visible(cr.getString(cr.getColumnIndex(COL_MTR_NOT_VISIBLE)) == null ? 0 : cr.getInt(cr.getColumnIndex(COL_MTR_NOT_VISIBLE)));

					CustDetails.setmDLTEc_GoK(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_DLTEc_GoK))));//31-12-2014
					CustDetails.setmOldTecBill(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_OldTecBill))));//Nitish on 21-04-2016
					//30-07-2015
					CustDetails.setmAadharNo(cr.getString(cr.getColumnIndex(COL_AadharNo)));
					CustDetails.setmVoterIdNo(cr.getString(cr.getColumnIndex(COL_VoterIdNo)));				
					CustDetails.setmMtrMakeFlag(cr.getInt(cr.getColumnIndex(COL_MtrMakeFlag)));
					CustDetails.setmMtrBodySealFlag(cr.getInt(cr.getColumnIndex(COL_MtrBodySealFlag)));
					CustDetails.setmMtrTerminalCoverFlag(cr.getInt(cr.getColumnIndex(COL_MtrTerminalCoverFlag)));
					CustDetails.setmMtrTerminalCoverSealedFlag(cr.getInt(cr.getColumnIndex(COL_MtrTerminalCoverSealedFlag)));
					CustDetails.setmFACValue(cr.getString(cr.getColumnIndex(COL_FACValue)));//19-12-2015
					//CustDetails.setmUID(cr.getInt(cr.getColumnIndex(COL_UID_Slab)));
					//CustDetails.setMmConnectionNo(cr.getString(cr.getColumnIndex(COL_ConnectionNo_Slab)));
					//CustDetails.setMmRRNo(cr.getString(cr.getColumnIndex(COL_RRNo_Slab)));
					CustDetails.setMmECRate_Count(cr.getInt(cr.getColumnIndex(COL_ECRate_Count)));
					CustDetails.setMmECRate_Row(cr.getInt(cr.getColumnIndex(COL_ECRate_Row)));
					CustDetails.setMmFCRate_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FCRate_1))));
					CustDetails.setMmCOL_FCRate_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FCRate_2))));
					CustDetails.setMmUnits_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_1))));
					CustDetails.setMmUnits_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_2))));
					CustDetails.setMmUnits_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_3))));
					CustDetails.setMmUnits_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_4))));
					CustDetails.setMmUnits_5(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_5))));
					CustDetails.setMmUnits_6(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_6))));
					CustDetails.setMmEC_Rate_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_1))));
					CustDetails.setMmEC_Rate_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_2))));
					CustDetails.setMmEC_Rate_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_3))));
					CustDetails.setMmEC_Rate_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_4))));
					CustDetails.setMmEC_Rate_5(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_5))));
					CustDetails.setMmEC_Rate_6(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_6))));
					CustDetails.setMmEC_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_1))));
					CustDetails.setMmEC_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_2))));
					CustDetails.setMmEC_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_3))));
					CustDetails.setMmEC_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_4))));
					CustDetails.setMmEC_5(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_5))));
					CustDetails.setMmEC_6(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_6))));
					CustDetails.setMmFC_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_1))));
					CustDetails.setMmFC_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_2))));
					CustDetails.setMmTEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TEc_Slab))));
					CustDetails.setMmEC_FL_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_1))));
					CustDetails.setMmEC_FL_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_2))));
					CustDetails.setMmEC_FL_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_3))));
					CustDetails.setMmEC_FL_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_4))));
					CustDetails.setMmEC_FL(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL))));
					CustDetails.setMmnew_TEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_new_TEc))));
					CustDetails.setMmold_TEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_old_TEc))));

					//23-06-2021 for FC
					CustDetails.setMmNEWFC_UNIT1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT1))));
					CustDetails.setMmNEWFC_UNIT2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT2))));
					CustDetails.setMmNEWFC_UNIT3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT3))));
					CustDetails.setMmNEWFC_UNIT4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT4))));
					CustDetails.setMmNEWFC_Rate3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_Rate3))));
					CustDetails.setMmNEWFC_Rate4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_Rate4))));
					CustDetails.setMmNEWFC3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC3))));
					CustDetails.setMmNEWFC4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC4))));
					
					CustDetails.setmFC_Slab_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_Slab_3))));
					
				}//Cursor IF END
			} catch (Exception e) {
				// TODO: handle exception
				Log.d("punit", e.toString());
				throw e;
			}
			finally
			{
				if(cr!=null)
				{
					cr.close();
				}
			}

		}
		//26-05-2021 Disconnection For Arrears greater than 10
		public AutoDDLAdapter GetConnIdRRNoDisc() throws Exception
		{
			AutoDDLAdapter conList = null;
			cr=null ;
			try
			{
				QueryParameters qParam = StoredProcedure.GetConnIdRRNoDisc();
				cr = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());		
				for(int i=1; i<=cr.getCount();++i)
				{
					if(conList == null)
					{
						conList = new AutoDDLAdapter(mcntx, new ArrayList<DDLItem>(), 5);
					}
					if(i==1)
						cr.moveToFirst();
					else
						cr.moveToNext();		
					conList.AddItem(cr.getString(cr.getColumnIndex("ConnId")), cr.getString(cr.getColumnIndex("ConnIdRRNo")));

				}
			} catch (Exception e) {
				// TODO: handle exception
				//throw e;
			}
			finally
			{
				if(cr!=null)
				{
					cr.close();
				}
			}
			return conList;
		}
		//26-05-2021 Disconnection For Arrears greater than 10
		public void GetPrevDisc(String custId ) throws Exception
		{
			ReadSlabNTarifSbmBillCollection CustDetails = BillingObject.GetBillingObject();

			cr=null ;
			try
			{
				QueryParameters qParam = StoredProcedure.GetPrevDisc(custId);
				cr = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
				if(cr.getCount() > 0)//Cursor IF Start
				{
					//for(int i=1; i<=cr.getCount();++i)
					//{

					//if(i==1){ 
					cr.moveToFirst();



					CustDetails.setmAbFlag(cr.getString(cr.getColumnIndex(COL_AbFlag)));
					CustDetails.setmAccdRdg(cr.getString(cr.getColumnIndex(COL_AccdRdg)));
					CustDetails.setmAccdRdg_rtn(cr.getString(cr.getColumnIndex(COL_AccdRdg_rtn)));
					CustDetails.setmAddress1(cr.getString(cr.getColumnIndex(COL_Address1)));
					CustDetails.setmAddress2(cr.getString(cr.getColumnIndex(COL_Address2)));
					CustDetails.setmApplicationNo(cr.getString(cr.getColumnIndex(COL_ApplicationNo)));
					CustDetails.setmArears(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Arears))));
					CustDetails.setmArrearsOld(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ArrearsOld))));
					CustDetails.setmAvgCons(cr.getString(cr.getColumnIndex(COL_AvgCons)));
					CustDetails.setmBankID(cr.getInt(cr.getColumnIndex(COL_BankID)));
					CustDetails.setmBatch_No(cr.getString(cr.getColumnIndex(COL_Batch_No)));
					CustDetails.setmBillable(cr.getString(cr.getColumnIndex(COL_Billable))); 
					CustDetails.setmBillDate(cr.getString(cr.getColumnIndex(COL_BillDate)));
					CustDetails.setmBillFor(cr.getString(cr.getColumnIndex(COL_BillFor)));
					CustDetails.setmBillNo(cr.getString(cr.getColumnIndex(COL_BillNo)));
					CustDetails.setmBillTotal(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_BillTotal))));
					CustDetails.setmBjKj2Lt2(cr.getString(cr.getColumnIndex(COL_BjKj2Lt2)));
					CustDetails.setmBlCnt(cr.getString(cr.getColumnIndex(COL_BlCnt)));
					CustDetails.setmBOBilled_Amount(cr.getString(cr.getColumnIndex(COL_BOBilled_Amount)));
					CustDetails.setmBOBillFlag(cr.getInt(cr.getColumnIndex(COL_BOBillFlag)));
					CustDetails.setmCapReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_CapReb))));
					CustDetails.setmChargetypeID(cr.getInt(cr.getColumnIndex(COL_ChargetypeID)));
					CustDetails.setmChequeDDDate(cr.getString(cr.getColumnIndex(COL_ChequeDDDate)));
					CustDetails.setmChequeDDNo(cr.getInt(cr.getColumnIndex(COL_ChequeDDNo)));
					CustDetails.setmConnectionNo(cr.getString(cr.getColumnIndex(COL_ConnectionNo)));
					CustDetails.setmConsPayable(cr.getString(cr.getColumnIndex(COL_ConsPayable)));
					CustDetails.setmCustomerName(cr.getString(cr.getColumnIndex(COL_CustomerName)));
					CustDetails.setmDateTime(cr.getString(cr.getColumnIndex(COL_DateTime)));
					CustDetails.setmDayWise_Flag(cr.getString(cr.getColumnIndex(COL_DayWise_Flag)));
					CustDetails.setmDemandChrg(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_DemandChrg))));
					CustDetails.setmDLAvgMin(cr.getString(cr.getColumnIndex(COL_DLAvgMin)));
					CustDetails.setmDlCount(cr.getInt(cr.getColumnIndex(COL_DlCount)));
					CustDetails.setmDlCount(cr.getInt(cr.getColumnIndex(COL_DlCount)));
					CustDetails.setmDLTEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_DLTEc))));
					CustDetails.setmDPdate(cr.getString(cr.getColumnIndex(COL_DPdate)));
					CustDetails.setmDrFees(cr.getString(cr.getColumnIndex(COL_DrFees)));
					CustDetails.setmDueDate(cr.getString(cr.getColumnIndex(COL_DueDate)));
					CustDetails.setmECReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ECReb))));
					CustDetails.setmEcsFlag(cr.getString(cr.getColumnIndex(COL_EcsFlag)));
					CustDetails.setmExLoad(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ExLoad))));
					CustDetails.setmFC_Slab_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_Slab_2))));
					CustDetails.setmFixedCharges(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FixedCharges))));
					CustDetails.setmFLReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FLReb))));
					CustDetails.setmForMonth(cr.getString(cr.getColumnIndex(COL_ForMonth)));
					CustDetails.setmGoKArrears(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_GoKArrears))));
					CustDetails.setmGoKPayable(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_GoKPayable))));
					CustDetails.setmGprs_Flag(cr.getInt(cr.getColumnIndex(COL_Gprs_Flag)));
					CustDetails.setmGps_Latitude_image(cr.getString(cr.getColumnIndex(COL_Gps_Latitude_image)));
					CustDetails.setmGps_Latitude_print(cr.getString(cr.getColumnIndex(COL_Gps_Latitude_print)));
					CustDetails.setmGps_LatitudeCardinal_image(cr.getString(cr.getColumnIndex(COL_Gps_LatitudeCardinal_image)));
					CustDetails.setmGps_LatitudeCardinal_print(cr.getString(cr.getColumnIndex(COL_Gps_LatitudeCardinal_print)));
					CustDetails.setmGps_Longitude_image(cr.getString(cr.getColumnIndex(COL_Gps_Longitude_image)));
					CustDetails.setmGps_Longitude_print(cr.getString(cr.getColumnIndex(COL_Gps_Longitude_print)));
					CustDetails.setmGps_LongitudeCardinal_image(cr.getString(cr.getColumnIndex(COL_Gps_LongitudeCardinal_image)));
					CustDetails.setmGps_LatitudeCardinal_print(cr.getString(cr.getColumnIndex(COL_Gps_LatitudeCardinal_print)));
					CustDetails.setmGvpId(cr.getString(cr.getColumnIndex(COL_GvpId)));
					CustDetails.setmHCReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_HCReb))));
					CustDetails.setmHLReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_HLReb))));
					CustDetails.setmHWCReb(cr.getString(cr.getColumnIndex(COL_HWCReb)));
					CustDetails.setmImage_Cap_Date(cr.getString(cr.getColumnIndex(COL_Image_Cap_Date)));
					CustDetails.setmImage_Cap_Time(cr.getString(cr.getColumnIndex(COL_Image_Cap_Time)));
					CustDetails.setmImage_Name(cr.getString(cr.getColumnIndex(COL_Image_Name)));
					CustDetails.setmImage_Path(cr.getString(cr.getColumnIndex(COL_Image_Path)));
					CustDetails.setmIntrst_Unpaid(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Intrst_Unpaid))));
					CustDetails.setmIntrstCurnt(cr.getString(cr.getColumnIndex(COL_IntrstCurnt)));
					CustDetails.setmIntrstOld(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_IntrstOld))));
					CustDetails.setmIODD11_Remarks(cr.getString(cr.getColumnIndex(COL_IODD11_Remarks)));
					CustDetails.setmIODRemarks(cr.getString(cr.getColumnIndex(COL_IODRemarks)));
					CustDetails.setmIssueDateTime(cr.getString(cr.getColumnIndex(COL_IssueDateTime)));
					CustDetails.setmKVA_Consumption(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_KVA_Consumption))));
					CustDetails.setmKVAAssd_Cons(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_KVAAssd_Cons))));
					CustDetails.setmKVAFR(cr.getString(cr.getColumnIndex(COL_KVAFR)));
					CustDetails.setmKVAH_OldConsumption(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_KVAFR))));
					CustDetails.setmKVAIR(cr.getString(cr.getColumnIndex(COL_KVAIR)));
					CustDetails.setmLatitude(cr.getString(cr.getColumnIndex(COL_Latitude)));
					CustDetails.setmLatitude_Dir(cr.getString(cr.getColumnIndex(COL_Latitude_Dir)));
					CustDetails.setmLegFol(cr.getString(cr.getColumnIndex(COL_LegFol)));
					CustDetails.setmLinMin(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_LinMin))));
					CustDetails.setmLocationCode(cr.getString(cr.getColumnIndex(COL_LocationCode)));
					CustDetails.setmLongitude(cr.getString(cr.getColumnIndex(COL_Longitude)));
					CustDetails.setmLongitude_Dir(cr.getString(cr.getColumnIndex(COL_Longitude_Dir)));
					CustDetails.setmMCHFlag(cr.getString(cr.getColumnIndex(COL_MCHFlag)));
					CustDetails.setmMeter_serialno(cr.getString(cr.getColumnIndex(COL_Meter_serialno)));
					CustDetails.setmMeter_type(cr.getString(cr.getColumnIndex(COL_Meter_type)));
					CustDetails.setmMF(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_MF))));
					CustDetails.setmMobileNo(cr.getString(cr.getColumnIndex(COL_MobileNo)));
					CustDetails.setmMtd(cr.getString(cr.getColumnIndex(COL_Mtd)));
					CustDetails.setmMtrDisFlag(cr.getInt(cr.getColumnIndex(COL_MtrDisFlag)));
					CustDetails.setmNewNoofDays(cr.getString(cr.getColumnIndex(COL_NewNoofDays)));
					CustDetails.setmNoOfDays(cr.getString(cr.getColumnIndex(COL_NoOfDays)));
					CustDetails.setmOld_Consumption(cr.getString(cr.getColumnIndex(COL_Old_Consumption)));
					CustDetails.setmOldConnID(cr.getString(cr.getColumnIndex(COL_OldConnID)));
					CustDetails.setmOtherChargeLegend(cr.getString(cr.getColumnIndex(COL_OtherChargeLegend)));
					CustDetails.setmOthers(cr.getString(cr.getColumnIndex(COL_Others)));
					CustDetails.setmPaid_Amt(cr.getInt(cr.getColumnIndex(COL_Paid_Amt)));
					CustDetails.setmPayment_Mode(cr.getString(cr.getColumnIndex(COL_Payment_Mode)));
					CustDetails.setmPenExLd(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_PenExLd))));
					CustDetails.setmPF(cr.getString(cr.getColumnIndex(COL_PF)));
					CustDetails.setmPfPenAmt(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_PfPenAmt))));
					CustDetails.setmPreRead(cr.getString(cr.getColumnIndex(COL_PreRead)));
					CustDetails.setmPreStatus(cr.getString(cr.getColumnIndex(COL_PreStatus)));//current status
					CustDetails.setmPrevRdg(cr.getString(cr.getColumnIndex(COL_PrevRdg)));
					CustDetails.setmRcptCnt(cr.getInt(cr.getColumnIndex(COL_RcptCnt)));
					CustDetails.setmReaderCode(cr.getString(cr.getColumnIndex(COL_ReaderCode)));
					CustDetails.setmReadingDay(cr.getString(cr.getColumnIndex(COL_ReadingDay)));
					CustDetails.setmRebateFlag(cr.getString(cr.getColumnIndex(COL_RebateFlag)));
					CustDetails.setmReceipt_No(cr.getInt(cr.getColumnIndex(COL_Receipt_No)));
					CustDetails.setmReceiptAmnt(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ReceiptAmnt))));
					CustDetails.setmReceiptDate(cr.getString(cr.getColumnIndex(COL_ReceiptDate)));
					CustDetails.setmReceipttypeflag(cr.getString(cr.getColumnIndex(COL_Receipttypeflag)));
					CustDetails.setmRecordDmnd(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_RecordDmnd))));
					CustDetails.setmRemarks(cr.getString(cr.getColumnIndex(COL_Remarks)));
					CustDetails.setmRRFlag(cr.getString(cr.getColumnIndex(COL_RRFlag)));
					CustDetails.setmRRNo(cr.getString(cr.getColumnIndex(COL_RRNo)));
					CustDetails.setmSancHp(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SancHp))));
					CustDetails.setmSancKw(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SancKw))));
					CustDetails.setmSancLoad(cr.getString(cr.getColumnIndex(COL_SancLoad)));
					CustDetails.setmSBMNumber(cr.getString(cr.getColumnIndex(COL_SBMNumber)));
					CustDetails.setmSectionName(cr.getString(cr.getColumnIndex(COL_SectionName)));
					CustDetails.setmSlowRtnPge(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SlowRtnPge))));
					CustDetails.setmSpotSerialNo(cr.getInt(cr.getColumnIndex(COL_SpotSerialNo)));
					CustDetails.setmStatus(cr.getInt(cr.getColumnIndex(COL_Status)));//previous status
					CustDetails.setmSubId(cr.getString(cr.getColumnIndex(COL_SubId)));
					CustDetails.setmSupply_Points(cr.getInt(cr.getColumnIndex(COL_Supply_Points)));
					CustDetails.setmTariffCode(cr.getInt(cr.getColumnIndex(COL_TariffCode)));
					CustDetails.setmTarifString(cr.getString(cr.getColumnIndex(COL_TarifString)));     
					CustDetails.setmTaxAmt(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TaxAmt))));
					CustDetails.setmTaxFlag(cr.getString(cr.getColumnIndex(COL_TaxFlag)));
					CustDetails.setmTcName(cr.getString(cr.getColumnIndex(COL_TcName)));
					CustDetails.setmTEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TEc))));
					CustDetails.setmTFc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TFc))));
					CustDetails.setmThirtyFlag(cr.getString(cr.getColumnIndex(COL_ThirtyFlag)));
					CustDetails.setmTourplanId(cr.getString(cr.getColumnIndex(COL_TourplanId)));
					CustDetails.setmTvmMtr(cr.getString(cr.getColumnIndex(COL_TvmMtr)));
					CustDetails.setmTvmPFtype(cr.getString(cr.getColumnIndex(COL_TvmPFtype)));
					//CustDetails.setmUIDR_Slab(cr.getInt(cr.getColumnIndex(COL_UID_Slab)));
					CustDetails.setmUnits(cr.getString(cr.getColumnIndex(COL_Units)));

					CustDetails.setMmUID(cr.getInt(cr.getColumnIndex(COL_UID_S_BcData)));//punit 25022014
					CustDetails.setmMeter_Present_Flag(cr.getString(cr.getColumnIndex(COL_METER_PRESENT_FLAG)) == null ? 0 : cr.getInt(cr.getColumnIndex(COL_METER_PRESENT_FLAG)));
					CustDetails.setmMtr_Not_Visible(cr.getString(cr.getColumnIndex(COL_MTR_NOT_VISIBLE)) == null ? 0 : cr.getInt(cr.getColumnIndex(COL_MTR_NOT_VISIBLE)));

					CustDetails.setmDLTEc_GoK(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_DLTEc_GoK))));//31-12-2014
					CustDetails.setmOldTecBill(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_OldTecBill))));//Nitish on 21-04-2016

					//30-07-2015
					CustDetails.setmAadharNo(cr.getString(cr.getColumnIndex(COL_AadharNo)));
					CustDetails.setmVoterIdNo(cr.getString(cr.getColumnIndex(COL_VoterIdNo)));				
					CustDetails.setmMtrMakeFlag(cr.getInt(cr.getColumnIndex(COL_MtrMakeFlag)));
					CustDetails.setmMtrBodySealFlag(cr.getInt(cr.getColumnIndex(COL_MtrBodySealFlag)));
					CustDetails.setmMtrTerminalCoverFlag(cr.getInt(cr.getColumnIndex(COL_MtrTerminalCoverFlag)));
					CustDetails.setmMtrTerminalCoverSealedFlag(cr.getInt(cr.getColumnIndex(COL_MtrTerminalCoverSealedFlag)));

					CustDetails.setmFACValue(cr.getString(cr.getColumnIndex(COL_FACValue)));//19-12-2015
					//CustDetails.setmUID(cr.getInt(cr.getColumnIndex(COL_UID_Slab)));
					//CustDetails.setMmConnectionNo(cr.getString(cr.getColumnIndex(COL_ConnectionNo_Slab)));
					//CustDetails.setMmRRNo(cr.getString(cr.getColumnIndex(COL_RRNo_Slab)));
					CustDetails.setMmECRate_Count(cr.getInt(cr.getColumnIndex(COL_ECRate_Count)));
					CustDetails.setMmECRate_Row(cr.getInt(cr.getColumnIndex(COL_ECRate_Row)));
					CustDetails.setMmFCRate_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FCRate_1))));
					CustDetails.setMmCOL_FCRate_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FCRate_2))));
					CustDetails.setMmUnits_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_1))));
					CustDetails.setMmUnits_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_2))));
					CustDetails.setMmUnits_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_3))));
					CustDetails.setMmUnits_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_4))));
					CustDetails.setMmUnits_5(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_5))));
					CustDetails.setMmUnits_6(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_6))));
					CustDetails.setMmEC_Rate_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_1))));
					CustDetails.setMmEC_Rate_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_2))));
					CustDetails.setMmEC_Rate_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_3))));
					CustDetails.setMmEC_Rate_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_4))));
					CustDetails.setMmEC_Rate_5(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_5))));
					CustDetails.setMmEC_Rate_6(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_6))));
					CustDetails.setMmEC_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_1))));
					CustDetails.setMmEC_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_2))));
					CustDetails.setMmEC_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_3))));
					CustDetails.setMmEC_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_4))));
					CustDetails.setMmEC_5(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_5))));
					CustDetails.setMmEC_6(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_6))));
					CustDetails.setMmFC_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_1))));
					CustDetails.setMmFC_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_2))));
					CustDetails.setMmTEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TEc_Slab))));
					CustDetails.setMmEC_FL_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_1))));
					CustDetails.setMmEC_FL_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_2))));
					CustDetails.setMmEC_FL_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_3))));
					CustDetails.setMmEC_FL_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_4))));
					CustDetails.setMmEC_FL(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL))));
					CustDetails.setMmnew_TEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_new_TEc))));
					CustDetails.setMmold_TEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_old_TEc))));


					//23-06-2021 for FC
					CustDetails.setMmNEWFC_UNIT1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT1))));
					CustDetails.setMmNEWFC_UNIT2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT2))));
					CustDetails.setMmNEWFC_UNIT3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT3))));
					CustDetails.setMmNEWFC_UNIT4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT4))));
					CustDetails.setMmNEWFC_Rate3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_Rate3))));
					CustDetails.setMmNEWFC_Rate4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_Rate4))));
					CustDetails.setMmNEWFC3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC3))));
					CustDetails.setMmNEWFC4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC4))));
					
					CustDetails.setmFC_Slab_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_Slab_3))));
					
				}//Cursor IF END
			} catch (Exception e) {
				// TODO: handle exception
				Log.d("punit", e.toString());
				throw e;
			}
			finally
			{
				if(cr!=null)
				{
					cr.close();
				}
			}
		}
		//26-05-2021 Disconnection For Arrears greater than 10
		public void GetNextDisc(String custId ) throws Exception
		{
			ReadSlabNTarifSbmBillCollection CustDetails = BillingObject.GetBillingObject();

			cr=null ;
			try
			{
				QueryParameters qParam = StoredProcedure.GetNextDisc(custId);
				cr = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
				if(cr.getCount() > 0)//Cursor IF Start
				{
					//for(int i=1; i<=cr.getCount();++i)
					//{

					//if(i==1){ 
					cr.moveToFirst();



					CustDetails.setmAbFlag(cr.getString(cr.getColumnIndex(COL_AbFlag)));
					CustDetails.setmAccdRdg(cr.getString(cr.getColumnIndex(COL_AccdRdg)));
					CustDetails.setmAccdRdg_rtn(cr.getString(cr.getColumnIndex(COL_AccdRdg_rtn)));
					CustDetails.setmAddress1(cr.getString(cr.getColumnIndex(COL_Address1)));
					CustDetails.setmAddress2(cr.getString(cr.getColumnIndex(COL_Address2)));
					CustDetails.setmApplicationNo(cr.getString(cr.getColumnIndex(COL_ApplicationNo)));
					CustDetails.setmArears(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Arears))));
					CustDetails.setmArrearsOld(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ArrearsOld))));
					CustDetails.setmAvgCons(cr.getString(cr.getColumnIndex(COL_AvgCons)));
					CustDetails.setmBankID(cr.getInt(cr.getColumnIndex(COL_BankID)));
					CustDetails.setmBatch_No(cr.getString(cr.getColumnIndex(COL_Batch_No)));
					CustDetails.setmBillable(cr.getString(cr.getColumnIndex(COL_Billable))); 
					CustDetails.setmBillDate(cr.getString(cr.getColumnIndex(COL_BillDate)));
					CustDetails.setmBillFor(cr.getString(cr.getColumnIndex(COL_BillFor)));
					CustDetails.setmBillNo(cr.getString(cr.getColumnIndex(COL_BillNo)));
					CustDetails.setmBillTotal(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_BillTotal))));
					CustDetails.setmBjKj2Lt2(cr.getString(cr.getColumnIndex(COL_BjKj2Lt2)));
					CustDetails.setmBlCnt(cr.getString(cr.getColumnIndex(COL_BlCnt)));
					CustDetails.setmBOBilled_Amount(cr.getString(cr.getColumnIndex(COL_BOBilled_Amount)));
					CustDetails.setmBOBillFlag(cr.getInt(cr.getColumnIndex(COL_BOBillFlag)));
					CustDetails.setmCapReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_CapReb))));
					CustDetails.setmChargetypeID(cr.getInt(cr.getColumnIndex(COL_ChargetypeID)));
					CustDetails.setmChequeDDDate(cr.getString(cr.getColumnIndex(COL_ChequeDDDate)));
					CustDetails.setmChequeDDNo(cr.getInt(cr.getColumnIndex(COL_ChequeDDNo)));
					CustDetails.setmConnectionNo(cr.getString(cr.getColumnIndex(COL_ConnectionNo)));
					CustDetails.setmConsPayable(cr.getString(cr.getColumnIndex(COL_ConsPayable)));
					CustDetails.setmCustomerName(cr.getString(cr.getColumnIndex(COL_CustomerName)));
					CustDetails.setmDateTime(cr.getString(cr.getColumnIndex(COL_DateTime)));
					CustDetails.setmDayWise_Flag(cr.getString(cr.getColumnIndex(COL_DayWise_Flag)));
					CustDetails.setmDemandChrg(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_DemandChrg))));
					CustDetails.setmDLAvgMin(cr.getString(cr.getColumnIndex(COL_DLAvgMin)));
					CustDetails.setmDlCount(cr.getInt(cr.getColumnIndex(COL_DlCount)));
					CustDetails.setmDlCount(cr.getInt(cr.getColumnIndex(COL_DlCount)));
					CustDetails.setmDLTEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_DLTEc))));
					CustDetails.setmDPdate(cr.getString(cr.getColumnIndex(COL_DPdate)));
					CustDetails.setmDrFees(cr.getString(cr.getColumnIndex(COL_DrFees)));
					CustDetails.setmDueDate(cr.getString(cr.getColumnIndex(COL_DueDate)));
					CustDetails.setmECReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ECReb))));
					CustDetails.setmEcsFlag(cr.getString(cr.getColumnIndex(COL_EcsFlag)));
					CustDetails.setmExLoad(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ExLoad))));
					CustDetails.setmFC_Slab_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_Slab_2))));
					CustDetails.setmFixedCharges(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FixedCharges))));
					CustDetails.setmFLReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FLReb))));
					CustDetails.setmForMonth(cr.getString(cr.getColumnIndex(COL_ForMonth)));
					CustDetails.setmGoKArrears(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_GoKArrears))));
					CustDetails.setmGoKPayable(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_GoKPayable))));
					CustDetails.setmGprs_Flag(cr.getInt(cr.getColumnIndex(COL_Gprs_Flag)));
					CustDetails.setmGps_Latitude_image(cr.getString(cr.getColumnIndex(COL_Gps_Latitude_image)));
					CustDetails.setmGps_Latitude_print(cr.getString(cr.getColumnIndex(COL_Gps_Latitude_print)));
					CustDetails.setmGps_LatitudeCardinal_image(cr.getString(cr.getColumnIndex(COL_Gps_LatitudeCardinal_image)));
					CustDetails.setmGps_LatitudeCardinal_print(cr.getString(cr.getColumnIndex(COL_Gps_LatitudeCardinal_print)));
					CustDetails.setmGps_Longitude_image(cr.getString(cr.getColumnIndex(COL_Gps_Longitude_image)));
					CustDetails.setmGps_Longitude_print(cr.getString(cr.getColumnIndex(COL_Gps_Longitude_print)));
					CustDetails.setmGps_LongitudeCardinal_image(cr.getString(cr.getColumnIndex(COL_Gps_LongitudeCardinal_image)));
					CustDetails.setmGps_LatitudeCardinal_print(cr.getString(cr.getColumnIndex(COL_Gps_LatitudeCardinal_print)));
					CustDetails.setmGvpId(cr.getString(cr.getColumnIndex(COL_GvpId)));
					CustDetails.setmHCReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_HCReb))));
					CustDetails.setmHLReb(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_HLReb))));
					CustDetails.setmHWCReb(cr.getString(cr.getColumnIndex(COL_HWCReb)));
					CustDetails.setmImage_Cap_Date(cr.getString(cr.getColumnIndex(COL_Image_Cap_Date)));
					CustDetails.setmImage_Cap_Time(cr.getString(cr.getColumnIndex(COL_Image_Cap_Time)));
					CustDetails.setmImage_Name(cr.getString(cr.getColumnIndex(COL_Image_Name)));
					CustDetails.setmImage_Path(cr.getString(cr.getColumnIndex(COL_Image_Path)));
					CustDetails.setmIntrst_Unpaid(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Intrst_Unpaid))));
					CustDetails.setmIntrstCurnt(cr.getString(cr.getColumnIndex(COL_IntrstCurnt)));
					CustDetails.setmIntrstOld(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_IntrstOld))));
					CustDetails.setmIODD11_Remarks(cr.getString(cr.getColumnIndex(COL_IODD11_Remarks)));
					CustDetails.setmIODRemarks(cr.getString(cr.getColumnIndex(COL_IODRemarks)));
					CustDetails.setmIssueDateTime(cr.getString(cr.getColumnIndex(COL_IssueDateTime)));
					CustDetails.setmKVA_Consumption(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_KVA_Consumption))));
					CustDetails.setmKVAAssd_Cons(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_KVAAssd_Cons))));
					CustDetails.setmKVAFR(cr.getString(cr.getColumnIndex(COL_KVAFR)));
					CustDetails.setmKVAH_OldConsumption(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_KVAFR))));
					CustDetails.setmKVAIR(cr.getString(cr.getColumnIndex(COL_KVAIR)));
					CustDetails.setmLatitude(cr.getString(cr.getColumnIndex(COL_Latitude)));
					CustDetails.setmLatitude_Dir(cr.getString(cr.getColumnIndex(COL_Latitude_Dir)));
					CustDetails.setmLegFol(cr.getString(cr.getColumnIndex(COL_LegFol)));
					CustDetails.setmLinMin(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_LinMin))));
					CustDetails.setmLocationCode(cr.getString(cr.getColumnIndex(COL_LocationCode)));
					CustDetails.setmLongitude(cr.getString(cr.getColumnIndex(COL_Longitude)));
					CustDetails.setmLongitude_Dir(cr.getString(cr.getColumnIndex(COL_Longitude_Dir)));
					CustDetails.setmMCHFlag(cr.getString(cr.getColumnIndex(COL_MCHFlag)));
					CustDetails.setmMeter_serialno(cr.getString(cr.getColumnIndex(COL_Meter_serialno)));
					CustDetails.setmMeter_type(cr.getString(cr.getColumnIndex(COL_Meter_type)));
					CustDetails.setmMF(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_MF))));
					CustDetails.setmMobileNo(cr.getString(cr.getColumnIndex(COL_MobileNo)));
					CustDetails.setmMtd(cr.getString(cr.getColumnIndex(COL_Mtd)));
					CustDetails.setmMtrDisFlag(cr.getInt(cr.getColumnIndex(COL_MtrDisFlag)));
					CustDetails.setmNewNoofDays(cr.getString(cr.getColumnIndex(COL_NewNoofDays)));
					CustDetails.setmNoOfDays(cr.getString(cr.getColumnIndex(COL_NoOfDays)));
					CustDetails.setmOld_Consumption(cr.getString(cr.getColumnIndex(COL_Old_Consumption)));
					CustDetails.setmOldConnID(cr.getString(cr.getColumnIndex(COL_OldConnID)));
					CustDetails.setmOtherChargeLegend(cr.getString(cr.getColumnIndex(COL_OtherChargeLegend)));
					CustDetails.setmOthers(cr.getString(cr.getColumnIndex(COL_Others)));
					CustDetails.setmPaid_Amt(cr.getInt(cr.getColumnIndex(COL_Paid_Amt)));
					CustDetails.setmPayment_Mode(cr.getString(cr.getColumnIndex(COL_Payment_Mode)));
					CustDetails.setmPenExLd(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_PenExLd))));
					CustDetails.setmPF(cr.getString(cr.getColumnIndex(COL_PF)));
					CustDetails.setmPfPenAmt(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_PfPenAmt))));
					CustDetails.setmPreRead(cr.getString(cr.getColumnIndex(COL_PreRead)));
					CustDetails.setmPreStatus(cr.getString(cr.getColumnIndex(COL_PreStatus)));//current status
					CustDetails.setmPrevRdg(cr.getString(cr.getColumnIndex(COL_PrevRdg)));
					CustDetails.setmRcptCnt(cr.getInt(cr.getColumnIndex(COL_RcptCnt)));
					CustDetails.setmReaderCode(cr.getString(cr.getColumnIndex(COL_ReaderCode)));
					CustDetails.setmReadingDay(cr.getString(cr.getColumnIndex(COL_ReadingDay)));
					CustDetails.setmRebateFlag(cr.getString(cr.getColumnIndex(COL_RebateFlag)));
					CustDetails.setmReceipt_No(cr.getInt(cr.getColumnIndex(COL_Receipt_No)));
					CustDetails.setmReceiptAmnt(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_ReceiptAmnt))));
					CustDetails.setmReceiptDate(cr.getString(cr.getColumnIndex(COL_ReceiptDate)));
					CustDetails.setmReceipttypeflag(cr.getString(cr.getColumnIndex(COL_Receipttypeflag)));
					CustDetails.setmRecordDmnd(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_RecordDmnd))));
					CustDetails.setmRemarks(cr.getString(cr.getColumnIndex(COL_Remarks)));
					CustDetails.setmRRFlag(cr.getString(cr.getColumnIndex(COL_RRFlag)));
					CustDetails.setmRRNo(cr.getString(cr.getColumnIndex(COL_RRNo)));
					CustDetails.setmSancHp(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SancHp))));
					CustDetails.setmSancKw(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SancKw))));
					CustDetails.setmSancLoad(cr.getString(cr.getColumnIndex(COL_SancLoad)));
					CustDetails.setmSBMNumber(cr.getString(cr.getColumnIndex(COL_SBMNumber)));
					CustDetails.setmSectionName(cr.getString(cr.getColumnIndex(COL_SectionName)));
					CustDetails.setmSlowRtnPge(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_SlowRtnPge))));
					CustDetails.setmSpotSerialNo(cr.getInt(cr.getColumnIndex(COL_SpotSerialNo)));
					CustDetails.setmStatus(cr.getInt(cr.getColumnIndex(COL_Status)));//previous status
					CustDetails.setmSubId(cr.getString(cr.getColumnIndex(COL_SubId)));
					CustDetails.setmSupply_Points(cr.getInt(cr.getColumnIndex(COL_Supply_Points)));
					CustDetails.setmTariffCode(cr.getInt(cr.getColumnIndex(COL_TariffCode)));
					CustDetails.setmTarifString(cr.getString(cr.getColumnIndex(COL_TarifString)));     
					CustDetails.setmTaxAmt(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TaxAmt))));
					CustDetails.setmTaxFlag(cr.getString(cr.getColumnIndex(COL_TaxFlag)));
					CustDetails.setmTcName(cr.getString(cr.getColumnIndex(COL_TcName)));
					CustDetails.setmTEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TEc))));
					CustDetails.setmTFc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TFc))));
					CustDetails.setmThirtyFlag(cr.getString(cr.getColumnIndex(COL_ThirtyFlag)));
					CustDetails.setmTourplanId(cr.getString(cr.getColumnIndex(COL_TourplanId)));
					CustDetails.setmTvmMtr(cr.getString(cr.getColumnIndex(COL_TvmMtr)));
					CustDetails.setmTvmPFtype(cr.getString(cr.getColumnIndex(COL_TvmPFtype)));
					//CustDetails.setmUIDR_Slab(cr.getInt(cr.getColumnIndex(COL_UID_Slab)));
					CustDetails.setmUnits(cr.getString(cr.getColumnIndex(COL_Units)));

					CustDetails.setMmUID(cr.getInt(cr.getColumnIndex(COL_UID_S_BcData)));//punit 25022014
					CustDetails.setmMeter_Present_Flag(cr.getString(cr.getColumnIndex(COL_METER_PRESENT_FLAG)) == null ? 0 : cr.getInt(cr.getColumnIndex(COL_METER_PRESENT_FLAG)));
					CustDetails.setmMtr_Not_Visible(cr.getString(cr.getColumnIndex(COL_MTR_NOT_VISIBLE)) == null ? 0 : cr.getInt(cr.getColumnIndex(COL_MTR_NOT_VISIBLE)));

					CustDetails.setmDLTEc_GoK(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_DLTEc_GoK))));//31-12-2014
					CustDetails.setmOldTecBill(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_OldTecBill))));//Nitish on 21-04-2016

					//30-07-2015
					CustDetails.setmAadharNo(cr.getString(cr.getColumnIndex(COL_AadharNo)));
					CustDetails.setmVoterIdNo(cr.getString(cr.getColumnIndex(COL_VoterIdNo)));				
					CustDetails.setmMtrMakeFlag(cr.getInt(cr.getColumnIndex(COL_MtrMakeFlag)));
					CustDetails.setmMtrBodySealFlag(cr.getInt(cr.getColumnIndex(COL_MtrBodySealFlag)));
					CustDetails.setmMtrTerminalCoverFlag(cr.getInt(cr.getColumnIndex(COL_MtrTerminalCoverFlag)));
					CustDetails.setmMtrTerminalCoverSealedFlag(cr.getInt(cr.getColumnIndex(COL_MtrTerminalCoverSealedFlag)));

					CustDetails.setmFACValue(cr.getString(cr.getColumnIndex(COL_FACValue)));//19-12-2015
					//CustDetails.setmUID(cr.getInt(cr.getColumnIndex(COL_UID_Slab)));
					//CustDetails.setMmConnectionNo(cr.getString(cr.getColumnIndex(COL_ConnectionNo_Slab)));
					//CustDetails.setMmRRNo(cr.getString(cr.getColumnIndex(COL_RRNo_Slab)));
					CustDetails.setMmECRate_Count(cr.getInt(cr.getColumnIndex(COL_ECRate_Count)));
					CustDetails.setMmECRate_Row(cr.getInt(cr.getColumnIndex(COL_ECRate_Row)));
					CustDetails.setMmFCRate_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FCRate_1))));
					CustDetails.setMmCOL_FCRate_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FCRate_2))));
					CustDetails.setMmUnits_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_1))));
					CustDetails.setMmUnits_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_2))));
					CustDetails.setMmUnits_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_3))));
					CustDetails.setMmUnits_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_4))));
					CustDetails.setMmUnits_5(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_5))));
					CustDetails.setMmUnits_6(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_Units_6))));
					CustDetails.setMmEC_Rate_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_1))));
					CustDetails.setMmEC_Rate_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_2))));
					CustDetails.setMmEC_Rate_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_3))));
					CustDetails.setMmEC_Rate_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_4))));
					CustDetails.setMmEC_Rate_5(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_5))));
					CustDetails.setMmEC_Rate_6(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_Rate_6))));
					CustDetails.setMmEC_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_1))));
					CustDetails.setMmEC_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_2))));
					CustDetails.setMmEC_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_3))));
					CustDetails.setMmEC_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_4))));
					CustDetails.setMmEC_5(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_5))));
					CustDetails.setMmEC_6(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_6))));
					CustDetails.setMmFC_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_1))));
					CustDetails.setMmFC_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_2))));
					CustDetails.setMmTEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_TEc_Slab))));
					CustDetails.setMmEC_FL_1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_1))));
					CustDetails.setMmEC_FL_2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_2))));
					CustDetails.setMmEC_FL_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_3))));
					CustDetails.setMmEC_FL_4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL_4))));
					CustDetails.setMmEC_FL(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_EC_FL))));
					CustDetails.setMmnew_TEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_new_TEc))));
					CustDetails.setMmold_TEc(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_old_TEc))));
					
					//23-06-2021 for FC
					CustDetails.setMmNEWFC_UNIT1(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT1))));
					CustDetails.setMmNEWFC_UNIT2(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT2))));
					CustDetails.setMmNEWFC_UNIT3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT3))));
					CustDetails.setMmNEWFC_UNIT4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_UNIT4))));
					CustDetails.setMmNEWFC_Rate3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_Rate3))));
					CustDetails.setMmNEWFC_Rate4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC_Rate4))));
					CustDetails.setMmNEWFC3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC3))));
					CustDetails.setMmNEWFC4(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_NEWFC4))));
					
					CustDetails.setmFC_Slab_3(new BigDecimal(cr.getDouble(cr.getColumnIndex(COL_FC_Slab_3))));

				}//Cursor IF END
			} catch (Exception e) {
				// TODO: handle exception
				Log.d("punit", e.toString());
				throw e;
			}
			finally
			{
				if(cr!=null)
				{
					cr.close();
				}
			}
		}

}
