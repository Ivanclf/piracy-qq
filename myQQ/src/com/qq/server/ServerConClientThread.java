package com.qq.server;

import com.qq.common.Message;
import com.qq.common.MessageType;

import java.net.*;
import java.util.*;
import java.io.*;

public class ServerConClientThread extends Thread {
    private final Socket socket;
    ServerConClientThread clientThread;
    public ServerConClientThread(Socket socket){
        this.socket = socket;
    }

    public void notifyAllOtherFriends(String notifyString){
        HashMap<String,?> threadOperation = ManageClientThread.threadOperation;
        Iterator<String> it = threadOperation.keySet().iterator();

        while(it.hasNext()){
            Message msg = new Message();
            msg.setContent(notifyString);
            msg.setMsgType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
            String onId = it.next();

            if(threadOperation.get(onId) != null) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.getClientThread(onId).socket.getOutputStream());
                    msg.setGetter(onId);
                    oos.writeObject(msg);
                    System.out.println("要发送给" + onId + " " + notifyString + "刚上线的消息");
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
                if(msg.getMultiChat() != null){
                    System.out.println(msg.getSender()+"给"+msg.getMultiChat()+"内容为："+msg.getContent());
                }
                else {
                    System.out.println(msg.getSender() + "给" + msg.getGetter() + "内容为：" + msg.getContent());
                }
                switch (msg.getMesType()) {
                    //普通消息
                    case MessageType.MESSAGE_COMMON:
                    case MessageType.MESSAGE_FILE: {
                        clientThread = ManageClientThread.getClientThread(msg.getGetter());
                        new ObjectOutputStream(clientThread.socket.getOutputStream()).writeObject(msg);
                        System.out.println("消息或文件发送成功");
                        break;
                    }
                    //获取好友信息
                    case MessageType.MESSAGE_GET_ONLINE_FRIEND: {
                        System.out.println(msg.getSender() + " 获取他的好友");
                        String allOnLineUserId = ManageClientThread.getAllOnLineUserId();
                        System.out.println(msg.getSender() + " 的好友有 " + allOnLineUserId);
                        msg.setContent(allOnLineUserId);
                        new ObjectOutputStream(socket.getOutputStream()).writeObject(msg);
                        break;
                    }
                    //离开消息
                    case MessageType.MESSAGE_EXIT: {
                        ManageClientThread.removeClientThread(msg.getSender());
                        //发送下线消息
                        notifyAllOtherFriends(ManageClientThread.getAllOnLineUserId());
                        break;
                    }
                    //群聊消息
                    case MessageType.MESSAGE_MULTI:
                        // System.out.println("测试：" + msg.getContent());
                        for (int i = 0; i <= 20; i++) {
                            if ((ManageClientThread.getClientThread(String.valueOf(i)) != null) && (!String.valueOf(i).equals(msg.getSender()))) {
                                msg.setGetter(String.valueOf(i));
                                clientThread = ManageClientThread.getClientThread(String.valueOf(i));
                                new ObjectOutputStream(clientThread.socket.getOutputStream()).writeObject(msg);
                                System.out.println("给：" + i + " 消息发送成功");
                            }
                        }
                        break;
                }
            }catch (Exception e){
                System.out.println("发生了线程错误，请检查后再试！");
                e.printStackTrace();
            }
        }
    }
}
