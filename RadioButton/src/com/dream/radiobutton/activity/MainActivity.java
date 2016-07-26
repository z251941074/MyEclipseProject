package com.dream.radiobutton.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends Activity {

	private RadioGroup rgObj;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rgObj = (RadioGroup)findViewById(R.id.RadiogroupId);
		
		OnCheckedChangeListener setOnCheckedChangeListener = new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch(checkedId)
				{
				case R.id.appleId:
					Toast.makeText(MainActivity.this, "apple", Toast.LENGTH_SHORT).show();
					break;
				case R.id.googleId:
					Toast.makeText(MainActivity.this, "google", Toast.LENGTH_SHORT).show();
					break;
				case R.id.microsoftId:
					Toast.makeText(MainActivity.this, "microsoft", Toast.LENGTH_SHORT).show();
					break;
				}
			}	
		};
		rgObj.setOnCheckedChangeListener(setOnCheckedChangeListener);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
