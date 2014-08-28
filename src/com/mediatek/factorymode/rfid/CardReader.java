/**
 * 文件名：CardReader.java
 * author: jimbo
 * 版本信息 : magniwill copyright 2014
 * 日期：2014-8-25
 * 
 */
package com.mediatek.factorymode.rfid;

import java.util.Arrays;

import android.util.Log;

import com.odm.rfid.RfidDevice;


/**
 * @author Administrator
 *
 */
public class CardReader {
	private static final String TAG = "CardReader";
	private static final boolean DEBUG = true;

	/**
	 * 找卡命令
	 */
	byte[] search_card_command = new byte[] { (byte) 0xAA, (byte) 0xAA,
			(byte) 0xAA, (byte) 0x96, (byte) 0x69, (byte) 0x00, (byte) 0x03,
			(byte) 0x20, (byte) 0x01, (byte) 0x22 };
	/**
	 * 选卡命令
	 */
	byte[] select_card_command = new byte[] { (byte) 0xAA, (byte) 0xAA,
			(byte) 0xAA, (byte) 0x96, (byte) 0x69, (byte) 0x00, (byte) 0x03,
			(byte) 0x20, (byte) 0x02, (byte) 0x21 };
	/**
	 * 读卡命令
	 */
	byte[] read_card_command = new byte[] { (byte) 0xAA, (byte) 0xAA,
			(byte) 0xAA, (byte) 0x96, (byte) 0x69, (byte) 0x00, (byte) 0x03,
			(byte) 0x30, (byte) 0x01, (byte) 0x32 };
	/**
	 * 读模块命令
	 */
	byte[] read_card_module = new byte[] { (byte) 0xAA, (byte) 0xAA,
			(byte) 0xAA, (byte) 0x96, (byte) 0x69, (byte) 0x00, (byte) 0x03,
			(byte) 0x12, (byte) 0xFF, (byte) 0xEE };
	/**
	 * E8读卡命令
	 */
	byte[] read_card_command_e8 = new byte[] { (byte) 0xAA, (byte) 0xAA,
			(byte) 0xAA, (byte) 0x96, (byte) 0x69, (byte) 0x00, (byte) 0x03,
			(byte) 0x20, (byte) 0x01, (byte) 0x22 };
	
	private static final byte[] VALID_READ_HEADER = new byte[] { (byte) 0xaa,
			(byte) 0xaa, (byte) 0xaa, (byte) 0x96, (byte) 0x69, (byte) 0x05,
			(byte) 0x08 };
	
	
	public RfidDevice device;

	public CardReader(RfidDevice device) {
		this.device = device;
	}

	/**
	 * 找卡
	 * 
	 * @return
	 */
	public int searchCard() throws InterruptedException {
		// if (DEBUG) Log.i("TAG", "CardReader-----searchCard");
		if (DEBUG)
			Log.d("CardReader", "searchCard device.mFd=" + device.getFd());
		if (DEBUG)
			Log.d("CardReader", "searchCard device.mFd=" + device.getFd());
		device.Write(search_card_command, 10);
		Thread.sleep(80);// 15
		byte[] pBuffer = new byte[1024];
		int rr = device.Read(pBuffer);
		return rr;
	}

	/**
	 * 选卡
	 * 
	 * @return
	 */
	public int selectCard() throws InterruptedException {
		// if (DEBUG) Log.i("TAG", "CardReader-----selectCard");
		device.Write(select_card_command, 10);
		Thread.sleep(100);
		byte[] pBuffer = new byte[1024];
		int rr = device.Read(pBuffer);
		return rr;
	}

	/**
	 * 读卡
	 * 
	 * @return
	 */
	public byte[] readCard() throws InterruptedException {
		if (DEBUG)
			Log.i("TAG", "CardReader-------readCard");
		int count = 0;
		int dstPos = 0;
		byte[] dst = new byte[2048];
		byte[] pBuffer1 = new byte[1024];

		if (DEBUG)
			Log.i(TAG, "readCard 读卡 ");
		device.Write(read_card_command, 10);
		if (DEBUG)
			Log.i(TAG, "readCard------------");
		Thread.sleep(100);

		while (dstPos < 1294 && count++ < 10) {
			if (DEBUG)	Log.i(TAG, "readCard-------start read");
			int d1 = device.Read(pBuffer1);
			if (d1 > 0) {
				System.arraycopy(pBuffer1, 0, dst, dstPos, d1);
				dstPos += d1;
			}
			if (DEBUG)	Log.i(TAG, "readCard-------dstPos="+dstPos+"  count="+count + "  d1="+d1);
			Thread.sleep(160);// **时间太短界面会出问题
		}

		if (DEBUG)
			Log.i(TAG, "readCard dstPos=--==" + dstPos);
		if (dstPos >= 1295) {// 原
			byte[] ret = new byte[dstPos];
			System.arraycopy(dst, 0, ret, 0, dstPos);
			if (ret.length < 12) {
				return this.readCard();
			} else {
				if (DEBUG)
					Log.i(TAG, "ret-===========" + ret.length);
				return ret;
			}
		} else {
			return null;
		}
	}
	
	public byte[] readCardE8() throws InterruptedException {
		if (DEBUG) Log.i("TAG", "CardReader-------readCard");
		int count = 0;
		int dstPos = 0;
		byte[] dst = new byte[2048];
		byte[] pBuffer1 = new byte[1024];

		if (DEBUG) Log.i(TAG, "readCard 读卡 ");
		device.Read(pBuffer1); // skip old data
		device.Write(read_card_command_e8, 10);
		if (DEBUG) Log.i(TAG, "readCard------------=");
		Thread.sleep(1700);

		while (dstPos < 1294 && count++ < 10){
			int d1 = device.Read(pBuffer1);
			byte[] header = Arrays.copyOfRange(pBuffer1, 0, VALID_READ_HEADER.length);
			boolean startWithValidRead = Arrays.equals(VALID_READ_HEADER, header);
			if (!startWithValidRead) {
				header = Arrays.copyOfRange(dst, 0, VALID_READ_HEADER.length);
				startWithValidRead = Arrays.equals(VALID_READ_HEADER, header);
			}

			if (d1 > 0 && startWithValidRead) {
				System.arraycopy(pBuffer1, 0, dst, dstPos, d1);
				dstPos += d1;
			}
			Thread.sleep(100);
		}
	
		if (DEBUG) Log.i(TAG, "readCard dstPos=--==" + dstPos);
		if (dstPos >= 1294) {//原
			byte[] ret = new byte[dstPos];
			System.arraycopy(dst, 0, ret, 0, dstPos);
			if(ret.length < 12){
				return this.readCard();
			}else{
				if (DEBUG) Log.i(TAG, "ret-==========="+ret.length);
				return ret;
			}
		} else {
			return null;
		}
	}
	

	/**
	 * 读取模块信息
	 * 
	 * @throws InterruptedException
	 */
	public byte[] readModule() throws InterruptedException {
		Log.i(TAG, " readModule   start ");

		int len = 0;
		byte[] buffer = new byte[1024];
//		for (int i = 0; i < 50; i++) {
		for (int i = 0; i < 100; i++) {
//				Log.i(TAG, "11111 readModule  before tty Write i = " + i);
			device.Write(read_card_module, 10);
				Log.i(TAG, "22222 readModule  after tty Write i = " + i);
			Thread.sleep(100);
				Log.i(TAG, "33333 readModule  before tty Read" );
			len = device.Read(buffer);
				Log.i(TAG, "44444 readModule after tty Read - read len = " + len);
			Thread.sleep(10);
			if (len == 27) {
				return buffer;
			}
		} 
		return null;
	}

}
