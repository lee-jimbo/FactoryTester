/**
 * 文件名：IdCardXhw.java
 * author: jimbo
 * 版本信息 : magniwill copyright 2014
 * 日期：2014-8-25
 * 
 */
package com.mediatek.factorymode.rfid;

import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.odm.rfid.RfidDevice;

/**
 * @author Administrator
 *
 */
public class IdCardXhw extends AbstractIdCardReader{

	byte[] read_card_module = new byte[] { (byte) 0xAA, (byte) 0xAA,
			(byte) 0xAA, (byte) 0x96, (byte) 0x69, (byte) 0x00, (byte) 0x03,
			(byte) 0x12, (byte) 0xFF, (byte) 0xEE };

	private static final String TAG = "IdCardXhw";
	private static final boolean DEBUG = true;

	private RfidDevice mRfidDevice;
	private CardReader mIDCardReader = null;

	private Handler outHandler = null;
	private Context context = null;
	private int nativeLongTimes = 0;

	boolean enable = false;
	boolean fd = false;
	private boolean flag = false;

	public IdCardXhw(Context context, Handler handler) {
		this.context = context;
		this.outHandler = handler;
	}

	/**
	 * 关闭读卡设备：
	 */
	@Override
	public int close() {
		// TODO Auto-generated method stub
		Log.d(TAG, "closeRfidDevice ");
		if (null != mRfidDevice) {
		} else {
			return STATUS_FAILED;
		}
		// if (mRfidDevice == null) {
		//
		// }
		if (mRfidDevice.isOpened()) {
			int closeRet = mRfidDevice.CloseDevice();
			Log.d(TAG, "rfid close, ret: " + closeRet);
		}
		if (mRfidDevice.isEnabled()) {
			boolean disable = mRfidDevice.rfidDeviceDisable();
			Log.d(TAG, "rfid disable, ret: " + disable);
		}
		mIDCardReader = null;
		mRfidDevice = null;
		return STATUS_OK;
	}

