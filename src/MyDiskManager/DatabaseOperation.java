package MyDiskManager;

import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class DatabaseOperation {
    //数据库登录参数默认值
    private final String ipaddr = "100.68.1.169";
    private final int port = 3306;
    private final String database = "MyDiskManager";
    private final String username = "zhouyang";
    private final String password = "Zy@19930207";

    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    boolean connectionState=false;

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public DatabaseOperation() {
        conn = getConnect();
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating statement", e);
        }
    }

    private Connection getConnect() {
        Connection conn = null;
        String url = "jdbc:mysql://" + ipaddr + ":" + port + "/" + database + "?autoReconnect=true";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
        return conn;
    }

    private Connection getConnect(String ipaddr,int port,String database,String username,String password)
    {
        Connection conn=null;
        String url="jdbc:mysql://"+ipaddr+":"+port+"/"+database;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn=DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("sql driver error");
        } catch (SQLException e1) {
            e1.printStackTrace();
            System.out.println("sql connection information error, please recheck your input like ip/port/username/password");
        }
        return conn;
    }

    private ResultSet query(String sql) {
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    //查询MD5值，查询到返回true，查询不到返回false
    public boolean queryMD5(String md5) {
        boolean result = false;
        String sql = "SELECT * FROM FileInfo WHERE md5 = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, md5);
            rs = preparedStatement.executeQuery();
            result = rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error executing MD5 query", e);
        } finally {
            closeResultSet();
        }
        return result;
    }

    public boolean insert(NameSpace ns) {
        boolean result = false;
        String sql = "INSERT INTO FileInfo (`filename`, `length`, `hashcode`, `md5`, `lastmodified`, `recordtime`, `path`, `rank`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, ns.filename);
            preparedStatement.setLong(2, ns.length);
            preparedStatement.setLong(3, ns.hashcode);
            preparedStatement.setString(4, ns.md5);
            preparedStatement.setString(5, ns.lastmodified);
            preparedStatement.setString(6, ns.recordtime);
            preparedStatement.setString(7, ns.path);
            preparedStatement.setInt(8, ns.rank);
            preparedStatement.executeUpdate();
            result = true;
        } catch (SQLException e) {
            throw new RuntimeException("Error executing insert statement", e);
        }
        return result;
    }

    public boolean addEXIF(ImageKeyword ik)
    {
        boolean result=false;
        String sql="insert into `exif_information` (`md5`,`ImageSize`,`ProductInformation`,`GPS_Longitude`,`GPS_Latitude`,`GPS_Altitude`) VALUES ('"+ik.md5+"','"+(ik.Image_Height+" X "+ik.Image_Width)+"','"+(ik.make+"  "+ik.model)+"','"+ik.GPS_Longitude+"','"+ik.GPS_Latitude+"','"+ik.GPS_Altitude+"')";
        try {
            stmt=conn.createStatement();
            stmt.execute(sql);
            result=true;
        } catch (SQLException e) {
            result=false;
            throw new RuntimeException(e);
        }
        return result;
    }

    public boolean updateRank(String md5) {
        boolean updateStatus = false;
        String sql = "UPDATE FileInfo SET `rank` = `rank` + 1 WHERE `md5` = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, md5);
            preparedStatement.executeUpdate();
            updateStatus = true;
        } catch (SQLException e) {
            throw new RuntimeException("Error executing update statement", e);
        }
        return updateStatus;
    }

    private void closeResultSet() {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            // 记录日志
            e.printStackTrace();
        }
    }
    //程序操作完毕，释放数据库连接
    public void closeDatabase() {
        closeResultSet();
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            // 记录日志
            e.printStackTrace();
        }
    }

    //实时释放ResultSet
    private void closeDatabase(ResultSet rs)
    {
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void queryFileName(String filename)
    {
        String sql="select * from `FileInfo` where `filename` like '%"+filename+"%'";
        ResultSet rs=query(sql);
        showResultSet(rs);
        closeDatabase(rs);
    }

    public void queryName(String keyword)
    {
        String sql="select * from `FileInfo` where `path` like '%"+keyword+"%'";
        ResultSet rs=query(sql);
        showResultSet(rs);
        closeDatabase(rs);
    }

    public void showResultSet(ResultSet rs)
    {
        //       String result="";
        try {
            while (rs.next()) {
                //    System.out.print(rs.getString("filename")+"\t");
                System.out.print(rs.getString("length")+"\t");
                System.out.print(rs.getString("lastmodified")+"\t");
                System.out.print(rs.getString("hashcode")+"\t");
                System.out.print(rs.getString("md5")+"\t");
                System.out.print(rs.getString("rank")+"\t");
                System.out.print(rs.getString("path")+"\n");
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
