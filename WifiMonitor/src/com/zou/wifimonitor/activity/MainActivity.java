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

		// 登录进行控制
		Button loginbtn = (Button) findViewById(R.id.login);
		loginbtn.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				// 得到LocationManager对象
				wifiManager = (WifiManager) MainActivity.this.getSystemService(Context.WIFI_SERVICE);
				mWifiInfo = wifiManager.getConnectionInfo();

				String usrname = usrnameEt.getText().toString();
				String password = passwEt.getText().toString();
				SharedPreferences references = getSharedPreferences("account", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_WRITEABLE);
				String name = references.getString("username", "");
				String psw = references.getString("password", "");

				// 登录保存用户数据
				SaveUserData();

				// 先判断wifi是否连接
				if (mWifiInfo != null)
				{

					if (name.equals(usrname) && psw.equals(password) && !name.equals("") && !psw.equals(""))
					{
						Intent intent = new Intent(MainActivity.this, ControlChoice.class);
						startActivity(intent);
						Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_LONG).show();
					}

					else
					{
						Toast.makeText(MainActivity.this, "请输入正确的用户名和密码", Toast.LENGTH_LONG).show();
					}
				} else
				{
					Toast.makeText(MainActivity.this, "wifi未连接", Toast.LENGTH_LONG).show();

				}
			}
		});
		// 注册用户
		Button registerbtn = (Button) findViewById(R.id.register);
		registerbtn.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{
				Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});

		// 退出登录
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
	 * 载入已记住用户信息
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
	 * 保存用户信息
	 */
	private void SaveUserData()
	{
		// 载入配置文件
		SharedPreferences logininfo = getSharedPreferences("MyUserInfo", 0);
		// 写入配置文件
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
