package com.smartlab.connection;

public class MobileClientApp {

	

	public String write(String msg) {
		String back = MobileClientService.WriteMsgToMSForCallBack("58.60.196.90",
				7777, msg);

		System.out.println("client rev:" + back);
		return  back;
	}
}
