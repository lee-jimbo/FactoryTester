package com.mediatek.factorymode.vibratortest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import com.mediatek.factorymode.FactoryMode;
import com.mediatek.factorymode.R;
import com.mediatek.factorymode.earphone.Earphone;

public class Vibratortest extends Activity {
	Vibrator vibrator;
	Button succesButton ;
	Button failButton ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vibrator);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		long[] pattern = { 400, 800, 400, 800}; // OFF/ON/OFF/ON...
		vibrator.vibrate(pattern, 2);
		//vibrator.vibrate(30000);
		succesButton = (Button)this.findViewById(R.id.vibrator_bt_ok);
		failButton = (Button)this.findViewById(R.id.vibrator_bt_failed);
		succesButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Vibratortest.this, FactoryMode.class);
				setResult(RESULT_OK,intent);
				finish();
			}
			
		});
		failButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Vibratortest.this, FactoryMode.class);
				setResult(RESULT_CANCELED,intent);
				finish();
			}
			
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (null != vibrator) {
			vibrator.cancel();
		}
		super.onPause();
	}

	@Override
	protected void onStop() {
		if (null != vibrator) {
			vibrator.cancel();
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (null != vibrator) {
			vibrator.cancel();
		}
		super.onDestroy();
	}
	
}
