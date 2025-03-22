package com.lightspeed.tasks.counter;

import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtil {

    private static Logger sharedLogger;

    private LoggerUtil() {
    }

    public static Logger getLogger() {
        if (sharedLogger == null) {
            sharedLogger = Logger.getLogger("UniqueIPCounter");

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter() {
                @Override
                public String format(LogRecord logRecord) {
                    return logRecord.getLevel() + ": " + logRecord.getMessage() + "\n";
                }
            });

            sharedLogger.addHandler(consoleHandler);
            sharedLogger.setUseParentHandlers(false);
        }
        return sharedLogger;
    }
}
