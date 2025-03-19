package com.lightspeed.tasks.counter.counter;

import com.lightspeed.tasks.counter.splitter.FileSplitter;

import java.time.Duration;
import java.time.Instant;

public class IPAddressCounter {

    private final FileSplitter fileSplitter;

    private final ConcurrentBitSetMap counter;

    public IPAddressCounter(FileSplitter fileSplitter) {
        this.fileSplitter = fileSplitter;
        this.counter = new ConcurrentBitSetMap();
    }

    public CountingResult processFile() {
        Instant start = Instant.now();

        fileSplitter.extractFileChunks()
                .parallelStream()
                .map(chunkRange -> fileSplitter.getLinesPerChunk(chunkRange))
                .forEach(listOfIPs -> counter.putIPsToBitSet(listOfIPs));

        Instant end = Instant.now();

        return new CountingResult(counter.getUniqueNumberOfIPs(), Duration.between(start, end).toMillis());
    }
}
