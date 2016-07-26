package com.czy;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;


import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class client extends Activity implements SensorEventListener,OnTouchListener,
OnGestureListener{
	/***********   摄像头   发送码***********************************
	#define Camera_Up       5
	#define Camera_Down     6
	#define Camera_Left     7
	#define Camera_Right    8
	#define Camera_Stop     9
	 *****************************************************************/
	private final static int CAM_UP    = 0x5<<24;
	private final static int CAM_DOWN  = 0x6<<24;
	private final static int CAM_LEFT  = 0x7<<24;
	private final static int CAM_RIGHT = 0x8<<24;
	private final static int CAM_STOP  = 0x9<<24;
	
	/*************控制小车行走   编码**************************************
	#define CAR_STOP        0
	#define CAR_GO_FORWARD      1
	#define CAR_GO_BACK         2
	#define CAR_TURN_LEFT       3
	#define CAR_TURN_RIGHT      4
	#define CONNECT_CLOSE       10   //断开连接
	 ********************************************************************/
	private final static int CAR_STOP       = 0x0<<24;
	private final static int CAR_GO_FORWARD = 0x1<<24;
	private final static int CAR_GO_BACK    = 0x2<<24;
	private final static int CAR_TURN_LEFT  = 0x3<<24;
	private final static int CAR_TURN_RIGHT = 0x4<<24;	
	private final static int CONNECT_CLOSE  = 0xa<<24;
	  
	/*************重力感应      控制小车行走   编码**************************************
		
	 ********************************************************************/
	private final static int GRAVITY_GO_FORWARD = 0x1;
	private final static int GRAVITY_GO_BACK    = 0x2;
	private final static int GRAVITY_TURN_LEFT  = 4;
	private final static int GRAVITY_TURN_RIGHT = 8;
	private final static int GRAVITY_STOP       = 10;
	
	
	/*************控制灯**************************************
		#define AUTO_Drive      13   //自动模式
		#define CTL_Drive       14  //遥控模式
	 *********************************************************************/
	private final static int AUTO_DRIVE  = 0xd<<24;//184549376;
	private final static int CAR_CONTROL   = 0xe<<24;//201326592;
	
	private final static String IMAGE_PATH = "/sdcard/gec_capture/";
	
	private Button bntDriveMode = null;
	private Button bntForward = null;
	private Button bntBack = null;
	private Button bntLeft = null;
	private Button bntRight = null;
	private CheckBox checkBoxGlavity =null;
	private Button bntCapture = null;
	
	private ImageView imView = null;
	private Handler messageHandler =null;
	private Boolean isCameraStop = true;//判别视频开关状态
	private Boolean isCarCtrl = true;//判别小车驾驶模式状态
	private String conStr = "http://192.168.2.1";
	// "http://192.168.1.234:8080/?action=stream";
	private String conurl = "192.168.2.1";
//	private HttpRequest http = null;
	private String cmdPid = "";
	
    private boolean isConnecting = true;
    private boolean isNetWorkError = false;
    private Thread mThreadClient = null;
	private Socket mSocketClient = null;
	private Socket mSocketHttp   = null;
	static PrintWriter mPrintWriterClient = null;
	static BufferedReader mBufferedReaderClient	= null;
	static DataOutputStream out = null;
    public static String CameraIp;
    
    private float x, y, z;
    private SensorManager sensorManager;
    private boolean isSensorOpen=false;//判别重力感应开关
    private int iSensorChange = GRAVITY_STOP;
    
    private boolean b_chageCam = false;  // 摄像头调整状态
    private boolean b_ispressed = false; // 判断调整摄像头,上位机屏幕是否按下,按下就继续调整,松开就停止 
    private boolean b_isCapture = false;  
    private char[]  recv_buffer = new char[256];
    GestureDetector mGestureDetector = null; // 手势
    
    /******************设置按钮状态***********************/
	public void setButtonsVisible(int i_visible)
	{
		bntForward.setVisibility(i_visible);
        bntBack.setVisibility(i_visible);
        bntLeft.setVisibility(i_visible);
        bntRight.setVisibility(i_visible);      
        
	}
	
        /** Called when the activity is first created. */
    //@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题 
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
        		WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //防止手机休眠

        
        setContentView(R.layout.main);
        
        imView = (ImageView)findViewById(R.id.imageView1);
		bntForward  = (Button) findViewById(R.id.forward);
		bntBack     = (Button) findViewById(R.id.back);
		bntLeft     = (Button) findViewById(R.id.left);
		bntRight    = (Button) findViewById(R.id.right);		
		bntDriveMode  = (Button) findViewById(R.id.drive_mode);
		bntCapture = (Button) findViewById(R.id.capture);
		checkBoxGlavity = (CheckBox) findViewById(R.id.checkBox1);

		/***  界面滑动初始化  ***/
        mGestureDetector = new GestureDetector(this);
        FrameLayout ll=(FrameLayout)findViewById(R.id.mainLayout);
        ll.setOnTouchListener(this);
        ll.setLongClickable(true); 	
       
		/***  从之前的activity中获取出递过来的数据  ***/  
		Bundle bundle = this.getIntent().getExtras();//取得Bundle对象中的数据
		conStr = "http://" + bundle.getString("conStr")+":8080/?action=stream";
		conurl = bundle.getString("conurl")+":4000";
		
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);//  重力感应
		
		/***  重力感应按钮  ***/
		checkBoxGlavity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            //该方法的第一个参数是CompoundButton对象，第二个参数是isChecked是否选中
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked){//勾选重力感应
                	if((mSocketClient==null) || !(mSocketClient.isConnected())){
                		new AlertDialog.Builder(com.czy.client.this).setTitle("notification").setMessage("服务器连接失败")  
	                    .setPositiveButton("ok", new DialogInterface.OnClickListener() { 
	                        @Override  
	                        public void onClick(DialogInterface dialog, int which) {  
	                            // TODO Auto-generated method stub   	      
	                        }  
	                    }).show(); 
                		checkBoxGlavity.setChecked(false);
                		return;                		
                	}else{
                		isSensorOpen=true;
                		setButtonsVisible(View.INVISIBLE);
	                 
                	} 
                }else{ //没有勾选重力感应
                    isSensorOpen=false;
                    setButtonsVisible(View.VISIBLE);                    
    				iSensorChange = GRAVITY_STOP;	
    				//System.out.println("GRAVITY_STOP.......");
                }
            }
        });
		
		/******************  向前按钮          ******************
		 ******* 控制方向的按钮统一采用了:******************
		 ******* 按下发送方向命令, 释放发送停止命令*********/
		bntForward.setOnTouchListener(new View.OnTouchListener() 
        {
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch(action)
				{
				case MotionEvent.ACTION_DOWN:
					SendCmd(CAR_GO_FORWARD);			    
				break;
				case MotionEvent.ACTION_UP:
					SendCmd(CAR_STOP);					    			    
				}
				return false;// return false表示系统会继续处理
			}
        }
        );
		/***  向后按钮  ***/
		bntBack.setOnTouchListener(new View.OnTouchListener() 
        {
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch(action)
				{
				case MotionEvent.ACTION_DOWN:
					SendCmd(CAR_GO_BACK);				    				    
				break;
				case MotionEvent.ACTION_UP:
					SendCmd(CAR_STOP);					    				    
				}
				return false;
			}
        	
        }
        );
		/***  向左按钮  ***/
		bntLeft.setOnTouchListener(new View.OnTouchListener() 
        {
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch(action)
				{
				case MotionEvent.ACTION_DOWN:
					SendCmd(CAR_TURN_LEFT);			    			    
				break;
				case MotionEvent.ACTION_UP:
					SendCmd(CAR_STOP);		    			    
				}
				return false;
			}
        }
        );
		/***  向右按钮  ***/
		bntRight.setOnTouchListener(new View.OnTouchListener() 
        {
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch(action)
				{
				case MotionEvent.ACTION_DOWN:
					SendCmd(CAR_TURN_RIGHT);						
				break;
				case MotionEvent.ACTION_UP: 					
					SendCmd(CAR_STOP);			    	
				}
				return false;
			}
        }
		);
		/**********  行驶模式按钮 :  手动   自动      *******/				
		bntDriveMode.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(isCarCtrl){// 主要是对标志位进行判定
					isCarCtrl = false;
					isSensorOpen = false;
					SendCmd(AUTO_DRIVE);
					setButtonsVisible(View.INVISIBLE);                     
                    checkBoxGlavity.setVisibility(View.INVISIBLE); 
				}
				else{
					isCarCtrl=true;
					SendCmd(CAR_CONTROL);
					setButtonsVisible(View.VISIBLE);
                    checkBoxGlavity.setVisibility(View.VISIBLE);
                    checkBoxGlavity.setChecked(false);
				}
			}
		});    
		/***  截屏按钮  ***/
		bntCapture.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				b_isCapture = true;
				System.out.println("bntCapture.......");
			}
		});    
		/**   创建视频保存为图片的路径   **/
		File parent = new File(IMAGE_PATH); 
        if(parent!=null&&!parent.exists()){ 
        	parent.mkdirs(); 
        } 
		
		/***  消息队列  ***/
        Looper looper = Looper.myLooper();
        messageHandler = new MessageHandler(looper);  
        
        
        /***** 指定下方创建的mRunnable , 开启线程client,控制小车  , 
         *     具体参见 private Runnable	mRunnable	= new Runnable()***/
		mThreadClient = new Thread(mRunnable); 
		mThreadClient.start();
		
        connectToHttpServer();  // 连接http服务器                
    }
	
	/***************************************************
	 *	   连接http服务器
	 *******************************************************/
	public void connectToHttpServer(){				
		/*****  开启线程http ,处理视频流  ***/
		new Thread() {
            @SuppressWarnings("unchecked")
			public void run() {
            	try {
            		System.out.println("run.......");
					URL url =new URL(conStr);
					System.out.println(url.getHost());
			//		Socket server = new Socket(url.getHost(), url.getPort());
					int timeout = 2000;
					Socket serverHttp = new Socket();	//portnum				
					SocketAddress isa = new InetSocketAddress(url.getHost(), url.getPort());   
					serverHttp.connect(isa, timeout);
					mSocketHttp = serverHttp;
					OutputStream os = serverHttp.getOutputStream();
					InputStream  is = serverHttp.getInputStream();
					System.out.println("InputStream.......");
					StringBuffer request = new StringBuffer();
					//设置   发送HTTP协议访问的头内容
					request.append("GET " + url.getFile() + " HTTP/1.0\r\n");
					request.append("Host: " + url.getHost() + "\r\n");
					request.append("\r\n");
					Log.i("request","request");
					os.write(request.toString().getBytes(), 0, request.length());
					
					//获取Socket返回的数据流信息, 从中截取出图片, 并调用messageHandler进行处理
					StreamSplit localStreamSplit = new StreamSplit(new DataInputStream(new BufferedInputStream(is)));
					Hashtable localHashtable = localStreamSplit.readHeaders();
					
					String str3 = (String)localHashtable.get("content-type");
					int n = str3.indexOf("boundary=");
					Object localObject2 = "--";
					if (n != -1){
						localObject2 = str3.substring(n + 9);
						str3 = str3.substring(0, n);
					if (!((String)localObject2).startsWith("--"))
							localObject2 = "--" + (String)localObject2;
					}
					if (str3.startsWith("multipart/x-mixed-replace")){
						localStreamSplit.skipToBoundary((String)localObject2);
					}
					Log.i("request","request");
					Message message1 = Message.obtain();
					message1.arg1 = 1;
				    messageHandler.sendMessage(message1);
					do{
						if (localObject2 != null){
							localHashtable = localStreamSplit.readHeaders();
							if (localStreamSplit.isAtStreamEnd())
								break;
							str3 = (String)localHashtable.get("content-type");
							if (str3 == null)
								throw new Exception("No part content type");
						}
						if (str3.startsWith("multipart/x-mixed-replace")){//起始头部
							n = str3.indexOf("boundary=");
							localObject2 = str3.substring(n + 9);
							localStreamSplit.skipToBoundary((String)localObject2);
						}else{// 非头部处理
							byte[] localObject3 = localStreamSplit.readToBoundary((String)localObject2);
							if (localObject3.length == 0)
								break;
							
							Message message = Message.obtain();
							message.arg1 = 0;
						    message.obj = BitmapFactory.decodeByteArray(localObject3, 0, localObject3.length);
						    /***********保存视频为图片*************/
						    if(b_isCapture){
						    	String strFileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + ".jpg";
						    	if(saveBitmap2file((Bitmap)(message.obj),strFileName)){
						    		message.arg2 = 1;
						    	}else{
						    		message.arg2 = -1;
						    	}
						    	b_isCapture = false;	
						    }
						    messageHandler.sendMessage(message);
						}
					    try{
					        Thread.sleep(10L);
					    }catch (InterruptedException localInterruptedException){
					    	
					    }
					}while (isConnecting);
//					serverHttp.close();
//					mThreadClient.stop();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(" wrong ");
					if(isConnecting){// 判断当前socket是否已经退出关闭,不判断很容易会导致程序退出时崩溃
						isNetWorkError = true; 
						Message message = Message.obtain();
						message.arg1 = -1;						
						mHandler.sendMessage(message);	
					}
								    
				}
            }
		}.start();
	}
	
	/***************************************************
	 *	   保存到文件, 路径为IMAGE_PATH /sdcard/gec_capture/
	 *   返回值 true false
	 *******************************************************/
	private boolean  saveBitmap2file(Bitmap bmp,String filename){  
	   CompressFormat format= Bitmap.CompressFormat.JPEG;  
	   int quality = 100;  
	   OutputStream stream = null;  
	   try {  
	       stream = new FileOutputStream(IMAGE_PATH + filename);  
	   } catch (FileNotFoundException e) {  
	           // TODO Auto-generated catch block  
		   e.printStackTrace();  
		   System.out.println(" /sdcard/gec_capture no exist.... "); 
		   return false;
	   }   
	   return bmp.compress(format, quality, stream);  
   }  
	
	protected void onResume(){
    	super.onResume();
    	List sensors=(List) sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
    	if(sensors.size()>0){
    		sensorManager.registerListener(this, (Sensor) sensors.get(0),SensorManager.SENSOR_DELAY_FASTEST);
    	}	    	
	}	 

	/************线程处理tcp socket***************/
    private Runnable	mRunnable	= new Runnable() 
	{
		public void run()
		{
			int timeout = 2000;
			System.out.println("mRunnable.......");
			int start = conurl.indexOf(":");
			String sIP = conurl.substring(0, start);
			String sPort = conurl.substring(start+1);
			int port = Integer.parseInt(sPort);				
			Log.d("gjz", "IP:"+ sIP + ":" + port);		
			
			try 
			{							
				//连接服务器  mSocketClient = new Socket(sIP, port);	//portnum	
				mSocketClient = new Socket();	//portnum				
				SocketAddress isa = new InetSocketAddress(sIP, port);     
				mSocketClient.connect(isa, timeout);
				
				//取得输入、输出流
				mBufferedReaderClient = new BufferedReader(new InputStreamReader(mSocketClient.getInputStream()));
				mPrintWriterClient = new PrintWriter(mSocketClient.getOutputStream(), true);
				out = new DataOutputStream(mSocketClient.getOutputStream());
				

				Message msg = new Message();
                msg.what = 1;
				mHandler.sendMessage(msg);		
				//break;
			}
			catch (Exception e) 
			{
				Message msg = new Message();
                msg.what = 1;
				mHandler.sendMessage(msg);
				System.out.println("mRunnable....connect failed ...");
//				finish();
				return;
			}			

			
			
			while (isConnecting)
			{
				try
				{	
					//if ( (recvMessageClient = mBufferedReaderClient.readLine()) != null )
					if(mSocketClient.isConnected()&&(mBufferedReaderClient.read(recv_buffer)>0))
					{						
						Message msg = new Message();
		                msg.what = -1000;
						mHandler.sendMessage(msg);
						
					}
				}
				catch (Exception e)
				{
					Message msg = new Message();
	                msg.what = 1;
					mHandler.sendMessage(msg);
					System.out.println("mBufferedReaderClient....connect failed ...");
				}
			}
		}
	};
    
	/**  处理视频连接失败的提示  **/
    Handler mHandler = new Handler()
	{										
		  public void handleMessage(Message msg)										
		  {		
			  System.out.println("mHandler");
			  super.handleMessage(msg);	
			  if(msg.arg1 == -1){
				  new AlertDialog.Builder(com.czy.client.this).setTitle("网络连接").setMessage("视频服务器连接失败")  
		            .setPositiveButton("ok", new DialogInterface.OnClickListener() { 
		                @Override  
		                public void onClick(DialogInterface dialog, int which) {  
		                    // TODO Auto-generated method stub   
//		                	finish();
		                }  
		            }).show(); 
			  }
			  if(msg.what == -1000){
				  String strRecvMsg = String.copyValueOf(recv_buffer);
				  if(strRecvMsg.indexOf("csb_error")!=-1){
					  if(isCarCtrl==false){
							new AlertDialog.Builder(com.czy.client.this).setTitle("网络连接").setMessage("超声波出现故障")  
				            .setPositiveButton("ok", new DialogInterface.OnClickListener() { 
				                @Override  
				                public void onClick(DialogInterface dialog, int which) {  
				                    // TODO Auto-generated method stub 
				                	isCarCtrl = true;
				                	setButtonsVisible(View.VISIBLE);
				                    checkBoxGlavity.setVisibility(View.VISIBLE);
				                    checkBoxGlavity.setChecked(false);
	//			                	finish();
				                }  
				            }).show(); 
					  }
					  
				  }
				  if(strRecvMsg.indexOf("new_connect")!=-1){
						new AlertDialog.Builder(com.czy.client.this).setTitle("网络连接").setMessage("有新的连接, 如需要控制请重新连接")  
			            .setPositiveButton("ok", new DialogInterface.OnClickListener() { 
			                @Override  
			                public void onClick(DialogInterface dialog, int which) {  
			                    // TODO Auto-generated method stub   				                	
			                	finish();
			                }  
			            }).show(); 
				  }
				  for (int i = 0; i < recv_buffer.length; i++) {
					  recv_buffer[i] = '\0';        
			      }
			  }
			  //String strRecvMsg = String.copyValueOf(buffer);	

		  }									
	 };	
	 
	 
	 /******************释放资源************************/
	 public void onDestroy() {
		
		if (isConnecting) 
		{
			isConnecting = false;
			if((mSocketClient!=null) && (mSocketClient.isConnected())){
				System.out.println("on destroy");
				
				try {
						if(mSocketClient.isConnected()){
							SendCmd(CONNECT_CLOSE);							
						}			
						mPrintWriterClient.close();
						mPrintWriterClient = null;
						mSocketClient.close();
						mSocketClient = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
				mThreadClient.interrupt();				
			}			
			if((mSocketHttp!=null)&&(mSocketHttp.isConnected()) ){
				try {
					mSocketHttp.close();					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if(isSensorOpen){//关闭重力感应
			sensorManager.unregisterListener(this);
		}
		super.onDestroy();
		
	//	isCameraStop = true;
	//	isLightOpen = true;
	}
    
	 /******************发送命令************************/
    public void SendCmd(int cmd){    	 
    	 try {
    		 System.out.println("SendCmd");
    		 	if((mSocketClient==null) || !(mSocketClient.isConnected())){
    		 		System.out.println("mSocketClient.isConnected");
    		 		if(b_ispressed==false){// 检测按钮或者手势是否按下,松开则不显示
    		 			new AlertDialog.Builder(this).setTitle("网络连接").setMessage("TCP服务器连接失败")  
	                    .setPositiveButton("ok", new DialogInterface.OnClickListener() { 
	                        @Override  
	                        public void onClick(DialogInterface dialog, int which) {  
	                            // TODO Auto-generated method stub   

	                        }  
	                    }).show(); 
    		 			b_ispressed = true;
    		 		}else{
    		 			b_ispressed = false;
    		 		}    		 		 
    		 		return;
    		 	}
				out.writeInt(cmd);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}         
		     try {
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				    
    }
    
    
    class MessageHandler extends Handler {
        public MessageHandler(Looper looper) {
            super(looper);
        }
        public void handleMessage(Message msg) {
//        	System.out.println("MessageHandler handleMessage");
        	/* 更新视频显示 */
        	switch (msg.arg1) {
			case 0:
				imView.setImageBitmap((Bitmap)msg.obj);
				//count++;
				break;
			case -1:	
				
			default:
				break;
			}
        	/* 图片保存提示 */
        	switch (msg.arg2) {
			case 1:
				Toast.makeText(com.czy.client.this, "保存图片成功, 保存路径/sdcard/gec_capture", Toast.LENGTH_LONG).show(); 
				break;
			case -1:	
				Toast.makeText(com.czy.client.this, "保存图片失败", Toast.LENGTH_LONG).show(); 
				break;
			default:
				break;
			}
        	
        }
    }

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
/*******************************************************
 * 重力感应
 *****************************************************/
	private int index = 0;
	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
		
		/*
		private final static int GRAVITY_GO_FORWARD = 0x1;
		private final static int GRAVITY_GO_BACK    = 0x2;
		private final static int GRAVITY_TURN_LEFT  = 0x4;
		private final static int GRAVITY_TURN_RIGHT = 0x8;
		private final static int GRAVITY_STOP = 0;
		*/
		
		if(isSensorOpen){//判断是否勾选中重力感应
			x = arg0.values[SensorManager.DATA_X];        
			y = arg0.values[SensorManager.DATA_Y];        
			z = arg0.values[SensorManager.DATA_Z];		
			
			
			if(index++>80){
				index = 0;
				System.out.println("x is"+x);
				System.out.println("y is"+y);
				System.out.println("z is"+z);
			}
			if(x>=-6.5 && x<=3.0 && y>-3 && y<3  && z>3)
			{
				
				if((iSensorChange != GRAVITY_GO_FORWARD)){
					SendCmd(CAR_GO_FORWARD );
					iSensorChange = GRAVITY_GO_FORWARD;
//					System.out.println("bntForward");

				}
			}
			else if(x>=3.0 && x<= 8.5 && y>-3 && y<5 && z>6.5)//stop 
			{
				if((iSensorChange != GRAVITY_STOP)){
					SendCmd(CAR_STOP);	
					iSensorChange = GRAVITY_STOP;	
//					System.out.println("GRAVITY_STOP.......");
				}
					
		
			}
//			else if(x>=-2 && x<=8 && y>-3 && y<3 && z<-3)//stop 
//			{
//				if((iSensorChange != GRAVITY_GO_BACK)){
//					SendCmd(CAR_GO_BACK );
//					iSensorChange = GRAVITY_GO_BACK;
////					System.out.println("bntBack");
//				}	
//		
//			}
			else if((y>=3 && x>-8 && x<4)||(y>=1 && x>3)||(y>=1 && x<-7))
			{
				if((iSensorChange != GRAVITY_TURN_RIGHT)){
					SendCmd(CAR_TURN_RIGHT );
					iSensorChange = GRAVITY_TURN_RIGHT;	
//					System.out.println("GRAVITY_TURN_RIGHT");
				}
								   
			}
			else if((y<=-3.5 && x>-8 && x<4)||(y<=-1.5 && x>3)||(y<=-1.5 && x<-7))
			{
				if((iSensorChange != GRAVITY_TURN_LEFT)){
					SendCmd(CAR_TURN_LEFT );
					iSensorChange = GRAVITY_TURN_LEFT;
//					System.out.println("GRAVITY_TURN_LEFT");
				}
			}					
			else // back
			{
				if((iSensorChange != GRAVITY_GO_BACK)){
					SendCmd(CAR_GO_BACK );
					iSensorChange = GRAVITY_GO_BACK;
//					System.out.println("bntBack");
				}
//				if((iSensorChange != GRAVITY_STOP)){
//					SendCmd(CAR_STOP);	
//					iSensorChange = GRAVITY_STOP;	
////					System.out.println("GRAVITY_STOP.......");
//				}
			}
		}
		
	}
	/***********主要处理了, 在界面上对摄像头调整后释放时, 发送给小车摄像头停止的调整的命令************/
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
//		Log.i("touch","touch");
		switch (event.getAction()) {
			case MotionEvent.ACTION_UP : {// 鼠标弹起保存最后状态, 释放的时候响应               
				if(b_chageCam){ //  判断摄像头是否调整中
//					Log.d(this.getClass().getName(), "onTouch b_chageCam"  ); 
					SendCmd(CAM_STOP);
					b_chageCam = false;
				}				
                return false;
            }
		}
		return mGestureDetector.onTouchEvent(event); //  调用手势
	}
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {                
		// TODO Auto-generated method stub     
        return false;  
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/*******************************************************
	 * 处理界面滑动,识别手势 , 上下左右滑动
	 *****************************************************/	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub 
		    final int i_yOffset = 30; 
		    final int i_xOffset = 30; 
		    boolean   isValidOffset = false; 
            float y1 = e1.getY(), y2 = e2.getY();
            float x1 = e1.getX(), x2 = e2.getX();  
//            Log.d(this.getClass().getName(), "Fling" + "(" + x1  + "," + x2 + ")");  
//            Log.d(this.getClass().getName(), "scroll" + "(" + y1  + "," + y2 + ")");
            if(!b_chageCam){// 摄像头现在不处于调整状态
            	if (y1 -y2 > i_yOffset) {        //下滑动	                
	                Log.d(this.getClass().getName(), "To Down" + "(" + y1+ "," + y2 + ")");
	                SendCmd(CAM_DOWN);
	                isValidOffset = true;   
	            } else if (y1 - y2 < -i_yOffset) {    //上滑动
	                Log.d(this.getClass().getName(), "To UP" + "(" + y1  + "," + y2 + ")");  
	                SendCmd(CAM_UP);
	                isValidOffset = true; 
	            }else if (x1 -x2 > i_xOffset) {    //左滑动：
	                Log.d(this.getClass().getName(), "To LEFT" + "(" + x1+ "," + x2 + ")");  
	                SendCmd(CAM_LEFT);
	                isValidOffset = true;  
	            } else if (x1 - x2 < -i_xOffset) {    //右滑动：
	                Log.d(this.getClass().getName(), "To Right" + "(" + x1  + "," + x2 + ")"); 
	                SendCmd(CAM_RIGHT);
	                isValidOffset = true;   
	            }
            }
            if(isValidOffset){
            	b_chageCam = true;
            }
            return isValidOffset;  		
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		 
		return false;
	}	
}