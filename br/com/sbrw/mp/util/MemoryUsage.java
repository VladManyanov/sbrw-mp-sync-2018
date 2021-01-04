/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MemoryUsage implements Runnable {
	private String filename;
  
	public MemoryUsage(Integer port) {
		this.filename = "memorylog-" + port.toString() + ".log";
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleWithFixedDelay(this, 0L, 1L, TimeUnit.HOURS);
	}
  
	public void run() {
		Long freeMemory = Long.valueOf(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
		freeMemory = Long.valueOf(freeMemory.longValue() / 1024L / 1024L);
		writeToFile(freeMemory.toString() + "\n");
	}
  
	private void writeToFile(String data) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			File file = new File(this.filename);
			if (!file.exists()) {
				file.createNewFile(); 
			}
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			bw.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		try {
			if (bw != null) {
				bw.close(); 
			}
			if (fw != null) {
				fw.close(); 
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			} 
		} 
	}
}
