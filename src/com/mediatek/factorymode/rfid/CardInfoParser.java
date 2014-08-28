/**
 * �ļ�����CardInfoParser.java
 * author: jimbo
 * �汾��Ϣ : magniwill copyright 2014
 * ���ڣ�2014-8-25
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
		nativemap.put("01", "��");
		nativemap.put("02", "�ɹ�");
		nativemap.put("03", "��");
		nativemap.put("04", "��");
		nativemap.put("05", "ά���");
		nativemap.put("06", "��");
		nativemap.put("07", "��");
		nativemap.put("08", "׳");
		nativemap.put("09", "����");
		nativemap.put("10", "����");
		nativemap.put("11", "��");
		nativemap.put("12", "��");
		nativemap.put("13", "��");
		nativemap.put("14", "��");
		nativemap.put("15", "����");
		nativemap.put("16", "����");
		nativemap.put("17", "������");
		nativemap.put("18", "��");
		nativemap.put("19", "��");
		nativemap.put("20", "����");
		nativemap.put("21", "��");
		nativemap.put("22", "�");
		nativemap.put("23", "��ɽ");
		nativemap.put("24", "����");
		nativemap.put("25", "ˮ");
		nativemap.put("26", "����");
		nativemap.put("27", "����");
		nativemap.put("28", "����");
		nativemap.put("29", "�¶�����");
		nativemap.put("30", "��");
		nativemap.put("31", "���Ӷ�");
		nativemap.put("32", "����");
		nativemap.put("33", "Ǽ");
		nativemap.put("34", "����");
		nativemap.put("35", "����");
		nativemap.put("36", "ë��");
		nativemap.put("37", "����");
		nativemap.put("38", "����");
		nativemap.put("39", "����");
		nativemap.put("40", "����");
		nativemap.put("41", "������");
		nativemap.put("42", "ŭ");
		nativemap.put("43", "���α��");
		nativemap.put("44", "����˹");
		nativemap.put("45", "���¿�");
		nativemap.put("46", "�°�");
		nativemap.put("47", "����");
		nativemap.put("48", "ԣ��");
		nativemap.put("49", "��");
		nativemap.put("50", "������");
		nativemap.put("51", "����");
		nativemap.put("52", "���״�");
		nativemap.put("53", "����");
		nativemap.put("54", "�Ű�");
		nativemap.put("55", "���");
		nativemap.put("56", "��ŵ");
	}
  
	public static RunawayBean parser(byte[] dst,String type) throws UnsupportedEncodingException{
		String imagName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		if(type.equals("01")){		// 01����(�ص�ȶ�·��)
			headPath01 = SystemUtil.getePoliceDir() + "zaitao(zhongdian)/"+imagName;
			headPaths01 = new File(headPath01);
			if (!headPaths01.exists() )
				headPaths01.mkdirs();
		}else if(type.equals("02")){// 02��ס/��ס�ȶ�·��
			headPath02 = SystemUtil.getePoliceDir() + "changzhu(zanzhu)/"+imagName;
			headPaths02 = new File(headPath02);
			if (!headPaths02.exists() )
				headPaths02.mkdirs();
		}
        int bfType = 0x424d; // λͼ�ļ����ͣ�0��1�ֽڣ�   
        int bfSize = 54 + width * height * 3;// bmp�ļ��Ĵ�С��2��5�ֽڣ�   
        int bfReserved1 = 0;// λͼ�ļ������֣�����Ϊ0��6-7�ֽڣ�   
        int bfReserved2 = 0;// λͼ�ļ������֣�����Ϊ0��8-9�ֽڣ�   
        int bfOffBits = 54;// �ļ�ͷ��ʼ��λͼʵ������֮����ֽڵ�ƫ������10-13�ֽڣ� 
        // ����Ϣͷ�ı�����ֵ   
        int biSize = 40;// ��Ϣͷ������ֽ�����14-17�ֽڣ�   
        int biWidth = width;// λͼ�Ŀ�18-21�ֽڣ�   
        int biHeight = height;// λͼ�ĸߣ�22-25�ֽڣ�   
        int biPlanes = 1; // Ŀ���豸�ļ��𣬱�����1��26-27�ֽڣ�   
        int biBitcount = 24;// ÿ�����������λ����28-29�ֽڣ���������1λ��˫ɫ����4λ��16ɫ����8λ��256ɫ������24λ�����ɫ��֮һ��   
        int biCompression = 0;// λͼѹ�����ͣ�������0����ѹ������30-33�ֽڣ���1��BI_RLEBѹ�����ͣ���2��BI_RLE4ѹ�����ͣ�֮һ��   
        int biSizeImage = width * height;// ʵ��λͼͼ��Ĵ�С��������ʵ�ʻ��Ƶ�ͼ���С��34-37�ֽڣ�   
        int biXPelsPerMeter = 0;// λͼˮƽ�ֱ��ʣ�ÿ����������38-41�ֽڣ��������ϵͳĬ��ֵ   
        int biYPelsPerMeter = 0;// λͼ��ֱ�ֱ��ʣ�ÿ����������42-45�ֽڣ��������ϵͳĬ��ֵ   
        int biClrUsed = 0;// λͼʵ��ʹ�õ���ɫ���е���ɫ����46-49�ֽڣ������Ϊ0�Ļ���˵��ȫ��ʹ����   
        int biClrImportant = 0;// λͼ��ʾ��������Ҫ����ɫ��(50-53�ֽ�)�����Ϊ0�Ļ���˵��ȫ����Ҫ   
    	   
		int start = 14;
		// ����30λ����Ӧ�ĵ��ϵ�λ���Դ�����
		byte[] namebyte = Arrays.copyOfRange(dst, start, start+=30);
		byte[] sexbyte = Arrays.copyOfRange(dst, start, start+=2);
		byte[] nativebyte = Arrays.copyOfRange(dst, start, start+=4);
		byte[] bornbyte = Arrays.copyOfRange(dst, start, start+=16);
		byte[] addressbyte = Arrays.copyOfRange(dst, start, start+=70);
		byte[] numberbyte = Arrays.copyOfRange(dst, start, start+=36);
		// ��֤����
		byte[] sendunitbyte = Arrays.copyOfRange(dst, start, start+=30);
		// ��Ч��
		byte[] validitybyte = Arrays.copyOfRange(dst, start, start+=32);
		// ����
		byte[] keepbyte = Arrays.copyOfRange(dst, start, start+=36);  //256���ֽ�
		
		byte[] imagebyte = Arrays.copyOfRange(dst, start, start+=1024);
		if (DEBUG) Log.i(TAG, "0000000000000000000000000000000");
		if (DEBUG) Log.d(TAG, "parser() image data start=" + start);
		

		byte[] dstData = new byte[38862];
		RfidDevice.rfidDeviceUnpack(imagebyte,dstData,0);
		//�����֤������ͷ��
		String fileName = new String(numberbyte, "UTF-16LE");
		if (DEBUG) Log.i(TAG, "------number1----------"+fileName);
//		String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//�Ե�ǰʱ������ͷ��
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
			//out = new BufferedOutputStream(new FileOutputStream(imageFile), 38862);//�л�����38862
			stream = new DataOutputStream (new FileOutputStream(imageFile));   

			// �����ļ�ͷ����  
			fileDataBuf.append(new byte[]{(byte)'B'}, 0, 1);// ����λͼ�ļ�����'BM'  
			fileDataBuf.append(new byte[]{(byte)'M'}, 0, 1);
			fileDataBuf.append(changeByte(bfSize), 0, 4); // ����λͼ�ļ���С  (38610)
			fileDataBuf.append(changeByte(bfReserved1), 0, 2);// ����λͼ�ļ�������   
			fileDataBuf.append(changeByte(bfReserved2), 0, 2);// ����λͼ�ļ�������   
			fileDataBuf.append(changeByte(bfOffBits), 0, 4);// ����λͼ�ļ�ƫ����   
			// ������Ϣͷ����   
			fileDataBuf.append(changeByte(biSize), 0, 4);// ������Ϣͷ���ݵ����ֽ���   
			fileDataBuf.append(changeByte(biWidth), 0, 4);// ����λͼ�Ŀ�   
			fileDataBuf.append(changeByte(biHeight), 0, 4);// ����λͼ�ĸ�   
			fileDataBuf.append(changeByte(biPlanes), 0, 2);// ����λͼ��Ŀ���豸����   
			fileDataBuf.append(changeByte(biBitcount), 0, 2);// ����ÿ������ռ�ݵ��ֽ���   
			fileDataBuf.append(changeByte(biCompression), 0, 4);// ����λͼ��ѹ������   
			fileDataBuf.append(changeByte(biSizeImage), 0, 4);// ����λͼ��ʵ�ʴ�С   
			fileDataBuf.append(changeByte(biXPelsPerMeter), 0, 4);// ����λͼ��ˮƽ�ֱ���   
			fileDataBuf.append(changeByte(biYPelsPerMeter), 0, 4);// ����λͼ�Ĵ�ֱ�ֱ���   
			fileDataBuf.append(changeByte(biClrUsed), 0, 4);// ����λͼʹ�õ�����ɫ��   
			fileDataBuf.append(changeByte(biClrImportant), 0, 4);// ����λͼʹ�ù�������Ҫ����ɫ��   
		
			// PALETTE none for 24 bit depth IMAGE DATA
			// starting in the bottom left, working right and then up  
			// a series of 3 bytes per pixel in the order B G R.
			if (DEBUG) Log.i(TAG, "2222222222222222222222222222222222222222");
			for (int j = 0 ; j < height; j++) {
				for (int i = 0; i < width; i++) {
					// change dst data from RGB to BGR ͷ����ת270��
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
				sex = "��";
			else if("2".equals(sex))
				sex = "Ů";
			else
				sex = "����";
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
		// ����30λ����Ӧ�ĵ��ϵ�λ���Դ�����
		byte[] namebyte = Arrays.copyOfRange(dst, start, start+=30);
		byte[] sexbyte = Arrays.copyOfRange(dst, start, start+=2);
		byte[] nativebyte = Arrays.copyOfRange(dst, start, start+=4);
		byte[] bornbyte = Arrays.copyOfRange(dst, start, start+=16);
		byte[] addressbyte = Arrays.copyOfRange(dst, start, start+=70);
		byte[] numberbyte = Arrays.copyOfRange(dst, start, start+=36);
		// ��֤����
		byte[] sendunitbyte = Arrays.copyOfRange(dst, start, start+=30);
		// ��Ч��
		byte[] validitybyte = Arrays.copyOfRange(dst, start, start+=32);
		// ����
		byte[] keepbyte = Arrays.copyOfRange(dst, start, start+=36);  //256���ֽ�
		
		byte[] imagebyte = Arrays.copyOfRange(dst, start, start+=1024);
		if (DEBUG) Log.i(TAG, "0000000000000000000000000000000");
		if (DEBUG) Log.d(TAG, "parser() image data start=" + start);
		try {
			String name = new String(namebyte, "UTF-16LE");
			String sex = new String(sexbyte, "UTF-16LE");
			if("1".equals(sex))
				sex = "��";
			else if("2".equals(sex))
				sex = "Ů";
			else
				sex = "����";
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
//			//�����֤������ͷ��
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
		
		 // ���ļ�ͷ�ı�����ֵ   
        int bfType = 0x424d; // λͼ�ļ����ͣ�0��1�ֽڣ�   
        int bfSize = 54 + width * height * 3;// bmp�ļ��Ĵ�С��2��5�ֽڣ�   
        int bfReserved1 = 0;// λͼ�ļ������֣�����Ϊ0��6-7�ֽڣ�   
        int bfReserved2 = 0;// λͼ�ļ������֣�����Ϊ0��8-9�ֽڣ�   
        int bfOffBits = 54;// �ļ�ͷ��ʼ��λͼʵ������֮����ֽڵ�ƫ������10-13�ֽڣ� 
        // ����Ϣͷ�ı�����ֵ   
        int biSize = 40;// ��Ϣͷ������ֽ�����14-17�ֽڣ�   
        int biWidth = width;// λͼ�Ŀ�18-21�ֽڣ�   
        int biHeight = height;// λͼ�ĸߣ�22-25�ֽڣ�   
        int biPlanes = 1; // Ŀ���豸�ļ��𣬱�����1��26-27�ֽڣ�   
        int biBitcount = 24;// ÿ�����������λ����28-29�ֽڣ���������1λ��˫ɫ����4λ��16ɫ����8λ��256ɫ������24λ�����ɫ��֮һ��   
        int biCompression = 0;// λͼѹ�����ͣ�������0����ѹ������30-33�ֽڣ���1��BI_RLEBѹ�����ͣ���2��BI_RLE4ѹ�����ͣ�֮һ��   
        int biSizeImage = width * height;// ʵ��λͼͼ��Ĵ�С��������ʵ�ʻ��Ƶ�ͼ���С��34-37�ֽڣ�   
        int biXPelsPerMeter = 0;// λͼˮƽ�ֱ��ʣ�ÿ����������38-41�ֽڣ��������ϵͳĬ��ֵ   
        int biYPelsPerMeter = 0;// λͼ��ֱ�ֱ��ʣ�ÿ����������42-45�ֽڣ��������ϵͳĬ��ֵ   
        int biClrUsed = 0;// λͼʵ��ʹ�õ���ɫ���е���ɫ����46-49�ֽڣ������Ϊ0�Ļ���˵��ȫ��ʹ����   
        int biClrImportant = 0;// λͼ��ʾ��������Ҫ����ɫ��(50-53�ֽ�)�����Ϊ0�Ļ���˵��ȫ����Ҫ   
    	   
		
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
		
					//	com.android.rfid.RfidDevice.rfidDeviceUnpack(rawImg, dstData, 0);	//����ʢ��so�����ͼƬ
		 
		//�����֤������ͷ��
		String fileName = id;
		if (DEBUG) Log.i(TAG, "------number1----------"+fileName);
		imageFile = new File(path+"/"+fileName+".bmp");
		
		DataOutputStream  stream = null;
		try {
			if (DEBUG) Log.i(TAG, "11111111111111111111111111");
			ByteArrayBuffer fileDataBuf = new ByteArrayBuffer(40000);
			//out = new BufferedOutputStream(new FileOutputStream(imageFile), 38862);//�л�����38862
			stream = new DataOutputStream (new FileOutputStream(imageFile));   

			// �����ļ�ͷ����  
			fileDataBuf.append(new byte[]{(byte)'B'}, 0, 1);// ����λͼ�ļ�����'BM'  
			fileDataBuf.append(new byte[]{(byte)'M'}, 0, 1);
			fileDataBuf.append(changeByte(bfSize), 0, 4); // ����λͼ�ļ���С  (38610)
			fileDataBuf.append(changeByte(bfReserved1), 0, 2);// ����λͼ�ļ�������   
			fileDataBuf.append(changeByte(bfReserved2), 0, 2);// ����λͼ�ļ�������   
			fileDataBuf.append(changeByte(bfOffBits), 0, 4);// ����λͼ�ļ�ƫ����   
			// ������Ϣͷ����   
			fileDataBuf.append(changeByte(biSize), 0, 4);// ������Ϣͷ���ݵ����ֽ���   
			fileDataBuf.append(changeByte(biWidth), 0, 4);// ����λͼ�Ŀ�   
			fileDataBuf.append(changeByte(biHeight), 0, 4);// ����λͼ�ĸ�   
			fileDataBuf.append(changeByte(biPlanes), 0, 2);// ����λͼ��Ŀ���豸����   
			fileDataBuf.append(changeByte(biBitcount), 0, 2);// ����ÿ������ռ�ݵ��ֽ���   
			fileDataBuf.append(changeByte(biCompression), 0, 4);// ����λͼ��ѹ������   
			fileDataBuf.append(changeByte(biSizeImage), 0, 4);// ����λͼ��ʵ�ʴ�С   
			fileDataBuf.append(changeByte(biXPelsPerMeter), 0, 4);// ����λͼ��ˮƽ�ֱ���   
			fileDataBuf.append(changeByte(biYPelsPerMeter), 0, 4);// ����λͼ�Ĵ�ֱ�ֱ���   
			fileDataBuf.append(changeByte(biClrUsed), 0, 4);// ����λͼʹ�õ�����ɫ��   
			fileDataBuf.append(changeByte(biClrImportant), 0, 4);// ����λͼʹ�ù�������Ҫ����ɫ��   
		
			// PALETTE none for 24 bit depth IMAGE DATA
			// starting in the bottom left, working right and then up  
			// a series of 3 bytes per pixel in the order B G R.
			if (DEBUG) Log.i(TAG, "2222222222222222222222222222222222222222");
			for (int j = 0 ; j < height; j++) {
				for (int i = 0; i < width; i++) {
					// change dst data from RGB to BGR ͷ����ת270��
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
