package com.qq.server;

import com.qq.common.*;

import java.io.*;
import java.net.*;
import java.sql.ResultSet;

public class QQserver {
    SqlHelper sqlHelper = null;
    int port = 9999;

    //关闭资源
    public QQserver() {
        try {

            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User user = (User) ois.readObject();
                System.out.println("账号： " + user.getUserId() + " 密码：" + user.getPasswd());
                //测试登录
                Message msg = new Message();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                // System.out.println(m.getSender()+"已经连上");
                //测试非数据库部分
                // if(u.getUserId().equals("22") || u.getUserId().equals("33") || u.getUserId().equals("44"))
                //数据库部分
                sqlHelper = new SqlHelper();
                String sql = "select QQPassword from QQUser where QQuserId=?";
                String[] paras = {user.getUserId()};
                ResultSet resultSet;
                String password = null;

                //注册部分
                if (user.getIsRegister()) {
                    resultSet = sqlHelper.queryExecute(sql, paras);
                    //注册失败，名字已被占用
                    if (resultSet.next()) {
                        msg.setMsgType(MessageType.MESSAGE_LOGIN_FAIL);
                        oos.writeObject(msg);
                        socket.close();
                    } else {
                        //注册成功
                        //在数据库中增加该用户信息
                        String insertSql = "insert into QQUser values(?,?)";
                        String[] paras2 = {user.getUserId(), user.getPasswd()};
                        sqlHelper.InsertData(insertSql, paras2);
                        msg.setMsgType(MessageType.MESSAGE_SUCCEED);
                        oos.writeObject(msg);

                        ServerConClientThread serverConClientThread = new ServerConClientThread(socket);
                        ManageClientThread.addClientThread(user.getUserId(), serverConClientThread);
                        serverConClientThread.start();

                        System.out.println("注册用户成功，测试是否开始下一个： 用户: " + user.getUserId());
                        serverConClientThread.notifyAllOtherFriends(ManageClientThread.getAllOnLineUserId());
                    }
                } else//登录部分
                {
                    resultSet = sqlHelper.queryExecute(sql, paras);
                    if (resultSet.next()) {
                        password = resultSet.getString("QQPassword").trim();
                    }

                    if (user.getPasswd().equals(password) && ManageClientThread.getClientThread(user.getUserId()) == null) {
                        //登录成功，开启一个新的线程连接
                        msg.setMsgType(MessageType.MESSAGE_SUCCEED);
                        oos.writeObject(msg);
                        ServerConClientThread serverConClientThread = new ServerConClientThread(socket);
                        ManageClientThread.addClientThread(user.getUserId(), serverConClientThread);
                        serverConClientThread.start();
                        System.out.println("测试是否开始下一个： 用户: " + user.getUserId());
                        serverConClientThread.notifyAllOtherFriends(ManageClientThread.getAllOnLineUserId());

                    } else {
                        msg.setMsgType(MessageType.MESSAGE_LOGIN_FAIL);
                        oos.writeObject(msg);
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
