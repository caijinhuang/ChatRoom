package com.cjh.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Caijh
 * 用于保存发送信息方的端口号，因为static方法对象在进程间无法实现共享所以只能用持久化文件的方法保存端口号信息。或者数据库也可以
 * 2017年4月15日 下午10:27:56
 */
public class readPort {
	private static File f = new File("src/port.txt"); 
	private static FileInputStream fis;
	public readPort() {
		try {
			fis =new FileInputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//设置文件内容
	public void setPort(String port) throws FileNotFoundException {
		FileOutputStream fos = new FileOutputStream(f);
		byte bt[] = port.getBytes();
		try {
			fos.write(bt);
			fos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//读取文件内容
	public String readNowPorts() {
		String s="";
		try {
		int c;
		while((c=fis.read())!=-1)
			s+=(char)c;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
}
