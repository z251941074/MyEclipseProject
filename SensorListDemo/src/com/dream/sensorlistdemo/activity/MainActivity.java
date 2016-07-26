package com.dream.sensorlistdemo.activity;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ׼����ʾ��Ϣ��UI�齨
		final TextView tx1 = (TextView) findViewById(R.id.TextView01);
		// ��ϵͳ�����л�ô�����������
		SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		// �Ӵ������������л��ȫ���Ĵ������б�
		List<Sensor> allSensors = sm.getSensorList(Sensor.TYPE_ALL);
       
		// ��ʾ�ж��ٸ�������
		tx1.setText("�������ֻ���" + allSensors.size() + "�������������Ƿֱ��ǣ�\n");
		
		// ��ʾÿ���������ľ�����Ϣ
		for (Sensor s : allSensors) {
			String tempString = "\n" + "  �豸���ƣ�" + s.getName() + "\n"
					+ "  �豸�汾��" + s.getVersion() + "\n" + "  ��Ӧ�̣�"
					+ s.getVendor() + "\n";
			
			switch (s.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				tx1.setText(tx1.getText().toString() + s.getType()
						+ " ���ٶȴ�����accelerometer" + tempString);
				break;
			case Sensor.TYPE_GYROSCOPE:
				tx1.setText(tx1.getText().toString() + s.getType()
						+ " �����Ǵ�����gyroscope" + tempString);
				break;
			case Sensor.TYPE_LIGHT:
				tx1.setText(tx1.getText().toString() + s.getType()
						+ " �������ߴ�����light" + tempString);
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				tx1.setText(tx1.getText().toString() + s.getType()
						+ " ��ų�������magnetic field" + tempString);
				break;
			case Sensor.TYPE_ORIENTATION:
				tx1.setText(tx1.getText().toString() + s.getType()
						+ " ���򴫸���orientation" + tempString);
				break;
			case Sensor.TYPE_PRESSURE:
				tx1.setText(tx1.getText().toString() + s.getType()
						+ " ѹ��������pressure" + tempString);
				break;
			case Sensor.TYPE_PROXIMITY:
				tx1.setText(tx1.getText().toString() + s.getType()
						+ " ���봫����proximity" + tempString);
				break;
			case Sensor.TYPE_TEMPERATURE:
				tx1.setText(tx1.getText().toString() + s.getType()
						+ " �¶ȴ�����temperature" + tempString);
				break;
			default:
				tx1.setText(tx1.getText().toString() + s.getType() + " δ֪������"
						+ tempString);
				break;

			}
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
