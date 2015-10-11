package com.mason.fakenioserver;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimeServerHandlerExecutePool {
	
	private ExecutorService executor;
	
	public TimeServerHandlerExecutePool(int maxpoolSize,int queueSize)
	{
		executor=new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),maxpoolSize,
				120L,TimeUnit.SECONDS,new ArrayBlockingQueue<java.lang.Runnable>(queueSize));
	}
	public void execute(java.lang.Runnable task)
	{
		executor.execute(task);
	}

}
