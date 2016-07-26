package com.activity.test;





import com.activity.test.R;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;


public class SendMessage extends MainActivity1{
		
		private  TextView tipTextView;	//电话号码提示
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
		 
		//
			super.onCreate(savedInstanceState);
			setContentView(R.layout.fragment_sendmessage);
			send();
			mian();
			finish();
			}	
	
		public void  send (){
			//数据获取
			SharedPreferences sp02 =getSharedPreferences("userInfo",SendMessage.MODE_PRIVATE);
			String phone =sp02.getString("PHONE_NUMBER", "18281601669");
			
			SharedPreferences sp03 =getSharedPreferences("userInfo1",SendMessage.MODE_PRIVATE);
			String address=sp03.getString("address", "未定位成功");
			//
			 String msgBody = "老人摔倒了请及时打电话询问情况！";
			 SmsManager sm = SmsManager.getDefault();
			 sm.sendTextMessage(phone,null,(msgBody+"地点是"+address),null,null);
			 Toast.makeText(this, ("已给监护人"+phone+"发送短信，如果为误检，请及时告知监护人"), Toast.LENGTH_LONG).show();
		}
		 
		public void mian(){

		//3秒后跳转到主程序界面
			Handler handler = new Handler();
			handler.postDelayed(new Runnable(){
			public void run(){
				//要执行的代码,跳转到主程序界面
				Intent intent = new Intent(getApplicationContext(),MainActivity1.class);
				//开始执行
				startActivity(intent);
				}
			}, 3000);//1000毫秒=1秒
		}
	}

