package com.mediatek.factorymode.sensor;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mediatek.factorymode.FactoryMode;
import com.mediatek.factorymode.R;
import com.mediatek.factorymode.bluetooth.Bluetooth;

public class GSensor extends Activity implements SensorEventListener{
	SensorManager sensorManager = null;
	Sensor orientationSensor = null;
	TextView accuracy_view= null;
	TextView value_0 = null;
	TextView value_1 = null;
	TextView value_2 = null;
	Button successButton;
	Button failButton;
	
	private boolean			mRegisteredSensor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gsensor);
	
		mRegisteredSensor = false;
		sensorManager = (SensorManager)getSystemService(this.SENSOR_SERVICE);
	
	
		orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		accuracy_view = (TextView)this.findViewById(R.id.gsensor_tv_info);
		value_0 = (TextView)this.findViewById(R.id.gsensor_xyz);
		
		accuracy_view.setText(getString(R.string.GSensor_tips));
		value_0.setText("X:\nY:\nZ");
		successButton = (Button)this.findViewById(R.id.gsensor_bt_ok);
		failButton = (Button)this.findViewById(R.id.gsensor_bt_failed);
		successButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GSensor.this, FactoryMode.class);
				setResult(RESULT_OK,intent);
				finish();
			}
			
		});
		failButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GSensor.this, FactoryMode.class);
				setResult(RESULT_CANCELED,intent);
				finish();
			}
			
		});
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		if(sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			
			}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		if (mRegisteredSensor)
		{
			sensorManager.unregisterListener(this);
			mRegisteredSensor = false;
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//sensorManager.registerListener(this,orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
		List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);

		if (sensors.size() > 0)
		{
			Sensor sensor = sensors.get(0);

			mRegisteredSensor = sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			float[] values = event.values;
			value_0.setText("X:"+values[0]+"\nY:"+values[1]+"\nZ:"+values[2]);
			}
	}

}
