package com.activity.test;

import com.activity.test.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;



public class Timer extends Activity {
	
		private Button button;
		 private MediaPlayer mp;
		 private String MP3; 
		 
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.fragment_timer);
			//
			SharedPreferences sp32 = this.getSharedPreferences("userInfo2", Timer.MODE_WORLD_READABLE);
			MP3=sp32.getString("MP", "");

			
	//判断传递来的是那种音效
			check();
			mp.start();
			this.button=(Button) this.findViewById(R.id.button001);
			this.button.setOnClickListener(new View.OnClickListener(){ 
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mp.stop();
					Intent intent = new Intent();  
	                intent.setClass(Timer.this, MainActivity1.class);  
	                startActivity(intent);  
	                finish();
				}
	    			});
			Handler handler = new Handler();
			handler.postDelayed(new Runnable(){
			public void run(){
				//要执行的代码,跳转到主程序界面
				Intent intent = new Intent(getApplicationContext(),SendMessage.class);
				//开始执行
				startActivity(intent);
				}
			}, 30000);//1000毫秒=1秒
			
	        	}
		public void check(){
			
			if(MP3.equals("MP1")==true){
				mp=MediaPlayer.create(this,R.raw.m1);
		      }else
		    	  if(MP3.equals("MP2")==true){
		    		  mp=MediaPlayer.create(this,R.raw.m2);
		    	  						}		
		    	  else
		    		  if(MP3.equals("MP3")==true){
		    		  	mp=MediaPlayer.create(this,R.raw.m3);
		    		  }		    	  
		    		  else
		    			  if(MP3.equals("MP4")==true){
		    			  	mp=MediaPlayer.create(this,R.raw.m4);
		    			  }	
		    			  else
		    				  if(MP3.equals("MP5")==true){
		    				  	mp=MediaPlayer.create(this,R.raw.m5);
		    				  }		
		    				  else
		    					  if(MP3.equals("MP6")==true){
		    					  	mp=MediaPlayer.create(this,R.raw.m6);
		    					  }	
		    					  else
		    						  if(MP3.equals("MP7")==true){
		    						  	mp=MediaPlayer.create(this,R.raw.m7);
		    						  }		  
		    						  else
		    							  if(MP3.equals("MP8")==true){
		    							  	mp=MediaPlayer.create(this,R.raw.m8);
		    							  }		  
		    							  else
		    								  if(MP3.equals("MP9")==true){
		    								  	mp=MediaPlayer.create(this,R.raw.m9);
		    								  }		  
		    								  else
		    									  if(MP3.equals("MP10")==true){
		    									  	mp=MediaPlayer.create(this,R.raw.m10);
		    									  }		
		    									  else
		    										  if(MP3.equals("MP11")==true){
		    										  	mp=MediaPlayer.create(this,R.raw.m11);
		    										  }		    	  
		}
	
}
