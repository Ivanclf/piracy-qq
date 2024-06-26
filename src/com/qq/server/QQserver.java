package com.qq.server;

import com.qq.common.*;

import java.io.*;
import java.net.*;
import java.sql.ResultSet;

public class QQserver {
    private SqlHelper sqlHelper = null;
    private int port = 9999;
    private final String insertSql = "insert into QQUser values(?,?)";
    private final String idCheck = "select QQPassword from QQUser where QQuserId=?";
    private String[] paras;
    private String[] paras2;

    /**
     * 服务器主程序
     */
    public QQserver() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                User user = (User) new ObjectInputStream(socket.getInputStream()).readObject();
                System.out.println("账号： " + user.getUserId() + " 密码：" + user.getPasswd());
                Message msg = new Message();
                ObjectOutputStream fromSocket = new ObjectOutputStream(socket.getOutputStream());
                sqlHelper = new SqlHelper();
                paras = new String[]{user.getUserId()};
                ResultSet resultSet = sqlHelper.queryExecute(idCheck, paras);
                String password = null;

                //注册部分
                if (user.getIsRegister()) {
                    //注册失败，名字已被占用
                    if (resultSet.next()) {
                        msg.setMsgType(MessageType.MESSAGE_LOGIN_FAIL);
                        fromSocket.writeObject(msg);
                        socket.close();
                    } else {
                        //注册成功
                        //在数据库中增加该用户信息

                        paras2 = new String[]{user.getUserId(), user.getPasswd()};
                        sqlHelper.InsertData(insertSql, paras2);
                        msg.setMsgType(MessageType.MESSAGE_SUCCEED);
                        fromSocket.writeObject(msg);

                        ServerConClientThread serverConClientThread = new ServerConClientThread(socket);
                        ManageClientThread.addClientThread(user.getUserId(), serverConClientThread);
                        serverConClientThread.start();
                        serverConClientThread.notifyAllOtherFriends(ManageClientThread.getAllOnLineUserId());
                    }
                } else//登录部分
                {
                    resultSet = sqlHelper.queryExecute(idCheck, paras);
                    if (resultSet.next()) {
                        password = resultSet.getString("QQPassword").trim();
                    }

                    if (user.getPasswd().equals(password) && ManageClientThread.getClientThread(user.getUserId()) == null) {
                        //登录成功，开启一个新的线程连接
                        msg.setMsgType(MessageType.MESSAGE_SUCCEED);
                        fromSocket.writeObject(msg);
                        ServerConClientThread serverConClientThread = new ServerConClientThread(socket);
                        ManageClientThread.addClientThread(user.getUserId(), serverConClientThread);
                        serverConClientThread.start();
                        serverConClientThread.notifyAllOtherFriends(ManageClientThread.getAllOnLineUserId());
                    } else {
                        msg.setMsgType(MessageType.MESSAGE_LOGIN_FAIL);
                        fromSocket.writeObject(msg);
                        socket.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("网络连接异常，请检查网络后再试！");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        QQserver qq = new QQserver();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void close() {
        try {
            if (sqlHelper != null) {
                sqlHelper.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
