package com.qq.client;

import com.qq.client.assistance.ManageClientConServerThread;
import com.qq.client.assistance.ManageMultiChat;
import com.qq.common.Message;
import com.qq.common.MessageType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectOutputStream;
import java.util.Date;

public class MultiChatView  extends JFrame implements ActionListener {

    private static final long serialVersionUID = 582207281703443438L;
    JTextArea messageWindow;
    JTextField messageInput;
    JButton messageSend;
    JPanel panel;
    JScrollPane scrollPane;
    private String ownerID;
    private String MultiChatID;
    public String getOwnerID(){
        return ownerID;
    }
    public String getMultiChatID(){
        return  MultiChatID;
    }
    public void setOwnerID(String ownerID){
        this.ownerID = ownerID;
    }
    public void setMultiChatID(String MultiChatID){
        this.MultiChatID = MultiChatID;
    }

    public void showMessages(Message m){
        String info = m.getSender()+"说："+m.getContent()+"\r\n";
        System.out.println("Multichat:"+info);
        messageWindow.append(info);
    }

    public MultiChatView(String ownerID ,String MultiChatID){
        this.ownerID = ownerID;
        this.MultiChatID = MultiChatID;

        messageWindow = new JTextArea();
        scrollPane = new JScrollPane(messageWindow);
        messageInput = new JTextField(20);
        messageSend = new JButton("发送");
        messageSend.addActionListener(this);
        panel = new JPanel();

        panel.add(messageInput);
        panel.add(messageSend);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                ManageMultiChat.removeMultiChat(ownerID+" "+MultiChatID);
            }
        });

        add(scrollPane,"Center");
        add(panel,"South");
        setTitle(MultiChatID+" ");
        setSize(400,300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == messageSend){
            Message m = new Message();
            m.setSender(this.ownerID);
            m.setMultiChat(this.MultiChatID);
            m.setContent(messageInput.getText());
            m.setMsgType(MessageType.MESSAGE_MULTI);
            m.setSendTime(new Date().toString());
            String info = m.getSender()+"说："+m.getContent()+"\r\n";
            System.out.println(info);

            messageWindow.append(info);
            messageInput.setText("");

            try{
                ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientServerThread(this.ownerID).getSocket().getOutputStream());
                oos.writeObject(m);
            }catch (Exception e1){
                e1.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        new MultiChatView("22", "33");
    }
}
