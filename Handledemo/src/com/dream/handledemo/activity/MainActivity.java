package com.dream.handledemo.activity;

import java.security.PublicKey;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Video;
import android.R.integer;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity
{
	private TextView tvObjTextView = null;
	private Handler handlerObj = null;
	private int current = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvObjTextView = (TextView) findViewById(R.id.tvId);
		/* 主线程不能被阻塞 */
		/*
		 * for(int index=0; index<=100;index++) {
		 * tvObjTextView.setText(String.valueOf(index)); try {
		 * Thread.sleep(1000); } catch (Exception e) { // TODO: handle exception
		 * e.printStackTrace(); } }
		 */
		/*接受消息*/
		handlerObj = new Handler()
		{
			public void handleMessage(Message msg)
			{
				tvObjTextView.setText(String.valueOf(current));
				current++;
			}
			
		};
		
		/*发送消息*/
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				while (true)
				{
					Message msg = new Message();
					handlerObj.sendMessage(msg);

					try
					{
						Thread.sleep(1000);
					} catch (Exception e)
					{
						// TODO: handle exception
						e.printStackTrace();
					}

				}

			}
		}).start();

	}

}
