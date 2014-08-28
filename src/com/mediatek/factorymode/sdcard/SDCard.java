package com.mediatek.factorymode.sdcard;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mediatek.factorymode.FactoryMode;
import com.mediatek.factorymode.R;
import com.mediatek.factorymode.R.string;
import com.mediatek.factorymode.earphone.Earphone;

public class SDCard extends Activity {
	
	private TextView sdcardinfo;
	private Button succesButton ;
	private Button failButton ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sdcard);
		//liuzhixiang 2012.3.12 begin
		//bug 648,ver:308v3,factory pattern fixture test: application factory model stops unexpectedly.
		sdcardinfo = (TextView)this.findViewById(R.id.sdcard_info);
		//liuzhixiang 2012.3.12 end
		//if (Environment.getExternalStorageState().equals(
		//		Environment.MEDIA_MOUNTED)) {
		    //liuzhixiang 2012.3.12 begin
		    //bug 648,ver:308v3,factory pattern fixture test: application factory model stops unexpectedly.
			//sdcardinfo = (TextView)this.findViewById(R.id.sdcard_info);
			//liuzhixiang 2012.3.12 end
		long availableMB =getAvailaleSize();
		long totalMB = getAllSize();
		if(totalMB > 0)
		{
			sdcardinfo.setText(getString(R.string.sdcard_tips_success)+"\n\n"+
			getString(R.string.sdcard_totalsize)+ totalMB + " MB\n\n"+
			getString(R.string.sdcard_freesize)+ availableMB + " MB");
		} else 
		{
			sdcardinfo.setText(getString(R.string.sdcard_tips_failed));
		}
		succesButton = (Button)this.findViewById(R.id.sdcard_bt_ok);
		failButton = (Button)this.findViewById(R.id.sdcard_bt_failed);
		succesButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SDCard.this, FactoryMode.class);
				setResult(RESULT_OK,intent);
				finish();
			}
			
		});
		failButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SDCard.this, FactoryMode.class);
				setResult(RESULT_CANCELED,intent);
				finish();
			}
			
		});
	}

	public long getAvailaleSize() {

		//File path = Environment.getExternalStorageDirectory(); 
		File path = new File("/storage/sdcard1");
		
		StatFs stat = new StatFs(path.getPath());

		long blockSize = stat.getBlockSize();

		long availableBlocks = stat.getAvailableBlocks();

		//return availableBlocks * blockSize;

		return (availableBlocks * blockSize)/1024 /1024; 

	}

	public long getAllSize() {

		//File path = Environment.getExternalStorageDirectory();
		File path = new File("/storage/sdcard1");
		
		StatFs stat = new StatFs(path.getPath());

		long blockSize = stat.getBlockSize();

		long availableBlocks = stat.getBlockCount();

		return (availableBlocks * blockSize)/1024 /1024; 

	}

}
