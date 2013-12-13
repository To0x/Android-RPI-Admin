package de.htw_berlin.Fernsteuerung;

public final class __DEFINES {
	protected final static String SERVER_IP = "127.0.0.1";
	protected final static int SERVER_PORT = 5000;
	protected final static int DEBUG = 1;
	
	/**
	 * WIFI Class to Handle the Errors with the WIFI-Connection
	 * @author Master
	 *
	 */
	protected final class WIFI {
		/**
		 * Connection is ok and the Network is correct.
		 */
		protected final static int OK = 0x00;
		/**
		 * Smartphone is not connected to any network - maybe WLan is off.
		 */
		protected final static int NOT_CONNECTED = 0x01;
		/**
		 * The Smartphone is connected to the wrong Network.
		 */
		protected final static int WRONG_NETWORK = 0x02;
		/**
		 * The Credentiols which are sent are false
		 */
		protected final static int FAILED_AUTH = 0x03;
		/**
		 * There is an unknown Error with the Connection -> try again!
		 */
		protected final static int UNKNOWN_ERROR = 0x04;
	}
}
