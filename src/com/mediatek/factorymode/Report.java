package com.mediatek.factorymode;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
public class Report extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report);
		TextView report_success = (TextView)this.findViewById(R.id.report_success);
		TextView report_fail =(TextView)this.findViewById(R.id.report_failed);
		TextView report_default =(TextView)this.findViewById(R.id.report_default);
		String testsuccess = new String();
		String testfail = new String();
		String testdefault= new String();
		
		for(int i = FactoryMode.TESTITEM_START;i<FactoryMode.TESTITEM_TATOL;i++ )
		{
			String string = getStringbyitemid(i);
			if(FactoryMode.testResult[i]== FactoryMode.TEST_OK)
			{
				testsuccess = testsuccess + "|"+string;
			}else if(FactoryMode.testResult[i]== FactoryMode.TEST_FAIL){
				testfail = testfail + "|" +string;
			}
			else {
				testdefault = testdefault + "|"+string;
			}
		}
		report_success.setText(getString(R.string.report_ok)+testsuccess);
		report_fail.setText(getString(R.string.report_failed)+testfail);
		report_default.setText(getString(R.string.report_notest)+testdefault);
	}
	private String getStringbyitemid(int item_id) {
		String str = null;
		switch (item_id) {
		case FactoryMode.TESTITEM_TOUCH:
			str = getString(R.string.touchscreen_name);
			break;
		case FactoryMode.TESTITEM_LCD:
			str = getString(R.string.lcd_name);
			break;
		case FactoryMode.TESTITEM_GPS:
			str = getString(R.string.gps_name);
			break;
		case FactoryMode.TESTITEM_POWER:
			str = getString(R.string.battery_name);
			break;
		case FactoryMode.TESTITEM_KEY:
			str = getString(R.string.KeyCode_name);
			break;
		case FactoryMode.TESTITEM_SPEAKER:
			str = getString(R.string.speaker_name);
			break;
		case FactoryMode.TESTITEM_HEADSET:
			str = getString(R.string.headset_name);
			break;
		case FactoryMode.TESTITEM_MIC:
			str = getString(R.string.microphone_name);
			break;
		case FactoryMode.TESTITEM_RECEIVER:
			str = getString(R.string.earphone_name);
			break;
		case FactoryMode.TESTITEM_WIFI:
			str = getString(R.string.wifi_name);
			break;
		case FactoryMode.TESTITEM_BT:
			str = getString(R.string.bluetooth_name);
			break;
		case FactoryMode.TESTITEM_SHAKE:
			str = getString(R.string.vibrator_name);
			break;
		case FactoryMode.TESTITEM_CALL:
			str = getString(R.string.telephone_name);
			break;
		case FactoryMode.TESTITEM_BL:
			str = getString(R.string.backlight_name);
			break;
		case FactoryMode.TESTITEM_MEMORY:
			str = getString(R.string.memory_name);
			break;
		case FactoryMode.TESTITEM_GSENSOR:
			str = getString(R.string.gsensor_name);
			break;
		/*	
		case FactoryMode.TESTITEM_MSENSOR:
			str = getString(R.string.msensor_name);
			break;
		*/
		case FactoryMode.TESTITEM_LSENSOR:
			str = getString(R.string.lsensor_name);
			break;
		case FactoryMode.TESTITEM_DSENSOR:
			str = getString(R.string.psensor_name);
			break;
		case FactoryMode.TESTITEM_TCARD:
			str = getString(R.string.sdcard_name);
			break;
		case FactoryMode.TESTITEM_BCAMERA:
			str = getString(R.string.camera_name);
			break;
		/*case FactoryMode.TESTITEM_FCAMERA:
			str = getString(R.string.subcamera_name);
			break;
		case FactoryMode.TESTITEM_FM:
			str = getString(R.string.FMRadio);
			break;*/
		case FactoryMode.TESTITEM_SIM:
			str = getString(R.string.SimCard);
			break;
		default:
			break;
		}
		return str;
	}
}
