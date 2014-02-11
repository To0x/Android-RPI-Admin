package de.htw_berlin.Fernsteuerung;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;


public class MainActivity extends Activity {

	private SensorHelper sens = null;
	private Timer timer;
	private TimerTask myTimer = null;
	private Sender sender = null;
	private final int TimerTick = 100;
	
	
	//--------------------------------------------
	// SPEED
	//--------------------------------------------
	private final int maxSpeed = 50;
	private final int minSpeed = 0;
	private final int speedDifference = 5;
	private int speed = 0;

	
	Button btnSpeed = null;
	Button btnBreak = null;
	SeekBar seekBarGravity = null;
	Switch switchGear = null;
	
	public int getSpeed() {
		return this.speed;
	}
	
	public float getGravity() {
		return sens.getcurrentGravity();
	}
	
	public boolean getGear() {
		return switchGear.isChecked();
	}
	
	public void onButtonClick(View view){
		
		switch (view.getId()) 
		{
			case  R.id.btnSpeed: 
			{
				if (speed != maxSpeed)
					speed += speedDifference;
				
				switchGear.setClickable(false);
				
				break;
			}
			case R.id.btnBreak: 
			{
				if (speed != minSpeed)
					speed -= speedDifference;
				
				if (speed == minSpeed)
					switchGear.setClickable(true);
				
				break;
			}
		}
	}
	
	public void setSeekBarProgress(int progress) {
		seekBarGravity.setProgress(progress);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.control);
		btnSpeed = (Button) findViewById(R.id.btnSpeed);
		btnBreak = (Button) findViewById(R.id.btnBreak);
		seekBarGravity = (SeekBar) findViewById(R.id.seekBarGravity);
		switchGear = (Switch) findViewById(R.id.switchGear);
		
		//TODO R�ckw�rtsgangswitch nur bei speed=0 aktivieren
		
		sens = new SensorHelper(this);
		sens.initSensoring();
		
		sender = new Sender(this);
		myTimer = new SendTimer(sender);
		
		//sender.sendBroadcast("Raspcar?"); --> is set to private (cause function is failure)
	}
	

	@Override
	protected void onPause() {
		super.onPause();
		
		sens.stopSensoring();
		
		timer.cancel();
		timer.purge();
		timer = null;
		
		myTimer = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		sens.startSensoring();
		timer = new Timer(false);
		myTimer = new SendTimer(sender);
		timer.schedule(myTimer,1000	 , TimerTick);
		
		sender.loadSettingsData();
	}

	//--------------------------------------------
	// MENU
	//--------------------------------------------
	public enum optionsMenu {
		SETTINGS("Settings"),
		SHUTDOWN("Shutdown Raspberry");
		
		private String text;

		private optionsMenu(String text) {
			this.text = text;
		}
		
		public String toString() {
			return this.text;
		}
		
		public static optionsMenu getEnum (String enumText) {
			for (optionsMenu option : optionsMenu.values()) {
				if (option.toString().equals(enumText))
					return option;
			}
			return null;
		}
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		for (optionsMenu option : optionsMenu.values()) {
			menu.add(option.toString());
		}
		
		return super.onCreateOptionsMenu(menu);
		
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		
		switch (optionsMenu.getEnum(item.getTitle().toString())) {
			
			case SETTINGS: {
				Intent intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				break;
			}
				
			case SHUTDOWN:
				break;
			
			}

		return super.onOptionsItemSelected(item);
		
	}
	
	
}