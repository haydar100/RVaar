package hu.rijkswaterstaat.rvaar;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Berkan on 10-3-2015.
 */
public class Connector {
    public static String dbName = "ID146926_mbafpp";
    public static String username = dbName;
    public static String driver = "com.mysql.jdbc.Driver";
    public static String password = "DafXFeuro6";
    public static String url = "jdbc:mysql://mysql037.hosting.combell.com";


    public static Connection createConnection(String driver, String dbNaam, String gebruikersnaam, String wachtwoord, String url) {
        try {
            Class.forName(driver).newInstance();
            Connection conn = DriverManager.getConnection(url + "/" + dbName, username, password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Connection failed", "Connector");
        }
        return null;
    }
}


