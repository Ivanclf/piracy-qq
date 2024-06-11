/**
 * 功能：登录后台实现
 */
package com.qq.client.assistance;

import com.qq.client.QQClientConServer;
import com.qq.common.User;

public class QQClientUser {
	
	// 检验用户合法性
	public boolean checkUser(User user) {
		return new QQClientConServer().SendLoginInfoTOServer(user);
	}
	public boolean checkRegisterUser(User user){
		return  new QQClientConServer().SendRegisterInfoTOServer(user);
	}
}
