/*
 *功能说明：本例程实现空间button的布局，以及一些点击button的响应，初步完成响应的配置
 *作者：dream
 *日期：2014/5/8
 */


package com.czy;

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

	private boolean   flag=false;
	private boolean   islighton = false;
	///////////////////////////////////
	private static final int MENU_ITEM_COUNTER = Menu.FIRST; 
	@Override  
	public boolean onCreateOptionsMenu(Menu menu) {          /*创建选项菜单*/
	    menu.add(0, MENU_ITEM_COUNTER, 0, "连接");   
	    menu.add(0, MENU_ITEM_COUNTER + 1, 0, "设置");   
        return super.onCreateOptionsMenu(menu);   
	}  
	@Override  
	public boolean onOptionsItemSelected(MenuItem item) {     /*选项菜单*/
	    switch (item.getItemId()) {   
	    case MENU_ITEM_COUNTER: 
	    	Conn();	    	
	        break;   
	    case MENU_ITEM_COUNTER + 1:   
	    	Setting();  
	        break;   

	    default:   
	        break;   
	    }   
	    return super.onOptionsItemSelected(item);   
	}  


    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //设置全屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);  //设置横屏 landscape 是横向，portrait 是纵向
  //设置按键===================================
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
//设置按键===================================        
               
    	yres = (ImageButton)findViewById(R.id.ImageButton01);
    	yres.setOnTouchListener(new yres_OnTouchListener()); 
        
    	speaker = (ImageButton)findViewById(R.id.ImageButton03);
    	speaker.setOnTouchListener(new speaker_OnTouchListener());
        
    	light = (ImageButton)findViewById(R.id.ImageButton02);
    	light.setOnClickListener(lightClickListener); 
        
		mContext = this;

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
        	String str = "1";	
        	Toast.makeText(mContext, "前进!", Toast.LENGTH_SHORT).show(); 
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
	 	Toast.makeText(mContext, "云台归位!", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(mContext, "摄像头灯guan!", Toast.LENGTH_SHORT).show();
				String str = "a";	
				sendData(str);				
			}
		else
			{				
				islighton = true;
				light.setImageResource(R.drawable.on);
				Toast.makeText(mContext, "摄像头灯开!", Toast.LENGTH_SHORT).show();
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
	Toast.makeText(mContext, "喇叭开！", Toast.LENGTH_SHORT).show();
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
		  
		         socketUDP = new DatagramSocket(portLocalNum);
				 btnSend.setEnabled(true);
                 } catch (Exception e) {
          // TODO Auto-generated catch block
           e.printStackTrace();}			
			try {
				      		    
  
			    byte data [] = str.getBytes(); 
		
			  
			    } catch (Exception e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
			    }
			   socketUDP.close(); 
			
		}
	/*menu链接设置*/
	public void Conn() {
		if(isStop){
			isStop = false;
			//conBtn.setText("Clo");
			setTitle("连接"); 
			new Thread() {
	            @SuppressWarnings("unchecked")
				public void run() {}
			}.start();
		}else{
			isStop = true;
			//conBtn.setText("Con");
			setTitle("断开"); 
		}
	} 
	/*menu菜单设置*/
    public void Setting() {
		LayoutInflater factory=LayoutInflater.from(mContext);
		final View v1=factory.inflate(R.layout.setting,null);
		AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
		dialog.setTitle("连接地址01");
		dialog.setView(v1);
		EditText et = (EditText)v1.findViewById(R.id.connectionurl);
    	et.setText(conStr);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	EditText qet = (EditText)v1.findViewById(R.id.connectionurl);
            	conStr = qet.getText().toString();
            	Toast.makeText(mContext, "设置成功!", Toast.LENGTH_SHORT).show(); 
            }
        });
        dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
        dialog.show();
	}
    
}