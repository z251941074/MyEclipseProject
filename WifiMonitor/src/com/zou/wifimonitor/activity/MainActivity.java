package com.zou.wifimonitor.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity
{
	private WifiManager wifiManager = null;
	private WifiInfo mWifiInfo;
	private EditText usrnameEt;
	private EditText passwEt;
	private CheckBox checkidinfo;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);

		usrnameEt = (EditText) findViewById(R.id.usrname);
		passwEt = (EditText) findViewById(R.id.passw);
		checkidinfo = (CheckBox) findViewById(R.id.checkid);

		LoadUserData();

		// ��¼���п���
		Button loginbtn = (Button) findViewById(R.id.login);
		loginbtn.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				// �õ�LocationManager����
				wifiManager = (WifiManager) MainActivity.this.getSystemService(Context.WIFI_SERVICE);
				mWifiInfo = wifiManager.getConnectionInfo();

				String usrname = usrnameEt.getText().toString();
				String password = passwEt.getText().toString();
				SharedPreferences references = getSharedPreferences("account", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_WRITEABLE);
				String name = references.getString("username", "");
				String psw = references.getString("password", "");

				// ��¼�����û�����
				SaveUserData();

				// ���ж�wifi�Ƿ�����
				if (mWifiInfo != null)
				{

					if (name.equals(usrname) && psw.equals(password) && !name.equals("") && !psw.equals(""))
					{
						Intent intent = new Intent(MainActivity.this, ControlChoice.class);
						startActivity(intent);
						Toast.makeText(MainActivity.this, "��¼�ɹ�", Toast.LENGTH_LONG).show();
					}

					else
					{
						Toast.makeText(MainActivity.this, "��������ȷ���û���������", Toast.LENGTH_LONG).show();
					}
				} else
				{
					Toast.makeText(MainActivity.this, "wifiδ����", Toast.LENGTH_LONG).show();

				}
			}
		});
		// ע���û�
		Button registerbtn = (Button) findViewById(R.id.register);
		registerbtn.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{
				Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});

		// �˳���¼
		Button extibtn = (Button) findViewById(R.id.exit);
		extibtn.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{
				System.exit(0);
			}
		});

	}

	/**
	 * �����Ѽ�ס�û���Ϣ
	 */
	private void LoadUserData()
	{
		SharedPreferences sp = getSharedPreferences("MyUserInfo", 0);
		if (sp.getBoolean("isSave", false))
		{
			String userName = sp.getString("userName", "");
			String userPassword = sp.getString("userPassword", "");
			if (!("".equals(userName) && "".equals(userPassword)))
			{
				usrnameEt.setText(userName);
				passwEt.setText(userPassword);
				checkidinfo.setChecked(true);
			}
		}
	}

	/**
	 * �����û���Ϣ
	 */
	private void SaveUserData()
	{
		// ���������ļ�
		SharedPreferences logininfo = getSharedPreferences("MyUserInfo", 0);
		// д�������ļ�
		Editor logininfoEd = logininfo.edit();
		if (checkidinfo.isChecked())
		{
			logininfoEd.putBoolean("isSave", true);
			logininfoEd.putString("userName", usrnameEt.getText().toString());
			logininfoEd.putString("userPassword", passwEt.getText().toString());
		} else
		{
			logininfoEd.putBoolean("isSave", false);
			logininfoEd.putString("userName", "");
			logininfoEd.putString("userPassword", "");
		}
		logininfoEd.commit();
	}
}
