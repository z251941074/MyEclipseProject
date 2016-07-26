/****************************************************************
date :   2012.11.12
author:  cxt
function:
     ���ļ���Ҫ�Ǵ�С����ȡgps��λ����,Ȼ�����ڰٶȵ�ͼ�и���
****************************************************************/

package com.czy;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.android.map.MyMapOverlay;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MKOLUpdateElement;
import com.baidu.mapapi.MKOfflineMap;
import com.baidu.mapapi.MKOfflineMapListener;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.Overlay;

public class LocationActivity extends MapActivity {//  �̳�MapActivity, ��ʾ�ٶȵ�ͼ��Ҫ��
	private MapView mapView;
	private MapController mMapCtrl;
	private List<Overlay> mapOverlays;
	public GeoPoint locPoint;
	private MyMapOverlay mOverlay;
	private TextView desText;
	private String lost_tips;
	
    private Thread mThreadClient = null;
    private Socket mSocketClient = null;
	static PrintWriter mPrintWriterClient = null;
	static BufferedReader mBufferedReaderClient	= null;
	static DataOutputStream out = null;
	private String conurl = "192.168.1.222";
    private boolean isConnecting = false;
    private char[] buffer = new char[64] ;
	
	private int point_X;
	private int point_Y;

	public final int MSG_VIEW_LONGPRESS   = 10001;
	public final int MSG_VIEW_ADDRESSNAME = 10002;
	public final int MSG_GONE_ADDRESSNAME = 10003;
	private Intent mIntent;
//	private int mLatitude;
//	private int mLongitude;
	private String name;
	private BMapManager mapManager;
	private MKLocationManager mLocationManager = null;
	private boolean isLoadAdrr = true;
	private MKSearch mMKSearch;
	private boolean hasRecvData = false;
	private MKOfflineMap mOffline = null; //��������
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.baidumap);
		initMap();
//		mIntent = getIntent();
//
//		name = mIntent.getStringExtra("name");
		mapView = (MapView) findViewById(R.id.map_view);
		desText = (TextView) this.findViewById(R.id.map_bubbleText);
		lost_tips = getResources().getString(R.string.load_tips);

		// ����:113.2759952545166 γ��:23.117055306224895 
		//23.162063,113.433602
		//AA113.44090_23.167100AA
		locPoint = new GeoPoint((int) (23.167100 * 1E6), (int) (113.44090 * 1E6));//new GeoPoint((int) (1000000 *24),(int)(1000000 *113));
//		desText.setText(name);
		mapView.setBuiltInZoomControls(true); //�����������õ����ſؼ�

////		mapView.setClickable(true);
		mMapCtrl = mapView.getController(); // �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����

		mMapCtrl.setCenter(locPoint); 
		
		point_X = this.getWindowManager().getDefaultDisplay().getWidth() / 2;
		point_Y = this.getWindowManager().getDefaultDisplay().getHeight() / 2;
		mOverlay = new MyMapOverlay(point_X, point_Y) {
			@Override
			public void changePoint(GeoPoint newPoint, int type) {
				if (type == 1) {
					mHandler.sendEmptyMessage(MSG_GONE_ADDRESSNAME);
				} else {
					mHandler.sendEmptyMessage(MSG_VIEW_LONGPRESS);
				}

			}
		};
