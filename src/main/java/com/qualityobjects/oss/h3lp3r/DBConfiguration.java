package com.qualityobjects.oss.h3lp3r;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories( 
		basePackages = {"com.qualityobjects.oss.h3lp3r.repository"}) 
@PropertySource({ "classpath:application.yml" })
public class DBConfiguration {

	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(DBConfiguration.class);

    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource getDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
 
}