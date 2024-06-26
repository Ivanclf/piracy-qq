/**
 * 功能：登录后台实现
 */
package com.qq.client.assistance;

import com.qq.client.QQClientConServer;
import com.qq.common.User;

public class QQClientUser {

	/**
	 * 检验登录用户合法性
	 * @param user
	 * @return 链接是否成功
	 */
	public boolean checkUser(User user) {
		return new QQClientConServer().SendLoginInfoTOServer(user);
	}

	/**
	 * 检验注册用户合法性
	 * @param user
	 * @return 链接是否成功
	 */
	public boolean checkRegisterUser(User user){
		return  new QQClientConServer().SendRegisterInfoTOServer(user);
	}
}
