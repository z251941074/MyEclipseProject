package com.activity.test;

import java.io.InputStream;
import java.io.ObjectInputStream;

import com.activity.test.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class Login3 extends Activity {
	
	public EditText phonenumber,phoneEditText;
	public TextView tipTextView;//提示框
	private Button button;
	public String phoneNumberValue;
	private SharedPreferences sp;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_login);
		phoneEditText= (EditText) findViewById(R.id.editText1);
		String phone = phoneEditText.getText().toString().trim();
		sp = this.getSharedPreferences("userInfo", Login3.MODE_WORLD_READABLE);
		phonenumber = (EditText) findViewById(R.id.editText1);
		phonenumber.setText(sp.getString("PHONE_NUMBER", "moren"));
  
		
		tipTextView = (TextView)findViewById(R.id.textView1);
		this.button = (Button) this.findViewById(R.id.button_login);
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
				Intent intent = new Intent();  
                intent.setClass(Login3.this, MainActivity1.class);  
                startActivity(intent);  
					}
											}
    			});
	
		this.button = (Button) this.findViewById(R.id.button_baocun);
		this.button.setOnClickListener(new View.OnClickListener(){  
  
			@Override
			public void onClick(View v) {

				int a=phoneEditText.length();
				if(a!=11){
					phoneEditText.setText("");
					Toast.makeText(getApplicationContext(), "输入电话号码格式不正确，请重新输入",
						     Toast.LENGTH_SHORT).show();;
				}
				else{
			  Toast.makeText(getApplicationContext(), "号码保存成功",
					     Toast.LENGTH_SHORT).show();;
				phoneNumberValue = phonenumber.getText().toString();
				//
				SharedPreferences.Editor editor = sp.edit();  
	            editor.putString("PHONE_NUMBER", phoneNumberValue);  
	            editor.commit(); 
				}
			}
    			});
		
		
		
	}
	


		

				
	}
	
	
	
