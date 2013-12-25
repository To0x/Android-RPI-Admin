package de.htw_berlin.Fernsteuerung;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import de.htw_berlin.Fernsteuerung.__DEFINES;

public class ConnectionHelper {
	
	private static int WifiError;
	/**
	 * Check if the right WLan (for Raspberry-Drive) is connected
	 * Use the WLan SSID to check!
	 * @return __DEFINES.WIFI.class (look for more details)
	 */
	public static int CheckWifi(Activity fromActivity) {
		 
		try
		{
			ConnectivityManager conManager = (ConnectivityManager)fromActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = conManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			
			if (networkInfo.isConnected() && networkInfo.getDetailedState() == DetailedState.CONNECTED) 
			{
				WifiManager wifiManager = (WifiManager) fromActivity.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				if (wifiInfo != null) 
				{
					if (wifiInfo.getSSID() == __DEFINES.WIFI.NETWORK_NAME) {
						WifiError = __DEFINES.WIFI.OK;
					}
					else
					{
						WifiError = __DEFINES.WIFI.WRONG_NETWORK;
					}
				}
			}
			else 
			{
				WifiError = __DEFINES.WIFI.NOT_CONNECTED;
			}
		} 
		catch (Exception e) 
		{
			WifiError = __DEFINES.WIFI.UNKNOWN_ERROR;
		}
		
		return WifiError;
	}
}
