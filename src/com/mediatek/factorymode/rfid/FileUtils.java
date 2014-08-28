/**
 * 文件名：FileUtils.java
 * author: jimbo
 * 版本信息 : magniwill copyright 2014
 * 日期：2014-8-25
 * 
 */
package com.mediatek.factorymode.rfid;

import java.io.File;

import android.content.Context;
import android.util.Log;

/**
 * @author Administrator
 *
 */
public class FileUtils {

	private static final String TAG = "FileUtils";

	public static void removeFile(String imgPath) {
		// TODO Auto-generated method stub
		File f = new File(imgPath);
		if( f.exists() ){
			f.delete();
		}
	}

	public static String getAppDir(Context cntx) {
		// TODO Auto-generated method stub
		File f =  cntx.getDir("data", Context.MODE_WORLD_WRITEABLE);
		String dir = f.getPath();
		Log.v(TAG, dir);
		return dir;
	}
	

}
