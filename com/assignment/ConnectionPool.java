package com.assignment;

import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

// a very simple connection pool
// in production enviroment we need to use a real one instead
public class ConnectionPool {
    private String url;
    private String user;
    private String password;
    private List<Connection> connectionPool;
    private List<Connection> usedConnections = new ArrayList<>();
    private static int INITIAL_POOL_SIZE = 1;
    
    public static ConnectionPool create(String url, String user, String password) throws SQLException {
        return new ConnectionPool(url, user, password);
    }
    
    public ConnectionPool(String _url, String _user, String _password) throws SQLException {
        url = _url;
        user = _user;
        password = _password;
        connectionPool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            connectionPool.add(createConnection(url, user, password));
        }
    }

    public Connection getConnection() throws SQLException {
        if (connectionPool.isEmpty()) {
            connectionPool.add(createConnection(url, user, password));
        }
        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        usedConnections.add(connection);
        return connection;
    }
    
    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }
    
    private static Connection createConnection(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}