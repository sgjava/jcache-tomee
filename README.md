![Title](images/title.png)
* Unit tests provide examples
* JAX-RS using CDI bean and JCache annotations
* JAX-RS bean validation
* Stand alone EJB container using CDI bean and JCache annotations
* Use tomee:run and RemoteUserServiceJCacheTest for remote client example
* Uses Hazelcast, Ehcache, BlazingCache or Apache JCS provider
* GeneratedCacheKey using String
* CacheResolverFactory in order to expose CacheManager used by JCache annotations
* Uses javax.cache.* only! No provider specific imports used

This is a simple application to test various JCache implementations. Some
implementations require properties in addition to the configuration file. In
order to test a particular implementation:

* Edit pom.xml and uncomment dependencies needed for JCache provider (Make sure
to comment out the current dependencies)
* Edit config/app.properties and specify configuration file with
app.jcache.config.file
* Change System properties in system.properties and JCache properties in
jcache.properties as needed
* Do a Maven clean install