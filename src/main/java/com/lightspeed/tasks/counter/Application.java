package com.lightspeed.tasks.counter;

import com.lightspeed.tasks.counter.counter.CountingResult;
import com.lightspeed.tasks.counter.counter.IPAddressCounter;
import com.lightspeed.tasks.counter.exception.FileProcessingException;
import com.lightspeed.tasks.counter.splitter.FileSplitter;

import java.util.logging.Logger;

public class Application {

    private static final Logger LOGGER = LoggerUtil.getLogger();

    public static void main(String[] args) {
        if (args.length == 0 || args.length > 2) {
            throw new IllegalArgumentException(
                    "Exactly 1 parameter is required, and a maximum of 2 parameters are allowed.");
        }

        FileSplitter fileSplitter = createFileSplitter(args);
        IPAddressCounter ipAddressCounter = new IPAddressCounter(fileSplitter);

        try {
            CountingResult countingResult = ipAddressCounter.processFile();

            LOGGER.info("The number of unique IPs: " + countingResult.uniqueIPCount());
            LOGGER.info("Execution time: " + countingResult.executionTime() + " ms");
        } catch (FileProcessingException ex) {
            LOGGER.severe(ex.getMessage());
        }
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
