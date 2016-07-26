package com.example.bluetoothactivity;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//javaapk.com�ṩ����
public class MainActivity extends Activity {
	
	final int HEIGHT=320;   //���û�ͼ��Χ�߶�
    final int WIDTH=320;    //��ͼ��Χ���
    final int X_OFFSET = 5;  //x�ᣨԭ�㣩��ʼλ��ƫ�ƻ�ͼ��Χһ�� 
    private int cx = X_OFFSET;  //ʵʱx������
    int centerY = HEIGHT /2;  //y���λ��
    TextView myview = null;   //�����·���ʾ��ȡ���ݵĵط�
    final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");  
    //uuid ��Ϊ��Ƭ������ģ����
    final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();  
    //��ȡ���ֻ�������������
    static int REQUEST_ENABLE_BT = 1;  //��������ʱʹ��
    BluetoothSocket socket = null;    //�������ݴ����socket
    int READ = 1;                   //���ڴ���������Ϣ���е�ʶ����
    int paintflag=1;//��ͼ�Ƿ���ͣ��־λ��0Ϊ��ͣ
    public ConnectedThread thread = null;   //���������豸�߳�
    static int temp = 0;                //��ʱ�������ڱ�����յ�������
    private SurfaceHolder holder = null;    //��ͼʹ�ã����Կ���һ��SurfaceView��
                                             //SurfaceHolder�ǽӿڣ�������һ��surace�ļ�����
    private Paint paint = null;      //����
    SurfaceView surface = null;     //SurfaceView����ͼ��View������,������Ƕ��һ��ר�����ڻ��Ƶ�Surface
                                     //Surface����һ��ר��������ͼ�Σ�graphics����ͼ��image���ĵط�,
                                    //�������ΪSurface���ǹ������ݵĵط���SurfaceView����չʾ���ݵĵط���
    Timer timer = new Timer();       //һ��ʱ����ƵĶ������ڿ���ʵʱ��x�����꣬
                                     //ʹ�������������ʾ������ǰ����ɨ��
    TimerTask task = null;   //ʱ�䣨Timer�����ƶ����һ������
    private Button stop_bn=null;//��ͣ��ť
    private List<String> listDevices = new ArrayList<String>();//����������װString������
    private ArrayAdapter<String> adtDevices;//����������Adapter��ʾ(�����ݺ���ͼ���������
                                            //������Adapter��������Ȼ����ʾ����ͼ����)���������豸��Ϣ
    BlueBroadcastReceiver mReceiver=new BlueBroadcastReceiver();//�㲥
    
    /* ���ڻ�ͼ��ļ���˵��
     * SurfaceView ��View�ļ̳��࣬�����ͼ��
     * ��Ƕ��һ��ר�����ڻ��Ƶ�Surface�����Կ������Surface�ĸ�ʽ�ͳߴ硣
     * SurfaceView�������Surface�Ļ���λ�á�
     * 
     * ʵ�ֹ��̣��̳�SurfaceView��ʵ��SurfaceHolder.Callback�ӿ�------>
     * SurfaceView.getHolder()���SurfaceHolder����----->SurfaceHolder.addCallback(callback)
     * ��ӻص�����----->surfaceHolder.lockCanvas()���Canvas������������------>
     * Canvas�滭------->SurfaceHolder.unlockCanvasAndPost(Canvas canvas)����������ͼ��
     * ���ύ�ı䣬��ͼ����ʾ��
     * 
     * �����õ���һ����SurfaceHolder�����԰�������surface�Ŀ�������
     * ��������surface����������Canvas�ϻ���Ч���Ͷ��������Ʊ��棬��С�����ص�
     * 
     * �����м������õķ���������������������������
     * */



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		myview = (TextView)findViewById(R.id.myview); 
        Button bluetooth =(Button)findViewById(R.id.button); 
        Button sin =(Button)findViewById(R.id.sin); 
        Button clear =(Button)findViewById(R.id.clear);
        stop_bn=(Button)findViewById(R.id.stop_bn);
        stop_bn.setOnClickListener(new MyButtonStopListener());
        surface = (SurfaceView)findViewById(R.id.show);
        //��ʼ��SurfaceHolder����
        holder = surface.getHolder();  
        holder.setFixedSize(WIDTH+50, HEIGHT+100);  //���û�����С��Ҫ��ʵ�ʵĻ�ͼλ�ô�һ��
        /*���ò��ε���ɫ�Ȳ���*/
        paint = new Paint();  
		paint.setColor(Color.GREEN);  //�����ε���ɫ����ɫ�ģ��������������ɫ
        paint.setStrokeWidth(3);
        bluetooth.setOnClickListener(new MyButtonListener());    
        //��Ӱ�ť������   �������� ��������ͨ���߳�
        clear.setOnClickListener(new MyButtonClearListener()); 
       
