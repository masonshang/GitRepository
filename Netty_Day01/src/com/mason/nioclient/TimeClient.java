package com.mason.nioclient;

public class TimeClient {
	public static void main(String[] args)
	{
		int port=8081;
		if(args!=null&&args.length>0)
		{
			port=Integer.parseInt(args[0]);
		}
		new Thread(new TimeClientHandle("127.0.0.1",port),"TimeClietn-001").start();
	}

}
