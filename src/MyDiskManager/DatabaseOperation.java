package MyDiskManager;

import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class DatabaseOperation {
    //数据库登录参数默认值
    final static String ipaddr="192.168.0.102";
    final static int port=3306;
    final static String database="MyDiskManager";
    final static String username="zhouyang";
    final static String password="zhouyang@2021";

    //考虑到反复申请数据库连接效率会很低，故连接上数据库后，主要是循环使用stmt和rs，conn建议保持常开
    Connection conn=null;
    Statement stmt=null;
    ResultSet rs=null;
    boolean connectionState=false;

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    DatabaseOperation()
    {
        conn=getConnect(ipaddr,port,database,username,password);
        try {
            stmt=conn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Connection getConnect()
    {
        Connection conn=null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try {
                conn= DriverManager.getConnection("jdbc:mysql://"+ipaddr+":"+port+"/"+database,username,password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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
    public boolean queryMD5(String md5)
    {
        boolean result=false;
        String sql="select * from FileInfo where md5='"+md5+"'";
        try {
            stmt=conn.createStatement();
            rs=stmt.executeQuery(sql);
            if(rs.next())
            {
                result=true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        closeDatabase(rs);
        return result;
    }

    public boolean insert(NameSpace ns)
    {
        String sql="insert into `FileInfo` (`filename`,`length`,`hashcode`,`md5`,`lastmodified`,`path`,`rank`) values ('"+ns.filename+"',"+ns.length+","+ns.hashcode+",'"+ns.md5+"','"+ns.lastmodified+"',\""+ns.path+"\","+ns.rank+")";
        boolean result=false;
        try {
            stmt=conn.createStatement();
            stmt.execute(sql);
            result=true;
        } catch (SQLException e) {
            System.out.println(sql);
            throw new RuntimeException(e);
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

    public boolean updateRank(String md5)
    {
        boolean updateStatus=false;
        String sql="update `FileInfo` set `rank`=`rank`+1 where md5='"+md5+"'";
        try {
            stmt=conn.createStatement();
            stmt.executeUpdate(sql);
            updateStatus=true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return updateStatus;

    }

    //程序操作完毕，释放数据库连接
    private void closeDatabase(Connection conn,Statement stmt,ResultSet rs)
    {
        try {
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeDatabase()
    {
        try {
            if (rs != null) {
                rs.close();
            }
            if(stmt!=null)
            {
                stmt.close();
            }
            if(conn!=null)
            {
                conn.close();
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
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
