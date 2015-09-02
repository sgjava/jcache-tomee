/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on September 2, 2015
 * sgoldsmith@codeferm.com
 */
package com.codeferm.jcache.tomee;

import java.lang.annotation.Annotation;
import javax.cache.annotation.CacheKeyGenerator;
import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.GeneratedCacheKey;

/**
 * Generates a {@link GeneratedCacheKey} based on a
 * {@link CacheKeyInvocationContext}.
 * <p>
 * Implementations must be thread-safe.
 *
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
public class StringKeyGenerator implements CacheKeyGenerator {

    /**
     * Called for each intercepted method invocation to generate a suitable
     * cache key (as a {@link GeneratedCacheKey}) from the
     * {@link CacheKeyInvocationContext} data.
     *
     * @param cacheKeyInvocationContext Information about the intercepted method
     * invocation
     * @return A non-null cache key for the invocation.
     */
    @Override
    public final GeneratedCacheKey generateCacheKey(
            final CacheKeyInvocationContext<? extends Annotation> cacheKeyInvocationContext) {
        // Return first parameter which must be a String
        return new StringGeneratedCacheKey((String) cacheKeyInvocationContext.
                getAllParameters()[0].getValue());
    }
}
