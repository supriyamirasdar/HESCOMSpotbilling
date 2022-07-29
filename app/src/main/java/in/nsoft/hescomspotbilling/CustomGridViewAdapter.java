package in.nsoft.hescomspotbilling;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomGridViewAdapter extends ArrayAdapter<Item>{
	Context ctx;
	int layoutResourceId;
	ArrayList<Item> data = new ArrayList<Item>();

	public CustomGridViewAdapter(Context context, int layoutResId, ArrayList<Item> data1)
	{
		super(context, layoutResId, data1);
		ctx = context;
		layoutResourceId = layoutResId;
		data = data1;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = convertView;
		RecordHolder holder = null;
		
		if(row == null)
		{
			LayoutInflater inflater = ((Activity)ctx).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new RecordHolder();
			holder.txtTitle = (TextView)row.findViewById(R.id.item_text);
			holder.imageItem = (ImageView)row.findViewById(R.id.item_image);
			row.setTag(holder);			
		}
		else
		{
			holder = (RecordHolder)row.getTag();
		}
		Item item = data.get(position);
		holder.txtTitle.setText(item.Name);
		holder.imageItem.setImageBitmap(item.bitmap);
		
		return row;
	}
	
	static class RecordHolder
	{
		TextView txtTitle;
		ImageView imageItem;
	}
}
