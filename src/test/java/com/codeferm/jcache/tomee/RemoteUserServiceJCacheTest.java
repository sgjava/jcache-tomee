/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on July 2, 2016
 * sgoldsmith@codeferm.com
 */
package com.codeferm.jcache.tomee;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test service using CDI beans to cache.
 *
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
@Ignore // Uncomment after Maven clean install goals (to build war), then run tomee:run before running this test
public class RemoteUserServiceJCacheTest {

    /**
     * Logger.
     */
    private static final Logger log = Logger.
            getLogger(RemoteUserServiceJCacheTest.class.getName());

    /**
     * Get JAX-RS client configured with filters and interceptors.
     * JacksonJaxbJsonProvider
     *
     * @param useJacksonProv Register JacksonJsonProvider if true.
     * @return JAX-RS client.
     */
    public final Client getRestClient(final boolean useJacksonProv) {
        // Set up web client with various filters/interceptors
        /*
        final Client client = ClientBuilder.newClient().register(
                ClientRequestLoggingFilter.class);
         */
        final Client client = ClientBuilder.newClient();
        // Register Jackson JSON provider
        if (useJacksonProv) {
            client.register(JacksonJsonProvider.class);
        }
        // Set timeout properties without importing CXF packages
        client.property("http.connection.timeout", 10000L);
        client.property("http.receive.timeout", 10000L);
        return client;
    }

    /**
     * Test event service.
     */
    @Test
    public final void remoteTest() throws InterruptedException {
        log.info("remoteTest()");
        // Set up web client with various filters/interceptors
        final Client client = getRestClient(true);
        // Set value for cache key to null which should fail validation
        final UserDto userDto = new UserDto(7242350598L, 1, "test", "Test User");
        for (int i = 0; i < 10; i++) {
            final UserDto response = client.target(
                    "http://localhost:8080/jcache-tomee-1.0.0-SNAPSHOT/user/v1/userinfo").
                    request().post(Entity.
                            entity(userDto, MediaType.APPLICATION_JSON),
                            UserDto.class);
            TimeUnit.MILLISECONDS.sleep(50);
        }
    }
}
