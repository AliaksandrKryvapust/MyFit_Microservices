package itacad.aliaksandrkryvapust.usermicroservice.repository.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class CacheStorage<TYPE> {
    private final Cache<String, TYPE> cache;

    public CacheStorage(int expirationTime, TimeUnit timeUnit) {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expirationTime, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public TYPE get(String key) {
        return cache.getIfPresent(key);
    }

    public void add(String key, TYPE value){
        if (key!=null && value!=null){
            cache.put(key, value);
        }
    }
}
