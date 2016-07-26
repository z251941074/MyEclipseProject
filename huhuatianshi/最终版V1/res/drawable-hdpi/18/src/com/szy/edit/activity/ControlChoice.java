package com.szy.edit.activity;

import javax.security.auth.PrivateCredentialPermission;

import com.szy.edit.activity.ControlChoice;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ControlChoice extends Activity
{
	
	private Button OpenledSMMS;
	private Button OpenwaterSMMS;
	private Button surekeySMMS;
	private EditText Roominfo;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control);
		
		OpenledSMMS =(Button)findViewById(R.id.button1);
		OpenwaterSMMS =(Button)findViewById(R.id.button3);
		surekeySMMS =(Button)findViewById(R.id.button); 
		
		Roominfo = (EditText) findViewById(R.id.Roomifno);
		Roominfo.setCursorVisible(false);      
		Roominfo.setFocusable(false);         
		Roominfo.setFocusableInTouchMode(false);    

		
		OpenledSMMS.setOnClickListener(listener);
		OpenwaterSMMS.setOnClickListener(listener);
		surekeySMMS.setOnClickListener(listener);
	}

	private OnClickListener listener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{

			Button btn=(Button)v;
			switch (btn.getId())
			{
			case R.id.button1:
				Toast.makeText(ControlChoice.this, "打开LED", Toast.LENGTH_SHORT).show();	
				break;
//				String MMSstr = "室内有害气体超标，请求物业前往处理！";
//
//				SharedPreferences references = getSharedPreferences("account", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_WRITEABLE);
//				String phnoenum = references.getString("PropertyPhnoe", "");
//
//				if (!phnoenum.equals(""))
//				{
//					sendSMS(phnoenum, MMSstr);
//				} else
//				{
//					Toast.makeText(ControlChoice.this, "注册时未输入物业电话，请重新注册！", Toast.LENGTH_LONG).show();
//				}
//				break;
//				

			case R.id.button3:
				Toast.makeText(ControlChoice.this, "打开水阀", Toast.LENGTH_SHORT).show();
				break;
				
			case R.id.button:		
				Intent intent=new Intent();
				intent.setAction(Intent.ACTION_SENDTO);
				intent.setData(Uri.parse("smsto:18681618704"));
				intent.putExtra("sms_body", "welcome ");
				startActivity(intent);
				break;
				
			default:
				break;
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
