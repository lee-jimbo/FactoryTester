/**
 * 文件名：FingerTester.java
 * author: jimbo
 * 版本信息 : magniwill copyright 2014
 * 日期：2014-8-27
 * 
 */
package com.mediatek.factorymode.finger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mediatek.factorymode.FactoryMode;
import com.mediatek.factorymode.R;
import com.odm.OdmUtil;
import com.odm.as60x.AS60xDevice;
import com.odm.as60x.AS60xUtil;
import com.odm.tty.TtyUtil;

/**
 * @author Administrator
 *
 */
public class FingerTester extends Activity {
	private Button succesButton;
	private Button failButton;


	private static final String TAG = "FingerTester";
	private TextView mTextViewInfo;
	private ScrollView mScrollViewInfo;
	
	private ImageView mImageFingerprint;
	private Button mButtonGetSysInfo;
	private Button mButtonGetImage;
	private Button mButtonConnectTest;
	private Button mButtonEraseFlash;

	private AS60xDevice mAS60xDevice;
	private static final int MSG_UPDATE_TEXTVIEW = 1;
	private static final int MSG_UPDATE_SCROLLVIEW = 2;
	private static final int MSG_GET_IMAGE_START = 3;
	private static final int MSG_GET_IMAGE_END = 4;
	private static final int MSG_UPDATE_IMAGEVIEW = 5;

	private static final int UPDATE_SCROLLVIEW_WAIT_MILLIS = 100;
	// private static final int AP_SERIAL_MAX_RATE = 460800;
	private static final int AP_SERIAL_MAX_RATE = 230400;
	// private static final int AP_SERIAL_MAX_RATE = 115200;
	// private static final int AP_SERIAL_MAX_RATE = 57600;

	// ------------------ mHandler ----------------

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_UPDATE_TEXTVIEW:
				mTextViewInfo.append((String) msg.obj);
				mTextViewInfo.append("\n");
				break;

			case MSG_UPDATE_SCROLLVIEW:
				if (mTextViewInfo.getHeight() - mScrollViewInfo.getHeight() > 0) {
					mScrollViewInfo.scrollTo(0, mTextViewInfo.getHeight() - mScrollViewInfo.getHeight());
				}
				break;
			// 开始获取指纹：
			case MSG_GET_IMAGE_START:
				mButtonGetImage.setEnabled(false);
				break;

			// 结束获取指纹：
			case MSG_GET_IMAGE_END:
				mButtonGetImage.setEnabled(true);
				break;

			// 更新ImageView的信息：
			case MSG_UPDATE_IMAGEVIEW:
				mImageFingerprint.setImageBitmap((Bitmap) msg.obj);
				break;

