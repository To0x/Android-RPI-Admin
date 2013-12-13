package de.htw_berlin.Fernsteuerung;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class FernsteuerungActivity extends Activity implements SensorEventListener {
	
	private Sensor mRotVectSensor;
	private SensorManager mSensorManager;

	private TextView textView1;
	
	private float gravity[];
	private float geomagnetic[];
	
	private TextView textView2;
	private EditText editText;
	
	private float calibratedX = 0, calibratedY = 0;
	private SeekBar seekHorizontal, seekVertical;
	private Button btnCalibrate;
	private Button btnChange;
	
	private TextView debugText[][];
	
	private Socket clientSocket;
	
	private int __layout = __DEFINES.DEBUG;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		ConnectionHelper.CheckWifi(this);
		
		debugText = new TextView[3][3];
		gravity = new float[3];
		geomagnetic = new float[3];
		if (__layout == 0)
			setContentView(R.layout.activity_fernsteuerung);
		else if (__layout == 1)
			setContentView(R.layout.debug);
		
		// Steht hier weil sonst eine networkonmainthreadexception kommt 
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		if (__layout == 0) {
			textView1 = (TextView) findViewById(R.id.textView2);
		    textView2 = (TextView) findViewById(R.id.textViewServerOutput);
		    editText = (EditText) findViewById(R.id.editText1);
		    btnChange = (Button) findViewById(R.id.buttonChange);
		}
		else if (__layout == 1) {
				debugText[0][0] = (TextView) findViewById(R.id.textView1);
				debugText[0][1] = (TextView) findViewById(R.id.textView2);
				debugText[0][2] = (TextView) findViewById(R.id.textView3);
				debugText[1][0] = (TextView) findViewById(R.id.textView4);
				debugText[1][1] = (TextView) findViewById(R.id.textView5);
				debugText[1][2] = (TextView) findViewById(R.id.textView6);
				debugText[2][0] = (TextView) findViewById(R.id.textView7);
				debugText[2][1] = (TextView) findViewById(R.id.textView8);
				debugText[2][2] = (TextView) findViewById(R.id.textView9);
		}
			
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fernsteuerung, menu);
		return true;
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener( this);

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		 mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
		 mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
		 
	}

	protected float[] lowPass (float[] input, float[] output) {
		if (output == null) return input;
		
		for (int i = 0 ; i < input.length ; i++) {
			output[i] = 0.8f * output[i] + 0.2f * input[i];
			output[i] = input[i] - output[i];
		}
		return output;
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {		

		float[] R = new float[9];
		float[] I = new float[9];
		float[] orientationMatrix = new float[3];
		
		
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			//System.arraycopy(event.values, 0, gravity, 0, 3);
			gravity = lowPass(event.values.clone(), gravity);
		}
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			//System.arraycopy(event.values, 0, geomagnetic,0,3);
			geomagnetic = lowPass(event.values.clone(), geomagnetic);
		}
		
		if ((gravity != null) && (geomagnetic != null)) {
			if (mSensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
			//	mSensorManager.getOrientation(R, orientationMatrix);
			}
			
			if (__layout == __DEFINES.DEBUG) {
				//debugText[0][0].setText(String.format("%d", (int) Math.toDegrees(orientationMatrix[0])));
				//debugText[0][1].setText(String.format("%d", (int) Math.toDegrees(orientationMatrix[1])));
				//debugText[0][2].setText(String.format("%d", (int) Math.toDegrees(orientationMatrix[2])));
				debugText[1][0].setText(String.format("%d", (int) Math.toDegrees(gravity[0])));
				debugText[1][1].setText(String.format("%d", (int) Math.toDegrees(gravity[1])));
				debugText[1][2].setText(String.format("%d", (int) Math.toDegrees(gravity[2])));
				debugText[2][0].setText(String.format("%d", (int) Math.toDegrees(geomagnetic[0])));
				debugText[2][1].setText(String.format("%d", (int) Math.toDegrees(geomagnetic[1])));
				debugText[2][2].setText(String.format("%d", (int) Math.toDegrees(geomagnetic[2])));
			}
			
		}
		
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}
	
	public void onButtonClick(View view){
		switch (view.getId()) {
			case  R.id.buttonSubmit: {
				System.out.println(" Button pressed");
				BufferedReader textIn = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(editText.getText().toString().getBytes())));
				try {
					String input = textIn.readLine();
					BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
					clientOut.println(input);	
					
					String response = clientIn.readLine();
					textView2.setText("Server sagt: " + response);
					clientOut.flush();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				break;
			}
			case R.id.buttonChange: {
				if (__layout == 0) {
					setContentView(R.layout.debug);
					this.__layout = __DEFINES.DEBUG;
				}
				else if (__layout == 1) {
					setContentView(R.layout.activity_fernsteuerung);
					__layout = 0;
				}
				
				if (__layout == 0) {
					textView1 = (TextView) findViewById(R.id.textView2);
				    textView2 = (TextView) findViewById(R.id.textViewServerOutput);
				    editText = (EditText) findViewById(R.id.editText1);
				    btnChange = (Button) findViewById(R.id.buttonChange);
				}
				else if (__layout == 1) {
					debugText[0][0] = (TextView) findViewById(R.id.textView1);
					debugText[0][1] = (TextView) findViewById(R.id.textView2);
					debugText[0][2] = (TextView) findViewById(R.id.textView3);
					debugText[1][0] = (TextView) findViewById(R.id.textView4);
					debugText[1][1] = (TextView) findViewById(R.id.textView5);
					debugText[1][2] = (TextView) findViewById(R.id.textView6);
					debugText[2][0] = (TextView) findViewById(R.id.textView7);
					debugText[2][1] = (TextView) findViewById(R.id.textView8);
					debugText[2][2] = (TextView) findViewById(R.id.textView9);
				}	
				break;
			}
		}
	}
}
