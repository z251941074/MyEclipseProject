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
		//数据传递
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
					Toast.makeText(getApplicationContext(), "输入电话号码格式不正确，请重新输入",
						     Toast.LENGTH_SHORT).show();;
				}
				else{

					Toast.makeText(getApplicationContext(), "号码修改成功",
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
					//要执行的代码,跳转到设置界面
					Intent intent = new Intent(getApplicationContext(),setting.class);
					//开始执行
					startActivity(intent);
					Toast.makeText(getApplicationContext(), "取消号码修改",
						     Toast.LENGTH_SHORT).show();;
					
					}
				}, 500);//1000毫秒=1秒
			}
	
		});
		
		

	
}

public void running(){
	//传递数据phone
	//获取到输入的电话号码
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
//30秒后跳转到主程序界面
	Handler handler = new Handler();
	handler.postDelayed(new Runnable(){
	public void run(){
		//要执行的代码,跳转到主程序界面
		Intent intent = new Intent(getApplicationContext(),setting.class);
		//开始执行
		startActivity(intent);
		}
	}, 500);//1000毫秒=1秒
}
}


