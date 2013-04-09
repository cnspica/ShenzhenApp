package com.smartlab.connection;

public class MobileClientApp {

	

	public String write(String msg) {
		String back = MobileClientService.WriteMsgToMSForCallBack("42.121.130.21",
				7777, msg);

		System.out.println("client rev:" + back);
		return  back;
	}
}
