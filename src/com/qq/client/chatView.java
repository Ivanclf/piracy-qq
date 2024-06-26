package com.qq.client;

import com.qq.client.assistance.ManageClientConServerThread;
import com.qq.common.Message;
import com.qq.common.MessageType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

public class chatView {
    private JPanel chatView;
    private JTextArea messageWindow;
    private JTextField messageInput;
    private JButton messageSend;
    private JButton fileSend;
    private JButton stickers;
    private JButton cast;
    private String ownerId;
    private String friendId;
    private JFrame frame = new JFrame();
    public chatView(String ownerId,String friendId){

        frame.setTitle("和"+this.getFriendId()+"的聊天");
        frame.setContentPane(this.chatView);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        messageWindow.setEditable(false);
        // 发送消息
        messageSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Message m = new Message();
                m.setSender(ownerId);
                m.setGetter(friendId);
                m.setContent(messageInput.getText());
                m.setMsgType(MessageType.MESSAGE_COMMON);
                m.setSendTime(new Date().toString());

                String info = m.getSender() + ": " + m.getContent() + "\r\n";
                messageWindow.append(info);
                messageInput.setText(null);

                try{
                    ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientServerThread(ownerId).getSocket().getOutputStream());
                    oos.writeObject(m);
                } catch (IOException e1){
                    e1.printStackTrace();
                }
            }
        });
        // 文件传输
        fileSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int select = fileChooser.showOpenDialog(chatView);
                if(select == JFileChooser.APPROVE_OPTION) {//选择的是否为确认
                    File file = fileChooser.getSelectedFile();
                    System.out.println("文件" + file.getName() + " 被打开");
                    Message m = new Message();
                    m.setSender(ownerId);
                    m.setGetter(friendId);
                    m.setMsgType(MessageType.MESSAGE_FILE);
                    m.setSendTime(new Date().toString());
                    m.setFile(file);

                    String info = m.getSender() + "传输文件： \t" + file.getName() + "\r\n";
                    messageWindow.append(info);
                    messageInput.setText("");

                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientServerThread(ownerId).getSocket().getOutputStream());
                        oos.writeObject(m);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        // 表情包
        stickers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        // 投屏
        cast.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
    public void showMessage(Message m){
        String info;
        if (m.getMesType().equals(MessageType.MESSAGE_FILE)){
            info ="文件： "+m.getFile().getName()+" 接受成功\r\n";
        }
        else {
            info = m.getSender() + "对" + m.getGetter() + "说：" + m.getContent() + "\r\n";
        }
        messageWindow.append(info);
    }
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public void dispose() {
        frame.dispose();
    }

    public static void main(String[] args){
        new chatView("33","22");
    }
}