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
	//�������ݵ�����
	private static final ThreadPoolExecutor putExecutor = new ThreadPoolExecutor(1, 1, 30, TimeUnit.SECONDS	, new LinkedBlockingDeque<Runnable>());
	private static ScheduledExecutorService sche = Executors.newSingleThreadScheduledExecutor();
	public static long maxPut = 1000000;
	
	public static Random random = new Random(100000);
	@Before
	public void init()throws Exception{
		System.out.println("��ʼ��ǰ���߳���-->"+AppPerformUtil.getThreadUsage());
		sche.scheduleAtFixedRate(new Runnable() {
			public void run() {
				try {
					System.out.println("ִ���е��߳���-->"+AppPerformUtil.getThreadUsage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0,2, TimeUnit.SECONDS);
	}
	
	@After
	public void destroy()throws Exception{
		System.out.println("��������߳���-->"+AppPerformUtil.getThreadUsage());
		sche.shutdown();
	}
	
	@Test
	public void testGoogleMapThread(){
		System.out.println(" ----------����google map ----------------");
		Map<String, String> map = new ConcurrentLinkedHashMap.Builder<String, String>().maximumWeightedCapacity(1000).build();
		long counter = 0;
		while(counter<maxPut){
			map.put( random.nextLong()+"", "");
			counter++;
		}
	}
	
	
	@Test
	public void testMapThread(){
		System.out.println(" ----------����jdk map ----------------");
		Map<String, String> map = new ConcurrentHashMap<String, String>();
		long counter = 0;
		while(counter<maxPut){
			map.put( random.nextLong()+"","");
			counter++;
		}
	}
	

}
