package com.example.diyhub;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {
    Connection con;
    static String username;
    static String pass;
    static String ip;
    String port;
    static String database;

    @SuppressLint("NewApi")
    public Connection connectionClass()
    {
        ip = "192.168.1.13";
        database = "DIYHub";
        username = "janpord";
        pass = "Wakowakoako1.";
        port = "1433";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL= "jdbc:jtds:sqlserver://"+ ip + ":"+ port+";"+ "databasename="+ database+";user="+username+";password="+pass+";";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch(Exception ex)
        {
            Log.e("Error", ex.getMessage());
        }



        return connection;
    }


}
