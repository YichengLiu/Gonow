package db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import models.Entertainment;

public class DBInterface {
    private Properties props = new Properties();
    private String driver = "com.mysql.jdbc.Driver";
    private Connection conn = null;
    private static DBInterface instance = new DBInterface();

    public DBInterface() {
        try {
            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            conn = DriverManager.getConnection(props.getProperty("dbURL"), props.getProperty("dbUSERNAME"), props.getProperty("dbPASSWD"));
            if(!conn.isClosed()){
                System.out.println("Succeeded connecting to the Database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public ArrayList<Entertainment> getEntertainmentByName(String query) {
        ArrayList<Entertainment> result = new ArrayList<Entertainment>();

        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from entertainment_list where title LIKE '%" + query + "%';");

            while(rs.next()) {
                Entertainment e = new Entertainment();
                e.id = rs.getString("id");
                e.name = rs.getString("title");
                e.address = rs.getString("address");
                e.price = rs.getInt("price");
                e.rate = rs.getInt("remark");
                e.keyWords = new HashMap<String, Double>();

                result.add(e);
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Entertainment getEntertainmentById(String id) {
        Entertainment result = new Entertainment();

        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from entertainment_list where id = '" + id + "';");

            while(rs.next()) {
                result.id = rs.getString("id");
                result.name = rs.getString("title");
                result.address = rs.getString("address");
                result.price = rs.getInt("price");
                result.rate = rs.getInt("remark");
                result.keyWords = new HashMap<String, Double>();
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static DBInterface getInstance() {
        return instance;
    }
}
