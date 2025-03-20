package com.lightspeed.tasks.counter;

import com.lightspeed.tasks.counter.counter.CountingResult;
import com.lightspeed.tasks.counter.counter.IPAddressCounter;
import com.lightspeed.tasks.counter.splitter.FileSplitter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IPAddressCounterTest {

    private IPAddressCounter ipAddressCounter;

    @Test
    void testOneIP() {
        ipAddressCounter = new IPAddressCounter(
                new FileSplitter("src/test/resources/testCount/1ip.txt"));
        CountingResult countingResult = ipAddressCounter.processFile();

        assertEquals(1, countingResult.uniqueIPs());
    }

    @Test
    void testTwoIPs() {
        ipAddressCounter = new IPAddressCounter(
                new FileSplitter("src/test/resources/testCount/2ips.txt"));
        CountingResult countingResult = ipAddressCounter.processFile();

        assertEquals(2, countingResult.uniqueIPs());
    }

    @Test
    void test150IPs() {
        ipAddressCounter = new IPAddressCounter(
                new FileSplitter("src/test/resources/testCount/150ips.txt"));
        CountingResult countingResult = ipAddressCounter.processFile();

        assertEquals(150, countingResult.uniqueIPs());
    }

    @Test
    void testEmptyFile() {
        ipAddressCounter = new IPAddressCounter(
                new FileSplitter("src/test/resources/exceptionalCases/empty.txt"));
        CountingResult countingResult = ipAddressCounter.processFile();

        assertEquals(0, countingResult.uniqueIPs());
    }

    @Test
    void test2IPsWithBlankLine() {
        ipAddressCounter = new IPAddressCounter(
                new FileSplitter("src/test/resources/exceptionalCases/2ips_blank_line.txt"));
        CountingResult countingResult = ipAddressCounter.processFile();

        assertEquals(2, countingResult.uniqueIPs());
    }
}