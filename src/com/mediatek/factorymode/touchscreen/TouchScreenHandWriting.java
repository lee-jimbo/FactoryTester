package com.mediatek.factorymode.touchscreen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.mediatek.factorymode.FactoryMode;
import com.mediatek.factorymode.KeyCode;
import com.mediatek.factorymode.TouchView;
import com.mediatek.factorymode.R;

public class TouchScreenHandWriting extends Activity {
	
	private String TAG = "TouchScreenHandWriting";
	private TouchView myview = null;
	private Button successButton = null;
	private Button failButton = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(new MyView(this));

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    //setContentView(myview = new TouchView(this));
		setContentView(R.layout.touchscreen_handwriting);
		myview = (TouchView)this.findViewById(R.id.toucheview);
		successButton =(Button)this.findViewById(R.id.touchscreen_bt_ok);
		failButton =(Button)this.findViewById(R.id.touchscreen_bt_failed);

		successButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TouchScreenHandWriting.this, FactoryMode.class);
				setResult(RESULT_OK,intent);
				finish();
			}
			
		});
		failButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TouchScreenHandWriting.this, FactoryMode.class);
				setResult(RESULT_CANCELED,intent);
				finish();
			}
			
		});
	}
}
