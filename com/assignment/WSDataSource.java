package com.assignment;

import java.sql.Connection;
import java.sql.SQLException;

// a simple DataSource class
// provides connections with static methods since I want to make things simple
public class WSDataSource {
    static ConnectionPool wsConnectionPool = null;

    static public void initConnections(String url, String user, String password) throws SQLException {
        wsConnectionPool = ConnectionPool.create(url, user, password);
    }

    static public Connection getConnection() throws SQLException, Exception {
        if (wsConnectionPool == null)
        {
            throw new Exception("connection pool not initialized");
        }
        return wsConnectionPool.getConnection();
    }

    static public void releaseConnection(Connection conn) {
        if (wsConnectionPool == null)
        {
            return;
        }
        wsConnectionPool.releaseConnection(conn);
    }
}
