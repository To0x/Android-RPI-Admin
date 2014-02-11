package de.htw_berlin.Fernsteuerung;

import java.util.TimerTask;

public class SendTimer extends TimerTask {

	private Sender sender = null;
	
	public SendTimer(Sender s)
	{
		this.sender = s;
	}
	
	@Override
	public void run() {

		this.sender.sendNow();
		
	}
	
}
