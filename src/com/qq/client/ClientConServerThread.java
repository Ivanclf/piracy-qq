/**
 * 功能：客户端连接服务器线程
 */
package com.qq.client;

import com.qq.client.assistance.ManageMultiChat;
import com.qq.client.assistance.ManageQQChat;
import com.qq.client.assistance.ManageQQFriendList;
import com.qq.common.Message;
import com.qq.common.MessageType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
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
                    ClientChatView qqChat = ManageQQChat.getQQChat(m.getGetter() + " " + m.getSender());
                    if (qqChat == null) {
                        qqChat = new ClientChatView(m.getGetter(), m.getSender());
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

                    // 修改相应的好友列表
                    QQFriendList qqFriendList = ManageQQFriendList.getQQFriendList(getter);
                    // 更新在线好友
                    if (qqFriendList != null) {
                        qqFriendList.updateFriend(m);
                    }
                }
                //群聊包
                else if (m.getMesType().equals(MessageType.MESSAGE_MULTI)) {
                    MultiChatView multiChatView = ManageMultiChat.getMultiChat(m.getGetter() + " " + m.getMultiChat());
                    if (multiChatView == null) {
                        System.out.println("窗口被关闭了，需要新建群聊窗口");
                        multiChatView = new MultiChatView(m.getGetter(), m.getMultiChat());
                        ManageMultiChat.addMultiChat(m.getGetter() + " " + m.getMultiChat(), multiChatView);
                    }
                    multiChatView.showMessages(m);
                } else if (m.getMesType().equals(MessageType.MESSAGE_FILE)) {//传输文件
                    ClientChatView qqChat = ManageQQChat.getQQChat(m.getGetter() + " " + m.getSender());
                    if (qqChat == null) {
                        qqChat = new ClientChatView(m.getGetter(), m.getSender());
                        // 把聊天界面加入到管理类
                        ManageQQChat.addQQChat(m.getGetter() + " " + m.getSender(), qqChat);
                    }
                    File file = new File("./FileRecv/" + m.getFile().getName());
                    FileInputStream fis = new FileInputStream(m.getFile());
                    FileOutputStream fos = new FileOutputStream(file);
                    try {
                        byte[] buf = new byte[1024];
                        //循环读取
                        while (fis.read(buf) != -1) {
                            //输出到指定文件
                            fos.write(buf);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    m.setContent(m.getFile().getName() + " 接收成功");
                    qqChat.showMessage(m);
                    System.out.println("文件 " + m.getFile().getName() + " 接收成功！");
                }
            } catch (Exception e) {
                System.out.println("网络连接异常，请检查网络后再试！");
                e.printStackTrace();
            }
        }
    }
}
