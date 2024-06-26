/**
 * 功能：客户端连接服务器的后台
 */
package com.qq.client;

import java.io.*;
import java.net.*;

import com.qq.client.assistance.ManageClientConServerThread;
import com.qq.common.*;
public class QQClientConServer {
	public Socket s;

	/**
	 * 登录链接服务器
	 * @param o
	 * @return 连接是否成功
	 */
	public boolean SendLoginInfoTOServer(Object o) {
		boolean b = false;
		try {
			// 连接127.0.0.1的9999端口
			s = new Socket("127.0.0.1", 9999);
			// 向服务器发送账号信息
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(o);

			// 从服务器收到验证是否通过的Message对象
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			Message ms = (Message) ois.readObject();
			// 验证登录是否成功
			if (ms.getMesType().equals(MessageType.MESSAGE_SUCCEED)) {
				// 创建一个该qq和服务器端保持通讯连接的线程
				ClientConServerThread ccst = new ClientConServerThread(s);
				// 启动该线程
				ccst.start();
				ManageClientConServerThread.addClientConServerThread(
						((User) o).getUserId(), ccst);

				b = true;
			} else {
				// 关闭Socket
				s.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 注册链接服务器
	 * @param o
	 * @return 链接是否成功
	 */
	public boolean SendRegisterInfoTOServer(Object o) {
		boolean b = false;
		try {
			// 连接127.0.0.1的9999端口
			s = new Socket("127.0.0.1", 9999);
			// 向服务器发送账号信息
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(o);

			// 从服务器收到验证是否通过的Message对象
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			Message ms = (Message) ois.readObject();
			// 验证注册是否成功
			if (ms.getMesType().equals(MessageType.MESSAGE_SUCCEED)) {
				// 创建一个该qq和服务器端保持通讯连接的线程
				ClientConServerThread ccst = new ClientConServerThread(s);
				// 启动该线程
				ccst.start();
				ManageClientConServerThread.addClientConServerThread(((User) o).getUserId(), ccst);
				b = true;
			} else {
				// 关闭Socket
				s.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}


}
