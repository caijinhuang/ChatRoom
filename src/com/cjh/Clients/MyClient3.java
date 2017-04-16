package com.cjh.Clients;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.cjh.Server.readPort;

/**
 * @author Caijh
 *
 * 2017年4月11日 下午10:17:46
 */
public class MyClient3 implements Runnable{
	Socket clientSocket;
	boolean flag = true;
	Thread connenThread;	//用于向服务器发送请求
	BufferedReader cin;
	DataOutputStream cout;
	public static void main(String[] args) {
		new MyClient3().clientStart();
	}
	
	public void clientStart(){
		try {
//			clientSocket =  new Socket("localhost", 8080);
			clientSocket = new Socket("localhost", 8080,null,8083);//8083是客户端的端口号，每个客户端的端口号必须独立不能重复
			System.out.println("Client03 成功建立连接");
			while(flag){
				InputStream ins = clientSocket.getInputStream();
				cin = new BufferedReader(new InputStreamReader(ins));
				OutputStream outs = clientSocket.getOutputStream();
				cout = new DataOutputStream(outs);
				connenThread = new Thread(this);
				connenThread.start(); 	//启动线程，向服务器发送信息
				String aLine;
				while((aLine = cin.readLine()) != null){
					System.out.println("消息来自: "+new readPort().readNowPorts());
					System.out.println(aLine);
					if(aLine.equals("bye")){
						flag = false;
						connenThread.interrupt(); //关闭线程
						break;
					}
				}
				cout.close();
				cin.close();
				ins.close();
				outs.close();
				clientSocket.close();  	//关闭Socket连接
				System.exit(0);
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true){
			try {
				int ch;
				while((ch=System.in.read())!=-1){
					cout.write(ch);
					if(ch == '\n'){
						cout.flush(); //将缓冲区内容向输出流发送
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
