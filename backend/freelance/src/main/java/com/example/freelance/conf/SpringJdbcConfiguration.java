package com.example.freelance.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class SpringJdbcConfiguration {
    @Bean
    public DataSource getMicrosoftSQLDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://localhost:1433;databaseName=freelance_db;encrypt=true;trustServerCertificate=true;");
        dataSource.setUsername("superadmin");
        dataSource.setPassword("admin");
        return dataSource;
    }
}
