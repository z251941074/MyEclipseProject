package com.szy.edit.activity;

import javax.security.auth.PrivateCredentialPermission;

import com.szy.edit.activity.ControlChoice;

import android.net.Uri;
import android.os.Bundle;
import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;


public class ControlChoice extends Activity
{
	
	private Button OpenledSMMS;
	private Button OpenwaterSMMS;
	private Button surekeySMMS;
	private Button readsmmschaxun;
	private Button readsmmsguanbi;
	
	private EditText Roominfo;
	
	
	private static final String LOG_TAG = "Sms Query";
	public final static String SMS_URI_ALL =  "content://sms/"; //0     
	public final static String SMS_URI_INBOX = "content://sms/inbox";//1     
	public final static String SMS_URI_SEND = "content://sms/sent";//2     
	public final static String SMS_URI_DRAFT = "content://sms/draft";//3     
	public final static String SMS_URI_OUTBOX = "content://sms/outbox";//4     
	public final static String SMS_URI_FAILED = "content://sms/failed";//5     
	public final static String SMS_URI_QUEUED = "content://sms/queued";//6 

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control);
		
		OpenledSMMS =(Button)findViewById(R.id.button1);
		OpenwaterSMMS =(Button)findViewById(R.id.button3);
		surekeySMMS =(Button)findViewById(R.id.button); 
		readsmmschaxun =(Button)findViewById(R.id.button10);
		readsmmsguanbi =(Button)findViewById(R.id.button11); 
		
		Roominfo = (EditText) findViewById(R.id.Roomifno);
		Roominfo.setCursorVisible(false);      
		Roominfo.setFocusable(false);         
		Roominfo.setFocusableInTouchMode(false);    

		
		OpenledSMMS.setOnClickListener(listener);
		OpenwaterSMMS.setOnClickListener(listener);
		surekeySMMS.setOnClickListener(listener);
		readsmmschaxun.setOnClickListener(listener);
		readsmmsguanbi.setOnClickListener(listener);
	}

	/* ***发送短信的控制函数*** */
	private void sendSMS(String phoneNumber, String message)
	{
		// 得到SMS的管理类
		SmsManager smsManager = SmsManager.getDefault();
		PendingIntent paIntent = PendingIntent.getBroadcast(ControlChoice.this, 0, new Intent("SMS_SENT"), 0);

		registerReceiver(new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{
				int resultCode = getResultCode();

				switch (resultCode)
				{
				case Activity.RESULT_OK:
					Toast.makeText(ControlChoice.this, "已经通知护花天使", Toast.LENGTH_LONG).show();
					break;

				default:
					Toast.makeText(ControlChoice.this, "通知护花天使失败", Toast.LENGTH_LONG).show();
				}
			}
		}, new IntentFilter("SMS_SENT"));

		smsManager.sendTextMessage(phoneNumber, null, message, paIntent, null);
	}
	
	/** *//**
	* 处理短信结果
	* @param cur
	* @param all 用来判断是读一条还是全部读。后来没有用all，可以无视
	*/
	private StringBuilder processResults(Cursor cur, boolean all) {
	   // TODO Auto-generated method stub
	   StringBuilder str=new StringBuilder();
	   if (cur.moveToFirst()) {

	         String name; 
	         String phoneNumber;       
	         String sms;
	         
	         
	         int nameColumn = cur.getColumnIndex("person");
	         int phoneColumn = cur.getColumnIndex("address");
	         int smsColumn = cur.getColumnIndex("body");
	         
//	         do {
//	             // Get the field values
//	             name = cur.getString(nameColumn);             
//	             phoneNumber = cur.getString(phoneColumn);
//	             sms = cur.getString(smsColumn);
//	             
//	           //  str.append("{");
//	           //  str.append(name+",");
//	           //  str.append(phoneNumber+",");
//	             str.append(sms);
//	           //  str.append("}");
//	             
//
//	             
//	             if (null==sms)
//	             sms="";
//	             
//	             
//	/**//*             if (all)
//	             mView.loadUrl("javascript:navigator.SmsManager.droidAddContact('" + name + "','" + phoneNumber + "','" + sms +"')");             
//	             else
//	             mView.loadUrl("javascript:navigator.sms.droidFoundContact('" + name + "','" + phoneNumber + "','" + sms +"')");*/
//	                          
//	         } while (cur.moveToNext());
	         if(cur.moveToFirst()){
	             // Get the field values
	             name = cur.getString(nameColumn);             
	             phoneNumber = cur.getString(phoneColumn);
	             sms = cur.getString(smsColumn);
	             int sms_len=sms.length();
	             char stringArr[] = sms.toCharArray(); //字符串转化为数组
	             String   s=new   String(stringArr);
	             System.out.println("smslen的值："+sms_len);
	             System.out.println("短信内容是"+s);   
	             
	             char[] temp01={' ',' ', ' ',' ',' '};
	             char[] temp02={' ',' ', ' ',' ',' '};
	             int i=0,j=0,k=0,n=0,m=0;
//	            while(stringArr[i] !=' ')
//	            {	
//	            		 temp01[j]=stringArr[i];
//	            		 i++;
//	            		 j++;
//	            }
	             
	             temp01[0]=stringArr[0];
	             temp01[1]=stringArr[1];
	             String  Temperature_Env=new   String(temp01);
	             System.out.println("i的值："+i);
	             System.out.println("Temperature_Env内容是"+Temperature_Env);  
	             
	             str.append("环境温度是");
	             str.append(Temperature_Env);
	             str.append("℃"+ "\n");
	             
	             
//	            while(stringArr[i+1] !=' ')
//	            {
//	            		 temp01[k]=stringArr[i+1];
//	            		 i++;
//	            		 k++;
//	            }
	             temp01[0]=stringArr[3];
	             temp01[1]=stringArr[4];
	             String  Temperature_Soil=new   String(temp01);
	             System.out.println("i的值："+i);
	             System.out.println("Temperature_Soil内容是"+Temperature_Soil);
	             str.append("土壤温度是");
	             str.append(Temperature_Soil);
	             str.append("℃"+ "\n");
	             
	             temp01[0]=stringArr[6];
	             temp01[1]=stringArr[7];
		         String  Hui_Soil=new   String(temp01);  
		         str.append("土壤湿度是");
		         str.append(Hui_Soil);
		         str.append("%"+ "\n");
		         
		         temp01[0]=stringArr[9];
		         temp01[1]=stringArr[10]; 
	             String  s1=new   String(temp01);
	             System.out.println("天气内容是"+s1);
		        switch (temp01[1])
				{
				case '0':
					 str.append("天气是");
		             str.append("晴天"+"\n");
		             str.append(" "); 
					break;
				case '1':
					str.append("天气是");
		             str.append("多云"+"\n");
		             str.append(" "); 
					break;
				case '3':
					str.append("天气是");
		             str.append("阴天"+"\n");
		             str.append(" "); 
					break;
				case '4':
					 str.append("天气是");
		             str.append("昏暗"+"\n");
		             str.append(" "); 
					break;
				case '5':
					 str.append("天气是");
		             str.append("黑暗"+"\n");
		             str.append(" "); 
					break;
				default:
					break;
				} 
	             if (null==sms)
	             sms="";
	             
	             
	/**//*             if (all)
	             mView.loadUrl("javascript:navigator.SmsManager.droidAddContact('" + name + "','" + phoneNumber + "','" + sms +"')");             
	             else
	             mView.loadUrl("javascript:navigator.sms.droidFoundContact('" + name + "','" + phoneNumber + "','" + sms +"')");*/
	                          
	         }
	/**//*         if (all)
	         mView.loadUrl("javascript:navigator.SmsManager.droidDone()");
	         else
	         mView.loadUrl("javascript:navigator.sms.droidDone();");*/
	     }
	     else
	     {
	     str.append("no result!");
	/**//*     if(all)
	        mView.loadUrl("javascript:navigator.SmsManager.fail()");
	     else
	        mView.loadUrl("javascript:navigator.sms.fail('None found!')");*/
	     }

	   return str;
	}//processResults
	
	
	/**
	* 读取短信
	* @return
	*/
	public String getSmsAndSendBack()
	{
	   String[] projection = new String[] {
	     "_id",
	     "address",
	     "person",
	     "body"
	    };
	   StringBuilder str=new StringBuilder();
	   try{
	    Cursor myCursor = managedQuery(Uri.parse("content://sms/inbox"), projection,null, null , "date desc");
	    str.append(processResults(myCursor, true));  
	    //    str.append("getContactsAndSendBack has executed!");
	    /**//*    myCursor = managedQuery(Uri.parse("content://sms/inbox"),
	      new String[] { "_id", "address", "read" },
	      " address=? and read=?",
	      new String[] { "12345678901", "0" }, "date desc");*/
	   
	   }
	   catch (SQLiteException ex)
	   {
//	    Log.d(LOG_TAG, ex.getMessage());
	   }
	   return str.toString();
	}
	public String smmstoedit()
	{
		
		String str;
		str = getSmsAndSendBack();
		return str;
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
/////////////////////////////////////打电话begin
				SharedPreferences references1 = getSharedPreferences("account", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_WRITEABLE);
				String phnoenum1 = references1.getString("PropertyPhnoe", "");
				//用intent启动拨打电话  
				Intent intent1 = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phnoenum1)); 
				startActivity(intent1);
				Toast.makeText(ControlChoice.this, "关闭浇水进行中...", Toast.LENGTH_LONG).show();
