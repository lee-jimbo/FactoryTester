package com.mediatek.factorymode.signal;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mediatek.factorymode.FactoryMode;
import com.mediatek.factorymode.R;

public class Signal extends Activity{
	
	private Button successbutton;
	private Button failbuttonButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signal);
		
		successbutton = (Button)this.findViewById(R.id.signal_bt_ok);
		failbuttonButton = (Button)this.findViewById(R.id.signal_bt_failed);
		successbutton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Signal.this, FactoryMode.class);
				setResult(RESULT_OK,intent);
				finish();
			}
			
		});
		failbuttonButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Signal.this, FactoryMode.class);
				setResult(RESULT_CANCELED,intent);
				finish();
			}
			
		});
		Intent callIntent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+112));
		startActivity(callIntent);
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
