package com.mediatek.factorymode.bluetooth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.mediatek.factorymode.FactoryMode;
import com.mediatek.factorymode.R;
import com.mediatek.factorymode.vibratortest.Vibratortest;

public class Bluetooth extends Activity {
	private TextView btstate;
	private ListView btlistview;
	private BluetoothAdapter btadapter;
	private ArrayAdapter<String> adapter;
	private Button succesButton ;
	private Button failButton ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.btlist);
		btstate = (TextView) this.findViewById(R.id.bt_state_id);
		btlistview = (ListView) this.findViewById(R.id.listView1); 
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

		btlistview.setAdapter(adapter);
		btadapter = BluetoothAdapter.getDefaultAdapter();
		
		if (btadapter == null) {
			btstate.setText("not support bluetooth");
		} else {
			if (!btadapter.isEnabled()) {
				btstate.setText(getString(R.string.Bluetooth_opening));
				btadapter.enable();
			} else {
				btstate.setText(getString(R.string.Bluetooth_open));
			}
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
			intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
			intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
			intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
			intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
			this.registerReceiver(receiver, intentFilter);
			btadapter.startDiscovery();
			
		}
		succesButton = (Button)this.findViewById(R.id.bttest_ok);
		failButton = (Button)this.findViewById(R.id.bttest_failed);
		succesButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Bluetooth.this, FactoryMode.class);
				setResult(RESULT_OK,intent);
				finish();
			}
			
		});
		failButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Bluetooth.this, FactoryMode.class);
				setResult(RESULT_CANCELED,intent);
				finish();
			}
			
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(btadapter !=null)
		{
			btadapter.disable();
		}
		super.onDestroy();
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d("BT>>>>>>>>>>>>>action", action);
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {				
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Log.d("BT>>>>>>>>>>>>>"+device.getName(), "22222:"+device.getAddress());
			
				adapter.add("device name:" + device.getName() + "\ndevice addr:"
						+ device.getAddress());
				btlistview.setAdapter(adapter);

			} else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
				
				String stateExtra = BluetoothAdapter.EXTRA_STATE;  
                int  btstates = intent.getIntExtra(stateExtra, -1);  
				if ((btstates == BluetoothAdapter.STATE_TURNING_ON)
						|| (btstates == BluetoothAdapter.STATE_ON)) {
					btstate.setText(getString(R.string.Bluetooth_open));
					btadapter.startDiscovery();
				} else if ((btstates == BluetoothAdapter.STATE_TURNING_OFF)
						|| (btstates == BluetoothAdapter.STATE_OFF)) {
					btstate.setText(getString(R.string.Bluetooth_closed));
				}
			}else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			}else if((BluetoothAdapter.ACTION_DISCOVERY_FINISHED).equals(action))
			{
				//btadapter.cancelDiscovery();
			}
		}
	};
	
}
