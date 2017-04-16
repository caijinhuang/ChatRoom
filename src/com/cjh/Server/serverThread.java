package com.cjh.Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

/**
 * @author Caijh
 *
 *         2017年4月12日 下午9:06:52
 */
public class serverThread extends Thread {
	private BufferedReader sin; // 输入流对象
	private DataOutputStream sout; // 输出流对象
	private Thread connenThread; // 接收信息的线程
	private Thread connenThreadOutput; // 发送信息的线程
	private InputStream ins;
	private OutputStream outs;
	private boolean flag = true;
	private Socket client;
	private String port="";
	private char ch[];

	public serverThread(Socket client) {
		this.client = client;
		System.out.println("端口："+client.getPort()+"连接建立完毕！");
		try {
			ins = client.getInputStream();
			sin = new BufferedReader(new InputStreamReader(ins));
			connenThread = new Thread(this);
			connenThreadOutput = new Thread(new serverOutputStream(outs,sout));
			connenThread.start();// 启动线程，接收客户端信息
			connenThreadOutput.start(); // 启动线程，向客户端发送信息
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 判断是否转发
	 * @param aLine 接受到的消息
	 * @return
	 */
	public boolean isMine(String aLine){
		port="";
		ch = aLine.toCharArray();
		if(ch.length>4){
			for(int i=0;i<4;i++){
				port+=ch[i];
			}
			//检查端口是否存在
			if(new serverOutputStream(outs, sout).isPort(port)){
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	}
	/**
	 * 接收消息的线程
	 */
	@Override
	public void run() {
		while (flag) {
			try {
				String aLine;
				while ((aLine = sin.readLine()) != null) {
					//判断是否需要转发
					if(isMine(aLine)){
						if (aLine.equals("bye")) {
							flag = false;
							//从map表中删除socket信息
							System.out.println("客户端口："+client.getPort()+"断开连接");
							new MyServer().Smap.remove(String.valueOf(client.getPort()));
							connenThread.interrupt(); // 线程中断
							break;
						}else{
							System.out.println("消息来自："+client.getPort());
							System.out.println(aLine);
						}
					}else{
						//将信息转发出去
						new serverOutputStream(outs, sout).sendInfo(aLine, port,String.valueOf(client.getPort()));
					}
				}
				sin.close();
				ins.close();
				connenThreadOutput.interrupt();
				this.interrupt();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
