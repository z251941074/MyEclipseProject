package com.dream.c;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity
{
    private Button[] btnNum = new Button[11];// ��ֵ��ť  
    private Button[] btnCommand = new Button[5];// ���Ű�ť  
    
    private EditText editTextShow = null;// ��ʾ����
    private EditText editTextResult = null;// ��ʾ����
    private Button btnClear = null; // clear��ť  
    private String lastCommand; // ���ڱ��������  
    private boolean clearFlag; // �����ж��Ƿ������ʾ�����ֵ,true��Ҫ,false����Ҫ  
    private boolean firstFlag; // �����ж��Ƿ����״�����,true�״�,false�����״�  
    private double result; // ������ 
	public void calculator()
	{
		// ��ʼ������ֵ
		result = 0; // x��ֵ
		firstFlag = true; // ���״�����
		clearFlag = false; // ����Ҫ���
		lastCommand = "="; // �����
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        // ��ȡ�����  
        btnCommand[0] = (Button) findViewById(R.id.add); 
        btnCommand[1] = (Button) findViewById(R.id.subtract); 
        btnCommand[2] = (Button) findViewById(R.id.multiply); 
        btnCommand[3] = (Button) findViewById(R.id.divide); 
        btnCommand[4] = (Button) findViewById(R.id.equal); 
        // ��ȡ����  
        btnNum[0] = (Button) findViewById(R.id.num0); 
        btnNum[1] = (Button) findViewById(R.id.num1); 
        btnNum[2] = (Button) findViewById(R.id.num2); 
        btnNum[3] = (Button) findViewById(R.id.num3); 
        btnNum[4] = (Button) findViewById(R.id.num4); 
        btnNum[5] = (Button) findViewById(R.id.num5); 
        btnNum[6] = (Button) findViewById(R.id.num6); 
        btnNum[7] = (Button) findViewById(R.id.num7); 
        btnNum[8] = (Button) findViewById(R.id.num8); 
        btnNum[9] = (Button) findViewById(R.id.num9); 
        btnNum[10] = (Button) findViewById(R.id.point); 
        
     // ��ʼ����ʾ�������  
        editTextResult = (EditText) findViewById(R.id.result); 
        editTextResult.setText("0.0");
        
        editTextShow = (EditText) findViewById(R.id.show); 
        editTextShow.setText("0.0");
        
        // ʵ��������������  
        NumberAction na = new NumberAction(); 
        CommandAction ca = new CommandAction(); 
        for (Button bc : btnCommand) { 
            bc.setOnClickListener(ca); 
        } 
        for (Button bc : btnNum) { 
            bc.setOnClickListener(na); 
        } 
        // clear��ť�Ķ���  
        btnClear = (Button) findViewById(R.id.clear); 
        btnClear.setOnClickListener(new OnClickListener() { 
            @Override 
            public void onClick(View view) { 
                editTextShow.setText("0.0"); 
                editTextResult.setText("0.0"); 
                // ��ʼ������ֵ  
                result = 0; // x��ֵ  
                firstFlag = true; // ���״�����  
                clearFlag = false; // ����Ҫ���  
                lastCommand = "="; // �����  
            } 
        }); 
	}
    private class NumberAction implements OnClickListener { 
        @Override 
        public void onClick(View view) { 
            Button btn = (Button) view; 
            String input = btn.getText().toString(); 
            if (firstFlag) { // �״�����  
                // һ�Ͼ�".",��ʲôҲ����  
                if (input.equals(".")) { 
                    return; 
                } 
                // �����"0.0"�Ļ�,�����  
                if (editTextShow.getText().toString().equals("0.0")) { 
                	editTextShow.setText(""); 
                } 
                firstFlag = false;// �ı��Ƿ��״�����ı��ֵ  
            } else { 
                String editTextStr = editTextShow.getText().toString(); 
                // �ж���ʾ�����ֵ�����Ƿ��Ѿ���".",�����,���������".",��ʲô������  
                if (editTextStr.indexOf(".") != -1 && input.equals(".")) { 
                    return; 
                } 
                // �ж���ʾ�����ֵ����ֻ��"-",���������".",��ʲô������  
                if (editTextStr.equals("-") && input.equals(".")) { 
                    return; 
                } 
                // �ж���ʾ�����ֵ�����"0",����Ĳ���".",��ʲôҲ����  
                if (editTextStr.equals("0") && !input.equals(".")) { 
                    return; 
                } 
            } 
            // ����ҵ����������Ժ�,���������ֵĻ�,��Ҫ�����ʾ�����ֵ  
            if (clearFlag) { 
            	editTextShow.setText(""); 
                clearFlag = false;// ��ԭ��ʼֵ,����Ҫ���  
            } 
            editTextShow.setText(editTextShow.getText().toString() + input);// ������ʾ�����ֵ  
        } 
    } 
    
    // ���Ű�ť������  
    private class CommandAction implements OnClickListener { 
        @Override 
        public void onClick(View view) { 
            Button btn = (Button) view; 
            String inputCommand = (String) btn.getText(); 
            if (firstFlag) {// �״�����"-"�����  
                if (inputCommand.equals("-")) { 
                	editTextShow.setText("-");// ��ʾ�������������Ϊ"-"  
                    firstFlag = false;// �ı��״�����ı��  
                } 
            } else { 
                if (!clearFlag) {// ���flag=false����Ҫ�����ʾ����ֵ,�͵��÷�������  
                    calculate(Double.parseDouble(editTextShow.getText().toString()));// ������ʾ�����ֵ,������  
                } 
                // ���������������  
                lastCommand = inputCommand; 
                clearFlag = true;// ��Ϊ�������Ѿ�����������,  
            } 
        } 
    } 
    
    // �����õķ���  
    private void calculate(double x) { 
         
         
        if (lastCommand.equals("+")) { 
            result += x; 
        } else if (lastCommand.equals("-")) { 
            result -= x; 
        } else if (lastCommand.equals("*")) { 
            result *= x; 
        } else if (lastCommand.equals("/")) { 
            result /= x; 
        } else if (lastCommand.equals("=")) { 
            result = x; 
        } 
        editTextResult.setText("" + result); 
    } 

    
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
