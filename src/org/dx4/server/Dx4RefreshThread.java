package org.dx4.server;

import org.dx4.services.Dx4Services;

public class Dx4RefreshThread extends Thread
{
	private Dx4Services dx4Services;

	public void setDx4Services(Dx4Services dx4Services) {
		this.dx4Services = dx4Services;
	}

	public Dx4Services getDx4Services() {
		return dx4Services;
	}
	
	
}
