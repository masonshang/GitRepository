package common;

import java.util.HashMap;

import org.apache.mina.core.session.IoSession;

public class SessionSingle {
	
	private static SessionSingle instance=null;
	
	public synchronized static SessionSingle getInstance(){
		if(instance==null)
		{
			instance=new SessionSingle();
			System.out.println("^^^^^^^^^");
		}
		return instance;
	}
	public static IoSession session;
	public static HashMap<String,String> map=new HashMap<String,String>();

	public void addMap(String value)
	{
		map.put("1", value);
	}
	
	public String getMapValue()
	{
		return map.get("1");
	}
	
	public IoSession getSession() {
		return session;
	}
	public void setSession(IoSession session) {
		this.session = session;
	}
	
	
	
	
//	public void addSession(IoSession session)
//	{
//		this.session=session;
//	}
//
//	public IoSession getSession()
//	{
//		return this.session;
//	}
//	
}
