package com.activity.test;


import com.activity.test.R;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.os.Build;

public class MainActivity1 extends Activity {
Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		
        this.button = (Button) this.findViewById(R.id.button1);
		this.button.setOnClickListener(new View.OnClickListener(){  
  
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Editable text=phoneEditText.getText();
				
	
				Intent intent = new Intent();  
                intent.setClass(MainActivity1.this, setting.class); 
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);  
                finish();
			}
			
    			});
		this.button = (Button) this.findViewById(R.id.button2);
		this.button.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();  
                intent.setClass(MainActivity1.this, jc.class);  
                startActivity(intent);  
                finish();	
			} 
		});
	}
}
