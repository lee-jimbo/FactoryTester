package com.mediatek.factorymode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.mediatek.factorymode.backlight.BackLight;
import com.mediatek.factorymode.bluetooth.Bluetooth;
import com.mediatek.factorymode.camera.CameraTest;
import com.mediatek.factorymode.camera.SubCamera;
import com.mediatek.factorymode.earphone.Earphone;
import com.mediatek.factorymode.finger.FingerTester;
//import com.mediatek.FMRadio.FMRadiotest;
import com.mediatek.factorymode.gps.GPS;
import com.mediatek.factorymode.headset.HeadSet;
import com.mediatek.factorymode.lcd.LCD;
import com.mediatek.factorymode.memory.Memory;
import com.mediatek.factorymode.micophone.MicRecorder;
import com.mediatek.factorymode.printer.PrinterTester;
import com.mediatek.factorymode.rfid.RfidTester;
import com.mediatek.factorymode.sdcard.SDCard;
import com.mediatek.factorymode.sensor.GSensor;
import com.mediatek.factorymode.sensor.LSensor;
import com.mediatek.factorymode.sensor.MSensor;
import com.mediatek.factorymode.sensor.PSensor;
import com.mediatek.factorymode.signal.Signal;
import com.mediatek.factorymode.simcard.SimCard;
import com.mediatek.factorymode.touchscreen.TouchScreenHandWriting;
import com.mediatek.factorymode.vibratortest.Vibratortest;
import com.mediatek.factorymode.wifi.WiFiTest;

public class FactoryMode extends Activity {
	public static final String TAG = "FactoryMode";

	public static final int TESTITEM_START = 0;
	public static final int TESTITEM_TOUCH = TESTITEM_START;
	public static final int TESTITEM_LCD = 1;
	public static final int TESTITEM_GPS = 2;
	public static final int TESTITEM_POWER = 3;
	public static final int TESTITEM_KEY = 4;
	public static final int TESTITEM_SPEAKER = 5;
	public static final int TESTITEM_HEADSET = 6;
	public static final int TESTITEM_MIC = 7;
	public static final int TESTITEM_RECEIVER = 8;
	public static final int TESTITEM_WIFI = 9;
	public static final int TESTITEM_BT = 10;
	public static final int TESTITEM_SHAKE = 11;
	public static final int TESTITEM_CALL = 12;
	public static final int TESTITEM_BL = 13;
	public static final int TESTITEM_MEMORY = 14;
	public static final int TESTITEM_GSENSOR = 15;
	//public static final int TESTITEM_MSENSOR = 16;
	public static final int TESTITEM_LSENSOR = 16;
	public static final int TESTITEM_DSENSOR = 17;
	public static final int TESTITEM_TCARD = 18;
	public static final int TESTITEM_BCAMERA = 19;
//	public static final int TESTITEM_FCAMERA = 20;
//	public static final int TESTITEM_FM = 21;
//	public static final int TESTITEM_SIM = 22;
//	public static final int TESTITEM_TATOL = 23;
	public static final int TESTITEM_SIM = 20;
	public static final int TESTITEM_PRINT = 21;
	public static final int TESTITEM_RFID = 22;
	public static final int TESTITEM_FINGER = 23;
//	public static final int TESTITEM_FINGER = 23;
//	public static final int TESTITEM_POS= 23;
	public static final int TESTITEM_END = 24;
	public static final int TESTITEM_TATOL = TESTITEM_END;
	
	private static int currentTestMode = 0;
	private static int currenttestitem = TESTITEM_START;
	public static final int TESTMODE_ALLTEST = 1;
	public static final int TESTMODE_AUTOTEST = 2;
	
	public static int testResult[];
	public static final int TEST_OK = 1;
	public static final int TEST_FAIL = 2;
	public static final int NOTTEST = 0;

