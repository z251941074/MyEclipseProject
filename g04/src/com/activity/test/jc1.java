package com.activity.test;

import com.activity.test.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class jc1 extends Activity {
	private SensorManager sensorManager = null;
	private Sensor sensor = null;
	private float gravity[] = new float[3];
	double        				A;
	double						A1;
	double g=9.8;
	double i=2.3*g;
	
	
	
	
	
	//SharedPreferences sp42= this.getSharedPreferences("userInfo3", jc.MODE_WORLD_READABLE);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_jc);
		//SharedPreferences sp42= this.getSharedPreferences("userInfo3", jc.MODE_WORLD_READABLE);
		//String K=sp42.getString("I","1");
		

	chk();
	
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		
	}
	
public  void chk(){
	SharedPreferences sp43 =getSharedPreferences("userInfo3",jc1.MODE_PRIVATE);
	String K =sp43.getString("i", "1");
		if(K.equals("1")==true){
				i=2*g;
			}else 
				if(K.equals("2")==true){
					i=2.3*g;
			}else 
				if(K.equals("3")==true){
					i=2.5*g;
			}
	}
		

	private SensorEventListener listener = new SensorEventListener() {
		
		
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			
		}
		@Override
		public void onSensorChanged(SensorEvent e) {
			gravity[0] = e.values[0];
			gravity[1] = e.values[1];
			gravity[2] = e.values[2];
			//

			A = Math.pow(gravity[0], 2)+Math.pow(gravity[1], 2)+Math.pow(gravity[2], 2);
			A1 = Math.sqrt(A);
				    if(A1<(i)){
		
							Intent intent = new Intent(getApplicationContext(),Timer.class);
							startActivity(intent);
							
										}	
				    finish();
							     				
				}		
			
	};
	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(listener, sensor,
				SensorManager.SENSOR_DELAY_NORMAL);
	}


	@Override
	protected void onStop() {
		super.onStop();
		sensorManager.unregisterListener(listener);
			
		}
		}
