package com.dream.gravitydemo.activity;

import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	private SensorManager sensorManageroneObj;
	private SensorManager sensorManagertwoObj;
	private Sensor sensorone;
	private Sensor sensortwo;
	private TextView gravityObj;
	private TextView proximityObj;
	private float x,y,z;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gravityObj=(TextView)findViewById(R.id.gravity);
		proximityObj = (TextView)findViewById(R.id.proximity);
		 //1 通过服务得到传感器管理对象   
		sensorManageroneObj = (SensorManager) getSystemService(SENSOR_SERVICE); /*重力感应*/
		sensorone = sensorManageroneObj.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//得到一个重力传感器实例
        //TYPE_ACCELEROMETER    加速度传感器(重力传感器)类型。  
        //TYPE_ALL              描述所有类型的传感器。  
        //TYPE_GYROSCOPE        陀螺仪传感器类型  
        //TYPE_LIGHT            光传感器类型  
        //TYPE_MAGNETIC_FIELD   恒定磁场传感器类型。  
        //TYPE_ORIENTATION      方向传感器类型。  
        //TYPE_PRESSURE         描述一个恒定的压力传感器类型  
        //TYPE_PROXIMITY        常量描述型接近传感器  
        //TYPE_TEMPERATURE      温度传感器类型描述  
		/*2 添加重力感应侦听，并实现其方法*/
		 SensorEventListener  sensorEventListener= new  SensorEventListener(){

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSensorChanged(SensorEvent event) {
				// TODO Auto-generated method stub
				x =event.values[sensorManageroneObj.DATA_X];
				y =event.values[sensorManageroneObj.DATA_Y];
				z =event.values[sensorManageroneObj.DATA_Z];
				gravityObj.setText("x="+(int)x+"y="+(int)y+"z="+(int)z);
			}
		 };
		 /*3  注册Listener，SENSOR_DELAY_GAME为检测的精确度，  */
		 sensorManageroneObj.registerListener(sensorEventListener, sensorone, sensorManageroneObj.SENSOR_DELAY_GAME);
	}
		
}

