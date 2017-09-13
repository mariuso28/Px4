package org.dx4.json.message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public enum Dx4GameTypeJson{

	D2(2,"2 Digit 1st Prize Only",2,Dx4PayOutTypeJson.First,false,false),
	D4Big(4,"4D 1st/2nd/3rd/Special/Consolation Prizes",4,Dx4PayOutTypeJson.First,false,true),  
	D4Small(4,"4D 1st/2nd/3rd Prizes",4,Dx4PayOutTypeJson.First,false,false), 
	ABCA(3,"3D 1st Prize Only",3,Dx4PayOutTypeJson.First,false,false),
	ABCC(3,"3D 1st/2nd/3rd Prizes",3,Dx4PayOutTypeJson.First,false,true),								// true	
	D4IBoxBig(4,"4D - IBox 1st/2nd/3rd/Special/Consolation Prizes",4,Dx4PayOutTypeJson.FirstIB24,true,true),  
	D4IBoxSmall(4,"4D - IBox 1st/2nd/3rd Prizes",4,Dx4PayOutTypeJson.FirstIB24,true,false), 
	D4BoxBig(4,"4D - Box 1st/2nd/3rd/Special/Consolation Prizes",4,Dx4PayOutTypeJson.FirstIB24,true,true),
	D4BoxSmall(4,"4D - Box 1st/2nd/3rd Prizes",4,Dx4PayOutTypeJson.FirstIB24,true,false);
	
	private static int idx;
	private int index;
	private int optionNum;
	private String desc;
	private int digits;
	private Dx4PayOutTypeJson topPayout;
	private boolean boxType;
	private boolean big;
	
	Dx4GameTypeJson(int options,String desc,int digits,Dx4PayOutTypeJson topPayout,boolean boxType,boolean big)
	{
		optionNum = options;
		index = Dx4GameTypeJson.getIdx()+1;
		Dx4GameTypeJson.setIdx(index);
		setDesc(desc);
		setDigits(digits);
		setTopPayout(topPayout);
		setBoxType(boxType);
		setBig(big);
	}
	
	public double calcStake(String choice,double stake)
	{
		if (this.equals(D4BoxBig) || this.equals(D4BoxSmall))
		{
			int countDigits = createCombos(choice).size();
			return countDigits * stake;
		}			
		return stake;
	}
	
	public static HashSet<String> createCombos(String num)
	{
		HashSet<String> combs = new HashSet<String>();
		for (String comb : combos(num))
			combs.add(comb);
		return combs;
	}
	
	private static List<String> combos(String num)
	{
		List<String> combs = new ArrayList<String>();
		if (num.length()==1)
		{
			combs.add(num);
			return combs;
		}
		for (int i=0; i<num.length(); i++)
		{
			String combo = num.substring(i,i+1);
			String rem = remNum(num,i);
			List<String> rems = combos(rem);
			for (String r : rems)
				combs.add(combo + r);
		}
		return combs;
	}

	private static String remNum(String num, int index) {
		String rem = "";
		for (int i=0; i < num.length(); i++)
		{
			if (i!=index)
				rem += num.charAt(i);
		}
		return rem;
	}

	public int getOptionNum()
	{
		return optionNum;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDigits(int digits) {
		this.digits = digits;
	}

	public int getDigits() {
		return digits;
	}

	public static void setIdx(int idx) {
		Dx4GameTypeJson.idx = idx;
	}

	public static int getIdx() {
		return idx;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public Dx4PayOutTypeJson getTopPayout() {
		return topPayout;
	}

	public void setTopPayout(Dx4PayOutTypeJson topPayout) {
		this.topPayout = topPayout;
	}

	public boolean isBoxType() {
		return boxType;
	}

	public void setBoxType(boolean boxType) {
		this.boxType = boxType;
	}

	public boolean isBig() {
		return big;
	}

	public void setBig(boolean big) {
		this.big = big;
	}

}
