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
		 //1 ͨ������õ��������������   
		sensorManageroneObj = (SensorManager) getSystemService(SENSOR_SERVICE); /*������Ӧ*/
		sensorone = sensorManageroneObj.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//�õ�һ������������ʵ��
        //TYPE_ACCELEROMETER    ���ٶȴ�����(����������)���͡�  
        //TYPE_ALL              �����������͵Ĵ�������  
        //TYPE_GYROSCOPE        �����Ǵ���������  
        //TYPE_LIGHT            �⴫��������  
        //TYPE_MAGNETIC_FIELD   �㶨�ų����������͡�  
        //TYPE_ORIENTATION      ���򴫸������͡�  
        //TYPE_PRESSURE         ����һ���㶨��ѹ������������  
        //TYPE_PROXIMITY        ���������ͽӽ�������  
        //TYPE_TEMPERATURE      �¶ȴ�������������  
		/*2 ���������Ӧ��������ʵ���䷽��*/
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
		 /*3  ע��Listener��SENSOR_DELAY_GAMEΪ���ľ�ȷ�ȣ�  */
		 sensorManageroneObj.registerListener(sensorEventListener, sensorone, sensorManageroneObj.SENSOR_DELAY_GAME);
	}
		
}

