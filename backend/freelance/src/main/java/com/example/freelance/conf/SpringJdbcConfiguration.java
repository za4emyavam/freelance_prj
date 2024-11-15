package com.example.freelance.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class SpringJdbcConfiguration {
    /*@Bean
    public DataSource getMicrosoftSQLDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://localhost:1433;databaseName=freelance_db;encrypt=true;trustServerCertificate=true;");
        dataSource.setUsername("superadmin");
        dataSource.setPassword("admin");
        return dataSource;
    }*/
    private final UserRoutingDataSource userRoutingDataSource;

    public SpringJdbcConfiguration(UserRoutingDataSource userRoutingDataSource) {
        this.userRoutingDataSource = userRoutingDataSource;
    }

    @Bean
    public DataSource dataSource() {
        return userRoutingDataSource;
    }

    /*Map<String, DataSource> dataSources = new ConcurrentHashMap<>();

    @Bean
    public DataSource getDataSource() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return dataSources.get(authentication.getName());
        }
        // Если пользователь не аутентифицирован, используем connect_user по умолчанию
        return dataSources.get("connection_user");
    }

    public void addUserDataSource(String username, DataSource dataSource) {
        dataSources.put(username, dataSource);
    }

    public DataSource createUserDataSource(String username, String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:sqlserver://localhost:1433;databaseName=freelance_db;encrypt=true;trustServerCertificate=true;");
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }*/

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
