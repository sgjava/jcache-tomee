/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on September 1, 2015
 * sgoldsmith@codeferm.com
 */
package com.codeferm.jcache.tomee;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import javax.ejb.Lock;
import static javax.ejb.LockType.READ;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * Singleton bean to manage cache.
 *
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
@Singleton
@Startup
@Lock(READ)
public class CacheBean {

    /**
     * Logger.
     */
    //CHECKSTYLE:OFF ConstantName - Logger OK to be static final and lower case
    private static final Logger log = Logger.
            getLogger(CacheBean.class.getName());
    //CHECKSTYLE:ON ConstantName
    /**
     * Caching provider.
     */
    private CachingProvider cachingProvider;
    /**
     * Cache manager.
     */
    private CacheManager cacheManager;

    /**
     * Get cache manager.
     *
     * @return Cache manager.
     */
    //CHECKSTYLE:OFF DesignForExtension - CDI beans cannot have final methods
    public CacheManager getCacheManager() {
        //CHECKSTYLE:ON DesignForExtension
        return cacheManager;
    }

    /**
     * Using EHCache provider configured via ehcache.xml in classpath.
     */
    //CHECKSTYLE:OFF DesignForExtension - CDI beans cannot have final methods
    @PostConstruct
    //CHECKSTYLE:ON DesignForExtension
    public void init() {
        log.info("PostConstruct");
        cachingProvider = Caching.getCachingProvider();
        cacheManager = cachingProvider.getCacheManager();
    }

    /**
     * Destroy cache.
     */
    //CHECKSTYLE:OFF DesignForExtension - CDI beans cannot have final methods
    @PreDestroy
    //CHECKSTYLE:ON DesignForExtension
    public void destroy() {
        log.info("PreDestroy");
        cacheManager.close();
        cachingProvider.close();
    }
}
