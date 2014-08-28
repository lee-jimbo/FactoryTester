package com.mediatek.factorymode.earphone;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mediatek.factorymode.FactoryMode;
import com.mediatek.factorymode.KeyCode;
import com.mediatek.factorymode.R;

public class Earphone extends Activity{
	private AudioManager audiomanager;
	private Button succesButton ;
	private Button failButton ;
	MediaPlayer mpbg ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.microphone);
		audiomanager = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);
		audiomanager.setSpeakerphoneOn(false); 
		audiomanager.setRouting(AudioManager.MODE_RINGTONE,AudioManager.ROUTE_EARPIECE,AudioManager.ROUTE_ALL);
		setVolumeControlStream(AudioManager.STREAM_RING);
		audiomanager.setMode(AudioManager.MODE_IN_CALL);
		succesButton = (Button)this.findViewById(R.id.mic_bt_ok);
		failButton = (Button)this.findViewById(R.id.mic_bt_failed);
		succesButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Earphone.this, FactoryMode.class);
				setResult(RESULT_OK,intent);
				finish();
			}
			
		});
		failButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Earphone.this, FactoryMode.class);
				setResult(RESULT_CANCELED,intent);
				finish();
			}
			
		});
		playonce();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		audiomanager.setMode(AudioManager.MODE_NORMAL);
		super.onDestroy();
		mpbg.pause();
		mpbg.release();
	}
	protected void playonce()
	{

		mpbg = MediaPlayer.create(this,R.raw.tada);
		mpbg.setVolume(1.0f,1.0f);
		mpbg.setLooping(true);
		mpbg.start();
	}
}
