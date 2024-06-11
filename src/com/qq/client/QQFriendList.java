/**
 * 功能：好友界面(包括我的好友，群聊)
 */
package com.qq.client;

import com.qq.client.assistance.ManageClientConServerThread;
import com.qq.client.assistance.ManageMultiChat;
import com.qq.client.assistance.ManageQQChat;
import com.qq.common.Message;
import com.qq.common.MessageType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ObjectOutputStream;

/**
 * 请去friendList文件重构一下QQ好友列表代码
 */
public class QQFriendList extends JFrame implements ActionListener, MouseListener {

    private static final long serialVersionUID = -7933056442552732399L;
    // 卡片布局
    CardLayout cardLayout;
    // 第一张卡片
    JPanel friendListTotal, friendListSeparate, panelForGroupChatListButton;
    JButton myFriendInList, groupChatInList;
    JScrollPane scrollPaneForList;
    // 第二张卡片
    JPanel groupChatTotal, groupChatSeparate, panelForGroupButtons;
    JButton myFriendInGroup, groupChatInGroup;
    JScrollPane scrollPaneForGroup;
    String ownerId;
    JLabel[] friendInit, groupInit;
    // 标记好友是否在线
    Boolean[] friendListOnlineFlag, groupOnlineFlag;//标记群成员在线情况
    String MultiChatID = "qunliao";
    public QQFriendList(String ownerId) {
        this.ownerId = ownerId;
        // 处理第一张卡片(好友列表)
        myFriendInList = new JButton("我的好友");
        groupChatInList = new JButton("群聊");
        groupChatInList.addActionListener(this);
        friendListTotal = new JPanel(new BorderLayout());
        // 假定有10个好友
        friendListSeparate = new JPanel(new GridLayout(10, 1, 4, 4));

        // 初始化好友
        friendInit = new JLabel[50];
        friendListOnlineFlag = new Boolean[50];
        for (int i = 0; i < friendInit.length; i++) {
            friendInit[i] = new JLabel(String.valueOf(i + 1) , new ImageIcon("images/touxiang.png"), JLabel.LEFT);
            friendInit[i].setEnabled(false);
            friendListOnlineFlag[i] = false;
            if (friendInit[i].getText().equals(ownerId)) {
                friendInit[i].setEnabled(true);
                friendListOnlineFlag[i] = true;
            }
            friendInit[i].addMouseListener(this);
            // jphy2初始化50个好友
            friendListSeparate.add(friendInit[i]);
        }
        scrollPaneForList = new JScrollPane(friendListSeparate);
        panelForGroupChatListButton = new JPanel(new GridLayout(1, 1));

        // 按钮加入jphy3
        panelForGroupChatListButton.add(groupChatInList);

        // 加入friendListTotal,对friendListTotal初始化
        friendListTotal.add(myFriendInList, "North");
        friendListTotal.add(scrollPaneForList, "Center");
        friendListTotal.add(panelForGroupChatListButton, "South");

        // 处理第二张卡片
        myFriendInGroup = new JButton("我的好友");
        myFriendInGroup.addActionListener(this);
        groupChatInGroup = new JButton("群聊");
        groupChatTotal = new JPanel(new BorderLayout());
        // 假定有群聊中有20人
        groupChatSeparate = new JPanel(new GridLayout(20, 1, 4, 4));

        // 初始化群聊成员
        groupInit = new JLabel[20];
        groupOnlineFlag = new Boolean[20];
        for (int i = 0; i < groupInit.length; i++) {
            groupInit[i] = new JLabel("" + (i + 1), new ImageIcon(
                    "images/touxiang.png"), JLabel.LEFT);
            groupInit[i].setEnabled(false);
            groupOnlineFlag[i] = false;
            if (groupInit[i].getText().equals(ownerId)) {
                groupInit[i].setEnabled(true);
                groupOnlineFlag[i] = true;
            }
            groupInit[i].addMouseListener(this);
            groupChatSeparate.add(groupInit[i]);
        }
        scrollPaneForGroup = new JScrollPane(groupChatSeparate);
        // jpmsr2初始化20个群聊成员
        panelForGroupButtons = new JPanel(new GridLayout(2, 1));

        // 按钮加入jpmsr3
        panelForGroupButtons.add(myFriendInGroup);
        panelForGroupButtons.add(groupChatInGroup);

        // 加入jpmsr1,对jpmsr1初始化
        groupChatTotal.add(panelForGroupButtons, "North");
        groupChatTotal.add(scrollPaneForGroup, "Center");


        // 把JFrame设置为CardLayout布局
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        // 加入JFrame
        add(friendListTotal, "1");//第一种布局
        add(groupChatTotal, "2");//第二种布局

        //设置窗口关闭后给服务器发送离开的信息
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Message m = new Message();
                m.setMsgType(MessageType.MESSAGE_EXIT);
                m.setSender(ownerId);
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientServerThread(ownerId).getS().getOutputStream());
                    oos.writeObject(m);

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        // 设置窗体
        setTitle("QQ Chat" + "\t" + ownerId);
        setIconImage(new ImageIcon("images/icon.jpg").getImage());
        setSize(350, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        new QQFriendList("1");
    }