        holder.addCallback(new Callback() {  
            public void surfaceChanged(SurfaceHolder holder,int format,int width,int height){ 
                drawBack(holder); 
                //���û����仰����ʹ���ڿ�ʼ���г���������Ļû�а�ɫ�Ļ�������
                //ֱ�����°�������Ϊ�ڰ������ж�drawBack(SurfaceHolder holder)�ĵ���
            }

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				
			} 
        });
        //��Ӱ�ť������  ���TextView����
        /*һ��listview������ʾ�������������豸*/
        ListView arraylistview=(ListView)findViewById(R.id.arraylistview);     
        adtDevices=new ArrayAdapter<String>(this,R.layout.array_item,listDevices);      
        arraylistview.setAdapter(adtDevices);
        arraylistview.setOnItemClickListener(new ListChooseListener());
        
        // Register the BroadcastReceiver  
       	IntentFilter intent = new IntentFilter();
		intent.addAction(BluetoothDevice.ACTION_FOUND);// ��BroadcastReceiver��ȡ���������  
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, intent); // Don't forget to unregister during onDestroy
        
       
       //��Ӱ�ť������ ������ͼ�߳�
        sin.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				new DrawThread().start();  //�߳�����
				
			}
        	
        });
    }
	
	/*ѡ�������豸����������*/
	class ListChooseListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String str = listDevices.get(position);
			String[] values = str.split("\\|");//�ָ��ַ�
			String address=values[1];
			Log.e("address",values[1]);
			BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);		
			Method m;			//��������
			try {
				m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
				socket = (BluetoothSocket) m.invoke(device, Integer.valueOf(1));
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				//socket = device.createRfcommSocketToServiceRecord(uuid); //�������ӣ��÷���������)
				mBluetoothAdapter.cancelDiscovery();  
				//ȡ�����������豸
				socket.connect(); 
				setTitle("���ӳɹ�");
				Toast.makeText(MainActivity.this, "���ӳɹ�", Toast.LENGTH_SHORT).show();
				
			} catch (IOException e) {
				e.printStackTrace();
				setTitle("����ʧ��");//Ŀǰ������ʧ�ܻᵼ�³������ANR
			}
			thread = new ConnectedThread(socket);  //����ͨ�ŵ��߳�
			thread.start();
		}
		
	}
	/*�㲥���������������������Ĺ㲥*/
	class BlueBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action=intent.getAction();
			// When discovery finds a device
			if(action.equals(BluetoothDevice.ACTION_FOUND)){
				// Get the BluetoothDevice object from the Intent 
				BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// Add the name and address to an array adapter to show in a ListView
				String str=(device.getName() + "|" + device.getAddress());
				listDevices.add(str);
				adtDevices.notifyDataSetChanged();//��̬����listview
			}
			
			
		}
		
	}
	
	
	/*����������ť*/
	class MyButtonListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			//���û�д���������ʱ������
			if (!mBluetoothAdapter.isEnabled()) {
	            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	            startActivity(enableBtIntent);
	        }

			 mBluetoothAdapter.startDiscovery();
			 System.out.println("��ʼ��������");
			
           /*
			BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("00:11:22:33:AA:BB");
			try {
				socket = device.createRfcommSocketToServiceRecord(uuid); //��������
				mBluetoothAdapter.cancelDiscovery();  
				
				socket.connect(); 
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			thread = new ConnectedThread(socket);  //����ͨ�ŵ��߳�
			thread.start();
			*/
		}
    	
    }
	class MyButtonStopListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(paintflag==1){
			paintflag=0;
			stop_bn.setText("��ʼ");}
			else
				{paintflag=1;stop_bn.setText("��ͣ");}
			
		}
		
	}
	class MyButtonClearListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			myview.setText("");
		}
		
	}
	
	Handler handler = new Handler() {  //���Ǵ�����Ϣ���е�Handler����

		@Override
		public void handleMessage(Message msg) {
			//������Ϣ
			if (msg.what==READ) {
				String str = (String)msg.obj;	//����ת��			
				myview.append(" "+str);	  //��ʾ�ڻ����·���TextView��
				
			}
			super.handleMessage(msg);
		}
		
	};
	
	/*
	 * ����ֻʵ�������ݵĽ��գ��������ݵķ�������ʵ��
	 * 
	 * */
	private class ConnectedThread extends Thread {
		
		private final InputStream mmInStream;
        //���캯��
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream(); //��ȡ������
            } catch (IOException e) { }
     
            mmInStream = tmpIn;
        }
     
        public void run() {
        	byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()   
            // Keep listening to the InputStream until an exception occurs
            while (true) {        	
                try {                	
                    // Read from the InputStream            
                	 bytes = mmInStream.read(buffer); //bytes���鷵��ֵ��Ϊbuffer����ĳ���
                     // Send the obtained bytes to the UI activity
                	 String str = new String(buffer);
                	 temp = byteToInt(buffer);   //��һ������ʵ������ת������byte��int
                     handler.obtainMessage(READ, bytes, -1, str)
                             .sendToTarget();     //ѹ����Ϣ����
                     
                } catch (Exception e) {
                	System.out.print("read error");
                    break;
                    
                }
            }
        }    
    }
	
	//��ͼ�̣߳�ʵʱ��ȡtemp ��ֵ����yֵ
	public class DrawThread extends Thread {

		public void run() {
			// TODO Auto-generated method stub
			drawBack(holder);    //����������������
            if(task != null){ 
                task.cancel(); 
            } 
            task = new TimerTask() { //�½�����
            	
                
                @Override 
                public void run() { 
                	if(paintflag==1){
                	//��ȡÿһ��ʵʱ��y����ֵ
                	//���»��Ƶ������Ҳ�������Ҫ���ƽ��յ���������ע�͵������cy[];
                	int cy[]=new int[3];
                    cy[0] =  centerY -(int)(50 * Math.sin((cx -5) *2 * Math.PI/150)); 
                    cy[1] =  centerY -(int)(100 * Math.sin((cx -5) *2 * Math.PI/150)); 
                    cy[2] =  centerY -(int)(10 * Math.sin((cx -5) *2 * Math.PI/150)); 
                	
                    //int cy = centerY + temp; //ʵʱ��ȡ��temp��ֵ����Ϊ���ڻ�����˵
                	
                    //�����Ͻ���ԭ�㣬������Ҫ��yֵ����Ҫ�ӻ����м俪ʼ����
                    for(int i=0;i<3;i++){
                    Canvas canvas = holder.lockCanvas(new Rect(cx,cy[i]-2,cx+2,cy[i]+2)); 
                    //����������ֻ������Rect(cx,cy-2,cx+2,cy+2)����������ı䣬��С������
                   if(i==0)
                   {	paint.setColor(Color.GREEN);//���ò�����ɫ
                	   canvas.drawPoint(cx, cy[i], paint); //���    
                   }
                   else if(i==1)
                   { paint.setColor(Color.RED);
                   canvas.drawPoint(cx, cy[i], paint); //���           
                   }   
                   else if(i==2)
                   { paint.setColor(Color.BLUE);
                   canvas.drawPoint(cx, cy[i], paint); //���           
                   }   
                    holder.unlockCanvasAndPost(canvas);  //��������
                    }
                    cx++;    //cx ������ ����������ʱ�����ͼ��    
                    cx++; //����Լ��趨
                    if(cx >=WIDTH){                       
                        cx=5;     //����������ͷ��ʼ��                   
                        drawBack(holder);  //����֮�����ԭ����ͼ�񣬴��¿�ʼ                 
                    		}
                    	
                	}
                } 
            }; 
            timer.schedule(task, 0,1); //��1ms��ִ��һ�θ�ѭ�����񻭳�ͼ�� 
            //��һ�����1ms����һ���㣬Ȼ��������ȥ
  
		}	 
	}
	
	//���û�������ɫ������XY���λ��
    private void drawBack(SurfaceHolder holder){ 
        Canvas canvas = holder.lockCanvas(); //��������
        //���ư�ɫ���� 
        canvas.drawColor(Color.WHITE); 
        Paint p = new Paint(); 
        p.setColor(Color.BLACK); 
        p.setStrokeWidth(2); 
         
        //���������� 
       canvas.drawLine(X_OFFSET, centerY, WIDTH, centerY, p); //����X�� ǰ�ĸ���������ʼ����
       canvas.drawLine(X_OFFSET, 20, X_OFFSET, HEIGHT, p); //����Y�� ǰ�ĸ���������ʼ����

        holder.unlockCanvasAndPost(canvas);  //�������� ��ʾ����Ļ��
        holder.lockCanvas(new Rect(0,0,0,0)); //�����ֲ���������ط������ı�
        holder.unlockCanvasAndPost(canvas); 
        
    }
    //����ת������byte��int
    /*
     * ���� 1byte=8bit��int = 4 byte�� 
     * һ�㵥Ƭ������c51 8λ��  MSP430  16λ ������ֻ��Ҫ�õ�������byte��ok 
     * */
    public static int byteToInt(byte[] b){
    	  return (((int)b[0])+((int)b[1])*256);
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0,1, 1, "exit");// ���menu�˵�һ��item 		
		//��һ�������ǲ˵�����������֣����id���ڶ�����item��id ����������item
		//���һ����item��ʾ�����ݡ�
		return super.onCreateOptionsMenu(menu);
	}
    	//�����²˵�ʱ��ѡ������һ��item������º���
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		finish();
		return super.onMenuItemSelected(featureId, item);
	}

    
    @Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
	}
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		 this.unregisterReceiver(mReceiver);
			super.onDestroy();
			android.os.Process.killProcess(android.os.Process.myPid());
			
			thread.destroy();
	}
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		
	}

}