			default:
				break;
			}
		}
	};

	private View.OnClickListener mListener = new View.OnClickListener() {
		public void onClick(View view) {
			switch (view.getId()) {

			case R.id.finger_button_get_sysinfo: // --- 获取系统信息：
				getSysInfo();
				break;

			case R.id.finger_button_get_image: // -- 获取指纹信息：
				mImageFingerprint.setImageResource(R.drawable.image_nofinger);
				new Thread() {
					public void run() {
						mHandler.sendEmptyMessage(MSG_GET_IMAGE_START);
						getFingetprintChar();
						mHandler.sendEmptyMessage(MSG_GET_IMAGE_END);
					}
				}.start();
				break;

			case R.id.finger_button_connect_test: // ------- 测试连接：
				deviceConnectTest();
				break;

			// -------- 2013-10-16-14:43 cancle

			case R.id.finger_button_erase_flash:
			// new AlertDialog.Builder(FingerprintActivity.this)
			// .setTitle("Attention")
			// .setMessage("Erase FLASH will cause AS60x can't not work anymore, it will need to download firmware by PC tools through USB.")
			// .setNeutralButton(
			// android.R.string.ok, new DialogInterface.OnClickListener() {
			// public void onClick(DialogInterface dialog, int which) {
			// deviceEraseFlash();
			// }
			// }
			// )
			// .setNegativeButton(android.R.string.cancel, null)
			// .setOnCancelListener(null)
			// .create()
			// .show();
			 break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.finger);

		// ok\cancel button
		succesButton = (Button) this.findViewById(R.id.finger_ok);
		failButton = (Button) this.findViewById(R.id.finger_failed);
		succesButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FingerTester.this, FactoryMode.class);
				setResult(RESULT_OK, intent);
				finish();
			}

		});
		failButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FingerTester.this, FactoryMode.class);
				setResult(RESULT_CANCELED, intent);
				finish();
			}

		});



		
		mTextViewInfo = (TextView) findViewById(R.id.text_view_info);
        mScrollViewInfo = (ScrollView) findViewById(R.id.scroll_view_info);
        mImageFingerprint = (ImageView) findViewById(R.id.image_fingerprint);
        mButtonGetSysInfo = (Button) findViewById(R.id.finger_button_get_sysinfo);
        mButtonGetImage = (Button) findViewById(R.id.finger_button_get_image);// -- 获取指纹信息:
        mButtonConnectTest = (Button) findViewById(R.id.finger_button_connect_test);
        mButtonEraseFlash = (Button) findViewById(R.id.finger_button_erase_flash);

        mImageFingerprint.setImageResource(R.drawable.image_nofinger);
        mButtonGetSysInfo.setOnClickListener(mListener);
        mButtonGetImage.setOnClickListener(mListener);
        mButtonConnectTest.setOnClickListener(mListener);
        mButtonEraseFlash.setOnClickListener(mListener);

        mButtonEraseFlash.setVisibility(View.GONE);

        boolean sysParaChanged = false;
        mAS60xDevice = new AS60xDevice(FingerTester.this, TtyUtil.E11_AS60x_TTY_DEV);
