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
		
		private  TextView tipTextView;	//�绰������ʾ
		
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
			//���ݻ�ȡ
			SharedPreferences sp02 =getSharedPreferences("userInfo",SendMessage.MODE_PRIVATE);
			String phone =sp02.getString("PHONE_NUMBER", "18281601669");
			
			SharedPreferences sp03 =getSharedPreferences("userInfo1",SendMessage.MODE_PRIVATE);
			String address=sp03.getString("address", "δ��λ�ɹ�");
			//
			 String msgBody = "����ˤ�����뼰ʱ��绰ѯ�������";
			 SmsManager sm = SmsManager.getDefault();
			 sm.sendTextMessage(phone,null,(msgBody+"�ص���"+address),null,null);
			 Toast.makeText(this, ("�Ѹ��໤��"+phone+"���Ͷ��ţ����Ϊ��죬�뼰ʱ��֪�໤��"), Toast.LENGTH_LONG).show();
		}
		 
		public void mian(){

		//3�����ת�����������
			Handler handler = new Handler();
			handler.postDelayed(new Runnable(){
			public void run(){
				//Ҫִ�еĴ���,��ת�����������
				Intent intent = new Intent(getApplicationContext(),MainActivity1.class);
				//��ʼִ��
				startActivity(intent);
				}
			}, 3000);//1000����=1��
		}
	}

