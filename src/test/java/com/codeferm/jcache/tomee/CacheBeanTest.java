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
     * Our key/value bean with JCache annotations.
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
     * Test JSR-107 @CacheResult.
     */
    @Test
    public final void cacheResultShort() {
        log.info("cacheResultShort");
        assertNotNull(keyValueBean);
        log.info(String.format("Cache names: %s", cacheBean.getCacheManager().
                getCacheNames()));
        // Let's clear the cache
        keyValueBean.invalidateCache();
        // Add some stuff
        keyValueBean.shortResult("key1", "value1");
        keyValueBean.shortResult("key2", "value2");
        keyValueBean.shortResult("key3", "value3");
        // See if existing key added to cache (check log)
        keyValueBean.shortResult("key1", "value1");
        try (Cache<StringGeneratedCacheKey, String> testCache
                = cacheBean.getCacheManager().getCache("shortCache")) {
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
            assertEquals(3, map.size());
            log.info(String.format("Map of cache: %s", map));
            // Let's try looking up by key
            final String value = testCache.get(new StringGeneratedCacheKey(
                    "key1"));
            assertNotNull(value);
            assertEquals(value, "value1");
        }
    }

    /**
     * Test JSR-107 @CacheResult.
     */
    @Test
    public final void cacheResultMed() {
        log.info("cacheResultMed");
        assertNotNull(keyValueBean);
        log.info(String.format("Cache names: %s", cacheBean.getCacheManager().
                getCacheNames()));
        // Let's clear the cache
        keyValueBean.invalidateCache();
        // Add some stuff
        keyValueBean.medResult("key4", "value4");
        keyValueBean.medResult("key5", "value5");
        keyValueBean.medResult("key6", "value6");
        // See if existing key added to cache (check log)
        keyValueBean.medResult("key4", "value4");
        try (Cache<StringGeneratedCacheKey, String> testCache
                = cacheBean.getCacheManager().getCache("medCache")) {
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
            assertEquals(3, map.size());
            log.info(String.format("Map of cache: %s", map));
            // Let's try looking up by key
            final String value = testCache.get(new StringGeneratedCacheKey(
                    "key4"));
            assertNotNull(value);
            assertEquals(value, "value4");
        }
    }

    /**
     * Test JSR-107 @CacheResult.
     */
    @Test
    public final void cacheResultLong() {
        log.info("cacheResultLong");
        assertNotNull(keyValueBean);
        log.info(String.format("Cache names: %s", cacheBean.getCacheManager().
                getCacheNames()));
        // Let's clear the cache
        keyValueBean.invalidateCache();
        // Add some stuff
        keyValueBean.longResult("key7", "value7");
        keyValueBean.longResult("key8", "value8");
        keyValueBean.longResult("key9", "value9");
        // See if existing key added to cache (check log)
        keyValueBean.longResult("key9", "value9");
        try (Cache<StringGeneratedCacheKey, String> testCache
                = cacheBean.getCacheManager().getCache("longCache")) {
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
            assertEquals(3, map.size());
            log.info(String.format("Map of cache: %s", map));
            // Let's try looking up by key
            final String value = testCache.get(new StringGeneratedCacheKey(
                    "key7"));
            assertNotNull(value);
            assertEquals(value, "value7");
        }
    }

    /**
     * Test JSR-107 @CachePut.
     */
    @Test
    public final void cachePut() {
        log.info("cachePut");
        assertNotNull(keyValueBean);
        // Let's clear the cache
        keyValueBean.invalidateCache();
        try (Cache<StringGeneratedCacheKey, String> testCache
                = cacheBean.getCacheManager().getCache("shortCache")) {
            // Let's test @CachePut
            keyValueBean.add("key4", "value4");
            assertNotNull(testCache.get(new StringGeneratedCacheKey("key4")));
        }
    }

    /**
     * Test JSR-107 @CachePut.
     */
    @Test
    public final void cacheRemove() {
        log.info("cacheRemove");
        assertNotNull(keyValueBean);
        // Let's clear the cache
        keyValueBean.invalidateCache();
        try (Cache<StringGeneratedCacheKey, String> testCache
                = cacheBean.getCacheManager().getCache("shortCache")) {
            // Let's test @CachePut
            keyValueBean.add("key5", "value5");
            assertNotNull(testCache.get(new StringGeneratedCacheKey("key5")));
            // Let's test @CacheRemove
            keyValueBean.remove("key5");
            assertNull(testCache.get(new StringGeneratedCacheKey("key5")));
        }
    }

    /**
     * Test JSR-107 @CacheRemoveAll.
     */
    @Test
    public final void cacheRemoveAll() {
        log.info("cacheRemoveAll");
        assertNotNull(keyValueBean);
        // Let's clear the cache
        keyValueBean.invalidateCache();
        try (Cache<StringGeneratedCacheKey, String> testCache
                = cacheBean.getCacheManager().getCache("shortCache")) {
            // Let's test @CachePut
            keyValueBean.add("key6", "value6");
            assertNotNull(testCache.get(new StringGeneratedCacheKey("key6")));
            // Let's clear the cache
            keyValueBean.invalidateCache();
            assertFalse(testCache.iterator().hasNext());
        }
    }
}
