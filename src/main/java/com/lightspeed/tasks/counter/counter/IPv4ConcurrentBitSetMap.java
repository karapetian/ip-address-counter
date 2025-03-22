package com.lightspeed.tasks.counter.counter;

import com.lightspeed.tasks.counter.LoggerUtil;
import com.lightspeed.tasks.counter.exception.InvalidIPAddressException;
import org.roaringbitmap.RoaringBitmap;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class IPv4ConcurrentBitSetMap {

    private static final Logger LOGGER = LoggerUtil.getLogger();

    private static final int MAX_OCTET_VALUE = 255;

    private final ConcurrentHashMap<Integer, RoaringBitmap> bitSetMap;

    public IPv4ConcurrentBitSetMap() {
        bitSetMap = new ConcurrentHashMap<>();
        initializeMap();
    }

    public void putIPsToBitSet(List<String> ipAddresses) {
        for (String ip : ipAddresses) {
            try {
                IPAddressComponents shardingKeyAndValue = processIpAddress(ip);

                RoaringBitmap bitMap = bitSetMap.get(shardingKeyAndValue.firstOctet());
                synchronized (bitMap) {
                    bitMap.add(shardingKeyAndValue.concatenatedOctets());
                }
            } catch (InvalidIPAddressException e) {
                LOGGER.warning("Skipping invalid address: " + ip);
            }
        }
    }

    public long getUniqueNumberOfIPs() {
        return bitSetMap.values().stream()
                .mapToLong(RoaringBitmap::getLongCardinality)
                .sum();
    }

    /**
     * Processes an IPv4 address by splitting it into octets and converting it into a {@code IPAddressComponents}.
     * The first octet is extracted as the {@code firstOctet}, and the remaining octets are concatenated (without dots)
     * to form the {@code concatenatedOctets}.
     *
     * @param ip to process
     * @return processed input a IPAddressComponents
     * @throws InvalidIPAddressException when the input is not a valid IPv4 ip or contains invalid chars
     */
    private IPAddressComponents processIpAddress(String ip) throws InvalidIPAddressException {
        int firstOctet = 0;
        int octet = 0;
        int octetIndex = 0;
        int result = 0;

        for (int i = 0; i < ip.length(); i++) {
            char current = ip.charAt(i);

            if (isNotValidChar(current) || octet > MAX_OCTET_VALUE) {
                throw new InvalidIPAddressException("Invalid IP found");
            }

            if (current == '.') {
                if (octetIndex == 0) {
                    firstOctet = octet;
                } else {
                    result = result * 1000 + octet;
                }
                octet = 0;
                octetIndex++;
            } else {
                octet = octet * 10 + (current - '0');
            }
        }

        result = result * 1000 + octet;
        return new IPAddressComponents(firstOctet, result);
    }

    private boolean isNotValidChar(char c) {
        return !(Character.isDigit(c) || c == '.');
    }

    private void initializeMap() {
        for (int i = 0; i <= MAX_OCTET_VALUE; i++) {
            bitSetMap.put(i, new RoaringBitmap());
        }
    }
}
