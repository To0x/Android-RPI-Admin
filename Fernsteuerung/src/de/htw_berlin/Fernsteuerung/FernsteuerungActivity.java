package de.htw_berlin.Fernsteuerung;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
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

import org.xmlrpc.android.*;

public class FernsteuerungActivity extends Activity implements SensorEventListener {

	private Sensor mRotVectSensor;
	private SensorManager mSensorManager;
	private float[] orientationVals=new float[3];
	private float[] mRotationMatrix=new float[16];
	private TextView textView1;
	
	private TextView textView2;
	private EditText editText;
	
	private float calibratedX = 0, calibratedY = 0;
	private SeekBar seekHorizontal, seekVertical;
	private Button btnCalibrate;
	private Button btnChange;
	
	final int SOCKET = 9050;
	final String SERVER_IP = "192.168.43.19";
	private Socket clientSocket;
	
	private int __layout = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (__layout == 0)
			setContentView(R.layout.activity_fernsteuerung);
		else if (__layout == 1)
			setContentView(R.layout.angle_rotation);
		
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
			textView1 = (TextView) findViewById(R.id.ServerResponse);
			seekHorizontal = (SeekBar) findViewById(R.id.seekHorizontal);
			seekVertical = (SeekBar) findViewById(R.id.seekVertical);
			btnCalibrate = (Button) findViewById(R.id.buttonReset);
			btnChange = (Button) findViewById(R.id.buttonChange);
			seekHorizontal.setProgress(50);
			seekVertical.setProgress(50);
		}
			
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    mRotVectSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    
	    //---------------------- RPC -----------------------//
	    XMLRPCClient client = null;
		try {
			client = new XMLRPCClient(new java.net.URL("http://192.168.178.42:9000"));
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	    int result = 0;
		try {
			result = (Integer) client.call("add",2,4);
		} catch (XMLRPCException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		textView1.setText(result);
		
		try {
			// included Timeout for Connection!
			clientSocket = new Socket();
			clientSocket.setSoTimeout(200);
			clientSocket.connect(new InetSocketAddress(SERVER_IP, SOCKET), 200);
		} catch (Exception e) {
			System.out.println("Failure by creating Socket: " + e);
		}
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
		 mSensorManager.registerListener(this, mRotVectSensor, SensorManager.SENSOR_DELAY_GAME);	}

	@Override
	public void onSensorChanged(SensorEvent event) {		
		SensorManager.getRotationMatrixFromVector(mRotationMatrix,event.values);
        SensorManager.remapCoordinateSystem(mRotationMatrix,SensorManager.AXIS_X, SensorManager.AXIS_Z, mRotationMatrix);
        SensorManager.getOrientation(mRotationMatrix, orientationVals);
        orientationVals[0]=(float)Math.toDegrees(orientationVals[0]);
        orientationVals[2]=(float)Math.toDegrees(orientationVals[2]);
        orientationVals[1]=(float)Math.toDegrees(orientationVals[1]);
        if (__layout == 0)
        	textView1.setText(String.format("X: %.0f, Y: %.0f, Z: %.0f", orientationVals[1], orientationVals[2], orientationVals[0]));
        else if (__layout == 1) {
        	seekHorizontal.setProgress(50 - (int) ((orientationVals[2] - calibratedY) / 180 * 100));
        	seekVertical.setProgress(50 - (int) ((orientationVals[0] - calibratedX) / 180 * 100));
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
			case R.id.buttonReset: {
				System.out.println("Button RESET!");
				calibratedX = orientationVals[0];
				calibratedY = orientationVals[2];
				seekHorizontal.setProgress(50);
				seekVertical.setProgress(50);
				break;
			}
			case R.id.buttonChange: {
				if (__layout == 0) {
					setContentView(R.layout.angle_rotation);
					this.__layout = 1;
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
					seekHorizontal = (SeekBar) findViewById(R.id.seekHorizontal);
					seekVertical = (SeekBar) findViewById(R.id.seekVertical);
					btnCalibrate = (Button) findViewById(R.id.buttonReset);
					btnChange = (Button) findViewById(R.id.buttonChange);
					seekHorizontal.setProgress(50);
					seekVertical.setProgress(50);
				}	
				break;
			}
		}
	}
}
