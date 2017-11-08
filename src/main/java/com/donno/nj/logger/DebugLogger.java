package com.donno.nj.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugLogger {
    private static final Logger logger = LoggerFactory.getLogger(DebugLogger.class);

    public static void log(final String info) {
        logger.debug(info);
    }
}
