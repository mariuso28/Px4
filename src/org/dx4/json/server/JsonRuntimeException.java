package org.dx4.json.server;

public class JsonRuntimeException extends RuntimeException {


	private static final long serialVersionUID = -2688170638332409170L;

	public JsonRuntimeException(String message)
	{
		super("Json Exception : " + message);
	}
}
