/**
 * 文件名：RfidTester.java
 * author: jimbo
 * 版本信息 : magniwill copyright 2014
 * 日期：2014-8-22
 * 
 */
package com.mediatek.factorymode.rfid;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mediatek.factorymode.FactoryMode;
import com.mediatek.factorymode.R;

/**
 * @author Administrator
 *
 */
public class RfidTester extends Activity {
	
	private Button succesButton ;
	private Button failButton ;

	
	
	private IdCardXhw reader = null;
	private Thread mWriteThread;
	private TextView name;
	private TextView sex;
	private TextView nation;
	private TextView birth_year;
	private TextView address;
	private TextView number;
	private ImageView imghead;
	private SoundPool sp;
	private int num0;
	BluetoothAdapter blueTooth;
	private TextView tv_qfjg;
	private TextView tv_yxqx, tv_shebeiMode;
	private static final String TAG = "RfidTester";
	private int openCount = 0;
	private int closedCount = 0;
	
	
    private TextView mTextViewInfo;
    private ScrollView mScrollViewInfo;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.rfid);

		
		mTextViewInfo = (TextView) findViewById(R.id.text_view_info);
        mScrollViewInfo = (ScrollView) findViewById(R.id.scroll_view_info);
		
		
		succesButton = (Button)this.findViewById(R.id.rfid_ok);
		failButton = (Button)this.findViewById(R.id.rfid_failed);
		succesButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RfidTester.this, FactoryMode.class);
				setResult(RESULT_OK,intent);
				finishTest();
				finish();
			}
			
		});
		failButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RfidTester.this, FactoryMode.class);
				setResult(RESULT_CANCELED,intent);
				finishTest();
				finish();
			}
			
		});
		
		//读取身份证信息
		Resources resources = getResources();
		setTitle(resources.getString(R.string.rfid_app_title));
		
		sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		num0 = sp.load(RfidTester.this, R.raw.mm, 1);
		
		Log.i(TAG , "model = " + Build.MODEL);
		name = (TextView) findViewById(R.id.name);
		birth_year = (TextView) findViewById(R.id.birth_year);
		nation = (TextView) findViewById(R.id.nation);
		sex = (TextView) findViewById(R.id.sex);
		address = (TextView) findViewById(R.id.address);
		number = (TextView) findViewById(R.id.number);
		imghead = (ImageView) findViewById(R.id.imghead);

		tv_qfjg = (TextView) findViewById(R.id.qfjg);
		tv_yxqx = (TextView) findViewById(R.id.yxqx);
		tv_shebeiMode = (TextView) findViewById(R.id.tv_shebeiMode);
		tv_shebeiMode.setText(resources.getString(R.string.product_mode) + Build.MODEL);
		readIdCard();
	}
	
	
	public static final int UPDATE_NOW_ACTIONS = 10;
	public static final int UPDATE_SCROLL_ACTION_TEXT = 11;
	
	public Handler handler = new Handler() {
		
		public void handleMessage(android.os.Message msg) {
			if (msg.what == IdCardXhw.STATUS_OK) {
				Bundle b = msg.getData();
				IdCardInfo card = (IdCardInfo) b
						.getSerializable(IdCardXhw.EXTRA_DATA);
				Log.i(TAG, "card number = " + card.getmPersonIdCardNum());
				Log.i(TAG, "card name = " + card.getmPersonName());
				Log.i(TAG, "card department = " + card.getmPersonDepartment());
				Log.i(TAG, "card birthday = " + card.getmPersonBirthday());
				Log.i(TAG, "card nation = " + card.getmPersonNation());
				Log.i(TAG, "card sex = " + card.getmPersonSex());
				Log.i(TAG, "card start date = " + card.getmPersonStarDate());
				Log.i(TAG, "card end date = " + card.getmPersonEndDate());
				Log.i(TAG, "card address = " + card.getmPersonAddress());
				Log.i(TAG, "card image path = " + card.getmPersonImage());
				sp.play(num0, 1, 1, 0, 0, 1);
				if (null != reader) {
					closedCount = closedCount + 1;
					reader.close();// 关闭读卡设备
					reader = null;
					Log.e(TAG, "关闭读卡设备.... ... closedCount = " + closedCount);
				}
				updateUI(card);
			}else if(msg.what == UPDATE_NOW_ACTIONS){
				mTextViewInfo.append((String)msg.obj);
                mTextViewInfo.append("\n");
				
			}else if(msg.what == UPDATE_SCROLL_ACTION_TEXT){
				if (mTextViewInfo.getHeight() - mScrollViewInfo.getHeight() > 0) {
					mScrollViewInfo.scrollTo(0, mTextViewInfo.getHeight() - mScrollViewInfo.getHeight());
				}
			}
		};
	};	
	private void readIdCard() {
		openCount = openCount + 1;
		Log.e(TAG, "打开打开设备... ... openCount = " + openCount);
		if (blueTooth == null) {
			blueTooth = BluetoothAdapter.getDefaultAdapter();
			if (blueTooth.isEnabled()) {
				blueTooth.disable();
				blueTooth = null;
			}
		}

		mWriteThread = new Thread() {

			@Override
			public void run() {
				reader = new IdCardXhw(RfidTester.this, handler);
				if (reader.hasModule()) {
					if (reader.open() == IdCardXhw.STATUS_OK) {
						reader.scanMore(handler, 100);
					} 
				}
			}
		};
		mWriteThread.start();
	}
	
	public void updateUI(IdCardInfo card) {
		name.setText(card.getmPersonName());
		String bir = card.getmPersonBirthday();
		if (null != bir) {
			if (bir.length() >= 8) {
				birth_year.setText(bir.substring(0, 4) + "."
						+ bir.substring(4, 6) + "." + bir.substring(6, 8));
			}
		}
		nation.setText(card.getmPersonNation());
		sex.setText(card.getmPersonSex());
		address.setText(card.getmPersonAddress());
		number.setText(card.getmPersonIdCardNum());
		// Bitmap bitmap = getLoacalBitmap(card.getmPersonImage());
		Log.i(TAG, "身份证照片 card.getmPersonImage()="+card.getmPersonImage());
		imghead.setImageBitmap(card.getmPersonImage());

		tv_qfjg.setText(card.getmPersonDepartment());
		String yxqx = card.getmPersonStarDate();
		if (null != yxqx && yxqx.length() >= 16) {
			tv_yxqx.setText(yxqx.substring(0, 4) + "." + yxqx.substring(4, 6)
					+ "." + yxqx.substring(6, 8) + "--" + yxqx.substring(8, 12)
					+ "." + yxqx.substring(12, 14) + "."
					+ yxqx.substring(14, 16));
		} else {
			if (null != yxqx && yxqx.length() > 0) {
				tv_yxqx.setText(yxqx);
			}
		}
		if (null != mWriteThread) {
			Thread.currentThread().interrupt();
			if (Thread.currentThread().interrupted()) {
				Log.i(TAG, "释放读卡的线程。。。");
			} else {
				Log.i(TAG, "读卡线程--未关闭。。。");
			}
			mWriteThread = null;
		}
		// 下一次读卡：
		readIdCard();
	}	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			finishTest();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void finishTest(){
		if (null != mWriteThread) {
			Thread.currentThread().interrupt();
			if (Thread.currentThread().interrupted()) {
				Log.i(TAG, "relesse read thread");
			} else {
				Log.i(TAG, "read thread is'st closed");
			}
			mWriteThread = null;
		}
		if (null != reader) {
			reader.close();// 关闭读卡设备
			reader = null;
		}
	}

}
