package in.nsoft.hescomspotbilling;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.util.Log;
//Class Created by Tamilselvan on 12-02-2014 
public class PrinterCharacterAlign {

	public static int TotalChar = 48;
	public static int EndBalanceChar = 6;
	public static int StartBalanceChar = 2;
	/*public static int TotalChar = 52;
	public static int EndBalanceChar = 10;
	public static int StartBalanceChar = 2;*/
	StringBuilder sb = new StringBuilder();
	//Create By Tamilselvan on 12-02-2014	
	//Align Center
	public String CenterAlign(String strValue, String SpacingChar)
	{
		PrinterCharacterAlign pca = new PrinterCharacterAlign();
		int bal = 0;
		String rtr = "";
		StringBuilder sbcenter = new StringBuilder();
		sbcenter.delete(0, sbcenter.length());
		if(strValue.length() != 0)
		{
			if(strValue.length() < TotalChar)//IF Length Less than 72
			{
				bal = TotalChar - strValue.length();
				int b = bal/2;
				rtr = pca.CreateBlankSpaceSequence(b, SpacingChar);
				sbcenter.append(rtr);
				sbcenter.append(strValue);
				sbcenter.append(rtr);
			}
			else if(strValue.length() > TotalChar)
			{
				sbcenter.append(strValue.subSequence(0, TotalChar - 1));
			}
		}
		if(sbcenter.toString().length() < TotalChar)
		{
			bal = TotalChar - sbcenter.toString().length();
			sbcenter.append(pca.CreateBlankSpaceSequence(bal, SpacingChar));
		}
		return sbcenter.toString() + "\n";	//02-04-2021
	}
	//Create By Tamilselvan on 12-02-2014	
	//Align Center
	public String CenterAlign(String strValue, String SpacingChar, int totalLength)
	{
		PrinterCharacterAlign pca = new PrinterCharacterAlign();
		int bal = 0;
		String rtr = "";
		StringBuilder sbcenter = new StringBuilder();
		sbcenter.delete(0, sbcenter.length());
		if(strValue.length() != 0)
		{
			if(strValue.length() < totalLength)//IF Length Less than 72
			{
				bal = totalLength - strValue.length();
				int b = bal/2;
				rtr = pca.CreateBlankSpaceSequence(b, SpacingChar);
				sbcenter.append(rtr);
				sbcenter.append(strValue);
				sbcenter.append(rtr);
			}
			else if(strValue.length() > totalLength)
			{
				sbcenter.append(strValue.subSequence(0, totalLength - 1));
			}
		}
		if(sbcenter.toString().length() < totalLength)
		{
			bal = totalLength - sbcenter.toString().length();
			sbcenter.append(pca.CreateBlankSpaceSequence(bal, SpacingChar));
		}
		return sbcenter.toString();	
	}
	//Create By Tamilselvan on 12-02-2014
	//Align Right
	public  String RightAlign(String strValue, String SpacingChar)
	{
		PrinterCharacterAlign pca = new PrinterCharacterAlign();
		int bal = 0;
		String rtr = "";
		StringBuilder sbRight = new StringBuilder();
		sbRight.delete(0, sbRight.length());
		if(strValue.length() != 0)
		{
			if(strValue.length() < TotalChar)//IF Length Less than 72
			{
				bal = TotalChar - strValue.length();				
				rtr = pca.CreateBlankSpaceSequence(bal-1, SpacingChar);
				sbRight.append(rtr);
				sbRight.append(strValue);
			}
			else if(strValue.length() >= TotalChar)
			{
				sbRight.append(strValue.subSequence(0, TotalChar-1));
			}
		}
		return sbRight.toString();
	}
	//Create By Tamilselvan on 12-02-2014
	//Align Left
	public String LeftAlign(String strValue, String SpacingChar)
	{
		PrinterCharacterAlign pca = new PrinterCharacterAlign();
		int bal = 0;
		String rtr = "";
		StringBuilder sbleft = new StringBuilder();
		sbleft.delete(0, sbleft.length());

		//Modified Nitish 19-05-2014
		sbleft.append(CreateBlankSpaceSequence(2, SpacingChar));

		if(strValue.length() != 0)
		{
			if(strValue.length() < TotalChar)//IF Length Less than 72
			{
				bal = TotalChar - strValue.length();				
				rtr = pca.CreateBlankSpaceSequence(bal-1, SpacingChar);
				sbleft.append(strValue);
				sbleft.append(rtr);
			}
			else if(strValue.length() >= TotalChar)
			{
				sbleft.append(strValue.subSequence(0, TotalChar-1));
			}
		}
		return sbleft.toString();
	}
	//Create By Tamilselvan on 12-02-2014
	//Parallel Left Align
	public String ParallelLeftAlignfor72Char(String str1, String str2, String SpacingChar)
	{
		PrinterCharacterAlign pca = new PrinterCharacterAlign();
		int bal = 0;
		StringBuilder sb = new StringBuilder();
		if(str1.trim().length() !=0)
		{
			if(str1.trim().length() <= 36)
			{
				bal = 36 - str1.trim().length();
				sb.append(str1.trim());
				sb.append(pca.CreateBlankSpaceSequence(bal, SpacingChar));
			}
		}
		if(str2.length() !=0)
		{
			if(str2.trim().length() <= 36)
			{
				bal = 36 - str2.trim().length();
				sb.append(str2);
				sb.append(pca.CreateBlankSpaceSequence(bal-1, SpacingChar));
			}
		}
		return sb.toString();
	}
	//Create By Tamilselvan on 12-02-2014
	//Creating line with SpacingChar
	public String CreateBlankSpaceSequence(int Count, String SpacingChar)
	{
		String str = "";
		for(int i = 0; i < Count; i++)
		{
			str = str + SpacingChar;
		}
		return str;
	}

