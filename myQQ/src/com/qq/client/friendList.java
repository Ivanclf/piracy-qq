package com.qq.client;

import javax.swing.*;
import java.awt.*;

public class friendList extends JFrame {
    private static final long serialVersionUID = -7933056442552732399L;
    private String userId = null;
    private JButton groupChat;
    private JPanel friendListTotal, friendListSeparate;
    private JPanel[] friendInit;
    private boolean friendListOnlineFlag;

    private String imagePath = "images/icon.jpg";
    public friendList(String userId){
        this.userId = userId;

        // 好友列表
        groupChat = new JButton("群聊");
        friendListTotal = new JPanel(new BorderLayout());

        // 初始化好友
        friendInit = new JPanel[50];

        // 设置窗体
        setTitle("QQ Chat" + "\t" + userId);
        setIconImage(new ImageIcon(imagePath).getImage());
        setSize(350, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
