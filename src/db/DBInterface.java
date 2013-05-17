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

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import models.Entertainment;

public class DBInterface {
    private Properties props = new Properties();
    private String driver = "com.mysql.jdbc.Driver";
    private DataSource datasource = null;

    public DBInterface() {
        try {
            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        PoolProperties p = new PoolProperties();
        p.setUrl(props.getProperty("dbURL"));
        p.setDriverClassName(driver);
        p.setUsername(props.getProperty("dbUSERNAME"));
        p.setPassword(props.getProperty("dbPASSWD"));
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors(
          "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
          "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        datasource = new DataSource();
        datasource.setPoolProperties(p);
    }

    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = datasource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public ArrayList<Entertainment> getEntertainmentByName(String query) {
        ArrayList<Entertainment> result = new ArrayList<Entertainment>();
        Connection conn = null;
        try {
            conn = datasource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT entertainment_list.*, keyword.keyword from entertainment_list join keyword on entertainment_list.title = keyword.target where title LIKE '%" + query + "%';");

            while(rs.next()) {
                Entertainment e = new Entertainment();
                e.id = rs.getString("id");
                e.name = rs.getString("title");
                e.address = rs.getString("address");
                e.price = rs.getInt("price");
                e.rate = rs.getInt("remark");
                if (rs.getString("keyword").isEmpty()) {
                    continue;
                }
                e.setKeyword(rs.getString("keyword"));

                if (result.contains(e) == false) {
                    result.add(e);
                }
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Entertainment> getEntertainmentByKeyword(String query) {
        ArrayList<Entertainment> result = new ArrayList<Entertainment>();
        Connection conn = null;
        try {
            conn = datasource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT entertainment_list.*, keyword.keyword from entertainment_list join keyword on entertainment_list.title = keyword.target where keyword.keyword LIKE '%" + query + "::=%';");

            while(rs.next()) {
                Entertainment e = new Entertainment();
                e.id = rs.getString("id");
                e.name = rs.getString("title");
                e.address = rs.getString("address");
                e.price = rs.getInt("price");
                e.rate = rs.getInt("remark");
                e.setKeyword(rs.getString("keyword"));

                result.add(e);
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Entertainment> getRandomEntertainment(int limit) {
        ArrayList<Entertainment> result = new ArrayList<Entertainment>();
        Connection conn = null;
        try {
            conn = datasource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            while (result.size() < limit) {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT r1.*, keyword.keyword FROM entertainment_list AS r1" +
                        " JOIN (SELECT (RAND() * (SELECT MAX(id) FROM entertainment_list)) AS id) AS r2" +
                        " JOIN keyword ON r1.title = keyword.target" +
                        " WHERE r1.id >= r2.id  AND r1.remark > 35 ORDER BY r1.id ASC  LIMIT 1;");

                while(rs.next()) {
                    Entertainment e = new Entertainment();
                    e.id = rs.getString("id");
                    e.name = rs.getString("title");
                    e.address = rs.getString("address");
                    e.price = rs.getInt("price");
                    e.rate = rs.getInt("remark");
                    e.setKeyword(rs.getString("keyword"));

                    result.add(e);
                }

                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Entertainment getEntertainmentById(String id) {
        Entertainment result = new Entertainment();
        Connection conn = null;
        try {
            conn = datasource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT entertainment_list.* from entertainment_list where id = '" + id + "';");

            while(rs.next()) {
                result.id = rs.getString("id");
                result.name = rs.getString("title");
                result.address = rs.getString("address");
                result.price = rs.getInt("price");
                result.rate = rs.getInt("remark");
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
