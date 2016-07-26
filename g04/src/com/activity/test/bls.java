package com.activity.test;

import com.activity.test.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class bls extends Activity {
	private MediaPlayer mp;
	private String MP;
	private SharedPreferences sp31;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_bls);
		sp31= this.getSharedPreferences("userInfo2", bls.MODE_WORLD_READABLE);
		
}


	public void bt01onclick(View view)
    {   
		
		mp=MediaPlayer.create(this,R.raw.m1);
		mp.start();
   
    }
    public void bt02onclick(View view)
    {
    	if(mp.isPlaying() ){
    		mp.stop();	
    	}
    	
    	
    }
    
    public void bt04onclick(View view)
    {
    	//第一个不能这样设置。原因不知。会造成程序闪退；
    	 if (mp.isPlaying()) {
             mp.stop();
             mp.release();
             mp = null;
         }
        	mp=MediaPlayer.create(this,R.raw.m2);
            mp.start();
       
		
    }
    public void bt05onclick(View view)
    {
    	if(mp.isPlaying() ){
    		mp.stop();	
    	}
    	
    }
    public void bt07onclick(View view)
    {
    	//第一个不能这样设置。原因不知。会造成程序闪退；
    	 if (mp.isPlaying()) {
             mp.stop();
             mp.release();
             mp = null;
         }
        	mp=MediaPlayer.create(this,R.raw.m3);
            mp.start();
       
		
    }
    public void bt08onclick(View view)
    {
    	if(mp.isPlaying() ){
    		mp.stop();	
    	}
    	
    }
    public void bt010onclick(View view)
    {
    	//第一个不能这样设置。原因不知。会造成程序闪退；
    	 if (mp.isPlaying()) {
             mp.stop();
             mp.release();
             mp = null;
         }
        	mp=MediaPlayer.create(this,R.raw.m4);
            mp.start();
       
		
    }
    public void bt011onclick(View view)
    {
    	if(mp.isPlaying() ){
    		mp.stop();	
    	}
    	
    }
    public void bt013onclick(View view)
    {
    	//第一个不能这样设置。原因不知。会造成程序闪退；
    	 if (mp.isPlaying()) {
             mp.stop();
             mp.release();
             mp = null;
         }
        	mp=MediaPlayer.create(this,R.raw.m5);
            mp.start();
       
		
    }
    public void bt014onclick(View view)
    {
    	if(mp.isPlaying() ){
    		mp.stop();	
    	}
    	
    }
    public void bt016onclick(View view)
    {
    	//第一个不能这样设置。原因不知。会造成程序闪退；
    	 if (mp.isPlaying()) {
             mp.stop();
             mp.release();
             mp = null;
         }
        	mp=MediaPlayer.create(this,R.raw.m6);
            mp.start();
       
		
    }
    public void bt017onclick(View view)
    {
    	if(mp.isPlaying() ){
    		mp.stop();	
    	}
    	
    }
    public void bt019onclick(View view)
    {
    	//第一个不能这样设置。原因不知。会造成程序闪退；
    	 if (mp.isPlaying()) {
             mp.stop();
             mp.release();
             mp = null;
         }
        	mp=MediaPlayer.create(this,R.raw.m7);
            mp.start();
       
		
    }
    public void bt020onclick(View view)
    {
    	if(mp.isPlaying() ){
    		mp.stop();	
    	}
    	
    }
    public void bt022onclick(View view)
    {
    	//第一个不能这样设置。原因不知。会造成程序闪退；
    	 if (mp.isPlaying()) {
             mp.stop();
             mp.release();
             mp = null;
         }
        	mp=MediaPlayer.create(this,R.raw.m8);
            mp.start();
       
		
    }
    public void bt023onclick(View view)
    {
    	if(mp.isPlaying() ){
    		mp.stop();	
    	}
    	
    }
    public void bt025onclick(View view)
    {
    	//第一个不能这样设置。原因不知。会造成程序闪退；
    	 if (mp.isPlaying()) {
             mp.stop();
             mp.release();
             mp = null;
         }
        	mp=MediaPlayer.create(this,R.raw.m9);
            mp.start();
       
		
    }
    public void bt026onclick(View view)
    {
    	if(mp.isPlaying() ){
    		mp.stop();	
    	}
    	
    }
    public void bt028onclick(View view)
    {
    	//第一个不能这样设置。原因不知。会造成程序闪退；
    	 if (mp.isPlaying()) {
             mp.stop();
             mp.release();
             mp = null;
         }
        	mp=MediaPlayer.create(this,R.raw.m10);
            mp.start();
       
		
    }
    public void bt029onclick(View view)
    {
    	if(mp.isPlaying() ){
    		mp.stop();	
    	}
    	
    }
    public void bt031onclick(View view)
    {
    	//第一个不能这样设置。原因不知。会造成程序闪退；
    	 if (mp.isPlaying()) {
             mp.stop();
             mp.release();
             mp = null;
         }
        	mp=MediaPlayer.create(this,R.raw.m11);
            mp.start();
       
		
    }
    public void bt032onclick(View view)
    {
    	if(mp.isPlaying() ){
    		mp.stop();	
    	}
    	
    }
    
    //传递报警音效键值对
    public void bt03onclick(View view)
    {
    	String MP="MP1"	;
    	SharedPreferences.Editor editor = sp31.edit();  
        editor.putString("MP", MP);  
        editor.commit(); 
    	}
    public void bt06onclick(View view)
    {
    	String MP="MP2"	;
    	SharedPreferences.Editor editor = sp31.edit();  
        editor.putString("MP", MP);  
        editor.commit(); 
    	}	
    public void bt09onclick(View view)
    {
    	String MP="MP3"	;
    	SharedPreferences.Editor editor = sp31.edit();  
        editor.putString("MP", MP);  
        editor.commit(); 
    	}
    public void bt012onclick(View view)
    {
    	String MP="MP4"	;
    	SharedPreferences.Editor editor = sp31.edit();  
        editor.putString("MP", MP);  
        editor.commit(); 
    	}
    public void bt015onclick(View view)
    {
    	String MP="MP5"	;
    	SharedPreferences.Editor editor = sp31.edit();  
        editor.putString("MP", MP);  
        editor.commit(); 
    	}
    public void bt018onclick(View view)
    {
    	String MP="MP6"	;
    	SharedPreferences.Editor editor = sp31.edit();  
        editor.putString("MP", MP);  
        editor.commit(); 
    	}
    public void bt021onclick(View view)
    {
    	String MP="MP7"	;
    	SharedPreferences.Editor editor = sp31.edit();  
        editor.putString("MP", MP);  
        editor.commit(); 
    	}
    public void bt024onclick(View view)
    {
    	String MP="MP8"	;
    	SharedPreferences.Editor editor = sp31.edit();  
        editor.putString("MP", MP);  
        editor.commit(); 
    	}
    public void bt027onclick(View view)
    {
    	String MP="MP9"	;
    	SharedPreferences.Editor editor = sp31.edit();  
        editor.putString("MP", MP);  
        editor.commit(); 
    	}
    public void bt030onclick(View view)
    {
    	String MP="MP10"	;
    	SharedPreferences.Editor editor = sp31.edit();  
        editor.putString("MP", MP);  
        editor.commit(); 
    	}
    public void bt033onclick(View view)
    {
    	String MP="MP11"	;
    	SharedPreferences.Editor editor = sp31.edit();  
        editor.putString("MP", MP);  
        editor.commit(); 
    	}
    
    


	public void bt034onclick(View view)
    {
    	Handler handler = new Handler();
		handler.postDelayed(new Runnable(){
		public void run(){
			//要执行的代码,跳转到主程序界面
			Intent intent = new Intent(getApplicationContext(),setting.class);
			//开始执行
			startActivity(intent);
			mp.stop();
			}
		},500);//1000毫秒=1秒
	
    	
    }
   
}
