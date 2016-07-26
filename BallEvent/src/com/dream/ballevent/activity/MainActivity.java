package com.dream.ballevent.activity;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.dream.ballevent.activity.view.BallView;

public class MainActivity extends Activity {

	private  LinearLayout LlObj = null; 
	private  BallView ballviewobj = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		LlObj = (LinearLayout)findViewById(R.id.Layout);
		
		ballviewobj = new BallView(this);
		
		LlObj.addView(ballviewobj);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	/*回调事件*/
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		ballviewobj.x = event.getX();
		ballviewobj.y = event.getY();
		ballviewobj.invalidate();
		return super.onTouchEvent(event);
	}
	
}
