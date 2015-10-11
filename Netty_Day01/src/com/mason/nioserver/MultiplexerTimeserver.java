package com.mason.nioserver;

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


public class MultiplexerTimeserver implements Runnable{
	//初始化多路复用器
	private Selector selector;
	//监听通道
	private ServerSocketChannel serverChannel;
	private volatile boolean stop;
	
	public MultiplexerTimeserver(int port)
	{
		try {
			selector=Selector.open();
			serverChannel=ServerSocketChannel.open();
			//设置为异步非阻塞模式
			serverChannel.configureBlocking(false);
			//绑定监听端口
			serverChannel.socket().bind(new InetSocketAddress(port),1024);
			//
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("The time server is start in port:"+port);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void stop()
	{
		this.stop=true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
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
			//处理新接入的请求消息
			if(key.isAcceptable())
			{
				ServerSocketChannel ssc=(ServerSocketChannel)key.channel();
				SocketChannel sc=ssc.accept();
				sc.configureBlocking(false);
				sc.register(selector, SelectionKey.OP_READ);
			}
			if(key.isReadable())
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
					System.out.println("The time server receive order:"+body);
					String currentTime="QUERY TIME ORDER".equals(body)?new Date(
							System.currentTimeMillis()).toString():"BAD ORDER";
					dowrite(sc,currentTime);
				}else if(readButes<0)
				{
					key.cancel();
					sc.close();
				}
				
			}
		}
	}
	
	private void dowrite(SocketChannel sc,String response) throws IOException
	{
		if(response!=null&&response.trim().length()>0)
		{
			byte[] bytes=response.getBytes();
			ByteBuffer writeBuffer=ByteBuffer.allocate(bytes.length);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			sc.write(writeBuffer);
		}
	}
	
	
}
