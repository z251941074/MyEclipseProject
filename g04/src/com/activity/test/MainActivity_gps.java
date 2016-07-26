package com.activity.test;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.activity.test.R;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
public class MainActivity_gps extends Activity {
	
	

	/* ================Modle======================= */
	/** 输出日志标识 */
	private String TAG = "MainActivity";
	/** 等待中的动画播放与停止的控制器 */
	private AnimationDrawable animationDrawable;
	/** 定位成功标识 */
	private final int LOCATION_SUCCESS = 1;
	/** 定位失败标识 */
	private final int LOCATION_ERROR = 0;
	/** 百度定位器 */
	private LocationClient mLocClient;
	/** 延迟发送，设置成全局，用于解决快速按按钮导致的开启了多个线程 */
	private Runnable mRunable;
	/* ================View======================= */
	/** 等待中动画图片控件 */
	private ImageView iv_main;
	/** 展示定位信息的控件 */
	private TextView textView_main;
	/** 点击该按钮可开始网络定位，并且打开等待中的动画效果 */
	private Button button_main;
	/* ================Control======================= */
	/** 自定义的定们监听 */
	private BDLocationListener mLocationListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			Log.i(TAG, "===onReceiveLocation(),location-->" + location);
			if (location != null) {
				String city = "";
				String address = "";
				city = location.getCity();
				
				
				
				//共享数据
				SharedPreferences sp11 =getSharedPreferences("userInfo1",MainActivity_gps.MODE_PRIVATE);
 
				address = location.getAddrStr();			
				SharedPreferences.Editor editor = sp11.edit();  
	            editor.putString("address", address);  
	            editor.commit();
				
				
				
				double dLat = location.getLatitude();
				double dLon = location.getLongitude();
				Message msg = handler.obtainMessage();
				if (city == null) {
					msg.what = LOCATION_ERROR;
					msg.sendToTarget();
				} else {
					int end = city.indexOf("市");
					if (end != -1) {
						city = city.substring(0, end);
					}
					end = address.indexOf("市");
					if (end != -1) {
						address = address.substring(end + 1, address.length());
					}

					// 定位成功
					Log.i(TAG, "===dLat-->" + dLat);
					Log.i(TAG, "===dLon-->" + dLon);
					Log.i(TAG, "===city-->" + city);
					Log.i(TAG, "===address-->" + address);
					Map<String, String> map = new HashMap<String, String>();
					map.put("dLat", "" + dLat);
					map.put("dLon", "" + dLon);
					map.put("city", city);
					map.put("address", address);
					msg.what = LOCATION_SUCCESS;
					msg.obj = map;
					msg.sendToTarget();
				}

			} else {
				Message msg = handler.obtainMessage();
				msg.what = LOCATION_ERROR;
				msg.sendToTarget();
			}
			mLocClient.stop();
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			Log.i(TAG, "===onReceivePoi(),arg0-->" + arg0);
		}
	};
	/** 定位操作处理 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Log.i(TAG, "===handleMessage()");
			switch (msg.what) {
			case LOCATION_SUCCESS:
				Log.i(TAG, "===定位成功");// 成功
				/* 展示内容 */
				Map<String, String> map = (HashMap<String, String>) msg.obj;
				textView_main.setText("纬度:" + map.get("dLat") + "\n" + "经度:" + map.get("dLon") + "\n" + "城市:" + map.get("city") + "\n" + "地址:" + map.get("address"));
				animationDrawable.stop();// 停止播放动画
				break;

			case LOCATION_ERROR:
				Log.i(TAG, "===定位失败");// 失败
				textView_main.setText("定位失败，无内容");
				animationDrawable.stop();// 停止播放动画
				break;
			}
		}

	};
	/** 图片监听 */
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Log.i(TAG, "===onClick()");
			// iv_main.startAnimation(animationSet);
			switch (arg0.getId()) {
			case R.id.button_main:
				textView_main.setText("正在努力定位中，请稍后ing...");
				animationDrawable.start();// 开始播放等待动画
				handler.removeCallbacks(mRunable);
				mRunable = new Runnable() {

					@Override
					public void run() {
						mLocClient.start();// 开始定位

					}
				};
				handler.postDelayed(mRunable, 2000);// 为了看动画，延迟执行定位
				break;
			}
		}
	};

	/* ==================Lifecycle========================= */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "===onCreate()");
		initData();
		initView();
	}

	@Override
	public void onDestroy() {
		if (mLocClient != null) {
			mLocClient.stop();// 停止定位
		}
		super.onDestroy();
	}

	/* ================子过程=============== */
	/**
	 * 初始化布局
	 */
	private void initView() {
		Log.i(TAG, "===initView()");
		/* 主界面 */
		setContentView(R.layout.activity_main);
		/* 等待图片控件 */
		iv_main = (ImageView) findViewById(R.id.iv_main);
		iv_main.setImageResource(R.drawable.animation1);
		animationDrawable = (AnimationDrawable) iv_main.getDrawable();// 等待动画
		/* 定位信息内容的对象 */
		textView_main = (TextView) findViewById(R.id.textView_main);
		/* Button开始定位按钮 */
		button_main = (Button) findViewById(R.id.button_main);
		button_main.setOnClickListener(mOnClickListener);
	}

	/**
	 * 数初化数据
	 */
	private void initData() {
		Log.i(TAG, "===initData()");
		/* 初始化定位信息 */
		mLocClient = new LocationClient(this);// 创建定位器
		// mLocClient.setAK("64qAcRkfBfe6Rh6c37tfEAi8");// 设置Key值
		mLocClient.registerLocationListener(mLocationListener);

		LocationClientOption mLocOption = new LocationClientOption();// 位置区域设置
		mLocOption.setOpenGps(true);// 开启手机GPS导航
		mLocOption.setAddrType("all");// 返回的定位结果包含地址信息
		mLocOption.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02.必须用这个才能和地图完美匹配。
		mLocOption.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		mLocOption.disableCache(true);// 禁止启用缓存定位
		mLocOption.setPoiNumber(5); // 最多返回POI个数
		mLocOption.setPoiDistance(1000); // poi查询距离
		mLocOption.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		mLocClient.setLocOption(mLocOption);
	}
	 public void btfonclick(View view)
	    {
		 Handler handler = new Handler();
			handler.postDelayed(new Runnable(){
			public void run(){
				//要执行的代码,跳转到主程序界面
				Intent intent = new Intent(getApplicationContext(),setting.class);
				//开始执行
				startActivity(intent);
				}
			},500);//1000毫秒=1秒
	    	
	    }

}
