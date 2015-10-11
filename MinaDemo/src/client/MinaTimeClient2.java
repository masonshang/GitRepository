package client;


import java.util.Scanner;

import org.apache.mina.core.session.IoSession;

import common.SessionSingle;

/**
 * Created by Loftor on 2014/8/15.
 */
public class MinaTimeClient2 {

	public static void main(String[] args) {
SessionSingle.getInstance();
		//        IoConnector connector = new NioSocketConnector();
//        connector.getFilterChain().addLast( "logger", new LoggingFilter() );
//        connector.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new PrefixedStringCodecFactory(Charset.forName("UTF-8"))));
//        connector.setHandler(new TimeClientHander());
//        ConnectFuture connectFuture = connector.connect(new InetSocketAddress("192.168.1.105",BaseConfig.PORT));
//        //等待建立连接
//        connectFuture.awaitUninterruptibly();
//        System.out.println("连接成功");
//
//        IoSession session = (IoSession) connectFuture.getSession().getAttribute("SessionTest");
//        if(session==null)
//        {
//        	session = (IoSession) connectFuture.getSession();
//        	System.out.println("session");
//        	session.setAttribute("SessionTest");
//        }
		IoSession session=SessionSingle.getInstance().getSession();
		System.out.println(SessionSingle.getInstance().getMapValue());
        Scanner sc = new Scanner(System.in);

        boolean quit = false;

        while(!quit){

            String str = sc.next();
            if(str.equalsIgnoreCase("quit")){
                quit = true;
            }
            session.write(str);
        }

        //关闭
        if(session!=null){
            if(session.isConnected()){
                session.getCloseFuture().awaitUninterruptibly();
            }
            //connector.dispose(true);
        }


    }

}