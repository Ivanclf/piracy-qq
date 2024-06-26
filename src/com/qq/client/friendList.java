package com.qq.client;

import com.qq.client.assistance.ManageQQChat;
import com.qq.common.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class friendList extends JFrame implements MouseListener {
    private static final long serialVersionUID = -7933056442552732399L;
    private String userId = null;
    private JPanel friendListTotal, friendListSeparate;
    private JLabel[] friendInit;
    private boolean[] friendListOnlineFlag; //标记成员在线情况
    private JScrollPane scrollPaneForList;
    private int amount = 0;
    private String imagePath = "images/icon.jpg";
    public friendList(String userId){
        this.userId = userId;

        // 好友列表
        friendListTotal = new JPanel(new BorderLayout());
        friendListSeparate = new JPanel(new GridLayout(10,1,4,4));

        // 初始化好友
        friendInit = new JLabel[amount];
        friendListOnlineFlag = new boolean[amount];
        for(int i = 0; i < friendInit.length; i++){
            friendInit[i] = new JLabel(String.valueOf(i + 1) , new ImageIcon("images/touxiang.png"), JLabel.LEFT);
            friendInit[i].setEnabled(false);
            friendListOnlineFlag[i] = false;
            if (friendInit[i].getText().equals(userId)) {
                friendInit[i].setEnabled(true);
                friendListOnlineFlag[i] = true;
            }
            friendInit[i].addMouseListener(this);
            friendListSeparate.add(friendInit[i]);
        }

        // 设置窗体
        setTitle("QQ Chat" + " " + userId);
        setIconImage(new ImageIcon(imagePath).getImage());
        setSize(350, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        setContentPane(friendListTotal);
        scrollPaneForList = new JScrollPane(friendListSeparate);
        friendListTotal.add(scrollPaneForList);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * 双击好友时的操作
     */
    @Override
    public void mouseClicked(MouseEvent e){
        if (e.getClickCount() == 2) {
            // 得到该好友的编号
            String friendUID = ((JLabel) e.getSource()).getText();
            // 如果聊天的人不在线
            if (!friendListOnlineFlag[Integer.parseInt(friendUID) - 1]) {
                JOptionPane.showMessageDialog(this, "不能和不在线的人聊天");
            } else if (friendUID.equals(userId)) {
                JOptionPane.showMessageDialog(this, "不能和自己聊天");
            } else{
                // 把聊天界面加入到管理类
                chatView qqChat = new chatView(userId, friendUID);
                ManageQQChat.addQQChat(this.userId + " " + friendUID, qqChat);
            }
        }
    }

    /**
     * 更新在线好友
     */
    public void updateFriend(Message m) {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        String[] onLineFriend = m.getContent().split(" ");
        for (int i = 0; i < amount; i++) {
            friendInit[i].setEnabled(false);
            friendListOnlineFlag[i] = false;
        }

        for (String s : onLineFriend) {
            friendInit[Integer.parseInt(s) - 1].setEnabled(true);
            friendListOnlineFlag[Integer.parseInt(s) - 1] = true;
            System.out.println("已更新 " + s + " 的状态");
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public static void main(String[] args){
        new friendList("1");
    }
}