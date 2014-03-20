package de.htw_berlin.Fernsteuerung;

import java.util.TimerTask;

import android.util.Log;
import android.webkit.WebView;

public class MemoryHelper extends TimerTask{

	private Runtime info = null;
	private MainActivity act = null;
	
	public MemoryHelper(Runtime info, WebView wv, MainActivity act) {
		this.info = info;
		this.act = act;
	}
	
	@Override
	public void run() {
		
		Log.i("MEMORY","check Memory!");
		
		if (info.freeMemory() *100L / info.totalMemory() < 20L) {
			
			act.clearMemory();
			
			Log.e("MEMORY","################################################# 5cleared");
			
			System.gc();
		}
	}

}
