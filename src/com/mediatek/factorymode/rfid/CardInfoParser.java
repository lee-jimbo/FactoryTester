/**
 * 文件名：CardInfoParser.java
 * author: jimbo
 * 版本信息 : magniwill copyright 2014
 * 日期：2014-8-25
 * 
 */
package com.mediatek.factorymode.rfid;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import com.odm.rfid.RfidDevice;

/**
 * @author Administrator
 *
 */
public class CardInfoParser {
	private static final String TAG = "CardInfoParser";
	private static final boolean DEBUG = true;
			
	static int width = 102;   
    static int height = 126;
    static int pad = ((width * 3) % 4) > 0 ? (4 - (width * 3) % 4) : 0;
	public static Map<String, String> nativemap = new HashMap<String, String>();
	private static Handler handler;
	private static String headPath01;
	private static String headPath02;
	private static String headPath03;
	private static String headPath00;
	private static File headPaths01;
	private static File headPaths02;
	private static File headPaths03;
	private static File headPaths00;
	private static File imageFile;
	
	static {
		nativemap.put("01", "汉");
		nativemap.put("02", "蒙古");
		nativemap.put("03", "回");
		nativemap.put("04", "藏");
		nativemap.put("05", "维吾尔");
		nativemap.put("06", "苗");
		nativemap.put("07", "彝");
		nativemap.put("08", "壮");
		nativemap.put("09", "布依");
		nativemap.put("10", "朝鲜");
		nativemap.put("11", "满");
		nativemap.put("12", "侗");
		nativemap.put("13", "瑶");
		nativemap.put("14", "白");
		nativemap.put("15", "土家");
		nativemap.put("16", "哈尼");
		nativemap.put("17", "哈萨克");
		nativemap.put("18", "傣");
		nativemap.put("19", "黎");
		nativemap.put("20", "傈僳");
		nativemap.put("21", "佤");
		nativemap.put("22", "畲");
		nativemap.put("23", "高山");
		nativemap.put("24", "拉祜");
		nativemap.put("25", "水");
		nativemap.put("26", "东乡");
		nativemap.put("27", "纳西");
		nativemap.put("28", "景颇");
		nativemap.put("29", "柯尔克孜");
		nativemap.put("30", "土");
		nativemap.put("31", "达斡尔");
		nativemap.put("32", "仫佬");
		nativemap.put("33", "羌");
		nativemap.put("34", "布朗");
		nativemap.put("35", "撒拉");
		nativemap.put("36", "毛南");
		nativemap.put("37", "仡佬");
		nativemap.put("38", "锡伯");
		nativemap.put("39", "阿昌");
		nativemap.put("40", "普米");
		nativemap.put("41", "塔吉克");
		nativemap.put("42", "怒");
		nativemap.put("43", "乌孜别克");
		nativemap.put("44", "俄罗斯");
		nativemap.put("45", "鄂温克");
		nativemap.put("46", "德昂");
		nativemap.put("47", "保安");
		nativemap.put("48", "裕固");
		nativemap.put("49", "京");
		nativemap.put("50", "塔塔尔");
		nativemap.put("51", "独龙");
		nativemap.put("52", "鄂伦春");
		nativemap.put("53", "赫哲");
		nativemap.put("54", "门巴");
		nativemap.put("55", "珞巴");
		nativemap.put("56", "基诺");
	}
  
