/**
 * 文件名：SystemUtil.java
 * author: jimbo
 * 版本信息 : magniwill copyright 2014
 * 日期：2014-8-25
 * 
 */
package com.mediatek.factorymode.rfid;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.util.Log;

/**
 * @author Administrator
 * 
 */
public class SystemUtil {
	private static final String TAG = "SystemUtil";
	private static String sdDir = null;
	private static String ePoliceDir = null;

	public static String getSDPath() {
		if (sdDir == null) {
			boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
			// 获取跟目录
			File sdDirFile = Environment.getExternalStorageDirectory();
			if (sdCardExist)
				sdDir = sdDirFile.toString();
		}
		
		Log.i(TAG, "getSDPath sddir:" + sdDir);
		return sdDir;
	}

	public static String getePoliceDir() {
		if (ePoliceDir == null) {
			ePoliceDir = getSDPath() + "/ePolice/";
			File ePoliceDirFile = new File(ePoliceDir);
			if (!ePoliceDirFile.exists())
				ePoliceDirFile.mkdirs();// 创建 的是文件
		}
		return ePoliceDir;
	}

	public static void openPlateScanner(Activity cntx) {
		PackageInfo pi;
		String packageName = "com.cubicimage.plate";
		try {
			pi = cntx.getPackageManager().getPackageInfo(packageName, 0);
			Intent resolveIntent = new Intent("android.intent.action.scanPlate", null);
			resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			resolveIntent.setPackage(pi.packageName);

			PackageManager pm = cntx.getPackageManager();
			List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);

			ResolveInfo ri = apps.iterator().next();
			if (ri != null) {
				Log.v(TAG, "resolved packageName:" + ri.activityInfo.packageName);
				String className = ri.activityInfo.name;

				Intent intent = new Intent("android.intent.action.scanPlate");
				intent.addCategory(Intent.CATEGORY_LAUNCHER);

				ComponentName cn = new ComponentName(packageName, className);

				intent.setComponent(cn);
				// startActivity(intent);
				cntx.startActivityForResult(intent, 0);
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean hasPlateScanner(Activity cntx) {
		PackageInfo pi;
		String packageName = "com.cubicimage.plate";
		try {
			pi = cntx.getPackageManager().getPackageInfo(packageName, 0);
			Intent resolveIntent = new Intent("android.intent.action.scanPlate", null);
			resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			resolveIntent.setPackage(pi.packageName);

			PackageManager pm = cntx.getPackageManager();
			List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);

			return apps.isEmpty() ? false : true;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

}
