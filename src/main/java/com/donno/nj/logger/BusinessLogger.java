package com.donno.nj.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BusinessLogger {
    private static final Logger logger = LoggerFactory.getLogger(BusinessLogger.class);

    public static void log(final String info) {
        logger.info(info);
    }
}
