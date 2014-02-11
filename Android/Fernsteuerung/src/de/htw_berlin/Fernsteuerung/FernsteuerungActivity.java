package de.htw_berlin.Fernsteuerung;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import de.htw_berlin.Fernsteuerung.R;

import de.htw_berlin.Fernsteuerung.__DEFINES.*;

public class FernsteuerungActivity extends Activity {
	
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
	
	float mGravityX = 0;
	float mGravityY = 0;
	
    float azimuth = 0;
	
	private Socket clientSocket;
	
	private int actuallyLayout = -1;
	private void loadLayout (int layout) {
		
		switch (layout) {
		
			case LAYOUT.DEBUG:
			{				
				setContentView(R.layout.debug);
				debugText = new TextView[3][3];
				debugText[0][0] = (TextView) findViewById(R.id.textView1);
				debugText[0][1] = (TextView) findViewById(R.id.textView2);
				debugText[0][2] = (TextView) findViewById(R.id.textView3);
				debugText[1][0] = (TextView) findViewById(R.id.textView4);
				debugText[1][1] = (TextView) findViewById(R.id.textView5);
				debugText[1][2] = (TextView) findViewById(R.id.textView6);
				debugText[2][0] = (TextView) findViewById(R.id.textView7);
				debugText[2][1] = (TextView) findViewById(R.id.textView8);
				debugText[2][2] = (TextView) findViewById(R.id.textView9);
				break;
			}
				
			case LAYOUT.LIVE:
			{
				setContentView(R.layout.activity_fernsteuerung);
				textView1 = (TextView) findViewById(R.id.textView2);
			    textView2 = (TextView) findViewById(R.id.textViewServerOutput);
			    editText = (EditText) findViewById(R.id.editText1);
			    btnChange = (Button) findViewById(R.id.buttonChange);
			    break;
			}
			
		
		}
		actuallyLayout = layout;
		return;
	}

	private int startRPCConnection() {
		
		//TODO unbedingt rausholn!!!
		//XXX in Thread umschreiben
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
		    StrictMode.setThreadPolicy(policy);
		}
		
		try {
			
			//TODO check if needed?
			// included Timeout for Connection!
			/*
			clientSocket = new Socket();
			clientSocket.setSoTimeout(200);
			clientSocket.connect(new InetSocketAddress(__DEFINES.SERVER_IP, __DEFINES.SERVER_PORT), 200);
			*/
			
			
			XMLRPCClient client = null;
			client = new XMLRPCClient(new java.net.URL(String.format("http://%s:%d", __DEFINES.SERVER_IP, __DEFINES.SERVER_PORT)));
			int result = 0;
			//result =  (Integer) client.call("add",2,4);
			//client.call("control","192.168.178.37");
			return result;
			
		} catch (MalformedURLException e1) {
			Toast.makeText(getApplicationContext(), e1.getMessage(), Toast.LENGTH_LONG).show();
		//} catch (XMLRPCException e1) {
		//	Toast.makeText(getApplicationContext(), e1.getMessage(), Toast.LENGTH_LONG).show();
		}
		return -1;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		int bla = this.startRPCConnection();
		Log.d("Ergebnis: ", String.valueOf(bla));
		Toast.makeText(getApplicationContext(), String.format("Result: %d", bla), Toast.LENGTH_LONG).show();
		
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		
        /*getFragmentManager().beginTransaction()
        .replace(android.R.id.content, new SettingsActivity())
        .commit();
*/        

	}
	
	/*
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		loadLayout(LAYOUT.LIVE);
		
		int errorCode;
		if ((errorCode = ConnectionHelper.CheckWifi(this)) != WIFI.OK) {
			Context c = getApplicationContext();
			CharSequence text = __DEFINES.WIFI.NOT_CONNECTED_ERROR_TEXT;
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(c, text, duration);
			toast.show();
			
			//ViewHelper.alterUser(this, errorCode);
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
	
		final int con = 10;
		
		
		this.mGravityX = event.values[1] * 2;
		this.mGravityY = event.values[0] * 2;
		if(this.mGravityX > con)
			this.mGravityX = con;
		if(this.mGravityY > con)
			this.mGravityY = con;
		if(this.mGravityX < con * (-1))
			this.mGravityX = con * (-1);
		if(this.mGravityY < con * (-1))
			this.mGravityY = con * (-1);

		mGravityX *= 4.5;
		mGravityY *= 4.5;
		
		float[] R = new float[9];
		float[] I = new float[9];
		float[] orientationMatrix = new float[3];
		
		
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			
			if (gravity[1] - event.values[1] < 10 ||gravity[1] - event.values[1] > 10) {
				System.arraycopy(event.values, 0, gravity, 0, 3);	
			}

			//gravity = lowPass(event.values.clone(), gravity);
		}
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			System.arraycopy(event.values, 0, geomagnetic,0,3);
			//geomagnetic = lowPass(event.values.clone(), geomagnetic);
		}
		
		if ((gravity != null) && (geomagnetic != null)) {
			if (mSensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
				//mSensorManager.getOrientation(R, orientationMatrix);
			}
			
			if (actuallyLayout == LAYOUT.DEBUG) {
				
				debugText[0][0].setText(String.format("%5.4f", gravity[1]));
				
				debugText[0][1].setText(String.format("%5.4f", mGravityX));
				debugText[0][2].setText(String.format("%5.4f", mGravityY));
				
				
				
				//debugText[0][0].setText(String.format("%d", (int) Math.toDegrees(orientationMatrix[0])));
				//debugText[0][1].setText(String.format("%d", (int) Math.toDegrees(orientationMatrix[1])));
				//debugText[0][2].setText(String.format("%d", (int) Math.toDegrees(orientationMatrix[2])));
				debugText[1][0].setText(String.format("%d", (int) Math.toDegrees(gravity[0])));
				debugText[1][1].setText(String.format("%d", (int) Math.toDegrees(gravity[1] / 10)));
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
		switch (view.getId()) 
		{
			case  R.id.buttonSubmit: 
			{
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
			case R.id.buttonChange: 
			{
				
				if (actuallyLayout == LAYOUT.DEBUG)
					loadLayout(LAYOUT.LIVE);
				else
					loadLayout(LAYOUT.DEBUG);

				break;
			}
		}
	}
	*/
}
