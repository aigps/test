package org.test.util;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;


public class ConcurrentMapTest {
	//保存数据的任务
	private static final ThreadPoolExecutor putExecutor = new ThreadPoolExecutor(1, 1, 30, TimeUnit.SECONDS	, new LinkedBlockingDeque<Runnable>());
	private static ScheduledExecutorService sche = Executors.newSingleThreadScheduledExecutor();
	public static long maxPut = 1000000;
	
	public static Random random = new Random(100000);
	@Before
	public void init()throws Exception{
		System.out.println("初始化前的线程数-->"+AppPerformUtil.getThreadUsage());
		sche.scheduleAtFixedRate(new Runnable() {
			public void run() {
				try {
					System.out.println("执行中的线程数-->"+AppPerformUtil.getThreadUsage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0,2, TimeUnit.SECONDS);
	}
	
	@After
	public void destroy()throws Exception{
		System.out.println("结束后的线程数-->"+AppPerformUtil.getThreadUsage());
		sche.shutdown();
	}
	
	@Test
	public void testGoogleMapThread(){
		System.out.println(" ----------测试google map ----------------");
		Map<String, String> map = new ConcurrentLinkedHashMap.Builder<String, String>().maximumWeightedCapacity(1000).build();
		long counter = 0;
		while(counter<maxPut){
			map.put( random.nextLong()+"", "");
			counter++;
		}
	}
	
	
	@Test
	public void testMapThread(){
		System.out.println(" ----------测试jdk map ----------------");
		Map<String, String> map = new ConcurrentHashMap<String, String>();
		long counter = 0;
		while(counter<maxPut){
			map.put( random.nextLong()+"","");
			counter++;
		}
	}
	

}
