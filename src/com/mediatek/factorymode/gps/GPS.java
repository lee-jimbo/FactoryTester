package com.mediatek.factorymode.gps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.mediatek.factorymode.FactoryMode;
import com.mediatek.factorymode.KeyCode;
import com.mediatek.factorymode.R;
import com.mediatek.factorymode.R.string;

public class GPS extends Activity {

	private TextView gps_state_id;
	private TextView gps_satellite_id;
	private TextView gps_signal_id;
	private TextView gps_result_id;
	private Chronometer gps_time_id;
	private Button succesButton ;
	private Button failButton ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps);
		gps_state_id = (TextView) this.findViewById(R.id.gps_state_id);
		gps_satellite_id = (TextView) this.findViewById(R.id.gps_satellite_id);
		gps_signal_id = (TextView) this.findViewById(R.id.gps_signal_id);
		gps_result_id = (TextView) this.findViewById(R.id.gps_result_id);
		
		succesButton = (Button)this.findViewById(R.id.gps_bt_ok);
		failButton = (Button)this.findViewById(R.id.gps_bt_failed);
		
		gps_time_id = (Chronometer) this.findViewById(R.id.gps_time_id);
		gps_time_id.setFormat(getString(R.string.GPS_time));
		gps_time_id.start();
		
		gps_satellite_id.setText(getString(R.string.GPS_satelliteNum)+0);
		gps_signal_id.setText(getString(R.string.GPS_Signal)+"nosignal");
		openGPSSettings();
		getLocation(); 
		
		succesButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GPS.this, FactoryMode.class);
				setResult(RESULT_OK,intent);
				finish();
			}
			
		});
		failButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GPS.this, FactoryMode.class);
				setResult(RESULT_CANCELED,intent);
				finish();
			}
			
		});

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings","com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(this, 0, gpsIntent, 0).send();
		}
		catch (CanceledException e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	private void openGPSSettings() {
		LocationManager alm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			gps_state_id.setText(getString(R.string.GPS_connect));
			return;
		}
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings","com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		try {
			gps_state_id.setText(getString(R.string.GPS_connect));
			PendingIntent.getBroadcast(this, 0, gpsIntent, 0).send();
		}
		catch (CanceledException e) {
			e.printStackTrace();
		}
		//ContentResolver resolver = this.getContentResolver();
        //Settings.Secure.setLocationProviderEnabled(resolver, LocationManager.GPS_PROVIDER,true);
        
	}

	LocationManager locationManager;
	 private static boolean getGpsState(Context context) {
	        ContentResolver resolver = context.getContentResolver();
	        boolean open = Settings.Secure.isLocationProviderEnabled(resolver, LocationManager.GPS_PROVIDER);
	        System.out.println("getGpsState:"+open);
	        return open;
	    }        
	private void getLocation() {
	
		String serviceName = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) this.getSystemService(serviceName);
	
		// Criteria criteria = new Criteria();
		// criteria.setAccuracy(Criteria.ACCURACY_FINE);

		// criteria.setAltitudeRequired(false);
		// criteria.setBearingRequired(false);
		// criteria.setCostAllowed(true);
		// criteria.setPowerRequirement(Criteria.POWER_LOW);

		// String provider = locationManager.getBestProvider(criteria, true);

		String provider = LocationManager.GPS_PROVIDER;
		Location location = locationManager.getLastKnownLocation(provider);
		if (location == null)
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		updateToNewLocation(location);
		
		locationManager.requestLocationUpdates(provider, 1000, 0,
				locationListener);
		locationManager.addGpsStatusListener(statusListener); 
	}

	private List<GpsSatellite> numSatelliteList = new ArrayList<GpsSatellite>(); 

	private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) { 
			GpsStatus status = locationManager.getGpsStatus(null); 
			updateGpsStatus(event, status);
		}
	};

	private void updateGpsStatus(int event, GpsStatus status) {
		if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
			int maxSatellites = status.getMaxSatellites();
			Iterator<GpsSatellite> it = status.getSatellites().iterator();
			numSatelliteList.clear();
			int count = 0;
			while (it.hasNext() && count <= maxSatellites) {
				GpsSatellite s = it.next();
				numSatelliteList.add(s);
				count++;
			}
		}
		gps_satellite_id.setText(getString(R.string.GPS_satelliteNum)+numSatelliteList.size() );
		if(numSatelliteList.size()==0){
			gps_signal_id.setText(getString(R.string.GPS_Signal)+numSatelliteList.size());
		}else if (numSatelliteList.size()>2){
			gps_signal_id.setText(getString(R.string.GPS_Signal)+getString(R.string.GPS_normal));
		}else{
			gps_signal_id.setText(getString(R.string.GPS_Signal)+numSatelliteList.size());
		}
	}

	private void updateToNewLocation(Location location) {
		TextView tv1;
		// tv1 = (TextView) this.findViewById(R.id.tv1);
		if (location != null) {
			} else {

		}
	}

	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			
			if (location != null) {
				updateToNewLocation(location);
				
			}
		}

		public void onProviderDisabled(String provider) {
			
			updateToNewLocation(null);
			
		}

		public void onProviderEnabled(String provider) {
			
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
	};
}
