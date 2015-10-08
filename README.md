```
       _|    _|_|_|                      _|                      
       _|  _|          _|_|_|    _|_|_|  _|_|_|      _|_|        
       _|  _|        _|    _|  _|        _|    _|  _|_|_|_|      
 _|    _|  _|        _|    _|  _|        _|    _|  _|            
   _|_|      _|_|_|    _|_|_|    _|_|_|  _|    _|    _|_|_|      

                          Using

   _|_|_|_|_|                          _|_|_|_|  _|_|_|_|  
       _|      _|_|    _|_|_|  _|_|    _|        _|        
       _|    _|    _|  _|    _|    _|  _|_|_|    _|_|_|    
       _|    _|    _|  _|    _|    _|  _|        _|        
       _|      _|_|    _|    _|    _|  _|_|_|_|  _|_|_|_|  
```

### JCache using TomEE 7.0.0
* Unit tests provide examples
* JAX-RS using CDI bean and JCache annotations
* Stand alone EJB container using CDI bean and JCache annotations
* Uses Ehcache or Apache JCS provider
* GeneratedCacheKey using String
* Uses javax.cache.* only! No net.sf.ehcache.* imports used

One annoying thing you'll notice is that cachingProvider.getCacheManager() with
the provider configuration in the classpath works as expected (at least as I
expected). This uses the configuration file to create named caches that can be
accessed with getCache("cacheName").

Using cachingProvider.getCacheManager(new File("src/config/ehcache.xml").toURI(), null, null)
does not work the same way as cachingProvider.getCacheManager() with regards
to named caches. getCache("cacheName") does not work using this signature of
getCacheManager. Since this probably isn't addressed in the JCache specification
it must be implementation specific.