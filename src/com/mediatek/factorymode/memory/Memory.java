package com.mediatek.factorymode.memory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mediatek.factorymode.FactoryMode;
import com.mediatek.factorymode.R;
import com.mediatek.factorymode.R.string;
import com.mediatek.factorymode.earphone.Earphone;

public class Memory extends Activity {
	
	private ActivityManager activityManager;
	private TextView memoryinfo;
	private Button succesButton ;
	private Button failButton ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memory);
		activityManager = (ActivityManager)this.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		long Avaimemsize = getSystemAvaialbeMemorySize();
		String totalmemsize = getTotalMemory();
		memoryinfo = (TextView)this.findViewById(R.id.comm_info);
		memoryinfo.setText(getString(R.string.memorytotal)+totalmemsize+ "\n"+
		getString(R.string.memoryfree)+ Avaimemsize+"MB");
		succesButton = (Button)this.findViewById(R.id.memory_bt_ok);
		failButton = (Button)this.findViewById(R.id.memory_bt_failed);
		succesButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Memory.this, FactoryMode.class);
				setResult(RESULT_OK,intent);
				finish();
			}
			
		});
		failButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Memory.this, FactoryMode.class);
				setResult(RESULT_CANCELED,intent);
				finish();
			}
			
		});
		
	}
	public long getSystemAvaialbeMemorySize(){
		  MemoryInfo memoryInfo = new MemoryInfo();
		  activityManager.getMemoryInfo(memoryInfo);
		  long memSize = memoryInfo.availMem;
		  
		  System.out.println("getSystemAvaialbeMemorySize()...memory size: " + memSize);
		  
		return memSize/1024/1024;
		 }
	private String getTotalMemory() {	
		String str1 = "/proc/meminfo";
		String str2;        
		String[] arrayOfString;
		long initial_memory = 0;
		try{  
			FileReader localFileReader = new FileReader(str1);  
			BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
  
			for(String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}
			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
			localBufferedReader.close();
		} 
		catch(IOException e){
		
		}
		return Formatter.formatFileSize(getBaseContext(), initial_memory);
	}
}
