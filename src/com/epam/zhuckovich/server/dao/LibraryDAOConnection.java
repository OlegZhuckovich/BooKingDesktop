package com.epam.zhuckovich.server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class LibraryDAOConnection {

    private static final String connectionURL = "jdbc:mysql://localhost:3306/BooKingDatabase?autoReconnect=true&useSSL=false";
    private static Connection sqlConnection;
    private static LibraryDAOConnection instance;

    private LibraryDAOConnection(){}

    public static LibraryDAOConnection getInstance() throws SQLException {
        if(instance==null){
            Properties property  = new Properties();
            property.put("user","root");
            property.put("password","2017");
            property.put("characterEncoding","UTF-8");
            property.put("useUnicode","true");

            sqlConnection = DriverManager.getConnection(connectionURL,property);
        }
        return instance;
    }

    public static Connection getConnection(){
        return sqlConnection;
    }
}
