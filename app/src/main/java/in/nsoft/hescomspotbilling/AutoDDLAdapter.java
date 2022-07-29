package in.nsoft.hescomspotbilling;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;




public class AutoDDLAdapter extends ArrayAdapter<DDLItem> implements Filterable{

	private Context mContext;
	private ArrayList<DDLItem> mList;
	ArrayList<DDLItem> filList = new ArrayList<DDLItem>();
	private static final String TAG = AutoDDLAdapter.class.getName();
	//Modified Rajiv 25-02-2014
	private int startpoint=0;
	public AutoDDLAdapter(Context context, ArrayList<DDLItem> list, int i) {
		super(context, R.layout.genericauto,list);		
		mContext = context;
		mList = list;	
		startpoint = i;
	}	
	//End Rajiv
	public AutoDDLAdapter(Context context,ArrayList<DDLItem> list)
	{super(context, R.layout.genericauto,list);		
	mContext=context;
	mList=list;	
	}
	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub 
		Filter filter = new Filter(){
			@Override
			protected FilterResults performFiltering(CharSequence constraints){
				FilterResults filterResults = new FilterResults();
				try
				{
					//rajiv commented on 05-05-2014
					//synchronized(filterResults)
					{
						if(constraints!=null)
						{
							//rajiv commented on 05-05-2014
							//filList.clear();	
							//rajiv added on 05-05-2014
							ArrayList<DDLItem> resultList = new ArrayList<DDLItem>();
							for(DDLItem d1:mList)	
							{						
								String strValue = d1.getValue().toString();

								//Modified Rajiv 25-02-2014
								if(strValue.substring(startpoint).toUpperCase().startsWith(constraints.toString().toUpperCase()))
								{
									//rajiv commented on 05-05-2014
									//filList.add(d1);
									
									//rajiv added on 05-05-2014
									resultList.add(d1);
								}
								//End Rajiv					
							}	
							
							//rajiv commented on 05-05-2014
							//filterResults.values = filList;					
							//filterResults.count=filList.size();	
							
							//rajiv added on 05-05-2014
							filterResults.values = resultList;					
							filterResults.count=resultList.size();	

						}
					}								
				}
				catch(Exception e)
				{
					Log.d(TAG, e.toString());
				}
				finally
				{
					//notifyDataSetChanged();
				}
				//Modified Nitish 01-04-2014

				//End

				return filterResults;
			}	
			@Override
			protected void publishResults(CharSequence arg0, final FilterResults result) {
				// TODO Auto-generated method stub
				try
				{
					if(result!=null && result.count>0)
					{		//rajiv added on 05-05-2014
						filList= (ArrayList<DDLItem>) result.values;
						
						notifyDataSetChanged();
					}else
					{				
						notifyDataSetInvalidated();				
					}
				}
				catch(Exception e)
				{
					Log.d(TAG, e.toString());
				}
				finally
				{
					//notifyDataSetChanged();
				}

				//End
			}		
		};
		return filter;		
	}	

	@Override
	public DDLItem getItem(int position) {	
		DDLItem  itm = null; 
		try
		{
			itm = filList.get(position);

		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return itm;//filList.get(position);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int count = 0;
		try
		{
			count = filList.size();
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return count;//filList.size();
	}

	@Override
	public View getDropDownView( int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View  v = null;
		try
		{
			v = getCustomView(position, convertView, parent);
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return v;//getCustomView(position, convertView, parent);
	}	
	@Override
	public View getView( int position, View convertView, ViewGroup parent) {

		// TODO Auto-generated method stub
		View  v = null;
		try
		{
			v = getCustomView(position, convertView, parent);
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return v;//getCustomView(position, convertView, parent);
	}	
	public View getCustomView( int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//return super.getView(position, convertView, parent);
		LayoutInflater inflater = null;
		View row = null;
		try
		{
			inflater = ((Activity)mContext).getLayoutInflater();
			row = inflater.inflate(R.layout.genericauto, parent, false);	
			TextView label = (TextView)row.findViewById(R.id.lblAutoComplete);		
			label.setText(GetValue(position));		
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return row; 
	}
	public String GetId(int index) 
	{
		String str = "";
		try
		{
			str = filList.get(index).getId();
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return str;//filList.get(index).getId();
	}
	public String GetValue(int index)
	{		
		String str = "";
		try	
		{
			str = filList.get(index).getValue();
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		return str;//filList.get(index).getValue();
	}	
	public void AddItem(String id,String value)	
	{
		try
		{
			DDLItem item= new DDLItem( id,value); 
			mList.add(item);
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}	
}
