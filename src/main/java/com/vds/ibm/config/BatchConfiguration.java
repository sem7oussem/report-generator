package com.vds.ibm.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import com.vds.ibm.srvc.WowDataService;
import com.vds.ibm.ReportGeneratorLanucher;
import com.vds.ibm.batch.JobCompletionNotificationListener;
import com.vds.ibm.batch.WowConnectionItemProcessor;
import com.vds.ibm.bean.WowConnection;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    // tag::readerwriterprocessor[]
    
    @Autowired
    @Qualifier("iocDataSource")
    private DataSource iocDataSource;
    
    @Autowired
    private WowDataService wowDataService;
    
    @Bean(destroyMethod="")
    public JdbcCursorItemReader<WowConnection> reader() throws Exception {
    	String sqlStatement ="select ID, PRODUCT_NAME, PERSISTENT_ID, PATH, OUTPUT_DIR, TYPE, SUBTYPE, VALUE, START_TIME, END_TIME, DATA, LASTUPDATEDATE "
				+ "from IOC.SLM_METRIC"
				+ " WHERE START_TIME BETWEEN '" + ReportGeneratorLanucher.startDate + "' AND '"+ReportGeneratorLanucher.endDate+"'";
//    			"select ID, PRODUCT_NAME, PERSISTENT_ID, PATH, OUTPUT_DIR, TYPE, SUBTYPE, VALUE, START_TIME, END_TIME, DATA, LASTUPDATEDATE "
//				+ "from IOC.SLM_METRIC"
//				+ " WHERE START_TIME BETWEEN '2018-05-01' AND '2018-05-14'";
    			
    			
    			
        return new JdbcCursorItemReaderBuilder<WowConnection>()
        		.dataSource(iocDataSource)
                .name("reader")
        		.fetchSize(1000000)
        		.ignoreWarnings(true)
        		.sql(sqlStatement)
        		.rowMapper(new RowMapper<WowConnection>() {
					
					@Override
					public WowConnection mapRow(ResultSet rs, int arg1) throws SQLException {
						WowConnection connection = new WowConnection();
						connection.setId(rs.getLong(1));
						connection.setProductName(rs.getString(2));
						connection.setPersistentId(rs.getString(3));
						connection.setPath(rs.getString(4));
						connection.setOutputDir(rs.getString(5));
						connection.setType(rs.getString(6));
						connection.setSubtype(rs.getString(7));
						connection.setValue(rs.getInt(8));
						connection.setStartTime(rs.getDate(9));
						connection.setEndTime(rs.getDate(10));
						connection.setData(rs.getString(11));
						connection.setLastupdate(rs.getDate(12));
						return connection;
					}
				})
            .build();
    }

    @Bean
    public WowConnectionItemProcessor processor() {
        return new WowConnectionItemProcessor(wowDataService);
    }
    
    // end::readerwriterprocessor[]
    
    @Bean
    public JobCompletionNotificationListener listener() {
    	return new JobCompletionNotificationListener(wowDataService);
    }

    // tag::jobstep[]
    @Bean
    public Job importUserJob(Step step1) {
        return jobBuilderFactory.get("generateReport")
            .incrementer(new RunIdIncrementer())
            .listener(listener())
            .flow(step1)
            .end()
            .build();
    }

    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("step1")
            .<WowConnection, WowConnection> chunk(1000)
            .reader(reader())
            .processor(processor())
            .build();
    }
    
    @Bean
    public ResourcelessTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }
    // end::jobstep[]
}
