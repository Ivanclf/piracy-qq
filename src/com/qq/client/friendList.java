package com.qq.client;

import com.qq.client.assistance.ManageQQChat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class friendList extends JFrame implements ActionListener, MouseListener {
    private static final long serialVersionUID = -7933056442552732399L;
    private String userId = null;
    private JButton groupChat;
    private JPanel friendListTotal, friendListSeparate;
    private JPanel[] friendInit;
    private boolean[] friendListOnlineFlag, groupOnlineFlag; //标记群成员在线情况

    private String imagePath = "images/icon.jpg";
    public friendList(String userId){
        this.userId = userId;

        // 好友列表
        groupChat = new JButton("群聊");
        friendListTotal = new JPanel(new BorderLayout());

        // 初始化好友
        friendInit = new JPanel[50];


        // 设置窗体
        setTitle("QQ Chat" + " " + userId);
        setIconImage(new ImageIcon(imagePath).getImage());
        setSize(350, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == groupChat){

        }
    }
    @Override
    public void mouseClicked(MouseEvent e){
        if (e.getClickCount() == 2) {
            // 得到该好友的编号
            String friendNo = ((JLabel) e.getSource()).getText();
            // 如果聊天的人不在线
            if (!friendListOnlineFlag[Integer.parseInt(friendNo) - 1]) {
                JOptionPane.showMessageDialog(this, "不能和不在线的人聊天");
            } else if (!friendNo.equals(userId)
                    && friendListOnlineFlag[Integer.parseInt(friendNo) - 1]) {
                // 如果不是自己并且在线
                ClientChatView qqChat = new ClientChatView(userId, friendNo);
                // 把聊天界面加入到管理类
                ManageQQChat.addQQChat(this.userId + " " + friendNo, qqChat);
            } else {
                //如果是自己
                JOptionPane.showMessageDialog(this, "不能和自己聊天");
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        JLabel label = (JLabel) e.getSource();
        label.setForeground(Color.RED);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        JLabel label = (JLabel) e.getSource();
        label.setForeground(Color.BLACK);
    }

    public static void main(String[] args){
        new friendList("22");
    }
}
