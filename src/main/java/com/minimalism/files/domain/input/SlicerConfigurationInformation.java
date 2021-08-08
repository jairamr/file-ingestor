package com.minimalism.files.domain.input;

public class SlicerConfigurationInformation {
    private int numberOfThreads;
    private int threadReadBufferSize;
    private long fileSize;
    private int iterationCount;
    private int numberOfCores;

    public SlicerConfigurationInformation() {}

    public SlicerConfigurationInformation(int numberOfThreads, int threadReadBufferSize, long fileSize, int iterationCount, int numberOfCores) {
        this.numberOfThreads = numberOfThreads;
        this.threadReadBufferSize = threadReadBufferSize;
        this.fileSize = fileSize;
        this.iterationCount = iterationCount;
        this.numberOfCores = numberOfCores;
    }
    
    /** 
     * @return int
     */
    public int getNumberOfThreads() {
        return numberOfThreads;
    }
    
    /** 
     * @param numberOfThreads
     */
    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }
    
    /** 
     * @return int
     */
    public int getThreadReadBufferSize() {
        return threadReadBufferSize;
    }
    
    /** 
     * @param threadReadBufferSize
     */
    public void setThreadReadBufferSize(int threadReadBufferSize) {
        this.threadReadBufferSize = threadReadBufferSize;
    }
    
    /** 
     * @return long
     */
    public long getFileSize() {
        return fileSize;
    }
    
    /** 
     * @param fileSize
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
    
    /** 
     * @return int
     */
    public int getIterationCount() {
        return iterationCount;
    }
    
    /** 
     * @param iterationCount
     */
    public void setIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
    }
    
    /** 
     * @return int
     */
    public int getNumberOfCores() {
        return numberOfCores;
    }
    
    /** 
     * @param numberOfCores
     */
    public void setNumberOfCores(int numberOfCores) {
        this.numberOfCores = numberOfCores;
    }

    
}
