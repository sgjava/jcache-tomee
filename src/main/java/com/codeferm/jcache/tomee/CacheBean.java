/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on September 1, 2015
 * sgoldsmith@codeferm.com
 */
package com.codeferm.jcache.tomee;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
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
 * Singleton bean to manage caches.
 *
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
@Singleton
@Startup
@Lock(READ)
@SuppressWarnings("checkstyle:designforextension") // CDI beans not allowed to have final methods
public class CacheBean {

    /**
     * Logger.
     */
    //CHECKSTYLE:OFF ConstantName - Logger OK to be static final and lower case
    private static final Logger log = Logger.
            getLogger(CacheBean.class.getName());
    //CHECKSTYLE:ON ConstantName
    /**
     * Application properties.
     */
    private static final String APP_PROPS_FILE = System.getProperty("user.home")
            + "/config/app.properties";
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
     * Load Properties from file.
     *
     * @param fileName Name of property file.
     * @return Populated properties.
     */
    public Properties loadProperties(final String fileName) {
        log.info(String.format("Loading properties from %s", fileName));
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(fileName)) {
            properties.load(is);
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
        return properties;
    }

    /**
     * Configure cache manager and create caches.
     */
    //CHECKSTYLE:OFF DesignForExtension - CDI beans cannot have final methods
    @PostConstruct
    //CHECKSTYLE:ON DesignForExtension
    public void init() {
        log.info("PostConstruct");
        // App properties
        final Properties appProperties = loadProperties(APP_PROPS_FILE);
        final Properties sysProperties = loadProperties(appProperties.
                getProperty("app.system.props.file"));
        // See if we have system properties
        if (!sysProperties.isEmpty()) {
            // Set all system properties
            sysProperties.stringPropertyNames().stream().
                    map((key) -> {
                        System.setProperty(key, sysProperties.getProperty(key));
                        return key;
                    }).
                    forEach((key) -> {
                        log.info(String.format("Setting system property %s=%s",
                                key, sysProperties.getProperty(key)));
                    });
        }
        // CachingProvider.getCacheManager properties
        final Properties jcacheProperties = loadProperties(appProperties.
                getProperty("app.jcache.props.file"));
        log.info("Getting caching provider");
        cachingProvider = Caching.getCachingProvider();
        // Get cache manager
        log.info("Getting cache manager");
        // Should we use default getCacheManager?
        if (appProperties.getProperty("app.use.default.getcachemanager").equals(
                "true")) {
            log.info("Using no args getCacheManager()");
            cacheManager = cachingProvider.getCacheManager();
        } else {
            cacheManager = cachingProvider.getCacheManager(new File(
                    appProperties.getProperty("app.jcache.config.file")).toURI(),
                    null, jcacheProperties);
        }
        log.info("Creating caches");
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
