package com.czy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Log_face extends Activity {
	private Context mContext = null;
	static private String conStr = "192.168.2.1";
	static private String conurl = "192.168.2.1";//"192.168.1.105";//
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_face);
        mContext = this;
        ImageButton button_cam = (ImageButton)findViewById(R.id.button_cam);
 /*       Button button_map = (Button)findViewById(R.id.button_map);
        button_map.setVisibility(View.INVISIBLE);*/
        ImageButton button_set = (ImageButton)findViewById(R.id.button_set);
        ImageButton button_exit = (ImageButton)findViewById(R.id.button_exit);
        button_cam.setOnClickListener(new View.OnClickListener() {	
		@Override
		public void onClick(View v) {		
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("conStr",conStr);
				bundle.putString("conurl", conurl);//将Bundle对象assign给Intent
				intent.putExtras(bundle);
				intent.setClass(Log_face.this, client.class);
				startActivity(intent);
				//设置切换动画，从右边进入，左边退出,带动态效果
				overridePendingTransition(R.anim.in_scale_center, R.anim.out_to_left);
//				finish();
			}
		});  
    /*    
        button_map.setOnClickListener(new View.OnClickListener() {	
		@Override
		public void onClick(View v) {		
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("conurl", conurl);//将Bundle对象assign给Intent
				intent.putExtras(bundle);
				intent.setClass(Log_face.this, LocationActivity.class);
				startActivity(intent);
				//设置切换动画，从右边进入，左边退出,带动态效果
				overridePendingTransition(R.anim.in_scale_center, R.anim.out_to_left);
//				finish();
			}
		});  
        */
        button_set.setOnClickListener(new View.OnClickListener() {	
		@Override
		public void onClick(View v) {		
//			public void Setting() {
				
				LayoutInflater factory=LayoutInflater.from(mContext);
				final View v1=factory.inflate(R.layout.setting,null);
				AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
				
				dialog.setTitle("连接地址设置");
				dialog.setView(v1);
				EditText et1 = (EditText)v1.findViewById(R.id.connectionurl);
				EditText et2 = (EditText)v1.findViewById(R.id.controlurl);
		    	et1.setText(conStr);
		    	et2.setText(conurl);
		    	
		        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {
		            	EditText qet1 = (EditText)v1.findViewById(R.id.connectionurl);
		            	EditText qet2 = (EditText)v1.findViewById(R.id.controlurl);
		            	conStr = qet1.getText().toString();
		            	conurl = qet2.getText().toString();
		            	Toast.makeText(mContext, "设置成功！", Toast.LENGTH_SHORT).show(); 
		            }
		        });
		        dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
		        dialog.show();
			}
//			}
		});  
        
        button_exit.setOnClickListener(new View.OnClickListener() {	
    		@Override
    		public void onClick(View v) {		
    				finish();
    			}
    		}); 
	}
	@Override
	protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	// release application's RAM
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(intent);
		System.exit(0);
	}
	
}
