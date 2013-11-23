package com.dev.nativegesturerecognizer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.util.Log;

public class ISensor implements SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor aSensor, mSensor, gSensor;
	private float[] A_ = new float[3];
	private float[] G = new float[3];
	private float[] A = new float[3];
	private float[] M = new float[3];
	private float[] mR = new float[9];
	public float[] values = new float[4];
	private float[] prev = new float[] { 0, 0, 0 };
	private float[] diff = new float[] { 0, 0, 0 };
	private boolean Init;
	private int sleep = 14;
	public static enum MODE {
		START, PERFORMING, FINISH, PASSIVE
	};
	private int n;
	private long tinit;
	public MODE Mode = MODE.PASSIVE;
	private long t1;
	private long t2;

	ISensor(Context context) {
		mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
	}

	
	protected void SensorOn() {

		aSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		gSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

		mSensorManager.registerListener(this, gSensor,
				SensorManager.SENSOR_DELAY_GAME);
		mSensorManager.registerListener(this, mSensor,
				SensorManager.SENSOR_DELAY_GAME);
		mSensorManager.registerListener(this, aSensor,
				SensorManager.SENSOR_DELAY_GAME);

		new ThreadClass().start();
	}

	
	protected void SensorOf() {
		mSensorManager.unregisterListener(this);
	}

	
	public void processData(SensorEvent event) {

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

			A[0] = event.values[0];
			A[1] = event.values[1];
			A[2] = event.values[2];

			diff[0] = Math.abs(prev[0] - A[0]);
			diff[1] = Math.abs(prev[1] - A[1]);
			diff[2] = Math.abs(prev[2] - A[2]);

			prev[0] = A[0];
			prev[1] = A[1];
			prev[2] = A[2];

			if (!Init) {

				t1 = event.timestamp;
				tinit = t1;

			} else {
				sleep = (int) ((event.timestamp - t1) / 1000000);
				t1 = event.timestamp;
			}

			Init = true;

		    if (checkThreshold()) {

				Mode = MODE.PERFORMING;

			} 
		    if (!checkThreshold() && (Mode == MODE.PERFORMING)) {

				Mode = MODE.FINISH;
                Log.v("Thread","finish");
			} 

		} else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {

			G[0] = G[0] + (event.values[0] - G[0]) * 0.33334f;
			G[1] = G[1] + (event.values[1] - G[1]) * 0.33334f;
			G[2] = G[2] + (event.values[2] - G[2]) * 0.33334f;
		}
		else {
			M[0] = event.values[0];
			M[1] = event.values[1];
			M[2] = event.values[2];
		}
	}

	private class ThreadClass extends Thread {
		@Override
		public void run() {
			for (;;) {
				SensorManager.getRotationMatrix(mR, null, G, M);
				A_ = mult(mR, A);
				float[] values1 = new float[3];
				SensorManager.getOrientation(mR, values1);
				float[] rz = new float[9];
				rz[0] = (float) Math.cos(values1[0]);
				rz[1] = (float) -Math.sin(values1[0]);
				rz[2] = (float) 0;
				rz[3] = (float) Math.sin(values1[0]);
				rz[4] = (float) Math.cos(values1[0]);
				rz[5] = (float) 0;
				rz[6] = (float) 0;
				rz[7] = (float) 0;
				rz[8] = (float) 1;
				values = mult(rz, A_);
				
			}
		}
	}

	public float[] mult(float[] m1, float[] m2) {
		float[] m3 = new float[4];
		float sum = 0;
		for (int c = 0; c < 3; c++) {
			for (int k = 0; k < 3; k++) {
				sum = sum + m1[(c * 3) + k] * m2[k];
			}
			m3[c] = sum;
			sum = 0;
		}
		m3[3] = (float) Math.sqrt(Math.pow(m3[0], 2) + Math.pow(m3[1], 2)
				+ Math.pow(m3[2], 2));
		return m3;
	}

	public boolean checkThreshold() {
		if (diff[0] < (0.7 * sleep / 70) && diff[1] < (0.7 * sleep / 70)
				&& diff[2] < (0.7 * sleep / 70))
			return false;
		return true;
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		processData(event);
	}
	
	
}