//        mAS60xDevice = new AS60xDevice(FingerprintActivity.this, TtyUtil.E11_AS60x_TTY_DEV, 57600);
//        mAS60xDevice = new AS60xDevice(FingerprintActivity.this, TtyUtil.E11_AS60x_TTY_DEV, 115200);
//        mAS60xDevice = new AS60xDevice(FingerprintActivity.this, TtyUtil.E11_AS60x_TTY_DEV, 230400);
//        mAS60xDevice = new AS60xDevice(FingerprintActivity.this, TtyUtil.E11_AS60x_TTY_DEV, 460800);
        mAS60xDevice.deviceEnable();
        mAS60xDevice.deviceOpen();
        
        if (AS60xUtil.Max_DataSize != mAS60xDevice.getDataSize()) {
            mAS60xDevice.setDataSize(AS60xUtil.SysPara_DataSize256);
            sysParaChanged = true;
        }
        Log.d(TAG,"reset 000   baudrate--------"+mAS60xDevice.getBaudRate());
        if (AP_SERIAL_MAX_RATE != mAS60xDevice.getBaudRate()) {
        	Log.d(TAG,"reset 111   baudrate--------"+mAS60xDevice.getBaudRate());
            mAS60xDevice.setBaudRate(AP_SERIAL_MAX_RATE);	//?????????? 
            Log.d(TAG,"reset 222 baudrate--------"+mAS60xDevice.getBaudRate());
            sysParaChanged = true;
        }
        if (sysParaChanged) {
            mAS60xDevice.deviceDisable();
            mAS60xDevice.deviceEnable();
        }
        mAS60xDevice.deviceOpen();
        showHwInitInfo();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//Activity被销毁，即退回界面的时候，关闭读卡设备：
        mAS60xDevice.deviceClose();
        mAS60xDevice.deviceDisable();
	}

	private void appendTextView(String text) {
		Message msgUpdateText = mHandler.obtainMessage(MSG_UPDATE_TEXTVIEW, (Object) text);
		mHandler.sendMessage(msgUpdateText);
		if (mTextViewInfo.getHeight() - mScrollViewInfo.getHeight() > 0) {
			mHandler.sendEmptyMessageDelayed(MSG_UPDATE_SCROLLVIEW, UPDATE_SCROLLVIEW_WAIT_MILLIS);
		}
	}

	private void showHwInitInfo() {
		if (mAS60xDevice.isEnabled()) {
			appendTextView("Enable device success.");
		} else {
			appendTextView("Enable device failed!");
		}
		if (mAS60xDevice.isOpened()) {
			appendTextView("Open device success.");
		} else {
			appendTextView("Open device failed!");
		}
	}
	
	/**
	 * 获取系统信息：
	 */
	private void getSysInfo() {
		AS60xUtil.SysInfo sysInfo = mAS60xDevice.readSysInfo();
		if (sysInfo != null) {
			appendTextView(sysInfo.toString());
		} else {
			appendTextView("Get SysInfo failed!");
		}
	}
	
	/**
	 * 连接测试
	 */
	private void deviceConnectTest() {
		int validTempleteNum = mAS60xDevice.doValidTempleteNum();
		appendTextView("get validTempleteNum=" + validTempleteNum);
		if (validTempleteNum >= 0) {
			appendTextView("connect with device success.");
		} else {
			appendTextView("connect with device failed!");
		}
	}

	private void updateImageView(Bitmap bitmap) {
		Message msgUpdateImage = mHandler.obtainMessage(MSG_UPDATE_IMAGEVIEW, (Object) bitmap);
		mHandler.sendMessage(msgUpdateImage);
	}

	/**
	 * 获取指纹
	 */
	private void getFingetprintChar() {
		int getImageRet = AS60xUtil.RetCode_Unknow;
		int getImgCnt = 500;
		System.out.println("请把你的手指放到指纹扫描器上--Englist: Please put your finger on the sersor");
		appendTextView("Please put your finger on the sersor.");
		for (int i = 0; i < getImgCnt && mAS60xDevice.isOpened(); i++) {
			getImageRet = mAS60xDevice.doGetImage();
			System.out.println("---getImageRet=" + getImageRet);
			System.out.println("获取的次数：GetImage i=" + i + ", result code=" + AS60xUtil.getResultCodeStr(getImageRet));
			appendTextView("GetImage i=" + i + ", result code=" + AS60xUtil.getResultCodeStr(getImageRet));
			if (getImageRet == AS60xUtil.RetCode_OK) {
				byte[] imageData = mAS60xDevice.doUpImage();
				if (imageData != null) {
					System.out.println("指纹更新成功：-- ");

					System.out.println("指纹图片的路径是：" + imageData.length);

					appendTextView("UpImage success.");
					appendTextView("Up fingerprint image data length: " + imageData.length);
					Bitmap imgBitmap = AS60xUtil.getImgBitmap(imageData, false);
					if (imgBitmap != null) {
						updateImageView(imgBitmap);
					}
				} else {
					System.out.println("指纹更新失败：-- ");
					appendTextView("UpImage failed.");
				}
				if (AS60xUtil.RetCode_OK == mAS60xDevice.doGenChar(AS60xUtil.BufferID_CharBuffer1)) {
					System.out.println("GenChar success. --  获取手指字符成功");
					appendTextView("GenChar success.");
					byte[] charData = mAS60xDevice.doUpChar(AS60xUtil.BufferID_CharBuffer1);
					if (charData != null) {
						System.out.println("UpChar success.-- 更新Char成功");

						System.out.println("手指字符的长度 Up fingerprint Char data length: ：" + charData.length);
						appendTextView("UpChar success.");
						appendTextView("Up fingerprint Char data length: " + charData.length);
					} else {
						appendTextView("UpChar failed.");
						System.out.println("UpChar failed. -- 更新Char失败");
					}
				} else {
					System.out.println("GenChar failed.获取手指字符失败");
					appendTextView("GenChar failed.");
				}
				break;
			} else {
				OdmUtil.delayms(200);
			}
		}
		if (getImageRet != AS60xUtil.RetCode_OK) {
			System.out.println("GetImage failed for try " + getImgCnt + " times.");
			appendTextView("GetImage failed for try " + getImgCnt + " times.");
		}
	}

//    private void deviceEraseFlash() {
//        int ret = AS60xUtil.RetCode_Unknow;
//        ret = mAS60xDevice.eraseFlash();
//        if (ret == AS60xUtil.RetCode_OK) {
//            appendTextView("updateSysCode erase flash success");
//        } else {
//            appendTextView("updateSysCode erase flash failed");
//        }
//    }	

}
