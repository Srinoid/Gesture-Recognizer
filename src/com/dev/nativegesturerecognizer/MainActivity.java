package com.dev.nativegesturerecognizer;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Vector;

import com.example.dtwgesturerecognizer.Point;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
private Dialog customMenuDialog;
private boolean train = false;
private float[] A1 = new float[4];
private Gezture mGesture;
enum MODE {
	PERFORMING, FINISH, PASSIVE
};
MODE mode = MODE.PASSIVE;
private GestureChecker gc;
private ISensor mISensor;
private TrainClass tc ;
Vector<Vector<Point>> trainset ;
Point[] cbd = new Point[32];
Vector<Vector<Integer>> qv = new Vector<Vector<Integer>>();
Vector<Point> seq = new Vector<Point>();
String name ="";
EditText et;
TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mISensor = new ISensor(getApplicationContext());
		mGesture = new Gezture();
		gc = new GestureChecker(this);
		tc = new TrainClass();
    	tc.start();
		et = (EditText) findViewById(R.id.editText1);
		tv = (TextView) findViewById(R.id.textView1);
		
		final Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 
			     if(train){
			    	 train = false;
			    	 button1.setText("Start Training");
			    	
			    	 
			     }else{
			    	 
			    	 train =true;
			    	 button1.setText("Stop Training");
			    	
			     }
			     
			
			}
		});
		
		
		
		
		Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				    if(et.getText().length()!=0){
			         
			         name = et.getText().toString();
			       
			       
					byte[] data = Converter.toByte(trainset);
					
					
					/*Uri nUri = getContentResolver().insert(
							GestureDataProvider.CONTENT_URI, mNewValues);*/

			 }
		   
		     trainset.clear();
			}
		
		});
		
		
		
		
		
		ImageView im1 = (ImageView) findViewById(R.id.setting);
		im1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gc = new GestureChecker(getApplicationContext());
				
			    customMenuDialog = dialogbuilder();
			
				customMenuDialog.show();
				
	             
	           
			
			}
		});
	}
   
	public Dialog dialogbuilder() {
		
		final String[] option = new String[gc.name.size()];
		gc.name.toArray(option);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, option);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Gesture Library:");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch (which) {
				case 0:
					
					
					
					break;
				case 1:
					
					break;
				case 2:
					
					break;
				
				}

			}
		});

		return builder.create();
	}
	
   
	private class TrainClass extends Thread {
		
		private volatile boolean running = true;
				
		@Override
		public void run() {
			trainset = new  Vector<Vector<Point>>();	
			super.run();
			 for (;;) {
				 		
			if(mISensor.Mode==ISensor.MODE.PERFORMING){
				
				   
							if(mISensor.values[0]!=0 && mISensor.values[1]!=0 && mISensor.values[2]!=0){
							Point p = new Point();
							p.x = mISensor.values[0];
							p.y = mISensor.values[1];
							p.z = mISensor.values[2];
							p.r = mISensor.values[3];
							mGesture.data.add(p);
							}
			} else if(mISensor.Mode==ISensor.MODE.FINISH) {
						 if (mGesture.data.size() > 40) {
							if(train)
							{
								Gezture sGesture = Scaler.boundBox(mGesture,64);
								Gezture hpf = Filter.hpfFilter(sGesture,(float)0.8);
								Gezture smG = Filter.smoothFilter(hpf, (float) 0.6);
							    Gezture norm = Filter.normalize(smG);
								FeatureExtractor fe = new FeatureExtractor();
								fe.setGesture(norm);
							    trainset.add(fe.getCentroidGezture());
							}else{
								
								Gezture sGesture = Scaler.boundBox(mGesture,64);
								Gezture hpf = Filter.hpfFilter(sGesture,(float)0.8);
								Gezture smG = Filter.smoothFilter(hpf, (float) 0.6);
							    Gezture norm = Filter.normalize(smG);
								FeatureExtractor fe = new FeatureExtractor();
								fe.setGesture(norm);
							    final String name1= gc.recognize(fe.getCentroidGezture());
							    Runnable r = new Runnable(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										
										tv.setVisibility(TextView.VISIBLE);
										tv.setText("Detected Gesture: " + name1);
									}
							    	
								
							    };
							    
							    runOnUiThread(r);
							}
							 
							 Log.v("Thread","finish");
							
							}
							
							mGesture = new Gezture();
							
						}else{
							
						}

						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			
				if (!running){
					Log.v("Thread","Exit"+trainset.size());
					return;
				}
			}
     	}
	
	}
	
	
	
	
	@Override
	public void onResume() {
		
		mISensor.SensorOn();
		super.onResume();
	}

	@Override
	public void onPause() {
		
		mISensor.SensorOf();
		super.onPause();
	}
	
	

	@Override
	public void onDestroy() {
		
		mISensor.SensorOf();
		super.onDestroy();
	}

	
	
}

