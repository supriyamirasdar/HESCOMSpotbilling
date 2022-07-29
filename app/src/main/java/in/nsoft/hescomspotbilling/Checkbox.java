package in.nsoft.hescomspotbilling;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.net.ParseException;
import android.util.Log;

//Naveen 11-03-2014
public class Checkbox {

	ReadSlabNTarifSbmBillCollection billingobj ;//= BillingObject.GetBillingObject();

	/*struct variables*/
	String 		rtDayWiseFlag		;//= 	billingobj.getmDayWise_Flag();
	String 		rtNewNoOfDays		;//= 	billingobj.getmNewNoofDays();
	String 		rtNoOfDays 			;//= 	billingobj.getmNoOfDays();
	String 		rtkVAFR 			;//= 	billingobj.getmKVAFR();
	String 		rtcons				;//=	billingobj.getmUnits();
	String 		rtTvmMtr			;//=	billingobj.getmTvmMtr();
	String 		rtPF1				;//=	billingobj.getmPF();
	String 		rtThirtyFlag 		;//= 	billingobj.getmThirtyFlag();
	String 		rtMtd 				;//= 	billingobj.getmMtd();	
	String 		rtPrestatus			;//=	billingobj.getmPreStatus();
	int 		rtSupplyPoints 		;//=	billingobj.getmSupply_Points();
	int 		rtdlcount			;//=	billingobj.getmDlCount();
	BigDecimal 	rtKVAAssd_Cons 		;//= 	billingobj.getmKVAAssd_Cons();
	BigDecimal 	rtKVA_Consumption 	;//= 	billingobj.getmKVA_Consumption();		
	BigDecimal 	rtLineM				;//=	billingobj.getmLinMin();
	BigDecimal	rtDemandChrg		;//=	billingobj.getmDemandChrg();
	BigDecimal	rtFC_Slab_2			;//=	billingobj.getmFC_Slab_2();
	BigDecimal	rtTFc				;//=	billingobj.getmTFc();
	BigDecimal	rtFC				;//=	billingobj.getmFixedCharges();
	BigDecimal 	Units_1				;//=	billingobj.getMmUnits_1();
	BigDecimal 	Units_2				;//=	billingobj.getMmUnits_2();
	BigDecimal 	Units_3				;//=	billingobj.getMmUnits_3();
	BigDecimal 	Units_4				;//=	billingobj.getMmUnits_4();
	BigDecimal 	Units_5				;//=	billingobj.getMmUnits_5();
	BigDecimal 	Units_6				;//=	billingobj.getMmUnits_6();		
	BigDecimal 	EC_1				;//=	billingobj.getMmEC_1();
	BigDecimal 	EC_2				;//=	billingobj.getMmEC_2();
	BigDecimal 	EC_3				;//=	billingobj.getMmEC_3();
	BigDecimal 	EC_4				;//=	billingobj.getMmEC_4();
	BigDecimal 	EC_5				;//=	billingobj.getMmEC_5();
	BigDecimal 	EC_6				;//=	billingobj.getMmEC_6();
	BigDecimal	EC_Rate_1			;//=	billingobj.getMmEC_Rate_1();
	BigDecimal	EC_Rate_2			;//=	billingobj.getMmEC_Rate_2();
	BigDecimal	EC_Rate_3			;//=	billingobj.getMmEC_Rate_3();
	BigDecimal	EC_Rate_4			;//=	billingobj.getMmEC_Rate_4();
	BigDecimal	EC_Rate_5			;//=	billingobj.getMmEC_Rate_5();
	BigDecimal	EC_Rate_6			;//=	billingobj.getMmEC_Rate_6();		
	BigDecimal	EC_FL				;//=	billingobj.getMmEC_FL();
	BigDecimal	EC_FL_1				;//=	billingobj.getMmEC_FL_1();
	BigDecimal	EC_FL_2				;//=	billingobj.getMmEC_FL_2();
	BigDecimal	EC_FL_3				;//=	billingobj.getMmEC_FL_3();
	BigDecimal	EC_FL_4				;//=	billingobj.getMmEC_FL_4();
	BigDecimal 	rtTEc				;//=	billingobj.getmTEc();		
	BigDecimal 	rtEcReb				;//=	billingobj.getmECReb();
	String 		rtRebFlag			;//=	billingobj.getmRebateFlag();
	BigDecimal 	rtHCReb				;//=	billingobj.getmHCReb();
	BigDecimal 	rtHLReb				;//=	billingobj.getmHLReb();
	BigDecimal 	rtCapReb			;//=	billingobj.getmCapReb();
	BigDecimal	rtFlReb				;//=	billingobj.getmFLReb();
	String 		rtHWCRebFlag		;//=	billingobj.getmHWCReb();		
	BigDecimal	rtTaxAmt			;//=	billingobj.getmTaxAmt();
	String 		rtTax				;//=	billingobj.getmTaxFlag();
	BigDecimal	rtGoKPayable		;//=	billingobj.getmGoKPayable();
	BigDecimal	rtDLTEc				;//=	billingobj.getmDLTEc();
	BigDecimal	rtDLTEc_GoK			;//=	billingobj.getDLTEc_GoK(); //Nitish 31-12-2014
	BigDecimal	rtPfPenAmt			;//=	billingobj.getmPfPenAmt();
	String 		rtkVAIR				;//=	billingobj.getmKVAIR();	
	BigDecimal	rtExLoad			;//=	billingobj.getmExLoad();
	BigDecimal	rtPenExLd			;//=	billingobj.getmPenExLd();
	String 		rtBillFor			;//=	billingobj.getmBillFor();
	String 		rtBjKj2Lt2			;//=	billingobj.getmBjKj2Lt2();
	BigDecimal	rtRecordDmnd		;//=	billingobj.getmRecordDmnd();
	BigDecimal	rtBillTotal			;
	String		rtDrFees_str		;
	String		rtOthers_str		;
	BigDecimal	rtArears			;
	String		rtIntrstCurnt_str	;
	BigDecimal	rtArearsUnpaid		;
	BigDecimal	rtIntrstUnpaid		;
	BigDecimal	rtIntrstOld			;
	BigDecimal	rtGoKArrears		;

	final static String TAG="in.nsoft.spotbilling.EC_Calculation";
	private boolean ConsPayable;//=false;
	private BigDecimal Mul, ir1, nou ;//= new BigDecimal(rtcons);
	private BigDecimal ir2 ;//Nitish 31-12-2014
	private int Row;
	private char FunctionCallFlag;
	BigDecimal	mtrcnst	;//=	billingobj.getmMF();		
	BigDecimal	Shp	;//=	billingobj.getmSancHp();
	BigDecimal	Skw	;//=	billingobj.getmSancKw();
	int tempf9 ;//= billingobj.getmTariffCode();
	int Status ;//= Integer.parseInt(rtPrestatus);

	//19-03-14
	BigDecimal md;
	//13-03-14
	BigDecimal[][] maintarif ;//= new BigDecimal[50][23];
	BigDecimal floattemp ;//= new BigDecimal(0.00);
	BigDecimal ptemp ;//= new BigDecimal(0.00);
	BigDecimal load ;//= new BigDecimal(0.00);
	BigDecimal record_demand ;//= new BigDecimal(0.00);
	BigDecimal Contractdemand ;//= new BigDecimal(0.00);
	int NoSlab, i, nodys;
	char[][] TarifStr ;//= new char[50][30];
	char[] buffer ;//= new char[200];

	//MACRO
	int DUEDAYS ;//= 15;
	int RebMax ;//= 50;
	int RebMaxOld ;//= 40;
	int SLABRATE ;//= 12;
	BigDecimal TaxRate ;//= new BigDecimal(0.06);
	BigDecimal PfPenalty ;//= new BigDecimal(0.02);
	BigDecimal RebRate ;//= new BigDecimal(0.50);
	BigDecimal RebRateOld ;//= new BigDecimal(0.40);

	private Context mcx;


	//Nitish 21-04-2015
	int PrvStatus ;

