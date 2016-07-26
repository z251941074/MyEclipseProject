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

	/* ***���Ͷ��ŵĿ��ƺ���*** */
	private void sendSMS(String phoneNumber, String message)
	{
		// �õ�SMS�Ĺ�����
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
					Toast.makeText(ControlChoice.this, "�Ѿ�֪ͨ������ʹ", Toast.LENGTH_LONG).show();
					break;

				default:
					Toast.makeText(ControlChoice.this, "֪ͨ������ʹʧ��", Toast.LENGTH_LONG).show();
				}
			}
		}, new IntentFilter("SMS_SENT"));

		smsManager.sendTextMessage(phoneNumber, null, message, paIntent, null);
	}
	
	/** *//**
	* ������Ž��
	* @param cur
	* @param all �����ж��Ƕ�һ������ȫ����������û����all����������
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
	             char stringArr[] = sms.toCharArray(); //�ַ���ת��Ϊ����
	             String   s=new   String(stringArr);
	             System.out.println("smslen��ֵ��"+sms_len);
	             System.out.println("����������"+s);   
	             
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
	             System.out.println("i��ֵ��"+i);
	             System.out.println("Temperature_Env������"+Temperature_Env);  
	             
	             str.append("�����¶���");
	             str.append(Temperature_Env);
	             str.append("��"+ "\n");
	             
	             
//	            while(stringArr[i+1] !=' ')
//	            {
//	            		 temp01[k]=stringArr[i+1];
//	            		 i++;
//	            		 k++;
//	            }
	             temp01[0]=stringArr[3];
	             temp01[1]=stringArr[4];
	             String  Temperature_Soil=new   String(temp01);
	             System.out.println("i��ֵ��"+i);
	             System.out.println("Temperature_Soil������"+Temperature_Soil);
	             str.append("�����¶���");
	             str.append(Temperature_Soil);
	             str.append("��"+ "\n");
	             
	             temp01[0]=stringArr[6];
	             temp01[1]=stringArr[7];
		         String  Hui_Soil=new   String(temp01);  
		         str.append("����ʪ����");
		         str.append(Hui_Soil);
		         str.append("%"+ "\n");
		         
		         temp01[0]=stringArr[9];
		         temp01[1]=stringArr[10]; 
	             String  s1=new   String(temp01);
	             System.out.println("����������"+s1);
		        switch (temp01[1])
				{
				case '0':
					 str.append("������");
		             str.append("����"+"\n");
		             str.append(" "); 
					break;
				case '1':
					str.append("������");
		             str.append("����"+"\n");
		             str.append(" "); 
					break;
				case '3':
					str.append("������");
		             str.append("����"+"\n");
		             str.append(" "); 
					break;
				case '4':
					 str.append("������");
		             str.append("�谵"+"\n");
		             str.append(" "); 
					break;
				case '5':
					 str.append("������");
		             str.append("�ڰ�"+"\n");
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
	* ��ȡ����
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
/////////////////////////////////////��绰begin
				SharedPreferences references1 = getSharedPreferences("account", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_WRITEABLE);
				String phnoenum1 = references1.getString("PropertyPhnoe", "");
				//��intent��������绰  
				Intent intent1 = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phnoenum1)); 
				startActivity(intent1);
				Toast.makeText(ControlChoice.this, "���������رս�ˮ...", Toast.LENGTH_LONG).show();
//				 try {
//			            Thread.sleep(20000);
//			        } catch (InterruptedException e) {
//			            e.printStackTrace(); 
//			        }
//				Roominfo.setText(smmstoedit());
				break;
//////////////////////////////////////////��绰end
///////////////////////////////////���Ͷ���begin
//				Toast.makeText(ControlChoice.this, "��LED", Toast.LENGTH_SHORT).show();	
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
//					Toast.makeText(ControlChoice.this, "ע��ʱδ���뻤����ʹ�绰��������ע�ᣡ", Toast.LENGTH_LONG).show();
//				}
//				break;
///////////////////////////////���Ͷ���end				
			case R.id.button3:
////////////////////////////////��绰begin
				SharedPreferences references3 = getSharedPreferences("account", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_WRITEABLE);
				String phnoenum3 = references3.getString("PropertyPhnoe", "");
				//��intent��������绰  
				Intent intent3 = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phnoenum3)); 
				startActivity(intent3);
				Toast.makeText(ControlChoice.this, "����������ˮ...", Toast.LENGTH_LONG).show();
//				try {
//		            Thread.sleep(20000);
//		        } catch (InterruptedException e) {
//		            e.printStackTrace(); 
//		        }
//				Roominfo.setText(smmstoedit());
				break;
////////////////////////////////��绰end
///////////////////////////////������begin
//				Toast.makeText(ControlChoice.this, "��ˮ��", Toast.LENGTH_SHORT).show();
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
//					Toast.makeText(ControlChoice.this, "ע��ʱδ���뻤����ʹ�绰��������ע�ᣡ", Toast.LENGTH_LONG).show();
//				}
//				break;
////////////////////////////////������end				
			case R.id.button:	
////////////////////////////////��绰begin
				SharedPreferences references5 = getSharedPreferences("account", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_WRITEABLE);
				String phnoenum5 = references5.getString("PropertyPhnoe", "");
				//��intent��������绰  
                Intent intent5 = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phnoenum5)); 
				startActivity(intent5);
				Toast.makeText(ControlChoice.this, "����״̬��ѯ��...", Toast.LENGTH_LONG).show();
//				try {
//		            Thread.sleep(2000);
//		        } catch (InterruptedException e) {
//		            e.printStackTrace(); 
//		        }
				//Roominfo.setText(smmstoedit());
				break;
//////////////////////////////// ��绰end
/////////////////////////////////���Ͷ���begin				
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
//					Toast.makeText(ControlChoice.this, "ע��ʱδ���뻤����ʹ�绰��������ע�ᣡ", Toast.LENGTH_LONG).show();
//				}
//				break;
//////////////////////////���Ͷ���end
			case R.id.button10:	
				SharedPreferences references10 = getSharedPreferences("account", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_WRITEABLE);
				String phnoenum10 = references10.getString("PropertyPhnoe", "");
				Toast.makeText(ControlChoice.this, "��ȡ������...", Toast.LENGTH_LONG).show();
				Roominfo.setText(smmstoedit());
				break;
			case R.id.button11:	
				SharedPreferences references11 = getSharedPreferences("account", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_WRITEABLE);
				String phnoenum11 = references11.getString("PropertyPhnoe", "");
				Toast.makeText(ControlChoice.this, "��ȡ������...", Toast.LENGTH_LONG).show();
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
