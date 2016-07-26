package com.activity.test;


import com.activity.test.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class setting extends Activity{
	Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//protected void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	setContentView(R.layout.fragment_setting);
	this.button = (Button) this.findViewById(R.id.layout_account);
	this.button.setOnClickListener(new View.OnClickListener(){  

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Editable text=phoneEditText.getText();

			Intent intent = new Intent();  
            intent.setClass(setting.this,pns.class); 
            startActivity(intent);  
            finish();
		}
		
	});
	
	this.button = (Button) this.findViewById(R.id.layout_notification);
	this.button.setOnClickListener(new View.OnClickListener(){  

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Editable text=phoneEditText.getText();
			

			Intent intent = new Intent();  
            intent.setClass(setting.this,ThreeTwoOneGoActivity.class); 
            startActivity(intent);  
		}
		
	});
	
	
	this.button = (Button) this.findViewById(R.id.layout_about);
	this.button.setOnClickListener(new View.OnClickListener(){  

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Editable text=phoneEditText.getText();
			

			Intent intent = new Intent();  
            intent.setClass(setting.this, shm.class); 
            startActivity(intent);  
            finish();
		}
		
	});
	this.button = (Button) this.findViewById(R.id.layout_general);
	this.button.setOnClickListener(new View.OnClickListener(){  

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Editable text=phoneEditText.getText();
			Intent intent = new Intent();  
            intent.setClass(setting.this, bls.class); 
            startActivity(intent);  
            finish();
		}
		
	});
	this.button = (Button) this.findViewById(R.id.layout_privacy);
	this.button.setOnClickListener(new View.OnClickListener(){  

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Editable text=phoneEditText.getText();
			

			Intent intent = new Intent();  
            intent.setClass(setting.this, yz.class); 
            startActivity(intent);  
            finish();
		}
		
	});
	this.button = (Button) this.findViewById(R.id.layout_exit);
	this.button.setOnClickListener(new View.OnClickListener(){  

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Editable text=phoneEditText.getText();
			

			Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            System.exit(0);//ÍË³ö³ÌÐò
		}
		
	});
	
	}
	public void btsebonclick(View view)
    {  
		Intent intent = new Intent(getApplicationContext(),MainActivity1.class);
		startActivity(intent);
		} 
}
