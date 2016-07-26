/* 2014年8月26号
 * 1 增加src/java文件
 * 2 增加layout布局文件 ，修改AndroidManifest.xml中哪个做为第一个LayoutMainActivity类
 * 3 增加Log.d调试信息过滤功能
 * 4 增加Layout布局Lineralayout
 */


package com.dream.layout_demo;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class LayoutMainActivity extends Activity
{
	private static final String LOG_TAG = "Layout_intent_msg";
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout);
		Log.d(LOG_TAG, "hello layout!!!");
	}
}