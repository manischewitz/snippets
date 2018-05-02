package main.org.orgname.shedule;


import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

@Configuration
public class DatabaseConfig {

	@Bean(name="dataSource")
	public DataSource singleConnectionDataSource() throws InterruptedException{
		SingleConnectionDataSource scds = new SingleConnectionDataSource();
		scds.setDriverClassName("org.h2.Driver");
		scds.setUrl("jdbc:h2:tcp://localhost/~/application");
		scds.setUsername("root");
		scds.setPassword("root");
		scds.setSuppressClose(true);
		return scds;
	}
}
