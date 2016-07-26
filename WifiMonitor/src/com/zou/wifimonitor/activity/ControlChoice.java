/* **
 ******************************************************************************
 * @file    ControlChoice.java
 * @author  zou
 * @version V1.0
 * @date    2013/04/12
 * @brief   ��λ�����ƽ��溯��
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

		// /<���ӵ�ָ����IP
		IpBtn.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				// /<��������IP
				IpAddress = IpEdit.getText().toString();

				wifiManager = (WifiManager) ControlChoice.this.getSystemService(Context.WIFI_SERVICE);
				mWifiInfo = wifiManager.getConnectionInfo();
				// /<������ӣ���ť��Ϊ"�Ͽ�"�����򱣳�����
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
							// NetState.setText("������IP��ַ");
							Toast.makeText(ControlChoice.this, "������Ip��ַ��", Toast.LENGTH_LONG).show();
						}
					} else
					{
						Toast.makeText(ControlChoice.this, "wifiδ����", 0).show();
					}
				} else
				{
					IpBtn.setText("����");
					IpEdit.setEnabled(true);
					NetState.setText("δ���ӵ�������");

					btnConnect = false;

					mPrintWriterClient.println("CLOSE");
					mPrintWriterClient.flush();
					onDestroy();
				}

			}
		});

		// /<������״̬������Ӧ�Ŀ���
		// /<1�� ����
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
		// Toast.makeText(ControlChoice.this, "�򿪴����ɹ�", 0).show();
		//
		// } else
		// {
		// Toast.makeText(ControlChoice.this, "δ���ӵ�������", 0).show();
		// }
		// }
		// });

		// /<2�� �ش�
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
		// Toast.makeText(ControlChoice.this, "�رմ����ɹ�", 0).show();
		//
		// } else
		// {
		// Toast.makeText(ControlChoice.this, "δ���ӵ�������", 0).show();
		// }
		//
		// }
		// });

		// /<3�� �ر�ú������
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
		// Toast.makeText(ControlChoice.this, "�ر�ú�����ųɹ�", 0).show();
		//
		// } else
		// {
		// Toast.makeText(ControlChoice.this, "δ���ӵ�������", 0).show();
		// }
		// }
		// });

		// /<���Ͷ���֪ͨ��ҵ
		// SendMMS.setOnClickListener(new View.OnClickListener()
		// {
		// public void onClick(View v)
		// {
		// String string = "�����к����峬�꣬������ҵǰ������";
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
		// Toast.makeText(ControlChoice.this, "ע��ʱδ������ҵ�绰��������ע�ᣡ",
		// Toast.LENGTH_LONG).show();
		// }
		//
		// }
		// });

		// /<������Ӧ�Ŀ�������
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
		// Toast.makeText(ControlChoice.this, "���ͳɹ�", 0).show();
		// } else
		// {
		// Toast.makeText(ControlChoice.this, "�������������",
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

		// /<��Ϣ������
		mHandler = new Handler()
		{

			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				// /<�ɹ����ӷ�����
				case 1:
				{
					if (mSocketClient.isConnected())
					{
						isConnecting = true;

						btnConnect = true;
						IpBtn.setText("�Ͽ�");
						IpEdit.setEnabled(false);
						NetState.setText("�Ѿ����ӵ�������");
						Toast.makeText(ControlChoice.this, "���ӳɹ���", 0).show();

						// /<��ʱ��ѯ�߳�
						delayThread = new Thread(delayRunnable);
						delayThread.start();
					}
					break;
				}
				// /<û�����ӵ�������
				case 2:
				{
					NetState.setText("δ���ӵ�������");
					Toast.makeText(ControlChoice.this, "���ӳ�ʱ��δ���ӵ���������", 0).show();
					Roomifno.setText("");
					break;
				}
				// /<�յ�����������Ϣ
				case 3:
				{
					if (!buffServerStr.equals(""))
					{
						if (((msg.obj).toString()).equals("Sucled"))
						{
							Toast.makeText(ControlChoice.this, "�������ش����ú���ɹ�", 0).show();

						} else if (((msg.obj).toString()).equals("Failled"))
						{
							Roomifno.setText("�������ش����ú��ʧ��");
						} else if (((msg.obj).toString()).equals("FailAskTem")) // /<�¶Ȳ�ѯʧ��
						{
							Roomifno.setText("�¶����ݶ�ȡʧ��");
						} else if (((msg.obj).toString()).equals("FailAskGas")) // /<ú����ѯʧ��
						{
							Roomifno.setText("ú�����ݶ�ȡʧ��");
						} else
						{
							if (buffServerStr.substring(0, 3).equals("TEM"))
							{
								str1 = buffServerStr.substring(3);
							} else if (buffServerStr.substring(0, 3).equals("GAS"))
							{
								str2 = buffServerStr.substring(3);
							}
							Roomifno.setText("�����¶�Ϊ��" + str1 + "��\n" + "�����к�����Ϊ��" + str2 + "\n");
						}
					} else
					{
						Roomifno.setText("δ��ȡ������������Ϣ��"); // /<��Ҫ�޸�
					}
					break;
				}
				// /<δ�յ�����������Ϣ
				case 4:
				{
					Roomifno.setText("δ��ȡ�����ڷ���������Ϣ");
					break;
				}
				}
			}

		};

		// /<��ʱ��Ϣ����ʵ�ֶ�ʱ��ѯ����
		delayHandler = new Handler()
		{

			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case 1:
				{
					// /<���ӳɹ��󣬲�ѯ�¶Ⱥ����庬��
					mPrintWriterClient.println("TMPASK");
					mPrintWriterClient.flush();
					break;
				}
				case 2:
				{
					// /<���ӳɹ��󣬲�ѯ�¶Ⱥ����庬��
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

	/* *******�¼����� ******** */
	private OnClickListener listener = new OnClickListener()
	{

		public void onClick(View v)
		{
			Button btn = (Button) v;
			if (isConnecting)
			{
				switch (btn.getId())
				{
				// /<���Ϳ�������
				case R.id.OpenWin:
				{
					mPrintWriterClient.println("ONWIN");
					mPrintWriterClient.flush();

					break;
				}
				// /<���͹ش�����
				case R.id.CloseWin:
				{
					mPrintWriterClient.println("OFFWIN");
					mPrintWriterClient.flush();
					break;
				}
				// /<���͹�ú��������
				case R.id.CloseGas:
				{
					mPrintWriterClient.println("OFFVAL");
					mPrintWriterClient.flush();
					break;
				}
				// /<������Ӧ�Ŀ�������
				case R.id.SendBtn:
				{
					String oderString = null;
					oderString = OderEdit.getText().toString();

					if (isConnecting && (oderString.length() > 0))
					{
						mPrintWriterClient.println(oderString);
						mPrintWriterClient.flush();
						Toast.makeText(ControlChoice.this, "���ͳɹ�", 0).show();
					} else
					{
						Toast.makeText(ControlChoice.this, "�������������", Toast.LENGTH_LONG).show();
					}
					break;
				}
				// /<���Ͷ���֪ͨ��ҵ����
				case R.id.SendMMS:
				{
					String MMSstr = "�����к����峬�꣬������ҵǰ������";

					SharedPreferences references = getSharedPreferences("account", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_WRITEABLE);
					String phnoenum = references.getString("PropertyPhnoe", "");

					if (!phnoenum.equals(""))
					{
						sendSMS(phnoenum, MMSstr);
					} else
					{
						Toast.makeText(ControlChoice.this, "ע��ʱδ������ҵ�绰��������ע�ᣡ", Toast.LENGTH_LONG).show();
					}
					break;
				}
				}

			} else
			{
				Toast.makeText(ControlChoice.this, "δ���ӵ����������������ӣ�", Toast.LENGTH_LONG).show();
			}
		}
	};

	/* ***���Ͷ��ŵĿ��ƺ���*** */
	private void sendSMS(String phoneNumber, String message)
	{
		// �õ�SMS�Ĺ�����
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
					Toast.makeText(ControlChoice.this, "�Ѿ�֪ͨ��ҵ", Toast.LENGTH_LONG).show();
					break;

				default:
					Toast.makeText(ControlChoice.this, "֪ͨ��ҵʧ��", Toast.LENGTH_LONG).show();
				}
			}
		}, new IntentFilter("SMS_SENT"));

		smsManager.sendTextMessage(phoneNumber, null, message, paIntent, null);
	}

	// /<��ʱ�߳�,ʵ�ֲ��ϵĲ�ѯ����
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

	/* ****���ӷ��������̣߳����ڽ��պͷ�����������**** */
	private Runnable mRunnable = new Runnable()
	{

		public void run()
		{
			try
			{
				// /< ����Socket
				// mSocketClient = new Socket(IpAddress, 8888);
				mSocketClient = new Socket();
				isconnect = new InetSocketAddress(IpAddress, 8888);
				mSocketClient.connect(isconnect, 5000); // /<ʵ��5�����������

				// /<�ɹ����ӷ�������Ϣ����
				Message msg = new Message();
				msg.what = 1;
				mHandler.sendMessage(msg);

				while (true)
				{
					try
					{
						// mSocketClient.setKeepAlive(true);///<���ֳ�����
						// /< ͨ��������������˷�����Ϣ
						mPrintWriterClient = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocketClient.getOutputStream())), true);

						// /< �������Է������˵���Ϣ
						mBufferedReaderClient = new BufferedReader(new InputStreamReader(mSocketClient.getInputStream()));
						buffServerStr = mBufferedReaderClient.readLine();

						// /<��ȡ������������Ϣ����Ϣ����
						msg = new Message();
						msg.what = 3;
						msg.obj = buffServerStr;
						mHandler.sendMessage(msg);
					} catch (Exception e)
					{
						// /<��ȡ������������Ϣ����Ϣ����
						msg = new Message();
						msg.what = 4;
						mHandler.sendMessage(msg);
						e.printStackTrace();
					}
				}

			} catch (Exception e)
			{
				// /<δ�ɹ����ӷ�������Ϣ����
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
