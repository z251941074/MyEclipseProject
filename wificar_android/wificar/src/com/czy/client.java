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
	/***********   ����ͷ   ������***********************************
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
	
	/*************����С������   ����**************************************
	#define CAR_STOP        0
	#define CAR_GO_FORWARD      1
	#define CAR_GO_BACK         2
	#define CAR_TURN_LEFT       3
	#define CAR_TURN_RIGHT      4
	#define CONNECT_CLOSE       10   //�Ͽ�����
	 ********************************************************************/
	private final static int CAR_STOP       = 0x0<<24;
	private final static int CAR_GO_FORWARD = 0x1<<24;
	private final static int CAR_GO_BACK    = 0x2<<24;
	private final static int CAR_TURN_LEFT  = 0x3<<24;
	private final static int CAR_TURN_RIGHT = 0x4<<24;	
	private final static int CONNECT_CLOSE  = 0xa<<24;
	  
	/*************������Ӧ      ����С������   ����**************************************
		
	 ********************************************************************/
	private final static int GRAVITY_GO_FORWARD = 0x1;
	private final static int GRAVITY_GO_BACK    = 0x2;
	private final static int GRAVITY_TURN_LEFT  = 4;
	private final static int GRAVITY_TURN_RIGHT = 8;
	private final static int GRAVITY_STOP       = 10;
	
	
	/*************���Ƶ�**************************************
		#define AUTO_Drive      13   //�Զ�ģʽ
		#define CTL_Drive       14  //ң��ģʽ
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
	private Boolean isCameraStop = true;//�б���Ƶ����״̬
	private Boolean isCarCtrl = true;//�б�С����ʻģʽ״̬
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
    private boolean isSensorOpen=false;//�б�������Ӧ����
    private int iSensorChange = GRAVITY_STOP;
    
    private boolean b_chageCam = false;  // ����ͷ����״̬
    private boolean b_ispressed = false; // �жϵ�������ͷ,��λ����Ļ�Ƿ���,���¾ͼ�������,�ɿ���ֹͣ 
    private boolean b_isCapture = false;  
    private char[]  recv_buffer = new char[256];
    GestureDetector mGestureDetector = null; // ����
    
    /******************���ð�ť״̬***********************/
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);//���ر��� 
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);//����ȫ��
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
        		WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //��ֹ�ֻ�����

        
        setContentView(R.layout.main);
        
        imView = (ImageView)findViewById(R.id.imageView1);
		bntForward  = (Button) findViewById(R.id.forward);
		bntBack     = (Button) findViewById(R.id.back);
		bntLeft     = (Button) findViewById(R.id.left);
		bntRight    = (Button) findViewById(R.id.right);		
		bntDriveMode  = (Button) findViewById(R.id.drive_mode);
		bntCapture = (Button) findViewById(R.id.capture);
		checkBoxGlavity = (CheckBox) findViewById(R.id.checkBox1);

		/***  ���滬����ʼ��  ***/
        mGestureDetector = new GestureDetector(this);
        FrameLayout ll=(FrameLayout)findViewById(R.id.mainLayout);
        ll.setOnTouchListener(this);
        ll.setLongClickable(true); 	
       
		/***  ��֮ǰ��activity�л�ȡ���ݹ���������  ***/  
		Bundle bundle = this.getIntent().getExtras();//ȡ��Bundle�����е�����
		conStr = "http://" + bundle.getString("conStr")+":8080/?action=stream";
		conurl = bundle.getString("conurl")+":4000";
		
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);//  ������Ӧ
		
		/***  ������Ӧ��ť  ***/
		checkBoxGlavity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            //�÷����ĵ�һ��������CompoundButton���󣬵ڶ���������isChecked�Ƿ�ѡ��
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked){//��ѡ������Ӧ
                	if((mSocketClient==null) || !(mSocketClient.isConnected())){
                		new AlertDialog.Builder(com.czy.client.this).setTitle("notification").setMessage("����������ʧ��")  
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
                }else{ //û�й�ѡ������Ӧ
                    isSensorOpen=false;
                    setButtonsVisible(View.VISIBLE);                    
    				iSensorChange = GRAVITY_STOP;	
    				//System.out.println("GRAVITY_STOP.......");
                }
            }
        });
		
		/******************  ��ǰ��ť          ******************
		 ******* ���Ʒ���İ�ťͳһ������:******************
		 ******* ���·��ͷ�������, �ͷŷ���ֹͣ����*********/
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
				return false;// return false��ʾϵͳ���������
			}
        }
        );
		/***  ���ť  ***/
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
		/***  ����ť  ***/
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
		/***  ���Ұ�ť  ***/
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
		/**********  ��ʻģʽ��ť :  �ֶ�   �Զ�      *******/				
		bntDriveMode.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(isCarCtrl){// ��Ҫ�ǶԱ�־λ�����ж�
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
		/***  ������ť  ***/
		bntCapture.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				b_isCapture = true;
				System.out.println("bntCapture.......");
			}
		});    
		/**   ������Ƶ����ΪͼƬ��·��   **/
		File parent = new File(IMAGE_PATH); 
        if(parent!=null&&!parent.exists()){ 
        	parent.mkdirs(); 
        } 
		
		/***  ��Ϣ����  ***/
        Looper looper = Looper.myLooper();
        messageHandler = new MessageHandler(looper);  
        
        
        /***** ָ���·�������mRunnable , �����߳�client,����С��  , 
         *     ����μ� private Runnable	mRunnable	= new Runnable()***/
		mThreadClient = new Thread(mRunnable); 
		mThreadClient.start();
		
        connectToHttpServer();  // ����http������                
    }
	
	/***************************************************
	 *	   ����http������
	 *******************************************************/
	public void connectToHttpServer(){				
		/*****  �����߳�http ,������Ƶ��  ***/
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
					//����   ����HTTPЭ����ʵ�ͷ����
					request.append("GET " + url.getFile() + " HTTP/1.0\r\n");
					request.append("Host: " + url.getHost() + "\r\n");
					request.append("\r\n");
					Log.i("request","request");
					os.write(request.toString().getBytes(), 0, request.length());
					
					//��ȡSocket���ص���������Ϣ, ���н�ȡ��ͼƬ, ������messageHandler���д���
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
						if (str3.startsWith("multipart/x-mixed-replace")){//��ʼͷ��
							n = str3.indexOf("boundary=");
							localObject2 = str3.substring(n + 9);
							localStreamSplit.skipToBoundary((String)localObject2);
						}else{// ��ͷ������
							byte[] localObject3 = localStreamSplit.readToBoundary((String)localObject2);
							if (localObject3.length == 0)
								break;
							
							Message message = Message.obtain();
							message.arg1 = 0;
						    message.obj = BitmapFactory.decodeByteArray(localObject3, 0, localObject3.length);
						    /***********������ƵΪͼƬ*************/
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
					if(isConnecting){// �жϵ�ǰsocket�Ƿ��Ѿ��˳��ر�,���жϺ����׻ᵼ�³����˳�ʱ����
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
	 *	   ���浽�ļ�, ·��ΪIMAGE_PATH /sdcard/gec_capture/
	 *   ����ֵ true false
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

	/************�̴߳���tcp socket***************/
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
				//���ӷ�����  mSocketClient = new Socket(sIP, port);	//portnum	
				mSocketClient = new Socket();	//portnum				
				SocketAddress isa = new InetSocketAddress(sIP, port);     
				mSocketClient.connect(isa, timeout);
				
				//ȡ�����롢�����
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
    
	/**  ������Ƶ����ʧ�ܵ���ʾ  **/
    Handler mHandler = new Handler()
	{										
		  public void handleMessage(Message msg)										
		  {		
			  System.out.println("mHandler");
			  super.handleMessage(msg);	
			  if(msg.arg1 == -1){
				  new AlertDialog.Builder(com.czy.client.this).setTitle("��������").setMessage("��Ƶ����������ʧ��")  
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
							new AlertDialog.Builder(com.czy.client.this).setTitle("��������").setMessage("���������ֹ���")  
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
						new AlertDialog.Builder(com.czy.client.this).setTitle("��������").setMessage("���µ�����, ����Ҫ��������������")  
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
	 
	 
	 /******************�ͷ���Դ************************/
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
		if(isSensorOpen){//�ر�������Ӧ
			sensorManager.unregisterListener(this);
		}
		super.onDestroy();
		
	//	isCameraStop = true;
	//	isLightOpen = true;
	}
    
	 /******************��������************************/
    public void SendCmd(int cmd){    	 
    	 try {
    		 System.out.println("SendCmd");
    		 	if((mSocketClient==null) || !(mSocketClient.isConnected())){
    		 		System.out.println("mSocketClient.isConnected");
    		 		if(b_ispressed==false){// ��ⰴť���������Ƿ���,�ɿ�����ʾ
    		 			new AlertDialog.Builder(this).setTitle("��������").setMessage("TCP����������ʧ��")  
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
        	/* ������Ƶ��ʾ */
        	switch (msg.arg1) {
			case 0:
				imView.setImageBitmap((Bitmap)msg.obj);
				//count++;
				break;
			case -1:	
				
			default:
				break;
			}
        	/* ͼƬ������ʾ */
        	switch (msg.arg2) {
			case 1:
				Toast.makeText(com.czy.client.this, "����ͼƬ�ɹ�, ����·��/sdcard/gec_capture", Toast.LENGTH_LONG).show(); 
				break;
			case -1:	
				Toast.makeText(com.czy.client.this, "����ͼƬʧ��", Toast.LENGTH_LONG).show(); 
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
 * ������Ӧ
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
		
		if(isSensorOpen){//�ж��Ƿ�ѡ��������Ӧ
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
	/***********��Ҫ������, �ڽ����϶�����ͷ�������ͷ�ʱ, ���͸�С������ͷֹͣ�ĵ���������************/
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
//		Log.i("touch","touch");
		switch (event.getAction()) {
			case MotionEvent.ACTION_UP : {// ��굯�𱣴����״̬, �ͷŵ�ʱ����Ӧ               
				if(b_chageCam){ //  �ж�����ͷ�Ƿ������
//					Log.d(this.getClass().getName(), "onTouch b_chageCam"  ); 
					SendCmd(CAM_STOP);
					b_chageCam = false;
				}				
                return false;
            }
		}
		return mGestureDetector.onTouchEvent(event); //  ��������
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
	 * ������滬��,ʶ������ , �������һ���
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
            if(!b_chageCam){// ����ͷ���ڲ����ڵ���״̬
            	if (y1 -y2 > i_yOffset) {        //�»���	                
	                Log.d(this.getClass().getName(), "To Down" + "(" + y1+ "," + y2 + ")");
	                SendCmd(CAM_DOWN);
	                isValidOffset = true;   
	            } else if (y1 - y2 < -i_yOffset) {    //�ϻ���
	                Log.d(this.getClass().getName(), "To UP" + "(" + y1  + "," + y2 + ")");  
	                SendCmd(CAM_UP);
	                isValidOffset = true; 
	            }else if (x1 -x2 > i_xOffset) {    //�󻬶���
	                Log.d(this.getClass().getName(), "To LEFT" + "(" + x1+ "," + x2 + ")");  
	                SendCmd(CAM_LEFT);
	                isValidOffset = true;  
	            } else if (x1 - x2 < -i_xOffset) {    //�һ�����
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