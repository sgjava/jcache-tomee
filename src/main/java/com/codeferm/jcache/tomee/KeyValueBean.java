/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on September 1, 2015
 * sgoldsmith@codeferm.com
 */
package com.codeferm.jcache.tomee;

import java.util.logging.Logger;
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import javax.ejb.Singleton;

/**
 * Singleton bean using JCache annotations. Value is stored in cache by key.
 *
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
@Singleton
@CacheDefaults(cacheResolverFactory = JCacheResolverFactory.class,
        cacheKeyGenerator = StringKeyGenerator.class, cacheName = "shortCache")
public class KeyValueBean {

    /**
     * Logger.
     */
    @SuppressWarnings("checkstyle:constantname") // Logger OK to be static final and lower case
    private static final Logger log = Logger.getLogger(KeyValueBean.class.
            getName());

    /**
     * A slow method that needs caching. Value is cached. If the key exists it
     * is retrieved from cache.
     *
     * @param key Cache key.
     * @param value Cache value.
     * @return Value.
     */
    //CHECKSTYLE:OFF DesignForExtension - CDI beans cannot have final methods
    @CacheResult
    //CHECKSTYLE:ON DesignForExtension
    public String shortResult(@CacheKey final String key, final String value) {
        log.info(String.format("Adding key: %s, value: %s", key, value));
        // Do something slow
        return value;
    }

    /**
     * A slow method that needs caching. Value is cached. If the key exists it
     * is retrieved from cache.
     *
     * @param key Cache key.
     * @param value Cache value.
     * @return Value.
     */
    //CHECKSTYLE:OFF DesignForExtension - CDI beans cannot have final methods
    @CacheResult(cacheName = "medCache")
    //CHECKSTYLE:ON DesignForExtension
    public String medResult(@CacheKey final String key, final String value) {
        log.info(String.format("Adding key: %s, value: %s", key, value));
        // Do something slow
        return value;
    }

    /**
     * A slow method that needs caching. Value is cached. If the key exists it
     * is retrieved from cache.
     *
     * @param key Cache key.
     * @param value Cache value.
     * @return Value.
     */
    //CHECKSTYLE:OFF DesignForExtension - CDI beans cannot have final methods
    @CacheResult(cacheName = "longCache")
    //CHECKSTYLE:ON DesignForExtension
    public String longResult(@CacheKey final String key, final String value) {
        log.info(String.format("Adding key: %s, value: %s", key, value));
        // Do something slow
        return value;
    }

    /**
     * Put value in cache.
     *
     * @param key Key.
     * @param value Value.
     */
    //CHECKSTYLE:OFF DesignForExtension - CDI beans cannot have final methods
    @CachePut
    //CHECKSTYLE:ON DesignForExtension
    public void add(@CacheKey final String key, @CacheValue final String value) {
        log.info(String.format("Adding key: %s, value: %s", key, value));
    }

    /**
     * Remove value in cache.
     *
     * @param key Key.
     */
    //CHECKSTYLE:OFF DesignForExtension - CDI beans cannot have final methods
    @CacheRemove
    //CHECKSTYLE:ON DesignForExtension
    public void remove(@CacheKey final String key) {
        log.info(String.format("Removing key: %s", key));
    }

    /**
     * Empty cache.
     */
    //CHECKSTYLE:OFF DesignForExtension - CDI beans cannot have final methods
    @CacheRemoveAll
    //CHECKSTYLE:ON DesignForExtension
    public void invalidateCache() {
        log.info("Cache invalidated");
    }
}
