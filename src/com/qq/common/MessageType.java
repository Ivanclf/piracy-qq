/**
 * 功能：定义包的种类
 */
package com.qq.common;

public interface MessageType {
	/**
	 * 表明登录成功
	 */
	String MESSAGE_SUCCEED = "1";
	/**
	 * 表明登录失败
	 */
	String MESSAGE_LOGIN_FAIL = "2";
	/**
	 * 普通信息包
	 */
	String MESSAGE_COMMON = "3";
	/**
	 * 要求在线好友的包
	 */
	String MESSAGE_GET_ONLINE_FRIEND = "4";
	/**
	 * 返回在线好友的包，格式：按空格划分
	 */
	String MESSAGE_RET_ONLINE_FRIEND = "5";
	/**
	 * 表示离开
	 */
	String MESSAGE_EXIT = "6";
	/**
	 * 表示群聊信息
	 */
	String MESSAGE_MULTI = "7";
	String MESSAGE_FILE = "8";
}
