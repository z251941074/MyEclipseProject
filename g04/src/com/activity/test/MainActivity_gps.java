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
	/** �����־��ʶ */
	private String TAG = "MainActivity";
	/** �ȴ��еĶ���������ֹͣ�Ŀ����� */
	private AnimationDrawable animationDrawable;
	/** ��λ�ɹ���ʶ */
	private final int LOCATION_SUCCESS = 1;
	/** ��λʧ�ܱ�ʶ */
	private final int LOCATION_ERROR = 0;
	/** �ٶȶ�λ�� */
	private LocationClient mLocClient;
	/** �ӳٷ��ͣ����ó�ȫ�֣����ڽ�����ٰ���ť���µĿ����˶���߳� */
	private Runnable mRunable;
	/* ================View======================= */
	/** �ȴ��ж���ͼƬ�ؼ� */
	private ImageView iv_main;
	/** չʾ��λ��Ϣ�Ŀؼ� */
	private TextView textView_main;
	/** ����ð�ť�ɿ�ʼ���綨λ�����Ҵ򿪵ȴ��еĶ���Ч�� */
	private Button button_main;
	/* ================Control======================= */
	/** �Զ���Ķ��Ǽ��� */
	private BDLocationListener mLocationListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			Log.i(TAG, "===onReceiveLocation(),location-->" + location);
			if (location != null) {
				String city = "";
				String address = "";
				city = location.getCity();
				
				
				
				//��������
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
					int end = city.indexOf("��");
					if (end != -1) {
						city = city.substring(0, end);
					}
					end = address.indexOf("��");
					if (end != -1) {
						address = address.substring(end + 1, address.length());
					}

					// ��λ�ɹ�
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
	/** ��λ�������� */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Log.i(TAG, "===handleMessage()");
			switch (msg.what) {
			case LOCATION_SUCCESS:
				Log.i(TAG, "===��λ�ɹ�");// �ɹ�
				/* չʾ���� */
				Map<String, String> map = (HashMap<String, String>) msg.obj;
				textView_main.setText("γ��:" + map.get("dLat") + "\n" + "����:" + map.get("dLon") + "\n" + "����:" + map.get("city") + "\n" + "��ַ:" + map.get("address"));
				animationDrawable.stop();// ֹͣ���Ŷ���
				break;

			case LOCATION_ERROR:
				Log.i(TAG, "===��λʧ��");// ʧ��
				textView_main.setText("��λʧ�ܣ�������");
				animationDrawable.stop();// ֹͣ���Ŷ���
				break;
			}
		}

	};
	/** ͼƬ���� */
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Log.i(TAG, "===onClick()");
			// iv_main.startAnimation(animationSet);
			switch (arg0.getId()) {
			case R.id.button_main:
				textView_main.setText("����Ŭ����λ�У����Ժ�ing...");
				animationDrawable.start();// ��ʼ���ŵȴ�����
				handler.removeCallbacks(mRunable);
				mRunable = new Runnable() {

					@Override
					public void run() {
						mLocClient.start();// ��ʼ��λ

					}
				};
				handler.postDelayed(mRunable, 2000);// Ϊ�˿��������ӳ�ִ�ж�λ
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
			mLocClient.stop();// ֹͣ��λ
		}
		super.onDestroy();
	}

	/* ================�ӹ���=============== */
	/**
	 * ��ʼ������
	 */
	private void initView() {
		Log.i(TAG, "===initView()");
		/* ������ */
		setContentView(R.layout.activity_main);
		/* �ȴ�ͼƬ�ؼ� */
		iv_main = (ImageView) findViewById(R.id.iv_main);
		iv_main.setImageResource(R.drawable.animation1);
		animationDrawable = (AnimationDrawable) iv_main.getDrawable();// �ȴ�����
		/* ��λ��Ϣ���ݵĶ��� */
		textView_main = (TextView) findViewById(R.id.textView_main);
		/* Button��ʼ��λ��ť */
		button_main = (Button) findViewById(R.id.button_main);
		button_main.setOnClickListener(mOnClickListener);
	}

	/**
	 * ����������
	 */
	private void initData() {
		Log.i(TAG, "===initData()");
		/* ��ʼ����λ��Ϣ */
		mLocClient = new LocationClient(this);// ������λ��
		// mLocClient.setAK("64qAcRkfBfe6Rh6c37tfEAi8");// ����Keyֵ
		mLocClient.registerLocationListener(mLocationListener);

		LocationClientOption mLocOption = new LocationClientOption();// λ����������
		mLocOption.setOpenGps(true);// �����ֻ�GPS����
		mLocOption.setAddrType("all");// ���صĶ�λ���������ַ��Ϣ
		mLocOption.setCoorType("bd09ll");// ���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02.������������ܺ͵�ͼ����ƥ�䡣
		mLocOption.setScanSpan(5000);// ���÷���λ����ļ��ʱ��Ϊ5000ms
		mLocOption.disableCache(true);// ��ֹ���û��涨λ
		mLocOption.setPoiNumber(5); // ��෵��POI����
		mLocOption.setPoiDistance(1000); // poi��ѯ����
		mLocOption.setPoiExtraInfo(true); // �Ƿ���ҪPOI�ĵ绰�͵�ַ����ϸ��Ϣ
		mLocClient.setLocOption(mLocOption);
	}
	 public void btfonclick(View view)
	    {
		 Handler handler = new Handler();
			handler.postDelayed(new Runnable(){
			public void run(){
				//Ҫִ�еĴ���,��ת�����������
				Intent intent = new Intent(getApplicationContext(),setting.class);
				//��ʼִ��
				startActivity(intent);
				}
			},500);//1000����=1��
	    	
	    }

}
