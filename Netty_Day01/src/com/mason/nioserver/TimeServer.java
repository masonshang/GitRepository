package com.mason.nioserver;

public class TimeServer {
	
	public static void main(String[] args){
		int port=8081;
		if(args!=null&&args.length>0)
		{
			port=Integer.parseInt(args[0]);
			
		}
		MultiplexerTimeserver timeServer=new MultiplexerTimeserver(port);
		new Thread(timeServer,"Nio-timeServer-001").start();
	}

}
