package org.dx4.agent;

import java.util.List;

public class Dx4SMA extends Dx4MA {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7640930857736123217L;
	private List<Dx4MA> mas;
	
	
	public void setMas(List<Dx4MA> mas) {
		this.mas = mas;
	}
	public List<Dx4MA> getMas() {
		return mas;
	}

}
