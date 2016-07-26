package com.dream.ballevent.activity.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class BallView extends View
{
	public float x = 30.00f;
	public float y = 30.00f;
	
	public BallView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void onDraw(Canvas canvas)
	{
		Paint paintObj = new Paint();
		paintObj.setColor(Color.RED);
		canvas.drawCircle(x, y, 10.00f, paintObj);		
		
	}
	
}