	public static RunawayBean parser(byte[] dst,String type) throws UnsupportedEncodingException{
		String imagName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		if(type.equals("01")){		// 01在逃(重点比对路径)
			headPath01 = SystemUtil.getePoliceDir() + "zaitao(zhongdian)/"+imagName;
			headPaths01 = new File(headPath01);
			if (!headPaths01.exists() )
				headPaths01.mkdirs();
		}else if(type.equals("02")){// 02常住/暂住比对路径
			headPath02 = SystemUtil.getePoliceDir() + "changzhu(zanzhu)/"+imagName;
			headPaths02 = new File(headPath02);
			if (!headPaths02.exists() )
				headPaths02.mkdirs();
		}
        int bfType = 0x424d; // 位图文件类型（0—1字节）   
        int bfSize = 54 + width * height * 3;// bmp文件的大小（2—5字节）   
        int bfReserved1 = 0;// 位图文件保留字，必须为0（6-7字节）   
        int bfReserved2 = 0;// 位图文件保留字，必须为0（8-9字节）   
        int bfOffBits = 54;// 文件头开始到位图实际数据之间的字节的偏移量（10-13字节） 
        // 给信息头的变量赋值   
        int biSize = 40;// 信息头所需的字节数（14-17字节）   
        int biWidth = width;// 位图的宽（18-21字节）   
        int biHeight = height;// 位图的高（22-25字节）   
        int biPlanes = 1; // 目标设备的级别，必须是1（26-27字节）   
        int biBitcount = 24;// 每个像素所需的位数（28-29字节），必须是1位（双色）、4位（16色）、8位（256色）或者24位（真彩色）之一。   
        int biCompression = 0;// 位图压缩类型，必须是0（不压缩）（30-33字节）、1（BI_RLEB压缩类型）或2（BI_RLE4压缩类型）之一。   
        int biSizeImage = width * height;// 实际位图图像的大小，即整个实际绘制的图像大小（34-37字节）   
        int biXPelsPerMeter = 0;// 位图水平分辨率，每米像素数（38-41字节）这个数是系统默认值   
        int biYPelsPerMeter = 0;// 位图垂直分辨率，每米像素数（42-45字节）这个数是系统默认值   
        int biClrUsed = 0;// 位图实际使用的颜色表中的颜色数（46-49字节），如果为0的话，说明全部使用了   
        int biClrImportant = 0;// 位图显示过程中重要的颜色数(50-53字节)，如果为0的话，说明全部重要   
    	   
		int start = 14;
		// 姓名30位，对应文档上的位数以此类推
		byte[] namebyte = Arrays.copyOfRange(dst, start, start+=30);
		byte[] sexbyte = Arrays.copyOfRange(dst, start, start+=2);
		byte[] nativebyte = Arrays.copyOfRange(dst, start, start+=4);
		byte[] bornbyte = Arrays.copyOfRange(dst, start, start+=16);
		byte[] addressbyte = Arrays.copyOfRange(dst, start, start+=70);
		byte[] numberbyte = Arrays.copyOfRange(dst, start, start+=36);
		// 发证机构
		byte[] sendunitbyte = Arrays.copyOfRange(dst, start, start+=30);
		// 有效期
		byte[] validitybyte = Arrays.copyOfRange(dst, start, start+=32);
		// 保留
		byte[] keepbyte = Arrays.copyOfRange(dst, start, start+=36);  //256个字节
		
		byte[] imagebyte = Arrays.copyOfRange(dst, start, start+=1024);
		if (DEBUG) Log.i(TAG, "0000000000000000000000000000000");
		if (DEBUG) Log.d(TAG, "parser() image data start=" + start);
		

		byte[] dstData = new byte[38862];
		RfidDevice.rfidDeviceUnpack(imagebyte,dstData,0);
		//以身份证号命名头像
		String fileName = new String(numberbyte, "UTF-16LE");
		if (DEBUG) Log.i(TAG, "------number1----------"+fileName);
//		String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//以当前时间命名头像
//		File imageFile = new File(SystemUtil.getePoliceDir()+ fileName + ".bmp");
		if(type.equals("01")){
		 imageFile = new File(headPath01+"/"+fileName+".bmp");
		}else if(type.equals("02")){
			imageFile = new File(headPath02+"/"+fileName+".bmp");
		}else if(type.equals("03")){
			imageFile = new File(headPath03+"/"+fileName+".bmp");
		}else if(type.equals("00")){
			imageFile = new File(headPath00+"/"+fileName+".bmp");
		}
		
		DataOutputStream  stream = null;
		try {
			if (DEBUG) Log.i(TAG, "11111111111111111111111111");
			ByteArrayBuffer fileDataBuf = new ByteArrayBuffer(40000);
			//out = new BufferedOutputStream(new FileOutputStream(imageFile), 38862);//有缓存区38862
			stream = new DataOutputStream (new FileOutputStream(imageFile));   

			// 输入文件头数据  
			fileDataBuf.append(new byte[]{(byte)'B'}, 0, 1);// 输入位图文件类型'BM'  
			fileDataBuf.append(new byte[]{(byte)'M'}, 0, 1);
			fileDataBuf.append(changeByte(bfSize), 0, 4); // 输入位图文件大小  (38610)
			fileDataBuf.append(changeByte(bfReserved1), 0, 2);// 输入位图文件保留字   
			fileDataBuf.append(changeByte(bfReserved2), 0, 2);// 输入位图文件保留字   
			fileDataBuf.append(changeByte(bfOffBits), 0, 4);// 输入位图文件偏移量   
			// 输入信息头数据   
			fileDataBuf.append(changeByte(biSize), 0, 4);// 输入信息头数据的总字节数   
			fileDataBuf.append(changeByte(biWidth), 0, 4);// 输入位图的宽   
			fileDataBuf.append(changeByte(biHeight), 0, 4);// 输入位图的高   
			fileDataBuf.append(changeByte(biPlanes), 0, 2);// 输入位图的目标设备级别   
			fileDataBuf.append(changeByte(biBitcount), 0, 2);// 输入每个像素占据的字节数   
			fileDataBuf.append(changeByte(biCompression), 0, 4);// 输入位图的压缩类型   
			fileDataBuf.append(changeByte(biSizeImage), 0, 4);// 输入位图的实际大小   
			fileDataBuf.append(changeByte(biXPelsPerMeter), 0, 4);// 输入位图的水平分辨率   
			fileDataBuf.append(changeByte(biYPelsPerMeter), 0, 4);// 输入位图的垂直分辨率   
			fileDataBuf.append(changeByte(biClrUsed), 0, 4);// 输入位图使用的总颜色数   
			fileDataBuf.append(changeByte(biClrImportant), 0, 4);// 输入位图使用过程中重要的颜色数   
		
			// PALETTE none for 24 bit depth IMAGE DATA
			// starting in the bottom left, working right and then up  
			// a series of 3 bytes per pixel in the order B G R.
			if (DEBUG) Log.i(TAG, "2222222222222222222222222222222222222222");
			for (int j = 0 ; j < height; j++) {
				for (int i = 0; i < width; i++) {
					// change dst data from RGB to BGR 头像旋转270度
					int imagedate = (j*width + i)*3;
					fileDataBuf.append(new byte[]{dstData[imagedate + 2]}, 0, 1);
					fileDataBuf.append(new byte[]{dstData[imagedate + 1]}, 0, 1);
					fileDataBuf.append(new byte[]{dstData[imagedate]}, 0, 1);
				}
				if (DEBUG) Log.i(TAG, "33333333333333333333333333333333333");
				// number of bytes in each row must be padded to multiple of 4
				for (int i = 0; i < pad; i++) {
					fileDataBuf.append(new byte[]{(byte)0}, 0, 1);
				}
			}
			stream.write(fileDataBuf.buffer(), 0, fileDataBuf.length());
		} catch (IOException e) {
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					if (DEBUG) Log.i(TAG, "IOException--------------"+e);
				}
			}
		}
		try {
			String name = new String(namebyte, "UTF-16LE");
			String sex = new String(sexbyte, "UTF-16LE");
			if("1".equals(sex))
				sex = "男";
			else if("2".equals(sex))
				sex = "女";
			else
				sex = "其他";
			String natv = new String(nativebyte, "UTF-16LE");
			natv = nativemap.get(natv);
			
			String birthday = new String(bornbyte, "UTF-16LE"); 
			String address = new String(addressbyte, "UTF-16LE");
			String number = new String(numberbyte, "UTF-16LE");
			
			if (DEBUG) Log.i(TAG, "-------number--------"+number);
			String sendunit = new String(sendunitbyte, "UTF-16LE");
			String validity = new String(validitybyte, "UTF-16LE");
			RunawayBean bean = new RunawayBean();
			bean.setName(name);
			bean.setSex(sex);
			bean.setPeople(natv);
			bean.setBirthday(birthday);
			bean.setAddress(address);
			bean.setNumber(number);
			bean.setSendunit(sendunit);
			bean.setValidity(validity);
			bean.setHeadimage(fileName);
			bean.setImagpath01(headPath01+"/"+fileName+".bmp");
			return bean;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			if (DEBUG) Log.i(TAG, "CardInfoParser Exception------"+e);
		}
		return null;
	}
	
	public static IdCardInfo parse(Context cntx, byte[] dst,String type) throws UnsupportedEncodingException{
		String imagName = String.valueOf(System.currentTimeMillis());
		String path = FileUtils.getAppDir(cntx);// + "IdCardReader";
		File f = new File(path);
		if( !f.exists() ){
			f.mkdirs();
		}
		
		if (DEBUG) Log.d(TAG, "parser() dst.length=---------------==" + dst.length);
		int start = 14;
		// 姓名30位，对应文档上的位数以此类推
		byte[] namebyte = Arrays.copyOfRange(dst, start, start+=30);
		byte[] sexbyte = Arrays.copyOfRange(dst, start, start+=2);
		byte[] nativebyte = Arrays.copyOfRange(dst, start, start+=4);
		byte[] bornbyte = Arrays.copyOfRange(dst, start, start+=16);
		byte[] addressbyte = Arrays.copyOfRange(dst, start, start+=70);
		byte[] numberbyte = Arrays.copyOfRange(dst, start, start+=36);
		// 发证机构
		byte[] sendunitbyte = Arrays.copyOfRange(dst, start, start+=30);
		// 有效期
		byte[] validitybyte = Arrays.copyOfRange(dst, start, start+=32);
		// 保留
		byte[] keepbyte = Arrays.copyOfRange(dst, start, start+=36);  //256个字节
		
		byte[] imagebyte = Arrays.copyOfRange(dst, start, start+=1024);
		if (DEBUG) Log.i(TAG, "0000000000000000000000000000000");
		if (DEBUG) Log.d(TAG, "parser() image data start=" + start);
		try {
			String name = new String(namebyte, "UTF-16LE");
			String sex = new String(sexbyte, "UTF-16LE");
			if("1".equals(sex))
				sex = "男";
			else if("2".equals(sex))
				sex = "女";
			else
				sex = "其他";
			String natv = new String(nativebyte, "UTF-16LE");
			natv = nativemap.get(natv);
			
			String birthday = new String(bornbyte, "UTF-16LE"); 
			String address = new String(addressbyte, "UTF-16LE");
			String number = new String(numberbyte, "UTF-16LE");
			
			if (DEBUG) Log.i(TAG, "-------number--------"+number);
			String sendunit = new String(sendunitbyte, "UTF-16LE");
			String validity = new String(validitybyte, "UTF-16LE");
			IdCardInfo bean = new IdCardInfo();
//			bean.setName(name.trim());
//			bean.setSex(sex);
//			bean.setRace(natv);
//			bean.setBirthday(birthday);
//			bean.setAddress(address.trim());
//			bean.setNumber(number);
//			bean.setIssueDept(sendunit.trim());
//			bean.setValidity(validity);
//			
//			//以身份证号命名头像
//			String img = parseImg(imagebyte, path, number+"_"+String.valueOf(System.currentTimeMillis()));
//			bean.setImgPath(img);
			
			bean.setmPersonName(name);
			bean.setmPersonSex(sex);
			bean.setmPersonNation(natv);
			bean.setmPersonBirthday(birthday);
			bean.setmPersonAddress(address);
			bean.setmPersonIdCardNum(number);
			bean.setmPersonDepartment(sendunit);
			bean.setmPersonStarDate(validity);
			String img = parseImg(imagebyte, path, number+"_"+String.valueOf(System.currentTimeMillis()));
			bean.setmPersonImage(BitmapFactory.decodeFile(img));
//			bean.setmPersonImage(BitmapFactory.decodeFile("/data/data/com.android.rfiddemo.test/app_data/id.bmp"));//----jimbo--------------------- test
			return bean;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			if (DEBUG) Log.i(TAG, "CardInfoParser Exception------"+e);
		}
		return null;
	}
	public static String parseImg(byte[] rawImg,String path, String id) throws UnsupportedEncodingException{
		String imgFile = null;
		File f = new File(path);
		if( !f.exists() ){
			f.mkdirs();
		}
		
		log("path="+ path + "  id="+id+ "  rawImg[]="+ convertByte2String(rawImg));
		
		 // 给文件头的变量赋值   
        int bfType = 0x424d; // 位图文件类型（0—1字节）   
        int bfSize = 54 + width * height * 3;// bmp文件的大小（2—5字节）   
        int bfReserved1 = 0;// 位图文件保留字，必须为0（6-7字节）   
        int bfReserved2 = 0;// 位图文件保留字，必须为0（8-9字节）   
        int bfOffBits = 54;// 文件头开始到位图实际数据之间的字节的偏移量（10-13字节） 
        // 给信息头的变量赋值   
        int biSize = 40;// 信息头所需的字节数（14-17字节）   
        int biWidth = width;// 位图的宽（18-21字节）   
        int biHeight = height;// 位图的高（22-25字节）   
        int biPlanes = 1; // 目标设备的级别，必须是1（26-27字节）   
        int biBitcount = 24;// 每个像素所需的位数（28-29字节），必须是1位（双色）、4位（16色）、8位（256色）或者24位（真彩色）之一。   
        int biCompression = 0;// 位图压缩类型，必须是0（不压缩）（30-33字节）、1（BI_RLEB压缩类型）或2（BI_RLE4压缩类型）之一。   
        int biSizeImage = width * height;// 实际位图图像的大小，即整个实际绘制的图像大小（34-37字节）   
        int biXPelsPerMeter = 0;// 位图水平分辨率，每米像素数（38-41字节）这个数是系统默认值   
        int biYPelsPerMeter = 0;// 位图垂直分辨率，每米像素数（42-45字节）这个数是系统默认值   
        int biClrUsed = 0;// 位图实际使用的颜色表中的颜色数（46-49字节），如果为0的话，说明全部使用了   
        int biClrImportant = 0;// 位图显示过程中重要的颜色数(50-53字节)，如果为0的话，说明全部重要   
    	   
		
		byte[] dstData = new byte[38862];
		RfidDevice.rfidDeviceUnpack(rawImg,dstData,0);
		//jimbo 
						//RfidUnPack.unpackImage(rawImg, dstData, 0);	//jni
//		byte[] resSrcData = "I'm magniwill jni test app!".getBytes();
//		byte[] resData = new byte[128];
//		if (DEBUG) Log.i(TAG, "????????------befor jni resSrcData="+ new String(resSrcData));
//		if (DEBUG) Log.i(TAG, "????????------befor jni call:resData="+ new String(resData));
//		RfidUnPack.unpackImage(resSrcData, resData, 0);	//jni
//		if (DEBUG) Log.i(TAG, "????????-----after jni call: resData="+ new String(resData));
		
					//	com.android.rfid.RfidDevice.rfidDeviceUnpack(rawImg, dstData, 0);	//调用盛本so库解析图片
		 
		//以身份证号命名头像
		String fileName = id;
		if (DEBUG) Log.i(TAG, "------number1----------"+fileName);
		imageFile = new File(path+"/"+fileName+".bmp");
		
		DataOutputStream  stream = null;
		try {
			if (DEBUG) Log.i(TAG, "11111111111111111111111111");
			ByteArrayBuffer fileDataBuf = new ByteArrayBuffer(40000);
			//out = new BufferedOutputStream(new FileOutputStream(imageFile), 38862);//有缓存区38862
			stream = new DataOutputStream (new FileOutputStream(imageFile));   

			// 输入文件头数据  
			fileDataBuf.append(new byte[]{(byte)'B'}, 0, 1);// 输入位图文件类型'BM'  
			fileDataBuf.append(new byte[]{(byte)'M'}, 0, 1);
			fileDataBuf.append(changeByte(bfSize), 0, 4); // 输入位图文件大小  (38610)
			fileDataBuf.append(changeByte(bfReserved1), 0, 2);// 输入位图文件保留字   
			fileDataBuf.append(changeByte(bfReserved2), 0, 2);// 输入位图文件保留字   
			fileDataBuf.append(changeByte(bfOffBits), 0, 4);// 输入位图文件偏移量   
			// 输入信息头数据   
			fileDataBuf.append(changeByte(biSize), 0, 4);// 输入信息头数据的总字节数   
			fileDataBuf.append(changeByte(biWidth), 0, 4);// 输入位图的宽   
			fileDataBuf.append(changeByte(biHeight), 0, 4);// 输入位图的高   
			fileDataBuf.append(changeByte(biPlanes), 0, 2);// 输入位图的目标设备级别   
			fileDataBuf.append(changeByte(biBitcount), 0, 2);// 输入每个像素占据的字节数   
			fileDataBuf.append(changeByte(biCompression), 0, 4);// 输入位图的压缩类型   
			fileDataBuf.append(changeByte(biSizeImage), 0, 4);// 输入位图的实际大小   
			fileDataBuf.append(changeByte(biXPelsPerMeter), 0, 4);// 输入位图的水平分辨率   
			fileDataBuf.append(changeByte(biYPelsPerMeter), 0, 4);// 输入位图的垂直分辨率   
			fileDataBuf.append(changeByte(biClrUsed), 0, 4);// 输入位图使用的总颜色数   
			fileDataBuf.append(changeByte(biClrImportant), 0, 4);// 输入位图使用过程中重要的颜色数   
		
			// PALETTE none for 24 bit depth IMAGE DATA
			// starting in the bottom left, working right and then up  
			// a series of 3 bytes per pixel in the order B G R.
			if (DEBUG) Log.i(TAG, "2222222222222222222222222222222222222222");
			for (int j = 0 ; j < height; j++) {
				for (int i = 0; i < width; i++) {
					// change dst data from RGB to BGR 头像旋转270度
					int imagedate = (j*width + i)*3;
					fileDataBuf.append(new byte[]{dstData[imagedate + 2]}, 0, 1);
					fileDataBuf.append(new byte[]{dstData[imagedate + 1]}, 0, 1);
					fileDataBuf.append(new byte[]{dstData[imagedate]}, 0, 1);
				}
				if(j%10 ==0) 
					if (DEBUG) Log.i(TAG, "33333333333333333333333333333333333");
				// number of bytes in each row must be padded to multiple of 4
				for (int i = 0; i < pad; i++) {
					fileDataBuf.append(new byte[]{(byte)0}, 0, 1);
				}
			}
			stream.write(fileDataBuf.buffer(), 0, fileDataBuf.length());
			
			stream.close();
			log("imageFile.getPath()="+imageFile.getPath());
			return imageFile.getPath();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public static byte[] changeByte(int data){   
    	if (DEBUG) Log.i(TAG, "CardInfoParser------changeByte");
    	byte b4 = (byte)((data)>>24);   
        byte b3 = (byte)(((data)<<8)>>24);   
        byte b2= (byte)(((data)<<16)>>24);   
        byte b1 = (byte)(((data)<<24)>>24);   
        byte[] bytes = {b1,b2,b3,b4};   
        log("changeByte-- data:"+data+ "   result byte[] is:"+convertByte2String(bytes));
       return bytes;   
  } 
	
	/**
	 * @param string
	 */
	private static void log(String string) {
		// TODO Auto-generated method stub
		if (DEBUG) Log.d(TAG, "--   "+string);
	}
	private static String convertByte2String(byte[] byteArray){
		StringBuilder sb = new StringBuilder();
		for(byte b:byteArray){
			sb.append("0X");
			sb.append(Integer.toHexString(b & 0XFF));
			sb.append(", ");
		}
		sb.delete(sb.length() -2, sb.length()-1);
		return new String(sb).toUpperCase();
	}
}