//				 try {
//			            Thread.sleep(20000);
//			        } catch (InterruptedException e) {
//			            e.printStackTrace(); 
//			        }
//				Roominfo.setText(smmstoedit());
				break;
//////////////////////////////////////////打电话end
///////////////////////////////////发送短信begin
//				Toast.makeText(ControlChoice.this, "打开LED", Toast.LENGTH_SHORT).show();	
//				break;
//				String MMSstr1 = "10";
//
//				SharedPreferences references1 = getSharedPreferences("account", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_WRITEABLE);
//				String phnoenum1 = references1.getString("PropertyPhnoe", "");
//
//				if (!phnoenum1.equals(""))
//				{
//					sendSMS(phnoenum1, MMSstr1);
//				} else
//				{
//					Toast.makeText(ControlChoice.this, "注册时未输入护花天使电话，请重新注册！", Toast.LENGTH_LONG).show();
//				}
//				break;
///////////////////////////////发送短信end				
			case R.id.button3:
////////////////////////////////打电话begin
				SharedPreferences references3 = getSharedPreferences("account", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_WRITEABLE);
				String phnoenum3 = references3.getString("PropertyPhnoe", "");
				//用intent启动拨打电话  
				Intent intent3 = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phnoenum3)); 
				startActivity(intent3);
				Toast.makeText(ControlChoice.this, "爱花即将浇水...", Toast.LENGTH_LONG).show();
