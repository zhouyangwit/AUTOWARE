package MyWallet;

import javax.swing.plaf.nimbus.State;
import java.sql.*;

public class MyDatabase {
    MyDatabase()
    {
        getStatement();


    }
    final static String ipaddr="192.168.137.86";
    final static int port=3306;
    final static String database="AUTOWARE";
    final static String username="zhouyang";
    final static String password="zhouyang@2021";

    Connection conn=null;
    Statement stmt=null;
    ResultSet rs=null;
    boolean connectionState=false;

    private Connection getConnect()
    {
        Connection conn=null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try {
                conn= DriverManager.getConnection("jdbc:mysql://"+ipaddr+":"+port+"/"+database,username,password);
                stmt=conn.createStatement();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    private Statement getStatement()
    {
        getConnect();
        try {
            stmt=conn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stmt;
    }

    public ResultSet query(String sql)
    {
        reset_rs();
        try {
            rs=stmt.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }

    public void reset_rs()
    {
        if(rs!=null)
        {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
