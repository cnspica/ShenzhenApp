package com.example.smartlab.activity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smartlab.JSONtool.JSONArray;
import com.smartlab.JSONtool.JSONObject;
import com.smartlab.connection.MobileClientApp;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MainActivity extends ListActivity {

	Button back;
	String value;
	static ArrayList<ParkAtom> parklist = new ArrayList<ParkAtom>();
	boolean needUpdate = false;
	String msg;

	String parkmsg = "{}";
	String parkinfo = "";
	String parkjson;
	String JSONback;
	int index;
	int num;

	char state;
	String parkdata;
	JSONArray result;
	JSONArray backup;
	static Boolean flag = true;
	private ProgressDialog d;

	private List<Map<String, Object>> mData;
	
	public Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		d = new ProgressDialog(MainActivity.this);
		d.setMessage("信息加载中......");
		d.closeOptionsMenu();
		d.setCancelable(false);// 显示时禁止触摸屏幕
		d.show();

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				d.dismiss();
				try {
					mData = getData();
					MyAdapter adapter = new MyAdapter(MainActivity.this);
					setListAdapter(adapter);

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					// Toast.makeText(getApplicationContext(), "网络超时，未连接",
					// Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		try {

			new Thread() {

				public void run() {
					while(flag){
						try {
							Thread.sleep(5000);
							getParkInfo();
							
							handler.sendEmptyMessage(1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				// 取车位信息
				public void getParkInfo() {
					MobileClientApp parkin = new MobileClientApp();
					System.out.println(setValue("action", "getinfo", parkmsg));
					parkinfo = parkin.write(setValue("action", "getinfo", parkmsg));
					try {
						result = new JSONArray(parkinfo);

						num = result.length();
						for (int i = 0; i < num; i++) {
							JSONObject jj = result.getJSONObject(i);
							ParkAtom pa = new ParkAtom();
							pa.setCardno(jj.optString("cardno"));
							pa.setCityname(jj.optString("cityname"));
							pa.setStatus(jj.optString("status"));
							pa.setStreet(jj.optString("street"));
							pa.setAblock(jj.optString("ablock"));
							pa.setPatomno(jj.optString("magnetno"));
							// System.out.println(pa.getPatomno());
							boolean res = update(pa);
//							if (res == true) {
//								needUpdate = true;
//								handler.sendEmptyMessage(0);
//								needUpdate = false;
//							}
						}
						System.out.println(num);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
//						Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT)
//								.show();
					}

				}
				
				// 更新arraylist
				public boolean update(ParkAtom a) {
					int atomno = 0;
					try {
						atomno = Integer.parseInt(a.getPatomno());
					} catch (Exception e) {
						// TODO: handle exception
						// Toast.makeText(getApplicationContext(),"转换异常",
						// Toast.LENGTH_SHORT).show();
						return false;
					}
					if (atomno == 0) {
						return false;
					}
					if (atomno > parklist.size()) {
						parklist.add(a);
					} else {
						ParkAtom temp = parklist.get(atomno - 1);
						if (!a.equals(temp)) {
							a.setIsupdate(false);
							parklist.set(atomno - 1, a);
						}
					}

					return true;

				}

			}.start();

		} catch (Exception e) {
			// TODO: handle exception
		}

		back = (Button) findViewById(R.id.btnback);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, ChooseArea.class);
				startActivity(intent);
				MainActivity.this.finish();
				overridePendingTransition(R.anim.out_right_left,
						R.anim.in_left_right);
			}
		});
	}
	


	// 封装
	public static String setValue(String key, String value, String fullMsg) {
		String res = "";
		JSONObject jsonObj = new JSONObject(fullMsg);
		if (jsonObj.optString(key) != "") {
			jsonObj.remove(key);
			jsonObj.put(key, value);
			res = jsonObj.toString();
		} else {
			jsonObj.put(key, value);
			res = jsonObj.toString();
		}
		return res;
	}

	// 解析
	public static String getValue(String key, String fullMsg) {
		JSONObject jsonObj = new JSONObject(fullMsg);
		String value = jsonObj.optString(key);
		return value;
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = null;
		for (int i = 1; i < parklist.size() + 1; i++) {

			map = new HashMap<String, Object>();
			ParkAtom tpa = parklist.get(i - 1);
			try {
				if (tpa.getStatus().equals("01")) {

					map.put("parkNO", tpa.getPatomno());
					map.put("chewei", R.drawable.pcar2);
					map.put("parkim", R.drawable.car);
					map.put("static", tpa.getStatus());
					map.put("userid", tpa.getCardno());
				} else {
					map.put("parkNO", tpa.getPatomno());
					map.put("chewei", R.drawable.pcar1);
					map.put("static", tpa.getStatus());
					map.put("parkim", R.drawable.white);
					// map.put("userid", "B123");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			System.out.println(list);
			list.add(i - 1, map);

		}

		return list;
	}

	// ListView 中某项被选中后的逻辑
	// @Override
	// protected void onListItemClick(ListView l, View v, int position, long id)
	// {
	//
	// Log.v("", (String)mData.get(position).get("parkna"));
	// }
	/**
	 * listview中点击按键弹出对话框
	 */

	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context handler) {
			this.mInflater = LayoutInflater.from(handler);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			System.out.println(mData.size());
			return mData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		public final class ViewHolder {
			public TextView parkNO;
			public ImageView chewei;
			public ImageView parkim;
			public TextView userid;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				index = position;
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.listview, null);
				holder.parkNO = (TextView) convertView
						.findViewById(R.id.parkID);
				holder.chewei = (ImageView) convertView
						.findViewById(R.id.reside);

				// holder.chewei.setText
				holder.parkim = (ImageView) convertView
						.findViewById(R.id.parkIM);

				holder.userid = (TextView) convertView
						.findViewById(R.id.userID);

				convertView.setTag(holder);

				// System.out.println(position);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}
			try {
				holder.parkNO.setText((String) mData.get(position)
						.get("parkNO"));
				// System.out.println(num);
				holder.chewei.setBackgroundResource((Integer) mData.get(
						position).get("chewei"));
				holder.parkim.setBackgroundResource((Integer) mData.get(
						position).get("parkim"));
				holder.userid.setText((String) mData.get(position)
						.get("userid"));

			} catch (Exception e) {
				// TODO: handle exception

			}

			return convertView;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	// 退出
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.showTips();
			return false;
		}
		return false;
	}

	// 退出
	private void showTips() {
		AlertDialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("退出程序").setMessage("是否退出小助手？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						flag = false;
						System.exit(0);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).create(); // 创建对话框
		alertDialog.show(); // 显示对话框
	}

	
	

}
