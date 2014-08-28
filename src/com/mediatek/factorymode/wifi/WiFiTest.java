package com.mediatek.factorymode.wifi;

import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mediatek.factorymode.FactoryMode;
import com.mediatek.factorymode.R;
import com.mediatek.factorymode.earphone.Earphone;

public class WiFiTest extends Activity{
	private TextView wifistate;
	private TextView wifiresult;
	private WifiManager mWifiManager;
	private WifiStateReceiver mWifiStateReceiver;
	List<ScanResult> currentWifiList;
	Button succesButton ;
	Button failButton ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi_test);
		wifistate = (TextView)this.findViewById(R.id.wifi_state_id);
		wifiresult = (TextView)this.findViewById(R.id.wifi_result_id);
		mWifiManager= (WifiManager)this.getSystemService(this.WIFI_SERVICE);
		if(mWifiManager.isWifiEnabled())
		{
			wifistate.setText(getString(R.string.WiFi_info_open));
		}else{
			wifistate.setText(getString(R.string.WiFi_info_opening));
		}
		mWifiManager.setWifiEnabled(true);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

		
		WifiStateReceiver mWifiStateReceiver = new WifiStateReceiver();
		registerReceiver(mWifiStateReceiver, intentFilter);
		mWifiManager.startScan();
		wifiresult.setText(getString(R.string.WiFi_scaning));
		
		succesButton = (Button)this.findViewById(R.id.wifi_bt_ok);
		failButton = (Button)this.findViewById(R.id.wifi_bt_failed);
		succesButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WiFiTest.this, FactoryMode.class);
				setResult(RESULT_OK,intent);
				finish();
			}
			
		});
		failButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WiFiTest.this, FactoryMode.class);
				setResult(RESULT_CANCELED,intent);
				finish();
			}
			
		});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mWifiManager.setWifiEnabled(false);
		super.onDestroy();
	}

	class WifiStateReceiver extends BroadcastReceiver {     
		   public void onReceive(Context c, Intent intent) {
			  String action = intent.getAction();
			  Log.d("wifi>>>>>>>>>>>>>action", action);
			  
			  if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
				  Bundle bundle = intent.getExtras();
				  int oldInt = bundle.getInt("previous_wifi_state");
				  int newInt = bundle.getInt("wifi_state");
				  
				  if(newInt==WifiManager.WIFI_STATE_DISABLED) {
					  //onWifiStateChange();
					  wifistate.setText(getString(R.string.WiFi_info_close));
				  }else if (newInt == WifiManager.WIFI_STATE_ENABLED){
					  wifistate.setText(getString(R.string.WiFi_info_open));
				  }else if (newInt == WifiManager.WIFI_STATE_ENABLING){
					  wifistate.setText(getString(R.string.WiFi_info_opening));
				  }else if(newInt==WifiManager.WIFI_STATE_DISABLING){
					  wifistate.setText(R.string.WiFi_info_closeing);
				  }else {
		       
				  }
			  }else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)){
				  currentWifiList = mWifiManager.getScanResults();  
				  String listview = " ";
				  for (int i = currentWifiList.size() - 1; i > 0; i--)
				  {
					  //currentWifiList.get(i).BSSID;
					  listview = currentWifiList.get(i).SSID+"\n" +listview;
					  Log.v("wwwwwwww", listview);
				  }			 
				  wifiresult.setText(listview +"");
			  }
		}
	}
	public static String StringizeIp(int ip) {
		  int ip4 = (ip>>24) & 0x000000FF;
		  int ip3 = (ip>>16) & 0x000000FF;
		  int ip2 = (ip>> 8 )& 0x000000FF;
		  int ip1 = ip       & 0x000000FF;
		  return Integer.toString(ip1) + "." + ip2 + "." + ip3 + "." + ip4;
		}
	private void onWifiStateChange() {
        String ip_str = "";
        WifiInfo info = mWifiManager.getConnectionInfo();
        if(info != null) {
         int ipaddr = info.getIpAddress();
         String wifissid = info.getSSID();
         ip_str = " (ip="+StringizeIp(ipaddr)+")";
        }
	}
}
