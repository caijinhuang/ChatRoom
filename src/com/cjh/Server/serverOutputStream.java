package com.cjh.Server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author Caijh
 *
 * 2017年4月14日 下午2:27:46
 */
public class serverOutputStream extends Thread {
	private DataOutputStream sout;
	private OutputStream outs;
	private String port="";
	public serverOutputStream(OutputStream outs,DataOutputStream sout) {
		
		this.outs = outs;
		this.sout = sout;
	}
	/**
	 * 用客户端端口号选择指定的客户端发送信息
	 * @param port 客户端端口号
	 */
	public DataOutputStream send(String port){
		try {
			outs = ((Socket)MyServer.Smap.get(port)).getOutputStream();
			sout = new DataOutputStream(outs);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sout;
	}
	/**
	 * 判断端口是否有效
	 * @param port 客户端端口号
	 * @return true or false
	 */
	public boolean isPort(String port){
		if(MyServer.Smap.get(port) != null)
			return true;
		else
			return false;
	}
	//客户端对客户端转发消息
	public void sendInfo(String aLine,String port,String portFrom){
		send(port);
		char ch[] = aLine.toCharArray();
			try {
			for(int i=4;i<ch.length;i++){
				sout.write((char)ch[i]);
			}
			new readPort().setPort(portFrom);
			sout.write('\n');//sout的内容必须要以'\n'结尾。
			sout.flush(); // 将缓冲区内容向客户端输出
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	/**
	 * 发送消息
	 */
	public void sendInfo(){
		int ch;
		String info = "";
		try {
			while ((ch = System.in.read()) != -1) {
				info += (char)ch;	
				if (ch == '\n') {
					//小于四个字节就直接发送给前一个客户端
					char c[] = info.toCharArray();
					if(c.length<=4){
						System.out.println("信息格式有误！格式为：目的端口号+信息内容");
						new readPort().setPort("8080");
						info = "";
						port = "";
					}else{
						port = "";
						for(int i=0;i<4;i++){
							port += c[i];
						}
						if(isPort(port)){
							sout = send(port);
							for(int i=4;i<c.length;i++)
								sout.write(c[i]);
							new readPort().setPort("8080");
							sout.flush(); // 将缓冲区内容向客户端输出
						}else{
							new readPort().setPort("8080");
							System.out.println("目的端口不存在！");
						}
						info="";//清空字符串
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 输出线程
	 * 用于读取键盘输入的信息，然后将信息发送给客户端
	 */
	@Override
	public void run() {
		sendInfo();
	}
}
