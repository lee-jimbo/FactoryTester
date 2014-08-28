package com.mediatek.factorymode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BatteryLog extends Activity {

	private IntentFilter mIntentFilter;
	
	private TextView status;
	private TextView level;
	private TextView scale;
	private TextView health;
	private TextView voltage;
	private TextView temperature;
	private TextView technology;
	private TextView uptime;
	private Button successButton;
	private Button failButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery_info);

		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(mIntentReceiver, filter);

		status = (TextView) this.findViewById(R.id.status);
		level = (TextView) this.findViewById(R.id.level);
		scale = (TextView) this.findViewById(R.id.scale);
		health = (TextView) this.findViewById(R.id.health);
		voltage = (TextView) this.findViewById(R.id.voltage);
		temperature = (TextView) this.findViewById(R.id.temperature);
		technology = (TextView) this.findViewById(R.id.technology);
		uptime = (TextView) this.findViewById(R.id.uptime);
		
		successButton = (Button)this.findViewById(R.id.battery_bt_ok);
		failButton = (Button)this.findViewById(R.id.battery_bt_failed);
		
		Long timeLong = SystemClock.elapsedRealtime();
		timeLong = timeLong/1000;
		long hour = timeLong/3600;
		long minute = (timeLong%3600)/60;
		long second = (timeLong%3600)%60;
	    uptime.setText(hour+getString(R.string.hour)+minute+getString(R.string.minute)+second+getString(R.string.second));
	    
	    successButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(BatteryLog.this, FactoryMode.class);
				setResult(RESULT_OK,intent);
				finish();
			}
			
		});
		failButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(BatteryLog.this, FactoryMode.class);
				setResult(RESULT_CANCELED,intent);
				finish();
			}
			
		});
	}
	@Override
	protected void onResume() {
		super.onResume();

		IntentFilter filter = new IntentFilter();

		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(mIntentReceiver, filter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mIntentReceiver);
	}

	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			int status = intent.getIntExtra("status", 0);
			int health = intent.getIntExtra("health", 0);
			boolean present = intent.getBooleanExtra("present", false);
			int level = intent.getIntExtra("level", 0);
			int scale = intent.getIntExtra("scale", 0);
			int icon_small = intent.getIntExtra("icon-small", 0);
			int plugged = intent.getIntExtra("plugged", 0);
			int voltage = intent.getIntExtra("voltage", 0);
			int temperature = intent.getIntExtra("temperature", 0);
			String technology = intent.getStringExtra("technology");
			String statusString = "";
			switch (status) {
			case BatteryManager.BATTERY_STATUS_UNKNOWN:
				statusString = "unknown";
				break;
			case BatteryManager.BATTERY_STATUS_CHARGING:
				statusString = "charging";
				// battery_image.setImageResource(R.drawable.stat_sys_battery_charge);
				// battery_image.getDrawable().setLevel(level);
				break;
			case BatteryManager.BATTERY_STATUS_DISCHARGING:
				statusString = "discharging";
				// battery_image.setImageResource(R.drawable.stat_sys_battery);
				// battery_image.getDrawable().setLevel(level);
				break;
			case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
				statusString = "not charging";
				break;
			case BatteryManager.BATTERY_STATUS_FULL:
				statusString = "full";
				
				break;
			}
			String healthString = "";
			switch (health) {
			case BatteryManager.BATTERY_HEALTH_UNKNOWN:
				healthString = "unknown";
				break;
			case BatteryManager.BATTERY_HEALTH_GOOD:
				healthString = "good";
				break;
			case BatteryManager.BATTERY_HEALTH_OVERHEAT:
				healthString = "overheat";
				break;
			case BatteryManager.BATTERY_HEALTH_DEAD:
				healthString = "dead";
				break;
			case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
				healthString = "voltage";
				break;
			case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
				healthString = "unspecified failure";
				break;
			}

			String acString = "";
			switch (plugged) {
			case BatteryManager.BATTERY_PLUGGED_AC:
				acString = "plugged ac";
				break;
			case BatteryManager.BATTERY_PLUGGED_USB:
				acString = "plugged usb";
				break;
			}
			BatteryLog.this.status.setText(statusString);
			BatteryLog.this.health.setText(healthString);
			BatteryLog.this.level.setText(""+level);
			BatteryLog.this.scale.setText(""+scale);
			BatteryLog.this.voltage.setText(""+voltage);
			BatteryLog.this.temperature.setText(""+temperature);
			BatteryLog.this.technology.setText(technology);
	
	
			if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
			
				Log.d("Battery", "" + intent.getIntExtra("level", 0));
		
				Log.d("Battery", "" + intent.getIntExtra("scale", 0));
		
				Log.d("Battery", "" + intent.getIntExtra("voltage", 0));

				Log.d("Battery", "" + intent.getIntExtra("temperature", 0));

				Log.d("Battery",
						"ss"
								+ intent.getIntExtra("status",
										BatteryManager.BATTERY_STATUS_CHARGING));

				Log.d("Battery", "" + intent.getIntExtra("plugged", 0));
	
				Log.d("Battery",
						""
								+ intent.getIntExtra("health",
										BatteryManager.BATTERY_HEALTH_UNKNOWN));
			}
		}
	};
}