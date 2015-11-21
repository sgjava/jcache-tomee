/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on September 1, 2015
 * sgoldsmith@codeferm.com
 */
package com.codeferm.jcache.tomee;

import java.io.File;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import javax.ejb.Lock;
import static javax.ejb.LockType.READ;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;

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
    @Produces
    public CacheManager getCacheManager() {
        //CHECKSTYLE:ON DesignForExtension
        return cacheManager;
    }

    /**
     * Using EHCache provider configured via ehcache.xml in classpath. Set
     * system properties for ehcache.xml substitution.
     */
    //CHECKSTYLE:OFF DesignForExtension - CDI beans cannot have final methods
    @PostConstruct
    //CHECKSTYLE:ON DesignForExtension
    public void init() {
        log.info("PostConstruct");
        cachingProvider = Caching.getCachingProvider();
        // Change to src/config/ehcache.xml for Ehcache provider and edit POM
        // to comment out JCS and add Ehcache dependency
        cacheManager = cachingProvider.getCacheManager(new File(
                "src/config/jcache.ccf").toURI(), CacheBean.class.
                getClassLoader());
        cacheManager.createCache("shortCache", new MutableConfiguration().
                setStoreByValue(false).setStatisticsEnabled(true).
                setManagementEnabled(true));
        cacheManager.createCache("medCache", new MutableConfiguration().
                setStoreByValue(false).setStatisticsEnabled(true).
                setManagementEnabled(true));
        cacheManager.createCache("longCache", new MutableConfiguration().
                setStoreByValue(false).setStatisticsEnabled(true).
                setManagementEnabled(true));
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
