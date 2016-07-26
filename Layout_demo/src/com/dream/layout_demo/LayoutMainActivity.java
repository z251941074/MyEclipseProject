/* 2014��8��26��
 * 1 ����src/java�ļ�
 * 2 ����layout�����ļ� ���޸�AndroidManifest.xml���ĸ���Ϊ��һ��LayoutMainActivity��
 * 3 ����Log.d������Ϣ���˹���
 * 4 ����Layout����Lineralayout
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