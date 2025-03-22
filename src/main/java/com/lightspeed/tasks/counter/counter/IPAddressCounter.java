package com.lightspeed.tasks.counter.counter;

import com.lightspeed.tasks.counter.splitter.FileSplitter;

import java.time.Duration;
import java.time.Instant;

public class IPAddressCounter {

    private final FileSplitter fileSplitter;

    private final IPv4ConcurrentBitSetMap bitSetMap;

    public IPAddressCounter(FileSplitter fileSplitter) {
        this.fileSplitter = fileSplitter;
        this.bitSetMap = new IPv4ConcurrentBitSetMap();
    }

    public CountingResult processFile() {
        Instant start = Instant.now();

        fileSplitter.extractFileChunks()
                .parallelStream()
                .map(fileSplitter::getLinesPerChunk)
                .forEach(bitSetMap::putIPsToBitSet);

        Instant end = Instant.now();

        return new CountingResult(bitSetMap.getUniqueNumberOfIPs(), Duration.between(start, end).toMillis());
    }
}