//		mapOverlays = mapView.getOverlays();
//		if (mapOverlays.size() > 0) {
//			mapOverlays.clear();
//		}
//		mapOverlays.add(mOverlay);
		mMapCtrl.setZoom(20);
		mHandler.sendEmptyMessage(MSG_VIEW_LONGPRESS);
		
		/*tcp network*/
		Bundle bundle = this.getIntent().getExtras();//ȡ��Bundle�����е�����
		conurl = bundle.getString("conurl")+":4000";
		System.out.println(conurl);
		for(int i = 0 ; buffer.length > i ; i++){
			buffer[i] = '0';
		}
		mThreadClient = new Thread(mRunnable); //  ָ���·�������mRunnable , �����߳�client,����С��
		mThreadClient.start();
		
		mOffline = new MKOfflineMap();
		mOffline.init(mapManager, new MKOfflineMapListener() {
		    @Override
		    public void onGetOfflineMapState(int type, int state) {
		        switch (type) {
				case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
					{
						MKOLUpdateElement update = mOffline.getUpdateInfo(state);
						//mText.setText(String.format("%s : %d%%", update.cityName, update.ratio));
					}
					break;
				case MKOfflineMap.TYPE_NEW_OFFLINE:
					Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
					break;
				case MKOfflineMap.TYPE_VER_UPDATE:
					Log.d("OfflineDemo", String.format("new offlinemap ver"));
					break;
				}    
		          }
		}
		);
		int num = mOffline.scan();
		System.out.println("num:"+num);
	}
	
	 private Runnable	mRunnable	= new Runnable() 
		{
			public void run()
			{
				int timeout = 2000;
				System.out.println("mRunnable.......");
				int start = conurl.indexOf(":");
				isConnecting = true;

				String sIP = conurl.substring(0, start);
				String sPort = conurl.substring(start+1);
				int port = Integer.parseInt(sPort);				
//				System.out.println("Integer.......");
				Log.d("gjz", "IP:"+ sIP + ":" + port);			
				
				try 
				{	
							
					//���ӷ�����  mSocketClient = new Socket(sIP, port);	//portnum	
					mSocketClient = new Socket();	//portnum				
					SocketAddress isa = new InetSocketAddress(sIP, port);     
					mSocketClient.connect(isa, timeout);
					//ȡ�����롢�����
					mBufferedReaderClient = new BufferedReader(new InputStreamReader(mSocketClient.getInputStream()));
					isConnecting = true;
					mPrintWriterClient = new PrintWriter(mSocketClient.getOutputStream(), true);
					out = new DataOutputStream(mSocketClient.getOutputStream()); 
					out.writeInt(251658240);

//					Message msg = new Message();
//	                msg.what = 1;
//					mHandler.sendMessage(msg);		
					//break;
				}
				catch (Exception e) 
				{
					Message msg = new Message();
	                msg.arg1 = -1;
					mHandler.sendMessage(msg);
					System.out.println("mRunnable....connect failed ...");
					return;
				}			

				
				while (isConnecting)
				{
					try
					{
						//if ( (recvMessageClient = mBufferedReaderClient.readLine()) != null )
						if((mBufferedReaderClient.read(buffer))>0)
						{						
							Message msg = new Message();
			                msg.what = 10;
							mHandler.sendMessage(msg);
							String st = new String(buffer);
							if(st.startsWith("AA")&&(hasRecvData==false)){
								hasRecvData = true;
//								mapOverlays = mapView.getOverlays();
//								if (mapOverlays.size() > 0) {
//									mapOverlays.clear();
//								}
//								mapOverlays.add(mOverlay);
//								mMapCtrl.setZoom(20);
//								mHandler.sendEmptyMessage(MSG_VIEW_LONGPRESS);
							}
							System.out.println("read:"+st);
						}
					}
					catch (Exception e)
					{
						Message msg = new Message();
		                msg.what = 0;
						mHandler.sendMessage(msg);
						System.out.println("mBufferedReaderClient....connect failed ...");
					}
				}
			}
		};
	    
//
	private void initMap() {

		// ��ʼ��MapActivity
		mapManager = new BMapManager(getApplication());
		// init�����ĵ�һ�����������������API Key
		mapManager.init("C66C0501D0280744759A6957C42543AE38F5D540", null);
		super.initMapActivity(mapManager);
		// ʵ����������ַ��
		mMKSearch = new MKSearch();
		// ��ʼ��������ַʵ��
		mMKSearch.init(mapManager, new MySearchListener());
	}
//
	@Override
	protected void onResume() {
		if (mapManager != null) {
			mapManager.start();
		}
		super.onResume();

	}

	@Override
	protected void onPause() {
		isLoadAdrr = false;
		if (mapManager != null) {
			mapManager.stop();
		}
		super.onPause();
	}


