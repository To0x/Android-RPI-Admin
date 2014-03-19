package de.htw_berlin.Fernsteuerung;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorHelper implements SensorEventListener{

	private float lastGravity = 0.0f;
	private float gravity = 0.0f;
	
	private SensorManager mSensorManager = null;
	private MainActivity content = null;
	
	public SensorHelper(MainActivity c){
		content = c;
	}
	
	public float getcurrentGravity() {
		return gravity;
	}
	
	private float roundOnFive(float input) {
		
		final int roundingVal = 5;
		
		int mod = Math.round(input % roundingVal);
		int div = Math.round(input / roundingVal);
		
		if (mod > (roundingVal/2)) {
			div++;
		}
		
		return (div * roundingVal);
		
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		
		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
			return;
		
		final int con = 10;
	
		if (Math.abs(lastGravity - event.values[1]) > 0.3f) {
		
			gravity = event.values[1] * 2;
			
			if (gravity > con)
				gravity = con;
			
			if (gravity < (con*-1))
				gravity = (con*-1);
			
			gravity *= 4.5f;
			gravity = roundOnFive(gravity);

			lastGravity = event.values[1];
			
			//if (gravity < 0)
				//content.setSeekBarProgress(Math.round(50-Math.abs(gravity)/45*50));
			//else
				//content.setSeekBarProgress(Math.round(gravity/45*50+50));
		}
		
	}

	public void initSensoring() {
		mSensorManager = (SensorManager) content.getSystemService(Context.SENSOR_SERVICE);
	}
	
	public void startSensoring() {
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
	}
	
	public void stopSensoring() {
		mSensorManager.unregisterListener(this);
	}

	
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	
}
