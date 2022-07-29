package in.nsoft.hescomspotbilling;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends android.support.v4.app.FragmentActivity implements OnMapReadyCallback{
	ReadSlabNTarifSbmBillCollection SlabColl;
	private GoogleMap mMap;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		SlabColl = BillingObject.GetBillingObject();
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;

		// Add a marker in Sydney and move the camera
		//LatLng aMark = new LatLng(Double.parseDouble(SlabColl.getmGps_Latitude_image()),Double.parseDouble(SlabColl.getmGps_Latitude_image()));
		//mMap.addMarker(new MarkerOptions().position(aMark).title(SlabColl.getmRRNo()+ " " + SlabColl.getmCustomerName()));
		//mMap.moveCamera(CameraUpdateFactory.newLatLng(aMark));

		LatLng aMark = new LatLng(Double.parseDouble(SlabColl.getmGps_Latitude_image().trim()), Double.parseDouble(SlabColl.getmGps_Longitude_image().trim()));
		mMap.addMarker(new MarkerOptions().position(aMark).title(SlabColl.getmRRNo().trim()+ " " + SlabColl.getmCustomerName().trim()));
		mMap.moveCamera(CameraUpdateFactory.newLatLng(aMark));
		final int zoom=8;
		mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom),1500,null);


	}
}






