package in.nsoft.hescomspotbilling;

public class MyMarker {
 private String mLabel;
 private Double mLatitude;
 private Double mLongitude;
 private String mBillcount;
 

 public MyMarker(String label,Double latitude,Double longitude,String Billcount)
 {
	 this.mLabel=label;
	 this.mLatitude=latitude;
	 this.mLongitude=longitude;
	 this.mBillcount=Billcount;
	 
 }
 public String getmLabel()
 {
	 return mLabel;
 }
 public void setmlabel(String mLabel)
 {
	 this.mLabel=mLabel;
	 
 }

public Double getmLatitude()
 {
	 return mLatitude;
 }
 public void setmLatitude(Double mLatitude)
 {
	 this.mLatitude=mLatitude;
 }
 public Double getmLongitude()
 {
	 return mLongitude;
 }
 public void setmlongitude(Double mLongitude)
 {
	 this.mLongitude=mLongitude;
 }
public String getmBillcount() {
	return mBillcount;
}
public void setmBillcount(String mBillcount) {
	this.mBillcount = mBillcount;
}
}
