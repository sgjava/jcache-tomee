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
import javax.cache.Cache;
import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

/**
 * Test CDI beans injected into test.
 *
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
public class CacheBeanTest {

    /**
     * Logger.
     */
    private static final Logger log = Logger.getLogger(CacheBeanTest.class.
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
     * EJB container.
     */
    private static EJBContainer container;

    /**
     * Set up.
     *
     * @throws NamingException possible exception.
     */
    @Before
    public final void setUp() throws NamingException {
        log.info("setUp()");
        // Set system properties for ehcache.xml substitution
        System.setProperty("ehcache.timeToIdleSeconds", "600");
        System.setProperty("ehcache.timeToLiveSeconds", "600");
        System.setProperty("ehcache.providerHost", "localhost");
        System.setProperty("ehcache.providerPort", "8000");
        System.setProperty("ehcache.listenerHost", "localhost");
        System.setProperty("ehcache.listenerPort", "8000");
        container = EJBContainer.createEJBContainer();
        container.getContext().bind("inject", this);
    }

    /**
     * Tear down.
     *
     * @throws NamingException Possible exception.
     */
    @After
    public final void tearDown() throws NamingException {
        container.getContext().unbind("inject");
        container.close();
    }

    /**
     * Test JSR-107 cache using EHCache provider.
     */
    @Test
    public final void testCache() {
        log.info("testCache");
        assertNotNull(cacheBean);
        log.info(String.format("Cache names: %s", cacheBean.getCacheManager().
                getCacheNames()));
        assertNotNull(keyValueBean);
        keyValueBean.slowMethod("key1", "value1");
        keyValueBean.slowMethod("key2", "value2");
        keyValueBean.slowMethod("key3", "value3");
        // See if existing key added to cache (check log)
        keyValueBean.slowMethod("key1", "value1");
        try (Cache<StringGeneratedCacheKey, String> testCache
                = cacheBean.getCacheManager().getCache("testCache")) {
            // Convert cache to Map
            final Map<String, String> map = new HashMap<>();
            Iterator<Cache.Entry<StringGeneratedCacheKey, String>> allCacheEntries
                    = testCache.iterator();
            while (allCacheEntries.hasNext()) {
                Cache.Entry<StringGeneratedCacheKey, String> currentEntry
                        = allCacheEntries.next();
                map.put(currentEntry.getKey().toString(), currentEntry.
                        getValue());
            }
            assertNotNull(map);
            // We added 3 unique keys to cache
            assertEquals(map.size(), 3);
            log.info(String.format("Map of cache: %s", map));
            // Let's try looking up by key
            final StringGeneratedCacheKey key = new StringGeneratedCacheKey(
                    "key1");
            final String value = testCache.get(key);
            assertNotNull(value);
            assertEquals(value, "value1");
            // Let's clear the cache
            keyValueBean.invalidateCache();
            allCacheEntries = testCache.iterator();
            assertFalse(allCacheEntries.hasNext());
            // Let's test @CachePut
            keyValueBean.add("key4", "value4");
            assertNotNull(testCache.get(new StringGeneratedCacheKey("key4")));
            // Let's test @CacheRemove
            keyValueBean.remove("key4");
            assertNull(testCache.get(new StringGeneratedCacheKey("key4")));
        }
    }
}
