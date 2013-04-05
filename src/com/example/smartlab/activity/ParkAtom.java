package com.example.smartlab.activity;

public class ParkAtom {

	private String status;
	private String street;
	private String cardno;
	private String cityname;
	private String ablock;//街道区域
	private String patomno;//车位号码
	private Boolean isupdate=true;//确认是否是最新的数据
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	public String getAblock() {
		return ablock;
	}
	public void setAblock(String ablock) {
		this.ablock = ablock;
	}
	public String getPatomno() {
		return patomno;
	}
	public void setPatomno(String patomno) {
		this.patomno = patomno;
	}
	public Boolean getIsupdate() {
		return isupdate;
	}
	public void setIsupdate(Boolean isupdate) {
		this.isupdate = isupdate;
	}
	
	
}
