package com.czy;
/*
 * 功能说明：
 * 实现小车的前进，后退、左、右转功能
 * 实现云台的俯仰和左右转
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
	private ImageButton forward;
	private ImageButton backward;
	private ImageButton left;
	private ImageButton right;
	private ImageButton yforward;
	private ImageButton ybackward;
	private ImageButton yleft;
	private ImageButton yright;
	private ImageButton yres;
	private ImageButton speaker;
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
        imView = (ImageView)findViewById(R.id.imageView1);	
        forward = (ImageButton)findViewById(R.id.forward);
        forward.setOnTouchListener(new forward_OnTouchListener());
        
        backward = (ImageButton)findViewById(R.id.backward);
        backward.setOnTouchListener(new backward_OnTouchListener()); 
        
        left = (ImageButton)findViewById(R.id.left);
        left.setOnTouchListener(new left_OnTouchListener());
        
        right = (ImageButton)findViewById(R.id.right);
        right.setOnTouchListener(new right_OnTouchListener()); 
        
        yforward = (ImageButton)findViewById(R.id.yforward);
        yforward.setOnTouchListener(new yforward_OnTouchListener());
        
        ybackward = (ImageButton)findViewById(R.id.ybackward);
        ybackward.setOnTouchListener(new ybackward_OnTouchListener()); 
        
        yleft = (ImageButton)findViewById(R.id.yleft);
        yleft.setOnTouchListener(new yleft_OnTouchListener());
        
        yright = (ImageButton)findViewById(R.id.yright);
        yright.setOnTouchListener(new yright_OnTouchListener()); 
               
    	yres = (ImageButton)findViewById(R.id.ImageButton01);
    	yres.setOnTouchListener(new yres_OnTouchListener()); 
        
    	speaker = (ImageButton)findViewById(R.id.ImageButton03);
    	speaker.setOnTouchListener(new speaker_OnTouchListener());
        
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
    ///////////////////////////////////////////////////
	class forward_OnTouchListener implements OnTouchListener
    {
      @Override
      public boolean onTouch(View v, MotionEvent event)
      {
        if (event.getAction() == MotionEvent.ACTION_DOWN)//按下
           {
        	forward.setImageResource(R.drawable.forward2);
        	Toast.makeText(mContext, "前进!", Toast.LENGTH_SHORT).show();
        	String str = "1";	
     	    sendData(str);	     	  
            }  
       if (event.getAction() == MotionEvent.ACTION_UP)//弹起
          {
    	   forward.setImageResource(R.drawable.forward1);
    	   String str = "0";	
    	   sendData(str);	
          }
       //返回true  表示事件处理完毕，会中断系统对该事件的处理。false 系统会同时处理对应的事件
       return true;
      }
    }
 
    ////////////////////////////////////
	class backward_OnTouchListener implements OnTouchListener
    {
      @Override
      public boolean onTouch(View v, MotionEvent event)
      {
        if (event.getAction() == MotionEvent.ACTION_DOWN)//按下
           {
        	backward.setImageResource(R.drawable.backward2);
        	Toast.makeText(mContext, "后退!", Toast.LENGTH_SHORT).show();
        	String str = "2";	
     	    sendData(str);		
            }  
       if (event.getAction() == MotionEvent.ACTION_UP)//弹起
          {
    	   backward.setImageResource(R.drawable.backward1);
    	   String str = "0";	
    	   sendData(str);	
          }
       //返回true  表示事件处理完毕，会中断系统对该事件的处理。false 系统会同时处理对应的事件
       return true;
      }
    }
 
    ////////////////////////////////////
	class left_OnTouchListener implements OnTouchListener
    {
      @Override
      public boolean onTouch(View v, MotionEvent event)
      {
        if (event.getAction() == MotionEvent.ACTION_DOWN)//按下
           {
        	left.setImageResource(R.drawable.left2);
        	Toast.makeText(mContext, "左转!", Toast.LENGTH_SHORT).show();
        	String str = "3";	
     	    sendData(str);		
            }  
       if (event.getAction() == MotionEvent.ACTION_UP)//弹起
          {
    	   left.setImageResource(R.drawable.left1);
    	   String str = "0";	
    	   sendData(str);	
          }
       //返回true  表示事件处理完毕，会中断系统对该事件的处理。false 系统会同时处理对应的事件
       return true;
      }
    }
 
    ////////////////////////////////////
	class right_OnTouchListener implements OnTouchListener
    {
      @Override
      public boolean onTouch(View v, MotionEvent event)
      {
        if (event.getAction() == MotionEvent.ACTION_DOWN)//按下
           {
        	right.setImageResource(R.drawable.right2);
           	Toast.makeText(mContext, "右转!", Toast.LENGTH_SHORT).show();
        	String str = "4";	
     	    sendData(str);		
            }  
       if (event.getAction() == MotionEvent.ACTION_UP)//弹起
          {
    	   right.setImageResource(R.drawable.right1);
    	   String str = "0";	
    	   sendData(str);	
          }
       //返回true  表示事件处理完毕，会中断系统对该事件的处理。false 系统会同时处理对应的事件
       return true;
      }
    }
 
    ////////////////////////////////////
	class yforward_OnTouchListener implements OnTouchListener
    {
      @Override
      public boolean onTouch(View v, MotionEvent event)
      {
        if (event.getAction() == MotionEvent.ACTION_DOWN)//按下
           {
        	yforward.setImageResource(R.drawable.forward2);
        	Toast.makeText(mContext, "云台前俯!", Toast.LENGTH_SHORT).show();
        	String str = "5";	
     	    sendData(str);		
            }  
       if (event.getAction() == MotionEvent.ACTION_UP)//弹起
          {
    	   yforward.setImageResource(R.drawable.forward1);
    	   String str = "0";	
    	   sendData(str);	
          }
       //返回true  表示事件处理完毕，会中断系统对该事件的处理。false 系统会同时处理对应的事件
       return true;
      }
    }
 
    ////////////////////////////////////
	class ybackward_OnTouchListener implements OnTouchListener
    {
      @Override
      public boolean onTouch(View v, MotionEvent event)
      {
        if (event.getAction() == MotionEvent.ACTION_DOWN)//按下
           {
        	ybackward.setImageResource(R.drawable.backward2);
        	Toast.makeText(mContext, "云台后仰!", Toast.LENGTH_SHORT).show();
        	String str = "6";	
     	    sendData(str);		
            }  
       if (event.getAction() == MotionEvent.ACTION_UP)//弹起
          {
    	   ybackward.setImageResource(R.drawable.backward1);
    	   String str = "0";	
    	   sendData(str);	
          }
       //返回true  表示事件处理完毕，会中断系统对该事件的处理。false 系统会同时处理对应的事件
       return true;
      }
    }
 
    ////////////////////////////////////
	class yleft_OnTouchListener implements OnTouchListener
    {
      @Override
      public boolean onTouch(View v, MotionEvent event)
      {
        if (event.getAction() == MotionEvent.ACTION_DOWN)//按下
           {
        	yleft.setImageResource(R.drawable.left2);
          	Toast.makeText(mContext, "云台左转!", Toast.LENGTH_SHORT).show();
        	String str = "7";	
     	    sendData(str);		
            }  
       if (event.getAction() == MotionEvent.ACTION_UP)//弹起
          {
    	   yleft.setImageResource(R.drawable.left1);
    	   String str = "0";	
    	   sendData(str);	
          }
       //返回true  表示事件处理完毕，会中断系统对该事件的处理。false 系统会同时处理对应的事件
       return true;
      }
    }
 
    ////////////////////////////////////
	class yright_OnTouchListener implements OnTouchListener
    {
      @Override
      public boolean onTouch(View v, MotionEvent event)
      {
        if (event.getAction() == MotionEvent.ACTION_DOWN)//按下
           {
        	yright.setImageResource(R.drawable.right2);
          	Toast.makeText(mContext, "云台右转!", Toast.LENGTH_SHORT).show();
        	String str = "8";	
     	    sendData(str);		
            }  
       if (event.getAction() == MotionEvent.ACTION_UP)//弹起
          {
    	   yright.setImageResource(R.drawable.right1);
    	   String str = "0";	
    	   sendData(str);	
          }
       //返回true  表示事件处理完毕，会中断系统对该事件的处理。false 系统会同时处理对应的事件
       return true;
      }
    }
////////////////////////////////////
class yres_OnTouchListener implements OnTouchListener
{
@Override
public boolean onTouch(View v, MotionEvent event)
{
if (event.getAction() == MotionEvent.ACTION_DOWN)//按下
{
	yres.setImageResource(R.drawable.guion);
String str = "9";	
sendData(str);		
}  
if (event.getAction() == MotionEvent.ACTION_UP)//弹起
{
	yres.setImageResource(R.drawable.guioff);
String str = "0";	
sendData(str);	
}
//返回true  表示事件处理完毕，会中断系统对该事件的处理。false 系统会同时处理对应的事件
return true;
}
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
				String str = "a";	
				sendData(str);				
			}
		else
			{				
				islighton = true;
				light.setImageResource(R.drawable.on);
				String str = "b";	
				sendData(str);						
			}
	}
};	
////////////////////////////////////
class speaker_OnTouchListener implements OnTouchListener
{
@Override
public boolean onTouch(View v, MotionEvent event)
{
if (event.getAction() == MotionEvent.ACTION_DOWN)//按下
{
	speaker.setImageResource(R.drawable.speakeron);
String str = "c";	
sendData(str);		
}  
if (event.getAction() == MotionEvent.ACTION_UP)//弹起
{
	speaker.setImageResource(R.drawable.speakeroff);
String str = "0";	
sendData(str);	
}
//返回true  表示事件处理完毕，会中断系统对该事件的处理。false 系统会同时处理对应的事件
return true;
}
}
    ////////////////////////////////////
	 void sendData(String str)
		{
			 try{
				 flag=true;
				 portRemoteNum=1376;
				 portLocalNum=8080;
		         addressIP = "192.168.10.1";
				 //addressIP = "192.168.1.103";
		         socketUDP = new DatagramSocket(portLocalNum);
				 btnSend.setEnabled(true);
                 } catch (Exception e) {
          // TODO Auto-generated catch block
           e.printStackTrace();}			
			try {
				      		    
			    InetAddress serverAddress = InetAddress.getByName(addressIP);	////通过主机名确定主机的IP地址   
			    byte data [] = str.getBytes(); 
			    DatagramPacket packetS = new DatagramPacket(data,
			    		data.length,serverAddress,portRemoteNum);	
			    //从本地端口给指定IP的远程端口发数据包
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