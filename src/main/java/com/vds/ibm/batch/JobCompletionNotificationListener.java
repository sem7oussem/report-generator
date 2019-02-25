package com.vds.ibm.batch;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;

import com.vds.ibm.bean.ReportData;
import com.vds.ibm.srvc.WowDataService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
	
	private WowDataService wowDataService;

	@Autowired
	public JobCompletionNotificationListener(WowDataService wowDataService) {
		this.wowDataService = wowDataService;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED! Time to verify the results");
			String datePattern = "yyyy_MM_dd_HH_mm";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern).withZone(ZoneOffset.UTC);
			String exportPath = ".\\" + formatter.format(LocalDateTime.now());
			try {
				Files.createDirectories(Paths.get(exportPath));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			OutputStreamWriter ostw = null;
			try {
				ostw = new OutputStreamWriter(new FileOutputStream(exportPath+"\\report.txt"), "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
			Writer out = new BufferedWriter(ostw);
			for(Entry<String, ReportData> entry : wowDataService.getUserCountData().entrySet()) {
				try {
					
					out.write(entry.getKey() + " :\n" + entry.getValue().toString());
					//writeEntrySet(entry);
				} catch (UnsupportedEncodingException e) {
					log.error(e.getMessage(), e);
				} catch (FileNotFoundException e) {
					log.error(e.getMessage(), e);
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}				
			}
			    try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	private void writeEntrySet(Entry<String, ReportData> entry) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		String datePattern = "yyyy_MM_dd_HH_mm";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern).withZone(ZoneOffset.UTC);
		String exportPath = ".\\" + formatter.format(LocalDateTime.now());
		Files.createDirectories(Paths.get(exportPath));
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportPath+"\\"+entry.getKey()+".txt"), "UTF-8"));
		try {
		    out.write(entry.getKey() + " :\n" + entry.getValue().toString());
		} finally {
		    out.close();
		}
	}
}
