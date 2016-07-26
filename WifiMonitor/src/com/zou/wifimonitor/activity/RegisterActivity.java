package com.zou.wifimonitor.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.register);

		// ��Ϊ������ؿؼ�Ҫ��Button��������(���õ������ڲ���ķ�ʽ)�з��ʣ���������ı���ǰҪ��final�ؼ���
		final EditText unameEt = (EditText) findViewById(R.id.uname);
		final EditText upassEt = (EditText) findViewById(R.id.upass);
		final EditText PropertyNum = (EditText) findViewById(R.id.PhnoeNum);

		final RadioGroup rg = (RadioGroup) findViewById(R.id.rg);
		final LinearLayout linear = (LinearLayout) findViewById(R.id.linear);
		final Spinner spinner = (Spinner) findViewById(R.id.province);

		String[] datas = new String[]
		{ "�����У�����", "����У���", "�Ϻ��У�����", "�����У��壩", " �ӱ�ʡ������", " ����ʡ��ԥ��", " ����ʡ���ƣ� ", "����ʡ���ɣ�", " ������ʡ���ڣ� ", "����ʡ���棩", " ����ʡ���", " ɽ��ʡ��³�� ", "�½�ά������£�", " ����ʡ���գ�", " �㽭ʡ���㣩", " ����ʡ���ӣ� ", "����ʡ������", " ����׳�壨�� ", "����ʡ���ʣ�", " ɽ��ʡ������ ", "���ɹţ��ɣ�", " ����ʡ���£�", " ����ʡ������", " ����ʡ������", " ����ʡ����", " �㶫ʡ������", " �ຣʡ���ࣩ ", "���أ��أ� ", "�Ĵ�ʡ������", " ���Ļ��壨���� ", "����ʡ����", "̨��ʡ��̨��" };
		// ʵ����Adapter
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, datas);
		// ����Spinner��������ʽ
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��Adapter��Spinner��
		spinner.setAdapter(adapter);

		// /<ȷ��ע��
		Button confirmbtn = (Button) findViewById(R.id.confirm);
		confirmbtn.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				String uname = unameEt.getText().toString();
				String password = upassEt.getText().toString();
				String PhnoeNum = PropertyNum.getText().toString();

				// ����Ա𣺻�ø�������Ȼ�����������������RadioButton,�ҵ�ѡ�еĲ����ֵ
				String sex = "";
				for (int i = 0; i < rg.getChildCount(); i++)
				{
					RadioButton rdo = (RadioButton) rg.getChildAt(i);
					if (rdo.isChecked())
					{
						sex = rdo.getText().toString();
						break;// ����ѭ��
					}
				}
				// ��ð��ã���ø�������Ȼ�����������������CheckBox,�ҵ�ѡ�еĲ����ֵ
				// String hobbys = "";
				// for (int i = 0; i < linear.getChildCount(); i++)
				// {
				// if (linear.getChildAt(i) instanceof CheckBox)
				// {
				// CheckBox chk = (CheckBox) linear.getChildAt(i);
				// if (chk.isChecked())
				// {
				// hobbys += chk.getText();
				// }
				// }
				// }
				// ��ü��᣺����Spinner��getSelectedItem()����
				String province = (String) spinner.getSelectedItem();
				// ��������Ϣ������������"+"��������
				// String result=uname+password+province+sex+hobbys;

				SharedPreferences references = RegisterActivity.this.getSharedPreferences("account", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_WRITEABLE);

				Editor editor = references.edit();

				editor.putString("username", uname);
				editor.putString("password", password);
				editor.putString("PropertyPhnoe", PhnoeNum);
				editor.commit();

				Toast.makeText(RegisterActivity.this, "ע��ɹ�", Toast.LENGTH_LONG).show();
				finish();

			}
		});

		// /<ȡ��ע�ᣬ�˳�
		Button cancelbtn = (Button) findViewById(R.id.cancel);
		cancelbtn.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{
				finish();
			}
		});

	}
}
