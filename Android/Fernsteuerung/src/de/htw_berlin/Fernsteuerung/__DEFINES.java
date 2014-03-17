package de.htw_berlin.Fernsteuerung;

public final class __DEFINES {
	protected final static String SERVER_IP = "192.168.55.1";
	protected final static int SERVER_PORT = 5001;
	
	public static final class LAYOUT {
	
		protected final static int LIVE = 0;
		protected final static int DEBUG = 1;
	}
	
	/**
	 * WIFI Class to Handle the Errors with the WIFI-Connection
	 * @author Master
	 *
	 */
	public static final class WIFI {
		/**
		 * Connection is ok and the Network is correct.
		 */
		protected static final int OK = 0x00;
		/**
		 * Smartphone is not connected to any network - maybe WLan is off.
		 */
		protected final static int NOT_CONNECTED = 0x01;
		protected final static String NOT_CONNECTED_ERROR_TEXT = "You are not connected to an WLAN\nPlease Turn On your WIFI.";
		/**
		 * The Smartphone is connected to the wrong Network.
		 */
		protected final static int WRONG_NETWORK = 0x02;
		protected final static String WRONG_NETWORK_ERROR_TEXT = String.format("You are connected to a Wrong Netowrk\nPlease connect to %s",__DEFINES.WIFI.NETWORK_NAME);
		/**
		 * The Credentiols which are sent are false
		 */
		protected final static int FAILED_AUTH = 0x03;
		protected final static String FAILED_AUTH_ERROR_TEXT = "The Authentification with the Network failes\nPlease try again.";
		/**
		 * There is an unknown Error with the Connection -> try again!
		 */
		protected final static int UNKNOWN_ERROR = 0x04;
		protected final static String UNKNOWN_ERROR_TEXT = "An Error with the WIFI occured\nPlease try again.";
		
		protected final static String NETWORK_NAME = "RaspberryDrive";
	}
}
