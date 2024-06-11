/**
 * 功能：管理客户端和服务器连接线程
 */
package com.qq.client.assistance;

import com.qq.client.ClientConServerThread;
import java.util.*;

public class ManageClientConServerThread {
	// 客户端与服务器连接的集合
	private static final HashMap<String, ClientConServerThread> threadOperator = new HashMap<>();

	// 把创建好的ClientConServerThread对象放入hm
	public static void addClientConServerThread(String uid, ClientConServerThread ccst) {
		threadOperator.put(uid, ccst);
	}

	// 通过uid取得线程
	public static ClientConServerThread getClientServerThread(String uid) {
		return threadOperator.get(uid);
	}
}