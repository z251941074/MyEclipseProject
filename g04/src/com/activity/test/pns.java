package com.activity.test;

import com.activity.test.R;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class pns extends Activity {
	Button button;
	public String phone;
	public EditText phoneEditText;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_pns);
		//���ݴ���
		//phonenumber= (EditText) findViewById(R.id.editText1);
		SharedPreferences sp01 =getSharedPreferences("userInfo",pns.MODE_PRIVATE);
		phoneEditText = (EditText) findViewById(R.id.phoneEditText);
		phoneEditText.setText(sp01.getString("PHONE_NUMBER", "moren"));
		
		//login(View view);	
		
		this.button = (Button) this.findViewById(R.id.button_genggai);
		this.button.setOnClickListener(new View.OnClickListener(){  
  
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Editable text=phoneEditText.getText();
				int a=phoneEditText.length();
				if(a!=11){
					phoneEditText.setText("");
					Toast.makeText(getApplicationContext(), "����绰�����ʽ����ȷ������������",
						     Toast.LENGTH_SHORT).show();;
				}
				else{

					Toast.makeText(getApplicationContext(), "�����޸ĳɹ�",
						     Toast.LENGTH_SHORT).show();;
						     running();		     
				}
					   
					 				}
    			});

		this.button = (Button) this.findViewById(R.id.button_quxiao);
		this.button.setOnClickListener(new View.OnClickListener(){ 

			public void onClick(View v) {
				Handler handler = new Handler();
				handler.postDelayed(new Runnable(){
				public void run(){
					//Ҫִ�еĴ���,��ת�����ý���
					Intent intent = new Intent(getApplicationContext(),setting.class);
					//��ʼִ��
					startActivity(intent);
					Toast.makeText(getApplicationContext(), "ȡ�������޸�",
						     Toast.LENGTH_SHORT).show();;
					
					}
				}, 500);//1000����=1��
			}
	
		});
		
		

	
}

public void running(){
	//��������phone
	//��ȡ������ĵ绰����
	//String phone =phoneEditText.getText().toString().trim();
	//if(phone!=null){
		//SharedPreferences.Editor editor = sp3.edit();
		//	editor.clear();
		//	editor.putString("PHONE_NUMBER", phone);
		//	editor.commit();
	SharedPreferences sp01 =getSharedPreferences("userInfo",pns.MODE_PRIVATE);
	String phoneNumberValue = phoneEditText.getText().toString();
	SharedPreferences.Editor editor = sp01.edit();
	editor.clear();
	editor.putString("PHONE_NUMBER", phoneNumberValue);
	editor.commit();
//30�����ת�����������
	Handler handler = new Handler();
	handler.postDelayed(new Runnable(){
	public void run(){
		//Ҫִ�еĴ���,��ת�����������
		Intent intent = new Intent(getApplicationContext(),setting.class);
		//��ʼִ��
		startActivity(intent);
		}
	}, 500);//1000����=1��
}
}


