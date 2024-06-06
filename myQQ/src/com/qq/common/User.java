/**
 * 用户信息类
 */
package com.qq.common;

import java.io.Serializable;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8433144405811117163L;
	/**
	 * 账号
	 */
	private String userId;
	/**
	 * 密码
	 */
	private String passwd;
	/**
	 * 是否注册
	 */
	private Boolean isRegister;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public Boolean getIsRegister(){
		return isRegister;
	}

	public void setIsRegister(Boolean isRegister) {
		this.isRegister = isRegister;
	}
}
