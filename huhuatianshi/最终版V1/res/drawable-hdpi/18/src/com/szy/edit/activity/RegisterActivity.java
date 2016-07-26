package com.szy.edit.activity;

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

		// 因为后续相关控件要在Button的侦听器(采用的匿名内部类的方式)中访问，所以下面的变量前要加final关键字
		final EditText unameEt = (EditText) findViewById(R.id.uname);
		final EditText upassEt = (EditText) findViewById(R.id.upass);
		final EditText PropertyNum = (EditText) findViewById(R.id.PhnoeNum);

		final RadioGroup rg = (RadioGroup) findViewById(R.id.rg);
		final LinearLayout linear = (LinearLayout) findViewById(R.id.linear);
		final Spinner spinner = (Spinner) findViewById(R.id.province);

		String[] datas = new String[]
		{ "北京市（京）", "天津市（津）", "上海市（沪）", "重庆市（渝）", " 河北省（冀）", " 河南省（豫）", " 云南省（云） ", "辽宁省（辽）", " 黑龙江省（黑） ", "湖南省（湘）", " 安徽省（皖）", " 山东省（鲁） ", "新疆维吾尔（新）", " 江苏省（苏）", " 浙江省（浙）", " 江西省（赣） ", "湖北省（鄂）", " 广西壮族（桂） ", "甘肃省（甘）", " 山西省（晋） ", "内蒙古（蒙）", " 陕西省（陕）", " 吉林省（吉）", " 福建省（闽）", " 贵州省（贵）", " 广东省（粤）", " 青海省（青） ", "西藏（藏） ", "四川省（川）", " 宁夏回族（宁） ", "海南省（琼）", "台湾省（台）" };
		// 实例化Adapter
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, datas);
		// 设置Spinner下拉的样式
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将Adapter与Spinner绑定
		spinner.setAdapter(adapter);

		// /<确认注册
		Button confirmbtn = (Button) findViewById(R.id.confirm);
		confirmbtn.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				String uname = unameEt.getText().toString();
				String password = upassEt.getText().toString();
				String PhnoeNum = PropertyNum.getText().toString();

				// 获得性别：获得父容器，然后遍历父容器中所有RadioButton,找到选中的并获得值
				String sex = "";
				for (int i = 0; i < rg.getChildCount(); i++)
				{
					RadioButton rdo = (RadioButton) rg.getChildAt(i);
					if (rdo.isChecked())
					{
						sex = rdo.getText().toString();
						break;// 跳出循环
					}
				}
				// 获得爱好：获得父容器，然后遍历父容器中所有CheckBox,找到选中的并获得值
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
				// 获得籍贯：调用Spinner的getSelectedItem()方法
				String province = (String) spinner.getSelectedItem();
				// 将所有信息串联起来：用"+"进行连接
				// String result=uname+password+province+sex+hobbys;

				SharedPreferences references = RegisterActivity.this.getSharedPreferences("account", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_WRITEABLE);

				Editor editor = references.edit();

				editor.putString("username", uname);
				editor.putString("password", password);
				editor.putString("PropertyPhnoe", PhnoeNum);
				editor.commit();

				Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
				finish();

			}
		});

		// /<取消注册，退出
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
