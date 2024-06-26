package com.qq.server;

import com.qq.common.Message;
import com.qq.common.MessageType;

import java.net.*;
import java.sql.ResultSet;
import java.util.*;
import java.io.*;

public class ServerConClientThread extends Thread {
    private SqlHelper sqlHelper = new SqlHelper();
    private ResultSet resultSet;
    private final Socket socket;
    private final String amountCheck = "select QQfriend from QQUser where QQuserId=?";
    private String[] paras;
    private ServerConClientThread clientThread;
    private String varcharValue;
    public ServerConClientThread(Socket socket){
        this.socket = socket;
    }

    public void notifyAllOtherFriends(String notifyString){
        HashMap<String,?> threadOperation = ManageClientThread.threadOperation;

        for (String s : threadOperation.keySet()) {
            Message msg = new Message();
            msg.setContent(notifyString);
            msg.setMsgType(MessageType.MESSAGE_RET_ONLINE_FRIEND);

            if (threadOperation.get(s) != null) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.getClientThread(s).socket.getOutputStream());
                    msg.setGetter(s);
                    oos.writeObject(msg);
                    System.out.println("要发送给" + s + " " + notifyString + "刚上线的消息");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void run(){
        while (true){
            try{
                Message msg = (Message) new ObjectInputStream(socket.getInputStream()).readObject();
                paras = new String[]{msg.getSender()};
                System.out.println(msg.getSender() + "给" + msg.getGetter() + "内容为：" + msg.getContent());
                switch (msg.getMesType()) {
                    //普通消息
                    case MessageType.MESSAGE_COMMON:
                    case MessageType.MESSAGE_FILE: {
                        clientThread = ManageClientThread.getClientThread(msg.getGetter());
                        new ObjectOutputStream(clientThread.socket.getOutputStream()).writeObject(msg);
                        System.out.println("消息或文件发送成功");
                        break;
                    }
                    //获取在线好友信息
                    case MessageType.MESSAGE_GET_ONLINE_FRIEND: {
                        System.out.println(msg.getSender() + " 获取他的好友");
                        String[] allOnLineUserId = ManageClientThread.getAllOnLineUserId().split(" ");
                        resultSet = sqlHelper.queryExecute(amountCheck, paras);
                        while (resultSet.next()){
                            varcharValue = resultSet.getString("QQfriend").replace(",", " ");
                        }
                        System.out.println(msg.getSender() + " 的好友有 " + varcharValue);
                        msg.setContent(Arrays.toString(allOnLineUserId));
                        new ObjectOutputStream(socket.getOutputStream()).writeObject(msg);
                        break;
                    }
                    //离开消息
                    case MessageType.MESSAGE_EXIT: {
                        //发送下线消息
                        ManageClientThread.removeClientThread(msg.getSender());
                        notifyAllOtherFriends(ManageClientThread.getAllOnLineUserId());
                        this.interrupt();
                    }
                }
            }catch (Exception e){
                System.out.println("发生了线程错误，请检查后再试！");
                this.interrupt();
                e.printStackTrace();
            }
        }
    }
}