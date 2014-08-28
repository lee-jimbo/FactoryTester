/**
 * 文件名：RunawayBean.java
 * author: jimbo
 * 版本信息 : magniwill copyright 2014
 * 日期：2014-8-25
 * 
 */
package com.mediatek.factorymode.rfid;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Administrator
 * 
 */
public class RunawayBean implements Parcelable, Serializable {

	private static final long serialVersionUID = 1L;
	private long time;
	private String police_id;
	private String police_department;
	private String cardtype;
	private String name;
	private String number;
	private String people;
	private String address;
	private String cardimage;
	private String headimage;
	private String status;
	private String jiguan;
	private String birthday;
	private String sex;
	private String checi;
	private String chexiang;
	private String zuohao;
	private String camera_path;
	private String record_path;
	private String chezhan;
	private String hechadi;
	private String sendunit;
	private String validity;
	private String imagpath01;
	private String imagpath02;
	private String imagpath03;
	private String imagpath00;
	private String imagpath04;
	private String imagpath05;

	public String getImagpath05() {
		return imagpath05;
	}

	public void setImagpath05(String imagpath05) {
		this.imagpath05 = imagpath05;
	}

	public String getImagpath04() {
		return imagpath04;
	}

	public void setImagpath04(String imagpath04) {
		this.imagpath04 = imagpath04;
	}

	public String getImagpath01() {
		return imagpath01;
	}

	public void setImagpath01(String imagpath01) {
		this.imagpath01 = imagpath01;
	}

	public String getImagpath02() {
		return imagpath02;
	}

	public void setImagpath02(String imagpath02) {
		this.imagpath02 = imagpath02;
	}

	public String getImagpath03() {
		return imagpath03;
	}

	public void setImagpath03(String imagpath03) {
		this.imagpath03 = imagpath03;
	}

	public String getImagpath00() {
		return imagpath00;
	}

	public void setImagpath00(String imagpath00) {
		this.imagpath00 = imagpath00;
	}

	public RunawayBean() {

	}

	private RunawayBean(Parcel source) {
		readFromParcel(source);
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getPolice_id() {
		return police_id;
	}

	public void setPolice_id(String police_id) {
		this.police_id = police_id;
	}

	public String getPolice_department() {
		return police_department;
	}

	public void setPolice_department(String police_department) {
		this.police_department = police_department;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPeople() {
		return people;
	}

	public void setPeople(String people) {
		this.people = people;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCardimage() {
		return cardimage;
	}

	public void setCardimage(String cardimage) {
		this.cardimage = cardimage;
	}

	public String getHeadimage() {
		return headimage;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getJiguan() {
		return jiguan;
	}

	public void setJiguan(String jiguan) {
		this.jiguan = jiguan;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCheci() {
		return checi;
	}

	public void setCheci(String checi) {
		this.checi = checi;
	}

	public String getChexiang() {
		return chexiang;
	}

	public void setChexiang(String chexiang) {
		this.chexiang = chexiang;
	}

	public String getZuohao() {
		return zuohao;
	}

	public void setZuohao(String zuohao) {
		this.zuohao = zuohao;
	}

	public String getCamera_path() {
		return camera_path;
	}

	public void setCamera_path(String camera_path) {
		this.camera_path = camera_path;
	}

	public String getRecord_path() {
		return record_path;
	}

	public void setRecord_path(String record_path) {
		this.record_path = record_path;
	}

	public String getChezhan() {
		return chezhan;
	}

	public void setChezhan(String chezhan) {
		this.chezhan = chezhan;
	}

	public String getHechadi() {
		return hechadi;
	}

	public void setHechadi(String hechadi) {
		this.hechadi = hechadi;
	}

	public String getSendunit() {
		return sendunit;
	}

	public void setSendunit(String sendunit) {
		this.sendunit = sendunit;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	// @Override
	public int describeContents() {
		return 0;
	}

	// @Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(time);
		dest.writeString(police_id);
		dest.writeString(police_department);
		dest.writeString(cardtype);
		dest.writeString(name);
		dest.writeString(number);
		dest.writeString(people);
		dest.writeString(address);
		dest.writeString(cardimage);
		dest.writeString(headimage);
		dest.writeString(status);
		dest.writeString(jiguan);
		dest.writeString(birthday);
		dest.writeString(sex);
		dest.writeString(checi);
		dest.writeString(chexiang);
		dest.writeString(zuohao);
		dest.writeString(camera_path);
		dest.writeString(record_path);
		dest.writeString(chezhan);
		dest.writeString(hechadi);
		dest.writeString(sendunit);
		dest.writeString(validity);
	}

	public void readFromParcel(Parcel source) {
		time = source.readLong();
		police_id = source.readString();
		police_department = source.readString();
		cardtype = source.readString();
		name = source.readString();
		number = source.readString();
		people = source.readString();
		address = source.readString();
		cardimage = source.readString();
		headimage = source.readString();
		status = source.readString();
		jiguan = source.readString();
		birthday = source.readString();
		sex = source.readString();
		checi = source.readString();
		chexiang = source.readString();
		zuohao = source.readString();
		camera_path = source.readString();
		record_path = source.readString();
		chezhan = source.readString();
		hechadi = source.readString();
		sendunit = source.readString();
		validity = source.readString();
	}

	// 必须提供一个名为CREATOR的static final属性 该属性需要实现android.os.Parcelable.Creator<T>接口
	public static final Parcelable.Creator<RunawayBean> CREATOR = new Parcelable.Creator<RunawayBean>() {
		private Parcel source;

		public RunawayBean createFromParcel(Parcel source) {
			this.source = source;
			return new RunawayBean(source);
		}

		public RunawayBean[] newArray(int size) {
			return new RunawayBean[size];
		}
	};

}
