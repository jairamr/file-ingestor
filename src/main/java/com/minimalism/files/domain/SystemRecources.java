package com.minimalism.files.domain;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

public class SystemRecources {
	private static SystemRecources instance;

	private long jvmMaxMemoryAllocated;
	private int jvmMaxProcessors;
	private long sysTotalPhysicalMemory;
	private long sysTotalVirtualMemory;
	private long sysAvailablePhysicalMemory;
	private long numberOfProcesses;

	private SystemRecources() {
		loadSystemState();
	}

	/**
	 * @return SystemRecources
	 */
	public static synchronized SystemRecources getInstance() {
		if (instance == null) {
			instance = new SystemRecources();
		}
		return instance;
	}

	public void loadSystemState() {
		OperatingSystemMXBean mBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
		this.sysTotalPhysicalMemory = mBean.getTotalMemorySize();
		this.sysTotalVirtualMemory = mBean.getCommittedVirtualMemorySize();
		this.sysAvailablePhysicalMemory = mBean.getFreeMemorySize();

		this.jvmMaxMemoryAllocated = Runtime.getRuntime().maxMemory();
		this.jvmMaxProcessors = Runtime.getRuntime().availableProcessors();
		this.numberOfProcesses = ProcessHandle.allProcesses().count();
	}

	/**
	 * @return long
	 */
	public long getJVMMaxMemoryAllocated() {
		return this.jvmMaxMemoryAllocated;
	}

	/**
	 * @return int
	 */
	public int getJVMMaxProcessors() {
		return this.jvmMaxProcessors;
	}

	/**
	 * @return long
	 */
	public long getSysTotalPhysicalMemory() {
		return this.sysTotalPhysicalMemory;
	}

	/**
	 * @return long
	 */
	public long getSysTotalVirtualMemory() {
		return this.sysTotalVirtualMemory;
	}

	/**
	 * @return long
	 */
	public long getSysAvailablePhysicalMemory() {
		return this.sysAvailablePhysicalMemory;
	}

	/**
	 * @return long
	 */
	public long getNumberOfProcesses() {
		return this.numberOfProcesses;
	}
}
