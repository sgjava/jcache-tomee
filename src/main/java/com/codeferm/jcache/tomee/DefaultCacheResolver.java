/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on November 19, 2015
 * sgoldsmith@codeferm.com
 */
package com.codeferm.jcache.tomee;

import java.lang.annotation.Annotation;
import javax.cache.Cache;
import javax.cache.annotation.CacheInvocationContext;
import javax.cache.annotation.CacheResolver;

/**
 * Default {@link javax.cache.annotation.CacheResolver} implementation for
 * standalone environments, where no Cache/CacheManagers are injected via CDI.
 *
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
public class DefaultCacheResolver implements CacheResolver {

    /**
     * Cache.
     */
    private final Cache<?, ?> cache;

    /**
     * Create a new default cache resolver that always returns the specified
     * cache.
     *
     * @param cache The cache to return for all calls to
     * {@link #resolveCache(CacheInvocationContext)}
     */
    public DefaultCacheResolver(final Cache<?, ?> cache) {
        if (cache == null) {
            throw new IllegalArgumentException("The Cache can not be null");
        }

        this.cache = cache;
    }

    /**
     * Resolve the Cache to use for the CacheInvocationContext.
     * 
     * @param <K> Cache key type.
     * @param <V> Cache value type.
     * @param cacheInvocationContext The context data for the intercepted method
     * invocation.
     * @return Typed cache.
     * @see
     * javax.cache.annotation.CacheResolver#resolveCache(javax.cache.annotation.CacheInvocationContext)
     */
    @Override
    @SuppressWarnings("unchecked")
    public final <K, V> Cache<K, V> resolveCache(
            final CacheInvocationContext<? extends Annotation> cacheInvocationContext) {
        return (Cache<K, V>) this.cache;
    }
}
