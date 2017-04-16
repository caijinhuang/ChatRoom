package com.cjh.Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * @author Caijh
 *
 *         2017年4月11日 下午9:19:04
 */
public class MyServer{
	boolean flag = true;	//标记是否结束
	Thread connenThread;	//想哭护短发送信息的线程
	BufferedReader sin;		//输入流对象
	DataOutputStream sout;	//输出流对象
	public static HashMap<String, Object> Smap = new HashMap<>();
	public static void main(String[] args) {
		try {
			ServerSocket server =  new ServerSocket(8080);
			while(true){//不断循环随时等待新的客户端接入服务器
				Socket clientSocket = server.accept();
				Smap.put(String.valueOf(clientSocket.getPort()),clientSocket);
				new serverThread(clientSocket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
