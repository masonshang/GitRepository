package client;


import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;

import common.BaseConfig;
import common.SessionSingle;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * Created by Loftor on 2014/8/15.
 */
public class MinaTimeClient {

	public static void main(String[] args) {
        IoConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast( "logger", new LoggingFilter() );
        connector.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new PrefixedStringCodecFactory(Charset.forName("UTF-8"))));
        connector.setHandler(new TimeClientHander());
        ConnectFuture connectFuture = connector.connect(new InetSocketAddress("127.0.0.1",BaseConfig.PORT));
        //�ȴ���������
        connectFuture.awaitUninterruptibly();
        System.out.println("���ӳɹ�");

        IoSession session = (IoSession) connectFuture.getSession();
        SessionSingle.getInstance().setSession(session);
        SessionSingle.getInstance().addMap("test1");
        Scanner sc = new Scanner(System.in);

        boolean quit = false;

        while(!quit){

            String str = sc.next();
            if(str.equalsIgnoreCase("quit")){
                quit = true;
            }
            session.write(str);
        }

        //�ر�
        if(session!=null){
            if(session.isConnected()){
                session.getCloseFuture().awaitUninterruptibly();
            }
            connector.dispose(true);
        }


    }

}