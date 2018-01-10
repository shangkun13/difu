package com.baihui.difu.util;
/**
 * 定义数据库连接和关闭
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.*;

public class DB {
	public static Logger log=Logger.getLogger(DB.class);
	
	
	
	/**
	 * 创建数据库链接
	 */
	public static Connection getConnection()  throws SQLException, ClassNotFoundException{
		  log.info("BaseDAO/getConnection() begin.");
	        String jdbc_driver = "com.mysql.jdbc.Driver";

	        //测试环境
	        String jdbc_url = null;
	        String jdbc_username = null;
	        String jdbc_password = null;
	        jdbc_url = "jdbc:mysql://192.168.111.141:3306/difu?characterEncoding=utf-8";
	        jdbc_username = "bhdc001admin";
	        jdbc_password = "baihuizhadmin";


	        // 正式环境
//	        jdbc_url="jdbc:mysql://192.168.101.100:3306/difu?characterEncoding=utf-8";  
//			jdbc_username="root";
//			jdbc_password="mysql#kabahui";  
	        log.info("jdbc_url:[" + jdbc_url + "]");
	        log.info("jdbc_username:[" + jdbc_username + "]");
	        log.info("jdbc_password:[" + jdbc_password + "]");
	        Class.forName(jdbc_driver);

	        log.info("BaseDAO/getConnection() end");
	        return DriverManager.getConnection(jdbc_url, jdbc_username, jdbc_password);
	}
	/**
	 * 关闭数据库链接
	 */
	public static void closeConnection(Connection conn){
		log.info("DB/closeConnection() method beginning!");
		try {
			if(conn!=null && !conn.isClosed()){
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			log.info("DB/closeConnection() method end!");
		}
	}
	
	/**
	 * 关闭执行语句
	 * @param ps
	 */
	public static void closeStatement(PreparedStatement ps){
		log.info("DB/closeStatement() method beginning!");
		try {
			if(ps!=null){
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			log.info("DB/closeStatement() method end!");
		}
		
	}
	
	/**
	 * 关闭结果集
	 */
	public static void closeResultSet(ResultSet rs) {
		log.info("DB/closeResultSet() method beginning!");
		try {
			if (rs != null) {
				rs.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			log.info("DB/closeResultSet() method end!");
		}
	}
	
	/**
     * 关闭全部(ResultSet,Statement,Connection)
     *
     * @throws SQLException
     * @Title: closeAll void
     * @author jason
     * @date 2013-11-30 上午10:05:40
     */
    public static void closeAll(ResultSet rs, PreparedStatement pstmt, Connection conn) throws SQLException {
        closeResultSet(rs);
        closeStatement(pstmt);
        closeConnection(conn);
    }

    public void closeAll(PreparedStatement pstmt, Connection conn) throws SQLException {
        closeStatement(pstmt);
        closeConnection(conn);
    }

}
