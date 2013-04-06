package com.smartlab.connection;

public class MobileClientApp {

	

	public String write(String msg) {
		String back = MobileClientService.WriteMsgToMSForCallBack("121.34.116.123",
				7777, msg);

		System.out.println("client rev:" + back);
		return  back;
	}
}
