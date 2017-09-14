package org.dx4.json.message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.dx4.game.d2.Dx4GameD2A;
import org.dx4.game.d2.Dx4GameD2B;
import org.dx4.game.d2.Dx4GameD2C;
import org.dx4.game.d2.Dx4GameD2CC;
import org.dx4.game.d2.Dx4GameD2D;
import org.dx4.game.d2.Dx4GameD2E;
import org.dx4.game.d2.Dx4GameD2EX;
import org.dx4.game.d3.Dx4GameABCA;
import org.dx4.game.d3.Dx4GameABCB;
import org.dx4.game.d3.Dx4GameABCC;
import org.dx4.game.d3.Dx4GameABCCC;
import org.dx4.game.d3.Dx4GameABCD;
import org.dx4.game.d3.Dx4GameABCE;
import org.dx4.game.d4.Dx4GameD4A;
import org.dx4.game.d4.Dx4GameD4B;
import org.dx4.game.d4.Dx4GameD4Big;
import org.dx4.game.d4.Dx4GameD4BoxBig;
import org.dx4.game.d4.Dx4GameD4BoxSmall;
import org.dx4.game.d4.Dx4GameD4C;
import org.dx4.game.d4.Dx4GameD4CC;
import org.dx4.game.d4.Dx4GameD4D;
import org.dx4.game.d4.Dx4GameD4E;
import org.dx4.game.d4.Dx4GameD4IBoxBig;
import org.dx4.game.d4.Dx4GameD4IBoxSmall;
import org.dx4.game.d4.Dx4GameD4Small;

@SuppressWarnings("rawtypes")
public enum Dx4GameTypeJson{

	
	D4Big(4,"4D 1st/2nd/3rd/Special/Consolation Prizes",4,Dx4PayOutTypeJson.First,false,true,"4B",Dx4GameD4Big.class),  
	D4Small(4,"4D 1st/2nd/3rd Prizes",4,Dx4PayOutTypeJson.First,false,false,"4S",Dx4GameD4Small.class), 	
	D4A(4,"4D 1st Prize Only",4,Dx4PayOutTypeJson.First,false,false,"4A",Dx4GameD4A.class), 	
	D4C(4,"4D In First/Second/Third",4,Dx4PayOutTypeJson.First,false,false,"4C/SN",Dx4GameD4C.class), 	
	D4D(4,"4D Special",4,Dx4PayOutTypeJson.Spec,false,false,"4D",Dx4GameD4D.class), 
	D4E(4,"4D Consolation",4,Dx4PayOutTypeJson.Cons,false,false,"4E",Dx4GameD4E.class), 
	D4B(4,"4D 2nd Prize Only",4,Dx4PayOutTypeJson.Second,false,false,"4SB/4B",Dx4GameD4B.class), 
	D4CC(4,"4D 3rd Prize Only",4,Dx4PayOutTypeJson.Third,false,false,"4SC/4C",Dx4GameD4CC.class), 

	ABCA(3,"3D 1st Prize Only",3,Dx4PayOutTypeJson.First,false,false,"3A/A",Dx4GameABCA.class),
	ABCC(3,"3D 1st/2nd/3rd Prizes",3,Dx4PayOutTypeJson.First,false,true,"3C/N",Dx4GameABCC.class),		
	ABCB(3,"3D 2nd Prize Only",3,Dx4PayOutTypeJson.Second,false,false,"3SB/3B",Dx4GameABCB.class),
	ABCCC(3,"3D 3rd Prize Only",3,Dx4PayOutTypeJson.Third,false,false,"3SC/3C",Dx4GameABCCC.class),
	ABCD(3,"3D Special",3,Dx4PayOutTypeJson.Spec,false,false,"3DC",Dx4GameABCD.class),
	ABCE(3,"3D Consolation",3,Dx4PayOutTypeJson.Cons,false,false,"3E",Dx4GameABCE.class),
	
	D2A(2,"2D 1st Prize Only",2,Dx4PayOutTypeJson.First,false,false,"2A/EA",Dx4GameD2A.class),
	D2C(3,"2D In 1st/2nd/3rd Prizes",2,Dx4PayOutTypeJson.First,false,true,"2C/EN",Dx4GameD2C.class),
	D2EX(3,"2D In 1st/2nd/3rd/Special/Consolation Prizes",2,Dx4PayOutTypeJson.Cons,false,true,"2B/EX",Dx4GameD2EX.class),
	D2B(2,"2D 2nd Prize Only",2,Dx4PayOutTypeJson.Second,false,false,"2SB/EB",Dx4GameD2B.class),
	D2CC(2,"3rd Prize Only",2,Dx4PayOutTypeJson.Third,false,false,"2SC/EC",Dx4GameD2CC.class),
	D2D(3,"2D Special",3,Dx4PayOutTypeJson.Spec,false,false,"2D",Dx4GameD2D.class),
	D2E(3,"2D Consolation",3,Dx4PayOutTypeJson.Cons,false,false,"2E",Dx4GameD2E.class),
	
	D4IBoxBig(4,"4D - IBox 1st/2nd/3rd/Special/Consolation Prizes",4,Dx4PayOutTypeJson.FirstIB24,true,true,"IBoxBig",Dx4GameD4IBoxBig.class),  
	D4IBoxSmall(4,"4D - IBox 1st/2nd/3rd Prizes",4,Dx4PayOutTypeJson.FirstIB24,true,false,"IBoxSmall",Dx4GameD4IBoxSmall.class), 
	D4BoxBig(4,"4D - Box 1st/2nd/3rd/Special/Consolation Prizes",4,Dx4PayOutTypeJson.FirstIB24,true,true,"BoxBig",Dx4GameD4BoxBig.class),
	D4BoxSmall(4,"4D - Box 1st/2nd/3rd Prizes",4,Dx4PayOutTypeJson.FirstIB24,true,false,"BoxSmall",Dx4GameD4BoxSmall.class);
	
	private static int idx;
	private int index;
	private int optionNum;
	private String desc;
	private int digits;
	private Dx4PayOutTypeJson topPayout;
	private boolean boxType;
	private boolean big;
	private String shortName;
	private Class correspondingClass;
	
	
	Dx4GameTypeJson(int options,String desc,int digits,Dx4PayOutTypeJson topPayout,boolean boxType,boolean big,String shortName,Class correspondingClass)
	{
		optionNum = options;
		index = Dx4GameTypeJson.getIdx()+1;
		Dx4GameTypeJson.setIdx(index);
		setDesc(desc);
		setDigits(digits);
		setTopPayout(topPayout);
		setBoxType(boxType);
		setBig(big);
		setShortName(shortName);
		setCorrespondingClass(correspondingClass);
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

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public void setOptionNum(int optionNum) {
		this.optionNum = optionNum;
	}

	public Class getCorrespondingClass() {
		return correspondingClass;
	}

	public void setCorrespondingClass(Class correspondingClass) {
		this.correspondingClass = correspondingClass;
	}
	
	

}
