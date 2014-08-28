/**
 * 文件名：IdCardInfo.java
 * author: jimbo
 * 版本信息 : magniwill copyright 2014
 * 日期：2014-8-25
 * 
 */
package com.mediatek.factorymode.rfid;

import java.io.Serializable;

import android.graphics.Bitmap;

/**
 * @author Administrator
 *
 */
public class IdCardInfo implements Serializable{
	  private static final long serialVersionUID = 1L;
	  private String mPersonName;
	  private String mPersonIdCardNum;
	  private String mPersonNation;
	  private String mPersonSex;
	  private String mPersonBirthday;
	  private String mPersonAddress;
	  private String mPersonDepartment;
	  private String mPersonStarDate;
	  private String mPersonEndDate;
	  private String mPersonNewAddress;
	  private Bitmap mPersonImage;
	  private String otherInfo;

	  public String getmPersonName()
	  {
	    return this.mPersonName;
	  }

	  public String getmPersonIdCardNum()
	  {
	    return this.mPersonIdCardNum;
	  }

	  public String getmPersonNation()
	  {
	    return this.mPersonNation;
	  }

	  public String getmPersonSex()
	  {
	    return this.mPersonSex;
	  }

	  public String getmPersonBirthday()
	  {
	    return this.mPersonBirthday;
	  }

	  public String getmPersonAddress()
	  {
	    return this.mPersonAddress;
	  }

	  public String getmPersonDepartment()
	  {
	    return this.mPersonDepartment;
	  }

	  public String getmPersonStarDate()
	  {
	    return this.mPersonStarDate;
	  }

	  public String getmPersonEndDate()
	  {
	    return this.mPersonEndDate;
	  }

	  public String getmPersonNewAddress()
	  {
	    return this.mPersonNewAddress;
	  }

	  public Bitmap getmPersonImage()
	  {
	    return this.mPersonImage;
	  }

	  public String getOtherInfo()
	  {
	    return this.otherInfo;
	  }

	  public void setmPersonName(String mPersonName)
	  {
	    this.mPersonName = mPersonName;
	  }

	  public void setmPersonIdCardNum(String mPersonIdCardNum) {
	    this.mPersonIdCardNum = mPersonIdCardNum;
	  }

	  public void setmPersonNation(String mPersonNation) {
	    this.mPersonNation = mPersonNation;
	  }

	  public void setmPersonSex(String mPersonSex) {
	    this.mPersonSex = mPersonSex;
	  }

	  public void setmPersonBirthday(String mPersonBirthday) {
	    this.mPersonBirthday = mPersonBirthday;
	  }

	  public void setmPersonAddress(String mPersonAddress) {
	    this.mPersonAddress = mPersonAddress;
	  }

	  public void setmPersonDepartment(String mPersonDepartment) {
	    this.mPersonDepartment = mPersonDepartment;
	  }

	  public void setmPersonStarDate(String mPersonStarDate) {
	    this.mPersonStarDate = mPersonStarDate;
	  }

	  public void setmPersonEndDate(String mPersonEndDate) {
	    this.mPersonEndDate = mPersonEndDate;
	  }

	  public void setmPersonNewAddress(String mPersonNewAddress) {
	    this.mPersonNewAddress = mPersonNewAddress;
	  }

	  public void setmPersonImage(Bitmap mPersonImage) {
	    this.mPersonImage = mPersonImage;
	  }

	  public void setOtherInfo(String otherInfo) {
	    this.otherInfo = otherInfo;
	  }}
