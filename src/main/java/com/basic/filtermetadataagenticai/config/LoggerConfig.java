package com.basic.filtermetadataagenticai.config;

/*
 * Created by Ankul on 18-09-2026 15:29
 */


import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;

public class LoggerConfig {
    public static Logger getLogger(String className) {
        Logger logger = Logger.getLogger(className);

        try {
            // Ensure logs directory exists
            File logDir = new File("logs");
            if (!logDir.exists()) {
                logDir.mkdirs(); // create directory if missing
            }
            // String logFilePattern = "logs/orders_%g_%u.log";
            String date = java.time.LocalDate.now().toString(); // 2026-09-04
            String logFilePattern = "logs/orders_" + date + "_%g.log";
            FileHandler fileHandler = new FileHandler(logFilePattern, 1024 * 1024, 10, true);
            fileHandler.setFormatter(new XMLFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Failed to set up logger: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return logger;
    }
}
