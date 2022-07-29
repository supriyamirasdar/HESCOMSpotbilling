

//Created By Nitish 15-04-2014
package in.nsoft.hescomspotbilling;

import android.content.Context;

public class CollectionObject {
	private static ReadCollection mCollectionObject;
	
	private CollectionObject()
	{
		
	}	
	//Get the Object
	public static ReadCollection GetCollectionObject()
	{
		if(mCollectionObject == null)
		{
			mCollectionObject = new ReadCollection();
			mCollectionObject.setmIsTimeSync(TimeChangedReceiver.isValid());
		}
		return mCollectionObject;
	}
	//Remove Object
	public static void Remove()
	{
		mCollectionObject = null;
	}
	public static void reset(Context ctx) throws Exception
	{		
		DatabaseHelper db = new DatabaseHelper(ctx);
		db.GetAllDatafromDb(mCollectionObject.getmConnectionNo());		
		
	}
}

