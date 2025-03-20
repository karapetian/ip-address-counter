package com.lightspeed.tasks.counter.counter;

import org.roaringbitmap.RoaringBitmap;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.lightspeed.tasks.counter.counter.IPConverter.convertIPToInt;

public class ConcurrentBitSetMap {

    private final ConcurrentHashMap<String, RoaringBitmap> bitSetMap;

    public ConcurrentBitSetMap() {
        bitSetMap = new ConcurrentHashMap<>();
        initializeMap();
    }

    public void putIPsToBitSet(List<String> ipAddresses) {
        for (String ip : ipAddresses) {
            String key = ip.substring(0, ip.indexOf("."));
            int ipInt = convertIPToInt(ip);

            RoaringBitmap bitMap = bitSetMap.get(key);
            synchronized (bitMap) {
                bitMap.add(ipInt);
            }
        }
    }

    public long getUniqueNumberOfIPs() {
        return bitSetMap.values().stream()
                .mapToLong(bitSet -> bitSet.getLongCardinality())
                .sum();
    }

    private void initializeMap() {
        for (int i = 0; i <= 255; i++) {
            bitSetMap.put(String.valueOf(i), new RoaringBitmap());
        }
    }
}
