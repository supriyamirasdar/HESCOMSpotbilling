package in.nsoft.hescomspotbilling;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.util.Log;

public class Calculate
{
	ReadSlabNTarifSbmBillCollection billObj;
	Context mcx;

	public Calculate(Context cx)
	{
		this.mcx=cx;
		billObj =BillingObject.GetBillingObject();
	}
	/**
	 * Consumption Calculation
	 */
	public void consumption()
	{
		try 
		{
			//BigDecimal PrevRdg = new BigDecimal(billObj.getmPrevRdg().trim());
			//PrevRdg = PrevRdg.setScale(0, BigDecimal.ROUND_FLOOR);
			//PrevRdg.toString().length();
			//Modified 05-07-2016
			BigDecimal PrevRdg = new BigDecimal(billObj.getmPrevRdg().trim()).setScale(2, BigDecimal.ROUND_FLOOR);			
			int lenPrevRdg = PrevRdg.setScale(0, BigDecimal.ROUND_FLOOR).toString().length();

			if (billObj.getmMtd().equals("1"))
			{
				//Nitish 25-06-2015 No IR Reset
				/*if(billObj.getmTvmMtr().equals("1"))
				{
					billObj.setmPrevRdg("0");
					//billObj.setmUnits(new BigDecimal( billObj.getmPreRead()).multiply(billObj.getmMF()).toString());
					//billObj.setmUnits( new BigDecimal(billObj.getmPreRead()).add((new BigDecimal((Math.pow(10, PrevRdg.toString().length())))).subtract(PrevRdg)).multiply(billObj.getmMF()).toString());
				}*/
				if(billObj.getmTarifString().indexOf("LT-7")!= -1)
				{
					if(billObj.getmPreStatus().equals("2") || billObj.getmPreStatus().equals("14"))
					{
						if(billObj.getmSancKw().compareTo(billObj.getmSancHp()) > 0)
						{
							billObj.setmUnits(String.valueOf(new BigDecimal(12).multiply(new BigDecimal(billObj.getmNoOfDays().trim())).multiply(billObj.getmSancKw())));
						}
						else
						{
							billObj.setmUnits(String.valueOf(new BigDecimal(12).multiply(new BigDecimal(billObj.getmNoOfDays().trim())).multiply(billObj.getmSancHp()).multiply(new BigDecimal(0.746))));
						}
						return;
					}
				}
				if(( (billObj.getmPreStatus().equals("1") || billObj.getmPreStatus().equals("2") || billObj.getmPreStatus().equals("5") || billObj.getmPreStatus().equals("14") ) && billObj.getmDLAvgMin().equals("2")) ||((billObj.getmPreStatus().equals("6")) || billObj.getmPreStatus().equals("7") || billObj.getmPreStatus().equals("8") || billObj.getmPreStatus().equals("15")))
				{
					billObj.setmUnits("0");
				}
				else if(((billObj.getmPreStatus().equals("1") || billObj.getmPreStatus().equals("2") || billObj.getmPreStatus().equals("5") || billObj.getmPreStatus().equals("14") ) && billObj.getmDLAvgMin().equals("1")) ||(billObj.getmPreStatus().equals("9") || billObj.getmPreStatus().equals("10") || billObj.getmPreStatus().equals("11")))
				{
					//if(billObj.getmPreStatus().equals("1") || billObj.getmPreStatus().equals("2") || billObj.getmPreStatus().equals("4") || billObj.getmPreStatus().equals("5") || billObj.getmPreStatus().equals("14"))
					if(billObj.getmPreStatus().equals("1") || billObj.getmPreStatus().equals("2") || billObj.getmPreStatus().equals("4") || billObj.getmPreStatus().equals("5") || billObj.getmPreStatus().equals("14")||billObj.getmPreStatus().equals("9") || billObj.getmPreStatus().equals("10") || billObj.getmPreStatus().equals("11"))	//30-08-2016 Take Avg for these Reasons 
					{
						//billObj.setmUnits(billObj.getmAvgCons()); //Commented 21-04-2016
						//21-04-2016
						if((billObj.getmPreStatus().equals("1")|| billObj.getmPreStatus().equals("5")) && (billObj.getmTariffCode()==1 && billObj.getmSancLoad().trim().equals("0.04")))
						{
							//billObj.setmUnits("18");
							//22-04-2017
							billObj.setmUnits("40");
						}
						else
						{
							billObj.setmUnits(billObj.getmAvgCons());
						}
					}
					else
					{
						if(billObj.getmMF().compareTo(new BigDecimal(0.00)) > 0)
						{

							billObj.setmUnits(String.valueOf(new BigDecimal(billObj.getmAvgCons().trim()).multiply(billObj.getmMF())));
						}
						else
						{
							billObj.setmUnits(billObj.getmAvgCons());
						}
					}//END Modified by Tamilselvan on 22-04-2014 (reference Gautham)
				}
				else if( billObj.getmPreStatus().equals("3"))
				{
					//Modified by Tamilselvan on 16-04-2014
					//MF > 0 Then Multiply with MF 
					if(billObj.getmMF().compareTo(new BigDecimal(0.00)) > 0)
					{
						//billObj.setmUnits( new BigDecimal(billObj.getmPreRead()).add((new BigDecimal((Math.pow(10, PrevRdg.toString().length())))).subtract(PrevRdg)).multiply(billObj.getmMF()).toString());
						//05-07-2016
						billObj.setmUnits( new BigDecimal(billObj.getmPreRead()).add((new BigDecimal((Math.pow(10,lenPrevRdg)))).subtract(PrevRdg)).multiply(billObj.getmMF()).toString());

					}
					else
					{
						//billObj.setmUnits( new BigDecimal(billObj.getmPreRead()).add((new BigDecimal((Math.pow(10, PrevRdg.toString().length())))).subtract(PrevRdg)).toString());
						//05-07-2016
						billObj.setmUnits( new BigDecimal(billObj.getmPreRead()).add((new BigDecimal((Math.pow(10, lenPrevRdg)))).subtract(PrevRdg)).toString());
					}
				}
				else if( billObj.getmPreStatus().equals("0"))
				{
					//Modified by Tamilselvan on 16-04-2014
					//MF > 0 Then Multiply with MF 
					if(billObj.getmMF().compareTo(new BigDecimal(0.00)) > 0)
					{
						billObj.setmUnits( new BigDecimal(billObj.getmPreRead()).subtract(PrevRdg).multiply(billObj.getmMF()).toString());
					}
					else
					{
						billObj.setmUnits( new BigDecimal(billObj.getmPreRead()).subtract(PrevRdg).toString());
					}
				}

				else if( billObj.getmPreStatus().equals("4") && billObj.getmMCHFlag().equals("1"))
				{
					//Modified by Tamilselvan on 16-04-2014
					//MF > 0 Then Multiply with MF 
					if(billObj.getmMF().compareTo(new BigDecimal(0.00)) > 0)
					{
						billObj.setmUnits( new BigDecimal(billObj.getmPreRead()).subtract(PrevRdg).multiply(billObj.getmMF()).add( new BigDecimal(billObj.getmOld_Consumption().trim())).toString());
					}
					else
					{
						billObj.setmUnits( new BigDecimal(billObj.getmPreRead()).subtract(PrevRdg).add( new BigDecimal(billObj.getmOld_Consumption().trim())).toString());
					}

				}
			}
			else
			{
				billObj.setmUnits(billObj.getmAvgCons());
			}
			//Added by Tamilselvan on 28-06-2014
			try
			{
				//Nitish 29-09-2015							
				//BigDecimal RFC = (new BigDecimal(billObj.getmUnits().trim()).multiply(new BigDecimal(0.10))).setScale(2, BigDecimal.ROUND_HALF_UP);
				//Modified 19-12-2015
				BigDecimal RFC = (new BigDecimal(billObj.getmUnits().trim()).multiply(new BigDecimal(billObj.getmFACValue().toString().trim()))).setScale(2, BigDecimal.ROUND_HALF_UP);
				billObj.setmKVAAssd_Cons(RFC);
				//billObj.setmKVAAssd_Cons(new BigDecimal(0));

			}
			catch (Exception e)
			{
				Log.d("Consumption", e.toString());
			}

			/*if ( SlowRtn > 0 && (sts1 == 0 || sts1 == 3))
			{					   
				nou = (nou * 100) / (float)(100 - fabs(SlowRtn));
				nou = round(nou);
			}
			if ( SlowRtn < 0 && (sts1 == 0 || sts1 == 3))
			{					   
				nou = (nou * 100) / (float)(100 + fabs(SlowRtn));
				nou = round(nou);
			}*/

			//Nitish 25-06-2015 Slow Rotation			
			if(billObj.getmSlowRtnPge().compareTo(new BigDecimal(0.00)) > 0 && (billObj.getmPreStatus().equals("0") ||  billObj.getmPreStatus().equals("3")) )
			{
				BigDecimal nou1 = new BigDecimal(BillingObject.GetBillingObject().getmUnits()).multiply(new BigDecimal(100));
				BigDecimal SlowRtn1 = (new BigDecimal(100)).subtract(new BigDecimal(Math.abs(billObj.getmSlowRtnPge().doubleValue())));
				billObj.setmUnits((nou1.divide(SlowRtn1,MathContext.DECIMAL128)).setScale(0, RoundingMode.HALF_UP).toString());

			}
			if(billObj.getmSlowRtnPge().compareTo(new BigDecimal(0.00)) < 0 && (billObj.getmPreStatus().equals("0") ||  billObj.getmPreStatus().equals("3")))
			{
				BigDecimal nou1 = new BigDecimal(BillingObject.GetBillingObject().getmUnits()).multiply(new BigDecimal(100));
				BigDecimal SlowRtn1 = (new BigDecimal(100)).add(new BigDecimal(Math.abs(billObj.getmSlowRtnPge().doubleValue())));
				billObj.setmUnits(nou1.divide(SlowRtn1,MathContext.DECIMAL128).setScale(0, RoundingMode.HALF_UP).toString());
			}
			//Nitish 23-02-2017 
			billObj.setmUnits(String.valueOf(new BigDecimal(billObj.getmUnits()).setScale(0,RoundingMode.HALF_UP)));

		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	public void fc()
	{ 
		char[] ch = new char[50];
		char[] ch1 = new char[50];
		String str1 = new String(ch);
		String str4 = "0";
		String str5 = "0";
		String slab1 = "0";
		String rate1 = "0";
		String slab2 = "0";
		String rate2 = "0";
		//23-06-2021
		String slab3 = "0";
		String rate3 = "0";
		String slab4 = "0";
		String rate4 = "0";
		try 
		{
			String str = billObj.getmTarifString();
			for(int i = 0; str.charAt(i) != '@'; i++)
			{
				ch[i] = str.charAt(i);
			}
			str1 = String.valueOf(ch);
			String slabtarif1[] = str1.split("%%");
			str4 = slabtarif1[1];
			String slabtarif[] = str4.split("#");
			int count = str1.length() - str1.replaceAll("\\#","").length();
			if(count == 3)
			{
				slab1 = slabtarif[2].trim();
				rate1 = slabtarif[3].trim();
			}
			else if(count == 5)
			{
				slab1 = slabtarif[2].trim();
				rate1 = slabtarif[3].trim();
				slab2 = slabtarif[4].trim();
				rate2 = slabtarif[5].trim();
			}
			//23-06-2021
			else if(count == 7) 
			{
				slab1 = slabtarif[2].trim();
				rate1 = slabtarif[3].trim();
				slab2 = slabtarif[4].trim();
				rate2 = slabtarif[5].trim();
				slab3 = slabtarif[6].trim();
				rate3 = slabtarif[7].trim();
			}
			else if(count == 9) 
			{
				slab1 = slabtarif[2].trim();
				rate1 = slabtarif[3].trim();
				slab2 = slabtarif[4].trim();
				rate2 = slabtarif[5].trim();
				slab3 = slabtarif[6].trim();
				rate3 = slabtarif[7].trim();
				slab4 = slabtarif[8].trim();
				rate4 = slabtarif[9].trim();
			}
			try
			{
				billObj.setMmNEWFC_UNIT1(new BigDecimal(slab1));
				billObj.setMmNEWFC_UNIT2(new BigDecimal(slab2));
				billObj.setMmNEWFC_UNIT3(new BigDecimal(slab3));
				billObj.setMmNEWFC_UNIT4(new BigDecimal(slab4));

				billObj.setMmFCRate_1(new BigDecimal(rate1).setScale(2, RoundingMode.HALF_UP));
				billObj.setMmCOL_FCRate_2(new BigDecimal(rate2).setScale(2, RoundingMode.HALF_UP));
				billObj.setMmNEWFC_Rate3(new BigDecimal(rate3).setScale(2, RoundingMode.HALF_UP));
				billObj.setMmNEWFC_Rate4(new BigDecimal(rate4).setScale(2, RoundingMode.HALF_UP));

				billObj.setMmFC_1(billObj.getmFixedCharges().setScale(2, RoundingMode.HALF_UP));
				billObj.setMmFC_2(billObj.getmFC_Slab_2().setScale(2, RoundingMode.HALF_UP));
				billObj.setMmNEWFC3(billObj.getmFC_Slab_3().setScale(2, RoundingMode.HALF_UP));
				billObj.setMmNEWFC4(new BigDecimal("0.0").setScale(2, RoundingMode.HALF_UP));
			}


			catch(Exception e)
			{

			}
			//End 23-06-2021
			/*if(billObj.getmDayWise_Flag().equals("Y"))//Converting rate for No of Days
			{
				billObj.setMmFCRate_1((new BigDecimal(rate1).divide(new BigDecimal(30), MathContext.DECIMAL128)).multiply(new BigDecimal(billObj.getmNewNoofDays().trim())));
				billObj.setMmCOL_FCRate_2((new BigDecimal(rate2).divide(new BigDecimal(30), MathContext.DECIMAL128)).multiply(new BigDecimal(billObj.getmNewNoofDays().trim())));
				billObj.setmTFc((billObj.getmFixedCharges()).add(billObj.getmFC_Slab_2()));
			}
			else
			{
				billObj.setMmFCRate_1(new BigDecimal(rate1));
				billObj.setMmCOL_FCRate_2(new BigDecimal(rate2));
			}

			if(billObj.getmDayWise_Flag().equals("N"))//Converting rate for No of Days
			{
				if(billObj.getmTariffCode()==1 || billObj.getmTariffCode()==3)
				{					
					BigDecimal avgCon = new BigDecimal(0.00);
					if(billObj.getmPreStatus().equals("0") || billObj.getmPreStatus().equals("3") || billObj.getmPreStatus().equals("4"))
					{
						avgCon = (new BigDecimal(billObj.getmUnits().trim()).setScale(0, BigDecimal.ROUND_FLOOR)).divide(new BigDecimal(billObj.getmDlCount()), MathContext.DECIMAL128);//.setScale(0, BigDecimal.ROUND_HALF_UP);
					}
					else
					{
						avgCon = (new BigDecimal(billObj.getmUnits().trim()).setScale(0, BigDecimal.ROUND_FLOOR)).divide(new BigDecimal(billObj.getmBillFor().trim()), MathContext.DECIMAL128);//.setScale(0, BigDecimal.ROUND_HALF_UP);
					}				
					//Nitish Modified 15-04-2017 for LT-1
					if((avgCon.compareTo(new BigDecimal(40)) <= 0) && (billObj.getmSancKw().compareTo(new BigDecimal(0.04)) <= 0))					
					{
						billObj.setmTFc(new BigDecimal(0.00));	
					}
					else 
					{
						//billObj.setmTFc((billObj.getmFixedCharges()).add(billObj.getmFC_Slab_2()));
						billObj.setmTFc(billObj.getMmFCRate_1().add(billObj.getmFC_Slab_2())); //29-03-2016
					}

				}
				else
				{
					billObj.setmTFc((billObj.getmFixedCharges()).add(billObj.getmFC_Slab_2()));
				}
			}*/
			//Nitish 19-12-2017 for LT-7 Weekly Billing
			billObj.setmIssueDateTime(new SimpleDateFormat("ddMMyyHHmmss").format(Calendar.getInstance().getTime()).trim());
			if(billObj.getmDayWise_Flag().equals("Y"))//Converting rate for No of Days
			{
				try
				{					
					if(billObj.getmTariffCode()==120 || billObj.getmTariffCode()==121)
					{						
						int lt7noofDays = Integer.valueOf(new SimpleDateFormat("dd").format(Calendar.getInstance().getTime())) + Integer.valueOf(billObj.getmNewNoofDays().trim());
						int noofweeks = ((new BigDecimal(lt7noofDays).divide(new BigDecimal(7.00), MathContext.DECIMAL128))).setScale(0, RoundingMode.CEILING).intValue();
						int lt7billIssueDay = 0;
						if((lt7noofDays%7)!=0)
						{
							lt7billIssueDay = 7 - (lt7noofDays%7);
						}
						billObj.setMmFCRate_1((new BigDecimal(rate1)).setScale(2, RoundingMode.HALF_UP));
						billObj.setMmCOL_FCRate_2((new BigDecimal(rate2)).setScale(2, RoundingMode.HALF_UP));
						//Added Nitish 22-11-2017
						//BigDecimal fc1Week = (billObj.getmFixedCharges().add(billObj.getmFC_Slab_2())).divide(new BigDecimal(4.00), MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_UP);
						BigDecimal fc1Week = (new BigDecimal(rate1.trim())).divide(new BigDecimal(4.00), MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_UP);
						//billObj.setmTFc((fc1Week.multiply(new BigDecimal(noofweeks))).setScale(2, RoundingMode.HALF_UP));
						billObj.setmTFc(new BigDecimal(billObj.getmSancLoad().trim()).multiply(fc1Week.multiply(new BigDecimal(noofweeks))).setScale(2, RoundingMode.HALF_UP));
						//End  22-11-2017
						Calendar c = Calendar.getInstance();    
						c.add(Calendar.DATE, lt7billIssueDay);						
						billObj.setmIssueDateTime(new SimpleDateFormat("ddMMyyHHmmss").format(c.getTime()).trim());						
					}
					else
					{
						billObj.setMmFCRate_1((new BigDecimal(rate1).divide(new BigDecimal(30), MathContext.DECIMAL128)).multiply(new BigDecimal(billObj.getmNewNoofDays().trim())));
						billObj.setMmCOL_FCRate_2((new BigDecimal(rate2).divide(new BigDecimal(30), MathContext.DECIMAL128)).multiply(new BigDecimal(billObj.getmNewNoofDays().trim())));
						billObj.setmTFc((billObj.getmFixedCharges()).add(billObj.getmFC_Slab_2()));
					}

				}
				catch(Exception e)
				{

				}
			}
			else
			{
				billObj.setMmFCRate_1(new BigDecimal(rate1));
				billObj.setMmCOL_FCRate_2(new BigDecimal(rate2));
			}			
			if(billObj.getmDayWise_Flag().equals("N"))//Converting rate for No of Days
			{
				if(billObj.getmTariffCode()==1 || billObj.getmTariffCode()==3)
				{					
					BigDecimal avgCon = new BigDecimal(0.00);
					if(billObj.getmPreStatus().equals("0") || billObj.getmPreStatus().equals("3") || billObj.getmPreStatus().equals("4")) //If Current Status is Normal
					{						
						//31-07-2015
						if(billObj.getmStatus()==6) //If PrevStatus  is 6
						{							
							avgCon = (new BigDecimal(billObj.getmUnits().trim()).setScale(0, BigDecimal.ROUND_FLOOR)).divide(new BigDecimal(billObj.getmDlCount()), MathContext.DECIMAL128);//.setScale(0, BigDecimal.ROUND_HALF_UP);
						}
						else
						{
							avgCon = (new BigDecimal(billObj.getmUnits().trim()).setScale(0, BigDecimal.ROUND_FLOOR)).divide(new BigDecimal(billObj.getmDlCount()), MathContext.DECIMAL128);//.setScale(0, BigDecimal.ROUND_HALF_UP);

						}
					}
					else
					{
						avgCon = (new BigDecimal(billObj.getmUnits().trim()).setScale(0, BigDecimal.ROUND_FLOOR)).divide(new BigDecimal(billObj.getmBillFor().trim()), MathContext.DECIMAL128);//.setScale(0, BigDecimal.ROUND_HALF_UP);
					}					
					//Nitish Modified 15-04-2017 for LT-1
					if((avgCon.compareTo(new BigDecimal(40)) <= 0) && (billObj.getmSancKw().compareTo(new BigDecimal(0.04)) <= 0))					
					{
						billObj.setmTFc(new BigDecimal(0.00));	
					}
					else 
					{					
						
						//billObj.setmTFc((billObj.getmFixedCharges()).add(billObj.getmFC_Slab_2()));
						billObj.setmTFc(billObj.getMmFCRate_1().add(billObj.getmFC_Slab_2())); //29-03-2016
						/*try
						{
							//24-06-2021 For LT-1
							if(new BigDecimal(billObj.getmSancLoad().trim()).compareTo(new BigDecimal(slab2)) > 0) //If Sanc Load is greater than slab2
							{
								//fc2 = (Unit 2 -Unit1)* rate2
								BigDecimal fc2 = (new BigDecimal(slab2).subtract(new BigDecimal(slab1))).multiply(new BigDecimal(rate2));

								//fc3 = (Sancload - Unit2) *  rate3
								BigDecimal fc3 = (new BigDecimal(billObj.getmSancLoad().trim()).subtract(new BigDecimal(slab2))).multiply(new BigDecimal(rate3));

								billObj.setMmFC_2(fc2.setScale(2, RoundingMode.HALF_UP));
								billObj.setMmNEWFC3(fc3.setScale(2, RoundingMode.HALF_UP));

								//Total FC = FC1+FC2+FC3
								billObj.setmTFc(billObj.getMmFCRate_1().add(billObj.getMmFC_2()).add(billObj.getMmNEWFC3()));

							}
							else
							{
								billObj.setmTFc((billObj.getMmFCRate_1()).add(billObj.getmFC_Slab_2()));
							}
						}
						catch(Exception e)
						{

						}*/
					}
				}
				else if(billObj.getmTariffCode()==120 || billObj.getmTariffCode()==121) //Added Nitish 22-11-2017
				{						
					int lt7noofDays = Integer.valueOf(new SimpleDateFormat("dd").format(Calendar.getInstance().getTime())) + Integer.valueOf(billObj.getmNewNoofDays().trim());
					int noofweeks = ((new BigDecimal(lt7noofDays).divide(new BigDecimal(7.00), MathContext.DECIMAL128))).setScale(0, RoundingMode.CEILING).intValue();
					int lt7billIssueDay = 0;
					if((lt7noofDays%7)!=0)
					{
						lt7billIssueDay = 7 - (lt7noofDays%7);
					}
					billObj.setMmFCRate_1((new BigDecimal(rate1)).setScale(2, RoundingMode.HALF_UP));
					billObj.setMmCOL_FCRate_2((new BigDecimal(rate2)).setScale(2, RoundingMode.HALF_UP));
					//Added Nitish 22-11-2017
					//BigDecimal fc1Week = (billObj.getmFixedCharges().add(billObj.getmFC_Slab_2())).divide(new BigDecimal(4.00), MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_UP);
					BigDecimal fc1Week = (new BigDecimal(rate1.trim())).divide(new BigDecimal(4.00), MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_UP);
					//billObj.setmTFc((fc1Week.multiply(new BigDecimal(noofweeks))).setScale(2, RoundingMode.HALF_UP));

					billObj.setmTFc(new BigDecimal(billObj.getmSancLoad().trim()).multiply(fc1Week.multiply(new BigDecimal(noofweeks))).setScale(2, RoundingMode.HALF_UP));
					//End  22-11-2017
					Calendar c = Calendar.getInstance();    
					c.add(Calendar.DATE, lt7billIssueDay);						
					billObj.setmIssueDateTime(new SimpleDateFormat("ddMMyyHHmmss").format(c.getTime()).trim());						
				}//End 22-11-2017
				else
				{
					//28-06-2021
					billObj.setmTFc((billObj.getmFixedCharges()).add(billObj.getmFC_Slab_2()).add(billObj.getmFC_Slab_3()));
					//Added 24-06-2021
					/*try
					{
						if(billObj.getmTarifString().indexOf("LT-2")!= -1 )
						{
							if(new BigDecimal(billObj.getmSancLoad().trim()).compareTo(new BigDecimal(slab2)) > 0) //If Sanc Load is greater than slab2
							{
								//fc2 = (Unit 2 -Unit1)* rate2
								BigDecimal fc2 = (new BigDecimal(slab2).subtract(new BigDecimal(slab1))).multiply(new BigDecimal(rate2));

								//fc3 = (Sancload - Unit2) *  rate3
								BigDecimal fc3 = (new BigDecimal(billObj.getmSancLoad().trim()).subtract(new BigDecimal(slab2))).multiply(new BigDecimal(rate3));

								billObj.setMmFC_2(fc2.setScale(2, RoundingMode.HALF_UP));
								billObj.setMmNEWFC3(fc3.setScale(2, RoundingMode.HALF_UP));

								//Total FC = FC1+FC2+FC3
								billObj.setmTFc(billObj.getmFixedCharges().add(billObj.getMmFC_2()).add(billObj.getMmNEWFC3()));

							}
							else
							{
								billObj.setmTFc((billObj.getmFixedCharges()).add(billObj.getmFC_Slab_2()));
							}

						}
						else
						{
							billObj.setmTFc((billObj.getmFixedCharges()).add(billObj.getmFC_Slab_2()));
						}
					}
					catch(Exception e)
					{

					}*/
				}
			}
			//End Nitish 19-12-2017 for LT-7 Weekly Billing
			if(billObj.getmPreStatus().equals("7"))//MR 
			{
				billObj.setmTFc(new BigDecimal(0.00));
				billObj.setMmFCRate_1(new BigDecimal(0.00));
				billObj.setMmCOL_FCRate_2(new BigDecimal(0.00));
				billObj.setmFixedCharges(new BigDecimal(0.00));
				billObj.setmFC_Slab_2(new BigDecimal(0.00));
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	public void Tax_Calc_New()
	{
		/*try
		{
			if((billObj.getmTariffCode() >= 1 && billObj.getmTariffCode() <= 4) && billObj.getmConsPayable().equals("N"))
			{
				billObj.setmTaxAmt(new BigDecimal(0.00));
			}
			else if(billObj.getmTaxFlag().equals("Y"))
			{
				billObj.setmTaxAmt(new BigDecimal(0.00));

				if(billObj.getmHCReb().compareTo(new BigDecimal(0)) == 1)
				{
					if(billObj.getmHWCReb().substring(1, 2).equals("Y") && Float.valueOf(billObj.getmUnits().trim()) != 0 && ( billObj.getmTariffCode() == 23 || billObj.getmTariffCode() == 24))
					{
						billObj.setmTaxAmt((billObj.getmTax_Ec().subtract((billObj.getmTax_Ec().multiply(new BigDecimal(0.2))))
								.subtract(billObj.getmCapReb()).subtract(billObj.getmECReb()).subtract(billObj.getmFLReb()))
								.multiply(new BigDecimal( 0.06)));
					}
					else
					{						
						billObj.setmTaxAmt((billObj.getmTax_Ec().subtract((billObj.getmTax_Ec().multiply(new BigDecimal(0.2))))
								.subtract(billObj.getmECReb()).subtract(billObj.getmFLReb())).multiply(new BigDecimal( 0.06)));
					}
				}
				else
				{
					if(billObj.getmHWCReb().substring(1, 2).equals("Y") && Float.valueOf(billObj.getmUnits().trim()) != 0 && ( billObj.getmTariffCode() == 23 || billObj.getmTariffCode() == 24))
					{
						billObj.setmTaxAmt((billObj.getmTax_Ec().subtract(billObj.getmCapReb()).subtract(billObj.getmECReb())
								.subtract(billObj.getmFLReb())).multiply(new BigDecimal( 0.06)));
					}
					else
					{
						//29-06-2015 for FL rebate
						if ( billObj.getmTariffCode() >= 25 && billObj.getmTariffCode() <= 27 )
						{
							billObj.setmTaxAmt(billObj.getmTEc().multiply(new BigDecimal(0.06)));
						}
						else
						{
							billObj.setmTaxAmt((billObj.getmTax_Ec().subtract(billObj.getmECReb()).subtract(billObj.getmFLReb()))
									.multiply(new BigDecimal( 0.06)));
						}

						//billObj.setmTaxAmt((billObj.getmTax_Ec().subtract(billObj.getmECReb()).subtract(billObj.getmFLReb()))
						//		.multiply(new BigDecimal( 0.06)));
					}
				}
				BigDecimal taxRFc = (billObj.getmKVAAssd_Cons().multiply(new BigDecimal(0.06))).setScale(2, BigDecimal.ROUND_HALF_UP);
				billObj.setmTaxAmt(taxRFc.add(billObj.getmTaxAmt()));
			}
			else if(billObj.getmTaxFlag().equals("N") && ((billObj.getmTariffCode() >= 1 && billObj.getmTariffCode() <= 4) && billObj.getmConsPayable().equals("Y")))
			{
				billObj.setmTaxAmt(new BigDecimal(0.00));
				if(billObj.getmHCReb().compareTo(new BigDecimal(0)) == 1)
				{
					if(billObj.getmHWCReb().substring(1, 2).equals("Y") && Float.valueOf(billObj.getmUnits().trim()) != 0 && ( billObj.getmTariffCode() == 23 || billObj.getmTariffCode() == 24))
					{
						billObj.setmTaxAmt((billObj.getmTax_Ec().subtract((billObj.getmTax_Ec().multiply(new BigDecimal(0.2))))
								.subtract(billObj.getmCapReb()).subtract(billObj.getmECReb()).subtract(billObj.getmFLReb()))
								.multiply(new BigDecimal( 0.06)));
					}
					else
					{
						billObj.setmTaxAmt((billObj.getmTax_Ec().subtract((billObj.getmTax_Ec().multiply(new BigDecimal(0.2))))
								.subtract(billObj.getmECReb()).subtract(billObj.getmFLReb())).multiply(new BigDecimal( 0.06)));
					}
				}
				else
				{
					if(billObj.getmHWCReb().substring(1, 2).equals("Y") && Float.valueOf(billObj.getmUnits().trim()) != 0 && ( billObj.getmTariffCode() == 23 || billObj.getmTariffCode() == 24))
					{
						billObj.setmTaxAmt((billObj.getmTax_Ec().subtract(billObj.getmCapReb()).subtract(billObj.getmECReb())
								.subtract(billObj.getmFLReb())).multiply(new BigDecimal( 0.06)));
					}
					else
					{
						//Nitish 30-04-2015
						if ( billObj.getmTariffCode() >= 25 && billObj.getmTariffCode() <= 27 )
						{
							billObj.setmTaxAmt(billObj.getmTEc().multiply(new BigDecimal(0.06)));
						}
						else
						{
						billObj.setmTaxAmt((billObj.getmTax_Ec().subtract(billObj.getmECReb()).subtract(billObj.getmFLReb()))
								.multiply(new BigDecimal( 0.06)));
						}	

						billObj.setmTaxAmt((billObj.getmTax_Ec().subtract(billObj.getmECReb()).subtract(billObj.getmFLReb()))
								.multiply(new BigDecimal( 0.06)));
					}
				}
				BigDecimal taxRFc = (billObj.getmKVAAssd_Cons().multiply(new BigDecimal(0.06))).setScale(2, BigDecimal.ROUND_HALF_UP);
				billObj.setmTaxAmt(taxRFc.add(billObj.getmTaxAmt()));
			}
			else
			{
				billObj.setmTaxAmt(new BigDecimal(0.00));
			}
		}
		catch(Exception e)
		{
			Log.d("Tax_Calc_New", e.toString());
		}*/

		//Modified 30-01-2016 Remove EC Rebate for TaxAmt Calculation 
		try
		{
			if((billObj.getmTariffCode() >= 1 && billObj.getmTariffCode() <= 4) && billObj.getmConsPayable().equals("N"))
			{
				billObj.setmTaxAmt(new BigDecimal(0.00));
			}
			else if(billObj.getmTaxFlag().equals("Y"))
			{
				billObj.setmTaxAmt(new BigDecimal(0.00));

				if(billObj.getmHCReb().compareTo(new BigDecimal(0)) == 1)
				{
					if(billObj.getmHWCReb().substring(1, 2).equals("Y") && Float.valueOf(billObj.getmUnits().trim()) != 0 && ( billObj.getmTariffCode() == 23 || billObj.getmTariffCode() == 24))
					{
						//31-07-2018 Modified tax as 9% of EC
						billObj.setmTaxAmt((billObj.getmTax_Ec().subtract((billObj.getmTax_Ec().multiply(new BigDecimal(0.2))))
								.subtract(billObj.getmCapReb()).subtract(billObj.getmFLReb()))
								.multiply(new BigDecimal( 0.09)));
					}
					else
					{						
						//Commented 07-02-2017
						//billObj.setmTaxAmt((billObj.getmTax_Ec().subtract((billObj.getmTax_Ec().multiply(new BigDecimal(0.2))))
						//		.subtract(billObj.getmFLReb())).multiply(new BigDecimal( 0.06)));

						//31-07-2018 Modified tax as 9% of EC
						billObj.setmTaxAmt((billObj.getmTax_Ec().subtract(billObj.getmFLReb()))
								.multiply(new BigDecimal( 0.09)));
					}
				}
				else
				{
					if(billObj.getmHWCReb().substring(1, 2).equals("Y") && Float.valueOf(billObj.getmUnits().trim()) != 0 && ( billObj.getmTariffCode() == 23 || billObj.getmTariffCode() == 24))
					{
						//31-07-2018 Modified tax as 9% of EC
						billObj.setmTaxAmt((billObj.getmTax_Ec().subtract(billObj.getmCapReb())
								.subtract(billObj.getmFLReb())).multiply(new BigDecimal( 0.09)));
					}
					else
					{
						//29-06-2015 for FL rebate
						if ( billObj.getmTariffCode() >= 25 && billObj.getmTariffCode() <= 27 )
						{
							//31-07-2018 Modified tax as 9% of EC
							billObj.setmTaxAmt(billObj.getmTEc().multiply(new BigDecimal(0.09)));
						}
						else
						{
							//31-07-2018 Modified tax as 9% of EC
							billObj.setmTaxAmt((billObj.getmTax_Ec().subtract(billObj.getmFLReb()))
									.multiply(new BigDecimal( 0.09)));
						}

						//billObj.setmTaxAmt((billObj.getmTax_Ec().subtract(billObj.getmECReb()).subtract(billObj.getmFLReb()))
						//		.multiply(new BigDecimal( 0.06)));
					}
				}
				//Added 29-07-2016
				try
				{
					if ((billObj.getmTariffCode() == 60 || billObj.getmTariffCode() == 61 || billObj.getmTariffCode() == 62) && ((billObj.getmSancHp().compareTo(new BigDecimal(10.00))==-1) || (billObj.getmSancHp().compareTo(new BigDecimal(10.00))==0)))
					{
						billObj.setmTaxAmt(new BigDecimal(0.00));
					}
					else
					{
						//BigDecimal taxRFc = (billObj.getmKVAAssd_Cons().multiply(new BigDecimal(0.06))).setScale(2, BigDecimal.ROUND_HALF_UP);
						//billObj.setmTaxAmt(taxRFc.add(billObj.getmTaxAmt()));
						//Modified 04-01-2017
						//BigDecimal taxRFc = (billObj.getmKVAAssd_Cons().multiply(new BigDecimal(0.06))).setScale(3, BigDecimal.ROUND_HALF_UP);
						BigDecimal taxRFc = new BigDecimal(0.00);//29-12-2016
						//billObj.setmTaxAmt((taxRFc.add(billObj.getmTaxAmt()).setScale(2, BigDecimal.ROUND_HALF_UP)));					
						//30-08-2017
						billObj.setmTaxAmt((taxRFc.add(billObj.getmTaxAmt()).setScale(3, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP));

					}	
				}
				catch(Exception e)
				{
					//BigDecimal taxRFc = (billObj.getmKVAAssd_Cons().multiply(new BigDecimal(0.06)));
					//31-12-2016
					BigDecimal taxRFc = new BigDecimal(0.00);
					billObj.setmTaxAmt((taxRFc.add(billObj.getmTaxAmt()).setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
				//BigDecimal taxRFc = (billObj.getmKVAAssd_Cons().multiply(new BigDecimal(0.06))).setScale(2, BigDecimal.ROUND_HALF_UP);
				//billObj.setmTaxAmt(taxRFc.add(billObj.getmTaxAmt()));
				//End 	21-04-2016	
			}
			else if(billObj.getmTaxFlag().equals("N") && ((billObj.getmTariffCode() >= 1 && billObj.getmTariffCode() <= 4) && billObj.getmConsPayable().equals("Y")))
			{
				billObj.setmTaxAmt(new BigDecimal(0.00));
				if(billObj.getmHCReb().compareTo(new BigDecimal(0)) == 1)
				{
					if(billObj.getmHWCReb().substring(1, 2).equals("Y") && Float.valueOf(billObj.getmUnits().trim()) != 0 && ( billObj.getmTariffCode() == 23 || billObj.getmTariffCode() == 24))
					{
						//31-07-2018 Modified tax as 9% of EC
						billObj.setmTaxAmt((billObj.getmTax_Ec().subtract((billObj.getmTax_Ec().multiply(new BigDecimal(0.2))))
								.subtract(billObj.getmCapReb()).subtract(billObj.getmFLReb()))
								.multiply(new BigDecimal( 0.09)));
					}
					else
					{
						//31-07-2018 Modified tax as 9% of EC
						billObj.setmTaxAmt((billObj.getmTax_Ec().subtract((billObj.getmTax_Ec().multiply(new BigDecimal(0.2))))
								.subtract(billObj.getmFLReb())).multiply(new BigDecimal( 0.09)));
					}
				}
				else
				{
					if(billObj.getmHWCReb().substring(1, 2).equals("Y") && Float.valueOf(billObj.getmUnits().trim()) != 0 && ( billObj.getmTariffCode() == 23 || billObj.getmTariffCode() == 24))
					{
						//31-07-2018 Modified tax as 9% of EC
						billObj.setmTaxAmt((billObj.getmTax_Ec().subtract(billObj.getmCapReb())
								.subtract(billObj.getmFLReb())).multiply(new BigDecimal( 0.09)));
					}
					else
					{
						//31-07-2018 Modified tax as 9% of EC
						if ( billObj.getmTariffCode() >= 25 && billObj.getmTariffCode() <= 27 )
						{
							billObj.setmTaxAmt(billObj.getmTEc().multiply(new BigDecimal(0.09)));
						}
						else
						{
							//31-07-2018 Modified tax as 9% of EC
							billObj.setmTaxAmt((billObj.getmTax_Ec().subtract(billObj.getmFLReb()))
									.multiply(new BigDecimal( 0.09)));
						}	
						//31-07-2018 Modified tax as 9% of EC
						billObj.setmTaxAmt((billObj.getmTax_Ec().subtract(billObj.getmFLReb()))
								.multiply(new BigDecimal( 0.09)));
					}
				}
				//BigDecimal taxRFc = (billObj.getmKVAAssd_Cons().multiply(new BigDecimal(0.06))).setScale(2, BigDecimal.ROUND_HALF_UP);
				//billObj.setmTaxAmt(taxRFc.add(billObj.getmTaxAmt()));

				//Modified 04-01-2017
				//BigDecimal taxRFc = (billObj.getmKVAAssd_Cons().multiply(new BigDecimal(0.06))).setScale(3, BigDecimal.ROUND_HALF_UP);
				BigDecimal taxRFc = new BigDecimal(0.00);//29-12-2016
				//billObj.setmTaxAmt((taxRFc.add(billObj.getmTaxAmt()).setScale(2, BigDecimal.ROUND_HALF_UP)));
				//30-08-2017
				billObj.setmTaxAmt((taxRFc.add(billObj.getmTaxAmt()).setScale(3, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP));
			}
			else
			{
				billObj.setmTaxAmt(new BigDecimal(0.00));
			}
		}
		catch(Exception e)
		{
			Log.d("Tax_Calc_New", e.toString());
		}
	}
	/*public void tax()
	{
		try 
		{
			//Modified by Tamilselvan on 21-04-2014
			//for getting old EC
			BigDecimal bgEC = new BigDecimal(0.00);
			if(billObj.getmFLReb().compareTo(new BigDecimal(0.00)) > 0)
			{
				bgEC = billObj.getmFLReb().subtract(billObj.getmTFc());
			}
			else
			{
				bgEC = billObj.getmTEc();
			}
			//END Modified by Tamilselvan on 21-04-2014
			if(billObj.getmTaxFlag().equals("N") && ((billObj.getmTariffCode()>=1 && billObj.getmTariffCode()<=4) && billObj.getmConsPayable().equals("Y")))  
			{
				if((billObj.getmECReb().compareTo(new BigDecimal(0))==1))
				{
					if((billObj.getmHCReb().compareTo(new BigDecimal(0))==1))
					{
						billObj.setmTaxAmt(((bgEC.subtract((bgEC.multiply(new BigDecimal(0.2)))).subtract(billObj.getmECReb())).multiply(new BigDecimal( 0.06))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					}
					else
					{
						billObj.setmTaxAmt(((bgEC.subtract(billObj.getmECReb().add(billObj.getmHCReb()))).multiply(new BigDecimal( 0.06))).setScale(2, BigDecimal.ROUND_HALF_EVEN));//punit 02-04-2014
					}
				}
				else
				{
					if((billObj.getmHCReb().compareTo(new BigDecimal(0))==1))
					{
						billObj.setmTaxAmt(((bgEC.subtract((bgEC.multiply(new BigDecimal(0.2)))).subtract(billObj.getmECReb())).multiply(new BigDecimal( 0.06))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					}
					else
					{
						billObj.setmTaxAmt((bgEC.multiply(new BigDecimal(0.06))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					}
				}
			}
			else
			{
				billObj.setmTaxAmt(new BigDecimal(0));
			}
			if(billObj.getmTaxFlag().equals("Y") && (billObj.getmTariffCode()==1 || billObj.getmTariffCode()==3))
			{ 
				//Modified by Tamilselvan on 14-04-2014
				BigDecimal avgCon = new BigDecimal(0.00);
				if(billObj.getmPreStatus().equals("0") || billObj.getmPreStatus().equals("3") || billObj.getmPreStatus().equals("4"))
				{
					avgCon = (new BigDecimal(billObj.getmUnits().trim()).setScale(0, BigDecimal.ROUND_FLOOR)).divide(new BigDecimal(billObj.getmDlCount()), MathContext.DECIMAL128);//.setScale(0, BigDecimal.ROUND_HALF_UP);
				}
				else
				{
					avgCon = (new BigDecimal(billObj.getmUnits().trim()).setScale(0, BigDecimal.ROUND_FLOOR)).divide(new BigDecimal(billObj.getmBillFor().trim()), MathContext.DECIMAL128);//.setScale(0, BigDecimal.ROUND_HALF_UP);
				}
				//END Modified by Tamilselvan on 14-04-2014
				if((avgCon.compareTo(new BigDecimal(18.0)) == 1) || (billObj.getmSancKw().compareTo(new BigDecimal(0.04))==1) )
				{
					//set @TaxAmt=(@EC)*@TAXPer
					billObj.setmTaxAmt((billObj.getmTEc().multiply(new BigDecimal(0.06) )).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				}
				else 
				{
					billObj.setmTaxAmt(new BigDecimal(0));
				}
			}
			else
			if(billObj.getmTaxFlag().equals("Y"))
			{
				if(((billObj.getmTariffCode() >= 1 && billObj.getmTariffCode() <= 4) && billObj.getmConsPayable().equals("Y")))
				{
					billObj.setmTaxAmt(new BigDecimal(0));
				}
				else
				{
					//	if (@TaxFlag='Y' and ((@ECReb>0) or (@HCReb>0)))
					if(billObj.getmTaxFlag().equals("Y") && (billObj.getmECReb().compareTo(new BigDecimal(0))==1))//|| (billObj.getmHCReb().compareTo(new BigDecimal(0))==1))  //tamil selvan 07-04-2014 start
					{
						if((billObj.getmHCReb().compareTo(new BigDecimal(0))==1))//|| (billObj.getmHLReb().compareTo(new BigDecimal(0))==1)
						{
							//billObj.setmTaxAmt((((billObj.getmTEc().subtract(billObj.getmTEc().multiply(new BigDecimal(0.2)))).subtract(billObj.getmECReb())).multiply(new BigDecimal( 0.06))).setScale(2, BigDecimal.ROUND_HALF_EVEN));//punit 07-04-2014
							billObj.setmTaxAmt(((bgEC.subtract((bgEC.multiply(new BigDecimal(0.2)))).subtract(billObj.getmECReb())).multiply(new BigDecimal( 0.06))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						}						
						else
						{
							//set @TaxAmt=(@EC-(@ECReb+@HCReb))*@TAXPer
							//billObj.setmTaxAmt(((billObj.getmTEc().subtract(billObj.getmHCReb().add(billObj.getmHCReb()))).multiply(new BigDecimal( 0.06))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
							billObj.setmTaxAmt(((bgEC.subtract(billObj.getmECReb().add(billObj.getmHCReb()))).multiply(new BigDecimal( 0.06))).setScale(2, BigDecimal.ROUND_HALF_EVEN));//punit 02-04-2014
						}
					}
					//if (@TaxFlag='Y')
					else if (billObj.getmTaxFlag().equals("Y"))
					{
						if((billObj.getmHCReb().compareTo(new BigDecimal(0))==1))// || (billObj.getmHLReb().compareTo(new BigDecimal(0))==1)
						{
							//billObj.setmTaxAmt((((billObj.getmTEc().subtract(billObj.getmTEc().multiply(new BigDecimal(0.2)))).subtract(billObj.getmECReb())).multiply(new BigDecimal( 0.06))).setScale(2, BigDecimal.ROUND_HALF_EVEN));//punit 07-04-2014
							billObj.setmTaxAmt(((bgEC.subtract((bgEC.multiply(new BigDecimal(0.2)))).subtract(billObj.getmECReb())).multiply(new BigDecimal( 0.06))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						}
						else
						{
							//set @TaxAmt=(@EC)*@TAXPer
							billObj.setmTaxAmt((bgEC.multiply(new BigDecimal(0.06))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						}
					}
				}
			}
			try
			{
				BigDecimal taxRFc = (billObj.getmKVAAssd_Cons().multiply(new BigDecimal(0.06))).setScale(2, BigDecimal.ROUND_HALF_UP);
				billObj.setmTaxAmt(taxRFc.add(billObj.getmTaxAmt()));
			}
			catch (Exception e) 
			{
				Log.d("Tac Calculation", e.toString());
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}  
	 */	
	public void rebate()
	{
		char[] ch=new char[10];
		String str=billObj.getmHWCReb();
		for(int i=0;i<str.length();i++)
		{
			ch[i]=str.charAt(i);
		}
		String handicapt = String.valueOf(ch[0]);
		String weaversflage = String.valueOf(ch[1]);
		String capacitor = String.valueOf(ch[2]);
		//punit stop 03-04-2014
		try 
		{
			if ((billObj.getmRebateFlag().equals("Y")))	
			{
				//if (((billObj.getmTariffCode()==20)||(billObj.getmTariffCode()==21)||(billObj.getmTariffCode()==22) ||(billObj.getmTariffCode()==23) ||(billObj.getmTariffCode()==24) ||(billObj.getmTariffCode()==25) ||(billObj.getmTariffCode()==26)||(billObj.getmTariffCode()==27)))
				if(billObj.getmTariffCode() != 60 && billObj.getmTariffCode() != 61 && billObj.getmTariffCode() != 62)//
				{
					if (((billObj.getmPreStatus().equals("0"))||(billObj.getmPreStatus().equals("2"))||(billObj.getmPreStatus().equals("3")) ||(billObj.getmPreStatus().equals("4")) ||(billObj.getmPreStatus().equals("9")) ||(billObj.getmPreStatus().equals("10")) ||(billObj.getmPreStatus().equals("14"))))
					{
						//if(( (new BigDecimal(billObj.getmUnits().trim()).divide(new BigDecimal(billObj.getmDlCount()))).compareTo(new BigDecimal(100)))<=0)
						if(( (new BigDecimal(billObj.getmUnits().trim()).divide(new BigDecimal(billObj.getmDlCount()),MathContext.DECIMAL128)).compareTo(new BigDecimal(100)))<=0)
						{
							billObj.setmECReb(new BigDecimal(billObj.getmUnits().trim()).multiply(new BigDecimal( 0.50)).setScale(2, BigDecimal.ROUND_HALF_EVEN));  //.multiply(new BigDecimal(billObj.getmDlCount()))
						}
						else
						{
							billObj.setmECReb((new BigDecimal(100)).multiply(new BigDecimal( 0.50)).multiply((new BigDecimal(billObj.getmDlCount()))).setScale(2, BigDecimal.ROUND_HALF_EVEN));  
						}
					}
				}
			}			
			if(handicapt.equals("Y"))
			{
				if((billObj.getmTariffCode() >= 40 && billObj.getmTariffCode() <= 43))
				{
					if(!billObj.getmPreStatus().equals("1") && !billObj.getmPreStatus().equals("5") && !billObj.getmPreStatus().equals("6"))
					{
						BigDecimal EC = new BigDecimal(0);//added By Tamilselvan on 05-04-2014
						BigDecimal FC = new BigDecimal(0);//added By Tamilselvan on 05-04-2014
						if(billObj.getmFC_Slab_2().compareTo(new BigDecimal(0.00)) == 1)//Fc Slab -2
						{
							//Modified By Tamilselvan on 05-04-2014
							EC = billObj.getMmEC_1().add(billObj.getMmEC_2().add(billObj.getMmEC_3().add(billObj.getMmEC_4().add(billObj.getMmEC_5().add(billObj.getMmEC_6())))));
							FC = billObj.getmFixedCharges().add(billObj.getmFC_Slab_2());
							//billObj.setmHCReb((((billObj.getMmEC_1().add(billObj.getMmEC_2()).add(billObj.getMmEC_3()).add(billObj.getMmEC_4().add(billObj.getMmEC_5()).add(billObj.getMmEC_6()))).multiply(new BigDecimal(0.2))).add((billObj.getmFixedCharges().add(billObj.getmFC_Slab_2())).multiply(new BigDecimal(0.02)))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
							billObj.setmHCReb(((EC.add(FC)).multiply(new BigDecimal(0.2))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						}
						else
						{
							//Modified By Tamilselvan on 05-04-2014
							EC = billObj.getMmEC_1().add(billObj.getMmEC_2().add(billObj.getMmEC_3().add(billObj.getMmEC_4().add(billObj.getMmEC_5().add(billObj.getMmEC_6())))));
							FC = billObj.getmTFc();
							//billObj.setmHCReb((((billObj.getMmEC_1().add(billObj.getMmEC_2()).add(billObj.getMmEC_3()).add(billObj.getMmEC_4().add(billObj.getMmEC_5()).add(billObj.getMmEC_6()))).multiply(new BigDecimal(0.2))).add((billObj.getmTFc()).multiply(new BigDecimal(0.02)))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
							billObj.setmHCReb(((EC.add(FC)).multiply(new BigDecimal(0.2))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						}
					}
				}
				else if((billObj.getmTariffCode() >= 20 && billObj.getmTariffCode() <= 24))
				{
					if(!billObj.getmPreStatus().equals("1") && !billObj.getmPreStatus().equals("5") && !billObj.getmPreStatus().equals("6"))
					{
						BigDecimal consumption = new BigDecimal(billObj.getmUnits().trim());
						billObj.setmHCReb((consumption.multiply(new BigDecimal(0.25))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					}
				}
			}
			if(weaversflage.equals("Y"))
			{
				if(!billObj.getmPreStatus().equals("1") && !billObj.getmPreStatus().equals("5") && !billObj.getmPreStatus().equals("6"))
				{
					//	1.25*cons&-ec
					//billObj.setmHCReb(((new BigDecimal(billObj.getmUnits()).multiply(new BigDecimal(1.25))).subtract(billObj.getMmEC_1().add(billObj.getMmEC_2()).add(billObj.getMmEC_3()).add(billObj.getMmEC_4().add(billObj.getMmEC_5()).add(billObj.getMmEC_6())))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					billObj.setmHLReb(((billObj.getMmEC_1().add(billObj.getMmEC_2().add(billObj.getMmEC_3().add(billObj.getMmEC_4().add(billObj.getMmEC_5().add(billObj.getMmEC_6())))))).subtract((new BigDecimal(billObj.getmUnits().trim()).multiply(new BigDecimal(1.25))))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				}
			}
			if(capacitor.equals("Y"))
			{
				//21-04-2016
				//if((billObj.getmTariffCode() >= 60 && billObj.getmTariffCode() <= 64))
				if((billObj.getmTariffCode() >= 60 && billObj.getmTariffCode() <= 65))//if tariff Code is between 60 and 64 then capacitor is applicable
				{
					if(!billObj.getmPreStatus().equals("1") && !billObj.getmPreStatus().equals("5") && !billObj.getmPreStatus().equals("6"))
					{
						billObj.setmCapReb((new BigDecimal(billObj.getmUnits().trim()).multiply(new BigDecimal(0.02))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						//21-04-2016 If GOKPayable > 0 then  GokPayable  =  GokPayable - capreb
						if(billObj.getmGoKPayable().compareTo(new BigDecimal(0.00))>0)
						{
							billObj.setmGoKPayable(billObj.getmGoKPayable().subtract(billObj.getmCapReb()));
						}
					}
				}
				//Added 21-04-2016 for Capacitor Rebate 
				else if(billObj.getmTariffCode() == 23 || billObj.getmTariffCode() == 24)
				{
					billObj.setmCapReb((new BigDecimal(billObj.getmUnits().trim()).multiply(new BigDecimal(0.25))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				}	
			}			
		} 
		catch (Exception e) 
		{
			Log.d("", e.toString());
			// TODO: handle exception
		}
	}

}
