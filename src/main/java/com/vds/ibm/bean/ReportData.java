package com.vds.ibm.bean;
import java.util.ArrayList;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportData {
	Integer loginCount ;
	Date lastLogin;
	ArrayList <String> detailedConnections;
	
	
	public ReportData(Integer loginCount, Date lastLogin , String detailedConnection) {
		super();
		this.loginCount = loginCount;
		this.lastLogin = lastLogin;
		this.detailedConnections = new  ArrayList<String>();
		this.detailedConnections.add(detailedConnection);
		
	}
	
	
	
	public Integer getLoginCount() {
		return loginCount;
	}
	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	

	public ArrayList<String> getDetailedConnections() {
		return detailedConnections;
	}



	public void setDetailedConnections(ArrayList<String> detailedConnections) {
		this.detailedConnections = detailedConnections;
	}



	@Override
	public String toString() {
		return "Nombre de connexions=" + loginCount + "\nDernière connexion =" + lastLogin + "\nJournal de connexions :\n"
				+ detailedConnections;
	}
	
}
