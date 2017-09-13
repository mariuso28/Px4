package org.dx4.json.message;

public class Dx4ResultJson {

	private Dx4StatusJson status;
	private String message;
	private Object result;

	public Dx4ResultJson() {}

	public void fail(String message)
	{
		setStatus(Dx4StatusJson.ERROR);
		setMessage(message);
	}
	
	public void warn(String message)
	{
		setStatus(Dx4StatusJson.WARN);
		setMessage(message);
	}
	
	public void success(Object result)
	{
		setStatus(Dx4StatusJson.OK);
		setResult(result);
	}
	
	public void success()
	{
		setStatus(Dx4StatusJson.OK);
	}
	
	public Dx4StatusJson getStatus() {
		return status;
	}

	public void setStatus(Dx4StatusJson status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	
}