	/***
	 * 判断是否有读二代证的模块
	 * 
	 */
	@Override
	public boolean hasModule() {
		// TODO Auto-generated method stub
		if (isE8()) {
			return true;
		}
		if (mRfidDevice == null) {
			mRfidDevice = new RfidDevice(this.context);
		}
		if (!mRfidDevice.isEnabled()) {
			Log.i("t", "!mRfidDevice.isEnabled()");
			enable = mRfidDevice.rfidDeviceEnable();
		}

		if (!mRfidDevice.isOpened()) {
			/*if (isE9()) {
				fd = mRfidDevice.OpenDevice(2);
			} else if (isE6()) {
				fd = mRfidDevice.OpenDevice(115200);
			} else if (isE8()) {
				fd = mRfidDevice.OpenDevice(4);
			} else if (isE11OrE6p()) {
				fd = mRfidDevice.OpenDevice(5);
			}
			// ========================================
			// ---------- add 2013-11- 25 -----------
			// ========================================
			else if (isE6P()) {
				fd = mRfidDevice.OpenDevice(5);
			}*/
			fd = mRfidDevice.OpenDevice(0);

		}
		fd = mRfidDevice.OpenDevice(0);
		if (mIDCardReader == null) {
			mIDCardReader = new CardReader(mRfidDevice);
		}
		Log.i(TAG, "enable = " + enable + "  fd="+fd);
		if (enable && fd) {

			try {
				byte[] by = mIDCardReader.readModule();
				if (by != null && by[6] == 20 && by[9] == -112) {
					return true;
				} else {
					return false;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Log.e(TAG, "设备上电或打开失败！");
		}
		return false;

	}

	@Override
	public int open() {
		// TODO Auto-generated method stub
		if (mRfidDevice == null || mRfidDevice.isOpened() == false
				|| mIDCardReader == null) {
			Log.d(TAG, "openRfidDevice ");
			mRfidDevice = new RfidDevice(this.context);
			enable = mRfidDevice.rfidDeviceEnable();
			if (DEBUG)
				Log.d(TAG, "rfid enable, ret: " + enable);
			if (isE9()) {
				System.out.println("=========E9-- 2");
				fd = mRfidDevice.OpenDevice(2);
			} else if (isE6()) {
				System.out.println("=========E9-- 115200");
				fd = mRfidDevice.OpenDevice(115200);
			} else if (isE8()) {
				System.out.println("=========E8-- 4");
				fd = mRfidDevice.OpenDevice(4);
			} else if (isE11OrE6p()) {
				System.out.println("=========E8-- 5");
				fd = mRfidDevice.OpenDevice(5);
			}

			// ========================================
			// ---------- add 2013-11- 25 -----------
			// ========================================
			else if (isE6P()) {
				fd = mRfidDevice.OpenDevice(5);
				Log.v(TAG, "E6P打开读卡设备:" + fd);
			}
			if (DEBUG)
				Log.d(TAG, "rfid open, fd: " + fd);
			mIDCardReader = new CardReader(mRfidDevice);
			if (enable && fd) {
				return STATUS_OK;
			} else {
				return STATUS_FAILED;
			}
		} else {
			return STATUS_OK;
		}

	}

	@Override
	public int scan(Handler handler) {
		if (isE8()) {
			return scanE8(handler);
		}
		flag = true;
		for (;;) {
			if (mRfidDevice != null && mRfidDevice.isOpened()) {
				int search_rs = 0;
				try {
					search_rs = mIDCardReader.searchCard();
					 
					System.out.println("找卡... search_rs = " + search_rs);
					updateAcions(handler, "searching card ... search_rs = " + search_rs);
					
					if (search_rs == 15) {
						Log.i(TAG, "找卡成功...");
						updateAcions(handler, "search card ok!... ... ");
						int select_rs = 0;
						int count = 0;
						do {
							select_rs = mIDCardReader.selectCard();
							Log.i(TAG, "选卡... select_rs =" + select_rs);
							updateAcions(handler, "selecting card... select_rs =" + select_rs);
							if (select_rs == 19) {
								Log.i(TAG, "选卡成功...");
								updateAcions(handler, "select card ok!... ... ");
								byte[] data = mIDCardReader.readCard();
								Log.i(TAG, "读卡...");
								updateAcions(handler, "reading card ...  ");
								if (data != null && data.length > 1294) {
									Log.i(TAG, "读卡成功...");
									updateAcions(handler, "read card ok!... ... ");
									String type = "01";
									try {
										IdCardInfo card = CardInfoParser.parse(
												context, data, type);
										if (card != null) {
											Message message = new Message();
											Bundle bundle = new Bundle();
											bundle.putSerializable(EXTRA_DATA,
													card);
											message.setData(bundle);
											message.what = STATUS_OK;
											handler.sendMessage(message);
											return STATUS_OK;
										} else {
											return STATUS_FAILED;
										}
									} catch (UnsupportedEncodingException e) {
										e.printStackTrace();
									}
								}

							}
							count++;
						} while (select_rs != 19 && count < 5);
					} else {
						Thread.sleep(300);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				return STATUS_FAILED;
			}
		}
	}

	public int scanE9(Handler handler, int longTimes) {
		if (mRfidDevice != null && mRfidDevice.isOpened()) {
			try {
				doSearch(mIDCardReader);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}

	@Override
	public int scanMore(Handler handler, int longTimes) {
		if (isE8()) {
			return scanMoreE8(handler, longTimes);
		}
		int counts = 0;
		for (;;) {
			if (mRfidDevice != null && mRfidDevice.isOpened()) {
				int search_rs = 0;
				try {
					if (mIDCardReader == null) {
						return 0;
					}
					search_rs = mIDCardReader.searchCard();
					Log.e(TAG, "找卡中... search_rs = " + search_rs);
					String action = "Searching card... search_rs = " + search_rs;
					updateAcions(handler, action);
					if (search_rs == 15) {
						Log.i(TAG, "找卡成功... ... ");
						updateAcions(handler, "search card ok!... ... ");
						int select_rs = 0;
						int count = 0;
						do {
							select_rs = mIDCardReader.selectCard();
							Log.e(TAG, "选卡中...");
							updateAcions(handler, "selecting card ... ... ");
							if (select_rs == 19) {
								Log.i(TAG, "选卡成功... ...");
								updateAcions(handler, "select card ok!... ... ");
								byte[] data = mIDCardReader.readCard();
								updateAcions(handler, "read card... ... ");
								if (data != null && data.length > 1294) {
									Log.i(TAG, "读卡成功... ... ");
									updateAcions(handler, "read card ok!... ... ");
									String type = "01";
									try {
										IdCardInfo card = CardInfoParser.parse(
												context, data, type);
										if (card != null) {
											Message message = new Message();
											Bundle bundle = new Bundle();
											bundle.putSerializable(EXTRA_DATA, card);
											message.setData(bundle);
											message.what = STATUS_OK;
											handler.sendMessage(message);
										}
									} catch (UnsupportedEncodingException e) {
										e.printStackTrace();
									}
								}
							}
							count++;
						} while (select_rs != 19 && count < 5);
					} else {
						Thread.sleep(300);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				return STATUS_FAILED;
			}
		}
	}


	public int scanE8(Handler handler) {
		byte[] data = null;
		IdCardInfo card = null;
		for (;;) {
			if (mRfidDevice != null && mRfidDevice.isOpened()) {
				try {
					data = mIDCardReader.readCardE8();
					if (data != null && data.length >= 1294) {
						String type = "01";
						card = CardInfoParser.parse(context, data, type);
						if (card != null) {
							Message message = new Message();
							Bundle bundle = new Bundle();
							bundle.putSerializable(EXTRA_DATA, card);
							message.setData(bundle);
							message.what = STATUS_OK;
							handler.sendMessage(message);
							// Thread.sleep(longTimes);
							return STATUS_OK;
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
		return STATUS_FAILED;
	}

	public int scanMoreE8(Handler handler, int longTimes) {
		byte[] data = null;
		IdCardInfo card = null;
		for (;;) {
			if (mRfidDevice != null && mRfidDevice.isOpened()) {
				try {

					data = mIDCardReader.readCardE8();
					if (data != null && data.length >= 1294) {
						String type = "01";
						card = CardInfoParser.parse(context, data, type);
						if (card != null) {
							Message message = new Message();
							Bundle bundle = new Bundle();
							bundle.putSerializable(EXTRA_DATA, card);
							message.setData(bundle);
							message.what = STATUS_OK;
							handler.sendMessage(message);
							Thread.sleep(longTimes);
						}
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// break;
			} else {
				return STATUS_FAILED;
			}
		}
	}

	/**
	 * 找卡：
	 * 
	 * @param reader
	 * @throws InterruptedException
	 */
	private void doSearch(CardReader reader) throws InterruptedException {
		if (DEBUG)
			Log.i(TAG, "MainActivity -----doSearch");
		int search_rs = 0;
		if (DEBUG)
			Log.d(TAG, "doSearch reader=" + reader);
		if (DEBUG)
			Log.d(TAG, "doSearch reader.device=" + reader.device);
		if (DEBUG)
			Log.d(TAG, "doSearch reader.device.mFd=" + reader.device.getFd());
		for (;;) {
			search_rs = reader.searchCard();
			Log.i("t", "search_rs========" + search_rs);
			if (search_rs == 15) {
				if (doSelete(reader) == STATUS_OK) {
					break;
				}
			} else {
				// mHandler.sendEmptyMessageDelayed(MSG_READ_ID_CARD, 100);
			}
			Thread.sleep(130);
		}
	}

	/**
	 * 选卡：
	 */
	private int doSelete(CardReader reader) throws InterruptedException {
		if (DEBUG)
			Log.i(TAG, "MainActivity-----doSelete");
		int select_rs = 0;
		int count = 0;
		do {
			if (DEBUG)
				Log.d(TAG, "doSelete reader=" + reader);
			if (DEBUG)
				Log.d(TAG, "doSelete reader.device=" + reader.device);
			if (DEBUG)
				Log.d(TAG,
						"doSelete reader.device.mFd=" + reader.device.getFd());
			select_rs = reader.selectCard();
			count++;
			// Thread.sleep(10);
		} while (select_rs != 19 && count < 5);
		if (doRead(reader) == STATUS_OK) {
			return STATUS_OK;
		} else {
			return STATUS_FAILED;
		}
	}

	/**
	 * 读卡：
	 * 
	 * @param reader
	 * @return
	 * @throws InterruptedException
	 */
	private int doRead(CardReader reader) throws InterruptedException {
		if (DEBUG)
			Log.i(TAG, "MainActivity------doRead");
		if (DEBUG)
			Log.d(TAG, "doRead reader=" + reader);
		if (DEBUG)
			Log.d(TAG, "doRead reader.device=" + reader.device);
		if (DEBUG)
			Log.d(TAG, "doRead reader.device.mFd=" + reader.device.getFd());

		byte[] data = reader.readCard();
		if (data != null && data.length > 1294) {

			String type = "01";
			try {
				IdCardInfo card = CardInfoParser.parse(context, data, type);
				if (card != null) {
					Message message = new Message();
					Bundle bundle = new Bundle();
					bundle.putSerializable(EXTRA_DATA, card);
					message.setData(bundle);
					message.what = STATUS_OK;
					outHandler.sendMessage(message);
					return STATUS_OK;
				} else {
					return STATUS_FAILED;
					// mHandler.sendEmptyMessageDelayed(MSG_READ_ID_CARD, 100);
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			return STATUS_FAILED;
			// mHandler.sendEmptyMessageDelayed(MSG_READ_ID_CARD, 100);
		}
		return STATUS_FAILED;

	}

	// ==========================机器型号：=============================================
	public boolean isE9() {
		if (Build.MODEL.equals("HW-E9")) {
			Log.i("t", "" + Build.MODEL);
			return true;
		}
		return false;
	}

	public boolean isE6() {
		if (Build.MODEL.equals("HW-E6")) {
			Log.i("t", "" + Build.MODEL);
			return true;
		}
		return false;
	}

	public boolean isE11OrE6p() {
		if (Build.MODEL.toUpperCase().equals("E11") || Build.MODEL.equals("E6P")) {
			Log.i("t", "" + Build.MODEL);
			return true;
		}
		return false;
	}

	public boolean isE8() {
		if (Build.MODEL.equals("E8F")) {
			Log.i("t", "" + Build.MODEL);
			return true;
		}
		return false;
	}

	public boolean isE6P() {
		if (Build.MODEL.equals("E6P")) {
			Log.i("型号：", "" + Build.MODEL);
			return true;
		}
		return false;
	}
	
	
	/**
	 * @param handler
	 * @param action
	 */
	private void updateAcions(Handler handler, String action) {
		// TODO Auto-generated method stub
		Message msg = handler.obtainMessage(RfidTester.UPDATE_NOW_ACTIONS);
		msg.obj = action;
		handler.sendMessage(msg);
		
		handler.sendEmptyMessageDelayed(RfidTester.UPDATE_SCROLL_ACTION_TEXT, 100);
	}	
}
