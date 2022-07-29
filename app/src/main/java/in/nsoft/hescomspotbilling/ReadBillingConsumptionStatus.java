package in.nsoft.hescomspotbilling;
//Created By Tamilselvan on 27-02-2014
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import android.content.Context;
import android.util.Log;

public class ReadBillingConsumptionStatus extends DefaultHandler{

	final String TAG = "in.nsoft.spotbilling.ReadDynamicNavigation";
	StringBuilder sb = new StringBuilder(); 
	DDLItem dNav, dNavnew;		 
	ArrayList<DDLItem> alConStatus = new ArrayList<DDLItem>();
	Context mContext=null;

	public ReadBillingConsumptionStatus(Context c)
	{
		mContext=c;
	}	
	//Created By Tamilselvan on 27-02-2014
	public ArrayList<DDLItem> ReadStatus()
	{
		try
		{					
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			dNav = new DDLItem();
			DefaultHandler dh = new DefaultHandler(){						

				public void startElement(String uri, String localName, String nodeName, Attributes atb)throws SAXException{
					sb.delete(0, sb.length());

				}
				public void endElement(String uri, String localName, String nodeName)throws SAXException{
					if(nodeName.equals("id"))
					{								
						dNav.setId(sb.toString());
					}
					else if(nodeName.equals("value"))
					{												
						dNav.setValue(sb.toString());								
						dNavnew = new DDLItem(dNav.getId(), dNav.getValue());								
						alConStatus.add(dNavnew);
					}		
				}
				public void characters(char ch[], int start, int length)throws SAXException
				{
					sb.append(new String(ch,start,length));						
				}
			};					
			sp.parse(new InputSource(mContext.getAssets().open("BillingConsumptionStatus.xml")) , dh);
		}
		catch(Exception ex)
		{
			Log.d(TAG, ex.toString());
		}
		return alConStatus;
	}
}
