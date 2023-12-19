package com.example.photos.util;

/**
 * @Auther: raolongxiang
 * @Date: 2023/12/6
 * @Description: com.example.photos.util
 */
public class IdWorker {

    private final long twepoch = 1398049504651L;
    private final long workerIdBits = 10L;
    private final long maxWorkerId = 1023L;
    private final long sequenceBits = 12L;
    private final long workerIdShift = 12L;
    private final long timestampLeftShift = 22L;
    private final long sequenceMask = 4095L;
    private long workerId;
    private long dataCenterId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    private volatile static IdWorker idWorker;

    public static IdWorker getInstance(){
        if(idWorker == null){
            getIdWorker();
        }
        return idWorker;
    }

    private static synchronized void getIdWorker(){
        if(idWorker == null){
            idWorker = new IdWorker(1);
        }
    }

    private IdWorker(long workerId) {
        if (workerId <= 1023L && workerId >= 0L) {
            this.workerId = workerId;
        } else {
            throw new IllegalArgumentException(String.format("workerId can't be greater than %d or less than 0", 1023L));
        }
    }

    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (timestamp < this.lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp));
        } else {
            if (this.lastTimestamp == timestamp) {
                this.sequence = this.sequence + 1L & 4095L;
                if (this.sequence == 0L) {
                    timestamp = this.tilNextMillis(this.lastTimestamp);
                }
            } else {
                this.sequence = 0L;
            }

            this.lastTimestamp = timestamp;
            return timestamp - 1398049504651L << 22 | this.workerId << 12 | this.sequence;
        }
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp;
        for(timestamp = this.timeGen(); timestamp <= lastTimestamp; timestamp = this.timeGen()) {
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

}
