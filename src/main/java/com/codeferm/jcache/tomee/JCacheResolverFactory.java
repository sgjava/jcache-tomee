/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on November 19, 2015
 * sgoldsmith@codeferm.com
 */
package com.codeferm.jcache.tomee;

import java.lang.annotation.Annotation;
import java.util.logging.Logger;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.annotation.CacheMethodDetails;
import javax.cache.annotation.CacheResolver;
import javax.cache.annotation.CacheResolverFactory;
import javax.cache.annotation.CacheResult;
import javax.cache.configuration.MutableConfiguration;
import javax.inject.Inject;

/**
 *
 * @author sgoldsmith
 */
public class JCacheResolverFactory implements CacheResolverFactory {

    /**
     * Logger.
     */
    @SuppressWarnings("checkstyle:constantname") // Logger OK to be static final and lower case
    private static final Logger log = Logger.
            getLogger(CacheBean.class.getName());
    //CHECKSTYLE:ON ConstantName
    /**
     * Cache manager.
     */
    @Inject
    private CacheManager cacheManager;

    /**
     * Default constructor for proxy.
     */
    public JCacheResolverFactory() {
    }

    /**
     * Return cache resolver.
     *
     * @param cacheMethodDetails Details of cache method.
     * @return Cache resolver.
     * @see
     * javax.cache.annotation.CacheResolverFactory#getCacheResolver(javax.cache.annotation.CacheMethodDetails)
     */
    @Override
    public final CacheResolver getCacheResolver(
            final CacheMethodDetails<? extends Annotation> cacheMethodDetails) {
        final String cacheName = cacheMethodDetails.getCacheName();
        Cache<?, ?> cache = this.cacheManager.getCache(cacheName);
        // Create Object cache if not found
        if (cache == null) {
            log.warning(String.format(
                    "No Cache named '%s' was found in the CacheManager, a default cache will be created",
                    cacheName));
            cacheManager.createCache(cacheName,
                    new MutableConfiguration<Object, Object>());
            cache = cacheManager.getCache(cacheName);
        }
        return new DefaultCacheResolver(cache);
    }

    /**
     * Return exception cache.
     *
     * @param cacheMethodDetails Details of cache method.
     * @return Cache resolver.
     */
    @Override
    public final CacheResolver getExceptionCacheResolver(
            final CacheMethodDetails<CacheResult> cacheMethodDetails) {
        final CacheResult cacheResultAnnotation = cacheMethodDetails.
                getCacheAnnotation();
        final String exceptionCacheName = cacheResultAnnotation.
                exceptionCacheName();
        if (exceptionCacheName == null || exceptionCacheName.trim().length()
                == 0) {
            throw new IllegalArgumentException(
                    "Can only be called when CacheResult.exceptionCacheName() is specified");
        }
        Cache<?, ?> cache = cacheManager.getCache(exceptionCacheName);
        // Create Object cache if not found
        if (cache == null) {
            log.warning(String.format(
                    "No Cache named '%s' was found in the CacheManager, a default cache will be created",
                    exceptionCacheName));
            cacheManager.createCache(exceptionCacheName,
                    new MutableConfiguration<Object, Object>());
            cache = cacheManager.getCache(exceptionCacheName);
        }
        return new DefaultCacheResolver(cache);
    }
}
