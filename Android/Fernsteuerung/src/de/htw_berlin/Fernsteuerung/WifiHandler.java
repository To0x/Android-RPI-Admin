package de.htw_berlin.Fernsteuerung;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

public class WifiHandler extends BroadcastReceiver {

	public static void WifiOn(Context context) 
	{
		ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		
		if (!(wifiInfo.getState() == NetworkInfo.State.CONNECTED) || wifiInfo.getState() == NetworkInfo.State.CONNECTING) 
		{
			WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			manager.setWifiEnabled(true);
		}
	}
	
	public static boolean checkWifi(Context context) {
		WifiManager mainWifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo currentWifi = mainWifi.getConnectionInfo();
		
		if (currentWifi != null) {
			if (currentWifi.getSSID() != null) {
				if (currentWifi.getSSID() == MainActivity.WIFI)
					;
				return true;
			}
		}
		return false;
	}

	public static boolean connectWifi(Context context) {
		boolean connected = false;
		WifiConfiguration wifi = new WifiConfiguration();
		wifi.SSID = "\"" + MainActivity.WIFI + "\"";
		wifi.preSharedKey = "\"" + _secure.__def.wifiPass + "\"";

		WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		manager.addNetwork(wifi);
		

		List<WifiConfiguration> list = manager.getConfiguredNetworks();
		for (WifiConfiguration i : list) {
			if (i.SSID != null && i.SSID.equals("\"" + MainActivity.WIFI + "\"")) {
				manager.disconnect();
				manager.enableNetwork(i.networkId, true);
				manager.reconnect();
				connected = true;
				break;
			}
		}

		return connected;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		//TODO Maybe stop sending Data
		
		Toast.makeText(context, "Network loss - trying to Reconnect!", Toast.LENGTH_LONG).show();
		
		NetworkInfo info = (NetworkInfo) intent
				.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
		Log.d("CAR", "NETWORK CHANGED! " + info.getDetailedState() + ": "
				+ info.toString());

		if (info.getDetailedState() == DetailedState.DISCONNECTED) {

			Log.d("CAR", "DISCONNECTED FROM WLAN!");
			WifiOn(context);

		} else if (info.getDetailedState() == DetailedState.CONNECTED) {

			if (!checkWifi(context)) {
				if (!connectWifi(context))
					Log.e("CAR","UNABLE TO RECONNECT!!!");
			}

		}
	}

}
