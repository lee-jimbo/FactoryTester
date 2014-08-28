package com.mediatek.factorymode.headset;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mediatek.factorymode.FactoryMode;
import com.mediatek.factorymode.R;
import com.mediatek.factorymode.VUMeter;

public class HeadSet  extends Activity {

	private Button succesButton ;
	private Button failButton ;
	private Button testheadbButton;
	
	private MediaRecorder mRecorder = null;
	private MediaPlayer mPlayer = null;
	private String  curPath = null ;
	private VUMeter mVUMeter;
	
	private static final int TEST_IDLE = 0;
	private static final int TEST_RECORDERING = 1;
	private static final int TEST_PLAYYING =2;
	private int curTestState = TEST_IDLE; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.headset);
		testheadbButton =(Button)this.findViewById(R.id.mic_bt_start);
	
		succesButton = (Button)this.findViewById(R.id.bt_ok);
		failButton = (Button)this.findViewById(R.id.bt_failed);
		mVUMeter = (VUMeter)this.findViewById(R.id.uvMeter);
		
		succesButton.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HeadSet.this, FactoryMode.class);
				setResult(RESULT_OK,intent);
				finish();
			}
		});
		failButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HeadSet.this, FactoryMode.class);
				setResult(RESULT_CANCELED,intent);
				finish();
			}
			
		});
		testheadbButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {	
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					if(curTestState == TEST_IDLE){
						curTestState = TEST_RECORDERING;
						startRecorder();
					}else if(curTestState == TEST_RECORDERING){
						curTestState = TEST_PLAYYING;
						stopRecorder();
						playRecordfile();
					}else if(curTestState == TEST_PLAYYING)
					{
						curTestState = TEST_IDLE;
						stopplay();
					}
				}else{
					testheadbButton.setText(getString(R.string.sdcard_tips_failed));
					testheadbButton.setEnabled(false);
				}
			}
		});
		
		if(getHeadsetState()== 0)
		{
			testheadbButton.setEnabled(false);
			testheadbButton.setText(getString(R.string.HeadSet_tips));
		}else {
			testheadbButton.setEnabled(true);
			testheadbButton.setText(getString(R.string.Mic_start));
		}
		IntentFilter filter = new IntentFilter(); 
        filter.addAction(Intent.ACTION_TIME_TICK); 
        filter.addAction(Intent.ACTION_HEADSET_PLUG); 
        this.registerReceiver(mIntentReceiver, filter);
	}
	private static final String HEADSET_STATE_PATH = "/sys/class/switch/h2w/state";
	public static int getHeadsetState() {
        try {
            FileReader file = new FileReader(HEADSET_STATE_PATH);
            char[] buffer = new char[1024];
            int len = file.read(buffer, 0, 1024);
            int headsetState = Integer.valueOf((new String(buffer, 0, len)).trim());
            Log.v("FMRadio", "---------------" + headsetState);
            return headsetState;

        } catch (Exception e) {
            return 0;
        }
    }
	void startRecorder(){
		File file = new File("/sdcard/"+ "YY"+ new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".amr");	
		curPath = file.getPath();
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);				
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		mRecorder.setOutputFile(file.getAbsolutePath());
		try {
			file.createNewFile();
			mRecorder.prepare();
			mRecorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		testheadbButton.setText(getString(R.string.Mic_stop));
		mVUMeter.setRecorder(mRecorder);
		mVUMeter.setState(1);
	}
	private void stopRecorder(){
		if (mRecorder != null) {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}		
		mVUMeter.setState(0);
	}
	private void playRecordfile(){
		mPlayer = new MediaPlayer();
		mPlayer.reset();
		try{
			mPlayer.setDataSource(curPath);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		testheadbButton.setText(getString(R.string.stopplayer));
	}
	private void stopplay(){
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
			testheadbButton.setText(getString(R.string.Mic_start));
		}
	}
	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { 
        @Override 
        public void onReceive(Context context, Intent intent) { 
            String action = intent.getAction(); 
            if (action.equals(Intent.ACTION_HEADSET_PLUG)) { 
            	 int curheatset = intent.getIntExtra("state", 0);
            	 if(curheatset == 0)
            	 {
            		if(curTestState == TEST_RECORDERING){
     					curTestState = TEST_IDLE;
     					stopRecorder();
     				}else if(curTestState == TEST_PLAYYING)
     				{
     					curTestState = TEST_IDLE;
     					stopplay();
     				}
            		testheadbButton.setEnabled(false);
            		testheadbButton.setText(getString(R.string.HeadSet_tips));
            	 }
            	 else if(curheatset == 1)
            	 {
            		testheadbButton.setEnabled(true);
            		testheadbButton.setText(getString(R.string.Mic_start));
            	 }
            } 
        }
     }; 
}
