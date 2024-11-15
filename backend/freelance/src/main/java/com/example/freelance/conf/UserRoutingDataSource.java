package com.example.freelance.conf;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class UserRoutingDataSource extends AbstractRoutingDataSource {

    private final Map<Object, Object> dataSources = new ConcurrentHashMap<>();


    public UserRoutingDataSource() {
        DataSource connectUserDataSource = createUserDataSource("connect_user", "connect_user_password");
        dataSources.put("connect_user", connectUserDataSource);

        this.setTargetDataSources(dataSources);
        this.setDefaultTargetDataSource(connectUserDataSource);

    }

    // Метод для добавления новых DataSource
    public void addUserDataSource(String username, DataSource dataSource) {
        dataSources.put(username, dataSource);
        this.setTargetDataSources(dataSources);
        this.afterPropertiesSet();
    }

    public DataSource createUserDataSource(String username, String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:sqlserver://localhost:1433;databaseName=freelance_db;encrypt=true;trustServerCertificate=true;");
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }



    @Override
    protected Object determineCurrentLookupKey() {
        // Логика выбора источника данных, например, из контекста пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName(); // Возвращает имя текущего пользователя
        }
        // Если пользователь не аутентифицирован, используем connect_user по умолчанию
        return "connect_user";
    }
}
