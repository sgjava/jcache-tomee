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
     * System properties file.
     */
    private static final String SYSTEM_PROPS_FILE
            = "src/config/system.properties";
    /**
     * JCache cache manager properties.
     */
    private static final String JCACHE_PROPS_FILE
            = "src/config/jcache.properties";
    /**
     * JCache configuration file. Use the following: src/config/jcs.ccf for
     * Apache JCS, src/config/ehcache.xml for Ehcache,
     * src/config/hazelcast-client.xml for Hazelcast. Be sure to edit POM
     * dependency for JCache provider.
     */
    private static final String JCACHE_CONFIG_FILE
            = "src/config/hazelcast-client.xml";
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
     * @param fileName
     * @return
     */
    public Properties loadProperties(final String fileName) {
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(fileName)) {
            properties.load(is);
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
        return properties;
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
        final Properties sysProperties
                = loadProperties(SYSTEM_PROPS_FILE);
        // See if we have system properties
        if (!sysProperties.isEmpty()) {
            // Set all system properties
            for (String key : sysProperties.stringPropertyNames()) {
                System.setProperty(key, sysProperties.getProperty(key));
                log.info(String.format("Setting system property %s=%s", key,
                        sysProperties.getProperty(key)));
            }
        }
        // CachingProvider.getCacheManager properties
        final Properties jcacheProperties = loadProperties(JCACHE_PROPS_FILE);
        cachingProvider = Caching.getCachingProvider();
        // Get cache manager
        cacheManager = cachingProvider.getCacheManager(new File(
                JCACHE_CONFIG_FILE).toURI(), CacheBean.class.getClassLoader(),
                jcacheProperties);
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
