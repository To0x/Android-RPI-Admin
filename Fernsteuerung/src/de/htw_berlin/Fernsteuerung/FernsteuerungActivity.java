package de.htw_berlin.Fernsteuerung;


import android.R.bool;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class FernsteuerungActivity extends Activity implements SensorEventListener {

	private Sensor mRotVectSensor;
	private SensorManager mSensorManager;
	private float[] orientationVals=new float[3];
	private float[] mRotationMatrix=new float[16];
	private TextView textView1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fernsteuerung);
		textView1 = (TextView) findViewById(R.id.textView2);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    mRotVectSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fernsteuerung, menu);
		return true;
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mSensorManager.unregisterListener( this);

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 mSensorManager.registerListener(this, mRotVectSensor, mSensorManager.SENSOR_DELAY_GAME);	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		//textView1.setText("Angle: " + event.values[1]);
		
		SensorManager.getRotationMatrixFromVector(mRotationMatrix,event.values);
        SensorManager.remapCoordinateSystem(mRotationMatrix,SensorManager.AXIS_X, SensorManager.AXIS_Z, mRotationMatrix);
        SensorManager.getOrientation(mRotationMatrix, orientationVals);
        orientationVals[2]=(float)Math.toDegrees(orientationVals[2]);
        textView1.setText(String.valueOf(orientationVals[2]));
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
}
