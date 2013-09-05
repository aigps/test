package org.test.util;

/**
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



/**
 * <pre>��ȡ��ǰӦ�õ����ܹ�����
 *  Title:
 *  Description:
 * </pre>
 * 
 * @author 
 * @version 1.00.00
 * 
 *          <pre>
 *  �޸ļ�¼
 *     �޸ĺ�汾:     �޸��ˣ�  �޸�����:     �޸�����:
 * </pre>
 */
public class AppPerformUtil{
	
	/**
	 * ϵͳʱ��
	 */
	private static long lastUpdatetime;
	/**
	 * ��ǰӦ��ʹ�õ�CPUʱ��
	 */
	private static long lastProcessCpuTime;
	
	
	/**
	 * ��ȡ��ǰ�Ľ��̵��̸߳���
	 * @return
	 * @throws Exception
	 */
	public static int getThreadUsage()throws Exception{
		java.lang.management.ThreadMXBean threadBean = java.lang.management.ManagementFactory.getThreadMXBean();
		return threadBean.getThreadCount();
	}
	
	/**
	 * ��ȡ��ǰ���̵��ڴ�ʹ��
	 * @return
	 * @throws Exception
	 */
	public static long getMemUsage()throws Exception{
		MemoryMXBean memoryUsage = ManagementFactory.getMemoryMXBean();
		return memoryUsage.getHeapMemoryUsage().getUsed()/1024/1024;
	}
	
	/**
	 * ��ȡϵͳCPUʹ����
	 * @return
	 * @throws Exception
	 */
	public static int getSysCpuUsage()throws Exception{
		com.sun.management.OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(com.sun.management.OperatingSystemMXBean.class);
		return (int)(osBean.getSystemCpuLoad()*100);
	}
	
	/**
	 * ��ȡϵͳCPUʹ����
	 * @return
	 * @throws Exception
	 */
	public static int getSysMemoryUsage()throws Exception{
		com.sun.management.OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(com.sun.management.OperatingSystemMXBean.class);
		return (int)(osBean.getFreePhysicalMemorySize()*100);
	}
	
	public static double getSysTotalMemory()throws Exception{
		com.sun.management.OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(com.sun.management.OperatingSystemMXBean.class);
		return Math.round((double)osBean.getTotalPhysicalMemorySize()/1024/1024/1024);
	}
	
	
	public static double getSysFreeMemory()throws Exception{
		com.sun.management.OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(com.sun.management.OperatingSystemMXBean.class);
		return ((double)osBean.getFreePhysicalMemorySize()/1024/1024/1024.0);
	}
	
	public static double getSysVirtualMemory()throws Exception{
		com.sun.management.OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(com.sun.management.OperatingSystemMXBean.class);
		return ((double)osBean.getCommittedVirtualMemorySize()/1024/1024/1024.0);

	}
	
	/**
	 * ��ȡCPU��ʹ����
	 * @return
	 * @throws Exception
	 */
	public static int getCpuUsage()throws Exception{
		com.sun.management.OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(com.sun.management.OperatingSystemMXBean.class);
		return (int)(osBean.getProcessCpuLoad()*100);
	}
	
	/**
	 * ��ȡCPU��ʹ����
	 * @return
	 * @throws Exception
	 */
	public static int getCpuUsageOld()throws Exception{
		int cpuUsage = 0;
		OperatingSystemMXBean o = ManagementFactory.getOperatingSystemMXBean();
		int nProcessors = o.getAvailableProcessors();
		com.sun.management.OperatingSystemMXBean osMxBean = null;
		if (o instanceof com.sun.management.OperatingSystemMXBean) {
			osMxBean = (com.sun.management.OperatingSystemMXBean) o;
		}
		RuntimeMXBean rtMxBean = ManagementFactory.getRuntimeMXBean();
		long uptime = rtMxBean.getUptime();
		long processCpuTime = osMxBean == null ? 0 : osMxBean.getProcessCpuTime();
		if (lastUpdatetime > 0 && uptime > lastUpdatetime) {
			long elapsedCpu = processCpuTime - lastProcessCpuTime;
			long elapsedTime = uptime - lastUpdatetime;
			cpuUsage = Math.min(100, (int)(elapsedCpu/ (elapsedTime * 10000F * nProcessors)));
		}
		lastUpdatetime = uptime;
		lastProcessCpuTime = processCpuTime;
//		System.err.println("jmx cpu usage:" + cpuUsage);
		return cpuUsage;
	}
	
	public static long getCurrentPid()throws Exception{
		java.lang.management.RuntimeMXBean runtime = java.lang.management.ManagementFactory.getRuntimeMXBean();
		String pidName = runtime.getName();
		long pid = Long.parseLong(pidName.substring(0,pidName.indexOf("@")));
		return pid;
	}
	

  	/**
	 * ���ݽ���ID��ȡ�˿���Ϣ
	 * @param pid
	 * @return ����TCP��ȡtcpPortList,����UDP��ȡudpPortList
	 * @throws Exception
	 */
	public static HashMap<String,HashSet<String>> getPortByPID(String pid) throws Exception {
		if (pid == null)
			return null;
		InputStream is = null;
		InputStreamReader ir = null;
		BufferedReader br = null;
		String line = null;
		String TCP_TYPE = "TCP";
		String UDP_TYPE = "UDP";
		String LISTENING_STATE_TYPE = "LISTENING";
		HashMap<String,HashSet<String>> portMap = new HashMap<String, HashSet<String>>();
		HashSet<String> tcpPortList = new HashSet<String>();
		HashSet<String> udpPortList = new HashSet<String>();
		portMap.put(TCP_TYPE, tcpPortList);
		portMap.put(UDP_TYPE, udpPortList);
		String[] array = null;
		try {
			Process p = Runtime.getRuntime().exec("netstat /ano");
			is = p.getInputStream();
			ir = new InputStreamReader(is);
			br = new BufferedReader(ir);
			while ((line = br.readLine()) != null && pid != null) {
				if (line.indexOf(pid) != -1 && line.indexOf(LISTENING_STATE_TYPE)!=-1) {
					line = line.replaceFirst("\\s+", "");
					array = line.split("\\s+");
					String type = array[0];
					String port = (array[1].split(":")[1]);
					if(type.indexOf(TCP_TYPE)!=-1){
						tcpPortList.add(port);
					}else{
						udpPortList.add(port);
					}
				}
			}
		} catch (IOException e) {
			throw new Exception("��ȡ���̶˿���Ϣ����");
		} finally {
			if (br != null)
				br.close();
			if (ir != null)
				ir.close();
			if (is != null)
				is.close();
		}
		return portMap;
	}



	public static void main(String[] args)throws Exception {
		long pid = AppPerformUtil.getCurrentPid();
		new Thread(new Runnable() {
			public void run() {
				while(true){
					double i = 100+100.0;
				}
			}
		}).start();
		while(true){
			String cpu = AppPerformUtil.getSysCpuUsage()+"";
			System.err.println("sytem  cpu ="+cpu);
			System.err.println("current jvm  cpu ="+AppPerformUtil.getCpuUsage());
			System.err.println("sytem total memory ="+AppPerformUtil.getSysTotalMemory());
			System.err.println("sytem free memory ="+AppPerformUtil.getSysFreeMemory());
			System.err.println("sytem vitual memory ="+AppPerformUtil.getSysVirtualMemory());
			Thread.currentThread().sleep(1000);
		}
	}
	

}