//	/**
//	 * ͨ����γ�Ȼ�ȡ��ַ
//	 * 
//	 * @param point
//	 * @return
//	 */
//	private String getLocationAddress(GeoPoint point) {
//		String add = "";
//		Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
//		try {
//			// ����:113.2759952545166 γ��:23.117055306224895 
//			List<Address> addresses = geoCoder.getFromLocation(
//					 (int) (23.117055 * 1E6), (int) (113.275995 * 1E6),
//					1);
//			Address address = addresses.get(0);
//			int maxLine = address.getMaxAddressLineIndex();
//			if (maxLine >= 2) {
//				add = address.getAddressLine(1) + address.getAddressLine(2);
//			} else {
//				add = address.getAddressLine(1);
//			}
//		} catch (IOException e) {
//			add = "";
//			e.printStackTrace();
//		}
//		return add;
//	}


	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(hasRecvData == false){
				desText.setVisibility(View.VISIBLE);
				desText.setText(lost_tips);
				return ;
			}
			switch (msg.what) {
			case MSG_VIEW_LONGPRESS:// ������ʱ�䷵��λ����Ϣ
			{
				if (null == locPoint)
					return;
				
				mMKSearch.reverseGeocode(locPoint);
				desText.setVisibility(View.VISIBLE);
				desText.setText(lost_tips);
				mMapCtrl.animateTo(locPoint);
				mapView.invalidate();
			}
				break;
			case MSG_VIEW_ADDRESSNAME:
				desText.setText((String) msg.obj);
				desText.setVisibility(View.VISIBLE);
				break;
			case MSG_GONE_ADDRESSNAME:
				desText.setVisibility(View.GONE);
				break;
			case 10:
				String readbuf = new String(buffer);
				for(int i = 0 ; buffer.length > i ; i++){
					buffer[i] = '0';
				}
				String longitude =  readbuf.substring(2,readbuf.indexOf("_") ) ;//����
				String latitude  =  readbuf.substring(readbuf.indexOf("_")+1 ,readbuf.lastIndexOf("AA")) ;//γ��
				System.out.println("longitude:"+longitude+"==latitude:"+latitude);
				if((longitude == null) || (latitude==null)){
					return;
				}
				locPoint = new GeoPoint((int) (Double.parseDouble(latitude) * 1E6), (int) (Double.parseDouble(longitude)  * 1E6));//new GeoPoint((int) (1000000 *24),(int)(1000000 *113));
				//mMKSearch.reverseGeocode(locPoint);
				//desText.setVisibility(View.VISIBLE);
				//desText.setText(lost_tips);
				//mMapCtrl.setCenter(locPoint);
				mHandler.sendEmptyMessage(MSG_VIEW_LONGPRESS);
				//mapView.invalidate();
				break;
			case -1:
				new AlertDialog.Builder(com.czy.LocationActivity.this).setTitle("��������").setMessage("����������ʧ��")  
	            .setPositiveButton("ok", new DialogInterface.OnClickListener() { 
	                @Override  
	                public void onClick(DialogInterface dialog, int which) {  
	                    // TODO Auto-generated method stub   
	                	finish();
	                }  
	            }).show(); 
				break;
			}
			
		}
	};

	// �رճ���Ҳ�رն�λ
	@Override
	protected void onDestroy() {		
		try {
			if((mSocketClient!=null)&&(mSocketClient.isConnected())){
				out.writeInt(268435456);// 16
				out.writeInt(167772160);// 10
				mSocketClient.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (mapManager != null) {
			mapManager.destroy();
			mapManager = null;
		}
		super.onDestroy();
	}

	/**
	 * �ڲ���ʵ��MKSearchListener�ӿ�,����ʵ���첽��������
	 * 
	 * @author liufeng
	 */
	public class MySearchListener implements MKSearchListener {
		/**
		 * ���ݾ�γ��������ַ��Ϣ���
		 * 
		 * @param result
		 *            �������
		 * @param iError
		 *            ����ţ�0��ʾ��ȷ���أ�
		 */
		public void onGetAddrResult(MKAddrInfo result, int iError) {
			if (result == null) {
				return;
			}
			Message msg = new Message();
			msg.what = MSG_VIEW_ADDRESSNAME;
			msg.obj = result.strAddr;
			mHandler.sendMessage(msg);

		}
		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	

}