package com.pivotal.cf.broker.config;

import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.pivotal.cf.broker.model.Plan;

@Configuration
@ComponentScan(basePackages = "com.pivotal.cf.broker")
@EnableJpaRepositories(basePackageClasses = Plan.class)
@EnableTransactionManagement
@EntityScan(basePackageClasses=Plan.class)
public class AppConfig {

	@Autowired
	private Environment env;
	
	@Autowired 
	@Qualifier("mdsbrokerdb")
	private DataSource ds;
	
	@Bean
	@Primary
	public DataSource datasource() throws Exception {
		Cloud cloud = getCloud();
		if (cloud != null)
		{
			return ds;
		}
		else
		{
			PoolProperties p = new PoolProperties();
			org.apache.tomcat.jdbc.pool.DataSource localds = new org.apache.tomcat.jdbc.pool.DataSource();
			p.setDriverClassName("com.mysql.jdbc.Driver");
			p.setUsername("yQliqLKfKwFE44Xe");
			p.setPassword("tRa6macqZiPFoK65");
			p.setUrl("jdbc:mysql://172.23.124.84:3306");
			p.setMaxActive(5);
			p.setMinIdle(1);
			p.setMaxIdle(2);
			p.setInitialSize(1);
			localds.setPoolProperties(p);
			return localds;
		}
	}

	@Bean(name = "adminDs")
	public DataSource adminDs() throws Exception {
		PoolProperties p = new PoolProperties();
		p.setDriverClassName(env.getProperty("dba.datasource.driverClassName"));
		p.setUsername(env.getProperty("dba.datasource.username"));
		p.setPassword(env.getProperty("dba.datasource.password"));
		p.setUrl(env.getProperty("dba.datasource.url"));
		p.setMaxActive(Integer.valueOf(env.getProperty("dba.datasource.max-active")));
		p.setMinIdle(Integer.valueOf(env.getProperty("dba.datasource.min-idle")));
		p.setMaxIdle(Integer.valueOf(env.getProperty("dba.datasource.max-idle")));
		p.setInitialSize(Integer.valueOf(env.getProperty("dba.datasource.initial-size")));
		org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
		ds.setPoolProperties(p);
		return ds;
	}

	private Cloud getCloud() {
        try {
            CloudFactory cloudFactory = new CloudFactory();
            return cloudFactory.getCloud();
        } catch (CloudException ce) {
            return null;
        }
    }

}
