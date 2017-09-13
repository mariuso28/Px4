package org.dx4.agent;

import java.util.List;

public class Dx4ZMA extends Dx4SMA {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7783503893640483812L;
	private List<Dx4SMA> smas;
	
	public void setSmas(List<Dx4SMA> smas) {
		this.smas = smas;
	}
	public List<Dx4SMA> getSmas() {
		return smas;
	} 
}
