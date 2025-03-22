package com.lightspeed.tasks.counter;

import com.lightspeed.tasks.counter.counter.CountingResult;
import com.lightspeed.tasks.counter.counter.IPAddressCounter;
import com.lightspeed.tasks.counter.splitter.FileSplitter;
import com.lightspeed.tasks.counter.exception.SourceFileNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IPAddressCounterTest {

    private IPAddressCounter ipAddressCounter;

    @Test
    void testOneIP() {
        ipAddressCounter = new IPAddressCounter(
                new FileSplitter("src/test/resources/testCount/1ip.txt"));
        CountingResult countingResult = ipAddressCounter.processFile();

        assertEquals(1, countingResult.uniqueIPCount());
    }

    @Test
    void testTwoIPs() {
        ipAddressCounter = new IPAddressCounter(
                new FileSplitter("src/test/resources/testCount/2ips.txt"));
        CountingResult countingResult = ipAddressCounter.processFile();

        assertEquals(2, countingResult.uniqueIPCount());
    }

    @Test
    void test150IPs() {
        ipAddressCounter = new IPAddressCounter(
                new FileSplitter("src/test/resources/testCount/150ips.txt"));
        CountingResult countingResult = ipAddressCounter.processFile();

        assertEquals(150, countingResult.uniqueIPCount());
    }

    @Test
    void testEmptyFile() {
        ipAddressCounter = new IPAddressCounter(
                new FileSplitter("src/test/resources/edgeCases/empty.txt"));
        CountingResult countingResult = ipAddressCounter.processFile();

        assertEquals(0, countingResult.uniqueIPCount());
    }

    @Test
    void test2IPs() {
        ipAddressCounter = new IPAddressCounter(
                new FileSplitter("src/test/resources/edgeCases/2ips.txt"));
        CountingResult countingResult = ipAddressCounter.processFile();

        assertEquals(2, countingResult.uniqueIPCount());
    }

    @Test
    void test2IPsWithBlankLine() {
        ipAddressCounter = new IPAddressCounter(
                new FileSplitter("src/test/resources/edgeCases/2ips_blank_line.txt"));
        CountingResult countingResult = ipAddressCounter.processFile();

        assertEquals(2, countingResult.uniqueIPCount());
    }

    @Test
    void test3IPsWithInvalidLine() {
        ipAddressCounter = new IPAddressCounter(
                new FileSplitter("src/test/resources/edgeCases/3ips_invalid_line.txt"));
        CountingResult countingResult = ipAddressCounter.processFile();

        assertEquals(3, countingResult.uniqueIPCount());
    }

    @Test
    void test4IPsWithInvalidIPs() {
        ipAddressCounter = new IPAddressCounter(
                new FileSplitter("src/test/resources/edgeCases/4ips_invalid_ips.txt"));
        CountingResult countingResult = ipAddressCounter.processFile();

        assertEquals(4, countingResult.uniqueIPCount());
    }

    @Test
    void testAbsentFile() {
        String absentFilePath = "absent.txt";
        ipAddressCounter = new IPAddressCounter(new FileSplitter(absentFilePath));

        SourceFileNotFoundException exception = assertThrows(SourceFileNotFoundException.class,
                () -> ipAddressCounter.processFile());

        assertEquals("File cannot be found: " + absentFilePath, exception.getMessage());
    }
}