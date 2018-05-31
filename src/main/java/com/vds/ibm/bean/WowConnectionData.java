package com.vds.ibm.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class WowConnectionData {
	
	// data={"isadmin":"true","action":"login","issystemadmin":"true","groups":"[\"SystemAdmins\"]","sessionid":"Escs3qYBvSB5m_2KvtWdaZk","userid":"sysadmin"}
	
	private boolean isadmin;
	private String action;
	private boolean issystemadmin;
	private String groups;
	private String sessionid;
	private String userid;
	
	public boolean isIsadmin() {
		return isadmin;
	}
	public void setIsadmin(boolean isadmin) {
		this.isadmin = isadmin;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public boolean isIssystemadmin() {
		return issystemadmin;
	}
	public void setIssystemadmin(boolean issystemadmin) {
		this.issystemadmin = issystemadmin;
	}
	public String getGroups() {
		return groups;
	}
	public void setGroups(String groups) {
		this.groups = groups;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	@Override
	public String toString() {
		return "userid=" + userid + ", action=" + action + ", issystemadmin=" + issystemadmin
				+ ", groups=" + groups + ", sessionid=" + sessionid + ", isadmin=" + isadmin;
	}

}
