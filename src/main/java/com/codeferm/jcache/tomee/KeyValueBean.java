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
import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.ejb.Stateless;

/**
 * Stateless bean using JCache annotations.
 *
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
@Stateless
@CacheDefaults(cacheName = "testCache")
public class KeyValueBean {

    /**
     * Logger.
     */
    //CHECKSTYLE:OFF ConstantName - Logger OK to be static final and lower case
    private static final Logger log = Logger.getLogger(KeyValueBean.class.
            getName());
    //CHECKSTYLE:ON ConstantName

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
    public String slowMethod(@CacheKey final String key, final String value) {
        log.info(String.format("Adding key: %s, value: %s", key, value));
        return value;
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
