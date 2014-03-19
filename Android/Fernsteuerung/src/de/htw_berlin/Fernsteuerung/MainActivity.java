package de.htw_berlin.Fernsteuerung;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.ToggleButton;


public class MainActivity extends Activity {

	//XXX
	//TODO
	/*
	 * Programmstart optimieren (direkt MainActivity)
	 * RPC-Connection einbauen
	 *    --> Shutdown Raspberry
	 *    --> Turn Camera ON / OFF
	 *    
	 * Camera-Check (Memory-Leek)
	 * Automatisches aufbauen der WLAN-Verbindung
	 * 
	 */
	
	//--------------------------------------------
	// DEFINES
	//--------------------------------------------
	private final static boolean SEND = true;
	private final static String VIDEOPATH = "http://192.168.55.1:8080/javascript_simple.html";
		
	//--------------------------------------------
	// TIMER
	//--------------------------------------------
	private final int TimerTick = 100;
	private Timer timer = null;
	private TimerTask myTimer = null;
	private TimerTask memoryHelper = null;
	
	//--------------------------------------------
	// SPEED
	//--------------------------------------------
	private final int maxSpeed = 100;
	private final int minSpeed = 0;
	private final int speedDifference = 5;
	private int speed = 0;
	
	//--------------------------------------------
	// VIEW-ELEMENTS
	//--------------------------------------------
	private Switch switchGear = null;
	private SeekBar speedBar = null;
	private WebView wvCamera = null;
	private ToggleButton tbLight = null;

	
	//--------------------------------------------
	// OTHER
	//--------------------------------------------
	private OnTouchListener l = null;
	private SensorHelper sens = null;
	private Sender sender = null;
	private Runtime info = null;
	

	//--------------------------------------------
	// GETTER
	//--------------------------------------------
	public int getSpeed() {
		return this.speed;
	}
	
	public float getGravity() {
		return sens.getcurrentGravity();
	}
	
	public boolean getGear() {
		return switchGear.isChecked();
	}
	
	public int getLight(){
		if (tbLight.isChecked())
		{	
			return 1;
		}
		return 0;
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
	
	public class Listener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			wvCamera.stopLoading();
			clearMemory();
			wvCamera = null;
			System.gc();
			
			initVideoStream();
			wvCamera.loadUrl(VIDEOPATH);
			return true;
		}
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	public void initVideoStream() {
		wvCamera = (WebView) findViewById(R.id.webViewCamera);
		wvCamera.getSettings().setJavaScriptEnabled(true);
		wvCamera.getSettings().setLoadWithOverviewMode(true);
		
		wvCamera.setWebViewClient(new WebViewClient());	
		wvCamera.setWebChromeClient(new WebChromeClient());
		
		wvCamera.setDrawingCacheEnabled(false);
		wvCamera.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		wvCamera.getSettings().setAppCacheEnabled(false);
		
		wvCamera.setOnTouchListener(l);
	}
	
	public void clearMemory() {
		
		wvCamera.clearCache(true);
		wvCamera.destroyDrawingCache();
		wvCamera.clearHistory();
		wvCamera.freeMemory();
	}
	
	
	//--------------------------------------------
	// LIVECYCLE
	//--------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		info = Runtime.getRuntime();
		
		l = new Listener();
		setContentView(R.layout.control);
		switchGear = (Switch) findViewById(R.id.switchGear);
		speedBar = (SeekBar) findViewById(R.id.seekBarSpeed);
		tbLight = (ToggleButton) findViewById(R.id.toggleButtonLight);
		
		speedBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			//TODO do for switchGear not for seekbar
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				speed = progress;
				if (speed == 0){
					switchGear.setClickable(true);
				} else {
					switchGear.setClickable(false);
				}
			}
		});
		
		initVideoStream();
		sens = new SensorHelper(this);
		sens.initSensoring();

		sender = new Sender(this);
		sender.loadSettingsData();
		myTimer = new SendTimer(sender);
//		memoryHelper = new MemoryHelper(info, wvCamera, this);
	}
		
	@Override
	protected void onPause() {
		super.onPause();
		
		sens.stopSensoring();
		
		timer.cancel();
		timer.purge();
		timer = null;
		
		wvCamera.stopLoading();
		myTimer = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		sens.startSensoring();
		timer = new Timer(false);
		myTimer = new SendTimer(sender);
//		memoryHelper = new MemoryHelper(info, wvCamera,this);
		sender.loadSettingsData();
		
//		timer.schedule(memoryHelper, 0, 1000);
		
		if (SEND)
			timer.schedule(myTimer,1000	 , TimerTick);
		
		
		wvCamera.loadUrl(VIDEOPATH);
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
