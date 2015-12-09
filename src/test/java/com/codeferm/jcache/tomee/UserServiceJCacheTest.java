/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on September 1, 2015
 * sgoldsmith@codeferm.com
 */
package com.codeferm.jcache.tomee;

import java.io.File;
import java.util.Map;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import org.apache.tomee.embedded.Configuration;
import org.apache.tomee.embedded.Container;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test service using CDI beans to cache.
 *
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
public class UserServiceJCacheTest {

    /**
     * Logger.
     */
    //CHECKSTYLE:OFF ConstantName - Logger OK to be static final and lower case
    private static final Logger log = Logger.
            getLogger(UserServiceJCacheTest.class.getName());
    //CHECKSTYLE:ON ConstantName
    /**
     * TomEE container.
     */
    private static Container container;
    /**
     * TomEE container configuration.
     */
    private static Configuration configuration;

    /**
     * Start EJB container.
     */
    @BeforeClass
    public static void start() throws Exception {
        log.info("start()");
        // tomee-embedded configuration
        configuration = new Configuration().randomHttpPort();
        container = new Container();
        container.setup(configuration);
        container.start();
        container.deployClasspathAsWebApp("/jcache-tomee", new File(
                "src/main/webapp"));
        log.info(String.format("TomEE embedded started on %s:%s",
                configuration.getHost(), configuration.getHttpPort()));
    }

    /**
     * Stop EJB container.
     */
    @AfterClass
    public static void stop() throws Exception {
        log.info("stop()");
        container.stop();
    }

    /**
     * Test event service.
     */
    @Test
    public final void testCache() {
        log.info("testCache()");
        final String postUrl = String.format(
                "http://%s:%s/jcache-tomee/user/v1/userinfo/",
                configuration.getHost(), configuration.getHttpPort());
        // Set up web client
        final Client client = ClientBuilder.newClient();
        // JSON provider
        client.register(JacksonJaxbJsonProvider.class);
        // Client logging filter
        client.register(ClientRequestLoggingFilter.class);
        // Get back test user's info
        final UserDto userDto = new UserDto(1, "test", "Test User");
        // First one goes into cache
        UserDto response = client.target(postUrl).request().post(Entity.entity(
                userDto, MediaType.APPLICATION_JSON), UserDto.class);
        assertNotNull(response);
        log.info(String.format("Response: %s", response));
        // Second DTO will have data cached (no Adding key: logged from KeyValueBean)
        response = client.target(postUrl).request().post(Entity.entity(
                userDto, MediaType.APPLICATION_JSON), UserDto.class);
        assertNotNull(response);
        log.info(String.format("Response: %s", response));
        // A few more times for good measure
        response = client.target(postUrl).request().post(Entity.entity(
                userDto, MediaType.APPLICATION_JSON), UserDto.class);
        assertNotNull(response);
        response = client.target(postUrl).request().post(Entity.entity(
                userDto, MediaType.APPLICATION_JSON), UserDto.class);
        assertNotNull(response);
        response = client.target(postUrl).request().post(Entity.entity(
                userDto, MediaType.APPLICATION_JSON), UserDto.class);
        assertNotNull(response);
        // Let's see what's in cache
        final String getUrl = String.format(
                "http://%s:%s/jcache-tomee/user/v1/getmap/",
                configuration.getHost(), configuration.getHttpPort());
        final Map<String, String> map = client.target(getUrl).request().get(
                Map.class);
        assertNotNull(map);
        log.info(String.format("Map of cache: %s", map));
    }
}
