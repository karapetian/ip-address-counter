package com.lightspeed.tasks.counter;

import com.lightspeed.tasks.counter.counter.CountingResult;
import com.lightspeed.tasks.counter.counter.IPAddressCounter;
import com.lightspeed.tasks.counter.splitter.FileSplitter;

public class Application {

    public static void main(String[] args) {
        if (args.length == 0 || args.length > 2) {
            throw new IllegalArgumentException(
                    "Exactly 1 parameter is required, and a maximum of 2 parameters are allowed.");
        }

        FileSplitter fileSplitter = createFileSplitter(args);
        IPAddressCounter ipAddressCounter = new IPAddressCounter(fileSplitter);
        CountingResult countingResult = ipAddressCounter.processFile();

        System.out.println("The unique number of IPs: " + countingResult.uniqueIPs());
        System.out.println("Execution time: " + countingResult.executionTime() + " ms");
    }

    private static FileSplitter createFileSplitter(String[] args) {
        FileSplitter fileSplitter;
        String sourceFilePath = args[0];
        if (args.length == 2) {
            fileSplitter = new FileSplitter(sourceFilePath, Long.parseLong(args[1]));
        } else {
            fileSplitter = new FileSplitter(sourceFilePath);
        }
        return fileSplitter;
    }
}
