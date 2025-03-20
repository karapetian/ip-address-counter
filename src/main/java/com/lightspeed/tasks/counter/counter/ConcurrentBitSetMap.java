package com.lightspeed.tasks.counter.counter;

import org.roaringbitmap.RoaringBitmap;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentBitSetMap {

    private final ConcurrentHashMap<Integer, RoaringBitmap> bitSetMap;

    public ConcurrentBitSetMap() {
        bitSetMap = new ConcurrentHashMap<>();
        initializeMap();
    }

    public void putIPsToBitSet(List<String> ipAddresses) {
        for (String ip : ipAddresses) {
            int firstDot = ip.indexOf('.');
            int firstOctetAsKey = Integer.parseInt(ip.substring(0, firstDot));

            int value = extractValue(ip, firstDot);

            RoaringBitmap bitMap = bitSetMap.get(firstOctetAsKey);
            synchronized (bitMap) {
                bitMap.add(value);
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
            bitSetMap.put(i, new RoaringBitmap());
        }
    }

    private int extractValue(String ipAddress, int firstDot) {
        int secondDot = ipAddress.indexOf('.', firstDot + 1);
        int thirdDot = ipAddress.indexOf('.', secondDot + 1);

        int secondOctet = Integer.parseInt(ipAddress.substring(firstDot + 1, secondDot));
        int thirdOctet = Integer.parseInt(ipAddress.substring(secondDot + 1, thirdDot));
        int forthOctet = Integer.parseInt(ipAddress.substring(thirdDot + 1));

        return  (secondOctet * 1_000_000) + (thirdOctet * 1_000) + forthOctet;
    }
}
