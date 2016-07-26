package com.czy;
/*
 * 功能说明：
 * 实现小车的对灯的控制
 * 时间：2014/5/9
 */
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Hashtable;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.WindowManager;
import java.net.DatagramSocket;
import android.widget.*;
public class client extends Activity  {
	private Context mContext = null;
	private ImageView imView = null;
	private Handler messageHandler =null;
	private Boolean isStop = true;
	private String conStr = "http://192.168.10.1:8080/?action=stream";
	private HttpRequest http = null;
	private String cmdPid = "";
	private Button btnSend;
	private ImageButton light;
	//////////////////////////////////
	private DatagramSocket socketUDP=null;
	private int       portRemoteNum;
	private int       portLocalNum;	
	private String    addressIP;
	private String    revData;
	private boolean   flag=false;
	private boolean   islighton = false;
	
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
     
        
    	light = (ImageButton)findViewById(R.id.ImageButton02);
    	light.setOnClickListener(lightClickListener); 
        
		mContext = this;
		http = new HttpRequest();    /* 调用HttpRequest类*/
		
		/*Looper扮演着一个Handler和消息队列之间通讯桥梁的角色。程序组件首先通过Handler把消息传递给Looper，
         * Looper把消息放入队列。Looper也把消息队列里的消息广播给所有的Handler，Handler接受到消息后调用handleMessage进行处理。
         */
        Looper looper = Looper.myLooper();     /*得到当前线程的Looper实例，由于当前线程是UI线程也可以通过Looper.getMainLooper()得到*/
        messageHandler = new MessageHandler(looper); //此处甚至可以不需要设置Looper，因为 Handler默认就使用当前线程的Looper  
    }
   
 
 
    
///////////////////////
private OnClickListener lightClickListener = new OnClickListener() {
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub				
		if (islighton) 
			{				
				islighton = false;
				light.setImageResource(R.drawable.off);
				Toast.makeText(mContext, "LED关闭!", Toast.LENGTH_SHORT).show();
				String str = "a";	
				sendData(str);				
			}
		else
			{				
				islighton = true;
				light.setImageResource(R.drawable.on);
				Toast.makeText(mContext, "LED打开!", Toast.LENGTH_SHORT).show();
				String str = "b";	
				sendData(str);						
			}
	}
};	
    ////////////////////////////////////
	 void sendData(String str)
		{    //功能注释：此处完成的Android之Socket的基于UDP传输，发送端
			 try{
				 flag=true;
				 portRemoteNum=1376;
				 portLocalNum=8080;
		         addressIP = "192.168.10.1";   // 下位机的的IP
				 //addressIP = "192.168.1.103";
		         socketUDP = new DatagramSocket(portLocalNum);  //创建一个DatagramSocket对象
				 btnSend.setEnabled(true);
                 } catch (Exception e) {
          // TODO Auto-generated catch block
           e.printStackTrace();}			
			try {
				      		    
			    InetAddress serverAddress = InetAddress.getByName(addressIP);	////通过主机名确定主机的IP地址, 创建一个 InetAddress ， 相当于是地址   
			    byte data [] = str.getBytes();      // 从创建，转为byte类型的数组
			    DatagramPacket packetS = new DatagramPacket(data,
			    		data.length,serverAddress,portRemoteNum);	              //创建一个DatagramPacket 对象，并指定要讲这个数据包发送到网络当中的哪个地址，以及端口号
			    //调用DatagramSocket对象的send方法 发送数据
			    socketUDP.send(packetS);     
			    } catch (Exception e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
			    }
			   socketUDP.close(); 
			
		}
	////////////////////////////////////  MessageHandler实现消息循环中要处理的message的方法 ,
    class MessageHandler extends Handler {   
        public MessageHandler(Looper looper) {
            super(looper);
        }
        public void handleMessage(Message msg) {
        	switch (msg.arg1) {
			case 0:
				imView.setImageBitmap((Bitmap)msg.obj);  //用于显示位图，暂时可以不要
				break;
			default:
				break;
			}
        	
        }
    }
    
}