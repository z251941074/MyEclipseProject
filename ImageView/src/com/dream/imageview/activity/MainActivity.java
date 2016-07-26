package com.dream.imageview.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	int resArrays[] = new int[]{
			R.drawable.lijiang,
			R.drawable.qiao,
			R.drawable.shuangta,
			R.drawable.shui,
			R.drawable.xiangbi,
		};
	private Button btnObj;
	private ImageView ivObj;
	private int currentPos = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnObj = (Button)findViewById(R.id.nextButton);
		ivObj = (ImageView)findViewById(R.id.ivId);
	/*	
		OnClickListener setOnClickListener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ivObj.setImageResource(resArrays[currentPos]);
				currentPos++;
				if(currentPos==5)
				{
					currentPos = 0;
				}
			}

		};
		btnObj.setOnClickListener(setOnClickListener);
		*/
        btnObj.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ivObj.setImageResource(resArrays[currentPos%5]);
				currentPos++;
			}
		});
	}

		


}
