package com.mason.nioclient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;


public class TimeClientHandle implements Runnable{
	
	private String host;
	private int port;
	private SocketChannel socketChannel;
	private Selector selector;
	private volatile boolean stop;
	
	
	public TimeClientHandle(String host,int port)
	{
		this.host=host==null?"127.0.0.1":host;
		this.port=port;
		try {
			selector=Selector.open();
			socketChannel=SocketChannel.open();
			socketChannel.configureBlocking(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try{
			doConnection();
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		while(!stop)
		{
			try {
				selector.select(1000);
				Set<SelectionKey> selectedKyes=selector.selectedKeys();
				Iterator it =selectedKyes.iterator();
				SelectionKey key=null;
				while(it.hasNext())
				{
					key=(SelectionKey) it.next();
					it.remove();
					try{
					handleInput(key);
					}catch(Exception ex)
					{
						if(key!=null)
						{
							key.cancel();
							if(key.channel()!=null)
							{
								key.channel().close();
							}
						}
					}
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//多路复用关闭后，所有注册上面的Channel和Pipe等资源都会被自动注册并关闭，所以不需要重复使用资源
		if(selector!=null)
		{
			try {
				selector.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private void handleInput(SelectionKey key) throws IOException
	{
		if(key.isValid())
		{
			SocketChannel sc=(SocketChannel)key.channel();
			if(key.isConnectable())
			{
				sc.register(selector, SelectionKey.OP_READ);
				dowrite(sc);
			}else{
				System.exit(1);
			}
		}if(key.isReadable())
		{
			SocketChannel sc=(SocketChannel)key.channel();
			ByteBuffer readBuffer=ByteBuffer.allocate(1024);
			int readButes=sc.read(readBuffer);
			if(readButes>0)
			{
				readBuffer.flip();
				byte[] bytes=new byte[readBuffer.remaining()];
				readBuffer.get(bytes);
				String body=new String(bytes,"UTF-8");
				System.out.println("Now is :"+body);
				this.stop=true;
			}else if(readButes<0)
			{
				key.cancel();
				sc.close();
			}
			
		}
	}
	
	private void doConnection() throws IOException
	{
		if(socketChannel.connect(new InetSocketAddress(host,port))){
			socketChannel.register(selector, SelectionKey.OP_READ);
			dowrite(socketChannel);
		}else
		{
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		}
	}
	
	private void dowrite(SocketChannel socketChannel) throws IOException
	{
		byte[] req="QUERY TIME ORDER".getBytes();
		ByteBuffer byteBuffer=ByteBuffer.allocate(req.length);
		byteBuffer.put(req);
		byteBuffer.flip();
		socketChannel.write(byteBuffer);
		if(!byteBuffer.hasRemaining())
		{
			System.out.println("Send order 2 server succeed.");
		}
	}

}
