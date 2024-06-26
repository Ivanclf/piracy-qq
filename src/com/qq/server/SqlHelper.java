/**
 * 功能：用于连接数据库
 */
package com.qq.server;

import java.sql.*;

public class SqlHelper {
    // 数据库变量
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://127.0.0.1:3306/QQdb";
    private String username = "root";
    @SuppressWarnings("unused")
    private String password = "123456";

    // 构造方法，用于初始化连接
    public SqlHelper() {
        try {
            // 连接驱动
            Class.forName(driver);
            // 获取连接
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    // 查询功能
    public ResultSet queryExecute(String sql, String[] paras) {
        try {
            // 创建PreparedStatement
            preparedStatement = connection.prepareStatement(sql);
            // 给prepareStatement参数赋值
            for (int i = 0; i < paras.length; i++) {
                preparedStatement.setString(i + 1, paras[i]);
            }
            //执行操作
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public Boolean InsertData(String sql, String[] paras) {

        boolean doNotError = false;
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < paras.length; i++) {
                preparedStatement.setString(i + 1, paras[i]);
            }
            doNotError = preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doNotError;
    }

    // 关闭数据库资源
    public void close() {
        // 关闭资源
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

//测试
	public static void main(String[] args) {
		SqlHelper helper = new SqlHelper();
		String sql = "select * from QQUser where 1=?";
		String[] paras = {"1"};
		ResultSet resultSet = helper.queryExecute(sql, paras);
		try {
			while(resultSet.next()){
			System.out.println(resultSet.getString("QQUserId") + " " + resultSet.getString("QQPassword"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
