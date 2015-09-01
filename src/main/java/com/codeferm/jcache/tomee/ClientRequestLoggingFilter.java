/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on September 1, 2015
 * sgoldsmith@codeferm.com
 */
package com.codeferm.jcache.tomee;

import java.io.IOException;
import java.util.logging.Logger;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

/**
 * Client request filter to log request.
 *
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
public class ClientRequestLoggingFilter implements ClientRequestFilter {

    /**
     * Logger.
     */
    //CHECKSTYLE:OFF ConstantName - Logger OK to be static final and lower case
    private static final Logger log = Logger.getLogger(
            ClientRequestLoggingFilter.class.getName());

    //CHECKSTYLE:ON ConstantName
    /**
     * Display request information.
     *
     * @param context Request context.
     * @throws IOException Possible exception.
     */
    @Override
    public final void filter(final ClientRequestContext context) throws
            IOException {
        // Log headers
        context.getHeaders().entrySet().stream().forEach((header) -> {
            header.getValue().stream().forEach((value) -> {
                log.info(String.format("%s:%s ", header.getKey(), value));
            });
        });
        log.info(String.format("URI: %s", context.getUri()));
        log.info(String.format("Method: %s", context.getMethod()));
        log.info(String.format("Headers: %s", context.getStringHeaders()));
        log.info(String.format("Entity: %s", context.getEntity()));
    }
}
