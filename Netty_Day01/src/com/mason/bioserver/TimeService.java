package com.mason.bioserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeService {

	public static void main(String[] args) {
		// Server·þÎñÆô¶¯Æ÷
		int port=8080;
		if(args!=null&&args.length>0)
		{
			port=Integer.valueOf(args[0]);
		}
		ServerSocket server=null;
		try {
			server=new ServerSocket(port);
			System.out.println("the time server is start in port:"+port);
			Socket socket=null;
			while(true)
			{
				socket=server.accept();
				new Thread(new TimeServerHandler(socket)).start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(server!=null)
			{
				System.out.println("the time server close");
				try {
					server.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				server=null;
			}
		}
		


	}

}
