package com.vds.ibm.config;

import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.vds.ibm.ReportGeneratorLanucher;

@Configuration
public class DBConfiguration {

//	    @Autowired
//	    private Environment env;
	    
	    @Bean(name = "iocDataSource")
	    public DataSource iocDataSource() {
	        return DataSourceBuilder.create()
	        		.driverClassName("com.ibm.db2.jcc.DB2Driver")
	        		.url("jdbc:db2://" + ReportGeneratorLanucher.host + ":50000/IOCDB")
	        		.username(ReportGeneratorLanucher.user)
	        		.password(ReportGeneratorLanucher.password)
	        		.build();
	    }
	    
	    @Primary
	    @Bean(name = "h2DataSource")
	    @ConfigurationProperties(prefix = "spring.datasource")
	    public DataSource h2DataSource() {
	        EmbeddedDatabaseBuilder databaseBuilder = new EmbeddedDatabaseBuilder();
	        return databaseBuilder.setType(EmbeddedDatabaseType.H2).build();
	    }

	    @Bean(name = "ingestionJdbcTemplate")
	    public JdbcTemplate iocJdbcTemplate()
	    {
	        return new JdbcTemplate(iocDataSource());
	    }
	
}
