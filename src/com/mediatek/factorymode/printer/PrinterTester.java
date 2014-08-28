/**
 * 文件名：PrinterTester.java
 * author: jimbo
 * 版本信息 : magniwill copyright 2014
 * 日期：2014-8-22
 * 
 */
package com.mediatek.factorymode.printer;


import com.mediatek.factorymode.FactoryMode;
import com.mediatek.factorymode.R;
import com.mediatek.factorymode.sdcard.SDCard;
import com.odm.sprt.SprtPrinter;
import com.odm.sprt.SprtPrinter.OnStateChangeListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author Administrator
 *
 */
public class PrinterTester extends Activity {
	
	private Button succesButton ;
	private Button failButton ;
	
	private Button mBeginPrintButton;
	private SprtPrinter mSprtPrinter;
	private OnStateChangeListener mSprtPrinterStateChangeListener = new OnStateChangeListener() {
		
		@Override
		public void onPrintingChange(boolean bPrinting) {
			// TODO Auto-generated method stub
			if(bPrinting){
			}else{
				
			}
		}
		
		@Override
		public void onNoPaperChange(boolean bNopaper) {
			if(bNopaper){
//				Toast.makeText(PrinterTester.this, "No paper!", Toast.LENGTH_SHORT).show();
			}else{
				
			}
			
		}
		
		@Override
		public void onBusyChange(boolean arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.printer);
		
		mBeginPrintButton = (Button) findViewById(R.id.button_print);
		mBeginPrintButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				turnOnPrinter();
				printList();
			}
		});
		
		
		
		
		succesButton = (Button)this.findViewById(R.id.print_ok);
		failButton = (Button)this.findViewById(R.id.print_failed);
		succesButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PrinterTester.this, FactoryMode.class);
				setResult(RESULT_OK,intent);
				finish();
			}
			
		});
		failButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PrinterTester.this, FactoryMode.class);
				setResult(RESULT_CANCELED,intent);
				finish();
			}
			
		});

		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		turnOffPrinter();
	}

	private void turnOnPrinter() {
		mSprtPrinter = new SprtPrinter(this, mSprtPrinterStateChangeListener );
		mSprtPrinter.deviceEnable();
		mSprtPrinter.deviceOpen();
	}

	private void turnOffPrinter() {
		if (null != mSprtPrinter) {
			mSprtPrinter.deviceClose();
			mSprtPrinter.deviceDisable();
			mSprtPrinter = null;
		}
	}
	
	/**
	 * 打印文书内容
	 */
	private void printList() {
		mSprtPrinter.doPrintText(getString(R.string.print_test_text));
		mSprtPrinter.doLineFeed();
		mSprtPrinter.printEnd();
		
	}
	
}
