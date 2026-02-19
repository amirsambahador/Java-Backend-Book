package org.j2os.common;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;

public class JDBC {
    private static final BasicDataSource DATA_SOURCE = new BasicDataSource();

    static {
        DATA_SOURCE.setDriverClassName("com.mysql.jdbc.Driver");
        DATA_SOURCE.setUrl("jdbc:mysql://localhost:3306/j2os");
        DATA_SOURCE.setUsername("root");
        DATA_SOURCE.setPassword("");
        DATA_SOURCE.setMaxTotal(100);
    }

    private JDBC() {
    }

    public static Connection getConnection() throws Exception {
        Connection connection = DATA_SOURCE.getConnection();
        connection.setAutoCommit(false);
        return connection;
    }
}
