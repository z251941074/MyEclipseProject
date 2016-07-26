package com.dream.intentdemo.activity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SecondActivity extends Activity {

	private Button btnObj;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		
		btnObj = (Button)findViewById(R.id.btn02);
		OnClickListener OnClickListener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentObj = new Intent(SecondActivity.this, MainActivity.class);
				Intent intentGet = getIntent();
				int a=intentGet.getIntExtra("a", 0);
				int b=intentGet.getIntExtra("a", 0);
				int c=intentGet.getIntExtra("c", 0);
				Log.i("SecondActivity", "a="+a);
				//startActivity(intentObj);
			}};
		btnObj.setOnClickListener(OnClickListener);
	}
	

}
