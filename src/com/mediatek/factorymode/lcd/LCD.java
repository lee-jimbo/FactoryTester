package com.mediatek.factorymode.lcd;

import java.util.Timer;
import java.util.TimerTask;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mediatek.factorymode.FactoryMode;
import com.mediatek.factorymode.R;

public class LCD extends Activity {

	private int colorindex = 0;
	private static final int COLORRED = 1;
	private static final int COLORGREEN = 2;
	private static final int COLORBLUE = 3;
	
	private TextView lcdtextview;
	

	Timer timer = new Timer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.lcd);
		lcdtextview = (TextView)this.findViewById(R.id.test_color_text1);
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run()
			{
				colorindex++;
				Message message = new Message();
				message.what= colorindex;
				handler.sendMessage(message);
			}
		}, 0, 1000);
	}

	protected void onDestroy() {
		timer.cancel();
		super.onDestroy();
	}
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			if (colorindex > 3) {
				//colorindex = 1;
				timer.cancel();
				AlertDialog.Builder dialog=new AlertDialog.Builder(LCD.this);  
                dialog.setTitle(getString(R.string.Report)).setMessage(getString(R.string.Report)).setPositiveButton(getString(R.string.Success), new DialogInterface.OnClickListener() {  
                 @Override  
                 public void onClick(DialogInterface dialog, int which) {  

                	 goback(RESULT_OK);

                 }  
             }).setNegativeButton(getString(R.string.Failed), new DialogInterface.OnClickListener() {  
                   
                 public void onClick(DialogInterface dialog, int which) {  
                	 goback(RESULT_CANCELED);
                 }  
             }).create().show();  
				
			}
			switch (colorindex) {
			case COLORRED:
				lcdtextview.setBackgroundColor(Color.RED);
				break;
			case COLORGREEN:
				lcdtextview.setBackgroundColor(Color.GREEN);
				break;
			case COLORBLUE:
				lcdtextview.setBackgroundColor(Color.BLUE);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	private void goback(int flag) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();  
	    intent.setClass(LCD.this, FactoryMode.class);  
	    setResult(flag,intent);
	    finish();
	}
}
