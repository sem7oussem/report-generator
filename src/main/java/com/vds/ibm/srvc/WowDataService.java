package com.vds.ibm.srvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.vds.ibm.bean.ReportData;
import com.vds.ibm.bean.WowConnection;
import com.vds.ibm.bean.WowConnectionData;

@Service
public class WowDataService {

	private Map<String, ReportData> userCountermap = new HashMap<>();
	
	public void handleCounterIncrement(final String userId, final Date loginDate,WowConnectionData connectionData) {
       Integer userCounter;
		ReportData userReport = userCountermap.get(userId);
		if(userReport == null )
			{ userCounter = null;} 
		else 
			{ userCounter = userReport.getLoginCount();}
        if(userCounter == null) {
        	String details = "userid=" + connectionData.getUserid() + ", action=" + connectionData.getAction() + ", issystemadmin=" + connectionData.isIssystemadmin()
    				+ ", groups=" + connectionData.getGroups() + ", isadmin=" + connectionData.isIsadmin() + ", date= " + loginDate +"\n";
        	ReportData init = new ReportData(1,loginDate,details);
            userCountermap.put(userId,init);
        } else {
           userCounter ++;
           ReportData journal = userCountermap.get(userId);
           //ArrayList<String>  details = journal.getDetailedConnections();
           journal.setLoginCount(userCounter);
           journal.setLastLogin(loginDate);
          // details.add("userid=" + connectionData.getUserid() + ", action=" + connectionData.getAction() + ", issystemadmin=" + connectionData.isIssystemadmin()
//			+ ", groups=" + connectionData.getGroups() + ", isadmin=" + connectionData.isIsadmin() + ", date= " + loginDate +"\n");
           //journal.setDetailedConnections(details);
           userCountermap.put(userId, journal);
        }
	}
	
	public Map<String, ReportData> getUserCountData() {
		return userCountermap;
	}

}
