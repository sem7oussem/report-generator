package com.vds.ibm.batch;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vds.ibm.srvc.WowDataService;
import com.vds.ibm.bean.WowConnection;
import com.vds.ibm.bean.WowConnectionData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WowConnectionItemProcessor implements ItemProcessor<WowConnection, WowConnection> {

	private WowDataService dataService;
	
    private static final Logger log = LoggerFactory.getLogger(WowConnectionItemProcessor.class);
    
    public WowConnectionItemProcessor(WowDataService dataService) {
    	this.dataService = dataService;
    }

    @Override
    public WowConnection process(final WowConnection wowConnection) throws Exception {
            String jsonConnectionData = wowConnection.getData();
            ObjectMapper mapper = new ObjectMapper();
//            log.info("Parsing the json fro, the data field : {}", jsonConnectionData);
            WowConnectionData connectionData = mapper.readValue(jsonConnectionData, WowConnectionData.class);
            // retrieve the userId json attribute value
            String userId = connectionData.getUserid();
            String action = connectionData.getAction();
            Date loginDate =  wowConnection.getStartTime();
//            log.info("Updating user {} counter in the map.", userId);
            // get the userId from the map
            if(userId != null && !userId.equals("sysadmin") && action.equals("login")) 
            {
            dataService.handleCounterIncrement(userId,loginDate,connectionData);
            }
    	return wowConnection;
    }
}
