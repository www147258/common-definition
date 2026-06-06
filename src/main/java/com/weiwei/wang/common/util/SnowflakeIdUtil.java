package com.weiwei.wang.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.Objects;

public final class SnowflakeIdUtil {

    private static final long DEFAULT_EPOCH = 1704067200000L;
    private static final long WORKER_ID_BITS = 5L;
    private static final long DATA_CENTER_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    private static final SecureRandom RANDOM = new SecureRandom();

    private final long epoch;
    private final long workerId;
    private final long dataCenterId;
    private long sequence;
    private long lastTimestamp = -1L;

    private SnowflakeIdUtil(long epoch, long workerId, long dataCenterId) {
        if (workerId < 0 || workerId > MAX_WORKER_ID) {
            throw new IllegalArgumentException("workerId must be between 0 and " + MAX_WORKER_ID);
        }
        if (dataCenterId < 0 || dataCenterId > MAX_DATA_CENTER_ID) {
            throw new IllegalArgumentException("dataCenterId must be between 0 and " + MAX_DATA_CENTER_ID);
        }
        this.epoch = epoch;
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    public static SnowflakeIdUtil createDefault() {
        return createDefault(0);
    }

    public static SnowflakeIdUtil createDefault(long conflictFactor) {
        InetAddress address = getLocalInet4Address();
        long ipHash = hashIp(address);
        long mixed = mix(ipHash, conflictFactor);
        long workerId = mixed & MAX_WORKER_ID;
        long dataCenterId = (mixed >> WORKER_ID_BITS) & MAX_DATA_CENTER_ID;
        return new SnowflakeIdUtil(DEFAULT_EPOCH, workerId, dataCenterId);
    }

    public static SnowflakeIdUtil create(long workerId, long dataCenterId) {
        return new SnowflakeIdUtil(DEFAULT_EPOCH, workerId, dataCenterId);
    }

    public synchronized long nextId() {
        long timestamp = currentTimeMillis();
        if (timestamp < lastTimestamp) {
            throw new IllegalStateException("Clock moved backwards by " + (lastTimestamp - timestamp) + "ms");
        }
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        return ((timestamp - epoch) << TIMESTAMP_SHIFT)
                | (dataCenterId << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    public long getWorkerId() {
        return workerId;
    }

    public long getWorkId() {
        return workerId;
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public long getDataId() {
        return dataCenterId;
    }

    public static String getLocalIp() {
        return getLocalInet4Address().getHostAddress();
    }

    private long waitNextMillis(long lastTimestamp) {
        long timestamp = currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = currentTimeMillis();
        }
        return timestamp;
    }

    private long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    private static InetAddress getLocalInet4Address() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (!networkInterface.isUp() || networkInterface.isLoopback() || networkInterface.isVirtual()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
                        return address;
                    }
                }
            }
            return InetAddress.getLocalHost();
        } catch (Exception exception) {
            byte[] randomIp = new byte[]{127, 0, 0, (byte) RANDOM.nextInt(255)};
            try {
                return InetAddress.getByAddress(randomIp);
            } catch (Exception ignored) {
                throw new IllegalStateException("Cannot resolve local IPv4 address", exception);
            }
        }
    }

    private static long hashIp(InetAddress address) {
        byte[] bytes = Objects.requireNonNull(address, "address").getAddress();
        long hash = 0L;
        for (byte value : bytes) {
            hash = (hash << Byte.SIZE) | (value & 0xFFL);
        }
        return hash;
    }

    private static long mix(long ipHash, long conflictFactor) {
        long value = ipHash ^ (conflictFactor * 0x9E3779B97F4A7C15L);
        value ^= (value >>> 30);
        value *= 0xBF58476D1CE4E5B9L;
        value ^= (value >>> 27);
        value *= 0x94D049BB133111EBL;
        return value ^ (value >>> 31);
    }
}
