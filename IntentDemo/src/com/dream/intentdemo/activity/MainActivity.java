package com.dream.intentdemo.activity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button btnObj;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnObj = (Button)findViewById(R.id.btn01);
		OnClickListener OnClickListener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int a=6;
				int b=7;
				int c=8;
				
				Intent intentObj = new Intent(MainActivity.this, SecondActivity.class);
				intentObj.putExtra("a", a);
				intentObj.putExtra("b", b);
				intentObj.putExtra("c", c);
				startActivity(intentObj);
			}};
		btnObj.setOnClickListener(OnClickListener);
		
	}

}
