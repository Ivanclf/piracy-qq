package com.qq.client;

import com.qq.client.assistance.ManageClientConServerThread;
import com.qq.client.assistance.ManageQQFriendList;
import com.qq.client.assistance.QQClientUser;
import com.qq.common.Message;
import com.qq.common.MessageType;
import com.qq.common.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;

public class loginUser {
    private JPanel loginUser;
    private JTextField accountField;
    private JPasswordField passwordField;
    private JButton login;
    private JButton register;
    private JLabel title;
    private JPanel clone;

    public loginUser() {
        JFrame frame = new JFrame("loginUser");
        frame.setContentPane(this.clone);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //登录验证
                QQClientUser qqClientUser = new QQClientUser();
                User u = new User();
                u.setUserId(accountField.getText().trim());
                u.setPasswd(new String(passwordField.getPassword()));
                u.setIsRegister(false);

                if (qqClientUser.checkUser(u)) {
                    try {
                        QQFriendList qqlist = new QQFriendList(u.getUserId());
                        ManageQQFriendList.addQQFriendList(u.getUserId(), qqlist);

                        ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientServerThread(u.getUserId()).getS().getOutputStream());
                        Message m = new Message();
                        m.setMsgType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
                        m.setSender(u.getUserId());
                        oos.writeObject(m);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "用户名或密码错误");
                }
            }
        });
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //注册
                QQClientUser qqClientUser = new QQClientUser();
                User u = new User();
                u.setUserId(accountField.getText().trim());
                u.setPasswd(new String(passwordField.getPassword()));
                u.setIsRegister(true);
                if (qqClientUser.checkRegisterUser(u)) {
                    try {
                        QQFriendList qqlist = new QQFriendList(u.getUserId());
                        ManageQQFriendList.addQQFriendList(u.getUserId(), qqlist);

                        ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientServerThread(u.getUserId()).getS().getOutputStream());
                        Message m = new Message();
                        m.setMsgType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
                        m.setSender(u.getUserId());
                        oos.writeObject(m);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "用户名已被占用，请重试");
                }

            }
        });
    }

    public static void main(String[] args) {
        new loginUser();
    }
}
