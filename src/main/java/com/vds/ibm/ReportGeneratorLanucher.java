package com.vds.ibm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReportGeneratorLanucher {
	
	 public static String startDate;
	 public static String endDate;
	 public static String host;
	 public static String user;
	 public static String password;
	 
    public static void main(String[] args) throws Exception {
        startDate = args[0];
        endDate = args[1];
        host = args[2];
        user = args[3];
        password = args[4];
        SpringApplication.run(ReportGeneratorLanucher.class, args);

    }
}
