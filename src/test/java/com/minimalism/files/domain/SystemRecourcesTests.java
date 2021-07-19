package com.minimalism.files.domain;

import org.junit.jupiter.api.Test;

public class SystemRecourcesTests {
    @Test
    void testLoadSystemState() {
        SystemRecources iut = SystemRecources.getInstance();
        iut.loadSystemState();
        System.out.println(String.format("JVM Max allocated Memory: %d", iut.getJVMMaxMemoryAllocated()));
        System.out.println(String.format("JVM Max Processors: %d", iut.getJVMMaxProcessors()));
        System.out.println(String.format("System Available Physical Memory: %d", iut.getSysAvailablePhysicalMemory()));
        System.out.println(String.format("System Total Physical Memory: %d", iut.getSysTotalPhysicalMemory()));
        System.out.println(String.format("System Total Virtual Memory: %d", iut.getSysTotalVirtualMemory()));
        System.out.println(String.format("System number of processes: %d", iut.getNumberOfProcesses()));
    }
}
