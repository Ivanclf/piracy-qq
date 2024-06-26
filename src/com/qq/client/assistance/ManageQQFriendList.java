/**
 * 管理好友、陌生人、黑名单界面
 */
package com.qq.client.assistance;

import com.qq.client.friendList;

import java.util.*;

public class ManageQQFriendList {
	private static final HashMap<String, friendList> listMap = new HashMap<>();

	// 将界面添加到集合中
	public static void addQQFriendList(String uid, friendList friendList) {
		listMap.put(uid, friendList);
	}

	// 从集合中获取界面
	public static friendList getFriendList(String uid) {
		return listMap.get(uid);
	}
}
