/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on September 1, 2015
 * sgoldsmith@codeferm.com
 */
package com.codeferm.jcache.tomee;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.cache.Cache;
import javax.ejb.EJB;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.validation.Valid;
import javax.validation.executable.ValidateOnExecution;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Service to work with UserDto an test caching.
 *
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
@Path("/user/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SuppressWarnings("checkstyle:designforextension") // Proxy beans allowed to have final methods
public class UserService {

    /**
     * Logger.
     */
    @SuppressWarnings("checkstyle:constantname") // Logger OK to be static final and lower case
    private static final Logger log = Logger.getLogger(UserService.class.
            getName());
    /**
     * Injected cache bean.
     */
    @EJB
    private CacheBean cacheBean;
    /**
     * Our key/value bean.
     */
    @EJB
    private KeyValueBean keyValueBean;
    /**
     * Injected JMS connection factory.
     */
    @Resource
    private ConnectionFactory connectionFactory;
    /**
     * Injected MDB.
     *
     * For TomEE use openejb.deploymentId.format={ejbJarId}/{ejbName}
     */
    @Resource(name = "jcache-tomee/LogBean")
    private Queue logBean;
    /**
     * Direct access to cache.
     */
    private Cache<StringGeneratedCacheKey, String> testCache;

    /**
     * Get instance of cache in order to get contents.
     */
    @PostConstruct
    public void init() {
        log.info("PostConstruct");
        testCache = cacheBean.getCacheManager().getCache("shortCache");
    }

    /**
     * D0 any clean up.
     */
    @PreDestroy
    public void done() {
        log.info("PreDestroy");
    }

    /**
     * Get Map representation of cache.
     *
     * @return Map with cache contents.
     */
    @Path("/getmap")
    @GET
    public Response getMap() {
        final Map<StringGeneratedCacheKey, String> map = new HashMap<>();
        Iterator<Cache.Entry<StringGeneratedCacheKey, String>> allCacheEntries
                = testCache.iterator();
        while (allCacheEntries.hasNext()) {
            Cache.Entry<StringGeneratedCacheKey, String> currentEntry
                    = allCacheEntries.next();
            map.put(currentEntry.getKey(), currentEntry.getValue());
        }
        return Response.ok(map).build();
    }

    /**
     * Cache user info. CDI bean keyValueBean is used for caching.
     *
     * @param userDto User DTO.
     * @return Populated User DTO.
     */
    @Path("/userinfo")
    @POST
    @ValidateOnExecution
    @Valid
    public Response userInfo(@Valid final UserDto userDto) throws JMSException {
        log.info(String.format("userDto: %s", userDto.toString()));
        keyValueBean.shortResult(userDto.getUserName(), userDto.getFullName());
        sendLogMessage(userDto.toString());
        // Return UserDto
        return Response.ok(userDto).build();
    }

    /**
     * Send text message to queue.
     *
     * @param text Text to send to MDB.
     * @throws JMSException Possible exception.
     */
    public void sendLogMessage(final String text) throws JMSException {
        log.info(String.format("Sending: %s", text));
        try (Connection connection = connectionFactory.createConnection()) {
            connection.start();
            try (Session session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE)) {
                final MessageProducer queue = session.createProducer(logBean);
                queue.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                final TextMessage message = session.createTextMessage(text);
                queue.send(message);
            }
        }
    }
}
