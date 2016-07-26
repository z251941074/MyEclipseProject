package com.activity.test;

import com.activity.test.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class yz extends Activity {
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_yz);
		SharedPreferences sp41= this.getSharedPreferences("userInfo3", yz.MODE_WORLD_READABLE);
		SharedPreferences.Editor editor = sp41.edit();  
        editor.putString("i","1");  
        editor.commit();
}


	public void btyz1onclick(View view)
    {  
		SharedPreferences sp41= this.getSharedPreferences("userInfo3", yz.MODE_WORLD_READABLE);
		SharedPreferences.Editor editor = sp41.edit();  
		double g=9.8;
		///i的值随设置改变， 
		String I="1";
		editor.clear();
        editor.putString("i",I );  
        editor.commit();
        Toast.makeText(getApplicationContext(), "选择比同龄人快一点",
			     Toast.LENGTH_SHORT).show();;
    }

	public void btyz2onclick(View view)
    {  
		SharedPreferences sp41= this.getSharedPreferences("userInfo3", yz.MODE_WORLD_READABLE);
		SharedPreferences.Editor editor = sp41.edit();  
		String I="2";
		editor.clear();
        editor.putString("i",I );  
        editor.commit();
        Toast.makeText(getApplicationContext(), "选择与同龄人差不多",
			     Toast.LENGTH_SHORT).show();;
    }
	
	public void btyz3onclick(View view)
    {  
		SharedPreferences sp41= this.getSharedPreferences("userInfo3", yz.MODE_WORLD_READABLE);
		SharedPreferences.Editor editor = sp41.edit();  
		String I="3";
		editor.clear();
        editor.putString("i",I );  
        editor.commit();
        Toast.makeText(getApplicationContext(), "选择比同龄人慢一点",
			     Toast.LENGTH_SHORT).show();;
    }
	public void btyz4onclick(View view)
    {  
		Intent intent = new Intent(getApplicationContext(),setting.class);
		startActivity(intent);
		} 
 
}