	// Old and New Tarif Diff Commented  28-12-2020
	/*********************Nitish Start Old and New Tarif Diff 13-11-2020************************************/	
	/*int ecrate_count=0;
	int ecrate_row = 0;	
	int next_tariff=0;
	BigDecimal TEc = new BigDecimal(0);
	BigDecimal old_TEc = new BigDecimal(0);
	BigDecimal new_TEc = new BigDecimal(0);
	BigDecimal old_nou = new BigDecimal(0);
	BigDecimal new_nou = new BigDecimal(0);	
	BigDecimal pold_TEc = new BigDecimal(0);	*/
	/*********************Nitish Start Old and New Tarif Diff End 13-11-2020************************************/
	//17-03-14
	public Checkbox(Context cx)	{
		try	
		{
			this.mcx = cx;
			billingobj = BillingObject.GetBillingObject();

			/*struct variables*/
			rtDayWiseFlag		= 	billingobj.getmDayWise_Flag();			
			rtNewNoOfDays		= 	billingobj.getmNewNoofDays();			
			rtNoOfDays 			= 	billingobj.getmNoOfDays();			
			rtkVAFR 			= 	billingobj.getmKVAFR();
			rtcons				=	billingobj.getmUnits();
			rtTvmMtr			=	billingobj.getmTvmMtr();
			rtPF1				=	billingobj.getmPF();
			rtThirtyFlag 		= 	billingobj.getmThirtyFlag();
			rtMtd 				= 	billingobj.getmMtd();
			rtPrestatus			=	billingobj.getmPreStatus();
			rtSupplyPoints 		=	billingobj.getmSupply_Points();
			rtdlcount			=	billingobj.getmDlCount();
			rtKVAAssd_Cons 		= 	billingobj.getmKVAAssd_Cons();
			rtKVA_Consumption 	= 	billingobj.getmKVA_Consumption();
			rtLineM				=	billingobj.getmLinMin();
			rtDemandChrg		=	billingobj.getmDemandChrg();
			rtFC_Slab_2			=	billingobj.getmFC_Slab_2();
			rtTFc				=	billingobj.getmTFc();
			rtFC				=	billingobj.getmFixedCharges();
			Units_1				=	billingobj.getMmUnits_1();
			Units_2				=	billingobj.getMmUnits_2();
			Units_3				=	billingobj.getMmUnits_3();
			Units_4				=	billingobj.getMmUnits_4();
			Units_5				=	billingobj.getMmUnits_5();
			Units_6				=	billingobj.getMmUnits_6();
			EC_1				=	billingobj.getMmEC_1();
			EC_2				=	billingobj.getMmEC_2();
			EC_3				=	billingobj.getMmEC_3();
			EC_4				=	billingobj.getMmEC_4();
			EC_5				=	billingobj.getMmEC_5();
			EC_6				=	billingobj.getMmEC_6();		
			EC_Rate_1			=	billingobj.getMmEC_Rate_1();
			EC_Rate_2			=	billingobj.getMmEC_Rate_2();
			EC_Rate_3			=	billingobj.getMmEC_Rate_3();
			EC_Rate_4			=	billingobj.getMmEC_Rate_4();
			EC_Rate_5			=	billingobj.getMmEC_Rate_5();
			EC_Rate_6			=	billingobj.getMmEC_Rate_6();
			EC_FL				=	billingobj.getMmEC_FL();	
			EC_FL_1				=	billingobj.getMmEC_FL_1();
			EC_FL_2				=	billingobj.getMmEC_FL_2();		
			EC_FL_3				=	billingobj.getMmEC_FL_3();
			EC_FL_4				=	billingobj.getMmEC_FL_4();
			rtTEc				=	billingobj.getmTEc();
			rtEcReb				=	billingobj.getmECReb();
			rtRebFlag			=	billingobj.getmRebateFlag();
			rtHCReb				=	billingobj.getmHCReb();
			rtHLReb				=	billingobj.getmHLReb();
			rtCapReb			=	billingobj.getmCapReb();
			rtFlReb				=	billingobj.getmFLReb();
			rtHWCRebFlag		=	billingobj.getmHWCReb();
			rtTaxAmt			=	billingobj.getmTaxAmt();
			rtTax				=	billingobj.getmTaxFlag();
			rtGoKPayable		=	billingobj.getmGoKPayable();
			rtDLTEc				=	billingobj.getmDLTEc();
			rtDLTEc_GoK			=	billingobj.getmDLTEc_GoK();//Nitish 31-12-2014
			rtPfPenAmt			=	billingobj.getmPfPenAmt();
			rtkVAIR				=	billingobj.getmKVAIR();
			rtExLoad			=	billingobj.getmExLoad();
			rtPenExLd			=	billingobj.getmPenExLd();
			rtBillFor			=	billingobj.getmBillFor();
			rtRecordDmnd		=	billingobj.getmRecordDmnd();
			//18-03-14
			rtDrFees_str		=	billingobj.getmDrFees();
			rtOthers_str		=	billingobj.getmOthers();
			rtArears			=	billingobj.getmArears();
			rtIntrstCurnt_str	=	billingobj.getmIntrstCurnt();	
			rtArearsUnpaid		=	billingobj.getmArrearsOld();
			rtIntrstUnpaid		=	billingobj.getmIntrst_Unpaid();
			rtIntrstOld			=	billingobj.getmIntrstOld();
			rtGoKArrears		=	billingobj.getmGoKArrears();

			//Nitish 27-04-2015
			PrvStatus = billingobj.getmStatus();

			ConsPayable=false;
			Mul = new BigDecimal(0);
			ir1 = new BigDecimal(0);
			ir2 = new BigDecimal(0);//Nitish 31-12-2014
			Row=0;
			FunctionCallFlag='0';

			//13-03-14
			maintarif = new BigDecimal[70][23]; //27-04-2017
			floattemp = new BigDecimal(0.00);
			ptemp = new BigDecimal(0.00);
			load = new BigDecimal(0.00);
			record_demand = new BigDecimal(0.00);
			Contractdemand = new BigDecimal(0.00);
			NoSlab=i=nodys=0;
			TarifStr = new char[70][30]; //27-04-2017
			buffer = new char[200];

			//26-03-14
			var_init();
			var_assign();
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	//18-03-14
	/**
	 * var_init() initializes variable values null to 0
	 */
	private void var_init()
	{
		Log.d(TAG, "var_init");

		if(rtDayWiseFlag==null)
			rtDayWiseFlag="N";
		if(rtNewNoOfDays==null)
			rtNewNoOfDays="0";
		if(rtNoOfDays==null)
			rtNoOfDays="0";
		if(rtkVAFR==null)
			rtkVAFR="0";
		if(rtcons==null)
			rtcons="0";
		if(rtTvmMtr==null)
			rtTvmMtr="0";
		if(rtPF1==null)
			rtPF1="0";
		if(rtThirtyFlag==null)
			rtThirtyFlag="0";
		if(rtMtd==null)
			rtMtd="0";
		if(rtPrestatus==null)
			rtPrestatus="0";
		if(rtKVAAssd_Cons.equals(null))
			rtKVAAssd_Cons=new BigDecimal(0.00);
		if(rtKVA_Consumption.equals(null))
			rtKVA_Consumption=new BigDecimal(0.00);
		if(rtLineM.equals(null))
			rtLineM=new BigDecimal(0.00);
		if(rtDemandChrg.equals(null))
			rtDemandChrg=new BigDecimal(0.00);
		if(rtFC_Slab_2.equals(null))
			rtFC_Slab_2=new BigDecimal(0.00);
		if(rtTFc.equals(null))
			rtTFc=new BigDecimal(0.00);
		if(rtFC.equals(null))
			rtFC=new BigDecimal(0.00);
		if(Units_1.equals(null))
			Units_1=new BigDecimal(0.00);
		if(Units_2.equals(null))
			Units_2=new BigDecimal(0.00);
		if(Units_3.equals(null))
			Units_3=new BigDecimal(0.00);
		if(Units_4.equals(null))
			Units_4=new BigDecimal(0.00);
		if(Units_5.equals(null))
			Units_5=new BigDecimal(0.00);
		if(Units_6.equals(null))
			Units_6=new BigDecimal(0.00);
		if(EC_1.equals(null))
			EC_1=new BigDecimal(0.00);
		if(EC_2.equals(null))
			EC_2=new BigDecimal(0.00);
		if(EC_3.equals(null))
			EC_3=new BigDecimal(0.00);
		if(EC_4.equals(null))
			EC_4=new BigDecimal(0.00);
		if(EC_5.equals(null))
			EC_5=new BigDecimal(0.00);
		if(EC_6.equals(null))
			EC_6=new BigDecimal(0.00);
		if(EC_Rate_1.equals(null))
			EC_Rate_1=new BigDecimal(0.00);
		if(EC_Rate_2.equals(null))
			EC_Rate_2=new BigDecimal(0.00);
		if(EC_Rate_3.equals(null))
			EC_Rate_3=new BigDecimal(0.00);
		if(EC_Rate_4.equals(null))
			EC_Rate_4=new BigDecimal(0.00);
		if(EC_Rate_5.equals(null))
			EC_Rate_5=new BigDecimal(0.00);
		if(EC_Rate_6.equals(null))
			EC_Rate_6=new BigDecimal(0.00);
		if(EC_FL.equals(null))
			EC_FL=new BigDecimal(0.00);		
		if(EC_FL_1.equals(null))
			EC_FL_1=new BigDecimal(0.00);
		if(EC_FL_2.equals(null))
			EC_FL_2=new BigDecimal(0.00);	
		if(EC_FL_3.equals(null))
			EC_FL_3=new BigDecimal(0.00);
		if(EC_FL_4.equals(null))
			EC_FL_4=new BigDecimal(0.00);
		if(rtTEc.equals(null))
			rtTEc=new BigDecimal(0.00);
		if(rtEcReb.equals(null))
			rtEcReb=new BigDecimal(0.00);
		if(rtRebFlag==null)
			rtRebFlag="N";
		if(rtHCReb.equals(null))
			rtHCReb=new BigDecimal(0.00);
		if(rtHLReb.equals(null))
			rtHLReb=new BigDecimal(0.00);
		if(rtCapReb.equals(null))
			rtCapReb=new BigDecimal(0.00);
		if(rtFlReb.equals(null))
			rtFlReb=new BigDecimal(0.00);
		if(rtHWCRebFlag==null)
			rtHWCRebFlag="NNN";
		if(rtTaxAmt.equals(null))
			rtTaxAmt=new BigDecimal(0.00);
		if(rtTax==null)
			rtTax="N";
		if(rtGoKPayable.equals(null))
			rtGoKPayable=new BigDecimal(0.00);
		if(rtDLTEc.equals(null))
			rtDLTEc=new BigDecimal(0.00);
		if(rtPfPenAmt.equals(null))
			rtPfPenAmt=new BigDecimal(0.00);
		if(rtkVAIR==null)
			rtkVAIR="0";
		if(rtExLoad.equals(null))
			rtExLoad=new BigDecimal(0.00);
		if(rtPenExLd.equals(null))
			rtPenExLd=new BigDecimal(0.00);
		if(rtBillFor==null)
			rtBillFor="0";
		if(rtBjKj2Lt2==null)
			rtBjKj2Lt2="0";
		if(rtRecordDmnd.equals(null))
			rtRecordDmnd=new BigDecimal(0.00);		
		if(rtArears.equals(null))
			rtArears=new BigDecimal(0.00);
		if(rtArearsUnpaid.equals(null))
			rtArearsUnpaid=new BigDecimal(0.00);
		if(rtIntrstUnpaid.equals(null))
			rtIntrstUnpaid=new BigDecimal(0.00);
		if(rtIntrstOld.equals(null))
			rtIntrstOld=new BigDecimal(0.00);
		if(rtGoKArrears.equals(null))
			rtGoKArrears=new BigDecimal(0.00);		
	}

	//18-03-14
	/**
	 * var_assign() assigns object values into local variables
	 */
	private void var_assign()
	{
		Log.d(TAG, "var_assign");

		//19-03-14
		md = new BigDecimal(0.00);

		nou = new BigDecimal(rtcons.trim());
		mtrcnst	=	billingobj.getmMF();
		if(mtrcnst.equals(null))
			mtrcnst=new BigDecimal(0);		
		Shp	=	billingobj.getmSancHp();
		if(Shp.equals(null))
			Shp=new BigDecimal(0);
		Skw	=	billingobj.getmSancKw();
		if(Skw.equals(null))
			Skw=new BigDecimal(0);
		tempf9 = billingobj.getmTariffCode();
		Status = Integer.parseInt(rtPrestatus.trim());

		DUEDAYS = 15;
		RebMax = 50;
		RebMaxOld = 40;
		SLABRATE = 12;
		TaxRate = new BigDecimal(0.06);
		PfPenalty = new BigDecimal(0.02);
		RebRate = new BigDecimal(0.50);
		RebRateOld = new BigDecimal(0.40);

		if(Skw.compareTo(new BigDecimal(0.00))==1)
			load = Skw;
		else if(Shp.compareTo(new BigDecimal(0.00))==1)
			load = Shp;
		else
			load = Skw;

		if(Skw.compareTo(new BigDecimal(0.00))==1)
			Contractdemand = Skw;
		else if(Shp.compareTo(new BigDecimal(0.00))==1)
			Contractdemand = Shp;
		else
			Contractdemand = Skw;
	}


	/**
	 * @return int
	 * cap_required() checks whether PF calculation is required or not, default PF value is 0.85
	 * return true  - PF calculation required
	 * return false - PF calculation not required
	 */
	public boolean pf_required()
	{			
		Log.d(TAG, "pf_required");

		BigDecimal pf_const, pf_current;
		pf_const = new BigDecimal(0.85);

		if ( Status == 0 || Status == 3 || Status == 4 || Status == 1 || Status == 2 || Status == 14) 
		{

			//09-08-2016 If Computed PF is present in file(other than 0.85 or .85) then PF Text Box should be disabled.
			if(!(rtPF1.toString().trim().equals("0.85")||rtPF1.toString().trim().equals(".85")))
			{
				pf_const = new BigDecimal(0.85);
				return false;
			}			
			//if ( (rtTvmMtr.equals("1") || rtTvmMtr.equals("2") || tempf9 >= 40) && (nou.compareTo(new BigDecimal(0))==1)) 
			//25-06-2015
			//if ( (rtTvmMtr.equals("1") || rtTvmMtr.equals("2") || rtTvmMtr.equals("4") || tempf9 >= 40) && (nou.compareTo(new BigDecimal(0))==1))
			if (tempf9 >= 40) //31-08-2016
			{
				pf_current = new BigDecimal(rtPF1);

				if((tempf9 == 100 || tempf9 == 101) && (pf_current.compareTo(pf_const)==-1))
					return false;
				else if( Status == 1 || Status == 2 || Status == 14)
					return false;
				else
					return true;
			}
			else
				return false;
		}
		else
			return false;
	}

	/**
	 * @return int
	 * md_required() checks whether MD calculation is required or not 
	 * return true  - MD calculation required
	 * return false - MD calculation not required
	 */
	public boolean md_required()
	{
		Log.d(TAG, "md_required");

		//if (( rtTvmMtr.equals("1") || tempf9==82 || tempf9==83) && ((Status == 0) || (Status == 3) || (Status == 4) ))
		//25-06-2015
		//if (( rtTvmMtr.equals("1") || rtTvmMtr.equals("4")) && ((Status == 0) || (Status == 3) || (Status == 4) ))   
		if ((Status == 0) || (Status == 3) || (Status == 4))  //30-08-2016
			return true;
		else
			return false;
	}

	/**
	 * @return int
	 * cap_required() checks whether Capacitor calculation is required or not
	 * return 2 - Charitable calculation required
	 * return 1 - Capacitor calculation required
	 * return 0 - Capacitor calculation not required
	 */
	/*public int cap_required()
	{
		Log.d(TAG, "cap_required");

		String C;
		try 
		{
			C = rtHWCRebFlag.substring(2, 1);

			if(C=="Y"&& (nou.compareTo(new BigDecimal(0))!=0) && ( tempf9 >= 60 && tempf9 <= 64))
			{
				if(Status != 1 && Status != 5 && Status != 6)
				{	// capacitor
					//rtCapReb = nou.multiply(new BigDecimal(0.02));	
					return 1;			
				}
				else
					return 0;
			}

			if(C=="Y" && (nou.compareTo(new BigDecimal(0))!=0) && ( tempf9 == 23 || tempf9 == 24))
			{	// charitable
				//rtCapReb = nou.multiply(new BigDecimal(0.25));
				return 2;
			}
			else
				return 0;
		} 
		catch (Exception e) 
		{
			Log.d(TAG, e.toString());
			return 0;
		}		
	}*/

	/**
	 * Checks the Tariff code from the Slab
	 */
	private void CheckSlab()
	{		
		Log.d(TAG, "CheckSlab");

		try
		{
			for(i=0;i<NoSlab;i++)
			{

				//next_tariff = maintarif[i+1][0]; //Original
				//Commented Old and New Tarif Diff 21-04-2016
				//next_tariff = Integer.valueOf(maintarif[i+1][0].intValue()); //Nitish  21-04-2016
				//next_tariff = Integer.valueOf(maintarif[i+1][0].intValue()); //commmented Nitish 28-12-2020
				if(maintarif[i][0].equals(new BigDecimal(tempf9)))
					break;
			}
		}
		catch(Exception e)
		{

		}
	}

	//14-03-14
	/**
	 * Reads Slab and Tariff from the TariffString object and splits rates and charges
	 */
	private void ReadSlabNTariff()
	{
		Log.d(TAG, "ReadSlabNTariff");

		int read_i=0,j=0,colj=0,chj=0,na=0,k=0,trf_cnt=0;
		char[] ref = new char[30];

		/*for ( read_i = 0; read_i < 50; read_i++ )
			for ( j = 0; j < 23; j++)
				maintarif[read_i][j] = new BigDecimal(0.00);*/

		//String tarif = billingobj.getmTarifString();

		try 
		{
			//19-03-14
			String ref1;
			read_i=trf_cnt=0;
			ReadSlabTariff objTarif =new ReadSlabTariff(mcx);
			List<String> listTariff=objTarif.getTariffName();
			NoSlab = listTariff.size();
			for(String tarif:listTariff)
			{
				for(na=2,k=0;tarif.charAt(na)!='#';na++,k++)
					TarifStr[trf_cnt][k] = tarif.charAt(na);
				trf_cnt++;

				na++;
				colj = 0;
				for ( j = 0; j < 30; j++)
					ref[j] = ' ';

				for ( chj = na, k = 0; tarif.charAt(chj) != '$'; chj++, k++ )
				{			
					if ( tarif.charAt(chj) != '~' && tarif.charAt(chj) != '#' && tarif.charAt(chj) != '@' )
						ref[k] = tarif.charAt(chj);
					else
					{
						ref1 = String.valueOf(ref).trim();
						ref[k] = '\0';
						if ( tarif.charAt(chj) == '~' )
						{	
							maintarif[read_i][colj++] = new BigDecimal(ref1);
							chj++;

							if (tarif.charAt(chj) == '@')
							{
								chj++;
								colj = 11;
							}
						}
						else if ( tarif.charAt(chj) == '@')
						{
							maintarif[read_i][colj++] = new BigDecimal(ref1);
							chj++;
							colj = 11;
						}
						else
							maintarif[read_i][colj++] = new BigDecimal(ref1);
						k = -1;
						for ( j = 0; j < 30; j++)
							ref[j] = ' ';
					}				
				}
				ref1 = String.valueOf(ref).trim();
				maintarif[read_i][colj] = new BigDecimal(ref1);
				read_i++;
			}
			maintarif[read_i]=null;
			//19-03-14
		} 
		catch (Exception e) 
		{
			Log.d(TAG, e.toString());
		}
	}

	/**
	 * EC_calculation() calculates only EC value based upon the slabrate
	 */
	public void EC_Calculation()
	{	
		Log.d(TAG, "EC_Calculation");

		/*local variables*/ 
		BigDecimal AvgUnits, pf_const, pf_current, SlCon, Runits, Runitsorig;
		pf_const = new BigDecimal(0.85);
		long 	Rkw=0;
		char	Done=' ', LT2BjKj=' ';
		int		Week=0, ExtraDays=0;
		float tax_ec=0;

		//26-03-14
		/*var_init();
		var_assign();*/
		ReadSlabNTariff();

		//Nitish 21-04-2016
		CheckSlab();
		try 
		{	
			//19-03-14
			for(int i=0;i<NoSlab;i++)
			{
				if(maintarif[i][0].equals(new BigDecimal(tempf9)))
				{
					Row=i;
					break;
				}
			}		

			//23-03-14
			nodys=0;
			//if(rtDayWiseFlag == "Y")
			if(rtDayWiseFlag.equals("Y")) //29-10-2014
				nodys = Integer.parseInt(rtNewNoOfDays.trim());
			else
				nodys = Integer.parseInt(rtNoOfDays.trim());

			Done = LT2BjKj = 'N';		
			rtkVAFR = "0.00";

			Week = nodys/7;	   
			ExtraDays = nodys - (Week * 7);

			//23-03-14
			ConsPayable = false;
			int billfor=0;
			billfor = Integer.parseInt(rtBillFor.trim());
			Mul = new BigDecimal(1);
			//29-06-2015 Include 6 and 8 status for VA and Dis
			//if( Status == 1 || Status == 2 || (Status == 5)||(Status == 14) ||(Status == 6) || (Status == 8))
			// Added on 21-04-2016 for MS ,NT and DIR
			if( Status == 1 || Status == 2 || (Status == 5)||(Status == 14) ||(Status == 6) || (Status == 8) || (Status == 9) || (Status == 10) || (Status == 15))
				Mul = new BigDecimal(billfor).compareTo(new BigDecimal(1))==1 ? new BigDecimal(billfor) : new BigDecimal(1);
				else
					Mul = new BigDecimal(rtdlcount);

			//24-03-14
			try 
			{
				//if (rtDayWiseFlag == "Y")
				if (rtDayWiseFlag.equals("Y")) //29-10-2014
				{
					//Mul = new BigDecimal(nodys).divide(new BigDecimal(30), 2, RoundingMode.HALF_UP);
					Mul = new BigDecimal(nodys).divide(new BigDecimal(30), MathContext.DECIMAL128);
				}

			}
			catch (Exception e) 
			{
				Log.d(TAG, e.toString());
				Mul = new BigDecimal(0.00);
			}
			//24-03-14

			if ( tempf9 >= 1 && tempf9 <= 4 )
			{
				if(rtMtd.equals("1"))
				{
					try 
					{
						//Nitish 31-07-2015
						BigDecimal nounew = nou;
						if(PrvStatus == 6)
						{
							// Commented 21-04-2016
							/*if (nounew.compareTo(new BigDecimal(18))==1 || (load.compareTo(new BigDecimal(0.04))==1 && (rtThirtyFlag.equals("2") || rtThirtyFlag.equals("5"))))
							{
								nounew = nou;
								Mul = new BigDecimal(1);								
							}*/

							//Added 21-04-2016
							nounew = nou.divide(Mul, 2, RoundingMode.HALF_UP);


						}
						else
						{
							nounew = nou.divide(Mul, 2, RoundingMode.HALF_UP);
						}
						// End Nitish 31-07-2015
						//if (nounew.compareTo(new BigDecimal(18))==1 || (load.compareTo(new BigDecimal(0.04))==1 && (rtThirtyFlag.equals("2") || rtThirtyFlag.equals("5"))))
						//22-04-2017
						if (nounew.compareTo(new BigDecimal(40))==1 || (load.compareTo(new BigDecimal(0.04))==1 && (rtThirtyFlag.equals("2") || rtThirtyFlag.equals("5"))))
						{
							ConsPayable = true;

							CheckSlab();

							//26-03-14
							short fl=0;
							for(int k=0;k<TarifStr[i].length; k++)
							{
								if(TarifStr[i][k]=='M')
								{
									fl=1;
									break;
								}
								if(TarifStr[i][k]=='U')
								{
									fl=2;
									break;
								}
							}

							//26-03-14
							//if(TarifStr[i].toString().contains("M"))
							if(fl==1)
							{
								for(int z=0;z<TarifStr.length;z++)
								{
									if(maintarif[z][0].equals(new BigDecimal(22)))
									{
										Row = z;
										break;
									}
								}
							}
							//else if(TarifStr[i][((TarifStr[i].length)-1)] == 'U')
							else if(fl==2)
							{
								for(int z=0;z<TarifStr.length;z++)
								{
									if(maintarif[z][0].equals(new BigDecimal(21)))
									{
										Row = z;
										break;
									}
								}
							}
						}
					} 
					catch (Exception e) 
					{
						Log.d(TAG, e.toString());
						ConsPayable = false;
					}					
				}
				else
				{				
					if (load.compareTo(new BigDecimal(0.04))==1 && rtThirtyFlag.equals("1"))
						ConsPayable = true;
				}
			}
			else
				ConsPayable = true;
			//23-03-14			


			billingobj.setmConsPayable(ConsPayable == true ? "Y" : "N" );//30-06-2014



			//Calculate()
			if((Status == 0 || Status == 3 || Status == 4) && (tempf9 == 120 || tempf9 == 121))
				rtLineM = rtLineM.multiply(new BigDecimal(rtdlcount));

			//24-03-14
			try 	
			{
				if ((Status != 0 && Status != 3 && Status != 4) && (tempf9 != 120 && tempf9 != 121))
					rtLineM = rtLineM.divide(new BigDecimal(rtdlcount), 2, RoundingMode.HALF_UP);
			}
			catch (Exception e) 
			{
				Log.d(TAG, e.toString());
				rtLineM=new BigDecimal(0.00);
			}			

			if(Rkw==9999)
				Rkw++;

			SlCon = new BigDecimal(0);
			if ( tempf9 >= 1 && tempf9 <= 4 )
			{
				//Modified Nitish 22-04-2017 for LT-1
				if(rtMtd.equals("1") && ConsPayable && rtThirtyFlag.equals("0"))  //rtThirtyFlag!="2" && rtThirtyFlag!="5"
					Runits = nou.subtract(Mul.multiply(new BigDecimal(40)));
				else
					Runits = nou;
			}
			else
				Runits = nou;

			if(Runits.equals(new BigDecimal(9999.00)))
				Runits = Runits.add(new BigDecimal(1));

			Runitsorig=Runits;	
			/********Nitish 23-05-2018 start new tariff & old Tariff diff**********/
			/********Commented  21-06-2018 New tariff & old Tariff diff**********/	
			/********Commented  28-12-2020 New tariff & old Tariff diff**********/	
			/*int noofmonths=0;
			int effective_year=0;	
			int effective_month=0;
			int for_year=0;	
			int for_month=0;

			effective_year=16;
			effective_month=4;

			int month_count=0;
			//month_count = rt.DlCount;
			//month_count=Mul; //Original
			month_count=Integer.valueOf(Mul.intValue()); //Nitish 27-04-2015
			if(month_count > noofmonths)
			{
				//Original
				//new_nou = Runits/month_count; 
				//old_nou = Runits-new_nou; 



				//Nitish 27-04-2015
				new_nou = Runits.divide(new BigDecimal(month_count), MathContext.DECIMAL128);
				old_nou = Runits.subtract(new_nou);

				//if(month_count/month_count==1) //doubt Original
				if(month_count/month_count==1)
				{
					SlCon = new BigDecimal(0);
					if ( tempf9 >= 1 && tempf9 <= 4 )
					{
						//if(rtMtd.equals("1") && ConsPayable && rtThirtyFlag.equals("0"))  //rtThirtyFlag!="2" && rtThirtyFlag!="5"
						//	Runits = new_nou.subtract(new BigDecimal(18));
						//22-04-2017
						if(rtMtd.equals("1") && ConsPayable && rtThirtyFlag.equals("0"))  //rtThirtyFlag!="2" && rtThirtyFlag!="5"
							Runits = new_nou.subtract(new BigDecimal(40));
						else
							Runits = new_nou;
					}
					else
						Runits = new_nou;

					if(Runits.equals(new BigDecimal(9999.00)))
						Runits = Runits.add(new BigDecimal(1));

					Runitsorig=Runits;
					//int ecrate_count=0, ecrate_row=0;
					//BigDecimal TEc;

					for(i=11;i<23;i+=2)
					{
						if( (tempf9 >= 1 && tempf9 <=4) && Done == 'N')
						{
							if(rtMtd.equals("1"))
							{
								Done = 'Y';

								if (!ConsPayable)
								{
									//Original
									//if(rt.ThirtyFlag == '0' || rt.ThirtyFlag == '2' || rt.ThirtyFlag == '5')
									//	rt.TEc +=  new_nou * maintarif[Row][i+1] > 30 * 1 ? new_nou * maintarif[Row][i+1] : 30 * 1;
									//else
									//	rt.TEc += 30 * 1;
									if(rtThirtyFlag.equals("0") || rtThirtyFlag.equals("2") || rtThirtyFlag.equals("5"))
										rtTEc = ((new_nou.multiply(maintarif[Row][i+1])).compareTo(new BigDecimal(50))>0 ? new_nou.multiply(maintarif[Row][i+1]) :new BigDecimal(50)).add(rtTEc);//13-11-2020
									else
										rtTEc = (new BigDecimal(50)).add(rtTEc);
									
									

									rtTFc = new BigDecimal(0);
									Units_1 = new_nou;
									EC_Rate_1 = maintarif[Row][i+1];
									EC_1 = rtTEc;						
									break;
								}
								else if ( rtThirtyFlag.equals("2") || rtThirtyFlag.equals("5"))
								{
									rtBjKj2Lt2 = "1";
									//rtTFc = maintarif[Row][2];  //22-04-2017
								}
							}
							else if(rtMtd.equals("0")) //Unmetered
							{	
								Done = 'Y';
								if (!ConsPayable)
								{
									//if ( rt.ThirtyFlag != '2' )
									//{
									//	rt.TFc = 0.00;
									//	if(rt.ThirtyFlag == '0' || rt.ThirtyFlag == '5')
									//	{
									//		rt.TEc += 18.00 * maintarif[Row][i+1] * 1;
									//	}
									//	else
									//	{
									//		rt.TEc += 30.00 * 1;
									//	}
									//}
									//else
									//{
									//	rt.TEc += 30.00 * 1;
									//	rt.TFc = 0.00;
									//}


									if (!rtThirtyFlag.equals("2"))
									{
										rtTFc = new BigDecimal(0);
										//if(rtThirtyFlag.equals("0") || rtThirtyFlag.equals("5"))
										//	rtTEc = (maintarif[Row][i+1].multiply(new BigDecimal(18))).add(rtTEc);
										//Nitish  22-04-2017
										if(rtThirtyFlag.equals("0") || rtThirtyFlag.equals("5"))
											rtTEc = (maintarif[Row][i+1].multiply(new BigDecimal(40))).add(rtTEc); //13-11-2020
										else
											rtTEc = rtTEc.add(new BigDecimal(40)); //13-11-2020										
										

									}
									else
									{
										rtTFc = new BigDecimal(0);
										//rtTEc = (new BigDecimal(30)).add(rtTEc);
										rtTEc = (new BigDecimal(40)).add(rtTEc);//13-11-2020
									}

									Units_1 = new_nou;
									EC_Rate_1 = maintarif[Row][i+1];
									EC_1 = rtTEc;
									break;
								}
							}
						}
						ecrate_count = ecrate_count+1;
						ecrate_row = Row;

						//if(maintarif[Row][i]==9998 || maintarif[Row][i]==9997)
						//{
						//	  rt.TEc = rt.TEc + maintarif[Row][12];
						//	  break;
						//}
						//if(maintarif[Row][i]!=9999)
						//{
						//	if (i==11)
						//		SlCon = (maintarif[Row][i]*1)<Runitsorig ? (maintarif[Row][i]*1):Runits;
						//	else
						//		SlCon = (maintarif[Row][i]*1)<Runitsorig ? ((maintarif[Row][i]*1)-(maintarif[Row][i-2]*1)):Runits;
						//}
						//else
						//	SlCon = Runits;

						if(maintarif[Row][i].equals(new BigDecimal(9998)) || maintarif[Row][i].equals(new BigDecimal(9997)))
						{
							rtTEc = maintarif[Row][12].add(rtTEc);
							break;
						}

						if(!maintarif[Row][i].equals(new BigDecimal(9999)))
						{
							if (i==11)
								SlCon = maintarif[Row][i].compareTo(Runitsorig)<0 ? maintarif[Row][i] : Runits;
								else
									SlCon = maintarif[Row][i].compareTo(Runitsorig)<0 ? (maintarif[Row][i].subtract(maintarif[Row][i-2])) : Runits;

									//SlCon = Runits;
						}
						else
						{
							SlCon = Runits;
							//if (i==11)
							//	SlCon = maintarif[Row][i].compareTo(Runitsorig)==-1 ? maintarif[Row][i] : Runits;
							//	else
							//		SlCon = maintarif[Row][i].compareTo(Runitsorig)==-1 ? maintarif[Row][i].subtract(maintarif[Row][i-2]) : Runits;
						}							


						rtTEc = (SlCon.multiply(maintarif[Row][i+1])).add(rtTEc);
						TEc = rtTEc;
						Runits=Runits.subtract(SlCon);

						if(i == 11)
						{
							EC_1 = (SlCon.multiply(maintarif[Row][i+1]));
							Units_1 = SlCon;
							EC_Rate_1 = maintarif[ecrate_row][i+1];				
						}
						if(i == 13)
						{
							Units_2 = SlCon;
							EC_Rate_2 = maintarif[ecrate_row][i+1];
							EC_2 = (SlCon.multiply(maintarif[Row][i+1]));				
						}
						if(i == 15)
						{
							Units_3 = SlCon;
							EC_Rate_3 = maintarif[ecrate_row][i+1];
							EC_3 = (SlCon.multiply(maintarif[Row][i+1]));
						}
						if(i == 17)
						{
							Units_4 = SlCon;
							EC_Rate_4 = maintarif[ecrate_row][i+1];
							EC_4 = (SlCon.multiply(maintarif[Row][i+1]));
						}
						if(i == 19)
						{
							Units_5 = SlCon;
							EC_Rate_5 = maintarif[ecrate_row][i+1];
							EC_5 = (SlCon.multiply(maintarif[Row][i+1]));
						}
						if(i == 21)
						{
							Units_6 = SlCon;
							EC_Rate_6 = maintarif[ecrate_row][i+1];
							EC_6 = (SlCon.multiply(maintarif[Row][i+1]));
						}

						SlCon = new BigDecimal(0);
						if (Runits.compareTo(new BigDecimal(0))==-1 || Runits.compareTo(new BigDecimal(0))==0)
							break;		
					} // for

					new_TEc = rtTEc;
					rtTEc=new BigDecimal(0);
				}// new end, old start

				if(month_count>1)
				{
					if(tempf9 == next_tariff)
					{
						SlCon = new BigDecimal(0);
						if ( tempf9 >= 1 && tempf9 <= 4 )
						{
							if(rtMtd.equals("1") && ConsPayable && !rtThirtyFlag.equals("2") && !rtThirtyFlag.equals("5"))
							{
								//Original
								//if(Mul>1) 
								//Runits = old_nou - 18 * (Mul-1); 
								//else 
								//	Runits = old_nou - 18 * Mul;

								//Nitish 27-04-2015							
								if(Mul.compareTo(new BigDecimal(1))>0)	
									Runits = old_nou.subtract(new BigDecimal(40).multiply(Mul.subtract(new BigDecimal(1))));
								else
									Runits = old_nou.subtract(new BigDecimal(40).multiply(Mul));
							}
							else
							{
								Runits = old_nou;
							}
						}						
						else
							Runits = old_nou;

						if(Runits.equals(new BigDecimal(9999.00)))
							Runits = Runits.add(new BigDecimal(1));
						Runitsorig=Runits;


						Done = 'N'; //21-04-2016
						for(i=11;i<23;i+=2)
						{
							if( (tempf9 >= 1 && tempf9 <=4) && Done == 'N')
							{
								Done = 'Y'; //21-04-2016
								if(rtMtd.equals("1"))
								{
									if (!ConsPayable)
									{
										if(rtThirtyFlag.equals("0") || rtThirtyFlag.equals("2") || rtThirtyFlag.equals("5"))
										{

											//Original
											//if(Mul>1)
											//	rtTEc +=  old_nou * maintarif[Row+1][i+1] > 30 * (Mul-1) ? old_nou * maintarif[Row+1][i+1] : 30 * (Mul-1);
											//else
											//	rtTEc +=  old_nou * maintarif[Row+1][i+1] > 30 * Mul ? old_nou * maintarif[Row+1][i+1] : 30 * Mul;

											//Nitish  27-04-2015
											if(Mul.compareTo(new BigDecimal(1))>0)	
											{
												if(Status==0 || Status==3 || Status==4) //21-04-2016
												{
													if((old_nou.multiply(maintarif[Row+1][i+1])).compareTo(new BigDecimal(45).multiply(Mul.subtract(new BigDecimal(1)))) > 0) //13-11-2020
													{
														rtTEc =  (old_nou.multiply(maintarif[Row+1][i+1])).add(rtTEc);
													}
													else
													{
														rtTEc =  (new BigDecimal(45).multiply(Mul.subtract(new BigDecimal(1)))).add(rtTEc); //13-11-2020
													}													
												}
											}
											else
											{
												if((old_nou.multiply(maintarif[Row+1][i+1])).compareTo(new BigDecimal(45).multiply(Mul)) > 0) //13-11-2020
												{
													rtTEc =  (old_nou.multiply(maintarif[Row+1][i+1])).add(rtTEc);
												}
												else
												{
													rtTEc =  (new BigDecimal(45).multiply(Mul)).add(rtTEc); //13-11-2020
													
												}

											}												

										}
										else
										{
											//Original
											//if(Mul>1)
											//	rtTEc += 30 * (Mul-1);
											//else
											//	rtTEc += 30 * Mul;
											//Nitish 27-04-2015
											if(Mul.compareTo(new BigDecimal(1))>0)	
											{												
												rtTEc =  (new BigDecimal(45).multiply(Mul.subtract(new BigDecimal(1)))).add(rtTEc); //13-11-2020

											}
											else
											{
												rtTEc =  (new BigDecimal(45).multiply(Mul)).add(rtTEc);		 //13-11-2020										
											}
											


										}
										break;
									}
								}
								else if(rtMtd.equals("0"))//If Unmetered
								{	
									if (!ConsPayable)
									{
										if (!rtThirtyFlag.equals("2"))
										{
											if(rtThirtyFlag.equals("0") || rtThirtyFlag.equals("5"))
											{
												//Original
												//if(Mul>1)
												//	rtTEc += 18.00 * maintarif[Row][i+1] * (Mul-1);
												//else
												//	rtTEc += 18.00 * maintarif[Row][i+1] * Mul;
												//23-05-2018
												if(Mul.compareTo(new BigDecimal(1))>0)	
													rtTEc = (new BigDecimal(40).multiply(maintarif[Row][i+1]).multiply(Mul.subtract(new BigDecimal(1)))).add(rtTEc); //13-11-2020
												else
													rtTEc = (new BigDecimal(40).multiply(maintarif[Row][i+1]).multiply(Mul)).add(rtTEc); //13-11-2020
											}
											else
											{
												//Original
												//if(Mul>1)
												//	rtTEc += 30.00 * (Mul-1);
												//else
												//	rtTEc += 30.00 * Mul;
												//Nitish 27-04-2015
												if(Mul.compareTo(new BigDecimal(1))>0)	
													rtTEc = (new BigDecimal(40).multiply(Mul.subtract(new BigDecimal(1)))).add(rtTEc); //13-11-2020
												else
													rtTEc = (new BigDecimal(40).multiply(Mul)).add(rtTEc); //13-11-2020
												

											}
										}
										else
										{
											//Original
											//if(Mul>1)
											//	rtTEc += 30.00 * (Mul-1);
											//else
											//	rtTEc += 30.00 * Mul;
											//Nitish 27-04-2015
											if(Mul.compareTo(new BigDecimal(1))>0)	
												rtTEc = (new BigDecimal(40).multiply(Mul.subtract(new BigDecimal(1)))).add(rtTEc); //13-11-2020
											else
												rtTEc = (new BigDecimal(40).multiply(Mul)).add(rtTEc); //13-11-2020
										} 
										break;
									}
								}
							}
							ecrate_count = ecrate_count+1;
							ecrate_row = Row;

							if(maintarif[Row+1][i].equals(new BigDecimal(9998)) || maintarif[Row+1][i].equals(new BigDecimal(9997)))		
							{
								rtTEc = maintarif[Row+1][12].add(rtTEc);
								break;
							}
							if(!maintarif[Row+1][i].equals(new BigDecimal(9999)))
							{
								if (i==11)
								{
									//Original
									//if(Mul>1)
									//	SlCon = (maintarif[Row+1][i]*(Mul-1))<Runitsorig ? (maintarif[Row+1][i]*(Mul-1)):Runits;
									//	else
									//		SlCon = (maintarif[Row+1][i]*Mul)<Runitsorig ? (maintarif[Row+1][i]*Mul):Runits;

									//Nitish 27-04-2015
									if(Mul.compareTo(new BigDecimal(1))>0)	
									{
										if((maintarif[Row+1][i].multiply(Mul.subtract(new BigDecimal(1)))).compareTo(Runitsorig)<0)
										{
											SlCon = maintarif[Row+1][i].multiply(Mul.subtract(new BigDecimal(1)));
										}
										else
										{
											SlCon = Runits;
										}										

									}	
									else
									{
										if((maintarif[Row+1][i].multiply(Mul)).compareTo(Runitsorig)<0)
										{
											SlCon = maintarif[Row+1][i].multiply(Mul);
										}
										else
										{
											SlCon = Runits;
										}
									}
								}
								else					
								{
									//Original
									//if(Mul>1)
									//	SlCon = (maintarif[Row+1][i]*(Mul-1))<Runitsorig ? ((maintarif[Row+1][i]*(Mul-1))-(maintarif[Row+1][i-2]*(Mul-1))):Runits;
									// else
									//		SlCon = (maintarif[Row+1][i]*Mul)<Runitsorig ? ((maintarif[Row+1][i]*Mul)-(maintarif[Row+1][i-2]*Mul)):Runits;

									//Nitish 27-04-2015
									if(Mul.compareTo(new BigDecimal(1))>0)	
									{
										if((maintarif[Row+1][i].multiply(Mul.subtract(new BigDecimal(1)))).compareTo(Runitsorig)<0)
										{
											SlCon = (maintarif[Row+1][i].multiply(Mul.subtract(new BigDecimal(1)))).subtract(maintarif[Row+1][i-2].multiply(Mul.subtract(new BigDecimal(1))));
										}
										else
										{
											SlCon = Runits;
										}										

									}	
									else
									{
										if((maintarif[Row+1][i].multiply(Mul)).compareTo(Runitsorig)<0)
										{
											SlCon = (maintarif[Row+1][i].multiply(Mul)).subtract(maintarif[Row+1][i-2].multiply(Mul));
										}
										else
										{
											SlCon = Runits;
										}	
									}

								}
							}
							else
								SlCon = Runits;

							//Original
							//rtTEc = rtTEc + (SlCon * maintarif[Row+1][i+1]);
							//TEc = rtTEc;
							//Runits=Runits-SlCon;

							//Nitish 27-04-2015
							rtTEc = (SlCon.multiply(maintarif[Row+1][i+1])).add(rtTEc);
							TEc = rtTEc;
							Runits=Runits.subtract(SlCon);

							SlCon = new BigDecimal(0);
							if (Runits.compareTo(new BigDecimal(0))==-1 || Runits.compareTo(new BigDecimal(0))==0)
								break;
						} // for 

						old_TEc = rtTEc;
						//printf("old_TEc %f\n", old_TEc);
						pold_TEc = old_TEc;
					}
					else
					{
						SlCon = new BigDecimal(0);
						if ( tempf9 >= 1 && tempf9 <= 4 )
						{

							if(rtMtd.equals("1") && ConsPayable && !rtThirtyFlag.equals("2") && !rtThirtyFlag.equals("5"))
							{

								//if(Mul>1)
								//	Runits = old_nou - 18 * (Mul-1);
								//else
								//	Runits = old_nou - 18 * Mul;							
								
								//Nitish 25-05-2018
								if(Mul.compareTo(new BigDecimal(1))>0)	
									Runits = old_nou.subtract(new BigDecimal(40).multiply(Mul.subtract(new BigDecimal(1))));
								else
									Runits = old_nou.subtract(new BigDecimal(40).multiply(Mul));
							}
							else
							{
								Runits = old_nou;
							}
						}
						else
							Runits = old_nou;

						if(Runits.equals(new BigDecimal(9999)))
							Runits = Runits.add(new BigDecimal(1));
						Runitsorig=Runits;

						for(i=11;i<23;i+=2)
						{
							if( (tempf9 >= 1 && tempf9 <=4) && Done == 'N')
							{
								Done = 'Y'; //21-04-2016
								if(rtMtd.equals("1"))
								{
									if (!ConsPayable)
									{
										if(rtThirtyFlag.equals("0") || rtThirtyFlag.equals("2") || rtThirtyFlag.equals("5"))
										{
											//Original
											//if(Mul>1)
											//	rtTEc +=  old_nou * maintarif[Row][i+1] > 30 * (Mul-1) ? old_nou * maintarif[Row][i+1] : 30 * (Mul-1);
											//else
											//	rtTEc +=  old_nou * maintarif[Row][i+1] > 30 * Mul ? old_nou * maintarif[Row][i+1] : 30 * Mul;
											//Nitish 27-04-2015
											if(Mul.compareTo(new BigDecimal(1))>0)	
											{
												if(Status==0 || Status==3 || Status==4)//21-04-2016
												{
													if((old_nou.multiply(maintarif[Row][i+1])).compareTo(new BigDecimal(45).multiply(Mul.subtract(new BigDecimal(1)))) > 0) //13-11-2020
													{
														rtTEc =  (old_nou.multiply(maintarif[Row][i+1])).add(rtTEc);
													}
													else
													{
														rtTEc =  (new BigDecimal(45).multiply(Mul.subtract(new BigDecimal(1)))).add(rtTEc); //13-11-2020
													}
												}
											}
											else
											{
												if((old_nou.multiply(maintarif[Row][i+1])).compareTo(new BigDecimal(45).multiply(Mul)) > 0) //13-11-2020
												{
													rtTEc =  (old_nou.multiply(maintarif[Row][i+1])).add(rtTEc);
												}
												else
												{
													rtTEc =  (new BigDecimal(45).multiply(Mul)).add(rtTEc); //13-11-2020
												}

											}
										}
										else
										{
											//Original
											//if(Mul>1)
											//	rtTEc += 30 * (Mul-1);
											//else
											//	rtTEc += 30 * Mul;

											//Nitish 27-04-2015
											if(Mul.compareTo(new BigDecimal(1))>0)	
												rtTEc = (new BigDecimal(45).multiply(Mul.subtract(new BigDecimal(1)))).add(rtTEc); //13-11-2020
											else
												rtTEc = (new BigDecimal(45).multiply(Mul)).add(rtTEc); //13-11-2020

										}
										break;
									}
								}//End rtMtd.equals("1")
								else if(rtMtd.equals("0"))//If Unmetered
								{	
									if (!ConsPayable)
									{
										if (!rtThirtyFlag.equals("2"))
										{
											if(rtThirtyFlag.equals("0") || rtThirtyFlag.equals("5"))
											{
												//Original
												//if(Mul>1)
												//	rtTEc += 18.00 * maintarif[Row][i+1] * (Mul-1);
												//else
												//	rtTEc += 18.00 * maintarif[Row][i+1] * Mul;
												
												//13-11-2020
												if(Mul.compareTo(new BigDecimal(1))>0)	
													rtTEc = (new BigDecimal(40).multiply(maintarif[Row][i+1]).multiply(Mul.subtract(new BigDecimal(1)))).add(rtTEc); //13-11-2020
												else
													rtTEc = (new BigDecimal(40).multiply(maintarif[Row][i+1]).multiply(Mul)).add(rtTEc); //13-11-2020

											}
											else
											{
												//Original
												//if(Mul>1)
												//	rtTEc += 30.00 * (Mul-1);
												//else
												//	rtTEc += 30.00 * Mul;
												//Nitish 27-04-2015
												if(Mul.compareTo(new BigDecimal(1))>0)	
													rtTEc = (new BigDecimal(40).multiply(Mul.subtract(new BigDecimal(1)))).add(rtTEc); //13-11-2020
												else
													rtTEc = (new BigDecimal(40).multiply(Mul)).add(rtTEc); //13-11-2020

											}
										}
										else
										{
											//Original
											//if(Mul>1)
											//	rtTEc += 30.00 * (Mul-1);
											//else
											//	rtTEc += 30.00 * Mul;
											//Nitish 27-04-2015
											if(Mul.compareTo(new BigDecimal(1))>0)	
												rtTEc = (new BigDecimal(40).multiply(Mul.subtract(new BigDecimal(1)))).add(rtTEc); //13-11-2020
											else
												rtTEc = (new BigDecimal(40).multiply(Mul)).add(rtTEc); //13-11-2020
										}
										break;
									}
								}////End rtMtd.equals("0")
							}
							ecrate_count = ecrate_count+1;
							ecrate_row = Row;

							if(maintarif[Row][i].equals(new BigDecimal(9998)) || maintarif[Row][i].equals(new BigDecimal(9997)))		
							{
								rtTEc = maintarif[Row][12].add(rtTEc);
								break;
							}
							if(!maintarif[Row][i].equals(new BigDecimal(9999)))
							{
								if (i==11)
								{
									//Original
									//if(Mul>1)
									//	SlCon = (maintarif[Row][i]*(Mul-1))<Runitsorig ? (maintarif[Row][i]*(Mul-1)):Runits;
									//	else
									//		SlCon = (maintarif[Row][i]*Mul)<Runitsorig ? (maintarif[Row][i]*Mul):Runits;
									//Nitish 27-04-2015
									if(Mul.compareTo(new BigDecimal(1))>0)	
									{
										if((maintarif[Row][i].multiply(Mul.subtract(new BigDecimal(1)))).compareTo(Runitsorig)<0)
										{
											SlCon = (maintarif[Row][i].multiply(Mul.subtract(new BigDecimal(1))));
										}
										else
										{
											SlCon = Runits;
										}										

									}	
									else
									{
										if((maintarif[Row][i].multiply(Mul)).compareTo(Runitsorig)<0)
										{
											SlCon = (maintarif[Row][i].multiply(Mul));
										}
										else
										{
											SlCon = Runits;
										}	
									}
								}
								else					
								{

									//Original
									//if(Mul>1)
									//	SlCon = (maintarif[Row][i]*(Mul-1))<Runitsorig ? ((maintarif[Row][i]*(Mul-1))-(maintarif[Row][i-2]*(Mul-1))):Runits;
									//	else
									//		SlCon = (maintarif[Row][i]*Mul)<Runitsorig ? ((maintarif[Row][i]*Mul)-(maintarif[Row][i-2]*Mul)):Runits;

									//Nitish 27-04-2015
									if(Mul.compareTo(new BigDecimal(1))>0)	
									{
										if((maintarif[Row][i].multiply(Mul.subtract(new BigDecimal(1)))).compareTo(Runitsorig)<0)
										{
											SlCon = (maintarif[Row][i].multiply(Mul.subtract(new BigDecimal(1)))).subtract(maintarif[Row][i-2].multiply(Mul.subtract(new BigDecimal(1))));
										}
										else
										{
											SlCon = Runits;
										}										

									}	
									else
									{
										if((maintarif[Row][i].multiply(Mul)).compareTo(Runitsorig)<0)
										{
											SlCon = (maintarif[Row][i].multiply(Mul)).subtract(maintarif[Row][i-2].multiply(Mul));
										}
										else
										{
											SlCon = Runits;
										}	
									}
								}
							}
							else
								SlCon = Runits;


							//Original
							//rt.TEc = rt.TEc + (SlCon * maintarif[Row][i+1]);
							//TEc = rt.TEc;

							//Runits=Runits-SlCon;

							//SlCon = 0;
							//if (Runits <= 0)						
							//	break;		

							//Nitish 27-04-2015
							rtTEc = SlCon.multiply(maintarif[Row][i+1]).add(rtTEc);
							TEc = rtTEc;
							Runits=Runits.subtract(SlCon);
							SlCon = new BigDecimal(0);
							if (Runits.compareTo(new BigDecimal(0))<=0)						
								break;

						} // for 

						//Original
						//old_TEc = rt.TEc;
						//printf("old_TEc %f\n", old_TEc); //doubt
						old_TEc = rtTEc;
						pold_TEc = old_TEc;
					}
				}
				//Original
				//rt.TEc = new_TEc + old_TEc;				
				//new_TEc = old_TEc = 0.00;

				//Nitish 27-04-2015
				rtTEc =  new_TEc.add(old_TEc);
				new_TEc = old_TEc = new BigDecimal(0);

			}
			else
			{
				for(i=11;i<23;i+=2)
				{
					if( (tempf9 >= 1 && tempf9 <=4) && Done == 'N')
					{
						if(rtMtd.equals("1"))
						{
							Done = 'Y';
							if (!ConsPayable)
							{
								if(rtThirtyFlag.equals("0") || rtThirtyFlag.equals("2") || rtThirtyFlag.equals("5"))
								{
									//Original
									//if(PrvStatus == 6 )
									//{
									//	rt.TEc +=  nou * maintarif[Row][i+1] > 30 ? nou * maintarif[Row][i+1] : 30;
									//}
									//else
									//rt.TEc +=  nou * maintarif[Row][i+1] > 30 * Mul ? nou * maintarif[Row][i+1] : 30 * Mul;


									//Nitish 27-04-2015
									if(PrvStatus == 6 )
									{
										if((nou.multiply(maintarif[Row][i+1])).compareTo(new BigDecimal(30)) > 0)
										{
											rtTEc =  (nou.multiply(maintarif[Row][i+1])).add(rtTEc);
										}
										else
										{
											//rtTEc =  (new BigDecimal(30)).add(rtTEc);
											rtTEc =  (new BigDecimal(50)).add(rtTEc); //13-11-2020
										}
									}
									else
									{
										if((nou.multiply(maintarif[Row][i+1])).compareTo(new BigDecimal(50).multiply(Mul)) > 0)
										{
											rtTEc =  (nou.multiply(maintarif[Row][i+1])).add(rtTEc);
										}
										else
										{
											//rtTEc =  (new BigDecimal(30).multiply(Mul)).add(rtTEc);
											rtTEc =  (new BigDecimal(50).multiply(Mul)).add(rtTEc); //13-11-2020
										}

									}

								}
								else
									//Original
									//rtTEc += 30 * Mul;
									//rtTEc =  (new BigDecimal(30).multiply(Mul)).add(rtTEc);
								rtTEc =  (new BigDecimal(50).multiply(Mul)).add(rtTEc); //13-11-2020

								rtTFc = new BigDecimal(0);

								Units_1 = nou;
								EC_Rate_1 = maintarif[Row][i+1];
								EC_1 = rtTEc;
								if(Status == 6)
								{
									rtTEc=new BigDecimal(50); //13-11-2020
									EC_1 = rtTEc;
								}

								break;
							}
							else if (rtThirtyFlag.equals("2") || rtThirtyFlag.equals("5"))
							{	
								rtBjKj2Lt2 = "1";
								rtTFc = maintarif[Row][2];
							}
						}//End rtMtd.equals("1")
						else if(rtMtd.equals("0"))//If Unmetered
						{	
							Done = 'Y';
							if (!ConsPayable)
							{
								if (!rtThirtyFlag.equals("2"))
								{
									rtTFc = new BigDecimal(0);
									if(rtThirtyFlag.equals("0") || rtThirtyFlag.equals("5"))
									{
										//Original
										//rtTEc += 18.00 * maintarif[Row][i+1] * Mul;

										//rtTEc =  (new BigDecimal(18).multiply(maintarif[Row][i+1]).multiply(Mul)).add(rtTEc);
										//23-05-2018
										rtTEc =  (new BigDecimal(40).multiply(maintarif[Row][i+1]).multiply(Mul)).add(rtTEc); //13-11-2020
									}
									else
									{
										//Original
										//rtTEc += 30.00 * Mul;
										//rtTEc =  (new BigDecimal(30).multiply(Mul)).add(rtTEc);
										rtTEc =  (new BigDecimal(40).multiply(Mul)).add(rtTEc); //13-11-2020
									}
								}
								else
								{
									//Original
									//rtTEc += 30.00 * Mul;
									//rtTEc =  (new BigDecimal(30).multiply(Mul)).add(rtTEc);
									rtTEc =  (new BigDecimal(40).multiply(Mul)).add(rtTEc); //13-11-2020

									rtTFc =new BigDecimal(0);
								}
								Units_1 = nou;
								EC_Rate_1 = maintarif[Row][i+1];
								EC_1 = rtTEc;
								if(Status == 6)
								{
									//rtTEc=new BigDecimal(30);	
									rtTEc=new BigDecimal(50);	 //13-11-2020
									EC_1 = rtTEc;	
								}								
								break;
							}
						}//End rtMtd.equals("0")
					}
					ecrate_count = ecrate_count+1;
					ecrate_row = Row;

					/////////////////
					if(maintarif[Row][i].equals(new BigDecimal(9998)) || maintarif[Row][i].equals(new BigDecimal(9997)))
					{

						rtTEc = maintarif[Row][12].add(rtTEc);
						break;
					}
					if(!maintarif[Row][i].equals(new BigDecimal(9999)))
					{						
						if(PrvStatus == 6)
						{
							//Original
							//if (i==11)
							//	SlCon = (maintarif[Row][i])<Runitsorig ? (maintarif[Row][i]):Runits;
							//	else 
							//	{
							//		SlCon = (maintarif[Row][i])<Runitsorig ? ((maintarif[Row][i])-(maintarif[Row][i-2])):Runits;
							//	} 

							//Nitish 27-04-2015
							if(i==11)
							{
								if((maintarif[Row][i]).compareTo(Runitsorig)<0)
								{
									SlCon = (maintarif[Row][i]);
								}
								else
								{
									SlCon = Runits;
								}	
							}
							else
							{
								if((maintarif[Row][i]).compareTo(Runitsorig)<0)
								{
									SlCon = (maintarif[Row][i]).subtract(maintarif[Row][i-2]);
								}
								else
								{
									SlCon = Runits;
								}	
							}
						}
						else
						{
							//if (i==11)
							//	SlCon = (maintarif[Row][i]*Mul)<Runitsorig ? (maintarif[Row][i]*Mul):Runits;
							//	else 
							//	{
							//		SlCon = (maintarif[Row][i]*Mul)<Runitsorig ? ((maintarif[Row][i]*Mul)-(maintarif[Row][i-2]*Mul)):Runits;
							//	} 
							//Nitish 27-04-2015
							if(i==11)
							{
								if((maintarif[Row][i].multiply(Mul)).compareTo(Runitsorig)<0)
								{
									SlCon = (maintarif[Row][i].multiply(Mul));
								}
								else
								{
									SlCon = Runits;
								}	
							}
							else
							{
								if((maintarif[Row][i].multiply(Mul)).compareTo(Runitsorig)<0)
								{
									SlCon = (maintarif[Row][i].multiply(Mul)).subtract(maintarif[Row][i-2].multiply(Mul));
								}
								else
								{
									SlCon = Runits;
								}	
							}
						}
					}
					else
						SlCon = Runits;
					//Original
					//rt.TEc = rt.TEc + (SlCon * maintarif[Row][i+1]);

					//TEc = rt.TEc;
					///Runits=Runits-SlCon;			

					rtTEc = (SlCon.multiply(maintarif[Row][i+1])).add(rtTEc);
					TEc = rtTEc;
					Runits=Runits.subtract(SlCon);



					//Nitish 27-04-2015
					if(i == 11)
					{

						//EC_1 = (SlCon * maintarif[Row][i+1]); //Original
						EC_1 = (SlCon.multiply(maintarif[Row][i+1]));
						Units_1 = SlCon;
						//printf("Units_1 %f\n", Units_1);
						EC_Rate_1 = maintarif[ecrate_row][i+1];
					}
					if(i == 13) 
					{
						Units_2 = SlCon;
						//printf("Units_2 %f\n", Units_2);
						EC_Rate_2 = maintarif[ecrate_row][i+1];
						//EC_2 = (SlCon * maintarif[Row][i+1]); //Original
						EC_2 = (SlCon.multiply(maintarif[Row][i+1]));
					}
					if((i == 15)) 
					{
						Units_3 = SlCon;
						//printf("Units_3 %f\n", Units_3);
						EC_Rate_3 = maintarif[ecrate_row][i+1];
						//EC_3 = (SlCon * maintarif[Row][i+1]); //Original
						EC_3 = (SlCon.multiply(maintarif[Row][i+1]));
					}
					if(i == 17) 
					{
						Units_4 = SlCon;
						//printf("Units_4 %f\n", Units_4);
						EC_Rate_4 = maintarif[ecrate_row][i+1];
						//EC_4 = (SlCon * maintarif[Row][i+1]); //Original
						EC_4 = (SlCon.multiply(maintarif[Row][i+1]));
					}
					if(i == 19) 
					{
						Units_5 = SlCon;
						//printf("asdf Units_5 %f\n", Units_5);
						EC_Rate_5 = maintarif[ecrate_row][i+1];
						//EC_5 = (SlCon * maintarif[Row][i+1]);//Original
						EC_5 = (SlCon.multiply(maintarif[Row][i+1]));
					}
					if(i == 21)
					{
						Units_6 = SlCon;
						EC_Rate_6 = maintarif[ecrate_row][i+1] ;
						//EC_6 = (SlCon * maintarif[Row][i+1]);//Original
						EC_6 = (SlCon.multiply(maintarif[Row][i+1]));
					} 

					SlCon = new BigDecimal(0);
					//Original
					// if (Runits <= 0)
					//		break;
					if (Runits.compareTo(new BigDecimal(0))<=0)						
						break;
				} // for 
			}
			*/
			/********Nitish 13-11-2020 end new tariff & old Tariff diff**********/
		

			/****************************Nitish Original Tarif, Original Tarif To Be Restored in DEC 2020*******************************************/
			/****************************Un Commented  Original Tarif, Original Tarif on 28-12-2020*******************************************/
			
			int ecrate_count=0, ecrate_row=0;
			//BigDecimal TEc;

			for(i=11;i<23;i+=2)
			{
				if( (tempf9 >= 1 && tempf9 <=4) && Done == 'N')
				{
					if(rtMtd.equals("1"))
					{
						Done = 'Y';

						if (!ConsPayable)
						{
							if(rtThirtyFlag.equals("0") || rtThirtyFlag.equals("2") || rtThirtyFlag.equals("5"))
							{
								//rtTEc = ((nou.multiply(maintarif[Row][i+1])).compareTo(Mul.multiply(new BigDecimal(50)))==1 ? nou.multiply(maintarif[Row][i+1]) : Mul.multiply(new BigDecimal(50))).add(rtTEc);////13-11-2020
								rtTEc = ((nou.multiply(maintarif[Row][i+1])).compareTo(Mul.multiply(new BigDecimal(60)))==1 ? nou.multiply(maintarif[Row][i+1]) : Mul.multiply(new BigDecimal(60))).add(rtTEc);////24-06-2021
							}
							else
							{
								//rtTEc = (Mul.multiply(new BigDecimal(30))).add(rtTEc);
								//rtTEc = (Mul.multiply(new BigDecimal(50))).add(rtTEc); ////13-11-2020
								rtTEc = (Mul.multiply(new BigDecimal(60))).add(rtTEc); ////24-06-2021
								
							}

							Units_1 = nou;
							EC_Rate_1 = maintarif[Row][i+1];
							EC_1 = rtTEc;						
							break;
						}
						else if ( rtThirtyFlag.equals("2") || rtThirtyFlag.equals("5"))
							rtBjKj2Lt2 = "1";
					}
					else if(rtMtd.equals("0"))
					{	
						Done = 'Y';
						if (!ConsPayable)
						{
							if ( rtThirtyFlag.equals("2"))
							{
								//rtTEc = (Mul.multiply(new BigDecimal(30))).add(rtTEc);
								//rtTEc = (Mul.multiply(new BigDecimal(40))).add(rtTEc); //13-11-2020
								rtTEc = (Mul.multiply(new BigDecimal(60))).add(rtTEc); //24-06-2021
							}
							else
							{
								
								if(rtThirtyFlag.equals("0") || rtThirtyFlag.equals("5"))
								{
									//rtTEc = (Mul.multiply(maintarif[Row][i+1]).multiply(new BigDecimal(40))).add(rtTEc);
									rtTEc = (Mul.multiply(maintarif[Row][i+1]).multiply(new BigDecimal(60))).add(rtTEc); //24-06-2021
								}
								else
								{
									
									//rtTEc = (Mul.multiply(new BigDecimal(40))).add(rtTEc); //13-11-2020
									rtTEc = (Mul.multiply(new BigDecimal(60))).add(rtTEc); //24-06-2021
								}
							}

							Units_1 = nou;
							EC_Rate_1 = maintarif[Row][i+1];
							EC_1 = rtTEc;
							break;
						}
					}
				}
				ecrate_count = ecrate_count+1;
				ecrate_row = Row;

				if(maintarif[Row][i].equals(new BigDecimal(9998)) || maintarif[Row][i].equals(new BigDecimal(9997)))
				{
					rtTEc = maintarif[Row][12].add(rtTEc);
					break;
				}

				if(maintarif[Row][i].equals(new BigDecimal(9999)))
					SlCon = Runits;
				else
				{
					if (i==11)
						SlCon = maintarif[Row][i].multiply(Mul).compareTo(Runitsorig)==-1 ? maintarif[Row][i].multiply(Mul) : Runits;
						else
							SlCon = maintarif[Row][i].multiply(Mul).compareTo(Runitsorig)==-1 ? maintarif[Row][i].multiply(Mul).subtract(maintarif[Row][i-2].multiply(Mul)) : Runits;
				}					

				rtTEc = (SlCon.multiply(maintarif[Row][i+1])).add(rtTEc);
				//TEc = rtTEc;

				Runits=Runits.subtract(SlCon);

				if(i == 11)
				{
					EC_1 = (SlCon.multiply(maintarif[Row][i+1]));
					Units_1 = SlCon;
					EC_Rate_1 = maintarif[ecrate_row][i+1];				
				}
				if(i == 13)
				{
					Units_2 = SlCon;
					EC_Rate_2 = maintarif[ecrate_row][i+1];
					EC_2 = (SlCon.multiply(maintarif[Row][i+1]));				
				}
				if(i == 15)
				{
					Units_3 = SlCon;
					EC_Rate_3 = maintarif[ecrate_row][i+1];
					EC_3 = (SlCon.multiply(maintarif[Row][i+1]));
				}
				if(i == 17)
				{
					Units_4 = SlCon;
					EC_Rate_4 = maintarif[ecrate_row][i+1];
					EC_4 = (SlCon.multiply(maintarif[Row][i+1]));
				}
				if(i == 19)
				{
					Units_5 = SlCon;
					EC_Rate_5 = maintarif[ecrate_row][i+1];
					EC_5 = (SlCon.multiply(maintarif[Row][i+1]));
				}
				if(i == 21)
				{
					Units_6 = SlCon;
					EC_Rate_6 = maintarif[ecrate_row][i+1];
					EC_6 = (SlCon.multiply(maintarif[Row][i+1]));
				}

				SlCon = new BigDecimal(0);
				if (Runits.compareTo(new BigDecimal(0))==-1 || Runits.compareTo(new BigDecimal(0))==0)
					break;		
			} // for	
					 
			/****************************************Nitish End Original Tarif To Be Restored 13-11-2020***************************************************/

			// naveen commented
			/*if(rtSupplyPoints > 0 && (Status == 0 || Status == 1 || Status == 2 || Status == 3 || Status == 4 || Status == 5 || Status == 14 ))
							rtTEc = new BigDecimal(rtSupplyPoints).multiply(rtTEc);*/

			//30-06-2014
			tax_ec = rtTEc.floatValue();
			billingobj.setmTax_Ec(new BigDecimal(tax_ec));

			if ( tempf9 >= 120 && tempf9 <= 121)
			{
				//24-03-14
				try 
				{
					//AvgUnits = (nou.multiply(new BigDecimal(7))).divide(new BigDecimal(nodys), 2, RoundingMode.HALF_UP);
					//11-04-2014
					AvgUnits = (nou.multiply(new BigDecimal(7))).divide(new BigDecimal(nodys), MathContext.DECIMAL128);
					rtTEc =  maintarif[Row][i+1];

					//11-04-2014
					rtTEc =  ((AvgUnits.multiply(new BigDecimal(Week)).multiply(rtTEc)).add((nou.multiply((rtTEc.divide(new BigDecimal(nodys), MathContext.DECIMAL128))).multiply(new BigDecimal(ExtraDays)))));

					// 30-06-2014
					tax_ec = rtTEc.floatValue();
					billingobj.setmTax_Ec(new BigDecimal(tax_ec));
				} 
				catch (Exception e) 
				{
					Log.d(TAG, e.toString());
					rtTEc = new BigDecimal(0.00);
				}
				//24-03-14
			}

			/*if ( tempf9 >= 25 && tempf9 <= 27 )
			{
				rtFlReb = rtTFc.add(rtTEc); 
				Runits=Runitsorig;

				for(int i=0;i<4;i++)
				{
					switch (i)
					{
					case 0:
						SlCon=Mul.multiply(new BigDecimal(200)).compareTo(Runitsorig)==-1 ? Mul.multiply(new BigDecimal(200)):Runits;
						EC_FL_1 = new BigDecimal(0);
						break;
					case 1:
						SlCon=Mul.multiply(new BigDecimal(280)).compareTo(Runitsorig)==-1 ? Mul.multiply(new BigDecimal(80)):Runits;
						rtTEc=(SlCon.multiply(new BigDecimal(0.10))).add(rtTEc);
						EC_FL_2 = (SlCon.multiply(new BigDecimal(0.10)));
						break;
					case 2:
						SlCon=Mul.multiply(new BigDecimal(400)).compareTo(Runitsorig)==-1 ? Mul.multiply(new BigDecimal(120)):Runits;
						rtTEc=(SlCon.multiply(new BigDecimal(1.82))).add(rtTEc);
						EC_FL_3 = (SlCon.multiply(new BigDecimal(1.82)));
						break;
					case 3:	
						SlCon=Runits;
						if(tempf9 == 26)
						{
							rtTEc=(SlCon.multiply(new BigDecimal(6.25))).add(rtTEc);
							EC_FL_4 = SlCon.multiply(new BigDecimal(6.25));
						}
						if(tempf9 == 27)
						{
							rtTEc=(SlCon.multiply(new BigDecimal(5.75))).add(rtTEc);
							EC_FL_4 = SlCon.multiply(new BigDecimal(5.75));
						}
						if(tempf9 == 21)
						{
							rtTEc=(SlCon.multiply(new BigDecimal(6.25))).add(rtTEc);
							EC_FL_4 = SlCon.multiply(new BigDecimal(6.25));
						}
						if(tempf9 == 22)
						{
							rtTEc=(SlCon.multiply(new BigDecimal(5.75))).add(rtTEc);
							EC_FL_4 = SlCon.multiply(new BigDecimal(5.75));
						}
						break;
					}
					Runits=Runits.subtract(SlCon);
					SlCon=new BigDecimal(0);
					if (Runits.compareTo(new BigDecimal(0))==-1 || Runits.compareTo(new BigDecimal(0))==0)
						break;
				}
				EC_FL = EC_FL_2.add(EC_FL_3).add(EC_FL_4);
			}*/
			//Nitish 30-04-2015
			if ( tempf9 >= 25 && tempf9 <= 27 )
			{
				rtFlReb = rtTFc.add(rtTEc); 
				Runits=Runitsorig;

				for(int i=0;i<4;i++)
				{
					switch (i)
					{
					case 0:
						SlCon=Mul.multiply(new BigDecimal(200)).compareTo(Runitsorig)==-1 ? Mul.multiply(new BigDecimal(200)):Runits;
						EC_FL_1 = new BigDecimal(0);
						break;
					case 1:
						SlCon=Mul.multiply(new BigDecimal(280)).compareTo(Runitsorig)==-1 ? Mul.multiply(new BigDecimal(80)):Runits;
						//rtTEc=(SlCon.multiply(new BigDecimal(0.10))).add(rtTEc);
						EC_FL_2 = (SlCon.multiply(new BigDecimal(0.10)));
						break;
					case 2:
						SlCon=Mul.multiply(new BigDecimal(400)).compareTo(Runitsorig)==-1 ? Mul.multiply(new BigDecimal(120)):Runits;
						//rtTEc=(SlCon.multiply(new BigDecimal(1.82))).add(rtTEc);
						EC_FL_3 = (SlCon.multiply(new BigDecimal(1.82)));
						break;
					case 3:	
						SlCon=Runits;
						if(tempf9 == 26)
						{											
							//EC_FL_4 = SlCon.multiply(new BigDecimal(7.80));  //31-05-2019
							//EC_FL_4 = SlCon.multiply(new BigDecimal(8.05));  //13-11-2020
							EC_FL_4 = SlCon.multiply(new BigDecimal(8.15));  //24-06-2021
						}
						if(tempf9 == 27)
						{													
							//EC_FL_4 = SlCon.multiply(new BigDecimal(7.30)); //31-05-2019
							//EC_FL_4 = SlCon.multiply(new BigDecimal(7.55)); //13-11-2020
							EC_FL_4 = SlCon.multiply(new BigDecimal(7.65));  //24-06-2021
						}
						if(tempf9 == 21)
						{													
							//EC_FL_4 = SlCon.multiply(new BigDecimal(7.80));  //31-05-2019
							//EC_FL_4 = SlCon.multiply(new BigDecimal(8.05));  //13-11-2020
							EC_FL_4 = SlCon.multiply(new BigDecimal(8.15));  //24-06-2021
						}
						if(tempf9 == 22)
						{													
							//EC_FL_4 = SlCon.multiply(new BigDecimal(7.30)); //31-05-2019
							//EC_FL_4 = SlCon.multiply(new BigDecimal(7.55)); //13-11-2020
							EC_FL_4 = SlCon.multiply(new BigDecimal(7.65));  //24-06-2021
						}
						break;
					}
					Runits=Runits.subtract(SlCon);
					SlCon=new BigDecimal(0);
					if (Runits.compareTo(new BigDecimal(0))==-1 || Runits.compareTo(new BigDecimal(0))==0)
						break;
				}
				EC_FL = EC_FL_2.add(EC_FL_3).add(EC_FL_4);
				//29-04-2015
				//ir1 = (st == 0 || st == 3 || st == 4 ? ( (rt.DLTEc+rt.DLTEc_GoK) > 0.0 ? (rt.DLTEc+rt.DLTEc_GoK) : 0.0) : 0.0);
				//(FC_1+FC_2+EC_1+EC_2+EC_3+EC_4) - ((rt.TEc+rt.TFc)-(FC_1+FC_2+EC_1+EC_2+EC_3+EC_4)))) + ir1

				ir1 = ((Status==0||Status==3||Status==4) ? (((rtDLTEc.add(rtDLTEc_GoK)).compareTo(new BigDecimal(0))==1) ? (rtDLTEc.add(rtDLTEc_GoK)) : new BigDecimal(0.00)) : new BigDecimal(0.00));
				rtFlReb = (rtFlReb.subtract(EC_FL)).add(ir1);
			}


			ir1 = ((Status==0||Status==3||Status==4) ? ((rtDLTEc.compareTo(new BigDecimal(0))==1) ? rtDLTEc : new BigDecimal(0.00)) : new BigDecimal(0.00));

			//27-12-2014 nagarjuna
			// ir2 = ( Status == 0 || Status == 3 || Status == 4 ? ((rt.DLTEc_GoK) > 0.00 ? (rt.DLTEc_GoK) : 0.00 ) : 0.00);
			//31-12-2014
			ir2 = ((Status==0||Status==3||Status==4) ? ((rtDLTEc_GoK.compareTo(new BigDecimal(0))==1) ? rtDLTEc_GoK : new BigDecimal(0.00)) : new BigDecimal(0.00));
			if( tempf9 >= 1 && tempf9 <= 4 ) 
			{
				if (!ConsPayable)
				{
					if ( rtThirtyFlag.equals("2") || rtThirtyFlag.equals("5"))
					{
						//rtGoKPayable = rtTEc.subtract(ir1);
						rtGoKPayable = rtTEc.subtract(ir2);  //31-12-2014
					}
					else
					{
						//rtGoKPayable = rtTEc.subtract(ir1);
						rtGoKPayable = rtTEc.subtract(ir2); //31-12-2014
					}
					//29-09-2015
					rtGoKPayable = rtGoKPayable.add(rtKVAAssd_Cons);
				}
				else
				{
					if(rtThirtyFlag.equals("0"))
					{
						Row -= 1;
						//rtGoKPayable = rtGoKPayable.add(((maintarif[Row][SLABRATE].multiply(new BigDecimal(18))).multiply(Mul)).subtract(ir1));
						//rtGoKPayable = rtGoKPayable.add(((maintarif[Row][SLABRATE].multiply(new BigDecimal(18))).multiply(Mul)).subtract(ir2)); //31-12-2014
						//rtTEc = (Mul.multiply(maintarif[Row][SLABRATE]).multiply(new BigDecimal(18))).add(rtTEc);
						//22-04-2017
						rtGoKPayable = rtGoKPayable.add(((maintarif[Row][SLABRATE].multiply(new BigDecimal(40))).multiply(Mul)).subtract(ir2)); //31-12-2014
						rtTEc = (Mul.multiply(maintarif[Row][SLABRATE]).multiply(new BigDecimal(40))).add(rtTEc);
						Row += 1;
					}
					else if (rtThirtyFlag.equals("1"))
					{
						//rtGoKPayable = rtGoKPayable.add(Mul.multiply(new BigDecimal(30))).subtract(ir1);
						rtGoKPayable = rtGoKPayable.add(Mul.multiply(new BigDecimal(30))).subtract(ir2); //31-12-2014
						rtTEc = (Mul.multiply(new BigDecimal(30))).add(rtTEc);
					}
				}
			}

			//26-12-2014 nagarjuna
			/*if( tempf9 >= 1 && tempf9 <= 4 ) 
			{		
				if ( ConsPayable == 'Y' )
				{
					//rt.TEc=rt.TEc-ir1;
					if(rt.ThirtyFlag != '0' && rt.ThirtyFlag != '1')
						if(ir2>0)
							rt.GoKPayable-=rt.DLTEc_GoK;
				}			
			}*/
			//Nitish 31-12-2014
			if( tempf9 >= 1 && tempf9 <= 4 ) 
			{		
				if (ConsPayable)
				{
					//rt.TEc=rt.TEc-ir1;
					if(!rtThirtyFlag.equals("0") && !rtThirtyFlag.equals("1"))
						if(ir2.compareTo(new BigDecimal(0))>0)
							rtGoKPayable= rtGoKPayable.subtract(rtDLTEc_GoK);
				}			
			}



			//29-10-2014
			if ((tempf9 == 60 || tempf9 == 61 || tempf9 == 62) && ((load.compareTo(new BigDecimal(10.00))==-1) || (load.compareTo(new BigDecimal(10.00))==0)))
			{
				rtGoKPayable = rtTEc.add(rtTFc);
				//29-09-2015
				rtGoKPayable = rtGoKPayable.add(rtKVAAssd_Cons);
			}
			ir1 = new BigDecimal(0);
			//24-03-2017
			try
			{
				billingobj.setmGoKPayable(rtGoKPayable.setScale(2, RoundingMode.HALF_UP));
			}
			catch(Exception e)
			{
				billingobj.setmGoKPayable(rtGoKPayable);
			}
			//End 24-03-2017
			billingobj.setmTEc(rtTEc);
			//billingobj.setmOldTecBill(pold_TEc);//Commented 28-12-2020
			billingobj.setmOldTecBill(new BigDecimal(0.0));//UnCommented 28-12-2020
			billingobj.setmKVAFR(rtkVAFR);
			billingobj.setMmUnits_1(Units_1);
			billingobj.setMmUnits_2(Units_2);
			billingobj.setMmUnits_3(Units_3);
			billingobj.setMmUnits_4(Units_4);
			billingobj.setMmUnits_5(Units_5);
			billingobj.setMmUnits_6(Units_6);
			billingobj.setMmEC_Rate_1(EC_Rate_1);
			billingobj.setMmEC_Rate_2(EC_Rate_2);
			billingobj.setMmEC_Rate_3(EC_Rate_3);
			billingobj.setMmEC_Rate_4(EC_Rate_4);
			billingobj.setMmEC_Rate_5(EC_Rate_5);
			billingobj.setMmEC_Rate_6(EC_Rate_6);
			billingobj.setMmEC_1(EC_1);
			billingobj.setMmEC_2(EC_2);
			billingobj.setMmEC_3(EC_3);
			billingobj.setMmEC_4(EC_4);
			billingobj.setMmEC_5(EC_5);
			billingobj.setMmEC_6(EC_6);
			billingobj.setMmEC_FL_1(EC_FL_1);
			billingobj.setMmEC_FL_2(EC_FL_2);
			billingobj.setMmEC_FL_3(EC_FL_3);
			billingobj.setMmEC_FL_4(EC_FL_4);
			billingobj.setMmEC_FL(EC_FL);
			billingobj.setmFLReb(rtFlReb);
			billingobj.setmLinMin(rtLineM);
		} 
		catch (Exception e) 
		{
			//25-03-14
			billingobj.setmGoKPayable(new BigDecimal(0.00));
			billingobj.setmTEc(new BigDecimal(0.00));
			billingobj.setmKVAFR("0.00");
			billingobj.setMmUnits_1(new BigDecimal(0.00));
			billingobj.setMmUnits_2(new BigDecimal(0.00));
			billingobj.setMmUnits_3(new BigDecimal(0.00));
			billingobj.setMmUnits_4(new BigDecimal(0.00));
			billingobj.setMmUnits_5(new BigDecimal(0.00));
			billingobj.setMmUnits_6(new BigDecimal(0.00));
			billingobj.setMmEC_Rate_1(new BigDecimal(0.00));
			billingobj.setMmEC_Rate_2(new BigDecimal(0.00));
			billingobj.setMmEC_Rate_3(new BigDecimal(0.00));
			billingobj.setMmEC_Rate_4(new BigDecimal(0.00));
			billingobj.setMmEC_Rate_5(new BigDecimal(0.00));
			billingobj.setMmEC_Rate_6(new BigDecimal(0.00));
			billingobj.setMmEC_1(new BigDecimal(0.00));
			billingobj.setMmEC_2(new BigDecimal(0.00));
			billingobj.setMmEC_3(new BigDecimal(0.00));
			billingobj.setMmEC_4(new BigDecimal(0.00));
			billingobj.setMmEC_5(new BigDecimal(0.00));
			billingobj.setMmEC_6(new BigDecimal(0.00));
			billingobj.setMmEC_FL_1(new BigDecimal(0.00));
			billingobj.setMmEC_FL_2(new BigDecimal(0.00));
			billingobj.setMmEC_FL_3(new BigDecimal(0.00));
			billingobj.setMmEC_FL_4(new BigDecimal(0.00));
			billingobj.setMmEC_FL(new BigDecimal(0.00));
			billingobj.setmFLReb(new BigDecimal(0.00));
			billingobj.setmLinMin(new BigDecimal(0.00));

			Log.d(TAG, e.toString());
		}
	}
	//13-03-14

	//27-03-14
	public void TotBillCal()
	{
		Log.d(TAG, "TotBillCal");
		try 
		{
			//19-03-14
			//if ( tempf9 >= 120 && tempf9 <= 121)
			//19-03-14
			//if ( tempf9 >= 120 && tempf9 <= 121)
			if ( tempf9 == 120) //26-04-2017
			{  			

				//Nitish 18-04-2017
				if(rtDayWiseFlag.equals("Y"))
				{
					if(rtTEc.compareTo(rtTFc)==1)
						rtTFc = new BigDecimal(0.00);
					else if(rtTFc.compareTo(rtTEc)==1)
						rtTEc = new BigDecimal(0.00);		
					else
						rtTFc = new BigDecimal(0.00);
				}
				else
				{
					/*if(rtTEc.compareTo(rtFC.add(rtFC_Slab_2))==1)
						rtTFc = new BigDecimal(0.00);
					else if(rtFC.add(rtFC_Slab_2).compareTo(rtTEc)==1)
					{			   		
						rtTFc = rtFC.add(rtFC_Slab_2);
						rtTEc = new BigDecimal(0.00);		
					}
					else
						rtTFc = new BigDecimal(0.00);*/
					
					//Added Nitish 22-12-2017
					if(rtTEc.compareTo(rtTFc)==1)
						rtTFc = new BigDecimal(0.00);
					else if(rtTFc.compareTo(rtTEc)==1)
						rtTEc = new BigDecimal(0.00);		
					else
						rtTFc = new BigDecimal(0.00);
					//End 22-12-2017
				}
				//End Nitish 18-04-2017

				//25-03-14
				billingobj.setmTFc(rtTFc);
				billingobj.setmTEc(rtTEc);
			}


			int nodys=0;
			rtBillTotal=new BigDecimal(0.00);
			ir1 = new BigDecimal(0.00);

			//31-12-2014
			BigDecimal rtDLTEc_gok_Add = new BigDecimal(0.00);
			rtDLTEc_gok_Add = rtDLTEc.add(rtDLTEc_GoK);			
			ir1 = (Status == 0 || Status == 3 || Status == 4 ? ( rtDLTEc_gok_Add.compareTo(new BigDecimal(0.00))==1 ? rtDLTEc_gok_Add : new BigDecimal(0.00)) : new BigDecimal(0.00));

			if(rtDayWiseFlag.equals("Y")) //29-10-2014
				nodys = Integer.parseInt(rtNewNoOfDays.trim());
			else
				nodys = Integer.parseInt(rtNoOfDays.trim());

			int billfor=0;
			billfor = Integer.parseInt(rtBillFor.trim());
			Mul = new BigDecimal(1);
			if (Status == 1 || Status == 2 || Status == 5)
				Mul = ((new BigDecimal(billfor).compareTo(new BigDecimal(1))==1) ? new BigDecimal(billfor) : new BigDecimal(1));
			else
				Mul = new BigDecimal(rtdlcount);

			//24-03-14
			try 
			{
				if (rtDayWiseFlag.equals("Y"))  //29-10-2014
					Mul= new BigDecimal(nodys).divide(new BigDecimal(30), 2, RoundingMode.HALF_UP);
			} 
			catch (Exception e) 
			{
				Log.d(TAG, e.toString());
				Mul = new BigDecimal(0.00);
			}
			//24-03-14

			BigDecimal rtDrFees;
			BigDecimal rtIntrstCurnt;
			//26-03-14
			BigDecimal rtOthers;

			if(rtDrFees_str==null || rtDrFees_str.contains(" "))
				rtDrFees=new BigDecimal(0.00);
			else
				rtDrFees = new BigDecimal(rtDrFees_str);
			if(rtIntrstCurnt_str==null || rtIntrstCurnt_str.contains(" "))
				rtIntrstCurnt=new BigDecimal(0.00);
			else		
				rtIntrstCurnt = new BigDecimal(rtIntrstCurnt_str);
			//26-03-14
			if(rtOthers_str==null || rtOthers_str.contains(" "))
				rtOthers=new BigDecimal(0.00);
			else		
				rtOthers = new BigDecimal(rtOthers_str);

			floattemp = new BigDecimal(0.00);
			floattemp = (((rtDemandChrg.compareTo(new BigDecimal(0.00))==1 ? rtDemandChrg : rtTFc).multiply(Mul).add(rtTEc)).compareTo(rtLineM)==-1 ? rtLineM : (rtDemandChrg.compareTo(new BigDecimal(0.00))==1 ? rtDemandChrg : rtTFc)).add(rtTEc);
			//30-10-2014
			//floattemp = floattemp.add(((tempf9 >= 1 && tempf9 <=4) || (rtGoKArrears.compareTo(new BigDecimal(0.00))==1 || ((tempf9 == 60 || tempf9 == 61 || tempf9 == 62) && (load.compareTo(new BigDecimal(10.00))==-1 || load.compareTo(new BigDecimal(10.00))==0) && rtRebFlag=="Y"))) ? rtGoKArrears : new BigDecimal(0.00)); 
			floattemp = floattemp.add(((tempf9 >= 1 && tempf9 <=4) || (rtGoKArrears.compareTo(new BigDecimal(0.00))==1 || ((tempf9 == 60 || tempf9 == 61 || tempf9 == 62) && ((load.compareTo(new BigDecimal(10.00))==-1 || load.compareTo(new BigDecimal(10.00))==0)) && rtRebFlag.equals("Y")))) ? rtGoKArrears : new BigDecimal(0.00)); 
			//27-03-14		
			floattemp = floattemp.add(rtDrFees).add(rtOthers).add(rtPfPenAmt).add(rtPenExLd).add(rtArears).add(rtIntrstCurnt).add(rtTaxAmt)
					.add((tempf9 >= 100 && tempf9 <= 101 ? rtArearsUnpaid.add(rtIntrstUnpaid) : new BigDecimal(0.00)))
					.add(rtIntrstOld.subtract(rtHLReb.compareTo(new BigDecimal(0.00))==1 ? rtHLReb : new BigDecimal(0.00))
							.subtract(rtHCReb.compareTo(new BigDecimal(0.00))==1 ? rtHCReb : new BigDecimal(0.00))
							.subtract(rtCapReb.compareTo(new BigDecimal(0.00))==1 ? rtCapReb : new BigDecimal(0.00))
							.subtract(rtEcReb.compareTo(new BigDecimal(0.00))==1 ? rtEcReb : new BigDecimal(0.00))
							.subtract(rtFlReb.compareTo(new BigDecimal(0.00))==1 ? rtFlReb : new BigDecimal(0.00)));

			floattemp = floattemp.subtract(ir1);
			//billtotal_b4_roundoff = floattemp;
			//rtBillTotal = floattemp.setScale(0, BigDecimal.ROUND_HALF_EVEN);
			//27-03-14
			rtBillTotal = floattemp.add(billingobj.getmKVAAssd_Cons());//Add floattemp with FEC 28-06-2014

			billingobj.setmBillTotal(rtBillTotal);
		}
		catch (Exception e)
		{
			//25-03-14
			billingobj.setmBillTotal(new BigDecimal(0.00));
			billingobj.setmTFc(new BigDecimal(0.00));
			billingobj.setmTEc(new BigDecimal(0.00));
			Log.d(TAG, e.toString());
		}
	}

	//18-03-14
	public void MD_Calculation(BigDecimal md)
	{
		Log.d(TAG, "MD_Calculation");

		rtDemandChrg	= new BigDecimal(0.00);
		rtPenExLd	  	= new BigDecimal(0.00);
		//25-03-14
		rtExLoad	 	= new BigDecimal(0.00);		
		rtRecordDmnd = new BigDecimal(0.00);

		if(md.compareTo(new BigDecimal(0.00))==1)
		{			
			//if((rtTvmMtr.equals("1")||tempf9==82||tempf9==83)&&((Status==0)||(Status==3)||(Status==4)))
			//Modified 25-06-2015
			//if((rtTvmMtr.equals("1")|| rtTvmMtr.equals("4")) && (Status == 0 || Status == 3 || Status == 4))  
			if(Status == 0 || Status == 3 || Status == 4) //29-03-2016
			{
				try 
				{
					//Mul = ((rtDayWiseFlag=="Y") ? (new BigDecimal(nodys).divide(new BigDecimal(30), 2, RoundingMode.HALF_UP)) : new BigDecimal(1));
					//29-10-2014
					Mul = ((rtDayWiseFlag.equals("Y")) ? (new BigDecimal(nodys).divide(new BigDecimal(30), 2, RoundingMode.HALF_UP)) : new BigDecimal(1));
				} 
				catch (Exception e) 
				{
					Mul = new BigDecimal(0.00);
				}

				//25-03-14
				try 
				{
					Cal_exldNDmnd(md);

					billingobj.setmRecordDmnd(rtRecordDmnd);
					billingobj.setmExLoad(rtExLoad);
					billingobj.setmDemandChrg(rtDemandChrg);
					billingobj.setmPenExLd(rtPenExLd);		
					billingobj.setmSancHp(Contractdemand);
				} 
				catch (Exception e) 
				{
					//25-03-14
					billingobj.setmRecordDmnd(new BigDecimal(0.00));
					billingobj.setmExLoad(new BigDecimal(0.00));
					billingobj.setmDemandChrg(new BigDecimal(0.00));
					billingobj.setmPenExLd(new BigDecimal(0.00));		
					billingobj.setmSancHp(new BigDecimal(0.00));
				}
			}
		}
	}

	//18-03-214
	private void Cal_exldNDmnd(BigDecimal md)
	{		
		Log.d(TAG, "Cal_exldNDmnd");

		//25-03-14
		floattemp = new BigDecimal(0.00);

		if(md.compareTo(new BigDecimal(0.00))==1)
		{
			floattemp = md;
			record_demand = floattemp;	
			//15-04-2014
			rtRecordDmnd = floattemp;
			floattemp=floattemp.divide(new BigDecimal(0.746), 2, RoundingMode.HALF_UP);

			//floattemp = roundoffQuarter(floattemp); //31-12-2016

			if(tempf9 == 94 || tempf9 == 42 || tempf9 == 43)
				//Nitish 25-06-2015
				if (rtTvmMtr.equals("1")  || rtTvmMtr.equals("4")) 
				{
					if(Contractdemand.compareTo(new BigDecimal(5.00))==-1)
						Contractdemand = new BigDecimal(5.00);
				}

			if(tempf9 == 94 || tempf9 == 42 || tempf9 == 43)
				rtDemandChrg = Calculate_DC() ;
			if(floattemp.compareTo(new BigDecimal(0.00))==1)
			{
				//15-04-2014
				//rtRecordDmnd = floattemp.multiply(mtrcnst);
				//29-03-2016
				//if(tempf9 > 1 && tempf9 < 44)
				//if((tempf9 > 1 && tempf9 < 44) || tempf9 ==101 || tempf9 ==94 || tempf9 ==95 || tempf9 ==126 || tempf9 ==127 || tempf9 ==164 || tempf9 ==165) //23-08-2016
				//if((tempf9 >= 1 && tempf9 <= 44) || tempf9 ==101 || tempf9 ==94 || tempf9 ==95 || tempf9 ==120||tempf9 ==121 ||tempf9 ==126 || tempf9 ==127 || tempf9 ==164 || tempf9 ==165) //20-01-2017
				if((tempf9 >= 1 && tempf9 <= 44) || tempf9 ==101  || tempf9 ==120||tempf9 ==121 ) //20-04-2017
				{					
					floattemp=record_demand;
				}				
				//End 29-03-2016
				if(floattemp.multiply(mtrcnst).compareTo(Contractdemand)==1)
				{				
					//rtExLoad  = (tempf9 == 94 || tempf9 == 42 || tempf9 == 43) ? roundoffQuarter(floattemp.multiply(mtrcnst)) : ((floattemp.multiply(mtrcnst)).subtract(load));	
					//Nitish 25-06-2015  
					//rtExLoad  = (tempf9 == 42 || tempf9 == 43) ? roundoffQuarter(floattemp.multiply(mtrcnst)) : roundoffQuarter((floattemp.multiply(mtrcnst)).subtract(load));				
					//Nitish 25-01-2017
					//If MD > 1 and SancLoad < 1 then rtExLoad = (MD-1)
					try
					{
						if(floattemp.compareTo(new BigDecimal(1.00)) > 0 && load.compareTo(new BigDecimal(1.00))==-1)
						{
							rtExLoad  = (tempf9 == 42 || tempf9 == 43) ? roundoffQuarter(floattemp.multiply(mtrcnst)) : roundoffQuarter((floattemp.multiply(mtrcnst)).subtract(new BigDecimal(1.00)));				
						}
						else
						{
							BigDecimal subValue = new BigDecimal(0.00);
							subValue = (floattemp.multiply(mtrcnst)).subtract(load);
							if(subValue.compareTo(new BigDecimal(1.00))==-1 && load.compareTo(new BigDecimal(1.00))==-1) //25-06-2016
							{
								rtExLoad  = (tempf9 == 42 || tempf9 == 43) ? roundoffQuarter(floattemp.multiply(mtrcnst)) : new BigDecimal(0.00);				
							}
							else
							{
								//Nitish 24-01-2017 LT-5b,LT-5a Do Not Subtract With Load 
								rtExLoad  = (tempf9 == 42 || tempf9 == 43 || tempf9 == 94 || tempf9 == 95 || tempf9 == 126 || tempf9 == 127 || tempf9 == 164 || tempf9 == 165 ||(tempf9 >= 80 && tempf9 <= 83)|| (tempf9 >= 122 && tempf9 <= 125)) ? roundoffQuarter(floattemp.multiply(mtrcnst)) : roundoffQuarter((floattemp.multiply(mtrcnst)).subtract(load));				
							}
						}
					}
					catch(Exception e)
					{
						rtExLoad  = (tempf9 == 42 || tempf9 == 43) ? roundoffQuarter(floattemp.multiply(mtrcnst)) : roundoffQuarter((floattemp.multiply(mtrcnst)).subtract(load));				
					}

					ptemp = Calculate_pen_Chrg(rtExLoad); 
				}
			}
			//rtPenExLd = ptemp;
			//Nitish 25-01-2017
			try
			{
				rtPenExLd = ptemp;
				rtPenExLd = rtPenExLd.setScale(2, RoundingMode.HALF_UP);				
			}
			catch (Exception e) 
			{
				rtPenExLd = new BigDecimal(0.00);
			}
			//End 25-01-2017
			Mul = new BigDecimal(1);
		}		
	}

	//18-03-14
	private BigDecimal roundoffQuarter(BigDecimal value)
	{
		Log.d(TAG, "roundoffQuarter");

		BigDecimal Range;
		//15-04-2014
		int rnge = value.intValue();
		Range = new BigDecimal(rnge);

		try 
		{
			for(int i=0;i<4;i++)
			{
				Range = Range.add(new BigDecimal(0.25));
				if(Range.compareTo(value)==1 || Range.compareTo(value)==0)
					break;
			}
			if(Range.subtract(value).compareTo((value.subtract(Range).add(new BigDecimal(0.25))))==1)
				return (Range.subtract(new BigDecimal(0.25)));
			else
				return Range;
		} 
		catch (Exception e) 
		{
			Log.d(TAG, e.toString());
			return null;
		}	
	}

	//18-03-14
	private BigDecimal Calculate_DC()
	{
		Log.d(TAG, "Calculate_DC");
		BigDecimal DC = new BigDecimal(0.00);
		DC = rtFC.add(rtFC_Slab_2);
		return DC;
	}

	private BigDecimal Get_Rate(BigDecimal Excessload) 
	{
		Log.d(TAG, "Get_Rate");

		BigDecimal Penalty=new BigDecimal(0.00),valueinKilowatt=new BigDecimal(1.00);
		BigDecimal a=new BigDecimal(0.00),b=new BigDecimal(0.00),c=new BigDecimal(0.00);	

		try 
		{
			if(tempf9 == 122 ||tempf9 == 123 || tempf9 == 124 ||tempf9 == 125 )  //LT5-a
			{
				a = Excessload;				
				
				//23-05-2018
				//BigDecimal lt5FC1 = new BigDecimal(45.00);
				//BigDecimal lt5FC2 = new BigDecimal(50.00);
				//BigDecimal lt5FC3 = new BigDecimal(70.00);
				//BigDecimal lt5FC4 = new BigDecimal(130.00);
				
				//31-05-2019
				/*BigDecimal lt5FC1 = new BigDecimal(55.00);
				BigDecimal lt5FC2 = new BigDecimal(60.00);
				BigDecimal lt5FC3 = new BigDecimal(80.00);
				BigDecimal lt5FC4 = new BigDecimal(140.00);*/
				
				//13-11-2020
				/*BigDecimal lt5FC1 = new BigDecimal(65.00);
				BigDecimal lt5FC2 = new BigDecimal(70.00);
				BigDecimal lt5FC3 = new BigDecimal(90.00);
				BigDecimal lt5FC4 = new BigDecimal(150.00);

				BigDecimal lt5R0 = new BigDecimal(0.00);
				BigDecimal lt5R1 = new BigDecimal(5.00);
				BigDecimal lt5R2 = new BigDecimal(40.00);
				BigDecimal lt5R3 = new BigDecimal(67.00);*/
				
				//24-06-2021
				BigDecimal lt5FC1 = new BigDecimal(75.00);
				BigDecimal lt5FC2 = new BigDecimal(85.00);
				BigDecimal lt5FC3 = new BigDecimal(105.00);
				BigDecimal lt5FC4 = new BigDecimal(170.00);
				BigDecimal lt5FC5 = new BigDecimal(200.00);

				BigDecimal lt5R0 = new BigDecimal(0.00);
				BigDecimal lt5R1 = new BigDecimal(5.00);
				BigDecimal lt5R2 = new BigDecimal(40.00);
				BigDecimal lt5R3 = new BigDecimal(67.00);
				BigDecimal lt5R4 = new BigDecimal(100.00);

				if(tempf9==125)
				{
					Penalty = Penalty.add(a.multiply(lt5FC4)); //Penalty4				

				}
				else if(tempf9==124)
				{
					if(a.compareTo(lt5R3) >= 0)
					{
						a = a.subtract(lt5R3);
						Penalty = Penalty.add((lt5R3).multiply(lt5FC3)); //Penalty3
						Penalty = Penalty.add(a.multiply(lt5FC4)); //Penalty4

					}
					else 
					{
						Penalty = Penalty.add(a.multiply(lt5FC3)); //Penalty3
					}
				}
				else if(tempf9==123)
				{
					if(a.compareTo(lt5R3)  >= 0)
					{						
						a = a.subtract(lt5R3);
						Penalty = Penalty.add((lt5R2).multiply(lt5FC2)); 	 //Penalty2
						Penalty = Penalty.add((lt5R3.subtract(lt5R2)).multiply(lt5FC3)); 	 //Penalty3
						Penalty = Penalty.add(a.multiply(lt5FC4)); //Penalty4

					}
					else if(a.compareTo(lt5R2)  >= 0)
					{
						a = a.subtract(lt5R2);
						Penalty = Penalty.add((lt5R2).multiply(lt5FC2)); 	 //Penalty2
						Penalty = Penalty.add(a.multiply(lt5FC3)); //Penalty3

					}
					else
					{
						Penalty = Penalty.add(a.multiply(lt5FC2)); //Penalty2
					}
				}
				else if(tempf9==122)
				{
					if(a.compareTo(lt5R3)  >= 0)
					{						
						a = a.subtract(lt5R3);
						Penalty = Penalty.add((lt5R1).multiply(lt5FC1)); 	 //Penalty1
						Penalty = Penalty.add((lt5R2.subtract(lt5R1)).multiply(lt5FC2)); 	 //Penalty2
						Penalty = Penalty.add((lt5R3.subtract(lt5R2)).multiply(lt5FC3)); 	 //Penalty3
						Penalty = Penalty.add(a.multiply(lt5FC4)); //Penalty4

					}
					else if(a.compareTo(lt5R2)  >= 0)
					{
						a = a.subtract(lt5R2);
						Penalty = Penalty.add((lt5R1).multiply(lt5FC1)); 	 //Penalty1
						Penalty = Penalty.add((lt5R2.subtract(lt5R1)).multiply(lt5FC2)); 	 //Penalty2
						Penalty = Penalty.add(a.multiply(lt5FC3)); //Penalty3

					}
					else if(a.compareTo(lt5R1)  >= 0)
					{
						a = a.subtract(lt5R1);
						Penalty = Penalty.add((lt5R1).multiply(lt5FC1)); 	 //Penalty1						
						Penalty = Penalty.add(a.multiply(lt5FC2)); //Penalty2
					}
					else
					{
						Penalty = Penalty.add(a.multiply(lt5FC1)); //Penalty1
					}

				}
				Penalty = Penalty.subtract(rtTFc); //FC
			}			
			else if(tempf9 == 80 || tempf9 == 81 || tempf9 == 82 || tempf9 == 83) //LT5-b
			{
				a = Excessload;
				
				
				//23-05-2018
				/*BigDecimal lt5FC1 = new BigDecimal(40.00);
				BigDecimal lt5FC2 = new BigDecimal(45.00);
				BigDecimal lt5FC3 = new BigDecimal(65.00);
				BigDecimal lt5FC4 = new BigDecimal(120.00);*/
				
				//31-05-2019
				/*BigDecimal lt5FC1 = new BigDecimal(45.00);
				BigDecimal lt5FC2 = new BigDecimal(55.00);
				BigDecimal lt5FC3 = new BigDecimal(75.00);
				BigDecimal lt5FC4 = new BigDecimal(125.00);*/
				
				/*BigDecimal lt5FC1 = new BigDecimal(55.00);
				BigDecimal lt5FC2 = new BigDecimal(65.00);
				BigDecimal lt5FC3 = new BigDecimal(85.00);
				BigDecimal lt5FC4 = new BigDecimal(135.00);

				BigDecimal lt5R0 = new BigDecimal(0.00);
				BigDecimal lt5R1 = new BigDecimal(5.00);
				BigDecimal lt5R2 = new BigDecimal(40.00);
				BigDecimal lt5R3 = new BigDecimal(67.00);*/
				
				BigDecimal lt5FC1 = new BigDecimal(65.00);
				BigDecimal lt5FC2 = new BigDecimal(80.00);
				BigDecimal lt5FC3 = new BigDecimal(100.00);
				BigDecimal lt5FC4 = new BigDecimal(155.00);
				BigDecimal lt5FC5 = new BigDecimal(185.00);

				BigDecimal lt5R0 = new BigDecimal(0.00);
				BigDecimal lt5R1 = new BigDecimal(5.00);
				BigDecimal lt5R2 = new BigDecimal(40.00);
				BigDecimal lt5R3 = new BigDecimal(67.00);
				BigDecimal lt5R4 = new BigDecimal(100.00);


				if(tempf9==83)
				{
					Penalty = Penalty.add(a.multiply(lt5FC4)); //Penalty4				

				}
				else if(tempf9==82)
				{
					if(a.compareTo(lt5R3)  >= 0)
					{
						a = a.subtract(lt5R3);
						Penalty = Penalty.add((lt5R3).multiply(lt5FC3)); //Penalty3
						Penalty = Penalty.add(a.multiply(lt5FC4)); //Penalty4

					}
					else 
					{
						Penalty = Penalty.add(a.multiply(lt5FC3)); //Penalty3
					}
				}
				else if(tempf9==81)
				{
					if(a.compareTo(lt5R3)  >= 0)
					{						
						a = a.subtract(lt5R3);
						Penalty = Penalty.add((lt5R2).multiply(lt5FC2)); 	 //Penalty2
						Penalty = Penalty.add((lt5R3.subtract(lt5R2)).multiply(lt5FC3)); 	 //Penalty3
						Penalty = Penalty.add(a.multiply(lt5FC4)); //Penalty4

					}
					else if(a.compareTo(lt5R2)  >= 0)
					{
						a = a.subtract(lt5R2);
						Penalty = Penalty.add((lt5R2).multiply(lt5FC2)); 	 //Penalty2
						Penalty = Penalty.add(a.multiply(lt5FC3)); //Penalty3

					}
					else
					{
						Penalty = Penalty.add(a.multiply(lt5FC2)); //Penalty2
					}
				}
				else if(tempf9==80)
				{
					if(a.compareTo(lt5R3)  >= 0)
					{						
						a = a.subtract(lt5R3);
						Penalty = Penalty.add((lt5R1).multiply(lt5FC1)); 	 //Penalty1
						Penalty = Penalty.add((lt5R2.subtract(lt5R1)).multiply(lt5FC2)); 	 //Penalty2
						Penalty = Penalty.add((lt5R3.subtract(lt5R2)).multiply(lt5FC3)); 	 //Penalty3
						Penalty = Penalty.add(a.multiply(lt5FC4)); //Penalty4

					}
					else if(a.compareTo(lt5R2)  >= 0)
					{
						a = a.subtract(lt5R2);
						Penalty = Penalty.add((lt5R1).multiply(lt5FC1)); 	 //Penalty1
						Penalty = Penalty.add((lt5R2.subtract(lt5R1)).multiply(lt5FC2)); 	 //Penalty2
						Penalty = Penalty.add(a.multiply(lt5FC3)); //Penalty3

					}
					else if(a.compareTo(lt5R1)  >= 0)
					{
						a = a.subtract(lt5R1);
						Penalty = Penalty.add((lt5R1).multiply(lt5FC1)); 	 //Penalty1						
						Penalty = Penalty.add(a.multiply(lt5FC2)); //Penalty2
					}
					else
					{
						Penalty = Penalty.add(a.multiply(lt5FC1)); //Penalty1
					}

				}
				Penalty = Penalty.subtract(rtTFc); //FC

			}
			//20-04-2017
			else if(tempf9 == 126 ||tempf9 == 127 || tempf9 == 165)  //LT-5a  DBT
			{
				a = Excessload;
				
				
				
				
				/*//13-11-2020
				BigDecimal lt5FC1 = (new BigDecimal(85.00));
				BigDecimal lt5FC2 = (new BigDecimal(115.00));
				BigDecimal lt5FC3 = (new BigDecimal(200.00));


				BigDecimal lt5R0 = new BigDecimal(0.00);
				BigDecimal lt5R1 = new BigDecimal(40.00);
				BigDecimal lt5R2 = new BigDecimal(67.00);*/
				
				//24-06-2021
				BigDecimal lt5FC1 = (new BigDecimal(100.00));
				BigDecimal lt5FC2 = (new BigDecimal(130.00));
				BigDecimal lt5FC3 = (new BigDecimal(220.00));
				BigDecimal lt5FC4 = (new BigDecimal(230.00));

				BigDecimal lt5R0 = new BigDecimal(0.00);
				BigDecimal lt5R1 = new BigDecimal(40.00);
				BigDecimal lt5R2 = new BigDecimal(67.00);
				BigDecimal lt5R3 = new BigDecimal(100.00);



				if(tempf9==165)
				{
					Penalty = Penalty.add(roundoffQuarter(a.multiply(new BigDecimal(0.746))).multiply(lt5FC3)); //Penalty3
				}
				else if(tempf9==127)
				{
					if(a.compareTo(lt5R2)  >= 0)
					{
						a = a.subtract(lt5R2);
						Penalty = Penalty.add(roundoffQuarter(lt5R2.multiply(new BigDecimal(0.746))).multiply(lt5FC2)); 	 //Penalty2
						Penalty = Penalty.add(roundoffQuarter(a.multiply(new BigDecimal(0.746))).multiply(lt5FC3)); //Penalty3

					}
					else 
					{
						Penalty = Penalty.add(roundoffQuarter(a.multiply(new BigDecimal(0.746))).multiply(lt5FC2)); //Penalty2
					}
				}
				else if(tempf9==126)
				{
					if(a.compareTo(lt5R2)  >= 0)
					{						
						a = a.subtract(lt5R2);
						Penalty = Penalty.add((roundoffQuarter(lt5R1.multiply(new BigDecimal(0.746)))).multiply(lt5FC1)); 	 //Penalty1
						Penalty = Penalty.add(((roundoffQuarter((lt5R2.subtract(lt5R1)).multiply(new BigDecimal(0.746))))).multiply(lt5FC2)); 	 //Penalty2
						Penalty = Penalty.add(roundoffQuarter(a.multiply(new BigDecimal(0.746))).multiply(lt5FC3)); //Penalty3

					}
					else if(a.compareTo(lt5R1) >= 0)
					{
						a = a.subtract(lt5R1);
						Penalty = Penalty.add((roundoffQuarter(lt5R1.multiply(new BigDecimal(0.746)))).multiply(lt5FC1)); 	 //Penalty1
						Penalty = Penalty.add(roundoffQuarter(a.multiply(new BigDecimal(0.746))).multiply(lt5FC2)); //Penalty2

					}
					else
					{
						Penalty = Penalty.add(roundoffQuarter(a.multiply(new BigDecimal(0.746))).multiply(lt5FC1)); //Penalty1
					}
				}				
				Penalty = Penalty.subtract(rtTFc); //FC
			}
			else if(tempf9 == 94 ||tempf9 == 95 || tempf9 == 164)  //LT-5b  DBT
			{
				a = Excessload;

			
				
				//31-05-2019
				/*BigDecimal lt5FC1 = (new BigDecimal(70.00));
				BigDecimal lt5FC2 = (new BigDecimal(100.00));
				BigDecimal lt5FC3 = (new BigDecimal(180.00));*/
				
				//13-11-2020
				/*BigDecimal lt5FC1 = (new BigDecimal(80.00));
				BigDecimal lt5FC2 = (new BigDecimal(110.00));
				BigDecimal lt5FC3 = (new BigDecimal(190.00));

				BigDecimal lt5R0 = new BigDecimal(0.00);
				BigDecimal lt5R1 = new BigDecimal(40.00);
				BigDecimal lt5R2 = new BigDecimal(67.00);*/
				
				//24-06-2021
				BigDecimal lt5FC1 = (new BigDecimal(95.00));
				BigDecimal lt5FC2 = (new BigDecimal(125.00));
				BigDecimal lt5FC3 = (new BigDecimal(210.00));
				BigDecimal lt5FC4 = (new BigDecimal(220.00));

				BigDecimal lt5R0 = new BigDecimal(0.00);
				BigDecimal lt5R1 = new BigDecimal(40.00);
				BigDecimal lt5R2 = new BigDecimal(67.00);
				BigDecimal lt5R3 = new BigDecimal(100.00);




				if(tempf9==164)
				{
					//Penalty = Penalty.add(a.multiply(lt5FC3)); //Penalty3
					//26-05-2018
					Penalty =  Penalty.add(roundoffQuarter(a.multiply(new BigDecimal(0.746))).multiply(lt5FC3)); //Penalty3
				}
				else if(tempf9==95)
				{
					if(a.compareTo(lt5R2)  >= 0)
					{
						a = a.subtract(lt5R2);
						Penalty = Penalty.add((roundoffQuarter(lt5R2.multiply(new BigDecimal(0.746)))).multiply(lt5FC2)); 	 //Penalty2
						Penalty = Penalty.add(roundoffQuarter(a.multiply(new BigDecimal(0.746))).multiply(lt5FC3)); //Penalty3

					}
					else 
					{
						Penalty = Penalty.add(roundoffQuarter(a.multiply(new BigDecimal(0.746))).multiply(lt5FC2)); //Penalty2
					}
				}
				else if(tempf9==94)
				{
					if(a.compareTo(lt5R2)  >= 0)
					{

						a = a.subtract(lt5R2);
						Penalty = Penalty.add((roundoffQuarter(lt5R1.multiply(new BigDecimal(0.746)))).multiply(lt5FC1)); 	 //Penalty1
						Penalty = Penalty.add(((roundoffQuarter((lt5R2.subtract(lt5R1)).multiply(new BigDecimal(0.746))))).multiply(lt5FC2)); 	 //Penalty2
						Penalty = Penalty.add(roundoffQuarter(a.multiply(new BigDecimal(0.746))).multiply(lt5FC3)); //Penalty3

					}
					else if(a.compareTo(lt5R1)  >= 0)
					{
						a = a.subtract(lt5R1);
						Penalty = Penalty.add((roundoffQuarter(lt5R1.multiply(new BigDecimal(0.746)))).multiply(lt5FC1)); 	 //Penalty1
						Penalty = Penalty.add(roundoffQuarter(a.multiply(new BigDecimal(0.746))).multiply(lt5FC2)); //Penalty2

					}
					else
					{
						Penalty = Penalty.add(roundoffQuarter(a.multiply(new BigDecimal(0.746))).multiply(lt5FC1)); //Penalty1
					}
				}
				Penalty = Penalty.subtract(rtTFc); //FC

			}//End 20-04-2017
			else
			{
				b = Excessload;

				for(i=1;i<=9;i+=2)
				{
					if(Contractdemand.compareTo((maintarif[Row][i].multiply(valueinKilowatt)))==-1)
					{
						if(b.equals(Excessload) && Contractdemand != maintarif[Row][i].multiply(valueinKilowatt))
						{
							if(maintarif[Row][i].equals(new BigDecimal(9999)) || floattemp.multiply(mtrcnst).compareTo(maintarif[Row][i].multiply(valueinKilowatt))==-1)
								a = Excessload;
							else if(floattemp.multiply(mtrcnst).compareTo(maintarif[Row][i].multiply(valueinKilowatt))==1)
							{

								a = (maintarif[Row][i].multiply(valueinKilowatt)).subtract(Contractdemand);
								//31-05-2016 For 
								if(a.compareTo(new BigDecimal(1.00))<=0)
								{
									if(tempf9 ==1  || tempf9 == 3||( tempf9 >= 20 && tempf9 <= 27))
										a = new BigDecimal(0.00);
								}
							}
						}
						else if(b.equals(Excessload) && Contractdemand.equals(maintarif[Row][i].multiply(valueinKilowatt)))
							a = Excessload;
						else if((b.compareTo(((maintarif[Row][i].multiply(valueinKilowatt)).subtract(maintarif[Row][i-2].multiply(valueinKilowatt))))==1) || (b.compareTo(((maintarif[Row][i].multiply(valueinKilowatt)).subtract(maintarif[Row][i-2].multiply(valueinKilowatt))))==0))
							a = (maintarif[Row][i].multiply(valueinKilowatt)).subtract(maintarif[Row][i-2].multiply(valueinKilowatt));
						else if((b.compareTo(((maintarif[Row][i].multiply(valueinKilowatt)).subtract(maintarif[Row][i-2].multiply(valueinKilowatt))))==-1) || (b.compareTo(((maintarif[Row][i].multiply(valueinKilowatt)).subtract(maintarif[Row][i-2].multiply(valueinKilowatt))))==0))
							a = b;

						Penalty = Penalty.add(a.multiply(maintarif[Row][i+1])); 

						b = b.subtract(a);
						c = c.add(a);

						if(c.equals(Excessload))
							break;
						if(c.compareTo(Excessload)>=0)  //19-01-2017
							break;
					}
				}
			}
			return Penalty;
		} 
		catch (Exception e)                                                                          
		{
			Log.d(TAG, e.toString());
			return new BigDecimal(0.00); //19-01-2017
			//return null;
		}		
	}//18-03-14
	private BigDecimal Calculate_pen_Chrg(BigDecimal exload)
	{
		Log.d(TAG, "Calculate_pen_Chrg");

		try
		{
			//return (rtDayWiseFlag == "Y" ? (Get_Rate(exload).multiply(Mul)).multiply(new BigDecimal(2.00)) : Get_Rate(exload).multiply(new BigDecimal(2.00)));
			return (rtDayWiseFlag.equals("Y") ? (Get_Rate(exload).multiply(Mul)).multiply(new BigDecimal(2.00)) : Get_Rate(exload).multiply(new BigDecimal(2.00)));
		}
		catch (Exception e) 
		{
			Log.d(TAG, e.toString());
			return null;
		}
	}

	//19-03-14
	private void AvgPFPenalty()
	{
		Log.d(TAG, "AvgPFPenalty");
		BigDecimal pf = new BigDecimal(0.85).setScale(2, BigDecimal.ROUND_HALF_UP);

		try 
		{
			ir1 = new BigDecimal(rtPF1);	

			//if (rtTvmMtr.equals("1") || rtTvmMtr.equals("2"))
			if (rtTvmMtr.equals("1") || rtTvmMtr.equals("2") || rtTvmMtr.equals("4")) //Nitish 25-06-2015			
			{
				if(rtKVAAssd_Cons.compareTo(nou)==1)
				{
					//24-03-14
					try 
					{
						ir1 = nou.divide(rtKVAAssd_Cons, 2, RoundingMode.HALF_UP);
					}
					catch (Exception e) 
					{
						Log.d(TAG, e.toString());
						ir1 = new BigDecimal(0.00);
					}
					//24-03-14

					rtPF1 = ir1.toString();
					ir1 = pf.subtract(ir1);
				}
				else
					ir1 = pf;
			}
			else if((ir1.compareTo(new BigDecimal(0.00))==1) && (ir1.compareTo(pf)==-1))
				ir1 = (pf.subtract(ir1));

			if(ir1.compareTo(pf)!=0)
			{
				ir1 = (ir1.multiply(new BigDecimal(100.00))).multiply(PfPenalty);		
				rtPfPenAmt = (ir1.compareTo(new BigDecimal(0.30))==1) ?  (nou.multiply(new BigDecimal(0.30)))  : (nou.multiply(ir1));				
			}			

			FunctionCallFlag = '1';	
		} 
		catch (Exception e) 
		{
			Log.d(TAG, e.toString());
		}			
	}	

	//24-03-14
	public void PF_Calculation(BigDecimal newpf)
	{
		Log.d(TAG, "PF_Calculation");

		try 
		{
			//24-03-14
			boolean pf_calc_flag=false;
			BigDecimal pf_const, pf_current;
			pf_const = new BigDecimal(0.85);

			//25-03-14
			rtPfPenAmt = new BigDecimal(0.00);

			ir1 = new BigDecimal(0.00);
			BigDecimal ir2 = new BigDecimal(0.00);

			//29-07-2016
			//if(Status == 0 || Status == 3 || Status == 4 || Status == 1 || Status == 2 || Status == 14)
			if(Status == 0 || Status == 3 || Status == 4 || Status == 1 || Status == 2 || Status == 14 || Status == 9 || Status == 10)
			{
				//if ( (rtTvmMtr.equals("1") || rtTvmMtr.equals("2") || tempf9 >= 40) && nou.compareTo(new BigDecimal(0.00))==1)
				//29-02-2016
				//if ( (rtTvmMtr.equals("1") || rtTvmMtr.equals("2") || rtTvmMtr.equals("4") ||  tempf9 >= 40) && nou.compareTo(new BigDecimal(0.00))==1) 
				if (nou.compareTo(new BigDecimal(0.00))==1) 
				{
					pf_current = new BigDecimal(rtPF1);
					//29-02-2016
					/*if((tempf9 == 100 || tempf9 == 101) && (pf_current.compareTo(pf_const)==-1))
					{
						if(FunctionCallFlag == '0')
							AvgPFPenalty();
					}
					else if( Status == 1 || Status == 2 || Status == 14) */
					//if( Status == 1 || Status == 2 || Status == 14)
					if( Status == 1) //15-07-2016
					{
						if(FunctionCallFlag == '0')
							AvgPFPenalty();
					}
					else
					{
						if(FunctionCallFlag == '0')
							pf_calc_flag=true;
					}
				}
			}
			//24-03-14

			//24-03-14
			if(newpf.compareTo(new BigDecimal(0.00))==1)
			{
				//24-03-14
				if(pf_calc_flag)
				{	
					//if ( rtTvmMtr.equals("1") || rtTvmMtr.equals("2"))
					//25-06-2015
					if ( rtTvmMtr.equals("1") || rtTvmMtr.equals("2") || rtTvmMtr.equals("4"))
					{
						// capture KVAFR compare with KVAIR and for status 3 rotation
						// calculate rtKVA_Consumption
						//27-03-14
						if(Status == 3)
						{
							ir1 = pf_const;				
							ir2 = newpf;

							if (ir2.compareTo(new BigDecimal(0.00))==1)
							{
								ir1 = ir2;
							}
						}
						else
							ir1 = newpf;						
					}
					else
					{
						//27-03-14
						ir1 = newpf;
						ir2 = pf_const;			
					}

					rtPF1 = ir1.toString();					

					ir2 = ir1;
					ir1 = new BigDecimal(0.00);

					if (ir2.compareTo(new BigDecimal(0.00))==0) 
						ir2 = pf_const;

					if(ir2.compareTo(pf_const)==-1)
					{
						//Commented 21-04-2016
						/*ir1 = pf_const.subtract(ir2);
						ir1 = ir1.multiply(new BigDecimal(100.00)).multiply(PfPenalty);
						rtPfPenAmt = (ir1.compareTo(new BigDecimal(0.30))==1) ?  nou.multiply(new BigDecimal(0.30)) : nou.multiply(ir1);						*/

						//Added 21-04-2016
						if(tempf9>=60 && tempf9<=65)
						{
							rtPfPenAmt = billingobj.getmSancHp().multiply(new BigDecimal(5.00)).setScale(2,RoundingMode.HALF_UP);;
						}
						else
						{
							ir1 = pf_const.subtract(ir2);
							ir1 = ir1.multiply(new BigDecimal(100.00)).multiply(PfPenalty);
							rtPfPenAmt = (ir1.compareTo(new BigDecimal(0.30))==1) ?  nou.multiply(new BigDecimal(0.30)) : nou.multiply(ir1);						
						}
					}
					ir2 = new BigDecimal(0.00);
					ir1 = new BigDecimal(0.00);
					FunctionCallFlag = '1';
				}
				//24-03-14
			}
			//25-03-14
			billingobj.setmPF(rtPF1);
			billingobj.setmPfPenAmt(rtPfPenAmt.setScale(2, RoundingMode.HALF_UP)); //29-09-2016
		} 
		catch (Exception e) 
		{
			//25-03-14
			billingobj.setmPF("0.00");
			billingobj.setmPfPenAmt(new BigDecimal(0.00));

			Log.d(TAG, e.toString());
		}
	}
}