	//Created by Tamilselvan on 24-02-2014
	public String LeftAlignInBoxLayout(String strValue, String SpacingChar)
	{
		int bal = 0;
		sb.delete(0, sb.length());
		if(strValue.trim().length() < TotalChar)
		{
			bal = (TotalChar - StartBalanceChar) - strValue.trim().length();
			sb.append(CreateBlankSpaceSequence(StartBalanceChar, SpacingChar));
			sb.append(strValue.trim());
			sb.append(CreateBlankSpaceSequence(bal, SpacingChar));		
		}
		return sb.toString();
	}
	//Created by Tamilselvan on 24-02-2014
	public String RightAlignInBoxLayout(String strValue, String SpacingChar)
	{
		int bal = 0;
		sb.delete(0, sb.length());
		if(strValue.trim().length() < TotalChar)
		{
			bal = (TotalChar - EndBalanceChar) - strValue.trim().length();
			sb.append(CreateBlankSpaceSequence(bal, SpacingChar));
			sb.append(strValue.trim());
			sb.append(CreateBlankSpaceSequence(EndBalanceChar, SpacingChar));
		}
		return sb.toString();
	}
	//Created By Tamilselvan on 24-02-2014
	public String Center3Parameter(String Str1, String Str2, String Str3, String SpacingChar) 
	{
		int bal = 0;

		int z = 0;
		sb.delete(0, sb.length());
		sb.append(CreateBlankSpaceSequence(2, SpacingChar));//9
		if(Str1.length()<=12)
		{
			bal = 12 - Str1.length();			
			z= bal/2;
			sb.append(CreateBlankSpaceSequence(bal, SpacingChar));
			sb.append(Str1);
			//sb.append(CreateBlankSpaceSequence(z, SpacingChar));
		}//16
		sb.append(CreateBlankSpaceSequence(3, SpacingChar));//0
		if(Str2.length()<=8)
		{
			bal = 8 - Str2.length();			
			z= bal/2;
			sb.append(CreateBlankSpaceSequence(bal, SpacingChar));
			sb.append(Str2);
			//sb.append(CreateBlankSpaceSequence(z, SpacingChar));
		} //27
		sb.append(CreateBlankSpaceSequence(1, SpacingChar));
		if(Str3.length()<=12)
		{
			bal = 12 - Str3.length();			
			z= bal/2;
			sb.append(CreateBlankSpaceSequence(bal, SpacingChar));
			sb.append(Str3); 
			//sb.append(CreateBlankSpaceSequence(z, SpacingChar));
		}//44
		sb.append(CreateBlankSpaceSequence(10, SpacingChar));

		return sb.toString();//.substring(0, 51)
	}
	public BillingParameters getFCEC(ReadSlabNTarifSbmBillCollection sbc)
	{
		BillingParameters bp = new BillingParameters();
		String TariffString[] = null;
		try
		{
			//30-06-2021
			BigDecimal sancLoad = new BigDecimal(0.0);
			if(sbc.getmSancHp().compareTo(new BigDecimal(0.00)) > 0)
			{
				sancLoad = sbc.getmSancHp().setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			else if(sbc.getmSancKw().compareTo(new BigDecimal(0.00)) > 0)
			{
				sancLoad = sbc.getmSancKw().setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			
			//FC Slab
			if(sbc.getmFC_Slab_2().compareTo(new BigDecimal(0.00)) == 1)//Fc Slab -2
			{
				//Nitish Added 22-11-2017
				if(sbc.getmTariffCode()==120 || sbc.getmTariffCode()==121) //Only One Slab
				{
					BillingParameters.FixedCharges fc = (new BillingParameters()).new FixedCharges();//fc slab 1
					BigDecimal snLo = new BigDecimal(sbc.getmSancLoad().trim());
					if(snLo.compareTo(new BigDecimal(1)) <= 0)
					{
						fc.setUnitRange((snLo.divide(snLo)).toString());
					}
					else
					{
						fc.setUnitRange(sbc.getmSancLoad());
					}
					//Commented Nitish 21-04-2016
					//fc.setRate(String.valueOf(sbc.getMmFCRate_1().setScale(2, BigDecimal.ROUND_HALF_UP)));
					//21-04-2016
					try
					{
						if((sbc.getmTariffCode()==23 || sbc.getmTariffCode()==24) && (sbc.getmSancKw().compareTo(new BigDecimal(1.00)) > 0)) //Take FC rate2
						{
							fc.setRate(String.valueOf(sbc.getMmCOL_FCRate_2().setScale(2, BigDecimal.ROUND_HALF_UP)));
						}
						else
						{
							fc.setRate(String.valueOf(sbc.getMmFCRate_1().setScale(2, BigDecimal.ROUND_HALF_UP)));
						}
					}
					catch(Exception e)
					{
						fc.setRate(String.valueOf(sbc.getMmFCRate_1().setScale(2, BigDecimal.ROUND_HALF_UP)));
					}

					fc.setAmount(String.valueOf(sbc.getmTFc().setScale(2, BigDecimal.ROUND_HALF_UP)));
					bp.getmFixedCharges().add(fc);
				}//End 22-11-2017
				else // 2 Slabs
				{
					//24-06-2021
					BillingParameters.FixedCharges fc2 = (new BillingParameters()).new FixedCharges();
					BillingParameters.FixedCharges fc1 = (new BillingParameters()).new FixedCharges();//fc slab 1
					//fc1.setUnitRange("1");
					fc1.setUnitRange(String.valueOf(sbc.getMmNEWFC_UNIT1())); //26-06-2021
					fc1.setRate(String.valueOf(sbc.getMmFCRate_1().setScale(2, BigDecimal.ROUND_HALF_UP)));
					fc1.setAmount(String.valueOf(sbc.getmFixedCharges().setScale(2, BigDecimal.ROUND_HALF_UP)));
					bp.getmFixedCharges().add(fc1);

					//BigDecimal slab2Load = new BigDecimal(sbc.getmSancLoad())

					/*fc1.setUnitRange(String.valueOf(Float.valueOf(sbc.getmSancLoad()) - 1));
					fc1.setRate(String.valueOf(sbc.getMmCOL_FCRate_2().setScale(2, BigDecimal.ROUND_HALF_UP)));
					fc1.setAmount(String.valueOf(sbc.getmFC_Slab_2().setScale(2, BigDecimal.ROUND_HALF_UP)));
					bp.getmFixedCharges().add(fc1);	*/

					//24-06-2021
					if(sbc.getmTarifString().indexOf("LT-2")!= -1 )
					{
						/*if(new BigDecimal(sbc.getmSancLoad().trim()).compareTo(sbc.getMmNEWFC_UNIT2()) > 0) //If Sanc Load is greater than slab2
						{


							fc2.setUnitRange(String.valueOf(sbc.getMmNEWFC_UNIT2().subtract(sbc.getMmNEWFC_UNIT1()))); //Unit2-Unit1
							fc2.setRate(String.valueOf(sbc.getMmCOL_FCRate_2().setScale(2, BigDecimal.ROUND_HALF_UP))); //Rate2
							fc2.setAmount(String.valueOf(sbc.getMmFC_2().setScale(2, BigDecimal.ROUND_HALF_UP))); //FC2
							bp.getmFixedCharges().add(fc2);	

							BillingParameters.FixedCharges fc3 = (new BillingParameters()).new FixedCharges();
							fc3.setUnitRange(String.valueOf(new BigDecimal(sbc.getmSancLoad().trim()).subtract(sbc.getMmNEWFC_UNIT2()))); //SancLoad - Unit2
							fc3.setRate(String.valueOf(sbc.getMmNEWFC_Rate3().setScale(2, BigDecimal.ROUND_HALF_UP))); //Rate3
							fc3.setAmount(String.valueOf(sbc.getmFC_Slab_2().setScale(2, BigDecimal.ROUND_HALF_UP))); //FC3
							bp.getmFixedCharges().add(fc3);	

						}
						else
						{
							fc2.setUnitRange(String.valueOf(Float.valueOf(sbc.getmSancLoad()) - 1));
							fc2.setRate(String.valueOf(sbc.getMmCOL_FCRate_2().setScale(2, BigDecimal.ROUND_HALF_UP)));
							fc2.setAmount(String.valueOf(sbc.getmFC_Slab_2().setScale(2, BigDecimal.ROUND_HALF_UP)));
							bp.getmFixedCharges().add(fc2);	
						}*/

						if(sbc.getmFC_Slab_3().compareTo(new BigDecimal(0.00)) == 1)//Fc Slab -3
						{


							fc2.setUnitRange(String.valueOf(sbc.getMmNEWFC_UNIT2().subtract(sbc.getMmNEWFC_UNIT1()))); //Unit2-Unit1
							fc2.setRate(String.valueOf(sbc.getMmCOL_FCRate_2().setScale(2, BigDecimal.ROUND_HALF_UP))); //Rate2
							fc2.setAmount(String.valueOf(sbc.getMmFC_2().setScale(2, BigDecimal.ROUND_HALF_UP))); //FC2
							bp.getmFixedCharges().add(fc2);	

							BillingParameters.FixedCharges fc3 = (new BillingParameters()).new FixedCharges();
							fc3.setUnitRange(String.valueOf((sancLoad.subtract(sbc.getMmNEWFC_UNIT2())).setScale(2, BigDecimal.ROUND_HALF_UP)));
							fc3.setRate(String.valueOf(sbc.getMmNEWFC_Rate3().setScale(2, BigDecimal.ROUND_HALF_UP))); //Rate3
							fc3.setAmount(String.valueOf(sbc.getmFC_Slab_3().setScale(2, BigDecimal.ROUND_HALF_UP))); //FC3
							bp.getmFixedCharges().add(fc3);	

						}
						else
						{
							fc2.setUnitRange(String.valueOf((sancLoad.subtract(sbc.getMmNEWFC_UNIT1())).setScale(2, BigDecimal.ROUND_HALF_UP)));
							fc2.setRate(String.valueOf(sbc.getMmCOL_FCRate_2().setScale(2, BigDecimal.ROUND_HALF_UP)));
							fc2.setAmount(String.valueOf(sbc.getmFC_Slab_2().setScale(2, BigDecimal.ROUND_HALF_UP)));
							bp.getmFixedCharges().add(fc2);	
						}

					}
					else
					{
						fc2.setUnitRange(String.valueOf((sancLoad.subtract(sbc.getMmNEWFC_UNIT1())).setScale(2, BigDecimal.ROUND_HALF_UP)));
						fc2.setRate(String.valueOf(sbc.getMmCOL_FCRate_2().setScale(2, BigDecimal.ROUND_HALF_UP)));
						fc2.setAmount(String.valueOf(sbc.getmFC_Slab_2().setScale(2, BigDecimal.ROUND_HALF_UP)));
						bp.getmFixedCharges().add(fc2);	
					}
					//End 24-06-2021

				}
			}
			else  //One Slab
			{
				BillingParameters.FixedCharges fc = (new BillingParameters()).new FixedCharges();//fc slab 1
				BigDecimal snLo = new BigDecimal(sbc.getmSancLoad().trim());
				if(snLo.compareTo(new BigDecimal(1)) <= 0)
				{
					fc.setUnitRange((snLo.divide(snLo)).toString());
				}
				else
				{
					//fc.setUnitRange(sbc.getmSancLoad());
					//Added 29-05-2018
					if(sbc.getmTariffCode() == 126 ||sbc.getmTariffCode() == 127 || sbc.getmTariffCode() == 165 || sbc.getmTariffCode() == 94 ||sbc.getmTariffCode() == 95 || sbc.getmTariffCode() == 164)
					{						
						fc.setUnitRange(String.valueOf(roundoffQuarter(snLo.multiply(new BigDecimal(0.746)))));
					}
					else
						fc.setUnitRange(String.valueOf(sancLoad));
				}
				//Commented Nitish 21-04-2016
				//fc.setRate(String.valueOf(sbc.getMmFCRate_1().setScale(2, BigDecimal.ROUND_HALF_UP)));
				//21-04-2016
				try
				{
					//26-06-2021
					if((sbc.getmTariffCode()==23 || sbc.getmTariffCode()==24))
					{
						if(sbc.getmSancKw().compareTo(new BigDecimal(1.00)) > 0) //Take FC rate1
						{
							//fc.setRate(String.valueOf(sbc.getMmCOL_FCRate_2().setScale(2, BigDecimal.ROUND_HALF_UP)));
							fc.setRate(String.valueOf(sbc.getMmFCRate_1().setScale(2, BigDecimal.ROUND_HALF_UP))); //26-06-2021
						}
						else
						{
							//fc.setRate(String.valueOf(sbc.getMmFCRate_1().setScale(2, BigDecimal.ROUND_HALF_UP)));
							fc.setRate(String.valueOf(sbc.getmFixedCharges().setScale(2, BigDecimal.ROUND_HALF_UP))); //26-06-2021 Take Minimum Charge for FixedCharges which is equal to fixed charge slab1
						}
					}
					else
					{
						fc.setRate(String.valueOf(sbc.getMmFCRate_1().setScale(2, BigDecimal.ROUND_HALF_UP))); //26-06-2021
					}
				}
				catch(Exception e)
				{
					fc.setRate(String.valueOf(sbc.getMmFCRate_1().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
				//End 21-04-2016

				fc.setAmount(String.valueOf(sbc.getmTFc().setScale(2, BigDecimal.ROUND_HALF_UP)));
				bp.getmFixedCharges().add(fc);
			}
			//END FC Slab
			//EC Slabs
			BillingParameters.EnergyCharges Ec_Slab_1 = (new BillingParameters()).new EnergyCharges();//EC slab 1
			Ec_Slab_1.setUnitRange(GetWholeNumber(sbc.getMmUnits_1()));
			Ec_Slab_1.setRate(String.valueOf(sbc.getMmEC_Rate_1().setScale(2, BigDecimal.ROUND_HALF_UP)));
			Ec_Slab_1.setAmount(String.valueOf(sbc.getMmEC_1().setScale(2, BigDecimal.ROUND_HALF_UP)));
			bp.getmEnergyCharges().add(Ec_Slab_1);

			BillingParameters.EnergyCharges Ec_Slab_2 = (new BillingParameters()).new EnergyCharges();//EC slab 2
			Ec_Slab_2.setUnitRange(GetWholeNumber(sbc.getMmUnits_2()));
			Ec_Slab_2.setRate(String.valueOf(sbc.getMmEC_Rate_2().setScale(2, BigDecimal.ROUND_HALF_UP)));
			Ec_Slab_2.setAmount(String.valueOf(sbc.getMmEC_2().setScale(2, BigDecimal.ROUND_HALF_UP)));
			bp.getmEnergyCharges().add(Ec_Slab_2);

			BillingParameters.EnergyCharges Ec_Slab_3 = (new BillingParameters()).new EnergyCharges();//EC slab 3
			Ec_Slab_3.setUnitRange(GetWholeNumber(sbc.getMmUnits_3()));
			Ec_Slab_3.setRate(String.valueOf(sbc.getMmEC_Rate_3().setScale(2, BigDecimal.ROUND_HALF_UP)));
			Ec_Slab_3.setAmount(String.valueOf(sbc.getMmEC_3().setScale(2, BigDecimal.ROUND_HALF_UP)));
			bp.getmEnergyCharges().add(Ec_Slab_3);

			BillingParameters.EnergyCharges Ec_Slab_4 = (new BillingParameters()).new EnergyCharges();//EC slab 4
			Ec_Slab_4.setUnitRange(GetWholeNumber(sbc.getMmUnits_4()));
			Ec_Slab_4.setRate(String.valueOf(sbc.getMmEC_Rate_4().setScale(2, BigDecimal.ROUND_HALF_UP)));
			Ec_Slab_4.setAmount(String.valueOf(sbc.getMmEC_4().setScale(2, BigDecimal.ROUND_HALF_UP)));
			bp.getmEnergyCharges().add(Ec_Slab_4);
			//END EC Slabs
			//Nitish Added Extra EC Slabs 14-04-2017
			BillingParameters.EnergyCharges Ec_Slab_5 = (new BillingParameters()).new EnergyCharges();//EC slab 5
			Ec_Slab_5.setUnitRange(GetWholeNumber(sbc.getMmUnits_5()));
			Ec_Slab_5.setRate(String.valueOf(sbc.getMmEC_Rate_5().setScale(2, BigDecimal.ROUND_HALF_UP)));
			Ec_Slab_5.setAmount(String.valueOf(sbc.getMmEC_5().setScale(2, BigDecimal.ROUND_HALF_UP)));
			bp.getmEnergyCharges().add(Ec_Slab_5);

			BillingParameters.EnergyCharges Ec_Slab_6 = (new BillingParameters()).new EnergyCharges();//EC slab 6
			Ec_Slab_6.setUnitRange(GetWholeNumber(sbc.getMmUnits_6()));
			Ec_Slab_6.setRate(String.valueOf(sbc.getMmEC_Rate_6().setScale(2, BigDecimal.ROUND_HALF_UP)));
			Ec_Slab_6.setAmount(String.valueOf(sbc.getMmEC_6().setScale(2, BigDecimal.ROUND_HALF_UP)));
			bp.getmEnergyCharges().add(Ec_Slab_6);
			//Nitish End Extra EC Slabs 14-04-2017
			//END EC Slabs

		}
		catch(Exception e)
		{
			Log.d("Tamil", e.toString());
		}
		return bp;
	}
	
	public String TwoParallelStringRightAlign(String str1, String str2, char SpacingChar)
	{
		int bal = 0;			
		sb.delete(0, sb.length());
		int part = (TotalChar/2);
		if(str1.length() <= part)
		{
			bal = part - str1.length();
			sb.append(CreateBlankSpaceSequence(bal, String.valueOf(SpacingChar)));
			sb.append(str1);						
		}
		if(str2.length() <= part)
		{
			bal = (part - EndBalanceChar) - str2.length();
			sb.append(CreateBlankSpaceSequence(bal, String.valueOf(SpacingChar)));	
			sb.append(str2);
			sb.append(CreateBlankSpaceSequence(EndBalanceChar, String.valueOf(SpacingChar)));			
		}
		return sb.toString();
	}
	//Nitish 10-04-2014
	//Seperate Printing into two columns
	public String TwoParallelString(String str1, String str2, char SpacingChar, char seperator)
	{
		int bal = 0;
		sb.delete(0, sb.length());
		//Modified Nitish 19-05-2014
		sb.append(CreateBlankSpaceSequence(2,  String.valueOf(SpacingChar)));
		int part = ((TotalChar/2)-1);

		if(str1.length() <= part)
		{
			bal = part - str1.length();			
			sb.append(str1);
			sb.append(CreateBlankSpaceSequence(bal, String.valueOf(SpacingChar)));			
		}
		str2 = seperator + str2;
		if(str2.length() <= part)
		{
			bal = part - str2.length();
			sb.append(str2);
			sb.append(CreateBlankSpaceSequence(bal, String.valueOf(SpacingChar)));			
		}		
		return sb.toString() + "\n"; //02-04-2021
	}

	//Seperate Printing into three columns
	public String ThreeParallelString(String str1, String str2,String str3, char SpacingChar)
	{
		int bal = 0;		
		sb.delete(0, sb.length());
		//Modified Nitish 19-05-2014
		sb.append(CreateBlankSpaceSequence(2,  String.valueOf(SpacingChar)));		
		int part = (((TotalChar-1)/3)-1);

		if(str1.length() <= part)
		{
			bal = part - str1.length();			
			sb.append(str1);
			sb.append(CreateBlankSpaceSequence(bal, String.valueOf(SpacingChar)));			
		}		
		if(str2.length() <= part)
		{
			bal = part - str2.length();
			sb.append(str2);
			sb.append(CreateBlankSpaceSequence(bal, String.valueOf(SpacingChar)));			
		}
		if(str3.length() <= part)
		{
			bal = part - str3.length();
			sb.append(str3);
			sb.append(CreateBlankSpaceSequence(bal, String.valueOf(SpacingChar)));			
		}
		return sb.toString()+ "\n"; //02-04-2021
	}
	//Provide Line Seperation
	public String LineSeperation()
	{
		String LineText =null;
		//if(length == TotalChar)
		LineText = CreateBlankSpaceSequence(TotalChar, "-");//52 
		return LineText + "\n"; //02-04-2021
	}

	//End Nitish

	//punit 24032014

	public String perticulartext(String strValue, String SpacingChar)
	{
		int bal = 0;
		sb.delete(0, sb.length());
		if(strValue.trim().length() < 28)
		{
			bal = (28 - 3) - strValue.trim().length();
			sb.append(CreateBlankSpaceSequence(bal, SpacingChar));
			sb.append(strValue.trim());
			sb.append(CreateBlankSpaceSequence(3, SpacingChar));
		}
		return sb.toString();
	}
	public String DoubleFonttext(String strValue, String SpacingChar)
	{

		sb.delete(0, sb.length());
		if(strValue.trim().length() < 20)
		{
			sb.append(CreateBlankSpaceSequence(24, SpacingChar));
			sb.append(strValue.trim());			
		}
		else
		{

		}
		return sb.toString();
	}
	public String sbname(String strValue, String SpacingChar)
	{
		int bal = 0;
		sb.delete(0, sb.length());
		if(strValue.trim().length() < 30)
		{
			bal = (30 - 7) - strValue.trim().length();
			sb.append(CreateBlankSpaceSequence(bal, SpacingChar));
			sb.append(strValue.trim());
			sb.append(CreateBlankSpaceSequence(7, SpacingChar));
		}
		return sb.toString();
	}
	//punit stop 24032014
	//Tamilselvan on 27-03-2014
	public String GetWithDrawal(String Amt)
	{
		String str = "";
		if(Amt.equals("0.00"))
		{
			str = "0.00";
		}
		else
		{
			if(Amt.length() > 0)
			{
				int a = 10 - Amt.length(); 
				str = " WA - " + CreateBlankSpaceSequence(a, " ") + Amt;
			}
			else
			{
				str = "0.00";
			}
		}		
		return str;
	}
	//Tamilselvan on 28-03-2014
	public String GetWholeNumber(BigDecimal val)
	{
		String str = "";
		//String[] t = {};
		if(val.toString().indexOf(".") != -1)
		{
			//30-10-2014
			//str = (val.toString().split("\\."))[0];
			str = val.setScale(0, RoundingMode.HALF_UP).toString();
			//End 30-10-2014

			/*//29-10-2014
			str = (val.toString().split("\\."))[0];
			t = (val.setScale(2,RoundingMode.HALF_UP ).toString().split("\\."));
			str = t[0].toString();
			BigDecimal splitVal = new BigDecimal(t[1].toString());
			if(splitVal.compareTo(new BigDecimal(50)) >= 0)
			{
				str = new BigDecimal(str).add(new BigDecimal(1)).toString();
			}
			//End 29-10-2014*/
		}
		else
		{
			str = val.toString();
		}
		return str;
	}
	//Nitish 12-05-2014
	public String ThreeParallelStringRRNoCollReport(String str1, String str2,String str3, char SpacingChar)
	{
		int bal = 0;			
		sb.delete(0, sb.length());
		//Modified Nitish 19-05-2014				
		sb.append(CreateBlankSpaceSequence(2,  String.valueOf(SpacingChar)));
		int part = ((TotalChar-1)/3);
		if(str1.length() <= part) //For 48 Characters ->18 Char 1 column
		{
			bal = part - str1.length() + (part/4);
			sb.append(str1);
			sb.append(CreateBlankSpaceSequence(bal, String.valueOf(SpacingChar)));			
		}		
		if(str2.length() <= part) //For 48 Characters ->12 Char 2 column
		{
			bal = part - str2.length() - (part/4);
			sb.append(str2);
			sb.append(CreateBlankSpaceSequence(bal, String.valueOf(SpacingChar)));			
		}
		if(str3.length() <= part) //For 48 Characters ->12 Char 3 column
		{
			bal = part - str3.length() - (part/4);		
			sb.append(CreateBlankSpaceSequence(bal, String.valueOf(SpacingChar)));	
			sb.append(str3);
		}
		return sb.toString();
	}
	//Nitish 12-05-2014
	public String FourParallelStringRRNoCollReport(String str1, String str2,String str3, String str4, char SpacingChar)
	{
		int bal = 0;			
		sb.delete(0, sb.length());
		//Modified Nitish 19-05-2014				
		sb.append(CreateBlankSpaceSequence(2,  String.valueOf(SpacingChar)));
		
		//int part = ((TotalChar-1)/3);
		//Modified 06-03-2021
		int tot = 48;
		int part = ((tot-1)/3);
		if(str1.length() <= (part)) //For 48 Characters ->18 Char 1 column //Modified 06-03-2021 
		{
			bal = part - str1.length()-1;
			sb.append(str1);
			sb.append(CreateBlankSpaceSequence(bal, String.valueOf(SpacingChar)));			
		}		
		if(str2.length() <= part) //For 48 Characters ->12 Char 2 column
		{
			bal = part - str2.length() - (part/2);
			sb.append(str2);
			sb.append(CreateBlankSpaceSequence(bal, String.valueOf(SpacingChar)));			
		}
		if(str3.length() <= part) //For 48 Characters ->12 Char 3 column
		{
			bal = part - str3.length() - (part/2);	
			sb.append(str3);
			sb.append(CreateBlankSpaceSequence(bal, String.valueOf(SpacingChar)));	
		}
		if(str4.length() <= part) //For 48 Characters ->12 Char 3 column
		{
			bal = part - str4.length() - (part/4);			
			sb.append(CreateBlankSpaceSequence(bal, String.valueOf(SpacingChar)));	
			sb.append(str4);
		}
		return sb.toString()+"\n";				
	}

	//25-06-2015
	public BigDecimal roundoffQuarter(BigDecimal value)
	{
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

			return null;
		}	
	}
	//26-09-2015
	public String DoubleFonttextRRNo(String strValue, String SpacingChar)
	{

		sb.delete(0, sb.length());
		if(strValue.trim().length() < 22)
		{
			sb.append(CreateBlankSpaceSequence(15, SpacingChar));
			sb.append(strValue.trim());			
		}
		else
		{

		}
		return sb.toString();
	}
	//Nitish For BarCodeLength 19-12-2015
	public String ConnectionNoTrim(String connectionno)
	{

		int zeroCount = 7 - (connectionno.trim().length()); //7 digits
		String str = connectionno;
		for(int i = 0; i < zeroCount; i++)
		{
			str = "0" + str ;
		}
		return str;
	}

	//02-04-2021
	public String ThreeParallelStringKannada(String str1, String str2,String str3, char SpacingChar)
	{
		int bal = 0;
		TotalChar = 36;
		sb.delete(0, sb.length());
		//Modified Nitish 19-05-2014
		sb.append(CreateBlankSpaceSequence(2,  String.valueOf(SpacingChar)));		
		int part = 12;

		if(str1.length() <= part)
		{
			bal = part - str1.length();			
			sb.append(str1);
			sb.append(CreateBlankSpaceSequence(bal, String.valueOf(SpacingChar)));
			if(bal>=4)
				sb.append(CreateBlankSpaceSequence(bal, String.valueOf(SpacingChar)));
			else
				sb.append(CreateBlankSpaceSequence(bal/2, String.valueOf(SpacingChar)));


		}		
		if(str2.length() <= part)
		{
			bal = part - str2.length();			
			sb.append(str2);
			sb.append(CreateBlankSpaceSequence(bal, String.valueOf(SpacingChar)));
			sb.append(CreateBlankSpaceSequence(bal, String.valueOf(SpacingChar)));

		}
		if(str3.length() <= part)
		{
			bal = part - str3.length();			
			sb.append(CreateBlankSpaceSequence(bal, String.valueOf(SpacingChar)));	
			if(bal>=4)
				sb.append(CreateBlankSpaceSequence(bal, String.valueOf(SpacingChar)));	
			else
				sb.append(CreateBlankSpaceSequence(bal/2, String.valueOf(SpacingChar)));
			sb.append(str3);

		}
		return sb.toString();
	}

	public String RightAlignInBoxLayoutNew3(String strValue2, String SpacingChar , int rightValue)
	{

		int bal=0;
		sb.delete(0, sb.length());		
		strValue2 = strValue2.trim();
		//sb.append(CreateBlankSpaceSequence(leftspace, SpacingChar));
		//sb.append(":");
		sb.append(" ");
		if(strValue2.trim().length() <= rightValue)
		{
			bal = rightValue  - strValue2.trim().length();
			sb.append(CreateBlankSpaceSequence(bal, SpacingChar));
			sb.append(strValue2.trim());				
		}
		else
		{
			sb.append(strValue2.substring(0, (rightValue-2)).trim());	
		}
		return sb.toString();
	}

}   
