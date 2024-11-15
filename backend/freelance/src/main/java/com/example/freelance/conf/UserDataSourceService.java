package com.example.freelance.conf;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class UserDataSourceService {
    public DataSource createUserDataSource(String username, String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:sqlserver://localhost:1433;databaseName=freelance_db;encrypt=true;trustServerCertificate=true;");
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }



    public boolean checkCredentialsWithConnectUser(String username, String password) {
        try (Connection connection = createUserDataSource(username, password).getConnection()) {
            return true;
        } catch (SQLException e) {
            System.err.println("Authentication failed: " + e.getMessage());
            return false;
        }
    }
}
