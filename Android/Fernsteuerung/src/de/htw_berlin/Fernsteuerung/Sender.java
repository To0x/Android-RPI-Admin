package de.htw_berlin.Fernsteuerung;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
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
	private final String light = "light";
	private DatagramSocket s = null;
	private DatagramPacket packet = null;
	private InetAddress local = null;
	
	private int msgLenght;
	private byte[] bmsg;
	
	private String ip;
	private int port;
	
	private static JSONObject json;
	
	public Sender(MainActivity a) {
		act = a;
		loadSettingsData();
		json = new JSONObject();
		
		try {
			s = new DatagramSocket();
			local = InetAddress.getByName(ip);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void loadSettingsData() {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(act);
		ip = sharedPref.getString("pref_key_raspip","");
		port = Integer.valueOf(sharedPref.getString("pref_key_raspport", "0"));
	}
	
	public void sendNow () {
		
		try {
			json.put(speed, act.getSpeed());
			json.put(angle, act.getGravity());
			json.put(gear, act.getGear());
			json.put(light,  act.getLight());
			
		} catch (JSONException e) {
			Toast.makeText(act.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			Log.e(this.toString(), e.getMessage(), e);
		}
		
		sendNow(json.toString());
		json.remove(speed);
		json.remove(angle);
		json.remove(gear);
		json.remove(light);
		
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
	
	/* XXX ----------------------------------------
	 * XXX NOT IN USE
	 * XXX ----------------------------------------
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
	*/
		
	private void sendNow (String msg) {
		
		Log.d("Send Now: " , msg);
			
			try {
				msgLenght = msg.length();
				bmsg = msg.getBytes("ISO-8859-1");
				
				packet = new DatagramPacket(bmsg, msgLenght, local, port);
				
				s.send(packet);
				packet = null;
				bmsg = null;
				msgLenght = 0;
				
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

	}
	
}