	private GridView gridview;
	ArrayAdapter<String> adapter;
	private Button alltestButton = null;
	private Button autotestButton = null;
	public static final int alltestitem[] = {
			 TESTITEM_TOUCH,
			 TESTITEM_LCD,
			 TESTITEM_GPS,
			 TESTITEM_POWER ,
			 TESTITEM_KEY ,
			 TESTITEM_SPEAKER ,
			 TESTITEM_HEADSET ,
			 TESTITEM_MIC ,
			 TESTITEM_RECEIVER ,
			 TESTITEM_WIFI ,
			 TESTITEM_BT ,
			 TESTITEM_SHAKE ,
			 TESTITEM_CALL,
			 TESTITEM_BL ,
			 TESTITEM_MEMORY ,
			 TESTITEM_GSENSOR ,
			 //TESTITEM_MSENSOR ,
			 TESTITEM_LSENSOR ,
			 TESTITEM_DSENSOR ,
			 TESTITEM_TCARD ,
			 TESTITEM_BCAMERA ,
//			 TESTITEM_FCAMERA ,
//			 TESTITEM_FM ,
			 TESTITEM_SIM,
			 TESTITEM_PRINT,
			 TESTITEM_RFID,
			 TESTITEM_FINGER};
	public static final int autotestitem[] = {
			 TESTITEM_TOUCH,
		     TESTITEM_LCD,
			 TESTITEM_GPS,
			 TESTITEM_POWER ,
			 TESTITEM_KEY ,
			 TESTITEM_SPEAKER ,
			 TESTITEM_HEADSET ,
			 TESTITEM_MIC ,
			 TESTITEM_RECEIVER ,
			 TESTITEM_WIFI ,
			 TESTITEM_BT ,
			 TESTITEM_SHAKE ,
			 TESTITEM_CALL,
			 TESTITEM_BL ,
			 TESTITEM_MEMORY ,
			 TESTITEM_GSENSOR ,
			 //TESTITEM_MSENSOR ,
			 TESTITEM_LSENSOR ,
			 TESTITEM_DSENSOR ,
			 TESTITEM_TCARD ,
			 TESTITEM_BCAMERA ,
//			 TESTITEM_FCAMERA ,
//			 TESTITEM_FM ,
			 TESTITEM_SIM,
			 TESTITEM_PRINT,
			 TESTITEM_RFID,
			 TESTITEM_FINGER};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.main);

		gridview = (GridView) findViewById(R.id.main_grid);
		
		adapter = new ArrayAdapter<String>(this, R.layout.main_grid);
		gridview.setAdapter(adapter);
		
		testResult = new int[TESTITEM_TATOL];
		alltestButton = (Button)this.findViewById(R.id.main_bt_alltest);
		autotestButton = (Button)this.findViewById(R.id.main_bt_autotest);
		
		alltestButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FactoryMode.setCurrtetTestMode(FactoryMode.TESTMODE_ALLTEST);
				currenttestitem = 0;
				TestSelectedItem(alltestitem[currenttestitem]);
			}
		});
		autotestButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FactoryMode.setCurrtetTestMode(FactoryMode.TESTMODE_AUTOTEST);
				currenttestitem = 0;
				TestSelectedItem(autotestitem[currenttestitem]);
			}
		});
		for (int i = TESTITEM_START; i < alltestitem.length; i++) {
			adapter.add(getStringbyitemid(alltestitem[i]));
		}
		gridview.setOnItemClickListener(new ItemClickListener());
	}

	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
													// click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3// The row id of the item that was clicked
		){
			Log.e(TAG, "  arg2="+arg2+"   alltestitem[arg2]="+alltestitem[arg2]);
			
			TestSelectedItem(alltestitem[arg2]);
		}
	}
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		Log.v("requestCode"+requestCode,"resultCode"+resultCode);
		TextView tView = (TextView)gridview.getChildAt(requestCode);
		Log.v("abcd","onActivityResult:requestCode=" +requestCode+",resultCode="+resultCode);
		Log.v("abcd","tView=" +tView);
		if(resultCode == RESULT_OK)
		{
			tView.setTextColor(Color.BLUE);
			testResult[requestCode]= FactoryMode.TEST_OK;
		}
		else {
			tView.setTextColor(Color.RED);
			testResult[requestCode]= FactoryMode.TEST_FAIL;
		}
		Log.v("getCurrentTestMode:",FactoryMode.getCurrentTestMode()+"");
		
		if(FactoryMode.getCurrentTestMode() == TESTMODE_AUTOTEST )
		{
			currenttestitem = currenttestitem + 1;
			if(currenttestitem == autotestitem.length)
			{
			    Intent intent2 = new Intent();
				intent2.setClass(this, Report.class);
				startActivity(intent2);	
				FactoryMode.setCurrtetTestMode(0);
			}
			else {
				TestSelectedItem(autotestitem[currenttestitem]);
			}
		}
		if(FactoryMode.getCurrentTestMode() == TESTMODE_ALLTEST)
		{
			currenttestitem = currenttestitem + 1;
			if(currenttestitem == alltestitem.length)
			{
				Intent intent2 = new Intent();
				intent2.setClass(this, Report.class);
				startActivity(intent2);				
				FactoryMode.setCurrtetTestMode(0);
			}
			else {
				TestSelectedItem(alltestitem[currenttestitem]);
			}
		}
		
	}
	public void TestSelectedItem(int selectedItem) {
		Intent intent = new Intent();

		Log.d(TAG, "TestSelectedItem,selectedItem:" + selectedItem);
		switch (selectedItem) {
		case TESTITEM_TOUCH:
			intent.setClass(this, TouchScreenHandWriting.class);
			startActivityForResult(intent,TESTITEM_TOUCH); 
			break;
		case TESTITEM_LCD:
			intent.setClass(this, LCD.class);
			startActivityForResult(intent,TESTITEM_LCD); 
			break;
		case TESTITEM_GPS:
			intent.setClass(this, GPS.class);
			startActivityForResult(intent,TESTITEM_GPS); 
			break;
		case TESTITEM_POWER:
			intent.setClass(this, BatteryLog.class);
			startActivityForResult(intent,TESTITEM_POWER); 
			break;
		case TESTITEM_KEY:
			intent.setClass(this, KeyCode.class);
			startActivityForResult(intent,TESTITEM_KEY); 
			break;
		case TESTITEM_SPEAKER:
			intent.setClass(this, Speaker.class);
			startActivityForResult(intent,TESTITEM_SPEAKER); 
			break;
		case TESTITEM_HEADSET:
			intent.setClass(this, HeadSet.class);
			startActivityForResult(intent,TESTITEM_HEADSET); 
			break;
		case TESTITEM_MIC:
			intent.setClass(this,MicRecorder.class);
			startActivityForResult(intent, TESTITEM_MIC);
			break;
		case TESTITEM_RECEIVER:
			intent.setClass(this, Earphone.class);
			startActivityForResult(intent, TESTITEM_RECEIVER);
			break;

		case TESTITEM_WIFI:
			intent.setClass(this, WiFiTest.class);
			startActivityForResult(intent, TESTITEM_WIFI);
			break;
		case TESTITEM_BT:
			intent.setClass(this, Bluetooth.class);
			startActivityForResult(intent, TESTITEM_BT);
			break;
		case TESTITEM_SHAKE:
			intent.setClass(this, Vibratortest.class);
			startActivityForResult(intent, TESTITEM_SHAKE);
			break;
		case TESTITEM_CALL:
			intent.setClass(this, Signal.class);
			startActivityForResult(intent, TESTITEM_CALL);
			break;
		case TESTITEM_BL:
			intent.setClass(this, BackLight.class);
			startActivityForResult(intent, TESTITEM_BL);
			break;
		case TESTITEM_MEMORY:
			intent.setClass(this, Memory.class);
			startActivityForResult(intent, TESTITEM_MEMORY);
			break;
		case TESTITEM_GSENSOR:
			intent.setClass(this, GSensor.class);
			startActivityForResult(intent, TESTITEM_GSENSOR);
			break;
		/*
		case TESTITEM_MSENSOR:
			intent.setClass(this, MSensor.class);
			startActivityForResult(intent, TESTITEM_MSENSOR);
			break;
		*/
		case TESTITEM_LSENSOR:
			intent.setClass(this, LSensor.class);
			startActivityForResult(intent, TESTITEM_LSENSOR);
			break;
		case TESTITEM_DSENSOR:
			intent.setClass(this, PSensor.class);
			startActivityForResult(intent, TESTITEM_DSENSOR);
			break;
		case TESTITEM_TCARD:
			intent.setClass(this, SDCard.class);
			startActivityForResult(intent, TESTITEM_TCARD);
			break;
		case TESTITEM_BCAMERA:
			intent.setClass(this, CameraTest.class);
			startActivityForResult(intent, TESTITEM_BCAMERA);
			break;
	/*	case TESTITEM_FCAMERA:
			intent.setClass(this, SubCamera.class);
			startActivityForResult(intent, TESTITEM_FCAMERA);
			break;
		case TESTITEM_FM:
			intent.setClassName("com.mediatek.FMRadio", "com.mediatek.FMRadio.FMRadiotest");
			startActivityForResult(intent, TESTITEM_FM);
			break;*/
		case TESTITEM_SIM:
			intent.setClass(this, SimCard.class);
			startActivityForResult(intent, TESTITEM_SIM);
			break;
			
		case TESTITEM_PRINT:
			intent.setClass(this, PrinterTester.class);
			startActivityForResult(intent, TESTITEM_PRINT);
			break;
		case TESTITEM_RFID:
			intent.setClass(this, RfidTester.class);
			startActivityForResult(intent, TESTITEM_RFID);
			break;
		case TESTITEM_FINGER:
			intent.setClass(this, FingerTester.class);
			startActivityForResult(intent, TESTITEM_FINGER);
			break;
		
		default:
			break;
		}
	}

	public String getStringbyitemid(int item_id) {
		String str = null;
		switch (item_id) {
		case TESTITEM_TOUCH:
			str = getString(R.string.touchscreen_name);
			break;
		case TESTITEM_LCD:
			str = getString(R.string.lcd_name);
			break;
		case TESTITEM_GPS:
			str = getString(R.string.gps_name);
			break;
		case TESTITEM_POWER:
			str = getString(R.string.battery_name);
			break;
		case TESTITEM_KEY:
			str = getString(R.string.KeyCode_name);
			break;
		case TESTITEM_SPEAKER:
			str = getString(R.string.speaker_name);
			break;
		case TESTITEM_HEADSET:
			str = getString(R.string.headset_name);
			break;
		case TESTITEM_MIC:
			str = getString(R.string.microphone_name);
			break;
		case TESTITEM_RECEIVER:
			str = getString(R.string.earphone_name);
			break;
		case TESTITEM_WIFI:
			str = getString(R.string.wifi_name);
			break;
		case TESTITEM_BT:
			str = getString(R.string.bluetooth_name);
			break;
		case TESTITEM_SHAKE:
			str = getString(R.string.vibrator_name);
			break;
		case TESTITEM_CALL:
			str = getString(R.string.telephone_name);
			break;
		case TESTITEM_BL:
			str = getString(R.string.backlight_name);
			break;
		case TESTITEM_MEMORY:
			str = getString(R.string.memory_name);
			break;
		case TESTITEM_GSENSOR:
			str = getString(R.string.gsensor_name);
			break;
		/*
		case TESTITEM_MSENSOR:
			str = getString(R.string.msensor_name);
			break;
		*/
		case TESTITEM_LSENSOR:
			str = getString(R.string.lsensor_name);
			break;
		case TESTITEM_DSENSOR:
			str = getString(R.string.psensor_name);
			break;
		case TESTITEM_TCARD:
			str = getString(R.string.sdcard_name);
			break;
		case TESTITEM_BCAMERA:
			str = getString(R.string.camera_name);
			break;
		/*case TESTITEM_FCAMERA:
			str = getString(R.string.subcamera_name);
			break;
		case TESTITEM_FM:
			str = getString(R.string.FMRadio);
			break;*/
		case TESTITEM_SIM:
			str = getString(R.string.SimCard);
			break;
		case TESTITEM_PRINT:
			str = getString(R.string.print_name);
			break;
		case TESTITEM_RFID:
			str = getString(R.string.rfid_name);
			break;
		case TESTITEM_FINGER:
			str = getString(R.string.finger_name);
			break;
		default:
			break;
		}
		return str;
	}
	public static int getCurrentTestMode()
	{
		return currentTestMode;
	}
	public static void setCurrtetTestMode(int curmode)
	{
		currentTestMode = curmode;
	}
}
