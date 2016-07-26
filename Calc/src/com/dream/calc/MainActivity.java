package com.dream.calc;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener
{

	private EditText output;
	private EditText input;
	private Button btn0;
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;
	private Button btn5;
	private Button btn6;
	private Button btn7;
	private Button btn8;
	private Button btn9;
	private Button btnadd;
	private Button btnsubtract;
	private Button btnmultiply;
	private Button btndivide;
	private Button btnclear;
	private Button btnresult;
	private String str;// 保存数字
	private String strold;
	private char act;// 记录符号
	private int count;// 判断要计算的次数，如果超过一个符号，先算出来一部分
	private int result;// 计算的结果

	private int err;// 无错误的时候为0
	private int flag;// 一个标志，如果为1，可以响应运算消息，如果为0，不响应运算消息，只有前面是数字才可以响应运算消息

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		output = (EditText) findViewById(R.id.output);
		output.setText(R.string.output);

		input = (EditText) findViewById(R.id.input);
		input.setText(R.string.input);

		btn0 = (Button) findViewById(R.id.zero);
		btn0.setText(R.string.zero);

		btn1 = (Button) findViewById(R.id.one);
		btn1.setText(R.string.one);

		btn2 = (Button) findViewById(R.id.two);
		btn2.setText(R.string.two);

		btn3 = (Button) findViewById(R.id.three);
		btn3.setText(R.string.three);

		btn4 = (Button) findViewById(R.id.four);
		btn4.setText(R.string.four);

		btn5 = (Button) findViewById(R.id.five);
		btn5.setText(R.string.five);

		btn6 = (Button) findViewById(R.id.six);
		btn6.setText(R.string.six);

		btn7 = (Button) findViewById(R.id.seven);
		btn7.setText(R.string.seven);

		btn8 = (Button) findViewById(R.id.eight);
		btn8.setText(R.string.eight);

		btn9 = (Button) findViewById(R.id.nine);
		btn9.setText(R.string.nine);

		btnadd = (Button) findViewById(R.id.add);
		btnadd.setText(R.string.add);

		btnsubtract = (Button) findViewById(R.id.subtract);
		btnsubtract.setText(R.string.subtract);

		btnmultiply = (Button) findViewById(R.id.multiply);
		btnmultiply.setText(R.string.multiply);

		btndivide = (Button) findViewById(R.id.divide);
		btndivide.setText(R.string.divide);

		btnclear = (Button) findViewById(R.id.clear);
		btnclear.setText(R.string.clear);

		btnresult = (Button) findViewById(R.id.result);
		btnresult.setText(R.string.result);
		btn0.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);
		btn7.setOnClickListener(this);
		btn8.setOnClickListener(this);
		btn9.setOnClickListener(this);
		btnadd.setOnClickListener(this);
		btnsubtract.setOnClickListener(this);
		btnmultiply.setOnClickListener(this);
		btndivide.setOnClickListener(this);
		btnclear.setOnClickListener(this);
		btnresult.setOnClickListener(this);
		act = ' ';
		str = "";
		strold = "";
		count = 0;
		result = 0;
		err = 0;// 处理异常，默认无异常
		flag = 0;// 默认不能响应，按下第一个数字后改为1，可以响应
	}

	public void onClick(View view)
	{
		// TODO Auto-generated method stub
		/*
		 * switch(view.getId()){ case R.id.zero: num(0) ; break; case R.id.one:
		 * num(1) ; break; case R.id.two: num(2) ; break; case R.id.three:
		 * num(3) ; break; case R.id.four: num(4) ; break; case R.id.five:
		 * num(5) ; break; case R.id.six: num(6) ; break; case R.id.seven:
		 * num(7) ; break; case R.id.eight: num(8) ; break; case R.id.nine:
		 * num(9) ; break;
		 * 
		 * 
		 * case R.id.add: add() ; break; case R.id.subtract: sub() ; break; case
		 * R.id.multiply: multiply() ; break; case R.id.divide: divide() ;
		 * break; case R.id.clear: clear(); break; case R.id.result: result() ;
		 * if(err!=1&&flag!=0) output.setText(String.valueOf(result)); break;
		 * default: break; } input.setText(strold+act+str);
		 */
	}

	public void num(int n)
	{
		str = str + String.valueOf(n);
		flag = 1;
	}

	public void add()
	{
		if (flag != 0)
		{
			check();
			act = '+';
			flag = 0;
		}
	}

	public void sub()
	{
		if (flag != 0)
		{
			check();
			act = '-';
			flag = 0;
		}
	}

	public void multiply()
	{
		if (flag != 0)
		{
			check();
			act = '*';
			flag = 0;
		}
	}

	public void divide()
	{
		if (flag != 0)
		{
			check();
			act = '/';
			flag = 0;
		}
	}

	public void check()
	{
		if (count >= 1)
		{
			result();
			str = String.valueOf(result);

		}
		strold = str;
		str = "";
		count++;

	}

	public void result(){  
	        int a,b;  
	        a=Integer.parseInt(strold);  
	        b=Integer.parseInt(str);  
	        if(flag!=0)  
	        {  
	        if(b==0&&act=='/') {  
	            clear();  
	            output.setText("除数不能为零!");  
	            err=1;  
	        }  
	        result=0;  
	        if(err!=1){  
	        switch(act){  
	        case '+':  
	            result=a+b;  
	            break;  
	        case '-':  
	            result=a-b;  
	            break;  
	        case '*':  
	            result=a*b;  
	            break;  
	        case '/':  
	            result=a/b;  
	            break;  
	        default:  
	            break;  
	        }  
	     }
	 }
	}

	public void clear()
	{
		str = strold = "";
		count = 0;
		act = ' ';
		result = 0;
		flag = 0;
		input.setText(strold + act + str);
		output.setText("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