    // 更新在线好友
    public void updateFriend(Message m) {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        String[] onLineFriend = m.getContent().split(" ");
        for (int i = 0; i < 50; i++) {
            friendInit[i].setEnabled(false);
            friendListOnlineFlag[i] = false;
        }
        for (int i = 0; i < 20; i++) {
            groupInit[i].setEnabled(false);
            groupOnlineFlag[i] = false;
        }

        for (String s : onLineFriend) {

            friendInit[Integer.parseInt(s) - 1].setEnabled(true);
            friendListOnlineFlag[Integer.parseInt(s) - 1] = true;
            if (Integer.parseInt(s) < 20) {
                groupInit[Integer.parseInt(s) - 1].setEnabled(true);
                groupOnlineFlag[Integer.parseInt(s) - 1] = true;
            }
            System.out.println("已更新 " + s + " 的状态");
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == groupChatInList) {
            // 点击了群聊按钮，显示第二张卡片，并弹出群聊窗口
            cardLayout.show(this.getContentPane(), "2");
            if (ManageMultiChat.getMultiChat(this.ownerId + " " + this.MultiChatID) == null) {
                MultiChatView multiChat = new MultiChatView(ownerId, MultiChatID);
                ManageMultiChat.addMultiChat(this.ownerId + " " + this.MultiChatID, multiChat);
            }

        } else if (e.getSource() == myFriendInGroup) {
            // 点击了好友按钮，显示第一张卡片
            cardLayout.show(this.getContentPane(), "1");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        // 相应用户双击的事件，并得到好友的编号
        if (e.getClickCount() == 2) {
            // 得到该好友的编号
            String friendNo = ((JLabel) e.getSource()).getText();
            // 如果聊天的人不在线
            if (!friendListOnlineFlag[Integer.parseInt(friendNo) - 1]) {
                JOptionPane.showMessageDialog(this, "不能和不在线的人聊天");
            } else if (!friendNo.equals(ownerId)
                    && friendListOnlineFlag[Integer.parseInt(friendNo) - 1]) {
                // 如果不是自己并且在线
                ClientChatView qqChat = new ClientChatView(ownerId, friendNo);
                // 把聊天界面加入到管理类
                ManageQQChat.addQQChat(this.ownerId + " " + friendNo, qqChat);
            } else {
                //如果是自己
                JOptionPane.showMessageDialog(this, "不能和自己聊天");
            }

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        JLabel jl = (JLabel) e.getSource();
        jl.setForeground(Color.red);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        JLabel jl = (JLabel) e.getSource();
        jl.setForeground(Color.black);
    }
}