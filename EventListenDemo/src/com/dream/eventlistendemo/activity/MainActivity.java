package com.dream.eventlistendemo.activity;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity 
{

	private Button  btnObj = null;
	private final static String TAG = "TouchEvent";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/*获取事件源*/
		btnObj = (Button)findViewById(R.id.btn1);
		/*注册监听器到事件源*/
		
		/*匿名内部类注册监听器*/
		btnObj.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "you clicked me!!", Toast.LENGTH_SHORT).show();
			}
		});
//		btnObj.setOnClickListener(btnListener);		
	}
/*	View.OnClickListener btnListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Toast.makeText(MainActivity.this, "you clicked me!", Toast.LENGTH_SHORT).show();
		}
	}; 
*/	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.d(TAG, "x=" + event.getX() + "y=" + event.getY());
		return super.onTouchEvent(event);
	}
	

}
