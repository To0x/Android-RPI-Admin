package de.htw_berlin.Fernsteuerung;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.R.bool;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class FernsteuerungActivity extends Activity implements SensorEventListener {

	private Sensor mRotVectSensor;
	private SensorManager mSensorManager;
	private float[] orientationVals=new float[3];
	private float[] mRotationMatrix=new float[16];
	private TextView textView1;
	
	private TextView textView2;
	private EditText editText;
	final int SOCKET = 9050;
	final String SERVER_IP = "192.168.0.113";
	private Socket clientSocket;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fernsteuerung);
		
		// Steht hier weil sonst eine networkonmainthreadexception kommt 
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		textView1 = (TextView) findViewById(R.id.textView2);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    mRotVectSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

		try {
			clientSocket = new Socket(SERVER_IP, SOCKET);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Failure by creating Socket: " + e);
		}
	    textView2 = (TextView) findViewById(R.id.textViewServerOutput);
	    editText = (EditText) findViewById(R.id.editText1);
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
        textView1.setText(String.valueOf("Angle" + orientationVals[2]));
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub	
	}
	
	public void onButtonClick(View view){
		if (view.getId() == R.id.buttonSubmit){
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
