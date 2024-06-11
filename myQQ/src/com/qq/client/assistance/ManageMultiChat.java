package com.qq.client.assistance;

import com.qq.client.MultiChatView;

import java.util.HashMap;

public class ManageMultiChat {
    private static final HashMap<String, MultiChatView> mutiChatOperator = new HashMap<>();

    // 加入一个聊天界面
    public static void addMultiChat(String loginIdAndMultiChatId, MultiChatView multiChat) {
        mutiChatOperator.put(loginIdAndMultiChatId, multiChat);
    }

    // 获取一个聊天界面
    public static MultiChatView getMultiChat(String loginIdAndMultiChatId) {
        return mutiChatOperator.get(loginIdAndMultiChatId);
    }
    //删除一个群聊界面
    public static void removeMultiChat(String loginIdAndMultiChatId){
        mutiChatOperator.remove(loginIdAndMultiChatId);
    }
}
