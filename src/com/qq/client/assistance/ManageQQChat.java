/**
 * 功能：管理用户聊天界面
 */
package com.qq.client.assistance;

import com.qq.client.chatView;

import java.util.*;


public class ManageQQChat {
	private static final HashMap<String, chatView> chatWindow = new HashMap<>();

	// 加入一个聊天界面
	public static void addQQChat(String loginIdAndFriendId, chatView qqChat) {
		chatWindow.put(loginIdAndFriendId, qqChat);
	}

	// 获取一个聊天界面
	public static chatView getQQChat(String loginIdAndFriendId) {
		return chatWindow.get(loginIdAndFriendId);
	}

	public static void removeQQChat(String loginIdAndFriend){
		chatWindow.get(loginIdAndFriend).dispose();
		chatWindow.remove(loginIdAndFriend);
	}
}
