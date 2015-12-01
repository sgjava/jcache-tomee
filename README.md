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
* Uses Hazelcast, Ehcache or Apache JCS provider
* GeneratedCacheKey using String
* CacheResolverFactory in order to expose CacheManager used by JCache annotations
* Uses javax.cache.* only! No provider specific imports used
* Change JCache provider in pom.xml, configuration file in CacheBean and property files
