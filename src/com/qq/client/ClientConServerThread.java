/**
 * 功能：客户端连接服务器线程
 */
package com.qq.client;

import com.qq.client.assistance.ManageQQChat;
import com.qq.client.assistance.ManageQQFriendList;
import com.qq.common.Message;
import com.qq.common.MessageType;

import java.io.*;
import java.net.Socket;

public class ClientConServerThread extends Thread {
    private Socket s;

    /** 构造方法
     * @param s Socket端
     */
    public ClientConServerThread(Socket s) {
        this.s = s;
    }

    public Socket getSocket() {
        return s;
    }

    public void setS(Socket s) {
        this.s = s;
    }

    /**
     * 客户端接收服务器端
     */
    public void run() {
        while (true) {
            // 不停的读取从服务器发来的消息
            try {
                ObjectInputStream messageAccepted = new ObjectInputStream(s.getInputStream());
                Message m = (Message) messageAccepted.readObject();
                System.out.println("读取从服务器发来的消息" + m.getSender() + "给" + m.getGetter() + "内容：" + m.getContent());
                // 普通包
                if (m.getMesType().equals(MessageType.MESSAGE_COMMON)) {
                    // 从服务器获得的消息显示到相应的聊天界面
                    chatView qqChat = ManageQQChat.getQQChat(m.getGetter() + " " + m.getSender());
                    if (qqChat == null) {
                        qqChat = new chatView(m.getGetter(), m.getSender());
                        // 把聊天界面加入到管理类
                        ManageQQChat.addQQChat(m.getGetter() + " " + m.getSender(), qqChat);
                    }
                    // 显示
                    qqChat.showMessage(m);
                    // 返回在线好友的包
                } else if (m.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {
                    System.out.println("客户端接收到好友的状态：" + m.getContent());
                    String con = m.getContent();
                    // getter是相对于服务器的接收者，也就是自己的QQ
                    String getter = m.getGetter();
                    System.out.println("getter=" + getter + "\t" + con);
                    String[] temp = con.split(" ");
                    // 修改相应的好友列表
                    friendList friendList = ManageQQFriendList.getFriendList(getter);
                    friendList.setAmount(temp.length);
                    // 更新在线好友
                    friendList.updateFriend(m);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
