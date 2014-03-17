package de.htw_berlin.Fernsteuerung;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Enumeration;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class Sender {

	private MainActivity act = null;
	
	//--------------------------------------------
	// JSON-TEXT
	//--------------------------------------------
	private final String speed = "speed";
	private final String angle = "angle";
	private final String gear = "gear";
	
	private static JSONObject json;
	
	private String ip;
	private int port;
	
	public Sender(MainActivity a) {
		act = a;
		loadSettingsData();
		json = new JSONObject();
	}
	
	public void loadSettingsData() {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(act);	
		ip = sharedPref.getString("pref_key_raspip","");
		port = 5001;//Integer.valueOf(sharedPref.getString("pref_key_raspport", ""));
	}
	
	public void sendNow () {
		
		try {
			json.put(speed, act.getSpeed());
			json.put(angle, act.getGravity());
			json.put(gear, act.getGear());
			
		} catch (JSONException e) {
			Toast.makeText(act.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			Log.e(this.toString(), e.getMessage(), e);
		}
		
		sendNow(json.toString());
		json.remove(speed);
		json.remove(angle);
		json.remove(gear);
		
	}
	
	public static String getBroadcast() throws SocketException {
	    System.setProperty("java.net.preferIPv4Stack", "true");
	    for (Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces(); niEnum.hasMoreElements();) {
	        NetworkInterface ni = niEnum.nextElement();
	        if (!ni.isLoopback()) {
	            for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses()) {
	                return interfaceAddress.getBroadcast().toString().substring(1);
	            }
	        }
	    }
	    return null;
	}
	
	private void sendBroadcast(String msg) {
		
		//TODO funktoiniert nicht!
		
		try {
			DatagramSocket socket = new DatagramSocket(5001);
			socket.setBroadcast(true);
			InetAddress local = InetAddress.getByName(getBroadcast());
			DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(),
			    local, 5001);
			socket.send(packet);
		} catch (IOException e) {
			Toast.makeText(act.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			Log.e(this.toString(), e.getMessage(), e);
		}

	}
	
	private void sendNow (String msg) {
		
		Log.d("Send Now: " , msg);
		
		try {
			DatagramSocket s = new DatagramSocket();
			InetAddress local = InetAddress.getByName(ip);
			
			int msgLenght = msg.length();
			byte[] bmsg = msg.getBytes("ISO-8859-1");
			
			DatagramPacket packet = new DatagramPacket(bmsg, msgLenght, local, port);
			
			s.send(packet);
			
			
		} catch (SocketException e) {
			Log.e(this.toString(), e.getMessage(), e);
		} catch (UnknownHostException e) {
			Log.e(this.toString(), e.getMessage(), e);
		} catch (IOException e) {
			Log.e(this.toString(), e.getMessage(), e);
		}
	}
	
}
