package org.dx4.external.persistence;

import java.util.StringTokenizer;

public class NumberX
{
	private Integer number;
	private String code;
	private String cdesc;
	private String desc;
	
	public NumberX()
	{
	}
	
	public NumberX(String line)
	{
		StringTokenizer st = new StringTokenizer(line,"|");
		setCode(st.nextToken());
		setCdesc(st.nextToken());
		desc = st.nextToken();
		number = Integer.valueOf(stripLeadingZeros(code));
	}
	
	private String stripLeadingZeros(String code)
	{
		int index=0;
		while (index<code.length()-1)
		{
			if (code.charAt(index)!='0')
			{
				break;
			}
			index++;
		}
		return code.substring(index);
	}
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public Integer getNumber() {
		return number;
	}


	public void setNumber(Integer number) {
		this.number = number;
	}


	public String getCdesc() {
		return cdesc;
	}


	public void setCdesc(String cdesc) {
		this.cdesc = cdesc;
	}


	@Override
	public String toString() {
		return "NumberX [number=" + number + ", code=" + code + ", cdesc="
				+ cdesc + ", desc=" + desc + "]";
	}

	
	
}
