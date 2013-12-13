package de.htw_berlin.Fernsteuerung;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.URISyntaxException;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import de.htw_berlin.Fernsteuerung.__DEFINES;

public class RpcConnection {
	
	private XMLRPCClient client;
	private Socket clientSocket;
	
	public RpcConnection() {
		this.client = null;
		try
		{
			client = new XMLRPCClient(new java.net.URI(String.format("http://%s:%d", __DEFINES.SERVER_IP, __DEFINES.SERVER_PORT)));
			clientSocket = new Socket();
			clientSocket.setSoTimeout(200);
			clientSocket.connect(new InetSocketAddress(__DEFINES.SERVER_IP, __DEFINES.SERVER_PORT), 200);
		}
		catch (URISyntaxException e) {
			
		}
		catch (SocketException e) {
			
		}
		catch (IOException e) {
			
		}
		// Constructor
	}
	
	public Object call(String funcName) {
		Object result = null;
		try
		{
			result = client.call(funcName);
		}
		catch (XMLRPCException e) {
			//TODO : retry?
			return null;
		}
		return result;
	}
}
