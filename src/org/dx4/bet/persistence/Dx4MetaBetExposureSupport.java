package org.dx4.bet.persistence;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4MetaBetExpoOrder;
import org.dx4.secure.domain.Dx4Role;

public class Dx4MetaBetExposureSupport {
	private static final Logger log = Logger.getLogger(Dx4MetaBetExposureSupport.class);
	
	public static String getSqlCountString(String playDateStr,Dx4Role role,String agentCode,List<Dx4MetaBetExpoOrder> ordering)
	{
		String sql = getCountString(playDateStr,ordering);
		sql += " and code in (\n";
		sql += getValidPlayersSql(role,agentCode);
		sql += ")";
		sql += getGroupString(ordering);
		sql += "\n)";
		log.trace(sql);
		return sql;
	}
	
	public static String getSqlString(String playDateStr,Dx4Role role,String agentCode,List<Dx4MetaBetExpoOrder> ordering)
	{
		String sql = getExpoString(playDateStr,ordering);
		sql += " and code in (\n";
		sql += getValidPlayersSql(role,agentCode);
		sql += ")";
		sql += getGroupString(ordering);
		sql += getOrderString(ordering);
		log.trace(sql);
		return sql;
	}
	
	private static String getOrderString(List<Dx4MetaBetExpoOrder> ordering)
	{
		if (ordering.isEmpty())
			return "";
		String orderStr = " order by ";
		for (Dx4MetaBetExpoOrder order : ordering)
		{
			orderStr += order.name();
			if (order.equals(Dx4MetaBetExpoOrder.tbet) || order.equals(Dx4MetaBetExpoOrder.winexpo))
				orderStr += " desc";
			orderStr += ",";
		}
		return orderStr.substring(0,orderStr.length()-1);
		
	}
	
	private static String getGroupString(List<Dx4MetaBetExpoOrder> ordering)
	{
		String str = "\ngroup by ";
		for (Dx4MetaBetExpoOrder order : ordering)
		{
			if (order.equals(Dx4MetaBetExpoOrder.winexpo) || order.equals(Dx4MetaBetExpoOrder.tbet))
					continue;
			str += order.name() + ",";
		}
		if (ordering.contains(Dx4MetaBetExpoOrder.code) && !ordering.contains(Dx4MetaBetExpoOrder.choice))
			return str + "username,user.id";	// code only get contact details
		
		return str.substring(0,str.length()-1);
	}
	
	private static String getCountString(String playDateStr, List<Dx4MetaBetExpoOrder> ordering)
	{
		return "select count(*) from \n(" + getExpoString(playDateStr,ordering);
	}
	
	private static String getExpoString(String playDateStr, List<Dx4MetaBetExpoOrder> ordering)
	{
		String sql="";
		if (ordering.contains(Dx4MetaBetExpoOrder.choice) && ordering.contains(Dx4MetaBetExpoOrder.code))
			sql = "\nselect choice,sum(betexpo) as tbet,sum(winexpo) as winexpo,code from choice,user where metabetid in\n";
		else
		if (ordering.contains(Dx4MetaBetExpoOrder.choice))
			sql = "\nselect choice,sum(betexpo) as tbet,sum(winexpo) as winexpo from choice,user where metabetid in\n";
		else
		if (ordering.contains(Dx4MetaBetExpoOrder.code))			// code only get contact details
			sql = "\nselect sum(betexpo) as tbet,sum(winexpo) as winexpo,code,username,id,(select contact from profile where userid=user.id) from choice,user where metabetid in\n";
		else			// meaning less just tbet and winexpo
			sql = "\nselect sum(betexpo) as tbet,sum(winexpo) as winexpo from choice,user where metabetid in\n";   
		
		sql += "(select id from metabet where playerid = user.id and playgameid in \n" +
		"(select id from playgame where playdate='" + playDateStr + "') \n)";
		
		return sql;
	}
	
	private static String getValidPlayersSql(Dx4Role role,String agentCode)
	{
		/*
		 * select code from user where role='ROLE_PLAY' and 
((user.parentCode = 'c0')
or
(user.parentCode in 
(select code from user where parentcode = 'c0'))
or
(user.parentCode in 
(select code from user where parentcode in 
(select code from user where parentcode = 'c0')))
or
(user.parentCode in 
(select code from user where parentcode in 
(select code from user where parentcode in 
(select code from user where parentcode = 'c0'))))
or
(user.parentCode in 
(select code from user where parentcode in 
(select code from user where parentcode in 
(select code from user where parentcode in 
(select code from user where parentcode = 'c0')))))
)
		 */
		
		String sql = "\nselect code from user where role='ROLE_PLAY' and (";
		sql += getValidPlayersSql(1,agentCode);
		if (role.equals(Dx4Role.ROLE_AGENT))
			return sql + ")\n";
		sql += "or\n" + getValidPlayersSql(2,agentCode);
		if (role.equals(Dx4Role.ROLE_MA))
			return sql + ")\n";
		sql += "or\n" + getValidPlayersSql(3,agentCode);
		if (role.equals(Dx4Role.ROLE_SMA))
			return sql + ")\n";
		sql += "or\n" + getValidPlayersSql(4,agentCode);
		if (role.equals(Dx4Role.ROLE_ZMA))
			return sql + ")\n";
		sql += "or\n" + getValidPlayersSql(5,agentCode);
		if (role.equals(Dx4Role.ROLE_COMP))
			return sql + ")\n";
		
		return null;
	}
	
	
	private static String getValidPlayersSql(int tier,String parentCode)
	{
		if (tier == 1)
		{
			return "(user.parentCode = '" + parentCode + "')";
		}
		String sql = "(user.parentCode in ";
		int close = 0;
		tier--;
		while (tier>1)
		{
			sql += "\n(select code from user where parentcode in ";
			close++;
			tier--;
		}
		sql += "\n(select code from user where parentcode = '" + parentCode + "')";
		while (close>=0)
		{
			sql += ")";
			close--;
		}
		return sql + "\n";
	}
	
	public static void main(String[] args)
	{
		// String sql = getValidPlayersSql(Dx4Role.ROLE_COMP,"c0");
		List<Dx4MetaBetExpoOrder> ordering = new ArrayList<Dx4MetaBetExpoOrder>();
		ordering.add(Dx4MetaBetExpoOrder.tbet);
		ordering.add(Dx4MetaBetExpoOrder.choice);
	    String sql = getSqlString("2014-05-06 00:00:00.0",Dx4Role.ROLE_COMP,"c0",ordering);
	    log.trace(sql);
	}
}
