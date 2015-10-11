package com.mason.fakenioserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.mason.bioserver.TimeServerHandler;

public class TimeSerer {
	public static void main(String[] args)
	{
	  int port=8080;
	  if(args!=null&&args.length>0)
	  {
		  port=Integer.valueOf(args[0]);
	  }
	  ServerSocket server=null;
	  try {
		server=new ServerSocket(port);
		System.out.println("The time server is started in port:"+port);
		Socket socket=null;
		
		TimeServerHandlerExecutePool singleExecute=new TimeServerHandlerExecutePool(50,1000);
		while(true)
		{
			socket=server.accept();
			singleExecute.execute(new TimeServerHandler(socket));
		}
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		if(server!=null)
		{
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
