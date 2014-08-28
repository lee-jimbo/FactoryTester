package com.mediatek.factorymode.sensor;

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
import com.mediatek.factorymode.earphone.Earphone;

public class PSensor extends Activity implements SensorEventListener{
	private SensorManager sensorManager = null;
	private Sensor PSensor = null;
	private TextView accuracy_view= null;
	private TextView value_0 = null;
	private TextView value_1 = null;
	private TextView value_2 = null;
	private Button succesButton ;
	private Button failButton ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.psensor);
	
		sensorManager = (SensorManager)getSystemService(this.SENSOR_SERVICE);

		PSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		//accuracy_view = (TextView)this.findViewById(R.id.proximity);
		value_0 = (TextView)this.findViewById(R.id.proximity);
		
		succesButton = (Button)this.findViewById(R.id.psensor_bt_ok);
		failButton = (Button)this.findViewById(R.id.psensor_bt_failed);
		succesButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PSensor.this, FactoryMode.class);
				setResult(RESULT_OK,intent);
				finish();
			}
			
		});
		failButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PSensor.this, FactoryMode.class);
				setResult(RESULT_CANCELED,intent);
				finish();
			}
			
		});
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		if(sensor.getType() == Sensor.TYPE_PROXIMITY){
			
			}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sensorManager.unregisterListener(this, PSensor);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		sensorManager.registerListener(this,PSensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if(event.sensor.getType() == Sensor.TYPE_PROXIMITY){

			
			float[] values = event.values;
			value_0.setText(getString(R.string.proximity)+values[0]);
			}
	}

}
