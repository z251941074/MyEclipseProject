/* **
 ******************************************************************************
 * @file    ControlChoice.java
 * @author  zou
 * @version V1.0
 * @date    2013/04/12
 * @brief   上位机控制界面函数
 ******************************************************************************
 * @copy
 *
 * The file is created by Zou Bo
 */

package com.zou.wifimonitor.activity;

/* Includes ------------------------------------------------------------------*/
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ControlChoice extends Activity
{
	private InetSocketAddress isconnect;
	private String IpAddress;
	private EditText IpEdit;
	private Button IpBtn;
	private EditText NetState;

	private Button OpenWindow;
	private Button CloseWindow;
	private Button CloseGas;
	private Button SendMMS;
	private Button CheckStat;

	private EditText OderEdit;
	private Button SendBtn;

	private String str1 = null;
	private String str2 = null;

	private EditText Roomifno;

	private Thread mThreadClient = null;
	private Thread delayThread = null;
	private Socket mSocketClient = null;
	static PrintWriter mPrintWriterClient = null;
	static BufferedReader mBufferedReaderClient = null;
	private boolean isConnecting = false;
	private boolean btnConnect = false;
	private WifiManager wifiManager = null;
	private WifiInfo mWifiInfo = null;

	private Handler mHandler = null;
	private String buffServerStr = null;
	private Handler delayHandler;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.control);

		IpEdit = (EditText) findViewById(R.id.IpEdit);
		IpBtn = (Button) findViewById(R.id.IpBtn);
		NetState = (EditText) findViewById(R.id.NetState);

		OpenWindow = (Button) findViewById(R.id.OpenWin);
		CloseWindow = (Button) findViewById(R.id.CloseWin);
		CloseGas = (Button) findViewById(R.id.CloseGas);
		SendBtn = (Button) findViewById(R.id.SendBtn);

		SendMMS = (Button) findViewById(R.id.SendMMS);
		// CheckStat = (Button) findViewById(R.id.CheckStat);

		OderEdit = (EditText) findViewById(R.id.OderEdit);

		Roomifno = (EditText) findViewById(R.id.Roomifno);

		OpenWindow.setOnClickListener(listener);
		CloseWindow.setOnClickListener(listener);
		CloseGas.setOnClickListener(listener);
		SendBtn.setOnClickListener(listener);
		SendMMS.setOnClickListener(listener);

		// /<连接到指定的IP
		IpBtn.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				// /<获得输入的IP
				IpAddress = IpEdit.getText().toString();

				wifiManager = (WifiManager) ControlChoice.this.getSystemService(Context.WIFI_SERVICE);
				mWifiInfo = wifiManager.getConnectionInfo();
				// /<如果连接，则按钮变为"断开"，否则保持连接
				if (btnConnect == false)
				{
					if (mWifiInfo != null)
					{
						if (IpAddress.length() > 0)
						{
							mThreadClient = new Thread(mRunnable);
							mThreadClient.start();
						} else
						{
							// NetState.setText("请输入IP地址");
							Toast.makeText(ControlChoice.this, "请输入Ip地址！", Toast.LENGTH_LONG).show();
						}
					} else
					{
						Toast.makeText(ControlChoice.this, "wifi未连接", 0).show();
					}
				} else
				{
					IpBtn.setText("连接");
					IpEdit.setEnabled(true);
					NetState.setText("未连接到服务器");

					btnConnect = false;

					mPrintWriterClient.println("CLOSE");
					mPrintWriterClient.flush();
					onDestroy();
				}

			}
		});

		// /<对室内状态进行相应的控制
		// /<1、 开窗
		// OpenWindow.setOnClickListener(new View.OnClickListener()
		// {
		//
		// public void onClick(View v)
		// {
		// mThreadClient = new Thread(mRunnable);
		// mThreadClient.start();
		//
		// if (isConnecting)
		// {
		// mPrintWriterClient.println("ONWIN");
		// mPrintWriterClient.flush();
		//
		// Toast.makeText(ControlChoice.this, "打开窗户成功", 0).show();
		//
		// } else
		// {
		// Toast.makeText(ControlChoice.this, "未连接到服务器", 0).show();
		// }
		// }
		// });

		// /<2、 关窗
		// CloseWindow.setOnClickListener(new View.OnClickListener()
		// {
		// public void onClick(View v)
		// {
		// mThreadClient = new Thread(mRunnable);
		// mThreadClient.start();
		//
		// if (isConnecting)
		// {
		// mPrintWriterClient.println("OFFWIN");
		// mPrintWriterClient.flush();
		//
		// Toast.makeText(ControlChoice.this, "关闭窗户成功", 0).show();
		//
		// } else
		// {
		// Toast.makeText(ControlChoice.this, "未连接到服务器", 0).show();
		// }
		//
		// }
		// });

		// /<3、 关闭煤气阀门
		// CloseGas.setOnClickListener(new View.OnClickListener()
		// {
		//
		// public void onClick(View v)
		// {
		// mThreadClient = new Thread(mRunnable);
		// mThreadClient.start();
		//
		// if (isConnecting)
		// {
		// mPrintWriterClient.println("OFFVAL");
		// mPrintWriterClient.flush();
		//
		// Toast.makeText(ControlChoice.this, "关闭煤气阀门成功", 0).show();
		//
		// } else
		// {
		// Toast.makeText(ControlChoice.this, "未连接到服务器", 0).show();
		// }
		// }
		// });

		// /<发送短信通知物业
		// SendMMS.setOnClickListener(new View.OnClickListener()
		// {
		// public void onClick(View v)
		// {
		// String string = "室内有害气体超标，请求物业前往处理！";
		//
		// SharedPreferences references = getSharedPreferences("account",
		// Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE +
		// Context.MODE_WORLD_WRITEABLE);
		// String phnoenum = references.getString("PropertyPhnoe", "");
		//
		// if (!phnoenum.equals(""))
		// {
		// sendSMS(phnoenum, string);
		// } else
		// {
		// Toast.makeText(ControlChoice.this, "注册时未输入物业电话，请重新注册！",
		// Toast.LENGTH_LONG).show();
		// }
		//
		// }
		// });

		// /<发送相应的控制命令
		// SendBtn.setOnClickListener(new View.OnClickListener()
		// {
		// // String oderString = null;
		//
		// public void onClick(View v)
		// {
		// mThreadClient = new Thread(mRunnable);
		// mThreadClient.start();
		// String oderString = null;
		// oderString = OderEdit.getText().toString();
		//
		// if (isConnecting && (oderString.length() > 0))
		// {
		// mPrintWriterClient.println(oderString);
		// mPrintWriterClient.flush();
		// Toast.makeText(ControlChoice.this, "发送成功", 0).show();
		// } else
		// {
		// Toast.makeText(ControlChoice.this, "请输入控制命令",
		// Toast.LENGTH_LONG).show();
		// }
		// }
		// });
		Timer timer = new Timer();
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				this.cancel();
			}
		}, 500);

		// /<消息处理函数
		mHandler = new Handler()
		{

			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				// /<成功连接服务器
				case 1:
				{
					if (mSocketClient.isConnected())
					{
						isConnecting = true;

						btnConnect = true;
						IpBtn.setText("断开");
						IpEdit.setEnabled(false);
						NetState.setText("已经连接到服务器");
						Toast.makeText(ControlChoice.this, "连接成功！", 0).show();

						// /<定时查询线程
						delayThread = new Thread(delayRunnable);
						delayThread.start();
					}
					break;
				}
				// /<没有连接到服务器
				case 2:
				{
					NetState.setText("未连接到服务器");
					Toast.makeText(ControlChoice.this, "连接超时，未连接到服务器！", 0).show();
					Roomifno.setText("");
					break;
				}
				// /<收到服务器的消息
				case 3:
				{
					if (!buffServerStr.equals(""))
					{
						if (((msg.obj).toString()).equals("Sucled"))
						{
							Toast.makeText(ControlChoice.this, "开窗、关窗或关煤气成功", 0).show();

						} else if (((msg.obj).toString()).equals("Failled"))
						{
							Roomifno.setText("开窗、关窗或关煤气失败");
						} else if (((msg.obj).toString()).equals("FailAskTem")) // /<温度查询失败
						{
							Roomifno.setText("温度数据读取失败");
						} else if (((msg.obj).toString()).equals("FailAskGas")) // /<煤气查询失败
						{
							Roomifno.setText("煤气数据读取失败");
						} else
						{
							if (buffServerStr.substring(0, 3).equals("TEM"))
							{
								str1 = buffServerStr.substring(3);
							} else if (buffServerStr.substring(0, 3).equals("GAS"))
							{
								str2 = buffServerStr.substring(3);
							}
							Roomifno.setText("室内温度为：" + str1 + "度\n" + "室内有害气体为：" + str2 + "\n");
						}
					} else
					{
						Roomifno.setText("未读取到服务器的消息！"); // /<需要修改
					}
					break;
				}
				// /<未收到服务器的消息
				case 4:
				{
					Roomifno.setText("未读取到室内服务器的信息");
					break;
				}
				}
			}

		};

		// /<延时消息处理，实现定时查询数据
		delayHandler = new Handler()
		{

			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case 1:
				{
					// /<连接成功后，查询温度和气体含量
					mPrintWriterClient.println("TMPASK");
					mPrintWriterClient.flush();
					break;
				}
				case 2:
				{
					// /<连接成功后，查询温度和气体含量
					mPrintWriterClient.println("GASASK");
					mPrintWriterClient.flush();
					break;
				}
				default:
					break;
				}
			}

		};

	}

	/* *******事件监听 ******** */
	private OnClickListener listener = new OnClickListener()
	{

		public void onClick(View v)
		{
			Button btn = (Button) v;
			if (isConnecting)
			{
				switch (btn.getId())
				{
				// /<发送开窗命令
				case R.id.OpenWin:
				{
					mPrintWriterClient.println("ONWIN");
					mPrintWriterClient.flush();

					break;
				}
				// /<发送关窗命令
				case R.id.CloseWin:
				{
					mPrintWriterClient.println("OFFWIN");
					mPrintWriterClient.flush();
					break;
				}
				// /<发送关煤气阀命令
				case R.id.CloseGas:
				{
					mPrintWriterClient.println("OFFVAL");
					mPrintWriterClient.flush();
					break;
				}
				// /<发送相应的控制命令
				case R.id.SendBtn:
				{
					String oderString = null;
					oderString = OderEdit.getText().toString();

					if (isConnecting && (oderString.length() > 0))
					{
						mPrintWriterClient.println(oderString);
						mPrintWriterClient.flush();
						Toast.makeText(ControlChoice.this, "发送成功", 0).show();
					} else
					{
						Toast.makeText(ControlChoice.this, "请输入控制命令", Toast.LENGTH_LONG).show();
					}
					break;
				}
				// /<发送短信通知物业处理
				case R.id.SendMMS:
				{
					String MMSstr = "室内有害气体超标，请求物业前往处理！";

					SharedPreferences references = getSharedPreferences("account", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_WRITEABLE);
					String phnoenum = references.getString("PropertyPhnoe", "");

					if (!phnoenum.equals(""))
					{
						sendSMS(phnoenum, MMSstr);
					} else
					{
						Toast.makeText(ControlChoice.this, "注册时未输入物业电话，请重新注册！", Toast.LENGTH_LONG).show();
					}
					break;
				}
				}

			} else
			{
				Toast.makeText(ControlChoice.this, "未连接到服务器，请先连接！", Toast.LENGTH_LONG).show();
			}
		}
	};

	/* ***发送短信的控制函数*** */
	private void sendSMS(String phoneNumber, String message)
	{
		// 得到SMS的管理类
		SmsManager smsManager = SmsManager.getDefault();
		PendingIntent paIntent = PendingIntent.getBroadcast(ControlChoice.this, 0, new Intent("SMS_SENT"), 0);

		registerReceiver(new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{
				int resultCode = getResultCode();

				switch (resultCode)
				{
				case Activity.RESULT_OK:
					Toast.makeText(ControlChoice.this, "已经通知物业", Toast.LENGTH_LONG).show();
					break;

				default:
					Toast.makeText(ControlChoice.this, "通知物业失败", Toast.LENGTH_LONG).show();
				}
			}
		}, new IntentFilter("SMS_SENT"));

		smsManager.sendTextMessage(phoneNumber, null, message, paIntent, null);
	}

	// /<延时线程,实现不断的查询数据
	private Runnable delayRunnable = new Runnable()
	{

		public void run()
		{
			try
			{
				while (btnConnect)
				{
					Thread.sleep(5000);
					Message delaymsg;
					delaymsg = new Message();
					delaymsg.what = 1;
					delayHandler.sendMessage(delaymsg);

					Thread.sleep(5000);
					delaymsg = new Message();
					delaymsg.what = 2;
					delayHandler.sendMessage(delaymsg);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	};

	/* ****连接服务器的线程，用于接收和发送网络数据**** */
	private Runnable mRunnable = new Runnable()
	{

		public void run()
		{
			try
			{
				// /< 创建Socket
				// mSocketClient = new Socket(IpAddress, 8888);
				mSocketClient = new Socket();
				isconnect = new InetSocketAddress(IpAddress, 8888);
				mSocketClient.connect(isconnect, 5000); // /<实现5秒的连接重试

				// /<成功连接服务器消息处理
				Message msg = new Message();
				msg.what = 1;
				mHandler.sendMessage(msg);

				while (true)
				{
					try
					{
						// mSocketClient.setKeepAlive(true);///<保持长连接
						// /< 通过网络向服务器端发送消息
						mPrintWriterClient = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocketClient.getOutputStream())), true);

						// /< 接收来自服务器端的消息
						mBufferedReaderClient = new BufferedReader(new InputStreamReader(mSocketClient.getInputStream()));
						buffServerStr = mBufferedReaderClient.readLine();

						// /<获取到服务器的信息，消息处理
						msg = new Message();
						msg.what = 3;
						msg.obj = buffServerStr;
						mHandler.sendMessage(msg);
					} catch (Exception e)
					{
						// /<获取到服务器的信息，消息处理
						msg = new Message();
						msg.what = 4;
						mHandler.sendMessage(msg);
						e.printStackTrace();
					}
				}

			} catch (Exception e)
			{
				// /<未成功连接服务器消息处理
				Message msg = new Message();
				msg.what = 2;
				mHandler.sendMessage(msg);
				e.printStackTrace();
			} finally
			{
				try
				{
					if (mPrintWriterClient != null)
					{
						mPrintWriterClient.close();
					}
					if (mBufferedReaderClient != null)
					{
						mBufferedReaderClient.close();

					}
					if (mSocketClient != null)
					{
						mSocketClient.close();
					}
				} catch (Exception e)
				{
					// TODO: handle exception
				}
			}
		}
	};

	public void onDestroy()
	{

		if (isConnecting)
		{
			isConnecting = false;
			try
			{
				if (mSocketClient != null)
				{
					// mSocketClient.shutdownInput();
					// mSocketClient.shutdownOutput();

					// mSocketClient.close();
					// mSocketClient = null;

					mPrintWriterClient.close();
					mPrintWriterClient = null;
					// mBufferedReaderClient.close();
					// mBufferedReaderClient = null;
				}
				mSocketClient.close();
				mSocketClient = null;

			} catch (IOException e)
			{
				e.printStackTrace();
			}
			mThreadClient.interrupt();
			delayThread.interrupt();
		}
		super.onDestroy();
	}

}
