package in.nsoft.hescomspotbilling;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class InternetStartUpReceiver extends BroadcastReceiver {

	private static final String TAG = InternetStartUpReceiver.class.getName();
	Thread th;
	static volatile boolean contTh = true;


	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		final Context ctx = arg0;		
		DatabaseHelper db = new DatabaseHelper(ctx);
		TelephonyManager mgr = (TelephonyManager)arg0.getSystemService(Context.TELEPHONY_SERVICE);
		String IMEINumber = mgr.getDeviceId();
		String SimNumber = mgr.getSimSerialNumber();
		
		try
		{
			if(new ConnectionDetector(ctx).isConnectedToInternet())
			{
				try
				{
					db.EventLogSave("3", IMEINumber, SimNumber, "Data Connection Opened  ");//15-07-2016
				}
				catch(Exception e)
				{

				}
			}
			else
			{
				try
				{
					db.EventLogSave("3", IMEINumber, SimNumber, "Data Connection Closed  ");//15-07-2016
				}
				catch(Exception e)
				{

				}
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		/*try
		{
			if(connectivity.isConnectedToInternet())//Check Internet available or not
			{//If Available 
				setContTh(true);
				th = new Thread(new Runnable() 
				{

					@Override
					public void run() 
					{
						// TODO Auto-generated method stub
						try
						{
							DatabaseHelper db = new DatabaseHelper(ctx);
							th.setName(String.valueOf(R.string.ThreadName));
							while(isContTh())
							{
								//Billed Connection send to server.====================================================================
								ArrayList<String> alStr = db.GetDataSendToServer();
								for(String str :alStr) 
								{
									try
									{
										String BillingData = str;//get string
										HttpClient httpclt = new DefaultHttpClient();//object for HttpClient

										//Sending MtrPhoto to Server Added on 30-04-2014 Tamilselvan
										String ImgName = db.GetPhotoNameByConnectionNo(BillingData.substring(14, 24));
										HttpPost httpPostImage = new HttpPost(ConstantClass.IPAddress + "/Android_Photo_Transfer");
										List<NameValuePair> lvpImage = new ArrayList<NameValuePair>(1);
										lvpImage.add(new BasicNameValuePair("photo", CommonFunction.getPhotoStream(ImgName)));
										lvpImage.add(new BasicNameValuePair("FileName", ImgName));
										httpPostImage.setEntity((new UrlEncodedFormEntity(lvpImage)));
										httpPostImage.setHeader("Content-Type","application/x-www-form-urlencoded");							
										HttpResponse resImg = httpclt.execute(httpPostImage);
										HttpEntity entImg = resImg.getEntity();
										if(entImg != null)
										{
											String rtrImgValue = EntityUtils.toString(entImg);
											if(rtrImgValue.equals("ACK"))
											{
										//Sending billing data to server 
										HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Bescom_GPRS_Data_Android");
										List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
										lvp.add(new BasicNameValuePair("BillingData", BillingData));
										httpPost.setEntity((new UrlEncodedFormEntity(lvp)));
										httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");							
										HttpResponse res = httpclt.execute(httpPost);
										HttpResponse k = res;
										HttpEntity ent = k.getEntity();
										if(ent != null)
										{
											String rValue = EntityUtils.toString(ent);
											Log.d(TAG, rValue);
											if(rValue.equals("ACK"))
											{
												String value = db.UpdateBlCnt(BillingData.substring(14, 24));
												String[] splitvalue = value.split("/");
												if(Integer.valueOf(splitvalue[0]) < Integer.valueOf(splitvalue[1]))
												{
													db.UpdateStatusMasterDetailsByID("10", "1", value);
												}
												else if(Integer.valueOf(splitvalue[0]) == Integer.valueOf(splitvalue[1]))
												{
													db.UpdateStatusMasterDetailsByID("10", "0", value);
												}
											}
										}//End of Sending billing data to server 
										//}
										//}//End Sending MtrPhoto to server
									}
									catch(Exception e)
									{
										Log.d(TAG, e.toString());
									}
								}//End of Billed Connection send to server.=============================================================

								//Collection Data send to Server========================================================================
								ArrayList<String> alStrColl = db.GetDataSendToServerColl();
								for(String str :alStrColl) 
								{
									try
									{
										String CollData = str;//get string
										HttpClient httpclt = new DefaultHttpClient();//object for HttpClient								
										HttpPost httpPost = new HttpPost(ConstantClass.IPAddress+"/Bescom_GPRS_ReceiptData_Android");
										List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
										lvp.add(new BasicNameValuePair("ReceiptData", CollData));
										httpPost.setEntity((new UrlEncodedFormEntity(lvp)));
										httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");							
										HttpResponse res = httpclt.execute(httpPost);
										HttpResponse k = res;
										HttpEntity ent = k.getEntity();
										if(ent != null)
										{
											//db.UpdateRcptCnt(CollData.substring(1, 8)+"   ",CollData.substring(33,38));
											String rValue = EntityUtils.toString(ent);
											Log.d(TAG, rValue);
											if(rValue.equals("ACK"))
											{
												//Nitish 08-05-2014
												CommonFunction cf = new CommonFunction();
												//Add Spaces towards right to ConnectionNo so that its length is 10
												String updateConnId = cf.addSpaceRight(CollData.substring(1, 8).trim() ,10 - CollData.substring(1, 8).trim().length() , ' ');
												String value = db.UpdateRcptCnt(updateConnId ,CollData.substring(40, 45));// ConnectionNo and ReceiptNo are parameters												
												String[] splitvalue = value.split("/");
												if(Integer.valueOf(splitvalue[0]) < Integer.valueOf(splitvalue[1])) //If GPRS not sent
												{
													db.UpdateStatusMasterDetailsByID("13", "1", value);
												}
												else if(Integer.valueOf(splitvalue[0]) == Integer.valueOf(splitvalue[1])) //If all Collection GPRS sent
												{
													db.UpdateStatusMasterDetailsByID("13", "0", value);
												}
											}
										}
									}									
									catch(Exception e)
									{
										Log.d(TAG, e.toString());
									}
								}//END Collection Data send to Server====================================================================	
								Thread.sleep(30000);
							}
						}
						catch(Exception e)
						{
							Log.d(TAG, e.toString());
						}
					}
				});
				th.start();
			}
			else//if Internet not available
			{
				//contTh = false;
				setContTh(false);
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}*/
		
	}


	public synchronized static boolean isContTh() {//Added on 09-06-2014
		return contTh;
	}


	public synchronized static void setContTh(boolean contTh) {
		InternetStartUpReceiver.contTh = contTh;
	}
}
