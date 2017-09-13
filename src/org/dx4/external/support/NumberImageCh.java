package org.dx4.external.support;

import java.util.StringTokenizer;


public class NumberImageCh
{
	private String number;
	private String chinese;
	private String desc;
	
	public NumberImageCh(String line)
	{
		StringTokenizer st = new StringTokenizer(line,"|");
		number = st.nextToken();
		setChinese(st.nextToken());
		desc = st.nextToken();
	}
	
	public NumberImageCh(String number, String chinese, String desc) {
		super();
		this.number = number;
		this.setChinese(chinese);
		this.desc = desc;
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setChinese(String chinese) {
		this.chinese = chinese;
	}

	public String getChinese() {
		return chinese;
	}

	@Override
	public String toString() {
		return "NumberImageCh [number=" + number + ", chinese=" + chinese
				+ ", desc=" + desc + "]";
	}
	
	
}
