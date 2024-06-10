package com.qq.client;


import com.qq.common.Message;
import  com.qq.common.MessageType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;


public class ClientChatView  extends JFrame  implements ActionListener{

    JTextArea messageWindow;
    JTextField messageInput;
    JButton messageSend, fileSend;
    JPanel panel;
    JScrollPane scrollPane;
    JFileChooser fileChooser;

    private String ownerId;
    private String friendId;

    public String getOwnerId(){
        return ownerId;
    }

    public void setOwnerId(String ownerId){
        this.ownerId = ownerId;
    }

    public String getFriendId(){
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public void showMessage(Message m){
        String info = null;
        if (m.getMesType().equals(MessageType.MESSAGE_FILE)){
            info ="文件： "+m.getFile().getName()+" 接受成功\r\n";
        }
        else {
            info = m.getSender() + "对" + m.getGetter() + "说：" + m.getContent() + "\r\n";
        }
        messageWindow.append(info);
    }

    public ClientChatView(String ownerId,String friendId){
        this.ownerId = ownerId;
        this.friendId = friendId;
        messageWindow = new JTextArea();
        messageInput = new JTextField(20);
        scrollPane = new JScrollPane(messageWindow);
        messageSend = new JButton("发送");
        fileSend = new JButton("文件");
        fileChooser = new JFileChooser();
        fileSend.addActionListener(this);
        panel = new JPanel();
        messageSend.addActionListener(this);
        messageWindow.setEditable(false);

        panel.add(fileSend);
        panel.add(messageInput);
        panel.add(messageSend);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                ManageQQChat.removeQQChat(ownerId+" "+friendId);
            }
        });

        add(scrollPane,"Center");
        add(panel,"South");
        setIconImage(new ImageIcon("images/icon.jpg").getImage());
        setTitle(this.getOwnerId()+"和"+this.getFriendId()+"聊天");
        setSize(400,300);
        setLocationRelativeTo(null);
        setVisible(true);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == messageSend){//发送消息
            Message m = new Message();
            m.setSender(this.ownerId);
            m.setGetter(this.friendId);
            m.setContent(messageInput.getText());
            m.setMesType(MessageType.MESSAGE_COMMON);
            m.setSendTime(new Date().toString());

            String info = m.getSender()+"对"+m.getGetter()+"说："+m.getContent()+"\r\n";
            messageWindow.append(info);
            messageInput.setText("");

            try{
                ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientServerThread(this.ownerId).getS().getOutputStream());
                oos.writeObject(m);
            } catch (IOException e1){
                e1.printStackTrace();
            }
        }
        else if (e.getSource() == fileSend){//文件传输功能

            int select = fileChooser.showOpenDialog(this);
            if(select == JFileChooser.APPROVE_OPTION){//选择的是否为确认
                File file = fileChooser.getSelectedFile();
                System.out.println("文件"+file.getName()+" 被打开");
                Message m = new Message();
                m.setSender(this.ownerId);
                m.setGetter(this.friendId);
                m.setMesType(MessageType.MESSAGE_FILE);
                m.setSendTime(new Date().toString());
                m.setFile(file);

                String info = m.getSender()+" 给 "+m.getGetter()+"传输文件： "+file.getName()+"\r\n";
                messageWindow.append(info);
                messageInput.setText("");

                try{
                    ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientServerThread(this.ownerId).getS().getOutputStream());
                    oos.writeObject(m);
                } catch (IOException e1){
                    e1.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args){

        ClientChatView c = new ClientChatView("33","22");
    }

}
