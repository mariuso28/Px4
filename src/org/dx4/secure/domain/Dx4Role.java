package org.dx4.secure.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.admin.Dx4Admin;
import org.dx4.agent.Dx4Agent;
import org.dx4.agent.Dx4Comp;
import org.dx4.agent.Dx4MA;
import org.dx4.agent.Dx4SMA;
import org.dx4.agent.Dx4ZMA;
import org.dx4.player.Dx4Player;

public enum Dx4Role implements Serializable{
	
		ROLE_PLAY("Player",0,'p',"Player",Dx4Player.class,"FFD6D6"),
		ROLE_AGENT("Agent",1,'a',"Agent",Dx4Agent.class,"FFF7D6"),
		ROLE_MA("Master Agent",2,'m',"MA",Dx4MA.class,"E9FFD6"),
		ROLE_SMA("Super Master Agent",3,'s',"SMA",Dx4SMA.class,"D6FFEE"),
		ROLE_ZMA("Corporate Master Agent",4,'z',"ZMA",Dx4ZMA.class,"D6ECFF"),
		ROLE_COMP("Company",5,'c',"Company",Dx4Comp.class,"E3D6FF"),
		ROLE_ADMIN("Admin",6,'x',"Admin",Dx4Admin.class,"F7D6FF");
		
		private static final Logger log = Logger.getLogger(Dx4Role.class);
		private String desc;
		private int rank;					// don't use ordinal cos we might mess with the enum order
		private Character code;
		private String shortCode;
		@SuppressWarnings("rawtypes")
		private Class correspondingClass;
		private String color;
		private static HashMap<Character,Dx4Role> codeMap;
		
		@SuppressWarnings("rawtypes")
		private Dx4Role(String desc,int rank,char code,String shortCode,Class correspondingClass,String color)
		{
			setRank(rank);
			setDesc(desc);
			setCode(code);
			setColor(color);
			setCorrespondingClass(correspondingClass);
			setShortCode(shortCode);
			Dx4Role.addCodeMap(this,code);
		}
		
		
		public String getColor() {
			return color;
		}



		public void setColor(String color) {
			this.color = color;
		}



		private static void addCodeMap(Dx4Role role,Character code)
		{
			if (codeMap==null)									// static initialization dont work
				codeMap=new HashMap<Character,Dx4Role>();
			codeMap.put(code, role);
		}
		
		public static Dx4Role getRoleForCode(String code)
		{
			Character ch = '?';
			for (int index=code.length()-1; index>=0; index--)
			{
				ch = code.charAt(index);
				if (!Character.isDigit(ch))
					break;
			}
			return codeMap.get(ch);
		}
		
		@SuppressWarnings("rawtypes")
		public static Class getRoleClassForCode(String code)
		{
			Dx4Role role = getRoleForCode(code);
			return role.getCorrespondingClass();
		}
		
		private void setDesc(String desc)
		{
			this.desc = desc;
		}
		
		public void setRank(int rank) {
			this.rank = rank;
		}

		public int getRank() {
			return rank;
		}

		public String getDesc()
		{
			return desc;
		}
		
		public boolean isAgentRole()
		{
			return this.equals(ROLE_COMP) || this.equals(ROLE_ZMA) || this.equals(ROLE_SMA)
				|| this.equals(ROLE_MA) || this.equals(ROLE_AGENT);
		}
		
		public List<Dx4Role> getSubMemberRoles()
		{
			List<Dx4Role> roles = new ArrayList<Dx4Role>();
			if (this.equals(ROLE_ADMIN))
			{
				roles.add(Dx4Role.ROLE_COMP);
				return roles;
			}
			
			roles.add(ROLE_PLAY);
			if (this.equals(ROLE_AGENT))
				return roles;
			roles.add(0,ROLE_AGENT);
			if (this.equals(ROLE_MA))
				return roles;
			roles.add(0,ROLE_MA);
			if (this.equals(ROLE_SMA))
				return roles;
			roles.add(0,ROLE_SMA);
			if (this.equals(ROLE_ZMA))
				return roles;
			roles.add(0,ROLE_ZMA);
			if (this.equals(ROLE_COMP))
				return roles;
			
			log.error("getSubMemberRoles : unknown high level role : " + this);
			
			return roles;
		}

		public void setCode(Character code) {
			this.code = code;
		}

		public Character getCode() {
			return code;
		}

		public void setShortCode(String shortCode) {
			this.shortCode = shortCode;
		}

		public String getShortCode() {
			return shortCode;
		}

		public void setCorrespondingClass(@SuppressWarnings("rawtypes") Class correspondingClass) {
			this.correspondingClass = correspondingClass;
		}

		@SuppressWarnings("rawtypes")
		public Class getCorrespondingClass() {
			return correspondingClass;
		}


		public List<Dx4Role> getAllRoles()
		{
			List<Dx4Role> roles = new ArrayList<Dx4Role>();
			roles.add(this);
			if (rank==0)
				return roles;
			
			if (rank>0)
			{
				roles.add(1,ROLE_PLAY);
			}
			if (rank<2)
				return roles;
			if (rank>1)
			{
				roles.add(1,ROLE_AGENT);
			}
			if (rank==2)
				return roles;
			if (rank>2)
			{
				roles.add(1,ROLE_MA);
			}
			if (rank==3)
				return roles;
			if (rank>3)
			{
				roles.add(1,ROLE_SMA);
			}
			if (rank==4)
				return roles;
			if (rank>4)
			{
				roles.add(1,ROLE_ZMA);
			}
			if (rank==5)
				return roles;
			if (rank>5)
			{
				roles.add(1,ROLE_COMP);
			}
			if (rank==6)
				return roles;
			
			log.error("getRoles : unknown high level role : " + this);
			
			return new ArrayList<Dx4Role>();
		}

		
}
