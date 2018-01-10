package com.baihui.difu.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.baihui.difu.entity.User;
import com.baihui.difu.util.DB;

public class UserDao extends BaseDAO{

	/**
     * @param
     * @return
     * @throws Exception
     * @throws
     * @Description: 批量插入数据
     */
    public void save(List<User> users) throws Exception {

        String sql = "INSERT INTO USER(userName,baihuiId,email,job)" +
                " VALUES (?,?,?,?)"; //4
        List<Object[]> params = new ArrayList<Object[]>();
        for (int i = 0; i < users.size(); i++) {
            Object[] objects = {users.get(i).getUserName(), users.get(i).getBaihuiId(), users.get(i).getEmail(),
            		users.get(i).getJob()};
            params.add(objects);
        }
        try {
            super.sqlBatchExecute(sql, params);
        } catch (SQLException e) {
            log.error("插入数据库失败");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public String getTableName() {
		return "user";
	}

	public User getUserBybaihuiId(String ownerId) throws ClassNotFoundException {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user=new User();
        String sql = "select email,job from user where baihuiId=? ";
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            Object[] ownerIds={ownerId};
            fillStatement(preparedStatement, ownerIds);
            resultSet = preparedStatement.executeQuery();
           
            if (resultSet.next()) {
            	user.setEmail(resultSet.getString("email")) ;
            	user.setJob(resultSet.getString("job"));
            }
        } catch (SQLException e) {
            log.error("根据baihuiId获取用户", e);
        } finally {
            try {
                DB.closeAll(resultSet, preparedStatement, connection);
            } catch (SQLException e) {
                log.error("关闭异常！", e);
            }
        }
		return user;
	}

	public void delete() {
		String sql = "delete from user";
		try {
			super.sqlBatchExecute(sql, null);
		} catch (Exception e) {
			log.info("删除user表失败");
			e.printStackTrace();
		}
	}
	/***
	 * 根据邮箱获取baihuiID
	 * 
	 * */
	public String getbaihuiIDByEmail(String email) throws ClassNotFoundException {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user=new User();
        String sql = "select baihuiId from user where email=? ";
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            Object[] emails={email};
            fillStatement(preparedStatement, emails);
            resultSet = preparedStatement.executeQuery();
           
            if (resultSet.next()) {
            	return resultSet.getString("baihuiId") ;
            }
        } catch (SQLException e) {
            log.error("根据baihuiId获取用户", e);
        } finally {
            try {
                DB.closeAll(resultSet, preparedStatement, connection);
            } catch (SQLException e) {
                log.error("关闭异常！", e);
            }
        }
		return "";
	}


}
