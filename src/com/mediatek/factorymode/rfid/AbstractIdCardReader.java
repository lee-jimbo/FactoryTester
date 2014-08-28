/**
 * 文件名：AbstractIdCardReader.java
 * author: jimbo
 * 版本信息 : magniwill copyright 2014
 * 日期：2014-8-25
 * 
 */
package com.mediatek.factorymode.rfid;

import android.os.Handler;

/**
 * @author Administrator
 *
 */
public abstract class AbstractIdCardReader
{
  public static final String EXTRA_DATA = "IdCardReaderInterface";
  public static final int STATUS_OK = 0;
  public static final int STATUS_FAILED = 256;
  public static final int STATUS_SUCCESS = 0;
  public static final int STATUS_FAILED_READ = 1024;

  public abstract boolean hasModule();

  public abstract int open();

  public abstract int scan(Handler paramHandler);
  
  public abstract int scanMore(Handler paramHandler, int paramInt);

  public abstract int close();
}