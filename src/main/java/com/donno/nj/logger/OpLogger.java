package com.donno.nj.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpLogger {
    private static final Logger logger = LoggerFactory.getLogger(OpLogger.class);
    public static void log(final String info) {
        logger.info(info);
    }
}
