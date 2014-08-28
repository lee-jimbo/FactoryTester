package com.mediatek.factorymode.backlight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.mediatek.factorymode.FactoryMode;
import com.mediatek.factorymode.R;
import com.mediatek.factorymode.earphone.Earphone;

public class BackLight extends Activity {
	private Button succesButton ;
	private Button failButton ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backlight);
		Button display_lcd_on = (Button)this.findViewById(R.id.Display_lcd_on);
		display_lcd_on.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.screenBrightness = 1.0f;
				getWindow().setAttributes(lp);
			}
		});
		Button display_lcd_off = (Button)this.findViewById(R.id.Display_lcd_off);
		display_lcd_off.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				WindowManager.LayoutParams lp = getWindow().getAttributes();

				lp.screenBrightness = 0.1f;

				getWindow().setAttributes(lp);
			}
		});
		succesButton = (Button)this.findViewById(R.id.display_bt_ok);
		failButton = (Button)this.findViewById(R.id.display_bt_failed);
		succesButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(BackLight.this, FactoryMode.class);
				setResult(RESULT_OK,intent);
				finish();
			}
			
		});
		failButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(BackLight.this, FactoryMode.class);
				setResult(RESULT_CANCELED,intent);
				finish();
			}
			
		});
		}
}
