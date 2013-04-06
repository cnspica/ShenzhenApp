package com.example.smartlab.activity;


import com.smartlab.JSONtool.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


public class ChooseArea extends Activity{

	Spinner citychoose;
	Spinner streetchoose;
	Button enter;
	String fullm;
	String citylist;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.area);
		checkNET();
		
		
		//获取城市列表
//		MobileClientApp chcity=new MobileClientApp();
//		citylist=chcity.write(ChooseArea.setBook("action", "getcitylist", fullm));
//		ChooseArea.getValue("citylist", citylist);
		
		
		// spinner城市选择
				ArrayAdapter aacity = ArrayAdapter.createFromResource(this,
						R.array.city, android.R.layout.simple_spinner_item);
				//aacity.setDropDownViewResource(R.layout.spinnerdropdown);
				citychoose = (Spinner) findViewById(R.id.CityChoose);
				citychoose.setAdapter(aacity);
				//citychoose.setPrompt("城市选择");
				
		// spinner街道选择
		ArrayAdapter aastreet = ArrayAdapter.createFromResource(this,
				R.array.street, android.R.layout.simple_spinner_item);
		//aastreet.setDropDownViewResource(R.layout.spinnerdropdown);
		streetchoose = (Spinner) findViewById(R.id.StreetChoose);
		streetchoose.setAdapter(aastreet);
		//streetchoose.setPrompt("搜索");
		
		enter=(Button)findViewById(R.id.Enter);
		enter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent=new Intent(ChooseArea.this,MainActivity.class);
				startActivity(intent);
				
				ChooseArea.this.finish();
				overridePendingTransition(R.anim.in_right_left,R.anim.out_left_right);
			}
		});
	}
	// 判断网络状态
	private void checkNET() {
		boolean flag = false;
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager.getActiveNetworkInfo() != null) {
			flag = manager.getActiveNetworkInfo().isAvailable();
		}
		if (!flag) {
			AlertDialog.Builder builder = new AlertDialog.Builder(ChooseArea.this);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("网络状态");
			builder.setMessage("当前网络不可用，是否设置网络？");
			builder.setPositiveButton("设置",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(
									android.provider.Settings.ACTION_SETTINGS);
							startActivityForResult(intent, 0);
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			builder.create();
			builder.show();
		}

	}
	//解析
	public static String getValue(String key,String fullMsg){
		JSONObject jsonObj=new JSONObject(fullMsg);
		String value=jsonObj.optString(key);
		return value;	
	}
	//获取城市列表
	public static String setBook(String key,String value,String fullMsg){
		String res="";
		JSONObject jsonObj=new JSONObject(fullMsg);
		if(jsonObj.optString("key")!=""){
			jsonObj.remove("key");
			jsonObj.put("key" , "value");
			res=jsonObj.toString();
		}else{
			jsonObj.put("key", "value");
			res=jsonObj.toString();
		}
		return res;
	}
}

