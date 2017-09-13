package org.gz.baseuser;

import org.gz.admin.GzAdmin;
import org.gz.agent.GzAgent;
import org.gz.dustbin.GzDustbin;

public enum GzRoleType {
	GZADMIN(0,GzAdmin.class),DUSTBIN(1,GzDustbin.class),AGENT(2,GzAgent.class),PLAYER(3,GzBaseUser.class);
	private int type;
	@SuppressWarnings("rawtypes")
	private Class correspondingClass;
	
	private GzRoleType(int type,@SuppressWarnings("rawtypes") Class correspondingClass)
	{
		setType(type);
		setCorrespondingClass(correspondingClass);
	}

	@SuppressWarnings("rawtypes")
	public static Class getCorrespondingClassForType(int type) {
		for (GzRoleType rt : GzRoleType.values())
			if (type == rt.getType())
				return rt.getCorrespondingClass();
		return null;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@SuppressWarnings("rawtypes")
	public Class getCorrespondingClass() {
		return correspondingClass;
	}

	public void setCorrespondingClass(@SuppressWarnings("rawtypes") Class correspondingClass) {
		this.correspondingClass = correspondingClass;
	}
}
