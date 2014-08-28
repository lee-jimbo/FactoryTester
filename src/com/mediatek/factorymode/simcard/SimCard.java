package com.mediatek.factorymode.simcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mediatek.factorymode.FactoryMode;
import com.mediatek.factorymode.R;
//import com.mediatek.telephony.TelephonyManagerEx;
import com.mediatek.common.featureoption.FeatureOption;
import com.android.internal.telephony.ITelephony;
import android.os.SystemProperties;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.telephony.PhoneConstants;
import android.os.ServiceManager;

public class SimCard extends Activity{

	public static final String TAG = "SimCard test"; 
	private TextView sim1TextView ;
	private TextView sim2TextView;
	private Button successButton;
	private Button failButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simcard);
		//TelephonyManager tm = (TelephonyManager)getSystemService(this.TELEPHONY_SERVICE);
		//TelephonyManagerEx tm = TelephonyManagerEx.getDefault();
		boolean Sim1Exist = true;
              boolean Sim2Exist = true;

		sim1TextView = (TextView)this.findViewById(R.id.simcard_sim1_info);
		sim2TextView = (TextView)this.findViewById(R.id.simcard_sim2_info);
		successButton = (Button)this.findViewById(R.id.simcard_bt_ok);
	      failButton = (Button)this.findViewById(R.id.simcard_bt_failed);
		if (FeatureOption.MTK_GEMINI_SUPPORT == true)
             {
                     ITelephony  iTelephony = ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
            	 	try{
                   		Sim1Exist = iTelephony.isSimInsert(PhoneConstants.GEMINI_SIM_1);
                   		Sim2Exist = iTelephony.isSimInsert(PhoneConstants.GEMINI_SIM_2);
             	 	}catch(RemoteException e){
                   		Log.i(TAG, "RemoteException happens......");
             	 	}
             }
		if(Sim1Exist)
		{
			sim1TextView.setText("sim1 exist");
		}else
		{
			sim1TextView.setText("sim1 not exist");
		}
		if(Sim2Exist)
		{
			sim2TextView.setText("sim2 exist");
		}else
		{
			sim2TextView.setText("sim2 not exist");
		}

		//remove temporily by david.w 
//		if(FeatureOption.MMI_SINGLE_SIM_CONFIG == true) 
		if ("true".equals(SystemProperties.get("ro.config.nosim2signal", "false")))
		{
			sim2TextView.setVisibility(View.GONE);
		}
		successButton = (Button)this.findViewById(R.id.simcard_bt_ok);
		failButton = (Button)this.findViewById(R.id.simcard_bt_failed);
		successButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SimCard.this, FactoryMode.class);
				setResult(RESULT_OK,intent);
				finish();
			}
			
		});
		failButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SimCard.this, FactoryMode.class);
				setResult(RESULT_CANCELED,intent);
				finish();
			}
			
		});
	}

}