//				try {
//		            Thread.sleep(20000);
//		        } catch (InterruptedException e) {
//		            e.printStackTrace(); 
//		        }
//				Roominfo.setText(smmstoedit());
				break;
////////////////////////////////打电话end
///////////////////////////////发短信begin
//				Toast.makeText(ControlChoice.this, "打开水阀", Toast.LENGTH_SHORT).show();
//				break;
//				String MMSstr3 = "20";
//
//				SharedPreferences references3 = getSharedPreferences("account", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_WRITEABLE);
//				String phnoenum3 = references3.getString("PropertyPhnoe", "");
//
//				if (!phnoenum3.equals(""))
//				{
//					sendSMS(phnoenum3, MMSstr3);
//				} else
//				{
//					Toast.makeText(ControlChoice.this, "注册时未输入护花天使电话，请重新注册！", Toast.LENGTH_LONG).show();
//				}
//				break;
////////////////////////////////发短信end				
			case R.id.button:	
////////////////////////////////打电话begin
				SharedPreferences references5 = getSharedPreferences("account", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_WRITEABLE);
				String phnoenum5 = references5.getString("PropertyPhnoe", "");
				//用intent启动拨打电话  
                Intent intent5 = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phnoenum5)); 
				startActivity(intent5);
				Toast.makeText(ControlChoice.this, "爱花状态查询中...", Toast.LENGTH_LONG).show();
//				try {
//		            Thread.sleep(2000);
//		        } catch (InterruptedException e) {
//		            e.printStackTrace(); 
//		        }
				//Roominfo.setText(smmstoedit());
				break;
//////////////////////////////// 打电话end
/////////////////////////////////发送短信begin				
//				String MMSstr5 = "30";
//
//				SharedPreferences references5 = getSharedPreferences("account", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_WRITEABLE);
//				String phnoenum5 = references5.getString("PropertyPhnoe", "");
//
//				if (!phnoenum5.equals(""))
//				{
//					sendSMS(phnoenum5, MMSstr5);
//					Roominfo.setText(smmstoedit());
//				} else
//				{
//					Toast.makeText(ControlChoice.this, "注册时未输入护花天使电话，请重新注册！", Toast.LENGTH_LONG).show();
//				}
//				break;
//////////////////////////发送短信end
			case R.id.button10:	
				SharedPreferences references10 = getSharedPreferences("account", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_WRITEABLE);
				String phnoenum10 = references10.getString("PropertyPhnoe", "");
				Toast.makeText(ControlChoice.this, "读取短信中...", Toast.LENGTH_LONG).show();
				Roominfo.setText(smmstoedit());
				break;
			case R.id.button11:	
				SharedPreferences references11 = getSharedPreferences("account", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_WRITEABLE);
				String phnoenum11 = references11.getString("PropertyPhnoe", "");
				Toast.makeText(ControlChoice.this, "读取短信中...", Toast.LENGTH_LONG).show();
				Roominfo.setText(smmstoedit());
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
