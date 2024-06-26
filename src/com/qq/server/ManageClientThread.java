package com.qq.server;

import java.util.HashMap;
import java.util.Iterator;

public class ManageClientThread {
    public static HashMap<String,ServerConClientThread> threadOperation = new HashMap<>();

    /**
     * 增加线程
     * @param uid
     * @param ct 线程
     */
    public static void addClientThread(String uid, ServerConClientThread ct){
        threadOperation.put(uid,ct);
        System.out.println("在hashmap中已添加 " + uid);
    }

    /**
     * 移除线程
     * @param uid
     */
    public static void removeClientThread(String uid){
        threadOperation.remove(uid);
    }

    /**
     * 获得相关线程并进行操作
     * @param uid
     */
    public static ServerConClientThread getClientThread(String uid){
        return threadOperation.get(uid);
    }
    /**
     * 返回当前在线的人的情况
     */
    public static String getAllOnLineUserId() {
        // 使用迭代器进行遍历，其中keySet用于找键，后面的迭代器对键遍历
        Iterator<String> it = threadOperation.keySet().iterator();
        StringBuilder res = new StringBuilder();
        while (it.hasNext()) {
            res.append(it.next()).append(" ");
        }
        return res.toString();
    }
}
