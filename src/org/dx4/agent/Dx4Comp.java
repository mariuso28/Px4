package org.dx4.agent;

import java.util.List;

public class Dx4Comp extends Dx4ZMA 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2528600169681440470L;
	private List<Dx4ZMA> zmas;

	public void setZmas(List<Dx4ZMA> zmas) {
		this.zmas = zmas;
	}

	public List<Dx4ZMA> getZmas() {
		return zmas;
	}
}
