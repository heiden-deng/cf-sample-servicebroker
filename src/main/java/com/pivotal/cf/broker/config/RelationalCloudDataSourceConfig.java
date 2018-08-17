package com.pivotal.cf.broker.config;

import javax.sql.DataSource;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.config.java.ServiceScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ServiceScan
public class RelationalCloudDataSourceConfig extends AbstractCloudConfig {

    @Bean(name = "mdsbrokerdb")
    public DataSource dataSource() {
        return connectionFactory().dataSource();
    }

}